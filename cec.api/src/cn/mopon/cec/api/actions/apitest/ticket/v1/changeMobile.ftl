<div class="page" layouth="0">
    <div class="pageFormContent">
	    <form class="pageForm required-validate" action="${ctx}/api/ticket/v1/changeMobile">
	        <fieldset>
	            <legend>更改权益卡手机号码</legend>
	            <dl>
	                <dt>渠道编号:</dt>
	                <dd><input type="text" name="channelCode" class="required" value="0003"/></dd>
	            </dl>
	            <dl>
	                <dt>旧手机号码:</dt>
	                <dd><input type="text" name="oldMobile" class="required" value="18682267561"/></dd>
	            </dl>
	            <dl>
	                <dt>新手机号码:</dt>
	                <dd><input type="text" name="newMobile" class="required" value="18682267561"/></dd>
	            </dl>
	            <dl>
	                <dt>卡类编号:</dt>
	                <dd><input type="text" name="typeCode" class="required" value="00001"/></dd>
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
