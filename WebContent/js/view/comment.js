$.Comment = {
		elListContainer : null,
		params : null,
		localeMsg : null,
		colorSet: null,
		init : function(params) {
			$.Common.ShowProgress("#area_progress","Waiting..","000000","0.7");

			this.params 	= params;
			
			//Set globalization.
			$.Comment.localeMsg = $.Common.Localize($.Lang, "data-i18n", params.LANG,"Comment");
			//Set UI set
			$.Comment.setUIColor();
			$.Comment.initializeComment();
		
		},
		initializeComment : function()
		{
			
			$(window).on('resize', $.Common.windowCallback(function(){
				$("#ipt_content").getNiceScroll().resize();
				$("#comtList").getNiceScroll().resize();
			}));
			
			$(window).on("unload", function() {
				if(window.opener != null)
				{
					window.opener.$.Actor.getCommentCnt();
				}
			});
			
			
			
			$("#ipt_content").on("keypress",function(e){
				var code = (e.keyCode ? e.keyCode : e.which);
			    if (code == 13) 
			    	{
			    		$("#ipt_content").getNiceScroll().resize();
			    	}
			});
			
			$("#apply").unbind().bind("click",function(){
				$.Comment.write($("#ipt_title"), $("#ipt_content"));
			});
			
			$.Comment.elListContainer = $("#comtList");
			
			$.Comment.refresh();
			
		},
		getCommentCnt : function() {
			$.when($.Common.RunCommand(g_CommentCommand, "GET_COMMENT_COUNT", $.Comment.params)).then(function(resMsg){
				
				$("#comt_cnt").html(" (" + resMsg.COMT_CNT + ")");
			});
		},
		//Get comment I/F Key related list
		getCommentList : function(callback) {
			
			//Add progress
			var elProgress = $(document.createElement('div'));
			elProgress.addClass("comment_progress");
			elProgress.attr("id","comment_progress");
			elProgress.appendTo($("#comtList"));
			
			$.Common.ShowProgress("#comment_progress","Waiting..","000000","0.7");
			
			$.when($.Common.RunCommand(g_CommentCommand, "GET_COMMENT_LIST", $.Comment.params)).then(function(arObjList){
				
				$("#comtList").empty();
				
				$.Common.HideProgress("#comment_progress");
				$.each(arObjList, function(){
					$.Comment.drawComment(this);
				});
				
				if(callback != null)
				{
					callback();
				}
				
			})
			.always(function(){
				$.Common.HideProgress("#comment_progress");
			});
		},
		/**
		 * Draw comment 
		 */
		drawComment: function(objItem) {
			
				var userID 		= objItem.REG_USER;
				var userNM 	= objItem.USER_NM;
				var title 		= objItem.COMT_TITLE;
				var content 	= objItem.COMT_DATA;
				var regTime 	= objItem.REG_TIME;
				var comtIrn	= objItem.COMT_IRN;
				var myComt 	= objItem.MY_COMT;
				
				var commentBG = $.Comment.colorSet.COMMENT_BG;
				var commentFont = $.Comment.colorSet.COMMENT_FONT;
				
				//Wrapper
				var elWrapper = $(document.createElement('div'));
				elWrapper.addClass("comt_item_wrapper");
				elWrapper.appendTo($("#comtList"));
				
				var elArea = $(document.createElement('div'));
				elArea.addClass("comt_item_area");
				
				var elOptionsArea = $(document.createElement('div'));
				
				
				if(myComt == "1")
				{
					elArea.addClass("my_comt");
					elOptionsArea.addClass("my_comt");
					
				//	
					commentBG = $.Comment.colorSet.MY_COMMENT_BG;
					commentFont = $.Comment.colorSet.MY_COMMENT_FONT;
				}
				else
				{
					elArea.addClass("other_comt");
				//	
				}
				elArea.appendTo(elWrapper);
				
				//Draw ption area
				elOptionsArea.addClass("comt_item_options");
				elOptionsArea.appendTo(elArea);
				
				$(document.createElement('div')).css("clear","both").appendTo(elArea);
				
				var elRegUser = $(document.createElement('div'));
				elRegUser.addClass("reg_user");
				elRegUser.html(userNM + " (" + userID + ")");
				elRegUser.appendTo(elOptionsArea);
				
				var elRegTime = $(document.createElement('div'));
				elRegTime.addClass("reg_time");
				elRegTime.html(regTime);
				elRegTime.appendTo(elOptionsArea);
			
				/**
				 * Draw content area
				 */
				var elContentsArea = $(document.createElement('div'));
				elContentsArea.addClass("comt_item");
				elContentsArea.appendTo(elArea);
				
				var elCommentArea =  $(document.createElement('div'));
				elCommentArea.attr("comt-irn",comtIrn);
				
				if(myComt == "1")
				{
					elContentsArea.attr("my_comt", "1");
					elContentsArea.css("float","right");
					
					var elRemove = $(document.createElement('div'));
					elRemove.addClass("remove");
					elRemove.append($(document.createElement('img')).attr("src",g_RootURL + "image/common/remove.png"));
					elRemove.appendTo(elContentsArea);
					
					elRemove.on({
						mouseenter : function() {
							$(this).css("opacity","0.5");
						},
						mouseleave : function() {
							$(this).css("opacity","1");
						},
						click : function(){
							$.Comment.remove(comtIrn);
						}
					});
					
					var elModify = $(document.createElement('div'));
					elModify.addClass("modify");
					elModify.append($(document.createElement('img')).attr("src",g_RootURL + "image/common/modify.png"));
					elModify.appendTo(elContentsArea);
					
					elModify.on({
						mouseenter : function() {
							$(this).css("opacity","0.5");
						},
						mouseleave : function() {
							$(this).css("opacity","1");
						},
						click : function(){
							$.Comment.modify(elCommentArea, objItem);
						}
					});
					
					//Add mouse event.
					elContentsArea.on({
						mouseenter : function() {
							elRemove.css("visibility", "visible");
							elModify.css("visibility", "visible");
						},
						mouseleave : function() {
							elRemove.css("visibility", "hidden");
							elModify.css("visibility", "hidden");
						}
					});
					
				}
				else
				{
					elContentsArea.attr("my_comt", "0");
				}
				
				elCommentArea.addClass("comt_item_content");
				elCommentArea.css("background-color","#"+commentBG);
				elCommentArea.appendTo(elContentsArea);
				
				
				var elTitle = $(document.createElement('div'));
				elTitle.addClass("title");
				elTitle.css("color","#"+commentFont);
				elTitle.html(title);
				elTitle.appendTo(elCommentArea);
				
				var elContent = $(document.createElement('div'));
				elContent.addClass("content");
				elContent.css("color","#"+commentFont);
				elContent.html(content);
				elContent.appendTo(elCommentArea);
			
				/**
				 * Add divider
				 */
				var elDivider = $(document.createElement('div'));
				elDivider.addClass("comt_divider");
				elDivider.appendTo($("#comtList"));
	
		},
		
		/**
		 * Write comment
		 */
		write : function(title, content) {
			var title 		= $(title).val();
			var content 	= $(content).val();
			
			//Check if null
			if($.Common.isBlank(title) || $.Common.isBlank(content))
			{
				$.Common.simpleAlert(null, this.localeMsg.INPUT_CONTENTS, null);
				return;
			}
			
			var objComment = {
				"KEY" : $.Comment.params.KEY,
				"TITLE" : title,
				"CONTENT" : content
			};

			$.Operation.execute($.Comment, "WRITE_COMMENT", objComment);

		},
		refresh: function() {
			$.Comment.reset();
			$.Comment.getCommentCnt();
			
			var callback = function() { $.Comment.moveToScroll(); }
			
			$.Comment.getCommentList(callback);
		},
		remove : function(comtIrn) {
			$.Common.simpleConfirm($.Comment.localeMsg.Confirm,$.Comment.localeMsg.CONFIRM_DOWNLOAD, function(){
				
				var objComment = {
					"KEY" : $.Comment.params.KEY,
					"COMT_IRN" : comtIrn,
					"CORP_NO" : $.Comment.params.CROP_NO,
					"USER_ID" : $.Comment.params.USER_ID,
				};

				$.Operation.execute($.Comment, "REMOVE_COMMENT", objComment);

			});
		},
		moveToScroll : function(top) {
			
			if(top == null) top = $.Comment.elListContainer.prop("scrollHeight");
			$.Comment.elListContainer.scrollTop(top);
			
		},
		modify : function(elComment, objItem) {
			
			var title 		= objItem.COMT_TITLE;
			var content 	= objItem.COMT_DATA;
			content = content.replaceAll("<br>","\n");
			var comtIrn	= objItem.COMT_IRN;

			elComment.css("background-color","#f9f9f9");
			elComment.css("box-shadow","inset 0px 0px 1px 1px #"+$.Comment.colorSet.COMMENT_BG_FOCUS);
			elComment.css("-webkit-box-shadow","inset 0px 0px 1px 1px #"+$.Comment.colorSet.COMMENT_BG_FOCUS);
			$("#ipt_title").val(title);
			$("#ipt_content").val(content);
			
			//set apply button to modify
			$("#apply").unbind().bind("click",function() {

				var objComment = {
					"KEY": $.Comment.params.KEY,
					"COMT_IRN": comtIrn,
					"TITLE": $("#ipt_title").val(),
					"CONTENT": $("#ipt_content").val()
				};

				$.when($.Operation.execute($.Comment, "MODIFY_COMMENT", objComment)).then(function (flag) {

					if ("T" === flag) {
						var offsetTop = $.Comment.elListContainer.scrollTop();

						var callBack = function () {
							$.Comment.moveToScroll(offsetTop);
						}
						$.Comment.getCommentCnt();
						$.Comment.getCommentList(callBack);

					} else {
						$.Common.simpleAlert(null, objRes.MSG, null);
					}

				}).always(function () {
					$.Comment.reset();

					var commentBG = $.Comment.colorSet.MY_COMMENT_BG;

					elComment.css("background-color", "#" + commentBG);
					elComment.css("box-shadow", null);
					elComment.css("-webkit-box-shadow", null);

					$("#apply").unbind().bind("click", function () {
						$.Comment.write($("#ipt_title"), $("#ipt_content"));
					});
				});
			})
		},
		reset : function() {
			$("#ipt_title").val("");
			$("#ipt_content").val("");
		},
		setUIColor : function()
		{
			var objColorComment = $.extend($.Color.PC.Comment, $.Color.Common);
	
			if(objColorComment != null)
			{
				$(".area_comment_title").css({"background":"#"+objColorComment.NAVIGATION, "color" : "#" + objColorComment.NAVIGATION_FONT});
				$(".area_comment_write").css({"border-top":"1px solid #"+objColorComment.INPUT_AREA});
				$(".ipt_content").niceScroll({cursorcolor:"#"+objColorComment.NAVIGATION});
				$(".ipt_title:focus").css({"border-bottom":"2px solid #"+objColorComment.WRITE_BORDER});
				$(".ipt_content:focus").css({"border-bottom":"2px solid #"+objColorComment.WRITE_BORDER});
				$("#ipt_content").niceScroll({horizrailenabled: false, cursorcolor:"#"+objColorComment.NAVIGATION});
				$(".apply").css({"background":"#"+objColorComment.WRITE_BUTTON});
				$("#comtList").niceScroll({horizrailenabled: false, cursorcolor:"#"+objColorComment.NAVIGATION});
				$.Comment.colorSet = objColorComment;
			}
		},
}