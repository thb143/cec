<div class="page">
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
          		<#if cinema.status == EnabledStatus.ENABLED>
            		<li><@dwz.a href="/product/search-ticketProduct-gen" target="selectedTodo" postType="String" rel="selectedShowIds" callback="dialogReloadDone" title="您是否确定要批量重新生成选座票产品？"><span class="a38">批量重新生成</span></@dwz.a></li>
           		</#if>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="60">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="30" align="center"><@s.checkbox class="checkboxCtrl" group="selectedShowIds" /></th>
                    <th width="130" align="center">排期编码</th>
                    <th align="center">影院名称</th>
                    <th align="center">影片名称</th>
                    <th width="90" align="center">影厅名称</th>
                    <th width="70" align="center">放映类型</th>
                    <th width="115" align="center">放映时间</th>
                    <th width="60" align="center">标准票价</th>
                    <th width="60" align="center">排期状态</th>
                    <th width="60" align="center">生成状态</th>
                    <th width="60" align="center">产品状态</th>
                </tr>
            </thead>
            <tbody>
                <#list showPage as show>
	                <tr>
	                    <td>${show_index+1}</td>
	                    <td>
		                    <#if show.status != ShowStatus.INVALID>
			                   <@s.checkbox name="selectedShowIds" value="${show.id}" />
			                </#if>
	                    </td>
	                    <td><@dwz.a href="/product/show-view?showId=${show.id}" target="dialog" width="XL" height="L" title="查看排期">${show.code}</@dwz.a></td>
	                    <td>${show.cinema.name}</td>
	                    <td class="l-text"><@dwz.a href="/operate/film-view?film=${show.film.id}" target="dialog" title="查看影片">${show.film.name}</@dwz.a></td>
	                    <td>${show.hall.name}</td>
	                    <td>${show.showType}</td>
	                    <td>${show.showTime?string('yyyy-MM-dd HH:mm')}</td>
	                    <td class="r-text">${show.stdPrice?string("0.00")}</td>
	                    <td class="${show.status.color}">${show.status}</td>
	                    <td class="${(show.genLog.status.color)!}">${(show.genLog.status)!}</td>
	                    <td class="${(show.genLog.product.status.color)!}">${(show.genLog.product.status)!}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>