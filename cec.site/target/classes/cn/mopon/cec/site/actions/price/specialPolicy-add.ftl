<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialPolicy-save">
            <div class="pageFormContent" layoutH="60">
              	<dl class="nowrap">
	 				<h3>${specialPolicy.type}</h3>
	 			</dl>
				<div class="divider"></div>
                <@pri.specialPolicy_operate path="specialPolicy" rulesSize=0/>
                 <input type="hidden" class="hallText" />
                <@pri.select_hall path="specialPolicy.halls" />
                <#if specialPolicy.type == SpecialPolicyType.FILM>
                	<@pri.select_film path="specialPolicy.films"/>
                </#if>
 				<@pri.select_channel path="specialPolicy.channels" />
            </div>
            <@dwz.formBar />
        </@dwz.form>
    </div>
</div>