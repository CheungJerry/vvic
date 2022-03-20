package com.mofong.vvic.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mofong.bean.Result;
import com.mofong.util.GetRequest;
import com.mofong.util.JsonUtil;
import com.mofong.vvic.bean.Cookie;
import com.mofong.vvic.bean.Log;
import com.mofong.vvic.bean.PosOrder;
import com.mofong.vvic.bean.PosOrderDetail;
import com.mofong.vvic.dao.LogDao;
import com.mofong.vvic.dao.PosOrderDao;

@Service("vvicService")
public class VvicService {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.service.VvicService.class);

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private CookieService cookieService;

	@Autowired
	private PosOrderDao posOrderDao;

//	@Autowired
//	private CookieDao cookieDao;

	@Autowired
	private LogDao logDao;

	private static String exportPosRecordUrl = "https://www.vvic.com/apic/trade/pos/record?currentPage=%s&pageSize=80&startDate=%s&endDate=%s&pos_resource_id=19&keywords=";

//	private String cookie;
//
//	private static CookieDao cookieDaoInit;
//
//	@PostConstruct
//	public void init() {
//		logger.info("从数据库获取cookie");
//		this.logDao.addLog(Log.info().setOperation("初始化cookie").setOperation_detail("从数据库获取cookie"));
//		cookieDaoInit = this.cookieDao;
//		this.cookie = cookieDaoInit.getCookieById("vvic");
//	}

	/**
	 * 更新posorder和posorderdetail数据
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public Result updateVvicDataByDate(int year, int month, Cookie cookie) {
		Date start = Date.valueOf(LocalDate.of(year, month, 1));
		Date end = Date.valueOf(start.toLocalDate().plusMonths(1L).plusDays(-1L));
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("订单总览", updatePosOrderFromDate(year, month, start, end, cookie));
		resultMap.put("订单详情", updatePosOrderDetailFromDate(year, month, start, end, cookie));
		return Result.success(resultMap);
	}

	/**
	 * 更新posorderdetail
	 * 
	 * @param year
	 * @param month
	 * @param start
	 * @param end
	 * @return
	 */
	private String updatePosOrderDetailFromDate(int year, int month, Date start, Date end, Cookie cookie) {
		String ymd = year + "-" + (month < 10 ? "0" + month : month);
		int delNum = posOrderDao.deletePosOrderDetailByYmd(ymd, cookie.getCookie_id());
		int insNum = (int) savePosOrderDetail(start.toString(), end.toString(), cookie);
		String rtn = cookie.getCookie_id() + " " + year + "年" + month + "月:删除订单详情 --> " + delNum + "条，重新插入" + insNum
				+ "条。";
		logger.info(rtn);
		this.logDao.addLog(Log.info().setOperation(rtn));
		return rtn;
	}

	/**
	 * 更新posorder
	 * 
	 * @param year
	 * @param month
	 * @param start
	 * @param end
	 * @return
	 */
	private String updatePosOrderFromDate(int year, int month, Date start, Date end, Cookie cookie) {
		String ymd = year + "-" + (month < 10 ? "0" + month : month);
		int delNum = posOrderDao.deletePosOrderByYmd(ymd, cookie.getCookie_id());
		int insNum = (int) savePosOrder(start.toString(), end.toString(), cookie);
		String rtn = cookie.getCookie_id() + " " + year + "年" + month + "月:删除订单 --> " + delNum + "条，重新插入" + insNum
				+ "条。";
		logger.info(rtn);
		this.logDao.addLog(Log.info().setOperation(rtn));
		return rtn;
	}

	/**
	 * vvic定时任务
	 */
	public void vvicShedule(Cookie cookie) {
		logger.info(cookie.getCookie_id() + ":" + LocalDateTime.now().toString() + "执行了定时任务");
		this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "定时任务开始执行"));
		try {
			String vvicDate = LocalDate.now().plusDays(-1L).toString();
			savePosOrder(vvicDate, vvicDate, cookie);
			this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "PosOrder完成"));
			savePosOrderDetail(vvicDate, vvicDate, cookie);
			this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "PosOrderDetail完成"));
			logger.info(cookie.getCookie_id() + ":" + LocalDateTime.now().toString() + "定时任务 执行成功");
			this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "获取数据成功"));
			LocalDateTime lastDay = LocalDateTime.now().plusDays(-1);
			if (lastDay.getDayOfMonth() % 10 == 0 || LocalDateTime.now().getDayOfMonth() == 1) {
				updateVvicDataByDate(lastDay.getYear(), lastDay.getMonthValue(), cookie);
			}
			this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "更新数据成功"));
		} catch (Exception e) {
			logger.error(cookie.getCookie_id() + ":" + LocalDateTime.now().toString() + "定时任务 执行失败");
			this.logDao.addLog(Log.error().setOperation(cookie.getCookie_id() + ":" + "定时任务执行失败")
					.setOperation_detail(e.getMessage()));
		} finally {
			logger.info(cookie.getCookie_id() + ":" + LocalDateTime.now().toString() + "完成了定时任务");
			this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":" + "定时任务结束执行"));
		}
	}

	public List<Map<String, Object>> queryTest() {
		return this.posOrderDao.queryTest();
	}

	public Object savePosOrderDetail(String start, String end, Cookie cookie) {
		List<PosOrderDetail> posOrderDetails = getPosOrderDetailListFromDate(start, end, cookie);
		logger.info("details count : " + posOrderDetails.size());
		int insertCount = 0;
		List<PosOrderDetail> tempList = new ArrayList<>();
		for (PosOrderDetail posOrderDetail : posOrderDetails) {
			if (tempList.size() == 100) {
				insertCount += this.posOrderDao.PosOrderDetailInsertBatch(tempList);
				tempList.clear();
				System.out.println(insertCount);
			}
			tempList.add(posOrderDetail);
		}
		if (tempList.size() != 0)
			insertCount += this.posOrderDao.PosOrderDetailInsertBatch(tempList);
		logger.info("details insert count : " + insertCount);
		this.logDao.addLog(Log.info().setOperation(cookie.getCookie_id() + ":PosOrderDetail共" + posOrderDetails.size()
				+ "条数据，插入成功" + insertCount + "条数据"));
		return Integer.valueOf(insertCount);
	}

	private List<PosOrderDetail> getPosOrderDetailListFromDate(String start, String end, Cookie cookie) {
		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie.getCookie());
		Integer curPage = Integer.valueOf(1);
		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
			List<PosOrderDetail> posOrderDetailResult = getPosOrderDetails(result, cookie);
			if (posOrderDetailResult.size() == 0) {
				break;
			}
			curPage = Integer.valueOf(curPage.intValue() + 1);
			posOrderDetails.addAll(posOrderDetailResult);
		}
		return posOrderDetails;
	}

	public Object savePosOrder(String start, String end, Cookie cookie) {
		logger.info(cookie.getCookie_id() + " start task");
		List<PosOrder> posOrders = getPosOrderListFromDate(start, end, cookie);
		logger.info("orders count : " + posOrders.size());
		int insertCount = 0;
		List<PosOrder> tempList = new ArrayList<>();
		for (PosOrder posOrder : posOrders) {
			if (tempList.size() == 100) {
				insertCount += this.posOrderDao.PosOrderInsertBatch(tempList);
				tempList.clear();
				System.out.println(insertCount);
			}
			tempList.add(posOrder);
		}
		if (tempList.size() != 0)
			insertCount += this.posOrderDao.PosOrderInsertBatch(tempList);
		logger.info("orders insert count : " + insertCount);
		this.logDao.addLog(Log.info().setOperation(
				cookie.getCookie_id() + ":PosOrder共" + posOrders.size() + "条数据，插入成功" + insertCount + "条数据"));
		return Integer.valueOf(insertCount);
	}

	private List<PosOrder> getPosOrderListFromDate(String start, String end, Cookie cookie) {
		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", cookie.getCookie());
		Integer curPage = Integer.valueOf(1);
		List<PosOrder> posOrders = new ArrayList<>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
			List<PosOrder> posOrdersResult = getPosOrders(result, cookie);
			if (posOrdersResult.size() == 0)
				break;
			Integer integer1 = curPage, integer2 = curPage = Integer.valueOf(curPage.intValue() + 1);
			posOrders.addAll(posOrdersResult);
		}
		return posOrders;
	}

