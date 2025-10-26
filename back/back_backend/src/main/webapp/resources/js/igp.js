igp = {
	showLoader : function(){
        $('.loader').css({
            'width' : $(window).width(),
            'height' : $(document).height()
        });
        $('.loader div').css('top', $(window).height()/2+$(document).scrollTop()-125);
		$('.loader').show();
	},
	hideLoader : function(){
		$('.loader').hide();
	},
	showMask : function(){
        $('#mask').css({
            'width' : $(window).width(),
            'height' : $(document).height()
        });
        $('#mask').fadeTo("slow", 0.8);
		$('#mask').show();
	},
	hideMask : function(){
		$('#mask').hide();
	},
	parseQueryString : function(str){
	    var params = {};
	    str.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, val){
	    	params[key] = val;
	    });
	    return params;
	},
	queryStringToInput : function(str, frm){
		var params = cwma.parseQueryString(str);
		for(var key in params){
			frm.append('<input type="hidden" name="'+key+'" value="'+params[key]+'">');
		}
	},

	isMobile : function(){
		var ret = false;
		var filter = 'win16|win32|win64|mac|macintel';

		if(navigator.platform){
			if(0 > filter.indexOf(navigator.platform.toLowerCase()))
				ret = true;
		}
		
		if($(window).outerWidth() < 768)
			ret = true;
		
		return ret;
	},
	isBrowser : function(){
		var browser;
		var agent = navigator.userAgent.toLowerCase();
		if(agent.indexOf("chrome") != -1){
			browser = "chrome";
		}else if(agent.indexOf("firefox") != -1){
			browser = "firefox";
		}else if((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)){
			browser = "explorer";
		}
		return browser;
	}
};

function stringCutVal(val, max) {
	if (val == null || val == 'null' || val == '') {
		return "공공 DB 내 해당 정보 없음";
	} else {
		if (val.length > max) {
			return val.substr(0,max) + "...";
		} else {
			return val;
		}
	}
}

function replaceAll(str, searchStr, replaceStr) {
	return str.split(searchStr).join(replaceStr);
}


function stringCutVal(val, max) {
	if (val == null || val == 'null' || val == '') {
		return "공공 DB 내 해당 정보 없음";
	} else {
		if (val.length > max) {
			return val.substr(0,max) + "...";
		} else {
			return val;
		}
	}
}

function stringCutValNoBr(val, max) {
	if (val == null || val == 'null' || val == '') {
		return "공공 DB 내 해당 정보 없음";
	} else {
		val = replaceAll(val,'<br>','');
		if (val.length > max) {
			return val.substr(0,max) + "...";
		} else {
			return val;
		}
	}
}


function stringNullReplaceVal(val, max) {
	if (val == null || val == 'null' || val == '') {
		return "공공 DB 내 해당 정보 없음";
	} else {
		return val;
	}
}

function stringNullReplaceMediVal(val, max) {
	if (val == null || val == 'null' || val == '') {
		return "공공 DB 내 해당 정보 없음";
	} else {
		return "<a href="+val+" target='_blank'>다운로드</a>";
	}
}

function stringNullCheck(val) {
	if (val == null || val == 'null' || val == '') {
		return "Y";
	} else {
		return val;
	}
}

function getImageURL(fileVO, alt, width, height) {
	if (fileVO == null || fileVO.length <= 0) {
		return "공공 DB 내 해당 정보 없음";
	} else {
		return "<img src='/common/download.do?fileSn=" + fileVO[0].fileSn + "&parntsSn=" + fileVO[0].parntsSn + "&parntsSe=" + fileVO[0].parntsSe + "' alt='" + alt + "' style='width:" + width + "px;height:" + height +"px;'/>";
	}
}

function getLinkURL(url, title, name) {
	if (url == null || url == 'null' || url == '') {
		return "공공기관 DB에 해당 정보가 존재하지 않습니다.";
	} else {
		return "<a href='" + url + "' title='" + title + "'>" + name + "</a>";
	}
}

// GHS 그림문자
function getGHSImgAdm(value) {

	if (value == null || value == '') {
		document.write('');
	} else {

		var path = "/static/skill/img/ghs/";
		var imgTag = "";
		var imgSplit = value.split('^');
	
		imgSplit.forEach(function(img) {
			imgTag += "<img src='" + path + img + ".gif' style='width:60px;height:60px;margin-right:5px;'/>";
		});
		
		document.write(imgTag);
	}
}
