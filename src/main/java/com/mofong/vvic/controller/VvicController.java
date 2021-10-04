package com.mofong.vvic.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mofong.bean.Result;
import com.mofong.vvic.service.VvicService;

@RestController
@CrossOrigin
public class VvicController {

	private static Logger logger = LoggerFactory.getLogger(VvicController.class);

	@Autowired
	private VvicService vvicService;

	@RequestMapping("/queryTest")
	// @RequestBody Map<String, Object> map
	public Result queryTest() {
		try {
			List<Map<String, Object>> result = vvicService.queryTest();
			return Result.success(result);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping("/scheduleTest")
	// @RequestBody Map<String, Object> map
	public Result scheduleTest() {
		try {
			vvicService.vvicShedule();
			return Result.success();
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping("/refreshCookie")
	// @RequestBody Map<String, Object> map
	public Result refreshCookie() {
		try {
			logger.info("手动刷新cookie");
			vvicService.init();
			return Result.success();
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
	}
}
