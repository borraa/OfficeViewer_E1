<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<title>Slip Comment</title>
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
	
	var CommentParams = {
			CORP_NO 				: "<c:out value="${sessionScope.CORP_NO}" />", 
			USER_ID 				: "<c:out value="${sessionScope.USER_ID}" />", 
			LANG 						: "<c:out value="${mParams['LANG'][0]}" />",
			KEY		 				: "<c:out value="${mParams['KEY'][0]}" />"
	}
	
	$.Comment.init(CommentParams);
})
</script>
</head>
<body>
<div class="wrapper comment">
	<div class="area_comment_title">
		<div class="title_icon">
			<img src="<c:url value='/image/pc/comment/title.png' />" />
		</div>
		<div class="comment_title">
			<span class="title" data-i18n="TITLE"></span><span id="comt_cnt"></span>
		</div>
		<%-- <div class="comment_jdoc_no" id="comment_jdoc_no">
			<span class="title" data-i18n=EVIDENCE_KEY></span>(<c:out value="${mParams['JDocNo'][0]}" />)</span>
		</div> --%>
	</div>
	<div id="comtList" class="area_comment_list">
	</div>
	<div class="area_comment_write">
			<div class="area_write_title">
				<div class="header"><span class="title" data-i18n="WRITE_HEADER"></span></div>
				<input type="text" id="ipt_title" class="ipt_title" tabindex="1" />
			</div>
			<div class="area_write_content">
				<div class="header"><span class="title" data-i18n="WRITE_CONTENT"></span></div>
				<div class="content">
					<textarea class="ipt_content" id="ipt_content" tabindex="2"></textarea>
				</div>
				<div class="apply" id="apply">
					<div>
						<img src="<c:url value='/image/common/confirm.png' />" tabindex="3" />
					</div>
				</div>
			</div>
	</div>
</div>
	

<script src="<c:url value='js/view/comment.js' />"></script>
<script src="<c:url value='/js/jquery.nicescroll.min.js' />"></script>
<script src="<c:url value='js/operation.js' />"></script>
</body>
</html>