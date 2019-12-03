package com.woonam.log4j2;

import java.io.File;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.woonam.util.Common;
import com.woonam.util.Profile;

public class LogScheduler {

    private Profile m_profile = null;
    private String rootPath = null;
    private Scheduler 			scheduler 		= null;
    private static Logger logger = LogManager.getLogger("Woonam");

    public LogScheduler(String rootPath) {
        this.rootPath = rootPath;
        this.m_profile = new Profile(rootPath + "conf" + File.separator + "conf.ini");
    }

    public boolean run() {

        if(this.m_profile == null) return false;

        boolean isRunning = true;

//
        if(!Init_Scheduler()) {
            isRunning = false;
        }

        return isRunning;
    }


    private boolean Init_Scheduler() {
        try {
            Properties quartzProperties = new Properties();
            quartzProperties.put("org.quartz.threadPool.threadCount", "1");

            //	private String EXPIRE = null;
            SchedulerFactory schedulFactoty = new StdSchedulerFactory(quartzProperties);
            scheduler 		= schedulFactoty.getScheduler();
            //  scheduler.shutdown();
            scheduler.clear();
            scheduler.start();

            // define the job and tie it to our HelloJob class
            JobBuilder jobBuilder = JobBuilder.newJob(LogRolling.class);
            JobDataMap data = new JobDataMap();
            data.put("latch", this);
            data.put("trace", null);
            data.put("port", 0);

            JobDetail jobDetail = jobBuilder.usingJobData("Rolling", "com.woonam.log4j2.LogRolling")
                    .usingJobData(data)
                    .withIdentity("Rolling", "Log")
                    .build();


            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "group")
                    .startAt(DateBuilder.todayAt(0, 0, 0))
//            .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
                            .withIntervalInHours(24))
                    .build();

            SchedulerContext sc = scheduler.getContext();
            sc.put("LOG_PATH", 	this.rootPath + File.separator + m_profile.getString("LOG","PATH","logs"));
            sc.put("EXPIRE",	m_profile.getString("LOG", "EXPIRE", "30"));

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {

            logger.error("Init_Scheduler() fail : ", e);
            try {
                scheduler.shutdown(true);
            } catch (SchedulerException e1) {
                // TODO Auto-generated catch block
                logger.error("Init_Scheduler() fail : ", e1);
            }
            return false;
        }
        return true;
    }

    public boolean shutdown() {
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e1) {
            logger.error("Init_Scheduler() fail : ", e1);
            return false;
        }
        return true;
    }

}
