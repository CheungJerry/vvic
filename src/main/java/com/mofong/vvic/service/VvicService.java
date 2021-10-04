package com.mofong.vvic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.mofong.util.GetRequest;
import com.mofong.util.JsonUtil;
import com.mofong.vvic.bean.PosOrder;
import com.mofong.vvic.bean.PosOrderDetail;
import com.mofong.vvic.dao.CookieDao;
import com.mofong.vvic.dao.CronDao;
import com.mofong.vvic.dao.PosOrderDao;

@Service("vvicService")
public class VvicService {

	private static Logger logger = LoggerFactory.getLogger(VvicService.class);

//	@Autowired
//	private GetRequest request;
//
//	@Autowired
//	private RestTemplate restTemplate;
	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private PosOrderDao posOrderDao;

	@Autowired
	private CookieDao cookieDao;

	private static String exportPosRecordUrl = "https://www.vvic.com/apic/trade/pos/record?currentPage=%s&pageSize=80&startDate=%s&endDate=%s&pos_resource_id=19&keywords=";

	private String cookie;
//	private String cookie = "chash=1994976581;cu=3E4089BD15704C9F7F6BED98BA9DEC93;_countlyIp=113.68.152.61;_uab_collina=161423511584128691602573;_ga=GA1.2.1550411645.1614235116;countlyIp=58.62.28.54;_ati=8966170530450;uf=2;hasFocusStall=false;acw_tc=7d48929516315270914873679eba1547bcf1fce7b9b2a340e91cf5e687;vvic_token=8963afb1-2db7-424c-8cc9-0dc102ba6726;ocity=\\\"\\\"; hasCityMarket=\\\"\\\"; sajssdk_2015_cross_new_user=1;Hm_lvt_fbb512d824c082a8ddae7951feb7e0e5=1631527097;Hm_lvt_fdffeb50b7ea8a86ab0c9576372b2b8c=1631527097;_gid=GA1.2.798506855.1631527098;algo4Cu=0;ISSUPPORTPANGGE=true;DEVICE_INFO=%7B%22device_id%22%3A%223E4089BD15704C9F7F6BED98BA9DEC93%22%2C%22device_channel%22%3A1%2C%22device_type%22%3A1%2C%22device_model%22%3A%22Windows%22%2C%22device_os%22%3A%22Windows%2010%22%2C%22device_lang%22%3A%22zh-CN%22%2C%22device_size%22%3A%221707*1067%22%2C%22device_net%22%3A%220%22%2C%22device_lon%22%3A113.27143134%2C%22device_lat%22%3A23.13533631%2C%22device_address%22%3A%22%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%22%2C%22browser_type%22%3A%22Chrome%22%2C%22browser_version%22%3A%2288.0.4324.182%22%7D;uid=683803;pn=9;mobile=13592896034;umc=1;ut=1;defaultShopId=52918;uno=0;userLoginAuto=0;generateToken=eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ2dmljLmNvbSIsInN1YiI6IjY4MzgwM3wyMDIxMDkxMzE4MDAxMDA3NXw5YjgzNzBkZWQ0YTE0Yzg5OWJmM2EyZGQxYTZkMDA2YiJ9.wZX3mjWDXA4L21sH5INzxn-3y_RIkL3rw6iLbzigpn7VoOTwJqEmYzo181gCWCFivfcuNz-SkPkgzAmeiEkUmw;posName=13592896034;shopId=52918;sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22683803%22%2C%22first_id%22%3A%2217bde97d35f9cb-02d5b838bf989de-73e356b-1821369-17bde97d360550%22%2C%22props%22%3A%7B%7D%2C%22%24device_id%22%3A%2217bde97d35f9cb-02d5b838bf989de-73e356b-1821369-17bde97d360550%22%7D;Hm_lpvt_fbb512d824c082a8ddae7951feb7e0e5=1631527211;Hm_lpvt_fdffeb50b7ea8a86ab0c9576372b2b8c=1631527211;algo4Uid=0";

	private static CookieDao cookieDaoInit;

	@PostConstruct
	public void init() {
		logger.info("从数据库获取cookie");
		cookieDaoInit = cookieDao;
		this.cookie = cookieDaoInit.getCookieById("vvic");
	}

	/**
	 * 定时获取数据
	 */
	public void vvicShedule() {
		logger.info(LocalDateTime.now().toString() + "执行了vvic定时任务");
		try {
			String vvicDate = LocalDate.now().plusDays(-1).toString();
			savePosOrderDetail(vvicDate, vvicDate);
			savePosOrder(vvicDate, vvicDate);
			logger.info(LocalDateTime.now().toString() + "vvic定时任务 执行成功");
		} catch (Exception e) {
			logger.error(LocalDateTime.now().toString() + "vvic定时任务 执行失败");
		}

	}

