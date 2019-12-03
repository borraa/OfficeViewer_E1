 <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/style.css' />" >
<script src='<c:url value="/js/jquery-ui.js" />'></script>
<script type="text/javascript" src="<c:url value="/js/Sample/Sample.js"/>"></script>
<title>SlipActor Sample for Web</title>
</head>
<%
	String strProtocol 		= request.getScheme().toUpperCase();
	/* String strURL			= g_profile.getString("DEV", "IP", "");
	String localWAS_Port	= g_profile.getString("LOCAL_WAS",request.getScheme().toUpperCase(), "");	
		
	String strURL 			= g_profile.getString("URL", strProtocol, "");
	pageContext.setAttribute("URL", strURL); */
%>
<script>
$(function(){

	<%-- var strURL 	= "<%=strURL%>"; --%>

	var WebParams = {
		XMLDATA_PATH	: "<c:url value='/Sample/Menu.xml' />",
		WEB_URL			: "./"
	}
	/* 
	var url = location.href;
	url.hostname;  //  'example.com'
	url.port;      //  12345
	url.search;    //  '?startIndex=1&pageSize=10'
	url.pathname;  //  '/blog/foo/bar'
	url.protocol;  //  'http:' */

	$.Sample.init(WebParams);

	$("#frameCloseBtn").click(function() {
		$.Sample.fnBtnClose();
	});
});

</script>

<body>
	<div class="wrapper">	
		<div class="sample_header">
			<div class="sample_content">
				<div class="logo"></div>
				<div id="topNavi" class="top_navi"></div>
				<div class="searchBox" id="searchBox">
					<!-- <input type="text" id="sample_search" value="" title="Search"> -->
				</div>				
			</div>
		</div>
			
		<div class="sample_body" style="padding-top: 15px;">
			<div>
				<div class="area_api" id="section_area_api"></div>
			</div>
		</div>
		
		<div class="frameBody">
			<div id="frameCloseBtn"></div>
			<div id="frameMenu"> 
				<iframe name="OfficeXPIFrm" id="OfficeXPIFrm" border="0" width="100%"  height="700px"></iframe>
				<!-- <div class="frameCloseBtn"></div> -->
			</div>
			<!-- <div class="frameBtn"></div> -->
		</div>
			
	</div>
</body>
</html>