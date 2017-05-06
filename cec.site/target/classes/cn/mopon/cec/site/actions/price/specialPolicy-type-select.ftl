<div class="page">
    <div class="pageContent">
        <div class="pageFormContent s-cols" layoutH="60">
            <dl class="nowrap">
                <dd>
                   <label><input type="radio" name="specialPolicyType" value="price/specialPolicy-add?type=${SpecialPolicyType.SELL.value}" onclick="$('#addSpecialPolicy').attr('href',this.value)" checked />${SpecialPolicyType.SELL}</label>
                </dd>
                <dd>
                   <label><input type="radio" name="specialPolicyType" value="price/specialPolicy-add?type=${SpecialPolicyType.FILM.value}" onclick="$('#addSpecialPolicy').attr('href',this.value)"/>${SpecialPolicyType.FILM}</label>
                </dd>
            </dl>
        </div>
        <@dwz.formBar showSubmitBtn=false>
        	<li>
                <@dwz.a class="button" id="addSpecialPolicy" href="price/specialPolicy-add?type=${SpecialPolicyType.SELL.value}" target="dialog" title="新增特殊定价策略" close="$.pdialog.closeCurrent();"><span>确定</span></@dwz.a>
            </li>
        </@dwz.formBar>
    </div>
</div>