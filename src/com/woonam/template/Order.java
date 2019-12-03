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

public class Order extends TemplateImpl{
	
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

	public Order(Profile profile) {
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
				
				endY = Draw_Contents(tool, ar_itemList.get(i).getAsJsonObject(), endY);
				
				//Check if overflow
				if(endY  == -1) {
					while(true) {
						Thread.sleep(10);
						endY = 100;
						endY = Draw_Contents(tool, ar_itemList.get(i).getAsJsonObject(), endY);
						if(endY != -1) break;
					}
				}
				
			}
			
			Draw_Bottom(tool, endY);
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
	
	private int Draw_Contents(TemplateTool tool, JsonObject objItem, int startY) {
		
		int y = startY;
		tool.setFontColor(new Color(0,0,0));
		
		int defHeight = 110;
		String itemNM = objItem.get("ITEM_NM").getAsString();
		String deptNM = objItem.get("PR_DEPT_NM").getAsString()+"\n("+objItem.get("IN_COMP_NM").getAsString()+")";
		//Get max height from longest text

		int itemNMHeight = tool.Get_TextHeight(itemNM,  390);
		if(itemNMHeight < defHeight) {
			itemNMHeight = defHeight;
		}
		
		int deptNMHeight = tool.Get_TextHeight(deptNM,  230);
		if(deptNMHeight < defHeight) {
			deptNMHeight = defHeight;
		}
		
		int bottom = (itemNMHeight < deptNMHeight ? deptNMHeight : itemNMHeight);
		
		int drawnHeight = y + bottom;
		
		if(drawnHeight > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			drawnHeight = y + bottom;
		}
		
			//Draw first line
		tool.DrawLine(250, y, 1975, 1);
		tool.DrawLine(250, drawnHeight, 1975, 1);
		
		tool.DrawLine(250, y, 1, bottom);
		tool.DrawLine(389, y, 1, bottom);
		tool.DrawLine(624, y, 1, bottom);
		tool.DrawLine(833, y, 1, bottom);
		tool.DrawLine(1255, y, 1, bottom);
		tool.DrawLine(1508, y, 1, bottom);
		tool.DrawLine(1758, y, 1, bottom);
		tool.DrawLine(2011, y, 1, bottom);
		tool.DrawLine(2224, y, 1, bottom);
		

		//Draw first columns
		String poLno = objItem.get("PO_LNO").getAsString();
		tool.DrawText(poLno , 250, y, 389, drawnHeight, tool.CENTER);
		
		String prNo = objItem.get("PO_NO").getAsString();
		if(prNo.trim().length() <= 4) prNo = "";
		tool.DrawText(prNo , 389, y, 624, drawnHeight, tool.CENTER);
		
		String prUser = objItem.get("PR_REG_NM").getAsString();
		tool.DrawText(prUser , 624, y, 833, drawnHeight, tool.CENTER);
		
		tool.Draw_MultiLineText(itemNM , 843, y, 1235, drawnHeight, 390, bottom, tool.LEFT);
		
		String prDept = objItem.get("PR_DEPT_NM").getAsString() + "\n(" + objItem.get("IN_COMP_NM").getAsString()+")";
		if(prDept.trim().length() <= 4) prDept = "";
		tool.Draw_MultiLineText(prDept , 1265, y, 1498, drawnHeight, 230, bottom, tool.CENTER);
		
		String price = objItem.get("ITEM_PRICE_VD").getAsString();
		price = price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(price , 1508, y, 1738, drawnHeight, tool.RIGHT);
		
		String rdDate = objItem.get("RD_DATE_NM").getAsString();
		tool.DrawText(rdDate , 1758, y, 2011, drawnHeight, tool.CENTER);
		
		String reqNM = objItem.get("REAL_REQ_NM").getAsString();
		tool.DrawText(reqNM , 2011, y, 2224, drawnHeight, tool.CENTER);
		
		
		
		String spec = objItem.get("SPEC").getAsString();
		String prPhone = objItem.get("PR_REG_PHONE").getAsString();
		String realPhone = objItem.get("REAL_REQ_TEL").getAsString();
		String rdLocation = objItem.get("RD_LOCAT").getAsString() + "(" + objItem.get("PR_REG_ID").getAsString()+")";
		
		//Get max height from longest text

		y = drawnHeight;
		
		int spectHeight = tool.Get_TextHeight(spec,  400);
		if(spectHeight < defHeight) {
			spectHeight = defHeight;
		}
		
		int prPhoneHeight = tool.Get_TextHeight(prPhone,  210);
		if(prPhoneHeight < defHeight) {
			prPhoneHeight = defHeight;
		}
		
		int realPhoneHeight = tool.Get_TextHeight(realPhone, 200);
		
		int rdLocationHeight = tool.Get_TextHeight(rdLocation, 200);
		
		bottom = (spectHeight < prPhoneHeight ? prPhoneHeight : spectHeight);

		bottom = (bottom < realPhoneHeight ? realPhoneHeight : bottom);
		
		bottom = (bottom < rdLocationHeight ? rdLocationHeight : bottom);
		
		drawnHeight = y + bottom;
		
		if(drawnHeight > imgBg.getHeight() - 100) {
			savePage(tool);
			
			y = 100;
			drawnHeight = y + bottom;
		}
		
		
		//Draw second line
		tool.DrawLine(250, y, 1, bottom);
		tool.DrawLine(389, y, 1, bottom);
		tool.DrawLine(624, y, 1, bottom);
		tool.DrawLine(833, y, 1, bottom);
		tool.DrawLine(1255, y, 1, bottom);
		tool.DrawLine(1508, y, 1, bottom);
		tool.DrawLine(1758, y, 1, bottom);
		tool.DrawLine(2011, y, 1, bottom);
		tool.DrawLine(2224, y, 1, bottom);
		
		//Draw left columns
		String unitCD = objItem.get("UNIT_CD").getAsString();
		tool.DrawText(unitCD , 250, y, 389, drawnHeight, tool.CENTER);
		
		tool.Draw_MultiLineText(rdLocation , 399, y, 624, drawnHeight, 200, bottom, tool.CENTER);
		
		String prNM = objItem.get("PR_REG_NM").getAsString();
		tool.DrawText(prNM , 624, y, 833, drawnHeight, tool.CENTER);
		
		String itemQuantity = objItem.get("ITEM_QTY").getAsString();
		itemQuantity = itemQuantity.substring(0, itemQuantity.indexOf("."));
		itemQuantity = itemQuantity.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(itemQuantity , 1255, y, 1488, drawnHeight, tool.RIGHT);
		
		String amt = objItem.get("ITEM_AMT").getAsString();
		amt = amt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
		tool.DrawText(amt , 1508, y, 1738, drawnHeight, tool.RIGHT);
		

		tool.Draw_MultiLineText(prPhone , 1778, y, 1981, drawnHeight, 210, bottom, tool.CENTER);
		tool.Draw_MultiLineText(realPhone , 2011, y, 2224, drawnHeight, 200, bottom, tool.CENTER);
		
	
	
		tool.DrawLine(250, drawnHeight, 1975, 1);
		
//		
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
		
		return drawnHeight;
	}
	
