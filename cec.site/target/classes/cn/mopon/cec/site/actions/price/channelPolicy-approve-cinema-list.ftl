<div class="page">
	<div class="tree-l-box" layoutH="0">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/channelPolicy-approve-cinema-list" targetType="div" rel="channelPolicyApproveBox" buttonText="筛选" alt="可输入影院名称、城市名检索">
				<input type="hidden" name="channelPolicyLogId" value="${channelPolicyLog.id}" />
			</@dwz.pageForm>
	    </div>
		<div class="pageContent" layoutH="37">
			<#if groupPage.contents?size gt 0>
				<ul class="tree expand" layoutH="64">
					<#list groupPage.contents as model>
		                <li>
			                <@dwz.a href="/price/channelPolicy-approve-view?channelPolicyLogId=${channelPolicyLog.id}&cinemaId=${model.cinema.id}&ruleStatus=${RuleStatus.UNAUDIT.value}" target="ajax" rel="channelPolicyApproveCinemaBox">[${model.cinema.county.city.name?substring(0,2)}]${model.cinema.name}</@dwz.a>
			            </li>
	                </#list>
			    </ul>
			    <div class="panelBar">
		            <@sys.pageNav pageModel=groupPage rel="channelPolicyApproveBox"/>
		        </div>
		    <#else>
		    	<div class="tree-msg" layoutH="74">
		    		<h3>没有待审批的影院</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="channelPolicyApproveCinemaBox" class="tree-r-box" layoutH="0"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=channelPolicyApproveCinemaBox]").eq(0).click();
        }, 300);
    }); 
</script>