<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageContent" layoutH="17">
            <ul id="accessTypeTree" class="ztree" layoutH="34"></ul>
            <input type="hidden" name="selectedId" value="${selectedId}" />
        </div>
	</div>
	<div id="accessTypeBox" class="tree-r-box"></div>
</div>
<script type="text/javascript">
    var accessTypeNodes = [
	    {
	        id : new Date(),
	        accessType : 1,
	        name : "选座票",
	        open : true,
	        title : "",
	        children : [
	             <#list ticketAccessTypes as ticketAccessType>
	             {
	                 id : "${ticketAccessType_index + 1}",
	                 accessTypeId : "${ticketAccessType.id}",
	                 name : "${ticketAccessType.name}",
	                 title : "",
	                 url : "operate/ticketAccessType-edit?id=${ticketAccessType.id}",
	                 box : "accessTypeBox",
	                 target : "ajax",
	                 open : true
	             },
	             </#list>
	        ]
	    },
	    {
	        id : new Date(),
	        accessType : 2,
	        name : "会员",
	        open : true,
	        title : "",
	        children : [
	             <#list memberAccessTypes as memberAccessType>
	             {
	                 id : "${memberAccessType_index + 1}",
	                 accessTypeId : "${memberAccessType.id}",
	                 name : "${memberAccessType.name}",
	                 title : "",
	                 url : "operate/memberAccessType-edit?id=${memberAccessType.id}",
	                 box : "accessTypeBox",
	                 target : "ajax",
	                 open : true
	             },
	             </#list>
	        ]
	    }
    ];
    var accessTypeSetting = {
        view : {
            addDiyDom : addAccessTypeDIYNode,
            selectedMulti : false,
            showIcon : false
        },
        data : {
        	key: {
        		title : "title"
        	}
        }
    };
    
    function addAccessTypeDIYNode(treeId, treeNode) {
    	var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.level == 0) {
        	var editStr = "<span id='addAccessTypeExpandNode_" + treeNode.tId + "'>";
        	editStr += "<a onclick=addAccessType('" + treeNode.accessType + "')>[新增]</a>";
	        editStr += "</span>";
            sObj.after(editStr);
            return;
        }
    	if (treeNode.parentNode && treeNode.parentNode.id != 2){
             return;
    	}
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("accessTypeId",treeNode.accessTypeId);
        if (treeNode.id < accessTypeNodes.length + 1) {
            return;
        }
    }
    
    function addAccessType(accessType) {
    	if(accessType == 1){
    		return "<a onclick='"+ addTicketAccessType() +"'>[新增]</a>";
     	}
    	return "<a onclick='"+ addMemberAccessType() +"'>[新增]</a>";
    }
  	// 新增选座票接入类型
    function addTicketAccessType() {
    	$.pdialog.open('operate/ticketAccessType-add', "ticketAccessType-add", "新增",  {width:700,height:400,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
	// 新增会员接入类型
    function addMemberAccessType() {
    	$.pdialog.open('operate/memberAccessType-add', "memberAccessType-add", "新增",  {width:700,height:400,mask:true ,minable:false,maxable:false,resizable:false,drawable:true});
    }
    
    var accessTypeTree;
    $(function() {
    	accessTypeTree = $.fn.zTree.init($("#accessTypeTree"), accessTypeSetting, accessTypeNodes);
    	$("a[accessTypeId]").each(function(){
    		$(this).click(function() {
    			$("input[name=selectedId]",$(this).getPageDiv()).val($(this).attr("accessTypeId"));
    		});
    	});
    	var selectedCinemaLink = $("a[accessTypeId]").eq(0);
    	if("${selectedId}" && $("a[accessTypeId=${selectedId}]").size() > 0) {
    		selectedCinemaLink = $("a[accessTypeId=${selectedId}]");
    	}
    	
    	setTimeout(function() {
    		selectedCinemaLink.click();
        }, 300);
    });	
</script>