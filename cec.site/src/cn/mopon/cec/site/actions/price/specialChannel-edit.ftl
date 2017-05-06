<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialChannel-update">
        <@s.hidden path="specialChannel.id" />
        <div class="pageFormContent" layoutH="60">
        	<dl>
				<dt><span><@s.message code="connectFee"/>：</span></dt>
	            <dd><@s.input path="specialChannel.connectFee" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="请输入0~1000的数字，格式：0000.00,如：1.00。"   value="${specialChannel.connectFee?string('0.00')}"/>元</dd>
	        </dl>
            <@ass.settleRule path="specialChannel.settleRule" />
		</div>
        <@dwz.formBar />
       </@dwz.form>
    </div>
</div>