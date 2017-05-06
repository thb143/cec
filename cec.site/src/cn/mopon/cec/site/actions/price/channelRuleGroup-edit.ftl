<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/channelRuleGroup-update">
			<@s.hidden path="channelRuleGroup.id" />
			<@s.hidden path="channelRuleGroup.policy" />
			<@s.hidden path="channelRuleGroup.cinema" />
			<div class="pageFormContent" layoutH="60">
				<dl>
					<dt>渠道名称：</dt>
		            <dd>${channelRuleGroup.policy.channel.name}</dd>
				</dl>
				<dl>
					<dt>影院名称：</dt>
		            <dd>${channelRuleGroup.cinema.name}</dd>
				</dl>
				<dl>
					<dt><@s.message code="connectFee"/>：</dt>
		            <dd><@s.input path="channelRuleGroup.connectFee" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="请输入0~1000的数字，格式：0000.00,如：1.00。" />元</dd>
		        </dl>
			</div>
			<@dwz.formBar />
		</div>
	</@dwz.form>
    </div>
</div>