	//Returns end Y
	private int Draw_ContentsHeader(TemplateTool tool) {
	
		try 
		{
			
			tool.DrawLine(250, 949, 1975, 1);
			tool.DrawLine(250, 1056, 1975, 1);

			tool.DrawLine(250, 949, 1, 220);
			tool.DrawLine(389, 949, 1, 220);
			tool.DrawLine(624, 949, 1, 220);
			tool.DrawLine(833, 949, 1, 220);
			tool.DrawLine(1255, 949, 1, 220);
			tool.DrawLine(1508, 949, 1, 220);
			tool.DrawLine(1758, 949, 1, 220);
			tool.DrawLine(2011, 949, 1, 220);
			tool.DrawLine(2224, 949, 1, 220);

			
			tool.DrawText("순번" , 250, 949, 389, 1059, tool.CENTER);
			tool.DrawText("의뢰번호" , 389, 949, 624, 1059, tool.CENTER);
			tool.DrawText("자재코드" , 624, 949, 833, 1059, tool.CENTER);
			tool.DrawText("자재명" , 833, 949, 1255, 1059, tool.CENTER);
			tool.DrawText("신청부서\n(납품처)" , 1255, 949, 1508, 1059, tool.CENTER);
			tool.DrawText("단가" , 1508, 949, 1758, 1059, tool.CENTER);
			tool.DrawText("납품기한" , 1758, 949, 2011, 1059, tool.CENTER);
			tool.DrawText("수령인" , 2011, 949, 2224, 1059, tool.CENTER);
			
			tool.DrawText("단위" , 250, 1060, 389, 1159, tool.CENTER);
			tool.DrawText("납품장소\n(업체)" , 389, 1060, 624, 1159, tool.CENTER);
			tool.DrawText("의뢰자" , 624, 1060, 833, 1159, tool.CENTER);
			tool.DrawText("Description" , 833, 1060, 1255, 1159, tool.CENTER);
			tool.DrawText("의뢰수량" , 1255, 1060, 1508, 1159, tool.CENTER);
			tool.DrawText("합계금액" , 1508, 1060, 1758, 1159, tool.CENTER);
			tool.DrawText("의뢰자\n연락처" , 1758, 1060, 2011, 1159, tool.CENTER);
			tool.DrawText("수령인\n연락처" , 2011, 1060, 2224, 1159, tool.CENTER);
		}
		catch(Exception e)
		{
			logger.error("Order - Draw", e);
			//this.m_objSlipInfo = null;
			return 0;
		}
		
		return 1170;
	}
	
