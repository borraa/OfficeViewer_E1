package com.woonam.log4j2;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;

import com.woonam.util.Common;

public class LogRolling implements Job{

    private Common m_C = null;
    private static Logger logger = LogManager.getLogger("Woonam");

    public LogRolling() {
        this.m_C = new Common();
    }

    @Override
    public void execute(JobExecutionContext context) {
        try
        {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            String logPath 		= (String) schedulerContext.get("LOG_PATH");
            String expire 		= (String) schedulerContext.get("EXPIRE");

            Task_RemoveLog(logPath, expire);
        }
        catch (Exception e) {
            logger.error("Failed to init LogRolling : ", e);
        }
    }

    private void Task_RemoveLog(String logPath, String expire) {

        File path = new File(logPath);

        String[] target = path.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                int lastIndex = !name.contains("-") ? 8 : name.indexOf("-");
                String strFileDate = name.substring(0, lastIndex).trim();

                if(!strFileDate.matches("\\p{Digit}+")) {
                    return false;
                }

                DateTime today = DateTime.parse(m_C.getToday("yyyyMMdd"), DateTimeFormat.forPattern("yyyyMMdd"));
                DateTime fileDate = DateTime.parse(strFileDate, DateTimeFormat.forPattern("yyyyMMdd"));

                int daysDistance = Days.daysBetween(fileDate.toInstant(), today.toInstant()).getDays();
                if(daysDistance > Integer.parseInt(expire)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });

       if(target != null && target.length > 0) {
           for (String targetFilePath : target) {
               File targetFile = new File(logPath + File.separator + targetFilePath);
               targetFile.delete();
           }
       }

        logger.info("Log rolling succefully finished. rolled count : " + (target != null ? target.length : 0));
    }
}
