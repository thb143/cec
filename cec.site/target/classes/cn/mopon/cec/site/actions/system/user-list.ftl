<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/system/user-list" alt="可输入用户名、姓名检索"/>
    </div>
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/system/user-add" target="dialog" title="新增用户"><span class="a09">新增</span></@dwz.a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="100">用户名</th>
                    <th width="80">姓名</th>
                    <th>默认职务</th>
                    <th width="200">邮箱</th>
                    <th width="90">手机</th>
                    <th width="80" align="center">邮件通知</th>
                    <th width="80" align="center">短信通知</th>
					<th width="130">创建时间</th>
                    <th width="60" align="center">启用状态</th>
                    <th width="180">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list userPage.contents as user>
                <tr>
                    <td>${user.username}</td>
                    <td>${user.name}</td>
                    <td>${user.settings.defaultActor.fullName}</td>
                    <td>${user.settings.email}</td>
                    <td>${user.settings.phone}</td>
                    <td>${user.settings.receiveEmail?string("是","否")}</td>
                    <td>${user.settings.receiveSms?string("是","否")}</td>
					<td>${user.createDate?datetime}</td>
                    <td class="${user.enabled?string(StatusColor.GREEN, StatusColor.GRAY)}">${user.enabled?string("启用","停用")}</td>
                    <td>
                        <@dwz.a href="/system/user-edit?user=${user.id}" target="dialog" height="S" title="编辑用户">编辑</@dwz.a>
                        <#if user.enabled>
                            <@dwz.a href="/system/user-disable?user=${user.id}" target="ajaxTodo" title="您确定要停用该用户吗？">停用</@dwz.a>
                        <#else>
                            <@dwz.a href="/system/user-enable?user=${user.id}" target="ajaxTodo" title="您确定要启用该用户吗？">启用</@dwz.a>
                        </#if>
                        <@dwz.a href="/system/user-pwd-reset?user=${user.id}" target="dialog" width="SS" height="SSS">重置密码</@dwz.a>
                        <@dwz.a href="/system/actor-list?user=${user.id}" target="dialog" width="S" height="SS">职务管理</@dwz.a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=userPage />
        </div>
    </div>
</div>