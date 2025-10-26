<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script src="resources/js/layout.js"></script>
<!-- ExcelJS 라이브러리 로드 -->
<script src="resources/js/exceljs.min.js"></script>
<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

var _arCupTeamList = [<c:forEach var="item" items="${cupTeamList}" varStatus="status">
{
	"index" : "${status.index}",
	"key": "${item.cup_team_id}",
	"nickName": "${item.nick_name}",
	"teamId": "${item.team_id}",
	"cupId": "${item.cup_id}"
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
	}
	
	//검색
	/* var teamSearch = "${teamSearch}";
	if(!isEmpty(teamSearch)){
		var sArea = "${sArea}";
		var sTeamType = "${sTeamType}";
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
		$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
		$("input[name=sNickName]").val("${sNickName}");
	} */
	
	//경기일정결과 그룹 조회
	var round = "${round}";
	var tour_team = "${cupInfoMap.tour_team}";
	console.log('--- round :'+ round );

	$(".tabnum > a[data-id='num"+round+"']").removeClass("gray-o");
	$(".tabnum > a[data-id='num"+round+"']").addClass("default");

	window.onpageshow = function(event) {
		if ( event.persisted || (window.performance && window.performance.navigation.type == 2)) {
			event.preventDefault();
			//document.cfrm.submit();
			console.log('hi')
		}
	}

});

//대회 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.cmgrfrm.submit();
}

//대회 관리 리스트 이동
function gotoCupMgr() {
	document.cmgrfrm.submit();
}

//대회 예선 결과 수정 이동
function gotoCupSubMatchResult(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
  document.cmgrsrfrm.submit();
}

//대회 본선 결과 수정 이동
function gotoCupMainMatchResult(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
  document.cmgrmrfrm.submit();
}

//대회 기본정보 수정
function gotoCupInfo(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cifrm.submit();
}

//대회 기본개요수상 수정
function gotoCupPrize(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cpfrm.submit();
}


//대회 점수 수정
function gotoAddCupTourMatch(){
	document.frm.submit();
	if(formCheck('frm')){
		//$('input[name=sFlag]').val(sFlag);

	}
}

//대회 토너먼트 영상 등록/수정 
function gotoCupTourMatchVideo(cmIdx, index){

	var live = $("[name='live"+index+"']").val();
	var rep = $("[name='rep"+index+"']").val();
	var high = $("[name='high"+index+"']").val();
	console.log('--- gotoCupTourMatchVideo cmIdx :'+ cmIdx +', index : '+ index + ', live :'+ live +', rep : '+ rep  +', high : '+ high);
	
	$("#videofrm [name='cupTourMatchId']").val(cmIdx);
	$("#videofrm [name='live']").val(live);
	$("#videofrm [name='reply']").val(rep);
	$("#videofrm [name='highlight']").val(high);
	
	document.videofrm.submit();
}

 
//대회 토너먼트 최종순위 확정 
function gotoAddCupTourRank(){
	if (confirm("토너먼트 최종순위를 확정 하시겠습니까?")) {
		$('#rankfrm input[name=sFlag]').val('0');
		document.rankfrm.submit();
	}
}
 
 
//대회 토너먼트 최종순위 삭제 
function gotoDelCupTourRank(){
	if (confirm("토너먼트 최종순위를 삭제 하시겠습니까?")) {
		$('#rankfrm input[name=sFlag]').val('2');
		document.rankfrm.submit();
	}
}




function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
   	if(valid == false){
   		return false;
   	} 

   	$form.find('input:text').not(".layer-pop").each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				*/
				switch ($obj.attr('name')){
				case 'sdate' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'edate' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
				} 

			
			}else{
				/* 
				if(isRegExp($obj.val())){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				 */
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
   	
    $form.find('select').not(".layer-pop").each(
   		function(key) {
   			var $obj = $(this);
   			if($obj.val() < 0) {
   				console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
   				switch ($obj.attr('name')){
   				case 'selArea' :
   					alert("알림!\n 광역 선택 하세요.");
   					break;
   				
   				}
   			
   				valid = false;
   				return false;
   			}
   		}
   	);
    
    if(valid == false){
   		return false;
   	} 
	
    var sdate = $("[name=sdate]").val();
	var edate = $("[name=edate]").val();
	
    if(!isEmpty(sdate) && !isEmpty(edate)) {
		if(compareDate1(sdate, edate, 'yyyy-MM-dd') > 0){
			$('input[name=edate]').focus();
			alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
			valid = false;
			return false;
		}
		
	}
    
    return valid;
}


//참가팀 일괄 삭제 
function gotoDelCupTeam(){
	if( _cupTeamCount == 0 ){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		document.delFrm.submit();
	}
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



//검색 
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



//학원/클럽 찾기 
function gotoTeamSearch(){
	if(checkTeamSearchForm()){
		var index = $('input[name=index]').val();
		var nickName = $('input[name=stNickName]').val();
		var sFlag = "${sFlag}";
		var ageGroup = "${ageGroup}";
		var jsonData = {
				"sFlag":sFlag,
				"index":index,
				"ageGroup":ageGroup,
				"nickName":nickName
		};
		doAjax("doSearchTeam", jsonData, searchTeamCallback, {"index":0, "jsonData":jsonData});
	}
}

function checkTeamSearchForm() {
	var valid = true;
	
	var $obj = $("input[name=stNickName]");
	if(isEmpty($obj.val())) {
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
		
		// remove blank
		var objVal = $obj.val();
		objVal = objVal.trim(objVal);
		$obj.val(objVal);
		
	}
	
  return valid;
}



//대회 경기일정 정보 이동 
function gotoCupMgrTourMatch(round) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";

	$('input[name=round]').val(round);
  document.cmgrtmfrm.submit();
}

//대회 팀 경기일정  
function gotoTeamMatch(trCnt){
	if(trCnt != null || trCnt != ''){
		$('input[name=index]').val(trCnt);
	}
	
	rowAddContentsHtml(trCnt);
}

