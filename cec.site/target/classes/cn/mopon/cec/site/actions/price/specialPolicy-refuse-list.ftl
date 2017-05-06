<div class="tree-l-box" layoutH="0">
	<div class="pageHeader">
		<@dwz.pageForm action="/price/specialPolicy-refuse-list" buttonText="筛选" alt="可输入特殊定价策略名称检索">
			<input type="hidden" name="selectedSpecialPolicyLogId" value="${selectedSpecialPolicyLogId}" />
		</@dwz.pageForm>
    </div>
	<div class="pageContent" layoutH="40">
		<#if specialPolicyRefuseLogs?size gt 0>
			<ul class="tree expand">
					<li>
			            <a>退回的特殊定价策略</a>
			            <ul>
			                <#list specialPolicyRefuseLogs as policyLog>
				                <li>
				                    <@dwz.a href="/price/specialPolicy-refuse-view?policyLogId=${policyLog.id}" target="ajax" rel="specialPolicyBox" refusedSpecialPolicyLogId="${policyLog.id}">${policyLog.policy.name}</@dwz.a>
				                </li>
			                </#list>
			            </ul>
			        </li>
		    </ul>
	    <#else>
	    	<div class="tree-msg">
	    		<h3>没有退回的特殊定价策略</h3>
	    	</div>
	    </#if>
	</div>
</div>
<div id="specialPolicyBox" class="tree-r-box" layoutH="0"></div>
<script>
    $(function() {
    	$("a[refusedSpecialPolicyLogId]").each(function() {
    		$(this).click(function() {
    			$("input[name=selectedSpecialPolicyLogId]",$(this).getPageDiv()).val($(this).attr("refusedPolicyLogId"));
    		});
    	});
    	var selectedPolicyLogLink = $("a[refusedSpecialPolicyLogId]").eq(0);
    	if("${selectedSpecialPolicyLogId}" && $("a[refusedSpecialPolicyLogId=${selectedSpecialPolicyLogId}]").size() > 0) {
    		selectedPolicyLogLink = $("a[refusedSpecialPolicyLogId=${selectedSpecialPolicyLogId}]");
    	}
    	setTimeout(function() {
    		selectedPolicyLogLink.click();
        }, 30);
    }); 
</script>