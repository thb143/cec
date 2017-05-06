<div class="page">
	<div class="pageContent">
		<@pri.cinemaPolicy_head cinemaPolicy=policyLog.policy />
	    <div class="pageFormContent" layoutH="110">
	    	<@pri.cinemaRule_list cinemaRuleList=cinemaRuleList policy=policyLog.policy logId=policyLog.id show=false treeId="cinemaRuleApprove"/>
	   	</div>
	   	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li><@dwz.a class="button" href="/price/cinemaPolicy-approve-pass?policyLogId=${policyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批通过该策略？"><span>审批通过</span></@dwz.a></li>
        	<li><@dwz.a class="button" href="/price/cinemaPolicy-approve-refuse?policyLogId=${policyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批退回该策略？"><span>审批退回</span></@dwz.a></li>
        	<li><a id="collapseBtn_${policyLog.policy.status}" class="button" href="javascript:expanAll('cinemaRuleApprove',3);"><span>全部展开/收缩</span></a></li>
        </@dwz.formBar>
	</div>
</div>
<script type="text/javascript">
$(function(){
	var url="/price/cinemaPolicy-approve-view?policyLogId=${policyLog.id}&status=";
	var selectPolicy =$(".page .tree-l-box .selected").find("a[rel='policyApproveBox']");
	$(".page #searchRule").find("select[name='cinemaRuleStatus']").change(function(){
		var href = selectPolicy.attr('href');
		selectPolicy.attr("href",url + $(this).val());
		selectPolicy.click();
		selectPolicy.attr("href",url + "${RuleStatus.UNAUDIT.value}");
	});
	// 默认全部展开。
	$("#cinemaRuleApprove").treetable('expandAll');
});
</script>