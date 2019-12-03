package com.woonam.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.woonam.connect.AgentConnect;
import com.woonam.model.GetModel;
import com.woonam.chiper.ARIAChiper;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import com.woonam.wdms.WdmFile;
import com.woonam.xvarm.XvarmFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/DownloadImage.do"})
public class DownloadImageController extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private Common m_C 					= null;
	private String m_RootPath 				= null;
	private String m_ConfPath				= null;
	private GetModel m_GM					= null;
	private AgentConnect m_AC			= null;
	private static Logger logger = LogManager.getLogger("Woonam");
	
	public DownloadImageController() {
		this.m_C 					= new Common();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		this.m_RootPath			= m_C.getRootPath(getServletContext());
		this.m_ConfPath 			= m_RootPath + File.separator +"conf" + File.separator + "conf.ini";
		
		HttpSession session		= req.getSession();
		Profile profile				= new Profile(m_ConfPath);

		String strImgType 	= req.getParameter("ImgType");
		String strDocIRN 		= req.getParameter("DocIRN");
		String strIdx 			= req.getParameter("Idx");
		String degree			= req.getParameter("degree");
		String strUserID 		= session.getAttribute("USER_ID") == null ? null : (String)session.getAttribute("USER_ID");
		String strCoCD 		= session.getAttribute("CORP_NO") == null ? null : (String)session.getAttribute("CORP_NO");
		
		int nDegree 			= 0;
		
		
		//인젝션 검사
	   	if(m_C.IsInjection(req.getParameterMap()))
	   	{
	 		logger.error("Injection detected.");
	 		resp.getWriter().print(m_C.writeResultMsg("F", "INJECTION_DETECTED"));
			return;
	   	}
		
	   	try {
	   		nDegree  = Integer.parseInt(degree);
	   	}
	   	catch(Exception e) {
	   		nDegree = 0;
	   	}
	   	
		String strTable = "IMG_SLIP_X";
		
		if(m_C.isBlank(strIdx))
		{
			strIdx = "1";
		}
		
		if("thumb".equalsIgnoreCase(strImgType))
		{
    		strTable = "IMG_SLIP_M";
		}
		
		this.m_AC = new AgentConnect(profile);
		this.m_GM 	= new GetModel(m_AC, session);
		
		//Check user info before download image
	/*	JsonObject objUserInfo = m_GM.getUserInfo(strUserID, strCoCD);
		if(objUserInfo == null || objUserInfo.size() <= 0)
		{
			return;
		}*/
		
		resp.setHeader("Content-Type","image/jpeg");
		resp.setHeader("Content-Transfer-Encoding", "binary;");
		resp.setHeader("Pragma", "no-cache;");
		resp.setHeader("Expires", "-1;"); 
		
		String strEDMS		=	profile.getString("AGENT_INFO", "EDMS", "");
		
		BufferedInputStream fin 		= 	null;
        BufferedOutputStream outs 	=	null;
        ByteArrayOutputStream baos 	= null;
    	try 
    	{     
    		byte[] bImage	=	 null;
        	
        	if("XVARM".equals(strEDMS))
        	{
        		XvarmFile slipimg			=	new XvarmFile(profile);
        		bImage	=	slipimg.Download(strDocIRN, strIdx, strTable);
        	}
        	else
        	{
        		WdmFile slipimg			=	new WdmFile(profile);
        		bImage	=	slipimg.Download(strDocIRN, strIdx, strTable);
        	}
    	         
        	ARIAChiper aria = new ARIAChiper(m_ConfPath);
       	   	bImage = aria.ARIA_Decode(bImage);
        	
    	    
    	   baos = new ByteArrayOutputStream();
    	   ImageIO.write(m_C.Rotate_Image(ImageIO.read(new ByteArrayInputStream(bImage)), nDegree), "jpg", baos);
    	   
    	   bImage = baos.toByteArray();
    	   byte b[] 		= new byte[bImage.length];
    	   
           fin 				= new BufferedInputStream(new ByteArrayInputStream(bImage));
           
           outs 			= new BufferedOutputStream(resp.getOutputStream());
         
           int read = 0;
           while ((read = fin.read(b)) != -1)
           {
               outs.write(b,0,read);
           }
    	} 
    	catch ( Exception e )  
    	{ 
    		logger.error("DownloadImage", e);
    	} 
    	
    	finally
    	{      
    		if(outs != null) 	try{outs.close();}catch(Exception e){ logger.error("Download Image", e); }
    		if(fin != null) 		try{fin.close();}catch(Exception e){ logger.error("Download Image", e); }
    		if(baos != null) 		try{baos.close();}catch(Exception e){ logger.error("Download Image", e); }
    	}
	}
}
