<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/ticketAccessType-save">
	        <div class="pageFormContent" layoutH="60" id="ticketAccessTypeAdd">
	         <@ope.accessType_operate path="ticketAccessType">
	         	<dl>
	                <dt>适配器类型：</dt>
	                <dd><@s.select path="ticketAccessType.adapter" items=TicketAccessAdapter?values itemValue="value" itemLabel="text" /></dd>             
	            </dl>
	         </@ope.accessType_operate>
	        </div>
	        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
<script>
 $(function(){
	 $("#ticketAccessTypeAdd").find("input:radio:first").attr('checked', true);
	 $("#ticketAccessTypeAdd").find("input:radio").each(function(i){
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