function rowAddContentsHtml(index){
	var trCnt = $("#frmTB > tbody tr").length;
	var item0Td = $('.item0 td').length;
	console.log('-- [rowAddContentsHtml] trCnt : ' + trCnt  );
	console.log('--- item0 td cnt : '+ $('.item0 td').length);
	if(item0Td == 1){
		$("#tblmId").html('');
		trCnt = 0; 
	} 

	$('input[name=trCnt]').val(trCnt + 1);
	/* if(index == null || index == ''){
		$('input[name=trCnt]').val(trCnt + 1);
	}else{
		$('input[name=trCnt]').val(trCnt);
	} */
	
	
	var szHtml = "";
	
	szHtml += "<tr class='item"+trCnt+"'>";
	szHtml += "<td>";
	szHtml += "<input type='text' name='date"+trCnt+"'  autocomplete='off' class='datepicker' placeholder='경기일 - 예) 20190301' maxlength='8'>";
	szHtml += "<input name='pdate"+trCnt+"' type='hidden' value=''>";
	szHtml += "</td>";
	szHtml += "<td><input type='text' name='time"+trCnt+"' placeholder='경기시간 - 예)14:00'> </td>";
	szHtml += "<td><input type='text' name='place"+trCnt+"' class='w100' placeholder='경기장소'> </td>";
	
	szHtml += "<td>";
	szHtml += "<select name='selHome"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>홈팀 선택</option>";
				for (var i = 0; i < _arCupTeamList.length; i++) {
					szHtml += "<option value="+_arCupTeamList[i].teamId+">"+_arCupTeamList[i].nickName+"</option>";
				}
	szHtml += "</select>";
	szHtml += "<input name='home"+trCnt+"' type='hidden' value=''>";
	szHtml += "</td>";
	szHtml += "<td>";
	szHtml += "<select name='selAway"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>어웨이팀 선택</option>";
				for (var i = 0; i < _arCupTeamList.length; i++) {
					szHtml += "<option value="+_arCupTeamList[i].teamId+">"+_arCupTeamList[i].nickName+"</option>";
				}
	szHtml += "</select>";
	szHtml += "<input name='away"+trCnt+"' type='hidden' value=''>"
	szHtml += " </td>";
	
	szHtml += "<td class='admin'>";
		//추가 시 해당 리스트 바로 밑으로 생성
		//szHtml += "<a class='btn-pop' data-id='find-club' onclick='gotoTeam("+trCnt+");'><i class='xi-plus-circle-o'></a></i>";
		szHtml += "<a onclick='removeTeamMatch("+trCnt+");'><i class='xi-close-circle-o'></i></a>";
		
		
	szHtml += "</td>";
	szHtml += "</tr> ";
	
	$('#tblmId').append(szHtml);
	
	$(document).find("input[name^=date]").removeClass('hasDatepicker').datepicker({
		changeMonth: true, 
	       changeYear: true,
	       nextText: '다음 달',
	       prevText: '이전 달', 
	       dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
	       dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
	       monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	       monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	       dateFormat: "yymmdd",
	       //maxDate: 0,                       // 선택할수있는 최소날짜, ( 0 : 오늘 이후 날짜 선택 불가)
	       onClose: function( selectedDate ) {    
	            //시작일(startDate) datepicker가 닫힐때
	            //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
	           $("#endDate").datepicker( "option", "minDate", selectedDate );
	       }  
	});     
}

//리그 경기일정 rows 삭제 
function removeTeamMatch(index){
	$(".item"+index).remove();//tr 테그 삭제
	console.log('--  [removeTeamMatch] frm tr lng : ' + $("#frmTB > tr").length +', index : '+ index );
	
	if($("#frmTB > tbody tr").length == 0){
		var szHtml = "<tr class='item0'><td id='idEmptyList' colspan='6'>등록된 내용이 없습니다.</td></tr>";
		$('#tblmId').append(szHtml);
	}else{
		resetRowDataOrder();
	}
}


//리그 경기일정 정보 리스트 순서 재정의 
function resetRowDataOrder(){
	var trCnt = $('#frmTB > tbody tr').length;
	console.log('-- [resetRowDataOrder] trCnt : ' + trCnt);
	$("input[name=trCnt]").val(trCnt);
	for (var i = 0; i < trCnt; i++) {
		 $("#frmTB > tbody tr:eq("+i+")").attr('class', 'item'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=date]").attr('name', 'date'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=pdate]").attr('name', 'pdate'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=time]").attr('name', 'time'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=place]").attr('name', 'place'+i);
		 $("#frmTB > tbody tr:eq("+i+") td select[name^=selHome]").attr('name', 'selHome'+i);
		 $("#frmTB > tbody tr:eq("+i+") td select[name^=selAway]").attr('name', 'selAway'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=home]").attr('name', 'home'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=away]").attr('name', 'away'+i);
		// $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(0)").attr('onclick', 'gotoTeam(\''+i+'\')');
		 $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(0)").attr('onclick', 'removeTeamMatch(\''+i+'\')');
		 
	}
}

//대회 > 관리 경기결과 수정  
function gotoCupTourMatch(ctmIdx, index, cupSdate, cupEdate){
	console.log('--- gotoCupTourMatch ctmIdx :'+ ctmIdx +', index : '+ index +', cupSdate : '+ cupSdate+', cupEdate : '+ cupEdate);
	if(setContentsData(ctmIdx, index, cupSdate, cupEdate)){
		document.fixfrm.submit();
	}
}

