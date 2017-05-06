<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/cinemaPolicy-list" buttonText="筛选" alt="可输入影院名称、行政区划检索">
				<input type="hidden" name="selectedPolicyId" value="${selectedPolicyId}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="37">
			<#if cinemaPage.contents?size gt 0>
            	<ul id="policyTree" class="ztree" layoutH="74"></ul>
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
	<div id="policyBox" class="tree-r-box"></div>
</div>
<script type="text/javascript">
    var policyNodes = [
      <#list cinemaPage.contents as cinema>
       {
           id : ${cinema_index + 1},
           cinemaId : "${cinema.id}",
           name : "[${cinema.county.city.name}".substring(0,3)+"]${cinema.name}",
           open : true,
           title : "",
           children : [
           <#list cinema.validPolicys as policy>
           {
               id : ${cinema_index + 1} * 10 + ${policy_index},
               polyId : "${policy.id}",
               box : "policyBox",
               url : "price/cinemaPolicy-view?policyId=${policy.id}",
               name : "${policy.name}",
               enabled : "${policy.enabled}" == "${EnabledStatus.DISABLED}" ? false : true,
               target : "ajax",
               tabindex : "${policy_index}",
               expired : "${policy.expired}",
               valid : "${policy.valid}" == "${ValidStatus.UNVALID}" ? false : true,
               status : "${policy.status}",
               title: "${policy.name}"
           },
           </#list>
           ]
       },
       </#list>
    ];
    var policySetting = {
        view : {
            addHoverDom : addPolicyExpandNode,
            removeHoverDom : removePolicyExpandNode,
            addDiyDom : addPolicyDIYNode,
            selectedMulti : false,
            showIcon : false
        },
        data : {
        	key: {
        		title : "title"
        	}
        }
    };
    
    function addPolicyDIYNode(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("policyId",treeNode.polyId);
        // 策略标记颜色。
        if(treeNode.level == 1) {
        	setPolicyTitleColor(treeNode, "#" + treeNode.tId + "_a");
        }
    }
    
    function addPolicyRemove(treeNode) {
    	// 如果策略处于待审核、待审批状态，不允许进行该操作。
        if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}") {
        	return "";
        }
     	// 如果策略未过期且已启用，不允许删除。
     	if (!treeNode.expired && treeNode.valid) {
    		return "";
    	}
     	if(treeNode.level != 1){
     		return "";
     	}
     	return "<a onclick=policyDelete('" + treeNode.polyId + "')>[删除]</a>";
    }
    
    function addPolicyEnabled(treeNode) {
    	// 如果策略未生效，不允许启用。
    	if (!treeNode.valid) {
    		return "";
    	}
    	if(treeNode.expired) {
    		return "";
    	}
    	if(treeNode.level != 1){
     		return "";
     	}
    	if(!treeNode.enabled) {
    		return "<a onclick=policyEnable('" + treeNode.polyId + "')>[启用]</a>";
        }
    	return "<a onclick=policyDisable('" + treeNode.polyId + "')>[停用]</a>";
    }
    
    function addPolicyEdit(treeNode) {
    	// 如果策略处于待审核、待审批状态，不允许进行该操作。
		if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}") {
        	return "";
        }
    	if(treeNode.level != 1){
     		return "";
     	}
    	if(treeNode.expired && treeNode.valid){
    		return "";
    	}
    	return "<a onclick=policyEdit('" + treeNode.polyId + "')>[编辑]</a>";
    }
   	
   	function policyEnable(policyId) {
   		alertMsg.confirm('启用策略将重新生成相关的渠道排期，您是否确定要启用该策略？', {
			okCall: function(){
				ajaxTodo('price/cinemaPolicy-enable?cinemaPolicy=' + policyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
   	function policyDisable(policyId) {
   		alertMsg.confirm('停用策略将作废相关的渠道排期，您是否确定要停用该策略？', {
			okCall: function(){
				ajaxTodo('price/cinemaPolicy-disable?cinemaPolicy=' + policyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
    function policyEdit(policyId) {
    	$.pdialog.open('price/cinemaPolicy-edit?policyId=' + policyId, "cinemaPolicy-edit", "编辑策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    function policyAdd(channelId) {
    	$.pdialog.open('price/cinemaPolicy-add?cinemaId=' + channelId, "policy-add", "新增策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    function policyCopy(channelId) {
    	$.pdialog.open('price/cinemaPolicy-copy-select?cinemaId=' + channelId, "cinemaPolicy-copy-select", "复制新增策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
   	function policyDelete(policyId) {
   		alertMsg.confirm('您是否确定要删除该策略？', {
			okCall: function(){
				ajaxTodo('price/cinemaPolicy-delete?policyId=' + policyId);
			}
		});
   	}
    
    function policyExpandNode(treeNode) {
        var addStr = "<span id='addPolicyExpandNode_" + treeNode.tId + "'>";
        <@sec.any name="POLICY_SWITCH">
        addStr += addPolicyEdit(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_MANAGE">
        addStr += addPolicyRemove(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_SWITCH">
        addStr += addPolicyEnabled(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_MANAGE">
        if(!treeNode.expired && treeNode.level == 1 ) {
        	if($("li#" + treeNode.tId).prev().length != 0) {
        		addStr += "<a id='policyUp_" + treeNode.id + "'>[上移]</a>";
        	}
        	if($("li#" + treeNode.tId).next().length != 0) {
        		addStr += "<a id='policyDown_" + treeNode.id + "'>[下移]</a>";
        	}
        }
        </@sec.any>
        addStr += "</span>";
        return addStr;
    }

    function removePolicyExpandNode(treeId, treeNode) {
        $("#addPolicyExpandNode_" + treeNode.tId).unbind().remove();
    };
    
    function addPolicyExpandNode(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addPolicyExpandNode_" + treeNode.tId).length > 0)
            return;
        if (treeNode.level == 0) {
        	var editStr = "<span id='addPolicyExpandNode_" + treeNode.tId + "'>";
            <@sec.any name="POLICY_MANAGE">
	            editStr += "<a onclick=policyAdd('" + treeNode.cinemaId + "')>[新增]</a>";
	            editStr += "<a onclick=policyCopy('" + treeNode.cinemaId + "')>[复制新增]</a>";
            </@sec.any>
            editStr += "</span>";
            sObj.after(editStr);
            return;
        }
        sObj.after(policyExpandNode(treeNode));
        $("#policyUp_" + treeNode.id).bind("click", function() {
        	moveNode(0, treeNode.tId);
            return false;
        });
        $("#policyDown_" + treeNode.id).bind("click", function() {
        	moveNode(1, treeNode.tId);
            return false;
        });
    };
    
    //type 1:下移    0:上移 
    function moveNode(type, curIndex){
    	var curLi = $("#" + curIndex);
    	var moveLi = (type==1) ? curLi.next() : curLi.prev();
    	if (type == 1) {
    		moveLi.after(curLi);
    	} else {
    		curLi.after(moveLi);
    	}
    	var policyId = curLi.find("a").attr("policyid");
    	var url = (type==1)?"price/cinemaPolicy-down":"price/cinemaPolicy-up";
    	$.post(url,{policyId:policyId},function(data){
    		curLi.find("a").first().click();
		});
    }
    
    function setPolicyTitleColor(node,id) {
    	if(node.level == 1){
	       	if(node.expired || (!node.enabled && node.valid)) {
	       		// 已过期或下架
	       		$(id).css("color","${StatusColor.GRAY}");
	       	} else {
	       		if(!node.valid) {
	       			// 未生效
	        		$(id).css("color","${StatusColor.RED}");
	        	} else {
	        		if(node.status == "${PolicyStatus.APPROVED}") {
	        			// 已生效且已审批
	        			$(id).css("color","${StatusColor.GREEN}");
	        		} else {
	        			// 已生效且待提交、待审核、待审批
	        			$(id).css("color","${StatusColor.ORANGE}");
	        		}
	        	}
	       	}
    	}
    }

    var cinemaPolicyTree;
    $(function() {
    	cinemaPolicyTree = $.fn.zTree.init($("#policyTree"), policySetting, policyNodes),flag =0;
    	$("a[policyId]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectedPolicyId]",$(this).getPageDiv()).val($(this).attr("policyId"));
    		});
    	});
    	var selectedPolicyLink = $("a[policyId]").eq(0);
    	if("${selectedPolicyId}" && $("a[policyId=${selectedPolicyId}]").size() > 0) {
    		selectedPolicyLink = $("a[policyId=${selectedPolicyId}]");
    	}
    	setTimeout(function() {
    		selectedPolicyLink.click();
        }, 10);
    	
    	$("#policyTree").find("li[class='level0']").each(function(i){
 	    	var liId = $(this).attr("id").split("_")[1];
 	    	if("${selectedPolicyId}" == "" || $("a[policyId=${selectedPolicyId}]").attr("id") == undefined){
 	    		if($(this).find("ul").length >0){
 	    			flag ++;
 	    		}
 	    		if(flag > 1){
 	    			$(this).find("#policyTree_"+ liId +"_switch").click();
 	    		}
 	    	}else{
 	    		var selectId = $("a[policyId=${selectedPolicyId}]").closest("ul").closest("li").attr("id").split("_")[1];
 	    		if(liId != selectId){
 	    			$(this).find("#policyTree_"+ liId +"_switch").click();
 	    		}
 	    	}
 	    });
    });	
</script>