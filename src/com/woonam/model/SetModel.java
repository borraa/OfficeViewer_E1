package com.woonam.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.woonam.connect.AgentConnect;
import com.woonam.constants.Queries;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import com.woonam.wdms.PreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetModel {
	private AgentConnect m_AC 		= null;
	private Profile m_Profile				= null;
	private Common m_C				 	= null;
	private HttpSession session		= null;

	private static Logger logger = LogManager.getLogger("Woonam");
	
	public SetModel(AgentConnect AC, HttpSession session)
	{
		this.m_AC 		= AC;
		this.m_Profile 	= AC.getProfile();
		this.m_C 		= new Common();
		this.session 	= session;
	}
	
	public SetModel(AgentConnect AC)
	{
		this.m_AC 		= AC;
		this.m_Profile 	= AC.getProfile();
		this.m_C 		= new Common();
		this.session 	= null;
	}
	
	public boolean Update_cardStatus(String convertKey, String status) {
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
			
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.UPDATE_CARD_STATUS);
    		pStmt.setString(0, convertKey); 
        	pStmt.setString(1, status);
        	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public boolean Update_reportStatus(String convertKey, String status) {
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
			
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.UPDATE_REPORT_STATUS);
    		pStmt.setString(0, convertKey); 
        	pStmt.setString(1, status);
        	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public boolean Update_taxStatus(String convertKey, String status) {
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
			
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.UPDATE_TAX_STATUS);
    		pStmt.setString(0, convertKey); 
        	pStmt.setString(1, status);
        	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public boolean Update_orderStatus(String convertKey, String status) {
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
			
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.UPDATE_ORDER_STATUS);
    		pStmt.setString(0, convertKey); 
        	pStmt.setString(1, status);
        	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public boolean Add_History(String sdocNo, String historyCode, String corpNo, String userId) {
		
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
		
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.HISTORY_ADD);
    		pStmt.setString(0, sdocNo);
        	pStmt.setString(1, "");
        	pStmt.setString(2, "");
        	pStmt.setString(3, historyCode);
        	pStmt.setString(4, corpNo);
        	pStmt.setString(5, userId); 
    	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
		
	}
	
