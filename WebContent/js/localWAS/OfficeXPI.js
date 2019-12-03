"use strict";
$.OfficeXPI = {
		XPI_ENUM : {
			IN_PROGRESS: 0,
			NO_DETECTED: 1,
			DETECTED: 2
		},
		xpiParams : null,
		localeMsg : null,
		xpiCommand : null,
		xpiCallback : null,
		isDetected : 0,
		browserInfo : null,
		wasURL : null,
		//환경 값 셋팅
		init : function (mapXPIParams, localeMsg)
		{
			var deferred = $.Deferred();  
			
			this.xpiParams = mapXPIParams;
			if(this.xpiParams == null)
			{
				console.log("Failed to load XPI Params.");
				
				return deferred.reject().promise();
			}
			
			this.browserInfo = $.Common.GetBrowserVersion();
			
			$.OfficeXPI.localeMsg = $.Common.Localize(localeMsg, "data-i18n", $.OfficeXPI.xpiParams.LANG,"LocalWAS");
			
			$.when($.OfficeXPI.CallLocalWAS("")).then(function(){
				deferred.resolve();			
			}).fail(function(){
				deferred.reject();
			});
			
		
		
			return deferred.promise();
		},
		CallLocalWAS : function(strParam, fnCallback)
		{
			/*if(this.isDetected == $.OfficeXPI.XPI_ENUM.NO_DETECTED)
			{
				if(confirm(m_msgJSON.CONFIRM_INSTALL))
				{
				//	window.open("./Install.jsp");
				}	
				return false;
			}*/
			var deferred = $.Deferred();  
			
			this.xpiCommand 		= strParam;
			this.xpiCallback 		= fnCallback;

		
			if(this.isDetected ==  $.OfficeXPI.XPI_ENUM.DETECTED)
			{
				this.wasURL = this.xpiParams.LOCAL_WAS_URL;
				return this.Execute($.OfficeXPI.xpiCommand, $.OfficeXPI.xpiCallback);
			}
			else
			{
				this.wasURL = this.xpiParams.LOCAL_WAS_URL +"/API/Install /PROJ'"+this.xpiParams.SERVER_KEY+"'";
			
				$.when($.OfficeXPI.CheckInstall(this.wasURL)).then(function(){
					deferred.resolve();
				})
				.fail(function() {
					deferred.reject();
				});
			}
			
			return deferred.promise();
		},
		
		CheckInstall : function(strURL)
		{
			var deferred = $.Deferred();  
			if(this.browserInfo.ActingVersion > 9)
			{

				$.ajax({
				    type : "POST",
				    url : $.OfficeXPI.wasURL, 
				    dataType : "json",
				    success : function(res) {
//				    	$.when($.OfficeXPI.Execute("", $.OfficeXPI.xpiCallback))
//						.then(function(){
//							
//							$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.DETECTED;
//							$.OfficeXPI.setCSOperation();
//							deferred.resolve();
//						})
//						.fail(function(){
//							deferred.reject();
//						});
				    	$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.DETECTED;
				    	$.OfficeXPI.setCSOperation();
						deferred.resolve();
				    },
				    error : function(e, status, error )
				    {
				    	$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.NO_DETECTED;
						deferred.reject();
				    }
				});
			}
			else
			{
				var request = new XDomainRequest();
				
				if (!request) 
				{
					console.log('XMLHTTP instance cannot be created.');
					deferred.reject();
				}	
				try
				{
					request.open("GET", strURL);
					request.onload = function() 
					{
						//If call localwas successfully
						if(!$.Common.isBlank($.OfficeXPI.xpiCommand))
						{
							$.when($.OfficeXPI.Execute($.OfficeXPI.xpiCommand, $.OfficeXPI.xpiCallback))
							.then(function(){
								
								$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.DETECTED;
								$.OfficeXPI.setCSOperation();
								deferred.resolve();
							})
							.fail(function(){
								deferred.reject();
							});
						}
					}
					
					request.ontimeout = function() 
					{ 
						$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.DETECTED;
						deferred.reject();
					}
					request.onerror = function() 
					{ 
						$.OfficeXPI.isDetected = $.OfficeXPI.XPI_ENUM.DETECTED;
						deferred.reject();
					}
				
					setTimeout(function() 
					{
						request.send();
					}, 500);
				}
				catch(exception){deferred.reject();}
			}	
			return deferred.promise();
		},
	
		Execute : function(strCommand, fnCallBack)
		{

			console.log("ajax start");
//			alert( $.OfficeXPI.wasURL  + strCommand);
			$.ajax({
			    type : "POST",
			    url : $.OfficeXPI.wasURL + strCommand , 
			  //  timeout : 5000,
			    dataType : "json",
			  //  jsonp : "callback",
			    success : function(evt) {
			    	if(fnCallBack)
			    	{		  
					//alert(evt);
			    		fnCallBack();
			    	}
			    },
			    error : function(e, status, error )
			    {
			    	console.log("status : " + e.status);
			    	console.log("err msg : " +error);
			    	console.log("res text : " +e.responseText);
			    },
			    done : function() {
			    	$.Common.HideProgress("#slip_progress");
					$.Common.HideProgress("#attach_progress");
			    }
			    
			});
		},
		setCSOperation : function()
		{
			//Change icon button
			var btnIconList = $("#list_btnLeft").find("[cs_operation=1]");
			
			$.each(btnIconList, function(){
				
				var elImg 	= $(this).children("img");
				
				if(elImg != null)
				{
					var imgURL = elImg.attr("src");
					
					var csImgURL = imgURL.substring(0,imgURL.lastIndexOf(".")) + "_cs.png";
					elImg.attr("src",csImgURL)
				}
		
			});
		},
};
