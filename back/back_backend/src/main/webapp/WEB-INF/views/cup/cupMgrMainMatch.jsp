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
<!-- ExcelJS 라이브러리 로드 -->
<script src="resources/js/exceljs.min.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

var _arCupTeamList = [<c:forEach var="item" items="${cupMainTeamList}" varStatus="status">
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
	var groups = "${groups}";
	var group_count = "${cupInfoMap.group_count}";
	
	console.log('--- groups :'+ groups + ", cupInfoMap.groups_count:" + group_count );

	$(".tabnum > a").removeClass("default");
	$(".tabnum > a").removeClass("gray-o");
	
	for (var i = 0; i <= group_count; i++) {
		if(i == groups-1){
			$(".tabnum > a:eq("+i+")").addClass("default");
			
		}else{
			$(".tabnum > a:eq("+i+")").addClass("gray-o");
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

//대회 토너먼트 결과 수정 이동
function gotoCupTourMatchResult(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
	document.cmgrtrfrm.submit();
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
function gotoAddCupMainMatch(){
	if(formCheck('frm')){
		//$('input[name=sFlag]').val(sFlag);
		document.frm.submit();
	}
}

//대회 본선 영상 등록/수정 
function gotoCupMainMatchVideo(cmIdx, index){

	var live = $("[name='live"+index+"']").val();
	var rep = $("[name='rep"+index+"']").val();
	var high = $("[name='high"+index+"']").val();
	console.log('--- gotoCupMainMatchVideo cmIdx :'+ cmIdx +', index : '+ index + ', live :'+ live +', rep : '+ rep  +', high : '+ high);
	
	$("#videofrm [name='cupMainMatchId']").val(cmIdx);
	$("#videofrm [name='live']").val(live);
	$("#videofrm [name='reply']").val(rep);
	$("#videofrm [name='highlight']").val(high);
	
	document.videofrm.submit();
}



//대회 본선 최종순위 확정 
function gotoAddCupMainRank(){
	if (confirm("본선 "+"${groups}"+"조 최종순위를 확정 하시겠습니까?")) {
		$('input[name=sFlag]').val('0');
		document.rankfrm.submit();
	}
}
//대회 본선 최종순위 수정 
function gotoFixCupMainRank(){
	if (confirm("본선 "+"${groups}"+"조 최종순위를 수정 하시겠습니까?")) {
		$('input[name=sFlag]').val('6');
		document.rankfrm.submit();
	}
}
//대회 본선 최종순위 삭제 
function gotoDelCupMainRank(){
	if (confirm("본선 "+"${groups}"+"조 최종순위를 삭제 하시겠습니까?")) {
		$('input[name=sFlag]').val('2');
		document.rankfrm.submit();
	}
}


function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
   	if(valid == false){
   		return false;
   	} 

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				*/
				switch ($obj.attr('name')){
				case 'leagueName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
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



//리그 경기일정 정보 이동 
function gotoCupMgrMainMatch(groups) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";
	$('input[name=groups]').val(groups);
  document.cmgrmmfrm.submit();
}

//리그 팀 경기일정  
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


const fnEndMatch = (matchId, home, away, homeId, awayId, typeId, title, body) => {

	const cupId = $("input[name=cupId]").val();
	const ageGroup = $("input[name=ageGroup]").val();

	const confirmMsg = confirm("경기 종료 처리 하시겠습니까?");

	if (confirmMsg) {
		const data = {
			cupId: cupId,
			cupMainMatchId: matchId + "",
			ageGroup: ageGroup
		}

		$.ajax({
			type: 'POST',
			url: '/cupEndMainMatch',
			data: JSON.stringify(data),
			dataType: "text",
			contentType: "application/json",
			success: function(data) {
				if (JSON.parse(data).data.stateCode === 200) {
					alert('종료 되었습니다.');
					location.reload();
				} else {
					alert('실패 했습니다.');
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



/* function gotoDel(idx, emblem){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=teamId]').val(idx);
		$('input[name=emblem]').val(emblem);
		document.delFrm.submit();
	}
} */

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
			$("#crawStatus").text("");
			downloadExcel(title, data);
		},
		error: function (request, status, error) {
			$("#crawStatus").text("");
			alert('에러가 발생했습니다. 다시 시도해주세요.');
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


const getPlayDataUp = (e, idx, fType, matchId, matchOrder, title, ageGroup) => {
	alert('불러오기 시작합니다.');
	$("#crawStatus").text("크롤링중...");
	const level = getLevelFromAgeGroup(ageGroup);
	const params = {
		title,
		matchType: 'MATCH',
		level,
		ageGroup,
		matchId : matchId,
		targetMatchOrder: matchOrder,
		cupType: 'MAIN',
		sDate: '${cupInfoMap.sdate1}',
		cupId: cupId
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
		success: function (data) {
			$("#crawStatus").text("");
			const homeScore = $("[name=homeScore" + idx + "]").val();
			const awayScore = $("[name=awayScore" + idx + "]").val();
			const homePkScore = $("[name=homePk" + idx + "]").val();
			const awayPkScore = $("[name=awayPk" + idx + "]").val();

			function gameResultDiff(msg) {
				if (homeScore != data.homeScore || awayScore != data.awayScore || data.homePenaltyScore != homePkScore || data.awayPenaltyScore != awayPkScore) {
					$("[name=homeScore" + idx + "]").val(data.homeScore);
					$("[name=awayScore" + idx + "]").val(data.awayScore);
					$("[name=homePk" + idx + "]").val(data.homePenaltyScore);
					$("[name=awayPk" + idx + "]").val(data.awayPenaltyScore);
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
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + matchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('" + matchId + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).removeClass('gray-o')
						$("#detailBtn_" + idx).addClass('default');
						let msg = '라인업을 불러왔습니다.';
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
						$("#playBtn_" + idx).attr("onclick", "getPlayDataUp(this,'" + idx + "', 'PLAY', '" + matchId + "', '" + matchOrder + "', '" + title + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('" + matchId + "', '" + ageGroup + "')");
						$("#detailBtn_" + idx).removeClass('gray-o')
						$("#detailBtn_" + idx).addClass('default');
						let msg = '게임이 진행중입니다.\n라인업과 진행중인 결과를 불러왔습니다.';
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
						$("#detailBtn_" + idx).attr("onclick", "goDetailLineup('" + matchId + "', '" + ageGroup + "')");
						let msg = '게임이 종료되었습니다.\n라인업과 종료된 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
						alert(msg);
					} else if (fType == 'PLAY') {
						$(e).removeClass('default')
						$(e).addClass('gray-o');
						$(e).attr('onclick', null);
						let msg = '게임이 종료되었습니다.\n종료된 결과를 불러왔습니다.';
						msg = gameResultDiff(msg);
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

const getAllPlayDataUp = () => {

	const sDate = $("#crawSDate").val();
	const sTime = $("#crawSTime option:selected").val();
	const sMinute = $("#crawSMinute option:selected").val();

	if (isEmpty(sDate) || isEmpty(sTime) || isEmpty(sMinute)) {
		alert("경기 일자를 선택 해주세요.");
		return false;
	}

	alert('불러오기 시작합니다.');
	$("#crawStatus").text("크롤링중...");
	const level = getLevelFromAgeGroup('${ageGroup}');

	const params = {
		title: '${cupInfoMap.cup_name_origin}',
		matchType: 'MATCH',
		level,
		ageGroup: '${ageGroup}',
		foreignId: '${cupId}',
		cupType: 'Main',
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
	location.href = '/cupMgrMainMatchPlayData?cupId=' + cupId + '&matchId=' + matchId + '&ageGroup=' + ageGroup;
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

//대회 경기 개별 수정
function gotoSaveCupMainMatchOne(idx, match_id){
	const date = $('input[name="date' + idx + '"]').val();
	const time = $('input[name="time' + idx + '"]').val();
	const place = $('input[name="place' + idx + '"]').val();
	const selHome = $('select[name="selHome' + idx + '"]').val();
	const selAway = $('select[name="selAway' + idx + '"]').val();
	const selHomeStr = $('select[name="selHome' + idx + '"] option:selected').text();
	const selAwayStr = $('select[name="selAway' + idx + '"] option:selected').text();
	
	if (date == null || date == '') {
		alert('경기일을 입력해주세요.');
		return false;
	}
	
	if (time == null || time == '') {
		alert('경기시간을 입력해주세요.');
		return false;
	}
	
	if (place == null || place == '') {
		alert('경기장소를 입력해주세요.');
		return false;
	}
	
	if (selHome == null || selHome == '' || selHome == -1) {
		alert('홈팀을 선택해주세요.');
		return false;
	}
	
	if (selAway == null || selAway == '' || selAway == -1) {
		alert('어웨이팀을 선택해주세요.');
		return false;
	}
	$('#mainMatchFrm input[name="cupMainMatchId"]').val(match_id);
	$('#mainMatchFrm input[name="date"]').val(date);
	$('#mainMatchFrm input[name="time"]').val(time);
	$('#mainMatchFrm input[name="place"]').val(place);
	$('#mainMatchFrm input[name="selHome"]').val(selHome);
	$('#mainMatchFrm input[name="selAway"]').val(selAway);
	$('#mainMatchFrm input[name="homeNickName"]').val(selHomeStr);
	$('#mainMatchFrm input[name="awayNickName"]').val(selAwayStr);
	document.mainMatchFrm.submit();
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
            matchId: id,
            matchType: 'SUB',
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
        <div class="others">
          
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
            본선 경기결과 일괄 등록
          </h3>
        </div>
        
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="cupSubMatch">
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
				  <span id="crawStatus" style="font-size: 25px;"></span>
				  <a class="btn-large blue-o"
					 onclick="getAddExcelDate('${cupInfoMap.cup_name_origin}', '${ageGroup}');">대회결과 엑셀
					  다운로드</a>
				<!-- <a class="btn-large default btn-show" data-id="num">참가팀 생성</a> -->
				<a class="btn-large default" onclick="gotoAddExcel();" >본선결과 일괄 등록</a>
				<!-- <a class="btn-large default btn-pop" data-id="update-leagueTeam-add"  >참가팀 일괄 등록</a> -->
				<a class="btn-large gray-o">엑셀 폼 다운로드</a>
              </td>
            </tr>
          </tbody>
        </table>
        </form>
        <br>
        
        <c:set var="groupData" value="가|나|다|라|마|바|사|아|자|차|카|타|파|하" /> <!-- 참가팀 본선 조 정렬 -->
		<c:set var="groupColumn" value="${fn:split(groupData, '|')}"/>
        
		<!-- 대회 경기일정 리스트 -->
        <div id="num1">
        <div class="tabnum" style="margin-bottom:20px;">
        <c:forEach var="i" items="${groupColumn}"  begin="0" step="1" end="${cupInfoMap.m_group_count-1}" varStatus="status">
        	<a class="btn-large w6 btn-tab" data-id="num${status.count}" onclick="gotoCupMgrMainMatch(${status.count});">${i}조</a>
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
        <form name="frm" id="frm" method="post"  action="save_cupMgrMainMatch" onsubmit="return false;">
           <input type="hidden" name="sFlag" value="6">
           <input type="hidden" name="mvFlag">
           <input type="hidden" name="ageGroup"  value="${ageGroup}">
           <input type="hidden" name="trCnt" value="${fn:length(cupMainMatchResultList)}">
           <input type="hidden" name="cupId" value="${cupId}">
           <input type="hidden" name="cupName" value="${cupInfoMap.cup_name}">
        
        
        <table id="frmTB"cellspacing="0" class="update fixed over">
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
			<col width="15%">
			<col width="10%">
			<col width="5%">
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
              <th colspan="3">홈팀</th>
              <th colspan="3">어웨이팀</th>
			  <th rowspan="2">결과</th>
              <!-- <th rowspan="2">사유</th> -->
              <th rowspan="2">영상</th>
              <th rowspan="2">관리</th>
              <th rowspan="2">경기관리</th>
				<th rowspan="2">라인업</th>
				<th rowspan="2">라인업 알림</th>
				<th rowspan="2">득점 결과</th>
				<th rowspan="2">상세정보</th>
				<th rowspan="2">경기상태</th>
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
          	<c:if test="${empty cupMainMatchResultList}">
				<tr class="item0">
					<td id="idEmptyList" colspan="13">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			
			<c:forEach var="result" items="${cupMainMatchResultList}" varStatus="status"> 
				<tr class="item${status.index}">
	              <td>
	              	${result.playdate}
	              	<input type="hidden" name="cupMainMatchId${status.index}" value="${result.cup_main_match_id}">
	              </td>
				  <td>${result.ptime}</td>
				  <td><span class="address">${result.place}</span></td>
	              
	              <td class="tr">
	              	<a class="title" onclick="gotoTeamMgrDet('${result.home_id}');">
	              	<c:choose>
	              	<c:when test="${result.home_type eq '0'}"><span>[학원] </span></c:when>
	              	<c:when test="${result.home_type eq '1'}"><span>[클럽] </span></c:when>
	              	<c:when test="${result.home_type eq '2'}"><span>[유스] </span></c:when> 
	              	</c:choose>
	              	${result.home}
					</a>
				  	<c:choose>
	              	<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	              	<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
	              	</c:choose>
				  </td>
	              <td><input type="text" class="tc" name="homeScore${status.index}" value="${result.home_score }" placeholder="홈팀 점수 입력"></td>
	              <td>
	              	<input type="text" class="tc" name="homePk${status.index}" value="${result.home_pk}" autocomplete="off" >
	              	<%--
	              	 <c:choose>
	              	<c:when test="${result.match_type eq 1 }">
	              		<input type="text" class="tc" value="${result.home_pk}">
	              	</c:when>
	              	<c:otherwise>
		              	<input type="text" class="gray tc" readonly>
	              	</c:otherwise>
	              	</c:choose> 
	              	--%>
	              </td>
	              <td>
	              	<input type="text" class="tc" name="awayPk${status.index}" value="${result.away_pk}" autocomplete="off" >
	              	<%-- 
	              	<c:choose>
	              	<c:when test="${result.match_type eq 1 }">
	              		<input type="text" class="tc" value="${result.away_pk}">
	              	</c:when>
	              	<c:otherwise>
		              	<input type="text" class="gray tc" readonly>
	              	</c:otherwise>
	              	</c:choose>
	              	 --%>
	              </td>
	              <td><input type="text" class="tc" name="awayScore${status.index}" value="${result.away_score }" placeholder="어웨이팀 점수 입력"autocomplete="off" ></td>
	              <td class="tl">
				  	<c:choose>
	              	<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	              	<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
	              	</c:choose>
					<a class="title" onclick="gotoTeamMgrDet('${result.away_id}');">
	              	${result.away}
	              	<c:choose>
	              	<c:when test="${result.away_type eq '0'}"><span>[학원] </span></c:when>
	              	<c:when test="${result.away_type eq '1'}"><span>[클럽] </span></c:when>
	              	<c:when test="${result.away_type eq '2'}"><span>[유스] </span></c:when> 
	              	</c:choose>
	              	</a>
				  </td>
				  <td class="tc">
					<select name="selMatchType${status.index}">
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
				  	<input type="text" name="reason${status.index}" value="${result.reason}" placeholder="사유 입력" autocomplete="off" > 
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
				                 <a class="btn-large default" onclick="gotoCupMainMatchVideo('${result.cup_main_match_id}', '${status.index}');">수정</a>
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
				                  <c:forEach var="res1" items="${cupMainMatchRank}" varStatus="status1">
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
				                  <c:forEach var="res1" items="${cupMainMatchRank}" varStatus="status1">
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
					                 <a class="btn-large default" onclick="gotoSaveCupMainMatchOne('${status.index}', '${result.cup_main_match_id}');">수정</a>
					                 <a class="btn-large gray-o layer-close">취소</a>
	                 
								</td>
								</tr>
							</tbody>
						  </table>
						</div>
					</td>
					<td>
						<c:set var="now" value="<%=new java.util.Date()%>" />
						<fmt:parseDate value="${result.parse_date}" var="parsedDate" pattern="yyyy-MM-dd HH:mm" />
						<fmt:formatDate value="${parsedDate}" var="dbTime" pattern="yyyy-MM-dd HH:mm" />
						<fmt:formatDate value="${now}" var="currentTime" pattern="yyyy-MM-dd HH:mm" />
						<c:choose>
							<c:when test="${result.time_diff > 0 && result.end_flag eq 0}">
								<a class="btn-large default fixBtn" onclick="fnEndMatch(${result.cup_main_match_id}, '${result.home}', '${result.away}', '${result.home_id}', '${result.away_id}', '${pushEnd.type_id}', '${pushEnd.case_title}', '${pushEnd.case_text}')">경기종료</a>
							</c:when>
							<c:when test="${dbTime > currentTime}">
								경기 전
							</c:when>
							<c:when test="${result.time_diff > 0 && result.end_flag eq 1}">
								종료
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${result.match_status eq 'READY' }">
								<a class="btn-large default" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'LINEUP' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'END' }">
								<a class="btn-large gray-o" onclick="getPlayDataUp(this, '${status.index}', 'LINEUP', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${result.send_flag eq '0' and result.match_status ne 'READY'}">
								<a class="btn-large default" onclick="sendPush('LINEUP', '${result.cup_main_match_id}', '${result.home}', '${result.away}', '${result.home_id}', '${result.away_id}', '${pushLineup.type_id}', '${pushLineup.case_title}', '${pushLineup.case_text}')">발송하기</a>
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
								<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large default" id="playBtn_${status.index}" onclick="getPlayDataUp(this, '${status.index}', 'PLAY', '${result.cup_main_match_id}','${result.match_order}', '${cupInfoMap.cup_name_origin}', '${ageGroup}');">불러오기</a>
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
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_main_match_id}', '${ageGroup}');">상세정보</a>
							</c:when>
							<c:when test="${result.match_status eq 'START' }">
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_main_match_id}', '${ageGroup}');">상세정보</a>
							</c:when>
							<c:when test="${result.match_status eq 'END' }">
								<a class="btn-large default" id="detailBtn_${status.index}" onclick="goDetailLineup('${result.cup_main_match_id}', '${ageGroup}');">상세정보</a>
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
          <div class="others">
          
            <c:choose>
          	<c:when test="${fn:length(cupMainTeamList) eq 0 }">
	            <a class="btn-large default" onclick="gotoAddCupMainMatch();">본선결과 등록</a>
	            <!-- <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">본선결과 등록 후 팀 생성</a> -->
          	</c:when>
          	<c:otherwise>
	            <a class="btn-large default" onclick="gotoAddCupMainMatch();">본선결과 수정</a>
	            <!-- <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(1);">본선결과 수정 후 팀 생성</a> -->
          	
          	</c:otherwise>
          	</c:choose>
          	            
            <a class="btn-large gray-o" onclick="gotoCupSubMatchResult('${cupInfoMap.cup_id}');">예선 결과 이동</a>
            <a class="btn-large gray-o" onclick="gotoCupTourMatchResult('${cupInfoMap.cup_id}');">토너먼트 결과 이동</a>

          	
<%-- 
          	<a class="btn-large default" onclick="gotoDelCupTeam(2);">일정 일괄 삭제</a>
          	
          	<c:choose>
          	<c:when test="${fn:length(cupMainTeamList) eq 0 }">
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
        
        
        <!-- 현재 순위 -->
        
        <br>
        <div class="title">
          <h3 class="w50">
            <c:choose>
            <c:when test="${empty cupMainMatchRankByFinal}">현재순위</c:when>
            <c:otherwise>최종순위</c:otherwise>
            </c:choose>
          </h3>
        </div>
        
        <form name="rankfrm" id="rankfrm" method="post"  action="save_cupMgrMainMatchRank" onsubmit="return false;"> 
           <input name="ageGroup" type="hidden" value="${ageGroup}">
           <input type="hidden" name="trCnt" value="${fn:length(cupMainMatchRank)}">
           <input type="hidden" name="cupId" value="${cupId}">
           <input type="hidden" name="groups" value="${groups}">
           <input type="hidden" name="sFlag" value="">
           <input type="hidden" name="cupType" value="${cupInfoMap.cup_type}">
           <input type="hidden" name="thisRound" value="본선">
           
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="9%">
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
			<col width="7%">
          </colgroup>
          <thead>
            <tr>
			  <th>순위</th>
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
              <th>와일드카드</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
          
          
          
          	<c:if test="${empty cupMainMatchRank}">
				<tr class="item0">
					<td id="idEmptyList" colspan="13">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
          
          	<c:if test="${empty cupMainMatchRankByFinal}">
          	<c:forEach var="result" items="${cupMainMatchRank}" varStatus="status"> 
              <tr>
                <td>
                	<select id="selRank${status.index}" name="selRank${status.index}">
                	<c:forEach var="i" begin="1" end="${cupInfoMap.m_team_count }" step="1">
	                  <option value="${i}"  <c:if test="${i eq status.count }"> selected</c:if> >${i}위</option>
                	</c:forEach>
	                </select>
                </td>
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
                <td class="admin">
                	<!-- <a class="btn-large gray">취소</a> -->
	                <a class="btn-large gray-o layer-pop" data-id="layer-o-1">등록</a>
	                <div class="table-layer" id="layer-o-1" style="width: 400px">
	                  <table cellspacing="0" class="update">
	                    <tbody>
	                      <tr>
	                        <td>토너먼트</td>
	                        <td>
	                          <select class="w100" readonly="">
	                            <option selected="">1경기</option>
	                          </select>
	                        </td>
	                        <td>
	                          <select class="w100" readonly="">
	                            <option selected="">어웨이팀</option>
	                          </select>
	                        </td>
	                        <td class="admin">
	                          <a class="btn-large default">등록</a>
	                          <a class="btn-large gray-o layer-close">취소</a>
	                        </td>
	                      </tr>
	                    </tbody>
	                  </table>
	                </div>
                	
                </td>
                <td>
					<input type="checkbox" name="resultType${status.index}" id="resultType${status.index}" value="4" <c:if test="${result.result eq '본선' }">checked</c:if>><label for="resultType${status.index}">탈락</label>
                </td>
                
              </tr>
             </c:forEach>
	        </c:if>
	        
          	<c:if test="${!empty cupMainMatchRankByFinal}">
          	<c:forEach var="result" items="${cupMainMatchRankByFinal}" varStatus="status"> 
              <tr>
                <td>
                	<select id="selRank${status.index}" name="selRank${status.index}">
                	<c:forEach var="i" begin="1" end="${cupInfoMap.team_count }" step="1">
	                  <option value="${i}"  <c:if test="${i eq result.confirmRank }"> selected</c:if> >${i}위</option>
                	</c:forEach>
	                </select>
                </td>
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
                	<input name="teamId${status.index}" type="hidden" value="${result.team_id}">
                	<input name="cupResultId${status.index}" type="hidden" value="${result.cup_result_id}">
                </td>
                <td>
                	${result.playTotalCnt}
                	<input name="playTotalCnt${status.index}" type="hidden" value="${result.playTotalCnt}">
               	</td>
                <td><input name="rankPoint${status.index}" class="tc" type="text" value="${result.rankPoint}" maxlength="3"></td>
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
                <td class="admin">
                	<!-- <a class="btn-large gray">취소</a> -->
	                <a class="btn-large gray-o layer-pop" data-id="layer-o-1">등록</a>
	                <div class="table-layer" id="layer-o-1" style="width: 400px">
	                  <table cellspacing="0" class="update">
	                    <tbody>
	                      <tr>
	                        <td>토너먼트</td>
	                        <td>
	                          <select class="w100" readonly="">
	                            <option selected="">1경기</option>
	                          </select>
	                        </td>
	                        <td>
	                          <select class="w100" readonly="">
	                            <option selected="">어웨이팀</option>
	                          </select>
	                        </td>
	                        <td class="admin">
	                          <a class="btn-large default">등록</a>
	                          <a class="btn-large gray-o layer-close">취소</a>
	                        </td>
	                      </tr>
	                    </tbody>
	                  </table>
	                </div>
                	
                </td>
                <td>
					<input type="checkbox" name="resultType${status.index}" id="resultType${status.index}" value="4" <c:if test="${result.result eq '본선' }">checked</c:if>><label for="resultType${status.index}">탈락</label>
                </td>
                
              </tr>
             </c:forEach>
	        </c:if>
	        
	        
          </tbody>
        </table>
        </form>
        <div class="body-foot">
          <div class="search">
            
          </div>
		  <a class="btn-large gray-o" onclick="gotoCupMgr();"><i class="xi-long-arrow-left"></i> 목록으로 이동 </a>
          <div class="others">
<!--             <a class="btn-large default" onclick="gotoCupSubRank();">최종순위 확정</a> -->
            
            
            <c:if test="${!empty cupMainMatchRank}">
            
            <c:choose>
            <c:when test="${empty cupMainMatchRankByFinal}">
            	<!-- 참가팀 수 만큼 예선순위 등록되지 않으면 본선 최종순위확정 미노출 -->
            	<c:if test="${cupInfoMap.cup_team eq rankCount}">
            	<a class="btn-large default" onclick="gotoAddCupMainRank();">최종순위 확정</a>
            	</c:if>
            </c:when>
            <c:otherwise>
            	<a class="btn-large default" onclick="gotoFixCupMainRank();">최종순위 수정</a>
            	<a class="btn-large default" onclick="gotoDelCupMainRank();">최종순위 삭제</a>
            </c:otherwise>
            </c:choose>
            
            </c:if>
          </div>
          
        </div>
        
        
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
  
<form name="cmgrmmfrm" id="cmgrmmfrm" method="post"  action="cupMgrMainMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="${cupId}">
  <input name="groups" type="hidden" value="">
</form>  

<form name="cmgrtrfrm" id="cmgrtrfrm" method="post"  action="cupMgrTourMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
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


<form name="videofrm" id="videofrm" method="post"  action="save_cupMgrMainMatchVideo" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="1">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="cupId" type="hidden" value="${cupId}">
   <input name="cupMainMatchId" type="hidden" value="">
   <input name="live" type="hidden" value="">
   <input name="reply" type="hidden" value="">
   <input name="highlight" type="hidden" value="">
</form>  

<form name="mainMatchFrm" id="mainMatchFrm" method="post" action="save_cupMgrMainMatchOne" onsubmit="return false;">
	<c:choose>
		<c:when test="${(ageGroup eq 'U12' || ageGroup eq 'U11') && (teamType ne null && teamType ne '')}">
			<input name="teamType" type="hidden" value="${teamType}">
		</c:when>
	</c:choose>
	<input type="hidden" name="sFlag" value="4">
	<input name="ageGroup" type="hidden" value="${ageGroup}">
	<input name="cupId" type="hidden" value="${cupId}">
	<input name="groups" type="hidden" value="${groups}">
	<input name="date" type="hidden" value="">
	<input name="time" type="hidden" value="">
	<input name="place" type="hidden" value="">
	<input name="selHome" type="hidden" value="">
	<input name="selAway" type="hidden" value="">
	<input name="cupMainMatchId" type="hidden" value="">
	<input name="homeNickName" type="hidden" value="">
	<input name="awayNickName" type="hidden" value="">
</form>

</html>