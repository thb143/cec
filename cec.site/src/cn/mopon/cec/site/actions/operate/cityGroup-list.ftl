<div class="page">
	<div class="tree-l-box" layoutH="0">
		<input type="hidden" name="selectedId" value="${selectedId}" />
		<div class="pageContent" layoutH="0">
            <#if cityGroups?size gt 0>
		    	<ul class="tree" layoutH="36">
		            <li>
		                <a>已定义分组</a>
		                <ul>
		                	<#list cityGroups as cityGroup>
			                    <li><@dwz.a href="/operate/cityGroup-edit?cityGroup=${cityGroup.id}" target="ajax" rel="cityGroupBox" cityGroupId="${cityGroup.id}">${cityGroup.name}</@dwz.a></li>
		                    </#list>
		                </ul>
		            </li>
		        </ul>
            <#else>
		    	<div class="tree-msg" layoutH="56">
		    		<h3>没有符合条件的城市分组。</h3>
		    	</div>
		    </#if>
            <@sec.any name="FILM_MANAGE">
	       		<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    		<li><@dwz.a href="/operate/cityGroup-add" title="新增城市分组" target="dialog" class="button"><span>新增城市分组</span></@dwz.a></li>
		   		</@dwz.formBar>
	   		</@sec.any>
        </div>
	</div>
	<div id="cityGroupBox" class="tree-r-box"></div>
</div>
<script type="text/javascript">
$(function() {
    setTimeout(function() {
        $("a[citygroupid='${selectedCityGroupId}']").click();
    }, 200);
}); 
</script>