<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE html>

<html>
<head>
<title>Insert title here</title>
</head>
<script>

/* var objRequestParams = {Command: strCommand}; //Array 

$.each(objParam, function(key, value){
	objRequestParams[key] = value;
});

var deferred = $.Deferred();

$.ajax({
	url:strURL,
    type:'post',
    data:objRequestParams,
    traditional:true,
    contentType: 'application/x-www-form-urlencoded; charset=UTF-8', 
    dataType: 'json',
	success: function(data) {
        deferred.resolve(data);
    },
    error: function(error) {
        deferred.reject(error);
    }
}); */

$.Multipart = {
		Run : function(element) {
			// FormData 객체 생성
			
		//	var form = $("#multiForm")[0];
		  //  var formData = new FormData(form);
		    
		  
		//  	var file = ;
		
		  var formData = new FormData();
		  formData.append("Command","MULTIPART_TEST");
		  formData.append("TITLE",encodeURIComponent($("#title").val()));
		  
			
			$.each($("#file").prop('files'), function(i){
				formData.append("FILE_"+i,this);
			});
			
			$.ajax({
				url: g_RootURL + "/MultipartCommand.do",
	            type:'post',
	            data:formData,
	            traditional:true,
	              dataType: 'json',
	              enctype: 'multipart/form-data',
		            contentType: false, 
		        	 cache:false,
	        	 processData: false,
		    	success: function(data) {
		        //    deferred.resolve(data);
		        },
		        error: function(error) {
		       //     deferred.reject(error);
		        }
			});
			
		


		}
}

</script>
<body>

<form action="<c:url value='/MultipartCommand.do' />" method="post" id="multiForm" enctype="multipart/form-data">
  	Title : <input type="text" name="title" id="title" /><br>
    File : <input type="file" name="file" id="file" multiple="multiple" /> <br>
    <input type="button" value="Send" onclick="javascript:$.Multipart.Run()" />
</form>
</body>
</html>