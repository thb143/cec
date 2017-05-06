<style>
	.tree-l-box .pageHeader .formBar{
		height:0px; width:80px;float:left;padding-top: 0px;border-width: 0px 0 0 0;padding: 0 0px;
	}
</style>
<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@sec.any name="POLICY_MANAGE">
				<#if channelPolicy.status != PolicyStatus.AUDIT && channelPolicy.status != PolicyStatus.APPROVE && !channelPolicy.expired >
					<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
			    		<li><@dwz.a href="/price/channelPolicy-cinema-select?channelPolicyId=${channelPolicy.id}" target="dialog" class="button" height="S"><span>添加影院</span></@dwz.a></li>
			   		</@dwz.formBar>
				</#if>
			</@sec.any>
			<@dwz.pageForm action="/price/channelPolicy-cinema-list" targetType="div" rel="channelPolicyBox" buttonText="筛选" alt="可输入影院名称、行政区划检索" >
				<input type="hidden" name="channelPolicyId" value="${channelPolicy.id}" />
				<input type="hidden" name="selectGroupId" value="${selectGroupId}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="37">
			<#if groupPage.contents?size gt 0>
				<ul id="groupTree" class="ztree" layoutH="74"></ul>
			    <div class="panelBar">
		            <@sys.pageNav pageModel=groupPage rel="channelPolicyBox"/>
		        </div>
		    <#else>
		    	<div class="tree-msg" layoutH="84">
		    		<h3>没有符合条件的影院。</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="groupBox" class="tree-r-box"></div>
