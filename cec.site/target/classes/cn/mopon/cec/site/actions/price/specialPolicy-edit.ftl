<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialPolicy-update">
        	<@s.hidden path="specialPolicy.id" />
            <div class="pageFormContent" layoutH="60">
                <dl class="nowrap">
	 				<h3>${specialPolicy.type}</h3>
	 			</dl>
				<div class="divider"></div>
                <@pri.specialPolicy_operate path="specialPolicy" rulesSize=specialPolicy.rules?size/>
                <#if specialPolicy.rules?size == 0>
	                <@pri.select_hall path="specialPolicy.halls" hallMList=hallList />
	                <#if specialPolicy.type == SpecialPolicyType.FILM>
	                	<@pri.select_film  path="specialPolicy.films" filmList=specialPolicy.films/>
	                </#if>
	 				<@pri.select_channel path="specialPolicy.channels" channelList=specialPolicy.channels policyId=specialPolicy.id />
            	</#if>
            </div>
            <@dwz.formBar />
        </@dwz.form>
    </div>
</div>