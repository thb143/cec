<@dwz.reload action="/product/benefitCardType-snackRule-view" id="${benefitCardType.id}"/>
<div class="page">
	<div class="pageContent">
	   <@pro.benefitCardTypePolicy_head benefitCardType=benefitCardType/>
		<div class="pageFormContent" layoutH="111">
		   <@pro.benefitCardType_basicInfo benefitCardType=benefitCardType />
		   <#if benefitCardType.isBounded()>
		   		<@pro.benefitCardTypeSnackRule_list benefitCardType=benefitCardType.boundType />
		   <#else>
		   		<@pro.benefitCardTypeSnackRule_list benefitCardType=benefitCardType />
		   </#if>
	    </div>
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    	<@sec.any name="PRODUCT_MANAGE">
				<#if benefitCardType.status == PolicyStatus.SUBMIT>
					<li>
						<@dwz.a href="/product/benefitCardType-submit?typeId=${benefitCardType.id}" target="ajaxTodo" class="button" callback="navTabAjaxDone" title="您是否确定要提交审核？"><span>提交审核</span></@dwz.a>
					</li>
				</#if>
			</@sec.any>
			<#if benefitCardType.isBounded()>
				<#if benefitCardType.boundType.logs?size gt 0>
					<@sec.any name="PRODUCT_VIEW">
							<li>
					            <@dwz.a href="/product/benefitCardTypeLog-view?typeId=${benefitCardType.boundType.id}" target="dialog" class="button" height="S"><span>查看审批记录</span></@dwz.a>
					        </li>
					</@sec.any>
				</#if>
			<#else>
				<#if benefitCardType.logs?size gt 0>
					<@sec.any name="PRODUCT_VIEW">
							<li>
					            <@dwz.a href="/product/benefitCardTypeLog-view?typeId=${benefitCardType.id}" target="dialog" class="button" height="S"><span>查看审批记录</span></@dwz.a>
					        </li>
					</@sec.any>
				</#if>
			</#if>
			<#if benefitCardType.isBounded()>
				<#if benefitCardType.boundType.rules?size gt 0>
					<li>
						<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
			        </li>
		        </#if>
	        <#else>
	        	<#if benefitCardType.rules?size gt 0>
					<li>
						<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
			        </li>
		        </#if>
	        </#if>
	        <@sec.any name="PRODUCT_MANAGE">
	        	<#if benefitCardType.status != PolicyStatus.AUDIT && benefitCardType.status != PolicyStatus.APPROVE>
		        	<li>
		        		<#if benefitCardType.isBounded()>
		        			<@dwz.a href="/product/benefitCardType-snackRule-add?benefitCardTypeId=${benefitCardType.boundType.id}" target="dialog" class="button" height="S"><span>新增规则</span></@dwz.a>
		        		<#else>
		        			<@dwz.a href="/product/benefitCardType-snackRule-add?benefitCardTypeId=${benefitCardType.id}" target="dialog" class="button" height="S"><span>新增规则</span></@dwz.a>
		        		</#if>
		        	</li>
	        	</#if>
	        </@sec.any>
	    </@dwz.formBar>
	</div>
</div>