public boolean Copy_Replace(String jdocNo, String sdocNo, String corpNo, String userId) {
		
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
		
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.COPY_REPLACE);
    		pStmt.setString(0, jdocNo);
        	pStmt.setString(1, sdocNo);
        	pStmt.setString(2, corpNo);
        	pStmt.setString(3, userId); 
    	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
		
	}
	
	public boolean Insert_SlipDocFromTemplate(String sdocNo, String corpNo, String partNo, String userID, String sdocKind, String sdocName, int slipCnt, String jdocNo) {
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
		
    	try
    	{
    		
    		PreparedStatement pStmt = new PreparedStatement(Queries.INSERT_SLIPDOC);
    		pStmt.setString(0, sdocNo); // SDOC_NO
        	pStmt.setString(1, corpNo); //CORP_NO
        	pStmt.setString(2, partNo); //PART_NO
        	pStmt.setString(3, userID+""); //REG_USER
        	pStmt.setString(4, m_C.getToday("yyyyMM")); //SDOC_MONTH
        	pStmt.setString(5, sdocKind); //SDOC_KIND
        	pStmt.setString(6, sdocName); //SDOC_NAME
        	pStmt.setString(7, slipCnt+""); //SLIP_CNT
        	pStmt.setString(8, jdocNo); //JDOC_NO
    	
    		String resQuery 	= m_AC.SetData(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public boolean Insert_SlipFromTemplate(JsonObject obj_data, String sdocNo, int slipNo)
	{
		boolean res = false;
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		if (m_AC == null)	return false;
		
		int width 		= obj_data.get("WIDTH").getAsInt();
		int height 		= obj_data.get("HEIGHT").getAsInt();
		int size 			= obj_data.get("SIZE").getAsInt();
		String path 		= obj_data.get("PATH").getAsString();
		String docIRN 	= obj_data.get("DOC_IRN").getAsString();
		String name 	= obj_data.get("NAME").getAsString();
		
		width = m_C.pixel2pt(width, Integer.parseInt(m_Profile.getString("WAS_INFO", "IMG_DPI", "200")));
		height = m_C.pixel2pt(height, Integer.parseInt(m_Profile.getString("WAS_INFO", "IMG_DPI", "200")));
		
    	try
    	{
    		
			PreparedStatement pStmt = new PreparedStatement(Queries.INSERT_SLIP);
    		pStmt.setString(0, m_C.getIRN("")); // SLIP_IRN
        	pStmt.setString(1, docIRN); //DOC_IRN
        	pStmt.setString(2, sdocNo); //SDOC_NO
        	pStmt.setString(3, slipNo+""); //SLIP_NO
        	pStmt.setString(4, size+""); //FILE_SIZE
        	pStmt.setString(5, name); //SLIP_TITLE
        	pStmt.setString(6, "1"); //SLIP_STEP
        	pStmt.setString(7, "1"); //SLIP_FLAG
        	pStmt.setString(8, "0,0,"+width+","+height); //SLIP_RECT
        	pStmt.setString(9, "0"); //SLIP_ROTATE
    	
    		String resQuery 	= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= resQuery.substring(0,1);
			int nResCnt			=  m_C.getResCnt(resQuery);
			if(nResCnt > 0) res = true;
			
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return res;
	}
	
	public int Change_JDocNo(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		int nResCnt	 = -1;
		if (m_AC == null)	return nResCnt;
		
	//	String strLang				= m_C.getParamValue(mapParams, "LANG", "ko");
		String from		= m_C.getParamValue(mapParams, "FROM", null);
		String to			= m_C.getParamValue(mapParams, "TO", null);
		String userID 	= m_C.getParamValue(mapParams, "USER_ID", null);
		String coCD 	= m_C.getParamValue(mapParams, "CORP_NO", null);
		
    	try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.CHANGE_KEY);
    		pStmt.setString(0, from);
        	pStmt.setString(1, to);
        	pStmt.setString(2, coCD);
        	pStmt.setString(3, userID);
    	
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			nResCnt			=  m_C.getResCnt(res);
			
//			if(nResCnt > 0)
//			{
//				bRes = true;
//			}
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
//			bRes = false;
		}
    	
    	return nResCnt;
	}
	
	public boolean removeComment(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;
		
	//	String strLang				= m_C.getParamValue(mapParams, "LANG", "ko");
		String jdocNo		= m_C.getParamValue(mapParams, "KEY", null);
		String comtIRN		= m_C.getParamValue(mapParams, "COMT_IRN", null);
		String corpNo		= m_C.getParamValue(session, "CORP_NO", null);
		String partNo		= m_C.getParamValue(session, "PART_NO", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
    	try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.REMOVE_COMMENT);
    		pStmt.setString(0, jdocNo);
    		pStmt.setString(1, comtIRN);
    		pStmt.setString(2, corpNo);
    		pStmt.setString(3, userID);
    	
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public boolean modifyComment(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;
		
	//	String strLang				= m_C.getParamValue(mapParams, "LANG", "ko");
		String jdocNo		= m_C.getParamValue(mapParams, "KEY", null);
		String title			= m_C.getParamValue(mapParams, "TITLE", null);
		String content		= m_C.getParamValue(mapParams, "CONTENT", null);
		String comtIRN		= m_C.getParamValue(mapParams, "COMT_IRN", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
    	try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.MODIFY_COMMENT);
    		pStmt.setString(0, title);
    		pStmt.setString(1, content);
    		pStmt.setString(2, comtIRN);
    		pStmt.setString(3, jdocNo);
    		pStmt.setString(4, userID);
    	
    		String res 			= m_AC.SetData(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public boolean Remove_byKind(Map<String, String[]> mapParams) {
		
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;

//		@JdocNo			varchar(70),							-- w
//		@IsSystem		char(1),		-- 0 or 1				-- 시스템으로 생성된 증빙의 여부
//		@SdocKind		varchar(4),								-- 삭제하기 위한 증빙 종류 코드
//		@NotJdocNo		varchar(max) = null		
		
		String key		= m_C.getParamValue(mapParams, "KEY", "");
		String isSystem		= m_C.getParamValue(mapParams, "IS_SYSTEM", "0");
		String kind		= m_C.getParamValue(mapParams, "KIND", "");
		String except[]		= m_C.getParamValue(mapParams, "EXCEPT");
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.REMOVE_BY_KIND);
    		pStmt.setString(0, key);
    		pStmt.setString(1, isSystem);
    		pStmt.setString(2, kind);
    		if(except == null || except.length <= 0) {
    			pStmt.setNull(3);
    		}
    		else {
    			pStmt.setProcArray(3, new ArrayList<Object>(Arrays.asList(except)));
    		}
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	
	}
	
	public boolean removeAll(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;

		String strLang		= m_C.getParamValue(mapParams, "LANG", "ko");
		String key		= m_C.getParamValue(mapParams, "VALUE", "");
		String keyType		= m_C.getParamValue(mapParams, "FIELD", "JDOC_NO");
		String corpNo		= m_C.getParamValue(mapParams, "CORP_NO", session.getAttribute("CORP_NO") != null ?  session.getAttribute("CORP_NO").toString() : null);
		String userID		= m_C.getParamValue(mapParams, "USER_ID", session.getAttribute("USER_ID") != null ?  session.getAttribute("USER_ID").toString() : null);
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.REMOVE_ALL);
    		pStmt.setString(0, key);
    		pStmt.setString(1, corpNo);
    		pStmt.setString(2, userID);
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public boolean Change_Step(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;

		String strLang		= m_C.getParamValue(mapParams, "LANG", "ko");
		String key		= m_C.getParamValue(mapParams, "KEY", "");
		String step		= m_C.getParamValue(mapParams, "STEP", "");
		String corpNo		= m_C.getParamValue(mapParams, "CORP_NO", null);
		String userID		= m_C.getParamValue(mapParams, "USER_ID", null);
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.CHANGE_STEP);
    		pStmt.setString(0, key);
    		pStmt.setString(1, step);
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public boolean Mapping_Card(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean nRes = false;
		if (m_AC == null)	return false;

		String key			= m_C.getParamValue(mapParams, "KEY", null);
		String appr_no		= m_C.getParamValue(mapParams, "APPR_NO", "");
		String user_id		= m_C.getParamValue(mapParams, "USER_ID", "");
		String corp_no		= m_C.getParamValue(mapParams, "CORP_NO", "");
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.MAPPING_CARD);
    	
    		pStmt.setString(0, key);
    		pStmt.setString(1, appr_no);
        	pStmt.setString(2, corp_no);
    		pStmt.setString(3, user_id);
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				nRes = true;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			nRes = false;
		}
    	
    	return nRes;
	}
	
	public boolean Rotate_Slip(Map<String, String[]> mapParams) {
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;

		String degree		= m_C.getParamValue(mapParams, "DEGREE", "");
		String field			= m_C.getParamValue(mapParams, "FIELD", "");
		String value			= m_C.getParamValue(mapParams, "VALUE", "");
		String corpNo		= m_C.getParamValue(session, "CORP_NO", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.ROTATE_SLIP);
    		pStmt.setString(0, field);
    		pStmt.setString(1, value);
    		pStmt.setString(2, degree);
    		
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public int removeSlip(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		int nRes = 0;
		if (m_AC == null)	return nRes;

		String strLang		= m_C.getParamValue(mapParams, "LANG", "ko");
		String field			= m_C.getParamValue(mapParams, "FIELD", "");
		String[] value		= m_C.getParamValue(mapParams, "VALUE");
		String corpNo		= m_C.getParamValue(session, "CORP_NO", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
		try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.REMOVE_SLIP);
    		pStmt.setProcArray(0, new ArrayList<Object>(Arrays.asList(value)));
    		pStmt.setString(1, field);
    		pStmt.setString(2, corpNo);
    		pStmt.setString(3, userID);
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			//if(nResCnt > 0)
			{
				nRes = nResCnt;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			nRes = -1;
		}
    	
    	return nRes;
	}
	
	public int removeAttach(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		int nRes = -1;
		if (m_AC == null)	return nRes;

		String strLang		= m_C.getParamValue(mapParams, "LANG", "ko");
		String field			= m_C.getParamValue(mapParams, "FIELD", "");
		String[] value		= m_C.getParamValue(mapParams, "VALUE");
		String corpNo		= m_C.getParamValue(session, "CORP_NO", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
		try
    	{
			PreparedStatement pStmt = new PreparedStatement(Queries.REMOVE_ATTACH);
    		pStmt.setProcArray(0, new ArrayList<Object>(Arrays.asList(value)));
    		pStmt.setString(1, field);
    		pStmt.setString(2, corpNo);
    		pStmt.setString(3, userID);
    		
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			//if(nResCnt > 0)
			{
				nRes = nResCnt;
			}
    	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			nRes = -1;
		}
    	
    	return nRes;
	}
	
	public boolean writeComment(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		boolean bRes = false;
		if (m_AC == null)	return bRes;
		
	//	String strLang				= m_C.getParamValue(mapParams, "LANG", "ko");
		String jdocNo		= m_C.getParamValue(mapParams, "KEY", null);
		String title			= m_C.getParamValue(mapParams, "TITLE", null);
		String content		= m_C.getParamValue(mapParams, "CONTENT", null);
		String corpNo		= m_C.getParamValue(session, "CORP_NO", null);
		String partNo		= m_C.getParamValue(session, "PART_NO", null);
		String userID		= m_C.getParamValue(session, "USER_ID", null);
		
    	try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.WRITE_COMMENT);
    		pStmt.setString(0, m_C.getIRN("C"));
    		pStmt.setString(1, jdocNo);
    		pStmt.setString(2, corpNo);
    		pStmt.setString(3, partNo);
    		pStmt.setString(4, userID);
    		pStmt.setString(5, title);
    		pStmt.setString(6, content);
    		pStmt.setString(7, "1");
			pStmt.setDBDate(8);
			pStmt.setDBDate(9);
    	
    		String res 			= m_AC.SetData(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			int nResCnt			=  m_C.getResCnt(res);
			
			if(nResCnt > 0)
			{
				bRes = true;
			}
	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
			bRes = false;
		}
    	
    	return bRes;
	}
	
	public int Copy_SlipDoc(Map<String, String[]> mapParams)
	{
		String strFuncName 	= new Object(){}.getClass().getEnclosingMethod().getName();
		int nResCnt	 = -1;
		if (m_AC == null)	return nResCnt;
		
		String from			= m_C.getParamValue(mapParams, "FROM", "");
		String to				= m_C.getParamValue(mapParams, "TO", "");
		String userID 		= m_C.getParamValue(mapParams, "USER_ID", null);
		String corpNo		= m_C.getParamValue(mapParams, "CORP_NO", null);
		
    	try
    	{
    		PreparedStatement pStmt = new PreparedStatement(Queries.COPY_SLIP_JDOC_NO);
    		pStmt.setString(0, from);
        	pStmt.setString(1, to);
        	pStmt.setString(2, corpNo);
        	pStmt.setString(3, userID);
    	
    		String res 			= m_AC.SetProcedure(pStmt.getQuery(), strFuncName);
    		String resFlag		= res.substring(0,1);
			nResCnt			=  m_C.getResCnt(res);

	   	}
    	catch(Exception e)
		{
			logger.error(strFuncName, e);
		}
    	
    	return nResCnt;
	}
	
	
}
