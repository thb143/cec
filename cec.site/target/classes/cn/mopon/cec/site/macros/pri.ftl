<#-- 结算策略标题信息 -->
<#macro cinemaPolicy_head cinemaPolicy=cinemaPolicy show=true>
	<div style="border-bottom:1px solid #CCC;padding-bottom:7px;">
		<div style="font-size:15px;font-weight:bold;line-height:34px;padding-left:5px;">${cinemaPolicy.name}</div>
		<div>
			【<b>
				<span class="${cinemaPolicy.status.color}">${cinemaPolicy.status}</span> - 
				<span class="${cinemaPolicy.valid.color}">${cinemaPolicy.valid}</span> - 
				<span class="${cinemaPolicy.enabled.color}">${cinemaPolicy.enabled}</span>
			</b>】
			【 策略有效期限：${cinemaPolicy.startDate?date} - ${cinemaPolicy.endDate?date} 】【 上报类型：${cinemaPolicy.submitType}<#if (cinemaPolicy.amount >= 0)>+</#if>${cinemaPolicy.amount?string("0.00")}元 】
    	</div>
    	<#if show>
    		<div class="searchBar" style="top: 27px; position: absolute; right: 4px;" id="searchRule">
    			<div class="subBar">
    				<ul>
    					<li><@s.select path="cinemaRuleModel.cinemaRuleStatus" headerLabel="全部" items=RuleStatus?values itemValue="value" itemLabel="text" value="${cinemaRuleModel.cinemaRuleStatus.value}"/></li>
    				</ul>
    			</div>
    		</div>
    	</#if>
    </div>
</#macro>

<#-- 结算策略审批信息 -->
<#macro cinemaPolicyLog_view cinemaPolicyLog=cinemaPolicyLog>
	<dl class="nowrap">
		<h3>审批信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>提交人：</dt>
		<dd>${cinemaPolicyLog.submitter.name}</dd>
	</dl>
	<dl>
		<dt>提交时间：</dt>
		<dd>${cinemaPolicyLog.submitTime?datetime}</dd>
	</dl>
	<#if cinemaPolicyLog.auditor??>
		<dl>
			<dt>审核人：</dt>
			<dd>${cinemaPolicyLog.auditor.name}</dd>
		</dl>
		<dl>
			<dt>审核时间：</dt>
			<dd>${cinemaPolicyLog.auditTime?datetime}</dd>
		</dl>
	</#if>
	<#if cinemaPolicyLog.approver??>
		<dl>
			<dt>审批人：</dt>
			<dd>${cinemaPolicyLog.approver.name}</dd>
		</dl>
		<dl>
			<dt>审批时间：</dt>
			<dd>${cinemaPolicyLog.approveTime?datetime}</dd>
		</dl>
	</#if>
</#macro>

<#-- 结算策略按钮栏 -->
<#macro cinemaPolicy_bar policy=cinemaPolicy treeId="cinemaRuleSubmit">
	<#local isApprove = (policy.status == PolicyStatus.AUDIT || policy.status == PolicyStatus.APPROVE)>
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
    	<@sec.any name="POLICY_MANAGE">
			<#if policy.status == PolicyStatus.SUBMIT && !policy.expired && policy.rules?size gt 0>
				<li>
					<@dwz.a href="/price/cinemaPolicy-submit?policyId=${policy.id}" target="ajaxTodo" class="button" callback="navTabAjaxDone" title="您是否确定要提交审核？"><span>提交审核</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<#if policy.logs?size gt 0>
			<li>
				<@dwz.a href="/price/cinemaPolicyLog-view?policyId=${policy.id}" target="dialog" class="button" title="查看策略审批记录"><span>审批记录</span></@dwz.a>
			</li>
		</#if>
		<#if policy.rules?size gt 0>
			<li>
	            <a id="collapseBtn_${policy.status}" class="button" href="javascript:void(0);"><span>全部展开/收缩</span></a>
	        </li>
        </#if>
        <@sec.any name="PRODUCT_MANAGE">
	        <#if policy.valid == ValidStatus.VALID && policy.enabled == EnabledStatus.ENABLED && !policy.getExpired()>
				<li>
					<@dwz.a href="/product/cinemaPolicy-show-list?cinemaPolicy=${policy.id}" target="dialog" class="button" width="L" height="L" title="策略排期检索"><span>排期检索</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<@sec.any name="POLICY_SWITCH">
	        <#if !isApprove && (!policy.expired || policy.valid == ValidStatus.UNVALID)>
		        <li>
		            <@dwz.a href="/price/cinemaPolicy-edit?policyId=${policy.id}" target="dialog" class="button" height="S"><span>编辑策略</span></@dwz.a>
		        </li>
	        </#if>
        </@sec.any>
    </@dwz.formBar>
	<script>
		$(function(){
			var collapseBtn = $("#collapseBtn_${policy.status}");
			collapseBtn.click(function(){
				expanAll("${treeId}",3);
			});
		});
	</script>
</#macro>

