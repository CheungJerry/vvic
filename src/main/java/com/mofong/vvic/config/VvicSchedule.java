package com.mofong.vvic.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ObjectUtils;

import com.mofong.vvic.bean.Cookie;
import com.mofong.vvic.dao.CronDao;
import com.mofong.vvic.service.CookieService;
import com.mofong.vvic.service.VvicService;

@Configuration
@EnableScheduling
public class VvicSchedule implements SchedulingConfigurer {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.config.VvicSchedule.class);

	@Autowired
	private VvicService vvicService;

	@Autowired
	private CronDao cronDao;

	/**
	 * 定时获取vvic数据
	 */
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Iterator<Entry<String, Cookie>> cookieIterator = CookieService.cookieMap.entrySet().iterator();
		while (cookieIterator.hasNext()) {
			Map.Entry<String, Cookie> entry = (Map.Entry<String, Cookie>) cookieIterator.next();
			taskRegistrar.addTriggerTask(() -> this.vvicService.vvicShedule(entry.getValue()), triggerContext -> {
				String cron = this.cronDao.getCronById(entry.getValue().getCookie_id());
				if (ObjectUtils.isEmpty(cron)) {
					logger.error("cron错误");
				}
				return (new CronTrigger(cron)).nextExecutionTime(triggerContext);
			});
		}

	}
}
