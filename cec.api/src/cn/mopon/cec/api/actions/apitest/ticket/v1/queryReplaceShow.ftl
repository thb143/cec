<div class="page" layouth="0">
    <div class="pageFormContent">
        <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/queryReplaceShow">
            <fieldset>
                <legend>查询替换排期信息接口</legend>
                <dl>
                    <dt>渠道编号:</dt>
                    <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
                </dl>
                <dl>
                    <dt>渠道场次编码:</dt>
                    <dd><input type="text" name="channelShowCode" value="141000005262" class="required"/></dd>
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
