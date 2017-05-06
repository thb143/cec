<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/operate/channel-list" alt="可输入渠道名称检索"/>
    </div>
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <@sec.any name="CHANNEL_MANAGE">
	                <li>
	                    <@dwz.a href="/operate/channel-add" target="dialog" title="新增渠道" height="S"><span class="a09">新增渠道</span></@dwz.a>
	                </li>
                </@sec.any>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="80" align="center">渠道编号</th>
                    <th width="120">渠道名称</th>
                    <th width="55" align="center">渠道类型</th>
                    <th align="center">开放票务接口</th>
                    <th width="55" align="center">开放状态</th>
                    <th width="55" align="center">销售状态</th>
                    <th width="120">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list channelPage.contents as channel>
                    <tr>
                        <td>${channel_index+1}</td>
                        <td>${channel.code}</td>
                        <td><@dwz.a href="/operate/channel-view?channelId=${channel.id}" target="dialog" title="查看渠道" height="S">${channel.name}</@dwz.a></td>
                        <td>${channel.type}</td>
                   		<td class="l-text">${channel.settings.ticketApiMethodsTexts}</td>
                        <td class="${channel.opened?string('${StatusColor.GREEN}','${StatusColor.GRAY}')} center">${channel.opened?string("开放","关闭")}</td>
                        <td class="${channel.salable?string('${StatusColor.GREEN}','${StatusColor.GRAY}')} center">${channel.salable?string("开放","关闭")}</td>
                        <td>
                            <@sec.any name="CHANNEL_MANAGE">
                            	<@dwz.a href="/operate/channel-edit?channelId=${channel.id}" target="dialog" title="编辑渠道" height="S">编辑</@dwz.a>
                            </@sec.any>
                            <@sec.any name="CHANNEL_SET">
                                 <@dwz.a href="/operate/channel-set?settingsId=${channel.settings.id}" target="dialog" width="S" height="S" title="渠道设置">设置</@dwz.a>
                             </@sec.any>
                            <@sec.any name="CHANNEL_SWITCH">
                                 <#if channel.opened>
                                     <@dwz.a href="/operate/channel-disable?channel=${channel.id}" target="ajaxTodo" title="关闭渠道将作废相关的渠道排期，您是否确定要关闭该渠道？">关闭</@dwz.a>
                                 <#else>
                                     <@dwz.a href="/operate/channel-enable?channel=${channel.id}" target="ajaxTodo" title="开放渠道将重新生成相关的渠道排期，您是否确定要开放该渠道？">开放</@dwz.a>
                                 </#if>
                                 <#if channel.salable>
	                        		 <@dwz.a href="/operate/channel-close-salable?channel=${channel.id}" target="ajaxTodo" title="停售渠道将下架相关的渠道排期，您是否确定要停售该渠道？">停售</@dwz.a>
	                        	 <#else>
	                        		 <@dwz.a href="/operate/channel-open-salable?channel=${channel.id}" target="ajaxTodo" title="启售渠道将上架相关的渠道排期，您是否确定要启售该渠道？">启售</@dwz.a>
	                        	 </#if>
                             </@sec.any>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=channelPage />
        </div>
    </div>
</div>