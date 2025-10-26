var navPath = '';
//ajax전송
$.fn.ajaxIgp = function(options){
	var isErr = false;
	var frm = $(this);
	var funcErr, funcBef, funcComp, obj, err, valid;
	
	if(options){
		funcErr = options.error;
		funcBef = options.beforeSubmit;
		funcComp = options.complete;
		valid = (options.beforeValid != undefined?options.beforeValid:true);
	}
	
	var baseOpt = {
		error:function(res, status, ex){
			//상태값체크
			if(res.status == 440){
				$('[data-target="#loginPop"]').click();
				
			}else if(res.status == 400){
				//validation 처리
				if(res.responseJSON){
					$(res.responseJSON.errors).each(function(){
						obj = $('[name="'+this.field+'"]');
						alert(this.defaultMessage);
						$(obj).focus();
						return false;
					});
				}
				
			}else{
				console.log(res);
				console.log(status);
				console.log(ex);
				alert('오류가 발생하였습니다.');
			}
			
			if(funcErr)
				funcErr(res, status, ex);
		},complete:function(res, status){
			if(funcComp)
				funcComp(res, status);
		}
	};
	
	if(valid){
		//필수값 체크
		frm.find('[required]').each(function(){
			if(!$(this).val() || $(this).val() == $(this).data('replace') || ($(this).attr('type') == 'radio' && !$('[name="'+$(this).attr('name')+'"]:checked').length)){
				isErr = true;
				alert($(this).attr('title')+'을(를) 입력해주세요');
				$(this).focus();
				return false;
			}
		});
		
		//날짜체크
		frm.find('.hasDatepicker').each(function(){
			if($(this).val() && $(this).val() != $(this).val().formatDate()){
				isErr = true;
				alert($(this).attr('title')+'의 날짜형식이 잘못되었습니다');
				$(this).focus();
				return false;
				
			}else if($(this).val() && $(this).val() > '2079-06-30'){
				isErr = true;
				alert('입력가능한 최대일은 2079-06-30일입니다');
				$(this).focus();
				return false;
			}
		});
		
		//날짜체크
		frm.find('.hasMonthpicker').each(function(){
			if($(this).val() && $(this).val() != ($(this).val()+'01').formatDate('yy-mm')){
				isErr = true;
				alert($(this).attr('title')+'의 날짜형식이 잘못되었습니다');
				$(this).focus();
				return false;
				
			}else if($(this).val() && $(this).val() > '2079-06'){
				isErr = true;
				alert('입력가능한 최대일은 2079-06-30일입니다');
				$(this).focus();
				return false;
			}
		});
	}
	
	if(funcBef && !isErr){
		var retVal = funcBef();
		if(retVal == undefined)
			isErr = false;
		else
			isErr = !retVal;
	}
	
	if(!isErr){
		$(frm[0].elements).each(function(){
			if($(this).data('replace'))
				$(this).val($(this).val().replace($(this).data('replace'), ''));
		});
		
		options.beforeSubmit = null;
		$.extend(true, options, baseOpt);
		frm.ajaxSubmit(options);
	}
};

//날짜추가함수
Date.prototype.add = function(dt){
	this.setDate(this.getDate() + dt);
	return this;
}

