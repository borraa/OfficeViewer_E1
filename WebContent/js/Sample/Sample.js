$.Sample = {
	xmlData : null,
	xmlURL : null,
	WebParams : null,
	curSection : null,
	curType : null,
	vURL : null,
	vUserID : null,
	vSvrMode : null,
	vCorpNo : null,
	vViewMode : null,
	vJDocNo : null,
	vApprStat : null,
	init : function(params){
		
		$.Sample.WebParams = params;
		$.Sample.xmlURL = $.Sample.WebParams.XMLDATA_PATH;
		
		//Get xml list
		$.when($.Common.getConfData($.Sample.xmlURL, "xml")).then(function (xml) {
			
			$.Sample.xmlData = $(xml);
			
			//Assign default section.
			var xmlSection = $.Sample.xmlData.find("Section");
			$.Sample.curSection = 1;
			
			//setup search function.
			//$.Sample.SearchFunc();
			
			//Draw logo
			$.Sample.drawLogo(xmlSection.eq($.Sample.curSection).attr("Type"));
			
			//Draw Top menu
			$.Sample.drawTopNavi(xmlSection);

			//Draw right frame
			$.Sample.movePage(xmlSection.eq($.Sample.curSection).attr("Type"));
			
		}).fail(function(){
			console.log("F");
		}).always(function(){
			console.log("Always");
		});
		
	},
	drawLogo : function(title) {

		var logoTitle = $(document.createElement('div'));
		logoTitle.addClass("logoTitle");
		logoTitle.html("전자증빙 Sample(" + title + ")");
		logoTitle.appendTo(".logo");
		
	},
	drawTopNavi : function(menu) {

		$.each(menu, function(){
			var sectionUSE 	= $(this).attr("USE");
			var title 		= $(this).attr("Type");
			var elTitle 	= $(document.createElement('div'));
			
			if(sectionUSE == "0"){
				$(this).css("display", "none");   
			} else {
				elTitle.html(title);
				elTitle.appendTo($("#topNavi"));
			}
			
			elTitle.on({
				click: function(){
					$.Sample.movePage(title);
				}
			});
		});
	},
	movePage : function(type) {
		
//		$("#sample_search").each(function(){
//			this.value = $(this).attr('title');
//		});
		
		$.Sample.fnBtnClose();
		$(".logoTitle").empty();
		
		var nodeSection = $.Sample.xmlData.find("Section");
	
		$.each(nodeSection, function() {
			if(type.toUpperCase() == $(this).attr("Type").toUpperCase())
			{	
				$.Sample.curType = $(this).attr("Type");	// 이동할 페이지의 Type
				$(".logoTitle").html("전자증빙 Sample(" + $(this).attr("Type") + ")"); 	// 페이지 이동 시 해당 Type으로 LogoTitle 변경
				$.Sample.drawItem($(this));
				return false;
			}
		});
	},
	drawItem : function(nodeSection) {
		
		$("#section_area_api").empty();
		
		$.each(nodeSection.children(), function(){
			var tagName = this.tagName; //Get tagname
			
			switch(tagName.toUpperCase())
			{
			case "TITLE" :
				$.Sample.drawTitle($(this));
				break;
			/*case "COLUMN" : 
				$.Sample.drawColumn($(this));
				break;*/
			}
		});
	},
	paramChange : function() {
		
	},
	drawTitle : function(node) {
		
		// Draw Title
		var title = node.attr("Title");
		var elTitle = $(document.createElement('div'));
		var elEffect = $(document.createElement('b'));
		elEffect.html(title);
		elEffect.appendTo(elTitle);
		elTitle.addClass("api_title");
		
		// Draw Column
		$.each(node.find("Column"), function(){
			var CloumnTitle = $(this).attr("Title");
			var elApiFunc = $(document.createElement('div'));
			elApiFunc.addClass("api_func");
			
			var elFuncTitle = $(document.createElement('div'));
			elFuncTitle.addClass("api_func_title");
			elFuncTitle.html(CloumnTitle);
			elFuncTitle.attr("onclick", "$.Sample.apiContext('"+$(this).attr("ID")+"')");
			elFuncTitle.appendTo(elApiFunc);
			
			var elFuncText = $(document.createElement('div'));
			elFuncText.addClass("api_func_text");
			elFuncText.text($(this).children().attr("Title"));
			elFuncText.attr("onclick", "$.Sample.apiContext('"+$(this).attr("ID")+"')");
			elFuncText.appendTo(elApiFunc);
			
			var elBtn = $(document.createElement('div'));
			elBtn.addClass("api_func_run");
			elBtn.html("Run");
			elBtn.attr("onclick", "$.Sample.fn"+$(this).attr("ID")+"()");
			elBtn.appendTo(elApiFunc);
			elApiFunc.appendTo(elTitle);
			
			// Draw Contents 
			var elContents = $(document.createElement('div'));
			elContents.addClass("api_func_contents");
			elContents.attr("id", $(this).attr("ID"));
			
			var elContentsArea = $(document.createElement('div'));
			elContentsArea.addClass("api_contents_area");
			
			var elContentsSource = $(document.createElement('div'));
			elContentsSource.addClass("api_contents_Source");
			
			var fullURL = $(this).find("SampleURL").text();
			var elSourceURL = $(document.createElement('b'));
		//	elSourceURL.text("Example > " + $.Sample.WebParams.WEB_URL);
			elSourceURL.text("Example > " + location.href + fullURL);
			elSourceURL.appendTo(elContentsSource);
			
			// Draw Description
			var elFuncDepiction = $(this).find("Description");
			var elDepiction = $(document.createElement('div'));
			elDepiction.addClass("api_depiction");
			elDepiction.append(elFuncDepiction.innerHTML);
			elDepiction.appendTo(elContentsArea);

			// Draw Parameter
			var elFuncParam = $(this).find("Parameter");
			var elFuncInput = $(this).find("Input");
			
			var elParam = $(document.createElement('div'));
			elParam.addClass("api_param");

			$.each($(elFuncParam).children(), function(){
				var elInput = $(document.createElement('div'));
				elInput.addClass("api_param_input");
				//elInput.attr("onclick", "$.Sample.paramChange()");
				elInput.attr("onclick", "");
				elInput.text($(this).attr("Title") + " : ");
				
				var typeName = $(this).attr("type");

				switch(typeName)
				{
				case "Radio" :
					$.each($(this).children(), function(){
						var ParamLabel = $(document.createElement('label'));
						ParamLabel.text($(this).attr("Title"));
						
						var ParamTypeRadio = $(document.createElement('input'));
						ParamTypeRadio.attr("type", "radio");
						ParamTypeRadio.attr("name", $(this).parent().attr("name"));
						ParamTypeRadio.val($(this).attr("Value"));
						if($(this).index() == 0) {
							ParamTypeRadio.attr("checked", true);
						}
						ParamTypeRadio.appendTo(ParamLabel);
						ParamLabel.appendTo(elInput);
					})
					break;
				case "Text" :
					var ParamTypeText = $(document.createElement('input'));
					ParamTypeText.attr("type", "text");
					ParamTypeText.attr("id", $(this).attr("name"));
					ParamTypeText.val($(this).children().attr("Value"));
					ParamTypeText.appendTo(elInput);
					break;
				}
				elInput.appendTo(elParam);
			});
			
			elParam.appendTo(elContentsArea);
			elContentsArea.appendTo(elContents);
			elContentsSource.appendTo(elContents);
			elContents.appendTo(elTitle);
		});
		
		elTitle.appendTo($("#section_area_api"));
		
	},
	apiContext : function(objID){
		
		var obj 			= document.getElementById(objID);
		var vDisplay 		= obj.style.display;
		
		if( vDisplay == "none" || vDisplay == "" ){ 
			$("#"+objID).slideDown();
		} else {
			$("#"+objID).slideUp();
		}
	},
	SearchFunc : function(){
		
		$("#sample_search").each(function(){
			this.value = $(this).attr('title');
			
			$(this).focus(function(){
				if(this.value == $(this).attr('title')) {
					this.value = "";
				}
			});
			$(this).blur(function(){
				if(this.value == "") {
					this.value = $(this).attr('title');
				}
			});
		});
		
		 // 키워드 검색
		$("#sample_search").keyup(function(){
			var searchTxt = $(this).val();
			
			//$("#section_area_api > .api_title > .api_func").hide();
			$("#section_area_api > .api_title > .api_func").hide();
			$(".api_func_contents").hide();
			$.Sample.fnBtnClose();
			
			var temp = $("#section_area_api > .api_title > .api_func:contains('" + searchTxt + "')");
			
			$(temp).show();
		});
	},
	fnBtnRun : function(){
		$("#frameCloseBtn").addClass("open");
		$("#frameMenu").addClass("open"); 
	},
	fnBtnClose : function(){
		$("#frameCloseBtn").removeClass("open"); 
		$("#frameMenu").removeClass("open"); 
	},
	makeViewerUrl : function(vJDocNo){
		
		$.Sample.fnBtnRun(); 
		
		var vParams		= 	"KEY="				+ decodeURIComponent(decodeURIComponent(vJDocNo))
							+"&SVR_MODE="		+ $.Sample.vSvrMode
							+"&USER_ID="		+ $.Sample.vUserID
							+"&CORP_NO="		+ $.Sample.vCorpNo
							+"&VIEW_MODE="		+ $.Sample.vViewMode
							+"&APPR="			+ $.Sample.vApprStat


		var vResult		= $.Sample.vURL + "index.jsp?"+ vParams;
		

		return vResult;
	},
	// 전자증빙 Viewer 생성
	fnSLIP_Create : function(){
		
		var fnURL			= null;
		fnURL 				= $.Sample.WebParams.WEB_URL;
		//fnServer
		var fnSvrMode		= $(':radio[name="SLIPCreate_Server"]:checked').val();
		var fnViewMode 		= $(':radio[name="SLIPCreate_ViewMode"]:checked').val();
		//var fnWorkGroup 	= "slip";
		var fnJDocNo		= $("#SLIPCreate_JDocNo").val();
	  	var fnUserInfo		= $("#SLIPCreate_UserInfo").val();
	  	var fnLang			= $(':radio[name="SLIPCreate_Lang"]:checked').val();
	  	var fnCorpNo 		= $("#SLIPCreate_CorpNo").val();
		var fnApprStat 		= $(':radio[name="SLIPCreate_ApprStat"]:checked').val();
	  	
	  	if(fnCorpNo == "")
	  	{
	  		alert("법인을 입력하세요.");
	  		return;
	  	}
	  	
	  	var ret = $.Sample.SLIP_Create(fnURL, fnSvrMode, fnJDocNo, fnUserInfo, fnCorpNo, fnViewMode, fnApprStat);

	  	//뷰어 새로고침
		$("#OfficeXPIFrm").attr("src", ret);
	},
	SLIP_Create : function(fnURL, fnSvrMode, fnJDocNo, fnUserInfo, fnCorpNo, fnViewMode, fnApprStat){
		
		$.Sample.vURL			= fnURL;
		$.Sample.vSvrMode		= fnSvrMode;
		$.Sample.vJDocNo		= fnJDocNo;
		$.Sample.vUserID		= fnUserInfo;
		$.Sample.vCorpNo		= fnCorpNo;
		$.Sample.vViewMode		= fnViewMode;
		$.Sample.vApprStat		= fnApprStat;
		
		return $.Sample.makeViewerUrl($.Sample.vJDocNo);
	},
	// 전자증빙 Viewer 초기화
	fnSLIP_Init : function(){

		$.Sample.vJDocNo 	= "EMPTY";
		var ret 			= $.Sample.makeViewerUrl($.Sample.vJDocNo);
		
		$("#OfficeXPIFrm").attr("src", ret);	
	},
	// 전자증빙 조회
	fnSLIP_Load : function(){
		
		var vJDocNo		= $("#SLIPLoad_JDocNo").val();
		
	   	if(vJDocNo == "")
	   	{
	   		alert("증빙 키를 입력하세요.");
	   		return;
	   	}
	   	
		var ret 		= $.Sample.makeViewerUrl(vJDocNo);
	   	
	  	$("#OfficeXPIFrm").attr("src", ret);
	},
	// 전자증빙 Viewer 모드 변경
	fnSLIP_SetMode : function(){
		
		var vViewMode		= $(':radio[name="SLIPSetMode_ViewMode"]:checked').val();  
		$.Sample.vViewMode	= vViewMode;

	   	var ret				= $.Sample.makeViewerUrl($.Sample.vJDocNo);
	   	
		$("#OfficeXPIFrm").attr("src", ret);
	}
}
