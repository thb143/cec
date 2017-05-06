<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/channel-set-save">
            <@s.hidden path="channelSettings.id" />
            <@s.hidden path="channelSettings.channel" />
            <div class="pageFormContent s-cols" layoutH="60">
				<dl class="nowrap">
	            	<dt>提前停售时间：</dt>
	            	<dd>
	            		<@s.input path="channelSettings.stopSellTime" class="required" customvalid="regexStopTime(element)" min="-30" max="90" maxlength="3" title="停售时间必须是整数，介于-30和90之间 。" /><span class="dd-span">（分钟）</span><span class="dd-span" style="color: red;"></span>
	            	</dd>
	            </dl>
	            <dl class="nowrap">
	            	<dt>提前停退时间：</dt>
	            	<dd>
	            		<@s.input path="channelSettings.stopRevokeTime" class="required" customvalid="regexStopTime(element)" min="-60" max="2880" maxlength="4" title="停售时间必须是整数，介于-60和2880之间 。" /><span class="dd-span">（分钟）</span><span class="dd-span" style="color: red;"> 注：以上两项修改后对新生成的排期有效。</span>
	            	</dd>
	            </dl>
	            <dl class="nowrap">
	            	<dt>接口签名设置：</dt>
	            	<dd>
	            		<@s.radios id="verifySign" path="channelSettings.verifySign" items=VerifySign?values itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"/>
					</dd>
	            </dl>
	            <dl>
                    <dt>接口日志长度：</dt>
                    <dd>
                    	<@s.input path="channelSettings.logLength" min="0" max="5000" class="required digits" />
                    </dd>
                </dl>
	            <dl class="nowrap" height="90%;">
	            	<dt>开放票务接口：</dt>
	            	<dd>
	            		<@dwz.checkboxs path="channelSettings.ticketApiMethods" items=TicketApiMethod?values itemLabel="text" itemValue="value" required=false />
	            	</dd>
	            </dl>
            </div>
            <@dwz.formBar />
        </@dwz.form>
    </div>
</div>