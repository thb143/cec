<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/cinemaRule-save">
			<@s.hidden path="cinemaRule.policy" value="${cinemaRule.policy.id}" />
			<div layouth="60" class="pageFormContent" style="height: 501px; overflow: auto;">
				<@s.hidden path="cinemaRule.showType" value="${cinemaRule.showType.value}" />
				<dl class="nowrap">
		        	<dt>规则名称：</dt>
					<dd><@s.input path="cinemaRule.name" class="required m-input" maxlength="20"/></dd>
				</dl>
				<dl class="nowrap">
					<dt>关联影厅：</dt>
					<dd>
						<@dwz.checkboxs path="cinemaRule.halls" items=cinemaRule.policy.cinema.enableHalls itemValue="id" itemLabel="fullName" />
					</dd>
				</dl>
				<@ass.periodRule path="cinemaRule.periodRule" />
				<@ass.settleRule path="cinemaRule.settleRule" ruleType="cinemaRule" />
			</div>
			<@dwz.formBar />
		</@dwz.form>
    </div>
</div>
<script type="text/javascript">
$(function(){
	$(".page").find("input[name='settleRule.basisType']:radio").eq(2).parent().hide();
});
</script>