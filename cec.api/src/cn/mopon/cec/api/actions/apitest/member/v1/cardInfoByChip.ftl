<div class="page" layouth="0">
    <div class="pageFormContent">
        <form class="pageForm required-validate" action="${ctx}/api/member/v1/queryCardInfoByChip">
            <fieldset>
                <legend>获取会员卡信息(芯片号)</legend>
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
