package com.woonam.template;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Order_Eng extends TemplateImpl{
	
	private String m_TempletePath = null; 
	private JsonObject obj_orderInfo = null;
	private JsonArray m_arImageInfo = new JsonArray();
	private int m_nCurPage 					= 0;
	private long m_nFileTotalSize 			= 0;
	private String m_workPath = null;
	private Profile m_Profile = null;
	private Common m_C = new Common();
	private static Logger logger = LogManager.getLogger("Woonam");

	private BufferedImage imgBg = null;

	public Order_Eng(Profile profile) {
		this.m_Profile = profile;
	}
	
	@Override
	public JsonObject run(JsonObject obj_data, String bgPath, String workPath) {
		
		JsonObject obj_res = null;
		if(m_C.isBlank(bgPath)) return null;
		
		this.obj_orderInfo = obj_data;
		this.m_TempletePath = bgPath;
		this.m_workPath = workPath;
	
		try {
			this.imgBg		 	= ImageIO.read(new File(m_C.Get_RootPathForJava() + File.separator + m_TempletePath + File.separator + "bg.jpg"));
			obj_res =  Draw();

			return obj_res;
	
		}
		catch(Exception e) {
			logger.error("Order - Failed load background.", e);
			return null;
		}
		finally {
			if(imgBg != null) imgBg.flush();
		}
	}

	//Draw Templete data
	private JsonObject Draw() throws Exception
	{
		JsonObject obj_res = new JsonObject();
		
	//	String typeCode = obj_orderInfo.get("TypeCode").getAsString();
	//	String headerBG = m_C.Get_RootPathForJava() + File.separator + m_TempletePath + File.separator;
		
		 
		JsonArray ar_itemList = obj_orderInfo.get("ITEM_LIST") != null ? obj_orderInfo.get("ITEM_LIST").getAsJsonArray() : null;
		
		TemplateTool tool = new TemplateTool(imgBg);
		tool.setFontColor(new Color(0,0,0));
		tool.setFontStyle("Dotum", tool.NORMAL, 20);
		//Draw header
		if(Draw_Header(tool)) {

			int endY = Draw_ContentsHeader(tool);
			
			for(int i = 0; i < ar_itemList.size(); i++) {
				
				endY = Draw_ContentsGoods(tool, ar_itemList.get(i).getAsJsonObject(), endY);
				
			}
			endY = Draw_GoodsTotal(tool, endY);
			
			endY = Draw_ContentsDescription(tool, endY);
			
			//Draw_Bottom(tool, endY);
			//Save last page.
			savePage(tool);
			
			//m_objSlipInfo.put("ImageTotalSize", m_nFileTotalSize);
			//m_objSlipInfo.put("ImageInfo", m_arImageInfo);
			obj_res.addProperty("TOTAL_SIZE", m_nFileTotalSize / 1024);
			obj_res.add("IMG_INFO", m_arImageInfo);
			obj_res.addProperty("CONVERT_KEY", obj_orderInfo.get("ConvertKey").getAsString());
			obj_res.addProperty("SDOC_NAME", "");
			obj_res.addProperty("SDOC_KIND", obj_orderInfo.get("SDocKind").getAsString());
			return obj_res;
		}
		else {
			return null;
		}
	}	
	
	private int Draw_Bottom(TemplateTool tool, int startY) {
		
		int y = startY;
		y += 40;
		
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		tool.setFontColor(new Color(0,0,0));
		tool.setFontStyle("Dotum", tool.NORMAL, 34);
		
		StringBuffer sbAmt = new StringBuffer();
		sbAmt.append("2. 합계금액 : ");
		sbAmt.append(obj_orderInfo.get("PO_TOT_AMT_LOC_VD").getAsString());
		sbAmt.append(" 통화 : ");
		sbAmt.append(obj_orderInfo.get("CUR").getAsString());
		sbAmt.append(" (부가세 별도) ");
		tool.DrawText(sbAmt.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		StringBuffer sbAprvTerm = new StringBuffer();
		sbAprvTerm.append("3. 결제조건 : ");
		sbAprvTerm.append(obj_orderInfo.get("APRV_TERMS_NM").getAsString());
		tool.DrawText(sbAprvTerm.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		StringBuffer sbDelyTerm = new StringBuffer();
		sbDelyTerm.append("4. 인도조건 : ");
		sbDelyTerm.append(obj_orderInfo.get("DELY_TERMS_NM").getAsString());
		tool.DrawText(sbDelyTerm.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		StringBuffer sbPayTerm = new StringBuffer();
		sbPayTerm.append("5. 지급조건 : ");
		sbPayTerm.append(obj_orderInfo.get("PAY_TERMS_NM").getAsString());
		tool.DrawText(sbPayTerm.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		StringBuffer sbTallyTerm = new StringBuffer();
		sbTallyTerm.append("6. 검수방법 : ");
		sbTallyTerm.append(obj_orderInfo.get("TALLY_MTD_NM").getAsString());
		tool.DrawText(sbTallyTerm.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		StringBuffer sbPurcNM = new StringBuffer();
		sbPurcNM.append("7. 구매담당자 : ");
		sbPurcNM.append(obj_orderInfo.get("PURC_CHR_NM").getAsString());
		tool.DrawText(sbPurcNM.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String prepay = obj_orderInfo.get("PRE_PAY_PCT").getAsString();
		StringBuffer sbPrePay = new StringBuffer();
		sbPrePay.append("9. 선급금 지급율 : ");
		if(!m_C.isBlank(prepay)) {
			sbPrePay.append("발주금액의 (");
			sbPrePay.append(prepay);
			sbPrePay.append(")%");
		}
		tool.DrawText(sbPrePay.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String grntNM = obj_orderInfo.get("CNTRT_PFMC_GRNT_YN_NM").getAsString();
		String grntPercent = obj_orderInfo.get("CNTRT_PFMC_GRNT_RATE").getAsString();
		
		StringBuffer sbGrntNM = new StringBuffer();
		sbGrntNM.append("9. 계약이행 보증 : ");
		if(!m_C.isBlank(grntNM)) {
			sbGrntNM.append(grntNM);
		}
		if(!m_C.isBlank(grntPercent)) {
			sbGrntNM.append(" (");
			sbGrntNM.append("발주금액의 ");
			sbGrntNM.append(grntPercent);
			sbGrntNM.append("%");
			sbGrntNM.append(")");
		}
		tool.DrawText(sbGrntNM.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String preGrntNM = obj_orderInfo.get("PREPAY_PFMC_GRNT_YN_NM").getAsString();
		String preGrntPercent = obj_orderInfo.get("PREPAY_PFMC_GRNT_RATE").getAsString();
		
		StringBuffer sbpreGrntNM = new StringBuffer();
		sbpreGrntNM.append("10. 선급금 지급 보증 : ");
		if(!m_C.isBlank(preGrntNM)) {
			sbpreGrntNM.append(preGrntNM);
		}
		if(!m_C.isBlank(preGrntPercent)) {
			sbpreGrntNM.append(" (");
			sbpreGrntNM.append("선급금의 ");
			sbpreGrntNM.append(preGrntPercent);
			sbpreGrntNM.append("%");
			sbpreGrntNM.append(")");
		}
		tool.DrawText(sbpreGrntNM.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String defGrntNM = obj_orderInfo.get("DEFECT_PFMC_GRNT_YN_NM").getAsString();
		String defGrntPercent = obj_orderInfo.get("DEFECT_PFMC_GRNT_RATE").getAsString();
		
		StringBuffer sbdefGrntNM = new StringBuffer();
		sbdefGrntNM.append("11.하자이행 보증 : ");
		if(!m_C.isBlank(defGrntNM)) {
			sbdefGrntNM.append(defGrntNM);
		}
		if(!m_C.isBlank(preGrntPercent)) {
			sbdefGrntNM.append(" (");
			sbdefGrntNM.append("발주금액의 ");
			sbdefGrntNM.append(defGrntPercent);
			sbdefGrntNM.append("%, 1년");
			sbdefGrntNM.append(")");
		}
		tool.DrawText(sbdefGrntNM.toString(), 250, y, 2230, y + 30, tool.LEFT);
		
		y += 140;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String credate = obj_orderInfo.get("PO_CRE_DATE_NM").getAsString();
		tool.DrawText(credate, 300, y, 1220, y + 30, tool.LEFT);
		
		String creskdate = obj_orderInfo.get("PO_CRE_DATE_NM").getAsString();
		tool.DrawText(creskdate, 1220, y, 2230, y + 30, tool.LEFT);

		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String vdnm = obj_orderInfo.get("VD_NM_LOC").getAsString();
		tool.DrawText("수주자 : " + vdnm, 300, y, 1220, y + 30, tool.LEFT);
		
		String compNM = obj_orderInfo.get("COMP_NM").getAsString();
		tool.DrawText("발주자 : " + compNM, 1220, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String repnm = obj_orderInfo.get("REP_NM_LOC").getAsString();
		tool.DrawText("대표자 : " + repnm, 300, y, 1220, y + 30, tool.LEFT);
		
		String skrepnm = obj_orderInfo.get("SK_REP_NM_LOC").getAsString();
		tool.DrawText("대표자 : " + skrepnm, 1220, y, 2230, y + 30, tool.LEFT);
		
		y += 60;
		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		String addr = obj_orderInfo.get("ADDR").getAsString();
		tool.DrawMultiLineText("주   소 : " + addr, 300, y, 1220, y + 30, 880,0,10, tool.LEFT);
		
		String skaddr = obj_orderInfo.get("ADDR").getAsString();
		tool.DrawMultiLineText("주   소 : " + skaddr, 1220, y, 2230, y + 30, 880,0,10, tool.LEFT);
		
		
		return 0;
	}
	
	
	private int Draw_ContentsDescription(TemplateTool tool, int startY) {
		
		int y = startY + 20;

		int defHeight = 80;
		int bottom = y + defHeight;
		
		//Check new page
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}

		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("1. SHIPPING DATE AT PORT OF EXIT : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 1060, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;

		if(y > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("2. SHIPPING PORT : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 1060, y, 2218, bottom, tool.LEFT);

		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;

		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("3. DESTINATION : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 1060, y, 2218, bottom, tool.LEFT);


		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("4. PAY CONDITION : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 1060, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("5. TERMS OF DELIVERY : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		String delivery = obj_orderInfo.get("DELY_TERMS_NM").getAsString();
		tool.DrawText(delivery, 1060, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("6. TERMS OF PAYMENT : ", 260, y, 1040, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		String payment = obj_orderInfo.get("PAY_TERMS_NM").getAsString();
		tool.DrawText(payment, 1060, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		
		defHeight = 110;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(1460, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("7. BANK GUARANTEES : ", 260, y, 1040, y + 330, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		
		tool.DrawText("1) Advanced Payment\nBond", 1060, y , 2218, bottom, tool.LEFT);
		String preGrnt = obj_orderInfo.get("PREPAY_PFMC_GRNT_RATE").getAsString();
		tool.DrawText("("+preGrnt+")% of Advanced payment amount until the\nDelivery date", 1480, y , 2218, bottom, tool.LEFT);
	
		//Check new page
		y = y + defHeight;
		
		defHeight = 110;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(1050, y, 1178, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(1460, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);

		tool.DrawText("2) Performance Bond", 1060, y , 1450, bottom, tool.LEFT);
		String cntGrnt = obj_orderInfo.get("CNTRT_PFMC_GRNT_RATE").getAsString();
		tool.DrawText("("+cntGrnt+")% of Total contract amount until the\nDelivery date", 1480, y , 2218, bottom, tool.LEFT);
	
		//Check new page
		y = y + defHeight;
		
		defHeight = 110;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(1050, y, 1178, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1050, y, 1, defHeight);
		tool.DrawLine(1460, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);

		tool.DrawText("3) Warranty Bond", 1060, y , 1450, bottom, tool.LEFT);
		String warnGrnt = obj_orderInfo.get("DEFECT_PFMC_GRNT_RATE").getAsString();
		tool.DrawText("("+warnGrnt+")% of Total contract amount until the\nDelivery date", 1480, y , 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 80;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("8. FORWARDER : ", 260, y, 2218, bottom, tool.LEFT);

		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 1060, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 600;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 260, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 80;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("9. REMARKS : ", 260, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 400;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 260, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 80;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);
		tool.DrawText("10. ORIGIN", 260, y, 2218, bottom, tool.LEFT);
		
		//Check new page
		y = y + defHeight;
		defHeight = 300;
		bottom = y + defHeight;
		
		if(bottom > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			bottom = y + defHeight;
		}
		
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		
		tool.setFontStyle("Dotum", tool.NORMAL, 30);
		tool.DrawText("", 260, y, 2218, bottom, tool.LEFT);
		
		y = y + defHeight;
		tool.DrawLine(250, y, 1975, 1);
		

		tool.setFontStyle("Dotum", tool.BOLD, 40);
		//Check new page
		defHeight = 60;
		y = y + defHeight;
		bottom = y + defHeight;
		tool.DrawText("Chong, Kwang-Seok", 260, y, 2218, bottom, tool.RIGHT);
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		tool.DrawText("Team Leader of", 260, y, 2218, bottom, tool.RIGHT);
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		tool.DrawText("Purchasing Team", 260, y, 2218, bottom, tool.RIGHT);
		
		
		//Check new page
		y = y + defHeight;
		bottom = y + defHeight;
		tool.DrawText("SK Chemicals", 260, y, 2218, bottom, tool.RIGHT);
		
		//End of Line
		return bottom;
	}
	
	private int Draw_GoodsTotal(TemplateTool tool, int startY) {
		
		int y = startY;
		int defHeight = 80;
		
		if(startY > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
		}
		
		int bottom = y + defHeight;
		
		tool.DrawLine(250, y, 1, defHeight);
		tool.DrawLine(1724, y, 1, defHeight);
		tool.DrawLine(1938, y, 1, defHeight);
		tool.DrawLine(2228, y, 1, defHeight);
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, bottom, 1975, 1);
		
		tool.setFontStyle("Dotum", tool.BOLD, 30);

		tool.DrawText("TOTAL PRICE" , 250, y, 1724, bottom, tool.CENTER);

		String cur = obj_orderInfo.get("CUR").getAsString();
		tool.DrawText(cur, 1724, y, 1928, bottom, tool.RIGHT);
		String poTot = obj_orderInfo.get("PO_TOT_AMT_NM").getAsString();
		tool.DrawText(poTot, 1938, y, 2218, bottom, tool.RIGHT);
		
		
		return bottom;
	}
	
	private int Draw_ContentsGoods(TemplateTool tool, JsonObject objItem, int startY) {
		
		tool.setFontColor(new Color(0,0,0));
		tool.setFontStyle("Dotum", tool.NORMAL, 30);
	
		
		int y = startY;
		
		int defHeight = 80;
		String itemNM = objItem.get("ITEM_NM").getAsString();
		String itemSPEC = objItem.get("SPEC").getAsString();
	
		//Get max height from longest text
		int wordHeight =  tool.Get_TextHeight(itemNM,  360);
		
		if(wordHeight < defHeight) {
			wordHeight = defHeight;
		}
		
		int wordHeight2 = tool.Get_TextHeight(itemSPEC, 550);
		if(wordHeight2 < defHeight) {
			wordHeight2 = defHeight;
		}
		
		int bottom = (wordHeight < wordHeight2 ? wordHeight2 : wordHeight);
		
		int drawnHeight = y + bottom;
		
		if(drawnHeight > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			drawnHeight = y + bottom;
		}
		
		tool.DrawLine(250, y, 1, bottom);
		tool.DrawLine(396, y, 1, bottom);
		tool.DrawLine(796, y, 1, bottom);
		tool.DrawLine(1385, y, 1, bottom);
		tool.DrawLine(1574, y, 1, bottom);
		tool.DrawLine(1724, y, 1, bottom);
		tool.DrawLine(1938, y, 1, bottom);
		tool.DrawLine(2228, y, 1, bottom);
		tool.DrawLine(250, y, 1975, 1);
		
		
		
		String poLno = objItem.get("PO_LNO").getAsString();
		tool.DrawText(poLno , 260, y, 386, drawnHeight, tool.CENTER);
		
		String itemNm = objItem.get("ITEM_NM").getAsString();
		tool.Draw_MultiLineText(itemNm , 406, y, 786, drawnHeight,360, bottom, tool.LEFT);
		
		String spec = objItem.get("SPEC").getAsString();
		tool.Draw_MultiLineText(spec , 806, y, 1375, drawnHeight,550, bottom, tool.LEFT);
		
		String qty = objItem.get("ITEM_QTY").getAsString();
		qty = qty.substring(0,qty.indexOf("."));
		qty = qty.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(qty , 1395, y, 1564, drawnHeight, tool.RIGHT);
		
		String uint = objItem.get("UNIT_CD").getAsString();
		tool.DrawText(uint , 1584, y, 1714, drawnHeight, tool.RIGHT);

		String pricevd = objItem.get("ITEM_PRICE_VD").getAsString();
		pricevd = pricevd.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(pricevd , 1734, y, 1928, drawnHeight, tool.RIGHT);
		
		String amount = objItem.get("ITEM_AMT").getAsString();
		amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(amount , 1948, y, 2218, drawnHeight, tool.RIGHT);
		
		return drawnHeight;
//		
//		int secondWordY = nextY + hPadding;
//		
//		//Get max height from longest text
//		String accNM = objItem.get("ACC_ACC_NM").getAsString();
//		String prPhone = objItem.get("PR_REG_PHONE").getAsString();
//		String reqPhone = objItem.get("REAL_REQ_TEL").getAsString();
//		
//		int accNMHeight =  tool.DrawMultiLineText(accNM, 833, secondWordY, 1255, 0, 380, 10, tool.LEFT);
//		int prPhoneHeight =  tool.DrawMultiLineText(prPhone, 1758, secondWordY, 2011, 0, 210, 10, tool.CENTER);
//		int reqPhoneHeight =  tool.DrawMultiLineText(reqPhone, 2011, secondWordY, 2224, 0, 170, 10, tool.CENTER);
//	
//		//Calc max height
//		int ar_heights[] = {accNMHeight, prPhoneHeight, reqPhoneHeight};
//		
//		int maxHeight = 0;
//		for (int i = 0; i < ar_heights.length; i++)
//		{
//		     if (ar_heights[i] > maxHeight)
//		     {
//		    	 maxHeight = ar_heights[i];
//		     }
//		}
//		
//		int secondLineHeight = maxHeight - nextY;
//		if(secondLineHeight < defHeight) {
//			maxHeight = nextY + defHeight;
//			secondLineHeight = defHeight;
//		}
//		
//		//Redraw on new page
//		if(maxHeight > imgBg.getHeight() - 100) {
//			savePage(tool);
//			
//			return -1;
//		}
//		
//		tool.setFontColor(new Color(0,0,0));
//		//Draw first line
//		tool.DrawLine(250, startY, 1975, 1);
//		tool.DrawLine(250, nextY, 1975, 1);
//		
//		tool.DrawLine(250, startY, 1, firstLineHeight);
//		tool.DrawLine(389, startY, 1, firstLineHeight);
//		tool.DrawLine(624, startY, 1, firstLineHeight);
//		tool.DrawLine(833, startY, 1, firstLineHeight);
//		tool.DrawLine(1255, startY, 1, firstLineHeight);
//		tool.DrawLine(1508, startY, 1, firstLineHeight);
//		tool.DrawLine(1758, startY, 1, firstLineHeight);
//		tool.DrawLine(2011, startY, 1, firstLineHeight);
//		tool.DrawLine(2224, startY, 1, firstLineHeight);
//
//		//Draw left columns
//		String poLno = objItem.get("PO_LNO").getAsString();
//		tool.DrawText(poLno , 250, firstWordY, 389, nextY, tool.CENTER);
//		
//		String prNo = objItem.get("PO_NO").getAsString() + "\n(" + objItem.get("PR_REG_ID").getAsString()+")";
//		if(prNo.trim().length() <= 4) prNo = "";
//		tool.DrawText(prNo , 389, firstWordY - 40, 624, nextY, tool.CENTER);
//		
//		String prUser = objItem.get("PR_REG_NM").getAsString();
//		tool.DrawText(prUser , 624, firstWordY, 833, nextY, tool.CENTER);
//		
//		String prDept = objItem.get("PR_DEPT_NM").getAsString() + "\n(" + objItem.get("IN_COMP_NM").getAsString()+")";
//		if(prDept.trim().length() <= 4) prDept = "";
//		tool.DrawText(prDept , 1255, firstWordY, 1508, nextY, tool.CENTER);
//		
//		String price = objItem.get("ITEM_PRICE_VD").getAsString();
//		price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//		tool.DrawText(price , 1508, firstWordY, 1738, nextY, tool.RIGHT);
//		
//		String rdDate = objItem.get("RD_DATE_NM").getAsString();
//		tool.DrawText(rdDate , 1758, firstWordY, 2011, nextY, tool.CENTER);
//		
//		String reqNM = objItem.get("REAL_REQ_NM").getAsString();
//		tool.DrawText(reqNM , 2011, firstWordY, 2224, nextY, tool.CENTER);
//		
//		//Draw second line
//		tool.DrawLine(250, nextY, 1, secondLineHeight);
//		tool.DrawLine(389, nextY, 1, secondLineHeight);
//		tool.DrawLine(624, nextY, 1, secondLineHeight);
//		tool.DrawLine(833, nextY, 1, secondLineHeight);
//		tool.DrawLine(1255, nextY, 1, secondLineHeight);
//		tool.DrawLine(1508, nextY, 1, secondLineHeight);
//		tool.DrawLine(1758, nextY, 1, secondLineHeight);
//		tool.DrawLine(2011, nextY, 1, secondLineHeight);
//		tool.DrawLine(2224, nextY, 1, secondLineHeight);
//		
//		//Draw left columns
//		String unitCD = objItem.get("UNIT_CD").getAsString();
//		tool.DrawText(unitCD , 250, secondWordY, 389, maxHeight, tool.CENTER);
//		
//		String rdLocation = objItem.get("RD_LOCAT").getAsString() + "\n(" + objItem.get("PR_REG_ID").getAsString()+")";
//		if(rdLocation.trim().length() <= 4) rdLocation = "";
//		tool.DrawText(rdLocation , 389, secondWordY - 40, 624, maxHeight, tool.CENTER);
//		
//		String prNM = objItem.get("PR_REG_NM").getAsString();
//		tool.DrawText(prNM , 624, secondWordY, 833, maxHeight, tool.CENTER);
//		
//		String itemQuantity = objItem.get("ITEM_QTY").getAsString();
//		itemQuantity = itemQuantity.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//		tool.DrawText(itemQuantity , 1255, secondWordY, 1488, maxHeight, tool.RIGHT);
//		
//		String amt = objItem.get("ITEM_AMT").getAsString();
//		amt = amt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//		tool.DrawText(amt , 1508, secondWordY, 1738, maxHeight, tool.RIGHT);
//	
//		tool.DrawLine(250, maxHeight, 1975, 1);
//		
//		return maxHeight;
	}
	
	//Returns end Y
	private int Draw_ContentsHeader(TemplateTool tool) {
	
		try 
		{
			tool.setFontStyle("Dotum", tool.NORMAL,  30);
			tool.DrawText("We are pleased to order you of the following goods on the terms and conditions set forth below ", 250, 1220, 2230, 1240, tool.LEFT);
		
			tool.setFontStyle("Dotum", tool.BOLD,  30);
			tool.DrawLine(250, 1252, 1980, 4);

			tool.DrawLine(250, 1256, 1, 100);
			tool.DrawLine(396, 1256, 1, 100);
			tool.DrawLine(796, 1256, 1, 100);
			tool.DrawLine(1385, 1256, 1, 100);
			tool.DrawLine(1574, 1256, 1, 100);
			tool.DrawLine(1724, 1256, 1, 100);
			tool.DrawLine(1938, 1256, 1, 100);
			tool.DrawLine(2228, 1256, 1, 100);
			tool.DrawLine(250, 1356, 1975, 1);

			tool.DrawText("No." , 250, 1256, 396, 1356, tool.CENTER);
			tool.DrawText("Item" , 396, 1256, 796, 1356, tool.CENTER);
			tool.DrawText("Description" , 796, 1256, 1385, 1356, tool.CENTER);
			tool.DrawText("Q'TY" , 1385, 1256, 1574, 1356, tool.CENTER);
			tool.DrawText("UNIT" , 1574, 1256, 1724, 1356, tool.CENTER);
			tool.DrawText("UNIT\nPRICE" , 1724, 1256, 1938, 1356, tool.CENTER);
			tool.DrawText("AMOUNT" , 1938, 1256, 2228, 1356, tool.CENTER);
			
//			
//			tool.DrawLine(250, 949, 1975, 1);
//			tool.DrawLine(250, 1056, 1975, 1);
//
//			tool.DrawLine(250, 949, 1, 220);
//			tool.DrawLine(389, 949, 1, 220);
//			tool.DrawLine(624, 949, 1, 220);
//			tool.DrawLine(833, 949, 1, 220);
//			tool.DrawLine(1255, 949, 1, 220);
//			tool.DrawLine(1508, 949, 1, 220);
//			tool.DrawLine(1758, 949, 1, 220);
//			tool.DrawLine(2011, 949, 1, 220);
//			tool.DrawLine(2224, 949, 1, 220);
//
//			
//			tool.DrawText("순번" , 250, 949, 389, 1059, tool.CENTER);
//			tool.DrawText("의뢰번호" , 389, 949, 624, 1059, tool.CENTER);
//			tool.DrawText("자재코드" , 624, 949, 833, 1059, tool.CENTER);
//			tool.DrawText("자재명" , 833, 949, 1255, 1059, tool.CENTER);
//			tool.DrawText("신청부서\n(납품처)" , 1255, 910, 1508, 1059, tool.CENTER);
//			tool.DrawText("단가" , 1508, 949, 1758, 1059, tool.CENTER);
//			tool.DrawText("납품기한" , 1758, 949, 2011, 1059, tool.CENTER);
//			tool.DrawText("수령인" , 2011, 949, 2224, 1059, tool.CENTER);
//			
//			tool.DrawText("단위" , 250, 1060, 389, 1159, tool.CENTER);
//			tool.DrawText("납품장소\n(업체)" , 389, 1029, 624, 1159, tool.CENTER);
//			tool.DrawText("의뢰자" , 624, 1060, 833, 1159, tool.CENTER);
//			tool.DrawText("Description" , 833, 1060, 1255, 1159, tool.CENTER);
//			tool.DrawText("의뢰수량" , 1255, 1060, 1508, 1159, tool.CENTER);
//			tool.DrawText("합계금액" , 1508, 1060, 1758, 1159, tool.CENTER);
//			tool.DrawText("의뢰자\n연락처" , 1758, 1029, 2011, 1159, tool.CENTER);
//			tool.DrawText("수령인\n연락처" , 2011, 1029, 2224, 1159, tool.CENTER);
		}
		catch(Exception e)
		{
			logger.error("Order - Draw", e);
			//this.m_objSlipInfo = null;
			return 0;
		}
		
		return 1356;
	}
	
	private boolean Draw_Header(TemplateTool tool) {
	
		try 
		{	
			tool.setFontStyle("Dotum", tool.NORMAL, 40);
		
			tool.DrawText("SK Chemicals e-Procurement System" , 230, 250, 2230, 290, tool.LEFT);
			
			tool.setFontStyle("Dotum", tool.BOLD, 60);
		
			tool.DrawText("SK Chemicals CO., Ltd." , 250, 370, 2230, 430, tool.CENTER);
			
			tool.setFontStyle("Dotum", tool.NORMAL,  38);
			tool.DrawText("Head Office : SK Chemicals Building" , 250, 520, 2230, 560, tool.LEFT);
			tool.DrawText("686 Sampyeong-dong, Bundang-gu, Seongnam-si" , 250, 560, 2230, 600, tool.LEFT);
			tool.DrawText(",Gyeonggi-do 463-400, Korea" , 250, 600, 2230, 640, tool.LEFT);
			tool.DrawText("+82-2-2008-2008" , 250, 640, 2230, 680, tool.LEFT);
				
			tool.setFontStyle("Dotum", tool.BOLD, 60);

			tool.DrawText("PURCHASE ORDER" , 250, 770, 2230, 820, tool.CENTER);

			tool.setFontStyle("Dotum", tool.NORMAL,  48);
			String pono 		= obj_orderInfo.get("PO_NO").getAsString();
			tool.DrawText("PO No. : " + pono , 740, 910, 2230, 950, tool.LEFT);
			String poDate		= obj_orderInfo.get("PO_CRE_DATE_NM").getAsString();
			tool.DrawText("Date : " + poDate , 740, 960, 2230, 1000, tool.LEFT);
		
			tool.setFontStyle("Dotum", tool.NORMAL,  38);
			tool.DrawText("Messers : ", 250, 1080, 2230, 1120, tool.LEFT);
			tool.DrawText("C.C. : ", 250, 1120, 2230, 1160, tool.LEFT);

			
		
		}
		catch(Exception e)
		{
			logger.error("Order - Draw", e);
			//this.m_objSlipInfo = null;
			return false;
		}
		
		return true;
	}
		
	
	private void savePage(TemplateTool tool) {
		//String strImagePath = m_strImageSavePath + File.separator +m_nCurPage+".jpg";
		
		File path = new File(m_workPath);
		if(!path.exists()) {
			path.mkdirs();
		}
		
		String docIRN = m_C.getIRN("");
		StringBuffer sbImgPath = new StringBuffer();
		sbImgPath.append(m_workPath);
		sbImgPath.append(File.separator);
		sbImgPath.append(docIRN);
		sbImgPath.append(".jpg");
		
		long lFileSize = tool.saveImageFile(sbImgPath.toString(), tool.IMAGE_JPG);
		m_nFileTotalSize += lFileSize;
		JsonObject objJsonItem = new JsonObject();
		//-IMPORTANT- save background image size to use on upload slip.
//		objJsonItem.put("Width", tool.getBackgroundWidth());
//		objJsonItem.put("Height", tool.getBackgroundHeight());
//		objJsonItem.put("Size", lFileSize);
//		objJsonItem.put("PageIndex", m_nCurPage);
//		objJsonItem.put("Path", strImagePath);
		objJsonItem.addProperty("WIDTH", tool.getBackgroundWidth());
		objJsonItem.addProperty("HEIGHT", tool.getBackgroundHeight());
		objJsonItem.addProperty("SIZE", lFileSize / 1024);
		objJsonItem.addProperty("INDEX", m_nCurPage);
		objJsonItem.addProperty("DOC_IRN", docIRN);
		objJsonItem.addProperty("PATH", sbImgPath.toString());
		objJsonItem.addProperty("NAME", "");
		
		tool.setBackgroudImage(imgBg);
		tool.setFontColor(new Color(0,0,0));
		m_arImageInfo.add(objJsonItem);
	}
	
}
