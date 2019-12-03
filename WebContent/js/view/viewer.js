"use strict";
$.Viewer = {
		localeMsg : null,
		colorSet : null,
		params : null,
		slipRange : 9999,
		thumbWidth : 140,
		startIdx : 0,
		slipTotalCnt : 0,
		arObjSlipItem: null,
		arObjAttachItem : null,
		contextMenu : null,
		attachCnt : 0,
		viewer : null,
		is_Maximized : false,
		currentKey : null,
		Cur_Slip : null,
		init : function(params) {
			
			this.params 			= params;
			this.currentKey 		= this.params.KEY;
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#info_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#original_progress","Waiting..","000000","0.7");
		
			
			$(window).on('resize', function() {
				$.each($("[tag=drag]"), function(){
					var elPrev = $(this).prev();
					var left = elPrev.outerWidth() + parseInt(elPrev.offset().left) - $("#list_btnLeft").width();
					$(this).css("left",left);
				});
				
			});
			
			/**
			 * resize scroll window on resize browser
			 */
			$(window).on('resize', $.Common.windowCallback(function(){
				
				if($.Viewer.is_Maximized) {
					$.Viewer.is_Maximized = false;
					$.Viewer.ZoomIn_Thumb();
				}
				// else {
				// 	$.Actor.ZoomOut_Thumb();
				// }
				else {	
					$("#area_slip").getNiceScroll().resize();
					$("#area_attach").getNiceScroll().resize();	
				}
			
			}));

			//Set globalization.
			$.Viewer.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"Viewer");

			
			
			//Set UI set
			$.Viewer.setUIColor();
			
			$.Viewer.contextMenu = $.Menu.PC.Viewer;
		
			if("SIMPLE" != $.Viewer.params.VIEW_MODE.toUpperCase()){
				if("1" == $.Viewer.params.MENU)
				{
					$.Viewer.Init_MenuList();
				}
			}
			else {
				$(".viewer_left").hide();
				$(".viewer_right_extra").hide();
				$(".viewer_right").css("width","100%");
				$(".area_attach_list").hide();
			}
			
			
			
			$('#slip_masonry').masonry({
				  // options
				  itemSelector: '#slip_item',
				  columnWidth: $.Viewer.thumbWidth,
				  horizontalOrder: true,
				  isFitWidth: true,
				  gutter:20
				});
			
			$.Viewer.initializeSlip();
			
			
			if("SIMPLE" != $.Viewer.params.VIEW_MODE.toUpperCase()){
				
				//Set drag event
				$.Viewer.setBorderDrag();
				
				//Toggle attachlist area
				$.Viewer.getAttachCount($.Viewer.params);


			}
			$.Viewer.viewer = $.Viewer.setImageViewer();
			
		},
		Reload_Thumb : function(target, elThumb, objData) {
			
			$.each(objData,function() {
				
				var sbImgURL = new StringBuffer();
				sbImgURL.append(this.rootURL);
				sbImgURL.append("DownloadImage.do?");
				sbImgURL.append("ImgType=thumb");
				sbImgURL.append("&DocIRN="+this.DOC_IRN);
				sbImgURL.append("&Idx="+this.DOC_NO);
				sbImgURL.append("&degree="+this.SLIP_ROTATE);
				sbImgURL.append("&UserID="+target.params.USER_ID);
				sbImgURL.append("&CorpNo="+target.params.CORP_NO);
				sbImgURL.append('?'+Math.random());
//				
				
				
				var elThumb = $("#slip_masonry").find("[idx='" + this.SLIP_IRN + "']"); 
				var elImg = elThumb.find(".link > img");
				var imgURL = elImg.attr('src');
				elImg.attr('src', sbImgURL.toString());
				
				setTimeout(function(){ $('#slip_masonry').masonry("layout"); }, 100);

			});
	
			$("#area_slip").getNiceScroll().resize();
			
			$(".context_wrapper").hide();
			elThumb.find(".area_thumb").click();
			
		},
		initializeSlip : function() {
			$.Viewer.getSlipList();
		},
		ZoomIn_Thumb : function() {

			if(!$.Viewer.is_Maximized) {

				$('#slip_masonry').masonry({
				   columnWidth: $(".area_slip").width() - 40
				});
				$(".slip_item").css("width", "100%");

				$('#slip_masonry').masonry('layout');
				$("#area_slip").getNiceScroll().resize();

				$.Viewer.is_Maximized = true;
			}

		},
		ZoomOut_Thumb : function() {
			if($.Viewer.is_Maximized) {
				$('#slip_masonry').masonry({
					columnWidth:  $.Viewer.thumbWidth
					});
				$(".slip_item").css("width",  $.Viewer.thumbWidth);

				$('#slip_masonry').masonry('layout');
				$("#area_slip").getNiceScroll().resize();

				$.Viewer.is_Maximized = false;
			}
		},
		Init_MenuList : function() {
			
			//Set left menu position
			$("#areaViewer").addClass("menu_on");

			$.Viewer.initButtons();
			
			$.Viewer.contextMenu = $.Menu.PC.Actor;
        	
			$.Viewer.Draw_MainContext();
//        	var arObjMenu = $.Common.sortContextMenuItem($.Viewer.localeMsg,  $.Viewer.contextMenu['Menu'], $.Viewer.params.VIEW_MODE, $.Viewer.currentKey);
//			$.ContextMenu.getMenu($.Viewer, $("#btn_open_menu"), $("#btn_open_menu"), arObjMenu, $.Viewer.params, { spacing_top : true});				
//		
			var arObjAdd = $.Common.sortContextMenuItem($.Viewer.localeMsg,  $.Viewer.contextMenu['Add'], $.Viewer.params.VIEW_MODE, $.Viewer.currentKey);
			$.ContextMenu.getMenu($.Viewer, $("#btn_add_slip"), $("#btn_add_slip"), arObjAdd, $.Viewer.params, { spacing_top : true});				
		
			var arObjRemove = $.Common.sortContextMenuItem($.Viewer.localeMsg,  $.Viewer.contextMenu['Remove'], $.Viewer.params.VIEW_MODE, $.Viewer.currentKey);
			$.ContextMenu.getMenu($.Viewer, $("#btn_remove"), $("#btn_remove"), arObjRemove, $.Viewer.params, { spacing_top : true});	

			/*
			 * Detect LocalWAS
			 */	
			if(typeof OfficeXPI == "undefined")
			{
				
				//Load XPI Script
				$.getScript(g_XPI_URL, function() {
				
					var localWAS_URL = location.protocol + "//127.0.0.1:" +  $.Viewer.params.XPI_PORT;
					
					var XPIParams = {
							LOCAL_WAS_URL		: localWAS_URL,
							LANG 						: $.Viewer.params.LANG,
							SERVER_KEY 			: $.Viewer.params.SERVER_KEY,
					};
					
					/**
					 * Call LocalWAS
					 */
					$.when($.OfficeXPI.init(XPIParams, $.Lang)).then(function(a){
					
					}).fail(function(){
			        	
					});
		        }).fail(function(){
		        	
		        }).always(function(){
		        	
		        });
			}
		},
		Draw_MainContext :function() {
			var arObjMenu = $.Common.sortContextMenuItem($.Viewer.localeMsg,  $.Viewer.contextMenu['Menu'], $.Viewer.params.VIEW_MODE, $.Viewer.currentKey);
			$.ContextMenu.getMenu($.Viewer, $("#btn_open_menu"), $("#btn_open_menu"), arObjMenu, $.Viewer.params, { spacing_top : true});				
		
		},
		change_GroupKey : function(option) {
			var key = option.value;
			
			if($.Common.isBlank(key)) key = $.Viewer.params.KEY;
			
			$.Viewer.currentKey = key;
			
			$.Viewer.Draw_MainContext();
			$.Common.Draw_LeftMenu($.Viewer);
			
			$.Viewer.addSlipItem($.Viewer.arObjSlipItem, $("#slip_masonry"), key);
			
			var elAttachArea = $("#viewer_right_extra");
			var isShown = elAttachArea.attr("show");
			
			if($.Viewer.arObjAttachItem != null && "1" == isShown) {
				$.Viewer.addAttachItem($.Viewer.arObjAttachItem, $("#area_attach"), key);
			}
			
//			if(!$.Common.isBlank($.Actor.currentKey)) {
//				$.Actor.getCommentCnt();
//			}
		
		},
		pageSubmit : function() {
			//	$.Common.postSubmit(g_RootURL + "/slip_actor.jsp", $.Actor.params, "post");
				$.Viewer.reset();
				$.Viewer.initializeSlip();
				$.Viewer.getAttachList($.Viewer.params);
				//$.Viewer.resetViewer();
		},
		reset : function() {
			$(".slip_masonry").empty();
			$(".area_attach").empty();
			

			$(".key_select option").eq(0).prop("selected",true);
			
			$.Viewer.slipTotalCnt = 0;
			$.Viewer.attachTotalCnt = 0;
			$.Viewer.startIdx = 0;
		},
		initButtons : function()
		{
			$.Common.Draw_LeftMenu($.Viewer);
			
			$("#list_btnLeft > div").each(function(i) {
			//	$(this).unbind();
				$(this).on({
	        	    mouseenter: function () {
	        	    	var elImg 		= $(this).children('img');
	    				var imgURL 	= elImg.attr("src");
	    				var modifiedImgURL 	= imgURL.substring(0,imgURL.lastIndexOf(".")) + "_on.png";
	    				
	        	    	$(this).children('img').attr('src',  modifiedImgURL);
	        	    //	$(this).css( 'cursor', 'pointer' );
	        	    },
	        	    mouseleave: function () {
	        	    	var elImg 		= $(this).children('img');
	    				var imgURL 	= elImg.attr("src");
	    				var modifiedImgURL 	= imgURL.replaceAll("_on","");
	    				
	        	    	$(this).children('img').attr('src', modifiedImgURL);
	        	    	//$(this).css( 'cursor', 'default' );
	        	    },
	        	    /*click: function() {
	        	    	var isCSOperation = $(this).attr("cs_operation");
	        	    	
	        	    	if("1" === isCSOperation)
        	    		{
        	    			$.Common.simpleAlert("확인",$.Actor.localeMsg.CHECKING_XPI, 0.3);
        	    		}
	        	    	
	        	    }*/
	        	});
			});
		},
		setBorderPosition : function() {
			$('#dragBar_viewer').css("left",$(".viewer_left").outerWidth());
			$('#dragBar_extra').css("left",$(".viewer_left").outerWidth() + $(".viewer_right").outerWidth());
			
		},
		setBorderEvent : function(elBorder) {
			
			var elPrevID = elBorder.prev().attr("id");
        	var elNextID = elBorder.next().attr("id");
        	var minAreaX = 0;
        	var maxAreaX = 0;
			
			elBorder.mousedown(function(e){
					minAreaX = 0;
					maxAreaX = 0;
					//Get min width
		        	$.each(elBorder.siblings(), function(){
		    			if($(this).attr("id") == elPrevID)
		    			{
		    				minAreaX += parseInt($(this).attr("min-width"));
		    				return false;
		    			}
		    			minAreaX += $(this).outerWidth();
		        	});
		        	
		        	//Get max width
		        	$.each(elBorder.siblings(), function(){
		    			if($(this).attr("id") == elNextID)
		    			{
		    				maxAreaX =  $(document).width() - parseInt($(this).attr("min-width")) ;
		    				
		    				var elLast = elBorder.siblings("[align=last]");
		    				
		    				if(elNextID != elLast.attr("id"))
		    				{
			    				if(elLast.is(":visible")) {
			    					var lastLeft =  elLast.offset().left;
			    					maxAreaX  =  maxAreaX - ($(document).width() - lastLeft) ;
			    				}
		    				}
		    				return false;
		    			}
		        	});
		        	
				   $(this).attr("dragging","1");
			       var elSlip 		= $(".slip_wrapper");
			       var areaDragBar	= $(document.createElement('div'))
			       							.attr({
			       								id:"dragBarArea",
			       							})
			       							.css({
			       								position:"absolute",
			       								top:0,
			       								right:0,
			       								bottom:0,
			       								left:0,
			       								width:"100%",
			       								height:"100%",
			       							}).appendTo('.wrapper');
			       
			       var ghostBar 	= $(document.createElement('div'))
			       							.attr({
			       								id:'ghostBar',
			       							})
					                        .css({
					    	   					width: "2px",
			    	   							height: elBorder.outerHeight(),
			    	   							top: $(".area_viewer_title").height(),
			    	   							left: elBorder.offset().left - 1,
			    	   							background:"rgba(0,0,0,0.4)",
			    	   							position:"absolute"
					                        }).appendTo(areaDragBar);
			       
			       minAreaX += 10;
			       
			        $(document).off("mousemove").mousemove(function(e){
			        	e.preventDefault();
			        	
			        	if(e.clientX >= minAreaX && e.clientX <=  maxAreaX)
			        	{
			        		ghostBar.css("left", e.clientX);
			        	}
			        	else
			        	{
			        		if(e.clientX < minAreaX) ghostBar.css("left", minAreaX);
			        		if(e.clientX > maxAreaX) ghostBar.css("left", maxAreaX);
			        	}
			       });
			    });
			

			   $(document).mouseup(function(e){

				  var isDragging = elBorder.attr("dragging");
				  if("1" == isDragging)
			      {
					  var left 			= $('#ghostBar').offset().left;
					  if("1" == $.Viewer.params.MENU) {
						  left -= $("#list_btnLeft").width();
					  }
					  var beforeLeft 	= elBorder.offset().left;
					  if("1" == $.Viewer.params.MENU) {
						  beforeLeft -= $("#list_btnLeft").width();
					  }
					  
					  elBorder.css("left", left);
					  
					  var moveWidth = left - beforeLeft;
					  var moveWidthPercent = $.Common.round(moveWidth / $(".area_viewer_content").outerWidth() * 100, 1); // Math.round(moveWidth / $(document).outerWidth() * 100, 1);
				
					  if(moveWidthPercent != 0)
					  {
						  var prevWidthPercent = $.Common.getWidthPercent(elBorder.prev());
						  var prevWidth = prevWidthPercent + moveWidthPercent;
						  
						  
						  
						  elBorder.prev().css("width", prevWidth+"%");
	
						  var elLast = elBorder.siblings(":last");
						  
				
						  moveWidthPercent = -moveWidthPercent;
						  var nextWidthPercent =  $.Common.getWidthPercent(elBorder.next());
						  var nextWidth = nextWidthPercent + (moveWidthPercent);
						  
						 nextWidth = $.Viewer.Verify_Width(elBorder.next(), nextWidth);
						  
						  


						  elBorder.next().css("width", nextWidth+"%");
					  }
					  $('#ghostBar').remove();
					  $("#dragBarArea").remove();
					  
					  $(document).unbind('mousemove');
					  elBorder.attr("dragging","0");
					  
					  $('#slip_masonry').masonry('layout');
					  
					  if($("#area_slip").is(":visible")) {
						  $("#area_slip").getNiceScroll().resize();
					  }
					  
					  $.Viewer.resetViewer();
			       }
			    });
		},
		Verify_Width : function(elTarget, targetWidth){
			var totalWidthPercent = 0;
			var res = targetWidth;
			$.each($(".area_viewer_content").children("div"), function() {
				
				if($(this).attr("tag") == "drag") {
					return true;
				}

				if(this == elTarget[0]) {
					return true;
				}

				totalWidthPercent += $.Common.getWidthPercent($(this));
			});

			totalWidthPercent += targetWidth;

			if(totalWidthPercent > 100 ) {
				res = res - (totalWidthPercent - 100);
			}

			return res;
		},
		setBorderDrag : function() {
			
			$.Viewer.setBorderPosition();
			$.Viewer.setBorderEvent($('#dragBar_viewer'));
			$.Viewer.setBorderEvent($('#dragBar_extra'));
			
		},
		getSlipList : function() {
		
			var params = $.Viewer.params;
			var objCntParams = {
					KEY :  $.Common.get_ObjectValue(params.KEY),
					KEY_TYPE : params.KEY_TYPE,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG
			};
			
			var objListParams = {
					KEY : params.KEY,
					KEY_TYPE : params.KEY_TYPE,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG,
					START_IDX : $.Viewer.startIdx,
					PER : $.Viewer.slipRange
			};

			// $.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_COUNT", objCntParams),
			// 		$.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res1, res2) {
			// 		$.Viewer.slipTotalCnt = parseInt(res1.THUMB_CNT);
			// 		if($.Viewer.slipTotalCnt > 0)
			// 		{
			// 			$.Viewer.arObjSlipItem = res2;
			// 			$.Viewer.addSlipItem(res2, $("#slip_masonry"));
			// 			$.Viewer.startIdx = ($.Viewer.startIdx + $.Viewer.slipRange) + 1;
			//
			// 		}
			// 		else
			// 		{
			// 		//	$.Common.HideProgress("#slip_progress");
			// 		}
			//
			// 	})
			$.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res) {
				if(res != null && res.length > 0) {
					var thumbCnt = 0;
					$.each(res, function(){
						if("0" === this.DOC_NO) {
							thumbCnt++;
						}
					});
					$.Viewer.slipTotalCnt = thumbCnt;

					$.Viewer.arObjSlipItem = res;
					$.Viewer.addSlipItem(res, $("#slip_masonry"));
					$.Viewer.startIdx = ($.Viewer.startIdx + $.Viewer.slipRange) + 1;

				}
				else {
					$.Common.HideProgress("#slip_progress");
				}


			})
			.fail(function(res){
				alert("Failed to load thumbs.");
		//

			}).always(function(){
				$.Common.HideProgress("#info_progress");
				$.Common.HideProgress("#slip_progress");
			});
		
		},
		getAttachCount : function(params) {
			
			var objCntParams = {
					KEY :  $.Common.get_ObjectValue(params.KEY),
					KEY_TYPE : params.KEY_TYPE,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_ATTACH_LIST", objCntParams)).done(function(res) {
						$.Viewer.attachCnt = res.length;
				if($.Viewer.attachCnt > 0) {
					$.Viewer.toggleAttachList();
				}
						
			}).fail(function(res){
		//		alert("Failed to load attach.");
			}).always(function(){
				
	//			$.Common.HideProgress("#attach_progress");
				
			});
			
		},
		getAttachList : function(params)
		{
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			var objListParams = {
					KEY :  params.KEY,
					KEY_TYPE : params.KEY_TYPE,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG,
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_ATTACH_LIST", objListParams)).done(function(res) {
				
				$.Viewer.arObjAttachItem = res;
				$.Viewer.addAttachItem(res, $("#area_attach"), $.Viewer.currentKey);
				
				$("#area_attach").niceScroll({horizrailenabled: false, cursorcolor:"#"+$.Viewer.colorSet.NAVIGATION});
				
			}).fail(function(res){
	//			alert("Failed to load attach.");
			}).always(function(){
				
				$.Common.HideProgress("#attach_progress");
				
			});
		},
		addAttachItem : function(arObjAttach, elDest, specificKey) {
			
			elDest.empty();
			
			$.each(arObjAttach,function(i){
				
				if(!$.Common.isBlank(specificKey) && specificKey.indexOf(",") == -1 && this.JDOC_NO != specificKey) return true;
				
				var elAttach = $.Viewer.getAttachElement(this);
				elAttach.appendTo(elDest);
				
				$.Viewer.setAttachMouseEvent(elAttach);
			});
		},
		//Draw attach item
		getAttachElement : function(objData) {
		
			//Draw attach outline
			var elAttach = $(document.createElement('div'));
			elAttach.addClass("attach_item");
			elAttach.attr("id","attach_item");
			elAttach.attr("idx",objData.SDOC_NO);
			elAttach.attr("command","VIEW_ORIGINAL_ATTACH");
			
			//Draw checkbox area
			var elTitleCheckbox = $(document.createElement('div'));
			elTitleCheckbox.addClass("viewer_area_cb");
			elTitleCheckbox.appendTo(elAttach);
			
			//Draw checkbox
			var elCheckbox =  $(document.createElement('label'));
			elCheckbox.addClass("cb_container");
			elCheckbox.addClass("attach_check");
			elCheckbox.append($(document.createElement('input')).attr({"id":"chk", "type":"checkbox"}));
			elCheckbox.append($(document.createElement('span')).addClass("checkbox"));
			elCheckbox.appendTo(elTitleCheckbox);
			
		
			
			var elInfoArea = $(document.createElement('div'));
			elInfoArea.addClass("area_info");
		//	elInfoArea.css("background-color","rgb("+objData.SDOC_COLOR+")");
			elInfoArea.appendTo(elAttach);
			
			var titleName = objData.SDOC_NAME;
		//	if("AFTER" == $.Viewer.params.VIEW_MODE) {
				
				if("1" == objData.SDOC_AFTER) {
					titleName = "★) "+objData.SDOC_NAME;
				}
		//	}
			
			var elAttachTitle = $(document.createElement('div'));
			elAttachTitle.addClass("area_name");
			elAttachTitle.css("color","#"+$.Viewer.colorSet.FONT_COLOR );
			elAttachTitle.append($(document.createElement('span')).html(titleName));
			elAttachTitle.appendTo(elInfoArea);
			
			//Draw attach Type
			var elAttachType = $(document.createElement('div'));
			elAttachType.html(objData.SDOC_KINDNM);
			elAttachType.addClass("area_attach_type");
			elAttachType.css({
				"color":"#"+$.Viewer.colorSet.ATTACH_TYPE_FONT
			});
			elAttachType.appendTo(elInfoArea);
			
			
			//Draw attach Type area
			var elAttachBtnArea = $(document.createElement('div'));
			elAttachBtnArea.addClass("area_attach_down");
			elAttachBtnArea.attr("command","VIEW_ORIGINAL_ATTACH")
			elAttachBtnArea.append($(document.createElement('img')).attr("src", g_RootURL+"image/common/context/download_cs.png"));
			elAttachBtnArea.unbind("click").bind("click",function(e){e.stopPropagation(); $.Operation.execute($.Viewer, elAttachBtnArea, objData)});
			elAttachBtnArea.appendTo(elAttach);
			
			return elAttach;
		},
		setUIColor : function()
		{
			var objColor =  $.extend($.Color.PC.Viewer, $.Color.Common);

			if(objColor != null)
			{
				$(".area_menu_btn").css("background","#"+objColor.NAVIGATION);
				$(".area_viewer_title").css({"background":"#"+objColor.NAVIGATION, "color" : "#" + objColor.NAVIGATION_FONT});
				$(".info_title").css({"border-bottom":"2px solid #"+objColor.BORDER});
				$(".viewer_main").css({"border-top":"2px solid #"+objColor.BORDER, "border-bottom":"2px solid #" + objColor.BORDER});
				$("#area_slip").niceScroll({horizrailenabled: false, cursorcolor:"#"+objColor.NAVIGATION});
				$(".viewer_right .title").css({"border-top":"2px solid #"+objColor.BORDER});
				$(".viewer_right .area_original").css({"border-bottom":"2px solid #"+objColor.BORDER});
				$.Viewer.colorSet = objColor;
			}
		},
		
		addContextMenu : function(elTarget, menuGroup, objData, viewMode, option) {
			//Set click event
			var fnClick = function(e) {
				
			}
			//Add icon
			var iconURL = g_RootURL+"image/common/option";
			
			var elBtn = $(document.createElement('div'));
			elBtn.unbind().bind("click",fnClick);
			elBtn.appendTo(elTarget);
			
			var elImg = $(document.createElement('img')).attr("src",iconURL + ".png");
			elImg.unbind('mouseenter mouseleave').hover(function(){
				$(this).css("opacity","0.7");
			},function(){
				$(this).css("opacity","1");
			});
			
			elImg.appendTo(elBtn);		
			
			var arObjMenu = $.Common.sortContextMenuItem($.Viewer.localeMsg, menuGroup, viewMode);
			
			$.ContextMenu.getMenu($.Viewer, elTarget, elBtn, arObjMenu, objData, option);
			
		},
		removeSlipElement : function(elTarget) {
			
			$.each(elTarget,function(){
				$(this).closest("#slip_item").remove();
			})
			$('#slip_masonry').masonry('layout');
			
		//	$.Viewer.redrawThumb();
			
			$("#area_slip").getNiceScroll().resize();
		},
		removeAttachElement : function(elTarget) {
			
			$.each(elTarget,function(){
				$(this).closest("#attach_item").fadeOut(200, function() { $(this).remove(); });
			})
			
			$("#area_attach").getNiceScroll().resize();
		},
		addSlipItem : function(arObjSlip, elDest, specificKey)
		{
			elDest.empty();
			elDest.masonry("layout");
			
			if(arObjSlip == null) return;
			
		//	var lastSDocNo = null;
			$.each(arObjSlip,function(i){
				
				if(!$.Common.isBlank(specificKey) && specificKey.indexOf(",") == -1 && this.JDOC_NO != specificKey) return true;
				
				var elThumb = $.Actor.getThumbElement(this, $.Viewer);
				
				if(elThumb != null)
				{			
					var elTitleArea = elThumb.find(".area_title_btn");
					
					//Draw fold icon
					$.Actor.addFoldIcon($.Viewer.arObjSlipItem, elTitleArea, elThumb.attr("idx"));
					
					//Draw option icon
					$.Viewer.addContextMenu(elTitleArea, $.Viewer.contextMenu["Thumb"], this, $.Viewer.params.VIEW_MODE);
				
					elThumb.appendTo(elDest);
					elDest.masonry('appended', elThumb);
					elThumb.css("opacity","0");
					$.Viewer.setThumbMouseEvent(elThumb.find(".area_thumb"));
				}
			});
			
			elDest.imagesLoaded(function(){
				
				$.each(elDest.children('[id="slip_item"]'), function(){
					$(this).css("opacity","1");
				});
				elDest.masonry('layout');
				
				//Finish UI Setting
				$.each($("#slip_masonry").find('[id="slip_item"]'), function(){
					if($(this).attr("first") == "1")
					{
						var idx = $(this).attr("idx");
						$.Actor.fold($.Viewer.arObjSlipItem, "btn_fold", idx);
					}
				});
				$('#slip_masonry').masonry('reloadItems');
				
				$.Common.HideProgress("[id=slip_progress]");
				$("#area_slip").getNiceScroll().resize();
				
				var version = $.Common.GetBrowserVersion().ActingVersion;
				if(version < 9)
				{
					if(Selectivizr != null)
					{
						Selectivizr.init(); //Refresh selectivizr if brower is under IE8
					}
				}
				
				if(version > 9)
				{
					//Add ripple effect.
					elDest.find('[class="area_effect"]').ripple({
						  maxDiameter: "200%"
					});
				}
				//select first item
				$.Viewer.selectSlipFromThumb();
			});
		},
		selectSlipFromThumb : function() {
			var selectedIdx 	= $.Viewer.params.SLIP_IRN;
			var isFold 			= $.Viewer.params.IS_SELECTED_FOLD;
			
			if(!$.Common.isBlank($.Viewer.currentKey)) {
				var selectedSlip = $("#slip_item:first");
				if(selectedSlip != null)
				{
					selectedSlip.find(".area_thumb").click();
				}
				
				return;
			}
			
			if(!$.Common.isBlank(selectedIdx))
			{
				$.each($("[id=slip_item]"), function(){
					
					if(selectedIdx == $(this).attr("idx"))
					{
						
						if("1" == isFold) {
							var objData = $.Common.getObjByValue($.Viewer.arObjSlipItem,"SLIP_IRN",selectedIdx)[0];
							var elParent = $("[group="+objData.SDOC_NO+"]").eq(0);
						//	elParent.attr("fold","1");
							$.Actor.fold($.Viewer.arObjSlipItem, "btn_fold", elParent.attr("idx"));
							elParent.attr("fold","1");
						//	
							
						}
						
						$(this).find(".area_thumb").click();
//						var item = $("[id=slip_item]:first").find(".area_thumb");
//						item.click();
						return false;
					}
				});
				
			}
			else
			{
				var selectedSlip = $("#slip_item:first");
				if(selectedSlip != null)
				{
					selectedSlip.find(".area_thumb").click();
				}
			}
		},
		displaySlipInfo : function(objData){
			
			$(".info_content").empty();
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.JDOC_NO, objData.JDOC_NO));
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.REG_DATE, objData.CABINET));
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.CORP_NO, objData.CORP_NM + "(" +objData.CORP_NO + ")"));
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.PART_NO, objData.PART_NM + "(" +objData.PART_NO + ")"));
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.REG_USER, objData.REG_USERNM + "(" +objData.REG_USER + ")"));
		},
		
		getInfoElement : function(title, value) {
			
			var elInfoBlock = $(document.createElement('div'));
			elInfoBlock.addClass("content_block");
			var elTitle = $(document.createElement('div'));
			elTitle.addClass("content_title");
			elTitle.html(title);
			elTitle.appendTo(elInfoBlock);
			
			var elValue = $(document.createElement('div'));
			elValue.addClass("content_value");
			elValue.html(value);
			elValue.appendTo(elInfoBlock);
			
			return elInfoBlock;
		},
		
		/**
		 * Display original image
		 */
		displayOriginal : function(elTarget, objData){
			
			elTarget.empty();
			$.Common.ShowProgress("#original_progress","Waiting..","000000","0.7");
			
			var sbImgURL = new StringBuffer();
			sbImgURL.append(this.rootURL);
			sbImgURL.append("DownloadImage.do?");
			sbImgURL.append("&DocIRN="+objData.DOC_IRN);
			sbImgURL.append("&Idx="+objData.DOC_NO);
			sbImgURL.append("&degree="+objData.SLIP_ROTATE);
			sbImgURL.append("&UserID="+$.Viewer.params.USER_ID);
			sbImgURL.append("&CorpNo="+$.Viewer.params.CORP_NO);
			
			var elCenterVerticalHelper = $(document.createElement('span'));
			elCenterVerticalHelper.addClass("helper");
			elCenterVerticalHelper.appendTo(elTarget);
			
			var elImage = $(document.createElement('img'));
			elImage.attr({
				"src":sbImgURL.toString()
			});
			elImage.appendTo(elTarget);
			
			$.Common.HideProgress("#original_progress");
			
			$.Viewer.resetViewer();
		},
		Download_Original : function(element) {
			if($.Viewer.Cur_Slip != null) {
				$.Operation.execute($.Viewer, $(element).attr("command"), $.Viewer.Cur_Slip);
			}
			
		},
		// ZoomIn_Viewer : function() {
		// 	$.Viewer.viewer.panzoom.zoomIn;
		// },
		// ZoomOut_Viewer : function() {
		// 	$.Viewer.viewer.panzoom.zoomOut;
		// },
		
		resetViewer : function() {
			if($.Viewer.viewer != null)
			{
				$.Viewer.viewer.panzoom("reset", false);
			}

			if($.Viewer.is_Maximized) {
					$.Viewer.is_Maximized = false;
					$.Viewer.ZoomIn_Thumb();
				}
		},
		/**
		 * Set mouse events on target thumb.
		 */
		setThumbMouseEvent : function(elThumb) {
			
			var version = $.Common.GetBrowserVersion().ActingVersion;

			var idx = elThumb.closest("#slip_item").attr("idx");
			var objSelelctedItem = $.Common.getObjByValue($.Viewer.arObjSlipItem,"SLIP_IRN",idx)[0];
			
		
			//Click event
			var evClick = function(e) {
				
				if(e.ctrlKey) {
					
				}
				else
				{
					$.Viewer.Cur_Slip = objSelelctedItem;

					//reset check status to false for multi selecting mode
					$("[id=slip_item]").css("outline","");
					$("[id=slip_item]").find("#chk").attr('checked', false);
					$("[id=attach_item]").css("background-color","");
					$("[id=attach_item]").find("#chk").attr('checked', false);
					
					
					//Get clicked slip info
					$.Viewer.displaySlipInfo(objSelelctedItem);
					
					//Show original image
					$.Viewer.displayOriginal($("#originalImage"), objSelelctedItem);
				}
				var cb = elThumb.closest("#slip_item").find("#chk")[0];
				cb.checked = !cb.checked;
				
				if(cb.checked) 
				{
					elThumb.closest("#slip_item").css("outline","2px solid #"+ $.Viewer.colorSet.FOCUSED_SLIP);
				}
				else
				{
					elThumb.closest("#slip_item").css("outline","");
				}
			}
			
			//Double click event
			var evDblClick = function() {
			//	$.Actor.run(elThumb.closest("#slip_item"));
			}

			var DELAY = 400, clicks = 0, timer = null;
			elThumb.on("click", function(e){
				e.preventDefault();
				if(version < 9)
				{
					evClick(e);
				}
				else
				{
					clicks++;  //count clicks
			        if(clicks === 1) {
			            timer = setTimeout(function() {
			            	evClick(e);  //perform single-click action    
			                clicks = 0;             //after action performed, reset counter
			            }, DELAY);
			        } else {
			            clearTimeout(timer);    //prevent single-click action
			            evDblClick(e);  //perform double-click action
			            clicks = 0;             //after action performed, reset counter
			        }
				}
		    })
		    .on("dblclick", function(e){
		    	if(version < 9)
				{
		    		evDblClick();
				}
				else
				{
					e.preventDefault();  //cancel system double-click event
				}
		    });
		},
		setAttachMouseEvent : function(elAttach) {
			
			elAttach.unbind("").bind("click",function(e){
				
				if(e.ctrlKey) {
					
				}
				else
				{
					$("[id=slip_item]").css("outline","");
					$("[id=slip_item]").find("#chk").attr('checked', false);
					$("[id=attach_item]").css("background-color","");
					$("[id=attach_item]").find("#chk").attr('checked', false);
				}
				
				var cb = elAttach.find("#chk")[0];
				cb.checked = !cb.checked;
				
				if(cb.checked) 
				{
					elAttach.css("background-color","#"+ $.Viewer.colorSet.FOCUSED_ATTACH);
				}
				else
				{
					elAttach.css("background-color","");
				}
				
			});
			
		},
		close : function() {
			window.close();
		},
		//Toggle attach list
		toggleAttachList : function() {
			
			var elAttachArea = $("#viewer_right_extra");
			var isShown = elAttachArea.attr("show");
			if("0" == isShown) 
			{
				
				var widthLeft = $.Common.getWidthPercent($(".viewer_left"));
				var widthViewer = $.Common.getWidthPercent($(".viewer_right"));
				var widthAttach = $.Common.getWidthPercent(elAttachArea);
					
				var viewerMin =  $.Common.round(parseInt($(".viewer_right").attr("min-width")) / $(document).outerWidth() * 100, 1);
				
				var expectedWidth = widthViewer - widthAttach;
				
				if(expectedWidth < viewerMin)
				{
					$(".viewer_left").css("width",(widthLeft - widthAttach) + "%");
//					
					$('#slip_masonry').masonry('layout');
					  
					  if($("#area_slip").is(":visible")) {
						  $("#area_slip").getNiceScroll().resize();
					  }
				}
				else
				{
					$(".viewer_right").css("width",(widthViewer - widthAttach) + "%");
				}
				
				
				
				
//				var leftOccupancy = 100 - widthAttach;
//				
//				var overflow = leftOccupancy -widthLeft - widthViewer;
//				
//				if(overflow < widthAttach)
//				{
//					$(".viewer_left").css("width",(widthLeft - widthAttach) + "%");
//					
//					$('#slip_masonry').masonry('layout');
//					  
//					  if($("#area_slip").is(":visible")) {
//						  $("#area_slip").getNiceScroll().resize();
//					  }
//				}
//				else
//				{
//					elViewerArea.css("width",(widthViewer - widthAttach) + "%");
//				}
				
				
				elAttachArea.show();
				elAttachArea.attr("show","1");
				
				//Get attach list on first show
				if($.Viewer.arObjAttachItem == null) {
					$.Viewer.getAttachList($.Viewer.params);
				}
			}
			else
			{
				var widthLeft = $.Common.getWidthPercent($(".viewer_left"));
				$(".viewer_right").css("width",100 - widthLeft + "%");
				elAttachArea.hide();
				elAttachArea.attr("show","0");
			}
			
			$.Viewer.resetViewer();
			
			$.Viewer.setBorderPosition();
		},
		setImageViewer : function() {
			
			var panzoom = $("#originalImage").panzoom({
				minScale: 0.7,
				maxScale: 5,
				   $zoomIn:$("#zoomIn"),
				  $zoomOut:$("#zoomOut"), 
				animate:true
			});
			
			panzoom.parent().on('mousewheel.focal', function(e) {
				e.preventDefault();
	            var delta = e.delta || e.originalEvent.wheelDelta;
	            var zoomOut = delta ? delta < 0 : e.originalEvent.deltaY > 0;
	            panzoom.panzoom('zoom', zoomOut, {
					animate: false,
					focal: e,
					increment:0.1
				});
			}); 
			
			 return panzoom;	
		 }
}