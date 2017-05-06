<div class="page">
    <div class="pageContent">
	    <div class="tabs" currentIndex="0" eventType="click">
	    	<div class="tabsHeader">
	    		<div class="tabsHeaderContent">
	    			<ul>
	    				<li><a href="javascript:;"><span>排期信息</span></a></li>
	    				<li><a href="javascript:;"><span>更新记录</span></a></li>
	    				<li><@s.a href="/product/show-normal-order-list?show=${show.id}" class="j-ajax"><span>正常订单</span></@s.a></li>
	    				<li><@s.a href="/product/show-revoke-order-list?show=${show.id}" class="j-ajax"><span>退票订单</span></@s.a></li>
	    			</ul>
	    		</div>
			</div>
			<div class="tabsContent">
				<div>
					<div class="page">
						<div class="pageContent">
							<div class="pageFormContent" layoutH="60" >
								<@pro.show_view />
							</div>
						</div>
					</div>
				</div>
				<div>
					<div class="page">
					    <div class="pageContent">
					    	<div class="pageFormContent" layoutH="60">
					    		<#list show.updateLogs as log>
					    			<div class="d-line"></div>
									<@sys.log_view log />
								</#list>
							</div>
					    </div>
					</div>
				</div>
				<div></div>
				<div></div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
</div>