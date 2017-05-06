<div class="page" layouth="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/openBenefitCard">
	        <fieldset>
	            <legend>开卡信息</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>用户手机号:</dt>
	                <dd><input type="text" name="mobile" class="required" value="18682267561"/></dd>
	            </dl>
	            <dl>
	                <dt>卡类编号:</dt>
	                <dd><input type="text" name="typeCode" class="required" value="00001"/></dd>
	            </dl>
	            <dl>
	                <dt>渠道开卡订单号:</dt>
	                <dd><input type="text" name="channelOrderCode" class="required" value="1000414052905411"/></dd>
	            </dl>
	            <dl>
	                <dt>开卡金额:</dt>
	                <dd><input type="text" name="initAmount" class="required" value="200.00"/></dd>
	            </dl>
	            <dl>
	                <dt>会员生日:</dt>
	                <dd><input type="text" name="birthday" value="1990-01-01"/></dd>
	            </dl>
	            <dl>
	                <dt>签名:</dt>
	                <dd><input type="text" name="sign" class="required" value="0003"/></dd>
	            </dl>
	            <button type="button" queryType="json" onclick="submitJsonQuery(this)">返回JSON</button>
	            <button type="button" queryType="xml" onclick="submitJsonQuery(this)">返回XML</button>
	        </fieldset>
	    </form>
    </div>
    <div layouth="120"></div>
</div>
