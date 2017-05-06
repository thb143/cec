<div class="page" layouH="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/queryShows">
	        <fieldset>
	            <legend>通过影院查排期接口</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>影院编码:</dt>
	                <dd><input type="text" name="cinemaCode" class="required" value="01010071"/></dd>
	            </dl>
	            <dl>
	                <dt>放映日期:</dt>
	                <dd><input type="text" name="startDate" class="date" value="2014-05-26"/></dd>
	            </dl>
	            <dl>
	                <dt>排期状态:</dt>
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
