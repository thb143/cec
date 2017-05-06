<@dwz.reload action="/product/snack-list" cinema="${cinema.id}"/>
<div class="page">
    <div class="pageContent">
        <div class="pageFormContent" layoutH="56">
        	<#if snacks?size gt 0>
            	<#list snacks as model>
            		<div id="${model.snack.id}" class="panel collapse" minH="100">
						<h1>
							<span style="line-height:28px;float:left;">
							    ${model_index+1}.&nbsp;
							    <span>${model.snack.type.name}</span>
							</span>
					        <span style="line-height:28px;float:right;">
					        	<@sec.any name="PRODUCT_MANAGE">
						        	<#if model.snack.status == SnackStatus.UNVALID>
						        		<@dwz.a href="/product/snack-edit?snack=${model.snack.id}" target="dialog" width="S" height="S" title="设置价格">[设置价格]</@dwz.a>
						        		<@dwz.a href="/product/snack-delete?snack=${model.snack.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该卖品？">[删除]</@dwz.a>
						        	<#else>
						        		<@dwz.a href="/product/snack-edit?snack=${model.snack.id}" target="dialog" width="S" height="S" title="编辑价格">[编辑价格]</@dwz.a>
						        		<#if model.snack.status == SnackStatus.OFF>
						        			<@dwz.a href="/product/snack-enable?snack=${model.snack.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要上架该卖品？">[上架]</@dwz.a>
						        		<#else>
						        			<@dwz.a href="/product/snack-disable?snack=${model.snack.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要下架该卖品？">[下架]</@dwz.a>
						        		</#if>
						        	</#if>
						        </@sec.any>
								&nbsp;&nbsp;&nbsp;&nbsp;
							</span>
						</h1>
						<div>
							<table class="list" width="98%">
								<tr>
									<td align="center" rowspan="7" width="200">
										<span><img src="${imagePath}${model.snack.type.image.path}_190x190.jpg" width="190" height="190" title="${model.snack.name}"/></span>
									</td>
								</tr>
								<tr>
									<td width="100">分类：</td>
									<td class="${StatusColor.RED}">${model.snack.type.group.name}</td>
								</tr>
								<tr>
									<td width="100">编码：</td>
									<td>${model.snack.code}</td>
								</tr>
								<tr>
									<td width="100">内容：</td>
									<td>${model.snack.type.remark}</td>
								</tr>
								<tr>
									<td width="100">状态：</td>
									<td class="${model.snack.status.color}">${model.snack.status}</td>
								</tr>
								<tr>
									<td width="100">门市价：</td>
									<td>${model.snack.stdPrice?string(",##0.00")} 元</td>
								</tr>
								<tr>
									<td width="100">结算价：</td>
									<td>${model.snack.submitPrice?string(",##0.00")} 元</td>
					             </tr>
					        </table>
							<span style="line-height:28px;float:left;font-weight: bold;">销售渠道</span>
							<@sec.any name="PRODUCT_MANAGE">
								<#if model.snack.status != SnackStatus.UNVALID && model.isAddChannel>
									<span style="line-height:28px;float:right;font-weight: bold;margin-right: 20px; margin-top: 10px;"><@dwz.a href="/product/snackChannel-add?snack=${model.snack.id}" target="dialog" height="SS" title="新增渠道">[新增渠道]</@dwz.a></span>
								</#if>
							</@sec.any>
							<table class="list" width="98%">
								<thead>
									<tr>
										<th align="center" width="30">序号</th>
										<th align="center">渠道名称</th>
										<th align="center" width="80"><@s.message code="connectFee"/></th>
										<th align="center" width="80">开放状态</th>
										<th align="center" width="150" >操作</th>
									</tr>
								</thead>
								<tbody>
									<#list model.snack.snackChannels as snackChannel>
										<tr>
											<td class="c-text">${snackChannel_index+1}</td>
											<td class="l-text">${snackChannel.channel.name}</td>
											<td align="center">${snackChannel.connectFee?string("0.00")}</td>
											<td align="center" class="${snackChannel.status.color}">${snackChannel.status}</td>
											<td align="center">
												<@dwz.a href="/product/snackChannel-edit?snackChannel=${snackChannel.id}" target="dialog" callback="dialogAjaxDone" title="编辑渠道" height="SS">编辑</@dwz.a>
												<#if model.isAddChannel>
													<@dwz.a href="/product/snackChannel-copy?snackChannel=${snackChannel.id}" target="dialog" callback="dialogAjaxDone" title="复制渠道" height="SS">复制</@dwz.a>
												</#if>
												<#if snackChannel.status == OpenStatus.OPENED>
													<@dwz.a href="/product/snackChannel-close?snackChannel=${snackChannel.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要关闭该渠道？">关闭</@dwz.a>
												<#else>
													<@dwz.a href="/product/snackChannel-open?snackChannel=${snackChannel.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要开放该渠道？">开放</@dwz.a>
												</#if>
											</td>
										</tr>
									</#list>
								</tbody>
							</table>
						</div>
	            	</div>
	            </#list>
	    	</#if>
	    </div>
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
	    	<@sec.any name="PRODUCT_MANAGE">
	    		<#if snackTypes?size gt 0>
					<li>
						<@dwz.a href="/product/snack-add?cinema=${cinema.id}" target="dialog" class="button"><span>新增卖品</span></@dwz.a>
					</li>
				</#if>
			</@sec.any>
			<#if snacks?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
		        </li>
	        </#if>
	    </@dwz.formBar>
    </div>
</div>