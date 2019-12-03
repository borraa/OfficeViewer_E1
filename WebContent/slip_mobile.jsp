<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<%@page import="java.util.Map"%> 
<html>
<head>
<title>Slip Mobile</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
</head>

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
	
	var MobileParams = {
			CORP_NO 				: "<c:out value="${sessionScope.CORP_NO}" />", 
			USER_ID 				: "<c:out value="${sessionScope.USER_ID}" />", 
			USER_NM 				: "<c:out value="${sessionScope.USER_NM}" />", 
			PART_NO 				: "<c:out value="${sessionScope.PART_NO}" />", 
			PART_NM 				: "<c:out value="${sessionScope.PART_NM}" />", 
			SERVER_KEY 			: "<c:out value="${SERVER_KEY}" />",	
			LANG 						: "<c:out value="${mParams['LANG'][0]}" />",
			KEY		 				: "<c:out value="${mParams['KEY'][0]}" />",
			KEY_TYPE		 		: "<c:out value="${mParams['KEY_TYPE'][0]}" />",
			VIEW_MODE			: "<c:url value ="${mParams['VIEW_MODE'][0]}" />",
			SVR_MODE				: "<c:url value ="${mParams['SVR_MODE'][0]}" />",
			MENU						: "<c:url value ="${mParams['MENU'][0]}" />",
			XPI_PORT				: "<c:url value ="${mParams['XPI_PORT'][0]}" />",
			MULTI_KEY				: <c:out value="${MULTI_KEY}" />,	
	}
	
    $.Mobile.init(MobileParams);
	
})
</script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mobile/style.css' />"> 
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mobile/slide-menu.css' />"> 
<link rel="stylesheet" type="text/css" href="<c:url value='css/menu/context-menu.css' />" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mobile/photoswipe/photoswipe.css' />"> 
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mobile/photoswipe/default-skin/default-skin.css' />"> 
<c:set var="ViewMode" value="${mParams['VIEW_MODE'][0]}" />
<c:set var="KeyType" value="${mParams['KEY_TYPE'][0]}" />
<c:set var="Key" value="${mParams['KEY'][0]}" />
<c:set var="isMultiKey" value="${MULTI_KEY}" />

