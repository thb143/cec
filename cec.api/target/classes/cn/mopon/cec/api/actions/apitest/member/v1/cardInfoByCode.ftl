<div class="page" layouth="0">
    <div class="pageFormContent">
        <form class="pageForm required-validate" action="${ctx}/api/member/v1/queryCardInfoByCode">
            <fieldset>
                <legend>获取会员卡信息(卡号)</legend>
                <dl>
                    <dt>渠道编号:</dt>
                    <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
                </dl>
                <dl>
                    <dt>影院编码：&nbsp;&nbsp;</dt>
                    <dd><input type="text" name="cinemaCode" value="gzxxx" class="required"/></dd>
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
