var ROOT_URL = "";
var AJAX_URL = "";
var SESSION_USER = "";
var EMP_NO = "";

var SESSION_EXPIRED = 111;

/*
 * 
 */

function doAjax(szCmd, jsonData, fnCallback, callbackParam) {
	var result = 0;
	var szParam = "cmd="+szCmd+"&data="+JSON.stringify( jsonData );
	console.log(szParam);
	$.ajax({
        type : "POST",
        url : "/ajax",
        data : szParam,  
        dataType : "json",
        async: false, //동기화
        //async: true, //비동기화
        contentType : "application/x-www-form-urlencoded; charset=UTF-8",  
        success : function(data){
           // alert("통신데이터 값 : " + data) ;
        	console.log(JSON.stringify( data));
        	result = Number(data.code);
        	
        	if(result == SESSION_EXPIRED){
				document.location.href="logout";
				return;
			}
        	
        	callbackParam.json = data.json;
			fnCallback(data, data.code, callbackParam);
        },

        error : function(request, status, error) {
//			console.log("code:"+request.status+ ", message: "+request.responseText+", error:"+error);
//			result = Number(request.status);
//			fnCallback(result, '', callbackParam);
			
			//showAlertError("Ajax 호출에 실패했습니다. : " + request.status);
			alert("Ajax 호출에 실패했습니다. : " + request.status);
			console.log("-- [doAjax nuclearPower] --- code:"+request.status+ ", message: "+request.responseText+", error:"+error);
			fnCallback(-1, '', callbackParam);
		}
	});
}


function gotoBack(){
	document.gotoBackFrm.submit();
}


//excel file check
function checkFileType(filePath) {
    var fileFormat = filePath.split(".");

    if (fileFormat.indexOf("xls") > -1 || fileFormat.indexOf("xlsx") > -1) {
      return true;
    } else {
      return false;
    }
}


/*
 * 체크박스 event 
 */

//체크박스 클릭 이벤트
function chkAll(mainObj, subObjName){
	var chkMainId = $(mainObj).attr("id");
	//console.log("-- chkAll chkMainId : "+ chkMainId + ", subObjName : "+ subObjName +', size : '+ $("input:checkbox[name='"+subObjName+"']").length);
	
	if($("#"+chkMainId+"").prop("checked")){
		$("input:checkbox[name='"+subObjName+"']").each(function(index) {
	//		console.log('--- 11 index : '+ index+', id : '+ $(this).attr('id'));
			$("#"+$(this).attr('id')).prop("checked", true);
		});
		
	}else{
		$("input:checkbox[name='"+subObjName+"']").each(function(index) {
			//console.log('--- 22 index : '+ index);
			$("#"+$(this).attr('id')).prop("checked", false);
		});
	}
	
}

//서브메뉴 체크박스 설정 이벤트
function smenuCheckEvt(mainObj, subObjName){
	var subObjSize = $("input:checkbox[name='"+subObjName+"']").length
	//console.log("--- [smenuCheckEvt] size : "+ subObjSize);
	var idx = 0;
	$("input:checkbox[name='"+subObjName+"']").each(function(index) {
		//if($("#"+subObjName+ index).prop("checked")){
		if($("#"+$(this).attr('id')).prop("checked")){
	//		console.log("---1 [smenuCheckEvt] id : "+ $(this).attr('id'));
			idx ++;
		}
	});
	
	//console.log("--- [smenuCheckEvt] size : "+ subObjSize + ", idx : "+ idx);
	
	if(idx == subObjSize){
		$("input:checkbox[name='"+mainObj+"']").prop("checked", true);
	}else{
		$("input:checkbox[name='"+mainObj+"']").prop("checked", false);
	}
}

//체크박스 체크 확인
function menuCheck(subObjName){
	var arMenu=[]; //초기화
	
	$("input:checkbox[name='"+subObjName+"']").each(function(index) { 
		//if($("#chkCSub"+index).prop("checked")){
		if($("#"+$(this).attr('id')).prop("checked")){
			//var lastStr = $(this).attr('id').charAt($(this).attr('id').length-1);
			var szKey = $(this).attr('id').substring(6,$(this).attr('id').length); //chkSub + key;
			//console.log('--- [menuCheck] checked id : '+ $(this).attr('id') +', szKey : ' + szKey+ ', index : '+ index);

			arMenu.push(szKey);
		}
	});
	//console.log('--- [menuCheck] arMenu size : '+ arMenu.length);
	
	return arMenu;
}