<#-- 结算规则信息 -->
<#macro cinemaRule_list cinemaRuleList=cinemaRuleList policy=cinemaPolicy logId=logId rel="policyBox" show=true treeId="cinemaRuleSubmit">
	<#local isApprove = (policy.status == PolicyStatus.AUDIT || policy.status == PolicyStatus.APPROVE) />
	<#local policyLogId = policy.lastRefusedLog.id />
	<#local randomNum = .now?datetime?string("yyyyMMddHHmmssSSS")/>
	<#if isApprove>
		<#local colspan="4" />
	<#elseif policy.status == PolicyStatus.SUBMIT || policy.status == PolicyStatus.BACKED>
		<#local colspan="3" />
	<#else>
		<#local colspan="2" />
	</#if>
	<table id="${treeId}" style="width:100%;">
		<thead>
			<th width="300">规则</th>
			<th>价格</th>
			<#if policy.status != PolicyStatus.APPROVED>
				<th width="30">状态</th>
			</#if>
			<th width="240">操作</th>
		</thead>
		<tbody>
			<#list cinemaRuleList.items as item>
				<tr data-tt-id="${item.showType.value}_${randomNum}" class="expanded" depth="1">
					<td colspan="${colspan}" style="font-weight: bold;">${item.showType}</td>
					<#if !isApprove>
						<td>
							<@sec.any name="POLICY_MANAGE">
					    		<#if !policy.expired>
					    			<@dwz.a href="/price/cinemaRule-add?policyId=${policy.id}&showType=${item.showType.value}" target="dialog" title="新增规则【${item.showType}】">[新增规则]</@dwz.a>
					    		</#if>
				    		</@sec.any>
				    		&nbsp;
			    		</td>
		    		</#if>
				</tr>
				<#list item.rules as rule>
				<#local isOperate = (rule.status == RuleStatus.AUDITPASS || rule.status == RuleStatus.APPROVEPASS) />
				<tr data-tt-id="${rule.id}_${randomNum}" data-tt-parent-id="${item.showType.value}_${randomNum}" depth="2">
					 <#if rule.isBounded() && rule.status != RuleStatus.REFUSE>
	   					<#local cinemaRuleNameColor = StatusColor.ORANGE>
		   			<#elseif (!rule.isBounded() && rule.enabled == EnabledStatus.ENABLED && rule.valid == ValidStatus.VALID)>
		   				<#local cinemaRuleNameColor = StatusColor.GREEN>
		   			<#elseif !rule.isBounded() && ((rule.enabled == EnabledStatus.DISABLED && rule.valid == ValidStatus.VALID) || rule.valid == ValidStatus.INVALID)>
		   				<#local cinemaRuleNameColor = StatusColor.GRAY>
		   			<#else>
		   				<#local cinemaRuleNameColor = StatusColor.BLACK>
		   			</#if>
					<#if rule.isBounded()>
						<#if rule.boundRule.name != rule.name>
							<td class="${cinemaRuleNameColor}" title="${rule.boundRule.name}"><strong>${rule.name}</strong></td>
						<#else>
							<td><strong>${rule.name}</strong></td>
						</#if>
						<#if rule.boundRule.settleRule.toString() != rule.settleRule.toString()>
							<td class="${cinemaRuleNameColor}" title="${rule.boundRule.settleRule.toString()}">${rule.settleRule.toString()}</td>
						<#else>
							<td>${rule.settleRule.toString()}</td>
						</#if>
					<#elseif !rule.isBounded() && (isOperate || rule.valid != ValidStatus.UNVALID)>
						<#if rule.valid == ValidStatus.INVALID>
							<td class="${cinemaRuleNameColor}"><strong><del>&nbsp;${rule.name}&nbsp;</del></strong></td>
							<td class="${cinemaRuleNameColor}"><del>&nbsp;${rule.settleRule.toString()}&nbsp;</del></td>
						<#else>
							<td class="${cinemaRuleNameColor}"><strong>${rule.name}</strong></td>
							<td class="${cinemaRuleNameColor}">${rule.settleRule.toString()}</td>
						</#if>
					<#else>
						<td><strong>${rule.name}</strong></td>
						<td>${rule.settleRule.toString()}</td>
					</#if>
					<#if rule.isUnAudit()>
						<td><span class="a13"></span></td>
					<#elseif isOperate>
						<td><span class="a19"></span></td>
					<#elseif rule.status == RuleStatus.REFUSE>
						<td><span class="a20"></span></td>
					<#elseif policy.status != PolicyStatus.APPROVED>
						<td><span></span></td>
					</#if>
						<td>
							<#if ((policy.status == PolicyStatus.SUBMIT || policy.status == PolicyStatus.BACKED)&& (rule.status == RuleStatus.UNAUDIT || rule.status == RuleStatus.REFUSE)
								|| policy.status == PolicyStatus.APPROVED) && rule.valid != ValidStatus.INVALID>
								<#if !policy.expired>
									<@sec.any name="POLICY_MANAGE">
							    		<#if rule.isBounded() && rule.valid == ValidStatus.VALID>
							        		<@dwz.a href="/price/cinemaRule-edit?cinemaRuleId=${rule.boundRule.id}" target="dialog" title="编辑规则【${item.showType}】">[编辑]</@dwz.a>
							      		<#else>
							      			<@dwz.a href="/price/cinemaRule-edit?cinemaRuleId=${rule.id}" target="dialog" title="编辑规则【${item.showType}】">[编辑]</@dwz.a>
							      		</#if>
							      		<@dwz.a href="/price/cinemaRule-copy-edit?cinemaRule=${rule.id}" target="dialog" width="M" height="M" title="复制规则">[复制]</@dwz.a>
										<!-- <@dwz.a href="/price/cinemaRule-copy?cinemaRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要复制该规则？">[复制]</@dwz.a> -->
							       		<#if rule.valid != ValidStatus.VALID>
							    		<@dwz.a href="/price/cinemaRule-delete?cinemaRuleId=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
							    		</#if>
						     		</@sec.any>
								</#if>
								<#if rule.valid == ValidStatus.VALID && policy.valid == ValidStatus.VALID && !policy.expired>
									<@sec.any name="POLICY_SWITCH">
										<#if rule.enabled == EnabledStatus.ENABLED>
											<@dwz.a href="/price/cinemaRule-disable?cinemaRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="停用规则将作废相关的渠道排期，您是否确定要停用该规则？">[停用]</@dwz.a> 
										<#else>
											<@dwz.a href="/price/cinemaRule-enable?cinemaRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="启用规则将重新生成相关的渠道排期，您是否确定要启用该规则？">[启用]</@dwz.a>
										</#if>
									</@sec.any>
									<@sec.any name="PRODUCT_MANAGE">
										<#if rule.enabled == EnabledStatus.ENABLED && policy.enabled == EnabledStatus.ENABLED>
											<@dwz.a href="/product/cinemaRule-show-list?cinemaRule=${rule.id}" target="dialog" width="L" height="L" title="规则排期检索">[排期检索]</@dwz.a>
										</#if>
									</@sec.any>
								</#if>
							</#if>
							<#if !show && isApprove>
								<#if rule.valid == ValidStatus.UNVALID>
									<#if policy.status == PolicyStatus.AUDIT>
										<a href="javascript:void(0);" alias="signPass" name="cinemaRule-sign-audit-pass" id="${rule.id}">[标记通过]</a>
										<a href="javascript:void(0);" alias="signRefuse" name="cinemaRule-sign-audit-refuse" id="${rule.id}">[标记退回]</a>
									<#else>
										<a href="javascript:void(0);" alias="signPass" name="cinemaRule-sign-approve-pass" id="${rule.id}">[标记通过]</a>
										<a href="javascript:void(0);" alias="signRefuse" name="cinemaRule-sign-approve-refuse" id="${rule.id}">[标记退回]</a>
									</#if>
								</#if>
							</#if>
							&nbsp;
						</td>
					</tr>
					<#if policy.status == PolicyStatus.APPROVED>
						<#local cols ="3" />
					<#else>
						<#local cols ="4" />
					</#if>
					<tr data-tt-id="${rule.id}_${randomNum}_1" data-tt-parent-id="${rule.id}_${randomNum}" depth="3">
						<td colspan="${cols}">
							<#if rule.isBounded()>
								<#if rule.boundRule.periodRule.toString() != rule.periodRule.toString()>
									<span class="${cinemaRuleNameColor}" title='${rule.boundRule.periodRule.toString()}'>${rule.periodRule.toString()}</span>
								<#else>
									<span>${rule.periodRule.toString()}</span>
								</#if>
							<#else>
								<span>${rule.periodRule.toString()}</span>
							</#if>
						</td>
					</tr>
					<tr data-tt-id="${rule.id}_${randomNum}_2" data-tt-parent-id="${rule.id}_${randomNum}" depth="3">
						<td colspan="${cols}">
							<#if rule.isBounded()>
								<#if rule.boundRule.hallsText != rule.hallsText>
									<span class="${cinemaRuleNameColor}" title='${rule.boundRule.hallsText}'>影厅：${rule.hallsText}</span>
								<#else>
									<span>影厅：${rule.hallsText}</span>
								</#if>
							<#else>
								<span>影厅：${rule.hallsText}</span>
							</#if>
						</td>
					</tr>
				</#list>
			</#list>
		</tbody>
	</table>
	<script type="text/javascript">
	    var cinemaRuleTree = $("#${treeId}");
	    cinemaRuleTree.treetable({
			expandable : true
		});
		$(function(){
			//默认展开一级节点。
			cinemaRuleTree.find("tr[depth = '1']").each(function(){
				cinemaRuleTree.treetable('expandNode',$(this).attr('data-tt-id'))
			});
			signRule(cinemaRuleTree);
			$("#${treeId} tbody").on("mouseover", "tr", function() {
				$(".selected","#${treeId}").not(this).removeClass("selected");
				$(this).toggleClass("selected");
			});
		});
	</script>
</#macro>

<#-- 渠道策略标题信息 -->
<#macro channelPolicy_head channelPolicy=channelPolicy show=true searchCinema=false>
    <div style="border-bottom:1px solid #CCC;padding-bottom:7px;">
    	<div style="font-size:15px;font-weight:bold;line-height:34px;padding-left:5px;">${channelPolicy.name}</div>
    	<div>
    		【<b>
				<span class="${channelPolicy.status.color}">${channelPolicy.status}</span> - 
				<span class="${channelPolicy.valid.color}">${channelPolicy.valid}</span> - 
				<span class="${channelPolicy.enabled.color}">${channelPolicy.enabled}</span>
			</b>】
			【 策略有效期限：${channelPolicy.startDate?date} - ${channelPolicy.endDate?date} 】
    	</div>
    	<#if show>
		    <div class="searchBar" style="top: 25px; position: absolute; right: 4px;" id="searchRule">
		       	<div class="subBar">
		            <ul>
		               <li selectName="${treeName}"><@s.select path="channelPolicyLog.policy.ruleStatus" headerLabel="全部" items=RuleStatus?values itemValue="value" itemLabel="text" value="${channelPolicyLog.policy.ruleStatus}"/></li>
		            </ul>
		        </div>
		    </div>
		</#if>
		<#if searchCinema>
			<@dwz.pageForm action="/price/channelPolicy-cinema-list" targetType="div" rel="channelPolicyBox" buttonText="筛选" alt="可输入影院名称、城市名检索">
			 	<input type="hidden" name="channelPolicyId" value="${channelPolicy.id}"/>
	    	</@dwz.pageForm>
		</#if>
    </div>
</#macro>

