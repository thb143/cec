<div class="tree-l-box" layoutH="0">
	<div class="pageHeader">
		<@dwz.pageForm action="/price/specialPolicy-audit-list" buttonText="筛选" alt="可输入特殊定价策略名称检索" />
    </div>
	<div class="pageContent" layoutH="40">
		<#if specialPolicyLogs?size gt 0>
			<ul class="tree expand">
					<li>
			            <a>待审核特殊定价策略</a>
			            <ul>
			                <#list specialPolicyLogs as policyLog>
				                <li>
				                    <@dwz.a href="/price/specialPolicy-audit-view?policyLogId=${policyLog.id}" target="ajax" rel="specialPolicyAuditBox">${policyLog.policy.name}</@dwz.a>
				                </li>
			                </#list>
			            </ul>
			        </li>
		    </ul>
	    <#else>
	    	<div class="tree-msg">
	    		<h3>没有待审核的特殊定价策略</h3>
	    	</div>
	    </#if>
	</div>
</div>
<div id="specialPolicyAuditBox" class="tree-r-box" layoutH="0"></div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=specialPolicyAuditBox]").eq(0).click();
        }, 30);
    }); 
</script>