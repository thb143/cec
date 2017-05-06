<div class="page">
    <div class="pageContent">
        <@dwz.form action="/system/person-set-save">
        	<@s.hidden path="userSettings.id" />
	        <div class="pageFormContent" layoutH="60">
	        	<@sys.user_settings path="userSettings" />
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