<#-- 渠道规则信息 -->
<#macro channelRule_list channelPolicy=channelPolicy treeName=treeName show=true>
	<#local isApprove = (channelPolicy.status == PolicyStatus.AUDIT || channelPolicy.status == PolicyStatus.APPROVE)>
	<#if isApprove>
		<#local colspan="4" />
		<#local cinemaColspan="3" />
	<#elseif channelPolicy.status == PolicyStatus.SUBMIT || channelPolicy.status == PolicyStatus.BACKED>
		<#local colspan="3" />
		<#local cinemaColspan="2" />
	<#else>
		<#local colspan="2" />
		<#local cinemaColspan="1" />
	</#if>
	<table id="${treeName}" style="width:100%;">
		<#if channelPolicy.channelRuleGroupModels?size gt 0 || !show>
			<thead>
				<th width="300">规则</th>
				<th>价格</th>
				<#if channelPolicy.status != PolicyStatus.APPROVED>
					<th width="30">状态</th>
				</#if>
				<th width="240">操作</th>
			</thead>
		</#if>
		<tbody>
			<#list channelPolicy.channelRuleGroupModels as groupModel>
				<#local cinemaNameColor = groupModel.group.ruleGroupColor />
				<tr data-tt-id="${groupModel.group.cinema.id}_${randomNum}" class="expanded" depth="1" style="background-color:#F0F8FF ">
			    	<td class="${cinemaNameColor}" style="font-weight: bold;">${groupModel.group.cinema.name}</td>
			    	<td colspan="${cinemaColspan}"><@s.message code="connectFee"/>：${groupModel.group.connectFee?string("0.00")}</td>
			    	<#if !isApprove>
			    		<td>
			    			<#if !channelPolicy.expired>
								<@sec.any name="POLICY_MANAGE">
									<#assign setConnectFee>设置<@s.message code="connectFee"/></#assign>
									<@dwz.a href="/price/channelRuleGroup-edit?channelRuleGroup=${groupModel.group.id}" target="dialog" width="SS" height="SSS" title=setConnectFee>[设置<@s.message code="connectFee"/>]</@dwz.a>
						      		<#if groupModel.group.rules?size gt 0 && channelPolicy.channelRuleGroupModels?size gt 1>
						    			<@dwz.a href="/price/channelRuleGroup-copy-select?groupId=${groupModel.group.id}&policyId=${groupModel.group.policy.id}" target="dialog" height="S" title="复制到影院">[复制到影院]</@dwz.a>
						    		</#if>
						    		<#if !(groupModel.group.rules?size gt 0) || channelPolicy.valid == ValidStatus.UNVALID>
						    			<@dwz.a href="/price/channelRuleGroup-delete?groupId=${groupModel.group.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该影院？">[删除]</@dwz.a>
						    		</#if>
						    		<#if groupModel_index == 0 && groupModel_index == channelPolicy.channelRuleGroupModels?size-1>
						    		<#elseif groupModel_index == 0>
						    			<@dwz.a href="/price/channelRuleGroup-down?groupId=${groupModel.group.id}" target="ajaxTodo" alias="movedown">[下移]</@dwz.a>
						    		<#elseif groupModel_index == channelPolicy.channelRuleGroupModels?size-1>
						    			<@dwz.a href="/price/channelRuleGroup-up?groupId=${groupModel.group.id}" target="ajaxTodo" alias="moveup">[上移]</@dwz.a>
						    		<#else>
						    			<@dwz.a href="/price/channelRuleGroup-down?groupId=${groupModel.group.id}" target="ajaxTodo" alias="movedown">[下移]</@dwz.a>
						    			<@dwz.a href="/price/channelRuleGroup-up?groupId=${groupModel.group.id}" target="ajaxTodo" alias="moveup">[上移]</@dwz.a>
						    		</#if>
					     		</@sec.any>
							</#if>
							<#if channelPolicy.valid == ValidStatus.VALID && !channelPolicy.expired>
								<@sec.any name="POLICY_SWITCH">
									<#if groupModel.group.status == OpenStatus.OPENED>
										<@dwz.a href="/price/channelRuleGroup-close?channelRuleGroup=${groupModel.group.id}" callback="navTabAjaxDone" target="ajaxTodo" title="关闭影院会将作废相关的渠道排期，您是否确定要关闭该影院？">[关闭]</@dwz.a>
									<#else>
										<@dwz.a href="/price/channelRuleGroup-open?channelRuleGroup=${groupModel.group.id}" callback="navTabAjaxDone" target="ajaxTodo" title="开放影院将重新生成相关的渠道排期，您是否确定要开放该影院？">[开放]</@dwz.a>
									</#if>
								</@sec.any>
							</#if>
							<@sec.any name="PRODUCT_MANAGE">
								<#if channelPolicy.valid == ValidStatus.VALID && channelPolicy.enabled == EnabledStatus.ENABLED && !channelPolicy.expired && groupModel.group.status == OpenStatus.OPENED && groupModel.group.hasValidAndEnabledRules()>
									<@dwz.a href="/product/channelRuleGroup-show-list?channelRuleGroup=${groupModel.group.id}" target="dialog" width="L" height="L" title="排期检索">[排期检索]</@dwz.a>
								</#if>
							</@sec.any>
						</td>
				    </#if>
				</tr>
				<#list groupModel.items as item>
					<tr data-tt-id="${groupModel.group.cinema.id + item.showType.value}_${randomNum}" data-tt-parent-id="${groupModel.group.cinema.id}_${randomNum}" depth="2">
				    	<td colspan="${colspan}" style="font-weight: bold;">${item.showType}</td>
				    	<#if !isApprove>
				    		<td>
					    		<@sec.any name="POLICY_MANAGE">
						    		<#if !channelPolicy.expired>
						    			<@dwz.a href="/price/channelRule-add?groupId=${groupModel.group.id}&showType=${item.showType.value}" target="dialog" title="新增规则【${item.showType}】">[新增规则]</@dwz.a>
						    		</#if>
					    		</@sec.any>
					    		&nbsp;
				    		</td>
				    	</#if>
					</tr>
					<#list item.rules as rule>
						<#local isOperate = (rule.status == RuleStatus.AUDITPASS || rule.status == RuleStatus.APPROVEPASS) />
						<tr data-tt-id="${rule.id}_${randomNum}" data-tt-parent-id="${groupModel.group.cinema.id + item.showType.value}_${randomNum}" depth="3">
							<#if rule.isBounded() && rule.status != RuleStatus.REFUSE>
				   				<#local channelRuleNameColor = StatusColor.ORANGE>
				   			<#elseif (!rule.isBounded() && rule.enabled == EnabledStatus.ENABLED && rule.valid == ValidStatus.VALID) >
				   				<#local channelRuleNameColor = StatusColor.GREEN>
				   			<#elseif !rule.isBounded() && ((rule.enabled == EnabledStatus.DISABLED && rule.valid == ValidStatus.VALID) || rule.valid == ValidStatus.INVALID)>
				   				<#local channelRuleNameColor = StatusColor.GRAY>
				   			<#else>
				   				<#local channelRuleNameColor = StatusColor.BLACK>
				   			</#if>
				   			<#if rule.isBounded()>
								<#if rule.boundRule.name != rule.name>
									<td class="${channelRuleNameColor}" title="${rule.boundRule.name}"><strong>${rule.name}</strong></td>
								<#else>
									<td><strong>${rule.name}</strong></td>
								</#if>
								<#if rule.boundRule.settleRule.toString() != rule.settleRule.toString()>
									<td class="${channelRuleNameColor}" title="${rule.boundRule.settleRule.toString()}">${rule.settleRule.toString()}</td>
								<#else>
									<td>${rule.settleRule.toString()}</td>
								</#if>
							<#elseif !rule.isBounded() && (isOperate || rule.valid != ValidStatus.UNVALID)>
								<#if rule.valid == ValidStatus.INVALID>
									<td class="${channelRuleNameColor}"><strong><del>&nbsp;${rule.name}&nbsp;</del></strong></td>
									<td class="${channelRuleNameColor}"><del>&nbsp;${rule.settleRule.toString()}&nbsp;</del></td>
								<#else>
									<td class="${channelRuleNameColor}"><strong>${rule.name}</strong></td>
									<td class="${channelRuleNameColor}">${rule.settleRule.toString()}</td>
								</#if>
							<#else>
								<td><strong>${rule.name}</strong></td>
								<td>${rule.settleRule.toString()}</td>
							</#if>
							<#if rule.isUnAudit()>
								<td><span class="a13"></span></td>
							<#elseif isOperate>
								<td><span class="a19"></span></td>
							<#elseif rule.status == RuleStatus.REFUSE>
								<td><span class="a20"></span></td>
							<#elseif channelPolicy.status != PolicyStatus.APPROVED>
								<td><span></span></td>
							</#if>
							<td>
								<#if  ((channelPolicy.status == PolicyStatus.SUBMIT || channelPolicy.status == PolicyStatus.BACKED)&& (rule.status == RuleStatus.UNAUDIT || rule.status == RuleStatus.REFUSE)
									|| channelPolicy.status == PolicyStatus.APPROVED) && rule.valid != ValidStatus.INVALID>
					    			<#if !channelPolicy.expired>
										<@sec.any name="POLICY_MANAGE">
								      		<#if rule.isBounded() && rule.valid == ValidStatus.VALID>
									      		<@dwz.a href="/price/channelRule-edit?channelRuleId=${rule.boundRule.id}" target="dialog" title="编辑规则【${item.showType}】">[编辑]</@dwz.a>
											<#else>
												<@dwz.a href="/price/channelRule-edit?channelRuleId=${rule.id}" target="dialog" title="编辑规则【${item.showType}】">[编辑]</@dwz.a>
									    	</#if>
									    	<@dwz.a href="/price/channelRule-copy-edit?channelRule=${rule.id}" target="dialog" width="M" height="M" title="复制规则">[复制]</@dwz.a>
											<!-- <@dwz.a href="/price/channelRule-copy?channelRuleId=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要复制该规则？">[复制]</@dwz.a> -->
											<#if rule.valid != ValidStatus.VALID>
												<@dwz.a href="/price/channelRule-delete?channelRuleId=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
											</#if>
							     		</@sec.any>
									</#if>
									<#if rule.valid == ValidStatus.VALID && channelPolicy.valid == ValidStatus.VALID && !channelPolicy.expired>
										<@sec.any name="POLICY_SWITCH">
											<#if rule.enabled == EnabledStatus.ENABLED>
												<@dwz.a href="/price/channelRule-disable?channelRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="停用规则将作废相关的渠道排期，您是否确定要停用该规则？">[停用]</@dwz.a>
											<#else>
												<@dwz.a href="/price/channelRule-enable?channelRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="启用规则将重新生成相关的渠道排期，您是否确定要启用该规则？">[启用]</@dwz.a>
											</#if>
										</@sec.any>
										<@sec.any name="PRODUCT_MANAGE">
											<#if rule.enabled == EnabledStatus.ENABLED && channelPolicy.enabled == EnabledStatus.ENABLED && groupModel.group.status == OpenStatus.OPENED>
												<@dwz.a href="/product/channelRule-show-list?channelRule=${rule.id}" target="dialog" width="L" height="L" title="排期检索">[排期检索]</@dwz.a>
											</#if>
										</@sec.any>
									</#if>
							    </#if>
								<#if !show && isApprove>
									<#if rule.valid == ValidStatus.UNVALID>
										<#if channelPolicy.status == PolicyStatus.AUDIT>
											<a href="javascript:void(0);" alias="signPass" name="channelRule-sign-audit-pass" id="${rule.id}">[标记通过]</a> 
											<a href="javascript:void(0);" alias="signRefuse" name="channelRule-sign-audit-refuse" id="${rule.id}">[标记退回]</a>
										<#else>
											<a href="javascript:void(0);" alias="signPass" name="channelRule-sign-approve-pass" id="${rule.id}">[标记通过]</a> 
											<a href="javascript:void(0);" alias="signRefuse" name="channelRule-sign-approve-refuse" id="${rule.id}">[标记退回]</a>
										</#if>
									</#if>
								</#if>
								&nbsp;
							</td>
						</tr>
						<#if channelPolicy.status == PolicyStatus.APPROVED>
							<#local cols ="3" />
						<#else>
							<#local cols ="4" />
						</#if>
						<tr data-tt-id="${rule.id+rule_index}_${randomNum}_1" data-tt-parent-id="${rule.id}_${randomNum}" depth="4">
							<td colspan="${cols}">
								<#if rule.isBounded()>
									<#if rule.boundRule.periodRule.toString() != rule.periodRule.toString()>
										<span class="${channelRuleNameColor}" title='${rule.boundRule.periodRule.toString()}'>${rule.periodRule.toString()}</span>
									<#else>
										<span>${rule.periodRule.toString()}</span>
									</#if>
								<#else>
									<span>${rule.periodRule.toString()}</span>
								</#if>
							</td>
						</tr>
						<tr data-tt-id="${rule.id+rule_index}_${randomNum}_2" data-tt-parent-id="${rule.id}_${randomNum}" depth="4">
							<td colspan="${cols}">
								<#if rule.isBounded()>
									<#if rule.boundRule.hallsText != rule.hallsText>
										<span class="${channelRuleNameColor}" title='影厅：${rule.boundRule.hallsText}'>影厅：${rule.hallsText}</span>
									<#else>
										<span>影厅：${rule.hallsText}</span>
									</#if>
								<#else>
									<span>影厅：${rule.hallsText}</span>
								</#if>
							</td>
						</tr>
					</#list>
				</#list>
			</#list>
		</tbody>
	</table>
	<script type="text/javascript">
		var channelRuleTree = $("#${treeName}");
		channelRuleTree.treetable({
			expandable : true
		});
		$(function(){
			//默认展开一级节点。
			channelRuleTree.find("tr[depth = '1']").each(function(){
				channelRuleTree.treetable('expandNode',$(this).attr('data-tt-id'))
			});
			channelRuleTree.find("tr[depth = '2']").each(function(){
				channelRuleTree.treetable('expandNode',$(this).attr('data-tt-id'))
			});
			if('${treeName}' != 'channelPolicyView'){
				channelRuleTree.find("tr[depth = '3']").each(function(){
					channelRuleTree.treetable('expandNode',$(this).attr('data-tt-id'))
				});
				signRule(channelRuleTree);
			}
			$("#${treeName} tbody").on("mouseover", "tr", function() {
				$(".selected","#${treeName}").not(this).removeClass("selected");
				$(this).toggleClass("selected");
			});
		    
		    // 更换规则状态
		    if ('${treeName}' != 'channelPolicyView') {
			    var selectPolicy =$(".page .tree-l-box .selected").find("a[rel=${treeName}"+"Box]");
				var url=selectPolicy.attr("href");
				url = url.substring(0,url.lastIndexOf("=")+1);
				$("select").change(function(){
					var href = selectPolicy.attr('href');
					selectPolicy.attr("href",url + $(this).val());
					selectPolicy.click();
					selectPolicy.attr("href",url + "${RuleStatus.UNAUDIT.value}");
				});
		    }
		});
	</script>
