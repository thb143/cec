<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/hallType-save">
	        <div class="pageFormContent" layoutH="60">
	            <dl>
	                <dt>影厅类型名称：</dt>
	                <dd><@s.input path="hallType.name" maxlength="20" class="required" /></dd>
	            </dl>
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
