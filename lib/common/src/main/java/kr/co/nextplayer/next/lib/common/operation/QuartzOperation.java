package kr.co.nextplayer.next.lib.common.operation;

import kr.co.nextplayer.next.lib.common.property.TaskDefineProperty;
import kr.co.nextplayer.next.lib.common.util.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Map;

@Slf4j
@Configuration
public class QuartzOperation {
   private Scheduler scheduler;

    public QuartzOperation(@Autowired SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException {
        scheduler = schedulerFactoryBean.getScheduler();
    }

    /**
     * create a schedule job
     */
    public boolean createScheduleJob(TaskDefineProperty taskDefineProperty) {
        try {
            JobKey jobKey = taskDefineProperty.getJobKey();
            JobDataMap jobDataMap = getJobDataMap(taskDefineProperty.getJobDataMap());
            String description = taskDefineProperty.getDescription();
            Class<? extends Job> jobClass = taskDefineProperty.getJobClass();
            String cron = taskDefineProperty.getCronExpression();
            String misfireHandlingType = taskDefineProperty.getMisfireHandlingType();
            JobDetail jobDetail = getJobDetail(jobKey, description, jobDataMap, jobClass);
            Trigger trigger = getTrigger(jobKey, description, jobDataMap, cron, misfireHandlingType);
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            log.info(jobDetail.getKey() + " Timed task creates successfully " + LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(), "yyyy-MM-dd HH:mm:ss"));
            return true;
        }  catch (SchedulerException e) {
            log.error("CreateScheduleJob occurs a SchedulerException", e);
            return false;
        }
    }

    /**
     * modify a job's cronExpression or jobDataMap
     * @param taskDefineProperty
     * @return
     */
    public boolean modifyScheduleJob(TaskDefineProperty taskDefineProperty) {
        String cronExpression = taskDefineProperty.getCronExpression();
        String misfireHandlingType = taskDefineProperty.getMisfireHandlingType();
        if (!CronExpression.isValidExpression(cronExpression)) {
            return false;
        }
        JobKey jobKey = taskDefineProperty.getJobKey();
        TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
        try {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (cronTrigger == null) {
                log.info("cronTrigger in modifyScheduleJob is null");
                return false;
            }
            JobDataMap jobDataMap = new JobDataMap();
            if (!cronTrigger.getJobDataMap().equals(jobDataMap)) {
                jobDataMap = getJobDataMap(taskDefineProperty.getJobDataMap());
            } else {
                jobDataMap = cronTrigger.getJobDataMap();
            }

            if (!cronTrigger.getCronExpression().equalsIgnoreCase(cronExpression) || !cronTrigger.getJobDataMap().equals(jobDataMap)) {
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder(misfireHandlingType, cronExpression))
                        .usingJobData(jobDataMap)
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }
            log.info(cronTrigger.getKey() + " Timed task modifies successfully " + LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(), "yyyy-MM-dd HH:mm:ss"));
            return true;
        } catch (SchedulerException e) {
            log.error("modifyScheduleJob occures a SchedulerException", e);
            return false;
        }
    }

    /**
     * Get the definition of a scheduled task
     * JobDetail is the definition of the task, Job is the execution logic of the task
     * @param jobKey the name of the scheduled task group name
     * @param description The description of the scheduled task
     * @param jobDataMap The metadata of the scheduled task
     * @param jobClass {@link org.quartz.Job} The real execution logic definition class of the scheduled task
     */
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap jobDataMap, Class<? extends Job> jobClass) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(jobDataMap)
                .usingJobData(jobDataMap)
                .requestRecovery()
                .storeDurably()
                .build();
    }

    /**
     * Get Trigger (Job trigger, execution rule)
     * @param jobKey the name of the scheduled task group name
     * @param description The description of the scheduled task
     * @param jobDataMap The metadata of the scheduled task
     * @param cronExpression The execution cron expression of the scheduled task
     */
    public Trigger getTrigger(JobKey jobKey, String description, JobDataMap jobDataMap, String cronExpression, String misfireHandlingType) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .withDescription(description)
                .withSchedule(cronScheduleBuilder(misfireHandlingType, cronExpression))
                .usingJobData(jobDataMap)
                .build();
    }

    public JobDataMap getJobDataMap(Map<?, ?> map) {
        return map == null ? new JobDataMap() : new JobDataMap(map);
    }

    /**
     * Timed task stop
     */
    public Boolean pauseScheduleJob(String name, String group) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
            log.info(group + "." + name + " Timed task pauses successfully " + LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(), "yyyy-MM-dd HH:mm:ss"));
        } catch (SchedulerException e) {
            log.error("ShutdownScheduleJob occurs a SchedulerException", e);
            return false;
        }
        return true;
    }

    /**
     * Timed task resume
     */
    public Boolean resumeScheduleJob(String name, String group) {
        try {
            scheduler.resumeTrigger(TriggerKey.triggerKey(name, group));
            log.info(group + "." + name + " Timed task resumes successfully " + LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(), "yyyy-MM-dd HH:mm:ss"));
        } catch (SchedulerException e) {
            log.error("resumeScheduleJob occurs a SchedulerException", e);
            return false;
        }
        return true;
    }

    /**
     * Timed task delete
     */
    public Boolean delScheduleJob(String name, String group) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, group));
            scheduler.resumeTrigger(TriggerKey.triggerKey(name, group));
            scheduler.deleteJob(JobKey.jobKey(name, group));
            log.info(group + "." + name + " Timed task deletes successfully " + LocalDateUtil.formatUTC_format(LocalDateUtil.getLocalDateByZone(), "yyyy-MM-dd HH:mm:ss"));
        } catch (SchedulerException e) {
            log.error("delScheduleJob occurs a SchedulerException", e);
            return false;
        }
        return true;
    }

    private CronScheduleBuilder cronScheduleBuilder (String misfireHandlingType, String cronExpression) {
        if ("doNothing".equals(misfireHandlingType)) {
            return CronScheduleBuilder.cronSchedule(cronExpression)
                .withMisfireHandlingInstructionDoNothing();
        } else if ("ignoreMisfires".equals(misfireHandlingType)) {
            return CronScheduleBuilder.cronSchedule(cronExpression)
                .withMisfireHandlingInstructionIgnoreMisfires();
        }

        return CronScheduleBuilder.cronSchedule(cronExpression);
    }

}
