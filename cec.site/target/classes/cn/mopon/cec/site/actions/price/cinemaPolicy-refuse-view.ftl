<@dwz.reload action="/price/cinemaPolicy-refuse-view" policyLogId="${policyLog.id}" />
<div class="page">
	<@pri.cinemaPolicy_head cinemaPolicy=policyLog.policy />
	<div class="pageContent">
	    <div class="pageFormContent" layoutH="110">
	        <@pri.cinemaRule_list cinemaRuleList=cinemaRuleList policy=policyLog.policy logId=policyLog.id treeId="cinemaRuleRefuse"/>
	    </div>
	    <@pri.cinemaPolicy_bar policy=policyLog.policy treeId="cinemaRuleRefuse"/>
	</div>
</div>
<script type="text/javascript">
$(function(){
	var url="/price/cinemaPolicy-refuse-view?policyLogId=${policyLog.id}&status=";
	var selectPolicy =$(".page .tree-l-box .selected").find("a[rel='policyBox']");
	$(".page #searchRule").find("select[name='cinemaRuleStatus']").change(function(){
		var href = selectPolicy.attr('href');
		selectPolicy.attr("href",url + $(this).val());
		selectPolicy.click();
		selectPolicy.attr("href",url);
	});
});
</script>