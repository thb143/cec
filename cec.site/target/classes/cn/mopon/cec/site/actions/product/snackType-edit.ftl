<div class="page">
    <div class="pageContent" id="snackTypeEdit">
        <@dwz.form action="/product/snackType-update" >
        	<@s.hidden path="snackType.id" />
        	<@s.hidden path="snackType.group"  />
	        <div class="pageFormContent" layoutH="60">
	            <dl class="nowrap" >
	                <dt>名称：</dt>
	                <dd><@s.input path="snackType.name" maxlength="20" class="required l-input" /></dd>
	            </dl>
	            <dl class="nowrap">
	            	<dt>内容：</dt>
	            	<dd><@s.textarea rows="5"  path="snackType.remark" maxlength="120" class="l-input"/></dd>
	            </dl>
	            <dl class="nowrap">
	                <dt>图片：</dt>
	                <dd><@ass.singleImageUpload path="snackType.image" url="${imagePath}" /></dd>
	            </dl>
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>