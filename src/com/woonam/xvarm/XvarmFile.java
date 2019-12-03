package com.woonam.xvarm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;
import com.windfire.apis.asys.asysUsrSql;
import com.windfire.apis.data.asysDataResult;
import com.woonam.util.Profile;


public class XvarmFile {
	
	private Profile 			profile				=	null;
	public String 				m_strXvarmGateWay	=	null;
	public asysConnectData		conXvarm = null;
	
	
	
	public XvarmFile(Profile profile)
	{
		this.profile = profile;
		
		String strSvrMode		=	profile.getString("AGENT_INFO", "SERVER", "");
		String strXvarm			=	strSvrMode + "_XVARM";
		String xvarm_ip 		= 	profile.getString(strXvarm, "XVARM_IP","");
		int xvarm_port			= 	Integer.parseInt(profile.getString(strXvarm,"XVARM_PORT","2102"));
		String xvarm_client 	= 	profile.getString(strXvarm, "XVARM_CLIENT","");
		String xvarm_id 		= 	profile.getString(strXvarm, "XVARM_ID","");
		String xvarm_pw 		= 	profile.getString(strXvarm, "XVARM_PW","");
		this.m_strXvarmGateWay	=	profile.getString(strXvarm, "XVARM_GATEWAY", "XVARM_MAIN");
		conXvarm = new asysConnectData(xvarm_ip, xvarm_port, xvarm_client, xvarm_id, xvarm_pw);
		
	}
	
	public byte[] Download(String _wdDocIrn, String _wdDocNo, String _DocTable)
	{
		String 	 strWhere	=	"doc_irn="+_wdDocIrn+";page_num="+_wdDocNo;
		String 	 strFileInfo = umGetElementId(_DocTable, strWhere);
		String[] arrFileinfo	=	strFileInfo.split(";");
		String	 xvarm_eid		=	arrFileinfo[0];
		
		ByteArrayOutputStream baosFile	=	exportFiletoByte(xvarm_eid);
		
		return baosFile.toByteArray();
	}

