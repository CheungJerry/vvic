package com.mofong.vvic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ObjectUtils;

import com.mofong.vvic.dao.CronDao;
import com.mofong.vvic.service.VvicService;

@Configuration
@EnableScheduling
public class VvicSchedule implements SchedulingConfigurer {

	private static Logger logger = LoggerFactory.getLogger(VvicSchedule.class);

	@Autowired
	private VvicService vvicService;

	@Autowired
	private CronDao cronDao;

//	static String  cron = "0/5 * * * * ?";

	/**
	 * 执行定时任务.
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(
				// 1.添加任务内容(Runnable)
				() -> vvicService.vvicShedule(),
				// 2.设置执行周期(Trigger)
				triggerContext -> {
					// 2.1 从数据库获取执行周期
					String cron = cronDao.getCronById("vvic");
					// 2.2 合法性校验.
					if (ObjectUtils.isEmpty(cron)) {
						logger.error("cron错误");
					}
					// 2.3 返回执行周期(Date)
					return new CronTrigger(cron).nextExecutionTime(triggerContext);
				});
	}
}
