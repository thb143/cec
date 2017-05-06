<#macro ticketOrderSearch searchModel=searchModel>
	<li>渠道：<@s.select path="searchModel.channelCode" headerLabel="全部" items=channels itemValue="code" itemLabel="name" /></li>
	<li>订单类型：<@s.select path="searchModel.ticketOrderType" headerLabel="全部" items=TicketOrderType?values itemValue="value" itemLabel="text"/></li>
	<#if searchModel.orderBy != "createTime"><li>有无卖品：<@s.boolSelect path="searchModel.hasSnack" headerLabel="全部" headerValue="" trueLabel="有" trueValue="1" falseLabel="无" falseValue="0" /></li></#if>
	<#if searchModel.orderBy == "createTime">
		<#local timeName = "创建时间">
	<#elseif searchModel.orderBy == "payTime">
		<#local timeName = "支付时间">
	<#elseif searchModel.orderBy == "confirmTime">
		<#local timeName = "确认时间">
	<#elseif searchModel.orderBy == "revokeTime">
		<#local timeName = "退票时间">
	</#if>
	<li>${timeName}：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
	<li> 至 <@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
</#macro>

<#-- 订单排期信息 -->
<#macro channelShow_view order=order>
	<dl class="nowrap">
		<h3>影院排期信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>影院排期编码：</dt>
		<dd>${order.showCode}</dd>
	</dl>
	<dl>
	    <dt>影片语言：</dt>
	    <dd>${order.language}</dd>
	</dl>
	<dl>
	    <dt>影院编码：</dt>
	    <dd>${order.cinema.code}</dd>
	</dl>
	<dl>
	    <dt>接入商：</dt>
	    <dd>${order.provider}</dd>
	</dl>
	<dl>
	    <dt>影院名称：</dt>
	    <dd>${order.cinema.name}</dd>
	</dl>
	<dl>
	    <dt>影厅编码：</dt>
	    <dd>${order.hall.code}</dd>
	</dl>
	<dl>
	    <dt>影厅名称：</dt>
	    <dd>${order.hall.name}</dd>
	</dl>
	<dl>
	    <dt>影片编码：</dt>
	    <dd>${order.filmCode}</dd>
	</dl>
	<dl>
	    <dt>影片名称：</dt>
	    <dd>${order.film.name}</dd>
	</dl>
	<dl>
	    <dt>放映类型：</dt>
	    <dd>${order.showType}</dd>
	</dl>
	<dl>
	    <dt>放映时间：</dt>
	    <dd>${order.showTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>时长：</dt>
	    <dd>${order.duration} 分钟</dd>
	</dl>
	<dl>
	    <dt><@s.message code="minPrice"/>：</dt>
	    <dd>${order.minPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="stdPrice"/>：</dt>
	    <dd>${order.stdPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl class="nowrap">
		<h3>渠道排期信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>渠道排期编码：</dt>
		<dd>${order.channelShowCode}</dd>
	</dl>
	<dl>
	    <dt>渠道名称：</dt>
	    <dd>${order.channel.name}</dd>
	</dl>
	<dl>
	    <dt>影院结算规则：</dt>
	    <dd style="color: ${order.cinemaRuleColor};cursor: default;" title="${order.cinemaRuleSummary}">${order.cinemaRuleName}</dd>
	</dl>
	<dl>
	    <dt>渠道结算规则：</dt>
	    <dd style="color: ${order.channelRuleColor};cursor: default;" title="${order.channelRuleSummary}">${order.channelRuleName}</dd>
	</dl>
	<#if order.benefitCardConsumeOrder != null>
		<dl class="nowrap">
	    	<dt>权益卡类规则：</dt>
	    	<dd style="color: ${order.benefitCardConsumeOrder.ruleColor};cursor: default;" title="${order.benefitCardConsumeOrder.ruleSummary}">${order.benefitCardConsumeOrder.ruleName}</dd>
		</dl>
	</#if>
	<dl>
	    <dt><@s.message code="cinemaPrice"/>：</dt>
	    <dd>${order.singleCinemaPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="channelPrice"/>：</dt>
	    <dd>${order.singleChannelPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="submitPrice"/>：</dt>
	    <dd>${order.singleSubmitPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="connectFee"/>：</dt>
	    <dd>${order.singleConnectFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="circuitFee"/>：</dt>
	    <dd>${order.singleCircuitFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="subsidyFee"/>：</dt>
	    <dd>${order.singleSubsidyFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt>停售时间：</dt>
	    <dd>${order.stopSellTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>停退时间：</dt>
	    <dd>${order.stopRevokeTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>创建时间：</dt>
	    <dd>${order.channelShowCreateDate?datetime}</dd>
	</dl>
	<dl>
	    <dt>最后更新时间：</dt>
	    <dd>${order.channelShowModifyDate?datetime}</dd>
	</dl>
</#macro>

<#-- 选座票凭证信息 -->
<#macro ticketVoucher_view ticketVoucher=ticketVoucher>
	<dl>
	    <dt>订单号：</dt>
	    <dd><strong>${ticketVoucher.order.code}</strong></dd>
	</dl>
	<dl>
	    <dt>凭证编号：</dt>
	    <dd>${ticketVoucher.code}</dd>
	</dl>
	<dl>
	    <dt>下发手机号：</dt>
	    <dd>${ticketVoucher.order.mobile}</dd>
	</dl>
	<dl>
	    <dt>下发方式：</dt>
	    <dd>${ticketVoucher.sendType}</dd>
	</dl>
	<dl class="nowrap">
	    <dt>短信内容：</dt>
	    <dd>${ticketVoucher.sms}</dd>
	</dl>
	<dl class="nowrap">
		<dt>彩信内容：</dt>
	    <dd>${ticketVoucher.mms}</dd>
	</dl>
	<#nested>
</#macro>

<#-- 选座票订单信息 -->
<#macro ticketOrder_view order=order>
	<dl>
         <dt>订单号：</dt>
         <dd>${order.code}</dd>
    </dl>
    <dl>
         <dt>订单类型：</dt>
         <dd>${order.type}</dd>
    </dl>
    <dl>
         <dt>订单状态：</dt>
         <dd>${order.status}</dd>
    </dl>
    <dl>
         <dt>手机号码：</dt>
         <dd>${order.mobile}</dd>
    </dl>
    <#if order.benefitCardConsumeOrder != null>
    	<dl>
	         <dt>权益卡卡号：</dt>
	         <dd>${order.benefitCardConsumeOrder.card.code}</dd>
	    </dl>
	    <dl>
	         <dt>权益卡类型：</dt>
	         <dd>${order.benefitCardConsumeOrder.card.type.name}</dd>
	    </dl>
    </#if>
    <dl>
         <dt>影院排期编号：</dt>
         <dd>${order.showCode}</dd>
    </dl>
    <dl>
         <dt>渠道排期编号：</dt>
         <dd>${order.channelShowCode}</dd>
    </dl>
    <dl>
         <dt>影院名称：</dt>
         <dd>${order.cinema.name}</dd>
    </dl>
    <dl>
         <dt>渠道名称：</dt>
         <dd>${order.channel.name}</dd>
    </dl>
    <dl>
         <dt>影院订单号：</dt>
         <dd>${order.cinemaOrderCode}</dd>
    </dl>
    <dl>
         <dt>接入商：</dt>
         <dd>${order.provider}</dd>
    </dl>
    <dl>
         <dt>渠道订单号：</dt>
         <dd>${order.channelOrderCode}</dd>
    </dl>
    <dl>
        <dt>创建时间：</dt>
         <dd>${order.createTime?datetime}</dd>
    </dl>
    <dl>
         <dt>支付时间：</dt>
         <dd>${(order.payTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
    </dl>
    <dl>
         <dt>出票时间：</dt>
         <dd>${(order.confirmTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
    </dl>
    <dl>
         <dt>过期时间：</dt>
         <dd>${order.expireTime?datetime}</dd>
    </dl>
    <dl>
         <dt>退票时间：</dt>
         <dd>${(order.revokeTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
    </dl>
</#macro>

<#-- 选座票信息 -->
<#macro ticketVoucher_view voucher=voucher>
	<dl>
		<dt>凭证编号：</dt>
		<dd>${voucher.code}</dd>
	</dl>
	<dl>
		<dt>打票状态：</dt>
		<dd>${voucher.status}</dd>
	</dl>
	<dl>
         <dt>取票号：</dt>
         <dd>${voucher.printCode}</dd>
    </dl>
    <dl>
         <dt>取票验证码：</dt>
         <dd>${voucher.verifyCode}</dd>
    </dl>
    <dl>
    	<dt>打印状态：</dt>
    	<dd>${voucher.printable?string("可打印","不可打印")}</dd>
    </dl>
    <dl>
    	<dt>重打印次数：</dt>
    	<dd>${voucher.reprintCount}</dd>
    </dl>
    <dl>
    	<dt>生成时间：</dt>
    	<dd>${voucher.genTime?datetime}</dd>
    </dl>
    <dl>
    	<dt>过期时间：</dt>
    	<dd>${voucher.expireTime?date}</dd>
    </dl>
    <dl>
    	<dt>验证时间：</dt>
    	<dd>${(voucher.confirmPrintTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
    </dl>
</#macro>

<#-- 选座票订单明细 -->
<#macro ticketOrder_items order=order>
	<div class="divider"></div>
	<div>
		<dl class="s-cols" style="width:852px;height:auto">
			<table class="list" width="98%">
		        <thead>
		            <tr>
		              	<th width="30" align="center">序号</th>
						<th width="120" align="center">影票编码</th>
						<th align="center">座位编码</th>
						<th width="60" align="center">座位</th>
						<th width="45" align="center"><@s.message code="salePrice" /></th>
						<th width="70" align="center"><@s.message code="channelPrice" /></th>
						<th width="45" align="center"><@s.message code="submitPrice" /></th>
						<th width="45" align="center"><@s.message code="serviceFee" /></th>
						<th width="45" align="center"><@s.message code="connectFee" /></th>
						<th width="45" align="center"><@s.message code="channelFee" /></th>
						<th width="45" align="center"><@s.message code="circuitFee" /></th>
						<th width="45" align="center"><@s.message code="subsidyFee" /></th>
						<th width="45" align="center"><@s.message code="discountAmount" /></th>
		            </tr>
		        </thead>
				<tbody>
	          		<#list order.orderItems as orderItem>
			            <tr>
			                <td align="center">${orderItem_index+1}</td>
			                <td align="center">${orderItem.ticketCode}</td>
			                <td align="center">${orderItem.seatCode}</td>
			                <td align="center">${orderItem.seatRow}排${orderItem.seatCol}座</td>
			                <td align="right">${orderItem.salePrice?string(",##0.00")}</td>
			                <td align="right">${orderItem.channelPrice?string(",##0.00")}</td>
			                <td align="right">${orderItem.submitPrice?string(",##0.00")}</td>
			                <td align="right">${orderItem.serviceFee?string(",##0.00")}</td>
			                <td align="right">${orderItem.connectFee?string(",##0.00")}</td>
			                <td align="right">${orderItem.channelFee?string(",##0.00")}</td>
			                <#if orderItem.circuitFee gt 0>
			                	<td align="right" class="${StatusColor.GREEN}">${orderItem.circuitFee?string(",##0.00")}</td>
			                <#else>
			                	<td align="right">${orderItem.circuitFee?string(",##0.00")}</td>
			                </#if>
			                <#if orderItem.subsidyFee gt 0>
			                	<td align="right" class="${StatusColor.RED}">${orderItem.subsidyFee?string(",##0.00")}</td>
			                <#else>
			                	<td align="right">${orderItem.subsidyFee?string(",##0.00")}</td>
			                </#if>
			                <td align="right">${orderItem.discountPrice?string(",##0.00")}</td>
			            </tr>
			        </#list>
			        <tr>
		                <td colspan="4" align="right"><b>选座票合计</b></td>
		                <td align="right">${order.ticketAmount?string(",##0.00")}</td>
		                <td align="right">${order.channelAmount?string(",##0.00")}</td>
		                <td align="right">${order.submitAmount?string(",##0.00")}</td>
		                <td align="right">${order.serviceFee?string(",##0.00")}</td>
		                <td align="right">${order.connectFee?string(",##0.00")}</td>
		                <td align="right">${order.channelFee?string(",##0.00")}</td>
		                <#if order.circuitFee gt 0>
		                	<td align="right" class="${StatusColor.GREEN}">${order.circuitFee?string(",##0.00")}</td>
		                <#else>
		                	<td align="right">${order.circuitFee?string(",##0.00")}</td>
		                </#if>
		                <#if order.subsidyFee gt 0>
		                	<td align="right" class="${StatusColor.RED}">${order.subsidyFee?string(",##0.00")}</td>
		                <#else>
		                	<td align="right">${order.subsidyFee?string(",##0.00")}</td>
		                </#if>
		                <#if order.snackOrder != null>
		                	<td align="right">${(order.discountAmount - order.snackOrder.discountAmount)?string(",##0.00")}</td>
		                <#else>
		                	<td align="right">${order.discountAmount?string(",##0.00")}</td>
		                </#if>
		            </tr>
		        </tbody>
		    </table>
		</dl>
	</div>
</#macro>
<#-- 选座票卖品明细 -->
<#macro ticketOrder_snacks order=order>
	<div class="divider"></div>
	<div>
		<dl class="s-cols" style="width:852px;height:auto">
			<table class="list" width="98%"> 
		        <thead>
		            <tr>
		              	<th width="30" align="center">序号</th>
						<th width="60" align="center"><@s.message code="snackName" /></th>
						<th width="160" align="center"><@s.message code="snackRemark" /></th>
						<#if order.benefitCardConsumeOrder != null>
							<th width="80" align="center">权益卡规则</th>
						</#if>
						<th width="45" align="center"><@s.message code="snackCount" /></th>
						<th width="45" align="center"><@s.message code="saleAmount" /></th>
						<th width="50" align="center"><@s.message code="cinemaAmount" /></th>
						<th width="50" align="center"><@s.message code="channelAmount" /></th>
						<th width="45" align="center"><@s.message code="connectFee" /></th>
						<th width="45" align="center"><@s.message code="channelFee" /></th>
						<th width="45" align="center"><@s.message code="discountAmount" /></th>
		            </tr>
		        </thead>
				<tbody>
	          		<#list order.snackOrder.orderItems as orderSnack>
			            <tr>
			                <td align="center">${orderSnack_index+1}</td>
			                <td align="center">${orderSnack.snack.type.name}</td>
			                <td align="center">${orderSnack.snack.type.remark}</td>
			                <#if order.benefitCardConsumeOrder != null>
								<td align="center" title="${orderSnack.ruleSummary}" class="${orderSnack.ruleColor}">${orderSnack.ruleName}</td>
							</#if>
			                <td align="center">${orderSnack.count} </td>
			                <td align="right">${orderSnack.salePrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.cinemaPrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.channelPrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.connectFee?string(",##0.00")}</td>
			                <td align="right">${orderSnack.channelFee?string(",##0.00")}</td>
			                <td align="right">${orderSnack.discountPrice?string(",##0.00")}</td>
			              </tr>
			        </#list>
		        </tbody>
		    </table>
		</dl>
	</div>
</#macro>

<#-- 卖品订单卖品明细 -->
<#macro snackOrder_snacks order=order>
	<div class="divider"></div>
	<div>
		<dl class="s-cols" style="width:852px;height:auto">
			<table class="list" width="98%">
		        <thead>
		            <tr>
		              	<th width="30" align="center">序号</th>
						<th width="160" align="center">卖品名称</th>
						<#if order.benefitCardConsumeSnackOrder != null || (order.ticketOrder != null && order.ticketOrder.benefitCardConsumeOrder != null)>
							<th width="80" align="center">权益卡规则</th>
						</#if>
						<th width="60" align="center">卖品数量</th>
						<th width="60" align="center">销售价</th>
						<th width="45" align="center">门市价</th>
						<th width="50" align="center">影院结算价</th>
						<th width="50" align="center">渠道结算价</th>
						<th width="45" align="center">渠道费</th>
						<th width="45" align="center"><@s.message code="discountAmount" /></th>
		            </tr>
		        </thead>
				<tbody>
	          		<#list order.orderItems as orderSnack>
			            <tr>
			                <td align="center">${orderSnack_index+1}</td>
			                <td align="center">${orderSnack.snack.type.name}</td>
			                <#if order.benefitCardConsumeSnackOrder != null || (order.ticketOrder != null && order.ticketOrder.benefitCardConsumeOrder != null)>
								<td align="center" title="${orderSnack.ruleSummary}" class="${orderSnack.ruleColor}">${orderSnack.ruleName}</td>
							</#if>
			                <td align="center">${orderSnack.count}</td>
			                <td align="center">${orderSnack.salePrice?string(",##0.00")} </td>
			                <td align="right">${orderSnack.stdPrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.cinemaPrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.channelPrice?string(",##0.00")}</td>
			                <td align="right">${orderSnack.channelFee?string(",##0.00")}</td>
			                <td align="right">${orderSnack.discountPrice?string(",##0.00")}</td>
			              </tr>
			        </#list>
		        </tbody>
		    </table>
		</dl>
	</div>
</#macro>

<#-- 权益卡订单查询 -->
<#macro benefitCardOrderSearch searchModel=searchModel>
	<li>卡类：<@s.select path="searchModel.benefitCardTypeCode" headerLabel="全部" items=benefitCardTypes itemValue="code" itemLabel="name" /></li>
	<#if searchModel.orderType == "open">
		<#local channelName = "开卡渠道">
		<#local timeName = "开卡时间">
	<#elseif searchModel.orderType == "recharge">
		<#local channelName = "续费渠道">
		<#local timeName = "续费时间">
	<#elseif searchModel.orderType == "consume">
		<#local channelName = "消费渠道">
		<#local timeName = "消费时间">
	</#if>
	<li>${channelName}：<@s.select path="searchModel.channelCode" headerLabel="全部" items=channels itemValue="code" itemLabel="name" /></li>
	<#if searchModel.orderType == "recharge">
		<li>订单状态：<@s.select path="searchModel.rechargeStatus" headerLabel="全部" items=BenefitCardRechargeStatus?values itemValue="value" itemLabel="text" /></li>
	<#elseif searchModel.orderType == "consume">
		<li>订单状态：<@s.select path="searchModel.consumeStatus" headerLabel="全部" items=TicketOrderStatus?values itemValue="value" itemLabel="text" /></li>
	</#if>
	<li>${timeName}：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
	<li> 至 <@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
</#macro>

<#-- 卖品订单信息 -->
<#macro snackOrder_view order=order>
	<dl>
         <dt>订单号：</dt>
         <dd>${order.code}</dd>
    </dl>
    <dl>
         <dt>订单状态：</dt>
         <dd>${order.status}</dd>
    </dl>
    <dl>
         <dt>手机号码：</dt>
         <dd>${order.mobile}</dd>
    </dl>
    <dl>
         <dt>影院名称：</dt>
         <dd>${order.cinema.name}</dd>
    </dl>
    <#if order.benefitCardConsumeSnackOrder != null>
    	<dl>
	         <dt>权益卡卡号：</dt>
	         <dd>${order.benefitCardConsumeSnackOrder.card.code}</dd>
	    </dl>
	    <dl>
	         <dt>权益卡类型：</dt>
	         <dd>${order.benefitCardConsumeSnackOrder.card.type.name}</dd>
	    </dl>
	<#elseif order.ticketOrder != null && order.ticketOrder.benefitCardConsumeOrder != null>
		<dl>
	         <dt>权益卡卡号：</dt>
	         <dd>${order.ticketOrder.benefitCardConsumeOrder.card.code}</dd>
	    </dl>
	    <dl>
	         <dt>权益卡类型：</dt>
	         <dd>${order.ticketOrder.benefitCardConsumeOrder.card.type.name}</dd>
	    </dl>
    </#if>
    <dl>
         <dt>渠道名称：</dt>
         <dd>${order.channel.name}</dd>
    </dl>
    <#if order.ticketOrder.cinemaOrderCode !=null>
	    <dl>
	         <dt>影院订单号：</dt>
	         <dd>${order.ticketOrder.cinemaOrderCode}</dd>
	    </dl>
	    <dl>
	         <dt>渠道订单号：</dt>
	         <dd>${order.ticketOrder.channelOrderCode}</dd>
	    </dl>
	<#else>
	 	<dl>
	         <dt>渠道订单号：</dt>
	         <dd>${order.channelOrderCode}</dd>
	    </dl>
    </#if>
    <dl>
        <dt>创建时间：</dt>
         <dd>${order.createTime?datetime}</dd>
    </dl>
    <#if order.ticketOrder.cinemaOrderCode !=null>
	    <dl>
	         <dt>支付时间：</dt>
	         <dd>${(order.ticketOrder.payTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
	    </dl>
    <#else>
	    <dl>
	         <dt>支付时间：</dt>
	         <dd>${order.createTime?datetime}</dd>
	    </dl>
    </#if>
    <#if order.status == TicketOrderStatus.REVOKED>
	    <dl>
	         <dt>退卖品时间：</dt>
	         <dd>${(order.revokeTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
	    </dl>
    </#if>
</#macro>

<#-- 卖品凭着 -->
<#macro snackVoucher_view voucher=voucher>
	<dl>
		<dt>凭证号：</dt>
		<dd>${voucher.code}</dd>
	</dl>
	<dl>
		<dt>订单状态：</dt>
		<dd>${voucher.status}</dd>
	</dl>
    <dl>
    	<dt>核销状态：</dt>
    	<dd>${voucher.printable?string("可打印","不可打印")}</dd>
    </dl>
    <dl>
    	<dt>生成时间：</dt>
    	<dd>${voucher.genTime?datetime}</dd>
    </dl>
    <dl>
    	<dt>核销时间：</dt>
    	<dd>${(voucher.confirmPrintTime?string("yyyy-MM-dd HH:mm:ss"))!}</dd>
    </dl>
</#macro>
<#-- 卖品订单查询 -->
<#macro snackOrderSearch searchModel=searchModel>
	<#if searchModel.orderType == "success">
		<#local channelName = "渠道">
		<#local timeName = "确认时间">
	<#elseif searchModel.orderType == "back">
		<#local channelName = "渠道">
		<#local timeName = "退卖品时间">
	</#if>
	<li>${channelName}：<@s.select path="searchModel.channelCode" headerLabel="全部" items=channels itemValue="code" itemLabel="name" /></li>
	<li>${timeName}：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
	<li> 至 <@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
</#macro>