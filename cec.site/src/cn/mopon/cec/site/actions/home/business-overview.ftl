<div class="panel" >
	<div class="panelHeader">
		<div class="panelHeaderContent">
    		<h1>业务概览</h1>
 		</div>
 	</div>
    <div class="panelContent" style="height:180px">
   		<div class="pageFormContent">
   			<p style="width:260px">
				<label>影院总数：</label>
			    <label>${businessOverview.cinemaCount} 家</label>
			</p>
			<p style="width:260px">
				<label>渠道总数：</label>
			    <label>${businessOverview.channelCount} 家</label>
			</p>
			<p style="width:260px">
				<label>订单总数：</label>
			    <label>${businessOverview.orderCount} 笔</label>
			</p>
			<p style="width:260px">
				<label>出票总数：</label>
			    <label>${businessOverview.ticketCount} 张</label>
			</p>
			<p style="width:260px">
				<label>订单总金额：</label>
			    <label>${businessOverview.orderAmount?string(",##0.00")} 元</label>
			</p>
			<p style="width:260px">
				<label>今日订单：</label>
			    <label>${businessOverview.todayOrderCount} 笔</label>
			</p>
			<p style="width:260px">
				<label>今日出票：</label>
			    <label>${businessOverview.todayTicketCount} 张</label>
			</p>
			<p style="width:260px">
				<label>今日总金额：</label>
			    <label>${businessOverview.todayOrderAmount?string(",##0.00")} 元</label>
			</p>
			<p style="width:260px">
				<label>最高订单日：</label>
			    <label>${businessOverview.topOrderDate?date}</label>
			</p>
			<p style="width:260px">
				<label>最高订单数：</label>
			    <label>${businessOverview.topOrderCount} 笔</label>
			</p>
   		</div>
	</div>
    <div class="panelFooter">
    	<div class="panelFooterContent"></div>
    </div>
</div>