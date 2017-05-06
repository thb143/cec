<@dwz.reload action="/operate/hall-list" cinemaId="${cinema.id}" />
<div class="page">
	<div class="pageContent">
		<div class="panelBar">
			<ul class="toolBar">
				<@sec.any name="CINEMA_MANAGE">
					<li>
						<@dwz.a href="/operate/hall-sync?cinema=${cinema.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要同步所有影厅及座位？"><span class="a34">同步影厅</span></@dwz.a>
					</li>
				</@sec.any>
			</ul>
		</div>
		<@dwz.form action="/operate/hall-update">
			<@s.hidden path="cinema.id" />
			<table class="table" width="100%" layoutH="84">
				<thead>
					<tr>
						<th width="30" align="center">序号</th>
						<th align="center">影厅名称</th>
						<th width="130" align="center">影厅编码</th>
						<th width="100" align="center">影厅类型</th>
	                    <th width="80" align="center">座位数量</th>
	                    <th width="80" align="center">状态</th>
	                    <th width="120" align="center">操作</th>
	                </tr>
	            </thead>
	            <tbody>
	            	<#list cinema.validHalls as hall>
	            		<tr>
	            			<td>${hall_index+1}</td>
	            			<td>${hall.name}</td>
	            			<td>${hall.code}</td>
	            			<td>
		            			<select name="type">
	   								<option value="${hall.id}|"></option>
			                        <#list hallTypeList as hallType>
			                       	 	<option value="${hall.id}|${hallType.id}"
			                       	 		<#if hall.hallType?? && hall.hallType.id== hallType.id >selected = selected</#if>>
				                       	 	${hallType.name}
			                       	 	</option>
			                       	</#list>
		                        </select>
	            			</td>
	            			<td>${hall.seatCount}</td>
	            			<td class="${hall.status.color}">${hall.status}</td>
	            			<td>
	            				<@sec.any name="CINEMA_SWITCH">
	            					<#if hall.status == HallStatus.ENABLED>
	            						<@dwz.a href="/operate/hall-disable?hall=${hall.id}" target="ajaxTodo" callback="navTabAjaxDone" title="停用影厅将作废相关的渠道排期，您是否确定要停用该影厅？">停用</@dwz.a>
	            					<#elseif hall.status == HallStatus.DISABLED>
	            						<@dwz.a href="/operate/hall-enable?hall=${hall.id}" target="ajaxTodo" callback="navTabAjaxDone" title="启用影厅将重新生成相关的渠道排期，您是否确定要启用该影厅？">启用</@dwz.a>
	            					</#if>
	            				</@sec.any>
	            				<@sec.any name="CINEMA_MANAGE">
	            					<#if hall.status == HallStatus.ENABLED>
	            						<@dwz.a href="/operate/hall-seats-sync?hall=${hall.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要重新同步该影厅的座位？">同步座位</@dwz.a>
	            					</#if>
	            				</@sec.any>
		                   </td>
		               </tr>
	                </#list>
	            </tbody>
	        </table>
		    <@sec.any name="CINEMA_MANAGE">
	        	<@dwz.formBar showCancelBtn=false />
	        </@sec.any>
        </@dwz.form>
    </div>
</div>