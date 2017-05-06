<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/cinemaPolicy-approve-list" buttonText="筛选" alt="可输入策略名称、影院名称、城市名检索" />
	    </div>
		<div class="pageContent" layoutH="37">
			<#if cinemaPolicyLogPage.contents?size gt 0>
				<ul class="tree expand" layoutH="64">
					<#list cinemaPolicyLogPage.contents as policyLog>
		                <li>
		                    <@dwz.a href="/price/cinemaPolicy-approve-view?policyLogId=${policyLog.id}&status=${RuleStatus.UNAUDIT.value}" target="ajax" rel="policyApproveBox">[${policyLog.policy.cinema.county.city.name?substring(0,2)}]${policyLog.policy.cinema.name}-${policyLog.policy.name}</@dwz.a>
		                </li>
			        </#list>
			    </ul>
			    <div class="panelBar">
		            <@sys.pageNav pageModel=cinemaPolicyLogPage />
		        </div>
		    <#else>
		    	<div class="tree-msg">
		    		<h3>没有待审批的策略</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="policyApproveBox" class="tree-r-box" layoutH="0"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=policyApproveBox]").eq(0).click();
        }, 300);
    }); 
</script>