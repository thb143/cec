<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageContent" layoutH="36">
			<input type="hidden" name="selectedId" value="${selectedId}" />
            <#if snackGroups?size gt 0>
            	<ul id="snackGroupTree" class="ztree"></ul>
	        <#else>
		        <div class="tree-msg">
		    		<h3>没有卖品分类。</h3>
		    	</div>
	    	</#if>
        </div>
        <@sec.any name="PRODUCT_MANAGE">
	       		<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    			 <li><@dwz.a href="/product/snackGroup-add" target="dialog"  title="新增卖品分类" height="SSS" width="S" class="button"><span>新增卖品分类</span></@dwz.a></li>
		   		</@dwz.formBar>
		</@sec.any>
	</div>
	<div id=snackGroupBox class="tree-r-box"></div>
</div>
<script>
	var snackGroupNodes = 
		{name:"卖品分类",
		 open:true,
		 children : [
             <#list snackGroups as snackGroup>
             {
                 id : "${snackGroup_index + 1}",
                 snackGroup : "${snackGroup.id}",
                 name : "${snackGroup.name}",
                 title : "",
                 open : true,
                 url : "product/snackType-view?groupId=${snackGroup.id}",
                 target : "ajax",
                 box : "snackGroupBox"
             },
             </#list>
        ]
		};
	
	var snackGroupSetting = {
		view : {
			addHoverDom : addSnackGroupExpandNode,
			removeHoverDom : removeSnackGroupExpandNode,
			addDiyDom : addSnackGroupDIYNode,
		    showIcon : false
		},
		data : {
			key: {
				title : "title"
			}
		}
	};
	
	function addSnackGroupDIYNode(treeId, treeNode) {
		var aObj = $("#" + treeNode.tId + "_a");
		$("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
		$("#" + treeNode.tId + "_a").attr("snackGroup",treeNode.snackGroup);
	}
	
	function addSnackGroupExpandNode(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addSnackGroupExpandNode_" + treeNode.tId).length > 0)
            return;
        if (treeNode.level == 1) {
        	var editStr = "<span id='addSnackGroupExpandNode_" + treeNode.tId + "'>";
        	editStr += "<a onclick=editSnackGroup('" + treeNode.snackGroup + "')>[编辑]</a>";
	        editStr += "</span>";
            sObj.after(editStr);
        }
        return;
    };
    
    function removeSnackGroupExpandNode(treeId, treeNode) {
        $("#addSnackGroupExpandNode_" + treeNode.tId).unbind().remove();
    };
	
	function editSnackGroup(snackGroupId) {
		$.pdialog.open('product/snackGroup-edit?snackGroup='+snackGroupId, "editSnackGroup-edit", "编辑",  {width:500,height:250,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
	
	$(function() {
		$.fn.zTree.init($("#snackGroupTree"), snackGroupSetting, snackGroupNodes);
		var selectedLink;
		if("${selectedId}" == ""){
			selectedLink = $("a[snackGroup]").first();
		}else{
			$("a[snackGroup]").each(function(){
				if($(this).attr("snackGroup")=="${selectedId}"){
					selectedLink = $(this);
				}
			})
		}
		
		setTimeout(function() {
			selectedLink.click();
		}, 200);
	});
</script>