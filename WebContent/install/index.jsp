<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/install.css" />" />

<title>Download</title>
<%
	
%>
<script>

function DownloadFile()
{
	self.location.href= g_RootURL + "UpdateFiles/SetupSLIP.exe";
}
function DownloadAndroid()
{
	self.location.href=vDownloadURL + "UpdateFiles/OfficeSLIP.apk";
}

</script>
</head>
<body>
	<div class="wrapper">
		<div class="main_title"></div>
		<div class="main_content">
			<div class="areaLeft">
				<img src="<c:url value="/image/common/scanner.png" />" />
			</div>
			<div class="areaRight">
				<div class="title">전자증빙 시스템 다운로드</div>
				<div class="content">
					<div class="description">
						* Windows 7이상은 다운로드 후 "관리자 권한으로 실행"해 주세요.<br> * 시스템은 설치 후에 꼭
						재부팅을 완료해야 합니다.<br> * 브라우져에서 애플리케이션 실행관련 보안 메세지가 나타나면,<br>
						다시메세지가 나타나지 않게 체크or체크해제 하시고 "실행"을 눌러주세요
					</div>
					<div class="areaDownload">
						<div class="area_btn"  onclick="DownloadFile();">
							<span class="btn"> 다운로드
							</span>
						</div>
						<span class="platform"> Windows 플랫폼 </span>
					</div>
					
				</div>

			</div>
		</div>
	</div>

</body>
</html>