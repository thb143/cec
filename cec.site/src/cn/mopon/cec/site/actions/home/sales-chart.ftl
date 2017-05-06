<div class="panel">
	<div class="panelHeader">
		<div class="panelHeaderContent">
    		<h1>销售趋势</h1>
 		</div>
 	</div>
 	<div class="panelContent" style="height:320px;">
 		<@ecs.chart id="sales-chart" action="/home/sales-chart-data.json" style="width:98%;height:98%;">
			<input type="hidden" name="name" value="TestChart" />
		</@ecs.chart>
    </div>
   	<div class="panelFooter">
    	<div class="panelFooterContent"></div>
	</div>
</div>