<#-- 行政区划选择组件 -->
<#macro countyCombox path>
    <@s.bind path />
    <#if s.status.value??>
        <#local selectedProvince = s.status.actualValue.city.province>
        <#local selectedCity = s.status.actualValue.city>
        <#local randomNum = .now?datetime?string("yyyyMMddHHmmssSSS")/>
    <select id="province${randomNum}" name="province" ref="city${randomNum}" refUrl="<@s.url "/assist/city-list?provinceCode={value}" />"
            class="combox">
        <@s.options items=DistrictHelper.getProvinces() itemValue="code" itemLabel="name" values="${selectedProvince.code}" />
    </select>
    <select id="city${randomNum}" name="city" ref="county${randomNum}" refUrl="<@s.url "/assist/county-list?cityCode={value}" />"
            class="combox">
        <@s.options items=selectedProvince.cities itemValue="code" itemLabel="name" values="${selectedCity.code}" />
    </select>
        <@s.select path=path items=selectedCity.counties itemValue="code" itemLabel="name" class="combox required" id="county${randomNum}"/>
    <#else>
    <select id="province${randomNum}" name="province" ref="city${randomNum}" refUrl="<@s.url "/assist/city-list?provinceCode={value}" />"
            class="combox">
        <option value="">所有省市</option>
        <@s.options items=DistrictHelper.getProvinces() itemValue="code" itemLabel="name" />
    </select>
    <select id="city${randomNum}" name="city" ref="county${randomNum}" refUrl="<@s.url "/assist/county-list?cityCode={value}" />"
            class="combox">
        <option value="">所有城市</option>
    </select>
    <select id="county${randomNum}" name="${s.name}" class="combox required">
        <option value="">所有区县</option>
    </select>
    </#if>
</#macro>

<#-- 费用规则组件 -->
<#macro feeRule path>
    <@s.bind path />
    <#local typeId = path + ".type">
    <#local typeName = s.name + ".type">
    <#local amountId = path + ".amount">
    <#local amountName = s.name + ".amount">
    <#local percentId = path + ".percent">
    <#local percentName = s.name + ".percent">
    <#list FeeRuleType?values as feeRuleType>
        <#local isChecked = s.status.value.type == feeRuleType>
        <#if feeRuleType == FeeRuleType.NONE>
        <span feeType="${FeeRuleType.NONE.value}">
				<label class="dd-span">
                    <input type="radio" id="${typeId}" name="${typeName}"
                           value="${FeeRuleType.NONE.value}"<#if isChecked> checked="checked"</#if> />
                ${FeeRuleType.NONE}
                </label>
			</span>
        </#if>
        <#if feeRuleType == FeeRuleType.FIXED>
        <span feeType="${FeeRuleType.FIXED.value}">
				<label class="dd-span">
                    <input type="radio" id="${typeId}" name="${typeName}"
                           value="${FeeRuleType.FIXED.value}"<#if isChecked> checked="checked"</#if> />
                ${FeeRuleType.FIXED}&nbsp;
                </label>
				<input type="text" id="${amountId}" name="${amountName}" min="0" max="1000"
                       class="required number ss-input hideError" value="${(s.status.value.amount?string('0.00'))!}"
                       customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/>
				<span class="dd-span">&nbsp;元&nbsp;</span>
			</span>
        </#if>
        <#if feeRuleType == FeeRuleType.PERCENT>
        <span feeType="${FeeRuleType.PERCENT.value}">
				<label class="dd-span">
                    <input type="radio" id="${typeId}" name="${typeName}"
                           value="${FeeRuleType.PERCENT.value}"<#if isChecked> checked="checked"</#if> />
                ${FeeRuleType.PERCENT}&nbsp;
                </label>
				<input type="text" id="${percentId}" name="${percentName}" min="0" max="100"
                       class="required number ss-input hideError" value="${(s.status.value.percent?string('0.00'))!}"
                       customvalid="regexPrice(element)" title="<@s.message 'input.percent.validate' />"/>
				<span class="dd-span">&nbsp;%&nbsp;</span>
			</span>
        </#if>
    </#list>
<script>
    $(function () {
        var feeRuleTypeRadios = $("#" + "${typeId}".escape());
        var feeRuleSpans = feeRuleTypeRadios.closest("span[feeType]");
        feeRuleTypeRadios.each(function () {
            $(this).click(function () {
                var selectedFeeType = $(this).val();
                feeRuleSpans.each(function () {
                    var inputText = $(this).find("input[type=text]");
                    if ($(this).attr("feeType") == selectedFeeType) {
                        inputText.addClass("required");
                        inputText.removeAttr("disabled");
                        inputText.focus();
                    } else {
                        inputText.removeClass("required error");
                        inputText.attr("disabled", "disabled");
                    }
                });
            });
            if ($(this).attr("checked")) {
                $(this.click());
            }
        });
    });
</script>
</#macro>

<#-- 时段规则组件 -->
<#macro periodRule path>
    <@s.bind path />
    <#local name = s.name>
    <#local items = s.status.value.items>
    <#local type = s.status.value.type>
    <#local typeId = path + ".type">