</#macro>

<#-- 策略审批信息 -->
<#macro channelPolicyLog_view channelPolicyLog=channelPolicyLog>
	<dl class="nowrap">
		<h3>审批信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>提交人：</dt>
		<dd>${channelPolicyLog.submitter.name}</dd>
	</dl>
	<dl>
		<dt>提交时间：</dt>
		<dd>${channelPolicyLog.submitTime?datetime}</dd>
	</dl>
	<#if channelPolicyLog.auditor??>
		<dl>
			<dt>审核人：</dt>
			<dd>${channelPolicyLog.auditor.name}</dd>
		</dl>
		<dl>
			<dt>审核时间：</dt>
			<dd>${channelPolicyLog.auditTime?datetime}</dd>
		</dl>
	</#if>
	<#if channelPolicyLog.approver??>
		<dl>
			<dt>审批人：</dt>
			<dd>${channelPolicyLog.approver.name}</dd>
		</dl>
		<dl>
			<dt>审批时间：</dt>
			<dd>${channelPolicyLog.approveTime?datetime}</dd>
		</dl>
	</#if>
</#macro>

<#-- 结算策略按钮栏 -->
<#macro channelPolicy_bar channelPolicy=channelPolicy treeName="channelPolicyView">
	<#local isApprove = (channelPolicy.status == PolicyStatus.AUDIT || channelPolicy.status == PolicyStatus.APPROVE)>
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
    	<@sec.any name="POLICY_MANAGE">
			<#if channelPolicy.status == PolicyStatus.SUBMIT && !channelPolicy.expired>
				<li>
					<@dwz.a href="/price/channelPolicy-submit?channelPolicyId=${channelPolicy.id}" target="ajaxTodo" class="button" callback="navTabAjaxDone" title="您是否确定要提交审核？"><span>提交审核</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<#if !channelPolicy.expired && !isApprove>
			<@sec.any name="POLICY_MANAGE">
				<li>
		            <@dwz.a href="/price/channelPolicy-connectFee-set?channelPolicyId=${channelPolicy.id}" target="dialog" class="button" height="S"><span>影院<@s.message code="connectFee"/>设置</span></@dwz.a>
		        </li>
			</@sec.any>
		</#if>
		<#if channelPolicy.valid == ValidStatus.VALID && !channelPolicy.expired && !isApprove>
			<@sec.any name="POLICY_SWITCH">
				<li>
		            <@dwz.a href="/price/channelPolicy-cinema-set?channelPolicyId=${channelPolicy.id}" target="dialog" class="button" height="S"><span>影院开放设置</span></@dwz.a>
		        </li>
			</@sec.any>
		</#if>
		<#if channelPolicy.logs?size gt 0>
			<li>
				<@dwz.a href="/price/channelPolicyLog-view?channelPolicyId=${channelPolicy.id}" target="dialog" class="button" title="查看策略审批记录"><span>审批记录</span></@dwz.a>
			</li>
		</#if>
		<#if treeName != "channelGroupListView">
			<li>
	            <a id="collapseBtn_${channelPolicy.id}" class="button" href="javascript:expanAll('${treeName}',4);"><span>全部展开/收缩</span></a>
	        </li>
        </#if>
		<@sec.any name="POLICY_SWITCH">
	        <#if !isApprove && (!channelPolicy.expired || channelPolicy.valid == ValidStatus.UNVALID)>
		        <li>
		            <@dwz.a href="/price/channelPolicy-edit?channelPolicyId=${channelPolicy.id}" target="dialog" class="button" height="S"><span>编辑策略</span></@dwz.a>
		        </li>
	        </#if>
        </@sec.any>
    </@dwz.formBar>
</#macro>

<#--
 * 选择影厅。
 *
 * path：选择框绑定的属性路径
 * hallMList：数据模型
 * flag：是否显示选择按钮
 * url：访问路径
 * policyId : 当前特殊定价策略的ID
 -->
<#macro select_hall path hallMList policyId flag=true url="price/specialPolicy-hall-select">
	<@s.bind path />
	<dl class="nowrap">
	    <#if flag==true>
			<dt>选择影院：</dt>
			<input type="hidden" id="selectCinemas" />
			<input type="hidden" id="hallIds" value="${selectHall}"/>
			<dd><input style="display:none;" id="selectHalls" name="selectHalls" class="required" value="${hallMList??}"/><a class="button" href="${url}?policyId=${policyId}" target="dialog" rel="specialPolicy-hall-select" mask="true" width='850' height='550'  minable='false' maxable='false' resizable='false' drawable='true'><span>选择影院</span></a></dd>
		</#if>		
	</dl>
	<dl class="nowrap">
		<table class="list" width="567">
			<thead>
				<tr>
					<th width="30" align="center">序号</th>
					<th width="120" align="center">影院名称</th>
					<th align="center">影厅名称</th>
				</tr>
			</thead>
			<tbody <#if flag>id="hallData" </#if> >
				<#assign index =0 />
				<#list hallMList as hall>
					<tr>
						<td align="center">${hall_index+1}</td>
						<td cinemaId="${hall.cinemaId}">${hall.cinemaName}</td>
						<td>
							<#list hall.hallName?split(",") as hallname>${hallname}</#list>
							<#list hall.hallId?split(",") as hallid>
								<#if hallid?trim>
									<input type="hidden" name="${s.name}[${index}].id" value="${hallid}" <#if flag> class="hallids" hallid="${hallid}" </#if>/>
									<#assign index =index+1 />
								</#if>
							</#list>
						</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</dl>
</#macro>


<#-- 
 * 选择影院影厅组件。
 *
 * path：选择框绑定的属性路径
 * callBack：回调是否删除已选排期
 --> 
