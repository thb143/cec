[
    <#list districts as district>
	["${district.code}", "${district.name}"]<#if district_has_next>,</#if>
	</#list>
]