<dl class="nowrap">
    <dt>时间限制：</dt>
    <dd><@s.radios path="${typeId}" id="${typeId}" items=PeriodRuleType?values itemLabel="text" itemValue="value" prefix="<label class='dd-span'>" suffix="</label>" /></dd>
</dl>
<dl periodType="${PeriodRuleType.DAY.value}" class="nowrap">
    <dt>${PeriodRuleType.DAY}：</dt>
    <dd>
        <table class="list itemDetail" addButton="添加时间" width="100%">
            <thead>
            <tr>
                <th type="span" align="center" defaultVal="每天">日期</th>
                <@timeTh name />
                <th type="del" width="60" align="center">操作</th>
            </tr>
            </thead>
            <tbody>
                <#if type == PeriodRuleType.DAY>
                    <#list items as item>
                    <tr class="unitBox">
                        <td align="center"><span>每天</span></td>
                        <@timeInput name item item_index />
                        <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </dd>
</dl>
<dl periodType="${PeriodRuleType.WEEK.value}" class="nowrap">
    <dt>${PeriodRuleType.WEEK}：</dt>
    <dd>
        <table class="list itemDetail" addButton="添加时间" width="100%">
            <thead>
            <tr>
                <#list ["周一", "周二", "周三", "周四", "周五", "周六", "周日"] as week>
                    <th type="checkbox" name="${name}.items[#index#].days" defaultVal="${week_index + 1}" align="center"
                        fieldClass="required hideError">${week}</th>
                </#list>
                <@timeTh name />
                <th type="del" width="60" align="center">操作</th>
            </tr>
            </thead>
            <tbody>
                <#if type == PeriodRuleType.WEEK>
                    <#list items as item>
                    <tr class="unitBox">
                        <#list [1, 2, 3, 4, 5, 6, 7] as day>
                           <#if item.days?if_exists>
	                    		<#local isChecked = item.days?seq_contains(day)>
	                    			<td align="center"><input type="checkbox" name="${name}.items[${item_index}].days"
	                                                  value="${day}" class="required hideError"<#if isChecked>
	                                                  checked="checked"</#if> /></td>
	                    	<#else>
	                    		 <td align="center"><input type="checkbox" name="${name}.items[${item_index}].days"
	                                                  value="${day}" class="required hideError" /></td>
	                    	</#if>
                        </#list>
                        <@timeInput name item item_index />
                        <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </dd>
</dl>
<dl periodType="${PeriodRuleType.HOLIDAY.value}" class="nowrap">
    <dt>${PeriodRuleType.HOLIDAY}：</dt>
    <dd>
        <table class="list itemDetail" addButton="添加时间" width="100%">
            <thead>
            <tr>
                <th type="date" name="${name}.items[#index#].date" align="center" size="39" format="yyyy-MM-dd"
                    fieldClass="required hideError"  fieldAttrs={readonly:"true"} >日期
                </th>
                <@timeTh name />
                <th type="del" width="60" align="center">操作</th>
            </tr>
            </thead>
            <tbody>
                <#if type == PeriodRuleType.HOLIDAY>
                    <#list items as item>
                    <tr class="unitBox">
                        <td align="center">
                            <input type="text" name="${name}.items[${item_index}].date" 
                                   size="39" datefmt="yyyy-MM-dd" value="${item.date.toString('yyyy-MM-dd')}"
                                   class="date required hideError" readonly="true" />
                        </td>
                        <@timeInput name item item_index />
                        <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </dd>
</dl>
<dl periodType="${PeriodRuleType.ROUND.value}" class="nowrap">
    <dt>${PeriodRuleType.ROUND}：</dt>
    <dd>
        <table class="list itemDetail" addButton="添加时间" width="100%">
            <thead>
            <tr>
                <th type="date" name="${name}.items[#index#].startDate" align="center" size="18" format="yyyy-MM-dd"
                    fieldClass="required hideError" fieldAttrs={id:"startDate#index#",readonly:"true"} >开始日期
                </th>
                <th type="date" name="${name}.items[#index#].endDate" align="center" size="18" format="yyyy-MM-dd"
                    fieldClass="required hideError" 
                    fieldAttrs={minRelation:"#startDate#index#",customvalid:"geDate(element,'#startDate#index#')",title:"结束日期不能小于开始日期。",readonly:"true"}>结束日期
                </th>
                <@timeTh name />
                <th type="del" width="60" align="center">操作</th>
            </tr>
            </thead>
            <tbody>
                <#if type == PeriodRuleType.ROUND>
                    <#list items as item>
                    <tr class="unitBox">
                        <td align="center"><input type="text" name="${name}.items[${item_index}].startDate"
                                                  value="${item.startDate.toString('yyyy-MM-dd')}" size="18"
                                                  datefmt="yyyy-MM-dd" class="date required hideError" readonly="true"
                                                  id="startDate${item_index}"/></td>
                        <td align="center"><input type="text" name="${name}.items[${item_index}].endDate"
                                                  value="${item.endDate.toString('yyyy-MM-dd')}" size="18" readonly="true"
                                                  datefmt="yyyy-MM-dd" class="date required hideError" minRelation="#startDate${item_index}"
                                                  customvalid="geDate(element,'#startDate${item_index}')"
                                                  title="结束日期不能小于开始日期。"/></td>
                        <@timeInput name item item_index />
                        <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </dd>
