<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<%@page import="java.util.Map"%> 
<html>
<head>
<title>Slip Actor</title>
<link rel="stylesheet" type="text/css" href="<c:url value='css/style.css' />" >
<link rel="stylesheet" type="text/css" href="<c:url value='css/menu/context-menu.css' />" >
<%
	//Generate system id
	String strSysID			=	null;
	String strCurTime			=	new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
	strSysID						=	session.getId()+strCurTime;
	
	//Attach parameters to pageContext
	Map<String, String[]> mParams = request.getParameterMap();
	pageContext.setAttribute("mParams", mParams);
	

	String strServerKey 						= g_profile.getString(request.getParameter("SVR_MODE"),"KEY", "");	
	pageContext.setAttribute("SERVER_KEY", strServerKey);
	
	String key = request.getParameter("KEY");
	
	//Detect whether multikey.
	boolean isMultiKey = false;
	if(!C.isBlank(key)) {
		if(key.indexOf(',') > -1) isMultiKey = true;
	}
	
	pageContext.setAttribute("MULTI_KEY", isMultiKey);

%>
<script>

$(function(){
		
	var ActorParams = {
		
			CORP_NO 				: "<c:out value="${sessionScope.CORP_NO}" />", 
			USER_ID 				: "<c:out value="${sessionScope.USER_ID}" />", 
			PART_NO 				: "<c:out value="${sessionScope.PART_NO}" />", 
			SERVER_KEY 			: "<c:out value="${SERVER_KEY}" />",	
			LANG 						: "<c:out value="${mParams['LANG'][0]}" />",
			KEY		 				: "<c:out value="${mParams['KEY'][0]}" />",
			KEY_TYPE		 		: "<c:out value="${mParams['KEY_TYPE'][0]}" />",
			VIEW_MODE			: "<c:url value ="${mParams['VIEW_MODE'][0]}" />",
			SVR_MODE				: "<c:url value ="${mParams['SVR_MODE'][0]}" />",
			MENU						: "<c:url value ="${mParams['MENU'][0]}" />",
			XPI_PORT				: "<c:url value ="${mParams['XPI_PORT'][0]}" />",
			APPR					: "<c:url value ="${mParams['APPR'][0]}" />",
			MULTI_KEY				: <c:out value="${MULTI_KEY}" />,	
	}
	
	
	
	$.Actor.init(ActorParams);
	
})
</script>
</head>

<c:set var="ViewMode" value="${mParams['VIEW_MODE'][0]}" />
<c:set var="KeyType" value="${mParams['KEY_TYPE'][0]}" />
<c:set var="Key" value="${mParams['KEY'][0]}" />
<c:set var="isMultiKey" value="${MULTI_KEY}" />


