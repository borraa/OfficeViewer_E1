package com.woonam.constants;

public class Queries {

	public final static String GET_USER_INFO = "EXEC ProcImg_user_info ?, ?, ? ";
//	public final static String GET_ATTACH_COUNT 	= " Select COUNT(c.DOC_IRN) As ATTACH_CNT From IMG_SLIPDOC_T a "
//																	+ " WITH(NOLOCK) Inner Join IMG_SLIPKIND_T b WITH(NOLOCK) On a.SDOC_KIND = b.KIND_NO "
//																	+ " Inner Join IMG_ORGFILE_T c WITH(NOLOCK) On a.SDOC_NO = c.SDOC_NO "
//																	+ " Where a.? in (?) And SDOC_STEP !='9' And a.FOLDER = 'ADDFILE' ";
//
//	public final static String GET_THUMB_COUNT 	= " Select COUNT(a.CABINET) As THUMB_CNT From IMG_SLIPDOC_T a WITH(NOLOCK) "
//																	+ " Inner Join  IMG_SLIP_T b WITH(NOLOCK) On a.SDOC_NO = b.SDOC_NO  And  a.SDOC_STEP != '9' "
//																	+ "  And  b.SLIP_FLAG != '9'   And  b.SLIP_NO = '0' Inner Join wd_IMG_SLIP_M c On "
//																	+ "  b.DOC_IRN = c.DOC_IRN Inner Join  IMG_SLIPKIND_T d WITH(NOLOCK) On a.SDOC_KIND = d.KIND_NO "
//																	+ " Where a.FOLDER='SLIPDOC' And a.? in (?) ";
//
	public final static String GET_ATTACH_LIST = "EXEC ProcImg_AddFile ?, ?, ? ";
	public final static String GET_SLIP_LIST = "EXEC ProcImg_Slip ?, ?, ?, ? ,? ";
	public final static String GET_HISTORY_LIST = "EXEC ProcImg_History ?,?, ?";
	public final static String GET_COMMENT_LIST = "EXEC ProcImg_Comment ?, ?";
	public final static String GET_IRN = "EXEC ProcImg_GetIrn ?";
	
	public final static String WRITE_COMMENT = "Insert Into IMG_COMMENT_T ( COMT_IRN, JDOC_NO, CORP_NO, PART_NO, REG_USER, COMT_TITLE, COMT_DATA, COMT_STEP, REG_TIME, UPDATE_TIME) "
															+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public final static String MODIFY_COMMENT = "Update IMG_COMMENT_T Set COMT_TITLE = ?, COMT_DATA = ? Where COMT_IRN = ? And JDOC_NO = ? And REG_USER = ?";

	public final static String REMOVE_COMMENT = "EXEC ProcImg_Comment_Del ?, ?, ?, ?";
	public final static String CHANGE_KEY = "EXEC ProcImg_JdocNo_Change ?, ?, ?, ?";
	public final static String GET_COMMENT_COUNT = "EXEC ProcImg_Comment_Cnt ?";

	public final static String REMOVE_ALL = "EXEC ProcImg_JdocNo_Del ?, ?, ?";
	public final static String REMOVE_SLIP = "EXEC ProcImg_Slip_Del ?, ?, ?, ?";
	public final static String ROTATE_SLIP = "EXEC ProcImg_Slip_Rotate ?, ?, ?";
	public final static String REMOVE_ATTACH = "EXEC ProcImg_AddFile_Del ?, ?, ?, ?";
	
	public final static String GET_ADDFILE_NAME = " Select FILENAME as FILE_NAME From " 
																	+ " wd_IMG_ORGFILE_X Where DOC_IRN = ? ";
	public final static String GET_ORIGINAL_INFO = " Select DOC_IRN, ORG_FILE From " 
			+ " IMG_ORGFILE_T Where ORG_IRN = ? And ORG_FLAG !='9' ";
	
    public final static String GET_API_SLIP_CNT = " EXEC ProcImg_GetCnt ?, ?, ? ";
    public final static String GET_API_SLIP_LIST = " EXEC ProcImg_GetList ?, ?, ?, ? ";