</dl>
<dl periodType="${PeriodRuleType.ROUNDWEEK.value}" class="nowrap">
    <dt>${PeriodRuleType.ROUNDWEEK}：</dt>
    <dd>
        <table class="list itemDetail" addButton="添加时间" width="100%">
            <thead>
            <tr>
             	<th type="date" name="${name}.items[#index#].startDate" align="center" size="10" format="yyyy-MM-dd"
                    fieldClass="required hideError" fieldAttrs={id:"startDate#index#",readonly:"true"} >开始日期
                </th>
                <th type="date" name="${name}.items[#index#].endDate" align="center" size="10" format="yyyy-MM-dd"
                    fieldClass="required hideError" 
                    fieldAttrs={minRelation:"#startDate#index#",customvalid:"geDate(element,'#startDate#index#')",title:"结束日期不能小于开始日期。",readonly:"true"}>结束日期
                </th>
                <#list ["周一", "周二", "周三", "周四", "周五", "周六", "周日"] as week>
                    <th type="checkbox" name="${name}.items[#index#].days" defaultVal="${week_index + 1}" align="center"
                        fieldClass="required hideError">${week}</th>
                </#list>
              	<th type="text" name="${name}.items[#index#].startTime" width="50" align="center" size="6" defaultval="00:00"
				    fieldClass="Wdate required hideError" fieldAttrs={onClick:"WdatePicker(\{dateFmt:'HH:mm',isShowToday:false\})",customvalid:'regexTime(element)',id:'startTime#index#',title:'<@s.message "input.time.validate" />'}>开始时间
				</th>
				<th type="text" name="${name}.items[#index#].endTime" width="50" align="center" size="6" defaultval="23:59"
				    fieldClass="Wdate required hideError" 
				    fieldAttrs={onClick:"WdatePicker(\{dateFmt:'HH:mm',isShowToday:false\})",customvalid:"geTime(element,'#startTime#index#')",title:"<@s.message 'input.time.validate' />结束时间不能小于开始时间。"}>结束时间
				</th>
                <th type="del" width="30" align="center">操作</th>
            </tr>
            </thead>
            <tbody>
            	<#if type == PeriodRuleType.ROUNDWEEK>
	            	<#list items as item>
	                   <tr class="unitBox">
	                   	<td align="center"><input type="text" name="${name}.items[${item_index}].startDate"
	                                                 value="${item.startDate.toString('yyyy-MM-dd')}" size="10"
	                                                 datefmt="yyyy-MM-dd" class="date required hideError" readonly="true"
	                                                 id="startDate${item_index}"/></td>
	                    <td align="center"><input type="text" name="${name}.items[${item_index}].endDate"
	                                              value="${item.endDate.toString('yyyy-MM-dd')}" size="10" readonly="true"
	                                              datefmt="yyyy-MM-dd" class="date required hideError" minRelation="#startDate${item_index}"
	                                              customvalid="geDate(element,'#startDate${item_index}')"
	                                              title="结束日期不能小于开始日期。"/></td>
	                    <#list [1, 2, 3, 4, 5, 6, 7] as day>
	                    	<#if item.days?if_exists>
	                    		<#local isChecked = item.days?seq_contains(day)>
	                    			<td align="center"><input type="checkbox" name="${name}.items[${item_index}].days"
	                                                  value="${day}" class="required hideError"<#if isChecked>
	                                                  checked="checked"</#if> /></td>
	                    	<#else>
	                    		 <td align="center"><input type="checkbox" name="${name}.items[${item_index}].days"
	                                                  value="${day}" class="required hideError" /></td>
	                    	</#if>
	                    </#list>
	                    <td>
							<input class="Wdate required hideError" type="text" name="${name}.items[${item_index}].startTime" size="6"
							   onClick="WdatePicker({dateFmt:'HH:mm',isShowToday:false})" value="${item.startTime.toString('HH:mm')}" id="startTime${item_index}" customvalid="regexTime(element)" title='<@s.message "input.time.validate" />'/>
						</td>
						<td>
							<input class="Wdate required hideError" type="text" name="${name}.items[${item_index}].endTime" size="6"
							   onClick="WdatePicker({dateFmt:'HH:mm',isShowToday:false})" value="${item.endTime.toString('HH:mm')}" 
							   customvalid="geTime(element,'#startTime${item_index}')" title="<@s.message 'input.time.validate' />结束时间不能小于开始时间。"/>
						</td>
	                    <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
	                   </tr>
	               </#list>
               </#if>
            </tbody>
        </table>
    </dd>
</dl>
<script>
    $(function () {
        var periodRuleTypeRadios = $("#" + "${typeId}".escape());
        var periodRuleDls = $("dl[periodType]");
        periodRuleTypeRadios.each(function () {
            $(this).click(function () {
                var selectedPeriodType = $(this).val();
                periodRuleDls.each(function () {
                    if ($(this).attr("periodType") == selectedPeriodType) {
                        $(this).show();
                        $(this).find("input").addClass("required");
                        $(this).find("input").removeAttr("disabled");
                    } else {
                        $(this).hide();
                        $(this).find("input").removeClass("required");
                        $(this).find("input").attr("disabled", "disabled");
                    }
                });
            });
            if ($(this).attr("checked")) {
                $(this.click());
            }
        });
    });
