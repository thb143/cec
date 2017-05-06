<div class="page">
	<div class="pageContent">
		<@dwz.form action="/product/benefitCardType-snackRule-update">
		<@s.hidden path="benefitCardTypeSnackRule.id" />
		<@s.hidden path="benefitCardTypeSnackRule.type.id" />
		<div layouth="60" class="pageFormContent"
			style="height: 501px; overflow: auto;">
			<dl class="nowrap">
				<dt>规则名称：</dt>
				<dd><@s.input path="benefitCardTypeSnackRule.name" class="required
					m-input" maxlength="20"/></dd>
			</dl>
			<input type="hidden" class="hallText" />
            <@pro.select_snack path="benefitCardTypeSnackRule.snacks" hallMList=snackList />
			<@pro.settleRule path="benefitCardTypeSnackRule.settleRule" />
		</div>
		<@dwz.formBar />
	  </@dwz.form>
	</div>
</div>