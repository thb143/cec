<div class="page">
	<div class="pageContent">
		<@pri.cinemaPolicy_head cinemaPolicy=policyLog.policy />
	    <div class="pageFormContent" layoutH="110">
	    	<@pri.cinemaRule_list cinemaRuleList=cinemaRuleList policy=policyLog.policy logId=policyLog.id show=false treeId="cinemaRuleAudit"/>
	   	</div>
	   	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li>
                <@dwz.a class="button" href="/price/cinemaPolicy-audit-pass?policyLogId=${policyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核通过该策略？"><span>审核通过</span></@dwz.a>
            </li>
            <li>
                <@dwz.a class="button" href="/price/cinemaPolicy-audit-refuse?policyLogId=${policyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核退回该策略？"><span>审核退回</span></@dwz.a>
            </li>
            <li>
            	<a id="collapseBtn_${policyLog.policy.status}" class="button" href="javascript:expanAll('cinemaRuleAudit',3);"><span>全部展开/收缩</span></a>
        	</li>
        </@dwz.formBar>
	</div>
</div>
<script type="text/javascript">
$(function(){
	var url="/price/cinemaPolicy-audit-view?policyLogId=${policyLog.id}&status=";
	var selectPolicy =$(".page .tree-l-box .selected").find("a[rel='cinemaPolicyAuditBox']");
	$(".page #searchRule").find("select[name='cinemaRuleStatus']").change(function(){
		var href = selectPolicy.attr('href');
		selectPolicy.attr("href",url + $(this).val());
		selectPolicy.click();
		selectPolicy.attr("href",url + "${RuleStatus.UNAUDIT.value}");
	});
	// 默认全部展开。
	$("#cinemaRuleAudit").treetable('expandAll');
});
</script>