<body>
    <nav>
    	<div class='navi-wrapper'>
    		<div class="navi-area-close">
    			<span id="navi-close" class="ssm-toggle-nav">
    				<img  src="<c:url value='/image/common/x.png' /> "/>
   				</span>
   			</div> 
    		<div class="nav-user-info">
    			<div id="nav-user-info"></div>
    			<div class="navi-bar"></div>
    			<div id="nav-part-info"></div>
    		</div>
    		<div class="navi-menu">
		    	<div id="navi-open-comment" class="cs-ripple" onclick="$.Mobile.Open_Comment()">
		    		코멘트 보기 
		    	</div>
		    	<div id="navi-open-history" class="cs-ripple" onclick="$.Mobile.Open_History()">
		    		히스토리 보기
		    	</div>
	    	</div>
    	</div>
    </nav>
    <main>
    	<div class="top-navi">
    		<div class="navi-left-btn">
	    		 <a class="ssm-toggle-nav" id="btn-menu"  href="#"> 
	    			<img src="<c:url value="/image/pc/actor/btn_menu.png" />" />
	    		 </a> 
    		</div>
    		<div class="navi-title"><span class="evidence" data-i18n="E-EVIDENCE"></span></div>
    		<div class="navi-right-btn"></div>
    	</div>
    	<div class="area-title">
    		<div class="key-title">
    			<span class="evidence" data-i18n="EVIDENCE_KEY"></span>
    		</div>
    		<div class="key-content">
	    		<c:choose>
					<c:when test="${isMultiKey eq true }">
						<select class="key_select" onchange="javascript:$.Mobile.change_GroupKey(this);">
							<option value="">ALL</option> 
							<c:forEach items="${fn:split(Key, ',') }" var="curKey">
							<option value="<c:out value="${curKey}"></c:out>">${curKey}</option> 
							</c:forEach>
	
						</select>
					</c:when>
					<c:otherwise>
						<span class="key" id="key" >
							<c:out value="${Key}"></c:out>
						</span>
					</c:otherwise>
				</c:choose>
			</div>
    	</div>
    	<div class="area-menu" id="btn-main-viewpager">
    		<div id="show-slip" class="menu-slip " flag="slip" onclick="$.Mobile.Display_MainView(this);">
    			<span class="evidence" data-i18n="SLIP"></span>
    		</div>
    		<div id="show-attach" class="menu-attach" flag="attach" onclick="$.Mobile.Display_MainView(this);">
    			<span class="evidence" data-i18n="ATTACH"></span>
    		</div>
    	</div>
    	
    	<div class="view_wrapper" id="view-wrapper">
	    	<div class="slip_wrapper" id="slip-wrapper" >
	    			<div id="slip_progress"></div>
	    			<div id="slip_masonry" class="slip_masonry"></div>
	    	</div>
    	
	    	<div class="attach_wrapper"  id="attach-wrapper"  >
	    			<div id="attach_progress"></div>
	    			<div id="area-attach"  class="area-attach"></div>
	    	</div>
        
    	</div>
    	
    	<!-- Root element of PhotoSwipe. Must have class pswp. -->
	<div class="pswp" tabindex="-1" role="dialog" aria-hidden="true">
	
	    <!-- Background of PhotoSwipe. 
	         It's a separate element as animating opacity is faster than rgba(). -->
	    <div class="pswp__bg"></div>
	
	    <!-- Slides wrapper with overflow:hidden. -->
	    <div class="pswp__scroll-wrap">
	
	        <!-- Container that holds slides. 
	            PhotoSwipe keeps only 3 of them in the DOM to save memory.
	            Don't modify these 3 pswp__item elements, data is added later on. -->
	        <div class="pswp__container">
	            <div class="pswp__item"></div>
	            <div class="pswp__item"></div>
	            <div class="pswp__item"></div>
	        </div>
	
	        <!-- Default (PhotoSwipeUI_Default) interface on top of sliding area. Can be changed. -->
	        <div class="pswp__ui pswp__ui--hidden">
	
	            <div class="pswp__top-bar">
	
	                <!--  Controls are self-explanatory. Order can be changed. -->
	
	                <div class="pswp__counter"></div>
	
	                <button class="pswp__button pswp__button--close" title="Close (Esc)"></button>
	
	                <button class="pswp__button pswp__button--share" title="Share"></button>
	
	                <button class="pswp__button pswp__button--fs" title="Toggle fullscreen"></button>
	
	                <button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>
	
	                <!-- Preloader demo https://codepen.io/dimsemenov/pen/yyBWoR -->
	                <!-- element will get class pswp__preloader--active when preloader is running -->
	                <div class="pswp__preloader">
	                    <div class="pswp__preloader__icn">
	                      <div class="pswp__preloader__cut">
	                        <div class="pswp__preloader__donut"></div>
	                      </div>
	                    </div>
	                </div>
	            </div>
	
	            <div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">
	                <div class="pswp__share-tooltip"></div> 
	            </div>
	
	            <button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)">
	            </button>
	
	            <button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)">
	            </button>
	
	            <div class="pswp__caption">
	                <div class="pswp__caption__center"></div>
	            </div>
	        </div>
	    </div>
	</div>
    </main>
    <div class="ssm-overlay ssm-toggle-nav"></div>
</body>
<script src="<c:url value='/js/view/mobile.js' />"></script>
<script src="<c:url value='/js/mobile/touch-swipe.min.js' />"></script>
<script src="<c:url value='/js/mobile/slide-menu.min.js' />"></script>
<script src="<c:url value='/js/menu/context-menu.js' />"></script>
<script src="<c:url value='/js/effect/ripple.js' />"></script>
<script src="<c:url value='/js/operation.js' />"></script>
<script src="<c:url value='/js/masonry/masonry.pkgd.min.js' />"></script>
<script src="<c:url value='/js/masonry/imagesloaded.pkgd.min.js' />"></script>
<script src="<c:url value='/js/mobile/photoswipe/photoswipe-ui-default.min.js' />"></script>
<script src="<c:url value='/js/mobile/photoswipe/photoswipe.min.js' />"></script>
</html>