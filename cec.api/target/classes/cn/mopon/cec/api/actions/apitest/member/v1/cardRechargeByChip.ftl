<div class="page" layouth="0">
    <div class="pageFormContent">
        <form class="pageForm required-validate" action="${ctx}/api/member/v1/cardRechargeByChip">
            <fieldset>
                <legend>会员卡充值(芯片号)</legend>
                <dl>
                    <dt>渠道编号:</dt>
                    <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
                </dl>
                <dl>
                    <dt>影院编码：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="cinemaCode" value="gzxxx" class="required"/></dd>
                </dl>
                 <dl>
                    <dt>芯片号:</dt>
                    <dd><input type="text" name="chipCode" class="required" value="5t1236wq5"/></dd>
                </dl>
                <dl class="nowrap">
                    <dt>充值金额：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="money" value="100.7" class="required"/>整型（单位:元）</dd>
                </dl>
                <dl class="nowrap">
                    <dt>外部充值流水号：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="partnerDepositCode" value="asdfas" class="required"/></dd>
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
