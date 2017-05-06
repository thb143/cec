var TimeStint = {
	existArray : new Array(),
	delExistArray : function(text) {
		var array = new Array();
		$.each(this.existArray, function(index, value) {
			if (text != value) {
				array.push(value);
			}
		});
		TimeStint.existArray = array;
	},
	createUUID : function() {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
				function(c) {
					var r = Math.random() * 16 | 0, v = c === 'x' ? r
							: (r & 0x3 | 0x8);
					return v.toString(16);
				});
	},
	goldTimeModel : function(weeks) {
		var uuid = this.createUUID();
		return "<div id='" + uuid + "' class=\"uploadify-queue-item\">"
				+ "<div class=\"cancel\">" + "<a href=\"javascript:$('#" + uuid
				+ "').remove();TimeStint.delExistArray('" + weeks
				+ "')\">X</a>" + "</div>" + "<span>" + weeks + "</span>"
				+ "</div>";
	},
	goldTimeVerify : function() {
		if ($('#goldEndTime').val() == '' || $('#goldStartTime').val() == '') {
			$('#goldTimeError').html('开始时间和结束时间不能为空');
			return "";
		}
		var weeks = new Array();
		if ($("input[name='gold']:checked").val() == 'weeks') {
			$("input[name='week']:checked").each(function() {
				weeks.push($(this).val());
			});
		} else {
			weeks.push('0');// 0表示每天
		}
		if (weeks.length == 0) {
			$('#goldTimeError').html('星期限制不能为空');
			return "";
		}
		var saveText = weeks.join('、') + "  " + $('#goldStartTime').val() + "~"
				+ $('#goldEndTime').val();
		if (this.exist(saveText) != '') {
			$('#goldTimeError').html('已经存在该黄金时段');
			return "";
		}
		this.existArray.push(saveText);
		return saveText;
	},
	exist : function(text) {
		var result = $.grep(this.existArray, function(a) {
			return text == a;
		});
		return result;
	},
	addGoldTime : function() {
		var saveText = this.goldTimeVerify();
		if (saveText != '') {
			$('#goldTimeError').html('');
			$("#goldQueue").append(this.goldTimeModel(saveText));
		}
	},
	addSpecialTime : function() {	
		if ($("#specialDay").val() == '') {
			$('#specialTimeError').html('请选择特殊节假日期');
			return;
		}
		if ($('#specialDayStartTime').val() == ''
				|| $('#specialDayEndTime').val() == '') {
			$('#specialTimeError').html('开始时间和结束时间不能为空');
			return "";
		}
		if (this.exist($('#specialDayStartTime').val() + "~"
				+ $('#specialDayEndTime').val()) != '') {
			$('#specialTimeError').html('已经存在该时间段');
			return "";
		}
		this.existArray.push($('#specialDayStartTime').val() + "~"
				+ $('#specialDayEndTime').val());
		$('#specialTimeError').html('');
		$("#specialTimeQueue").append(
				this.goldTimeModel($('#specialDayStartTime').val() + "~"
						+ $('#specialDayEndTime').val()));
	},
	addSpecialDay : function() {
		if ($('#specialDay').val() == '') {
			$('#specialDayError').html('日期不能为空');
			return;
		}
		if (this.exist($('#specialDay').val()) != '') {
			$('#specialDayError').html('已经存在特殊节假日');
			return;
		}
		this.existArray.push($('#specialDay').val());
		$('#specialDayError').html('');
		$("#specialDayQueue")
				.append(this.goldTimeModel($('#specialDay').val()));
	},
	clearIt : function(type) {
		TimeStint.existArray = new Array();
		if (type == 'allTime') {
			$('#specialDay').val('');
			$('#specialDayStartTime').val('');
			$('#specialDayEndTime').val('');
			$('#specialTimeQueue').html('');
			$('#specialDayQueue').html('');
			$('#goldStartTime').val('');
			$('#goldEndTime').val('');
			$('#goldQueue').html('');
			$("input[name='week']").attr("checked", false);
			$('#goldTime').css('display','none');
			$('#holidays').css('display','none');
			return;
		}
		if (type == 'goldTime') {
			$('#specialDay').val('');
			$('#specialDayStartTime').val('');
			$('#specialDayEndTime').val('');
			$('#specialTimeQueue').html('');
			$('#specialDayQueue').html('');
			$('#goldTime').css('display','block');
			$('#holidays').css('display','none');
			return;
		}
		if (type == 'specialDay') {
			$('#goldStartTime').val('');
			$('#goldEndTime').val('');
			$('#goldQueue').html('');
			$("input[name='week']").attr("checked", false);
			$('#holidays').css('display','block');
			$('#goldTime').css('display','none');
			return;
		}
		if(type == 'allDay') {
			$("input[name='week']").attr("checked", false);
			$('#goldStartTime').val('');
			$('#goldEndTime').val('');
			$('#goldQueue').html('');
			return;
		}
		if(type == 'weeks') {
			$('#goldStartTime').val('');
			$('#goldEndTime').val('');
			$('#goldQueue').html('');
			return;
		}
	}
};
var settlement = {
		existArray : new Array(), 
		model : function(bigPrice,smallPrice,intervalPrice,id) {
	        return "<div id='"+id+"' style=\"float: left;margin-top: 5px;width: 600px;margin-left: 20px;\"><ul><li style=\"float: left;margin-top:5px;\">大于等于"+bigPrice+"元小于"+smallPrice+"元时按"+intervalPrice+"元结算&nbsp;</li><li style=\"float: left;\"><a class=\"button\" href=\"javascript:void(0);\" onclick=\"javascript:settlement.delDiv('"+id+"');\"><span>删除</span></a></li></ul></div><div style=\"clear:both;\" />";           
		},
		interval : function() {     
	        if(!$.isNumeric($('#bigPrice').val()) || !$.isNumeric($('#smallPrice').val()) || !$.isNumeric($('#intervalPrice').val())) {
	            $('#intervalPriceError').html('&nbsp;数据不完整或数据格式不对&nbsp;');
	            $('#intervalPriceError').css('display','block');
	            return;
	        }
	        var $splice = $('#bigPrice').val() + "-" + $('#smallPrice').val() + "-" + $('#intervalPrice').val();
	        if (this.exist($splice) != '') {
	            $('#intervalPriceError').html('&nbsp;已经存在该区间定价&nbsp;');
	            $('#intervalPriceError').css('display','block');
	            return;
	        }
	        var result = this.model($('#bigPrice').val(),$('#smallPrice').val(),$('#intervalPrice').val(),$splice);
	        this.existArray.push($splice);
	        $('#intervalResult').append(result);      
	        $('#intervalPriceError').css('display','none');
		},
		delDiv : function(id) {			
			this.delArray(id);
	        $('#' + id).remove();
		},
		delArray : function(text) {
			var array = new Array();
	        $.each(this.existArray, function(index, value) {
	            if (text != value) {
	                array.push(value);
	            }
	        });
	        this.existArray = array;
		},
		exist : function(text) {
			var result = $.grep(this.existArray, function(a) {
	            return text == a;
	        });
	        return result;
		},
		selectIt : function(no) {
			this.clearIts(no);
	        $("input[name=account]")[no].checked = true;
		},
		clearIts : function(no) {
			this.existArray = new Array();
	        if(no != 0) {
	            $('#fixedPrice').val('');
	        }
	        if(no != 1) {
	        	$('#fixedPriceAdd').val('');       
	        }
	        if(no != 2) {
	            $('#fixedPriceCut').val('');
	        }
	        if(no != 3) {
	        	$('#discountPrice').val('');
	        }
	        if(no != 4) {
	        	$('#discountAdd').val('');
	            $('#discountAddPrice').val('');
	        }
	        if(no != 5) {
	        	$('#discountCut').val('');
	            $('#discountCutPrice').val('');
	        }
	        if(no != 6) {
	        	$("table[class='list itemDetail'] tbody").html('');
	        }
		}
};