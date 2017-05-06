<div class="page">
    <div class="pageContent">
    	<div class="panelBar">
	    	<ul class="toolBar">
	    		<@sec.any name="PRODUCT_SWITCH">
	    			<li><@dwz.a href="/product/show-channel-batch-enable" target="selectedTodo" rel="channelShowIds" callback="dialogReloadDone" title="您是否确定要批量上架渠道排期？"><span class="a16">批量上架</span></@dwz.a></li>
	    			<li><@dwz.a href="/product/show-channel-batch-disable" target="selectedTodo" rel="channelShowIds" callback="dialogReloadDone" title="您是否确定要批量下架渠道排期？"><span class="a17">批量下架</span></@dwz.a></li>
	    		</@sec.any>
	    	</ul>
    	</div>
        <table class="table" width="100%" layoutH="50">
            <thead>
                <tr>
					<th width="30" align="center">序号</th>
					<th width="30" align="center"><@s.checkbox class="checkboxCtrl" group="channelShowIds" /></th>
					<th width="80">渠道名称</th>
					<th width="40" align="center">类型</th>
					<th>影院结算规则</th>
					<th>渠道结算规则</th>
					<th width="45" align="right">最低价</th>
					<th width="45" align="right"><@s.message code="stdPrice"/></th>
					<th width="70" align="right"><@s.message code="channelPrice"/></th>
					<th width="45" align="right"><@s.message code="submitPrice"/></th>
					<th width="45" align="right"><@s.message code="connectFee"/></th>
					<th width="45" align="right"><@s.message code="circuitFee"/></th>
					<th width="45" align="right"><@s.message code="subsidyFee"/></th>
					<th width="40" align="center">状态</th>
					<th width="40" align="center">操作</th>
				</tr>
            </thead>
            <tbody>
                <#list channelShows as channelShow>
					<tr>
						<td>${channelShow_index+1}</td>
						<td>
		                    <#if channelShow.isOnable() || channelShow.isOffable()>
			                   <@s.checkbox name="channelShowIds" value="${channelShow.id}" />
			                </#if>
	                    </td>
						<td>${channelShow.channel.name}</td>
						<td>${channelShow.type}</td>
						<td style="color: ${channelShow.cinemaRuleColor};cursor: default;" title="${channelShow.cinemaRuleSummary}">${channelShow.cinemaRuleName}</td>
						<td style="color: ${channelShow.channelRuleColor};cursor: default;" title="${channelShow.channelRuleSummary}">${channelShow.channelRuleName}</td>
						<td>${channelShow.minPrice?string(",##0.00")}</td>
						<td>${channelShow.stdPrice?string(",##0.00")}</td>
						<td>${channelShow.channelPrice?string(",##0.00")}</td>
						<td>${channelShow.submitPrice?string(",##0.00")}</td>
						<td>${channelShow.connectFee?string(",##0.00")}</td>
						<#if channelShow.circuitFee gt 0>
							<#assign circuitFeeColor = StatusColor.GREEN>
						<#else>
							<#assign circuitFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${circuitFeeColor}">${channelShow.circuitFee?string(",##0.00")}</td>
						<#if channelShow.subsidyFee gt 0>
							<#assign subsidyFeeColor = StatusColor.RED>
						<#else>
							<#assign subsidyFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${subsidyFeeColor}">${channelShow.subsidyFee?string(",##0.00")}</td>
						<td class="${channelShow.status.color}">${channelShow.status}</td>
						<td>
							<@sec.any name="PRODUCT_SWITCH">
	                        	<#if channelShow.isOffable()>
		                        	<@dwz.a href="/product/show-channel-disable?channelShow=${channelShow.id}" target="ajaxTodo" callback="dialogReloadDone" title="您是否确定要下架该渠道排期？">下架</@dwz.a>
		                        </#if>
		                        <#if channelShow.isOnable()>
		                        	<@dwz.a href="/product/show-channel-enable?channelShow=${channelShow.id}" target="ajaxTodo" callback="dialogReloadDone" title="您是否确定要上架该渠道排期？">上架</@dwz.a>
		                        </#if>
			                </@sec.any>
                        </td>
					</tr>
				</#list>
            </tbody>
        </table>
    </div>
</div>