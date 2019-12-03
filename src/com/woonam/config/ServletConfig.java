package com.woonam.config;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.woonam.log4j2.CustomConfigurationFactory;
import com.woonam.log4j2.LogScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

import com.woonam.util.Common;

@WebListener
public class ServletConfig implements ServletContextListener {

    private Common m_C = new Common();

    public void contextInitialized(ServletContextEvent event) {


        String rootPath 	= m_C.getRootPath(event.getServletContext());

       // LogManager.getLogger(ConfigurationFactory.getInstance());
        ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory());


        LogScheduler LS = new LogScheduler(rootPath);
        LS.run();

//        Logger logger = LogManager.getLogger();
//        logger.debug("A");
      //  Logger l = Logger().get

    }
    public void contextDestroyed(ServletContextEvent event) {
      //  LS.shutdown();
    }
}