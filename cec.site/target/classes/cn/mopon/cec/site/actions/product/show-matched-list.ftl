<div class="page">
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
          		<li><@dwz.a href="/product/show-match-gen" target="selectedTodo" callback="dialogAjaxDone" rel="showIds" title="您是否确定要批量重新生成渠道排期？"><span class="a38">批量重新生成渠道排期</span></@dwz.a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="50">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="30" align="center"><@s.checkbox class="checkboxCtrl" group="showIds" /></th>
                    <th width="130" align="center">影院排期编码</th>
                    <th width="100">影片名称</th>
                    <th>影院名称</th>
                    <th width="60" align="center">影厅名称</th>
                    <th width="60" align="center">放映类型</th>
                    <th width="115" align="center">放映时间</th>
                    <th width="45" align="right">最低价</th>
                    <th width="45" align="right"><@s.message code="stdPrice"/></th>
                    <th width="45" align="center">状态</th>
                </tr>
            </thead>
            <tbody id="showList">
                <#list shows as show>
	                <tr onclick="selectTr(event);">
	                    <td>${show_index+1}</td>
	                    <td><@s.checkbox name="showIds" value="${show.id}" /></td>
	                    <td>${show.code}</td>
	                    <td>${show.film.name}</td>
	                    <td>${show.cinema.name}</td>
	                    <td>${show.hall.name}</td>
	                    <td>${show.showType}</td>
	                    <td>${show.showTime?string('yyyy-MM-dd HH:mm')}</td>
	                    <td>${show.minPrice?string("0.00")}</td>
	                    <td>${show.stdPrice?string("0.00")}</td>
	                    <td class="${show.status.color}">${show.status}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>