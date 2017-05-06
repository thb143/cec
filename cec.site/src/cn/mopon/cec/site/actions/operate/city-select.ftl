<div class="page">
    <div class="pageContent">
    	<div class="pageFormContent" layoutH="60">
    		<dl class="nowrap">
    			<dt style="width:400px">选择城市（只显示已定义分组的城市）：</dt>
    			<dd style="width:660px">
	    			<table width="100%" layoutH="130" class="list" >
	    				<tbody>
	    					<#list cityGroups as cityGroup>
	    						<tr>
	    							<td valign="middle" width="15%"><label class='dd-span'><input type="checkbox" class="checkAll" group="cityCodes" />${cityGroup.name}</label></td>
	    							<td align="left" width="100%" >
	    								<@s.checkboxs path="minPriceGroup.cityCode" items=cityGroup.cities itemLabel="name" itemValue="code" class="toSelect" prefix="<label class='dd-span'>" suffix="</label>" />
	    							</td>
	    						</tr>
	    					</#list>
	    				</tbody>
	    			</table>
    			</dd>
    		</dl>
    	</div>
    	<@dwz.formBar showSubmitBtn=false >
        	<div class="button"><div class="buttonContent"><button type="button" class="selectCity">保存</button></div></div>
        </@dwz.formBar>
    </div>
</div>
<script>
$(function(){
	var checkedValue = $("#cityValues").val();
	$(".toSelect").each(function(){
		if(checkedValue != '' && checkedValue.indexOf($(this).val()) >= 0){
			$(this).attr("checked",true);
		}
	});
	$(".checkAll").each(function(){
		var size = $(this).closest("td").next().find("label input[type='checkbox']").size();
		var count = 0;
		$(this).closest("td").next().find("label input[type='checkbox']").each(function(){
			if ($(this).attr("checked")) {
				count++;
			}
		});
		if (size == count && size > 0) {
			$(this).attr("checked",true);
		} else {
			$(this).attr("checked",false);
		}
	});
});
$(".selectCity").click(function(){
	var checkedName = '';
	var checkedCode = '';
	$(".toSelect").each(function(){
		if($(this).attr("checked")){
			if (checkedName.indexOf($(this).closest("label").text()) >= 0) {
				return;
			}
			if (checkedCode.indexOf($(this).val()) >= 0) {
				return;
			}
			if (checkedName.length > 0) {
				checkedName += ","
			}
			if (checkedCode.length > 0) {
				checkedCode += ","
			}
 			checkedName += $(this).closest("label").text();
			checkedCode += $(this).val();
		}
	});
	$(".checkedCities").html("");
	if (checkedName.length > 0) {
		$(".checkedCities").html(checkedName+"。");
	} else {
		$(".checkedCities").html("除其他分类城市以外的所有城市及地区。");
	}
	$("#cityValues").val("");
	$("#cityValues").val(checkedCode);
	$.pdialog.close("city-select");
});
$(".checkAll").click(function(){
	if ($(this).attr("checked")) {
		$(this).closest("td").next().find("input[type='checkbox']").attr("checked",true);
	} else {
		$(this).closest("td").next().find("input[type='checkbox']").attr("checked",false);
	}
});
$(".toSelect").click(function(){
	var size = $(this).closest("label").siblings().size();
	var count = 0;
	$(this).closest("td").find("label input[type='checkbox']").each(function(){
		if ($(this).attr("checked")) {
			count++;
		}
	});
	if (size == count) {
		$(this).closest("td").prev().find("input[type='checkbox']").attr("checked",true);
	} else {
		$(this).closest("td").prev().find("input[type='checkbox']").attr("checked",false);
	}
});
</script>