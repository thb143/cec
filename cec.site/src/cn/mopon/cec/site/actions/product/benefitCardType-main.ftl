<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@dwz.pageForm action="/product/benefitCardType-main" alt="可输入卡类名称、卡类编号、卡类前缀检索">
				 <input type="hidden" name="benefitCardTypeId" value="${benefitCardTypeId}" />
		    </@dwz.pageForm>
		</div>
		<div class="pageContent" layoutH="37">
			<#if benefitCardTypes?size gt 0>
				<ul id="benefitCardTypeTree" class="ztree" layoutH="84"></ul>
			<#else>
				<div class="tree-msg" layoutH="94">
					<h3>没有符合条件的权益卡类。</h3>
				</div>
			</#if> 
			<@sec.any name="PRODUCT_MANAGE"> 
				<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
					<li>
						<@dwz.a href="/product/benefitCardType-add" title="新增卡类" height="S" target="dialog" class="button"><span>新增卡类</span></@dwz.a>
					</li> 
				</@dwz.formBar>
			</@sec.any>
		</div>
	</div>
	<div id="benefitCardTypeRuleBox" class="tree-r-box"></div>
</div>
<script type="text/javascript">
	$(function(){
    	  var benefitCardTypeNodes = [
             <#list benefitCardTypes as benefitCardType>
             	  	{
	                  id : "${benefitCardType_index+1}",
	                  cardId :"${benefitCardType.id}",
	                  name: "${benefitCardType.name}",
	                  title : "${benefitCardType.name}",
	                  box:"benefitCardTypeRuleBox",
	                  url:"product/benefitCardType-view?id=${benefitCardType.id}",
	                  enabled:"${benefitCardType.enabled}"=="${EnabledStatus.DISABLED}" ? false : true,
	                  valid : "${benefitCardType.valid}" == "${ValidStatus.UNVALID}" ? false : true,
	                  status : "${benefitCardType.status}",
	                  boundId:"${benefitCardType.boundType.id}",		  
	                  commonUrl:"benefitCardType",
	                  open : true,
	                  target : "ajax",
	                  tabindex : "${benefitCardType_index}",
	                  children : [
	                     {
                            id : ${benefitCardType_index + 1} * 10 + 1,
                            url : "product/benefitCardType-rule-view?id=${benefitCardType.id}",
                            name : "票务规则",
                            target : "ajax",
                            box : "benefitCardTypeRuleBox",
                            tabindex : ${benefitCardType_index + 1} * 10 + 1,
                            title: ""
                        },{
                       	 id : ${benefitCardType_index + 1} * 10 + 2,
                            url : "product/benefitCardType-snackRule-view?id=${benefitCardType.id}",
                            name : "卖品规则",
                            target : "ajax",
                            box : "benefitCardTypeRuleBox",
                            tabindex : ${benefitCardType_index + 1} * 10 + 2,
                            title: ""
                        }
                       ]
	               },
              </#list>
          ];  
    	  var benefitCardTypeSetting = {
		        view : {
		        	addHoverDom : addBenefitCardTypeExpandNode,
		            removeHoverDom : removeBenefitCardTypeExpandNode,
		            addDiyDom : addBenefitCardTypeDIYNode,
		        	selectedMulti : false,
		            showIcon : false
		        },
		        data : {
		        	key: {
		        		title : "title"
		        	}
		        }
		    };
    	
    	  benefitCardTypeTree = $.fn.zTree.init($("#benefitCardTypeTree"), benefitCardTypeSetting, benefitCardTypeNodes);
    	  
    	  $("a[cardId]").each(function(){
	    		$(this).click(function() {
	    			$("input[name=benefitCardTypeId]",$(this).getPageDiv()).val($(this).attr("cardId"));
	    		});
	    	});
    	  
    	  var selectedCardIdLink = $("a[cardId]").eq(0);
          if("${benefitCardTypeId}" && $("a[cardId=${benefitCardTypeId}]").size() > 0) {
        	  selectedCardIdLink = $("a[cardId=${benefitCardTypeId}]");
      	  }
      	  setTimeout(function() {
      		selectedCardIdLink.click();
          }, 300);
      });

	function addBenefitCardTypeExpandNode(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.level > 0) {
			return;
		} 
		if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0){
	    	 return;
	    }
	    sObj.after(benefitCardTypeExpandNode(treeNode));
	}
	function removeBenefitCardTypeExpandNode(treeId, treeNode) {
	    $("#addBtn_" + treeNode.tId).unbind().remove();
	}
	function addBenefitCardTypeDIYNode(treeId, treeNode) {
	    if (treeNode.parentNode && treeNode.parentNode.id != 2)
	        return;
	    var aObj = $("#" + treeNode.tId + "_a");
	    $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
	    $("#" + treeNode.tId + "_a").attr("cardId",treeNode.cardId);
	    if(treeNode.level == 0) {
	    	setBenefitCardTypeTitleColor(treeNode, "#" + treeNode.tId + "_a");
	    } 
	}
	function benefitCardTypeExpandNode(treeNode) {  
	    var addStr = "<span id='addBtn_" + treeNode.tId + "'>";
	     <@sec.any name="PRODUCT_MANAGE">
	        addStr += addBenefitCardTypeEdit(treeNode);
	    </@sec.any>
	    <@sec.any name="PRODUCT_SWITCH">
	    	addStr += addBenefitCardTypeEnabled(treeNode);
	    </@sec.any>
	    <@sec.any name="PRODUCT_MANAGE">
			//addStr += addSpecialPolicyCopy(treeNode);
		addStr += addBenefitCardTypeRemove(treeNode);
	    </@sec.any>
	    addStr += "</span>";
	    return addStr;
	}
	
	function addBenefitCardTypeEdit(treeNode) {
		// 如果卡类处于待审核、待审批状态，不允许进行该操作。
		if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}") {
	    	return "";
	    }
		return "<a onclick=benefitCardTypeEdit('" + treeNode.cardId + "','" + treeNode.commonUrl + "')>[编辑]</a>";
	}
	
	function benefitCardTypeEdit(policyId, commonUrl) {
		$.pdialog.open('product/' + commonUrl + '-edit?id=' + policyId, "benefitCardType-edit", "编辑卡类",  {width:700, height:400,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
	}
	
	function setBenefitCardTypeTitleColor(node,id) {
	  	if(!node.valid) {
	    	$(id).css("color","${StatusColor.RED}");
	    	return;
	    }
	  	if(node.valid && node.status == "${PolicyStatus.APPROVED}" && node.enabled) {
	  		$(id).css("color","${StatusColor.GREEN}");
	  		return;
	  	}
	  	if(node.valid && !node.enabled) {
	  		$(id).css("color","${StatusColor.GRAY}");
	  		return;
	  	}
	  	$(id).css("color","${StatusColor.ORANGE}");
	}
	
	function addBenefitCardTypeRemove(treeNode) {
		// 如果卡类处于待审核、待审批、审批通过状态，不允许进行该操作。
	    if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}" || treeNode.status == "${PolicyStatus.APPROVED}") {
	    	return "";
	    }
	    if(!treeNode.boundId) {
	    	return "";
	    }
	  	return "<a onclick=benefitCardTypeDelete('" + treeNode.cardId + "')>[删除]</a>";
	}
	
	function addBenefitCardTypeEnabled(treeNode) {
		// 如果策略未生效，不允许启用。
	 	if (!treeNode.valid) {
	 		return "";
	 	}
		if(!treeNode.enabled) {
			return "<a onclick=benefitCardTypeEnable('" + treeNode.cardId + "')>[启用]</a>";
	    }
		return "<a onclick=benefitCardTypeDisable('" + treeNode.cardId + "')>[停用]</a>";
	}
	
	function benefitCardTypeEnable(cardId) {
			alertMsg.confirm('启用卡类将重新生成相关的渠道排期，您是否确认要启用该卡类?', {
			okCall: function(){
				ajaxTodo('product/benefitCardType-enable?id=' + cardId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
	}
	
	function benefitCardTypeDisable(cardId) {
		alertMsg.confirm('您是否确认要停用该卡类?', {
			okCall: function(){
				ajaxTodo('product/benefitCardType-disabled?id=' + cardId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
	}
	
	function benefitCardTypeDelete(cardId){
		alertMsg.confirm('您是否确认要删除该卡类?', {
			okCall: function(){
				ajaxTodo('product/benefitCardType-delete?id=' + cardId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
	}
</script>