<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialChannel-save">
        <@s.hidden path="specialChannelListModel.specialRule" />
        <div class="pageFormContent" layoutH="60">
         	<dl class="nowrap">
				<dt>选择渠道：</dt>
				<dd>
	                <@dwz.checkboxs path="specialChannelListModel.channels" items=specialChannelListModel.switchChannels itemValue="id" itemLabel="name" />
				</dd>
			</dl>
		    <dl>
				<dt><span><@s.message code="connectFee"/>：</span></dt>
	            <dd><@s.input path="specialChannelListModel.origChannel.connectFee" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="请输入0~1000的数字，格式：0000.00,如：1.00。" />元</dd>
	        </dl>
            <@ass.settleRule path="specialChannelListModel.settleRule" />
		</div>
        <@dwz.formBar />
       </@dwz.form>
    </div>
</div>