	/**
	 * 查询测试
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryTest() {
		return posOrderDao.queryTest();
	}

	/**
	 * 根据日期获取小单数据
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Object savePosOrderDetail(String start, String end) {

		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie);
		Integer curPage = 1;
		List<PosOrderDetail> posOrderDetails = new ArrayList<PosOrderDetail>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, curPage.toString(), start, end);
			String result = com.mofong.util.GetRequest.requestByGet3(requestUrl, new HashMap<String, Object>(),
					restTemplate, headers);
			List<PosOrderDetail> posOrderDetailResult = getPosOrderDetails(result);
			if (posOrderDetailResult.size() == 0) {
				break;
			} else {
				curPage++;
			}
			posOrderDetails.addAll(posOrderDetailResult);
		}
		logger.info("details count : " + posOrderDetails.size());
		int insertCount = 0;
		List<PosOrderDetail> tempList = new ArrayList<PosOrderDetail>();
		for (PosOrderDetail posOrderDetail : posOrderDetails) {
			if (tempList.size() == 100) {
				insertCount += posOrderDao.PosOrderDetailInsertBatch(tempList);
				tempList.clear();
				System.out.println(insertCount);
			}
			tempList.add(posOrderDetail);
		}
		if (tempList.size() != 0) {
			insertCount += posOrderDao.PosOrderDetailInsertBatch(tempList);
		}
		logger.info("details insert count : " + insertCount);
		return insertCount;
	}

	/**
	 * 根据日期获取大单数据
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Object savePosOrder(String start, String end) {
		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie);
		Integer curPage = 1;
		List<PosOrder> posOrders = new ArrayList<PosOrder>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, curPage.toString(), start, end);
			String result = GetRequest.requestByGet3(requestUrl, new HashMap<String, Object>(), restTemplate, headers);
			List<PosOrder> posOrdersResult = getPosOrders(result);
			if (posOrdersResult.size() == 0) {
				break;
			} else {
				curPage++;
			}
			posOrders.addAll(posOrdersResult);
		}
		logger.info("orders count : " + posOrders.size());
		int insertCount = 0;
		List<PosOrder> tempList = new ArrayList<PosOrder>();
		for (PosOrder posOrder : posOrders) {
			if (tempList.size() == 100) {
				insertCount += posOrderDao.PosOrderInsertBatch(tempList);
				tempList.clear();
				System.out.println(insertCount);
			}
			tempList.add(posOrder);
		}
		if (tempList.size() != 0) {
			insertCount += posOrderDao.PosOrderInsertBatch(tempList);
		}
		logger.info("orders insert count : " + insertCount);
		return insertCount;
	}

	/**
	 * 导出打单记录
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public Object exportPosRecord(String start, String end) {
		boolean getPosRecordFlag = true;

		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie);
		Integer curPage = 1;
		List<PosOrderDetail> posOrderDetails = new ArrayList<PosOrderDetail>();
		Map<String, String> customerNameMap = new HashMap<String, String>();
		while (getPosRecordFlag) {
			String requestUrl = String.format(exportPosRecordUrl, curPage.toString(), start, end);
			System.out.println(requestUrl);
			String result = GetRequest.requestByGet3(requestUrl, new HashMap<String, Object>(), restTemplate, headers);

//		List<PosOrder> posOrders = getPosOrders(result);

			Map<String, Object> map = JsonUtil.str2Map(result);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			List<Map<String, Object>> recordList = (List<Map<String, Object>>) data.get("recordList");
			if (recordList.size() == 0) {
				break;
			} else {
				curPage++;
				getPosRecordFlag = true;
			}

			for (Map<String, Object> record : recordList) {
				customerNameMap.put((String) record.get("orderNo"), (String) record.get("customerName"));
			}

			List<PosOrderDetail> posOrderDetailsTemp = getPosOrderDetails(result);
			for (PosOrderDetail posOrderDetail : posOrderDetailsTemp) {
				posOrderDetail.setCustomerName(customerNameMap.get(posOrderDetail.getOrderNo()));
				posOrderDetails.add(posOrderDetail);
			}

		}

		// 写法1
		String fileName = "D://save//desktop/simpleWrite" + System.currentTimeMillis() + ".xls";
		// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
		// 如果这里想使用03 则 传入excelType参数即可
//		EasyExcel.write(fileName, PosOrderDetail.class).sheet("模板").doWrite(posOrderDetails);
		;
		return null;
	}

	private List<PosOrderDetail> getPosOrderDetails(String result) {
		List<PosOrderDetail> posOrderDetails = new ArrayList<PosOrderDetail>();

		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = queryRequestResult(map);
		if (!flag) {
			return new ArrayList<PosOrderDetail>();
		}
		List<Map<String, Object>> recordList = getPosRecordDetailList(map);
		for (Map<String, Object> record : recordList) {
			PosOrderDetail posOrderDetail = new PosOrderDetail(record);
			posOrderDetails.add(posOrderDetail);
		}
		return posOrderDetails;
	}

	private List<PosOrder> getPosOrders(String result) {
		List<PosOrder> posOrders = new ArrayList<PosOrder>();

		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = queryRequestResult(map);
		if (!flag) {
			return new ArrayList<PosOrder>();
		}
		List<Map<String, Object>> recordList = getPosRecordList(map);

		for (Map<String, Object> record : recordList) {
			PosOrder posOrder = new PosOrder(record);
			posOrders.add(posOrder);
		}
		return posOrders;
	}

	private List<Map<String, Object>> getPosRecordList(Map<String, Object> map) {
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		List<Map<String, Object>> recordList = (List<Map<String, Object>>) data.get("recordList");
		return recordList;
	}

	private List<Map<String, Object>> getPosRecordDetailList(Map<String, Object> map) {
		Map<String, Object> data = (Map<String, Object>) map.get("data");
		List<Map<String, Object>> recordList = (List<Map<String, Object>>) data.get("recordList");
		List<Map<String, Object>> recordDetailList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> record : recordList) {
			recordDetailList.addAll((List<Map<String, Object>>) record.get("orderDetailList"));
		}

		return recordDetailList;
	}

	private boolean queryRequestResult(Map<String, Object> data) {
		int code = (Integer) data.get("code");
		if (code == 200) {
			return true;
		} else {
			logger.info("请求失败，msg：" + (String) data.get("message"));
			return false;
		}
	}

}