//버튼 위치이동로 스크롤 이벤트
function autoScrollBtn(obj){
		
	var divEl = $(obj);
    
	var divX = divEl.offset().left;
	var divY = divEl.offset().top;
	_offset_top = divEl.offset().top;

	var res = divY.toString(); //숫자를 문자열로 변환
	var arDivY = res.split(".");
	//console.log('--- [autoScrollBtn] _offset_top : '+ _offset_top);
	
	$('html, body').animate({scrollTop : arDivY[0] - 140}, 400);
}

////radio 체크 확인
//function radioCheck(subObjName){
//	var result = ""; //초기화
//	
//	$("input:radio[name='"+subObjName+"']").each(function(index) { 
//		//if($("#chkCSub"+index).prop("checked")){
//		if($("#"+$(this).attr('id')).prop("checked")){
//			//var lastStr = $(this).attr('id').charAt($(this).attr('id').length-1);
//			var szKey = $(this).attr('id').substring(6,$(this).attr('id').length); //chkSub + key;
//			//console.log('--- [menuCheck] checked id : '+ $(this).attr('id') +', szKey : ' + szKey+ ', index : '+ index);
//			
//			result = szKey;
//		}
//	});
//	//console.log('--- [menuCheck] arMenu size : '+ arMenu.length);
//	
//	return result;
//}

// --------------------------------------------------------------------------------------------------

/*
 * 2019.04.23 
 * Date, Time Func
 */

function currentTime(){
	var date = new Date();
	var year  = date.getFullYear();
	var month = date.getMonth() + 1; // 0부터 시작하므로 1더함 더함
	var day   = date.getDate();
	var time = date.getTime();
	return year+month+ day+time;   
}

function today(){ //yyyy-MM-dd
    var date = new Date();

    var year  = date.getFullYear();
    var month = date.getMonth() + 1; // 0부터 시작하므로 1더함 더함
    var day   = date.getDate();

    if (("" + month).length == 1) { month = "0" + month; }
    if (("" + day).length   == 1) { day   = "0" + day;   }
   
    //console.log("----- today() : " + year + month + day);  
    
    return year+"-"+ month+"-"+ day;   
}


function getDateLabel(szDate) {
    
    var week = new Array('일', '월', '화', '수', '목', '금', '토');
    
    var getDate = new Date(parse(szDate)).getDay();
    //console.log('--- getDate : '+ getDate+', szDate: '+ szDate);
    var dateLabel = week[getDate];
   //console.log('--- dateLabel : '+ dateLabel);
    return dateLabel;
}

function parse(str) {
    var y = str.substr(0, 4);
    var m = str.substr(4, 2);
    var d = str.substr(6, 2);
    return new Date(y,m-1,d);
}

function getDateFormat(szDate, regx){
	//console.log('--- szDate : '+ szDate +', szDate len : '+ szDate.length);
	
	var date = new Date(parse(szDate));
	//console.log('-- yyyy-MM-dd : '+date.format('yyyy-MM-dd'));
	return date.format(regx);
	
}


Date.prototype.format = function (f) {

    if (!this.valueOf()) return " ";



    var weekKorName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

    var weekKorShortName = ["일", "월", "화", "수", "목", "금", "토"];

    var weekEngName = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    var weekEngShortName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

    var d = this;



    return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {

        switch ($1) {

            case "yyyy": return d.getFullYear(); // 년 (4자리)

            case "yy": return (d.getFullYear() % 1000).zf(2); // 년 (2자리)

            case "MM": return (d.getMonth() + 1).zf(2); // 월 (2자리)

            case "dd": return d.getDate().zf(2); // 일 (2자리)

            case "KS": return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)

            case "KL": return weekKorName[d.getDay()]; // 요일 (긴 한글)

            case "ES": return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)

            case "EL": return weekEngName[d.getDay()]; // 요일 (긴 영어)

            case "HH": return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)

            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)

            case "mm": return d.getMinutes().zf(2); // 분 (2자리)

            case "ss": return d.getSeconds().zf(2); // 초 (2자리)

            case "a/p": return d.getHours() < 12 ? "오전" : "오후"; // 오전/오후 구분

            default: return $1;

        }

    });

};