<#macro cinemaHall path>
	<@s.bind path />
	<div class="page">
		<div class="pageContent">
			<#if cinemaModel.specialPolicyId?if_exists>
				<div class="pageFormContent" layoutH="60">
			<#else>
				<div class="pageHeader">
					<@dwz.pageForm action="/price/specialPolicy-hall-select?specialPolicyId=${cinemaModel.specialPolicyId}" targetType="dialog" searchModel=cinemaModel alt="可输入影院名称检索"/>
				</div>
				<div class="pageFormContent" layoutH="95">
	   		</#if>
	   		<table id="cinema-treetable" style="width:100%">
	   			<#list proviceCinemas.items as province>
	   				<tr data-tt-id="${province.code}" id="${province.code}">
	   					<td colspan="2"><@s.checkbox class="checkboxCtrl" group="${province.code}" alias="province" id="${province.code}"/>${province.name}</td>
	   				</tr>
	   				<#list province.cities as cityModel>
	   					<tr data-tt-id="${cityModel.city.code}" data-tt-parent-id="${province.code}" id="${cityModel.city.code}" pId="${province.code}">
	   						<td colspan="2"><input type="checkbox" name="${province.code}"  alias="city" pId="${cityModel.city.code}"/>${cityModel.city.name}</td>
	   					</tr>
	   					<#list cityModel.cinemas as cinema>
	   						<tr data-tt-id="${cinema.code}" data-tt-parent-id="${cityModel.city.code}" id="${cinema.code}" pId="${cityModel.city.code}">
	   							<td style="width:250px;">
	   								<@s.checkbox class="checkboxCtrl" group="${cinema.code}" alias="cinema" cinemaId="${cinema.id}" cinemaName="${cinema.name}" id="${cinema.id}"/>${cinema.name}
	   							</td>
	   							<td>
	   								<#list cinema.halls as hall>
	   									<input type="checkbox" name="${cinema.code}" alias="hall" hallId="${hall.id}" hallName="${hall.fullName}" id="${hall.id}"/>${hall.fullName}
	   								</#list>
	   							</td>
	   						</tr>
	   					</#list>
	   				</#list>
	   			</#list>
	   		</table>
   			</div>
   		<@pri.button method="addHall()"/>
   		</div>
   	</div>
   	<script type="text/javascript" charset="utf-8">
	  	// 添加影院
		function addHall(){
	  		$("#channelData").empty();
	  		$("#channelDataJson").val("");
			var cinemaIds = '',hallIds = '',cineamHtml = '',num = 0,seq = 0;
			$("#cinema-treetable").find("input[type='checkbox'][alias='cinema']").each(function(){
				var halls = $(this).parent().next().find("input[type='checkbox'][alias='hall']").size();
				var checkHalls = $(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").size();
				if(halls == checkHalls && checkHalls > 0){
					cinemaIds += $(this).attr("cinemaId") +",";
				}
				var temp = '<tr><td class="c-text">' + (seq+1) + '</td><td class="l-text" cinemaId='+$(this).attr("cinemaId")+'>' + $(this).attr("cinemaName") +'</td><td class="l-text">';
				var hallText = '';
				$(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").each(function(){
					hallIds += $(this).attr("hallId") + ",";
					hallText += $(this).attr("hallName") + '&nbsp;<input type="hidden" name="${s.name}['+ num++ +'].id" value="' + $(this).attr("hallId") + '" />';;
				});
				if(hallIds != ''){
					temp += hallText +'</td></tr>';
					$("#selectHalls").val(hallIds);
					$("#hallIds").val(hallIds);
				}else{
					$("#selectHalls").val('');
					$("#hallIds").val('');
				}
				if(checkHalls >0){
					cineamHtml += temp;
				}
				if(cinemaIds != ''){
					$("#selectCinemas").val(cinemaIds);
				}
				if(cinemaIds.indexOf($(this).attr('id')) != -1){
					seq ++;
				}
			});
			$("#hallData").html('');
			$("#hallData").append(cineamHtml);
			$.pdialog.closeCurrent();
		}
	</script>
</#macro> 

<#--
 * 选择渠道。
 *
 * path：选择框绑定的属性路径
 * policyId：策略ID
 * channelList：渠道列表数据
 * flag：是否显示选择按钮
 * url：访问路径
 -->
<#macro select_channel policyId path channelList flag=true url="price/specialPolicy-channel-select">
	<@s.bind path />
	<dl class="nowrap">
		<#if flag==true>
			<dt>选择渠道：</dt>
			<dd><input style="display:none;" id="channelDataJson" name="channelDataJson" class="required" value="${channelList??}"/><a class="button" href="javascript:openSelectChannel()" rel="channel-select" mask="true" width='700' height='550' minable='false' maxable='false' resizable='false' drawable='true'><span>选择渠道</span></a></dd>
		</#if>
	</dl>
	<dl class="nowrap">
		<table class="list" width="567">
			<thead>
				<tr>
					<th width="30" align="center">序号</th>
					<th width="120" align="center">渠道编号</th>
					<th align="center">渠道名称</th>
					<th width="80" align="center">渠道类型</th>
				</tr>
			</thead>
			<tbody <#if flag> id="channelData" </#if> >
				<#list channelList as channel>
					<tr>
						<td align="center">${channel_index+1}</td>
						<td align="center">${channel.code}</td>
						<td>${channel.name}</td>
						<td align="center">${channel.type}</td>
						<input type="hidden" name="${s.name}[${channel_index}].id" value="${channel.id}" <#if flag> class="channelids" channelids="${channel.id}" </#if>/>
					</tr>
				</#list>
			</tbody>
		</table>
	</dl>
	<script type="text/javascript">
		function openSelectChannel() {
			if ($("#hallData tr").length <= 0) {
				alertMsg.error("请先选择影院。");
				return;
			}
			var cinemaIds = '';
			$("#hallData tr").each(function(){
				if(cinemaIds.length > 0) {
					cinemaIds = cinemaIds + ",";
				}
				cinemaIds += $(this).find("td").eq(1).attr("cinemaId");
			});
			$.pdialog.open('${url}?policyId=${policyId}&cinemaIds=' + cinemaIds, "channel-select", "选择渠道",  {width:700, height:550,mask:true,minable:false,maxable:false,resizable:false,drawable:true});
		}
	</script>
</#macro>

<#-- 选择渠道组件 --> 
<#macro selectChannel path>
	<@s.bind path />
	<div class="page">
		<div class="pageContent">
			<table class="table" width="100%" layoutH="60">
				<thead>
					<tr>
						<th width="30" align="center">序号</th>
						<th width="30" align="center">
							<@s.checkbox class="checkboxCtrl" group="channelids" value="true" />
						</th>
						<th width="100" align="center">渠道编号</th>
						<th align="center">渠道名称</th>
						<th width="80" align="center">渠道类型</th>
					</tr>
				</thead>
				<tbody id="channels">
					<#list channelList as channel>
						<tr target="sid_channelPage" rel="1" onclick="selectTr(event);">
							<td>${channel_index+1}</td>
							<td><@s.checkbox name="channelids" id=channel.id class="channelid" code=channel.code channelname=channel.name channelType=channel.type/></td>
							<td>${channel.code}</td>
							<td class="l-text">${channel.name}</td>
							<td>${channel.type}</td>
						</tr>
					</#list>
				</tbody>
			</table>
		</div>
		<@pri.button method="addChannel()"/>
	</div>
	<script type="text/javascript" charset="utf-8">
		function addChannel() {
			var row = '';
			var obj = new Array();
			$(".channelid").filter(':checked').each(
					function(e, i) {
						var selectobj = {};
						selectobj.id = $(this).attr('id');
						row += '<tr><td class="c-text">' + (e + 1)
								+ '</td><td class="c-text">' + $(this).attr('code')
								+ '</td><td>' + $(this).attr('channelname')
								+ '</td><td class="c-text">' + $(this).attr('channelType')
								+ '</td><input type="hidden" name="${s.name}['+e+'].id" value="'+$(this).attr('id')+'" class="channelids" channelids="'+$(this).attr('id')+'"/>'
								+ '</tr>';
						obj.push(selectobj);
					})
			if(obj.length>0){
				$("#channelDataJson").val(JSON.stringify(obj));
			}else{
				$("#channelDataJson").val('');
			}
			$("#channelData").html('');
			$("#channelData").append(row);
			$.pdialog.close("channel-select");
		}
		
		function setChannels() {
			var addObj = eval("(" + $("#channelDataJson").val() + ")");
			$.each(addObj, function(i, e) {
				$("#" + $(this)[0].id).attr("checked", true);
			});
		}
		$(document).ready(function() {
			if ("" != $("#channelDataJson").val()) {
				setChannels();
			}
			if(""!=$(".channelids")){
				$(".channelids").each(function(){
					$("#" +  $(this).attr("channelids")).attr("checked", true);
				});
			}
		});
	</script>
</#macro>

<#--
 * 选择影片。
 *
 * path：选择框绑定的属性路径
 * filmList：影片列表数据
 * flag：是否显示选择按钮
 * url：访问路径
 -->
<#macro select_film path filmList flag=true>
	<@s.bind path />
	<dl class="nowrap">
		<#if flag==true>
			<dt>选择影片：</dt>
			<dd><input style="display:none;" id="filmDataJson" name="filmDataJson" class="required" value="${filmList??}"/><a class="button" onclick="selectFilm();"><span>选择影片</span></a></dd>
		</#if>
	</dl>
	<dl class="nowrap">
		<table class="list" width="567">
			<thead>
				<tr>
					<th width="30" align="center">序号</th>
					<th width="120" align="center">影片编码</th>
					<th align="center">影片名称</th>
					<th width="80" align="center">语言</th>
					<th width="50" align="center">放映时长</th>
					<th width="70" align="center">公映日期</th>
					<#if flag><th width="70" align="center">操作</th></#if>
				</tr>
			</thead>
			<tbody <#if flag> id="filmData" </#if>>
				<#list filmList as film>
					<tr>
						<td align="center">${film_index+1}</td>
						<td align="center">${film.code}</td>
						<td class="l-text">${film.name}</td>
						<td align="center">${film.language}</td>
						<td align="center">${film.duration}</td>
						<td align="center">${film.publishDate?date}</td>
						<#if flag><td align="center"><a href="javascript:void(0)" onclick="deleteFilm(this)">移除</a></td></#if>
						<input type="hidden" name="${s.name}[${film_index}].id" value="${film.id}" <#if flag> class="filmids" filmids="${film.id}" </#if>/>
					</tr>
				</#list>
			</tbody>
		</table>
	</dl>
	<div id="filmCache"></div>
	<script type="text/javascript" charset="utf-8">
		function selectFilm(){
			var hallIds = '' ;
			if("" == $(".hallText").val() && "" != $(".hallids")){
				$(".hallids").each(function(){
					  hallIds += "," + $(this).attr('hallid')
				});
			  hallIds = hallIds.substring(1);		  
			}
			var hallName = $(".hallText").val();
			if(hallName == ""){
				hallName = hallIds;
			}
			$.pdialog.open('price/specialPolicy-film-select', "film-select", "选择影片",  {width:700, height:550,mask:true,minable:false,maxable:false,resizable:false,drawable:true});
		}
		function deleteFilm(object) {
			$(object).parent().parent().remove();
			var obj = new Array();
			$("#filmData tr").each(function(i, e) {  // 重新编号赋值 
				$(this).children().eq(0).html(i + 1);
				var selectobj = {};
				selectobj.id = $(this).find(".filmids").attr("value");
				obj.push(selectobj);
				var name = $(this).find(".filmids").attr("name");
				name = name.replace(/\[\d+\]/g,"[" + i + "]");
				$(this).find(".filmids").attr("name", name);
			});
			if(obj.length > 0){
				$("#filmDataJson").val(JSON.stringify(obj));
			}else{
				$("#filmDataJson").val('');
			}
		}
	</script>