function setContentsData(ctmIdx, index, cupSdate, cupEdate){
	var valid = true;
	
	var matchType = $("#selMatchType"+index+" option:selected").val();
	var homeScore = $('input[name=homeScore'+index+']').val();
	var awayScore = $('input[name=awayScore'+index+']').val();
	var homePk = $('input[name=homePk'+index+']').val();
	var awayPk = $('input[name=awayPk'+index+']').val();
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
	console.log('--- pdate : '+ pdate+', cupSdate : '+ cupSdate +', cupEdate : '+ cupEdate);
	
	if(compareDate(cupSdate, pdate) > 0){
		
		alert("알림!\n 경기일이 대회 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
		valid = false;
		return false;
		
	}else if(compareDate(pdate, cupEdate) > 0){
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
	
	$('input[name=cupTourMatchId]').val(ctmIdx);
	$('input[name=matchType]').val(matchType);
	$('input[name=matchDate]').val(pdate+" ("+yoil+") "+time);
	$('input[name=homeId]').val(selHomeId);
	$('input[name=home]').val(selHome);
	$('input[name=homeScore]').val(homeScore);
	$('input[name=homePk]').val(homePk);
	$('input[name=awayId]').val(selAwayId);
	$('input[name=away]').val(selAway);
	$('input[name=awayScore]').val(awayScore);
	$('input[name=awayPk]').val(awayPk);
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


function fnfixRanking(cupTourMatchId, home, away, homeId, awayId, typeId, title, body) {

	const cup_id = $("input[name=cupId]").val();
	const ageGroup = $("input[name=ageGroup]").val();

	const confirmMsg = confirm("경기 종료 처리 하시겠습니까?");

	if (confirmMsg) {
		const dataParam = {
			cupId: cup_id,
			cupTourMatchId: cupTourMatchId + "",
			ageGroup: ageGroup
		}

		$.ajax({
			type: 'POST',
			url: '/cupTourFixRanking',
			data: JSON.stringify(dataParam),
			dataType: "text",
			contentType: "application/json",
			success: function(data) {
				if (JSON.parse(data).data.stateCode === 200) {
					alert('종료 확정 되었습니다.');
					location.reload();
				} else {
					alert('확정에 실패 했습니다.');
					location.reload();
				}
			}
		});
		
		var param = {
	            ageGroup : ageGroup,
	            cupId: cupId,
	    };
		
		var bodyStr = '';
		
		bodyStr = body.replace('팀명', home);
		bodyStr = bodyStr.replace('팀명', away);
		
		const pushParam = {
				title: title,
				body: bodyStr,
	            uage: ageGroup,
	            home: homeId,
	            away: awayId,
	            path: encodeURIComponent('/contest/' + cupId + '?ageGroup=' + ageGroup),
	            param: JSON.stringify(param),
	            description: '대회 상세로 이동',
	            autoFlag: 0,
	            method: "END",
	            typeId: typeId
		}
		
		$.ajax({
			type: 'POST',
			async: false,
			url: '/insertContestPush',
			dataType: 'json',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(pushParam),
			success: function(data) {
				console.log(data);
				// location.reload();
			}
		});

	}

}


const getLevelFromAgeGroup = (ageGroup) => {
	let level = "";

	switch (ageGroup) {
		case "U11" :
			level = 51;
			break;
		case "U12" :
			level = 51;
			break;
		case "U14" :
			level = 52;
			break;
		case "U15" :
			level = 52;
			break;
		case "U17" :
			level = 53;
			break;
		case "U18" :
			level = 53;
			break;
		case "U20" :
			level = 54;
			break;
		case "U22" :
			level = 54;
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
		matchType: 'MATCH',
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
			downloadExcel(title, data);
			$("#crawStatus").text("");
		},
		error: function (request, status, error) {
			alert('에러가 발생했습니다. 다시 시도해주세요.');
			$("#crawStatus").text("");
		},
	});
}

function downloadExcel(title, list) {

	// ExcelJS 워크북 생성
	var workbook = new ExcelJS.Workbook();
	var sheet = workbook.addWorksheet('Sheet1');
	var sheet2 = workbook.addWorksheet('Sheet2');

	/*sheet1 start*/
	sheet.getRow(2).values = ['순번', '조명', '경기일', '경기장소', '홈팀명', '어웨이팀명', '홈팀', '어웨이팀', '승-홈팀', '승-어웨이팀', '결과', '사유'];
	sheet.columns = [
		{key: 'matchOrder', width: 5},
		{key: 'group', width: 5},
		{key: 'matchDataFormat', width: 30},
		{key: 'matchPlace', width: 30},
		{key: 'homeTeam', width: 30},
		{key: 'awayTeam', width: 30},
		{key: 'homeScore', width: 8},
		{key: 'awayScore', width: 8},
		{key: 'homePenaltyScore', width: 8},
		{key: 'awayPenaltyScore', width: 8},
		{key: 'scoreType', width: 8},
		{key: 'reason', width: 8},
	]
	const inputDataSheet1 = [];
	for (const v of list) {
		inputDataSheet1.push(
				{
					matchOrder: v.matchOrder,
					group: '',
					matchDataFormat: v.matchDateFormat,
					matchPlace: v.matchPlace,
					homeTeam: v.homeTeam,
					awayTeam: v.awayTeam,
					homeScore: v.homeScore,
					awayScore: v.awayScore,
					homePenaltyScore: v.homePenaltyScore,
					awayPenaltyScore: v.awayPenaltyScore,
					scoreType: getScoreType(v.scoreType),
					reason: '',
				}
		)
	}

	inputDataSheet1.map((item, index) => {
		sheet.addRow(item);
	});
	/*sheet1 end*/

	/*sheet2 start*/
	sheet2.getRow(2).values = ['순번', '강수', '토너먼트번호', '연결번호', '홈/어웨이', '경기일', '경기장소', '홈팀명', '어웨이팀명', '홈팀', '어웨이팀', '승-홈팀', '승-어웨이팀', '결과', '사유'];
	sheet2.columns = [
		{key: 'matchOrder', width: 5},
		{key: 'gang', width: 15},
		{key: 'tourNumber', width: 15},
		{key: 'connNumber', width: 15},
		{key: 'homeAway', width: 15},
		{key: 'matchDataFormat', width: 30},
		{key: 'matchPlace', width: 30},
		{key: 'homeTeam', width: 30},
		{key: 'awayTeam', width: 30},
		{key: 'homeScore', width: 8},
		{key: 'awayScore', width: 8},
		{key: 'homePenaltyScore', width: 8},
		{key: 'awayPenaltyScore', width: 8},
		{key: 'scoreType', width: 8},
		{key: 'reason', width: 8},
	]

	const inputDataSheet2 = [];
	for (const v of list) {
		inputDataSheet2.push(
				{
					matchOrder: v.matchOrder,
					gang: '',
					tourNumber: '',
					connNumber: '',
					homeAway: '',
					matchDataFormat: v.matchDateFormat,
					matchPlace: v.matchPlace,
					homeTeam: v.homeTeam,
					awayTeam: v.awayTeam,
					homeScore: v.homeScore,
					awayScore: v.awayScore,
					homePenaltyScore: v.homePenaltyScore,
					awayPenaltyScore: v.awayPenaltyScore,
					scoreType: getScoreType(v.scoreType),
					reason: '',
				}
		)
	}

	inputDataSheet2.map((item, index) => {
		sheet2.addRow(item);
	});
	/*sheet2 end*/

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


const getPlayDataUp = (e, idx, fType, matchId, matchOrder, title, ageGroup, home, away) => {
	alert('불러오기 시작합니다.');
	$("#crawStatus").text("데이터 수집중...");
	var cupId = '${cupId}';
	const level = getLevelFromAgeGroup(ageGroup);
	const params = {
		title,
		matchType: 'MATCH',
		level,
		ageGroup,
		matchId : matchId,
		targetMatchOrder: matchOrder,
		cupType: 'TOUR',
		sDate: '${cupInfoMap.sdate1}',
		cupId: cupId
	}
	
	if (home != null && home != '' && away != null && away != '') {
		params.home = home;
		params.away = away;
	}

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
			const homePkScore = $("[name=homePk" + idx + "]").val();
			const awayPkScore = $("[name=awayPk" + idx + "]").val();

			function gameResultDiff(msg) {
				if (homeScore != data.homeScore || awayScore != data.awayScore || data.homePenaltyScore != homePkScore || data.awayPenaltyScore != awayPkScore) {
					$("[name=homeScore"+idx+"]").val(data.homeScore);
					$("[name=awayScore"+idx+"]").val(data.awayScore);
					$("[name=homePk"+idx+"]").val(data.homePenaltyScore);
					$("[name=awayPk"+idx+"]").val(data.awayPenaltyScore);
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
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + matchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "', '" + home + "', '" + away + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+matchId+"', '"+ageGroup+"')");
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
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + matchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "', '" + home + "', '" + away + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+matchId+"', '"+ageGroup+"')");
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
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('"+matchId+"', '"+ageGroup+"')");
						let msg= '게임이 종료되었습니다.\n라인업과 종료된 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg);
					} else if (fType == 'PLAY') {
						$(e).removeClass('default')
						$(e).addClass('gray-o');
						$(e).attr('onclick', null);
						let msg= '게임이 종료되었습니다.\n종료된 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg)
					}
					$("#status_" + idx).html('종료');
					break;
			}
		},
		error: function (request, status, error) {
			alert('에러가 발생했습니다. 다시 시도해주세요.');
			$("#crawStatus").text("");
		},
	});
};

