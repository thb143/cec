<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/circuitSettings-save" id="circuitSettingsForm" targetType="navTab">
	        <@s.hidden path="circuitSettings.id"/>
	        <div class="pageFormContent" layoutH="55">
	        	<fieldset>
					<legend>结算方式设置</legend>
		        	<dl class="nowrap">
		                <dt>结算方式：</dt>
		                <dd>
		                	<@dwz.checkboxs path="circuitSettings.settleRuleTypes" items=SettleRuleType?values itemLabel="text" itemValue="value" />
		                </dd>
		            </dl>
				</fieldset>
	           	<fieldset>
					<legend>选座票锁座时间设置</legend>
		        	<dl class="nowrap">
		                <dt>锁定座位时间：</dt>
		                <dd>
		                	<@s.input path="circuitSettings.lockSeatTime" class="required digits" min="1" max="15" maxlength="2" customvalid="regexStopTime(element)" title="锁定座位时间必须是整数，介于1和15之间 。"/><span class="dd-span">（分钟）</span>
		                </dd>
		            </dl>
				</fieldset>
				<fieldset>
					<legend>日结时间设置</legend>
		        	<dl class="nowrap">
		                <dt>日结时间：</dt>
		                <dd>
		                	<@s.input path="circuitSettings.dayStatTime" class="required digits" range="0,6" maxlength="1" title="日结时间必须是整数，介于0和6之间 。"/><span class="dd-span">（统计该小时往后推24小时内的订单）</span>
		                </dd>
		            </dl>
				</fieldset>
				<fieldset>
					<legend>影片默认最低价设置</legend>
		        	<dl style="width:220px">
		                <dt style="width:60px">2D：</dt>
		                <dd style="width:150px"><@s.input path="circuitSettings.defaultMinPrice.normal2d" range="0,1000" class="required number" customvalid="regexPrice(element)" size="10" title="请输入一个0到1000之间的两位小数。"/></dd>
		            </dl>
		            <dl style="width:220px">
		                <dt style="width:60px">3D：</dt>
		                <dd style="width:150px"><@s.input path="circuitSettings.defaultMinPrice.normal3d" range="0,1000" class="required number" customvalid="regexPrice(element)" size="10" title="请输入一个0到1000之间的两位小数。"/></dd>
		            </dl>
		            <dl style="width:220px"> 
		                <dt style="width:60px">MAX2D：</dt>
		                <dd style="width:150px"><@s.input path="circuitSettings.defaultMinPrice.max2d" range="0,1000" class="required number" customvalid="regexPrice(element)" size="10" title="请输入一个0到1000之间的两位小数。"/></dd>
		            </dl>
		            <dl style="width:220px">
		                <dt style="width:60px">MAX3D：</dt>
		                <dd style="width:150px"><@s.input path="circuitSettings.defaultMinPrice.max3d" range="0,1000" class="required number" customvalid="regexPrice(element)" size="10" title="请输入一个0到1000之间的两位小数。"/></dd>
		            </dl>
		            <dl style="width:220px">
		                <dt style="width:60px">DMAX：</dt>
		                <dd style="width:150px"><@s.input path="circuitSettings.defaultMinPrice.dmax" range="0,1000" class="required number" customvalid="regexPrice(element)" size="10" title="请输入一个0到1000之间的两位小数。"/></dd>
		            </dl>
				</fieldset>
				<fieldset>
					<legend>预警通知设置</legend>
		            <dl class="nowrap">
	                    <dt>排期预警用户：</dt>
	                    <dd><@ass.userSelect path="circuitSettings.showWarnUsers"/></dd>
	                </dl>
	                <dl class="nowrap">
	                    <dt>价格预警用户：</dt>
	                    <dd><@ass.userSelect path="circuitSettings.priceWarnUsers"/></dd>
	                </dl>
	                <dl class="nowrap">
	                    <dt>订单预警用户：</dt>
	                    <dd><@ass.userSelect path="circuitSettings.orderWarnUsers"/></dd>
	                </dl>
	                <dl class="nowrap">
	                    <dt>影片预警用户：</dt>
	                    <dd><@ass.userSelect path="circuitSettings.filmWarnUsers"/></dd>
	                </dl>
	                <dl class="nowrap">
	                    <dt>统计通知用户：</dt>
	                    <dd><@ass.userSelect path="circuitSettings.statNoticeUsers"/></dd>
	                </dl>
				</fieldset>
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>