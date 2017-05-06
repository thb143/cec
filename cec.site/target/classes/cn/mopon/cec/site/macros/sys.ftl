<#macro organTree organ>
	<ul class="tree" layoutH="36">
		<@organTreeNode organ=organ />
	</ul>
</#macro>

<#macro organTreeNode organ>
	<li>
		<@dwz.a href="/system/organ-edit?organ=${organ.id}" target="ajax" rel="organBox" organId="${organ.id}" title="${organ.name}">
            ${organ.name}
		</@dwz.a>
		<#if organ.childs?size gt 0>
			<ul>
		        <#list organ.childs as childOrgan>
		        	<@organTreeNode organ=childOrgan />
		        </#list>
        	</ul>
        </#if>
    </li>
</#macro>

<#macro permissions permissionGroups>
    <#list permissionGroups as permissionGroup>
		<fieldset>
			<legend><label><input type="checkbox" onclick="selectAll(this)"/>${permissionGroup.name}</label></legend>
	        <#list permissionGroup.permissions as permission>
		        <label>
		            <input type="checkbox" name="permissionIds" value="${permission.id}" onclick="selectParent(this)"<#if permissionIds?seq_contains(permission.id)> checked="checked"</#if> />
		            ${permission.name}
		        </label>
	        </#list>
		</fieldset>
    </#list>
    <script>
    	//初始化判断，如果所有子节点都已选中，则选中全选的节点。
    	$(function(){
    		$(this).find("fieldset").each(function(){
    			var blag = true;
    			$(this).find("label input[name='permissionIds']").each(function(){
    				if (!this.checked) {
        				return blag = false;
        			}
        		});
    			if (blag) {
    				$(this).find("legend label input[type='checkbox']").attr("checked",true);
    			}
     		});
    	});
    	//全选、反选
    	function selectAll(obj){
    		var fieldset = $(obj).closest("fieldset");
    		var checked = $(obj).attr("checked");
    		fieldset.find("label input[name='permissionIds']").each(function(){
    			if(checked) {
    				if (!this.checked) {
    					this.checked = true;
    				}
    			} else {
    				if (this.checked) {
    					this.checked = false;
    				}
    			}
    		});
    	}
    	//所有子节点选中，则选择全选节点，否则不选中。
    	function selectParent(obj){
    		var fieldset = $(obj).closest("fieldset");
    		var legend = fieldset.find("legend input[type='checkbox']");
    		var checked = legend.attr("checked");
    		var blag = true;
    		fieldset.find("label input[name='permissionIds']").each(function(){
    			if (!this.checked) {
    				return blag = false;
    			}
    		});
    		if (checked && !blag) {
    			legend.attr("checked",false);
    		}
    		if (!checked && blag) {
    			legend.attr("checked",true);
    		}
    	}
    </script>
</#macro>

<#macro log_view log>
	<dl>
		<dt>操作时间：</dt>
		<dd>${log.createDate?datetime}</dd>
	</dl>
	<dl>
		<dt>操作用户：</dt>
		<dd>${log.creator}</dd>
	</dl>
	<dl class="nowrap">
		<dt>操作内容：</dt>
		<dd>${log.message}</dd>
	</dl>
	<#if log.hasData()>
		<table class="list" width="98%">
			<thead>
				<tr>
					<th width="20%">字段</th>
					<th width="40%">原值</th>
					<th width="40%">新值</th>
				</tr>
			</thead>
			<tbody>
				<#list log.toLogData() as data>
					<tr>
						<td style="font-weight:bold">${data.text}</td>
						<td style="word-break:break-all">${data.origData}</td>
						<td style="word-break:break-all;<#if data.isChanged()>color:red;</#if>">${data.newData}</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</#if>
</#macro>

<!-- 用户设置 -->
<#macro user_settings path>
	<dl>
		<dt>邮箱：</dt>
		<dd><@s.input path="${path}.email" maxlength="60" class="required email" /></dd>
	</dl>
	<dl>
	    <dt>手机：</dt>
	    <dd><@s.input path="${path}.phone" maxlength="11" class="required number" /></dd>
	</dl>
	<dl>
	    <dt>接收邮件通知：</dt>
	    <dd><@s.radio path="${path}.receiveEmail" trueText="是" falseText="否" prefix="<label class='dd-span'>" suffix="</label>"/></dd>
	</dl>
	<dl>
	    <dt>接收短信通知：</dt>
	    <dd><@s.radio path="${path}.receiveSms" trueText="是" falseText="否" prefix="<label class='dd-span'>" suffix="</label>"/></dd>
	</dl>
</#macro>

<#--
 * 分页导航条。
 *
 * pageModel：分页对象
 * onchange：每页条数选择框的onchange事件。不设置时根据targetType类型自动设置适合的事件函数，设置时将覆盖默认设置。
 * targetType：导航类型（navTab/dialog）
 -->
<#macro pageNav pageModel onchange targetType="navTab" rel="">
    <#if !onchange??>
        <#if targetType == "navTab">
            <#local onchange="navTabPageBreak({numPerPage:this.value}, '${rel}');" />
        </#if>
        <#if targetType == "dialog">
            <#local onchange="dialogPageBreak({numPerPage:this.value}, '${rel}');" />
        </#if>
    </#if>
<div class="pagination" targetType="${targetType}" rel="${rel}" totalCount="${pageModel.count}"
     numPerPage="${pageModel.size}" pageNumShown="2" currentPage="${pageModel.number}"></div>
</#macro>