package com.mofong.vvic.config;

import com.mofong.vvic.dao.CronDao;
import com.mofong.vvic.service.VvicService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ObjectUtils;

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
		taskRegistrar.addTriggerTask(() -> this.vvicService.vvicShedule(), triggerContext -> {
			String cron = this.cronDao.getCronById("vvic");
			if (ObjectUtils.isEmpty(cron))
				logger.error("cron错误");
			return (new CronTrigger(cron)).nextExecutionTime(triggerContext);
		});
	}
}
