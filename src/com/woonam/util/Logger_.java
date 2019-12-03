//package com.woonam.util;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//
//public class Logger_ extends SecurityManager {
//
//	private static int m_nLogLevel 				= 9;
//	private static String m_strLogPath 			= null;
//	private static boolean m_isDebugMode 	= false;
//	private static boolean m_isInitCompleted = false;
//	private static Common m_C	= null;
//	private static Logger logger = LogManager.getLogger("Woonam");
//
//	public static boolean init(Profile profile)
//    {
//		logger.fatal("fatal");
//		logger.info("info");
//		logger.debug("debug");
//		logger.error("error");
//
//		m_C = new Common();
//		if(profile != null)
//		{
//			m_isDebugMode 	= "FALSE".equalsIgnoreCase(profile.getString("WAS_INFO", "DEBUG", "FALSE")) ? false : true;
//			m_strLogPath 		= profile.getString("WAS_INFO", "LOG_PATH", "");
//			try
//			{
//				m_nLogLevel	= Integer.parseInt(profile.getString("AGENT_INFO", "LOG_LEVEL", "9"));
//			}
//			catch(Exception e)
//			{
//				m_nLogLevel	= 9;
//			}
//		}
//
//		if(m_C.isBlank(m_strLogPath))
//		{
//			SimpleDateFormat 	sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//
//			StringBuffer sbMsg = new StringBuffer();
//			sbMsg.append("[" + sdfTime.format(new Date()) + "]");
//			sbMsg.append(" ");
//			sbMsg.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//			sbMsg.append(" : ");
//			sbMsg.append("Logger path is not specified.");
//
//			System.err.println(sbMsg.toString());
//
//			return false;
//		}
//
//		/*if(m_strLogPath.charAt(m_strLogPath.length()-1) != File.separatorChar)
//		{
//			m_strLogPath += File.separator;
//		}*/
//
//		m_isInitCompleted = true;
//
//		return m_isInitCompleted;
//    }
//
//
//	private static String getLogFilePath()
//	{
//		String strRes = null;
//		SimpleDateFormat 	sdfDate = new SimpleDateFormat("yyyyMMdd");
//		SimpleDateFormat 	sdfHour = new SimpleDateFormat("yyyyMMdd-HH");
//
//		Date date = new Date();
//
//		String strLogPath = m_strLogPath + File.separator  + sdfDate.format(date) + ".log" ;
//
//		try
//		{
//			File logFile = new File(strLogPath);
//	        if(!logFile.exists())
//	        {
//	        	File parentFile = logFile.getParentFile();
//	        	if(!parentFile.exists())
//	        	{
//		        	if(parentFile.mkdirs())
//		        	{
//		        		logFile.createNewFile();
//		        	}
//		        	else
//		        	{
//		        		 throw new IOException("Failed to create directory " + logFile.getParent());
//		        	}
//	        	}
//	        	else
//	        	{
//	        		logFile.createNewFile();
//	        	}
//	        }
//
//	        strRes = strLogPath;
//		}
//		catch (Exception e)
//		{
//			SimpleDateFormat 	sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//
//			StringBuffer sbMsg = new StringBuffer();
//			sbMsg.append("[" + sdfTime.format(new Date()) + "]");
//			sbMsg.append(" ");
//			sbMsg.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//			sbMsg.append(" : ");
//			sbMsg.append("Failed save log.\n");
//			sbMsg.append(strLogPath);
//
//			System.err.println(sbMsg.toString());
//		}
//
//		return strRes;
//	}
//
//    public static void writeException(String msg, Exception e, int nLevel)
//	{
//    	if(m_nLogLevel >= nLevel)
//		{
//			String strLogPath = getLogFilePath();
//			//Write sysout if logpath is not specified.
//			if(m_C.isBlank(strLogPath) || !m_isInitCompleted)
//			{
//				SimpleDateFormat 	sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//
//				StringBuffer sbMsg = new StringBuffer();
//				sbMsg.append("[" + sdfTime.format(new Date()) + "]");
//				sbMsg.append(" ");
//				sbMsg.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//				sbMsg.append(" : ");
//				sbMsg.append("Logger path is not specified.");
//
//				System.err.println(sbMsg.toString());
//	    		return;
//			}
//
//			BufferedWriter bw = null ;
//			SimpleDateFormat	 sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//            StringBuffer sbOutput = new StringBuffer();
//			try
//			{
//				StringWriter errors = new StringWriter();
//				e.printStackTrace(new PrintWriter(errors));
//
//	            bw = new BufferedWriter(new FileWriter(strLogPath, true));
//
//	            sbOutput.append("\n");
//	            sbOutput.append("[" + sdfTime.format(new Date()) + "]");
//	            sbOutput.append(" ");
//	            sbOutput.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//	            sbOutput.append(" : ");
//	            sbOutput.append(msg);
//	            sbOutput.append("\n");
//	            sbOutput.append(errors.toString());
//
//	            bw.write(sbOutput.toString());
//	            bw.close();
//	            bw = null;
//
//	    		if(m_isDebugMode)
//	    		{
//	    			System.out.println(sbOutput.toString());
//	    		}
//	        }
//	        catch(Exception ex) {
//
//	        	StringBuffer sbMsg = new StringBuffer();
//	        	sbOutput.append("[" + sdfTime.format(new Date()) + "]");
//	            sbOutput.append(" ");
//	            sbOutput.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//	            sbOutput.append(" : ");
//	            sbOutput.append("Failed save log.\n");
//				sbMsg.append(m_strLogPath);
//
//				System.err.println(sbMsg.toString());
//				ex.printStackTrace();
//
//	        }
//			finally
//			{
//				if(bw != null)
//				{
//					try {
//						bw.close();
//					} catch (Exception e2) {
//						e2.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//
//
//    public static void write(String msg, int nLevel)
//    {
//    	if(m_nLogLevel >= nLevel)
//    	{
//    		String strLogPath = getLogFilePath();
//			//Write sysout if logpath is not specified.
//			if(m_C.isBlank(strLogPath) || !m_isInitCompleted)
//			{
//				SimpleDateFormat 	sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//
//				StringBuffer sbMsg = new StringBuffer();
//				sbMsg.append("[" + sdfTime.format(new Date()) + "]");
//				sbMsg.append(" ");
//				sbMsg.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//				sbMsg.append(" : ");
//				sbMsg.append("Logger path is not specified.");
//
//				System.err.println(sbMsg.toString());
//	    		return;
//			}
//
//			BufferedWriter bw = null ;
//    		SimpleDateFormat 	sdfTime = new SimpleDateFormat("HH:mm:ss:SSS");
//    		StringBuffer sbOutput = new StringBuffer();
//
//	    	try {
//	    		bw = new BufferedWriter(new FileWriter(strLogPath, true));
//
//	    		sbOutput.append("[" + sdfTime.format(new Date()) + "]");
//	            sbOutput.append(" ");
//	            sbOutput.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//	            sbOutput.append(" : ");
//	            sbOutput.append(msg);
//
//	            bw.write(sbOutput.toString());
//	            bw.newLine();
//	            bw.close();
//	            bw = null;
//
//	    		if(m_isDebugMode)
//	    		{
//	    			System.out.println(sbOutput.toString());
//	    		}
//	        }
//	        catch(Exception e) {
//	        	StringBuffer sbMsg = new StringBuffer();
//	        	sbOutput.append("[" + sdfTime.format(new Date()) + "]");
//	            sbOutput.append(" ");
//	            sbOutput.append(SecurityManagerMethod.mySecurityManager.getCallerClassName(2));
//	            sbOutput.append(" : ");
//	            sbOutput.append("Failed save log.\n");
//				sbMsg.append(m_strLogPath);
//
//				System.err.println(sbMsg.toString());
//				e.printStackTrace();
//	        }
//	    	finally
//			{
//				if(bw != null)
//				{
//					try {
//						bw.close();
//					} catch (Exception e2) {
//						e2.printStackTrace();
//					}
//				}
//			}
//    	}
//    }
//
//    //Get current class name
//    public static class SecurityManagerMethod {
//
//    	public final static MySecurityManager mySecurityManager = new MySecurityManager();
//
//        public String  getCallerClassName(int callStackDepth) {
//            return mySecurityManager.getCallerClassName(callStackDepth);
//        }
//
//        /**
//         * A custom security manager that exposes the getClassContext() information
//         */
//        public static class MySecurityManager extends SecurityManager {
//            public String getCallerClassName(int callStackDepth) {
//                return getClassContext()[callStackDepth].getName();
//            }
//        }
//    }
//
//}