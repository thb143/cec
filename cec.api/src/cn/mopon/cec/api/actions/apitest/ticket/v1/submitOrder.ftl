<div class="page" layouth="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/submitOrder">
	        <fieldset>
	            <legend>请求出票接口</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>订单号 :</dt>
	                <dd><input type="text" name="orderCode" class="required" value="1401778621777"/></dd>
	            </dl>
	            <dl>
	                <dt>手机号码:</dt>
	                <dd><input type="text" name="mobile" class="required phone" value="18665984724"/></dd>
	            </dl>
	            <dl>
	                <dt>签名:</dt>
	                <dd><input type="text" name="sign" class="required" value="0003"/></dd>
	            </dl>
	            <dl class="nowrap">
	                <dt>座位信息:</dt>
	                <dd><input type="text" name="orderSeats" class="required" size="90" title="格式为 座位编号1:销售价[:渠道结算价+接入费]，座位编号2:销售价[:渠道结算价+接入费]"/></dd>
	            </dl>
	            <dl class="nowrap">
	                <dt>卖品信息:</dt>
	                <dd><input type="text" name="snacks" size="90" title="格式为 卖品1编号:销售价:数量:卖品1渠道结算价，卖品2编号:销售价:数量:卖品2渠道结算价"/></dd>
	            </dl>
	            <dl class="nowrap">
	                <dt>权益卡信息:</dt>
	                <dd><input type="text" name="benefit" size="90" title="格式为 开卡手机号:卡类号:优惠金额"/></dd>
	            </dl>
	            <button type="button" queryType="json" onclick="submitJsonQuery(this)">返回JSON</button>
	            <button type="button" queryType="xml" onclick="submitJsonQuery(this)">返回XML</button>
	        </fieldset>
	    </form>
    </div>
    <div layouth="120"></div>
</div>
