package com.woonam.log4j2;

import com.woonam.util.Common;
import com.woonam.util.Profile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;

import java.io.File;
import java.net.URI;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(10)
public class CustomConfigurationFactory extends ConfigurationFactory {

   // private static String m_profilePath = null;
 //   private static String m_rootPath = null;
 //   private static String m_rootPath = null;

    public CustomConfigurationFactory(){

    }

//    public CustomConfigurationFactory(String rootPath) {
//        m_rootPath = rootPath;// profilePath + "conf" + File.separator + "conf.ini";
//    }

    private static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        Common C				= new Common();
        String rootPath         = C.Get_RootPathForJava();
        Profile profile 		= new Profile(rootPath + "conf" + File.separator + "conf.ini");

        String logPath 			= profile.getString("LOG", "PATH", "logs");
        String async			= profile.getString("LOG", "ASYNC", "F");
        String rollingSize		= profile.getString("LOG", "ROLLING_SIZE", "10M");
        String logLevel         = profile.getString("LOG","LEVEL","ERROR");

        if("T".equalsIgnoreCase(async)) {
            System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
            LogManager.setFactory(new Log4jContextFactory());
        }

        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.WARN);
        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).
                addAttribute("level", Level.DEBUG));

        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
//        appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY,
//            Filter.Result.NEUTRAL).addAttribute("marker", "FLOW"));
        builder.add(appenderBuilder);


        // create a rolling file appender
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n");

        ComponentBuilder policyComponent = builder.newComponent("Policies")
//    	    .addComponent(builder.newComponent("CronTriggeringPolicy")
//    	    		.addAttribute("schedule", scheduler))
                .addComponent(builder.newComponent("OnStartupTriggeringPolicy"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", rollingSize))
                .addComponent(builder.newComponent("TimeBasedTriggeringPolicy")
                        .addAttribute("interval", "1")
                        .addAttribute("modulate", true));
//
//    	ComponentBuilder rolloverStrategy = builder.newComponent("DefaultRolloverStrategy");
//    			.addAttribute("fileIndex", "nomax")
//    			.addAttribute("min", "0")
//    			.addAttribute("max", "10000");
//
////
//    	ComponentBuilder deleteComponent = builder.newLayout("Delete")
//				.addAttribute("basePath", logPath)
//				.addAttribute("maxDepth", "1")
//		   .addComponent(builder.newComponent("IfFileName")
//		   	    .addAttribute("glob", "*/??????_*.log")
//		   	    	.addComponent(builder.newComponent("IfLastModified")
//		   	    			.addAttribute("age", expire)));
////		   .addComponent(builder.newComponent("IfAccumulatedFileCount")
////			   .addAttribute("exceeds","2"));
////
////
////
//     	rolloverStrategy.addComponent(deleteComponent);
//


        appenderBuilder = builder.newAppender("rolling", "RollingFile")
//    			.addAttribute("filename",  logPath + File.separator + C.getToday("yyyyMMdd") + ".log")
//        		.addAttribute("filePattern", logPath + File.separator + "%d{yyyyMMdd}_%i.log")
                .addAttribute("filename", rootPath + logPath + File.separator +"console.log")
                .addAttribute("filePattern",rootPath + logPath + File.separator + "%d{yyyyMMdd}_%i.log")
                .addAttribute("immediateFlush", true)
                .add(layoutBuilder)
                .addComponent(policyComponent);
//    	    .addComponent(rolloverStrategy);

        builder.add(appenderBuilder);



        builder.add(builder.newLogger("org.apache.logging.log4j", Level.DEBUG)
                .add(builder.newAppenderRef("rolling"))
                .add(builder.newAppenderRef("Stdout"))
                .addAttribute("additivity", false));

//
        builder.add(builder.newRootLogger(Level.TRACE)
                .add(builder.newAppenderRef("rolling").addAttribute("level", Level.getLevel(logLevel)))
                .add(builder.newAppenderRef("Stdout").addAttribute("level", Level.TRACE)));

        return builder.build();

    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}