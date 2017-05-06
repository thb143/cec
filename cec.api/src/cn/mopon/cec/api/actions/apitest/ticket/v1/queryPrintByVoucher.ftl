<div class="page" layouth="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/queryPrintByVoucher">
	        <fieldset>
	            <legend>通过凭证编号查询打票状态接口</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>订单编码 :</dt>
	                <dd><input type="text" name="orderCode" class="required" value="7"/></dd>
	            </dl>
	            <dl>
	                <dt> 凭证编号 :</dt>
	                <dd><input type="text" name="voucherCode" class="required" value="Bs5526895356"/></dd>
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
