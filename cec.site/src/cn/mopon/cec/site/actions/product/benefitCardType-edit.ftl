<div class="page">
	<div class="pageContent">
		<@dwz.form action="/product/benefitCardType-update">
		<div class="pageFormContent" layoutH="60">
		     <@s.hidden path="benefitCardType.id" />
			<div class="divider"></div>
			<dl>
				<dt>卡类编号：</dt>
				<dd>
					<#if benefitCardType.valid == ValidStatus.VALID>
						<@s.input path="benefitCardType.code" maxlength="20" readOnly="true" class="required digits" />
					<#else>
						<@s.input path="benefitCardType.code" maxlength="20" class="required digits" />
					</#if>
				</dd>
			</dl>
			<dl>
				<dt>卡号前缀：</dt>
				<dd>
					<#if benefitCardType.valid == ValidStatus.VALID>
						<@s.input path="benefitCardType.prefix" minlength="4" readOnly="true" maxlength="4" class="required digits" />
					<#else>
						<@s.input path="benefitCardType.prefix" minlength="4" maxlength="4" class="required digits" />
					</#if>
				</dd>
			</dl>
			<dl>
				<dt>卡类名称：</dt>
				<dd><@s.input path="benefitCardType.name" maxlength="20"
					class="required" /></dd>
			</dl>
			<dl>
				<dt>开卡金额：</dt>
				<dd><@s.input path="benefitCardType.initAmount" min="1"
					max="9999" class="required number" maxlength="7"  customvalid="regexPrice(element)" title="请输入0~9999的数字，格式：0000.00,如：1.00。"/></dd>
			</dl>
			<dl>
				<dt>有效期：</dt>
				<dd><@s.select path="benefitCardType.validMonth"
					items=ValidMonth?values itemLabel="text" itemValue="value"
					class="required combox" /></dd>
			</dl>
			<dl>
				<dt>续费金额：</dt>
				<dd><@s.input path="benefitCardType.rechargeAmount" min="1"
					max="9999" class="required number" maxlength="7"  customvalid="regexPrice(element)" title="请输入0~9999的数字，格式：0000.00,如：1.00。"/></dd>
			</dl>
			<dl>
				<dt>优惠总量：</dt>
				<dd><@s.input path="benefitCardType.totalDiscountCount" min="1" max="99999"
					maxlength="5" class="required digits" /></dd>
			</dl>
			<dl>
				<dt>日优惠量：</dt>
				<dd><@s.input path="benefitCardType.dailyDiscountCount" min="1" max="99999"
					maxlength="5" class="required digits" customvalid="leNumber(element,'#totalDiscountCount')" title="只能输入整数，日优惠量必须小于等于优惠总量"/></dd>
			</dl>
			<dl class="nowrap">
				<dt>可开卡渠道：</dt>
				<dd>
					<@dwz.checkboxs path="benefitCardType.channels" required=true items=channelList
					itemValue="id" itemLabel="name"  />
				</dd>
			</dl>
		</div>
		<@dwz.formBar /> </@dwz.form>
	</div>
</div>