</script>
</#macro>

<#-- 时段表头 -->
<#macro timeTh name>
<th type="text" name="${name}.items[#index#].startTime" width="50" align="center" size="13" defaultval="00:00"
    fieldClass="Wdate required hideError" fieldAttrs={onClick:"WdatePicker(\{dateFmt:'HH:mm',isShowToday:false\})",customvalid:'regexTime(element)',id:'startTime#index#',title:'<@s.message "input.time.validate" />'}>开始时间
</th>
<th type="text" name="${name}.items[#index#].endTime" width="50" align="center" size="13" defaultval="23:59"
    fieldClass="Wdate required hideError" 
    fieldAttrs={onClick:"WdatePicker(\{dateFmt:'HH:mm',isShowToday:false\})",customvalid:"geTime(element,'#startTime#index#')",title:"<@s.message 'input.time.validate' />结束时间不能小于开始时间。"}>结束时间
</th>
</#macro>

<#-- 时段输入框 -->
<#macro timeInput name item index>
<td>
<input class="Wdate required hideError" type="text" name="${name}.items[${index}].startTime" size="13"
	   onClick="WdatePicker({dateFmt:'HH:mm',isShowToday:false})" value="${item.startTime.toString('HH:mm')}" id="startTime${item_index}" customvalid="regexTime(element)" title='<@s.message "input.time.validate" />'/>
</td>
<td>
<input class="Wdate required hideError" type="text" name="${name}.items[${index}].endTime" size="13"
	   onClick="WdatePicker({dateFmt:'HH:mm',isShowToday:false})" value="${item.endTime.toString('HH:mm')}" 
	   customvalid="geTime(element,'#startTime${item_index}')" title="<@s.message 'input.time.validate' />结束时间不能小于开始时间。"/>
</td>
</#macro>

