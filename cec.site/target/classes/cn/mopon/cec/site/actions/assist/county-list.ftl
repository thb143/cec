[
    <#if counties?? && counties?size gt 0>
        <#list counties as county>
        ["${county.code}", "${county.name}"]<#if county_has_next>,</#if>
        </#list>
    <#else>
        ["", "所有区县"]
    </#if>
]