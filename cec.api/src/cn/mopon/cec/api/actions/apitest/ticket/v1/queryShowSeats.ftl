<div class="page" layouth="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/queryShowSeats">
	        <fieldset>
	            <legend>获取座位状态图接口</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>渠道排期编码:</dt>
	                <dd><input type="text" name="channelShowCode" class="required" value="1000414070105617"/></dd>
	            </dl>
	            <dl>
	                <dt>座位状态:</dt>
	                <dd><input type="text" name="status" value="1"/></dd>
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