String.prototype.string = function (len) { var s = '', i = 0; while (i++ < len) { s += this; } return s; };

String.prototype.zf = function (len) { return "0".string(len - this.length) + this; };

Number.prototype.zf = function (len) { return this.toString().zf(len); };

/*
 * compareDateTime 
 * args : yyyymmdd
 */
function compareDate1(obj1, obj2, regx){
	
	//시작일 , 종료일 을 비교해서 유효한지 확인한다.
	var sDate = getDateFormat(obj1, regx); //obj1; //2017-04-04;
	var eDate = getDateFormat(obj2, regx); //obj2; //2017-04-04;
	

	console.log('-------[compareDate] sDate : '+ sDate + " eDate : "+ eDate);
	
	if(sDate <= eDate){
		console.log('---- 정상');
		return 0;
	}else{
		console.log('---- 비정상');
		return 1;
	}
	
}



/*
 * compareDateTime 
 * args : yyyy-mm-dd
 */
function compareDate(obj1, obj2){
	var valid = true;
	
	var arMsg = [];
	
	//시작일 , 종료일 을 비교해서 유효한지 확인한다.
	var sDate = obj1; //2017-04-04;
	var eDate = obj2; //2017-04-04;
	var sTime = "00:00:00";
	var eTime = "23:59:59";
	
	var szSDate = makeDate(sDate, sTime).getTime();
	var szEDate = makeDate(eDate, eTime).getTime();
	
	//console.log('-------[compareDate] szSDate : '+ szSDate + " szEDate : "+ szEDate);
	
	if(szSDate <= szEDate){
		console.log('---- 정상');
		return 0;
	}else{
		console.log('---- 비정상');
		return 1;
	}
	
}

function makeDate(szDate, szTime) {
	//console.log("--- [makeDate] -- szDate : "+ szDate +", szTime : "+ szTime);
	var dateObj = new Date(szDate);

	var arTimes = szTime.split(":");
	var hour = Number(arTimes[0]); // hh
	var minute = Number(arTimes[1]); // mm
	var second = Number(arTimes[2]); // ss
	
	dateObj.setHours(hour);
	dateObj.setMinutes(minute);
	dateObj.setSeconds(second);
	
	//debugBy( ["----- makeDate : ", dateObj] );
	
	return dateObj;
}

/*
 * compareDateTime 
 * args : yyyy-mm-dd HH:mm
 */
function compareDateTime(obj1, obj2){
	var valid = true;
	
	var arMsg = [];
	
	//시작일 , 종료일 을 비교해서 유효한지 확인한다.
	var sDate = obj1; //2017-04-04 00:00;
	var eDate = obj2; //2017-04-04 23:59;
	var sSec = "00";
	var eSec = "59";
	//console.log('-------[compareDate] sDate : '+ sDate + " sSec : "+ sSec+ ", eDate : "+ eDate+ " eSec : "+ eSec);
	
	var szSDate = makeDateTime(sDate, sSec).getTime();
	var szEDate = makeDateTime(eDate, eSec).getTime();
	
	//console.log('-------[compareDate] szSDate : '+ szSDate + " szEDate : "+ szEDate);
	
	if(szSDate <= szEDate){
		console.log('---- 정상');
		return 0;
	}else{
		console.log('---- 비정상');
		return 1;
	}
	
}

function makeDateTime(szDate, szTimeSec) {
	//console.log("--- [makeDateTime] -- szDate : "+ szDate +", szTimeSec : "+ szTimeSec);
	
	var dateObj = new Date(szDate);
	//console.log('-- dateObj : '+ dateObj);
	
	dateObj.setSeconds(szTimeSec);
	
	//debugBy( ["----- makeDateTime : ", dateObj] );
	
	return dateObj;
}





