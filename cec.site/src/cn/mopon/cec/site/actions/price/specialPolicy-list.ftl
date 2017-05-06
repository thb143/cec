<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
        	<@dwz.pageForm action="/price/specialPolicy-list" buttonText="筛选" alt="可输入特殊定价策略名称检索">
        		<input type="hidden" name="selectedSpecialPolicyId" value="${selectedSpecialPolicyId}" />
        	</@dwz.pageForm>
    	</div>
       	<div class="pageContent" layoutH="37">
       		<#if specialPolicys.items?size gt 0>
	       		<ul id="specialPolicyTree" class="ztree" layoutH="84"></ul>
	       	<#else>
		    	<div class="tree-msg" layoutH="94">
		    		<h3>没有符合条件的特殊定价策略。</h3>
		    	</div>
		    </#if>
       		<@sec.any name="POLICY_MANAGE">
	       		<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    			<li><@dwz.a href="/price/specialPolicy-type-select.html" width="SS" height="SSS" title="选择特殊定价类型" target="dialog" class="button"><span>新增</span></@dwz.a></li>
		   		</@dwz.formBar>
	   		</@sec.any>
       	</div>
	</div>
	<div id="specialPolicyBox" class="tree-r-box"></div>
</div>
<script>
	var specialPolicyNodes = [
	<#list specialPolicys.items as specialPolicy>
    {
        id : ${specialPolicy_index + 1},
        name : "${specialPolicy.year}",
        open : true,
        title : "${specialPolicy.year}",
        children : [
        <#list specialPolicy.policys as policy>
        {
            id : ${specialPolicy_index + 1} * 10 + ${policy_index},
            polyId : "${policy.id}",
            box : "specialPolicyBox",
            commonUrl : "specialPolicy",
            url : "price/specialRule-view?policyId=${policy.id}",
            name : "${policy.name}",
            target : "ajax",
            tabindex : "${policy_index}",
            expired : "${policy.expired}",
            enabled : "${policy.enabled}" == "${EnabledStatus.DISABLED}" ? false : true,
            valid : "${policy.valid}" == "${ValidStatus.UNVALID}" ? false : true,
            status : "${policy.status}",
            title : "${policy.name}",
            isCanEnabled: "${policy.isCanEnabled()}",
            ruleSize : "${policy.rules?size}"
        },
        </#list>
        ]
    },
	</#list>
    ];
  
    var specialPolicySetting = {
        view : {
            addHoverDom : addSpecialPolicyExpandNode,
            removeHoverDom : removeSpecialPolicyExpandNode,
            addDiyDom : addSpecialPolicyDIYNode,
            selectedMulti : false,
            showIcon: false
        },
        data : {
        	key: {
        		title : "title"
        	}
        }
    };
   
    function addSpecialPolicyDIYNode(treeId, treeNode) {
        if (treeNode.parentNode && treeNode.parentNode.id != 2)
            return;
        var aObj = $("#" + treeNode.tId + "_a");
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("specialPolicyId",treeNode.polyId);
        if(!treeNode.isParent) {
        	setSpecialPolicyTitleColor(treeNode, "#" + treeNode.tId + "_a");
        }
    }
    
    function addSpecialPolicyRemove(treeNode) {
    	// 如果特殊定价策略处于待审核、待审批状态，不允许进行该操作。
        if(treeNode.status == "${SpecialPolicyStatus.AUDIT}" || treeNode.status == "${SpecialPolicyStatus.APPROVE}") {
        	return "";
        }
     	// 如果特殊定价策略未过期且已启用，不允许删除。
     	if (!treeNode.expired && treeNode.valid) {
    		return "";
    	}
     	return "<a onclick=specialPolicyDelete('" + treeNode.polyId + "')>[删除]</a>";
    }
    
    function addSpecialPolicyEdit(treeNode) {
    	// 如果特殊定价策略处于待审核、待审批状态，不允许进行该操作。
		if(treeNode.status == "${SpecialPolicyStatus.AUDIT}" || treeNode.status == "${SpecialPolicyStatus.APPROVE}") {
        	return "";
        }
    	return "<a onclick=specialPolicyEdit('" + treeNode.polyId + "','" + treeNode.commonUrl + "')>[编辑]</a>";
    }
    
    function addSpecialPolicyCopy(treeNode) {
    	return "<a onclick=specialPolicyCopy('" + treeNode.polyId + "')>[复制]</a>";
    }
   	
    function specialPolicyEdit(policyId, commonUrl) {
    	$.pdialog.open('price/' + commonUrl + '-edit?policyId=' + policyId, "specialPolicy-edit", "编辑",  {width:700, height:500,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
   	function specialPolicyDelete(policyId) {
   		alertMsg.confirm('您是否确定要删除该特殊定价策略？', {
			okCall: function(){
				ajaxTodo('price/specialPolicy-delete?policyId=' + policyId);
			}
		});
   	}
   	
   	function specialPolicyCopy(policyId) {
   		alertMsg.confirm('您是否确定要复制该特殊定价策略？', {
			okCall: function(){
				ajaxTodo('price/specialPolicy-copy?policyId=' + policyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
    function specialPolicyEnable(policyId) {
   		alertMsg.confirm('启用特殊定价策略将重新生成相关的渠道排期，您是否确定要启用该特殊定价策略？', {
			okCall: function(){
				ajaxTodo('price/specialPolicy-enable?policyId=' + policyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
   	function specialPolicyDisable(policyId) {
   		alertMsg.confirm('停用特殊定价策略会将作废相关的渠道排期，您是否确定要停用该特殊定价策略？', {
			okCall: function(){
				ajaxTodo('price/specialPolicy-disable?policyId=' + policyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
    
    function addSpecialPolicyEnabled(treeNode) {
    	// 如果特殊定价策略未生效，不允许启用。
    	if (!treeNode.valid || treeNode.isCanEnabled == "") {
    		return "";
    	}
    	if(treeNode.expired) {
    		return "";
    	}
    	if(!treeNode.enabled) {
    		return "<a onclick=specialPolicyEnable('" + treeNode.polyId + "')>[启用]</a>";
        }
    	return "<a onclick=specialPolicyDisable('" + treeNode.polyId + "')>[停用]</a>";
    }
    
    function specialPolicyExpandNode(treeNode) {  
        var addStr = "<span id='addBtn_" + treeNode.tId + "'>";
        <@sec.any name="POLICY_SWITCH">
	        addStr += addSpecialPolicyEdit(treeNode);
	    </@sec.any>
        <@sec.any name="POLICY_MANAGE">
       		addStr += addSpecialPolicyCopy(treeNode);
        	addStr += addSpecialPolicyRemove(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_SWITCH">
        	addStr += addSpecialPolicyEnabled(treeNode);
        </@sec.any>
        var selectIndex = Number(treeNode.tId.substring(treeNode.tId.indexOf("_") + 1));
        var nextNode = $("li[id=" + treeNode.tId + "]").parent().find("li[id=policyTree_" + (selectIndex + 1)+ "]");
        var prevNode = $("li[id=" + treeNode.tId + "]").parent().find("li[id=policyTree_" + (selectIndex - 1)+ "]");
        <@sec.any name="POLICY_MANAGE">
	        if(!treeNode.expired) {
	        	if($("li#" + treeNode.tId).prev().length != 0) {
	        		addStr += "<a id='specialPolicyUp_" + treeNode.id + "'>[上移]</a>";
	        	}
	        	if($("li#" + treeNode.tId).next().length != 0) {
	        		addStr += "<a id='specialPolicyDown_" + treeNode.id + "'>[下移]</a>";
	        	}
	        }
        </@sec.any>
        addStr += "</span>";
        return addStr;
    }

    function addSpecialPolicyExpandNode(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0)
            return;
        if (treeNode.id < specialPolicyNodes.length + 1) {
            return;
        }
        sObj.after(specialPolicyExpandNode(treeNode));
        $("#specialPolicyUp_" + treeNode.id).bind("click", function() {
           	specialPolicyMoveNode(0,treeNode.tId);
            return false;
        });
        $("#specialPolicyDown_" + treeNode.id).bind("click", function() {
        	specialPolicyMoveNode(1,treeNode.tId);
            return false;
        });
    };
    
  	//type 1:下移    0:上移 
	function specialPolicyMoveNode(type,curIndex){
		var curLi = $("#" + curIndex);
    	var moveLi = (type==1) ? curLi.next() : curLi.prev();
    	if (type == 1) {
    		moveLi.after(curLi);
    	} else {
    		curLi.after(moveLi);
    	}
    	var policyId = curLi.find("a").attr("specialPolicyId");
    	var url = (type==1) ? "price/specialPolicy-down" : "price/specialPolicy-up";
    	$.post(url,{policyId : policyId},function(data){
    		curLi.find("a").first().click();
		});
    }
	
    function setSpecialPolicyTitleColor(node,id) {
       	if(node.expired) {
       		$(id).css("color","${StatusColor.GRAY}");
       		return;
       	}
      	if(!node.valid) {
        	$(id).css("color","${StatusColor.RED}");
        	return;
        }
      	if(node.valid && node.status == "${SpecialPolicyStatus.APPROVED}" && node.enabled) {
      		$(id).css("color","${StatusColor.GREEN}");
      		return;
      	}
      	if(node.valid && !node.enabled) {
      		$(id).css("color","${StatusColor.GRAY}");
      		return;
      	}
      	$(id).css("color","${StatusColor.ORANGE}");
    }
  
    function removeSpecialPolicyExpandNode(treeId, treeNode) {
        $("#addBtn_" + treeNode.tId).unbind().remove();
    };

    var specialPolicyTree;
    $(document).ready(function() {
    	specialPolicyTree = $.fn.zTree.init($("#specialPolicyTree"), specialPolicySetting, specialPolicyNodes);
        $("a[specialPolicyId]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectedSpecialPolicyId]",$(this).getPageDiv()).val($(this).attr("specialPolicyId"));
    		});
    	});
    	var selectedSpecialPolicyIdLink = $("a[specialPolicyId]").eq(0);
    	if("${selectedSpecialPolicyId}" && $("a[specialPolicyId=${selectedSpecialPolicyId}]").size() > 0) {
    		selectedSpecialPolicyIdLink = $("a[specialPolicyId=${selectedSpecialPolicyId}]");
    	}
    	setTimeout(function() {
    		selectedSpecialPolicyIdLink.click();
        }, 300);
    });
</script>