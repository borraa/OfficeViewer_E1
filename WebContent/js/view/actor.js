"use strict";


$.Actor = {
		localeMsg : null,
		colorSet : null,
		params : null,
		slipRange : 9999,
		thumbWidth : 160,
		startIdx : 0,
		slipTotalCnt : 0,
		attachTotalCnt : 0,
		arObjSlipItem: null,
		arObjAttachItem : null,
		contextMenu : null,
		currentKey : null,
		is_Maximized : false,
		original_scrollHeight : -1,
		init : function(params) {
			 
			$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
			$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
			
			/**
			 * resize scroll window on resize browser
			 */
			
			$(window).on('resize', $.Common.windowCallback(function(){

				if($.Actor.is_Maximized) {
					$.Actor.is_Maximized = false;
					$.Actor.ZoomIn_Thumb();
				}
				// else {
				// 	$.Actor.ZoomOut_Thumb();
				// }
				else {	
					$("#area_slip").getNiceScroll().resize();
					$("#area_attach").getNiceScroll().resize();	
					$("#key").getNiceScroll().resize();
				}
			}));
			
			//Set Actor default enviroments.
			$.Actor.params 		= params;
	
			//if(!$.Actor.params.MULTI_KEY) {
				$.Actor.currentKey = $.Actor.params.KEY;
			//}
			
			$.Actor.initButtons();
			//Set globalization.
			$.Actor.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"Actor");
			//Set UI set
			$.Actor.setUIColor();
			
			//Get Actor menu list
			$.Actor.contextMenu = $.Menu.PC.Actor;
			
			$.Actor.Draw_MainContext();
			
			var arObjAdd = $.Common.sortContextMenuItem($.Actor.localeMsg,  $.Actor.contextMenu['Add'], $.Actor.params.VIEW_MODE, $.Actor.currentKey);
			$.ContextMenu.getMenu($.Actor, $("#btn_add_slip"), $("#btn_add_slip"), arObjAdd, $.Actor.params, { spacing_top : true});				
		
			var arObjRemove = $.Common.sortContextMenuItem($.Actor.localeMsg,  $.Actor.contextMenu['Remove'], $.Actor.params.VIEW_MODE, $.Actor.currentKey);
			$.ContextMenu.getMenu($.Actor, $("#btn_remove"), $("#btn_remove"), arObjRemove, $.Actor.params, { spacing_top : true});			
			
			//Set drag event
			$.Actor.setBorderDrag();
			/*
			 * Detect LocalWAS
			 */	
			if(typeof OfficeXPI == "undefined")
			{
				
				//Load XPI Script
				$.getScript(g_XPI_URL, function() {
				
					var localWAS_URL = location.protocol + "//127.0.0.1:" +  $.Actor.params.XPI_PORT;
					
					var XPIParams = {
							LOCAL_WAS_URL		: localWAS_URL,
							LANG 						: $.Actor.params.LANG,
							SERVER_KEY 			: $.Actor.params.SERVER_KEY,
					};
					
					/**
					 * Call LocalWAS
					 */
					$.when($.OfficeXPI.init(XPIParams, $.Lang)).then(function(a){
					
					}).fail(function(){
					//	$.Actor.attachDownloadLink();
			        	
					});
		        }).fail(function(){
		        	//$.Actor.attachDownloadLink();
		        	
		        }).always(function(){
		        	$.Actor.initializeSlip();
		        });
			}
		},
		Draw_MainContext :function() {
			var arObjMenu = $.Common.sortContextMenuItem($.Actor.localeMsg,  $.Actor.contextMenu['Menu'], $.Actor.params.VIEW_MODE, $.Actor.currentKey);
			$.ContextMenu.getMenu($.Actor, $("#btn_open_menu"), $("#btn_open_menu"), arObjMenu, $.Actor.params, { spacing_top : true});				
		
		},
		pageSubmit : function() {
		//	$.Common.postSubmit(g_RootURL + "/slip_actor.jsp", $.Actor.params, "post");
			$.Actor.reset();
			$.Actor.initializeSlip();
		},
		
		change_GroupKey : function(option) {
			var key = option.value;
			
			if($.Common.isBlank(key)) key = $.Actor.params.KEY;
			
			$.Actor.currentKey = key;
			
			$.Actor.Draw_MainContext();
			$.Common.Draw_LeftMenu($.Actor);
			
			$.Actor.addSlipItem($.Actor.arObjSlipItem, $("#slip_masonry"), key);
			$.Actor.addAttachItem($.Actor.arObjAttachItem, $("#area_attach"), key);
			
			if(!$.Common.isBlank($.Actor.currentKey) && $.Actor.currentKey.indexOf(",") <= -1) {
				$.Actor.getCommentCnt();
			}
		},
		resize_Scroll : function() {
			//$(".slip_wrapper").css("height",$.Actor.original_scrollHeight);
			//$(".attach_wrapper").css({"top":$.Actor.original_scrollHeight, "height": $(".wrapper").outerHeight() - $.Actor.original_scrollHeight});
		
		},
		ZoomIn_Thumb : function() {

			if(!$.Actor.is_Maximized) {

				$('#slip_masonry').masonry({
				   columnWidth: $(".area_slip").width() - 40
				});
				$(".slip_item").css("width", "100%");

				$('#slip_masonry').masonry('layout');
				$("#area_slip").getNiceScroll().resize();

				$.Actor.is_Maximized = true;
			}

		},
		ZoomOut_Thumb : function() {
			if($.Actor.is_Maximized) {
				$('#slip_masonry').masonry({
					columnWidth:  $.Actor.thumbWidth
					});
				$(".slip_item").css("width",  $.Actor.thumbWidth);

				$('#slip_masonry').masonry('layout');
				$("#area_slip").getNiceScroll().resize();

				$.Actor.is_Maximized = false;
			}
		},
		//Load slip items
		initializeSlip : function()
		{
			
			$.Actor.getSlipList($.Actor.params, $.Actor.startIdx);
			$.Actor.getAttachList($.Actor.params);
			
			if(!$.Actor.params.MULTI_KEY) {
				$.Actor.getCommentCnt();
			}
			
			$('#slip_masonry').masonry({
				  // options
				  itemSelector: '#slip_item',
				  columnWidth: $.Actor.thumbWidth,
				  horizontalOrder: true,
				  isFitWidth: true,
				  gutter: 20
				});
			
			$("#area_slip").scroll(function() {

			    if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
			    	// if( $.Actor.startIdx + $.Actor.slipRange >  $.Actor.slipTotalCnt)
					// {
			    	// 	return;
					// }
			    	//
			    	// $.Actor.getSlipList($.Actor.params, $.Actor.startIdx);
			    	//
			    }
			});
			
			//Link checkbox event
			$("#slip_checkAll").change(function(){
			
				var checked = this.checked;
				 var chkItems = $("[id=slip_item]:visible").find("input[type='checkbox']");
				 $.each(chkItems,function(){
					 this.checked = checked;
				 });	 
				 //Set focused color
				 if(checked)
				 {
					 $("[id=slip_item]:visible").css("outline","2px solid #"+ $.Actor.colorSet.FOCUSED_SLIP);	
				 }
				 else
				{
					 $("[id=slip_item]:visible").css("outline","");	
				}
			});
			
			$("#attach_checkAll").change(function(){
				
				var checked = this.checked;
				 var chkItems = $("#area_attach").find("input[type='checkbox']");
				 $.each(chkItems,function(){
					 this.checked =checked;
				 });	 
				//Set focused color
				 if(checked)
				 {
					 $("[id=attach_item]").css("background-color","#"+ $.Actor.colorSet.FOCUSED_ATTACH);	
				 }
				 else
				{
					 $("[id=attach_item]").css("background-color","");	
				}
			});
			
			
		},
		getCommentCnt: function() {
			var objParams = {
					KEY : $.Actor.currentKey
			};
			
			$.when($.Common.RunCommand(g_RootURL + "CommentCommand.do", "GET_COMMENT_COUNT", objParams)).then(function(value){
				$.Common.attachBadge($("#btn_open_comment"), value.COMT_CNT, $.Actor.colorSet.BADGE);
			});
			
		},
		setBorderDrag : function() {
			$('#dragBar').mousedown(function(e){
				
				$("[id=contextWrapper]").hide();
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
					    	   					width: elSlip.outerWidth(),
			    	   							height:"2px",
			    	   							top: elSlip.outerHeight(),
			    	   							left: elSlip.offset().left,
			    	   							background:"rgba(0,0,0,0.4)",
			    	   							position:"absolute"
					                        }).appendTo(areaDragBar);
			       
			        $(document).mousemove(function(e){
			        	e.preventDefault();
			        	var minAreaY	= 0 + $(".attach_title").outerHeight() - ghostBar.outerHeight();
			        	var maxAreaY	= $(".actor_right").outerHeight() - $(".attach_title").outerHeight();
			        	
			        	if(e.clientY >= minAreaY && e.clientY <= maxAreaY)
			        	{
			        		ghostBar.css("top", e.clientY);
			        	}
			        	else
			        	{
			        		if(e.clientY < minAreaY) ghostBar.css("top", minAreaY);
			        		if(e.clientY > maxAreaY) ghostBar.css("top", maxAreaY);
			        	}
			       });
			    });

			   $(document).mouseup(function(e){

				  var isDragging = $("#dragBar").attr("dragging");
				  if("1" == isDragging)
			      {
					  //Apply dragged height.
					  var top = $('#ghostBar').offset().top;
					  var top_percentage = $.Common.round(top / $(".wrapper").outerHeight() * 100, 1);
					  
					  $(".slip_wrapper").css("height",top_percentage+"%");
					  $(".attach_wrapper").css({"top":top_percentage+"%"});//)$(".wrapper").outerHeight() - $('#ghostBar').offset().top);
					  
					  $("#area_slip").getNiceScroll().resize();
					  $("#area_attach").getNiceScroll().resize();
					  
			          $('#ghostBar').remove();
			          $("#dragBarArea").remove();
			          $(document).unbind('mousemove');
					  $("#dragBar").attr("dragging","0");
					  $("#dragBar").css("top",top_percentage+"%");
			       }
			    });
		},
		getAttachList : function(params)
		{
			var objCntParams = {
					KEY : $.Common.get_ObjectValue(params.KEY),
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
			};
			
			$.when($.Common.RunCommand(g_ActorCommand, "GET_ATTACH_LIST", objListParams)).done(function(res) {
						$.Actor.attachTotalCnt = res.length;
						if($.Actor.attachTotalCnt > 0)
						{
							$.Actor.arObjAttachItem = res;
							$.Actor.addAttachItem(res,$("#area_attach"));
						}
			
			}).fail(function(res){
				alert("Failed to load attach.");
			}).always(function(){
				
				$.Common.HideProgress("#attach_progress");
				
			});
		},
		//Load thumb images
		getSlipList : function(params, startIdx)
		{
			
			var objCntParams = {
				KEY : params.KEY,
				KEY_TYPE : params.KEY_TYPE,
				USER_ID : params.USER_ID,
				CORP_NO : params.CORP_NO,
				LANG : params.LANG,
			};
			
			var objListParams = {
				KEY : params.KEY,
				KEY_TYPE : params.KEY_TYPE,
				USER_ID : params.USER_ID,
				CORP_NO : params.CORP_NO,
				LANG : params.LANG,
				START_IDX : startIdx,
				PER : this.slipRange
			};
		
			// $.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_COUNT", objCntParams),
			// 			$.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res1, res2) {
			// 	$.Actor.slipTotalCnt = parseInt(res1.THUMB_CNT);
			// 	if($.Actor.slipTotalCnt > 0)
			// 	{
			// 		$.Actor.arObjSlipItem = res2;
			// 		$.Actor.addSlipItem(res2, $("#slip_masonry"));
			// 		$.Actor.startIdx = ($.Actor.startIdx + $.Actor.slipRange) + 1;
			// 	}
			// 	else
			// 	{
			// 		$.Common.HideProgress("#slip_progress");
			// 	}
			//
			// })
			$.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objCntParams),
				$.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res1, res2) {
				$.Actor.slipTotalCnt = parseInt(res1.length);

				if($.Actor.slipTotalCnt > 0) {

					//if($.Actor.arObjSlipItem == null) {
					$.Actor.arObjSlipItem = res2;

					$.Actor.addSlipItem(res2, $("#slip_masonry"));
					$.Actor.startIdx = ($.Actor.startIdx + $.Actor.slipRange) + 1;

				}
				else {
					$.Common.HideProgress("#slip_progress");
				}


			})
			.fail(function(res1, res2){
				alert("Failed to load thumbs.");
				$.Common.HideProgress("#slip_progress");
			
			})
				.always(function(){
				
			});
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
			
			var arObjMenu = $.Common.sortContextMenuItem($.Actor.localeMsg, menuGroup, viewMode, $.Actor.currentKey);
			$.ContextMenu.getMenu($.Actor, elTarget, elBtn, arObjMenu, objData, option);
		},
	
		addAttachItem : function(arObjAttach,elDest, specificKey)
		{
			elDest.empty();
			
			$.each(arObjAttach,function(i){
				
				if(!$.Common.isBlank(specificKey) && specificKey.indexOf(",") == -1 && this.JDOC_NO != specificKey) return true;
				
				var elAttach = $.Actor.getAttachElement(this);
				
				var elTitleArea = elAttach.find(".area_btn");
				//Draw option icon
				$.Actor.addContextMenu(elTitleArea, $.Actor.contextMenu["Attach"], this, $.Actor.params.VIEW_MODE, {bottom_align:true});
				
				elAttach.appendTo(elDest);
				$.Actor.setAttachMouseEvent(elAttach);
		
			});
			
			
			$("#area_attach").getNiceScroll().resize();
			
		},
		addSlipItem : function(arObjSlip, elDest, specificKey)
		{
			
			//if(!$.Common.isBlank(specificKey)) {
			elDest.empty();
			elDest.masonry("layout");
			//}
			
			if(arObjSlip == null) return;
			//	var lastSDocNo = null;
			$.each(arObjSlip,function(i){
				
				if(!$.Common.isBlank(specificKey) && specificKey.indexOf(",") == -1 && this.JDOC_NO != specificKey) return true;
				
				var elThumb = $.Actor.getThumbElement(this, $.Actor);
				
				if(elThumb != null)
				{			
					var elTitleArea = elThumb.find(".area_title_btn");
				
					//Draw fold icon
					$.Actor.addFoldIcon($.Actor.arObjSlipItem, elTitleArea, elThumb.attr("idx"));
					
					//Draw option icon
					$.Actor.addContextMenu(elTitleArea, $.Actor.contextMenu["Thumb"], this, $.Actor.params.VIEW_MODE);
					
					elThumb.appendTo(elDest);
					elDest.masonry('appended', elThumb);
					elThumb.css("opacity","0");
					$.Actor.setThumbMouseEvent(elThumb.find(".area_thumb"));
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
						 $.Actor.fold($.Actor.arObjSlipItem, "btn_fold", $(this).attr("idx"));
					}
				});
				
				setTimeout(function(){
					$('#slip_masonry').masonry('reloadItems');
					
					$.Common.HideProgress("#slip_progress");
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
				}, 100);
			});
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
					$("[id=slip_item]").find("#chk").attr('checked', false);
					$("[id=attach_item]").css("background-color","");
					$("[id=attach_item]").find("#chk").attr('checked', false);
				}
				
				var cb = elThumb.closest("#slip_item").find("#chk")[0];
				//	cb.checked = true;
					cb.checked = !cb.checked;
					
					if(cb.checked) 
					{
						elThumb.closest("#slip_item").css("outline","2px solid #"+ $.Actor.colorSet.FOCUSED_SLIP);
						var version = $.Common.GetBrowserVersion().ActingVersion;
						if(version > 9) {
							
						}
					}
					else
					{
						elThumb.closest("#slip_item").css("outline","");
					}
			}
			
			//Double click event
			var evDblClick = function() {
				var targetThumb = elThumb.closest("#slip_item");

				var objData = $.Common.getObjByValue($.Actor.arObjSlipItem,"SLIP_IRN",targetThumb.attr("idx"))[0];
				$.Operation.execute($.Actor, targetThumb, objData);
			}
			elThumb.hover(function(e){
				$(this).find(".area_info").stop().fadeIn(300,function(){ $(this).addClass("show"); });
			},
			function(e) {
				$(this).find(".area_info").stop().fadeOut(300, function(){ $(this).removeClass("show"); });
			});


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
			            evDblClick();  //perform double-click action
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
					elAttach.css("background-color","#"+ $.Actor.colorSet.FOCUSED_ATTACH);
				}
				else
				{
					elAttach.css("background-color","");
				}
				
			});
			
		},
		getAttachElement : function(objData)
		{
			var idx 		= objData.SDOC_NO;
			//Draw attach outline
			var elAttach = $(document.createElement('div'));
			elAttach.addClass("attach_item");
			elAttach.attr("idx",idx);
			elAttach.attr("id","attach_item");
			elAttach.attr("command","VIEW_ORIGINAL_ATTACH");
			
			//Draw checkbox area
			var elTitleCheckbox = $(document.createElement('div'));
			elTitleCheckbox.addClass("area_cb");
			elTitleCheckbox.appendTo(elAttach);
			
			//Draw checkbox
			var elCheckbox =  $(document.createElement('label'));
			elCheckbox.addClass("cb_container");
			elCheckbox.addClass("attach_check");
			elCheckbox.append($(document.createElement('input')).attr({"id":"chk", "type":"checkbox"}));
			elCheckbox.append($(document.createElement('span')).addClass("checkbox"));
			elCheckbox.appendTo(elTitleCheckbox);

			//Draw attach Title area
			var elAttachTitleArea = $(document.createElement('div'));
			elAttachTitleArea.addClass("area_title");
			elAttachTitleArea.css("background-color","rgb("+objData.SDOC_COLOR+")");
			elAttachTitleArea.appendTo(elAttach);

			var titleName = objData.SDOC_NAME;
			//if("AFTER" == $.Actor.params.VIEW_MODE) {
				
				if("1" == objData.SDOC_AFTER) {
					titleName = "★) "+objData.SDOC_NAME;
				}
			//}
			
			//Draw thumb title
			var elAttachTitle = $(document.createElement('div'));
			elAttachTitle.addClass("attach_item_title");
			elAttachTitle.css("color","#"+$.Actor.colorSet.FONT_COLOR );
			elAttachTitle.append($(document.createElement('span')).html(titleName).unbind("click").bind("click",function(e){e.stopPropagation(); $.Operation.execute($.Actor, elAttach, objData); }));
			elAttachTitle.appendTo(elAttachTitleArea);

			
			
			//Draw attach Type area
			var elAttachTypeArea = $(document.createElement('div'));
			elAttachTypeArea.addClass("area_type");
			elAttachTypeArea.appendTo(elAttach);
			
			//Draw attach Type
			var elAttachType = $(document.createElement('div'));
			elAttachType.addClass("attach_item_type");
			elAttachType.html(objData.SDOC_KINDNM);
			elAttachType.css({
				"color":"#"+$.Actor.colorSet.ATTACH_TYPE_FONT,
				"background-color":"#"+$.Actor.colorSet.ATTACH_TYPE
			});
			elAttachType.appendTo(elAttachTypeArea);
			
			
			
			//Draw attach Type area
			var elAttachBtnArea = $(document.createElement('div'));
			elAttachBtnArea.addClass("area_btn");
			elAttachBtnArea.attr("type","ATTACH");
			elAttachBtnArea.appendTo(elAttach);
			
			var elDownBtn = $(document.createElement('div'));
			elDownBtn.addClass("down_attach");
			elDownBtn.attr("command","DOWN_ORIGINAL_ATTACH")
			elDownBtn.append($(document.createElement('img')).attr("src", g_RootURL+"image/common/context/download_cs.png"));
			elDownBtn.unbind("click").bind("click",function(e){ e.stopPropagation(); $.Operation.execute($.Actor, elDownBtn, objData); });
			elDownBtn.appendTo(elAttachBtnArea);
			
//			var elAttachOption = $(document.createElement('div'));
//			elAttachOption.addClass("option");
//			elAttachOption.append($(document.createElement('img')).attr("src", g_RootURL+"image/common/option.png"));
//			elAttachOption.appendTo(elAttachBtnArea);
	
			return elAttach;
		},
		
		getThumbElement : function(objData, callerObjData)
		{
			//Draw thumb outline
			var idx 		= objData.SLIP_IRN;
			var group 	= objData.SDOC_NO;
			
			var elThumb = $(document.createElement('div'));
			elThumb.addClass("slip_item");
			elThumb.attr("idx", idx);
			elThumb.attr("group", group);
			elThumb.css({
				"width" : callerObjData.thumbWidth
			});
			elThumb.attr("id","slip_item");
			elThumb.attr("command","VIEW_ORIGINAL_SLIP");
			if("1" == objData.SDOC_ONE)
			{
				elThumb.addClass("oneSlip");
			}

			elThumb.attr("follow",objData.SDOC_FOLLOW);
			
			
			//Draw thumb Title area
			var elThumbTitleArea = $(document.createElement('div'));
			elThumbTitleArea.addClass("area_title");
			//elThumbTitleArea.css("background-color","rgb("+objData.KIND_COLOR+")");
			elThumbTitleArea.appendTo(elThumb);
			
		//	if("AFTER" == callerObjData.params.VIEW_MODE) {
				
				if("1" == objData.SDOC_AFTER) {
					elThumbTitleArea.addClass("after");	
				}
		//	}
			
			
			//Draw checkbox area
			var elTitleCheckbox = $(document.createElement('div'));
			elTitleCheckbox.addClass("area_cb");
			elTitleCheckbox.appendTo(elThumbTitleArea);
			
			

			//Draw checkbox
			var elCheckbox =  $(document.createElement('label'));
			elCheckbox.addClass("cb_container");
			elCheckbox.addClass("slip_check");
			elCheckbox.append($(document.createElement('input')).attr({"id":"chk","type":"checkbox"}));
			elCheckbox.append($(document.createElement('span')).addClass("checkbox"));
			elCheckbox.appendTo(elTitleCheckbox);

			//Draw option btn area 
			var elTypeColor = $(document.createElement('div'));
			elTypeColor.attr("id","type_color");
			elTypeColor.css("background-color","rgb("+objData.KIND_COLOR+")");
			elTypeColor.appendTo(elThumbTitleArea);

			
			//Draw thumb title
			var elThumbTitle = $(document.createElement('div'));
			elThumbTitle.addClass("thumb_title");
			elThumbTitle.append($(document.createElement('span')).html(objData.SDOC_KINDNM));
			elThumbTitle.appendTo(elThumbTitleArea);
		
			
			if("1" == objData.SDOCNO_INDEX)
			{
				var elThumbBtnArea = $(document.createElement('div'));
				elThumbBtnArea.addClass("area_title_btn");
				elThumbBtnArea.attr("type","SLIP");
				elThumbBtnArea.appendTo(elThumbTitleArea);
				
				elThumb.attr("first","1");
				elThumb.attr("fold","1");
			}
			else
			{
				elThumb.css("display","none");
			}
			
			
			
			//Draw image area
			var elThumbImgArea = $(document.createElement('div'));
			elThumbImgArea.css("position","relative");
			elThumbImgArea.attr("class","area_thumb");
			elThumbImgArea.appendTo(elThumb);
			
			//Add link
			var vElImageHref = $(document.createElement('a'));
			vElImageHref.addClass("link");
			vElImageHref.appendTo(elThumbImgArea);
			
			//Add image
			var sbImgURL = new StringBuffer();
			sbImgURL.append(this.rootURL);
			sbImgURL.append("DownloadImage.do?");
			sbImgURL.append("ImgType=thumb");
			sbImgURL.append("&DocIRN="+objData.DOC_IRN);
			sbImgURL.append("&Idx="+objData.DOC_NO);
			sbImgURL.append("&degree="+objData.SLIP_ROTATE);
			sbImgURL.append("&UserID="+callerObjData.params.USER_ID);
			sbImgURL.append("&CorpNo="+callerObjData.params.CORP_NO);
			sbImgURL.append('?'+Math.random());
		
			var vElImage = $(document.createElement('img'));
			vElImage.attr("src",sbImgURL);
			vElImage.attr("class","thumb_img");

			$(vElImageHref).append(vElImage);
			
			
			
			var sbSlipInfo = new StringBuffer();
			sbSlipInfo.append(objData.SDOC_NAME);
			sbSlipInfo.append("<br/>"+objData.REG_USERNM);
			
			//Draw thumb Info area
			var elThumbInfoArea = $(document.createElement('div'));
			elThumbInfoArea.addClass("area_info");
			
			elThumbInfoArea.html(sbSlipInfo.toString());
			elThumbInfoArea.appendTo(elThumbImgArea);
			
			//Draw thumb effect area
			var elThumbEffectArea = $(document.createElement('div'));
			elThumbEffectArea.addClass("area_effect");
			elThumbEffectArea.appendTo(elThumbImgArea);
		
			//Add image
			
			return elThumb;
		},
		addFoldIcon : function(itemList, elTarget, idx) {
			
			if(elTarget.closest("#slip_item").attr("first") != "1") return;
			
			var objData = $.Common.getObjByValue(itemList,"SLIP_IRN",idx)[0];
			if(/*"1" == objData.SDOCNO_INDEX &&*/ parseInt(objData.SDOCNO_CNT) > 1)
			{
				var elBtn = $(document.createElement('div'));
				elBtn.attr("id","btn_fold");
				elBtn.unbind().bind("click", function(){ $.Actor.fold(itemList, "btn_fold", idx) });
				elBtn.appendTo(elTarget);
				
				var iconURL = g_RootURL+"image/common/fold.png";
				
//				if("0" == elTarget.closest("#slip_item").attr("fold")) {
//					 iconURL = g_RootURL+"image/common/fold.png";
//				}
				
				var elImg = $(document.createElement('img')).attr("src",iconURL);
				elImg.unbind('mouseenter mouseleave').hover(function(){
					$(this).css("opacity","0.7");
				},function(){
					$(this).css("opacity","1");
				});
				
				elImg.appendTo(elBtn);
			}
		},
