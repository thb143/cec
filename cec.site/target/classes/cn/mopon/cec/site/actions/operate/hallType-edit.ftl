<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/hallType-update">
			<@s.hidden path="hallType.id" />
	        <div class="pageFormContent" layoutH="56">
	        	<dl>
	                <dt>影厅类型名称：</dt>
	                <dd>
	                	<@s.input path="hallType.name" maxlength="20" class="required" />
	                </dd>
	            </dl>
	        </div>
	        <@dwz.formBar showCancelBtn=false />
        </@dwz.form>
    </div>
</div>