</#macro>

<#-- 选择影片组件 --> 
<#macro selectFilm path>
	<@s.bind path />
 	<div class="pageHeader">
 		<@dwz.pageForm action="/price/specialPolicy-film-select" onsubmit="setFilmCache();return dialogSearch(this);" alt="可输入影片编码、影片名称检索"/>
 	</div>
 	<div class="pageContent">
 		<table class="table" width="100%" layoutH="102">
 			<thead>
 				<tr>
                    <th width="30" align="center">序号</th>
                  	<th width="30" align="center">
						<@s.checkbox class="checkboxCtrl" group="filmids" value="true" id="filmCheck" onClick="allSelected()"/>
					</th>
                    <th width="100" align="center">影片编码</th>
                    <th align="center">影片名称</th>
                    <th width="80" align="center">语言</th>
                    <th width="60" align="center">放映时长</th>
                    <th width="80" align="center">公映日期</th>
                </tr>
            </thead>
            <tbody id="searchFilm">
                <#list filmList as film>
	                <tr onclick="selectMovie(event);">
	                    <td>${film_index+1}</td>
	                    <td>
						<@s.checkbox name="filmids" id=film.id
							class="filmid" filmcode=film.code filmname=film.name languages=film.language duration=film.duration publishDate=film.publishDate onClick="filmClick(this);" />
						</td>
	                    <td>${film.code}</td>
	                    <td class="l-text">${film.name}</td>
	                    <td>${film.language}</td>
	                    <td>${film.duration}</td>
	                    <td>${film.publishDate?date}</td>
	                </tr>
              </#list>
            </tbody>
        </table>
		<@pri.button method="addFilm()"/>
	</div>
	<script type="text/javascript" charset="utf-8">
		var addFilms = new Array();
		function addFilm() {
			var row = '';
			var obj = new Array();
			$.each(addFilms, function(e, val){
				var params = val.split("|");
				var selectobj = {};
				selectobj.id = params[0];
				row += '<tr class="c-text"><td>' + (e + 1)
						+ '</td><td class="c-text">' + params[1]
						+ '</td><td class="l-text">' + params[2]
						+ '</td><td class="c-text">' + params[3]
						+ '</td><td class="c-text">' + params[4]
						+ '</td><td class="c-text">' + params[5]
						+ '</td><td align="center"><a href="javascript:void(0)" onclick="deleteSelected(this)">移除</a>'
						+ '</td><input type="hidden" name="${s.name}['  +e + '].id" value="' + params[0] + '" class="filmids" filmids="' + params[0] + '"/>'
						+ '</tr>';
				obj.push(selectobj);
			});
			if(obj.length > 0){
				$("#filmDataJson").val(JSON.stringify(obj));
			}else{
				$("#filmDataJson").val('');
			}
			$("#filmData").html('');
			$("#filmData").append(row);
			$("#filmCache").removeData("filmCache");
			$.pdialog.close("film-select");
		}
		
		function selectMovie(event){
			var event = event? event: window.event;
			var element = event.srcElement ? event.srcElement:event.target;
			var tr = $(element).closest("tr");
			var chk = $(tr).find("input[type='checkbox']");
			if(element.type != 'checkbox'){
				if(chk.is(":checked")){
					chk.attr("checked",false);
					addFilms.splice($.inArray(wrapFilm(chk),addFilms),1);
				}else{
					chk.attr("checked",true);
					addFilms.push(wrapFilm(chk));
				}
			}
			var records = $(tr).closest("tbody").find("tr").length;
			var chks =  $(tr).closest("tbody").find("input[type='checkbox']:checked").length;
			if (chks >0 && records == chks){
				$(".page").find("input[type='checkbox'][group='" + chk.attr('name') +"']").attr("checked",true);
			}else{
				$(".page").find("input[type='checkbox'][group='" + chk.attr('name') +"']").attr("checked",false);
			}
		}
		
		$(document).ready(function() {
			if(""!=$(".filmids")){
				$(".filmids").each(function(){
					$("#" +  $(this).attr("filmids")).attr("checked", true);
				});
			}
			if(typeof($("#filmCache").data("filmCache")) == "undefined") {
				$(".filmid").filter(':checked').each(function(){
					var film = wrapFilm($(this));
					addFilms.push(film);
				});
			} else {
				// 缓存filmCache存在，则清空所有选择，然后通过缓存filmCache设置影片选中。
				$(".filmid").filter(':checked').each(function(){
					$(this).attr("checked",false);
				});  
				addFilms = $("#filmCache").data("filmCache");
				$.each(addFilms, function(i,val){  
					var params = val.split("|");
					$("#searchFilm input[id='" + params[0] +"']").attr("checked", true);
				});   
			}
			$(".close").click(function() {
				$("#filmCache").removeData("filmCache");
			});
		});
		function filmClick(current) {
			var currentFilm = $("#searchFilm input[id='" + current.id +"']");
			var film = wrapFilm(currentFilm);
			var exist = $.inArray(film, addFilms);
			if(exist != -1) {
				addFilms.splice($.inArray(film,addFilms),1);
			} else {
				addFilms.push(film);
			}
		}
		function allSelected() {
			setTimeout(function() {
				var selectNum = $(".filmid").filter(':checked').length;
				if(selectNum == "${filmList?size}") {
					// 全部选中
					$(".filmid").filter(':checked').each(
						function(i,val) {     
							var film = wrapFilm($(this));
							var exist = $.inArray(film, addFilms);
							if(exist == -1) {
								addFilms.push(film);
							}
						}
					)
				} else {
					// 全部取消
					addFilms.splice(0,addFilms.length);
				}
	        }, 200);
		}
		function setFilmCache() {
			$("#filmCache").data("filmCache",addFilms);
		}
		function wrapFilm(object) {
			return object.attr('id') + "|" + object.attr('filmcode') + "|" + object.attr('filmname') + "|" + object.attr('languages') + "|" + object.attr('duration') + "|" + object.attr('publishDate');
		}
		function deleteSelected(object) {
			$(object).parent().parent().remove();
			var obj = new Array();
			$("#filmData tr").each(function(i, e) {  // 重新编号赋值 
				$(this).children().eq(0).html(i + 1);
				var selectobj = {};
				selectobj.id = $(this).find(".filmids").attr("value");
				obj.push(selectobj);
				var name = $(this).find(".filmids").attr("name");
				name = name.replace(/\[\d+\]/g,"[" + i + "]");
				$(this).find(".filmids").attr("name", name);
			});
			if(obj.length > 0){
				$("#filmDataJson").val(JSON.stringify(obj));
			}else{
				$("#filmDataJson").val('');
			}
		}
	</script>
</#macro>

<#--
 * 选中提交按钮。
 * name：提交方法
 -->
<#macro button method closeMethod>	
	<div class="formBar">
		<ul>
			<li>
				<div class="buttonActive">
					<div class="buttonContent"><button type="button" onclick="${method};">保存</button></div>
				</div>
			</li>
			<li>
				<div class="button">
					<div class="buttonContent"><button class="close" type="button" onclick="${closeMethod}">取消</button></div>
				</div>
			</li>
		</ul>
	</div>
</#macro>	

<#-- 特殊定价策略标题信息 -->
<#macro specialPolicy_head specialPolicy=specialPolicy>
    <div style="border-bottom:1px solid #CCC;padding-bottom:7px;">
    	<div style="font-size:15px !important;font-weight:bold;line-height:34px;padding-left:5px;">
			${specialPolicy.name}
    	</div>
    	<div>
	    	【<b>
				<span class="${specialPolicy.status.color}">${specialPolicy.status}</span> - 
				<span class="${specialPolicy.valid.color}">${specialPolicy.valid}</span> - 
				<span class="${specialPolicy.enabled.color}">${specialPolicy.enabled}</span>
			</b>】
			【<#if specialPolicy.exclusive>独占<#else>不独占</#if>】【 策略有效期限：${specialPolicy.startDate?date} - ${specialPolicy.endDate?date} 】【 排期范围：${specialPolicy.showStartDate?date} - ${specialPolicy.showEndDate?date} 】
		</div>
    </div>
</#macro>

<#-- 特殊定价策略公有信息 -->
<#macro specialPolicy_view specialPolicy=specialPolicy>
	<dl class="nowrap">
		<dt>策略名称：</dt>
	    <dd>${specialPolicy.name}</dd>
	</dl>
	<dl>
	    <dt>策略类型：</dt>
	    <dd>${specialPolicy.type}</dd>
	</dl>
	<dl>
   		<dt>放映类型：</dt>
		<dd>${specialPolicy.showTypesText}</dd>
	</dl>
	<dl>
		<dt>策略起始日期：</dt>
	    <dd>${specialPolicy.startDate?date}</dd>
	</dl>
	<dl>
	    <dt>策略截止日期：</dt>
	    <dd>${specialPolicy.endDate?date}</dd>
	</dl>
	<dl>
		<dt>排期起始日期：</dt>
	    <dd>${specialPolicy.showStartDate?date}</dd>
	</dl>
	<dl>
	    <dt>排期截止日期：</dt>
	    <dd>${specialPolicy.showEndDate?date}</dd>
	</dl>
	<dl>
	    <dt>是否独占排期：</dt>
	    <dd>${specialPolicy.exclusive?string("独占","不独占")}</dd>
	</dl>
	<dl class="nowrap">
	    <dt>选择影院：</dt>
	    <dd>
	    	<@select_hall path="specialPolicy.halls" hallMList=specialPolicy.hallModels flag=false />
	    </dd>
	</dl>
	<#if specialPolicy.type == SpecialPolicyType.FILM>
		<dl class="nowrap">
			<dt>选择影片：</dt>
		    <dd>
		    	<@select_film filmList=specialPolicy.films path="specialPolicy.films" flag=false />
		    </dd>
		</dl>
	</#if>
	<dl class="nowrap">
	    <dt>选择渠道：</dt>
	    <dd>
	    	<@select_channel channelList=specialPolicy.channels path="specialPolicy.channels" flag=false />
	    </dd>
	</dl>
