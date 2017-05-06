<style>
	.tree-l-box .pageHeader .formBar{
		height:0px; width:80px;float:left;padding-top: 0px;border-width: 0px 0 0 0;padding: 0 0px;
	}
</style>
<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@sec.any name="CINEMA_MANAGE">
	       		<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    		<li><@dwz.a href="/operate/cinema-add" title="新增影院" target="dialog" class="button"><span>新增影院</span></@dwz.a></li>
		   		</@dwz.formBar>
	   		</@sec.any>
			<@dwz.pageForm action="/operate/cinema-list" buttonText="筛选" alt="可输入影院名称、行政区划、接入商类型检索">
				<input type="hidden" name="selectedId" value="${selectedId}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="37">
            <#if cinemaPage.contents?size gt 0>
            	<ul id="cinemaTree" class="ztree" layoutH="74"></ul>
            	<div class="panelBar">
		            <@sys.pageNav pageModel=cinemaPage />
		        </div>
            <#else>
		    	<div class="tree-msg" layoutH="84">
		    		<h3>没有符合条件的影院。</h3>
		    	</div>
		    </#if>
        </div>
	</div>
	<div id="cinemaBox" class="tree-r-box"></div>
</div>
<script type="text/javascript">
    var cinemaNodes = [
         <#list cinemaPage.contents as cinema>
         {
             id : "${cinema_index + 1}",
             cinemaId : "${cinema.id}",
             name : "[${cinema.county.city.name}".substring(0,3)+"]${cinema.name}",
             title : "",
             open : true,
             enabled : "${cinema.status}" == "${EnabledStatus.DISABLED}" ? false : true,
             salable : "${cinema.salable}",
             url : "operate/cinema-edit?cinema=${cinema.id}",
             target : "ajax",
             box : "cinemaBox",
             children : [
              {
                  id : ${cinema_index + 1} * 10 + 1,
                  url : "operate/ticketSettings-edit?id=${cinema.id}",
                  name : "选座票接入设置",
                  target : "ajax",
                  box : "cinemaBox",
                  tabindex : ${cinema_index + 1} * 10 + 1,
                  title: ""
              },{
             	 id : ${cinema_index + 1} * 10 + 2,
                  url : "operate/memberSettings-edit?id=${cinema.id}",
                  name : "会员接入设置",
                  target : "ajax",
                  box : "cinemaBox",
                  tabindex : ${cinema_index + 1} * 10 + 2,
                  title: ""
              },{
             	 id : ${cinema_index + 1} * 10 + 3,
                  url : "operate/hall-list?cinemaId=${cinema.id}",
                  name : "影厅管理",
                  target : "ajax",
                  box : "cinemaBox",
                  tabindex : ${cinema_index + 1} * 10 + 3,
                  title: ""
              }
             ]
         },
         </#list>
    ];
    var cinemaSetting = {
        view : {
            addHoverDom : addCinemaExpandNode,
            removeHoverDom : removeCinemaExpandNode,
            addDiyDom : addCinemaDIYNode,
            selectedMulti : false,
            showIcon : false
        },
        data : {
        	key: {
        		title : "title"
        	}
        }
    };
    
    function addCinemaDIYNode(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("cinemaId",treeNode.cinemaId);
        if(treeNode.level == 0) {
        	setCinemaColor(treeNode, "#" + treeNode.tId + "_a");
        }
    }
    
    function setCinemaColor(node,id) {
    	// 启用
    	if(node.enabled){
    		$(id).css("color","${StatusColor.GREEN}");
    	}else{
    		$(id).css("color","${StatusColor.GRAY}");
    	}
    }
    
    function addCinemaEnabled(treeNode) {
    	if(treeNode.level != 0){
     		return "";
     	}
    	if(!treeNode.enabled) {
    		return "<a onclick=cinemaEnable('" + treeNode.cinemaId + "')>[启用]</a>";
        }
    	return "<a onclick=cinemaDisable('" + treeNode.cinemaId + "')>[停用]</a>";
    }
    
    function addCinemaSalabled(treeNode) {
    	if(treeNode.level != 0){
     		return "";
     	}
    	if(!treeNode.salable) {
    		return "<a onclick=cinemaSalable('" + treeNode.cinemaId + "')>[启售]</a>";
        }
    	return "<a onclick=cinemaNonSalable('" + treeNode.cinemaId + "')>[停售]</a>";
    }
   	
   	function cinemaEnable(cinemaId) {
   		alertMsg.confirm('启用影院将重新生成相关的渠道排期，您是否确定要启用该影院？', {
			okCall: function(){
				ajaxTodo('operate/cinema-enable?cinema=' + cinemaId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
   	function cinemaDisable(cinemaId) {
   		alertMsg.confirm('停用影院会将作废相关的渠道排期，您是否确定要停用该影院？', {
			okCall: function(){
				ajaxTodo('operate/cinema-disable?cinema=' + cinemaId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
  	function cinemaSalable(cinemaId) {
   		alertMsg.confirm('启售影院将上架相关的渠道排期，您是否确定要启售该影院？', {
			okCall: function(){
				ajaxTodo('operate/cinema-open-salable?cinema=' + cinemaId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
   	function cinemaNonSalable(cinemaId) {
   		alertMsg.confirm('停售影院将下架相关的渠道排期，您是否确定要停售该影院？', {
			okCall: function(){
				ajaxTodo('operate/cinema-close-salable?cinema=' + cinemaId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	// 编辑
    function edit(cinemaId) {
    	$.pdialog.open('operate/cinema-edit?cinema=' + cinemaId, "cinema-edit", "编辑影院",  {width:700,height:500,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    function cinemaExpandNode(treeNode) {
        var addStr = "<span id='addCinemaExpandNode_" + treeNode.tId + "'>";
        <@sec.any name="CINEMA_SWITCH">
        	addStr += addCinemaEnabled(treeNode);
        	addStr += addCinemaSalabled(treeNode);
        </@sec.any>
        addStr += "</span>";
        return addStr;
    }

    function removeCinemaExpandNode(treeId, treeNode) {
        $("#addCinemaExpandNode_" + treeNode.tId).unbind().remove();
    };
    
    function addCinemaExpandNode(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addCinemaExpandNode_" + treeNode.tId).length > 0)
            return;
        if (treeNode.level == 0) {
        	var editStr = "<span id='addCinemaExpandNode_" + treeNode.tId + "'>";
        	 <@sec.any name="CINEMA_SWITCH">
	       	  	if(treeNode.enabled){
	          		editStr += "<a onclick=cinemaDisable('" + treeNode.cinemaId + "')>[停用]</a>";
	       	  	}else{
	       	 		editStr += "<a onclick=cinemaEnable('" + treeNode.cinemaId + "')>[启用]</a>";
	       	  	}
	       	 	if(treeNode.salable){
	   				editStr += "<a onclick=cinemaNonSalable('" + treeNode.cinemaId + "')>[停售]</a>";
	       	 	}else{
		 			editStr += "<a onclick=cinemaSalable('" + treeNode.cinemaId + "')>[启售]</a>";
        		}
		    </@sec.any>
	        editStr += "</span>";
            sObj.after(editStr);
            return;
        }
        sObj.after(cinemaExpandNode(treeNode));
    };

    var cinemaTree;
    $(function() {
    	cinemaTree = $.fn.zTree.init($("#cinemaTree"), cinemaSetting, cinemaNodes),flag =0;
	    $("#cinemaTree").find("li[class='level0']").each(function(i){
	    	var liId = $(this).attr("id").split("_")[1];
	    	if("${selectedId}" == "" || $("a[cinemaId=${selectedId}]").attr("id") == undefined){
	    		if($(this).find("ul").length >0){
	    			flag ++;
	    		}
	    		if(flag > 1){
	    			$(this).find("#cinemaTree_"+ liId +"_switch").click();
	    		}
	    	}else{
	    		var selectId = $("a[cinemaId=${selectedId}]").attr("id").split("_")[1];
	    		if(liId != selectId){
	    			$(this).find("#cinemaTree_"+ liId +"_switch").click();
	    		}
	    	}
	    });

    	$("a[cinemaId]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectedId]",$(this).getPageDiv()).val($(this).attr("cinemaId"));
    		});
    	});
    	var selectedCinemaLink = $("a[cinemaId]").eq(0);
    	if("${selectedId}" && $("a[cinemaId=${selectedId}]").size() > 0) {
    		selectedCinemaLink = $("a[cinemaId=${selectedId}]");
    	}
    	
    	setTimeout(function() {
    		selectedCinemaLink.click();
        }, 300);
    });	
</script>