$.Mobile = {
	localeMsg : null,
	colorSet : null,
	params : null,
	startIdx : 0,
	slipRange : 100,
	thumbWidth : 160,
	arObjSlipItem: null,
	arObjAttachItem : null,
	contextMenu : null,
	currentKey : null,
	m_Viewer : null,
	m_ViewerItem : null,
	init : function(params) {
		
		
		//Set Mobile default enviroments.
		$.Mobile.params = params;

		//Set globalization.
		$.Mobile.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"Mobile");
		//Set UI set
		$.Mobile.setUIColor();

		//Start finger slide
		$('nav').slideAndSwipe();

		$.Mobile.Init_Navi();

		$.Mobile.Set_ButtonEffect();

		$("#show-slip").click();


	},
	Init_Navi : function() {

		// $("#navi-close").on("click",function(){
		// 	  $('.ssm-toggle-nav').trigger("click");
		// });
		$("#nav-user-info").html($.Mobile.params.USER_NM + "(" +$.Mobile.params.USER_ID+")");
		$("#nav-part-info").html($.Mobile.params.PART_NM);

		if($.Common.params.MULTI_KEY && $.Common.isBlank($.Mobile.currentKey)) {
			$(".navi-menu").hide();
		}
		else {
			$(".navi-menu").show();
		}
	},
	Open_Viewer : function(index) {

		$(".pswp__bg").css("background","#DFE1E5");
		$(".pswp__top-bar").css("background","#"+$.Mobile.colorSet.TOP_NAVIGATION);
		    var swipe_Options = {
		    		index :index,
		    		bgOpacity : 1,
		    		tapToToggleControls:false,
		    		showInfoEl:true,
					loop:false,
					shareEl:false,
		    	//	getDoubleTapZoom:true,
		    		isClickableElement: function(el) {
		    		    return el.tagName === 'SELECT';
		    		},
	    		    getThumbBoundsFn: function(index) {

	    		    //	var nSlipIdx = $.Actor.m_GalleryItem[index].SlipIdx;//.map(function(e) { return e.SlipIdx; }).indexOf(index);
	    		    	
	    		    	// find thumbnail element
		    		    var thumbnail = $(".slip_item[thumb-idx='"+index+"']")[0];
	
		    		    // get window scroll Y
		    		    var pageYScroll = window.pageYOffset || document.documentElement.scrollTop; 
		    		    // optionally get horizontal scroll
	
		    		    // get position of element relative to viewport
		    		    var rect = thumbnail.getBoundingClientRect(); 
	
		    		    // w = width
		    		    return {x:rect.left, y:rect.top + pageYScroll + 40, w:rect.width};

		    		}, 
		    		history:false
		    };
		    
			this.m_Viewer 					= new PhotoSwipe($('.pswp')[0], PhotoSwipeUI_Default, $.Mobile.m_ViewerItem , swipe_Options);
		  	
			// this.m_Gallery.listen('beforeChange', function() {
			// 	$(".area_info").fadeOut(222);
			// 	var nCurIdx = this.getCurrentIndex();
			// 	if($.Actor.m_vJSONResImgData.length > 0 && nCurIdx == ($.Actor.m_vThumbLastIdx - 1))
			// 	{
			// 		$.Actor.addSlipItem($('.grid'));
			// 	}
			// 	$.Actor.SetViewTitle();
			// 	$("#btn_set_temp").unbind().bind("tap",function(){
			// 		 $.Actor.SetTemp();	
			// 	});
			// });
			
			
			// this.m_Gallery.listen('afterChange',function(){
				
			// 	var nGalleryIdx			= $.Actor.m_Gallery.getCurrentIndex();
			// 	var nSlipIdx 				= $.Actor.m_GalleryItem[nGalleryIdx].SlipIdx;
			// 	var arCurSlipInfo			= $.Actor.m_vJSONResImgData[nSlipIdx];
		
			// 	$.Actor.SetSlipTitle(arCurSlipInfo); 
				
			// /* 	if("1" == arCurSlipInfo.imageInfo.IsOne)
			// 	{
			// 		$.Actor.SetOneSlipList(arCurSlipInfo.imageInfo);
			// 	} */
			// });
			
			// this.m_Gallery.listen("close",function(){
			// 	$(".area_info").hide();
				
			// /* 	var arItems 	= $.Actor.m_Gallery.items;
			//  	for(var i = 0 ; i < arItems.length; i++)
			// 	{
			// 		var item = arItems[i];
			// 		if(item != null)
			// 		{
			// 			item.src 			= $.Actor.GetImageSrc("original",$.Actor.m_vJSONResImgData[i].imageInfo);
			// 			item.imageIdx 	= 1;
			// 		}
			// 	}  */
			// });
			// this.m_Gallery.listen("close",function(){
				
			// });
		//	
			this.m_Viewer.init();
	},
	change_GroupKey : function(option) {
		var key = option.value;
		
		if($.Common.isBlank(key)) key = null;//$.Mobile.params.KEY;
		
		$.Mobile.currentKey = key;
		
		if($.Mobile.arObjSlipItem != null) {
			$.Mobile.addSlipItem($.Mobile.arObjSlipItem, $("#slip_masonry"), key);
		}
		if($.Mobile.arObjAttachItem != null) {
			$.Mobile.addAttachItem($.Mobile.arObjAttachItem, $("#area-attach"), key);
		}

		$.Mobile.Init_Navi();
		
	},

	Set_ButtonEffect : function() {

		$("#show-slip").ripple({
					maxDiameter: "200%"
		});
		$("#show-attach").ripple({
					maxDiameter: "200%"
		});
		$("#btn-menu").ripple({
					maxDiameter: "200%"
		});
		// $("#navi-close").ripple({
		// 			maxDiameter: "200%",
		// });

		// $("#navi-open-comment").ripple({
		// 			maxDiameter: "200%",
		// });

		// $("#navi-open-history").ripple({
		// 			maxDiameter: "200%",
		// });
		

		
	},
	Close_frame : function()
		{
			$('.area-layout').fadeOut(333, function(){
				$(this).remove();
			});
		},

	Close_Navi : function() {
		$(".ssm-overlay").hide();
		$("nav").css('transform', 'translate(-280px,0)');
		$("nav").removeClass("ssm-nav-visible");
		
	},
	Open_Comment : function() {

			$.Mobile.Close_Navi();
			var el_layout = $(document.createElement('div'));
			el_layout.addClass("area-layout");
			el_layout.on("click",function() {
				$.Mobile.Close_frame();
			});
			el_layout.appendTo("body");

			$(".area-frame").remove();
			var el_areaFrame = $(document.createElement('div'));
			el_areaFrame.addClass("area-frame");
			el_areaFrame.appendTo(el_layout);

			var el_close = $(document.createElement('div'));
			el_close.append($(document.createElement('img')).attr("src",g_RootURL+"/image/common/x.png"));
			el_close.addClass("close-frame");
			el_close.on("click", function(){
				$.Mobile.Close_frame();
			});
			el_close.appendTo(el_areaFrame);

			var el_areaLoading = $(document.createElement('div'));
			el_areaLoading.attr("id","progress-mobile-comment");
			el_areaLoading.appendTo(el_areaFrame);
			
			$.Common.ShowProgress("#progress-mobile-comment","Waiting..","000000","0.7");

			var el_containerFrame = $(document.createElement('div'));
			el_containerFrame.addClass("container-frame");
			el_containerFrame.appendTo(el_areaFrame);

			var el_frame = $(document.createElement('iframe'));
			el_frame.addClass("frame-main");
			el_frame.appendTo(el_containerFrame);

			var url = new StringBuffer();
			url.append(g_RootURL + "/slip_comment.jsp?");
			url.append("KEY="+$.Mobile.params.KEY);
			url.append("&LANG="+$.Mobile.params.LANG);
			url.append("&USER_ID="+$.Mobile.params.USER_ID);
			url.append("&CORP_NO="+$.Mobile.params.CORP_NO);
		
			el_frame.attr("src",url.toString());


			el_frame.load(function(){
				$.Common.HideProgress("#progress-mobile-comment");
			});
	},
	Open_History : function() {

			$.Mobile.Close_Navi();
			var el_layout = $(document.createElement('div'));
			el_layout.addClass("area-layout");
			el_layout.on("click",function() {
				$.Mobile.Close_frame();
			});
			el_layout.appendTo("body");

			$(".area-frame").remove();
			var el_areaFrame = $(document.createElement('div'));
			el_areaFrame.addClass("area-frame");
			el_areaFrame.appendTo(el_layout);

			var el_close = $(document.createElement('div'));
			el_close.append($(document.createElement('img')).attr("src",g_RootURL+"/image/common/x.png"));
			el_close.addClass("close-frame");
			el_close.on("click", function(){
				$.Mobile.Close_frame();
			});
			el_close.appendTo(el_areaFrame);

			var el_areaLoading = $(document.createElement('div'));
			el_areaLoading.attr("id","progress-mobile-history");
			el_areaLoading.appendTo(el_areaFrame);
			
			$.Common.ShowProgress("#progress-mobile-history","Waiting..","000000","0.7");

			var el_containerFrame = $(document.createElement('div'));
			el_containerFrame.addClass("container-frame");
			el_containerFrame.appendTo(el_areaFrame);

			var el_frame = $(document.createElement('iframe'));
			el_frame.addClass("frame-main");
			el_frame.appendTo(el_containerFrame);

			var url = new StringBuffer();
			url.append(g_RootURL + "/slip_history.jsp?");
			url.append("KEY="+$.Mobile.params.KEY);
			url.append("&LANG="+$.Mobile.params.LANG);
			url.append("&USER_ID="+$.Mobile.params.USER_ID);
			url.append("&CORP_NO="+$.Mobile.params.CORP_NO);
		
			el_frame.attr("src",url.toString());


			el_frame.load(function(){
				$.Common.HideProgress("#progress-mobile-history");
			});
	},
	setUIColor : function()
	{
		var colorSet =  $.extend($.Color.Mobile.Viewer, $.Color.Common);
		if(colorSet != null)
		{
			
			$(".top-navi").css("background","#"+colorSet.TOP_NAVIGATION);
			$(".top-navi").css("color","#"+colorSet.TOP_NAVIGATION_FONT);
			$(".area-title").css("border","1px solid #" + colorSet.KEY_BORDER);
			$(".key-title").css("background","#"+colorSet.KEY_TITLE_BG);
			$(".key-title").css("color","#"+colorSet.KEY_TITLE_FONT);
			$(".key-content > span").css("color","#"+colorSet.KEY_CONTENT_FONT);
			$(".navi-wrapper").css("border-top","10px solid #"+colorSet.TOP_NAVIGATION);
			$(".navi-bar").css("background-color","#"+colorSet.TOP_NAVIGATION);
			$.Mobile.colorSet = colorSet;
			
		}
	},
	Draw_SlipList : function(params, startIdx) {

		//Get Mobile menu list
		$.Mobile.contextMenu = $.Menu.Mobile.Common;

		$('#slip_masonry').masonry({
			// options
			itemSelector: '#slip_item',
			columnWidth: $.Mobile.thumbWidth,
			horizontalOrder: true,
			isFitWidth: true,
			gutter: 30
		});

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
				START_IDX : startIdx,
				PER : this.slipRange
		};
	
		// $.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_COUNT", objCntParams),
		// 			$.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res1, res2) {
		// 	$.Mobile.slipTotalCnt = parseInt(res1.THUMB_CNT);
		// 	if($.Mobile.slipTotalCnt > 0)
		// 	{
		//
		// 		$.Mobile.m_ViewerItem = new Array($.Mobile.slipTotalCnt);
		// 		for(var i = 0; i < $.Mobile.m_ViewerItem.length; i++)
		// 		{
		// 			$.Mobile.m_ViewerItem[i] = {};
		// 		}
		//
		//
		//
		// 		$.Mobile.arObjSlipItem = res2;
		// 		$.Mobile.addSlipItem(res2, $("#slip_masonry"));
		// 		$.Mobile.startIdx = ($.Mobile.startIdx + $.Mobile.slipRange) + 1;
		//
		//
		// 	}
		// 	else
		// 	{
		// 		$.Common.HideProgress("#slip_progress");
		// 	}
		//
		// })
		$.when($.Common.RunCommand(g_ActorCommand, "GET_THUMB_LIST", objListParams)).done(function(res) {
			if(res != null && res.length > 0) {
				var thumbCnt = 0;
				$.each(res, function(){
					if("0" === this.DOC_NO) {
						thumbCnt++;
					}
				});
				$.Mobile.slipTotalCnt = thumbCnt;
				$.Mobile.m_ViewerItem = new Array($.Mobile.slipTotalCnt);

				for(var i = 0; i < $.Mobile.m_ViewerItem.length; i++)
				{
					$.Mobile.m_ViewerItem[i] = {};
				}



				$.Mobile.arObjSlipItem = res;
				$.Mobile.addSlipItem(res, $("#slip_masonry"));
				$.Mobile.startIdx = ($.Mobile.startIdx + $.Mobile.slipRange) + 1;

			}
			else {
				$.Common.HideProgress("#slip_progress");
			}


		})
		.fail(function(res){
		alert("Failed to load thumbs.");
		$.Common.HideProgress("#slip_progress");

		}).always(function(){
			
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
		
		var arObjMenu = $.Common.sortContextMenuItem($.Mobile.localeMsg, menuGroup, viewMode, $.Mobile.currentKey);
		$.ContextMenu.getMenu($.Mobile, elTarget, elBtn, arObjMenu, objData, option);
	},
	addSlipItem : function(ar_slipData, el_Dest, specificKey)
	{
		
		//if(!$.Common.isBlank(specificKey)) {
		el_Dest.empty();
		el_Dest.masonry("layout");
		//}
		//	var lastSDocNo = null;
		$.each(ar_slipData,function(i){
			
			if(!$.Common.isBlank(specificKey) && this.JDOC_NO != specificKey) return true;
			
			var elThumb = $.Mobile.getThumbElement(this, $.Mobile, i);
			
			if(elThumb != null)
			{			
				var elTitleArea = elThumb.find(".area_title_btn");
			
				//Draw fold icon
				$.Mobile.addFoldIcon($.Mobile.arObjSlipItem, elTitleArea, elThumb.attr("idx"));
				
				//Draw option icon
				$.Mobile.addContextMenu(elTitleArea, $.Mobile.contextMenu["Thumb"], this, $.Mobile.params.VIEW_MODE);
				
				elThumb.appendTo(el_Dest);
				el_Dest.masonry('appended', elThumb);
				elThumb.css("opacity","0");
			//	$.Mobile.setThumbMouseEvent(elThumb.find(".area_thumb"));

				$.Mobile.Set_ImageOriginal(i, this);
			}
		});

		
		el_Dest.imagesLoaded(function(){
			
			$.each(el_Dest.children('[id="slip_item"]'), function(){
				$(this).css("opacity","1");
			});
			el_Dest.masonry('layout');
			
			//Finish UI Setting
			$.each( $("#slip_masonry").find('[id="slip_item"]'), function(){
				if($(this).attr("first") == "1")
				{
						$.Mobile.fold($.Mobile.arObjSlipItem, "btn_fold", $(this).attr("idx"));
				}
			});
			$('#slip_masonry').masonry('reloadItems');
			
			$.Common.HideProgress("#slip_progress");
		
			//Add ripple effect.
			el_Dest.find('[class="area_effect"]').ripple({
					maxDiameter: "200%"
			});
	
		});
	},
	Set_ImageOriginal : function(index, objItem) {

		var sbThumbURL = new StringBuffer();
		sbThumbURL.append(this.rootURL);
		sbThumbURL.append("DownloadImage.do?");
		sbThumbURL.append("ImgType=thumb");
		sbThumbURL.append("&DocIRN="+objItem.THUMB_DOC_IRN);
		sbThumbURL.append("&Idx="+objItem.DOC_NO);
		sbThumbURL.append("&UserID="+$.Mobile.params.USER_ID);
		sbThumbURL.append("&CorpNo="+$.Mobile.params.CORP_NO);

		var sbImageURL = new StringBuffer();
		sbImageURL.append(this.rootURL);
		sbImageURL.append("DownloadImage.do?");
		sbImageURL.append("&DocIRN="+objItem.THUMB_DOC_IRN);
		sbImageURL.append("&Idx="+objItem.DOC_NO);
		sbImageURL.append("&UserID="+$.Mobile.params.USER_ID);
		sbImageURL.append("&CorpNo="+$.Mobile.params.CORP_NO);


		var elTitleArea = $(document.createElement('div'));
		elTitleArea.addClass("original-view-caption");

		var elTitle = $(document.createElement('span'));
		elTitle.addClass("caption-title");
		elTitle.html(objItem.SDOC_NAME);
		elTitle.appendTo(elTitleArea);

		var elNewLine = $(document.createElement('br'));
		elNewLine.appendTo(elTitleArea);

		var elSlipType = $(document.createElement('span'));
		elSlipType.addClass("caption-slip-type");
		elSlipType.html(objItem.SDOC_KINDNM);
		elSlipType.appendTo(elTitleArea);


		$.Mobile.m_ViewerItem[index] = {
			src: sbImageURL.toString(), 
			msrc:sbThumbURL.toString(),
			w:0,
			h:0,
			 title:elTitleArea.prop("outerHTML"),
			//  SlipIdx:nRealIdx
		};

		$.Mobile.Set_ImageDimension(sbImageURL.toString(), index);
						
	},

	Set_ImageDimension : function(imgURL, index)
		{
			var img = new Image();
			img.src = imgURL;

			$(img).load(function() {
				$.Mobile.m_ViewerItem[index].w = img.naturalWidth;
				$.Mobile.m_ViewerItem[index].h = img.naturalHeight;
				// sets a flag that slides should be updated
			//	$.Mobile.m_Viewer.invalidateCurrItems();
				// updates the content of slides
			//	$.Mobile.m_Viewer.updateSize(true);

				//$(this) = null;
			});
		

		},

	getThumbElement : function(objData, callerObjData, thumbIndex)
		{
			//Draw thumb outline
			var idx 		= objData.SLIP_IRN;
			var group 	= objData.SDOC_NO;
			
			var elThumb = $(document.createElement('div'));
			elThumb.addClass("slip_item");
			elThumb.attr("idx", idx);
			elThumb.attr("group", group);
			elThumb.attr("thumb-idx", thumbIndex);
			elThumb.css({
				"width" : callerObjData.thumbWidth
			});
			elThumb.attr("id","slip_item");
			elThumb.attr("command","VIEW_ORIGINAL_SLIP");
			if("1" == objData.SDOC_ONE)
			{
				elThumb.addClass("oneSlip");
			}
	
			//Draw thumb Title area
			var elThumbTitleArea = $(document.createElement('div'));
			elThumbTitleArea.addClass("area_title");
			// elThumbTitleArea.css("background-color","rgb("+objData.KIND_COLOR+")");
			elThumbTitleArea.appendTo(elThumb);
			
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
			sbImgURL.append("&DocIRN="+objData.THUMB_DOC_IRN);
			sbImgURL.append("&Idx="+objData.DOC_NO);
			sbImgURL.append("&UserID="+callerObjData.params.USER_ID);
			sbImgURL.append("&CorpNo="+callerObjData.params.CORP_NO);
		
			var vElImage = $(document.createElement('img'));
			vElImage.attr("src",sbImgURL);
			vElImage.attr("class","thumb_img");
			$(vElImageHref).append(vElImage);
			
			
			var sbSlipInfo = new StringBuffer();
			sbSlipInfo.append("<span class='slip-title'>" + objData.SDOC_NAME + "</span>");
			sbSlipInfo.append("<br/><span class='slip-reg-user'>"+objData.REG_USERNM + "</span>");
			
			//Draw thumb Info area
			var elThumbInfoArea = $(document.createElement('div'));
			elThumbInfoArea.addClass("area_info");
			
			elThumbInfoArea.html(sbSlipInfo.toString());
			elThumbInfoArea.appendTo(elThumbImgArea);
			
			//Draw thumb effect area
			var elThumbEffectArea = $(document.createElement('div'));
			elThumbEffectArea.addClass("area_effect");
			elThumbEffectArea.appendTo(elThumbImgArea);
			elThumbEffectArea.unbind("click").bind("click", function(){

				$.Mobile.Open_Viewer(thumbIndex);
			});
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
				elBtn.unbind().bind("click", function(){ $.Mobile.fold(itemList, "btn_fold", idx) });
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
		
		},
		/**
		 * Set mouse events on target thumb.
		 */
		setThumbMouseEvent : function(elThumb) {
			

		},

		Display_MainView : function(element) {
			var flag = $(element).attr("flag");

			$.each($("#btn-main-viewpager").find("div"), function() {
					$(this).removeClass("selected");
			});

			$.each($("#view-wrapper").children("div"), function() {
					$(this).hide();
			});

			switch(flag) {
				case "slip" : {

					$(element).addClass("selected");
					$("#slip-wrapper").show();

					if($.Mobile.arObjSlipItem == null) {
						$.Common.ShowProgress("#slip_progress","Waiting..","000000","0.7");
						$.Mobile.Draw_SlipList($.Mobile.params, $.Mobile.startIdx);
					}
					else
					{
						$("#slip_masonry").masonry('layout');
					}
					break;
				}
				case "attach" : {

					$(element).addClass("selected");
					$("#attach-wrapper").show();

					if($.Mobile.arObjAttachItem == null) {
						$.Common.ShowProgress("#attach_progress","Waiting..","000000","0.7");
						$.Mobile.Draw_AttachList($.Mobile.params);
					}
					break;
				}
			}

		},
		Draw_AttachList : function(params){
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
						$.Mobile.attachTotalCnt = res.length;
						if($.Mobile.attachTotalCnt > 0)
						{
							$.Mobile.arObjAttachItem = res;
							$.Mobile.addAttachItem(res,$("#area-attach"), $.Mobile.currentKey);
						}
			
			}).fail(function(res1){
				alert("Failed to load attach.");
			}).always(function(){
				
				$.Common.HideProgress("#attach_progress");
				
			});
		},

		addAttachItem : function(arObjAttach,elDest, specificKey)
		{
			elDest.empty();
			
			$.each(arObjAttach,function(i){
				
				if(!$.Common.isBlank(specificKey) && this.JDOC_NO != specificKey) return true;
				
				var elAttach = $.Mobile.Get_AttachElement(this);
				
				elAttach.appendTo(elDest);
				//$.Mobile.setAttachMouseEvent(elAttach);
		
			});
			
			//$("#area_attach").getNiceScroll().resize();
			
		},
		Get_AttachElement : function(objData) {
			var elAttach = $(document.createElement('div'));
			elAttach.addClass("attach-item");
			elAttach.attr("id","attach-item");
			
			var elLeftArea = $(document.createElement('div'));
			elLeftArea.addClass("attach-area-left");
			elLeftArea.appendTo(elAttach);

			var elTitle = $(document.createElement('div'));
			elTitle.addClass("attach-title");
			elTitle.html(objData.FILE_NAME);
			elTitle.appendTo(elLeftArea);

			var elAttachType = $(document.createElement('div'));
			elAttachType.addClass("attach-type");
			elAttachType.html(objData.ADD_KINDNM);
			elAttachType.appendTo(elLeftArea);

			var elRightArea = $(document.createElement('div'));
			elRightArea.addClass("attach-area-right");
			elRightArea.appendTo(elAttach);
			elRightArea.ripple({
					maxDiameter: "200%"
			});
			elRightArea.unbind("click").bind("click",function(e){
				e.stopPropagation();
				 $.Operation.execute($.Mobile, "DOWN_ORIGINAL_ATTACH", objData)});
			
			var elDownBtn = $(document.createElement('img'));
			elDownBtn.addClass("down-attach");
			elDownBtn.attr("src", g_RootURL+"image/common/context/download_cs.png");
			elDownBtn.attr("id","down-attach");
			elDownBtn.appendTo(elRightArea);
			
			return elAttach;

		},
		Download_attach : function(sdocNo) {

		}

		
}