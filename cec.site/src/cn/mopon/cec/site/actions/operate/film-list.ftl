<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/operate/film-list" alt="可输入影片编码、影片名称检索" />
    </div>
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <@sec.any name="FILM_MANAGE">
                	<li><@dwz.a href="/operate/film-sync-view" target="dialog" width="SS" height="SSS"><span class="a34">同步影片</span></@dwz.a></li>
                	<li><@dwz.a href="/operate/filmSyncLog-list" target="dialog" width="L" height="L"><span class="a15">查看同步日志</span></@dwz.a></li>
                </@sec.any>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="100" align="center">影片编码</th>
                    <th width="200" >影片名称</th>
                    <th width="80" align="center">时长（分钟）</th>
                    <th width="120">放映类型</th>
                    <th width="100" align="center">公映日期</th>
                    <th align="center">最低价分类</th>
                    <th width="150" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list filmPage.contents as film>
	                <tr>
	                    <td>${film_index+1}</td>
	                    <td>${film.code}</td>
	                    <td>
	                        <@dwz.a href="/operate/film-view?film=${film.id}" target="dialog" title="查看影片">${film.name}</@dwz.a>
	                    </td>
	                    <td>${film.duration}</td>
	                    <td>${film.showTypes}</td>
	                    <td>${film.publishDate?date}</td>
	                    <td>
	                    	<#list film.groups as group>
	                    		<@dwz.a href="/operate/minPriceGroup-edit?minPriceGroup=${group.id}" target="dialog" title="${group.summary}">${group.name}</@dwz.a>
	                    	</#list>
	                    </td>
	                    <td>
	                     <@sec.any name="FILM_MANAGE">
	                    	<@dwz.a href="/operate/minPriceGroup-add?film=${film.id}" target="dialog" title="新增最低价分类【${film.name}】" rel="minPriceGroup-add">新增最低价分类</@dwz.a>
	                     </@sec.any>
	                    </td>
	                </tr>
              	</#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=filmPage/>
        </div>
    </div>
</div>