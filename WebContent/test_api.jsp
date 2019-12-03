<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/common/common.jsp"%> 


<html>
<script>
$(function(){
	var url = "http://woonamsoft01.iptime.org:14480/OfficeViewer/services/API/Run?COMMAND=GET_COUNT&KEY=test1";
	$.ajax({
		url : url,
        type:'post',
        contentType: 'application/json; charset=UTF-8', 
        dataType: 'json',
        success:function(result){
        	
        	console.log(result);
        }
        });
})
</script>

</html>