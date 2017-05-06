<div class="page">
    <div class="pageContent">
    	<@dwz.form action="/operate/channel-save">
            <div class="pageFormContent" layoutH="60">
                <dl>
                    <dt>渠道编号：</dt>
                    <dd><@s.input path="channel.code" maxlength="60" class="required"/></dd>
                </dl>
                <dl>
                    <dt>渠道名称：</dt>
                    <dd><@s.input path="channel.name" maxlength="60" class="required" /></dd>
                </dl>
                <!-- <dl>
                    <dt>通讯密钥：</dt>
                    <dd><@s.input path="channel.secKey" maxlength="60" class="required" /></dd>
                </dl> -->
                <dl>
                    <dt>渠道类型：</dt>
                    <dd><@s.radios id="chennelType" path="channel.type" items=ChannelType?values itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"/></dd>
                </dl>
                <dl class="nowrap">
                    <dt>备注：</dt>
                    <dd><@s.textarea path="channel.remark" rows="5" maxlength="800" class="l-input" ></@s.textarea></dd>
                </dl>
            </div>
            <@dwz.formBar />
        </@dwz.form>
    </div>
</div>