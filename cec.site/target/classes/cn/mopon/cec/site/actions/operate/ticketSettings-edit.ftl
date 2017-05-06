<div class="page">
    <div class="pageContent" id="ticketSettingsFrm">
        <@dwz.form action="/operate/ticketSettings-update">
	       	<@s.hidden path="ticketSettings.id" />
	        <div class="pageFormContent" layoutH="55">
	        	<fieldset>
		            <legend><label><@s.checkbox path="ticketSettings.cinema.ticketSetted" class="setCheckbox" />选座票供应设置</label></legend>
	        		<div style="width:800px;" id="ticketSetting">
		                <div>
		                    <dl class="nowrap">
								<dt>放映类型：</dt>
								<dd>
		                    		<@dwz.checkboxs path="ticketSettings.showTypes" items=ShowType?values itemValue="value" itemLabel="text" />
								</dd>
							</dl>
							<dl class="nowrap">
		                        <dt>接入类型：</dt>
		                        <dd>
		                        	<select name="accessType" class="required">
		                        		<option value="">未设置</option>
		                        		<#list ticketAccessTypes as accessType>
		                        			<option value="${accessType.id}" type="${accessType.model.value}"
		                        				<#if ticketSettings.accessType.id == accessType.id >selected = selected</#if>>
		                        				${accessType.name}
		                        			</option>
		                        		</#list>
		                        	</select>
		                        	<span id ="tip" class="error required" title="必填字段。" style="display:none"></span>
		                        </dd>
		                    </dl>
	                        <dl class="nowrap">
		                        <dt>取票方式：</dt>
		                        <dd>
		                            <@s.radios path="ticketSettings.printMode" items=PrintMode?values itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"  class="required"/>
		                        </dd>
		                    </dl>
		                    <dl>
	                            <dt>同步排期天数：</dt>
	                            <dd>
	                                <@s.input path="ticketSettings.syncShowDays" min="1" max="30" class="required digits"/>
	                            </dd>
	                        </dl>
	                        <dl>
	                            <dt>保留排期天数：</dt>
	                            <dd>
	                                <@s.input path="ticketSettings.keepShowDays" min="1" max="30" class="required digits"/>
	                            </dd>
	                        </dl>
	                        <dl>
	                            <dt>生成凭证长度：</dt>
	                            <dd>
	                            	<@s.input path="ticketSettings.voucherCodeLength" min="6" max="10" class="required digits" />
	                            </dd>
	                        </dl>
	                        <dl>
	                            <dt>接口日志长度：</dt>
	                            <dd>
	                            	<@s.input path="ticketSettings.logLength" min="0" max="5000" class="required digits" />
	                            </dd>
	                        </dl>
	                        <dl class="nowrap">
	                            <dt>同步排期间隔：</dt>
	                            <dd>
	                            	<@s.input path="ticketSettings.syncShowInterval" range="0,1000" class="required digits" /><span class="dd-span">分钟</span>
	                            </dd>
	                        </dl>
	                        <div id="ticketParam">
		                        <dl class="nowrap">
			                        <dt>接入地址：</dt>
			                        <dd><@s.input path="ticketSettings.url" class="required url l-input" maxlength="800" /></dd>
			                    </dl>
			                    <dl>
			                        <dt>用户名：</dt>
			                        <dd><@s.input path="ticketSettings.username" maxlength="20" class="required" /></dd>
			                    </dl>
			                    <dl>
			                        <dt>密码：</dt>
			                        <dd><@s.input path="ticketSettings.password" maxlength="60" class="required" /></dd>
			                    </dl>
			                </div>
			                <dl class="nowrap">
						        <dt>参数配置：</dt>
						        <dd><@s.textarea path="ticketSettings.params" rows="5" maxlength="120" class="l-input" customvalid="regexParams(element)" title="参数=参数值形式，参数不能相同，多个请回车" ></@s.textarea></dd>
						    </dl>
						</div>
					</div>
		        </fieldset>
	        </div>
	        <@sec.any name="CINEMA_SET">
	        	<@dwz.formBar showCancelBtn=false/>
	        </@sec.any>
        </@dwz.form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
  	var model,accessModel;
	$(function(){
		model = $("select[name='accessType']","#ticketSetting");
		accessModel = model.find("option:selected").attr("type");
		control(accessModel);
		model.change(function(){
			accessModel = model.find("option:selected").attr("type");
		control(accessModel);
		});
        $(".setCheckbox").each(function(){
        	var setFieldset = $(this).closest("fieldset");
        	var inputs = setFieldset.find("div input,select,textarea")
            $(this).click(function() {
            	if($(this).is(":checked")) {
                    inputs.removeAttr("disabled");
                } else {
                	inputs.attr("disabled","disabled");
                }
            });
            if($(this).is(":checked")) {
                inputs.removeAttr("disabled");
            } else {
            	inputs.attr("disabled","disabled");
            }
        });
    });
	
	function control(accessModel){
		if(accessModel == "${AccessModel.CENTER.value}"){
			 $("#ticketParam","#ticketSetting").hide();
			 $("input[name='url']","#ticketSetting").removeClass("required").removeClass("url");
			 $("input[name='username']","#ticketSetting").removeClass("required");
			 $("input[name='password']","#ticketSetting").removeClass("required");
		}else{
			 $("#ticketParam","#ticketSetting").show();
			 $("input[name='url']","#ticketSetting").addClass("required").addClass("url");
			 $("input[name='username']","#ticketSetting").addClass("required");
			 $("input[name='password']","#ticketSetting").addClass("required");
		}	
	}
	
	$("#ticketSettingsFrm").find("button").click(function(){
		model = $("select[name='accessType']","#ticketSetting");
		if(model.html() == ''){
			$("#tip").css("display","block");
			return false;
		}
		$("#ticketSettingsFrm").submit();
	});
</script>

