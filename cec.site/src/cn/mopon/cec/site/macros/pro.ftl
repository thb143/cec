<#-- 排期信息 -->
<#macro show_view show=show>
	<dl>
		<dt>影院排期编码：</dt>
		<dd>${show.code}</dd>
	</dl>
	<dl>
	    <dt>影片语言：</dt>
	    <dd>${show.language}</dd>
	</dl>
	<dl>
	    <dt>影院编码：</dt>
	    <dd>${show.cinema.code}</dd>
	</dl>
	<dl>
	    <dt>接入商：</dt>
	    <dd>${show.provider}</dd>
	</dl>
	<dl>
	    <dt>影院名称：</dt>
	    <dd>${show.cinema.name}</dd>
	</dl>
	<dl>
	    <dt>影厅编码：</dt>
	    <dd>${show.hall.code}</dd>
	</dl>
	<dl>
	    <dt>影厅名称：</dt>
	    <dd>${show.hall.name}</dd>
	</dl>
	<dl>
	    <dt>影片编码：</dt>
	    <dd>${show.filmCode}</dd>
	</dl>
	<dl>
	    <dt>影片名称：</dt>
	    <dd>${show.film.name}</dd>
	</dl>
	<dl>
	    <dt>放映类型：</dt>
	    <dd>${show.showType}</dd>
	</dl>
	<dl>
	    <dt>放映时间：</dt>
	    <dd>${show.showTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>时长：</dt>
	    <dd>${show.duration} 分钟</dd>
	</dl>
	<dl>
	    <dt>过期时间：</dt>
	    <dd>${show.expireTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>最后同步时间：</dt>
	    <dd>
	    	<#if show.cinema.ticketSettings.lastSyncShowTime??>
	    		${show.cinema.ticketSettings.lastSyncShowTime?datetime}
	    	</#if>
	    </dd>
	</dl>
	<dl>
	    <dt><@s.message code="minPrice"/>：</dt>
	    <dd>${show.minPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="stdPrice"/>：</dt>
	    <dd>${show.stdPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt>创建时间：</dt>
	    <dd>${show.createDate?datetime}</dd>
	</dl>
	<dl>
	    <dt>最后更新时间：</dt>
	    <dd>${show.modifyDate?datetime}</dd>
	</dl>
</#macro>

<#-- 渠道排期信息 -->
<#macro channelShow_view channelShow=channelShow>
	<dl class="nowrap">
		<h3>影院排期信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>影院排期编码：</dt>
		<dd>${channelShow.showCode}</dd>
	</dl>
	<dl>
	    <dt>影片语言：</dt>
	    <dd>${channelShow.language}</dd>
	</dl>
	<dl>
	    <dt>影院编码：</dt>
	    <dd>${channelShow.cinema.code}</dd>
	</dl>
	<dl>
	    <dt>接入商：</dt>
	    <dd>${channelShow.provider}</dd>
	</dl>
	<dl>
	    <dt>影院名称：</dt>
	    <dd>${channelShow.cinema.name}</dd>
	</dl>
	<dl>
	    <dt>影厅编码：</dt>
	    <dd>${channelShow.hall.code}</dd>
	</dl>
	<dl>
	    <dt>影厅名称：</dt>
	    <dd>${channelShow.hall.name}</dd>
	</dl>
	<dl>
	    <dt>影片编码：</dt>
	    <dd>${channelShow.filmCode}</dd>
	</dl>
	<dl>
	    <dt>影片名称：</dt>
	    <dd>${channelShow.film.name}</dd>
	</dl>
	<dl>
	    <dt>放映类型：</dt>
	    <dd>${channelShow.showType}</dd>
	</dl>
	<dl>
	    <dt>放映时间：</dt>
	    <dd>${channelShow.showTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>时长：</dt>
	    <dd>${channelShow.duration} 分钟</dd>
	</dl>
	<dl>
	    <dt>过期时间：</dt>
	    <dd>${channelShow.expireTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt><@s.message code="minPrice"/>：</dt>
	    <dd>${channelShow.minPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="stdPrice"/>：</dt>
	    <dd>${channelShow.stdPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl class="nowrap">
		<h3>渠道排期信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>渠道排期编码：</dt>
		<dd>${channelShow.code}</dd>
	</dl>
	<dl>
	    <dt>渠道名称：</dt>
	    <dd>${channelShow.channel.name}</dd>
	</dl>
	<dl>
	    <dt>类型：</dt>
	    <dd>${channelShow.type}</dd>
	</dl>
	<dl>
	    <dt>上架状态：</dt>
	    <dd class="${channelShow.status.color}">${channelShow.status}</dd>
	</dl>
	<dl>
	    <dt>影院结算规则：</dt>
	    <dd style="color: ${channelShow.cinemaRuleColor};cursor: default;" title="${channelShow.cinemaRuleSummary}">${channelShow.cinemaRuleName}</dd>
	</dl>
	<dl>
	    <dt>渠道结算规则：</dt>
	    <dd style="color: ${channelShow.channelRuleColor};cursor: default;" title="${channelShow.channelRuleSummary}">${channelShow.channelRuleName}</dd>
	</dl>
	<dl>
	    <dt><@s.message code="cinemaPrice"/>：</dt>
	    <dd>${channelShow.cinemaPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="channelPrice"/>：</dt>
	    <dd>${channelShow.channelPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="submitPrice"/>：</dt>
	    <dd>${channelShow.submitPrice?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="connectFee"/>：</dt>
	    <dd>${channelShow.connectFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="circuitFee"/>：</dt>
	    <dd>${channelShow.circuitFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt><@s.message code="subsidyFee"/>：</dt>
	    <dd>${channelShow.subsidyFee?string(",##0.00")} 元</dd>
	</dl>
	<dl>
	    <dt>停售时间：</dt>
	    <dd>${channelShow.stopSellTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>停退时间：</dt>
	    <dd>${channelShow.stopRevokeTime?string("yyyy-MM-dd HH:mm")}</dd>
	</dl>
	<dl>
	    <dt>创建时间：</dt>
	    <dd>${channelShow.createDate?datetime}</dd>
	</dl>
	<dl>
	    <dt>最后更新时间：</dt>
	    <dd>${channelShow.modifyDate?datetime}</dd>
	</dl>
	<#if channelShow.benefitCardSettles?size gt 0>
		<dl class="nowrap">
			<h3>排期权益卡信息</h3>
		</dl>
		<dl class="s-cols" style="width:852px;height:auto">
			<table class="list" width="98%">
		        <thead>
		            <tr>
		              	<th width="80" align="center">卡类名称</th>
						<th width="80" align="center">规则名称</th>
						<th width="45" align="center"><@s.message code="cinemaPrice" /></th>
						<th width="45" align="center"><@s.message code="channelPrice" /></th>
						<th width="45" align="center"><@s.message code="submitPrice" /></th>
						<th width="45" align="center"><@s.message code="circuitFee" /></th>
						<th width="45" align="center"><@s.message code="subsidyFee" /></th>
		            </tr>
		        </thead>
				<tbody>
					<#list channelShow.benefitCardSettles as settle>
						<tr>
							<td align="center">${settle.rule.type.name}</td>
							<td align="center" style="color: ${settle.rule.enabled.color};cursor: default;" title="${settle.rule.summary}">${settle.rule.name}</td>
							<td align="right">${settle.cinemaPrice?string(",##0.00")}</td>
							<td align="right">${settle.channelPrice?string(",##0.00")}</td>
							<td align="right">${settle.submitPrice?string(",##0.00")}</td>
							<td align="right">${settle.circuitFee?string(",##0.00")}</td>
							<td align="right">${settle.subsidyFee?string(",##0.00")}</td>
						</tr>
					</#list>
		        </tbody>
		    </table>
		</dl>
	</#if>
</#macro>
<#-- 结算策略标题信息 -->
<#macro benefitCardTypePolicy_head benefitCardType=benefitCardType show=true>
	<div style="border-bottom:1px solid #CCC;padding-bottom:7px;">
		<div style="font-size:15px;font-weight:bold;line-height:34px;padding-left:5px;">${benefitCardType.name}</div>
		<div>
			【<b>
				<span class="${benefitCardType.status.color}">${benefitCardType.status}</span> - 
				<span class="${benefitCardType.valid.color}">${benefitCardType.valid}</span> - 
				<span class="${benefitCardType.enabled.color}">${benefitCardType.enabled}</span>
			</b>】
    	</div>
    </div>
</#macro>
<#-- 权益卡规则底部功能按钮 -->
<#macro benefitCardType_bar benefitCardType=benefitCardType treeName="benefitCardTypeView">
	<#local isApprove = (benefitCardType.status == PolicyStatus.AUDIT || benefitCardType.status == PolicyStatus.APPROVE)>
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
    	<@sec.any name="PRODUCT_MANAGE">
			<#if benefitCardType.status == PolicyStatus.SUBMIT>
				<li>
					<@dwz.a href="/product/benefitCardType-submit?typeId=${benefitCardType.id}" target="ajaxTodo" class="button" callback="navTabAjaxDone" title="您是否确定要提交审核？"><span>提交审核</span></@dwz.a>
				</li>
			</#if>
		</@sec.any>
		<#if benefitCardType.isBounded()>
			<#if benefitCardType.boundType.logs?size gt 0>
				<@sec.any name="PRODUCT_VIEW">
						<li>
				            <@dwz.a href="/product/benefitCardTypeLog-view?typeId=${benefitCardType.boundType.id}" target="dialog" class="button" height="S"><span>查看审批记录</span></@dwz.a>
				        </li>
				</@sec.any>
			</#if>
		<#else>
			<#if benefitCardType.logs?size gt 0>
				<@sec.any name="PRODUCT_VIEW">
						<li>
				            <@dwz.a href="/product/benefitCardTypeLog-view?typeId=${benefitCardType.id}" target="dialog" class="button" height="S"><span>查看审批记录</span></@dwz.a>
				        </li>
				</@sec.any>
			</#if>
		</#if>
		<@sec.any name="PRODUCT_MANAGE">
			<#if benefitCardType.valid == ValidStatus.VALID>
				<#if benefitCardType.isBounded()>
					<li>
				   		<@dwz.a href="/product/benefitCardType-show-list?benefitCardType=${benefitCardType.boundType.id}" class="button" target="dialog" width="L" height="L" title="权益卡类排期检索"><span>排期检索</span></@dwz.a>
				   	</li>
				<#else>
					<li>
				   		<@dwz.a href="/product/benefitCardType-show-list?benefitCardType=${benefitCardType.id}" class="button" target="dialog" width="L" height="L" title="权益卡类排期检索"><span>排期检索</span></@dwz.a>
				   	</li>
				</#if>
			</#if>
		</@sec.any>
		<#if benefitCardType.isBounded()>
			<#if benefitCardType.boundType.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
		        </li>
	        </#if>
        <#else>
        	<#if benefitCardType.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
		        </li>
	        </#if>
        </#if>
        <@sec.any name="PRODUCT_MANAGE">
        	<#if benefitCardType.status != PolicyStatus.AUDIT && benefitCardType.status != PolicyStatus.APPROVE>
		        <li>
		        	<#if benefitCardType.isBounded()>
		            	<@dwz.a href="/product/benefitCardType-rule-add?benefitCardTypeId=${benefitCardType.boundType.id}" target="dialog" class="button" height="S"><span>新增规则</span></@dwz.a>
		            <#else>
		            	<@dwz.a href="/product/benefitCardType-rule-add?benefitCardTypeId=${benefitCardType.id}" target="dialog" class="button" height="S"><span>新增规则</span></@dwz.a>
		            </#if>
		        </li>
			</#if>
		</@sec.any>
    </@dwz.formBar>
</#macro>

<#-- 结算规则组件 -->
<#macro settleRule path>
    <@s.bind path />
    <#local name = s.name>
    <#local value = s.status.value>
    <#local type = value.type>
    <#local typeId = path + ".type">
    <#local basisTypeId = path + ".basisType">
    <dl class="nowrap">
    <dt>结算规则：</dt>
    <dd><label class='dd-span'><input type="radio" name="${typeId}" id="${typeId}" checked="checked" value="3"/>渠道结算价减价</label></dd>
    </dl>
    <dl settleType="${SettleRuleType.FIXED_MINUS.value}" class="nowrap">
        <dt>${SettleRuleType.FIXED_MINUS}：</dt>
        <dd>
            <span class="dd-span">-&nbsp;</span>
            <input type="text" name="${name}.minus" value="${(value.minus?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
                   title="<@s.message 'input.price.validate' />"/>
            <span class="dd-span">&nbsp;元</span>
        </dd>
    </dl>
</#macro>

<#--
 * 选择卖品。
 *
 * path：选择框绑定的属性路径
 * hallMList：数据模型
 * flag：是否显示选择按钮
 * url：访问路径
 * policyId : 当前特殊定价策略的ID
 -->
<#macro select_snack path hallMList ruleId flag=true url="product/benefitCardTypeSnackRule-cinema-set">
	<@s.bind path />
	<dl class="nowrap">
	    <#if flag==true>
			<dt>选择影院：</dt>
			<input type="hidden" id="selectCinemas" />
			<input type="hidden" id="hallIds" value="${selectHall}"/>
			<dd><input style="display:none;" id="selectHalls" name="selectHalls" class="required" value="${hallMList??}"/><a class="button" href="${url}?ruleId=${ruleId}" target="dialog" rel="specialPolicy-hall-select" mask="true" width='850' height='550'  minable='false' maxable='false' resizable='false' drawable='true'><span>选择影院</span></a></dd>
		</#if>		
	</dl>
	<dl class="nowrap">
		<table class="list" width="567">
			<thead>
				<tr>
					<th width="30" align="center">序号</th>
					<th width="120" align="center">影院名称</th>
					<th align="center">卖品名称</th>
				</tr>
			</thead>
			<tbody <#if flag>id="hallData" </#if> >
			   	<#assign index =0 />
			    <#if hallMList??> 
				<#list hallMList?keys as cinemaName>
					<tr>
						<td align="center">${cinemaName_index+1}</td>
						<td cinemaId="${hallMList[cinemaName][0].cinema.id}">${cinemaName}</td>
						<td>
							<#list hallMList[cinemaName] as snack>
							    <#if snack_index==0 >
							    	<span>${snack.type.name}</span>
							    <#else>
							    	,<span>${snack.type.name}</span>
							    </#if>
							    <#if snack.id?trim >
								<input type="hidden" name="${s.name}[${index}].id" value="${snack.id}" <#if flag> class="hallids" hallid="${snack.id}" </#if> />
									<#assign index =index+1 />
								</#if>
						    </#list>
						</td>
					</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</dl>
</#macro>

<#-- 
 * 选择影院卖品组件。
 *
 * path：选择框绑定的属性路径
 * callBack：回调是否删除已选排期
 --> 
<#macro cinemaSnack path>
	<@s.bind path />
	<div class="page">
		<div class="pageContent">
			<#if cinemaModel.ruleId?if_exists>
				<div class="pageFormContent" layoutH="60">
			<#else>
				<div class="pageHeader">
					<@dwz.pageForm action="/product/benefitCardTypeSnackRule-cinema-set?ruleId=${cinemaModel.ruleId}" targetType="dialog" searchModel=cinemaModel alt="可输入影院名称检索"/>
				</div>
				<div class="pageFormContent" layoutH="95">
	   		</#if>
	   		<table id="cinema-treetable" style="width:100%">
	   			<#list proviceCinemas.items as province>
	   				<tr data-tt-id="${province.code}" id="${province.code}">
	   					<td colspan="2"><@s.checkbox class="checkboxCtrl" group="${province.code}" alias="province" id="${province.code}"/>${province.name}</td>
	   				</tr>
	   				<#list province.cities as cityModel>
	   					<tr data-tt-id="${cityModel.city.code}" data-tt-parent-id="${province.code}" id="${cityModel.city.code}" pId="${province.code}">
	   						<td colspan="2"><input type="checkbox" name="${province.code}"  alias="city" pId="${cityModel.city.code}"/>${cityModel.city.name}</td>
	   					</tr>
	   					<#list cityModel.cinemas as cinema>
	   						<tr data-tt-id="${cinema.code}" data-tt-parent-id="${cityModel.city.code}" id="${cinema.code}" pId="${cityModel.city.code}">
	   							<td style="width:250px;">
	   								<@s.checkbox class="checkboxCtrl" group="${cinema.code}" alias="cinema" cinemaId="${cinema.id}" cinemaName="${cinema.name}" id="${cinema.id}"/>${cinema.name}
	   							</td>
	   							<td>
	   								<#list cinema.snacks as snack>
	   									<input type="checkbox" name="${cinema.code}" alias="hall" hallId="${snack.id}" hallName="${snack.type.name}" id="${snack.id}"/>${snack.type.name}
	   								</#list>
	   							</td>
	   						</tr>
	   					</#list>
	   				</#list>
	   			</#list>
	   		</table>
   			</div>
   		<@pro.button method="addHall()"/>
   		</div>
   	</div>
   	<script type="text/javascript" charset="utf-8">
	  	// 添加影院
		function addHall(){
			var cinemaIds = '',hallIds = '',cineamHtml = '',num = 0,seq = 0;
			$("#cinema-treetable").find("input[type='checkbox'][alias='cinema']").each(function(){
				var halls = $(this).parent().next().find("input[type='checkbox'][alias='hall']").size();
				var checkHalls = $(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").size();
				if(halls == checkHalls && checkHalls > 0){
					cinemaIds += $(this).attr("cinemaId") +",";
				}
				var temp = '<tr><td class="c-text">' + (seq+1) + '</td><td class="l-text" cinemaId='+$(this).attr("cinemaId")+'>' + $(this).attr("cinemaName") +'</td><td class="l-text">';
				var hallText = '';
				$(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").each(function(){
					hallIds += $(this).attr("hallId") + ",";
					hallText += $(this).attr("hallName") + '&nbsp;<input type="hidden" name="${s.name}['+ num++ +'].id" value="' + $(this).attr("hallId") + '" />';;
				});
				if(hallIds != ''){
					temp += hallText +'</td></tr>';
					$("#selectHalls").val(hallIds);
					$("#hallIds").val(hallIds);
				}else{
					$("#selectHalls").val('');
					$("#hallIds").val('');
				}
				if(checkHalls >0){
					cineamHtml += temp;
				}
				if(cinemaIds != ''){
					$("#selectCinemas").val(cinemaIds);
				}
				if(cinemaIds.indexOf($(this).attr('id')) != -1){
					seq ++;
				}
			});
			$("#hallData").html('');
			$("#hallData").append(cineamHtml);
			$.pdialog.closeCurrent();
		}
	</script>
</#macro> 

<#--
 * 选择影厅。
 *
 * path：选择框绑定的属性路径
 * hallMList：数据模型
 * flag：是否显示选择按钮
 * url：访问路径
 * policyId : 当前特殊定价策略的ID
 -->
<#macro select_hall path hallMList ruleId flag=true url="product/benefitCardTypeRule-cinema-set">
	<@s.bind path />
	<dl class="nowrap">
	    <#if flag==true>
			<dt>选择影院：</dt>
			<input type="hidden" id="selectCinemas" />
			<input type="hidden" id="hallIds" value="${selectHall}"/>
			<dd><input style="display:none;" id="selectHalls" name="selectHalls" class="required" value="${hallMList??}"/><a class="button" href="${url}?ruleId=${ruleId}" target="dialog" rel="specialPolicy-hall-select" mask="true" width='850' height='550'  minable='false' maxable='false' resizable='false' drawable='true'><span>选择影院</span></a></dd>
		</#if>		
	</dl>
	<dl class="nowrap">
		<table class="list" width="567">
			<thead>
				<tr>
					<th width="30" align="center">序号</th>
					<th width="120" align="center">影院名称</th>
					<th align="center">影厅名称</th>
				</tr>
			</thead>
			<tbody <#if flag>id="hallData" </#if> >
			   	<#assign index =0 />
			    <#if hallMList??> 
				<#list hallMList?keys as cinemaName>
					<tr>
						<td align="center">${cinemaName_index+1}</td>
						<td cinemaId="${hallMList[cinemaName][0].cinema.id}">${cinemaName}</td>
						<td>
							<#list hallMList[cinemaName] as hall>
							    <#if hall_index==0 >
							    	<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
							    <#else>
							    	,<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
							    </#if>
							    <#if hall.id?trim >
								<input type="hidden" name="${s.name}[${index}].id" value="${hall.id}" <#if flag> class="hallids" hallid="${hall.id}" </#if> />
									<#assign index =index+1 />
								</#if>
						    </#list>
						</td>
					</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</dl>
</#macro>
<#-- 
 * 选择影院影厅组件。
 *
 * path：选择框绑定的属性路径
 * callBack：回调是否删除已选排期
 --> 
<#macro cinemaHall path>
	<@s.bind path />
	<div class="page">
		<div class="pageContent">
			<#if cinemaModel.ruleId?if_exists>
				<div class="pageFormContent" layoutH="60">
			<#else>
				<div class="pageHeader">
					<@dwz.pageForm action="/product/benefitCardTypeRule-cinema-set?ruleId=${cinemaModel.ruleId}" targetType="dialog" searchModel=cinemaModel alt="可输入影院名称检索"/>
				</div>
				<div class="pageFormContent" layoutH="95">
	   		</#if>
	   		<table id="cinema-treetable" style="width:100%">
	   			<#list proviceCinemas.items as province>
	   				<tr data-tt-id="${province.code}" id="${province.code}">
	   					<td colspan="2"><@s.checkbox class="checkboxCtrl" group="${province.code}" alias="province" id="${province.code}"/>${province.name}</td>
	   				</tr>
	   				<#list province.cities as cityModel>
	   					<tr data-tt-id="${cityModel.city.code}" data-tt-parent-id="${province.code}" id="${cityModel.city.code}" pId="${province.code}">
	   						<td colspan="2"><input type="checkbox" name="${province.code}"  alias="city" pId="${cityModel.city.code}"/>${cityModel.city.name}</td>
	   					</tr>
	   					<#list cityModel.cinemas as cinema>
	   						<tr data-tt-id="${cinema.code}" data-tt-parent-id="${cityModel.city.code}" id="${cinema.code}" pId="${cityModel.city.code}">
	   							<td style="width:250px;">
	   								<@s.checkbox class="checkboxCtrl" group="${cinema.code}" alias="cinema" cinemaId="${cinema.id}" cinemaName="${cinema.name}" id="${cinema.id}"/>${cinema.name}
	   							</td>
	   							<td>
	   								<#list cinema.halls as hall>
	   									<input type="checkbox" name="${cinema.code}" alias="hall" hallId="${hall.id}" hallName="${hall.fullName}" id="${hall.id}"/>${hall.fullName}
	   								</#list>
	   							</td>
	   						</tr>
	   					</#list>
	   				</#list>
	   			</#list>
	   		</table>
   			</div>
   		<@pro.button method="addHall()"/>
   		</div>
   	</div>
   	<script type="text/javascript" charset="utf-8">
	  	// 添加影院
		function addHall(){
			var cinemaIds = '',hallIds = '',cineamHtml = '',num = 0,seq = 0;
			$("#cinema-treetable").find("input[type='checkbox'][alias='cinema']").each(function(){
				var halls = $(this).parent().next().find("input[type='checkbox'][alias='hall']").size();
				var checkHalls = $(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").size();
				if(halls == checkHalls && checkHalls > 0){
					cinemaIds += $(this).attr("cinemaId") +",";
				}
				var temp = '<tr><td class="c-text">' + (seq+1) + '</td><td class="l-text" cinemaId='+$(this).attr("cinemaId")+'>' + $(this).attr("cinemaName") +'</td><td class="l-text">';
				var hallText = '';
				$(this).parent().next().find("input[type='checkbox'][alias='hall']:checked").each(function(){
					hallIds += $(this).attr("hallId") + ",";
					hallText += $(this).attr("hallName") + '&nbsp;<input type="hidden" name="${s.name}['+ num++ +'].id" value="' + $(this).attr("hallId") + '" />';;
				});
				if(hallIds != ''){
					temp += hallText +'</td></tr>';
					$("#selectHalls").val(hallIds);
					$("#hallIds").val(hallIds);
				}else{
					$("#selectHalls").val('');
					$("#hallIds").val('');
				}
				if(checkHalls >0){
					cineamHtml += temp;
				}
				if(cinemaIds != ''){
					$("#selectCinemas").val(cinemaIds);
				}
				if(cinemaIds.indexOf($(this).attr('id')) != -1){
					seq ++;
				}
			});
			$("#hallData").html('');
			$("#hallData").append(cineamHtml);
			$.pdialog.closeCurrent();
		}
	</script>
</#macro> 
<#--
 * 选中提交按钮。
 * name：提交方法
 -->
<#macro button method closeMethod>	
	<div class="formBar">
		<ul>
			<li>
				<div class="buttonActive">
					<div class="buttonContent"><button type="button" onclick="${method};">保存</button></div>
				</div>
			</li>
			<li>
				<div class="button">
					<div class="buttonContent"><button class="close" type="button" onclick="${closeMethod}">取消</button></div>
				</div>
			</li>
		</ul>
	</div>
</#macro>	

<#-- 权益卡卡类规则列表 -->
<#macro benefitCardTypeSnackRule_list benefitCardType=benefitCardType rel="" show=true>
	<#local isApprove = (benefitCardType.status == PolicyStatus.AUDIT || benefitCardType.status == PolicyStatus.APPROVE)>
	<#list benefitCardType.snackRulesExcludeCopy as rule>
		<div id="${benefitCardType.id}" class="panel collapse" minH="50">
			<h1>
				<span style="line-height:28px;float:left;padding-top:9px;">
					<#if rule.isBounded()>
		   				<#local specialRuleNameColor = StatusColor.ORANGE>
		   			<#elseif !rule.isBounded() && rule.enabled == EnabledStatus.ENABLED  && rule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GREEN>
		   			<#elseif !rule.isBounded() && rule.enabled != EnabledStatus.ENABLED && rule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GRAY>
		   			<#else>
		   				<#local specialRuleNameColor = StatusColor.RED>
		   			</#if>
		   			<#if rule.isBounded()>
		   				<#if rule.boundRule.name != rule.name>
		   					<span class="${specialRuleNameColor}" title="${rule.name}">${rule_index+1}.&nbsp;${rule.boundRule.name}</span>
		   				<#else>
		   					<span class="${specialRuleNameColor}">${rule_index+1}.&nbsp;${rule.boundRule.name}</span>
		   				</#if>
		   			<#else>
		   				<span class="${specialRuleNameColor}">${rule_index+1}.&nbsp;${rule.name}</span>
		   			</#if>
		   		</span>
		   		<#if show>
			   		<span style="line-height:28px;float:right;padding-right:20px;">
			   			<#if rule.isBounded()>
			   				<@sec.any name="PRODUCT_MANAGE">
			   					<#if !isApprove>
							   		<@dwz.a href="/product/benefitCardType-snackRule-edit?benefitCardTypeSnackRule=${rule.boundRule.id}" target="dialog" width="700" height="500">[编辑]${isApprove}</@dwz.a>
							   		<#if rule.boundRule.valid != ValidStatus.VALID>
							   			<@dwz.a href="/product/benefitCardTypeSnackRule-delete?benefitCardTypeSnackRule=${rule.boundRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
							   		</#if>
						   		</#if>
					   		</@sec.any>
					   	<#else>
					   		<@sec.any name="PRODUCT_MANAGE">
					   			<#if !isApprove>
						   			<@dwz.a href="/product/benefitCardType-snackRule-edit?benefitCardTypeSnackRule=${rule.id}" target="dialog" width="700" height="500">[编辑]${isApprove}</@dwz.a>
							   		<#if rule.valid != ValidStatus.VALID>
							   			<@dwz.a href="/product/benefitCardTypeSnackRule-delete?benefitCardTypeSnackRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
							   		</#if>
						   		</#if>
					   		</@sec.any>
					   		<@sec.any name="PRODUCT_SWITCH">
					   			<#if rule.valid == ValidStatus.VALID>
					   				<#if rule.enabled == EnabledStatus.ENABLED>
					   					<@dwz.a href="/product/benefitCardTypeSnackRule-disabled?benefitCardTypeSnackRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确认要停用该卡类规则？">[停用]</@dwz.a>
					   				<#else>
					   					<@dwz.a href="/product/benefitCardTypeSnackRule-enable?benefitCardTypeSnackRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确认要启用该卡类规则？">[启用]</@dwz.a>
					   				</#if>
					   			</#if>
					   		</@sec.any>
				   		</#if>
			   		</span>
		   		</#if>
			</h1>
			<div>
			  <table  class="list" width="98%">
			  	<tr>
			  		<td width="70">结算类型：</td>
			  		<#if rule.isBounded()>
			  			<td>${rule.boundRule.settleRule.type}${rule.boundRule.settleRule.minus?string(",##0.00")}元</td>
			  		<#else>
			  			<td>${rule.settleRule.type}${rule.settleRule.minus?string(",##0.00")}元</td>
			  		</#if>
			  	</tr>
			    <tr>
			    	<td valign="top">影院范围：</td>
			    	<td>
			    		<#if rule.isBounded()>
			    			<#list rule.boundRule.cinemas?keys as cinemaName>
							    <span style="display:block;padding:3px;">${cinemaName}:
								    <#list rule.boundRule.cinemas[cinemaName] as snack>
									     <#if snack_index==0>
									     	<span>${snack.type.name}</span>
									     <#else>
									     	,<span>${snack.type.name}</span>
									     </#if>
								    </#list>
							    </span>
						    </#list>
			    		<#else>
			    			<#list rule.cinemas?keys as cinemaName>
							    <span style="display:block;padding:3px;">${cinemaName}:
								    <#list rule.cinemas[cinemaName] as snack>
									     <#if snack_index==0>
									     	<span>${snack.type.name}</span>
									     <#else>
									     	,<span>${snack.type.name}</span>
									     </#if>
								    </#list>
							    </span>
						    </#list>
			    		</#if>
				   </td>
			    </tr>
			 </table>
			</div>
		</div>
	</#list>
</#macro>

<#-- 权益卡卡类规则列表 -->
<#macro benefitCardTypeRule_list benefitCardType=benefitCardType rel="" show=true>
	<#local isApprove = (benefitCardType.status == PolicyStatus.AUDIT || benefitCardType.status == PolicyStatus.APPROVE)>
	<#list benefitCardType.rulesExcludeCopy as rule>
		<div id="${benefitCardType.id}" class="panel collapse" minH="50">
			<h1>
				<span style="line-height:28px;float:left;padding-top:9px;">
					<#if rule.isBounded()>
		   				<#local specialRuleNameColor = StatusColor.ORANGE>
		   			<#elseif !rule.isBounded() && rule.enabled == EnabledStatus.ENABLED  && rule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GREEN>
		   			<#elseif !rule.isBounded() && rule.enabled != EnabledStatus.ENABLED && rule.valid == ValidStatus.VALID>
		   				<#local specialRuleNameColor = StatusColor.GRAY>
		   			<#else>
		   				<#local specialRuleNameColor = StatusColor.RED>
		   			</#if>
		   			<#if rule.isBounded()>
		   				<#if rule.boundRule.name != rule.name>
		   					<span class="${specialRuleNameColor}" title="${rule.name}">${rule_index+1}.&nbsp;${rule.boundRule.name}</span>
		   				<#else>
		   					<span class="${specialRuleNameColor}">${rule_index+1}.&nbsp;${rule.boundRule.name}</span>
		   				</#if>
		   			<#else>
		   				<span class="${specialRuleNameColor}">${rule_index+1}.&nbsp;${rule.name}</span>
		   			</#if>
		   		</span>
		   		<#if show>
			   		<span style="line-height:28px;float:right;padding-right:20px;">
			   			<#if rule.isBounded()>
			   				<@sec.any name="PRODUCT_MANAGE">
			   					<#if !isApprove>
							   		<@dwz.a href="/product/benefitCardType-rule-edit?benefitCardTypeRule=${rule.boundRule.id}" target="dialog" width="700" height="500">[编辑]${isApprove}</@dwz.a>
							   		<#if rule.boundRule.valid != ValidStatus.VALID>
							   			<@dwz.a href="/product/benefitCardTypeRule-delete?benefitCardTypeRule=${rule.boundRule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
							   		</#if>
						   		</#if>
					   		</@sec.any>
					   	<#else>
					   		<@sec.any name="PRODUCT_MANAGE">
					   			<#if !isApprove>
						   			<@dwz.a href="/product/benefitCardType-rule-edit?benefitCardTypeRule=${rule.id}" target="dialog" width="700" height="500">[编辑]${isApprove}</@dwz.a>
							   		<#if rule.valid != ValidStatus.VALID>
							   			<@dwz.a href="/product/benefitCardTypeRule-delete?benefitCardTypeRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="您是否确定要删除该规则？">[删除]</@dwz.a>
							   		</#if>
						   		</#if>
					   		</@sec.any>
					   		<@sec.any name="PRODUCT_SWITCH">
					   			<#if rule.valid == ValidStatus.VALID>
					   				<#if rule.enabled == EnabledStatus.ENABLED>
					   					<@dwz.a href="/product/benefitCardTypeRule-disabled?benefitCardTypeRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="停用规则将作废相关的渠道排期，您是否确认要停用该卡类规则?？">[停用]</@dwz.a>
					   				<#else>
					   					<@dwz.a href="/product/benefitCardTypeRule-enable?benefitCardTypeRule=${rule.id}" callback="navTabAjaxDone" target="ajaxTodo" title="启用规则将作废相关的渠道排期，您是否确认要启用该卡类规则?？">[启用]</@dwz.a>
					   				</#if>
					   			</#if>
					   		</@sec.any>
				   		</#if>
			   		</span>
		   		</#if>
			</h1>
			<div>
			  <table  class="list" width="98%">
			  	<tr>
			  		<td width="70">结算类型：</td>
			  		<#if rule.isBounded()>
			  			<td>${rule.boundRule.settleRule.type}${rule.boundRule.settleRule.minus?string(",##0.00")}元</td>
			  		<#else>
			  			<td>${rule.settleRule.type}${rule.settleRule.minus?string(",##0.00")}元</td>
			  		</#if>
			  	</tr>
			  	<tr>
			  		<td width="70">放映类型：</td>
			  		<#if rule.isBounded()>
			  			<td>${rule.boundRule.showTypesText}</td>
			  		<#else>
			  			<td>${rule.showTypesText}</td>
			  		</#if>
			  	</tr>
			    <tr>
			    	<td valign="top">影院范围：</td>
			    	<td>
			    		<#if rule.isBounded()>
			    			<#list rule.boundRule.cinemas?keys as cinemaName>
							    <span style="display:block;padding:3px;">${cinemaName}:
								    <#list rule.boundRule.cinemas[cinemaName] as hall>
									     <#if hall_index==0>
									     	<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
									     <#else>
									     	,<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
									     </#if>
								    </#list>
							    </span>
						    </#list>
			    		<#else>
			    			<#list rule.cinemas?keys as cinemaName>
							    <span style="display:block;padding:3px;">${cinemaName}:
								    <#list rule.cinemas[cinemaName] as hall>
									     <#if hall_index==0>
									     	<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
									     <#else>
									     	,<span>${hall.name}<#if hall.hallType??>(${hall.hallType.name})</#if></span>
									     </#if>
								    </#list>
							    </span>
						    </#list>
			    		</#if>
				   </td>
			    </tr>
			 </table>
			</div>
		</div>
	</#list>
</#macro>

<#-- 权益卡审批记录 -->
<#macro benefitCardTypeLog_view benefitCardTypeLog=benefitCardTypeLog>
	<dl class="nowrap">
		<h3>审批信息</h3>
	</dl>
	<div class="divider"></div>
	<dl>
		<dt>提交人：</dt>
		<dd>${benefitCardTypeLog.submitter.name}</dd>
	</dl>
	<dl>
		<dt>提交时间：</dt>
		<dd>${benefitCardTypeLog.submitTime?datetime}</dd>
	</dl>
	<#if benefitCardTypeLog.auditor??>
		<dl>
			<dt>审核人：</dt>
			<dd>${benefitCardTypeLog.auditor.name}</dd>
		</dl>
		<dl>
			<dt>审核时间：</dt>
			<dd>${benefitCardTypeLog.auditTime?datetime}</dd>
		</dl>
	</#if>
	<#if benefitCardTypeLog.approver??>
		<dl>
			<dt>审批人：</dt>
			<dd>${benefitCardTypeLog.approver.name}</dd>
		</dl>
		<dl>
			<dt>审批时间：</dt>
			<dd>${benefitCardTypeLog.approveTime?datetime}</dd>
		</dl>
	</#if>
	<#if benefitCardTypeLog.refuseNote??>
		<dl class="nowrap">
			<dt>退回意见：</dt>
			<dd class="${StatusColor.RED}">${benefitCardTypeLog.refuseNote} </dd>
		</dl>
	</#if>
</#macro>

<#-- 权益卡类基本信息 -->
<#macro benefitCardType_basicInfo benefitCardType=benefitCardType>
		<dl>
	         <dt>卡类编号：</dt>
	         <dd>${benefitCardType.code}</dd>
	    </dl>
	    <dl>
	         <dt>卡号前缀：</dt>
	         <dd>${benefitCardType.prefix}</dd>
	    </dl>
	    <dl>
	         <dt>卡类名称：</dt>
	         <dd>${benefitCardType.name}</dd>
	    </dl>
	    <dl>
	         <dt>开卡金额：</dt>
	         <dd>${benefitCardType.initAmount?string(",##0.00")}</dd>
	    </dl>
	    <dl>
	    	<dt>有效期：</dt>
	    	<dd>${benefitCardType.validMonth}</dd>
	    </dl>
	    <dl>
	    	<dt>续费金额：</dt>
	    	<dd>${benefitCardType.rechargeAmount?string(",##0.00")}</dd>
	    </dl>
	    <dl>
	    	<dt>优惠总量：</dt>
	    	<dd>${benefitCardType.totalDiscountCount}</dd>
	    </dl>
	    <dl>
	    	<dt>单日优惠次数：</dt>
	    	<dd>${benefitCardType.dailyDiscountCount}</dd>
	    </dl>
	    <dl class="nowrap">
	    	<dt>可开卡渠道：</dt>
	    	<dd>
	    		<#list benefitCardType.channels as channel>
	    			<#if channel_index == 0>
	    				${channel.name}
	    			<#else>
	    				,${channel.name}
	    			</#if>
	    		</#list>
	    	</dd>
	    </dl>
</#macro>

<#-- 权益卡规则审核底部功能按钮 -->
<#macro benefitCardTypeAudit_bar benefitCardTypeLog=benefitCardTypeLog >
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
		<li>
			<@dwz.a class="button" href="/product/benefitCardType-audit-pass?cardTypeLogId=${benefitCardTypeLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核通过该卡类？"><span>审核通过</span></@dwz.a>
		</li>
		<li>
			<@dwz.a class="button" href="/product/benefitCardType-audit-refuse?cardTypeLogId=${benefitCardTypeLog.id}" target="dialog" width="S" height="SS"><span>审核退回</span></@dwz.a>
		</li>
		<#if benefitCardTypeLog.type.isBounded()>
			<#if benefitCardTypeLog.type.boundType.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
				</li>
			</#if>
		<#else>
			<#if benefitCardTypeLog.type.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
				</li>
			</#if>
		</#if>
	</@dwz.formBar>
</#macro>


<#-- 权益卡规则审批底部功能按钮 -->
<#macro benefitCardTypeApprove_bar benefitCardTypeLog=benefitCardTypeLog >
	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
		<li>
			<@dwz.a class="button" href="/product/benefitCardType-approve-pass?cardTypeLogId=${benefitCardTypeLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批通过该卡类？"><span>审批通过</span></@dwz.a>
		</li>
		<li>
			<@dwz.a class="button" href="/product/benefitCardType-approve-refuse?cardTypeLogId=${benefitCardTypeLog.id}" target="dialog" width="S" height="SS"><span>审批退回</span></@dwz.a>
		</li>
		<#if benefitCardTypeLog.type.isBounded()>
			<#if benefitCardTypeLog.type.boundType.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
				</li>
			</#if>
		<#else>
			<#if benefitCardTypeLog.type.rules?size gt 0>
				<li>
					<a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
				</li>
			</#if>
		</#if>
	</@dwz.formBar>
</#macro>