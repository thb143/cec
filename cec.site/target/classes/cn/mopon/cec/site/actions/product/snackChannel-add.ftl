<div class="page">
    <div class="pageContent">
        <@dwz.form action="/product/snackChannel-save">
        <@s.hidden path="snackChannelListModel.snack" />
        <div class="pageFormContent" layoutH="60">
         	<dl class="nowrap">
				<dt>选择渠道：</dt>
				<dd>
	                <@dwz.checkboxs path="snackChannelListModel.channels" items=snackChannelListModel.switchChannels itemValue="id" itemLabel="name" />
				</dd>
			</dl>
		    <dl>
				<dt><span><@s.message code="connectFee"/>：</span></dt>
	            <dd><@s.input path="snackChannelListModel.origChannel.connectFee" class="required number s-input hideError" min="-1000" max="1000" customvalid="regexCanNegativePrice(element)" title="请输入-1000~1000的数字，格式：0000.00,如：1.00。" />元</dd>
	        </dl>
		</div>
        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>