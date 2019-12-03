<%@page import="javax.servlet.annotation.WebServlet"%>
<%@page import="com.google.gson.JsonObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/common/common.jsp"%> 
<%@ page import="com.woonam.util.CookieUtil" %>
<%@ page import="com.woonam.connect.AgentConnect" %>
<%@ page import="com.woonam.model.GetModel" %>
<%
	//Prepend session disappearing bugs on IE.
	response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");

	CookieUtil cookie = new CookieUtil(request);
	
	String strKey 											= request.getParameter("KEY");
	if(C.isBlank(strKey)) strKey 						= C.FindParameterFromCookie(response, cookie, "KEY");
	
	String strKeyType 									= request.getParameter("KEY_TYPE");
	if(C.isBlank(strKeyType)) strKeyType 			= C.FindParameterFromCookie(response, cookie, "KEY_TYPE","JDOC_NO");
	
	String strServerMode 								= request.getParameter("SVR_MODE");
	if(C.isBlank(strServerMode)) strServerMode 	= C.FindParameterFromCookie(response, cookie, "SVR_MODE","");
	else strServerMode = strServerMode.toUpperCase();
	
	String strCallGroup 									= request.getParameter("CALL_GRUP");
	if(C.isBlank(strCallGroup)) strCallGroup 			= C.FindParameterFromCookie(response, cookie, "CALL_GRUP");
	
	String strViewMode 									= request.getParameter("VIEW_MODE");
	if(C.isBlank(strViewMode)) strViewMode 		= C.FindParameterFromCookie(response, cookie, "VIEW_MODE","VIEW");
	else strViewMode = strViewMode.toUpperCase();
	
	String strPage 											= request.getParameter("PAGE");
	if(C.isBlank(strPage)) strPage 					= C.FindParameterFromCookie(response, cookie, "PAGE");
	
	String strLang 											= request.getParameter("LANG");
	if(C.isBlank(strLang)) strLang 						= C.FindParameterFromCookie(response, cookie, "LANG" ,"ko");
	else strLang = strLang.toLowerCase();
	
	String strUserID 										= request.getParameter("USER_ID");
	if(C.isBlank(strUserID)) strUserID 				= C.FindParameterFromCookie(response, cookie, "USER_ID");
	
	String strCorpNo 										= request.getParameter("CORP_NO");
	if(C.isBlank(strCorpNo)) strCorpNo 				= C.FindParameterFromCookie(response, cookie, "CORP_NO");
	
	String strWorkGroup 									= request.getParameter("WORK_GROUP");
	if(C.isBlank(strWorkGroup)) strWorkGroup 		= C.FindParameterFromCookie(response, cookie, "WORK_GROUP");
	
	String strMenu		 									= request.getParameter("MENU");
	if(C.isBlank(strMenu)) strMenu 					= C.FindParameterFromCookie(response, cookie, "MENU", "1");
		
	String strUseMobile									= g_profile.getString("WAS_INFO", "MOBILE_USE", "F").toUpperCase();
	//String strMobileMode									= g_profile.getString("WAS_INFO", "MOBILE_MODE", "VIEW").toUpperCase();
	String localWAS_Port								= g_profile.getString("LOCAL_WAS",request.getScheme().toUpperCase(), "");

	String command 										= request.getParameter("COMMAND");
	String isInterface 									= request.getParameter("INTERFACE");
	
	if(C.isBlank(strKey))
  	{
		RequestDispatcher rd = request.getRequestDispatcher("slip_error.jsp?ERR_MSG=EMPTY_JDOCNO");
  	 	rd.forward(request,response);
  	 	return;
  	}
	
	//Verify UserInfo
	AgentConnect AC			= new AgentConnect(g_profile);
	GetModel GD				= new GetModel(AC, request.getSession());
	
	JsonObject objUserInfo = GD.getUserInfo(strUserID, strCorpNo, strLang);
	if(objUserInfo == null)
	{
		RequestDispatcher rd = request.getRequestDispatcher("slip_error.jsp?ERR_MSG=NO_USER_INFO");
  	 	rd.forward(request,response);
  	 	return;
	}
	
	String strResUserNM 		= null;
	String strResCorpNM		= null;
	try
	{
		String strResUserID 	= objUserInfo.get("USER_ID").getAsString();
		String strResCorpNo 	= objUserInfo.get("CORP_NO").getAsString();
		
		if(!strUserID.equalsIgnoreCase(strResUserID) || !strCorpNo.equalsIgnoreCase(strResCorpNo))
		{
	RequestDispatcher rd = request.getRequestDispatcher("slip_error.jsp?ERR_MSG=NO_USER_INFO");
	  	 	rd.forward(request,response);
	  	 	return;
		}
		
		strResUserNM = objUserInfo.get("USER_NM").getAsString();
		strResCorpNM = objUserInfo.get("CORP_NM").getAsString();
		
		
		session.setAttribute("CORP_NO", 	objUserInfo.get("CORP_NO").getAsString());
		session.setAttribute("CORP_NM", 	strResCorpNM);
		session.setAttribute("PART_NO", 	objUserInfo.get("PART_NO").getAsString());
		session.setAttribute("PART_NM", 	objUserInfo.get("PART_NM").getAsString());
		session.setAttribute("USER_ID", 		strUserID);
		session.setAttribute("USER_NM", 	strResUserNM);
		session.setAttribute("USER_LANG",	strLang.toLowerCase());
		session.setAttribute("VIEW_MODE",	strViewMode);
	
	}
	catch(Exception e)
	{
		logger.error("Failed to get user info.", e);
		RequestDispatcher rd = request.getRequestDispatcher("slip_error.jsp?ERR_MSG=NO_USER_INFO");
  	 	rd.forward(request,response);
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
</head>
<script>

$(function(){
	
	g_ViewMode = "<%=strViewMode %>";
	
	var objParams = {};
	objParams["KEY"] 					= "<%=strKey %>";
	objParams["KEY_TYPE"] 		= "<%=strKeyType %>";
	objParams["SVR_MODE"] 		= "<%=strServerMode %>";
	objParams["CALL_GROUP"] 		= "<%=strCallGroup %>";
	objParams["VIEW_MODE"] 		= "<%=strViewMode %>";
	objParams["PAGE"] 				= "<%=strPage %>";
	objParams["LANG"] 				= "<%=strLang %>";
	objParams["WORK_GROUP"] 	= "<%=strWorkGroup %>";
	objParams["USER_ID"] 			= "<%=strUserID %>";
	objParams["CORP_NO"] 			= "<%=strCorpNo %>";
	objParams["USER_NM"] 			= "<%=strResUserNM %>";
	objParams["CORP_NM"] 			= "<%=strResCorpNM %>";
	objParams["MENU"] 				= "<%=strMenu %>";
	objParams["XPI_PORT"]			= "<%=localWAS_Port%>";

	var useMobile = "<%=strUseMobile%>";
	var targetURL = "<c:url value='/slip_actor.jsp' />";
	
	//If it's mobile mode
	if($.Common.determineMobile() && "T" === useMobile)
	{
<%-- 	//	objParams['VIEW_MODE'] = "<%=strMobileMode%>"; --%>
		targetURL 					= "<c:url value='/slip_mobile.jsp' />";
		
	}
	
	if("1" == "<%=isInterface%>") {
		switch("<%=command%>") {
		case "VIEW_ORIGINAL_SLIP" : 
			targetURL = "<c:url value ='/slip_viewer.jsp' />";
		}
	}
	
	$.Common.postSubmit(targetURL, objParams, "post");
});
</script>
</html>