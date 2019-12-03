"use strict";

var StringBuffer = function() 
{
			    this.buffer = new Array();
};
StringBuffer.prototype.append = function(str) 
{
    this.buffer[this.buffer.length] = str;
};
StringBuffer.prototype.toString = function() 
{
    return this.buffer.join("");
};

if (!('remove' in Element.prototype)) {
    Element.prototype.remove = function() {
        if (this.parentNode) {
            this.parentNode.removeChild(this);
        }
    };
}

$.Interface = {
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
		wasURL : "127.0.0.1",
		command : null,
		Run : function(Command, Parameters) {
			this.command = Command;
			this.xpiParams = Parameters;
			
			var localWASURL = this.xpiParams["LOCAL_WAS_URL"];
			this.Check_Install(localWASURL);
		},
		Run_Operation: function() {
			switch(this.command) {
				default:break;
				
				case "VIEW_ORIGINAL_SLIP" : 
					this.Open_Viewer();
					break;
			}
		},
		Open_Viewer : function() {
			var sbParam 	= new StringBuffer();
			var xpiParams = this.xpiParams;
			if($.Interface.isDetected == $.Interface.XPI_ENUM.DETECTED) {
			
				
				sbParam.append("/RUN");
				sbParam.append("/CMD'VIEW'");
				sbParam.append(" /PROJ'"+xpiParams.SERVER_KEY+"'");
				sbParam.append(" /SVR'"+xpiParams.SVR_MODE+"'");
				sbParam.append(" /CALL'WAS'");
				sbParam.append(" /WORK''");
				sbParam.append(" /LANG'"+xpiParams.LANG.toUpperCase()+"'");
				sbParam.append(" /CORP'"+xpiParams.CORP_NO+"'");
				sbParam.append(" /USER'"+xpiParams.USER_ID+"'");
				sbParam.append(" /KEYTYPE'"+xpiParams.KEY_TYPE+"'");
				sbParam.append(" /KEY'"+xpiParams.KEY+"'");
				sbParam.append(" /MODE'"+xpiParams.VIEW_MODE+"'");
				sbParam.append(" /VIEW'MULTI'");
				sbParam.append(" /MENU'HIDE'");
				sbParam.append(" /INFO'SHOW'");
//				sbParam.append(" /SELECTED'"+objData.SLIP_IRN+"'");
				
				$.Interface.Execute(sbParam.toString(), null);
			}
			else
			{
				var popupTitle = "Viewer";
				var sbURL = new StringBuffer();
				sbURL.append(xpiParams.OFFICE_VIEWER_URL+"index.jsp");
				
				var nWidth 	= 1250;
				var nHeight 	= 700;
				var vPopupCenterPosition = this.getDisplayCenterPosition(nWidth, nHeight);

				var elPopup = window.open(sbURL.toString(), popupTitle, vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
				xpiParams["COMMAND"] = this.command;
				xpiParams["INTERFACE"] = "1";			
				
				this.postSubmit(sbURL.toString(), xpiParams, "post", popupTitle);
				
			}
		},
		Check_Install : function(strURL)
		{
			if(!this.isBlank(strURL)) this.wasURL = strURL;
			
			var protocol = location.protocol;
			
			if("http:" == protocol) {
				this.wasURL = "http://"+this.wasURL+":9780";
			}
			else {
				this.wasURL = "https://"+this.wasURL+":9783";
			}
			
			//var localWAS_URL = location.protocol + "//127.0.0.1:" +  $.Actor.params.XPI_PORT;
			this.browserInfo = this.GetBrowserVersion();
			
			var deferred = $.Deferred();  
			if(this.browserInfo.ActingVersion > 9)
			{

				$.ajax({
				    type : "POST",
				    url : $.Interface.wasURL+"/API/Install /PROJ'"+$.Interface.xpiParams["SERVER_KEY"]+"'", 
				    dataType : "json",
				    success : function(res) {
				    	$.Interface.isDetected = $.Interface.XPI_ENUM.DETECTED;
				//    	$.Interface.setCSOperation();
						deferred.resolve();
				    },
				    error : function(e, status, error )
				    {
				    	$.Interface.isDetected = $.Interface.XPI_ENUM.NO_DETECTED;
						deferred.reject();
				    },
				    complete : function() {
				    	$.Interface.Run_Operation();
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
					request.open("GET", $.Interface.wasURL+"/API/Install /PROJ'"+$.Interface.xpiParams["SERVER_KEY"]+"'");
					request.onload = function() 
					{
						//If call localwas successfully
						if(!$.Common.isBlank($.Interface.xpiCommand))
						{
							$.when($.Interface.Execute($.Interface.xpiCommand, $.Interface.xpiCallback))
							.then(function(){
								
								$.Interface.isDetected = $.Interface.XPI_ENUM.DETECTED;
								$.Interface.setCSOperation();
								deferred.resolve();
							})
							.fail(function(){
								deferred.reject();
							});
						}
					}
					
					request.ontimeout = function() 
					{ 
						$.Interface.isDetected = $.Interface.XPI_ENUM.DETECTED;
						deferred.reject();
					}
					request.onerror = function() 
					{ 
						$.Interface.isDetected = $.Interface.XPI_ENUM.DETECTED;
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
		
		GetBrowserVersion : function()
		{
		    //Set defaults
		    var value = {
		        IsIE: false,
		        TrueVersion: 0,
		        ActingVersion: 0,
		        CompatibilityMode: false
		    };
		
		    var trident = navigator.userAgent.match(/Trident\/(\d+)/);
		    if (trident) {
		        value.IsIE = true;
		        value.TrueVersion = parseInt(trident[1], 10) + 4;
		    }
		
		    var msie = navigator.userAgent.match(/MSIE (\d+)/);
		    if (msie) {
		        value.IsIE = true;
		        //Find the IE version number from the user agent string
		        value.ActingVersion = parseInt(msie[1]);
		    } else {
		        //Must be IE 11 in "edge" mode
		        value.ActingVersion = value.TrueVersion;
		    }
		
		    if (value.IsIE && value.TrueVersion > 0 && value.ActingVersion > 0) {
		        value.CompatibilityMode = value.TrueVersion != value.ActingVersion;
		    }
		    else
		    {
		    	value.ActingVersion = 11;
		    }
		    return value;
		},
		Execute : function(strCommand, fnCallBack)
		{

			console.log("ajax start");
//			alert( $.OfficeXPI.wasURL  + strCommand);
			$.ajax({
			    type : "POST",
			    url : $.Interface.wasURL + strCommand , 
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
			    }		    
			});
		},
		isBlank: function(str) {
			var res = false;
			var blank_pattern = /^\s+|\s+$/g;
			if (str == null || str == "undefined" || str.replace(blank_pattern, "") == "") {
				res = true
			}
			return res;
		},
		getDisplayCenterPosition : function(nWidth, nHeight)
		{
			var nTop 	= ((screen.availHeight - nHeight)/2 - 40);
			var nLeft 	= (screen.availWidth - nWidth)/2;
			
			var sbRes = new StringBuffer();
			sbRes.append("top=");
			sbRes.append(nTop);
			sbRes.append(",left=");
			sbRes.append(nLeft);
			sbRes.append(",width=");
			sbRes.append(nWidth);
			sbRes.append(",height=");
			sbRes.append(nHeight);
			return sbRes.toString();
		},
		postSubmit : function(path, params, method, target) {
		    method = method || "post";

		    var form = document.createElement("form");
		    form.setAttribute("method", method);
		    form.setAttribute("action", path);
		    
		    if(!this.isBlank(target))
		    {
		    	form.setAttribute("target", target);
		    }
		    for(var key in params) {
		        if(params.hasOwnProperty(key)) {
		            var hiddenField = document.createElement("input");
		            hiddenField.setAttribute("type", "hidden");
		            hiddenField.setAttribute("name", key);
		            hiddenField.setAttribute("value", params[key]);

		            form.appendChild(hiddenField);
		         }
		    }

		    document.body.appendChild(form);
		    form.submit();
		    form.remove();
	}
}