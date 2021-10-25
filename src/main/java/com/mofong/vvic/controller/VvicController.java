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
import com.mofong.vvic.service.VvicService;

@RestController
@CrossOrigin
public class VvicController {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.controller.VvicController.class);

	@Autowired
	private VvicService vvicService;

	@GetMapping({ "/refreshVvicData" })
	public Result updateVvicData(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
		try {
			return this.vvicService.updateVvicDataByDate(year.intValue(), month.intValue());
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

	@RequestMapping({ "/scheduleTest" })
	public Result scheduleTest() {
		try {
			this.vvicService.vvicShedule();
			return Result.success();
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping({ "/refreshCookie" })
	public Result refreshCookie() {
		try {
			logger.info("手动刷新cookie");
			this.vvicService.init();
			return Result.success();
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping({ "/refreshCookieManual" })
	public Result refreshCookieManual(@RequestParam String cookie) {
		try {
			return this.vvicService.updateCookie(cookie);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
}
