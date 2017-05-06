<div class="tabs" currentIndex="0" eventType="click">
	<div class="tabsHeader">
        <div class="tabsHeaderContent">
            <ul>
                <li><a href="javascript:;"><span>待办事项</span></a></li>
                <li><a href="javascript:;"><span>将到期影院策略</span></a></li>
                <li><a href="javascript:;"><span>将到期渠道策略</span></a></li>
                <li><a href="javascript:;"><span>将到期特殊定价策略</span></a></li>
            </ul>
        </div>
    </div>
	<div class="tabsContent" style="height:180px;">
        <div class="pageFormContent">
        	<p style="width:260px">
        		<label>待审核影院策略：</label>
			    <label>
			    	<@dwz.a href="/price/cinemaPolicy-audit-list" title="影院结算策略审核">
			    		<span class="dd-span">${todoListModel.auditCinemaPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
            <p style="width:260px">
        		<label>待审批影院策略：</label>
			    <label>
			    	<@dwz.a href="/price/cinemaPolicy-approve-list" title="影院结算策略审批">
			    		<span class="dd-span">${todoListModel.approveCinemaPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
            <p style="width:260px">
        		<label>待审核渠道策略：</label>
			    <label>
			    	<@dwz.a href="/price/channelPolicy-audit-list" title="渠道结算策略审核">
			    		<span class="dd-span">${todoListModel.auditChannelPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
            <p style="width:260px">
        		<label>待审批渠道策略：</label>
			    <label>
			    	<@dwz.a href="/price/channelPolicy-approve-list" title="渠道结算策略审批">
			    		<span class="dd-span">${todoListModel.approveChannelPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
            <p style="width:260px">
        		<label>待审核特价策略：</label>
			    <label>
			    	<@dwz.a href="/price/specialPolicy-audit-list" title="特殊定价策略审核">
			    		<span class="dd-span">${todoListModel.auditSpecialPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
            <p style="width:260px">
        		<label>待审批特价策略：</label>
			    <label>
			    	<@dwz.a href="/price/specialPolicy-approve-list" title="特殊定价策略审批">
			    		<span class="dd-span">${todoListModel.approveSpecialPolicyCount} 条</span>
			    	</@dwz.a>
                </label>
            </p>
        </div>
        <div style="display:none;">
            <table class="list" width="98%">
                <thead>
	                <tr>
	                    <th width="200">影院名称</th>
	                    <th width="200">策略名称</th>
	                    <th width="150">到期时间</th>
	                </tr>
                </thead>
                <tbody>
	                <#list todoListModel.willExpireCinemaPolicys as policy>
		                <tr>
		                    <td>${policy.cinema.name}</td>
		                    <td>${policy.name}</td>
		                    <td>${policy.endDate?date}</td>
		                </tr>
	                </#list>
                </tbody>
            </table>
        </div>
        <div style="display:none;">
            <table class="list" width="98%">
                <thead>
	                <tr>
	                    <th width="200">渠道名称</th>
	                    <th width="200">策略名称</th>
	                    <th width="150">到期时间</th>
	                </tr>
                </thead>
                <tbody>
	                <#list todoListModel.willExpireChannelPolicys as policy>
		                <tr>
		                    <td>${policy.channel.name}</td>
		                    <td>${policy.name}</td>
		                    <td>${policy.endDate?date}</td>
		                </tr>
	                </#list>
                </tbody>
            </table>
        </div>
        <div style="display: none;">
            <table class="list" width="98%">
                <thead>
	                <tr>
	                    <th width="300">特殊定价策略名称</th>
	                    <th>到期时间</th>
	                </tr>
                </thead>
                <tbody>
	                <#list todoListModel.willExpireSpecialPolicys as policy>
		                <tr>
		                    <td>${policy.name}</td>
		                    <td>${policy.endDate?date}</td>
		                </tr>
	                </#list>
                </tbody>
            </table>
        </div>
    </div>
	<div class="tabsFooter">
	    <div class="tabsFooterContent"></div>
	</div>
</div>