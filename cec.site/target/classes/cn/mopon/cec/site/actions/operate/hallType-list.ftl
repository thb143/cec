<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageContent" layoutH="0">
            <#if hallTypeList?size gt 0>
            	<ul id="hallTypeTree" class="ztree" layoutH="46"></ul>
            <#else>
		    	<div class="tree-msg" layoutH="56">
		    		<h3>没有符合条件的影厅类型。</h3>
		    	</div>
		    </#if>
		    <@sec.any name="CINEMA_MANAGE">
	       		<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    			<li><@dwz.a href="/operate/hallType-add" title="新增影厅类型" target="dialog" width="S" height="S" weight="S" class="button"><span>新增影厅类型</span></@dwz.a></li>
		   		</@dwz.formBar>
		   	</@sec.any>
        </div>
	</div>
	<div id=hallTypeBox class="tree-r-box"></div>
</div>
<script type="text/javascript">
    var hallTypeNodes = 
    	{name:"影厅类型",
    		 open:true,
    		 children : [
    		             <#list hallTypeList as hallType>
    		             {
    		                 id : "${hallType_index + 1}",
    		                 hallType : "${hallType.id}",
    		                 name : "${hallType.name}",
    		                 title : "",
    		                 open : true,
    		                 url : "operate/hallType-edit?hallType=${hallType.id}",
    		                 target : "ajax",
    		                 box : "hallTypeBox"
    		             },
    		             </#list>
    		        ]
    	};
    
    var hallTypeSetting = {
        view : {
        	addDiyDom : addHallTypeDIYNode,
            showIcon : false
        },
        data : {
        	key: {
        		title : "title"
        	}
        }
    };

    function addHallTypeDIYNode(treeId, treeNode) {
        var aObj = $("#" + treeNode.tId + "_a");
        $("#" + treeNode.tId + "_a").attr("rel",treeNode.box);
        $("#" + treeNode.tId + "_a").attr("hallType",treeNode.hallType);
    }
    
    $(function() {
    	$.fn.zTree.init($("#hallTypeTree"), hallTypeSetting, hallTypeNodes);
    	var selectedLink;
    	if("${selectedId}" == ""){
			selectedLink = $("a[hallType]").first();
		}else{
			$("a[hallType]").each(function(){
				if($(this).attr("hallType")=="${selectedId}"){
					selectedLink = $(this);
				}
			})
		}
    	setTimeout(function() {
    		selectedLink.click();
        }, 200);
    });
</script>