</#macro>

<#-- 特殊定价规则列表 -->
<#macro specialRule_list specialPolicy=specialPolicy targetType="navTab" rel="" show=true>
	<#local isApprove = (specialPolicy.status == SpecialPolicyStatus.APPROVE || specialPolicy.status == SpecialPolicyStatus.AUDIT)>
	<#list specialPolicy.rulesExcludeCopy as specialRule>
		<div id="${specialRule.id}" class="panel collapse" minH="100">
			<h1>
				<span style="line-height:28px;float:left;">
				    ${specialRule_index+1}.&nbsp;
				    <#if specialRule.isBounded()>
		   				<#local specialRuleNameColor = StatusColor.ORANGE>
		   			<#elseif !specialRule.isBounded() && specialRule.enabled == EnabledStatus.ENABLED  && specialRule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GREEN>
		   			<#elseif !specialRule.isBounded() && specialRule.enabled != EnabledStatus.ENABLED && specialRule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GRAY>
		   			<#else>
		   				<#local specialRuleNameColor = StatusColor.RED>
		   			</#if>
		   			<#if specialRule.isBounded()>
		   				<#if specialRule.boundRule.name != specialRule.name>
		   					<span class="${specialRuleNameColor}" title="${specialRule.name}">${specialRule.boundRule.name}</span>
		   				<#else>
		   					<span class="${specialRuleNameColor}">${specialRule.boundRule.name}</span>
		   				</#if>
		   			<#else>
		   				<span class="${specialRuleNameColor}">${specialRule.name}</span>
		   			</#if>
				</span>
				<#if show && !isApprove && !specialPolicy.expired>
			        <span style="line-height:28px;float:right;">
				        <#if specialRule.isBounded()>
				        	<@sec.any name="POLICY_MANAGE">
				        		<#if specialRule.isEdit()>
				        			<@dwz.a href="/price/specialRule-edit?specialRuleId=${specialRule.boundRule.id}" target="dialog" title="编辑规则">[编辑]</@dwz.a>
				        		</#if>
				        		<@dwz.a href="/price/specialRule-copy?specialRuleId=${specialRule.boundRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要复制该规则？">[复制]</@dwz.a>
				        		<#if specialRule.boundRule.valid != ValidStatus.VALID>
				    				<@dwz.a href="/price/specialRule-delete?specialRuleId=${specialRule.boundRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
				    			</#if>
				        	</@sec.any>
				        <#else>
			        		<@sec.any name="POLICY_MANAGE">
			        			<#if specialRule.isEdit()>
				        			<@dwz.a href="/price/specialRule-edit?specialRuleId=${specialRule.id}" target="dialog" title="编辑规则">[编辑]</@dwz.a>
				        		</#if>
			        			<@dwz.a href="/price/specialRule-copy?specialRuleId=${specialRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要复制该规则？">[复制]</@dwz.a>
			        			<#if specialRule.valid != ValidStatus.VALID>
			    					<@dwz.a href="/price/specialRule-delete?specialRuleId=${specialRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
			    				</#if>
				        	</@sec.any>
				        	<@sec.any name="POLICY_SWITCH">
				        		<#if specialRule.valid == ValidStatus.VALID>
					        		<#if specialRule.enabled == EnabledStatus.ENABLED>
										<@dwz.a href="/price/specialRule-disable?specialRule=${specialRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="停用规则将作废相关的渠道排期，您是否确定要停用该规则？">[停用]</@dwz.a> 
									<#else>
										<@dwz.a href="/price/specialRule-enable?specialRule=${specialRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="启用规则将重新生成相关的渠道排期，您是否确定要启用该规则？">[启用]</@dwz.a>
									</#if>
								</#if>
				        	</@sec.any>
				        </#if>
						&nbsp;&nbsp;&nbsp;
					</span>
		        </#if>
			</h1>
			<div>
				<@specialRule_view specialRule=specialRule specialPolicy=specialPolicy/>
				<span style="line-height:28px;float:left;font-weight: bold;">销售渠道</span>
				<@sec.any name="POLICY_MANAGE">
					<#if show && !specialPolicy.expired && !isApprove && specialRule.isAddChannel()>
						<#if specialRule.isBounded()>
							<span style="line-height:28px;float:right;font-weight: bold;margin-right: 20px; margin-top: 10px;"><@dwz.a href="/price/specialChannel-add?specialRuleId=${specialRule.boundRule.id}" target="dialog" title="新增渠道">[新增渠道]</@dwz.a></span>
						<#else>
							<span style="line-height:28px;float:right;font-weight: bold;margin-right: 20px; margin-top: 10px;"><@dwz.a href="/price/specialChannel-add?specialRuleId=${specialRule.id}" target="dialog" title="新增渠道">[新增渠道]</@dwz.a></span>
						</#if>
					</#if>
				</@sec.any>
				<table class="list" width="98%">
					<thead>
						<tr>
							<th align="center" width="30">序号</th>
							<th align="center">渠道名称</th>
							<th align="center" width="80"><@s.message code="connectFee"/></th>
							<th align="center" width="400">结算规则</th>
							<#if show && !isApprove && !specialPolicy.expired>
								<#if !specialPolicy.expired>
									<th align="center" width="150" >操作</th>
								</#if>
							</#if>
						</tr>
					</thead>
					<tbody>
						<#list specialRule.channelsExcludeCopy as specialChannel>
							<tr>
								<td class="c-text">${specialChannel_index+1}</td>
								<td class="l-text">
									<#if specialChannel.isBounded()>
										<#local specialChannelNameColor = StatusColor.ORANGE>
									<#elseif !specialChannel.isBounded() && specialChannel.isEnabled() && specialChannel.valid == ValidStatus.VALID>
										<#local specialChannelNameColor = StatusColor.GREEN>
									<#elseif !specialChannel.isBounded() && !specialChannel.isEnabled() && specialChannel.valid == ValidStatus.VALID>
										<#local specialChannelNameColor = StatusColor.GRAY>
									<#else>
										<#local specialChannelNameColor = StatusColor.RED>
									</#if>
									<span class="${specialChannelNameColor}">${specialChannel.channel.name}</span>
								</td>
								<#if  specialChannel.isBounded()>
									<td align="center" class="${specialChannelNameColor}" title="${specialChannel.connectFee?string('0.00')}">${specialChannel.boundChannel.connectFee?string("0.00")}</td>
									<td align="center" class="${specialChannelNameColor}" title="${specialChannel.settleRule.toString()}">${specialChannel.boundChannel.settleRule.toString()}</td>
								<#else>
									<td align="center" class="${specialChannelNameColor}">${specialChannel.connectFee?string("0.00")}</td>
									<td align="center" class="${specialChannelNameColor}">${specialChannel.settleRule.toString()}</td>
								</#if>
								<#if show && !isApprove && !specialPolicy.expired>
									<td align="center">
										<#if specialChannel.isBounded()>
											<@sec.any name="POLICY_MANAGE">
												<@dwz.a href="/price/specialChannel-edit?specialChannelId=${specialChannel.boundChannel.id}" target="dialog" callback="dialogAjaxDone" title="编辑渠道" height="SS">编辑</@dwz.a>
												<#if specialRule.isAddChannel()>
													<@dwz.a href="/price/specialChannel-copy?specialChannelId=${specialChannel.boundChannel.id}" target="dialog" callback="dialogAjaxDone" title="复制渠道" height="SS">复制</@dwz.a>
												</#if>
												<#if specialChannel.boundChannel.valid != ValidStatus.VALID>
													<@dwz.a href="/price/specialChannel-delete?specialChannelId=${specialChannel.boundChannel.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该渠道？">删除</@dwz.a>
												</#if>
											</@sec.any>
										<#else>
											<@sec.any name="POLICY_MANAGE">
												<@dwz.a href="/price/specialChannel-edit?specialChannelId=${specialChannel.id}" target="dialog" callback="dialogAjaxDone" title="编辑渠道" height="SS">编辑</@dwz.a>
												<#if specialRule.isAddChannel()>
													<@dwz.a href="/price/specialChannel-copy?specialChannelId=${specialChannel.id}" target="dialog" callback="dialogAjaxDone" title="复制渠道" height="SS">复制</@dwz.a>
												</#if>
												<#if specialChannel.valid != ValidStatus.VALID>
													<@dwz.a href="/price/specialChannel-delete?specialChannelId=${specialChannel.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该渠道？">删除</@dwz.a>
												</#if>
											</@sec.any>
											<@sec.any name="POLICY_SWITCH">
												<#if specialChannel.valid == ValidStatus.VALID>
													<#if specialChannel.isEnabled()>
														<@dwz.a href="/price/specialChannel-close?specialChannel=${specialChannel.id}" target="ajaxTodo" callback="navTabAjaxDone" title="关闭渠道将作废相关的渠道排期，您是否确定要关闭该渠道？">关闭</@dwz.a>
													<#else>
														<@dwz.a href="/price/specialChannel-open?specialChannel=${specialChannel.id}" target="ajaxTodo" callback="navTabAjaxDone" title="开放渠道将重新生成相关的渠道排期，您是否确定要开放该渠道？">开放</@dwz.a>
													</#if>
												</#if>
											</@sec.any>
										</#if>
									</td>
								</#if>
							</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
	</#list>
