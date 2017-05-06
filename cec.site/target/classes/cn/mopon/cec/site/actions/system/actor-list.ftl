<@dwz.reload action="/system/actor-list" user=user.id /> 
<div class="page">
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/system/actor-add?user=${user.id}" target="dialog" width="S" height="SS"><span class="a09">新增职务</span></@dwz.a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="50">
            <thead>
                <tr>
                    <th>职务名称</th>
                    <th width="120">关联机构</th>
                    <th width="120">关联角色</th>
                    <th width="80">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list user.actors as actor>
                 <tr>
                     <td>${actor.name}</td>
                     <td>${actor.organ.name}</td>
                     <td>${actor.role.name}</td>
                     <td>
                         <@dwz.a href="/system/actor-edit?actor=${actor.id}" target="dialog" width="S" height="SS" title="编辑职务">编辑</@dwz.a>
                         <#if !actor.isDefaultActor()>
                         	<@dwz.a href="/system/actor-delete?actor=${actor.id}" target="ajaxTodo" callback="dialogReloadDone" title="您确定要删除该职务吗？">删除</@dwz.a>
                         </#if>
                     </td>
                 </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>