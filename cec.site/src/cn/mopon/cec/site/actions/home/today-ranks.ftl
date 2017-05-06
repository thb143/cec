<div class="tabs" currentIndex="0" eventType="click">
	<div class="tabsHeader">
        <div class="tabsHeaderContent">
            <ul>
                <li><a href="javascript:;"><span>今日影院排行</span></a></li>
                <li><a href="javascript:;"><span>今日渠道排行</span></a></li>
                <li><a href="javascript:;"><span>今日影片排行</span></a></li>
                <li><a href="javascript:;"><span>今日特价排行</span></a></li>
            </ul>
        </div>
    </div>
    <div class="tabsContent" style="height:320px">
    	<div>
    		<table class="list" width="98%">
    			<thead>
    				<tr>
    					<th width="200">影院名称</th>
    					<th width="100">订单数</th>
    					<th width="100">出票数</th>
    					<th width="100">订单金额</th>
    				</tr>
    			</thead>
    			<tbody>
    				<#list todayRanksModel.cinemaRank as rank>
    					<tr>
    						<td>${rank.name}</td>
    						<td>${rank.orderCount}</td>
    						<td>${rank.ticketCount}</td>
    						<td>${rank.orderAmount?string(",##0.00")}</td>
    					</tr>
    				</#list>
    			</tbody>
    		</table>
    	</div>
    	<div style="display: none;">
    		<table class="list" width="98%">
    			<thead>
    				<tr>
    					<th width="200">渠道名称</th>
    					<th width="100">订单数</th>
    					<th width="100">出票数</th>
    					<th width="100">订单金额</th>
    				</tr>
    			</thead>
    			<tbody>
    				<#list todayRanksModel.channelRank as rank>
    					<tr>
    						<td>${rank.name}</td>
    						<td>${rank.orderCount}</td>
    						<td>${rank.ticketCount}</td>
    						<td>${rank.orderAmount?string(",##0.00")}</td>
    					</tr>
    				</#list>
    			</tbody>
    		</table>
    	</div>
    	<div style="display: none;">
    		<table class="list" width="98%">
    			<thead>
    				<tr>
    					<th width="200">影片名称</th>
    					<th width="100">订单数</th>
    					<th width="100">出票数</th>
    					<th width="100">订单金额</th>
    				</tr>
    			</thead>
    			<tbody>
    				<#list todayRanksModel.filmRank as rank>
    					<tr>
    						<td>${rank.name}</td>
    						<td>${rank.orderCount}</td>
    						<td>${rank.ticketCount}</td>
    						<td>${rank.orderAmount?string(",##0.00")}</td>
    					</tr>
    				</#list>
    			</tbody>
    		</table>
    	</div>
    	<div style="display: none;">
    		<table class="list" width="98%">
    			<thead>
    				<tr>
    					<th width="200">特殊定价策略名称</th>
    					<th width="80">订单数</th>
    					<th width="80">出票数</th>
    					<th width="100">订单金额</th>
    					<th width="120">渠道补贴订单数</th>
    					<th width="100">渠道补贴票数</th>
    					<th width="100">渠道补贴金额</th>
    				</tr>
    			</thead>
    			<tbody>
    				<#list todayRanksModel.specialPolicyRank as rank>
    					<tr>
    						<td>${rank.name}</td>
    						<td>${rank.orderCount}</td>
    						<td>${rank.ticketCount}</td>
    						<td>${rank.orderAmount?string(",##0.00")}</td>
    						<td>${rank.channelSubsidyOrderCount}</td>
    						<td>${rank.channelSubsidyTicketCount}</td>
    						<td>${rank.channelSubsidyAmount?string(",##0.00")}</td>
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