function checkBrowzer(){
	var agent = navigator.userAgent.toLowerCase();
	var result = 0;
	if (agent.indexOf("chrome") != -1) {
		console.log("크롬 브라우저입니다.");

	}else if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
		console.log("인터넷익스플로러 브라우저입니다.");
		result = 1;
		
	}else if (agent.indexOf("safari") != -1) {
		console.log("사파리 브라우저입니다.");
		result = 2;
	}else if (agent.indexOf("firefox") != -1) {
		console.log("파이어폭스 브라우저입니다.");
		result = 3;
	}
	
	return result;
}




function changeDate(type, bResult){
	var sTime = " 00:00";
	var eTime = " 23:59";
	var today = szToday();
	var szDate = szToday();
	
	if(type == '1D'){
		szDate = szLastDay(1);
	}else if(type == '3D'){
		szDate = szLastDay(3);
	}else if(type == '1W'){
		szDate = szLastDay(7);
	}else if(type == '1M'){
		szDate = szLastMonth(1);
	}else if(type == '3M'){
		szDate = szLastMonth(3);
	}else if(type == '6M'){
		szDate = szLastMonth(6);
	}
	
	if(bResult == true){
		$("#datepicker1").val(szDate+sTime);
		$("#datepicker2").val(today+eTime);
		
	}else{
		$("#datepicker1").val(szDate);
		$("#datepicker2").val(today);
		
	}
	
}

/* 날짜 객체 받아서 문자열로 리턴하는 함수 */
function getDateStr(date){
	var year  = date.getFullYear();
    var month = date.getMonth() + 1; // 0부터 시작하므로 1더함 더함
    var day   = date.getDate();

    if (("" + month).length == 1) { month = "0" + month; }
    if (("" + day).length   == 1) { day   = "0" + day;   }
   
    //console.log("----- getDateStr() : " + year + month + day);  
    
    return year+"-"+ month+"-"+ day;   
}

/* 오늘 날짜를 문자열로 반환 */
function szToday() {
  var d = new Date()
  return getDateStr(d)
}

/* 오늘로부터 몇 일전 날짜 반환 */
function szLastDay(szStr) {
  var d = new Date()
  var dayOfMonth = d.getDate()
  d.setDate(dayOfMonth - szStr)
  return getDateStr(d)
}

/* 오늘로부터 몇 개월전 날짜 반환 */
function szLastMonth(szStr) {
  var d = new Date()
  var monthOfYear = d.getMonth()
  d.setMonth(monthOfYear - szStr)
  return getDateStr(d)
}

/* 오늘로부터 1주일전 날짜 반환 */
//function szLastOneWeek() {
//  var d = new Date()
//  var dayOfMonth = d.getDate()
//  d.setDate(dayOfMonth - 7)
//  return getDateStr(d)
//}
/* 오늘로부터 3개월전 날짜 반환 */
//function szLastThreeMonth() {
//  var d = new Date()
//  var monthOfYear = d.getMonth()
//  d.setMonth(monthOfYear - 3)
//  return getDateStr(d)
//}


function callDiffDate(sDate, eDate){
    var sdd = sDate;
    var edd = eDate;
    var ar1 = sdd.split('-');
    var ar2 = edd.split('-');
    var da1 = new Date(ar1[0], ar1[1], ar1[2]);
    var da2 = new Date(ar2[0], ar2[1], ar2[2]);
    var dif = da2 - da1;
    var cDay = 24 * 60 * 60 * 1000;// 시 * 분 * 초 * 밀리세컨
    var cMonth = cDay * 30;// 월 만듬
    var cYear = cMonth * 12; // 년 만듬
    
    var result = parseInt(dif/cMonth);
    console.log('--- cMonth result : '+ result);
    return result;
// if(sdd && edd){
//    document.getElementById('years').value = parseInt(dif/cYear)
//    document.getElementById('months').value = parseInt(dif/cMonth)
//    document.getElementById('days').value = parseInt(dif/cDay)
// }
}


