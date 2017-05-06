<select class="combox" name="${name}.items[#index#].settleRule.type">
<#list settleRuleTypes as settleRule>
	<option value="${settleRule.value}">${settleRule.text}</option>
</#list>
</select>