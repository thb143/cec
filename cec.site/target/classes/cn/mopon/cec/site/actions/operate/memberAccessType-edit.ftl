<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/memberAccessType-update">
        	<@s.hidden path="memberAccessType.id" />
	        <div class="pageFormContent" layoutH="56" id="memberAccessTypeEdit">
	        	<div style="width:800px;">
			        <@ope.accessType_operate path="memberAccessType">
			         	<dl>
			                <dt>适配器类型：</dt>
			                <dd><@s.select path="memberAccessType.adapter" items=MemberAccessAdapter?values itemValue="value" itemLabel="text" /></dd>             
			            </dl>
			        </@ope.accessType_operate>
		        </div>
	        </div>
	        <@dwz.formBar showCancelBtn=false />
        </@dwz.form>
    </div>
</div>
<script>
 $(function(){
	 if("${memberAccessType.model}" ==  "${AccessModel.CENTER}"){
		 $("#param",".page").show();
		 $("#url",".page").addClass("required").addClass("url");
		 $("#username",".page").addClass("required");
		 $("#password",".page").addClass("required");
	 }else{
		 $("#param",".page").hide();
		 $("#url",".page").removeClass("required").removeClass("url");
		 $("#username",".page").removeClass("required");
		 $("#password",".page").removeClass("required");
	 }
	 $("#memberAccessTypeEdit").find("input:radio").each(function(i){
		 $(this).click(function(){
			 if(i == 1){
				 $("#param",$(this).getPageDiv()).hide();
				 $("#url",$(this).getPageDiv()).removeClass("required").removeClass("url");
				 $("#username",$(this).getPageDiv()).removeClass("required");
				 $("#password",$(this).getPageDiv()).removeClass("required");
			 }else{
				 $("#url",$(this).getPageDiv()).addClass("required").addClass("url");
				 $("#username",$(this).getPageDiv()).addClass("required");
				 $("#password",$(this).getPageDiv()).addClass("required");
				 $("#param",$(this).getPageDiv()).show();
			 }
		 });
	 });
 });
</script>