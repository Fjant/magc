package com.xincheng.job.support.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.xincheng.job.model.JobEntity;
import com.xincheng.job.service.JobService;
import com.xincheng.job.support.QuartzJobFactory;
import com.xincheng.job.support.QuartzJobFactoryDisallowConcurrentExecution;

/**
 * 
 * @Description: 计划任务管理
 * @author chenjianlin
 * @date 2014年4月25日 下午2:43:54
 */
@Service
public class JobTaskService {
	public final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private JobService jobService;

	/**
	 * 从数据库中取 区别于getAllJob
	 * 
	 * @return
	 */
	public List<JobEntity> getAllTask() {
		try {
			return jobService.getAll();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加到数据库中 区别于addJob
	 */
	public void addTask(JobEntity job) {
		job.setCreateTime(new Date());
		try {
			jobService.save(job);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 从数据库中查询job
	 */
	public JobEntity getTaskById(Long jobId) {
		try {
			return jobService.getById(jobId);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更改任务状态
	 * 
	 * @throws SchedulerException
	 */
	public void changeStatus(Long jobId, String cmd) throws SchedulerException {
		JobEntity job = getTaskById(jobId);
		if (job == null) {
			return;
		}
		if ("stop".equals(cmd)) {
			deleteJob(job);
			job.setStatusId(JobEntity.STATUS_NOT_RUNNING);
		} else if ("start".equals(cmd)) {
			job.setStatusId(JobEntity.STATUS_RUNNING);
			addJob(job);
		}

		try {
			jobService.update(job);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 更改任务 cron表达式
	 * 
	 * @throws SchedulerException
	 */
	public void updateCron(Long jobId, String cron) throws SchedulerException {
		JobEntity job = getTaskById(jobId);
		if (job == null) {
			return;
		}
		job.setCronExpression(cron);
		if (JobEntity.STATUS_RUNNING == job.getStatusId()) {
			updateJobCron(job);
		}

		try {
			jobService.update(job);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 添加任务
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void addJob(JobEntity job) throws SchedulerException {

		deleteJob(job);

		if (job == null || JobEntity.STATUS_RUNNING != job.getStatusId()) {
			return;
		}

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		log.debug(scheduler + ".......................................................................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 不存在，创建一个
		if (null == trigger) {
			Class clazz = JobEntity.CONCURRENT_IS == job.getIsConcurrent() ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getName(), job.getGroup()).build();

			jobDetail.getJobDataMap().put("jobEntity", job);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	@PostConstruct
	public void init() throws Exception {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		// 这里获取任务信息数据
		List<JobEntity> jobList = jobService.getAll();

		for (JobEntity job : jobList) {
			addJob(job);
		}
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobEntity> getAllJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<JobEntity> jobList = new ArrayList<JobEntity>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				JobEntity job = new JobEntity();
				job.setName(jobKey.getName());
				job.setGroup(jobKey.getGroup());
				job.setRemark("触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				// job.setStatusId(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobEntity> getRunningJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<JobEntity> jobList = new ArrayList<JobEntity>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			JobEntity job = new JobEntity();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			job.setName(jobKey.getName());
			job.setGroup(jobKey.getGroup());
			job.setRemark("触发器:" + trigger.getKey());
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			// job.setStatusId(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void pauseJob(JobEntity jobEntity) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup());
		if (scheduler.checkExists(jobKey)) {
			scheduler.pauseJob(jobKey);
		}
		showJobs();
	}

	/**
	 * 恢复一个job
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void resumeJob(JobEntity jobEntity) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup());
		if (scheduler.checkExists(jobKey)) {
			scheduler.resumeJob(jobKey);
		} else {
			addJob(jobEntity);
		}
		showJobs();
	}

	/**
	 * 删除一个job
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void deleteJob(JobEntity jobEntity) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup());
		if (scheduler.checkExists(jobKey)) {
			scheduler.deleteJob(jobKey);
		}
	}

	/**
	 * 立即执行job
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void runAJobNow(JobEntity jobEntity) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup());
		if (scheduler.checkExists(jobKey)) {
			scheduler.triggerJob(jobKey);
		}
	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void updateJobCron(JobEntity jobEntity) throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getName(), jobEntity.getGroup());
		if (!scheduler.checkExists(jobKey)) {
			return;
		}

		TriggerKey triggerKey = TriggerKey.triggerKey(jobEntity.getName(), jobEntity.getGroup());

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			return;
		}

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());

		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		if (trigger == null) {
			return;
		}

		scheduler.rescheduleJob(triggerKey, trigger);
	}

	public static void main(String[] args) {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("schedulerFactoryBean");
	}

	private void showJobs() {
		try {
			List<JobEntity> jobs = getAllJob();
			System.out.println("所有任务：");
			for (JobEntity job : jobs) {
				System.out.println(job.getName());
			}

			jobs = getRunningJob();
			System.out.println("正在运行的任务：");
			for (JobEntity job : jobs) {
				System.out.println(job.getName());
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
