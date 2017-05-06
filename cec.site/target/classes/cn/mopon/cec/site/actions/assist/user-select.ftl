<div class="page">
    <div class="pageHeader">
        <div class="subBar">
			<ul>
				<li><div class="button"><div class="buttonContent"><button type="button" multLookup="userId" warn="请选择用户">确定</button></div></div></li>
			</ul>
		</div>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="65">
            <thead>
                <tr>
                	<th width="30"><input type="checkbox" class="checkboxCtrl" group="userId" /></th>
                    <th align="center">用户名</th>
                    <th width="150" align="center">姓名</th>
                    <th width="150" align="center">邮箱</th>
                    <th width="150" align="center">手机</th>
                </tr>
            </thead>
            <tbody>
                <#list sendUserList as user>
                <tr>
                	<#if selectedUserIds??><#assign selected = false><#list selectedUserIds as id><#if user.id == id><#assign selected = true></#if></#list></#if> 
                	<#if selected>
                		<td><input type="checkbox" name="userId" value="{${inputName}Names:'${user.name}',${inputName}:'${user.id}'}" checked/></td>
                    <#else>
                    	<td><input type="checkbox" name="userId" value="{${inputName}Names:'${user.name}',${inputName}:'${user.id}'}"/></td>
                    </#if>
                    <td>${user.username}</td>
                    <td>${user.name}</td>
                    <td>${user.settings.email}</td>
                    <td>${user.settings.phone}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<script>
$(document).ready(function() {
	$('button[multLookup=userId]').click(function(){
		var href = $("a#${inputName}").attr("href");
		if(href.indexOf('selectedUserIds') == -1) {
			var bool = false;
			 $('input[type="checkbox"][name="userId"]:checked').each(function(){
				bool = true;
			});
			if(bool) {
				$("a#${inputName}").attr("href",$("a#${inputName}").attr("href") + "&selectedUserIds={${inputName}}");
			}
		}
	});
});
</script>