</#macro>

<#-- 特殊定价规则信息 -->
<#macro specialRule_view specialRule=specialRule specialPolicy=specialPolicy>
	<table class="list" width="98%">
		<#if specialRule.isBounded()>
			<tr>
				<td width="70">放映类型：</td>
				<#if specialRule.showType != specialRule.boundRule.showType>
					<td class="${StatusColor.ORANGE}" title="${specialRule.showType}">${specialRule.boundRule.showType}</td>
				<#else>
					<td>${specialRule.boundRule.showType}</td>
				</#if>
			</tr>
			<#if specialPolicy.type != SpecialPolicyType.SELL>
			 	<tr>
			   		<td width="70">影片范围：</td>
			   		<#if specialRule.filmsNameText != specialRule.boundRule.filmsNameText>
			   			<td class="${StatusColor.ORANGE}" title="${specialRule.filmsNameText}">${specialRule.boundRule.filmsNameText}</td>
			   		<#else>
			   			<td>${specialRule.boundRule.filmsNameText}</td>
			   		</#if>
				</tr>
			</#if>
			<tr>
		  		<td width="70">影院范围：</td>
		  		<#if specialRule.cinemasNameText != specialRule.boundRule.cinemasNameText>
		  			<td class="${StatusColor.ORANGE}" title="${specialRule.cinemasNameText}">
			  			${specialRule.boundRule.cinemasNameText}
			  		</td>
			  	<#else>
			  		<td>
			  			${specialRule.boundRule.cinemasNameText}
			  		</td>
		  		</#if>
			</tr>
		 	<tr>
		  		<td width="70">时间限制：</td>
		   		<#if specialRule.periodRule.toString() != specialRule.boundRule.periodRule.toString()>
		   			<td class="${StatusColor.ORANGE}" title="${specialRule.periodRule.toString()}">${specialRule.boundRule.periodRule.toString()}</td>
		   		<#else>
		   			<td>${specialRule.boundRule.periodRule.toString()}</td>
		   		</#if>
			</tr>
			<tr>
				<td width="70">结算价格：</td>
		   		<#if specialRule.settleRule.toString() != specialRule.boundRule.settleRule.toString()>
		   			<td class="${StatusColor.ORANGE}" title="${specialRule.settleRule.toString()}">${specialRule.boundRule.settleRule.toString()}</td>
		   		<#else>
		   			<td>${specialRule.boundRule.settleRule.toString()}</td>
		   		</#if>
	   		</tr>
	   		<tr>
				<td width="70">上报类型：</td>
		   		<#if specialRule.submitType != specialRule.boundRule.submitType || specialRule.amount != specialRule.boundRule.amount>
		   			<td class="${StatusColor.ORANGE}" title="${specialRule.submitType}<#if (specialRule.amount >= 0)>+</#if>${specialRule.amount?string('0.00')}元">${specialRule.boundRule.submitType}<#if (specialRule.boundRule.amount >= 0)>+</#if>${specialRule.boundRule.amount?string("0.00")}元</td>
		   		<#else>
		   			<td>${specialRule.boundRule.submitType}<#if (specialRule.amount >= 0)>+</#if>${specialRule.amount?string("0.00")}元</td>
		   		</#if>
	   		</tr>
		<#else>
			<tr>
				<td width="70">放映类型：</td>
				<td>${specialRule.showType}</td>
			</tr>
			<#if specialPolicy.type != SpecialPolicyType.SELL>
				<tr>
					<td width="70">影片范围：</td>
					<td>${specialRule.filmsNameText}</td>
				</tr>
			</#if>
			<tr>
		  		<td width="70">影院范围：</td>
		  		<td>${specialRule.cinemasNameText}</td>
			</tr>
			<tr>
				<td width="70">时间限制：</td>
				<td>${specialRule.periodRule.toString()}</td>
			</tr>
			<tr>
				<td width="70">结算价格：</td>
				<td>${specialRule.settleRule.toString()}</td>
			</tr>
			<tr>
				<td width="70">上报类型：</td>
				<td>${specialRule.submitType}<#if (specialRule.amount >= 0)>+</#if>${specialRule.amount?string("0.00")}元</td>
			</tr>
		</#if>
	</table>
</#macro>

<#-- 特殊定价策略审批信息 -->
<#macro specialPolicyLog_view specialPolicyLog=specialPolicyLog>
	<dl class="nowrap">
		<h3>审批信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>提交人：</dt>
		<dd>${specialPolicyLog.submitter.name}</dd>
	</dl>
	<dl>
		<dt>提交时间：</dt>
		<dd>${specialPolicyLog.submitTime?datetime}</dd>
	</dl>
	<dl class="nowrap">
		<dt>提交说明：</dt>
		<dd>${specialPolicyLog.submitRemark}</dd>
	</dl>
	<#if specialPolicyLog.auditor??>
		<dl>
			<dt>审核人：</dt>
			<dd>${specialPolicyLog.auditor.name}</dd>
		</dl>
		<dl>
			<dt>审核时间：</dt>
			<dd>${specialPolicyLog.auditTime?datetime}</dd>
		</dl>
	</#if>
	<#if specialPolicyLog.approver??>
		<dl>
			<dt>审批人：</dt>
			<dd>${specialPolicyLog.approver.name}</dd>
		</dl>
		<dl>
			<dt>审批时间：</dt>
			<dd>${specialPolicyLog.approveTime?datetime}</dd>
		</dl>
	</#if>
	<#if specialPolicyLog.refuseNote??>
		<dl class="nowrap">
			<dt>退回意见：</dt>
			<dd class="${StatusColor.RED}">${specialPolicyLog.refuseNote} </dd>
		</dl>
	</#if>
</#macro>

<#-- 特殊定价策略下方按钮栏 -->
<#macro specialPolicy_bar specialPolicy=specialPolicy>
	<#local isApprove = (specialPolicy.status == SpecialPolicyStatus.AUDIT || specialPolicy.status == SpecialPolicyStatus.APPROVE)>
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
    	<@sec.any name="POLICY_MANAGE">
			<#if specialPolicy.status == SpecialPolicyStatus.SUBMIT && !specialPolicy.expired && specialPolicy.rules?size gt 0>
				<li>
					<@dwz.a href="/price/specialPolicy-submit?policyId=${specialPolicy.id}" target="dialog" class="button" width="S" height="SS"><span>提交审核</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<#if specialPolicy.logs?size gt 0>
			<li>
				<@dwz.a href="/price/specialPolicyLog-view?policyId=${specialPolicy.id}" target="dialog" class="button" title="查看策略审批记录"><span>审批记录</span></@dwz.a>
			</li>
		</#if>
        <li>
            <@dwz.a href="/price/specialPolicy-view?policyId=${specialPolicy.id}" target="dialog" class="button" width="L"><span>查看策略</span></@dwz.a>
        </li>
        <#if specialPolicy.rules?size gt 0>
	        <li>
	            <a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
	        </li>
        </#if>
        <@sec.any name="PRODUCT_MANAGE">
        	<#if specialPolicy.valid == ValidStatus.VALID  && specialPolicy.enabled ==  EnabledStatus.ENABLED && !specialPolicy.getExpired()>
		        <li>
		        	<@dwz.a href="/product/specialPolicy-show-list?specialPolicy=${specialPolicy.id}" width="L" height="L" target="dialog" class="button" title="特殊定价策略排期检索"><span>排期检索</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<@sec.any name="POLICY_SWITCH">
	        <#if !isApprove>
		        <li>
		            <@dwz.a href="/price/specialPolicy-edit?policyId=${specialPolicy.id}" target="dialog" class="button"><span>编辑策略</span></@dwz.a>
		        </li>
	        </#if>
        </@sec.any>
	    <@sec.any name="POLICY_MANAGE">
	        <#if !isApprove && !specialPolicy.expired>
		        <li>
		        	<@dwz.a href="/price/specialRule-add?policyId=${specialPolicy.id}" target="dialog" class="button"><span>新增规则</span></@dwz.a>
		        </li>
	        </#if>
        </@sec.any>
        <#nested>
    </@dwz.formBar>
</#macro>

<#-- 特殊定价策略新增、修改页面公共部分 -->
<#macro specialPolicy_operate path rulesSize=rulesSize>
	<@s.hidden path="${path}.type" />
	<dl class="nowrap">
		<dt>策略名称：</dt>
	  	<dd><@s.input path="${path}.name" class="required m-input" maxlength="40"/></dd>
	</dl>
	<dl>
		<dt>策略起始日期：</dt>
	   	<dd><@s.input path="${path}.startDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date"/></dd>
	</dl>
	<dl>
		<dt>策略截止日期：</dt>
	    <dd><@s.input path="${path}.endDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date" minRelation="#startDate" customvalid="geDate(element,'#startDate')" title="截止日期不能小于起始日期。"/></dd>
	</dl>
	<dl>
		<dt>排期起始日期：</dt>
	   	<dd><@s.input path="${path}.showStartDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date"/></dd>
	</dl>
	<dl>
		<dt>排期截止日期：</dt>
	    <dd><@s.input path="${path}.showEndDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date" minRelation="#showStartDate" customvalid="geDate(element,'#showStartDate')" title="排期截止日期不能小于排期起始日期。"/></dd>
	</dl>
	<dl>
	    <dt>是否独占排期：</dt>
		<dd><@s.radio path="${path}.exclusive" falseText="不独占" trueText="独占" prefix="<label class='dd-span'>" suffix="</label>"/></dd>
	</dl>
	<#if rulesSize == 0>
		<dl class="nowrap">
			<dt>放映类型：</dt>
			<dd><@dwz.checkboxs path="${path}.showTypes" items=ShowType?values itemValue="value" itemLabel="text"/></dd>
		</dl>
	</#if>
</#macro>