const getAllPlayDataUp = () => {

	const sDate = $("#crawSDate").val();
	const sTime = $("#crawSTime option:selected").val();
	const sMinute = $("#crawSMinute option:selected").val();

	if (isEmpty(sDate) || isEmpty(sTime) || isEmpty(sMinute)) {
		alert("경기 일자를 선택 해주세요.");
		return false;
	}

	alert('불러오기 시작합니다.');
	$("#crawStatus").text("데이터 수집중...");
	const level = getLevelFromAgeGroup('${ageGroup}');

	const params = {
		title: '${cupInfoMap.cup_name_origin}',
		matchType: 'MATCH',
		level,
		ageGroup: '${ageGroup}',
		foreignId: '${cupId}',
		cupType: 'TOUR',
		sDate: sDate,
		sTime: sTime,
		sMinute: sMinute
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

const goDetailLineup = (matchId, ageGroup) => {
	const cupId = '${cupId}';
	location.href = '/cupMgrTourMatchPlayData?cupId=' + cupId + '&matchId=' + matchId + '&ageGroup=' + ageGroup;
}

function gotoTeamMgrDet(teamId){
	/* $('#tmdfrm input[name="teamId"]').val(teamId);
	document.tmdfrm.submit(); */
	const ageGroup = '${ageGroup}'; 
	const today = new Date();
	const todayYear = today.getFullYear();
	let url = '/teamMgrDet?';
	url += 'ageGroup=' + ageGroup + '&sYear=' + todayYear + '&teamId=' + teamId 
	window.open(url, '_blank', 'fullscreen');
}

const getAllMatchScore = (date) => {
	
	const matchSize = "${fn:length(cupTourMatchResultList)}";
	console.log(matchSize);
	if (matchSize > 0) {
		alert('불러오기 시작합니다.');
		$("#crawStatus").text("크롤링중...");
		const level = getLevelFromAgeGroup('${ageGroup}');

		const params = {
			title: '${cupInfoMap.cup_name_origin}',
			matchType: 'MATCH',
			level,
			ageGroup: '${ageGroup}',
			foreignId: '${cupId}',
			cupType: 'TOUR',
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
	
	function sendPush(type, id, home, away, homeId, awayId, typeId, title, body) {
		var autoflag = '${pushLineup.auto_flag}';
		if (autoflag == 1) {
			alert('자동발송 기능이 off 로 설정되어 있습니다.\n자동발송을 on으로 설정해주세요');
			return false;
		}
		var age = '${ageGroup}';
		var cupId = '${cupId}';
		var param = {
			ageGroup : age,
			cupId: cupId,
		};
		var bodyStr = '';
		bodyStr = body.replace('팀명', home);
		bodyStr = bodyStr.replace('팀명', away);
		var data = {
	            title: title,
	            body: bodyStr,
	            uage: age,
	            home: homeId,
	            away: awayId,
	            path: encodeURIComponent('/contest/' + cupId + '?ageGroup=' + age),
	            param: JSON.stringify(param),
	            description: '대회 상세로 이동',
	            typeId: typeId,
	            method: type,
	            matchId: id,
	            matchType: 'TOUR',
	            autoFlag: 0
		};
		
		$.ajax({
            type: 'POST',
            url: '/insertContestPush',
            dataType: 'json',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(data),
            success: function (res) {
            	console.log(res);
                if (res.result == 'success') {
                	alert('푸시 발송 성공');
                	location.reload();
                } else {
                	alert('푸시 발송 실패');
                }
            }
        });
	}
}

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
  		  	<h2><span></span>대회정보 > 등록/수정</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>

      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="sfrm" id="sfrm" method="post"  action="league" onsubmit="return false;">
          	  <input name="sFlag" type="hidden" value="${sFlag}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup}">
  			  
              <input type="text" name="sLeagueName" placeholder="대회명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoCupMgr();"><i class="xi-long-arrow-left"></i> 대회등록 리스트</a>
          </div>
        </div>
        
        <div class="title">
          <h3>
            기본정보
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
                  	<c:if test="${cupInfoMap.play_flag eq '0'}">활성</c:if>
                  	<c:if test="${cupInfoMap.play_flag eq '1'}">비활성</c:if>
                  </td>
                  <th>대회유형</th>
                  <td class="tl" colspan="3">
                  	<c:if test="${cupInfoMap.cup_type eq '0'}">예선리그+토너먼트</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '1'}">예선리그+본선리그+토너먼트</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '2'}">풀리그</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '3'}">토너먼트</c:if>
                  </td>
                </tr>
                <tr>
                  <th>대회명</th>
                  <td class="tl">${cupInfoMap.cup_name}</td>
                  <th>대회기간</th>
                  <td class="tl">${cupInfoMap.play_sdate} ~ ${cupInfoMap.play_edate}</td>
                  <th>참가팀</th>
                  <td class="tl">${cupInfoMap.cup_team}팀</td>
                </tr>
                <tr>
                  <th>예선본선 조편성</th>
                  <td class="tl">${cupInfoMap.sub_team_count} , ${cupInfoMap.main_team_count}</td>
                  <th>토너먼트 타입</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.tour_type eq '0'}">대진표</c:if>
                  	<c:if test="${cupInfoMap.tour_type eq '1'}">추첨제</c:if>
                  </td>
                  <th>토너먼트 팀수</th>
                  <td class="tl">${cupInfoMap.tour_team}팀</td>
                </tr>
              </tbody>
            </table>
          </div>
          <br>
        
        
        
        
        
        
        <div class="title">
          <h3 class="w100">
            토너먼트 경기결과 일괄 등록
          </h3>
        </div>
        
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="cupTourMatch">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input name="cupId" type="hidden" value="${cupId}">
		<input name="cMgrFlag" type="hidden" value="cmgr">
        <table cellspacing="0" class="update">
          <tbody>
            <tr>
              <th>엑셀 파일 등록</th>
              <td class="tl">
                <input type="file" id="excelFile" name="excelFile">
              </td>
              <td class="tr">
				<!-- <a class="btn-large default btn-show" data-id="num">참가팀 생성</a> -->
				  <span id="crawStatus" style="font-size: 25px;"></span>
				  <a class="btn-large blue-o"
					 onclick="getAddExcelDate('${cupInfoMap.cup_name_origin}', '${ageGroup}');">대회결과 엑셀
					  다운로드</a>
				<a class="btn-large default" onclick="gotoAddExcel();" >토너먼트 경기결과 일괄 등록</a>
				<!-- <a class="btn-large default btn-pop" data-id="update-leagueTeam-add"  >참가팀 일괄 등록</a> -->
				<a class="btn-large gray-o">엑셀 폼 다운로드</a>
              </td>
            </tr>
          </tbody>
        </table>
        </form>
        <br>
        
		<!-- 대회 경기일정 리스트 -->
        <div id="num1">
        <div class="tabnum" style="margin-bottom:20px;">
        
        <c:set var="roundData" value="128|64|32|16|8|4|2" />
		<c:set var="arRound" value="${fn:split(roundData, '|')}" />

        <c:forEach var="res1" items="${arRound}" varStatus="index1">
	        <c:choose>
	        <c:when test="${res1 == 2 }">
				<a class="btn-large w6 btn-tab gray-o" data-id="num${res1}" onclick="gotoCupMgrTourMatch('${res1}');">결승</a>
			</c:when>
	        <c:when test="${res1 > 2 and res1 <= cupInfoMap.tour_team }">
				<a class="btn-large w6 btn-tab gray-o" data-id="num${res1}" onclick="gotoCupMgrTourMatch('${res1}');">${res1}강</a>
			</c:when>
	        <c:when test="${res1 > cupInfoMap.tour_team and (res1 - cupInfoMap.tour_team) < cupInfoMap.tour_team}">
				<a class="btn-large w6 btn-tab gray-o" data-id="num${cupInfoMap.tour_team}" onclick="gotoCupMgrTourMatch('${cupInfoMap.tour_team}');">와일드카드</a>
			</c:when>
	        </c:choose>
		</c:forEach>
        
        </div>

		<div style="position: relative;">

				<div class="search">
					<div>
						<span class="title ml_10">일자</span>
						<input type="date" id="crawSDate" name="sDate" >
						<span class="title ml_10">시간</span>
						<select id="crawSTime" name="sTime">
							<option value="">시간 선택</option>
							<c:forEach begin="0" end="23" var="hour">
								<option value="${hour < 10 ? '0' : ''}${hour}">${hour < 10 ? '0' : ''}${hour}</option>
							</c:forEach>
						</select>
						<select id="crawSMinute" name="sMinute">
							<option value="">분 선택</option>
							<c:forEach begin="0" end="50" var="minute">
								<option value="${minute < 10 ? '0' : ''}${minute}">${minute < 10 ? '0' : ''}${minute}</option>
							</c:forEach>
						</select>
						<button class="btn-large default" onclick="getAllPlayDataUp()">라인업 일괄 불러오기</button>
					</div>
				</div>
			<br />
		</div>


		<br />

        <div class="scroll">

		<form name="frm" id="frm" method="post"  action="save_cupMgrTourMatch" onsubmit="return false;">
            <input type="hidden" name="sFlag" value="6"> 
           <input type="hidden" name="mvFlag">
           <input type="hidden" name="ageGroup"  value="${ageGroup}">
           <input type="hidden" name="round"  value="${round}">
           <input type="hidden" name="trCnt" value="${fn:length(cupTourMatchResultList)}">
           <input type="hidden" name="cupId" value="${cupId}">
           <input type="hidden" name="round" value="${round}">

        <table id="frmTB"cellspacing="0" class="update over fixed">
          <colgroup>
            <col width="10%">
            <col width="5%">
            <col width="10%">
			<col width="15%">
			<col width="5%">
			<col width="5%">
			<col width="5%">
			<col width="5%">
			<col width="15%">
			<col width="12.5%">
			<col width="12.5%">
			<col width="5%">
			<col width="5%">
		    <%--<c:if test="${round ne 2}">--%>
			<col width="5%">
			<col width="5%">
			<col width="5%">
			<col width="5%">
			<col width="5%">
			<%--</c:if>--%>
          </colgroup>
          <thead>
            <tr>
              <th rowspan="2">경기일</th>
              <th rowspan="2">경기시간</th>
              <th rowspan="2">경기장소</th>
              <th colspan="3">홈팀</th>
              <th colspan="3">어웨이팀</th>
			  <th rowspan="2">결과</th>
              <!-- <th rowspan="2">사유</th> -->
              <th rowspan="2">영상</th>
              <th rowspan="2">관리</th>
			  <%--<c:if test="${round ne 2}">--%>
              <th rowspan="2">경기상태</th>
				<th rowspan="2">라인업</th>
				<th rowspan="2">라인업 알림</th>
				<th rowspan="2">득점 결과</th>
				<th rowspan="2">상세정보</th>
				<th rowspan="2">경기상태</th>
			  <%--</c:if>--%>
            </tr>
            <tr>
              <th>팀명</th>
              <th>점수</th>
              <th>PK</th>
              <th>PK</th>
              <th>점수</th>
              <th>팀명</th>
            </tr>
		  </thead>
		  <tbody id="tblmId">
          	<c:if test="${empty cupTourMatchResultList}">
				<tr class="item0">
					<td id="idEmptyList" colspan="13">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			
			<c:forEach var="result" items="${cupTourMatchResultList}" varStatus="status"> 
				<tr class="item${status.index}">
	              <td>
	              	${result.playdate}
	              	<input type="hidden" name="cupTourMatchId${status.index}" value="${result.cup_tour_match_id}">
	              </td>
				  <td>${result.ptime}</td>
				  <td>${result.place}</td>

	              <td class="tr">
	              	<c:if test="${result.home_id ne '-1'}">
					<a class="title" onclick="gotoTeamMgrDet('${result.home_id}');">
					</c:if>
						<c:choose>
	              		<c:when test="${result.home_type eq '0'}"><span>[학원] </span></c:when>
	              		<c:when test="${result.home_type eq '1'}"><span>[클럽] </span></c:when>
	              		<c:when test="${result.home_type eq '2'}"><span>[유스] </span></c:when> 
	              		</c:choose>
	              		${result.home}
	              	<c:if test="${result.home_id ne '-1'}">
					</a>
					</c:if>
						<c:choose>
		              	<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
		              	<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
		              	</c:choose>
				  </td>
	              <td>
	              	<input type="text" class="tc" <c:if test="${result.upd_flag eq 1 or result.upd_flag eq 2}">style="border: 1px solid red;"</c:if> name="homeScore${status.index}" value="${result.home_score }" placeholder="홈팀 점수 입력" autocomplete="off" ></td>
	              <td>
	              	<input type="text" class="tc" <c:if test="${result.upd_flag eq 2 or result.upd_flag eq 3}">style="border: 1px solid red;"</c:if> name="homePk${status.index}" value="${result.home_pk}" autocomplete="off" >
	              </td>
	              <td>
	              	<input type="text" class="tc" <c:if test="${result.upd_flag eq 2 or result.upd_flag eq 3}">style="border: 1px solid red;"</c:if> name="awayPk${status.index}" value="${result.away_pk}" autocomplete="off" >
	              </td>
	              <td>
	              	<input type="text" class="tc" <c:if test="${result.upd_flag eq 1 or result.upd_flag eq 2}">style="border: 1px solid red;"</c:if> name="awayScore${status.index}" value="${result.away_score }" placeholder="어웨이팀 점수 입력" autocomplete="off" >
	              </td>
	              <td class="tl">
						<c:choose>
		              	<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
		              	<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
		              	</c:choose>
		            <c:if test="${result.away_id ne '-1'}">
	              		<a class="title" onclick="gotoTeamMgrDet('${result.away_id}');">
	              	</c:if>
	              	${result.away}
					<c:choose>
	              	<c:when test="${result.away_type eq '0'}"><span>[학원] </span></c:when>
	              	<c:when test="${result.away_type eq '1'}"><span>[클럽] </span></c:when>
	              	<c:when test="${result.away_type eq '2'}"><span>[유스] </span></c:when> 
	              	</c:choose>
	              	<c:if test="${result.away_id ne '-1'}">
					</a>
					</c:if>
				  </td>
					<td class="tc">
						<select id="selMatchType${status.index}" name="selMatchType${status.index}">
							<option value="0" <c:if test="${result.match_type eq '0' }">selected</c:if>>정규</option>
							<option value="1" <c:if test="${result.match_type eq '1' }">selected</c:if>>승부차기</option>
							<option value="2" <c:if test="${result.match_type eq '2' }">selected</c:if>>홈팀부전승</option>
							<option value="3" <c:if test="${result.match_type eq '3' }">selected</c:if>>어웨이팀부전승</option>
							<option value="4" <c:if test="${result.match_type eq '4' }">selected</c:if>>홈팀몰수패</option>
							<option value="5" <c:if test="${result.match_type eq '5' }">selected</c:if>>어웨이팀몰수패</option>
							<option value="6" <c:if test="${result.match_type eq '6' }">selected</c:if>>홈팀실격패</option>
							<option value="7" <c:if test="${result.match_type eq '7' }">selected</c:if>>어웨이팀실격패</option>
						</select>
					</td>
	              	<%--
	              	<td>
	              	<input type="text" name="reason${status.index}" value="${result.reason}" placeholder="사유 입력"> 
				  	<c:choose>
				  	<c:when test="${ result.match_type eq 0}">
					  	<input type="text" class="gray" name="reason${status.index}" value="${result.reason}" readonly >
				  	</c:when>
				  	<c:otherwise>
					  	<input type="text" name="reason${status.index}" value="${result.reason}" placeholder="부전승 사유 입력">
				  	</c:otherwise>
				  	</c:choose>
				  	</td>
				  	 --%>
				  <td class="admin" style="font-size: 14px;">
	              	<a class="layer-pop" data-id="layer${status.index}-1">
	              	
	              	<c:choose>
	              	<c:when test="${fn:length(result.video_live) > 0}"><strong style="color: blue;">라</strong></c:when>
	              	<c:otherwise><font color="lightgray">라</font></c:otherwise>
	              	</c:choose>
	              	
	              	<%-- <c:choose>
	              	<c:when test="${fn:length(result.video_rep) > 0}"><strong style="color: blue;">다</strong></c:when>
	              	<c:otherwise><font color="lightgray">다</font></c:otherwise>
	              	</c:choose>
	              	
	              	<c:choose>
	              	<c:when test="${fn:length(result.video_high) > 0}"><strong style="color: blue;">하</strong></c:when>
	              	<c:otherwise><font color="lightgray">하</font></c:otherwise>
	              	</c:choose> --%>
	              	
	              	</a>


	              
					<div class="table-layer" id="layer${status.index}-1" style="width: 500px;">
					  <table cellspacing="0" class="update">
						<colgroup>
						  <col width="150px">
						  <%-- <col width="150px">
						  <col width="150px"> --%>
						  <col width="55px">
						</colgroup>
						<thead>
						  <tr>
							<th>생중계</th>
							<!-- <th>다시보기</th>
							<th>하이라이트</th> -->
							<th>관리</th>
						  </tr>
						</thead>
						<tbody>
						  <tr> 
							 <td><input type="text" name="live${status.index}" placeholder="생중계 주소 https://www.youtube.com/watch?v=mdq7BigWWOk" autocomplete="off" value="${result.video_live}"></td>
							<%-- <td><input type="text" name="rep${status.index}" placeholder="다시보기 주소" autocomplete="off" value="${result.video_rep}"></td> 
							<td><input type="text" name="high${status.index}" placeholder="하이라이트 주소" autocomplete="off" value="${result.video_high}"></td> --%>
							<td class="admin">
				                 <a class="btn-large default" onclick="gotoCupTourMatchVideo('${result.cup_tour_match_id}', '${status.index}');">수정</a>
				                 <a class="btn-large gray-o layer-close">취소</a>
							</td>
							</tr>
						</tbody>
					  </table>
					</div>
				  </td>
	              <td class="admin"><a class="layer-pop" data-id="layer${status.index}-2"><i class="xi-catched"></i></a>
						<div class="table-layer" id="layer${status.index}-2">
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
								  <input name="cupTourMatchId${status.index}" type="hidden" value="${result.cup_tour_match_id}">
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
				                  <c:forEach var="res1" items="${cupTeamList}" varStatus="status1">
					          	  <c:choose>
					          	  <c:when test="${result.home eq res1.nick_name}">
						          	<option value="${res1.team_id}" selected>${res1.nick_name}</option>
					          	  </c:when> 
					          	  <c:otherwise>
						          	<option value="${res1.team_id}">${res1.nick_name}</option>
					          	  </c:otherwise>
					          	  </c:choose>
					          	  </c:forEach>
				                </select>
				                <input name="home${status.index}" type="hidden" value="">
								</td>
								<td>
								 <select class="w100" id="selAway${status.index}" name="selAway${status.index}">
				                  <option value="-1" selected>어웨이팀 선택</option>
				                  <c:forEach var="res1" items="${cupTeamList}" varStatus="status1">
					          	  <c:choose>
					          	  <c:when test="${result.away eq res1.nick_name}">
						          	<option value="${res1.team_id}" selected>${res1.nick_name}</option>
					          	  </c:when> 
					          	  <c:otherwise>
						          	<option value="${res1.team_id}">${res1.nick_name}</option>
					          	  </c:otherwise>
					          	  </c:choose>
					          	  </c:forEach>
				                </select>
				                <input name="away${status.index}" type="hidden" value="">
								</td>
								<td class="admin">
					                 <a class="btn-large default" onclick="gotoCupTourMatch('${result.cup_tour_match_id}', '${status.index}', '${cupInfoMap.sdate1}', '${cupInfoMap.edate1}');">수정</a>
					                 <a class="btn-large gray-o layer-close">취소</a>
	                 
								</td>

								</tr>
							</tbody>
						  </table>
						</div>
					</td>
				  <td>
<%--				    <fmt:formatDate value="${result.parse_date}" var="formatDBDate" pattern="yyyy-MM-dd HH:mm:ss" />--%>
					<c:set var="now" value="<%=new java.util.Date()%>" />
					<fmt:parseDate value="${result.parse_date}" var="parsedDate" pattern="yyyy-MM-dd HH:mm" />
				    <fmt:formatDate value="${parsedDate}" var="dbTime" pattern="yyyy-MM-dd HH:mm" />
				    <fmt:formatDate value="${now}" var="currentTime" pattern="yyyy-MM-dd HH:mm" />

				    <%--<fmt:parseDate value="${result.parse_date}" var="parsedDate" pattern="yyyy-MM-dd HH:mm" />--%>

				    <c:choose>
						<c:when test="${result.time_diff > 0 && result.fix_flag eq 0}">
							<a class="btn-large default fixBtn" data-index${status.index}="" data-next-tour-no="${result.next_tour_no}"
							   data-next-tour-port="${result.next_tour_port}" data-home-score="${result.home_score}" data-away-score="${result.away_score}"
							   data-home-pk="${result.home_pk}" data-away-pk="${result.away_pk}"
							   onclick="fnfixRanking(${result.cup_tour_match_id}, '${result.home}', '${result.away}', '${result.home_id}', '${result.away_id}', '${pushEnd.type_id}', '${pushEnd.case_title}', '${pushEnd.case_text}')">경기종료</a>
						</c:when>
						<c:when test="${dbTime > currentTime}">
							경기 전
						</c:when>
						<c:otherwise>
							대진 확정
						</c:otherwise>
					</c:choose>
				  </td>
					<td>
						<c:choose>
							<c:when test="${result.match_status eq 'READY' }">
								<a class="btn-large default" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'LINEUP' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'END' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${result.send_flag eq '0'  and result.match_status ne 'READY'}">
								<a class="btn-large default" onclick="sendPush('LINEUP', '${result.cup_tour_match_id}', '${result.home}', '${result.away}', '${result.home_id}', '${result.away_id}', '${pushLineup.type_id}', '${pushLineup.case_title}', '${pushLineup.case_text}')">발송하기</a>
							</c:when>
							<c:otherwise>
								<a class="btn-large gray-o">발송하기</a>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${result.match_status eq 'READY' }">
								<a class="btn-large gray-o" id="playBtn_${status.index}">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'LINEUP' }">
								<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.cup_tour_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}', '${result.home_id}', '${result.away_id}');">불러오기</a>
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
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_tour_match_id}', '${ageGroup}');">상세정보</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_tour_match_id}', '${ageGroup}');">상세정보</a>
							</c:when>
							<c:when test="${result.match_status eq 'END' }">
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_tour_match_id}', '${ageGroup}');">상세정보</a>
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
        <div class="body-foot">
		  <a class="btn-large gray-o" onclick="gotoCupMgr();"><i class="xi-long-arrow-left"></i> 목록으로 이동 </a>
          <div class="others">
            <a class="btn-large default" onclick="gotoAddCupTourMatch();">경기결과 수정</a>
	            <%--<c:if test="${tourRankCount eq 0}">
	            	<c:if test="${cupInfoMap.cup_type ne 3 && cupInfoMap.cup_team eq rankCount}">
		            <a class="btn-large default" onclick="gotoAddCupTourRank();">최종순위 확정</a>
	            	</c:if>
	            	
	            	<c:if test="${cupInfoMap.cup_type eq 3}">
		            <a class="btn-large default" onclick="gotoAddCupTourRank();">최종순위 확정</a>
	            	</c:if>
	            </c:if>
	            <c:if test="${tourRankCount > 0}">
		            <a class="btn-large default" onclick="gotoDelCupTourRank();">최종순위 삭제</a>
	            </c:if>--%>

            <c:if test="${cupInfoMap.cup_type ne 3}">
            <a class="btn-large gray-o" onclick="gotoCupSubMatchResult('${cupInfoMap.cup_id}');">예선 결과 이동</a>
            </c:if>
            
            <c:if test="${cupInfoMap.cup_type eq 1}">
            <a class="btn-large gray-o" onclick="gotoCupMainMatchResult('${cupInfoMap.cup_id}');">본선 결과 이동</a>
            </c:if>
            <a class="btn-large default" onclick="getAllMatchScore('${cupInfoMap.sdate1}');">경기결과 일괄 불러오기</a>
          	