<#-- 结算规则组件 -->
<#macro settleRule path ruleType=ruleType>
    <@s.bind path />
    <#local name = s.name>
    <#local value = s.status.value>
    <#local type = value.type>
    <#local typeId = path + ".type">
    <#local basisTypeId = path + ".basisType">
    <#local minCinemaPrice = path + ".cinemaPriceBelowMinPrice">
    <#local minChannelPrice = path + ".channelPriceBelowMinPrice">
    <#local minSumbitPrice = path + ".sumbitPriceBelowMinPrice">
	<dl class="nowrap">
	    <dt>结算规则：</dt>
	    <dd><@s.radios path="${typeId}" id="${typeId}" items=circuit.settleRuleTypes itemLabel="text" itemValue="value" prefix="<label class='dd-span'>" suffix="</label>" /></dd>
	</dl>
    <dl class="nowrap" id="basisTypeDiv">
	    <dt>结算基准：</dt>
	    <dd><@s.radios path="${basisTypeId}" items=SettleBasisType?values itemLabel="text" itemValue="value" prefix="<label class='dd-span'>" suffix="</label>" /></dd>
	</dl>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.FIXED)>
    <dl settleType="${SettleRuleType.FIXED.value}" class="nowrap">
        <dt>${SettleRuleType.FIXED}：</dt>
        <dd>
            <input type="text" name="${name}.amount" value="${(value.amount?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
                   title="<@s.message 'input.price.validate' />"/>
            <span class="dd-span">&nbsp;元</span>
        </dd>
    </dl>
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.FIXED_PLUS)>
    <dl settleType="${SettleRuleType.FIXED_PLUS.value}" class="nowrap">
        <dt>${SettleRuleType.FIXED_PLUS}：</dt>
        <dd>
            <span class="dd-span">+&nbsp;</span>
            <input type="text" name="${name}.plus" value="${(value.plus?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
                   title="<@s.message 'input.price.validate' />"/>
            <span class="dd-span">&nbsp;元</span>
        </dd>
    </dl>
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.FIXED_MINUS)>
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
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.REBATE)>
    <dl settleType="${SettleRuleType.REBATE.value}" class="nowrap">
        <dt>${SettleRuleType.REBATE}：</dt>
        <dd>
            <input type="text" name="${name}.rebate" value="${(value.rebate?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="10"/>
            <span class="dd-span">&nbsp;折</span>
        </dd>
    </dl>
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.REBATE_PLUS)>
    <dl settleType="${SettleRuleType.REBATE_PLUS.value}" class="nowrap">
        <dt>${SettleRuleType.REBATE_PLUS}：</dt>
        <dd>
            <input type="text" name="${name}.rebate" value="${(value.rebate?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="10"/>
            <span class="dd-span">&nbsp;折&nbsp;+&nbsp;</span>
            <input type="text" name="${name}.plus" value="${(value.plus?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
                   title="<@s.message 'input.price.validate' />"/>
            <span class="dd-span">&nbsp;元</span>
        </dd>
    </dl>
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.REBATE_MINUS)>
    <dl settleType="${SettleRuleType.REBATE_MINUS.value}" class="nowrap">
        <dt>${SettleRuleType.REBATE_MINUS}：</dt>
        <dd>
            <input type="text" name="${name}.rebate" value="${(value.rebate?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="10"/>
            <span class="dd-span">&nbsp;折&nbsp;-&nbsp;</span>
            <input type="text" name="${name}.minus" value="${(value.minus?string('0.00'))!}"
                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
                   title="<@s.message 'input.price.validate' />"/>
            <span class="dd-span">&nbsp;元</span>
        </dd>
    </dl>
    </#if>
    <#if circuit.settleRuleTypes?seq_contains(SettleRuleType.ROUND)>
    <dl settleType="${SettleRuleType.ROUND.value}" class="nowrap">
        <dt>${SettleRuleType.ROUND}：</dt>
        <dd id="SettleRuleType_ROUND">
            <table class="list itemDetail" addButton="添加规则" width="100%">
                <thead>
                <tr>
                    <th type="text" name="${name}.items[#index#].startAmount" align="center" size="10"
                        fieldClass="number required hideError"
                        fieldAttrs="{min:0,max:1000,customvalid:'regexPrice(element)',title:'<@s.message "input.price.validate" />'}">
                        起始金额
                    </th>
                    <th type="text" name="${name}.items[#index#].endAmount" align="center" size="10"
                        fieldClass="number required hideError"
                        fieldAttrs="{min:0,max:1000,customvalid:'regexPrice(element)',title:'<@s.message "input.price.validate" />'}">
                        截止金额
                    </th>
                    <th type="enum" name="${name}.items[#index#].settleRule.type" align="center" size="18" width="100" enumUrl="/price/settleRule-select.ftl?name=${name}">
                        结算规则
                    </th>
                    <th type="text" name="${name}.items[#index#].settleRule.amount" value="${(value.amount?string('0.00'))!}" align="center" width="200">
                        定价金额
                    </th>
                    <th type="del" width="60" align="center">操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if type == SettleRuleType.ROUND>
                        <#list value.items as item>
                        <#local settleType =item.settleRule.type.value />
                        <tr class="unitBox">
                            <td align="center"><input type="text" name="${name}.items[${item_index}].startAmount"
                                                      value="${(item.startAmount?string('0.00'))!}" size="10"
                                                      class="number required hideError" min="0" max="1000"
                                                      customvalid="regexPrice(element)"
                                                      title="<@s.message 'input.price.validate' />"/></td>
                            <td align="center"><input type="text" name="${name}.items[${item_index}].endAmount"
                                                      value="${(item.endAmount?string('0.00'))!}" size="10"
                                                      class="number required hideError" min="0" max="1000"
                                                      customvalid="regexPrice(element)"
                                                      title="<@s.message 'input.price.validate' />"/></td>
                           <td align="center">
	                           	<select class="combox" name="${name}.items[${item_index}].settleRule.type">
									<#list settleRuleTypes as settleRule>
										<option value="${settleRule.value}" <#if settleType ==settleRule.value>selected="select"</#if>>${settleRule.text}</option>
									</#list>
								</select>
						   </td>
                           <td align="center">
                            <#if settleType == SettleRuleType.FIXED.value>
					            <input type="text" name="${name}.items[${item_index}].settleRule.amount" value="${(item.settleRule.amount?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
					                   title="<@s.message 'input.price.validate' />"/>
					            <span class="dd-span">&nbsp;元</span>
						    </#if>
						    <#if settleType == SettleRuleType.FIXED_PLUS.value>
					            <span class="dd-span">+&nbsp;</span>
					            <input type="text" name="${name}.items[${item_index}].settleRule.plus" value="${(item.settleRule.plus?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
					                   title="<@s.message 'input.price.validate' />"/>
					            <span class="dd-span">&nbsp;元</span>
						    </#if>
						    <#if settleType == SettleRuleType.FIXED_MINUS.value>
					            <span class="dd-span">-&nbsp;</span>
					            <input type="text" name="${name}.items[${item_index}].settleRule.minus" value="${(item.settleRule.minus?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
					                   title="<@s.message 'input.price.validate' />"/>
					            <span class="dd-span">&nbsp;元</span>
						    </#if>
						    <#if settleType == SettleRuleType.REBATE.value>
					            <input type="text" name="${name}.items[${item_index}].settleRule.rebate" value="${(item.settleRule.rebate?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="10"/>
					            <span class="dd-span">&nbsp;折</span>
						    </#if>
						    <#if settleType == SettleRuleType.REBATE_PLUS.value>
					            <input type="text" name="${name}.items[${item_index}].settleRule.rebate" value="${(item.settleRule.rebate?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="10"/>
					            <span class="dd-span">&nbsp;折&nbsp;+&nbsp;</span>
					            <input type="text" name="${name}.items[${item_index}].settleRule.plus" value="${(item.settleRule.plus?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
					                   title="<@s.message 'input.price.validate' />"/>
					            <span class="dd-span">&nbsp;元</span>
						    </#if>
						    <#if settleType == SettleRuleType.REBATE_MINUS.value>
					            <input type="text" name="${name}.items[${item_index}].settleRule.rebate" value="${(item.settleRule.rebate?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="10"/>
					            <span class="dd-span">&nbsp;折&nbsp;-&nbsp;</span>
					            <input type="text" name="${name}.items[${item_index}].settleRule.minus" value="${(item.settleRule.minus?string('0.00'))!}"
					                   class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)"
					                   title="<@s.message 'input.price.validate' />"/>
					            <span class="dd-span">&nbsp;元</span>
						    </#if>
                           </td>
                           <td align="center"><a class="btnDel" href="javascript:void(0)" style="float:none;">删除</a></td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </dd>
    </dl>
    </#if>
    <#if ruleType == "cinemaRule">
	<dl class="nowrap">
    	<table border="0" cellspacing="0" cellpadding="0" style="border:0;margin-left:5px" >
    		<tr height="30" >
    			<td width="150">影院结算价可低于最低价：</td>
    			<td><@s.radio path="${minCinemaPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    	</table>
	</dl>
    </#if>
    <#if ruleType == "channelRule">
    <dl class="nowrap">
    	<table border="0" cellspacing="0" cellpadding="0" style="border:0;margin-left:5px" >
    		<tr height="30" >
    			<td width="150">渠道结算价可低于最低价：</td>
    			<td><@s.radio path="${minChannelPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    		<tr height="30">
    			<td width="150">票房价可低于最低价：</td>
    			<td><@s.radio path="${minSumbitPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    	</table>
	</dl>
    </#if>
    <#if ruleType == "specialRule">
    <dl class="nowrap">
    	<table border="0" cellspacing="0" cellpadding="0" style="border:0;margin-left:5px" >
    		<tr height="30" >
    			<td width="150">影院结算价可低于最低价：</td>
    			<td><@s.radio path="${minCinemaPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    		<tr height="30" >
    			<td width="150">渠道结算价可低于最低价：</td>
    			<td><@s.radio path="${minChannelPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    		<tr height="30">
    			<td width="150">票房价可低于最低价：</td>
    			<td><@s.radio path="${minSumbitPrice}" prefix="<label class='dd-span'>" suffix="</label>" /></td>
    		</tr>
    	</table>
	</dl>
    </#if>

