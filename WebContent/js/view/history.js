$.History = {
		localeMsg : null,
		colorSet : null,
		params : null,
		init : function(params) {
			$.Common.ShowProgress("#history_progress","Waiting..","000000","0.7");

			this.params 			= params;
			
			//Set globalization.
			$.History.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"History");

			//Set UI set
			$.History.setUIColor();
			
			$.History.drawGridView();
			$.History.getHistoryList();
			$.History.localizeGrid();
			
					},
		localizeGrid : function() {
			$("#history_list").jsGrid("fieldOption", "SDOC_NAME", 			"title", this.localeMsg.SDOC_NAME);
			$("#history_list").jsGrid("fieldOption", "FOLDER", 			"title", this.localeMsg.FOLDER);
			$("#history_list").jsGrid("fieldOption", "KIND_NM", 			"title", this.localeMsg.KIND_NM);
			$("#history_list").jsGrid("fieldOption", "REG_USER", 		"title", this.localeMsg.REG_USER);
			$("#history_list").jsGrid("fieldOption", "HISTORY_TITLE",		"title", this.localeMsg.HISTORY_TITLE);
			$("#history_list").jsGrid("fieldOption", "HISTORY_USER", 		"title", this.localeMsg.HISTORY_USER);
			$("#history_list").jsGrid("fieldOption", "CABINET", 			"title", this.localeMsg.CABINET);
			$("#history_list").jsGrid("fieldOption", "INFO_ETC", 					"title", this.localeMsg.INFO_ETC);
			
		},
		
		getHistoryList : function() {
			$.when($.Common.RunCommand(g_HistoryCommand, "GET_HISTORY_LIST", $.History.params)).then(function(arObjList){
				
				var arObjHistoryItem = [];
				
				$.each(arObjList, function(){
					var objItem = {};
					objItem["SDOC_NAME"] 	= this.SDOC_NAME;
					var folder = this.FOLDER;
					if("SLIPDOC" == folder.toUpperCase()) {
						objItem["FOLDER"] = $.History.localeMsg.FOLDER_SLIP;
					}
					else {
						objItem["FOLDER"] = $.History.localeMsg.FOLDER_ATTACH;
					}

					objItem["KIND_NM"] 		= this.KindNM;
					objItem["REG_USER"] 	= this.RegUserNM + "<br/>(" +  this.REG_USER + ")";
					objItem["HISTORY_TITLE"] 		= this.HistoryNM;
					objItem["HISTORY_USER"] 			= this.HistoryUserNM + "<br/>(" +  this.HistoryUser + ")";
					objItem["CABINET"] 	= this.REG_TIME.substring(0,16);
					objItem["INFO_ETC"] 			= "";
					
					arObjHistoryItem.push(objItem);
					
				});
				$("#history_list").jsGrid("option", "data", arObjHistoryItem);
			})
			.always(function(){
				$.Common.HideProgress("#history_progress");

			});
					   
			
			$("#history_list").jsGrid("sort", { field: "cabinet", order: "desc" });
		
		
		},
		drawGridView : function() {
			
			$("#history_list").jsGrid({
		        width: "100%",
		        height:$(window).height() - 60,
		        filtering: false,
		        inserting: false,
		        editing: false,
		        selecting: false,
		        sorting: true,
		        paging: true,
		 
		        
		        pageSize: 15,
		        pageButtonCount: 5,
		        
		    //    data: arObjHistoryItems,
		 
		        fields: [
		            { name: "SDOC_NAME", 			type: "text", title:"", align: "left" },
		            { name: "FOLDER", 			type: "text", title:"", width: 40, align: "center" },
				            { name: "KIND_NM", 			type: "text", title:"", width: 40, align: "center" },
		            { name: "REG_USER", 			type: "text", title:"", width: 40, align: "center" },
		            { name: "HISTORY_TITLE", 			type: "text", title:"", width: 40, align: "center"},
		            { name: "HISTORY_USER", 					type: "text", title:"", width: 40, align: "center" },
		            { name: "CABINET", 					type: "text", title:"",width:40, align: "center" },
		            { name: "INFO_ETC", 				type: "text", title:"", width: 40, align: "center" }
		        ]
		    });
			
		},
		/*initializeHistory : function()
		{
			
			$("#ipt_content").on("keypress",function(e){
				var code = (e.keyCode ? e.keyCode : e.which);
			    if (code == 13) 
			    	{
			    		$("#ipt_content").getNiceScroll().resize();
			    	}
			});
		},*/
		setUIColor : function()
		{
			var objColorHistory =  $.extend($.Color.PC.History, $.Color.Common);
			
			if(objColorHistory != null)
			{
				$(".area_history_title").css({"background":"#"+objColorHistory.NAVIGATION, "color" : "#" + objColorHistory.NAVIGATION_FONT});
				$.History.colorSet = objColorHistory;
			}
		},
}