<%-- 
          	<a class="btn-large default" onclick="gotoDelCupTeam(2);">일정 일괄 삭제</a>
          	
          	<c:choose>
          	<c:when test="${fn:length(cupTeamList) eq 0 }">
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(0);">예선결과 등록</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">예선결과 등록 후 팀 생성</a>
          	</c:when>
          	<c:otherwise>
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(1);">예선결과 수정</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(1);">예선결과 수정 후 팀 생성</a>
          	
          	</c:otherwise>
          	</c:choose>
--%>
          </div>
        </div>
        
       </div>

		  <br>
		  <div class="title">
			  <h3 class="w50">
				  <c:choose>
					  <c:when test="${empty cupSubMatchRankByFinal}">현재순위</c:when>
					  <c:otherwise>최종순위</c:otherwise>
				  </c:choose>
				  <c:if test="${modify ne null and modify ne ''}">
					  <strong style="color: red; margin-left: 20px;">순위확정을 하세요</strong>
				  </c:if>
			  </h3>
		  </div>

	  <form name="rankfrm" id="rankfrm" method="post"  action="save_cupMgrTourMatchRank" onsubmit="return false;">
		  <input name="ageGroup" type="hidden" value="${ageGroup}">
		  <input type="hidden" name="trCnt" value="${fn:length(cupTourMatchRank)}">
		  <input type="hidden" name="cupId" value="${cupId}">
		  <input type="hidden" name="sFlag" value="">
		  <input type="hidden" name="cupType" value="${cupInfoMap.cup_type}">
		  <input type="hidden" name="thisRound" value="${round}">
		  <input type="hidden" name="teamType" value="${teamType}">

		  <table cellspacing="0" class="update over fixed">
			  <colgroup>
				  <col width="5%">
				  <col width="5%">
				  <col width="11%">
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
				  <th>엠블럼</th>
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
				  <th>관리</th>
			  </tr>
			  </thead>
			  <tbody>



			  <c:if test="${empty cupTourMatchRank}">
				  <tr class="item0">
					  <td id="idEmptyList" colspan="11">등록된 내용이 없습니다.</td>
				  </tr>
			  </c:if>

			  <c:if test="${!empty cupTourMatchRank}">
				  <c:forEach var="result" items="${cupTourMatchRank}" varStatus="status">
					  <tr>
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
						  <td>
							  <a class="title" onclick="gotoTeamMgrDet('${result.team_id}');">
								  ${result.team}
							  </a>
							  <input name="cupResultId${status.index}" type="hidden" value="${result.cup_result_id}">
							  <input name="teamId${status.index}" type="hidden" value="${result.team_id}">
						  </td>
						  <td>
							  ${result.playTotalCnt}
							  <input name="playTotalCnt${status.index}" type="hidden" value="${result.playTotalCnt}">
						  </td>
						  <td><input name="rankPoint${status.index}" class="tc" type="text" value="${result.rankPoint}" maxlength="3" id="${result.team_id}"></td>
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
							  <input name="losePoint${status.index}" type="hidden" value="${result.losePoint}">
						  </td>
						  <td>
							  ${result.goalPoint}
							  <input name="goalPoint${status.index}" type="hidden" value="${result.goalPoint}">
						  </td>
						  <td>
							  <c:set var="sRound" value=""/>
							  <c:choose>
								  <c:when test="${fn:contains(result.rResult, '강')}">
									  <c:set var="sRound" value="${fn:replace(result.rResult, '강', '')}"/>
								  </c:when>
							  </c:choose>
							  <c:choose>
								  <c:when test="${round ne '2'}">
							  		<input type="checkbox" name="resultType${status.index}" id="resultType${status.index}" value="${result.round}" <c:if test="${sRound eq round}">checked</c:if>><label for="resultType${status.index}">탈락</label>
								  </c:when>
								  <c:otherwise>
									  <input type="checkbox" name="resultType${status.index}" id="resultType${status.index}" value="${result.round}" <c:if test="${result.rResult eq '준우승'}">checked</c:if>><label for="resultType${status.index}">탈락</label>
								  </c:otherwise>
							  </c:choose>
						  </td>

					  </tr>
				  </c:forEach>
			  </c:if>

			  </tbody>
		  </table>
	  </form>
	  <div class="body-foot">
		  <a class="btn-large gray-o" onclick="gotoCupMgr();"><i class="xi-long-arrow-left"></i> 목록으로 이동 </a>
		  <div class="others">
			  <a class="btn-large default" onclick="gotoAddCupTourMatch();">경기결과 수정</a>
			  <c:if test="${tourRankCount eq 0}">
				  <c:if test="${cupInfoMap.cup_type ne 3 && cupInfoMap.cup_team eq rankCount}">
					  <a class="btn-large default" onclick="gotoAddCupTourRank();">최종순위 확정</a>
				  </c:if>

				  <c:if test="${cupInfoMap.cup_type eq 3}">
					  <a class="btn-large default" onclick="gotoAddCupTourRank();">최종순위 확정</a>
				  </c:if>
			  </c:if>
			  <c:if test="${tourRankCount > 0}">
				  <a class="btn-large gray-o" onclick="gotoDelCupTourRank();">최종순위 삭제</a>
			  </c:if>

			  <c:if test="${cupInfoMap.cup_type ne 3}">
				  <a class="btn-large gray-o" onclick="gotoCupSubMatchResult('${cupInfoMap.cup_id}');">예선 결과 이동</a>
			  </c:if>

			  <c:if test="${cupInfoMap.cup_type eq 1}">
				  <a class="btn-large gray-o" onclick="gotoCupMainMatchResult('${cupInfoMap.cup_id}');">본선 결과 이동</a>
			  </c:if>

			  <%--
							<a class="btn-large default" onclick="gotoDelCupTeam(2);">일정 일괄 삭제</a>

							<c:choose>
							<c:when test="${fn:length(cupTeamList) eq 0 }">
							  <a class="btn-large default" onclick="gotoAddLeagueTeam(0);">예선결과 등록</a>
							  <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">예선결과 등록 후 팀 생성</a>
							</c:when>
							<c:otherwise>
							  <a class="btn-large default" onclick="gotoAddLeagueTeam(1);">예선결과 수정</a>
							  <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(1);">예선결과 수정 후 팀 생성</a>

							</c:otherwise>
							</c:choose>
			  --%>
		  </div>
	  </div>
       
       
       <%--<form name="rankfrm" id="rankfrm" method="post"  action="save_cupMgrTourMatchRank" onsubmit="return false;">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input type="hidden" name="trCnt" value="${fn:length(cupTourMatchRank)}">
		<input type="hidden" name="cupId" value="${cupId}">
		<input type="hidden" name="sFlag" value="">
		<input type="hidden" name="cupType" value="${cupInfoMap.cup_type}">
		<input type="hidden" name="thisRound" value="${round}">
        
       <table cellspacing="0" class="update" style="display: block;">
       <!-- <table cellspacing="0" class="update" > --> 
          <colgroup>
			<col width="*">
			<col width="*">
            <col width="*">
            <col width="55px">
            <col width="*">
            <col width="*">
            <col width="*">
            <col width="*">
            <col width="*">
          </colgroup>
          <thead>
            <tr>
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
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
          
          <c:forEach var="result" items="${cupTourMatchRank}" varStatus="status"> 
              <tr>
				<td>
					<c:choose>
	              	<c:when test="${result.team_id eq '-1'}"><span class="label red" style="min-width: 55px!important;">매칭 오류</span></c:when>
	              	<c:when test="${result.team_type eq '0'}"><span class="label blue">학원</span></c:when>
	              	<c:when test="${result.team_type eq '1'}"><span class="label green">클럽</span></c:when>
	              	<c:when test="${result.team_type eq '2'}"><span class="label red">유스</span></c:when> &lt;%&ndash; 유스일경우?? &ndash;%&gt;
	              	</c:choose>
				</td>
                <td>
                	${result.team}
                	<input name="teamId${status.index}" type="hidden" value="${result.team_id}">
                	<input name="cupResultId${status.index}" type="hidden" value="${result.cup_result_id}">
                </td>
                <td>
                	${result.playTotalCnt}
                	<input name="playTotalCnt${status.index}" type="hidden" value="${result.playTotalCnt}">
               	</td>
                <td><input name="rankPoint${status.index}" class="tc" type="text" value="${result.rankPoint}"></td>
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
                	<input name="losePoint${status.index}" type="hidden" value="${result.losePoint}">
               	</td>
                <td>
                	${result.goalPoint}
                	<input name="goalPoint${status.index}" type="hidden" value="${result.goalPoint}">
               	</td>
                <td>
                	${result.round}
					<input name="resultType${status.index}" type="hidden" value="${result.round}" >
                </td>
                
              </tr>
             </c:forEach>
	        
          </tbody>
        </table> 
        </form>--%>
        
       
      </div>
    </div>
        
        
        
  <div>