//3자리마다 콤마추가 함수
String.prototype.formatMoney = function(decLen){
	if(String(this)){
		var str = this.toString();
		var dec = ''
		
		if(decLen && str.indexOf('.') >= 0){
			dec = str.split('.')[1];
			str = str.split('.')[0].replace(/[^-0-9.]+/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ",");
			
			if(dec && parseInt(dec)){
				dec = dec.substring(0, parseInt(decLen));
				str = str + '.' + dec;
			}
			
			return str;
			
		}else
			return String(parseInt(str.replace(/[^-0-9.]+/g, ''))).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
}

//3자리마다 콤마추가 함수
Number.prototype.formatMoney = function(){
	return this.toString().formatMoney();
}

//xxx-xx-xxxxx형식으로 만듬
String.prototype.formatBizno = function(){
	return this.replace(/[^0-9]+/g, '').replace(/(\d{3})(\d{1,2})(\d{5})?/, '$1-$2-$3');
}

//xxx-xx-xxxxx형식으로 만듬
Number.prototype.formatBizno = function(){
	return this.toString().formatBizno();
}

//xxxxxx-xxxxxxx형식으로 만듬
String.prototype.formatCorpno = function(){
	return this.replace(/[^0-9]+/g, '').replace(/(\d{6})(\d{7})?/, '$1-$2');
}

//xxxxxx-xxxxxxx형식으로 만듬
Number.prototype.formatCorpno = function(){
	return this.toString().formatBizno();
}

//날짜형식으로 만듬
String.prototype.formatDate = function(format){
	var dt = new Date();
	var strDt = this.replace(/\D/g,'').replace(/(\d{4})(\d{2})(\d{2})?(\d{2})?(\d{2})?(\d{2})?/,'$1-$2-$3-$4-$5-$6');
	var arr = strDt.split('-');
	
	if(arr.length > 3){
		dt.setYear(arr[0]);
		dt.setMonth(Number(arr[1])-1);
		dt.setMonth(Number(arr[1])-1);
		dt.setDate(arr[2]?arr[2]:'01');
		
		if(!format)
			format = 'yy-mm-dd';
		
		return $.datepicker.formatDate(format, dt);
	}
}

//날짜형식으로 만듬
Number.prototype.formatDate = function(){
	return this.toString().formatDate();
}

$(function(){
	var uri = location.pathname;
	var subBannerClass = {
			issue:'issue', 
			bbs:'bbs', 
			customer:'center', 
			mypage:'mypage', 
			user:'member'
		};
	
	//money 속성가지고있으면 3자리마다 콤마추가
	$('[money]').each(function(){
		if($(this).prop('tagName') == 'INPUT'){
			$(this).keyup(function(){
				$(this).val($(this).val().formatMoney($(this).attr('float')));
			});
			$(this).keyup();
		}else{
			var val = $(this).text().formatMoney($(this).attr('float'));
			if(val && $(this).attr('money'))
				val += $(this).attr('money')
			$(this).text(val);
		}
	});
	
	//bizno 속성가지고있으면 xxx-xx-xxxxx형식으로 만듬
	$('[bizno]').each(function(){
		if($(this).prop('tagName') == 'INPUT')
			$(this).val($(this).val().formatBizno());
		else
			$(this).text($(this).text().formatBizno());
	});
	
	//corpno 속성가지고있으면 xxxxxx-xxxxxxx형식으로 만듬
	$('[corpno]').each(function(){
		if($(this).prop('tagName') == 'INPUT')
			$(this).val($(this).val().formatCorpno());
		else
			$(this).text($(this).text().formatCorpno());
	});
	
	//date 속성을 가지고있으면 
	$('[date]').each(function(){
		if($(this).text().trim())
			$(this).text($(this).text().formatDate($(this).attr('date')));
	});
	
	//datedateToDate 속성을 가지고있으면 
	$('[dateToDate]').each(function(){
		var txt = $(this).text().trim();
		var ptn = $(this).attr('dateToDate');
		var arr;
		if(txt){
			arr = txt.split('~');
			if(arr[0].trim() && arr[1].trim())
				$(this).text(arr[0].formatDate(ptn) + ' ~ ' + arr[1].formatDate(ptn));
		}
	});
	
	//date 속성을 가지고있으면 
	$('[hasnext]').keyup(function(e){
		if($(this).attr('maxlength') && $(this).attr('maxlength') <= $(this).val().length){
			if($(this).attr('hasnext'))
				$('[name="'+$(this).attr('hasnext')+'"]').focus();
			else
				$(this).next().focus();
		}		
	});
	
	//number 속성가지고있으면 숫자만 입력가능
	$('[number]').each(function(){
		if($(this).prop('tagName') == 'INPUT'){
			$(this).on('keyup, keypress, keydown', function(e){
				if(e.keyCode < 96 && e.keyCode > 105 && /[a-z~!@#$%^&*()_+/.,<>?\-=`]/gi.test(String.fromCharCode(e.keyCode))){
					e.preventDefault();
					return false;
				}
				if(/[^0-9]/g.test($(this).val())){
					$(this).val($(this).val().replace(/[^0-9]/g, ''));
					e.preventDefault();
					return false;
				}

			});
		}
	});

});
