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
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mofong.vvic.bean.Cookie;
import com.mofong.vvic.dao.CronDao;
import com.mofong.vvic.service.CookieService;
import com.mofong.vvic.service.VvicService;

@Configuration
@EnableScheduling
@Service("vvicSchedule")
public class VvicSchedule implements SchedulingConfigurer {
	private static Logger logger = LoggerFactory.getLogger(com.mofong.vvic.config.VvicSchedule.class);

	@Autowired
	private VvicService vvicService;

	@Autowired
	private CronDao cronDao;

	private ScheduledTaskRegistrar TASK;

	/**
	 * 定时获取vvic数据
	 */
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Iterator<Entry<String, Cookie>> cookieIterator = CookieService.cookieMap.entrySet().iterator();
		while (cookieIterator.hasNext()) {
			Map.Entry<String, Cookie> entry = (Map.Entry<String, Cookie>) cookieIterator.next();
			String cron = this.cronDao.getCronById(entry.getValue().getCookie_id());
			if (ObjectUtils.isEmpty(cron)) {
				logger.error("cron错误");
				continue;
			}
//			taskRegistrar.addTriggerTask(() -> this.vvicService.vvicShedule(entry.getValue()), triggerContext -> {
//				return (new CronTrigger(cron)).nextExecutionTime(triggerContext);
//			});
			tasks(taskRegistrar, cron, entry.getValue());
		}
		TASK = taskRegistrar;
	}

	private void tasks(ScheduledTaskRegistrar taskRegistrar, String cron, Cookie cookie) {
		taskRegistrar.addTriggerTask(() -> this.vvicService.vvicShedule(cookie), triggerContext -> {
			logger.info(cookie.getCookie_id() + ":定时任务，cron:" + cron);
			return (new CronTrigger(cron)).nextExecutionTime(triggerContext);
		});
	}

}