<script>
    $(function () {
        var settleRuleTypeRadios = $("#" + "${typeId}".escape());
        var settleRuleDls = $("dl[settleType]");
        settleRuleDls.each(function () {
            $(this).hide();
        });
        settleRuleTypeRadios.each(function () {
            $(this).click(function () {
                var selectedSettleType = $(this).val();
                if(selectedSettleType == "${SettleRuleType.FIXED.value}"){
                	$("#basisTypeDiv",".page").hide();
                }else{
                	$("#basisTypeDiv",".page").show();
                }
                settleRuleDls.each(function () {
                    if ($(this).attr("settleType") == selectedSettleType) {
                        $(this).show();
                        $(this).find("input").addClass("required");
                        $(this).find("input").removeAttr("disabled");
                    } else {
                        $(this).hide();
                        $(this).find("input").removeClass("required");
                        $(this).find("input").attr("disabled", "disabled");
                    }
                });
            	if("${SettleRuleType.ROUND.value}" == selectedSettleType){
            		initSettleRule();
                } 
            });
            if ($(this).attr("checked")) {
                $(this.click());
            }
        });
        initSettleRule();
    });
    $("#SettleRuleType_ROUND",".page").find("select").live("change",function(){
    	var td =$(this).closest("td").next();
    	var name = $(this).attr("name");
		var index = name.substring(name.indexOf("[")+1,name.indexOf("]"));
    	td.html('');
   		td.html(getHtml($(this).val(),index));
    });
    function initSettleRule(){
    	$("#SettleRuleType_ROUND",".page").find("button").live("click",function(){
    		var money = $("#SettleRuleType_ROUND",".page").find("tbody").find("tr:last").find("td:eq(3)");
    		var name = money.find("input:eq(0)").attr("name");
    		var index = name.substring(name.indexOf("[")+1,name.indexOf("]"));
    		money.html('');
    		money.html(getHtml($("#SettleRuleType_ROUND",".page").find("tbody").find("tr:last").find("a").attr("value"),index));
    	})
    }
    function getHtml(selectValue,index){
    	if("${SettleRuleType.FIXED.value}" == selectValue){
    		return '<label style="width:60px"><input align="left" type="text" name="${name}.items['+ index +'].settleRule.amount" value="${(value.amount?string('0.00'))!}" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/></label><span class="dd-span">&nbsp;元</span>';
    	}else if("${SettleRuleType.FIXED_PLUS.value}" == selectValue){
    		return '<span class="dd-span">+&nbsp;</span><label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.plus" value="${(value.plus?string('0.00'))!}" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/></label><span class="dd-span">&nbsp;元</span>';
    	}else if("${SettleRuleType.FIXED_MINUS.value}" == selectValue){
    		return '<span class="dd-span">-&nbsp;</span><label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.minus" value="${(value.minus?string('0.00'))!}" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/></label><span class="dd-span">&nbsp;元</span>';
    	}else if("${SettleRuleType.REBATE.value}" == selectValue){
    		return '<label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.rebate" value="${(value.rebate?string('0.00'))!}" class="required number s-input hideError" min="0" max="10"/></label><span class="dd-span">&nbsp;折</span>';
    	}else if("${SettleRuleType.REBATE_PLUS.value}" == selectValue){
    		return '<label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.rebate" value="${(value.rebate?string('0.00'))!}" class="required number s-input hideError" min="0" max="10"/></label><span class="dd-span">&nbsp;折&nbsp;+&nbsp;</span><label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.plus" value="${(value.minus?string('0.00'))!}" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/></label><span class="dd-span">&nbsp;元</span>';
    	}else{
    		return '<label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.rebate" value="${(value.rebate?string('0.00'))!}" class="required number s-input hideError" min="0" max="10"/></label><span class="dd-span">&nbsp;折&nbsp;-&nbsp;</span><label style="width:60px"><input type="text" name="${name}.items['+ index +'].settleRule.minus" value="${(value.minus?string('0.00'))!}" class="required number s-input hideError" min="0" max="1000" customvalid="regexPrice(element)" title="<@s.message 'input.price.validate' />"/></label><span class="dd-span">&nbsp;元</span>';
    	}
    }
