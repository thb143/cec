<div class="page" layouth="0">
    <div class="pageFormContent">
        <form class="pageForm required-validate" action="${ctx}/api/member/v1/cardPay">
            <fieldset>
                <legend>会员卡支付</legend>
                <dl>
                    <dt>渠道编号:</dt>
                    <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
                </dl>
                <dl>
                    <dt>订单号：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="orderCode" value="" class="required"/></dd>
                </dl>
                <dl>
                    <dt>座位信息:</dt>
                    <dd><input type="text" name="seatInfos" class="required" value=""/></dd>
                </dl>
                 <dl>
                    <dt>会员卡号:</dt>
                    <dd><input type="text" name="cardCode" class="required" value="00001919"/></dd>
                </dl>
                <dl>
                    <dt>会员卡密码：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="password" value="123123" class="required"/></dd>
                </dl>
                 <dl>
                    <dt>手机号：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="mobile" value="13261120716" class="required"/></dd>
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
