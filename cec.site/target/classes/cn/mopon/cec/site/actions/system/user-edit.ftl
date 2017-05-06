<div class="page">
    <div class="pageContent">
    	<@dwz.form action="/system/user-update">
	        <@s.hidden path="user.id" />
	        <@s.hidden path="user.settings.id" />
	    	<div class="pageFormContent" layoutH="58">
				<dl>
	                <dt>用户名：</dt>
	                <dd><@s.input path="user.username" maxlength="20" class="required" /></dd>
				</dl>
				<dl>
	                <dt>姓名：</dt>
	                <dd><@s.input path="user.name" maxlength="20" class="required" /></dd>
	            </dl>
	            <@sys.user_settings path="user.settings" />
	            <dl>
	                <dt>序号：</dt>
	                <dd><@s.input path="user.ordinal" min="0" max="99999" class="digits" /></dd>
	            </dl>
			</div>
			<@dwz.formBar />
        </@dwz.form>
    </div>
</div>