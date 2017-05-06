<div class="page">
	<div class="pageContent" id="memberSettingsFrm">
		<@dwz.form action="/operate/memberSettings-update">
			<@s.hidden path="memberSettings.id" />
			<div class="pageFormContent" layoutH="55" id="memberSetting">
				<fieldset>
					<legend><label><@s.checkbox path="memberSettings.cinema.memberSetted" class="setCheckbox" />会员卡设置</label></legend>
					<div style="width:800px;">
						<div>
							<dl>
								<dt>接入类型：</dt>
								<dd>
			                        <select name="accessType" class="required">
				                        <#list memberAccessTypes as accessType>
				                       	 	<option value="${accessType.id}" type="${accessType.model.value}"
					                       	 	 <#if memberSettings.accessType.id == accessType.id >selected = selected</#if>>
					                       	 	${accessType.name}
				                       	 	</option>
				                       	</#list>
			                        </select>
				                    <span id ="tip" class="error required" title="必填字段。" style="display:none"></span>
		                        </dd>
		                    </dl>
		                   	<dl>
	                            <dt>接口日志长度：</dt>
	                            <dd>
	                            	<@s.input path="memberSettings.logLength" min="0" max="5000" class="required digits" />
	                            </dd>
	                        </dl>
		                    <div id="memberParam">
			                    <dl class="nowrap">
			                        <dt>接入地址：</dt>
			                        <dd><@s.input path="memberSettings.url" class="required url l-input" maxlength="800" /></dd>
			                    </dl>
			                    <dl>
			                        <dt>用户名：</dt>
			                        <dd><@s.input path="memberSettings.username" maxlength="20" class="required" /></dd>
			                    </dl>
			                    <dl>
			                        <dt>密码：</dt>
			                        <dd><@s.input path="memberSettings.password" maxlength="60" class="required" /></dd>
			                    </dl>
		                    </div>
		                    <dl class="nowrap">
						        <dt>参数配置：</dt>
						        <dd><@s.textarea path="memberSettings.params" rows="5" maxlength="120" class="l-input" customvalid="regexParams(element)" title="参数=参数值形式，参数不能相同，多个请回车" ></@s.textarea></dd>
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
		model = $("select[name='accessType']","#memberSetting");
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
			 $("#memberParam","#memberSetting").hide();
			 $("input[name='url']","#memberSetting").removeClass("required").removeClass("url");
			 $("input[name='username']","#memberSetting").removeClass("required");
			 $("input[name='password']","#memberSetting").removeClass("required");
		}else{
			 $("#memberParam","#memberSetting").show();
			 $("input[name='url']","#memberSetting").addClass("required").addClass("url");
			 $("input[name='username']","#memberSetting").addClass("required");
			 $("input[name='password']","#memberSetting").addClass("required");
		}	
	}
	
	$("#memberSettingsFrm").find("button").click(function(){
		model = $("select[name='accessType']","#memberSetting");
		if(model.html() == ''){
			$("#tip").css("display","block");
			return false;
		}
		$("#memberSettingsFrm").submit();
	});
</script>

