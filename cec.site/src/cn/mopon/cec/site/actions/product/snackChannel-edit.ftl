<div class="page">
    <div class="pageContent">
        <@dwz.form action="/product/snackChannel-update">
        <@s.hidden path="snackChannel.id" />
        <@s.hidden path="snackChannel.snack" />
        <@s.hidden path="snackChannel.channel" />
        <div class="pageFormContent" layoutH="60">
        	<dl>
				<dt><span><@s.message code="connectFee"/>：</span></dt>
	            <dd><@s.input path="snackChannel.connectFee" class="required number s-input hideError" min="-1000" max="1000" customvalid="regexCanNegativePrice(element)" title="请输入-1000~1000的数字，格式：0000.00,如：1.00。" value="${snackChannel.connectFee?string('0.00')}"/>元</dd>
	        </dl>
		</div>
        <@dwz.formBar />
       </@dwz.form>
    </div>
</div>