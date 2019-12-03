//package com.woonam.template;
//
//import java.awt.Color;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//
//import javax.imageio.ImageIO;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.woonam.connect.AgentConnect;
//import com.woonam.util.Common;
//import com.woonam.util.Logger;
//
//public class CashReceipt {
//
//	private String m_TempletePath = null; 
//	private AgentConnect m_imageData = null;
//	private AgentConnect m_companyInfo = null;
//	private JsonArray m_arJData = null;
//	private String m_strImageSavePath = null;
//	private JsonArray m_arImageInfo = new JsonArray();
//	private JsonObject m_objSlipInfo = new JsonObject();
//	
//	private int m_nCurPage 					= 0;
//	private int m_nHeaderBottom 			= 340;
//	private int m_nRowHeight 				= 100;
//	private int m_nCurRow					= 0;
//	private int nLineHorizontalPadding 		= 80;
//	private int nFontVerticalPadding 		= 7;
//	private int m_nRowPerPage 				= 18;
//	private int m_nTotalPage				= 0;
//	private long m_nFileTotalSize 			= 0;
//	private Common m_C = new Common();
//	
//	
//	
//	public void run(AgentConnect imageData, AgentConnect companyInfo, String strWorkPath, String strBackgroundPath) {
//		this.m_imageData = imageData;
//		this.m_companyInfo = companyInfo;
//		this.m_strImageSavePath = strWorkPath;
//		this.m_TempletePath = strBackgroundPath;
//		
//		//initialize tool
//		TempleteTool tool = new TempleteTool(m_TempletePath +  File.separator + "00.jpg");
//		if(tool != null)
//		{
//			tool.setFontStyle("굴림", tool.NORMAL, 30);
//			tool.setFontColor(new Color(0,0,0));
//		
//			File file = new File(m_strImageSavePath);
//			if(!file.exists())
//			{
//				file.mkdirs();
//			}
//				
//			Draw(tool);
//		}
//	}
//	
//	/*private void DrawTop(TempleteTool tool) {
//		//fixed value topline.	
//		tool.setFontStyle("굴림", tool.NORMAL, 30);
//		String strCurDate 		= new SimpleDateFormat("yyyy-MM-dd        HH:mm:ss").format(new Date());
//		tool.DrawText(strCurDate, 100,130);
//		tool.DrawText("Page "+(m_nCurPage+1)+" of "+m_nTotalPage, 100, 180);
//		
//		//center title
//		String strCo =  (String) ((JSONObject) m_objData.get("Header")).get("CO_TITLE_NM");
//		tool.DrawText(strCo, 650, 140);
//		tool.setFontStyle("NanumGothic", tool.BOLD, 50);
//		tool.DrawText("AP발생전표", 580, 180);
//		tool.setFontStyle("NanumGothic", tool.BOLD, 34);
//		
//		JSONArray objaddSignature = (JSONArray) m_objData.get("jArAddSignature");
//		if(objaddSignature!=null) {
//		int nSectionCnt2 = objaddSignature.size();
//			for(int i = 0; i <nSectionCnt2; i++)			
//			{
//				JSONObject objVSection = (JSONObject) objaddSignature.get(i);
//			
//			}
//		}
//	}
//	
//	private void DrawHeader(TempleteTool tool, JSONObject objHeader) {
//		
//		//InitSectionStrings
//		String strBatchType 		= "Btach Ty/No :   "  + objHeader.get("BATCH_TYPE").toString()  + " / "  + objHeader.get("BATCH_NUM").toString();
//		String strUser				= "전표 생성자 :   "	  + objHeader.get("USER_ID").toString() 	+ "    " + objHeader.get("EMP_NM").toString();
//		String strBatchStatus	= "Btach Status :   " + objHeader.get("BATCH_STATUS").toString();
//	
//		String strBatchDate 		= (String) objHeader.get("BATCH_DT");
//		strBatchDate 				= "Btach Date :   "   + (strBatchDate == null ? "" : strBatchDate.substring(0, 10));
//				
//		//Calc row position and inialize tool options
//		tool.setFontStyle("NanumGothic", tool.BOLD, 34);
//		int nCurRowTop = m_nHeaderBottom + (m_nRowHeight * m_nCurRow);
//		int nCurRowBottom = m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + m_nRowHeight;
//		
//		//Draw Text
//		tool.DrawText(strBatchType, 120, nCurRowTop, 720, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strUser, 720, nCurRowTop, 1970, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strBatchStatus, 1970, nCurRowTop, 2320, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strBatchDate, 2840, nCurRowTop, 3380, nCurRowBottom, tool.LEFT);
//		tool.DrawLine(nLineHorizontalPadding, nCurRowBottom , tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//		
//		m_nCurRow++;
//	}
//	
//	private void DrawSectionTitle(TempleteTool tool) {
//		
//		tool.setFontStyle("NanumGothic", tool.NORMAL, 30);
//		
//		int nCurRowTop 			= m_nHeaderBottom + (m_nRowHeight * m_nCurRow);
//		int nCurRowBottom 		= m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + m_nRowHeight;
//		int nCurTextTop 		= nFontVerticalPadding + nCurRowTop;
//		int nCurTextBottom 		= nCurTextTop + tool.getTextHeight("");
//		
//		tool.DrawText("Ln No", 100, nCurTextTop, 190, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Ty", 210, nCurTextTop, 280, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Co", 280, nCurTextTop, 400, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Account No", 400, nCurTextTop, 920, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Explanation", 920, nCurTextTop, 1440, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Cost Object", 1440, nCurTextTop, 1670, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Due Date", 1670, nCurTextTop, 1900, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Pay Inst", 1900, nCurTextTop, 2130, nCurTextBottom , tool.LEFT);
//		//new Line
//		nCurTextTop = nCurTextBottom + nFontVerticalPadding;
//		nCurTextBottom = nCurTextTop + tool.getTextHeight("");
//	
//		tool.DrawText("Remark", 920, nCurTextTop, 1440, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Subledger", 1440, nCurTextTop, 1670, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Asset No", 1670, nCurTextTop, 1900, nCurTextBottom , tool.LEFT);
//		tool.DrawText("R/V", 2130, nCurTextTop, 2230, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Posted", 2230, nCurTextTop, 2330, nCurTextBottom , tool.LEFT);
//		tool.DrawText("Debit",  2880, nCurTextTop, 3060, nCurTextBottom , tool.RIGHT);
//		tool.DrawText("Credit",  3060, nCurTextTop, 3400, nCurTextBottom , tool.RIGHT);
//		
//		tool.DrawLine(nLineHorizontalPadding, nCurRowTop - 10, tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//		tool.DrawLine(nLineHorizontalPadding, nCurRowBottom - 10, tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//		tool.DrawLine(nLineHorizontalPadding, nCurRowBottom , tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//		
//		m_nCurRow++;
//	}
//	
//	private void DrawSection(TempleteTool tool, JSONObject objSection) {
//		
//		//InitSectionStrings
//		String strDocCo 				= "Doc Co : " + (String) objSection.get("DOC_COMP_CD")+ " " + (String) objSection.get("DOC_COMP_NM");// can't find
//		String strDocNo					= "Doc No : " + (String) objSection.get("DOC_VOUCHER_INVOICE");
//		String strBankAcct				= "G/L Bank Account : " +(String) objSection.get("GL_BANK_ACCT_CD") + "   " + (String) objSection.get("GL_BANK_ACCT_NM");
//		String strGLDate				= "G/L Date : " + (String) objSection.get("GL_DT");
//		String strVoidFlag				=  (String) objSection.get("VOID_FLAG");
//		//Calc row position and inialize tool options
//		tool.setFontStyle("NanumGothic", tool.BOLD, 34);
//		int nCurRowTop = m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + nFontVerticalPadding;
//		int nCurRowBottom = m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + m_nRowHeight;
//
//		//Draw Text
//		tool.DrawText(strDocCo, 120, nCurRowTop, 720, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strBankAcct, 920, nCurRowTop, 2100, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strVoidFlag, 2130, nCurRowTop, 2230, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strDocNo, 2340, nCurRowTop, 2500, nCurRowBottom, tool.LEFT);
//		tool.DrawText(strGLDate, 2880, nCurRowTop, 3400, nCurRowBottom, tool.RIGHT);
//		
//		tool.DrawLine(nLineHorizontalPadding, nCurRowBottom , tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//		
//		m_nCurRow++;
//	}
//	
//	private TempleteTool DrawRow(TempleteTool tool, JSONObject objRow, String strDocNum) {
//		
//	//	tool = checkRowFilled(tool, strDocNum);
//		tool.setFontStyle("NanumGothic", tool.NORMAL, 30);
//		
//		int nCurRowTop 						= m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + nFontVerticalPadding;
//		int nCurRowBottom 					= m_nHeaderBottom + (m_nRowHeight * m_nCurRow) + m_nRowHeight;
//		int nCurTextTop 					= nFontVerticalPadding + nCurRowTop;
//		int nCurTextBottom 					= nCurTextTop + tool.getTextHeight("");
//		
//		String strDocPayItem		 		= (String) objRow.get("DOC_PAY_ITEM");
//		String strDocType 					= (String) objRow.get("DOC_TYPE");
//		String strDocCompCD 				= (String) objRow.get("DOC_COMP_CD");
//		String strAcctCD 					= (String) objRow.get("ACCT_CD");
//		String strAlphaNm 					= (String) objRow.get("ALPHA_NM");
//	//	String strRemark 					= (String) objRow.get("REMARK");
//		String strAddressNum 				= (String) objRow.get("ADDRESS_NUM");
//		String strDueJulia 					= (String) objRow.get("DT_DUE_JULIA");
//		String strPayInstr 					= (String) objRow.get("PAY_INSTR_CD") + " "+(String) objRow.get("PAY_INSTR_NM");
//		String strCurrencyCD 				= (String) objRow.get("CURRENCY_CD");
//		String strExchangeRate 				= (String) objRow.get("EXCHANGE_RATE");
//		String strDebitCurrencyAmount 		= (String) objRow.get("DEBIT_CURRENCY_AMOUNT");
//		String strCreditCurrencyAmount 		= (String) objRow.get("CREDIT_CURRENCY_AMOUNT");
//
//		
//		tool.DrawText(strDocPayItem, 100, nCurTextTop, 190, nCurTextBottom , tool.CENTER);
//		tool.DrawText(strDocType, 210, nCurTextTop, 280, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strDocCompCD, 280, nCurTextTop, 400, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strAcctCD, 400, nCurTextTop, 920, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strAlphaNm, 920, nCurTextTop, 1440, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strAddressNum, 1440, nCurTextTop, 1670, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strDueJulia, 1670, nCurTextTop, 1880, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strPayInstr, 1900, nCurTextTop, 2130, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strCurrencyCD, 2340, nCurTextTop, 2540, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strExchangeRate, 2540, nCurTextTop, 2880, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strDebitCurrencyAmount, 2880, nCurTextTop, 3060, nCurTextBottom , tool.RIGHT);	
//		tool.DrawText(strCreditCurrencyAmount, 3060, nCurTextTop, 3400, nCurTextBottom , tool.RIGHT);	
//		//new Line
//
//		nCurTextTop = nCurTextBottom + nFontVerticalPadding;
//		nCurTextBottom = nCurTextTop + tool.getTextHeight("");
//	
//		String strAcctNm 					= (String) objRow.get("ACCT_NM");
//		String strRemark 					= (String) objRow.get("REMARK");
//		String strSubLedger					= (String) objRow.get("SUBLEDGER");
//		String strDebitAmount 				= (String) objRow.get("DEBIT_AMOUNT");
//		String strCreditAmount 				= (String) objRow.get("CREDIT_AMOUNT");
//		
//		tool.DrawText(strAcctNm, 400, nCurTextTop, 920, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strRemark, 920, nCurTextTop, 1440, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strSubLedger, 1440, nCurTextTop, 1670, nCurTextBottom , tool.LEFT);
//		tool.DrawText(strDebitAmount, 2880, nCurTextTop, 3060, nCurTextBottom , tool.RIGHT);	
//		tool.DrawText(strCreditAmount, 3060, nCurTextTop, 3400, nCurTextBottom , tool.RIGHT);	
//		tool.DrawLine(nLineHorizontalPadding, nCurRowBottom, tool.getBackgroundWidth() - (nLineHorizontalPadding*2), 1);
//	
//		m_nCurRow++;
//
//		return tool;
//	}
//		
//	
//	private void setPageCount() {
//		
//		JSONArray arObjSection = (JSONArray) m_objData.get("Section");
//
//		int nPage = 0;
//			
//		for(int i = 0 ; i < arObjSection.size(); i++)
//		{
//			JSONObject objVItem = (JSONObject) arObjSection.get(i);
//			
//			
//			  all Sections = 2 (SectionTitle + bottom Sum)
//			 * VRow = 3 per 1 item
//			 * NMRow = 1 per 1 item
//			 * TotalSumRow = 1
//			 * PageSectionTitleROw = 1 per 1 page
//		  	 
//			int nTotalSumRow  = 0;
//			if(i >= arObjSection.size() -1)
//			{
//				nTotalSumRow = 1;
//			}
//			
//			int nHeaderRow 		= 1;
//			int nVSectionRow 	= 2;
//			int nVRowCnt 		= ((JSONArray)objVItem.get("Item")).size()/2;
//			
//			int nRowCnt		= nHeaderRow + nVSectionRow + nVRowCnt + nTotalSumRow;
//			double dPage 		= (double)nRowCnt /m_nRowPerPage;
//			int nTakingPage 	= (int)Math.ceil(dPage);
//			
//			nPage += nTakingPage;
//		}
//		
//		m_nTotalPage = nPage;
//	}
//	
//	private TempleteTool checkRowFilled(TempleteTool tool, String strDocNum) {
//		
//		if(m_nCurRow / m_nRowPerPage > 0)
//		{
//			
//			return newPage(tool, strDocNum);
//		}
//		else
//		{
//			return tool;
//		}
//	}
//	
//	private TempleteTool newPage(TempleteTool tool, String strDocNum) {
//		//Reset current row
//		m_nCurRow = 0;
//		//Save to file
//		savePage(tool, strDocNum);
//		//Increase page count
//		m_nCurPage++;
//		//Reset background draws
//		tool = new TempleteTool(m_TempletePath +  File.separator + "00.jpg");
//		
//		DrawTop(tool);
//		JSONObject objHeader = (JSONObject) m_objData.get("Header");
//		DrawHeader(tool, objHeader);	
//		DrawSectionTitle(tool);
//		
//		return tool;
//	
//	}*/
//	
//	private void savePage(TemplateTool tool) {
//		String strImagePath = m_strImageSavePath + File.separator +m_nCurPage+".jpg";
//		long lFileSize = tool.saveImageFile(strImagePath, tool.IMAGE_JPG);
//		m_nFileTotalSize += lFileSize;
//		JsonObject objJsonItem = new JsonObject();
//		//-IMPORTANT- save background image size to use on upload slip.
//		objJsonItem.addProperty("Width", tool.getBackgroundWidth());
//		objJsonItem.addProperty("Height", tool.getBackgroundHeight());
//		objJsonItem.addProperty("Size", lFileSize);
//		objJsonItem.addProperty("PageIndex", m_nCurPage);
//		objJsonItem.addProperty("Path", strImagePath);
//		m_arImageInfo.add(objJsonItem);
//	}
//	
//	//Draw Templete data
//	private void Draw(TemplateTool tool)
//	{
//		tool.setFontStyle("NanumGothic", tool.NORMAL, 14);
//
//	
//		try
//		{
//	//while(this.m_imageData.next())
//		{
//			//사업자등록번호
//			String strBizrNo = this.m_imageData.GetString("OWNR_BIZR_NO");
//			strBizrNo = strBizrNo.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
//			tool.DrawText(strBizrNo, 60, 132);
//			
//			//거래일시
//			String strUseDT = this.m_imageData.GetString("USE_DT").substring(2, this.m_imageData.GetString("USE_DT").length());
//			String strUseTM = this.m_imageData.GetString("USE_TM");
//			
//			String strFormatted = strUseDT.replaceAll("(\\d{2})(\\d{2})(\\d{2})", "$1.$2.$3") +"."+ strUseTM.replaceAll("(\\d{2})(\\d{2})(\\d{2})", "$1.$2.$3");
//			tool.DrawText(strFormatted, 60, 244);
//			
//			//결제구분
//			String strTranFG = this.m_imageData.GetString("TRAN_FG");
//			if("1".equals(strTranFG))
//			{
//				strTranFG = "승인"; 
//			}
//			else
//			{
//				strTranFG = "취소";
//			}
//			tool.DrawText(strTranFG, 60, 296);
//			
//			//공급금액
//			String strSPPRCAmt = this.m_imageData.GetString("SPPRC_AMT");
//			strSPPRCAmt = strSPPRCAmt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//			tool.DrawText(strSPPRCAmt, 324, 284, 464, 306, tool.RIGHT);
//			
//			//세액금액
//			String strTAXAmt = this.m_imageData.GetString("TAXAMT_AMT");
//			strTAXAmt = strTAXAmt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//			tool.DrawText(strTAXAmt, 324, 327, 464, 354, tool.RIGHT);
//			
//			//봉사금액
//			String strTIPAmt = this.m_imageData.GetString("TIP_AMT");
//			strTIPAmt = strTIPAmt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//			tool.DrawText(strTIPAmt, 324, 371, 464, 400, tool.RIGHT);
//			
//			//합계금액
//			String strTotalAmt = this.m_imageData.GetString("SUM_AMT");
//			strTotalAmt = strTotalAmt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//			tool.DrawText(strTotalAmt, 324, 424, 464, 430, tool.RIGHT);
//			
//			//승인번호
//			String strAprvlNo = this.m_imageData.GetString("NTS_APRVL_NO");
//			tool.DrawText(strAprvlNo, 240, 472);
//			
//			//가명점 명
//			String strFRCSName = this.m_imageData.GetString("FRCS_NM");
//			tool.DrawText(strFRCSName, 60, 526, 324, 546, tool.LEFT);
//			
//			//가맹점 사용자등록번호
//			String strSPLRNo = this.m_imageData.GetString("SELL_COMPANY_REGISTNUM");
//			strSPLRNo = strSPLRNo.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
//			tool.DrawText(strSPLRNo, 60, 580, 218, 600, tool.LEFT);
//			
//			//대표자 명
//			String strCEOName = this.m_imageData.GetString("CEO_NM");
//			tool.DrawText(strCEOName, 340, 524, 468, 544, tool.CENTER);
//			
//			//가명점 명
//			String strADDR = this.m_imageData.GetString("BASE_ADDR");
//			tool.DrawMultiLineText(strADDR, 238, 584, 460, 664,200,0,2, tool.LEFT);
//		}
//		}
//		catch(Exception e)
//		{
//			Logger.writeException("CoCard - Draw", e, 9);
//			this.m_objSlipInfo = null;
//		}
//		
//		//Save last page.
//		savePage(tool);
//		
//		m_objSlipInfo.addProperty("ImageTotalSize", m_nFileTotalSize);
//		m_objSlipInfo.add("ImageInfo", m_arImageInfo);
//		
//	}
//	
//	
//	public JsonObject getResImageInfo() {
//		return m_objSlipInfo;
//	}
//	
//}