//	/**
//	 * 本地导出
//	 * 
//	 * @param start
//	 * @param end
//	 * @return
//	 */
//	public Object exportPosRecord(String start, String end, String cookieId) {
//		boolean getPosRecordFlag = true;
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("cookie", CookieService.cookieMap.get(cookieId).getCookie());
//		Integer curPage = Integer.valueOf(1);
//		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
//		Map<String, String> customerNameMap = new HashMap<>();
//		while (getPosRecordFlag) {
//			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
//			System.out.println(requestUrl);
//			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
//			Map<String, Object> map = JsonUtil.str2Map(result);
//			Map<String, Object> data = (Map<String, Object>) map.get("data");
//			List<Map<String, Object>> recordList = (List<Map<String, Object>>) data.get("recordList");
//			if (recordList.size() == 0)
//				break;
//			Integer integer1 = curPage, integer2 = curPage = Integer.valueOf(curPage.intValue() + 1);
//			getPosRecordFlag = true;
//			for (Map<String, Object> record : recordList)
//				customerNameMap.put((String) record.get("orderNo"), (String) record.get("customerName"));
//			List<PosOrderDetail> posOrderDetailsTemp = getPosOrderDetails(result);
//			for (PosOrderDetail posOrderDetail : posOrderDetailsTemp) {
//				posOrderDetail.setCustomerName(customerNameMap.get(posOrderDetail.getOrderNo()));
//				posOrderDetails.add(posOrderDetail);
//			}
//		}
//		String fileName = "D://save//desktop/simpleWrite" + System.currentTimeMillis() + ".xls";
//		return null;
//	}

	/**
	 * 构造PosOrderDetail List
	 * 
	 * @param result
	 * @return
	 */
	private List<PosOrderDetail> getPosOrderDetails(String result, Cookie cookie) {
		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = cookieService.queryRequestResult(map, cookie.getCookie_id());
		if (!flag) {
			return new ArrayList<>();
		}
		List<Map<String, Object>> recordList = getPosRecordDetailList(map);
		for (Map<String, Object> record : recordList) {
			PosOrderDetail posOrderDetail = new PosOrderDetail(record);
			posOrderDetail.setAccount(cookie.getCookie_id());
			posOrderDetails.add(posOrderDetail);
		}
		return posOrderDetails;
	}

	/**
	 * 构造 PosOrder List
	 * 
	 * @param result
	 * @return
	 */
	private List<PosOrder> getPosOrders(String result, Cookie cookie) {
		List<PosOrder> posOrders = new ArrayList<>();
		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = cookieService.queryRequestResult(map, cookie.getCookie_id());
		if (!flag)
			return new ArrayList<>();
		List<Map<String, Object>> recordList = getPosRecordList(map);
		for (Map<String, Object> record : recordList) {
			PosOrder posOrder = new PosOrder(record);
			posOrder.setAccount(cookie.getCookie_id());
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
		List<Map<String, Object>> recordDetailList = new ArrayList<>();
		for (Map<String, Object> record : recordList)
			recordDetailList.addAll((List) record.get("orderDetailList"));
		return recordDetailList;
	}

}
