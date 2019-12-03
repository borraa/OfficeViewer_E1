<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<title>Slip History</title>
<link rel="stylesheet" type="text/css" href="<c:url value='css/grid/jsgrid-theme.css' />" >
<link rel="stylesheet" type="text/css" href="<c:url value='css/grid/jsgrid.css' />" >
<link rel="stylesheet" type="text/css" href="<c:url value='css/style.css' />" >
<%
	//Generate system id
	String strSysID			=	null;
	String strCurTime			=	new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
	strSysID						=	session.getId()+strCurTime;
	
	//Attach parameters to pageContext
	Map<String, String[]> mParams = request.getParameterMap();
	pageContext.setAttribute("mParams", mParams);

%>
<script>
$(function(){
	
	var historyParams = {
			CORP_NO 				: "<c:out value="${sessionScope.CORP_NO}" />", 
			USER_ID 				: "<c:out value="${sessionScope.USER_ID}" />", 
			LANG 						: "<c:out value="${mParams['LANG'][0]}" />",
			KEY		 				: "<c:out value="${mParams['KEY'][0]}" />"
	}
	
	$.History.init(historyParams);
	
	
})
</script>
</head>
<body>
<div id="history_progress" class="area_progress"></div>
<div class="wrapper history">
	<div class="area_history_title">
		<div class="title_icon">
			<img src="<c:url value='/image/pc/history/title.png' />" />
		</div>
		<div class="history_title">
			<span class="title" data-i18n="PAGE_TITLE"></span>
		</div>
	</div>
	
	<div id="history_list"  class="history_list">
	</div>
</div>
	
<script src="<c:url value='js/view/history.js' />"></script>
<script src="<c:url value='js/grid/jsgrid.js' />"></script>
</body>
</html>