</div>
<script>
	var groupNodes = [
	<#list groupPage.contents as group>
 	{
 		id : ${group_index + 1},
        policyId : "${group.policy.id}",
        groupId : "${group.id}",
	    cinemaId : "${group.cinema.id}",
        box : "groupBox",
        url : "price/channelPolicy-view?channelPolicyId=${group.policy.id}&cinemaId=${group.cinema.id}",
        name : "["+"${group.cinema.county.city.name?substring(0,2)}"+"]"+"${group.cinema.name}",
        target : "ajax",
        title: "${group.cinema.name}",
        status: "${group.status}",
        open: true,
        ruleSize: "${group.rules?size}",
        validAndEnabledRules: "${group.validAndEnabledRules}",
        policyStatus: "${group.policy.status}",
        policyExpired: "${group.policy.expired}",
        policyValid: "${group.policy.valid}",
        policyEnabled: "${group.policy.enabled}",
        ruleGroupColor : "${group.ruleGroupColor}"
 	},
 	</#list>
 	];
  
     var groupSetting = {
     	view : {
 			addHoverDom : addGroupExpandNode,
 			removeHoverDom : removeGroupExpandNode,
 			addDiyDom : addGroupDIYNode,
 			selectedMulti : false,
 			showIcon : false
 		},
 		data : {
 			key: {
 				title : "title"
 			}
 		}
     };
     
     function addGroupDIYNode(treeId, treeNode) {
         var aObj = $("#" + treeNode.tId + "_a");
         $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
         $("#" + treeNode.tId + "_a").attr("groupId",treeNode.groupId);
         if(treeNode.level == 0){
        	 setChannelRuleGroupTitleColor(treeNode, "#" + treeNode.tId + "_a");
         }
     }
     
     function addGroupRemove(treeNode) {
         if(treeNode.policyStatus != "${PolicyStatus.AUDIT}" && treeNode.policyStatus != "${PolicyStatus.APPROVE}" && !treeNode.policyExpired
        		 && (treeNode.policyValid == "${ValidStatus.UNVALID}" || treeNode.ruleSize == 0)) {
        	 return "<a onclick=groupDelete('" + treeNode.groupId + "')>[删除]</a>";
         }
         return "";
     }
     
     function addGroupConnect(treeNode) {
    	 if(treeNode.policyStatus != "${PolicyStatus.AUDIT}" && treeNode.policyStatus != "${PolicyStatus.APPROVE}" && !treeNode.policyExpired) {
         	return "<a onclick=groupEdit('" + treeNode.groupId + "')>[设置<@s.message code='connectFee'/>]</a>";
         }
     	 return "";
     }
     
     function addGroupCopy(treeNode) {
	 	 if(treeNode.policyStatus != "${PolicyStatus.AUDIT}" && treeNode.policyStatus != "${PolicyStatus.APPROVE}" && !treeNode.policyExpired
	   		 && "${groupCount}" > 1 && treeNode.ruleSize > 0) {
         	return "<a onclick=groupCopy('" + treeNode.groupId + "','" + treeNode.policyId + "')>[复制到影院]</a>";
         }
     	 return "";
     }
     
     function addGroupShow(treeNode) {
	 	 if(treeNode.policyStatus != "${PolicyStatus.AUDIT}" && treeNode.policyStatus != "${PolicyStatus.APPROVE}" && !treeNode.policyExpired
	   		 && treeNode.policyValid == "${ValidStatus.VALID}" && treeNode.policyEnabled == "${EnabledStatus.ENABLED}"
	   		 && treeNode.status == "${OpenStatus.OPENED}" && treeNode.validAndEnabledRules) {
         	return "<a onclick=groupShow('" + treeNode.groupId + "')>[排期检索]</a>";
         }
     	 return "";
     }
     
     function addGroupOpen(treeNode) {
	 	 if(treeNode.policyStatus != "${PolicyStatus.AUDIT}" && treeNode.policyStatus != "${PolicyStatus.APPROVE}" && !treeNode.policyExpired
	   		 && treeNode.policyValid == "${ValidStatus.VALID}") {
	 		 if(treeNode.status == "${OpenStatus.OPENED}"){
	 			return "<a onclick=groupClose('" + treeNode.groupId + "')>[关闭]</a>";
	 		 }else{
	 			return "<a onclick=groupOpen('" + treeNode.groupId + "')>[开放]</a>";
	 		 }
         }
     	 return "";
     }
     
     function groupDelete(groupId) {
 		alertMsg.confirm('您是否确定要删除该影院？', {
			okCall: function(){
				ajaxTodo('price/channelRuleGroup-delete?groupId=' + groupId);
			}
		});
 	 }
     
     function groupOpen(groupId) {
  		alertMsg.confirm('开放影院将重新生成相关的渠道排期，您是否确定要开放该影院？', {
 			okCall: function(){
 				ajaxTodo('price/channelRuleGroup-open?channelRuleGroup=' + groupId);
 			}
 		});
  	 }
     
     function groupClose(groupId) {
   		alertMsg.confirm('关闭影院会将作废相关的渠道排期，您是否确定要关闭该影院？', {
  			okCall: function(){
  				ajaxTodo('price/channelRuleGroup-close?channelRuleGroup=' + groupId);
  			}
  		});
   	 }
     
	 function groupShow(groupId) {
     	$.pdialog.open('product/channelRuleGroup-show-list?channelRuleGroup=' + groupId, "channelRuleGroup-show-list", "排期检索",  {width:900,height:600,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
     }
     
     function groupEdit(groupId) {
     	$.pdialog.open('price/channelRuleGroup-edit?channelRuleGroup=' + groupId, "channelRuleGroup-edit", "设置<@s.message code='connectFee'/>",  {width:400,height:250,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
     }
     
     function groupCopy(groupId,policyId) {
     	$.pdialog.open('price/channelRuleGroup-copy-select?groupId=' + groupId + '&policyId=' + policyId, "channelRuleGroup-copy-select", "复制到影院",  {width:700,height:400,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
     }
     
     function removeGroupExpandNode(treeId, treeNode) {
         $("#addGroupExpandNode_" + treeNode.tId).unbind().remove();
     };
     
     function addGroupExpandNode(treeId, treeNode) {
         var sObj = $("#" + treeNode.tId + "_span");
         if (treeNode.editNameFlag || $("#addGroupExpandNode_" + treeNode.tId).length > 0)
             return;
         if (treeNode.level == 0) {
         	var editStr = "<span id='addGroupExpandNode_" + treeNode.tId + "'>";
             <@sec.any name="POLICY_MANAGE">
	             editStr += addGroupConnect(treeNode);
	             editStr += addGroupRemove(treeNode);
	             editStr += addGroupCopy(treeNode);
             </@sec.any>
             <@sec.any name="POLICY_SWITCH">
             	editStr += addGroupOpen(treeNode);
	         </@sec.any>
	         <@sec.any name="PRODUCT_MANAGE">
	         	editStr += addGroupShow(treeNode);
	         </@sec.any>
             editStr += "</span>";
             sObj.after(editStr);
             return;
         }
     };
     
     function setChannelRuleGroupTitleColor(node,id) {
     	$(id).css("color",node.ruleGroupColor);
     }

     var groupTree;
     $(function() {
     	groupTree = $.fn.zTree.init($("#groupTree"), groupSetting, groupNodes),flag=0;
	    $("#groupTree").find("li[class='level0']").each(function(i){
	    	var liId = $(this).attr("id").split("_")[1];
	    	if("${selectGroupId}" == "" || $("a[groupId=${selectGroupId}]").attr("id") == undefined){
	    		if($(this).find("ul").length >0){
	    			flag ++;
	    		}
	    		if(flag > 1){
	    			$(this).find("#groupTree_"+ liId +"_switch").click();
	    		}
	    	}else{
	    		var selectId = $("a[groupId=${selectGroupId}]").attr("id").split("_")[1];
	    		if(liId != selectId){
	    			$(this).find("#groupTree_"+ liId +"_switch").click();
	    		}
	    	}
	    });

    	$("a[groupId]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectGroupId]",$(this).getPageDiv()).val($(this).attr("groupId"));
    		});
    	});
    	var selectedGroupLink = $("a[groupId]").eq(0);
    	if("${selectGroupId}" && $("a[groupId=${selectGroupId}]").size() > 0) {
    		selectedGroupLink = $("a[groupId=${selectGroupId}]");
    	}
    	
    	setTimeout(function() {
    		selectedGroupLink.click();
        }, 300);
     });	
</script>