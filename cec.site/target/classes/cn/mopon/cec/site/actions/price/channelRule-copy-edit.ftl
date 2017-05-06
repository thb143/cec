<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/channelRule-copy">
		<@s.hidden path="channelRule.group" value="${channelRule.group.id}" />
			<div layouth="60" class="pageFormContent" style="height: 501px; overflow: auto;">
				<dl class="nowrap">
		        	<dt>规则名称：</dt>
					<dd><@s.input path="channelRule.name" value="${channelRule.name}-副本" class="required m-input" maxlength="20"/></dd>
				</dl>
				<dl class="nowrap">
					<dt>放映类型：</dt>
		            <dd>
		           		<@s.radios path="channelRule.showType" items=showTypes itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"/>
		           	</dd>
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