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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.mofong.bean.Result;
import com.mofong.util.GetRequest;
import com.mofong.util.JsonUtil;
import com.mofong.vvic.bean.Log;
import com.mofong.vvic.bean.PosOrder;
import com.mofong.vvic.bean.PosOrderDetail;
import com.mofong.vvic.dao.CookieDao;
import com.mofong.vvic.dao.LogDao;
import com.mofong.vvic.dao.PosOrderDao;

@Service("vvicService")
public class VvicService {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.service.VvicService.class);

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private PosOrderDao posOrderDao;

	@Autowired
	private CookieDao cookieDao;

	@Autowired
	private LogDao logDao;

	private static String exportPosRecordUrl = "https://www.vvic.com/apic/trade/pos/record?currentPage=%s&pageSize=80&startDate=%s&endDate=%s&pos_resource_id=19&keywords=";

	private String cookie;

	private static CookieDao cookieDaoInit;

	@PostConstruct
	public void init() {
		logger.info("从数据库获取cookie");
		this.logDao.addLog(Log.info().setOperation("初始化cookie").setOperation_detail("从数据库获取cookie"));
		cookieDaoInit = this.cookieDao;
		this.cookie = cookieDaoInit.getCookieById("vvic");
	}

	public Result updateVvicDataByDate(int year, int month) {
		Date start = Date.valueOf(LocalDate.of(year, month, 1));
		Date end = Date.valueOf(start.toLocalDate().plusMonths(1L).plusDays(-1L));
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("订单总览", updatePosOrderFromDate(year, month, start, end));
		resultMap.put("订单详情", updatePosOrderDetailFromDate(year, month, start, end));
		return Result.success(resultMap);
	}

	private String updatePosOrderDetailFromDate(int year, int month, Date start, Date end) {
		Date maxDate = Date.valueOf(this.posOrderDao.queryMaxDateInPosOrderDetail());
		boolean hadUpdated = false;
		List<PosOrderDetail> podListDB = this.posOrderDao.queryPosOrderDetailByDate(start,
				Date.valueOf(end.toLocalDate().plusDays(1L)));
		List<PosOrderDetail> podListOnline = getPosOrderDetailListFromDate(start.toString(), end.toString());
		logger.info("PosOrderDetail ---> db list size : {}, Online list size : {}", Integer.valueOf(podListDB.size()),
				Integer.valueOf(podListOnline.size()));
		Map<Long, PosOrderDetail> dbListMap = new HashMap<>(podListDB.size());
		Map<Long, PosOrderDetail> onlineListMap = new HashMap<>(podListOnline.size());
		for (PosOrderDetail posOrderDetail : podListDB)
			dbListMap.put(posOrderDetail.getOrderDetailId(), posOrderDetail);
		for (PosOrderDetail posOrderDetail : podListOnline)
			onlineListMap.put(posOrderDetail.getOrderDetailId(), posOrderDetail);
		List<PosOrderDetail> changeList = new ArrayList<>();
		Set<String> delOrderNoSet = new HashSet<>();
		Iterator<Map.Entry<Long, PosOrderDetail>> onlineMapIterator = onlineListMap.entrySet().iterator();
		while (onlineMapIterator.hasNext()) {
			Map.Entry<Long, PosOrderDetail> entry = onlineMapIterator.next();
			if (Date.valueOf(((PosOrderDetail) entry.getValue()).getYmd()).after(maxDate))
				continue;
			PosOrderDetail dbPOS = dbListMap.get(entry.getKey());
			if (dbPOS == null) {
				hadUpdated = true;
				changeList.add(entry.getValue());
				delOrderNoSet.add(((PosOrderDetail) entry.getValue()).getOrderNo());
			}
		}
		if (hadUpdated) {
			int delNum = this.posOrderDao.deletePosOrderDetailByOrderNo(delOrderNoSet);
			int insertNum = this.posOrderDao.PosOrderDetailInsertBatch(changeList);
			logger.info("订单详情 --> {}年{}月修改了的订单号：{}",
					new Object[] { Integer.valueOf(year), Integer.valueOf(month), JsonUtil.obj2Str(delOrderNoSet) });
			String str = "订单详情 --> 更新" + year + "年" + month + "月的订单详情，线上订单详情总数：" + podListOnline.size() + "，数据库订单详情总数："
					+ podListDB.size() + "，修改的订单号数量：" + changeList.size();
			this.logDao.addLog(Log.info().setOperation("订单详情 --> 更新" + year + "年" + month + "月的订单详情")
					.setOperation_detail("线上订单详情总数：" + podListOnline.size() + "，数据库订单详情总数：" + podListDB.size()
							+ "，修改的订单号数量：" + changeList.size()));
			return str;
		}
		String rtn = "订单详情 --> " + year + "年" + month + "月的订单详情与线上一致。";
		logger.info(rtn);
		this.logDao.addLog(Log.info().setOperation(rtn));
		return rtn;
	}

	private String updatePosOrderFromDate(int year, int month, Date start, Date end) {
		Date maxDate = Date.valueOf(this.posOrderDao.queryMaxDateInPosOrder());
		boolean hadUpdated = false;
		List<PosOrder> poListDB = this.posOrderDao.queryPosOrderByDate(start,
				Date.valueOf(end.toLocalDate().plusDays(1L)));
		List<PosOrder> poListOnline = getPosOrderListFromDate(start.toString(), end.toString());
		logger.info("PosOrder ---> db list size : {}, Online list size : {}", Integer.valueOf(poListDB.size()),
				Integer.valueOf(poListOnline.size()));
		Map<String, PosOrder> dbListMap = new HashMap<>(poListDB.size());
		Map<String, PosOrder> onlineListMap = new HashMap<>(poListOnline.size());
		for (PosOrder posOrder : poListDB)
			dbListMap.put(posOrder.getOrderId(), posOrder);
		for (PosOrder posOrder : poListOnline)
			onlineListMap.put(posOrder.getOrderId(), posOrder);
		List<PosOrder> changeList = new ArrayList<>();
		Iterator<Map.Entry<String, PosOrder>> onlineMapIterator = onlineListMap.entrySet().iterator();
		while (onlineMapIterator.hasNext()) {
			Map.Entry<String, PosOrder> entry = onlineMapIterator.next();
			if (Date.valueOf(((PosOrder) entry.getValue()).getYmd()).after(maxDate))
				continue;
			PosOrder dbPO = dbListMap.get(entry.getKey());
			if (dbPO == null) {
				changeList.add(entry.getValue());
				hadUpdated = true;
				continue;
			}
			if (((PosOrder) entry.getValue()).equals(dbPO))
				continue;
			hadUpdated = true;
			changeList.add(entry.getValue());
		}
		Set<String> delSet = new HashSet<>();
		Iterator<Map.Entry<String, PosOrder>> dbMapIterator = dbListMap.entrySet().iterator();
		while (dbMapIterator.hasNext()) {
			Map.Entry<String, PosOrder> entry = dbMapIterator.next();
			PosOrder onlinePO = onlineListMap.get(entry.getKey());
			if (onlinePO == null) {
				delSet.add(((PosOrder) entry.getValue()).getOrderNo());
				hadUpdated = true;
			}
		}
		if (hadUpdated) {
			Set<String> orderNoSet = new HashSet<>();
			for (PosOrder posOrder : changeList) {
				this.posOrderDao.updatePosOrder(posOrder);
				orderNoSet.add(posOrder.getOrderNo());
			}
			this.posOrderDao.deletePosOrderByOrderNo(delSet);
			logger.info("订单总览 --> {}年{}月修改了的订单号：{}",
					new Object[] { Integer.valueOf(year), Integer.valueOf(month), JsonUtil.obj2Str(orderNoSet) });
			String str = "订单总览 --> 更新" + year + "年" + month + "月的订单总览，线上订单数量：" + poListOnline.size() + "，数据库订单数量："
					+ poListDB.size() + "，修改的订单数量：" + changeList.size() + "，删除的订单数量：" + delSet.size();
			this.logDao.addLog(Log.info().setOperation("订单总览 --> 更新" + year + "年" + month + "月的订单总览")
					.setOperation_detail("线上订单数量：" + poListOnline.size() + "，数据库订单数量：" + poListDB.size() + "，修改的订单数量："
							+ changeList.size() + "，删除的订单数量：" + delSet.size()));
			return str;
		}
		String rtn = "订单总览 --> " + year + "年" + month + "月的订单总览与线上一致。";
		logger.info(rtn);
		this.logDao.addLog(Log.info().setOperation(rtn));
		return rtn;
	}

	public Result updateCookie(String cookie) {
		if (StringUtils.isEmpty(cookie))
			return Result.error("参数为空");
		String dbCookie = this.cookieDao.getCookieById("vvic");
		if (!StringUtils.isEmpty(dbCookie) && cookie.trim().equals(dbCookie.trim()))
			return Result.success("cookie相同无需更新");
		logger.info("更新cookie");
		int updateCount = this.cookieDao.updateCookieById(cookie, "vvic");
		logger.info("更新cookie数量：" + updateCount);
		if (updateCount == 1) {
			this.cookie = cookie;
			return Result.success();
		}
		return Result.error("更新cookie数量：" + updateCount);
	}

	/**
	 * vvic定时任务
	 */
	public void vvicShedule() {
		logger.info(LocalDateTime.now().toString() + "执行了vvic定时任务");
		this.logDao.addLog(Log.info().setOperation("定时任务开始执行"));
		try {
			String vvicDate = LocalDate.now().plusDays(-1L).toString();
			savePosOrder(vvicDate, vvicDate);
			this.logDao.addLog(Log.info().setOperation("PosOrder完成"));
			savePosOrderDetail(vvicDate, vvicDate);
			this.logDao.addLog(Log.info().setOperation("PosOrderDetail完成"));
			logger.info(LocalDateTime.now().toString() + "vvic定时任务 执行成功");
			this.logDao.addLog(Log.info().setOperation("获取vvic数据成功"));
			LocalDateTime lastDay = LocalDateTime.now().plusDays(-1);
			if (lastDay.getDayOfMonth() % 10 == 0 || LocalDateTime.now().getDayOfMonth() == 1) {
				updateVvicDataByDate(lastDay.getYear(), lastDay.getMonthValue());
			}
			this.logDao.addLog(Log.info().setOperation("更新vvic数据成功"));
		} catch (Exception e) {
			logger.error(LocalDateTime.now().toString() + "vvic定时任务 执行失败");
			this.logDao.addLog(Log.error().setOperation("定时任务执行失败").setOperation_detail(e.getMessage()));
		} finally {
			logger.info(LocalDateTime.now().toString() + "完成了vvic定时任务");
			this.logDao.addLog(Log.info().setOperation("定时任务结束执行"));
		}
	}

	public List<Map<String, Object>> queryTest() {
		return this.posOrderDao.queryTest();
	}

	public Object savePosOrderDetail(String start, String end) {
		List<PosOrderDetail> posOrderDetails = getPosOrderDetailListFromDate(start, end);
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
		this.logDao.addLog(
				Log.info().setOperation("PosOrderDetail共" + posOrderDetails.size() + "条数据，插入成功" + insertCount + "条数据"));
		return Integer.valueOf(insertCount);
	}

	private List<PosOrderDetail> getPosOrderDetailListFromDate(String start, String end) {
		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", this.cookie);
		Integer curPage = Integer.valueOf(1);
		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
			List<PosOrderDetail> posOrderDetailResult = getPosOrderDetails(result);
			if (posOrderDetailResult.size() == 0)
				break;
			Integer integer1 = curPage, integer2 = curPage = Integer.valueOf(curPage.intValue() + 1);
			posOrderDetails.addAll(posOrderDetailResult);
		}
		return posOrderDetails;
	}

	public Object savePosOrder(String start, String end) {
		List<PosOrder> posOrders = getPosOrderListFromDate(start, end);
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
		this.logDao.addLog(Log.info().setOperation("PosOrder共" + posOrders.size() + "条数据，插入成功" + insertCount + "条数据"));
		return Integer.valueOf(insertCount);
	}

	private List<PosOrder> getPosOrderListFromDate(String start, String end) {
		boolean flag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", this.cookie);
		Integer curPage = Integer.valueOf(1);
		List<PosOrder> posOrders = new ArrayList<>();
		while (flag) {
			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
			List<PosOrder> posOrdersResult = getPosOrders(result);
			if (posOrdersResult.size() == 0)
				break;
			Integer integer1 = curPage, integer2 = curPage = Integer.valueOf(curPage.intValue() + 1);
			posOrders.addAll(posOrdersResult);
		}
		return posOrders;
	}

	public Object exportPosRecord(String start, String end) {
		boolean getPosRecordFlag = true;
		HttpHeaders headers = new HttpHeaders();
		headers.add("cookie", this.cookie);
		Integer curPage = Integer.valueOf(1);
		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
		Map<String, String> customerNameMap = new HashMap<>();
		while (getPosRecordFlag) {
			String requestUrl = String.format(exportPosRecordUrl, new Object[] { curPage.toString(), start, end });
			System.out.println(requestUrl);
			String result = GetRequest.requestByGetBackString(requestUrl, new HashMap<>(), this.restTemplate, headers);
			Map<String, Object> map = JsonUtil.str2Map(result);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			List<Map<String, Object>> recordList = (List<Map<String, Object>>) data.get("recordList");
			if (recordList.size() == 0)
				break;
			Integer integer1 = curPage, integer2 = curPage = Integer.valueOf(curPage.intValue() + 1);
			getPosRecordFlag = true;
			for (Map<String, Object> record : recordList)
				customerNameMap.put((String) record.get("orderNo"), (String) record.get("customerName"));
			List<PosOrderDetail> posOrderDetailsTemp = getPosOrderDetails(result);
			for (PosOrderDetail posOrderDetail : posOrderDetailsTemp) {
				posOrderDetail.setCustomerName(customerNameMap.get(posOrderDetail.getOrderNo()));
				posOrderDetails.add(posOrderDetail);
			}
		}
		String fileName = "D://save//desktop/simpleWrite" + System.currentTimeMillis() + ".xls";
		return null;
	}

	private List<PosOrderDetail> getPosOrderDetails(String result) {
		List<PosOrderDetail> posOrderDetails = new ArrayList<>();
		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = queryRequestResult(map);
		if (!flag)
			return new ArrayList<>();
		List<Map<String, Object>> recordList = getPosRecordDetailList(map);
		for (Map<String, Object> record : recordList) {
			PosOrderDetail posOrderDetail = new PosOrderDetail(record);
			posOrderDetails.add(posOrderDetail);
		}
		return posOrderDetails;
	}

	private List<PosOrder> getPosOrders(String result) {
		List<PosOrder> posOrders = new ArrayList<>();
		Map<String, Object> map = JsonUtil.str2Map(result);
		boolean flag = queryRequestResult(map);
		if (!flag)
			return new ArrayList<>();
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
		List<Map<String, Object>> recordDetailList = new ArrayList<>();
		for (Map<String, Object> record : recordList)
			recordDetailList.addAll((List) record.get("orderDetailList"));
		return recordDetailList;
	}

	private boolean queryRequestResult(Map<String, Object> data) {
		int code = ((Integer) data.get("code")).intValue();
		if (code == 200)
			return true;
		this.cookieDao.setCookieNotAvailable("vvic");
		logger.info("请求失败，msg：" + (String) data.get("message"));
		return false;
	}
}
