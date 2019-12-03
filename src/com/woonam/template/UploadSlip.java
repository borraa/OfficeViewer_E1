package com.woonam.template;

import java.io.File;
import java.util.Map;

import com.woonam.image.Office3GL;
import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woonam.connect.AgentConnect;
import com.woonam.model.GetModel;
import com.woonam.model.SetModel;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import com.woonam.wdms.WdmFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadSlip {

	private Common m_C 					= new Common();
	private Profile m_Profile 				= null;
	private WdmFile m_WF					= null;
	private AgentConnect m_AC			= null;
	private GetModel m_GM					= null;
	private SetModel m_SM					= null;
	private static Logger logger = LogManager.getLogger("Woonam");
	
	public UploadSlip(Profile profile) {
		this.m_Profile = profile;
		this.m_WF = new WdmFile(profile);
		this.m_AC = new AgentConnect(profile);
		this.m_GM = new GetModel(m_AC);
		this.m_SM = new SetModel(m_AC);
	}
	
	//upload slip
	public JsonObject run(JsonObject objData, Map params, String workPath) {
		JsonObject jRes = new JsonObject();
		jRes.addProperty("result", "F");
		jRes.addProperty("msg","");
		
		try {
			String convertKey = objData.get("CONVERT_KEY").getAsString();
			String sdocKind = objData.get("SDOC_KIND").getAsString();
			JsonArray ar_imgInfo = objData.get("IMG_INFO") == null ? null : objData.get("IMG_INFO").getAsJsonArray();
			
			if(ar_imgInfo == null || ar_imgInfo.size() <= 0) {
				jRes.addProperty("result", "F");
				jRes.addProperty("msg", "ERR_NULL_IMAGE");
				
				return jRes;
			}
			
			//String docIRN = m_C.getIRN("");
			String sdocNo = m_C.getIRN("S");
			int uploadCnt = 0;
			
			for(int i = 0; i < ar_imgInfo.size(); i++) {
				JsonObject obj_imgData = ar_imgInfo.get(i).getAsJsonObject();
				
				//Upload slip 
				if(Upload_Slip(obj_imgData, sdocNo, i)) {
					
					//Upload thumb
					if(Upload_Thumb(obj_imgData)) {
						uploadCnt ++ ;
					}
					else {
						jRes.addProperty("result", "F");
						jRes.addProperty("msg","FAILED_UPLOAD_THUMB");
						return jRes;
					}
				}
				else {
					jRes.addProperty("result", "F");
					jRes.addProperty("msg","FAILED_UPLOAD_SLIP");
					return jRes;
				}
			}
			
			//Verify slip cnt
			int uploaded_slip_cnt = m_GM.Verify_SlipCnt(objData.get("IMG_INFO").getAsJsonArray());
			if(uploaded_slip_cnt != -1 && uploaded_slip_cnt != uploadCnt) {
				jRes.addProperty("result", "F");
				jRes.addProperty("msg","UPLOAD_SLIP_CNT_NOT_MATCHED");
				return jRes;
			}
			
			//Verify thumb cnt 
			int uploaded_thumb_cnt = m_GM.Verify_ThumbCnt(objData.get("IMG_INFO").getAsJsonArray());
			if(uploaded_thumb_cnt != -1 && uploaded_thumb_cnt != uploadCnt) {
				jRes.addProperty("result", "F");
				jRes.addProperty("msg","UPLOAD_THUMB_CNT_NOT_MATCHED");
				return jRes;
			}
			
			//Get slipdoc index 
		//	int idx = m_SD.Insert_SlipDocFromTemplate();
			
			//Upload slip doc
			//return jRes;
			String corpNo = m_C.getParamValue(params, "CORP_NO", "");
			String userId = m_C.getParamValue(params, "USER_ID", "");
			String lang = m_C.getParamValue(params, "LANG", "KO");
			
			JsonObject obj_userInfo = m_GM.getUserInfo(userId, corpNo, lang);
			
			if(obj_userInfo == null || obj_userInfo.size() <= 0) {
				logger.error("WebService : No user exists. "
						+ "USER_ID : " + userId 
						+ ", CORP_NO : " + corpNo 
						+ ", LANG : " + lang);
				jRes.addProperty("result", "F");
				jRes.addProperty("msg", "NO_USER_INFO");
				return jRes;
			}
			
			String partNo = obj_userInfo.get("PART_NO").getAsString();
			
			if(m_SM.Insert_SlipDocFromTemplate(sdocNo,
					corpNo,
					partNo,
					userId,
					sdocKind,
					objData.get("SDOC_NAME").getAsString(),
					uploaded_slip_cnt,
					objData.get("CONVERT_KEY").getAsString()
				)) {
				
				//ÀçÃ³¸®
				m_SM.Copy_Replace(objData.get("CONVERT_KEY").getAsString(), sdocNo, corpNo, userId);
				
				m_SM.Add_History(sdocNo, "S10", corpNo, userId);
				
				jRes.addProperty("result", "T");
				jRes.addProperty("msg", "");
			}
			
			return jRes;
			
		}
		catch(Exception e) {
			logger.error("UploadSlip - Failed to upload slip.", e);
			jRes.addProperty("result", "F");
			jRes.addProperty("msg", "EXCEPTION_OCCURED");
			return jRes;
		}
		finally {
			try {
				FileUtils.deleteDirectory(new File(workPath));
			}
			catch(Exception e) {
				logger.error("UploadSlip - failed to remove workpath.", e);
			}
		}
	}
	
	
	private boolean Upload_Slip(JsonObject obj_imgData, String sdocNo, int slipNo) {
		
		boolean res = false;
		String path 			= obj_imgData.get("PATH").getAsString();
		String docIRN 		= obj_imgData.get("DOC_IRN").getAsString();
		
		if("T".equalsIgnoreCase(m_WF.Upload(path, docIRN, "0", "IMG_SLIP_X"))) {
		
			try {	
				res = m_SM.Insert_SlipFromTemplate(obj_imgData, sdocNo, slipNo);
			}
			catch(Exception e) {
				logger.error("UploadSlip - Failed to insert SLIP.", e);
				return false;
			}
		}
		
		return res;
	}

	
	private boolean Upload_Thumb(JsonObject obj_imgData) throws Exception {
		boolean res = false;
		

		String path 	= obj_imgData.get("PATH").getAsString();
		String docIRN = obj_imgData.get("DOC_IRN").getAsString();

		int desiredWidth = Integer.parseInt(m_Profile.getString("WAS_INFO","THUMB_WIDTH","500"));
		new Office3GL(path).getThumbNail(desiredWidth,"jpg",path);
//		
		if("T".equalsIgnoreCase(m_WF.Upload(path, docIRN, "0", "IMG_SLIP_M"))) {
			res = true;
		}
		
		return res;
	}
}