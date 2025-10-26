<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script> 
<script src="resources/js/layout.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript"> 

var _arLeagueTeamList = [<c:forEach var="item" items="${leagueTeamList}" varStatus="status">
{
	"index" : "${status.index}",
	"key": "${item.league_tema_id}",
	"leagueName": "${item.league_name}",
	"nickName": "${item.nick_name}",
	"teamId": "${item.team_id}",
	"leagueId": "${item.league_id}"
}<c:if test="${status.last eq false}">,</c:if></c:forEach>
];
var _arLeagueMatchCalendar = [<c:forEach var="item" items="${leagueMatchCalendar}" varStatus="status">
{
	"index" : "${status.index}",
	"years": "${item.years}",
	"months": "${item.months}"
}<c:if test="${status.last eq false}">,</c:if></c:forEach>
];

  
$(document).ready(function() {
	//연령대 선택
	var ageGroup = "${ageGroup}";
	console.log('--- ageGroup :'+ ageGroup );
	if(isEmpty(ageGroup)){
		$("#U18").addClass("active");
	}else{
		$("#"+ageGroup).addClass("active");
		$("input[name=ageGroup]").val(ageGroup);
	}
	
	//검색
	var teamSearch = "${teamSearch}";
	if(!isEmpty(teamSearch)){
		var sArea = "${sArea}";
		var sTeamType = "${sTeamType}";
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
		$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
		$("input[name=sNickName]").val("${sNickName}");
	}
	
	//console.log('-- [init] trCnt : ' + $('#frmTB > tbody tr').length);
	//console.log('--- item0 td cnt : '+ $('.item0 td').length);
	
	//경기일정결과 년도별 조회
	/* var years = "${years}";
	var toYear = today().substr(0, 4);
	console.log('--- years :'+ years +', toYear : '+ toYear);
	if(isEmpty(years)){
		$("select[name=selYears] option[value='"+toYear+"']").prop("selected", "selected");
	}else{
		
	} */
	
	//경기일정결과 월별 조회
	var months = "${months}";
	console.log('--- months :'+ months );
	//if(!isEmpty(months)){
		$(".tabnum > a").removeClass("default");
		$(".tabnum > a").removeClass("gray-o");
		$(".tabnum > a").removeClass("gray");
		
		for (var i = 0; i <= 12; i++) {
			if(i == months){
				$(".tabnum > a:eq("+i+")").addClass("default");
				
			}else{
				$(".tabnum > a:eq("+i+")").addClass("gray-o");
			}
		}
		if(_arLeagueMatchCalendar.length > 0){
			for (var i = 0; i < _arLeagueMatchCalendar.length; i++) {
				$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").removeClass("gray-o");
				if(_arLeagueMatchCalendar[i].months == months){
					$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").addClass("default");
					
				}else{
					$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").addClass("gray");
					
				}
			}
			
		}
	//}
	
	//경기 결과 선택 에 따른 점수 변경
	$('input:radio[name^=matchType]').click(function() {
		var $obj = $(this);
		var idx = $obj.attr('name').substring(9,$obj.attr('name').length);
		//console.log('--- matchType id : '+ $obj.attr('id')+', name : '+ $obj.attr('name')+', val : '+ $obj.val() +', idx : '+ idx);
		//몰수, 부전승 인 경우 점수변경 
		if($obj.val() > 1){
			if($obj.val() == 2 || $obj.val() == 5){ //홈.부전승, 어웨이.몰수패	
				 $('input[name=homeScore'+idx+']').val('3');
				 $('input[name=awayScore'+idx+']').val('0');
				 
			}else if($obj.val() == 3 || $obj.val() == 4){ //어웨이.부전승, 홈.몰수패	
				$('input[name=homeScore'+idx+']').val('0');
				$('input[name=awayScore'+idx+']').val('3');
			}
			
			$('input[name=reason'+idx+']').removeClass('gray');
			$('input[name=reason'+idx+']').removeAttr('readonly');
			$('input[name=reason'+idx+']').attr('placeholder', '부전승 사유 입력');
		}else {
			$('input[name=reason'+idx+']').addClass('gray');
			$('input[name=reason'+idx+']').removeAttr('placeholder');
			$('input[name=reason'+idx+']').attr('readonly', true);
		}
	});
		
});

//연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.lmgrfrm.submit();
}

