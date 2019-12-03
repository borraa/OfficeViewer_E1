<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/common.jsp"%>
<% 
	request.setCharacterEncoding("utf-8");
	String strErrorMsg = request.getParameter("ERR_MSG");
	if (C.isBlank(strErrorMsg))
	{
	    strErrorMsg = "PARAMETER_IS_NULL";
	}
	
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css" />">
<script>
$(function(){  		
	
	var lang = "<%=g_strLang%>";
	$.Common.Localize($.Lang , "data-i18n",lang,"Error");
	
	
});
</script>
    </head>

    <body>
        <div class="err-wrapper">
            <div class="header">
                   	ERROR
            </div>
            <div class="content">
            	<div class="area-info">
            			<div class="icon"><img src="<c:url value="/image/common/error.png "/>"/></div>
            			<div class="detail">
            				<span class="title">Error message : </span><br/><span  data-i18n="<%=strErrorMsg%>"></span>
            			</div>
            	</div>
              <%--   <hr class="err_hr"/>
                <div class="area_msg">
                    <div class="area_icon" id="area_icon"></div>
                    <div class="msg">
                        <span class="title" data-i18n="ERR_PAGE_TITLE"></span>
                        <p class="content"></p>
                    </div><br style="clear: both;"/></div>
                <hr class="err_hr"/>
                <div class="area_btn">
                    <!-- <div class="close"><span class="btn">닫기</span></div>-->
                </div>--%>
            </div> 
        </div>
    </body>
</html>