/**
 * 중복서브밋 방지
 * 
 * @returns {Boolean}
 */
var doubleSubmitFlag = false;
function doubleSubmitCheck(){
	console.log('-- doubleSubmitFlag : '+ doubleSubmitFlag);
    if(doubleSubmitFlag){
        return doubleSubmitFlag;
    }else{
        doubleSubmitFlag = true;
        return false;
    }
    
   
}

// 두날짜 일수 구하기 
function subtractDate(obj1, obj2){
	var betweenDay = 0;
	
	if(isEmpty(obj1)){
		return -1;
	}
	if(isEmpty(obj2)){
		return -1;
	}
	
	//시작일, 종료일을 비교해서 유효한지 확인한다.
	var sDate = obj1; //2017-04-04;
	var eDate = obj2; //2017-04-04;
	var sTime = "00:00:00";
	var eTime = "00:00:00";
	
	var szSDate = makeDate(sDate, sTime).getTime();
	var szEDate = makeDate(eDate, eTime).getTime();
	
	var betweenDay = ((szEDate - szSDate)/1000/60/60/24)+1;  
	console.log(' --- [subtractDate] betweenDay : '+ betweenDay);
	return betweenDay;
}

// --------------------------------------------------------------------------------------------------

/**
 * 문자열이 빈 문자열인지 체크하여 기본 문자열로 리턴한다.
 * @param str           : 체크할 문자열
 * @param defaultStr    : 문자열이 비어있을경우 리턴할 기본 문자열
 */
function nvl(str, defaultStr){
     
    if(typeof str == "undefined" || str == null || str == "")
        str = defaultStr ;
     
    return str ;
}


	
function isEmpty(val) {
	if(val == null || typeof val == 'undefined' || val.trim().length < 1  || Number(val) < 0) {
		return true;
	}
	//console.log("isEmpty val: " + val);
	return false;
}

function leadingZeros(n, digits) {
	  var zero = '';
	  n = n.toString();

	  if (n.length < digits) {
	    for (var i = 0; i < digits - n.length; i++)
	      zero += '0';
	  }
	  return zero + n;
}

//앞뒤 공백문자열을 제거
String.prototype.trim = function trim(str) { 
  return this.replace(/(^\s*)|(\s*$)/gi, ""); 
}

function isRegExp(str){
	  //특수문자 검증 start
	//  var str = "2011-12-27";
//	  var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
	
	 var regExp = /[<>&\"]/gi;
	 //var regExp = /[<>\"]/gi;
	 
	 if(regExp.test(str)){
	      //특수문자 제거
	      //var t = str.replace(regExp, "")
	      //alert("특수문자를 제거했습니다. ==>" + t);
		 console.log('--- <>&" 특수문자가 존재 합니다.');
		 return true;
	  }else{
	     // alert("정상적인 문자입니다. ==>" + str);
		  console.log('--- 정상적인 문자 입니다. ');
		  return false;
	  }
	  //특수문자 검증 end
}

function isNumber(str) { 
	return (/^[0-9]+$/).test(str);
	//str += ''; // 문자열로 변환
	//str = str.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
	//if (str == '' || isNaN(str)) return false;
	//return true;
}


function debugBy(arMsg) {
	var szMsg = "";
	for(var i=0; i < arMsg.length; i++) {
		if(arMsg[i] instanceof Date) {
			var dateObj = arMsg[i];
			var fullYear = dateObj.getFullYear();
			var month = dateObj.getMonth()+1;
			var date = dateObj.getDate();
			var hour  = dateObj.getHours();
			var min   = dateObj.getMinutes();
			
			if(("" + month).length == 1) { month = "0" + month; }
			if(("" + date).length == 1) { date = "0" + date;   }
			if(("" + hour).length == 1) { hour  = "0" + hour;  }
			if(("" + min).length == 1) { min   = "0" + min;   }
			
			szMsg += "["+fullYear+"/"+month+"/"+date+" "+hour+":"+min+"] ";
		} else {
			szMsg += arMsg[i] + " ";
		}
	}
	console.log( szMsg );
}