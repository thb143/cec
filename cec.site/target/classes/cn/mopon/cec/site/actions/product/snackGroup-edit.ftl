<div class="page">
    <div class="pageContent" id="snackGroupEdit">
        <@dwz.form action="/product/snackGroup-update" >
        	<@s.hidden path="snackGroup.id" />
	        <div class="pageFormContent" layoutH="60">
	            <dl>
	                <dt>名称：</dt>
	                <dd><@s.input path="snackGroup.name" maxlength="20" class="required" /></dd>
	            </dl>
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>