package com.woonam.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.woonam.model.GetModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Common {

	/**
	 * Get result query count
	 * @param str
	 * @return
	 */

	private static Logger logger = LogManager.getLogger("Woonam");

	public int getResCnt(String str)
	{
		int nResCnt = 0;
		try
		{
			nResCnt = Integer.parseInt(str.replaceAll("\\D+",""));
		}
		catch(NumberFormatException e)
		{
			nResCnt = 0;
		}
		
		return nResCnt;
	}
	
	public  BufferedImage Rotate_Image(BufferedImage src, double angle) {
		
		if(angle == 0) {
	    	return src;
	    }
		
		double theta = (Math.PI * 2) / 360 * angle;
	    int width = src.getWidth();
	    int height = src.getHeight();
	    BufferedImage dest;
	    if (angle == 90 || angle == 270) {
	        dest = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
	    } else {
	        dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
	    }

	    Graphics2D graphics2D = dest.createGraphics();

	    if (angle == 90) {
	        graphics2D.translate((height - width) / 2, (height - width) / 2);
	        graphics2D.rotate(theta, height / 2, width / 2);
	    } else if (angle == 270) {
	        graphics2D.translate((width - height) / 2, (width - height) / 2);
	        graphics2D.rotate(theta, height / 2, width / 2);
	    } else {
	        graphics2D.translate(0, 0);
	        graphics2D.rotate(theta, width / 2, height / 2);
	    }
	    graphics2D.drawRenderedImage(src, null);
	    return dest;
	}
	
    public int pixel2pt(int pixel, int dpi) {

        return  ( pixel * 720 ) / dpi;
    }
    
	
	public byte[] hexStringToByteArray(String s) {
		int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                + Character.digit(s.charAt(i + 1), 16));
	    }
	    return data;
	}
	
	//파일 리네임
	public boolean ReNameFile(String strFilePath, String strOldFileName)
	{	
		File file	=	new File(strOldFileName);
		File file2	=	new File(strFilePath);

		try
		{
		if(file.exists())
		{
			if(!file.renameTo(file2))
			{
				//if (logLevel >= 7) logger.write("파일명변경 실패 : "+strOldFilePath+"->"+strNewFilePath);	
			//.out.println("파일명바꾸기실패.");
			}
			else
			{
				//if (logLevel >= 7) logger.write("파일명변경 성공 : "+strOldFilePath+"->"+strNewFilePath);	
			//	System.out.println("파일명바꾸기성공.");
				return true;
			}
		}
		else
		{
			//if (logLevel >= 7) logger.write("해당 파일이 존재하지않습니다.");
		//	System.out.println("해당 파일이 존재하지않습니다.");
		}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		return false;
	}
	
	/**
	 * Root 경로 가져옴.
	 * @param ctx
	 * @param
	 * @return
	 */
	
	public String getRootPath(ServletContext ctx)
	{
		StringBuffer sbRootPath 	= new StringBuffer();
		StringBuffer sbTempPath 	= new StringBuffer();
		String strRealPath				= ctx.getRealPath("/"); 
	
		boolean bWindows 	= false; 
	    String strOS 			= System.getProperty("os.name"); 
	    
	    if(strOS.startsWith("Windows"))	bWindows = true; 
	    else                                  	bWindows = false; 
	     
	   int 	nMetaDataIdx	=	strRealPath.indexOf(".metadata");
		if(nMetaDataIdx > -1)
		{
			sbTempPath.append(strRealPath.substring(0, nMetaDataIdx));
			String strContextName		=	ctx.getContextPath();
			strContextName				=	strContextName.replaceAll("/", "");
			int nContextIdx				=	strRealPath.lastIndexOf(strContextName);
			if(nContextIdx > -1)
			{
				sbTempPath.append(	strRealPath.substring(nContextIdx));
				sbTempPath.append(	"WebContent");
			}
			sbRootPath	=	sbTempPath;
		}
		else
		{
			strRealPath				=	strRealPath.replaceAll("%20", " ");
		//	int nSubStart	= 0;
		/*	 if(bWindows)  
	         { 
				 nSubStart = 1;
	         } 
	         else 
	         { 
	        	 nSubStart = 0;
	         }*/
			
			 int nIdx        	= strRealPath.indexOf("WEB-INF"); 
			 if(nIdx == -1)
			 {
				 nIdx			= strRealPath.length();
			 }
			 	 
			 sbRootPath.setLength(0);
			 sbRootPath.append(strRealPath.substring(0, nIdx));
			
			/*if(sbRootPath.toString().lastIndexOf('/') < sbRootPath.length() -1)
			{	
				sbRootPath.append("/");
			}*/
		
			sbRootPath.append("");
		}
		return sbRootPath.toString();
	}
	
	/**
	 * Root 경로 가져옴.
	 * @return
	 */
		
	public String Get_RootPathForJava()
	{
		StringBuffer sbRootPath 	= new StringBuffer();
		StringBuffer sbTempPath 	= new StringBuffer();
		String strRealPath				= this.getClass().getResource("/").getPath();
	
		boolean bWindows 	= false; 
	    String strOS 			= System.getProperty("os.name"); 
	    
	    if(strOS.startsWith("Windows"))	bWindows = true; 
	    else                                  	bWindows = false; 
	     
		int	nMetaDataIdx	=	strRealPath.indexOf(".metadata");
		if(nMetaDataIdx > -1)
		{
			int nIdx	=	strRealPath.lastIndexOf("WEB-INF") - 1;
		
			sbTempPath.append(strRealPath.substring(0, nMetaDataIdx));
			
			String strContextName		=	strRealPath.substring(0,nIdx);
			nIdx								=	strContextName.lastIndexOf("/") + 1;
			strContextName				=	strContextName.substring(nIdx);
			int nContextIdx				=	strRealPath.indexOf(strContextName);
			if(nContextIdx > -1)
			{
				sbTempPath.append(strContextName);
				sbTempPath.append(	"/WebContent");
			}
			sbRootPath	=	sbTempPath;
		}
		else
		{
			strRealPath				=	strRealPath.replaceAll("%20", " ");
			int nSubStart	= 0;
			 if(bWindows)  
	         { 
				 nSubStart = 1;
	         } 
	         else 
	         { 
	        	 nSubStart = 0;
	         }
			
			 int nIdx        	= strRealPath.indexOf("WEB-INF"); 
			 if(nIdx == -1)
			 {
				 nIdx			= strRealPath.length();
			 }
			 	 
			 sbRootPath.setLength(0);
			 sbRootPath.append(strRealPath.substring(nSubStart, nIdx));
			
		/*	if(sbRootPath.toString().lastIndexOf('/') < sbRootPath.length() -1)
			{	
				sbRootPath.append("/");
			}*/
		
			sbRootPath.append("");
		}
		return sbRootPath.toString();
	}
	
	public String getEncodedFileName(HttpServletRequest request, String strFileName)
	{
        String header 					= request.getHeader("User-Agent");
        String res					 	= null;
        String strBrowser 				= null;
        
        if (header.indexOf("MSIE 10") > -1) 
        {
        	strBrowser = "MSIE10";
        } 
        else if (header.indexOf("Trident") > -1) 
        {
        	strBrowser = "MSIE11";
        }
        else if (header.indexOf("Chrome") > -1) 
        {
        	strBrowser = "Chrome";
        } 
        else if (header.indexOf("Opera") > -1)
        {
        	strBrowser=  "Opera";
        }
		else if (header.indexOf("Safari") > -1)
		{
			strBrowser = "Safari";
	    }
		else
        {
			strBrowser = "Firefox";
        }
     
        try 
        {
        	switch(strBrowser.toUpperCase()) {
        	case "MSIE10" :
        			res = URLEncoder.encode(strFileName, "UTF-8").replaceAll("\\+", "%20");	
        		break;
        	case "MSIE11" :
        			res = URLEncoder.encode(strFileName, "UTF-8").replaceAll("\\+", "%20");
        		break;
        	case "FIREFOX" :
        			res = new String(strFileName.getBytes("UTF-8"), "ISO-8859-1");
        		break;
        	case "OPERA" :
        			res = "\"" + new String(strFileName.getBytes("UTF-8"), "8859_1") + "\"";
        		break;
        	case "CHROME" :
        			StringBuffer sb = new StringBuffer(); 
    	            for (int i = 0; i < strFileName.length(); i++)
    	            {
    	                char c = strFileName.charAt(i);
    	                if (c > '~')
    	                {
    	                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
    	                } 
    	                else
    	                {
    	                    sb.append(c);
    	                }
    	            }
    	            res = sb.toString();
        		break;
        		default : 
        			 res = new String(strFileName.getBytes("UTF-8"), "8859_1");
        			break;
        	}
     
        } 
        catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("Download Attach - failed get file name", e);
		}
        return res;
    }
	
	//포맷대로 현재 날짜부르기
	public String getToday(String strFormat)
	{
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(strFormat, Locale.KOREA);
		String today = formatter.format(dateNow);	
			
		return today;
	}
	
	/**
	 * get new DOC_IRN
	 * @param strFlag
	 * @return
	 */
	public String getIRN(String strFlag) {
		
		String strUUID = UUID.randomUUID().toString();
		
		StringBuffer sbRes = new StringBuffer();
		sbRes.append(strFlag);
		sbRes.append(getToday("yyyyMMdd"));
		sbRes.append(strUUID.substring(strUUID.length() - 5, strUUID.length()));
		sbRes.append(getToday("HHmmssSSS"));
		
		return sbRes.toString();
	}
	
	/**
	 * Null 체크
	 * @param str
	 * @return
	 */
	public boolean isBlank(String str)
	{
		if(str == null || str.replaceAll("\\p{Z}", "").length()<=0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 파라미터 값이 쿠키에 저장되어있는지 확인. 
	 * @param cookie
	 * @param strParam
	 * @return 있으면 String 리턴. 없으면 ""(Not null)
	 */
	public String FindParameterFromCookie(HttpServletResponse response, CookieUtil cookie, String strParam)
	{
		String strRes = "";
		try
		{
	    	if(cookie.exists(strParam))
	    	{
	    		strRes = cookie.getValue(strParam);
	    		response.addCookie(cookie.deleteCookie(strParam));
	    	}
		}
		catch (Exception e) {
			logger.error("Loading Cookie : ", e);
		}
    	return strRes;
	}
	
	/**
	 * 파라미터 값이 쿠키에 저장되어있는지 확인. 
	 * @param cookie
	 * @param strParam
	 * @return 있으면 String 리턴. 없으면 ""(Not null)
	 */
	public String FindParameterFromCookie(HttpServletResponse response, CookieUtil cookie, String strParam, String strDefVal)
	{
		String strRes = strDefVal;
		try
		{
	    	if(cookie.exists(strParam))
	    	{
	    		strRes = cookie.getValue(strParam);
	    		response.addCookie(cookie.deleteCookie(strParam));
	    	}
		}
		catch (Exception e) {
			logger.error("Loading Cookie : ", e);
		}
    	return strRes;
	}
	/**
	 * Check user permission.
	 * @param GM
	 * @param session
	 * @return
	 */
	public boolean chk_UserPermission(GetModel GM, HttpSession session) {
		
		boolean bRes = false;
		try
		{
			String strUserID 	= session.getAttribute("USER_ID").toString();
			String strCorpNo 	= session.getAttribute("CORP_NO").toString();
			String strLang 		= session.getAttribute("USER_LANG").toString();
			
			JsonObject objUserInfo = GM.getUserInfo(strUserID, strCorpNo, strLang);
			
			String resUserID = objUserInfo.get("USER_ID").getAsString();
			
			if(!isBlank(resUserID)) bRes = true;
		}
		catch(Exception e)
		{
			logger.error("Failed check usre permission.", e);
			bRes = false;
		}
		
		return bRes;
	}
	
	/**
	 * Get IP
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public String GetIP(Profile profile) throws Exception
	{
		String strServerFlag	= profile.getString("AGENT_INFO", "SERVER", "PRD");
		String strServerIP		= profile.getString(strServerFlag,"IP","");
		
		String strRes	= "";
		//IP구하기
	
		InetAddress giriAddress 		= null;
		try
		{
			giriAddress = java.net.InetAddress.getByName(strServerIP);
			strRes = giriAddress.getHostAddress();
		}
		catch (UnknownHostException e)
		{
		//	Logger.writeException("Failed To Make new DocIRN : ",e , 3);
			throw e;	
		}
		return strRes;
	}
	
	/**
	 * 특수문자 변환
	 */
	public String ConvertSpecialChar(String str)
	{
		str = str.replaceAll("\\n", "&ltbr&gt");
		str = str.replaceAll(">", "〉");
		str = str.replaceAll("<", "〈");
		str = str.replaceAll("'", "''");
		str = str.replaceAll("!", "！");
		str = str.replaceAll("%", "％");
		str = str.replaceAll("&gt", "〉");
		str = str.replaceAll("&lt", "〈");
		str = str.replaceAll("&amp", "［");
		str = str.replaceAll("&ampquot", "］");
		str = str.replaceAll("&", "＆");
		str = str.replaceAll("'", "’");
//		str = str.replaceAll("-", "­");
		str = str.replaceAll("\\$", "＄");
		str = str.replaceAll("#", "＃");
		
		return str;
	}
	
	/**
	 * 인젝션 검사
	 * @param
	 * @return
	 */
	public boolean IsInjection(String str) {
		
		str = ConvertSpecialChar(str);
		String[] itemInjection = {
				"'", "--", "|", "\"",  "/", "\\",  ":",  ";", "%", "+", "<", ">", "#","&","(", ")",
				"select","or","union","insert","delete","and","union"
		};
	
		for(int i = 0; i < itemInjection.length; i++)
		{
			if(str.equalsIgnoreCase(itemInjection[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 인젝션 검사
	 * @param map
	 * @return
	 */
	public boolean IsInjection(Map<String, String[]> map) {
		
		for( String strKey : map.keySet() )
		{
			for(String strVal : map.get(strKey))
			{
				
				String[] itemInjection = {
						"'", "--", "|", "\"",  "/", "\\",  ":",  ";", "%", "+", "<", ">", "#","&","(", ")",
						"select","or","union","insert","delete","and","union"
				};
			
				for(int i = 0; i < itemInjection.length; i++)
				{
					if(strVal.equalsIgnoreCase(itemInjection[i]))
					{
						return true;
					}
				}
			//	{
				/*	 strVal.replace("'", "’");
					 strVal.replace("--", "­­");
					 strVal.replace("|", "｜");
					 strVal.replace("\"", "″");
					 strVal.replace("/", "／");
					 strVal.replace("\\", "＼");
					 strVal.replace(":", "：");
					 strVal.replace(";", "；");
					 strVal.replace("%", "％");
					 strVal.replace("+", "＋");
					 strVal.replace("<", "＜");
					 strVal.replace(">", "＞");
					 strVal.replace("#", "＃");
					 strVal.replace("&", "＆");
					 strVal.replace("(", "（");
					 strVal.replace(")", "）");*/
				
				//	 return true;
				//}
			}
        }
		return false;
	}
	
	public String getParamValue(HttpSession session, String strParamName, String strDefaultVal )
	{
		String strRes = strDefaultVal;
		try
		{
			strRes = session.getAttribute(strParamName) == null ? strDefaultVal : session.getAttribute(strParamName).toString();
			
		}
		catch(Exception e)
		{
			strRes = strDefaultVal;
		}
		return strRes;
	}
	
	public String getParamValue(Map<String, String[]> mapParams, String strParamName, String strDefaultVal )
	{
		String strVal = isBlank(parseParameter(mapParams, strParamName)) ? strDefaultVal : parseParameter(mapParams, strParamName);
		if(isBlank(strVal))
		{
			strParamName += "[]";
			strVal = isBlank(parseParameter(mapParams, strParamName)) ? strDefaultVal : parseParameter(mapParams, strParamName);
		}
		return strVal;
	}
	
	public String[] getParamValue(Map<String, String[]> mapParams, String strParamName)
	{
		String[] arVal = null;
		try {
			 arVal = mapParams.get(strParamName);
			if (arVal == null || arVal.length <= 0) {
				strParamName += "[]";
				arVal = mapParams.get(strParamName);
			}

			if (arVal != null) {
				for (int i = 0; i < arVal.length; i++) {
					try {
						arVal[i] = URLDecoder.decode(ConvertSpecialChar(arVal[i]), "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						logger.error("Failed to get parameter", e);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("Failed to get parameter", e);
		}
		return arVal;
	}
	
	private String parseParameter(Map<String, String[]> map, String strParamName)
	{
		StringBuffer sbParam = new StringBuffer();

		if(map.get(strParamName) != null)
		{
			for(String str : map.get(strParamName))
			{
				try {
					str = URLDecoder.decode(ConvertSpecialChar(str), "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sbParam.append(ConvertSpecialChar(str));
			}
		}
		return sbParam.toString();
	}

	/**
	 * Check parameter values
	 */
	public boolean IsNullParam(String[] strKey, Map<String, String[]> mapParams)
	{
		boolean bRes = false;
		for(String key : strKey)
		{
			String[] arrParamVal = mapParams.get(key);
			if(arrParamVal == null)
			{
				key += "[]";
				arrParamVal = mapParams.get(key);
				if(arrParamVal == null)
				{
					return true;
				}
				else {
					for (String s : arrParamVal) {
						if (isBlank(s)) {
							return true;
						}
					}
				}
			}
			else {
				for (String s : arrParamVal) {
					if (isBlank(s)) {
						return true;
					}
				}
			}
		}
		return bRes;
	}
	
	//Write reslut message
	public JsonObject writeResultMsg(String strFlag, String strMessage) 
	{
		JsonObject objRes = new JsonObject();
		objRes.addProperty("RESULT",strFlag);
		objRes.addProperty("MSG", strMessage);
		
		return objRes;
	}
//
//	//Set LogPath
//	public boolean setLogger(Profile profile, String rootPath)
//	{
//		boolean isInitCompleted 	= false;
//		if(!isBlank(rootPath))
//		{
//			profile = new Profile(rootPath + File.separator + "conf" + File.separator + "conf.ini");
//			if(profile != null)
//			{
//				isInitCompleted = Logger_.init(profile);
//			}
//		}
//
//		return isInitCompleted;
//	}
	
	public String getClientIP(Profile profile, HttpServletRequest req)
	{
		String strClientIP				= req.getHeader("X-Forwarded-For");
		if(isBlank(strClientIP))
		{
			strClientIP	= req.getHeader("Proxy-Client-IP");
			if(isBlank(strClientIP))
			{
				strClientIP				= req.getRemoteAddr();
				if(isBlank(strClientIP) || "0:0:0:0:0:0:0:1".equals(strClientIP))
				{
					try {
						strClientIP = GetIP(profile);
					} catch (Exception e) {
						strClientIP = "127.0.0.1";
					}
				}
			}
		}
		
		return strClientIP;
	}
}