</script>
</#macro>

<#-- 上传单张图片 -->
<#macro singleImageUpload path showUpload=true fileSizeLimit=1024 url=url mark=mark>
    <@s.bind path />
    <#local randomNum =.now?datetime?string("yyyyMMddHHmmssSSS")>
    <#if showUpload>
    <input id="FileInput${randomNum}${mark}" type="file" name="${s.name}"
           uploaderOption="{
             swf:'dwz/uploadify/scripts/uploadify.swf',
             uploader:'assist/file-upload',    
             buttonText:'请选择文件',
             removeCompleted:false,
             fileObjName:'file',
             formData:{'folder':'/upload/images'},
             fileSizeLimit:'${fileSizeLimit}KB',
             fileTypeDesc:'*.jpg;*.jpeg;*.gif;*.png;',
             fileTypeExts:'*.jpg;*.jpeg;*.gif;*.png;',
             auto:true,
             multi:true,
             itemTemplate:'<div id=$\{fileID\} class=uploadify-queue-item style=\'width:210px;float:left;\'><div class=cancel><a href=javascript:$(\'#$\{instanceID\}\').uploadify(\'cancel\',\'$\{fileID\}\');>X</a></div><span class=showImage_$\{fileID\}></span><span class=fileName>$\{fileName\}</span><span class=data></span><div class=uploadify-progress><div class=uploadify-progress-bar></div></div></div>',
             queueID:'${randomNum}',
             onUploadStart:function(file){
             	/* 屏蔽覆盖文件提示问题 */
             	this.queueData.files[file.id].uploaded = true;
             	if($('div#${randomNum}').find('.uploadify-queue-item').size() > 1) {
    				this.cancelUpload(file.id, false); 
      				$('#' + file.id).remove();
      				alertMsg.error('文件超过有效数量（1）');
      			}
			 },
			 onSelectError : function(file, errorCode, errorMsg) {
			 	switch(errorCode) {
			 		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			 		this.queueData.errorMsg = '任务数量超出队列限制';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			 		this.queueData.errorMsg = '文件大小超出限制(' + this.settings.fileSizeLimit + ')';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			 		this.queueData.errorMsg = '文件大小为0';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			 		this.queueData.errorMsg = '文件类型仅支持(' + this.settings.fileTypeDesc + ')';
			 		break;
			 	}
			 },
             onUploadSuccess:function(file,data,response) {
             	var json = DWZ.jsonEval(data);
             	$('.showImage_' + file.id).append('<img src='+json.path+' width=205 height=150/>');
                $('.showImage_' + file.id).append('<input type=hidden name=${s.name}.id id=${s.name}.id value='+json.id+' />');
             }
         }"
            />
    </#if>
<div id="${randomNum}" class="fileQueue">
    <#if s.status.value??>
        <div id="SWFUpload_${s.name}_0" class="uploadify-queue-item" style="width:210px;float:left;">
            <#if showUpload>
                <div class="cancel"><a
                        href="javascript:$('#FileInput${randomNum}${mark}').uploadify('cancel','SWFUpload_${s.name}_0');">X</a>
                </div>
            </#if>
            <span class="showImage_SWFUpload_${s.name}_0"><img src="${url}${s.status.actualValue.path}" width="205"
                                                               height="150/"><input type="hidden" name="${s.name}.id"
                                                                                    id="${s.name}.id"
                                                                                    value="${s.status.actualValue.id}"></span>
        </div>
    </#if>
</div>
</#macro>

