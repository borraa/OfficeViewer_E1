"use strict";
$.Viewer = {
		localeMsg : null,
		colorSet : null,
		params : null,
		slipRange : 100,
		thumbWidth : 140,
		startIdx : 0,
		slipTotalCnt : 0,
		arObjSlipItem: null,
		arObjAttachItem : null,
		contextMenu : null,
		attachCnt : 0,
		init : function(params) {
			
			this.params 				= params;
			
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#info_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#original_progress","Waiting..","000000","0.7");
		
			/**
			 * resize scroll window on resize browser
			 */
			$(window).on('resize', $.Common.windowCallback(function(){
				$(".slip_wrapper").getNiceScroll().resize();
			}));

			//Set globalization.
			$.Viewer.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"Viewer");

			//Set UI set
			$.Viewer.setUIColor();
			
			$.Viewer.contextMenu = $.Menu.PC.Viewer;
		
			
			$('#slip_masonry').masonry({
				  // options
				  itemSelector: '#slip_item',
				  columnWidth: $.Viewer.thumbWidth,
				  horizontalOrder: true,
				  isFitWidth: true,
				  gutter:20
				});
			
			$.Viewer.getSlipList();
			
			//Set drag event
			$.Viewer.setBorderDrag();
			
			//Toggle attachlist area
			$.Viewer.getAttachCount($.Viewer.params);
			$.Viewer.toggleAttachList();
			
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
		    				minAreaX += parseInt($(this).css("min-width"));
		    				return false;
		    			}
		    			
		    			
		    			minAreaX += $(this).outerWidth();
		        	});
		        	
		        	//Get max width
		        	$.each(elBorder.siblings(), function(){
		    			if($(this).attr("id") == elNextID)
		    			{
		    				maxAreaX =  $(document).width() - parseInt($(this).css("min-width"));
		    				
		    				var elLast = elBorder.siblings(":last");
		    				
		    				if(elNextID != elLast.attr("id"))
		    				{
			    				if(elLast.is(":visible")) {
			    					var lastLeft =  elLast.offset().left;
			    					maxAreaX  =  maxAreaX - ($(document).width() - lastLeft) ;
			    				}
		    				}
		    				return false;
		    				
		    			}
		    			
		    		//	maxAreaX +=  parseInt($(this).outerWidth());
		    			
//		    			if($(this).is(":visible")) {
//		    				
//		    				if($(this).is(':last-child')) {
//		    					maxAreaX +=  parseInt($(this).css("min-width"));
//		    				}
//		    				else
//		    				{
//		    					
//		    				}
//		    			}
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
					  elBorder.css("left", left);
					  
					  elBorder.prev().css("width", 'auto');
					  elBorder.prev().css("right",$(document).width() - left);
					  elBorder.next().css("width", 'auto');
					  elBorder.next().css("left", left);
					  
					  
					  $('#ghostBar').remove();
					  $("#dragBarArea").remove();
					  
					  $(document).unbind('mousemove');
					  elBorder.attr("dragging","0");
					  
					  $('#slip_masonry').masonry('layout');
					  
					  if($("#area_slip").is(":visible")) {
						  $("#area_slip").getNiceScroll().resize();
					  }
			       }
			    });
		},
		setBorderDrag : function() {
			
			$.Viewer.setBorderPosition();
			$.Viewer.setBorderEvent($('#dragBar_viewer'));
			$.Viewer.setBorderEvent($('#dragBar_extra'));
			
		},
		getSlipList : function() {
		
			var params = $.Viewer.params;
			var objCntParams = {
					JDOC_NO : params.JDOC_NO,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG
			};
			
			var objListParams = {
					JDOC_NO : params.JDOC_NO,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG,
					START_IDX : $.Viewer.startIdx,
					PER : $.Viewer.slipRange
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_COUNT", objCntParams), 
					$.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res1, res2) {
					$.Viewer.slipTotalCnt = parseInt(res1.THUMB_CNT);
					if($.Viewer.slipTotalCnt > 0)
					{
						$.Viewer.arObjSlipItem = res2;
						$.Viewer.addSlipItem(res2, $("#slip_masonry"));
						$.Viewer.startIdx = ($.Viewer.startIdx + $.Viewer.slipRange) + 1;
						
						$("#slip_item:first").closest("area_thumb").trigger("click");
					}
					else
					{
					//	$.Common.HideProgress("#slip_progress");
					}
						
				}).fail(function(res){
					alert("Failed to load thumbs.");
			//		
				
				}).always(function(){
					$.Common.HideProgress("#info_progress");
					$.Common.HideProgress("#slip_progress");
			});
		
		},
		getAttachCount : function(params) {
			
			var objCntParams = {
					JDOC_NO : params.JDOC_NO,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_ATTACH_LIST", objCntParams)).done(function(res) {
						$.Viewer.attachCnt = res.length;
						
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
					JDOC_NO : params.JDOC_NO,
					USER_ID : params.USER_ID,
					CORP_NO : params.CORP_NO,
					LANG : params.LANG,
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_ATTACH_LIST", objListParams)).done(function(res) {
				
				$.Viewer.arObjAttachItem = res;
				$.Viewer.addAttachItem(res, $("#attachContent"));
				
			}).fail(function(res){
	//			alert("Failed to load attach.");
			}).always(function(){
				
				$.Common.HideProgress("#attach_progress");
				
			});
		},
		addAttachItem : function(arObjAttach, elDest) {
			
			$.each(arObjAttach,function(i){
				var elAttach = $.Viewer.getAttachElement(this);
				elAttach.appendTo(elDest);
			});
		},
		//Draw attach item
		getAttachElement : function(objData) {
		
			//Draw attach outline
			var elAttach = $(document.createElement('div'));
			elAttach.addClass("attach_item");
			elAttach.attr("id","attach_item");
			elAttach.attr("command","VIEW_ORIGINAL_ATTACH");
			
			//Draw checkbox area
			var elTitleCheckbox = $(document.createElement('div'));
			elTitleCheckbox.addClass("viewer_area_cb");
			elTitleCheckbox.appendTo(elAttach);
			
			//Draw checkbox
			var elCheckbox =  $(document.createElement('label'));
			elCheckbox.addClass("cb_container");
			elCheckbox.addClass("attach_check");
			elCheckbox.append($(document.createElement('input')).attr({"id":"ipt_cb_chk", "type":"checkbox","sdoc_no":objData.SDOC_NO,"doc_irn":objData.DOC_IRN,"slip_irn":objData.SLIP_IRN}));
			elCheckbox.append($(document.createElement('span')).addClass("checkbox"));
			elCheckbox.appendTo(elTitleCheckbox);
			
		
			
			var elInfoArea = $(document.createElement('div'));
			elInfoArea.addClass("area_info");
		//	elInfoArea.css("background-color","rgb("+objData.SDOC_COLOR+")");
			elInfoArea.appendTo(elAttach);
			
			var elAttachTitle = $(document.createElement('div'));
			elAttachTitle.addClass("area_name");
			elAttachTitle.css("color","#"+$.Viewer.colorSet.FONT_COLOR );
			elAttachTitle.append($(document.createElement('span')).html(objData.FILE_NAME));
			elAttachTitle.appendTo(elInfoArea);
			
			//Draw attach Type
			var elAttachType = $(document.createElement('div'));
			elAttachType.html(objData.ADD_KINDNM);
			elAttachType.addClass("area_attach_type");
			elAttachType.css({
				"color":"#"+$.Viewer.colorSet.ATTACH_TYPE_FONT
			});
			elAttachType.appendTo(elInfoArea);
			
			
			//Draw attach Type area
			var elAttachBtnArea = $(document.createElement('div'));
			elAttachBtnArea.addClass("area_attach_down");
			elAttachBtnArea.attr("command","DOWN_ORIGINAL_ATTACH")
			elAttachBtnArea.append($(document.createElement('img')).attr("src", g_RootURL+"image/common/context/download_cs.png"));
			elAttachBtnArea.unbind("click").bind("click",function(){ $.Operation.execute(elAttachBtnArea, objData); });
			elAttachBtnArea.appendTo(elAttach);
			
			return elAttach;
		},
		setUIColor : function()
		{
			var objColor =  $.extend($.Color.PC.Viewer, $.Color.Common);

			if(objColor != null)
			{
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
	
		addSlipItem : function(arObjSlip, elDest)
		{
		//	var lastSDocNo = null;
			$.each(arObjSlip,function(i){
				
				var elThumb = $.Actor.getThumbElement(this, $.Viewer);
				
				if(elThumb != null)
				{			
					var elTitleArea = elThumb.find(".area_title_btn");
					
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
				$.each( $("#slip_masonry").find('[id="slip_item"]'), function(){
					if($(this).attr("first") == "1")
					{
						var sdocNo = $(this).find("#ipt_cb_chk").attr("sdoc_no");
						 $.Actor.fold("btn_fold", sdocNo);
					}
				});
				$('#slip_masonry').masonry('reloadItems');
				
			//	$.Common.HideProgress("#slip_progress");
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
				var item = $("[id=slip_item]:first").find(".area_thumb");
				item.click();
			});
		},
		
		displaySlipInfo : function(elThumb){
			
			var slipIrn = $(elThumb.closest("#slip_item").find("#ipt_cb_chk")[0]).attr("slip_irn");
			
			var objData = $.Common.getObjByValue($.Viewer.arObjSlipItem,"SLIP_IRN",slipIrn);
			$(".info_content").empty();
			$(".info_content").append($.Viewer.getInfoElement($.Viewer.localeMsg.JDOC_NO, objData.JDOC_NO));
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
		displayOriginal : function(elTarget, elCurImage){
			
			elTarget.empty();
			$.Common.ShowProgress("#original_progress","Waiting..","000000","0.7");
			
			var slipIrn = $(elCurImage.closest("#slip_item").find("#ipt_cb_chk")[0]).attr("slip_irn");
			//Add image
			var objData = $.Common.getObjByValue($.Viewer.arObjSlipItem,"SLIP_IRN",slipIrn);
			
			var sbImgURL = new StringBuffer();
			sbImgURL.append(this.rootURL);
			sbImgURL.append("DownloadImage.do?");
			sbImgURL.append("&DocIRN="+objData.DOC_IRN);
			sbImgURL.append("&Idx="+objData.DOC_NO);
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
		},
		/**
		 * Set mouse events on target thumb.
		 */
		setThumbMouseEvent : function(elThumb) {
			
			var version = $.Common.GetBrowserVersion().ActingVersion;

			//Click event
			var evClick = function(e) {
				
				if(e.ctrlKey) {
					
				}
				else
				{
					//reset check status to false for multi selecting mode
					$("[id=slip_item]").css("outline","");
					$("[id=slip_item]").find("#ipt_cb_chk").attr('checked', false);
					
					//Get clicked slip info
					$.Viewer.displaySlipInfo(elThumb);
					
					//Show original image
					$.Viewer.displayOriginal($("#originalImage"), elThumb);
				}
				var cb = elThumb.closest("#slip_item").find("#ipt_cb_chk")[0];
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
		close : function() {
			window.close();
		},
		//Toggle attach list
		toggleAttachList : function() {
			
			var elAttachArea = $("#viewer_right_extra");
			var isShown = elAttachArea.attr("show");
			if("1" == isShown) 
			{
				
				var right = elAttachArea.width();
				
				var expectedRight = $(document).width() - ($(".viewer_right").offset().left + right);
				
				
				if(expectedRight < parseInt($(".viewer_right").css("width")))
				{
					$(".viewer_left").css("right", parseInt($(".viewer_left").css("right")) + right);
					$(".viewer_right").css("left", parseInt($(".viewer_left").width()));
					
					$('#slip_masonry').masonry('layout');
				}
				
				var elLeft = $(".viewer_left");
				var elRight = $(".viewer_right");
				
				$(".viewer_right").css("right", right);
				elAttachArea.show();
				elAttachArea.attr("show","0");
				
				//Get attach list on first show
				if($.Viewer.arObjAttachItem == null) {
					$.Viewer.getAttachList($.Viewer.params);
				}
			}
			else
			{
				$(".viewer_right").css("right","0");
				elAttachArea.hide();
				elAttachArea.attr("show","1");
			}
			
			$.Viewer.setBorderPosition();
		}
}