//		drawThumbOptionBtn: function(elThumb, objData, attrVal, imgURL, clickEvent) {
//			//Draw fold/unfold button
//			var elBtn = $(document.createElement('div'));
//				elBtn.attr(attrVal);
//				elBtn.unbind().bind("click",clickEvent);
//				elBtn.appendTo(elThumb);
//				
//				var elImg = $(document.createElement('img')).attr("src",imgURL + ".png");
//				elImg.unbind('mouseenter mouseleave').hover(function(){
//					$(this).css("opacity","0.7");
//				},function(){
//					$(this).css("opacity","1");
//				});
//				
//				elImg.appendTo(elBtn);
//			
//		},
		fold:function(listData, strID, key){
		
			var objData = $.Common.getObjByValue(listData,"SLIP_IRN",key)[0];
			var elThumb = $("[idx="+key+"]");
			
			var isFold = elThumb.attr("fold");
			var foldIcon = null;
			if("1" == isFold)
			{
				foldIcon = g_RootURL+"image/common/unfold.png";
				elThumb.attr("fold", "0");
			}
			else
			{
				foldIcon = g_RootURL+"image/common/fold.png";
				elThumb.attr("fold", "1");
			}
			elThumb.find("#btn_fold").find("img").attr("src", foldIcon);
			
			var sdocNo = objData.SDOC_NO;
			
			$.each(listData, function() {
				
				if(this.SDOC_NO == sdocNo) {
					
					var idx = this.SLIP_IRN;
					var elTargetThumb =  $("[idx="+idx+"]");
					// Pass first thumb
					if(elTargetThumb.attr("first") != null) {
						return true;
					}
					
					if("1" == isFold) {
						elTargetThumb.css("display","none");
					}
					else
					{
						elTargetThumb.css("display","block");
					}
				}
			});
			
			$('#slip_masonry').masonry('layout');
			$("#area_slip").getNiceScroll().resize();

		},
		setUIColor : function()
		{
			var objColorActor =  $.extend($.Color.PC.Actor, $.Color.Common);
			if(objColorActor != null)
			{
				
				$(".actor_left").css("background","#"+objColorActor.NAVIGATION);
				$(".slip_title").css('border-top-color',"#"+objColorActor.NAVIGATION);

				$("#dragbar").css('background',"#"+objColorActor.NAVIGATION);
				$("#area_slip").niceScroll({horizrailenabled: false, cursorcolor:"#"+objColorActor.NAVIGATION});
				$("#area_attach").niceScroll({horizrailenabled: false, cursorcolor:"#"+objColorActor.NAVIGATION});
				$("#key").niceScroll({horizrailenabled: true, cursorcolor:"#"+objColorActor.NAVIGATION});
				$.Actor.colorSet = objColorActor;
				
			}
		},
		/*setXPILoadingEvent : function(element) {
			element.prop("onclick", null);
		},*/
		//왼쪽 버튼 메뉴 설정
		initButtons : function()
		{
			$.Common.Draw_LeftMenu($.Actor);
			
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
		Adjust_Size : function() {
			var top_percentage = 0;
			if($.Actor.original_scrollHeight == -1) {
				$.Actor.original_scrollHeight = $(".slip_wrapper").height();

				var titleHeight = $(".attach_title").outerHeight();
				var scrollHeight = $(".wrapper").outerHeight() - titleHeight;

				top_percentage = $.Common.round(scrollHeight / $(".wrapper").outerHeight() * 100, 1);

				
			//	$(".attach_wrapper").css({"top":scrollHeight, "height": titleHeight});
			}
			else
			{
				top_percentage = $.Common.round($.Actor.original_scrollHeight / $(".wrapper").outerHeight() * 100, 1);

				// $(".slip_wrapper").css("height",top_percentage+"%");
				// $(".attach_wrapper").css({"top":top_percentage+"%"});
			//	$(".attach_wrapper").css({"top":$.Actor.original_scrollHeight, "height": $(".wrapper").outerHeight() - $.Actor.original_scrollHeight});
				$.Actor.original_scrollHeight = -1;
			}

			$(".slip_wrapper").css("height", top_percentage+"%");
			$(".attach_wrapper").css({"top":top_percentage+"%"});
			$("#dragBar").css("top",top_percentage+"%");

			$("#area_slip").getNiceScroll().resize();
			$("#area_attach").getNiceScroll().resize();
		},
		maximize : function() {
			var titleHeight = $(".slip_title").outerHeight();
			$(".slip_wrapper").css("height", titleHeight);
			$(".attach_wrapper").css("height",$(".wrapper").outerHeight() - titleHeight);
        	
			$("#area_slip").getNiceScroll().resize();
			$("#area_attach").getNiceScroll().resize();
		},
		minimize : function() {
			var titleHeight = $(".attach_title").outerHeight();
			$(".slip_wrapper").css("height", $(".wrapper").outerHeight() - titleHeight);
			$(".attach_wrapper").css("height", titleHeight);
			
			$("#area_slip").getNiceScroll().resize();
			$("#area_attach").getNiceScroll().resize();
		},
		attachDownloadLink : function() {
			
				$(".actor_left > div").each(function(i) {
				
				$(this).unbind("click");
				$(this).on({
	        	    click: function() {
	        	    	var isCSOperation = $(this).attr("cs_operation");
	        	    	
	        	    	if("1" === isCSOperation)
        	    		{
        	    			$.Common.simpleConfirm($.Actor.localeMsg.Confirm,$.Actor.localeMsg.CONFIRM_DOWNLOAD, function(){
        	    				window.open("<c:url value='/down' />","_blank");
        	    			});
        	    		}
	        	    }
	        	});
			});
			
		},
		
		
		/*//Parse context menu.
		parseContextMenu : function(menuItem) {
			
			var arObjMenu = [];
			
			$.each(menuItem, function(){
				
				if($.Actor.params.VIEW_MODE.toUpperCase() == "VIEW")
				{
					if((this.MODE != null ) && this.MODE.toUpperCase() != "VIEW")
					{
						return true;
					}
				}
					
				if(!$.Common.isBlank(this.ID))
				{
					var menuID = this.ID;
					if(!$.Common.isBlank(this.ICON))
					{
						this["icon"] = g_RootURL + this.ICON;
					}
					this["title"] = $.Actor.localeMsg[menuID];
					this["click"] = function(){$.Operation.execute(menuID)}; //bind event
				}
				
				arObjMenu.push(this);
				
			});
			
			return arObjMenu;
		},*/
		
		/*//Get menu item
		openContextMenu : function(el, menuID, conetoption) {
		
			if($.Actor.contextMenu == null) return;
			
			var defOption = {leftClick : true};
			defOption = $.extend(defOption, option);
			
			var menuItem = $.Actor.contextMenu[menuID.toUpperCase()];
			
			var alMenuItem = $.Actor.parseContextMenu(menuItem);
			
			if(alMenuItem != null && alMenuItem.length > 0)
			{
				$.ContextMenu.create(el, alMenuItem, defOption);
			}
		},*/
		redrawThumb : function() {
			
			var beforeGroup = null;
			//Redraw thumb buttons
			$.each($("[id=slip_item]:visible"), function(){

				var key = $(this).attr("idx");
				
				var objData = $.Common.getObjByValue($.Actor.arObjSlipItem,"SLIP_IRN",key)[0];
				var sdocNo = objData.SDOC_NO;

				if(beforeGroup == sdocNo)
				{
					return true;
				}
				else
				{
					beforeGroup = sdocNo;
					var elThumbGroup = $("[group="+sdocNo+"]");
					var elFirstThumb = elThumbGroup.eq(0);
					var isFirst = elFirstThumb.attr("first");
					if(isFirst == "1") {
						return true;
					}
					else
					{
						var elThumbBtnArea = $(document.createElement('div'));
						elThumbBtnArea.addClass("area_title_btn");
						elThumbBtnArea.attr("type","SLIP");
						elThumbBtnArea.appendTo(elFirstThumb);
						
						elFirstThumb.attr("first","1");
						elFirstThumb.attr("fold","1");
						
						//Draw fold icon
						$.Actor.addFoldIcon($.Actor.arObjSlipItem, elThumbBtnArea, key);
						
						//Draw option icon
						$.Actor.addContextMenu(elThumbBtnArea, $.Actor.contextMenu["Thumb"], objData, $.Actor.params.VIEW_MODE);
					}
				}
			});
			
		},
	
		removeSlipElement : function(elTarget) {
			
			$.each(elTarget,function(){
				$(this).closest("#slip_item").remove();
			})
			$('#slip_masonry').masonry('layout');
			
			$.Actor.redrawThumb();
			
			$("#area_slip").getNiceScroll().resize();
		},
		removeAttachElement : function(elTarget) {
			
			$.each(elTarget,function(){
				$(this).closest("#attach_item").fadeOut(200, function() { $(this).remove(); });
			})
			
			$("#area_attach").getNiceScroll().resize();
		},
		Reload_Thumb : function(target, elThumb, objData) {
			
//			
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
			
		},
		reset: function() {
			$("#slip_masonry").empty();
			$("#area_attach").empty();
			

			$(".key_select option").eq(0).prop("selected",true);
			
			$.Actor.slipTotalCnt = 0;
			$.Actor.attachTotalCnt = 0;
			$.Actor.startIdx = 0;
		}
	
}