<div class="tree-l-box" layoutH="0">
	<div class="pageHeader">
		<@dwz.pageForm action="/price/specialPolicy-approve-list" buttonText="筛选" alt="可输入特殊定价策略名称检索" />
    </div>
	<div class="pageContent" layoutH="40">
		<#if specialPolicyApproveLogs?size gt 0>
			<ul class="tree expand">
					<li>
			            <a>待审批特殊定价策略</a>
			            <ul>
			                <#list specialPolicyApproveLogs as policyLog>
				                <li>
				                    <@dwz.a href="/price/specialPolicy-approve-view?policyLogId=${policyLog.id}" target="ajax" rel="specialPolicyApproveBox">${policyLog.policy.name}</@dwz.a>
				                </li>
			                </#list>
			            </ul>
			        </li>
		    </ul>
	    <#else>
	    	<div class="tree-msg">
	    		<h3>没有待审批的特殊定价策略</h3>
	    	</div>
	    </#if>
	</div>
</div>
<div id="specialPolicyApproveBox" class="tree-r-box" layoutH="0"></div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=specialPolicyApproveBox]").eq(0).click();
        }, 30);
    }); 
</script>