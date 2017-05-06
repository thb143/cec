<div class="page">
	<div class="pageContent">
		<@dwz.form action="/product/benefitCardType-snackRule-save">
		<@s.hidden path="snackRule.type" />
		<div layouth="60" class="pageFormContent"
			style="height: 501px; overflow: auto;">
			<dl class="nowrap">
				<dt>规则名称：</dt>
				<dd><@s.input path="snackRule.name" class="required
					m-input" maxlength="20"/></dd>
			</dl>
			<input type="hidden" class="hallText" />
            <@pro.select_snack path="snackRule.snacks" />
			<@pro.settleRule path="snackRule.settleRule" />
		</div>
		<@dwz.formBar /> </@dwz.form>
	</div>
</div>
