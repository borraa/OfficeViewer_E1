package com.woonam.recycle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woonam.connect.AgentConnect;
import com.woonam.model.GetModel;
import com.woonam.model.SetModel;
import com.woonam.template.Order;
import com.woonam.template.Order_Eng;
import com.woonam.template.TemplateImpl;
import com.woonam.template.UploadSlip;
import com.woonam.util.Common;
import com.woonam.util.Profile;

@WebServlet(urlPatterns = {"/Recycle.do"})
public class Recycle extends HttpServlet{
	
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
	
	public Recycle() {
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
		m_AC 						= new AgentConnect(profile);
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
		
		
//		String userID = req.getParameter("USER_ID") != null ? req.getParameter("USER_ID").toString() : session.ge ;
//		String corpNo = req.getParameter("CORP_NO").toString();
		
		this.m_GM = new GetModel(m_AC, session); 
		this.m_SM = new SetModel(m_AC, session);
		
		JsonArray ar_targetList = m_GM.Get_RecycleTargetList();
		
		ArrayList<String> listFailed = new ArrayList<>();
		if(ar_targetList != null) {
			for(int i = 0; i < ar_targetList.size(); i++) {
				
				JsonObject objItem = ar_targetList.get(i).getAsJsonObject();
				
				String ptiKey 	= objItem.get("PTI_KEY").getAsString();
			
				if(!Upload_Order(ptiKey, profile)) {
					listFailed.add(ptiKey);
				}
			}
			

			StringBuffer sbFailed = new StringBuffer();
			for(int i = 0 ; i < listFailed.size(); i++) {
				if(i == 0) {
					sbFailed.append("Failed list\n");
				}
				sbFailed.append(listFailed.get(i));
				sbFailed.append("\n");
			}
			
			out.print(sbFailed.toString());
		}
		else {
			out.print("No target.");
		}
		return;
	}
	
	private boolean Upload_Order(String ptiKey, Profile profile) {
		
		HashMap<String, String[]> mapVals = new HashMap<>();
		String[] key = {ptiKey};
		mapVals.put("APPR_NO", key);
		
		JsonObject obj_orderInfo = m_GM.Get_orderConverList(mapVals);
		
		if(obj_orderInfo == null || obj_orderInfo.size() <= 0) {
			return false;
		}
		
		String itemKey = obj_orderInfo.get("ItemJoinKey").getAsString();
		HashMap mapParamVals = new HashMap<String, String[]>();
		String[] tempKey = {itemKey};
		mapParamVals.put("ITEM_KEY", tempKey);
		
		JsonArray ar_orderItemList = m_GM.Get_orderItemConverList(mapParamVals);
		
		// 이미지 생성
		String vdCD = obj_orderInfo.get("VD_CD").getAsString();
		TemplateImpl orderForm =  null;
		
		String workBg = null;
		
		if("2".equals(vdCD.charAt(0)+"")) {
			workBg =  "ORDER_ENG_BG";
			orderForm = new Order_Eng(profile);
		}
		else {
			workBg =  "ORDER_BG";
			orderForm = new Order(profile);
		}
		
		// Set work path
		StringBuffer sbWorkPath = new StringBuffer();
		sbWorkPath.append(m_C.Get_RootPathForJava() + profile.getString("WAS_INFO", "TEMP_DIR",  "temp"));
		sbWorkPath.append(m_C.getIRN(""));
		sbWorkPath.append("system_recycle");
		sbWorkPath.append(m_C.getToday("yyyyMMddHHmmssSSS"));
				
		obj_orderInfo.add("ITEM_LIST", ar_orderItemList);
		
		JsonObject obj_resImage 	= orderForm.run(obj_orderInfo, profile.getString("TEMPLATE", workBg, ""), sbWorkPath.toString());
		if(obj_resImage == null || obj_resImage.size() <= 0) {
			return false;
		}
		obj_resImage.addProperty("SDOC_NAME", "발주서");
	
		UploadSlip upload = new UploadSlip(profile);
		
		HashMap<String, String[]> mapUser = new HashMap<>();
		String[] userId = {"I21257"};
		mapUser.put("USER_ID", userId);
		String[] corpNo = {"1000"};
		mapUser.put("CORP_NO", corpNo);
	
		
		JsonObject objRes = upload.run(obj_resImage, mapUser, sbWorkPath.toString());
		
		
		String strRes = objRes.get("result").getAsString();
		
		if("T".equalsIgnoreCase(strRes)) {

			m_SM.Update_orderStatus(ptiKey, "10");
			
			return true;
		}
		else {
			return false;
		}
	}
}