</body>

<form name="cmgrfrm" id="cmgrfrm" method="post"  action="cupMgr">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="cmgrsrfrm" id="cmgrsrfrm" method="post"  action="cupMgrSubMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>  

<form name="cmgrmrfrm" id="cmgrmrfrm" method="post"  action="cupMgrMainMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>  
  
<form name="cmgrtmfrm" id="cmgrmfrm" method="post"  action="cupMgrTourMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="${cupId}">
  <input name="round" type="hidden" value="">
</form>  


<form name="cifrm" id="cifrm" method="post"  action="cupInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>  

<form name="cpfrm" id="cpfrm" method="post"  action="cupPrize">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form> 

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

<form name="delFrm" id="delFrm" method="post"  action="save_cupTeam" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="2">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="cupId" type="hidden" value="${cupId}">
   <input type="hidden" name="months" value="${months}">
</form>  


<form name="videofrm" id="videofrm" method="post"  action="save_cupMgrTourMatchVideo" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="1">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="round" type="hidden" value="${round}">
   <input name="cupId" type="hidden" value="${cupId}">
   <input name="cupTourMatchId" type="hidden" value="">
   <input name="live" type="hidden" value="">
   <input name="reply" type="hidden" value="">
   <input name="highlight" type="hidden" value="">
