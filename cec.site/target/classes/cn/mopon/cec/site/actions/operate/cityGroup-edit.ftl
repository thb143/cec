<div class="page">
    <div class="pageContent">
    	<@dwz.form action="/operate/cityGroup-update">
    		<@s.hidden path="cityGroup.id"/>
            <div class="pageFormContent" layoutH="56">
                <dl class="nowrap">
                    <dt>分类名称：</dt>
                    <dd><@s.input path="cityGroup.name" maxlength="20" class="required"/></dd>
                </dl>
                <dl class="nowrap">
                	<dt>城市：</dt>
                	<dd style="width:85%">
		                <table width="100%" layoutH="105" class="list" >
							<tbody>
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
							</tbody>
						</table>
					</dd>
				</dl>
            </div>
            <@dwz.formBar showCancelBtn=false>
            	<li><@dwz.a class="button" href="/operate/cityGroup-delete?cityGroup=${cityGroup.id}" target="ajaxTodo" title="您是否确定要删除该城市分组？"><span>删除</span></@dwz.a></li>
            </@dwz.formBar>
        </@dwz.form>
    </div>
</div>