<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/cinemaPolicy-refuse-list" buttonText="筛选" alt="可输入策略名称、影院名称、城市名检索">
				<input type="hidden" name="selectedPolicyLogId" value="${selectedPolicyLogId}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="37">
			<#if cinemaPolicyLogPage.contents?size gt 0>
				<ul class="tree expand" layoutH="64">
					<#list cinemaPolicyLogPage.contents as policyLog>
		                <li>
		                    <@dwz.a href="/price/cinemaPolicy-refuse-view?policyLogId=${policyLog.id}&status=" target="ajax" rel="policyBox" refusedPolicyLogId="${policyLog.id}">[${policyLog.policy.cinema.county.city.name?substring(0,2)}]${policyLog.policy.cinema.name}-${policyLog.policy.name}</@dwz.a>
		                </li>
			        </#list>
			    </ul>
			    <div class="panelBar">
		            <@sys.pageNav pageModel=cinemaPolicyLogPage />
		        </div>
		    <#else>
		    	<div class="tree-msg">
		    		<h3>没有退回的策略</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="policyBox" class="tree-r-box" layoutH="0"></div>
</div>
<script>
    $(function() {
    	$("a[refusedPolicyLogId]").each(function() {
    		$(this).click(function() {
    			$("input[name=selectedPolicyLogId]",$(this).getPageDiv()).val($(this).attr("refusedPolicyLogId"));
    		});
    	});
    	var selectedPolicyLogLink = $("a[refusedPolicyLogId]").eq(0);
    	if("${selectedPolicyLogId}" && $("a[refusedPolicyLogId=${selectedPolicyLogId}]").size() > 0) {
    		selectedPolicyLogLink = $("a[refusedPolicyLogId=${selectedPolicyLogId}]");
    	}
    	setTimeout(function() {
    		selectedPolicyLogLink.click();
        }, 300);
    }); 
</script>