//리그 관리 리스트 검색 
function gotoSearch(){
	//console.log('--- gotoSearch');
	if(searchFormCheck("sfrm")){
		$("input[name=cp]").val(1);
		document.sfrm.submit(); 
	}
}

function searchFormCheck(regxForm) {
	var valid = true;
	
	var area = $("select[name=sArea]").val();
	var leagueName = $("input[name=sLeagueName]").val();
	
	if(area < 0 && isEmpty(leagueName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}
    
    if(valid == false){
   		return false;
   	} 
    
    if(!isEmpty(leagueName) && isRegExp(leagueName) ){
    	alert('알림!\n'+ '['+$("input[name=sLeagueName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
    	$("input[name=sLeagueName]").focus();
		valid = false;
		return false;
    }
    
    
    if(area < 0 && !isEmpty(leagueName)){
    	$("select[name=sArea]").val('');
	}
    
    return valid;
}

//리그 관리 리스트 이동
function gotoLeagueMgr() {
	 document.lmgrfrm.submit();
}

//리그 기본 정보 이동 
function gotoLeagueInfo(sFlag, idx) {
	 $('input[name=sFlag]').val(sFlag);
	 $('input[name=leagueId]').val(idx);
    document.lifrm.submit();
}

//리그 참가팀 정보 이동 
function gotoLeagueTeam(idx) {
    document.ltfrm.submit();
}

//리그 > 관리 경기결과 정보 이동 
function gotoLeagueMatchSchedule(month) {
	$('input[name=months]').val(month);
    document.lmgrlfrm.submit();
}

//리그 > 관리 경기결과 수정  
function gotoLeagueMatch(lmIdx, index, leagueSdate, leagueEdate){
	console.log('--- gotoLeagueMatch lmIdx :'+ lmIdx +', index : '+ index +', leagueSdate : '+ leagueSdate+', leagueEdate : '+ leagueEdate);
	if(setContentsData(lmIdx, index, leagueSdate, leagueEdate)){
		document.fixfrm.submit();
	}
}

function setContentsData(lmIdx, index, leagueSdate, leagueEdate){
	var valid = true;
	
	var matchType = $('input:radio[name=matchType'+index+']:checked').val();
	var homeScore = $('input[name=homeScore'+index+']').val();
	var awayScore = $('input[name=awayScore'+index+']').val();
	var date = $('input[name=date'+index+']').val();
	var time = $('input[name=time'+index+']').val();
	var place = $('input[name=place'+index+']').val();
	var reason = $('input[name=reason'+index+']').val();
	var selHomeId = $("#selHome"+index+" option:selected").val();
	var selAwayId = $("#selAway"+index+" option:selected").val();
	var selHome = $("#selHome"+index+" option:selected").text();
	var selAway = $("#selAway"+index+" option:selected").text();
	
	var $date = $('input[name=date'+index+']');
	if(isEmpty($date.val())) {
		alert('알림!\n'+ '['+$date.prop('placeholder')+']을 입력 하세요.');
		valid = false;
		return false;
	}
	var $time = $('input[name=time'+index+']');
	if(isEmpty($time.val())) {
		alert('알림!\n'+ '['+$time.prop('placeholder')+']을 입력 하세요.');
		valid = false;
		return false;
	}
	
	var pdate = getDateFormat(date, 'yyyy-MM-dd');
 	var yoil = getDateLabel(date);
 	console.log('--- date : '+ pdate+', yoil : '+ yoil +', time : '+ time);
 	
	
	//var szdate = getDateFormat(date, 'yyyy.MM.dd');
	console.log('--- pdate : '+ pdate+', leagueSdate : '+ leagueSdate +', leagueEdate : '+ leagueEdate);
	
	if(compareDate(leagueSdate, pdate) > 0){
		
		alert("알림!\n 경기일이 대회 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
		valid = false;
		return false;
		
	}else if(compareDate(pdate, leagueEdate) > 0){
		alert("알림!\n 경기일이 대회 종료일 보다 큽니다. \n 확인 후 다시 등록 하세요.");
		valid = false;
		return false;
	}
	
	var $homeScore = $('input[name=homeScore'+index+']');
	if(isEmpty($homeScore.val())) {
		alert('알림!\n'+ '['+$homeScore.prop('placeholder')+']을 입력 하세요.');
		valid = false;
		return false;
	}else {
		if(isNumber($homeScore.val()) == false){
			alert('알림!\n'+ '['+$homeScore.prop('placeholder')+'] 숫자로 입력 하세요.');
			valid = false;
			return false;
		}
		
	}
	
	var $awayScore = $('input[name=awayScore'+index+']');
	if(isEmpty($awayScore.val())) {
		alert('알림!\n'+ '['+$awayScore.prop('placeholder')+']을 입력 하세요.');
		valid = false;
		return false;
	}else{
		if(isNumber($awayScore.val()) == false){
			alert('알림!\n'+ '['+$awayScore.prop('placeholder')+'] 숫자로 입력 하세요.');
			valid = false;
			return false;
		}
	}
	
	
	var $place = $('input[name=place'+index+']');
	if(isEmpty($place.val())) {
		alert('알림!\n'+ '['+$place.prop('placeholder')+']을 입력 하세요.');
		valid = false;
		return false;
	}else {
		if(isRegExp($place.val())){
			alert('알림!\n'+ '['+$place.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
			$place.focus();
			valid = false;
			return false;
		}
	}
	
	if(selHomeId < 0) {
		alert("알림!\n [홈]팀을 선택 하세요.");
		valid = false;
		return false;
	}
	if(selAwayId < 0) {
		alert("알림!\n [어웨이]팀을 선택 하세요.");
		valid = false;
		return false;
	}
	
	var $reason = $('input[name=reason'+index+']');
	if(!isEmpty($reason.val()) && isRegExp($reason.val())){
		alert('알림!\n'+ '['+$reason.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
		valid = false;
		return false;
	}
	
 	$('input[name=leagueMatchId]').val(lmIdx);
 	$('input[name=matchType]').val(matchType);
 	$('input[name=matchDate]').val(pdate+" ("+yoil+") "+time);
 	$('input[name=homeId]').val(selHomeId);
 	$('input[name=home]').val(selHome);
 	$('input[name=homeScore]').val(homeScore);
 	$('input[name=awayId]').val(selAwayId);
 	$('input[name=away]').val(selAway);
 	$('input[name=awayScore]').val(awayScore);
 	$('input[name=place]').val(place);
 	$('input[name=reason]').val(reason);
	
	console.log('--  '
			+', matchType : '+ matchType
			+', homeScore : '+ homeScore
			+', awayScore : '+ awayScore
			+', date : '+ date
			+', time : '+ time
			+', place : '+ place
			+', reason : '+ reason
			+', selHomeId : '+ selHome
			+', selAwayId : '+ selAwayId
			+', selHome : '+ selHomeId
			+', selAway : '+ selAway
			/* +' ---------------------------------------------------- '
			+', leagueMatchId : '+ $('input[name=leagueMatchId]').val()
			+', matchType : '+ $('input[name=matchType]').val()
			+', matchDate : '+ $('input[name=matchDate]').val()
			+', home : '+ $('input[name=home]').val()
			+', homeId : '+ $('input[name=homeId]').val()
			+', homeScore : '+ $('input[name=homeScore]').val() */
			
	);
	return valid;
}


//엑셀 등록
function gotoAddExcel(){
	var file = $("#excelFile").val();

    if(isEmpty(file)) {
	    alert("파일을 선택해주세요.");
	    //showAlert("알림!","파일을 선택해주세요.");
	    return false;
	    
    }else if (!checkFileType(file)) {
	    alert("엑셀 파일만 업로드 가능합니다.");
    	return false;
    }

    if (confirm("업로드 하시겠습니까?")) {
    	document.excelUploadFrm.submit();
    }
}


//리그 경기결과 일과 수정
function gotoAllLeagueMatch(){
	console.log('-- [gotoAllLeagueMatch] trCnt : ' + $('#frmTB > tbody tr').length);
	console.log('-- [gotoAllLeagueMatch] item0 td cnt : '+ $('.item0 td').length);
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	if(formALLCheck('frm')){
		setAllContentsData('frm');
		document.frm.submit();
	}
}

function formALLCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	$form.find("input[name^=date]").each(
   		function(key) {
   			var $obj = $(this);
   			if(isEmpty($obj.val())) {
   				console.log('----------[formCheck]   name : '+ $obj.attr('name'));
				alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
				$obj.focus();
				valid = false;
				return false;
   			}
   			/* else{
	   			console.log('----------[input date] name : '+ $obj.attr('name')+', val : '+$obj.val()+', index:'+key);
	   		    var pdate = getDateFormat($obj.val(), 'yyyy-MM-dd');
	   		 	var yoil = getDateLabel($obj.val());
	   		    var ptime = $('input[name=time'+key+']').val();
	   		 	console.log('--- date : '+ pdate+', yoil : '+ yoil +', time : '+ ptime);
	   		 	$('input[name=pdate'+key+']').val(pdate+" ("+yoil+") "+ptime);
   				
   			} */
   		}
    );

	if(valid == false){
   		return false;
   	} 
	
	$form.find("input[name^=homeScore]").each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
				$obj.focus();
				valid = false;
				return false;

			
			}else{
				if(isRegExp($obj.val())){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				if(isNumber($obj.val()) == false){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 숫자로 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				// remove blank
				var objVal = $obj.val();
				objVal = objVal.trim(objVal);
				$obj.val(objVal);
				
			}
		}
	);
    
   	if(valid == false){
   		return false;
   	} 
   	
	$form.find("input[name^=awayScore]").each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
				$obj.focus();
				valid = false;
				return false;

			
			}else{
				if(isRegExp($obj.val())){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				if(isNumber($obj.val()) == false){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 숫자로 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				// remove blank
				var objVal = $obj.val();
				objVal = objVal.trim(objVal);
				$obj.val(objVal);
				
			}
		}
	);
    
   	if(valid == false){
   		return false;
   	} 
   	
    $form.find('select').each(
   		function(key) {
   			var $obj = $(this);
   			if($obj.val() < 0) {
   				console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
 				alert("알림!\n [홈/어웨이]팀을 선택 하세요.");
   				
   			
   				valid = false;
   				return false;
   			}
   		}
   	);
    
    
    return valid;
}

function setAllContentsData(regxForm){
	var $form = $("#"+regxForm);
	$form.find("input[name^=date]").each(
   		function(key) {
   			var $obj = $(this);
   			console.log('----------[input date] name : '+ $obj.attr('name')+', val : '+$obj.val()+', index:'+key);
   		    var pdate = getDateFormat($obj.val(), 'yyyy-MM-dd');
   		 	var yoil = getDateLabel($obj.val());
   		    var ptime = $('input[name=time'+key+']').val();
   		 	console.log('--- date : '+ pdate+', yoil : '+ yoil +', time : '+ ptime);
   		 	$('input[name=pdate'+key+']').val(pdate+" ("+yoil+") "+ptime);
   		}
    );
	
	$form.find("select[name^=selHome]").each(
		function(key) {
			var $obj = $(this);
			//$("#selTeam1 option:selected").text(); //$('select[name=selTeam1]').val();
			var text = $('select[name='+$obj.attr('name')+'] option:selected').text();
			console.log('----------[select selHome] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
			$('input[name=home'+key+']').val(text);
		}
	);
	$form.find("select[name^=selAway]").each(
		function(key) {
			var $obj = $(this);
			var text = $('select[name='+$obj.attr('name')+'] option:selected').text();
			console.log('----------[select selAway] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
			$('input[name=away'+key+']').val(text);
		}
	);
}



/* //경기일정 일괄 삭제 
function gotoDelLeagueMatch(){
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		document.delFrm.submit();
	}
} */




</script>
</head>
<body>
  <div class="wrapper" id="wrapper">
	<jsp:include page="../common/menu.jsp" flush="true">
	<jsp:param name="page" value="main" />
	<jsp:param name="main" value="0" />
	</jsp:include>
	
 <div class="contents active">
      <div class="head">
        <div class="sub-menu">
  		  	<h2><span></span>리그정보 > 관리</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
        <div class="others">
          <!-- 현재년도 --> 
		  <c:set var="now" value="<%=new java.util.Date()%>" />
		  <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set> 	
          <select name="selYears" id="selYears">
          	<c:forEach var="i" begin="2014" end="${sysYear }" step="1">
          		<c:choose>
	          	<c:when test="${i eq leagueInfoMap.lyears}">
	          		<option value="${i}" selected>${i}</option>
	          	</c:when> 
	          	<c:when test="${i eq sysYear}">
	          		<option value="${i}" selected>${i}</option>
	          	</c:when> 
	          	<c:otherwise>
	          		<option value="${i}">${i}</option>
	          	</c:otherwise>
	          	</c:choose> 
          	</c:forEach>
          </select>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="sfrm" id="sfrm" method="post"  action="leagueMgr" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="">
  			  
	          <select class="w10" name="sArea">
	          <option value="-1" selected>광역 선택</option>
	          <c:forEach var="result" items="${areaList}" varStatus="status">
	          	<option value="${result.area_name}">${result.area_name}</option>
	          </c:forEach>
	          </select>
	          
              <input type="text" name="sLeagueName" placeholder="리그명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoLeagueMgr();"><i class="xi-long-arrow-left"></i> 리그관리 리스트</a>
          </div>
        </div>
        
        <div class="title">
          <h3>
            기본정보 
            <c:choose>
            <c:when test="${empty leagueInfoMap.league_id}"><a onclick="gotoLeagueInfo('0', '');" class="btn-large default">등록</a></c:when>
            <c:otherwise> <a onclick="gotoLeagueInfo('1', '${leagueId}');" class="btn-large gray-o">수정</a></c:otherwise>
            </c:choose>
           
          </h3>
        </div>
       
        <div class="scroll">
          <table cellspacing="0" class="update">
          	<colgroup>
              <col width="10.3%">
              <col width="23%">
              <col width="10.3%">
              <col width="22%">
              <col width="10.3%">
              <col width="22%">
          </colgroup>
            <tbody>
              <tr>
                <th>활성여부</th>
                <td class="tl">
                	<c:choose>
	              	<c:when test="${leagueInfoMap.play_flag eq '1' }">비활성</c:when>
	              	<c:when test="${leagueInfoMap.play_flag eq '0' }">활성</c:when>
	              	</c:choose>
                </td>
                <th>리그명</th>
                <td class="tl" colspan="3">${leagueInfoMap.league_name}</td>
              </tr>
              
              <tr>
                <th>광역</th>
                <td class="tl">${leagueInfoMap.area_name}</td>
                <th>대회기간</th> 
                <td class="tl">
                	<c:if test="${!empty leagueInfoMap.sdate }">
	                	${leagueInfoMap.sdate} ~ ${leagueInfoMap.edate}
                	</c:if>
                </td>
                <th>참가팀</th>
                <td class="tl"><c:if test="${!empty leagueInfoMap.sdate }">${leagueInfoMap.ltCnt}팀</c:if></td>
              </tr>
              
            </tbody>
          </table>
        </div><br>
        
       
        <div class="title">
          <h3 class="w50">
            리그 참가팀
            <c:choose>
            <c:when test="${!empty leagueInfoMap.league_id && empty leagueMatchRank[0].league_id}">
            	<a class="btn-large default" onclick="gotoLeagueTeam(${leagueId});">등록</a>
            </c:when>
            <c:when test="${!empty leagueInfoMap.league_id && !empty leagueTeamList[0].league_id}">
             	<a class="btn-large gray-o" onclick="gotoLeagueTeam(${leagueId});">수정</a>
            </c:when>

            </c:choose>
           
          </h3>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update">
            <colgroup>
              	<col width="5%">
	            <col width="*">
	            <col width="*">
	            <col width="*">
	            <col width="*">
	            <col width="*">
	            <col width="*">
	            <col width="*">
	            <col width="*">
            </colgroup>
            <thead>
              <tr>
				<th>순위</th>
				<th>앰블럼</th>
				<th>구분</th>
				<th>팀명</th>
				<th>경기수</th>
				<th>승점</th>
				<th>승</th>
				<th>무</th>
				<th>패</th>
				<th>득점</th>
				<th>실점</th>
				<th>골득실</th>
              </tr>
            </thead>
            <tbody>
              <c:if test="${empty leagueMatchRank }">
				<tr class="item0">
					<td id="idEmptyList" colspan="12">등록된 내용이 없습니다.</td>
				</tr>
			  </c:if>	
              <c:forEach var="result" items="${leagueMatchRank}" varStatus="status"> 
              <tr>
                <td>${status.count}</td>
              	<td>
					<c:choose>
	              	<c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	              	<c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
	              	</c:choose>
				</td>
				<td>
					<c:choose>
	              	<c:when test="${result.team_id eq '-1'}"><span class="label red" style="min-width: 55px!important;">매칭 오류</span></c:when>
	              	<c:when test="${result.team_type eq '0'}"><span class="label blue">학원</span></c:when>
	              	<c:when test="${result.team_type eq '1'}"><span class="label green">클럽</span></c:when>
	              	<c:when test="${result.team_type eq '2'}"><span class="label red">유스</span></c:when> <%-- 유스일경우?? --%>
	              	</c:choose>
				</td>
                <td>${result.team}</td>
                <td>${result.playTotalCnt}</td>
                <td>${result.rankPoint}</td>
                <td>${result.win}</td>
                <td>${result.draw}</td>
                <td>${result.lose}</td>
                <td>${result.point}</td>
                <td>${result.losePoint}</td>
                <td>${result.goalPoint}</td>
              </tr>
              </c:forEach>
              
            </tbody>
          </table>
         </div><br>
       
        
         <div class="title">
          <h3 class="w100">
            리그 경기결과 일괄 등록
          </h3>
        </div>
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="leagueMatch">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input name="leagueId" type="hidden" value="${leagueId }">
		<input name="leagueName" type="hidden" value="${leagueInfoMap.league_name}">
		<input name="lMgrFlag" type="hidden" value="lmgr">
        <table cellspacing="0" class="update">
          <tbody>
            <tr>
              <th>엑셀 파일 등록</th>
              <td class="tl">
                <input type="file" id="excelFile" name="excelFile">
              </td>
              <td class="tr">
				<a class="btn-large default" onclick="gotoAddExcel();" >리그결과 일괄 등록</a>
				<a class="btn-large gray-o">엑셀 폼 다운로드</a>
              </td>
            </tr>
          </tbody>
        </table>
        </form>
        <br>
        
        <!-- 리그 경기일정 리스트 -->
        <div  id="num">
        <div class="tabnum" style="margin-bottom:10px;">
        	<a class="btn-large w6 btn-tab" data-id="num0" onclick="gotoLeagueMatchSchedule();">전체</a>
        	<c:forEach var="i" begin="1" end="12">
        		<a class="btn-large w6 btn-tab" data-id="num${i}" onclick="gotoLeagueMatchSchedule(${i});">${i}월</a>
        	</c:forEach>
        </div>
        
        
        <div class="scroll">
       <form name="frm" id="frm" method="post"  action="save_leagueMgrInfo" onsubmit="return false;"> 
           <input type="hidden" name="sFlag" value="6">
           <input type="hidden" name="trCnt" value="${fn:length(leagueMatchSchedule)}">
           <input type="hidden" name="leagueId" value="${leagueId}">
           <input type="hidden" name="leagueName"  value="${leagueInfoMap.league_name}">
           <input type="hidden" name="months" value="${months}">
           
        <table id="frmTB" cellspacing="0" class="update">
          <colgroup>
           <col width="*">
				<col width="55px">
				<col width="*">
				<col width="*">
				<col width="13%">
				<col width="55px">
				<col width="55px">
				<col width="13%">
				<col width="*">
				<col width="50px">
          </colgroup>
          <thead>
            <tr>
                <th rowspan="2">경기일</th>
				<th rowspan="2">경기시간</th>
				<th rowspan="2">경기장소</th>
				<th rowspan="2">결과</th>
				<th colspan="2">홈팀</th>
				<th colspan="2">어웨이팀</th>
				<th rowspan="2">사유</th>
				<th rowspan="2">관리</th>
            </tr>
             <tr>
				<th>팀명</th>
				<th>점수</th>
				<th>점수</th>
				<th>팀명</th>
			  </tr>
          </thead>
          <tbody id="tblmId">
          	<c:if test="${empty leagueMatchSchedule }">
				<tr class="item0">
					<td id="idEmptyList" colspan="8">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${leagueMatchSchedule}" varStatus="status"> 
				<tr class="item${status.index}">
				  <td>${result.playdate}</td>
				  <td>${result.ptime}</td>
				  <td>${result.place}</td>
				  <td class="tc">
					<input type="radio" name="matchType${status.index}" id="matchType${status.index}mt0" value="0" <c:if test="${result.match_type eq '0' }">checked</c:if> ><label for="matchType${status.index}mt0">정</label>
					<input type="radio" name="matchType${status.index}" id="matchType${status.index}mt2" value="2" <c:if test="${result.match_type eq '2' }">checked</c:if>><label for="matchType${status.index}mt2">홈.부</label>
					<input type="radio" name="matchType${status.index}" id="matchType${status.index}mt3" value="3" <c:if test="${result.match_type eq '3' }">checked</c:if>><label for="matchType${status.index}mt3">어.부</label>
					<input type="radio" name="matchType${status.index}" id="matchType${status.index}mt4" value="4" <c:if test="${result.match_type eq '4' }">checked</c:if>><label for="matchType${status.index}mt4">홈.몰</label>
					<input type="radio" name="matchType${status.index}" id="matchType${status.index}mt5" value="5" <c:if test="${result.match_type eq '5' }">checked</c:if>><label for="matchType${status.index}mt5">어.몰</label>
				  </td>
				  <td>
				  	<c:choose>
	              	<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	              	<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
	              	</c:choose>
	              	<c:choose>
	              	<%-- <c:when test="${result.home_type eq '0'}"><span class="label blue">학원</span></c:when>
	              	<c:when test="${result.home_type eq '1'}"><span class="label green">클럽</span></c:when>
	              	<c:when test="${result.home_type eq '2'}"><span class="label red">유스</span></c:when>  --%>
	              	<c:when test="${result.home_type eq '0'}"> [학원] </span></c:when>
	              	<c:when test="${result.home_type eq '1'}"> [클럽] </span></c:when>
	              	<c:when test="${result.home_type eq '2'}"> [유스] </span></c:when> 
	              	</c:choose>
	              	${result.home}
				  </td>
				  <td><input type="text" class="tc" name="homeScore${status.index}" value="${result.home_score }" placeholder="홈팀 점수 입력"></td>
				  <td><input type="text" class="tc" name="awayScore${status.index}" value="${result.away_score }" placeholder="어웨이팀 점수 입력"></td>
				  <td>
				  	<c:choose>
	              	<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	              	<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
	              	</c:choose>
	              	<c:choose>
	              	<%-- <c:when test="${result.away_type eq '0'}"><span class="label blue">학원</span></c:when>
	              	<c:when test="${result.away_type eq '1'}"><span class="label green">클럽</span></c:when>
	              	<c:when test="${result.away_type eq '2'}"><span class="label red">유스</span></c:when>  --%>
	              	<c:when test="${result.away_type eq '0'}"> [학원] </span></c:when>
	              	<c:when test="${result.away_type eq '1'}"> [클럽] </span></c:when>
	              	<c:when test="${result.away_type eq '2'}"> [유스] </span></c:when> 
	              	</c:choose>
	              	${result.away}
				  </td>
				  <td>
				  	<c:choose>
				  	<c:when test="${ result.match_type eq 0}">
					  	<input type="text" class="gray" name="reason${status.index}" value="${result.reason}" readonly >
				  	</c:when>
				  	<c:otherwise>
					  	<input type="text" name="reason${status.index}" value="${result.reason}" placeholder="부전승 사유 입력">
				  	</c:otherwise>
				  	</c:choose>
				  	
				  </td>
				  <td class="admin"><a class="layer-pop" data-id="layer${status.index}-1"><i class="xi-catched"></i></a>
					<div class="table-layer" id="layer${status.index}-1">
					  <table cellspacing="0" class="update">
						<colgroup>
						  <col width="150px">
						  <col width="100px">
						  <col width="200px">
						  <col width="150px">
						  <col width="150px">
						  <col width="55px">
						</colgroup>
						<thead>
						  <tr>
							<th>경기일</th>
							<th>경기시간</th>
							<th>경기장소</th>
							<th>홈팀</th>
							<th>어웨이팀</th>
							<th>관리</th>
						  </tr>
						</thead>
						<tbody>
						  <tr> 
							<td>
							  <input type="text" name="date${status.index}" placeholder="경기일 - 예) 20190301" value="${result.pdate}" class="datepicker" maxlength="8" autocomplete="off">
							  <input name="pdate${status.index}" type="hidden" value="">	
							  <input name="leagueMatchId${status.index}" type="hidden" value="${result.league_match_id}">
							</td>
							 <td>
							  <input type="text" name="time${status.index}" placeholder="경기시간 - 예) 14:00" value="${result.ptime}">
							</td>
							<td>
							  <input type="text" name="place${status.index}" placeholder="경기장소" class="w100" value="${result.place}">
							</td> 
							<td>
							  <select class="w100" id="selHome${status.index}" name="selHome${status.index}">
			                  <option value="-1" selected>홈팀 선택</option>
			                  <c:forEach var="res1" items="${leagueMatchRank}" varStatus="status1">
				          	  <c:choose>
				          	  <c:when test="${result.home eq res1.team}">
					          	<option value="${res1.team_id}" selected>${res1.team}</option>
				          	  </c:when> 
				          	  <c:otherwise>
					          	<option value="${res1.team_id}">${res1.team}</option>
				          	  </c:otherwise>
				          	  </c:choose>
				          	  </c:forEach>
			                </select>
			                <input name="home${status.index}" type="hidden" value="">
							</td>
							<td>
							 <select class="w100" id="selAway${status.index}" name="selAway${status.index}">
			                  <option value="-1" selected>어웨이팀 선택</option>
			                  <c:forEach var="res1" items="${leagueMatchRank}" varStatus="status1">
				          	  <c:choose>
				          	  <c:when test="${result.away eq res1.team}">
					          	<option value="${res1.team_id}" selected>${res1.team}</option>
				          	  </c:when> 
				          	  <c:otherwise>
					          	<option value="${res1.team_id}">${res1.team}</option>
				          	  </c:otherwise>
				          	  </c:choose>
				          	  </c:forEach>
			                </select>
			                <input name="away${status.index}" type="hidden" value="">
							</td>
							<td class="admin">
				                 <a class="btn-large default" onclick="gotoLeagueMatch('${result.league_match_id}', '${status.index}', '${leagueInfoMap.sdate1}', '${leagueInfoMap.edate1}');">수정</a>
				                 <a class="btn-large gray-o layer-close">취소</a>
                 
							</td>
						  </tr>
						</tbody>
					  </table>
					</div>
				  </td>
				</tr>
		
			</c:forEach>
          </tbody>
        </table>
        </form>
        </div>
        
        <!-- paging -->
        <%-- <div class="pagination">
			<c:if test="${prev}">
				<a href='javascript:gotoPaging(${start -1});'><i class="xi-angle-left-min"></i></a>
			</c:if>
			<c:forEach var="i" begin="${start }" end="${end }">
				<c:choose>
					<c:when test="${i eq cp}">
						<a href='javascript:gotoPaging(${i});' class="active">${i}</a>
					</c:when>
					<c:otherwise>
						<a href='javascript:gotoPaging(${i});' >${i}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${next }">
				<a href='javascript:gotoPaging(${end +1});'><i class="xi-angle-right-min"></i></a>
			</c:if>
        </div> --%>
        
        <div class="body-foot">
          <div class="search">
          </div>
          <div class="others">
          		<c:choose>
          		<c:when test="${fn:length(leagueMatchSchedule) gt 0 }">
		            <a class="btn-large gray-o" onclick="gotoAllLeagueMatch();">경기 결과 일과 수정</a>
          		</c:when>
          		</c:choose>
          		<!-- <a class="btn-large default" onclick="gotoDelLeagueMatch(2);">일정 일괄 삭제</a> -->
          </div>
        </div>
        </div>
      </div>
	  </div>
  <div>
</body>

<form name="lmgrfrm" id="lmgrfrm" method="post"  action="leagueMgr">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

<form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="${leagueId}">
</form>  

<form name="lmgrlfrm" id="lmgrlfrm" method="post"  action="leagueMgrInfo">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="${leagueId}">
  <input name="months" type="hidden" value="">
</form>  

<form name="ltfrm" id="ltfrm" method="post"  action="leagueTeam">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="${leagueId}">
</form>  

<form name="fixfrm" id="fixfrm" method="post"  action="save_leagueMgrInfo">
  <input type="hidden" name="cp" value="${cp}">
  <input type="hidden" name="sFlag" value="1">
  <input type="hidden" name="ageGroup"  value="${ageGroup}">
  <input type="hidden" name="leagueId"  value="${leagueId}">
  <input type="hidden" name="leagueMatchId"  value="" placeholder="리그경기일정 아이디">
  <input type="hidden" name="matchDate"  value="" placeholder="경기일 - 예) 20190301">
  <input type="hidden" name="place"  value="" placeholder="경기장소">
  <input type="hidden" name="home"  value="" placeholder="홈팀">
  <input type="hidden" name="homeId"  value="" placeholder="홈팀 아이디">
  <input type="hidden" name="homeScore"  value="" placeholder="홈팀 점수">
  <input type="hidden" name="away"  value="" placeholder="어웨이팀">
  <input type="hidden" name="awayId"  value="" placeholder="어웨이팀 아이디">
  <input type="hidden" name="awayScore"  value="" placeholder="어웨이팀 점수">
  <input type="hidden" name="matchType"  value="" placeholder="경기결과">
  <input type="hidden" name="reason"  value="">
</form>  

</html>