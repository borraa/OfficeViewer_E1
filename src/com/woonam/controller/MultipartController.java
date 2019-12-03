package com.woonam.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.woonam.connect.AgentConnect;
import com.woonam.model.GetModel;
import com.woonam.model.SetModel;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

@WebServlet(urlPatterns = {"/MultipartCommand.do"})
public class MultipartController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Common m_C 					= null;
	private String m_RootPath 				= null;
//	private boolean isInitCompleted 		= false;
	private GetModel m_GM					= null;
	private SetModel m_SM					= null;
	private AgentConnect m_AC			= null;
	
	enum ENUM_COMMAND {
		GET_HISTORY_LIST,
	}
	
	public MultipartController() {
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
		Profile profile				= new Profile(m_RootPath + File.separator +"conf" + File.separator + "conf.ini");
	//	this.isInitCompleted		= setLogger(profile);
		//Check logger init result.
//		if(!m_C.setLogger(profile, this.m_RootPath))
//		{
//			RequestDispatcher rd = req.getRequestDispatcher("slip_error.jsp?ERR_MSG=FAILED_LOGGER_INIT");
//	  	 	rd.forward(req,resp);
//	  	 	return;
//		}
		
		HttpSession session = req.getSession();
		PrintWriter out			= resp.getWriter();

		ArrayList<FileItem> al_file = new ArrayList<>();
		Map<String, String[]> mapParams = new HashMap<>();
		try {

    		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        	if(isMultipart)
        	{
        		 List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(new ServletRequestContext(req));
        		
        		for (FileItem item : multiparts) {
        			
        			if (item.isFormField()) { // Check regular field.
        				
        				String value = URLDecoder.decode(item.getString().replaceAll("(\\r|\\n)", ""),"UTF-8");
        				mapParams.put(item.getFieldName(), value.split(""));
        			}
        			else {
        				al_file.add(item);
        			}
        		}

        		String strCommand			= m_C.getParamValue(mapParams, "Command", null);
        		String strTitle			= m_C.getParamValue(mapParams, "TITLE", null);
        		

        		System.out.println(strCommand);
        		System.out.println(strTitle);
        		
        		for(int i = 0; i < al_file.size(); i++) {
        			String fileName = al_file.get(i).getName();
        			long fileSize = al_file.get(i).getSize();
        			System.out.println(fileName);
        			System.out.println("Size : " + fileSize);
        			
        			byte[] bItem = al_file.get(i).get();
        			
        			System.out.println(bItem.length);
        		}
        		String inputName = null;
//    			for (FileItem item : multiparts) {
//    				
//    				
//    				if(!item.isFormField())
//    				{
//    					if(item.getSize() > 0)
//    					{
//    						al_file.add(item);
//    					}
//    				}
//    			}
        	}
		}
		catch(Exception e) {
			out.print(m_C.writeResultMsg("F", "SERVER_EXCEPTION"));
		}
//		
//		Map<String, String[]> mapParams = new HashMap<String, String[]>(req.getParameterMap());
//		String strClientIP = m_C.getClientIP(profile, req);
//		String[] arClientIP = {strClientIP};
//		mapParams.put("ClientIP", arClientIP);
//		
//		
//		String strCommand			= m_C.getParamValue(mapParams, "Command", null);
//		
//		//파라미터 검사
//    	if(m_C.isBlank(strCommand))
//		{
//    		Logger.write("Command is null", 5);
//    		out.print(m_C.writeResultMsg("F", "COMMAND_IS_NULL"));
//			return;
//		}
//    	
//    	//인젝션 검사
//	   	if(m_C.IsInjection(req.getParameterMap()))
//	   	{
//	 		Logger.write("Injection detected.", 5);
//    		out.print(m_C.writeResultMsg("F", "INJECTION_DETECTED"));
//			return;
//	   	}
//    	
//		/*
//		 * Logger.write("----------------------------------------", 5);
//		 * Logger.write("ViewerCommand : Command - "+strCommand, 5);
//		 */
//		
//		ENUM_COMMAND EC = null;
//		try
//		{	
//			this.m_AC 	= new AgentConnect(profile);
//			this.m_GM 	= new GetModel(m_AC, session);
//			EC 	= ENUM_COMMAND.valueOf(strCommand);
//			
//			switch(EC)
//			{
//				case GET_HISTORY_LIST : {
//					String[] arrParam = {"KEY"};
//					if(m_C.IsNullParam(arrParam, mapParams))
//					{
//						Logger.write("Parameter of "+strCommand+" is NULL.", 3);
//						out.print(m_C.writeResultMsg("F", "PARAMETER_IS_NULL"));
//						return;
//					}
//					
//					JsonArray arObjRes = m_GM.getHistoryList(mapParams);
//					if(arObjRes == null)
//					{
//						out.print(m_C.writeResultMsg("F", "FAILED_LOAD_HISTORY_LIST"));
//					}
//					else
//					{
//						out.print(arObjRes.toString());
//					}
//				}
//				break;
//			}
//		}
//		catch (Exception e) {
//			out.print(m_C.writeResultMsg("F", "SERVER_EXCEPTION"));
//		}
		
		return;
	}
}
