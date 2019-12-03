"use strict";
$.Operation  = {
		execute : function(target, el, objData, elTarget) {

			var deferred = $.Deferred();

			var command 	= null;
			
			if(typeof(el) === 'string' || el instanceof String)
			{
				command 	= el;
			}
			else
			{
				command 	= $(el).attr("command");
			}
			
			
			var csFlag =  false;
			if($.OfficeXPI != null) {
				csFlag = ($.OfficeXPI.isDetected ==  $.OfficeXPI.XPI_ENUM.DETECTED) ? true : false;
			}
			
			switch(command)
			{
			case "ROTATE_CW" : 
				$.Operation.Rotate_Slip(target, objData, 90);
				break;
			case "ROTATE_180" : 
				$.Operation.Rotate_Slip(target, objData, 180);
				break;
			case "ROTATE_CCW" : 
				$.Operation.Rotate_Slip(target, objData, 270);
				break;
			case "OPEN_COMMENT" : 
				$.Operation.openComment(target);
				break;
			case "OPEN_HISTORY" : 
				$.Operation.openHistory(target);
				break;
			case "REMOVE_ALL" : 
				$.Operation.removeAll(target);
				break;
			case "REMOVE_SLIP" : 
				$.Operation.removeItem(target, objData);
				break;
			case "REMOVE_MENU_SLIP" : 
				$.Operation.removeMenuItem(target, objData, elTarget);
				break;
			case "REMOVE_MENU_ATTACH" : 
				$.Operation.removeMenuItem(target, objData, elTarget);
				break;
				
			case "ADD_SLIP" : 
				$.Operation.addSlip(target, objData);
				break;
			case "ADD_AFTER" : 
				$.Operation.addAfter(target, objData);
				break;
			case "ADD_ATTACH" : 
				$.Operation.addAttach(target, objData);
				break;
			case "VIEW_PROPERTY" : 
				$.Operation.viewProperty(target, objData);
				break;
			case "PRINT_SLIP" : 
				$.Operation.Print_Slip(target, objData);
				break;
			case "ADD_CAPTURE" : 
				$.Operation.addCapture(target, objData);
				break;
			case "ADD_URL" : 
				$.Operation.addURL(target, objData);
				break;
			case "ADD_XML" : 
				$.Operation.addTax(target, objData);
				break;
			case "MODIFY_AFTER" : 
				$.Operation.modifyAfter(target,objData);
				break;
			case "ADD_URL_LINK" : 
				$.Operation.addURL_Link(target, objData);
				break;
			case "ADD_SEARCH" : 
				$.Operation.Add_FromServer(target, objData);
				break;
			case "VIEW_ORIGINAL_SLIP" : 
				$.Operation.openViewer(target, objData);
				break;
			case "VIEW_ORIGINAL_ATTACH" : 
				$.Operation.openAttach(target, objData);
				break;
			case "DOWN_ORIGINAL_ATTACH" : 
				$.Operation.downloadAttach(target, objData);
				break;
			case "DOWN_ORIGINAL_SLIP" : 
				$.Operation.downloadOriginal(target, objData);
				break;
			case "DOWNLOAD_CS" : 
				$.Operation.Open_DownloadPage();
				break;
			case "WRITE_COMMENT" :
				$.Operation.Write_Comment(target, objData);
				break;
			case "MODIFY_COMMENT" :
				deferred = $.Operation.Modify_Comment(target, objData);
				break;
			case "REMOVE_COMMENT" :
				$.Operation.Remove_Comment(target, objData);
				break;
 			}

			return deferred.promise();
		},
		//detect CS
		detectCS : function(target, command, showAlert) {
			
			var bRes = false;
			
			var csFlag = $.OfficeXPI.isDetected;
			
			switch(csFlag)
			{
			case  $.OfficeXPI.XPI_ENUM.IN_PROGRESS : 
				if(showAlert)	$.Common.simpleAlert(null, target.localeMsg.CHECKING_XPI, 0.3);
				break;
			case  $.OfficeXPI.XPI_ENUM.DETECTED :
				bRes = true;
				break;
			default : 
				if(showAlert) {
					$.Common.simpleConfirm(target.localeMsg.Confirm,target.localeMsg.CONFIRM_DOWNLOAD, function(){
						window.open(g_DOWN_URL,"_blank");
					});
				}
				break;
			}
			
			return bRes;
		},
		
		//Add slip
		addSlip : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'PTI'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		//Rotate Slip 
		Rotate_Slip : function(target,objData, degrees) {
				
			var curDegrees = parseInt(objData.SLIP_ROTATE);
			var calc = (degrees + curDegrees) % 360
			
			var value = objData.SLIP_IRN;
			var field = "SLIP_IRN";
			
			var elTarget = $(".slip_item[idx='"+value+"']");
		//	var idx = elTarget.idx();
			
			if("1" == elTarget.attr("first")) {
				if("0"==elTarget.attr("fold")) {
					field = "SDOC_NO";
					value = objData.SDOC_NO;
				}
			}
			
			var params = {
					FIELD : field,
					VALUE: value,
					DEGREE : calc,
			};
			
			
			$.when($.Common.RunCommand(g_ActorCommand, "ROTATE_SLIP", params)).then(function(objRes){
				if("T" == objRes.RESULT.toUpperCase())
				{
//					target.pageSubmit();
					var objItemData = $.Common.getObjByValue(target.arObjSlipItem,field,value);
					$.each(objItemData, function(){
						this.SLIP_ROTATE = calc+"";
					});
					
					target.Reload_Thumb(target, elTarget, objItemData);
				}
				else {
					$.Common.simpleAlert(null,target.localeMsg[objRes.MSG]);
				}
			});
		},
		//Add After
		addAfter : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'FOLLOW'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		//Add After
		modifyAfter : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+target.params.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+target.params.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+target.params.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+target.params.CORP_NO+"'");
			sbParam.append(" /USER'"+target.params.USER_ID+"'");
			sbParam.append(" /AUTORUN'FOLLOW_MOD'");
			sbParam.append(" /KEYTYPE'"+target.params.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			sbParam.append(" /SDOC_NO'"+objData.SDOC_NO+"'");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+target.params.VIEW_MODE+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		Open_DownloadPage : function() {
			
			var nWidth 	= 1250;
			var nHeight 	= 700;
			var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

			window.open(g_RootURL+"install/index.jsp", null, vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
			
		},
		//Add from Server
		Add_FromServer : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			
			var sbParam 	= new StringBuffer();
			
			sbParam.append("/RUN");
			sbParam.append("/CMD'DLG'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'SEARCH'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		//Print Slip
		Print_Slip : function(target, objData) {
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}

			objData = target.params;

			var sbParam 	= new StringBuffer();
			
			sbParam.append("/RUN");
			sbParam.append("/CMD'PRINT'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
		},
		//Add Attach
		addAttach : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
			
			sbParam.append("/RUN");
			sbParam.append("/CMD'FILE'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'FILE'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		//View slip property
		viewProperty : function(target, objData) {
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
	
			var sbParam 	= new StringBuffer();
			
			sbParam.append("/RUN");
			sbParam.append("/CMD'DLG'");
			sbParam.append(" /PROJ'"+target.params.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+target.params.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+target.params.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+target.params.CORP_NO+"'");
			sbParam.append(" /USER'"+target.params.USER_ID+"'");
			sbParam.append(" /AUTORUN'PROPERTY'");
			sbParam.append(" /SDOC_NO'"+objData.SDOC_NO+"'");
			sbParam.append(" /FOLDER'SLIPDOC'");
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
		},
		
		//Add capture
		addCapture : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'Capture'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
	
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
		},
		
		//Add tax
		addTax : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'TAX'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
	
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		
		//Add url
		addURL : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'PTI'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'URL'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
	
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		
		//Add url link
		addURL_Link : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, true)) {
				return;
			}
			
			var sbParam 	= new StringBuffer();
		
			sbParam.append("/RUN");
			sbParam.append("/CMD'FILE'");
			sbParam.append(" /PROJ'"+objData.SERVER_KEY+"'");
			sbParam.append(" /SVR'"+objData.SVR_MODE+"'");
			sbParam.append(" /CALL'WAS'");
			sbParam.append(" /WORK''");
			sbParam.append(" /LANG'"+objData.LANG.toUpperCase()+"'");
			sbParam.append(" /CORP'"+objData.CORP_NO+"'");
			sbParam.append(" /USER'"+objData.USER_ID+"'");
			sbParam.append(" /AUTORUN'URL'");
			sbParam.append(" /KEYTYPE'"+objData.KEY_TYPE+"'");
			sbParam.append(" /KEY'"+target.currentKey+"'");
			sbParam.append(" /SLIP_TYPE''");
			//sbParam.append(" /AFTER'"+objData.SDOC_AFTER+"'");
			sbParam.append(" /MODE'"+objData.VIEW_MODE+"'");
	
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			
		},
		
		//View original slip 
		openViewer : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, false)) {
				$.Operation.viewOriginal(target, objData);
			}
			else
			{
				var sbParam 	= new StringBuffer();
				
				sbParam.append("/RUN");
				sbParam.append("/CMD'VIEW'");
				sbParam.append(" /PROJ'"+target.params.SERVER_KEY+"'");
				sbParam.append(" /SVR'"+target.params.SVR_MODE+"'");
				sbParam.append(" /CALL'WAS'");
				sbParam.append(" /WORK''");
				sbParam.append(" /LANG'"+target.params.LANG.toUpperCase()+"'");
				sbParam.append(" /CORP'"+target.params.CORP_NO+"'");
				sbParam.append(" /USER'"+target.params.USER_ID+"'");
				sbParam.append(" /KEYTYPE'"+target.params.KEY_TYPE+"'");
				sbParam.append(" /KEY'"+target.currentKey+"'");
				sbParam.append(" /MODE'"+target.params.VIEW_MODE+"'");
				sbParam.append(" /VIEW'MULTI'");
				sbParam.append(" /MENU'SHOW'");
				sbParam.append(" /INFO'SHOW'");
				sbParam.append(" /SELECTED'"+objData.SLIP_IRN+"'");
				
				$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
				$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
				
				$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			}
		},
		//Open attachment by through CS
		openAttach : function(target, objData) {
			
			if(!$.Operation.detectCS(target, objData, false)) {
				$.Operation.downloadAttach(target, objData);
			}
			else
			{
				var sbParam 	= new StringBuffer();
				
				sbParam.append("/RUN");
				sbParam.append("/CMD'RUN_FILE'");
				sbParam.append(" /PROJ'"+target.params.SERVER_KEY+"'");
				sbParam.append(" /SVR'"+target.params.SVR_MODE+"'");
				sbParam.append(" /CALL'WAS'");
				sbParam.append(" /WORK''");
				sbParam.append(" /LANG'"+target.params.LANG.toUpperCase()+"'");
				sbParam.append(" /CORP'"+target.params.CORP_NO+"'");
				sbParam.append(" /USER'"+target.params.USER_ID+"'");
				sbParam.append(" /KEYTYPE'SDOC_NO'");
				sbParam.append(" /KEY'"+objData.SDOC_NO+"'");
		
				$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
				$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
				
				$.OfficeXPI.CallLocalWAS(sbParam.toString(), target.pageSubmit);
			}
		},
		downloadAttach : function(target, objData) {
			
			var isURL = objData.SDOC_URL;
			if("1" == isURL) {
				var sbURL = new StringBuffer();
				sbURL.append(objData.ORG_FILE);

				var nWidth 	= 1250;
				var nHeight 	= 700;
				var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

				var elPopup = window.open(sbURL.toString(), "", vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
				
			}
			else {
				var docIRN = objData.DOC_IRN;
				
				var sbURL = new StringBuffer();
				sbURL.append(this.rootURL);
				sbURL.append("DownloadAttach.do?");
				sbURL.append("&DOC_IRN="+docIRN);
				
				self.window.open(sbURL.toString(), '_self');	
			}
		},
		
		downloadOriginal : function(target, objData) {
			
			
			var isURL = objData.SDOC_URL;
			if("1" == isURL) {
				var sbURL = new StringBuffer();
				sbURL.append(objData.ORG_FILENM);
				
				var nWidth 	= 1250;
				var nHeight 	= 700;
				var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

				var elPopup = window.open(sbURL.toString(), "", vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
				
			}
			else {
				var orgIRN = objData.ORG_FILE;
				
				if("0" == orgIRN) {
					$.Common.simpleAlert(null, target.localeMsg.NO_ORIGINAL_EXISTS);
				}
				else {
					var sbURL = new StringBuffer();
					sbURL.append(this.rootURL);
					sbURL.append("DownloadOriginal.do?");
					sbURL.append("&ORG_IRN="+orgIRN);
					
					self.window.open(sbURL.toString(), '_self');	
				}
			}
			
		},

		viewOriginal : function(target, objData) {
			
			var popupTitle = "Viewer";
			var sbURL = new StringBuffer();
			sbURL.append(g_RootURL+"slip_viewer.jsp");
			
			var nWidth 	= 1250;
			var nHeight 	= 700;
			var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

			var elPopup = window.open(sbURL.toString(), popupTitle, vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
		
			//Post submit to popup
			var objParams = {};
			objParams["KEY"] 			= target.params.KEY;
			objParams["KEY_TYPE"] 		= target.params.KEY_TYPE;
			objParams["LANG"] 				= target.params.LANG;
			objParams["VIEW_MODE"] 		= target.params.VIEW_MODE;
			objParams["SVR_MODE"]		= target.params.SVR_MODE;
			objParams["SLIP_IRN"]			= objData.SLIP_IRN;
			objParams["MENU"]				= target.params.MENU;
			objParams["XPI_PORT"]			= target.params.XPI_PORT;
			
			var elFirstGroupItem 			= $("[id=slip_item]");
			
			$.each(elFirstGroupItem, function() {
				if($(this).attr("group") == objData.SDOC_NO)
				{
					objParams["FOLD"]			= $(this).attr("fold");
					return false;
				}
			});
			
			if($.Common.isBlank(objParams["FOLD"])) {
				objParams["FOLD"]	 = "";
			}
			
			$.Common.postSubmit(sbURL.toString(), objParams, "post", popupTitle);
			
		},
		//Open comment
		openComment : function(target) {
	
			var popupTitle = "Comment";
			var sbURL = new StringBuffer();
			sbURL.append(g_RootURL+"slip_comment.jsp?");
			
			var nWidth 	= 520;
			var nHeight 	= 600;
			var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

			var elPopup = window.open(sbURL.toString(), popupTitle, vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
			
			//Post submit to popup
			var objParams = {};
			objParams["KEY"] 					= target.currentKey;
			objParams["LANG"] 				= target.params.LANG;
			objParams["USER_ID"] 				= target.params.USER_ID;
			objParams["CORP_NO"] 			= target.params.CORP_NO;
			
			$.Common.postSubmit(sbURL.toString(), objParams, "post", popupTitle);		
		},
		//Open history
		openHistory : function(target) {
			
			var popupTitle = "History";
			var sbURL = new StringBuffer();
			sbURL.append(g_RootURL+"slip_history.jsp?");
		
			var nWidth 	= 800;
			var nHeight 	= 500;
			var vPopupCenterPosition = $.Common.getDisplayCenterPosition(nWidth, nHeight);

			var elPopup = window.open(sbURL.toString(), popupTitle, vPopupCenterPosition+', toolbar=0, directories=0, status=0, menubar=0, scrollbars=0, resizable=0');
		
			//Post submit to popup
			var objParams = {};
			objParams["KEY"] 					= target.currentKey;
			objParams["LANG"] 				= target.params.LANG;
			objParams["USER_ID"] 				= target.params.USER_ID;
			objParams["CORP_NO"] 			= target.params.CORP_NO;
			
			$.Common.postSubmit(sbURL.toString(), objParams, "post", popupTitle);
		},
		
		// Remove slip / addfile by I/F Key
		removeAll : function(target) {
			$.Common.simpleConfirm(null,target.localeMsg.CONFIRM_REMOVE_ALL, function(){
				var objRemoveParams = {
						VALUE : target.params.KEY,
						FIELD : target.params.KEY_TYPE,
						LANG : target.params.LANG,
						VIEW_MODE : target.params.VIEW_MODE
				};
				
				$.when($.Common.RunCommand(g_ActorCommand, "REMOVE_ALL", objRemoveParams)).then(function(objRes){
					if("T" == objRes.RESULT.toUpperCase())
					{
						target.reset();
					}
					else {
						$.Common.simpleAlert(null,target.localeMsg[objRes.MSG]);
					}
				});
			});
		},
		Write_Comment : function(target, objData) {
			$.when($.Common.RunCommand(g_CommentCommand, "WRITE_COMMENT", objData)).then(function(objRes){
				if("T" === objRes.RESULT.toUpperCase())
				{
					target.refresh();
				}
				else
				{
					$.Common.simpleAlert(null, objRes.MSG, null);
				//	return;
				}
			});
		},
		Modify_Comment : function(target, objData) {

			var deferred = $.Deferred();
			$.when($.Common.RunCommand(g_CommentCommand, "MODIFY_COMMENT", objData)).then(function(objRes) {

					// if ("T" === ) {
				deferred.resolve(objRes.RESULT.toUpperCase());
					// }
					// else {
					// 	deferred.reject();
					// }

			}, function (reason) {
				deferred.reject(reason)
			});

			 return deferred.promise();
		},
		Remove_Comment : function(target, objData) {

			$.when($.Common.RunCommand(g_CommentCommand, "REMOVE_COMMENT", objData)).then(function(objRes){
				if("T" === objRes.RESULT.toUpperCase())
				{
					target.refresh();
				}
			});
		},

		//Remove menu item 
		removeMenuItem : function(target, objData, elTarget) {
			
			var key = null;
			var command = null;
			var field = null;
			var type = elTarget.attr("type");
			
			if("SLIP" == type) 
			{
				command 	= "REMOVE_SELECTED_SLIP";
				
				var elTarget = $(".slip_item[idx='"+objData.SLIP_IRN+"']");
			//	var idx = elTarget.idx();
				
				if("1"==elTarget.attr("fold")) {
					field 	= "SLIP_IRN";
					key 	= objData.SLIP_IRN;
				}
				else {

					field 	= "SDOC_NO";
					key 	= objData.SDOC_NO;
				}
				
//				
//				var isFold 	= elTarget.find("#btn_fold").attr("fold") == null ? "0" : elTarget.find("#btn_fold").attr("fold");
//				
//				if("0" == isFold) 
//				{
//					field 	= "SDOC_NO";
//					key 	= objData.SDOC_NO;
//				}
//				else
//				{
//					field 	= "SLIP_IRN";
//					key 	= objData.SLIP_IRN;
//				}
			}
			else 
			{
				command 	= "REMOVE_SELECTED_ATTACH";
				key 			= objData.SDOC_NO;
				field 			= "SDOC_NO";
			}
			
			var objParams = {
					FIELD : field,
					VALUE : key,
					LANG : target.params.LANG,
					VIEW_MODE : target.params.VIEW_MODE
			};
			
			
			$.Common.simpleConfirm(null,target.localeMsg.CONFIRM_REMOVE_SELECTED_ITEM, function(){

				$.when($.Common.RunCommand(g_ActorCommand, command, objParams)).then(function(objRes){
					if("T" == objRes.RESULT.toUpperCase())
					{
						if("SLIP" == type) 
						{
							$.Common.removeItem(target.arObjSlipItem, key, "SDOC_NO");
							target.removeSlipElement(elTarget.closest(".slip_item"));
						}
						else
						{
							$.Common.removeItem(target.arObjAttachItem, key, "SDOC_NO");
							target.removeAttachElement(elTarget.closest(".attach_item"));
						}
					}
					else
					{
						var resMsg = objRes.MSG;
						if($.Common.isBlank(resMsg)) {
							resMsg = target.localeMsg.FAILED_REMOVE_ITEM;
						}
						else {
							resMsg = target.localeMsg[objRes.MSG];
						}
						$.Common.simpleAlert(null, resMsg);
					}
				});
			});
		},
		// Remove selected item
		removeItem : function(target, objData) {
			
			var objCheckedSlip_SlipIrnData 		= [];
			var objCheckedSlip_SDocNoData 	= [];
			var objCheckedAttachData 	= [];
	
			var elCheckedSlip 		= $("#area_slip").find("[id=chk]:checked");
			var elCheckedAttach 	= $("#area_attach").find("[id=chk]:checked");
		
			if(elCheckedSlip.length <= 0 && elCheckedAttach.length <= 0)
			{
				$.Common.simpleAlert(null, target.localeMsg.CHECK_ITEM);
				return;
			}
			
			//Get slip key 
			$.each(elCheckedSlip, function(){
				var elSlip = $(this).closest(".slip_item");
				
				var idx = elSlip.attr("idx");
				var objItemData = $.Common.getObjByValue(target.arObjSlipItem,"SLIP_IRN",idx)[0];
				var isFirst = elSlip.attr("first");
				
				if("1" == isFirst) {
					var isFold = elSlip.attr("fold");
					if(isFold == "1") {
						objCheckedSlip_SlipIrnData.push(objItemData.SLIP_IRN);
					}
					else {
						objCheckedSlip_SDocNoData.push(objItemData.SDOC_NO);
					}
				}
				else
				{
					objCheckedSlip_SlipIrnData.push(objItemData.SLIP_IRN);
				}
			});
		
			//Get attach key
			$.each(elCheckedAttach, function(){
				var elAttach 			= $(this).closest(".attach_item");
				var idx 					= elAttach.attr("idx");
				var objItemData 		= $.Common.getObjByValue(target.arObjAttachItem,"SDOC_NO",idx)[0];
				
				objCheckedAttachData.push(objItemData.SDOC_NO);
			});
			
			$.Common.simpleConfirm(null,target.localeMsg.CONFIRM_REMOVE_SELECTED_ITEM, function(){
				
				//Remove slip by SLIP_IRN
				if(objCheckedSlip_SlipIrnData.length > 0)
				{
					var objSlipParams = {
							FIELD : "SLIP_IRN",
							VALUE : objCheckedSlip_SlipIrnData,
							LANG : target.params.LANG,
							VIEW_MODE : target.params.VIEW_MODE
					};
				
					$.when($.Common.RunCommand(g_ActorCommand, "REMOVE_SELECTED_SLIP", objSlipParams)).then(function(objResSlip){
						
						if("T" == objResSlip.RESULT.toUpperCase())
						{
								$.Common.removeItem(target.arObjSlipItem, objCheckedSlip_SlipIrnData, "SLIP_IRN");
								target.removeSlipElement(elCheckedSlip);
						}
					});
				}
				
				//Remove slip by SDOC_NO
				if(objCheckedSlip_SDocNoData.length > 0)
				{
					var objSlipParams = {
							FIELD : "SDOC_NO",
							VALUE : objCheckedSlip_SDocNoData,
							LANG : target.params.LANG,
							VIEW_MODE : target.params.VIEW_MODE
					};
					
					$.when($.Common.RunCommand(g_ActorCommand, "REMOVE_SELECTED_SLIP", objSlipParams)).then(function(objResSlip){
						
						if("T" == objResSlip.RESULT.toUpperCase())
						{
								$.Common.removeItem(target.arObjSlipItem, objCheckedSlip_SDocNoData, "SDOC_NO");
								target.removeSlipElement(elCheckedSlip);
						}
						else {
							$.Common.simpleAlert(null,target.localeMsg[objResSlip.MSG]);
						}
					});
				}
				
				//Remove attach
				if(objCheckedAttachData.length > 0)
				{
					var objAttachParams = {
							FIELD : "SDOC_NO",
							VALUE : objCheckedAttachData,
							LANG : target.params.LANG,
							VIEW_MODE : target.params.VIEW_MODE
					};
					
					$.when($.Common.RunCommand(g_ActorCommand, "REMOVE_SELECTED_ATTACH", objAttachParams)).then(function(objResAttach) {
						if("T" == objResAttach.RESULT.toUpperCase())
						{
							$.Common.removeItem(target.arObjAttachItem, objCheckedAttachData, "SDOC_NO");
							target.removeAttachElement(elCheckedAttach);
						}
						else {
							$.Common.simpleAlert(null,target.localeMsg[objResAttach.MSG]);
						}
					});
				}
			});
		},
}