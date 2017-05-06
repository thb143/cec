<div class="page">
	<div class="pageContent">
		<@dwz.form action="/product/benefitCardType-rule-update">
		<@s.hidden path="benefitCardTypeRule.id" />
		<@s.hidden path="benefitCardTypeRule.type.id" />
		<div layouth="60" class="pageFormContent"
			style="height: 501px; overflow: auto;">
			<dl class="nowrap">
				<dt>规则名称：</dt>
				<dd><@s.input path="benefitCardTypeRule.name" class="required
					m-input" maxlength="20"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>放映类型：</dt>
				<dd><@dwz.checkboxs path="benefitCardTypeRule.showTypes" items=ShowType?values itemValue="value" itemLabel="text" /></dd>
			</dl>
			<input type="hidden" class="hallText" />
            <@pro.select_hall path="benefitCardTypeRule.halls" hallMList=hallList />
			<@pro.settleRule path="benefitCardTypeRule.settleRule" />
		</div>
		<@dwz.formBar />
	  </@dwz.form>
	</div>
</div>