	private boolean Draw_Header(TemplateTool tool) {
	
		try 
		{	
			tool.setFontStyle("Dotum", tool.NORMAL, 50);
		
			tool.DrawText("SK Chemicals e-Procurement System" , 230, 200, 2230, 300, tool.LEFT);
			
			tool.setFontStyle("Dotum", tool.BOLD, 60);
		
			tool.DrawText("(주) SK Chemicals" , 250, 420, 2230, 470, tool.LEFT);
			
			tool.setFontStyle("Gulim", tool.NORMAL, 30);
			
			String tel1 = "판교        (02) 2008-2008";
			tool.DrawText(tel1 , 1150, 370, 2230, 400, tool.LEFT);
			String tel2 = "울산        (052) 256-0121";
			tool.DrawText(tel2 , 1710, 370, 2230, 400, tool.LEFT);
			String tel3 = "안산        (031) 491-0121";
			tool.DrawText(tel3 , 1150, 430, 2230, 460, tool.LEFT);
			String tel4 = "오산        (031) 612-8200";
			tool.DrawText(tel4 , 1710, 430, 2230, 460, tool.LEFT);
			String tel5 = "청주        (043) 270-9500";
			tool.DrawText(tel5 , 1150, 490, 2230, 520, tool.LEFT);
			
			tool.setFontStyle("Dotum", tool.BOLD, 60);

			tool.DrawText("資材發注書" , 1080, 640, 2230, 700, tool.LEFT);
			

			tool.setFontStyle("Gulim", tool.NORMAL, 30);
			
			
			String vdcd 		= obj_orderInfo.get("VD_CD").getAsString();
			String erp_vdcd 	= obj_orderInfo.get("ERP_VD_CD").getAsString();
			String vd_nm_loc 	= obj_orderInfo.get("VD_NM_LOC").getAsString();
			tool.DrawText("거래처코드 : " + vdcd + "(" + vd_nm_loc + ", " + erp_vdcd +")" , 250, 780, 2230, 800, tool.LEFT);
			
			String pono 		= obj_orderInfo.get("PO_NO").getAsString();
			tool.DrawText("발주 NO : " + pono, 250, 830, 2230, 850, tool.LEFT);
			
			tool.setFontStyle("Dotum", tool.NORMAL, 34);
			tool.DrawText("1. 발주내역", 250, 880, 2230, 910, tool.LEFT);
		
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
