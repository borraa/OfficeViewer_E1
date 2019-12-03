$.ContextMenu = {
		id : "context-menu",
		caller : null,
		option : {
			      shiftDisable : false, // Allow access to native contextMenu by rightclick + shift
			      disableDefaultContext: false, // disables the native contextmenu everywhere you click
			      leftClick : false, // show menu on left mouse click instead of right
			      isMatchTop : false,
		},
//		
//		create : function(el, menu, option) {
//			
//			$.ContextMenu.option.isMatchTop = false;
//			
//			$("#"+$.ContextMenu.id).remove();
//			$.ContextMenu.caller = el;
//			
//			var cm = $(document.createElement('div'));
//			cm.addClass("context-menu");
//			cm.attr("id", $.ContextMenu.id);
//			cm.hide().appendTo("body");
//			
//	
//			//integrate options.
//			$.ContextMenu.option = $.extend($.ContextMenu.option, option);
//			
//			var evName = "contextmenu";
//			if($.ContextMenu.option.leftClick) evName = "click";
//			
//			$(document).off(evName, $.ContextMenu.contextHandler).on(evName, $.ContextMenu.contextHandler);
//
//			    $(window).blur(function () {
//			    	$.ContextMenu.hideMenu();
//			    });
//
//		    $.ContextMenu.getMenu(menu);
//	
//		    
//		},
//		contextHandler : function(e) {
//					//	if(!leftClick) 
//			//	{
//					if ($.ContextMenu.option.disableDefaultContext) {
//						e.preventDefault();
//					}
//			      
//					$.ContextMenu.hideMenu();
//					
//					if($.ContextMenu.caller != null)
//					{
//						$.ContextMenu.mouseEvent(e);
//						$.ContextMenu.caller = null;
//					}
//			//	}
//		},
//		mouseEvent : function(e) {
//			var cm = $("#"+$.ContextMenu.id);
//			
//			  cm.css({
//			        display: 'none',
//			        position: 'absolute',
//			        zIndex: 1000
//			      });
//
//			      // Draw menu based on mouse pointer position.
//			 //   var mWidth = cm.outerWidth(true);
//	        //	var mHeight = cm.outerHeight(true);
//		      //  var xPos = ((e.pageX - $(window).scrollLeft()) + mWidth < $(window).width()) ? e.pageX : e.pageX - mWidth;
//		     //   var yPos = ((e.pageY - $(window).scrollTop()) + mHeight < $(window).height()) ? e.pageY : e.pageY - mHeight;
//				
//			  var elTarget =  $($.ContextMenu.caller);
//			  	var elTargetHeight = elTarget.height() / 3;
//			  	if($.ContextMenu.option.isMatchTop) elTargetHeight = 0;
//			  	var elPadding = 2;
//			  	var xPos = elTarget.offset().left + elTarget.width() + elPadding;
//			  	var yPos = elTarget.offset().top + elTargetHeight;
//	        	cm.show(0, function() {
//	        		cm.css({
//			        diplay: 'block',
//			        top: yPos + 'px',
//			        left: xPos + 'px',
//			        zIndex: 1000
//			      });
//	        	});
//			
//		},
//		hideMenu : function() {
//			if($("#"+$.ContextMenu.id).is(':visible')) $("#"+$.ContextMenu.id).hide();
//		},
		drawSubMenu : function(elParent, item) {
			
		},
		showMenu : function(elTarget, menu) {
		
			var padding = 2;
			var left = elTarget.offset().left + elTarget.width() + padding;
			var top = elTarget.offset().top;
			menu.css({
				display:"inline-table",
				left:left,
				top:top,
				position:"fixed",
				zIndex:1000,
				width:"auto"
			});
			
			//Check left breaks through window
			var menuWidth 		= menu.width();
			var estimatedWidth 	= left + menuWidth;
			var windowWidth 	= $(document).width();
			if(estimatedWidth  > windowWidth)
			{
				left = (elTarget.offset().left + elTarget.width()) - menuWidth - elTarget.width();
			}
			
			menu.css({
				left:left,
			});
			
			var menuHeight 		= menu.height();
			var estimatedHeight = top + menuHeight;
			var windowHeight 	= $(document).height();
			
			var isBottomAlign = menu.attr("bottom_align");
			if("true" == isBottomAlign) {
				top = top + elTarget.height() - menuHeight;
			}
			else
			{
				if(estimatedHeight  > windowHeight)
				{
					top = windowHeight - menuHeight;
				}
			}
			
			var isSpacingTop = menu.attr("spacing_top");
			if("true" == isSpacingTop) {
				top =  top + (elTarget.height() / 4)
			}
			menu.css({
				top:top,
			});
			
			var slipItem = $(elTarget).parents(".slip_item");

			var isFollow = slipItem.attr("follow");
			
			if("1" != isFollow) {
				menu.children().each(function() {
					var command = $(this).attr("command");
					if("MODIFY_AFTER" == command) {
						$(this).hide();
					}
				});
			}
			
			menu.fadeIn(300, function(){ $(this).show(); });
			
			$(window).blur(function () {
				menu.hide();
		    });
			
			$(window).on('resize', function () {
				menu.hide();
		    });
			$(window).on('mousewheel', function(){
				menu.hide();
			});
			
			$(window).on('click', function(){
				menu.hide();
			});
		},
		//parse menu items
		getMenu : function(objParent, elTarget, elTrigger, menu, objItemValue, option){
			
			var elContextArea =  $(document.createElement('div'));
			elContextArea.addClass("context_wrapper");
			elContextArea.attr("id","contextWrapper");
			
			if(option != null) {
				$.each(Object.keys(option), function() {
					elContextArea.attr(this, option[this]);
				});
			}
			
			$.each(menu, function(){
			     
				var title 			= this.TITLE;
				var icon 			= this.ICON;
				var evClick 		= this.click;
				var subMenu 		= this.SUBMENU;
				var id 				= this.MENU_ID;
				var cs_operation 	= this.CS_OPERATION;
				
				if("SEPARATOR" == id.toUpperCase())
				{
					var elSeparator = $(document.createElement('div'));
					elSeparator.addClass("separator");
					elSeparator.appendTo($("#"+$.ContextMenu.id));
					elContextArea.append(elSeparator);
					return true;
				}
				
				var elMenuArea = $(document.createElement('div'));
				elMenuArea.addClass("menu_item");
				elMenuArea.attr("command",id);
				elMenuArea.appendTo($("#"+$.ContextMenu.id));
				if(cs_operation != null) elMenuArea.attr("cs_operation",cs_operation);
				
				elMenuArea.unbind("click").bind("click", function(e){
					e.stopPropagation();
					$.Operation.execute(objParent, elMenuArea, objItemValue, elTarget);
				});
				
				//Add icon
				if(icon != null)
				{
					var elIconArea = $(document.createElement('div'));
					elIconArea.addClass("icon");
					elIconArea.append($(document.createElement('img')).attr("src",icon));
					elIconArea.appendTo(elMenuArea);
				}
				
				//Add title
				var elTitleArea = $(document.createElement('div'));
				elTitleArea.html(title);
				elTitleArea.appendTo(elMenuArea);
				
				//Add icon
				if(evClick != null)
				{
					elMenuArea.on({
						click : evClick
					});
				}
				
				if(subMenu != null)
				{
					//Add Arraow
					
					var arrowIcon = null;
					
					var elArrowArea = $(document.createElement('div'));
					elArrowArea.append($(document.createElement('img')).attr("src",arrowIcon));
					elArrowArea.appendTo(elMenuArea);
				
					$.ContextMenu.drawSubMenu(elMenuArea, subMenu);
				}
				
				elMenuArea.appendTo(elContextArea);
			});
			
			//Add show/hide this menu event to target
			elTrigger.unbind("click").bind("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				
				
				
				if(elContextArea.is(":visible")) {
					elContextArea.fadeOut(200, function(){ $(this).hide() });
				}
				else
				{
					
					$("[id=contextWrapper]").fadeOut(200, function(){ 
						$(this).hide();
						
					});
					
					$.ContextMenu.showMenu(elTrigger, elContextArea);
				}
			});
			
			if(elContextArea.children().length > 0) 
			{
				elContextArea.appendTo(elTarget);
				
				return elContextArea;
			}
			else
			{
				return null;
			}
		}	
}
