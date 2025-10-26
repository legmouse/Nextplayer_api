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
<script src="resources/js/exceljs.min.js"></script>
<!-- ExcelJS 라이브러리 로드 -->
<!-- <script src="resources/js/exceljs.min.js"></script> -->

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
				$(".tabnum > a:eq("+i+")").addClass("gray");
			}
		}
		if(_arLeagueMatchCalendar.length > 0){
			for (var i = 0; i < _arLeagueMatchCalendar.length; i++) {
				$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").removeClass("gray");
				if(_arLeagueMatchCalendar[i].months == months){
					$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").addClass("default");
					
				}else{
					$(".tabnum > a:eq("+_arLeagueMatchCalendar[i].months+")").addClass("gray-o");

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
	var url = 'leagueMgr';
	var ageGroup = "${ageGroup}";
	if (ageGroup != null && ageGroup != '') {
		url = 'leagueMgr?ageGroup=' + ageGroup;
	}
	window.history.pushState(null,null, url);
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
	
	//var matchType = $('input:radio[name=matchType'+index+']:checked').val();
	var matchType = $("#selectMatchType option:selected").val();
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
   	
    /*$form.find('select').each(
   		function(key) {
   			var $obj = $(this);
   			if($obj.val() < 0) {
   				console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
 				alert("알림!\n [홈/어웨이]팀을 선택 하세요.");
   				
   			
   				valid = false;
   				return false;
   			}
   		}
   	);*/
    
    
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
	$form.find("input[name^=homeScore]").each(
			function(key) {
				var $obj = $(this);
				var text = $('select[name='+$obj.attr('name')+'] option:selected').text();
				console.log('----------[input homeScore] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
				$('input[name=homeScore'+key+']').val($obj.val());
			}
	);
	$form.find("input[name^=awayScore]").each(
			function(key) {
				var $obj = $(this);
				var text = $('select[name='+$obj.attr('name')+'] option:selected').text();
				console.log('----------[input awayScore] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
				$('input[name=awayScore'+key+']').val($obj.val());
			}
	);
	$form.find("select[name^=matchType]").each(
			function(key) {
				var $obj = $(this);
				var text = $('select[name='+$obj.attr('name')+'] option:selected').text();
				console.log('----------[select matchType] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
				$('input[name=matchType'+key+']').val($obj.val());
			}
	)
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



//리그 최종순위 확정 
function gotoAddLeagueRank(){
	if (confirm("최종순위를 확정 하시겠습니까?")) {
		$('input[name=sFlag]').val('0');
		document.rankfrm.submit();
	}
}
//리그 최종순위 확정 수정 
function gotoFixLeagueRank(){
	if (confirm("최종순위를 수정 하시겠습니까?")) {
		$('input[name=sFlag]').val('6');
		document.rankfrm.submit();
	}
}
//리그 최종순위 확정 삭제 
function gotoDelLeagueRank(){
	if (confirm("최종순위를 삭제 하시겠습니까?")) {
		$('input[name=sFlag]').val('2');
		document.rankfrm.submit();
	}
}

const getLevelFromAgeGroup = (ageGroup) => {
	let level = "";

	switch (ageGroup) {
		case "U11" :
			level = 1;
			break;
		case "U12" :
			level = 1;
			break;
		case "U14" :
			level = 2;
			break;
		case "U15" :
			level = 2;
			break;
		case "U17" :
			level = 3;
			break;
		case "U18" :
			level = 3;
			break;
		case "U20" :
			level = 5;
			break;
		case "U22" :
			level = 5;
			break;
	}

	return level;
};

const getScoreType = (strValue) => {
	switch (strValue) {
		case "정규":
			return 0;
		case "승부차기":
			return 1;
		case "홈팀부전승":
			return 2;
		case "어웨이팀부전승":
			return 3;
		case "홈팀몰수패":
			return 4;
		case "어웨이팀몰수패":
			return 5;
		case "홈팀실격패":
			return 4;
		case "어웨이팀실격패":
			return 5;
		default:
			return 6;
	}
};

function getAddExcelDate(title, ageGroup) {

	alert('다운로드 시작합니다.');
	$("#crawStatus").text("크롤링중...");
	const level = getLevelFromAgeGroup(ageGroup);
	const params = {
		title,
		matchType: 'LEAGUE2',
		level,
	}

	$.ajax({
		url: '/craw/getMatchCraw',
		type: "GET",
		dataType: 'JSON',
		contentType: "application/json",
		data: params,
		tryCount: 1,
		retryLimit: 1,
		timeout: 1 * 60 * 60 * 1000, // 1hour
		success: function(data){
			downloadExcel(data, title);
			$("#crawStatus").text("");
		},
		error: function (request, status, error) {
			$("#crawStatus").text("");
			alert('에러가 발생했습니다. 다시 시도해주세요.');
		},
	});
}

function downloadExcel(list, title) {

	// ExcelJS 워크북 생성
	var workbook = new ExcelJS.Workbook();
	var sheet = workbook.addWorksheet('Sheet1');

	sheet.getRow(2).values = ['순번', '리그명', '경기일', '경기장소', '홈팀명', '어웨이팀명', '홈팀', '어웨이팀', '결과', '사유'];
	sheet.columns = [
		{key: 'matchOrder', width: 5},
		{key: 'title', width: 70},
		{key: 'matchDataFormat', width: 30},
		{key: 'matchPlace', width: 30},
		{key: 'homeTeam', width: 30},
		{key: 'awayTeam', width: 30},
		{key: 'homeScore', width: 8},
		{key: 'awayScore', width: 8},
		{key: 'scoreType', width: 8},
		{key: 'reason', width: 8},
	]
	const inputData = [];
	for (const v of list) {
		inputData.push(
				{
					matchOrder: v.matchOrder,
					title: title,
					matchDataFormat: v.matchDateFormat,
					matchPlace: v.matchPlace,
					homeTeam: v.homeTeam,
					awayTeam: v.awayTeam,
					homeScore: v.homeScore,
					awayScore: v.awayScore,
					scoreType: getScoreType(v.scoreType),
					reason: '',
				}
		)
	}

	inputData.map((item, index) => {
		sheet.addRow(item);
	});

	// 엑셀 파일 생성
	workbook.xlsx.writeBuffer().then(function (buffer) {
		// Blob 생성
		var blob = new Blob([buffer], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});

		// 다운로드 링크 생성
		var link = document.createElement('a');
		link.href = window.URL.createObjectURL(blob);
		link.download = title+'.xlsx';

		// 클릭(다운로드) 후 요소 제거
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	});
}

const getPlayDataUp = (e, idx, fType, leagueMatchId, matchOrder, title, ageGroup) => {
	alert('불러오기 시작합니다.');
	$("#crawStatus").text("데이터 수집중...");
	const level = getLevelFromAgeGroup(ageGroup);
	const params = {
		title,
		matchType: 'LEAGUE2',
		level,
		ageGroup,
		matchId : leagueMatchId,
		targetMatchOrder: matchOrder,
		sDate: '${leagueInfoMap.sdate1}'
	}
	console.log(params);

	$.ajax({
		url: '/craw/getPlayDataCraw',
		type: "GET",
		dataType: 'JSON',
		contentType: "application/json",
		data: params,
		tryCount: 1,
		retryLimit: 1,
		timeout: 1 * 60 * 60 * 1000, // 1hour
		success: function(data){
			$("#crawStatus").text("");
			const homeScore = $("[name=homeScore" + idx + "]").val();
			const awayScore = $("[name=awayScore" + idx + "]").val();

			function gameResultDiff(msg) {
				if (homeScore != data.homeScore || awayScore != data.awayScore) {
					$("[name=homeScore" + idx + "]").val(data.homeScore);
					$("[name=awayScore" + idx + "]").val(data.awayScore);
					msg += '\n점수에 변동이 있습니다.'
				}
				return msg;
			}

			switch (data.status) {
				case 'READY' :
					if (fType == 'LINEUP') {
						alert('라인업이 안나왔습니다.')
					}
					break;
				case 'LINEUP':
					if (fType == 'LINEUP') {
						$(e).removeClass('default')
						$(e).addClass('gray-o')
						$(e).attr('onclick', null);
						$("#playBtn_" + idx).removeClass('gray-o');
						$("#playBtn_" + idx).addClass('default');
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + leagueMatchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+leagueMatchId+"', '"+ageGroup+"')");
						$("#detailBtn_" + idx).removeClass('gray-o')
						$("#detailBtn_" + idx).addClass('default');
						let msg= '라인업을 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg);
					}
					$("#status_" + idx).html('라인업');
					break;
				case 'START':
					if (fType == 'LINEUP') {
						$(e).removeClass('default')
						$(e).addClass('gray-o');
						$(e).attr('onclick', null);
						$("#playBtn_" + idx).removeClass('gray-o');
						$("#playBtn_" + idx).addClass('default');
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + leagueMatchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+leagueMatchId+"', '"+ageGroup+"')");
						$("#detailBtn_" + idx).removeClass('gray-o')
						$("#detailBtn_" + idx).addClass('default');
						let msg= '게임이 진행중입니다.\n라인업과 진행중인 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg);
					} else if (fType == 'PLAY') {
						let msg = '게임이 진행중입니다.\n진행중인 결과를 불러왔습니다.'
						msg = gameResultDiff(msg);
						alert(msg)
					}
					$("#status_" + idx).html('진행중');
					break;
				case 'END':
					if (fType == 'LINEUP') {
						$(e).removeClass('default')
						$(e).addClass('gray-o');
						$(e).attr('onclick', null);
						$("#detailBtn_" + idx).removeClass('gray-o')
						$("#detailBtn_" + idx).addClass('default');
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+leagueMatchId+"', '"+ageGroup+"')");
						let msg= '게임이 종료되었습니다.\n라인업과 종료된 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg);
					} else if (fType == 'PLAY') {
						$(e).removeClass('default')
						$(e).addClass('gray-o');
						$(e).attr('onclick', null);
						let msg= '게임이 종료되었습니다.\n종료된 결과를 불러왔습니다.';
						gameResultDiff();
						alert(msg)
					}
					$("#status_" + idx).html('종료');
					break;
			}
		},
		error: function (request, status, error) {
			$("#crawStatus").text("");
			alert('에러가 발생했습니다. 다시 시도해주세요.');
		},
	});
};

const goDetailLineup = (matchId, ageGroup) => {
	const leagueId = '${leagueId}';
	location.href = '/leagueMgrMatchPlayData?leagueId=' + leagueId + '&matchId=' + matchId + '&ageGroup=' + ageGroup;
}

function gotoTeamMgrDet(teamId){
	const ageGroup = '${ageGroup}'; 
	const today = new Date();
	const todayYear = today.getFullYear();
	let url = '/teamMgrDet?';
	url += 'ageGroup=' + ageGroup + '&sYear=' + todayYear + '&teamId=' + teamId 
	window.open(url, '_blank', 'fullscreen');
}

const getAllPlayDataUp = () => {

	const sDate = $("#crawSDate").val();

	if (isEmpty(sDate)) {
		alert("경기 일자를 선택 해주세요.");
		return false;
	}

	alert('불러오기 시작합니다.');
	$("#crawStatus").text("데이터 수집중...");
	const level = getLevelFromAgeGroup('${ageGroup}');

	const params = {
		title: '${leagueInfoMap.league_name_origin}',
		matchType: 'LEAGUE2',
		level,
		ageGroup: '${ageGroup}',
		foreignId: '${leagueId}',
		cupType: '',
		sDate: sDate,
		sTime: '',
		sMinute: ''
	}

	$.ajax({
		url: '/craw/getAllPlayDataCraw',
		type: "GET",
		dataType: 'JSON',
		contentType: "application/json",
		data: params,
		tryCount: 1,
		retryLimit: 1,
		timeout: 1 * 60 * 60 * 1000, // 1hour
		success: function(data){
			$("#crawStatus").text("");
			if (data.state == 'success'){
				alert('크롤링을 완료 했습니다. \n 상세정보에서 확인 하세요.');
				location.reload();
			} else if (data.state == 'empty') {
				alert('조회된 경기가 없습니다.');
			}
		},
		error: function (request, status, error) {
			$("#crawStatus").text("");
			alert('에러가 발생했습니다. 다시 시도해주세요.');
		},
	});
}

const getAllMatchScore = (date) => {
	
	const matchSize = "${fn:length(leagueMatchSchedule)}";
	if (matchSize > 0) {
		alert('불러오기 시작합니다.');
		$("#crawStatus").text("크롤링중...");
		const level = getLevelFromAgeGroup('${ageGroup}');

		const params = {
			title: '${leagueInfoMap.league_name_origin}',
			matchType: 'LEAGUE2',
			level,
			ageGroup: '${ageGroup}',
			foreignId: '${leagueId}',
			sDate: date
		}

		$.ajax({
			url: '/craw/getGameAllMatchScoreCraw',
			type: "GET",
			dataType: 'JSON',
			contentType: "application/json",
			data: params,
			tryCount: 1,
			retryLimit: 1,
			timeout: 1 * 60 * 60 * 1000, // 1hour
			success: function(data){
				$("#crawStatus").text("");
				if (data.state == 'success'){
					alert('크롤링을 완료 했습니다. \n 상세정보에서 확인 하세요.');
					location.reload();
				} else if (data.state == 'empty') {
					alert('조회된 경기가 없습니다.');
				}
			},
			error: function (request, status, error) {
				$("#crawStatus").text("");
				alert('에러가 발생했습니다. 다시 시도해주세요.');
			},
		});
	} else {
		alert("등록된 경기일정이 없습니다.");
	}
}

</script>
</head>
<body>
<div class="wrapper" id="wrapper">
	<div style="display:none;" id="resultTable"></div>
	<jsp:include page="../common/menu.jsp" flush="true">
		<jsp:param name="page" value="main"/>
		<jsp:param name="main" value="0"/>
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
				<!--
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
          </select>현재년도 -->
			</div>
		</div>
		<div class="round body">
			<div class="body-head">
				<div class="search">
					<form name="sfrm" id="sfrm" method="post" action="leagueMgr" onsubmit="return false;">
						<input name="cp" type="hidden" value="${cp}">
						<input name="ageGroup" type="hidden" value="">

						<select class="w10" name="sArea">
							<option value="-1" selected>광역 선택</option>
							<c:forEach var="result" items="${areaList}" varStatus="status">
								<option value="${result.area_name}">${result.area_name}</option>
							</c:forEach>
						</select>

						<input type="text" name="sLeagueName" placeholder="리그명 입력"
							   onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
						<i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
					</form>
				</div>
				<div class="others">
					<a class="btn-large gray-o" onclick="gotoLeagueMgr();"><i class="xi-long-arrow-left"></i> 리그관리
						리스트</a>
				</div>
			</div>
			<div class="title">
				<h3>
					기본정보
					<c:choose>
						<c:when test="${empty leagueInfoMap.league_id}"><a onclick="gotoLeagueInfo('0', '');"
																		   class="btn-large default">등록</a></c:when>
						<c:otherwise> <a onclick="gotoLeagueInfo('1', '${leagueId}');"
										 class="btn-large gray-o">수정</a></c:otherwise>
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
						<td class="tl">${leagueInfoMap.league_name}</td>
						<th>순위 선정방식</th>
						<td class="tl">${leagueInfoMap.rank_type eq '0' ? '골득실' : '승자승'}</td>
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
			</div>
			<br>


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


			<form name="rankfrm" id="rankfrm" method="post" action="save_leagueMgrInfoRank" onsubmit="return false;">
				<input name="ageGroup" type="hidden" value="${ageGroup}">
				<input type="hidden" name="trCnt" value="${fn:length(leagueMatchRank)}">
				<input type="hidden" name="leagueId" value="${leagueId}">
				<input type="hidden" name="groups" value="${groups}">
				<input type="hidden" name="sFlag" value="">

				<div class="scroll">
					<table cellspacing="0" class="update over fixed">
						<colgroup>
							<col width="7%">
							<col width="5%">
							<col width="5%">
							<col width="20%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
							<col width="7%">
						</colgroup>
						<thead>
						<tr>
							<th>순위</th>
							<th>앰블럼</th>
							<th>구분</th>
							<th>팀명</th>
							<th>경기수</th>
							<th>승점</th>
							<th>추가승점</th>
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
								<td id="idEmptyList" colspan="13">등록된 내용이 없습니다.</td>
							</tr>
						</c:if>


						<c:if test="${empty leagueFinalRank }">
							<c:forEach var="result" items="${leagueMatchRank}" varStatus="status">
								<tr>
									<td>
										<select id="selRank${status.index}" name="selRank${status.index}">
											<c:forEach var="i" begin="1" end="${leagueInfoMap.ltCnt }" step="1">
												<option value="${i}"  <c:if
														test="${i eq status.count }"> selected</c:if> >${i}위
												</option>
											</c:forEach>
										</select>

									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png"
																					   class="logo"></c:when>
											<c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.team_id eq '-1'}"><span class="label red"
																						   style="min-width: 55px!important;">매칭 오류</span></c:when>
											<c:when test="${result.team_type eq '0'}"><span
													class="label blue">학원</span></c:when>
											<c:when test="${result.team_type eq '1'}"><span
													class="label green">클럽</span></c:when>
											<c:when test="${result.team_type eq '2'}"><span
													class="label red">유스</span></c:when> <%-- 유스일경우?? --%>
										</c:choose>
									</td>
									<td>
										<a class="title" onclick="gotoTeamMgrDet('${result.team_id}');">
											${result.team}
										</a>
										<input name="teamId${status.index}" type="hidden" value="${result.team_id}">
									</td>
									<td>
											${result.playTotalCnt}
										<input name="playTotalCnt${status.index}" type="hidden"
											   value="${result.playTotalCnt}">
									</td>
									<td>
											${result.rankPoint}
										<input name="rankPoint${status.index}" type="hidden"
											   value="${result.rankPoint}">
									</td>
									<td><input name="rankPointAdd${status.index}" class="tc" type="text"
											   value="${result.rankPointAdd}"></td>
									<td>
											${result.win}
										<input name="win${status.index}" type="hidden" value="${result.win}">
									</td>
									<td>
											${result.draw}
										<input name="draw${status.index}" type="hidden" value="${result.draw}">
									</td>
									<td>
											${result.lose}
										<input name="lose${status.index}" type="hidden" value="${result.lose}">
									</td>
									<td>
											${result.point}
										<input name="point${status.index}" type="hidden" value="${result.point}">
									</td>
									<td>
											${result.losePoint}
										<input name="losePoint${status.index}" type="hidden"
											   value="${result.losePoint}">
									</td>
									<td>
											${result.goalPoint}
										<input name="goalPoint${status.index}" type="hidden"
											   value="${result.goalPoint}">
									</td>
								</tr>
							</c:forEach>
						</c:if>


						<c:if test="${!empty leagueFinalRank }">
							<c:forEach var="result" items="${leagueFinalRank}" varStatus="status">
								<tr>
									<td>
										<select id="selRank${status.index}" name="selRank${status.index}">
											<c:forEach var="i" begin="1" end="${leagueInfoMap.ltCnt }" step="1">
												<option value="${i}"  <c:if
														test="${i eq result.rank }"> selected</c:if> >${i}위
												</option>
											</c:forEach>
										</select>

									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png"
																					   class="logo"></c:when>
											<c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.team_id eq '-1'}"><span class="label red"
																						   style="min-width: 55px!important;">매칭 오류</span></c:when>
											<c:when test="${result.team_type eq '0'}"><span
													class="label blue">학원</span></c:when>
											<c:when test="${result.team_type eq '1'}"><span
													class="label green">클럽</span></c:when>
											<c:when test="${result.team_type eq '2'}"><span
													class="label red">유스</span></c:when> <%-- 유스일경우?? --%>
										</c:choose>
									</td>
									<td>
										<a class="title" onclick="gotoTeamMgrDet('${result.team_id}');">
											${result.team}
										</a>
										<input name="teamId${status.index}" type="hidden" value="${result.team_id}">
										<input name="leagueResultId${status.index}" type="hidden"
											   value="${result.league_result_id}">
									</td>
									<td>
											${result.playTotalCnt}
										<input name="playTotalCnt${status.index}" type="hidden"
											   value="${result.playTotalCnt}">
									</td>
									<td>
											${result.rankPoint}
										<input name="rankPoint${status.index}" type="hidden"
											   value="${result.rankPoint}">
									</td>
									<td><input name="rankPointAdd${status.index}" class="tc" type="text"
											   value="${result.rankPointAdd}"></td>
									<td>
											${result.win}
										<input name="win${status.index}" type="hidden" value="${result.win}">
									</td>
									<td>
											${result.draw}
										<input name="draw${status.index}" type="hidden" value="${result.draw}">
									</td>
									<td>
											${result.lose}
										<input name="lose${status.index}" type="hidden" value="${result.lose}">
									</td>
									<td>
											${result.point}
										<input name="point${status.index}" type="hidden" value="${result.point}">
									</td>
									<td>
											${result.losePoint}
										<input name="losePoint${status.index}" type="hidden"
											   value="${result.losePoint}">
									</td>
									<td>
											${result.goalPoint}
										<input name="goalPoint${status.index}" type="hidden"
											   value="${result.goalPoint}">
									</td>
								</tr>
							</c:forEach>
						</c:if>


						</tbody>
					</table>
				</div>
			</form>
			<div class="body-foot">
				<div class="search">
				</div>
				<div class="others">
					<!-- <a class="btn-large default" onclick="gotoAddLeagueRank();">최종순위 확정</a>  -->

					<c:if test="${!empty leagueMatchRank}">

						<c:choose>
							<c:when test="${empty leagueFinalRank}">
								<a class="btn-large default" onclick="gotoAddLeagueRank();">최종순위 확정</a>
							</c:when>
							<c:otherwise>
								<a class="btn-large default" onclick="gotoFixLeagueRank();">최종순위 수정</a>
								<a class="btn-large default" onclick="gotoDelLeagueRank();">최종순위 삭제</a>
							</c:otherwise>
						</c:choose>

					</c:if>

				</div>

			</div>

			<br>


			<div class="title">
				<h3 class="w100">
					리그 경기결과 일괄 등록
				</h3>
			</div>
			<form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data"
				  action="excelUpload" onsubmit="return false;">
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
							<span id="crawStatus" style="font-size: 25px;"></span>
							<a class="btn-large blue-o"
							   onclick="getAddExcelDate('${leagueInfoMap.league_name_origin}', '${ageGroup}');">리그결과 엑셀 다운로드</a>
							<a class="btn-large default" onclick="gotoAddExcel();">리그결과 일괄 등록</a>
							<a class="btn-large gray-o">엑셀 폼 다운로드</a>
						</td>
					</tr>
					</tbody>
				</table>
			</form>
			<br>

			<!-- 리그 경기일정 리스트 -->
			<div id="num">
				<div style="position: relative;">

					<div class="search">
						<div>
							<span class="title ml_10">일자</span>
							<input type="date" id="crawSDate" name="sDate" >
							<button class="btn-large default" onclick="getAllPlayDataUp()">라인업 일괄 불러오기</button>
						</div>
					</div>
					<br />
					<br />
				</div>
				<div class="tabnum" style="margin-bottom:10px;">
					<a class="btn-large w6 btn-tab" data-id="num0" onclick="gotoLeagueMatchSchedule();">전체</a>
					<c:forEach var="i" begin="1" end="12">
						<a class="btn-large w6 btn-tab gray-o" data-id="num${i}"
						   onclick="gotoLeagueMatchSchedule(${i});">${i}월</a>
					</c:forEach>
				</div>

				<div class="scroll">
					<form name="frm" id="frm" method="post" action="save_leagueMgrInfo" onsubmit="return false;">
						<input type="hidden" name="sFlag" value="6">
						<input type="hidden" name="ageGroup" value=${ageGroup}>
						<input type="hidden" name="trCnt" value="${fn:length(leagueMatchSchedule)}">
						<input type="hidden" name="leagueId" value="${leagueId}">
						<input type="hidden" name="leagueName" value="${leagueInfoMap.league_name}">
						<input type="hidden" name="months" value="${months}">

						<table id="frmTB" cellspacing="0" class="update over fixed">
							<colgroup>
								<col width="10%">
								<col width="5%">
								<col width="10%">
								<col width="10%">
								<col width="5%">
								<col width="5%">
								<col width="10%">
								<col width="7.5%">
								<col width="12.5%">
								<col width="5%">
								<col width="5%">
								<col width="5%">
								<col width="5%">
								<col width="5%">

							</colgroup>
							<thead>
							<tr>
								<th rowspan="2">경기일</th>
								<th rowspan="2">경기시간</th>
								<th rowspan="2">경기장소</th>

								<th colspan="2">홈팀</th>
								<th colspan="2">어웨이팀</th>
								<th rowspan="2">결과</th>
								<th rowspan="2">사유</th>
								<th rowspan="2">관리</th>
								<th rowspan="2">라인업</th>
								<th rowspan="2">득점 결과</th>
								<th rowspan="2">상세정보</th>
								<th rowspan="2">경기상태</th>
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
									<td><span class="address">${result.place}</span></td>

									<td class="tr">
										<a class="title" onclick="gotoTeamMgrDet('${result.home_id}');">
										<c:choose>

											<c:when test="${result.home_type eq '0'}"> [학원] </span></c:when>
											<c:when test="${result.home_type eq '1'}"> [클럽] </span></c:when>
											<c:when test="${result.home_type eq '2'}"> [유스] </span></c:when>
										</c:choose>
											${result.home}
										</a>
										<c:choose>
											<c:when test="${empty result.home_emblem}"><img
													src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<input type="text" class="tc" <c:if test="${result.upd_flag eq 1}">style="border: 1px solid red;"</c:if> name="homeScore${status.index}" value="${result.home_score }" placeholder="홈팀 점수 입력">
									</td>
									<td>
										<input type="text" class="tc" <c:if test="${result.upd_flag eq 1}">style="border: 1px solid red;"</c:if> name="awayScore${status.index}" value="${result.away_score }" placeholder="어웨이팀 점수 입력">
									</td>
									<td class="tl">
										<c:choose>
											<c:when test="${empty result.away_emblem}"><img
													src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
										</c:choose>
										<a class="title" onclick="gotoTeamMgrDet('${result.away_id}');">
											${result.away}
										<c:choose>

											<c:when test="${result.away_type eq '0'}"> [학원] </span></c:when>
											<c:when test="${result.away_type eq '1'}"> [클럽] </span></c:when>
											<c:when test="${result.away_type eq '2'}"> [유스] </span></c:when>
										</c:choose>
										</a>
									</td>
									<td class="tc">
										<select id="selectMatchType" name="matchType${status.index}">
											<option value="0" <c:if test="${result.match_type eq '0' }">selected</c:if>>
												정규
											</option>
											<option value="1" <c:if test="${result.match_type eq '1' }">selected</c:if>>
												승부차기
											</option>
											<option value="2" <c:if test="${result.match_type eq '2' }">selected</c:if>>
												홈팀부전승
											</option>
											<option value="3" <c:if test="${result.match_type eq '3' }">selected</c:if>>
												어웨이팀부전승
											</option>
											<option value="4" <c:if test="${result.match_type eq '4' }">selected</c:if>>
												홈팀몰수패
											</option>
											<option value="5" <c:if test="${result.match_type eq '5' }">selected</c:if>>
												어웨이팀몰수패
											</option>
											<option value="6" <c:if test="${result.match_type eq '6' }">selected</c:if>>
												홈팀실격패
											</option>
											<option value="7" <c:if test="${result.match_type eq '7' }">selected</c:if>>
												어웨이팀실격패
											</option>
										</select>
									</td>
									<td>
										<c:choose>
											<c:when test="${ result.match_type eq 0}">
												<input type="text" class="gray" name="reason${status.index}"
													   value="${result.reason}" readonly>
											</c:when>
											<c:otherwise>
												<input type="text" name="reason${status.index}" value="${result.reason}"
													   placeholder="부전승 사유 입력">
											</c:otherwise>
										</c:choose>

									</td>
									<td class="admin"><a class="layer-pop" data-id="layer${status.index}-1"><i
											class="xi-catched"></i></a>
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
													<th>결과</th>
													<th>관리</th>
												</tr>
												</thead>
												<tbody>
												<tr>
													<td>
														<input type="text" name="date${status.index}"
															   placeholder="경기일 - 예) 20190301" value="${result.pdate}"
															   class="datepicker" maxlength="8" autocomplete="off">
														<input name="pdate${status.index}" type="hidden" value="">
														<input name="leagueMatchId${status.index}" type="hidden"
															   value="${result.league_match_id}">
													</td>
													<td>
														<input type="text" name="time${status.index}"
															   placeholder="경기시간 - 예) 14:00" value="${result.ptime}">
													</td>
													<td>
														<input type="text" name="place${status.index}"
															   placeholder="경기장소" class="w100" value="${result.place}">
													</td>
													<td>
														<select class="w100" id="selHome${status.index}"
																name="selHome${status.index}">
															<option value="-1" selected>홈팀 선택</option>
															<c:forEach var="res1" items="${leagueMatchRank}"
																	   varStatus="status1">
																<c:choose>
																	<c:when test="${result.home eq res1.team}">
																		<option value="${res1.team_id}"
																				selected>${res1.team}</option>
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
														<select class="w100" id="selAway${status.index}"
																name="selAway${status.index}">
															<option value="-1" selected>어웨이팀 선택</option>
															<c:forEach var="res1" items="${leagueMatchRank}"
																	   varStatus="status1">
																<c:choose>
																	<c:when test="${result.away eq res1.team}">
																		<option value="${res1.team_id}"
																				selected>${res1.team}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${res1.team_id}">${res1.team}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
														<input name="away${status.index}" type="hidden" value="">
													</td>
													<td>
															<%--								  <select class="w100" id="matchType${status.index}" name="matchType${status.index}">--%>
															<%--									  <option value="0" <c:if test="${res1.match_type eq '0' }">selected</c:if>>정규</option>--%>
															<%--									  <option value="1" <c:if test="${res1.match_type eq '1' }">selected</c:if>>승부차기</option>--%>
															<%--									  <option value="2" <c:if test="${res1.match_type eq '2' }">selected</c:if>>홈팀부전승</option>--%>
															<%--									  <option value="3" <c:if test="${res1.match_type eq '3' }">selected</c:if>>어웨이팀부전승</option>--%>
															<%--									  <option value="4" <c:if test="${res1.match_type eq '4' }">selected</c:if>>홈팀몰수패</option>--%>
															<%--									  <option value="5" <c:if test="${res1.match_type eq '5' }">selected</c:if>>어웨이팀몰수패</option>--%>
															<%--								  </select>--%>
														<input name="away${status.index}" type="hidden" value="">
													</td>
													<td class="admin">
														<a class="btn-large default"
														   onclick="gotoLeagueMatch('${result.league_match_id}', '${status.index}', '${leagueInfoMap.sdate1}', '${leagueInfoMap.edate1}');">수정</a>
														<a class="btn-large gray-o layer-close">취소</a>

													</td>
												</tr>
												</tbody>
											</table>
										</div>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.match_status eq 'READY' }">
												<a class="btn-large default" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.league_match_id}','${result.match_order}', '${leagueInfoMap.league_name_origin}', '${ageGroup}');">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'LINEUP' }">
												<a class="btn-large gray-o">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'START' }">
												<a class="btn-large gray-o">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'END' }">
												<a class="btn-large gray-o">불러오기</a>
											</c:when>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.match_status eq 'READY' }">
												<a class="btn-large gray-o" id="playBtn_${status.index}">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'LINEUP' }">
												<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.league_match_id}','${result.match_order}', '${leagueInfoMap.league_name_origin}', '${ageGroup}');">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'START' }">
												<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.league_match_id}','${result.match_order}', '${leagueInfoMap.league_name_origin}', '${ageGroup}');">불러오기</a>
											</c:when>
											<c:when test="${result.match_status eq 'END' }">
												<a class="btn-large gray-o" id="playBtn_${status.index}">불러오기</a>
											</c:when>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${result.match_status eq 'READY' }">
												<a class="btn-large gray-o" id="detailBtn_${status.index}">상세정보</a>
											</c:when>
											<c:when test="${result.match_status eq 'LINEUP' }">
												<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.league_match_id}', '${ageGroup}');">상세정보</a>
											</c:when>
											<c:when test="${result.match_status eq 'START' }">
												<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.league_match_id}', '${ageGroup}');">상세정보</a>
											</c:when>
											<c:when test="${result.match_status eq 'END' }">
												<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.league_match_id}', '${ageGroup}');">상세정보</a>
											</c:when>
										</c:choose>
									</td>
									<td>
										<div id="status_${status.index}">
											<c:choose>
												<c:when test="${result.match_status eq 'READY' }">
													준비중
												</c:when>
												<c:when test="${result.match_status eq 'LINEUP' }">
													라인업
												</c:when>
												<c:when test="${result.match_status eq 'START' }">
													진행중
												</c:when>
												<c:when test="${result.match_status eq 'END' }">
													종료
												</c:when>
											</c:choose>
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
						<a class="btn-large default" onclick="getAllMatchScore('${leagueInfoMap.sdate1}');">경기결과 일괄 불러오기</a>
						<c:choose>
							<c:when test="${fn:length(leagueMatchSchedule) gt 0 }">
								<a class="btn-large gray-o" onclick="gotoAllLeagueMatch();">경기 결과 일괄 수정</a>
							</c:when>
						</c:choose>
						<!-- <a class="btn-large default" onclick="gotoDelLeagueMatch(2);">일정 일괄 삭제</a> -->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>

<form name="lmgrfrm" id="lmgrfrm" method="post"  action="leagueMgr">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
	<input name="sYear" type="hidden" value="${sYear}">
	<input name="sLeagueName" type="hidden" value="${sLeagueName}">
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
	<input name="sYear" type="hidden" value="${sYear}">
	<input name="sLeagueName" type="hidden" value="${sLeagueName}">
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