    public final static String GET_ORDER_CONVERT_LIST = " EXEC ProcImg_Order_ConvertList ?, ?, ?, ?, ? ";
    public final static String GET_ORDERITEM_CONVERT_LIST = " EXEC ProcImg_OrderItem_ConvertList ? ";
    public final static String GET_REPORT_CONVERT_LIST = " EXEC ProcImg_Report_ConvertList ?, ?, ?, ?, ?, ? ";
    public final static String GET_CARD_CONVERT_LIST = " EXEC ProcImg_Card_ConvertList ?, ?, ?, ?, ? ";
    public final static String GET_TAX_CONVERT_LIST = " EXEC ProcImg_Tax_ConvertList ?, ?, ?, ?, ?, ? ";
    public final static String GET_TAXITEM_CONVERT_LIST = " EXEC ProcImg_TaxItem_ConvertList ? ";
    public final static String COPY_SLIP_JDOC_NO = " EXEC ProcImg_Copy ?, ?, ?, ? ";
    public final static String GET_SLIP_INFO = " EXEC ProcImg_SlipDOC ?, ?, ? ";
       
  
    public final static String REMOVE_BY_KIND = " EXEC ProcImg_JdocNo_DelKind ?, ?, ?, ? ";
    public final static String CHANGE_STEP = " EXEC ProcImg_Step ?, ? ";
    public final static String MAPPING_CARD = " EXEC ProcImg_Card_Mapping ?, ?, ?, ? ";
     
     
     public final static String INSERT_SLIPDOC = " Insert Into IMG_SLIPDOC_T  ( CABINET, FOLDER, SDOC_NO, CORP_NO, PART_NO, REG_USER, SDOC_MONTH, SDOC_KIND, "
     		+ "SDOC_STEP, SDOC_FLAG, SDOC_URL, SDOC_AFTER, SDOC_PTI, SDOC_COPY, SDOC_SECU, SDOC_SYSTEM, SDOC_NAME, SDOC_DEVICE,"
     		+ " SLIP_CNT, REG_TIME, UPDATE_TIME, JDOC_NO, JDOC_INDEX, SDOC_FOLLOW) "
     		+ "Values ("
     		+ " getDate(), 'SLIPDOC', ?, ?, ?, ?, ?, ?, '2', '1', '0', '0', '10' ,'0', '2', '1', ?, 'PC(SVR)', ?, getDate(), getDate(), ?, '10', '0' "
     		+ ")  ";
     public final static String INSERT_SLIP = " Insert Into IMG_SLIP_T ( SLIP_IRN, CABINET, FOLDER, DOC_IRN, SDOC_NO, SLIP_NO, REG_TIME, FILE_SIZE, SLIP_TITLE, SLIP_STEP,"
     		+ " SLIP_FLAG, SLIP_RECT, SLIP_ROTATE )"
    		+ " Values ( ?, getDate(), 'SLIP', ?, ?, ?, getDate(), ?, ?, ?, ?, ?, ? )";
     public final static String VERIFY_SLIP_CNT = " Select Count(DOC_IRN) As CNT From WD_IMG_SLIP_X Where DOC_IRN In (?) ";
     public final static String VERIFY_THUMB_CNT = " Select Count(DOC_IRN) As CNT From WD_IMG_SLIP_M Where DOC_IRN In (?) "; 
   /*UnUsed*/  public final static String GET_JDOCNO_INDEX = " Select MAX(JDOC_INDEX) as JDOC_INDEX From IMG_SLIPDOC_T Where JDOC_NO = ?";
     public final static String UPDATE_CARD_STATUS = " EXEC ProcImg_Card_UpdateStatus ?, ? ";
     public final static String UPDATE_TAX_STATUS = " EXEC ProcImg_Tax_UpdateStatus ?, ? ";
     public final static String UPDATE_ORDER_STATUS = " EXEC ProcImg_Order_UpdateStatus ?, ? ";
     public final static String UPDATE_REPORT_STATUS = " EXEC ProcImg_Report_UpdateStatus ?, ? ";
     
     public final static String HISTORY_ADD = " EXEC ProcImg_History_Add ?, ?, ?, ?, ?, ?";
     public final static String COPY_REPLACE = "	EXEC ProcImg_COPY_REPLACE ?, ?, ?, ? ";
     
     public final static String GET_RECYCLE_LIST = "	 Select * From IMG_PTISVR_RESULT_T Where PTI_TYPE='04' And PTI_STATUS = '00'  ";
     
     
}
