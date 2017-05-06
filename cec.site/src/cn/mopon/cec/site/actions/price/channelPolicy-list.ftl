<div class="page">
	<div class="tree-l-box" layoutH="0" style="width:20%;">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/channelPolicy-list" buttonText="筛选" alt="可输入策略名称检索">
				<input type="hidden" name="selectedChannelPolicyId" value="${selectedChannelPolicyId}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="40">
            <#if channelChannelPolicyListModel.items?size gt 0>
            	<ul id="channelPolicyTree" class="ztree"></ul>
            <#else>
		    	<div class="tree-msg" layoutH="94">
		    		<h3>没有符合条件的策略。</h3>
		    	</div>
		    </#if>
        </div>
	</div>
	<div id="channelPolicyBox" class="tree-r-box" style="width:79%;"></div>
</div>
<script type="text/javascript">
	var channelPolicyNodes = [
	<#list channelChannelPolicyListModel.items as channelChannelPolicyModel>
	{
	    id : ${channelChannelPolicyModel_index + 1},
	    channelId : "${channelChannelPolicyModel.channel.id}",
	    name : "${channelChannelPolicyModel.channel.name}",
	    open : true,
	    title : "",
	    children : [
	        <#list channelChannelPolicyModel.items as channelPolicyModel>
	        {
	            id : ${channelChannelPolicyModel_index + 1} * 10 + ${channelPolicyModel_index},
	            polyId : "${channelPolicyModel.channelPolicy.id}",
	            box : "channelPolicyBox",
	            url : "price/channelPolicy-cinema-list?channelPolicyId=${channelPolicyModel.channelPolicy.id}",
	            name : "${channelPolicyModel.channelPolicy.name}"+"["+${channelPolicyModel.count}+"]",
	            enabled : "${channelPolicyModel.channelPolicy.enabled}",
	            target : "ajax",
	            tabindex : "${channelPolicyModel_index}",
	            expired : "${channelPolicyModel.channelPolicy.expired}",
	            valid : "${channelPolicyModel.channelPolicy.valid}",
	            open : true,
	            status : "${channelPolicyModel.channelPolicy.status}",
	            title: "${channelPolicyModel.channelPolicy.name}"
	        },
	        </#list>
	    ]
	},
	</#list>
	];
 
    var channelPolicySetting = {
    	view : {
			addHoverDom : addChannelPolicyExpandNode,
			removeHoverDom : removeChannelPolicyExpandNode,
			addDiyDom : addChannelPolicyDIYNode,
			selectedMulti : false,
			showIcon : false
		},
		data : {
			key: {
				title : "title"
			}
		}
    };
    
    function addChannelPolicyDIYNode(treeId, treeNode) {
        if (treeNode.parentNode && treeNode.parentNode.id != 2){
            return;
        }
        var aObj = $("#" + treeNode.tId + "_a");
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("channelPolicyId",treeNode.polyId);
        if(treeNode.level > 1) {
        	$("#" + treeNode.tId + "_a").attr("cinemaId",treeNode.cinemaId);
        }
        if(treeNode.level == 1){
        	setChannelPolicyTitleColor(treeNode, "#" + treeNode.tId + "_a");
        }
        if(treeNode.level == 2){
        	setChannelRuleGroupTitleColor(treeNode, "#" + treeNode.tId + "_a");
        }
    }
    
    function addChannelPolicyRemove(treeNode) {
    	// 如果策略处于待审核、待审批状态，不允许进行该操作。
        if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}") {
        	return "";
        }
        if(treeNode.level == 2){
			return "";
		}
     	// 如果策略未过期且已启用，不允许删除。
     	if (!treeNode.expired && treeNode.valid == "${ValidStatus.VALID}") {
    		return "";
    	}
     	return "<a onclick=channelPolicyDelete('" + treeNode.polyId + "')>[删除]</a>";
    }
    
    function addChannelPolicyEnabled(treeNode) {
    	// 如果策略未生效，不允许启用。
    	if (treeNode.valid == "${ValidStatus.UNVALID}") {
    		return "";
    	}
    	if(treeNode.expired) {
    		return "";
    	}
    	 if(treeNode.level == 2){
 			return "";
 		}
    	if(treeNode.enabled == "${EnabledStatus.DISABLED}") {
    		return "<a onclick=channelPolicyEnable('" + treeNode.polyId + "')>[启用]</a>";
        } else {
	    	return "<a onclick=channelPolicyDisable('" + treeNode.polyId + "')>[停用]</a>";
        }
    }
    
    function addChannelPolicyEdit(treeNode) {
    	// 如果策略处于待审核、待审批状态，不允许进行该操作。
		if(treeNode.status == "${PolicyStatus.AUDIT}" || treeNode.status == "${PolicyStatus.APPROVE}") {
        	return "";
        }
		if(treeNode.level == 2){
			return "";
		}
		if(treeNode.expired && treeNode.valid == "${ValidStatus.VALID}"){
    		return "";
    	}
    	return "<a onclick=channelPolicyEdit('" + treeNode.polyId + "')>[编辑]</a>";
    }
   	
   	function channelPolicyEnable(channelPolicyId) {
   		alertMsg.confirm('启用策略将重新生成相关的渠道排期，您是否确定要启用该策略？', {
			okCall: function(){
				ajaxTodo('price/channelPolicy-enable?channelPolicy=' + channelPolicyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
   	function channelPolicyDisable(channelPolicyId) {
   		alertMsg.confirm('停用策略将作废相关的渠道排期，您是否确定要停用该策略？', {
			okCall: function(){
				ajaxTodo('price/channelPolicy-disable?channelPolicy=' + channelPolicyId,function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == DWZ.statusCode.ok){
						navTab.reload(json.forwardUrl);
					}
				});
			}
		});
   	}
   	
    function channelPolicyEdit(channelPolicyId) {
    	$.pdialog.open('price/channelPolicy-edit?channelPolicyId=' + channelPolicyId, "channelPolicy-edit", "编辑策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    function channelPolicyAdd(channelId) {
    	$.pdialog.open('price/channelPolicy-add?channelId=' + channelId, "policy-add", "新增策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    function channelPolicyCopy(channelId) {
    	$.pdialog.open('price/channelPolicy-copy-select?channelId=' + channelId, "channelPolicy-copy-select", "复制新增策略",  {width:700,height:350,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
   	function channelPolicyDelete(channelPolicyId) {
   		alertMsg.confirm('您是否确定要删除该策略？', {
			okCall: function(){
				ajaxTodo('price/channelPolicy-delete?channelPolicyId=' + channelPolicyId);
			}
		});
   	}
    
    function channelPolicyExpandNodes(treeNode) {
    	if(treeNode.level > 2) {
    		return;
    	}
        var addStr = "<span id='addChannelPolicyExpandNode_" + treeNode.tId + "'>";
        <@sec.any name="POLICY_SWITCH">
        addStr += addChannelPolicyEdit(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_MANAGE">
        addStr += addChannelPolicyRemove(treeNode);
        </@sec.any>
        <@sec.any name="POLICY_SWITCH">
        addStr += addChannelPolicyEnabled(treeNode);
        </@sec.any>
        var selectIndex = Number(treeNode.tId.substring(treeNode.tId.indexOf("_") + 1));
        var nextNode = $("li[id="+treeNode.tId+"]").next();
        var prevNode = $("li[id="+treeNode.tId+"]").prev();
        <@sec.any name="POLICY_MANAGE">
        if(!treeNode.expired && treeNode.level == 1) {
            if(nextNode.html() != null && prevNode.html() != null) {
                addStr += "<a alias='moveUp' id='channelPolicyUp_" + treeNode.id + "'>[上移]</a><a alias='moveDown' id='channelPolicyDown_" + treeNode.id + "'>[下移]</a>";
            } else if(prevNode.html() != null) {
                addStr += "<a alias='moveUp' id='channelPolicyUp_" + treeNode.id + "'>[上移]</a>";
            } else if(nextNode.html() != null) {
                addStr += "<a alias='moveDown' id='channelPolicyDown_" + treeNode.id + "'>[下移]</a>";
            }
        }
        </@sec.any>
        addStr += "</span>";
        return addStr;
    }

    function removeChannelPolicyExpandNode(treeId, treeNode) {
        $("#addChannelPolicyExpandNode_" + treeNode.tId).unbind().remove();
    };
    
    function addChannelPolicyExpandNode(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addChannelPolicyExpandNode_" + treeNode.tId).length > 0)
            return;
        if (treeNode.level == 0) {
        	var editStr = "<span id='addChannelPolicyExpandNode_" + treeNode.tId + "'>";
            <@sec.any name="POLICY_MANAGE">
	            editStr += "<a onclick=channelPolicyAdd('" + treeNode.channelId + "')>[新增]</a>";
	            editStr += "<a onclick=channelPolicyCopy('" + treeNode.channelId + "')>[复制新增]</a>";
            </@sec.any>
            editStr += "</span>";
            sObj.after(editStr);
            return;
        }
        sObj.after(channelPolicyExpandNodes(treeNode));
        var index = Number(treeNode.tId.substring(treeNode.tId.indexOf("_") + 1));
        $("#channelPolicyUp_" + treeNode.id).bind("click", function() {
            if(channelPolicyTree.getNodeIndex(channelPolicyTree.getNodeByTId(treeNode.tId)) > 0) {
                var selectIndex = index - 1;
                channelPolicyTree.selectNode(channelPolicyTree.getNodeByTId("channelPolicyTree_" + selectIndex),false);
            }
            moveChannelPolicy(0,index);
            removeChannelPolicyExpandNode(treeNode.tId, treeNode);
            return false;
        });
        $("#channelPolicyDown_" + treeNode.id).bind("click", function() {
            var selectIndex = index + 1;
            var node = channelPolicyTree.getNodeByTId("channelPolicyTree_" + selectIndex);
            if(!node.isParent) {
                channelPolicyTree.selectNode(channelPolicyTree.getNodeByTId("channelPolicyTree_" + selectIndex),false);
            }
            moveChannelPolicy(1,index);
            removeChannelPolicyExpandNode(treeNode.tId, treeNode);
            return false;
        });
    };
    
  	//type 1:下移    0:上移 
    function moveChannelPolicy(type,index){
    	var curLi = $("#channelPolicyTree_"+index);
    	var channelPolicyId = curLi.find("a").attr("channelpolicyid");
    	var moveLi = (type==1)?curLi.next():curLi.prev();
    	if (type == 1) {
    		moveLi.after(curLi);
    	} else {
    		curLi.after(moveLi);
    	}
    	var url = (type==1)?"price/channelPolicy-down":"price/channelPolicy-up";
    	$.post(url,{channelPolicyId:channelPolicyId},function(data){
    		curLi.find("a").first().click();
		});
    }
    
    function setChannelPolicyTitleColor(node,id) {
       	if(node.expired || (node.enabled =="${EnabledStatus.DISABLED}"  && node.valid == "${ValidStatus.VALID}")) {
       		// 已过期或下架
       		$(id).css("color","${StatusColor.GRAY}");
       	} else {
       		if(node.valid == "${ValidStatus.UNVALID}") {
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
    
    function setChannelRuleGroupTitleColor(node,id) {
    	$(id).css("color",node.ruleGroupColor);
    }

    var channelPolicyTree;
    $(function() {
    	channelPolicyTree = $.fn.zTree.init($("#channelPolicyTree"), channelPolicySetting, channelPolicyNodes),flag=0;
    	$("a[channelpolicyid]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectedChannelPolicyId]",$(this).getPageDiv()).val($(this).attr("channelpolicyid"));
    		});
    	});
    	
    	var selectedPolicyLink = $("a[channelpolicyid]").eq(0);
    	if("${selectedChannelPolicyId}" && $("a[channelpolicyid=${selectedChannelPolicyId}]").size() > 0) {
    		selectedPolicyLink = $("a[channelpolicyid=${selectedChannelPolicyId}]");
    	}
    	setTimeout(function() {
    		selectedPolicyLink.click();
        }, 10);
    	
    	$("#channelPolicyTree").find("li[class='level1']").each(function(i){
  	    	var liId = $(this).attr("id").split("_")[1];
  	    	if("${selectedChannelPolicyId}" == "" || $("a[channelpolicyid=${selectedChannelPolicyId}]").attr("id") == undefined){
  	    		if($(this).find("ul").length >0){
	    			flag ++;
	    		}
	    		if(flag > 0){
	    			$(this).find("#channelPolicyTree_"+ liId +"_switch").trigger("click");
	    		}
  	    	}else{
  	    		var selectId = $("a[channelpolicyid=${selectedChannelPolicyId}]").closest("ul").closest("li").attr("id").split("_")[1];
  	    		if(liId != selectId){
  	    			$(this).find("#channelPolicyTree_"+ liId +"_switch").trigger("click");
  	    		}
  	    	}
  	    });
    });	
</script>