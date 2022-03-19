package com.mofong.vvic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mofong.bean.Result;
import com.mofong.vvic.bean.Cookie;
import com.mofong.vvic.bean.Log;
import com.mofong.vvic.dao.CookieDao;
import com.mofong.vvic.dao.LogDao;

@Service("cookieService")
public class CookieService {

	private static Logger logger = LoggerFactory.getLogger(CookieService.class);

	private static CookieDao cookieDaoInit;

	public static Map<String, Cookie> cookieMap;

	@Autowired
	private CookieDao cookieDao;

	@Autowired
	private LogDao logDao;

	@PostConstruct
	public void init() {
		cookieMap = new HashMap<String, Cookie>();
		cookieDaoInit = this.cookieDao;
		List<Cookie> cookieList = cookieDaoInit.queryAllCookie();
		for (Cookie cookie : cookieList) {
			logger.info("从数据库获取 " + cookie.getCookie_id() + " cookie");
			this.logDao.addLog(Log.info().setOperation("初始化 " + cookie.getCookie_id() + " cookie")
					.setOperation_detail("从数据库获取cookie"));
			cookieMap.put(cookie.getCookie_id(), cookie);
		}
	}

	/**
	 * 更新cookie
	 * 
	 * @param cookie
	 * @return
	 */
	public Result updateCookie(String cookie, String cookieId) {
		if (StringUtils.isEmpty(cookie)) {
			return Result.error("参数为空");
		}

		String dbCookie = this.cookieDao.getCookieById(cookieId);
		if (!StringUtils.isEmpty(dbCookie) && cookie.trim().equals(dbCookie.trim())) {
			return Result.success("cookie相同无需更新");
		}

		logger.info("更新cookie");
		int updateCount = this.cookieDao.updateCookieById(cookie, cookieId);
		logger.info("更新cookie数量：" + updateCount);
		if (updateCount == 1) {
			cookieMap.get(cookieId).setCookie(cookie);
			return Result.success();
		}
		return Result.error("更新cookie数量：" + updateCount);
	}

	/**
	 * 判断cookie是否过期
	 * 
	 * @param data
	 * @return
	 */
	public boolean queryRequestResult(Map<String, Object> data, String cookieId) {
		int code = ((Integer) data.get("code")).intValue();
		if (code == 200) {
			return true;
		}
		this.cookieDao.setCookieNotAvailable(cookieId);
		logger.info("请求失败，msg：" + (String) data.get("message"));
		logger.info(cookieId + ":已失效");
		this.logDao.addLog(Log.info().setOperation(cookieId + ":已失效"));
		return false;
	}
}
