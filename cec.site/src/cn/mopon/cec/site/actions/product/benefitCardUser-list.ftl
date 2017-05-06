<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/product/benefitCardUser-list" alt="可输入用户手机号码检索">
        	<li>用户来源渠道：<@s.select path="searchModel.channelId" headerLabel="全部" items=channels itemValue="id" itemLabel="name" /></li>
        	<li>首次开卡时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
			<li> 至 <@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="86">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="80" align="center">手机号</th>
                    <th width="55" align="center">用户来源渠道编码</th>
                    <th width="80" align="center">用户来源渠道名称</th>
                    <th width="55" align="center">用户开卡数量</th>
                    <th width="80" align="center">首次开卡时间</th>
                </tr>
            </thead>
            <tbody>
                <#list benefitCardUserPage.contents as benefitCardUser>
                	<tr>
                		<td>${benefitCardUser_index+1}</td>
                		<td>${benefitCardUser.mobile}</td>
                		<td>${benefitCardUser.channel.code}</td>
                		<td>${benefitCardUser.channel.name}</td>
                		<td><#if benefitCardUser.cards?size gt 0><@dwz.a href="/product/benefitCardUser-cards?userId=${benefitCardUser.id}" target="dialog" width="XL" height="S" title="用户卡列表">${benefitCardUser.cards?size}</@dwz.a><#else>${benefitCardUser.cards?size}</#if></td>
                		<td>${benefitCardUser.createDate?datetime}</td>
                	</tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=benefitCardUserPage />
        </div>
    </div>
</div>