</form>


<form name="fixfrm" id="fixfrm" method="post"  action="save_cupMgrTourMatch">
  <input type="hidden" name="round" value="${round}">
  <input type="hidden" name="sFlag" value="1">
  <input type="hidden" name="ageGroup"  value="${ageGroup}">
  <input type="hidden" name="cupId"  value="${cupId}">
  <input type="hidden" name="cupTourMatchId"  value="" placeholder="대회토너먼트경기일정 아이디">
  <input type="hidden" name="matchDate"  value="" placeholder="경기일 - 예) 20190301">
  <input type="hidden" name="place"  value="" placeholder="경기장소">
  <input type="hidden" name="home"  value="" placeholder="홈팀">
  <input type="hidden" name="homeId"  value="" placeholder="홈팀 아이디">
  <input type="hidden" name="homeScore"  value="" placeholder="홈팀 점수">
  <input type="hidden" name="homePk"  value="" placeholder="홈팀 pk점수">
  <input type="hidden" name="away"  value="" placeholder="어웨이팀">
  <input type="hidden" name="awayId"  value="" placeholder="어웨이팀 아이디">
  <input type="hidden" name="awayScore"  value="" placeholder="어웨이팀 점수">
  <input type="hidden" name="awayPk"  value="" placeholder="어웨이팀 pk점수">
  <input type="hidden" name="matchType"  value="" placeholder="경기결과">
  <input type="hidden" name="reason"  value="">
</form>

</html>