package com.mofong.vvic.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mofong.bean.Result;
import com.mofong.vvic.config.VvicSchedule;
import com.mofong.vvic.service.CookieService;
import com.mofong.vvic.service.VvicService;

@RestController
@CrossOrigin
public class VvicController {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.controller.VvicController.class);

	@Autowired
	private VvicService vvicService;

	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private VvicSchedule vvicSchedule;

	/**
	 * 手动刷新vvic的数据
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@GetMapping({ "/refreshVvicData" })
	public Result updateVvicData(@RequestParam("year") Integer year, @RequestParam("month") Integer month,
			@RequestParam("account") String account) {
		try {
			return this.vvicService.updateVvicDataByDate(year.intValue(), month.intValue(), CookieService.cookieMap.get(account));
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping({ "/queryTest" })
	public Result queryTest() {
		try {
			List<Map<String, Object>> result = this.vvicService.queryTest();
			return Result.success(result);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

//	@RequestMapping({ "/scheduleTest" })
//	public Result scheduleTest() {
//		try {
//			this.vvicService.vvicShedule();
//			return Result.success();
//		} catch (Exception e) {
//			return Result.error(e.getMessage());
//		}
//	}

	/**
	 * 从数据库刷新cookie
	 * 
	 * @return
	 */
	@RequestMapping({ "/refreshCookie" })
	public Result refreshCookie() {
		try {
			logger.info("手动刷新cookie");
			this.cookieService.init();
			return Result.success();
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 手动更新cookie
	 * 
	 * @param cookie
	 * @param cookieId
	 * @return
	 */
	@RequestMapping({ "/refreshCookieManual" })
	public Result refreshCookieManual(@RequestParam("cookie") String cookie,
			@RequestParam("cookieId") String cookieId) {
		try {
			return this.cookieService.updateCookie(cookie, cookieId);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
	
//	/**
//	 * 手动更新cookie
//	 * 
//	 * @param cookie
//	 * @param cookieId
//	 * @return
//	 */
//	@RequestMapping({ "/checkSchedule" })
//	public Result checkAllSchedule() {
//		try {
//			vvicSchedule.queryAllSchedule();
//			return null;
//		} catch (Exception e) {
//			return Result.error(e.getMessage());
//		}
//	}
}
