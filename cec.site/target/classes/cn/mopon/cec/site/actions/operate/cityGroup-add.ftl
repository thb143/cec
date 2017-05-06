<div class="page">
    <div class="pageContent">
    	<@dwz.form action="/operate/cityGroup-save" >
            <div class="pageFormContent" layoutH="60">
                <dl class="nowrap">
                    <dt>分类名称：</dt>
                    <dd><@s.input path="cityGroup.name" maxlength="20" class="required"/></dd>
                </dl>
                <dl class="nowrap">
                <table width="100%" layoutH="105" class="list" >
				<tbody>
					<#if citySelectModel.cityGroupModels?size gt 0>
						<#list citySelectModel.cityGroupModels as cityGroupModel>
						<tr>
							<td  align="center" valign="middle" width="8%">
								${cityGroupModel.alpha}
							</td>
							<td align="left" width="100%" >
								<@s.checkboxs path="cityGroup.cities" items=cityGroupModel.cities itemLabel="name" itemValue="code" prefix="<label class='dd-span' sytle='margin-left:10px'>" suffix="</label>" />
							</td>
						</tr>
						</#list>
					<#else>
						<tr>
							<td  align="center" valign="middle" width="8%">
								没有可选城市。
							</td>
						</tr>
					</#if>
				</tbody>
				</table>
				</dl>
            </div>
            <@dwz.formBar />
        </@dwz.form>
    </div>
</div>