<body class="actor_body">
	<div id="area_progress" class="area_progress"></div>
	<div class="wrapper">
		<div class="actor_left" id="list_btnLeft">
			<div id="btn_open_menu" >
				<img src="<c:url value="/image/pc/actor/btn_menu.png" />" />
			</div>
			<c:if test="${ViewMode eq 'EDIT' or ViewMode eq 'AFTER'}"> 
				<div id="btn_add_slip"  cs_operation="1" >
					<img src="<c:url value="/image/pc/actor/btn_add_slip.png" />" />
				</div>
			</c:if>
			<c:if test="${KeyType eq 'JDOC_NO' }">
				<div id="btn_open_comment" onclick="javascript:$.Operation.execute($.Actor, this);" command="OPEN_COMMENT" >
					<img src="<c:url value="/image/pc/actor/btn_open_comment.png" />" />
				</div> 
				<div id="btn_open_history" onclick="javascript:$.Operation.execute($.Actor, this);" command="OPEN_HISTORY"  >
					<img src="<c:url value="/image/pc/actor/btn_open_history.png" />" />
				</div> 
			</c:if>
			<c:if test="${ViewMode eq 'EDIT' or ViewMode eq 'AFTER'}">
				<div id="btn_remove" >
					<img src="<c:url value="/image/pc/actor/btn_remove.png" />" 	/>
				</div>
			</c:if>
			<div id="btn_print" onclick="javascript:$.Operation.execute($.Actor, this);" cs_operation="1" command="PRINT_SLIP">
				<img src="<c:url value="/image/pc/actor/btn_print.png" />" />
			</div> 
		</div>
		<div class="actor_right">
			<div class="slip_wrapper">
				<div class="slip_title">
					<div class="slip_title_left">
						<div class="cb_slip_all">
							<label class="cb_container">
							  <input type="checkbox" id="slip_checkAll">
							  <span class="checkbox"></span>
							</label>
						</div>
						<div class="area_title">
							<span class="evidence" data-i18n="E-EVIDENCE">
								</span>
						</div>
						<div class="area_key">
						<div class="key_title">
							-&nbsp;<span data-i18n="EVIDENCE_KEY"></span> :
							</div>
							<c:choose>
								<c:when test="${isMultiKey eq true }">
								<div class="area_key_select">
									<select class="key_select" onchange="javascript:$.Actor.change_GroupKey(this);">
										<option value="">ALL</option> 
										<c:forEach items="${fn:split(Key, ',') }" var="curKey">
										<option value="<c:out value="${curKey}"></c:out>">${curKey}</option> 
										</c:forEach>
									</select>
									</div>
								</c:when>
								<c:otherwise>
									<div class="key" id="key" title="${Key}" >
										<c:out value="${Key}"></c:out>
									</div>
								</c:otherwise>
							</c:choose>
							
						</div>
					</div>
					<div class="slip_title_right">
						<div class="icon-btn" onclick="javascript:$.Actor.ZoomIn_Thumb();">
							<img src="<c:url value="/image/pc/actor/zoom_in.png" />" />
						</div>
						<div class="icon-btn" onclick="javascript:$.Actor.ZoomOut_Thumb();">
							<img src="<c:url value="/image/pc/actor/zoom_out.png" />" />
						</div>
					</div>
				</div>
				<div id="area_slip" class="area_slip">
					<div>
						<div id="slip_progress" class="slip_progress"></div>
						<div id="slip_masonry" class="slip_masonry"></div>
					</div>
				</div>
			</div>
			<div id="dragBar" dragging="0"></div>
			<div class="attach_wrapper">
				<div class="attach_title">
					<div class="attach_title_left">
					<div class="cb_attach_all">
							<label class="cb_container">
							  <input type="checkbox" id="attach_checkAll">
							  <span class="checkbox"></span>
							</label>
						</div>
						<div class="area_title">
							<span class="evidence" data-i18n="ATTACH"></span>
						</div>
					</div>		
					<div class="attach_title_right">
						<div class="area_maximize" onclick="javascript:$.Actor.Adjust_Size()" >
							<img src="<c:url value="/image/common/height-adjust.png" />" />
						</div>
						<%-- <div class="area_maximize" onclick="javascript:$.Actor.maximize()" >
							<img src="<c:url value="/image/common/maximize.png" />" />
						</div>
						<div class="area_minimize" onclick="javascript:$.Actor.minimize()" >
							<img src="<c:url value="/image/common/minimize.png" />" />
						</div> --%>
					</div>			
				</div>
				<div id="attach_progress" class="attach_progress"></div>
				<div id="area_attach" class="area_attach"></div>
			</div>
		</div>
	</div>
	
<script src="<c:url value='js/localWAS/OfficeXPI.js' />"></script>
<script src="<c:url value='js/view/actor.js' />"></script>
<script src="<c:url value='js/effect/ripple.js' />"></script>
<script src="<c:url value='js/masonry/masonry.pkgd.min.js' />"></script>
<script src="<c:url value='js/masonry/imagesloaded.pkgd.min.js' />"></script>
<script src="<c:url value='js/menu/context-menu.js' />"></script>
<script src="<c:url value='js/operation.js' />"></script>
<script src="<c:url value='/js/jquery.nicescroll.min.js' />"></script>
</body>
</html>