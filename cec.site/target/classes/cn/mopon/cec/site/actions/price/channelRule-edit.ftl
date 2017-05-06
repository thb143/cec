<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/channelRule-update">
			<@s.hidden path="channelRule.id" value="${channelRule.id}" />
			<@s.hidden path="channelRule.group" value="${channelRule.group.id}" />
			<@s.hidden path="channelRule.showType" value="${channelRule.showType.value}" />
			<div layouth="60" class="pageFormContent" style="height: 501px; overflow: auto;">
				<dl class="nowrap">
		        	<dt>规则名称：</dt>
					<dd><@s.input path="channelRule.name" class="required m-input" maxlength="20"/></dd>
				</dl>
				<dl class="nowrap">
					<dt>关联影厅：</dt>
					<dd>
						<@dwz.checkboxs path="channelRule.halls" items=channelRule.group.cinema.enableHalls itemValue="id" itemLabel="fullName" />
					</dd>
				</dl>
				<@ass.periodRule path="channelRule.periodRule" />
				<@ass.settleRule path="channelRule.settleRule" ruleType="channelRule" />
			</div>
			<@dwz.formBar />
		</@dwz.form>
    </div>
</div>