<#-- 上传多张图片 -->
<#macro multiImagesUpload path showUpload=true uploadLimit=0 fileSizeLimit=1024>
    <@s.bind path />
    <#local randomNum =.now?datetime?string("yyyyMMddHHmmssSSS")>
    <#if showUpload>
    <input id="FileInput${randomNum}" type="file" name="${s.name}"
           uploaderOption="{
             swf:'dwz/uploadify/scripts/uploadify.swf',
             uploader:'assist/file-upload',    
             buttonText:'请选择文件',
             removeCompleted:false,
             fileObjName:'file',
             formData:{'folder':'/upload/images'},
             fileSizeLimit:'${fileSizeLimit}KB',
             fileTypeDesc:'*.jpg;*.jpeg;*.gif;*.png;',
             fileTypeExts:'*.jpg;*.jpeg;*.gif;*.png;',
             auto:true,
             multi:true,
             itemTemplate:'<div id=$\{fileID\} class=uploadify-queue-item style=\'width:210px;float:left;\'><div class=cancel><a href=javascript:$(\'#$\{instanceID\}\').uploadify(\'cancel\',\'$\{fileID\}\');uploadResetImagesSort(\'$\{fileID\}\',\'${s.name}\');>X</a></div><span class=showImage_$\{fileID\}></span><span class=fileName>$\{fileName\}</span><span class=data></span><div class=uploadify-progress><div class=uploadify-progress-bar></div></div></div>',
             queueID:'${randomNum}',
             onUploadStart:function(file){
             	/* 屏蔽覆盖文件提示问题 */
             	this.queueData.files[file.id].uploaded = true;
             	if(${uploadLimit} != 0 && $('div#${randomNum}').find('.uploadify-queue-item').size() > ${uploadLimit}) {
    				this.cancelUpload(file.id, false); 
      				$('#' + file.id).remove();
      				alertMsg.error('文件超过有效数量（${uploadLimit}）');
      			}
			 },
			 onSelectError : function(file, errorCode, errorMsg) {
			 	switch(errorCode) {
			 		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			 		this.queueData.errorMsg = '任务数量超出队列限制';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			 		this.queueData.errorMsg = '文件大小超出限制(' + this.settings.fileSizeLimit + ')';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			 		this.queueData.errorMsg = '文件大小为0';
			 		break;
			 		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			 		this.queueData.errorMsg = '文件类型仅支持(' + this.settings.fileTypeDesc + ')';
			 		break;
			 	}
			 },
             onUploadSuccess:function(file,data,response) {
             	var json = DWZ.jsonEval(data);
             	$('.showImage_' + file.id).append('<img src='+json.path+' width=205 height=150/>');
                $('.showImage_' + file.id).append('<input type=hidden name=${s.name}[0].id id=${s.name}[0].id value='+json.id+' />');
                resetImagesSort(-1,'${s.name}');
             }
         }"
            />
    </#if>
<div id="${randomNum}" class="fileQueue">
    <#list s.status.actualValue as attachment>
        <div id="SWFUpload_${s.name}_${attachment_index}" class="uploadify-queue-item" style="width:210px;float:left;">
            <#if showUpload>
                <div class="cancel"><a
                        href="javascript:$('#FileInput${randomNum}').uploadify('cancel','SWFUpload_${s.name}_${attachment_index}');uploadResetImagesSort('SWFUpload_${s.name}_${attachment_index}','${s.name}');">X</a>
                </div>
            </#if>
            <span class="showImage_SWFUpload_${s.name}_${attachment_index}"><img src="${attachment.path}" width="205"
                                                                                 height="150/"><input type="hidden"
                                                                                                      name="${s.name}[${attachment_index}].id"
                                                                                                      id="${s.name}[${attachment_index}].id"
                                                                                                      value="${attachment.id}"></span>
        </div>
    </#list>
</div>
<script>
    /* 删除时，list会出现断续问题，所以需要重置list的index。*/
    resetImagesSort = function (selectIndex, fieldName) {
        var index = 0;
        $('#' + fieldName + ' .uploadify-queue-item').each(function () {
            if ($(this).attr("id") != selectIndex) {
                $(this).find('input').each(function () {
                    $(this).attr("name", fieldName + "[" + index + "].id");
                    $(this).attr("id", fieldName + "[" + index + "].id");
                });
                index++;
            }
        });
    }
</script>
</#macro>

<!-- 选择用户 -->
<#macro userSelect path>
    <@s.hidden path=path />
    <#local userNames>
        <@compress single_line=true>
            <#list s.status.actualValue as user>
            ${user.name}<#if user_has_next>,</#if>
            </#list>
        </@compress>
    </#local>
<textarea name="${s.name}Names" rows="3" cols="40" readonly>${userNames}</textarea>
    <#if s.status.actualValue?size gt 0>
        <#local selectedUserIds = "&selectedUserIds={${s.name}}" >
    </#if>
<a class="btnEdit" width="700" height="500" id="${s.name}"
   href="<@s.url '/assist/user-select' />?inputName=${s.name}${selectedUserIds}" lookupgroup="" target="dialog"
   minable='false' maxable='false' resizable='false' drawable='true' title="选择用户">选择用户</a>
<a class="btnDel" onclick="cleanSelectedUsers('${s.name}')" style="cursor:pointer;" title="清空用户">清空用户</a>
<script>
    var cleanSelectedUsers = function (name) {
        $("input#" + name).removeAttr("value");
        $("textarea[name=" + name + "Names]").val("");
        $("a#" + name).attr("href", "<@s.url '/assist/user-select' />?inputName=" + name);
    }
</script>
</#macro>