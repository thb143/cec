<div class="page">
	<div class="pageHeader">
       	<@dwz.pageForm action="/price/channelPolicy-cinema-select?channelPolicyId=${channelPolicyId}" targetType="dialog" searchModel=cinemaModel  alt="可输入影院名称检索"/>
	</div>
	<div class="pageContent">
		<@dwz.form action="/price/channelRuleGroup-save" >
		<input type="hidden" name="channelPolicyId" value="${channelPolicyId}" />
		<table id="treeTable" style="width:100%" layoutH="76">
			<#list cinemaListModel.items as province>
			<tr data-tt-id="${province.code}" class="expanded" depth="1">
           		<td><label><@s.checkbox class="checkboxCtrl" group="${province.code}" alias="province" id="${province.code}"/>${province.name}</label></td>
           	</tr>
           	<#list province.cities as cityModel>
           		<tr data-tt-id="${cityModel.city.code}" data-tt-parent-id="${province.code}" depth="2">
           			<td><label><input type="checkbox" name="${province.code}" alias="city" data-tt-parent-id="${cityModel.city.code}"/>${cityModel.city.name}</label></td>
         		</tr>
         		<tr data-tt-id="${cityModel.city.code+cityModel_index}" data-tt-parent-id="${cityModel.city.code}" depth="3">
         			<td>
	         			<#list cityModel.cinemas as cinema>
		         			<label><input type="checkbox" value="${cinema.id}" name="cinemaIds" alias="cinema"/>${cinema.name}</label>
	       				</#list>
         			</td>
       			</tr>
         	</#list>
            </#list>
        </table>
		<@dwz.formBar />
		</@dwz.form>
	</div>
</div>
<script type="text/javascript">
var tree = $("#treeTable");
tree.treetable({
	expandable : true
});
$(function(){
	//默认展开一级节点。
	tree.find("tr[depth = '1']").each(function(){
		tree.treetable('expandNode',$(this).attr('data-tt-id'))
	});
	tree.find("tr[depth = '2']").each(function(){
		tree.treetable('expandNode',$(this).attr('data-tt-id'))
	});
	
    $('#treeTable').find("input[type='checkbox'][alias='city']").each(function(){
    	selectCinema($(this));
    });
    
    $('#treeTable').find("input[type='checkbox'][alias='province']").each(function(){
    	$(this).change(function(){
    		var tr = $(this).closest("tr");
    		$('#treeTable').find("tr[data-tt-parent-id="+tr.attr("data-tt-id")+"]").each(function(){
   				if (tr.find("input").attr("checked")) {
   					$(this).find("input").attr("checked","true");
   					$('#treeTable').find("tr[data-tt-parent-id="+$(this).attr("data-tt-id")+"]").find("input").attr("checked","true");
   	    		} else {
   	    			$(this).find("input").removeAttr("checked");
   	    			$('#treeTable').find("tr[data-tt-parent-id="+$(this).attr("data-tt-id")+"]").find("input").removeAttr("checked");
   	    		}
    		});
    	});
    });
    
    $('#treeTable').find("input[type='checkbox'][alias='cinema']").each(function(){
    	$(this).change(function(){
    		invert($(this));
    	});
    });
    
    function selectCinema(obj){
    	obj.change(function(){
    		var tr = $(this).closest("tr");
    		$('#treeTable').find("tr[data-tt-parent-id="+tr.attr("data-tt-id")+"]").each(function(){
   				if (tr.find("input").attr("checked")) {
   					$(this).find("input").attr("checked","true");
   	    		} else {
   	    			$(this).find("input").removeAttr("checked");
   	    		}
    		});
    	});
    }
    
    function invert(cinema){
    	var tr = cinema.closest("tr");
		var cinemas = tr.find("input[alias='cinema']").size();
		var checkCinemas = tr.find("input[alias='cinema']:checked").size();
		var pid = tr.attr("data-tt-parent-id");
		if (cinemas == checkCinemas) {
			$("tr[data-tt-id="+pid+"]").find("input").attr("checked","true");
		}else{
			$("tr[data-tt-id="+pid+"]").find("input").removeAttr("checked");
		}
		var city = $("tr[data-tt-id="+pid+"]");
		checked(city,"city");
    }
    
    function checked(obj,alias){
    	var flag = true;
    	$('#treeTable').find("tr[data-tt-parent-id='" +obj.attr('data-tt-parent-id')+ "']").find("input[alias='"+alias+"']").each(function(){
			if (!$(this).is(":checked")){
				return flag = false;
			}
		});
		if (flag) {
			$("tr[data-tt-id="+obj.attr("data-tt-parent-id")+"]").find("input[type='checkbox']").attr("checked","true");
		} else {
			$("tr[data-tt-id="+obj.attr("data-tt-parent-id")+"]").find("input[type='checkbox']").removeAttr("checked");
		}
    }
});
</script>