	private ByteArrayOutputStream exportFiletoByte(String strElementId)
  	{
  		boolean bRet = false;
  		if (conXvarm == null) {
			//m_strError = "FILE DOWNLOAD FAIL : The connection require.";
			bRet = false;
		}
		if (strElementId.trim().equals("")) {
			//m_strError = "FILE DOWNLOAD FAIL : There are no ELEMENTID.";
			bRet = false;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try 
		{
			asysUsrElement aue = new asysUsrElement(conXvarm);
			aue.m_elementId = "XVARM_MAIN::" + strElementId.trim() + "::IMAGE";
			int i_Return = aue.getContent(out, "", "");//getContent(out,"");
			
			if (i_Return != 0)
			//	m_strError = "FILE DOWNLOAD FAIL : " + aue.getLastError();
			aue = null;
		} catch(Exception e) {
			//m_strError = "FILE DOWNLOAD FAIL[E] : " + e.getMessage();
		}
		finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return out;
  	}
	
	/** 
	 * 파일을 다운로드 한다.
	 *
	 * @param strIndexId 		색인 프로필 ID.
	 * @param strIndexInfo 		색인 정보 (ex: field1=value1;field2=value2;....)
	 * @param strLocalFilePath	입력할 파일의 완전한 경로
	 * return 성공시 true, 실패시 false (오류 메시지: m_strError)
	 */
	public boolean exportFileByIndex(String strIndexId, String strIndexInfo, String strLocalFilePath) {
		if (conXvarm == null) {
		//	m_strError = "FILE DOWNLOAD FAIL : The connection require.";
			return false;
		}
		if (strIndexId.trim().equals("")) {
		//	m_strError = "FILE DOWNLOAD FAIL : There are no ELEMENTID.";
			return false;
		}
		if (strLocalFilePath.equals("")) {
		//	m_strError = "FILE DOWNLOAD FAIL : There are no file information";
			return false;
		}
		
		if (!isExistFolder(strLocalFilePath, true)) {
		//	m_strError = "FILE DOWNLOAD FAIL : There are no directory for file download. " + strLocalFilePath;
			return false;
		}
		
		String strElemId = umGetElementId(strIndexId, strIndexInfo);
		
		if (strElemId.equals(""))
			return false;

		return exportFile(strElemId, strLocalFilePath);
	}
	
	/**
	 * 파일을 다운로드한다.
	 *
	 * @param strElementId 		등록된 파일의 elementid.
	 * @param strLocalFilePath	다운로드하여 저장하기 위한 완전한 경로
	 * return 성공시 true, 실패시 false (오류 메시지: m_strError)
	 */
	public boolean exportFile(String strElementId, String strLocalFile) {
		if (conXvarm == null) {
//			m_strError = "FILE DOWNLOAD FAIL : The connection require.";
			return false;
		}
		if (strElementId.trim().equals("")) {
//			m_strError = "FILE DOWNLOAD FAIL : There are no ELEMENTID.";
			return false;
		}
		if (strLocalFile.trim().equals("")) {
//			m_strError = "FILE DOWNLOAD FAIL : There are no file information";
			return false;
		}

		if (!isExistFolder(strLocalFile, true)) {
//			m_strError = "FILE DOWNLOAD FAIL : There are no directory for file download. " + strLocalFile;
			return false;
		}
		
		try {
			asysUsrElement aue = new asysUsrElement(conXvarm);
			aue.m_elementId = "XVARM_MAIN::" + strElementId.trim() + "::IMAGE";
			int i_Return = aue.getContent(strLocalFile);
			if (i_Return != 0)
			//	m_strError = "FILE DOWNLOAD FAIL : " + aue.getLastError();
			aue = null;
			if (i_Return == 0)
				return true;
			else
				return false;
		} catch(Exception e) {
		//	m_strError = "FILE DOWNLOAD FAIL[E] : " + e.getMessage();
			return false;
		}
	}
	
	// 폴더가 존재하는지 검사
  	public static boolean isExistFolder(String strPath, boolean bFile) {
  		File objFolder = null;
  		boolean bExist = false;
  		
  		if (bFile) {
  			File objFile = new File(strPath);
  			objFolder = new File(objFile.getParent());
  			objFile = null;
  		} else
  			objFolder = new File(strPath);

	    if (null != objFolder)
	    	bExist = objFolder.exists();

		objFolder = null;
		return bExist;
  	}
  	
	
	
	private String umGetElementId(String strIndexId, String strIndexInfo) {
		//확인쿼리를 작성한다.
		String s_SQL = "SELECT COUNT(*) FROM " + strIndexId;
		String s_SQLWhere = "";
		
		String[] arrTemp = strIndexInfo.split(";");
		for (int i=0; i < arrTemp.length; i++) {
			String[] arrTemp2 = arrTemp[i].split("=");

			if (2 != arrTemp2.length)
				continue;

			if (s_SQLWhere.equals("")) {
				s_SQLWhere = " WHERE " + arrTemp2[0] + " = '" + arrTemp2[1] + "'";
			} else {
				s_SQLWhere = s_SQLWhere + " AND " + arrTemp2[0] + " = '" + arrTemp2[1] + "'";
			}
		}
		
		//simplesql에 연결하여 대상건을 카운트 한다. (반드시 indexid = table name 인 경우에만 가능하다.)
		int i_Count = umGetCount(s_SQL+s_SQLWhere);
		if (i_Count < 0) {
			return "";
		} else if(i_Count == 0) {
	//		m_strError = "INDEX SEARCH FAIL : There are no data for the sql conditions.";
			return "";
		} else if(i_Count > 1) {
		//	m_strError = "INDEX SEARCH FAIL : There are 1 more files for the sql conditions.";
			return "";
		}
		
		s_SQL = "SELECT ElementId, file_name FROM " + strIndexId;
		String strElemId = umGetDBString(s_SQL+s_SQLWhere);
		
		if (null == strElemId)
			strElemId = "";
		
//		if (strElemId.equals("")) {
//			return "";
//		}
		return strElemId.trim();
	}
	

	private String umGetDBString(String strSQL) {
		String s_Return = "";
		if (strSQL.equals("")) {
		//	m_strError = "SQL FAIL : There are no sql contidions.";
			return "";
		}
		
		try {
			asysUsrSql altusrsql = new asysUsrSql(conXvarm);
			asysDataResult altdataresult = null;
			int ret = altusrsql.retrieve("SIMPLESQL_MAIN", strSQL, 1);
			if ( ret == 0 ) {
		        altdataresult = altusrsql.getResult();
		        for(boolean flag = altdataresult.nextRow(); flag; flag = altdataresult.nextRow())
		        	s_Return = altdataresult.getColValue(0).trim()+";"+altdataresult.getColValue(1).trim();
				altdataresult.close();
			} else {
			//	m_strError = "SQL FAIL : " + m_conn.getLastError();
			}
			altdataresult = null;
			altusrsql = null;
		} catch(Exception e) {
		//	m_strError = "SQL FAIL[E] : " + e.getMessage();
		}
		return s_Return;
	}
	
	private int umGetCount(String strSQL) {
		int i_Return = -1;
		
		if (strSQL.equals("")) {
			//m_strError = "QUERY FAIL : There are no sql contidions.";
			return -1;
		}
		
		try {
			asysUsrSql altusrsql = new asysUsrSql(conXvarm);
			asysDataResult altdataresult = null;
			int ret = altusrsql.retrieve("SIMPLESQL_MAIN", strSQL, 1);
			if ( ret == 0 ) {
		        altdataresult = altusrsql.getResult();
		        for(boolean flag = altdataresult.nextRow(); flag; flag = altdataresult.nextRow())
		        	i_Return = Integer.parseInt(altdataresult.getColValue(0).trim());
				altdataresult.close();
			} else {
				//m_strError = "QUERY FAIL : " + altusrsql.getLastError();
			}
			altdataresult = null;
			altusrsql = null;
		} catch(Exception e) {
			//m_strError = "QUERY FAIL[E] : " + e.getMessage();
		}
		return i_Return;
	}
}
