[
    <#if cities?? && cities?size gt 0>
        <#list cities as city>
        ["${city.code}", "${city.name}"]<#if city_has_next>,</#if>
        </#list>
    <#else>
        ["", "所有城市"]
    </#if>
]