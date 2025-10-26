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
});

//연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.lfrm.submit();
}

//리그 리스트 이동
function gotoLeague() {
	 document.lfrm.submit();
}

//리그 기본 정보 이동 
function gotoLeagueInfo(idx) {
    document.lifrm.submit();
}

//리그 참가팀 정보 이동 
function gotoLeagueTeam(idx) {
    document.ltfrm.submit();
}

//리그 경기일정 정보 이동 
function gotoLeagueMatch(month) {
	$('input[name=months]').val(month);
    document.lmfrm.submit();
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
	szHtml += "<input name='newTap"+trCnt+"' type='hidden' value='1'>";
	szHtml += "</td>";
	szHtml += "<td><input type='text' name='time"+trCnt+"' placeholder='경기시간 - 예)14:00'> </td>";
	szHtml += "<td><input type='text' name='place"+trCnt+"' class='w100' placeholder='경기장소'> </td>";
	
	szHtml += "<td>";
	szHtml += "<select name='selHome"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>홈팀 선택</option>";
				for (var i = 0; i < _arLeagueTeamList.length; i++) {
					szHtml += "<option value="+_arLeagueTeamList[i].teamId+">"+_arLeagueTeamList[i].nickName+"</option>";
				}
	szHtml += "</select>";
	szHtml += "<input name='home"+trCnt+"' type='hidden' value=''>";
	szHtml += "</td>";
	szHtml += "<td>";
	szHtml += "<select name='selAway"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>어웨이팀 선택</option>";
				for (var i = 0; i < _arLeagueTeamList.length; i++) {
					szHtml += "<option value="+_arLeagueTeamList[i].teamId+">"+_arLeagueTeamList[i].nickName+"</option>";
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
let delClick = 0;
//리그 경기일정 rows 삭제 
function removeTeamMatch(index, leagueMatchId){
	$(".item"+index).remove();//tr 테그 삭제
	console.log('--  [removeTeamMatch] frm tr lng : ' + $("#frmTB > tr").length +', index : '+ index );
	
	if($("#frmTB > tbody tr").length == 0){
		var szHtml = "<tr class='item0'><td id='idEmptyList' colspan='6'>등록된 내용이 없습니다.</td></tr>";
		$('#tblmId').append(szHtml);
	}
    /*else{
		resetRowDataOrder();
	}*/

    let str = "<input type='hidden' name='delTab"+delClick+"' value=" + leagueMatchId + " />"

    $(".delDiv").append(str);
    delClick++;
    $("input[name=delCnt]").val(delClick);
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


//리그 경기일정 정보 등록
function gotoAddLeagueMatch(sFlag){
	console.log('-- [gotoAddLeagueTeam] trCnt : ' + $('#frmTB > tbody tr').length);
	console.log('-- [gotoAddLeagueTeam] item0 td cnt : '+ $('.item0 td').length);
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	$('input[name=sFlag]').val(sFlag);
	if(formCheck('frm')){
		setContentsData();
		document.frm.submit();
	}
}

function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);

    $form.find('input:text').each(
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



function setContentsData(){

    const tblmId = $("#tblmId");

    $("input[name=trCnt]").val(tblmId[0].children.length);

	$(document).find("input[name^=date]").each(
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
	
	$(document).find("select[name^=selHome]").each(
		function(key) {
			var $obj = $(this);
			//$("#selTeam1 option:selected").text(); //$('select[name=selTeam1]').val();
			var text = $('select[name='+$obj.attr('name')+'] option:selected').text().trim();
			console.log('----------[select selHome] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
			$('input[name=home'+key+']').val(text);
		}
	);
	$(document).find("select[name^=selAway]").each(
		function(key) {
			var $obj = $(this);
			var text = $('select[name='+$obj.attr('name')+'] option:selected').text().trim();
			console.log('----------[select selAway] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
			$('input[name=away'+key+']').val(text);
		}
	);
}


//경기일정 일괄 삭제 
function gotoDelLeagueMatch(){
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
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

const fnDownload = () => {
    var leagueId = "${leagueId}";
    var ageGroup = "${ageGroup}";

    let jsonData = {
        'leagueId': leagueId,
        'ageGroup': ageGroup,
        'excelFlag': 'League'
    };

    let excelUrl = "excelDownload";
    let request = new XMLHttpRequest();

    request.open('POST', excelUrl, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.responseType = 'blob';

    request.onload = function(e) {

        $("#page-loading").hide();

        let filename = "";
        let disposition = request.getResponseHeader('Content-Disposition');
        if (disposition && disposition.indexOf('attachment') !== -1) {
            let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            let matches = filenameRegex.exec(disposition);
            if (matches != null && matches[1])
                filename = decodeURI( matches[1].replace(/['"]/g, '') );
        }
        //console.log("FILENAME: " + filename);

        if (this.status === 200) {

            let blob = this.response;
            if(window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, filename);
            }else{
                let downloadLink = window.document.createElement('a');
                let contentTypeHeader = request.getResponseHeader("Content-Type");
                downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                downloadLink.download = filename;
                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);
            }

            //setTimeout(() => $('#pop-proof-1').modal('hide'), 3000);
        }

    };

    request.send(JSON.stringify(jsonData));

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
  		  	<h2><span></span>리그정보 > 등록/수정</h2>
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
	          	<c:otherwise>
	          		<option value="${i}">${i}</option>
	          	</c:otherwise>
	          	</c:choose> 
          	</c:forEach>
            <!-- <option>2020</option>
            <option>2019</option>
            <option>2018</option>
            <option>2017</option>
            <option>2016</option>
            <option>2015</option>
            <option>2014</option> -->
          </select>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="sfrm" id="sfrm" method="post"  action="league" onsubmit="return false;">
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
          	<a class="btn-large gray-o" onclick="gotoLeague();"><i class="xi-long-arrow-left"></i> 리그등록 리스트</a>
          </div>
        </div>
        
        <div class="title">
          <h3>
            기본정보 
            <a class="btn-open open-1-o ac" data-id="open-1"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-1-c" data-id="open-1"><i class="xi-caret-up-circle-o"></i></a>
            <a onclick="gotoLeagueInfo(${leagueId});" class="btn-large gray-o">수정</a>
          </h3>
        </div>
        <div class="open-area" id="open-1">
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
                	${leagueInfoMap.sdate} ~ ${leagueInfoMap.edate}
                </td>
                <!-- <th>참가팀</th>
                <td class="tl">10팀</td> -->
              </tr>
              
            </tbody>
          </table>
        </div><br>
        </div>
       
        <div class="title">
          <h3 class="w50">
            리그 참가팀
            <a class="btn-open open-2-o ac" data-id="open-2"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-2-c" data-id="open-2"><i class="xi-caret-up-circle-o"></i></a>
            <a class="btn-large gray-o" onclick="gotoLeagueTeam(${leagueId});">수정</a>
          </h3>
        </div>
        <div class="open-area" id="open-2">
          <div class="scroll">
          <table cellspacing="0" class="update">
            <colgroup>
              <col width="10%">
              <col width="10%">
              <col width="15%">
              <col width="15%">
              <col width="*">
            </colgroup>
            <thead>
              <tr>
                <th>앰블럼</th>
                <th>구분</th>
                <th>사용명칭</th>
                <th>정식명칭</th>
                <th class="tl">소재지</th>
              </tr>
            </thead>
            <tbody>
              <c:if test="${empty leagueTeamList }">
				<tr class="item0">
					<td id="idEmptyList" colspan="5">등록된 내용이 없습니다.</td>
				</tr>
			  </c:if>	
              <c:forEach var="result" items="${leagueTeamList}" varStatus="status"> 
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
                <td>${result.nick_name}</td>
                <td>${result.team_name}</td>
                <td>${result.addr}</td>
              </tr>
              </c:forEach>
              
            </tbody>
          </table>
          </div><br>
        </div> 
       
        
         <div class="title">
          <h3 class="w100">
            리그 경기일 일괄 등록
          </h3>
        </div>
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="leagueMatch">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input name="leagueId" type="hidden" value="${leagueId }">
		<input name="leagueName" type="hidden" value="${leagueInfoMap.league_name}">
        <table cellspacing="0" class="update">
          <tbody>
            <tr>
              <th>엑셀 파일 등록</th>
              <td class="tl">
                <input type="file" id="excelFile" name="excelFile">
              </td>
              <td class="tr">
                <a class="btn-large default" onclick="fnDownload();">리그일정 다운로드</a>
				<a class="btn-large gray-o" onclick="gotoAddExcel();" >리그일정 일괄 등록</a>
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
        	<a class="btn-large w6 btn-tab" data-id="num0" onclick="gotoLeagueMatch();">전체</a>
        	<c:forEach var="i" begin="1" end="12">
        		<a class="btn-large w6 btn-tab" data-id="num${i}" onclick="gotoLeagueMatch(${i});">${i}월</a>
        	</c:forEach>
        </div>

        <div class="scroll">
        <form name="frm" id="frm" method="post"  action="save_leagueMatch" onsubmit="return false;">
           <input type="hidden" name="sFlag">
           <input type="hidden" name="ageGroup" value="${ageGroup}">
           <input type="hidden" name="trCnt" value="${fn:length(leagueMatchList)}">
           <input type="hidden" name="leagueId" value="${leagueId}">
           <input type="hidden" name="leagueName"  value="${leagueInfoMap.league_name}">
           <input type="hidden" name="months" value="${months}">
            <div class="delDiv">
                <input type="hidden" name="delCnt" value="0">
            </div>
        <table id="frmTB" cellspacing="0" class="update over">
          <colgroup>
            <col width="15%">
            <col width="15%">
            <col width="*%">
            <col width="15%">
            <col width="15%">
            <col width="55px">
          </colgroup>
          <thead>
            <tr>
              <th>경기일</th>
              <th>경기시간</th>
              <th>경기장소</th>
              <th>홈팀</th>
              <th>어웨이팀</th>
              <th>관리 <a class="btn-pop" data-id="find-club" onclick="gotoTeamMatch();"><i class="xi-plus-circle-o"></i></a></th>
            </tr>
          </thead>
          <tbody id="tblmId">
          	<c:if test="${empty leagueMatchList }">
				<tr class="item0">
					<td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${leagueMatchList}" varStatus="status"> 
			<tr class="item${status.index}">
				<td> 
					<input type="text" name="date${status.index}" value="${result.pdate}" class="datepicker" maxlength="8" autocomplete="off">
					<input name="pdate${status.index}" type="hidden" value="">
					<input name="leagueMatchId${status.index}" type="hidden" value="${result.league_match_id}">
					<input name="matchType${status.index}" type="hidden" value="${result.match_type}">
				 </td>
				 <td><input type="text" name="time${status.index}" value="${result.ptime}"></td>
				 <td><input type="text" name="place${status.index}" value="${result.place}" class="w100"></td>
				<td>
	                <select class="w100" id="selHome${status.index}" name="selHome${status.index}">
	                  <option value="-1" selected>미정</option>
	                  <c:forEach var="res1" items="${leagueTeamList}" varStatus="status1">
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
	                  <option value="-1" selected>미정</option>
	                  <c:forEach var="res1" items="${leagueTeamList}" varStatus="status1">
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
				  <%-- <a class="btn-pop" data-id="find-club" onclick="gotoTeamMatch(${status.index});"><i class="xi-plus-circle-o"></i> --%>
				  <a onclick="removeTeamMatch(${status.index}, '${result.league_match_id}');"><i class="xi-close-circle-o"></i></a>
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
          <a class="btn-large gray-o" onclick="gotoLeague();"><i class="xi-long-arrow-left"></i> 목록으로 이동 </a>
          <div class="others">
          		<c:choose>
          		<c:when test="${fn:length(leagueMatchList) eq 0 }">
		            <a class="btn-large default" onclick="gotoAddLeagueMatch(0);">일정 등록 완료</a>
          		</c:when>
          		<c:otherwise>
		            <a class="btn-large default" onclick="gotoAddLeagueMatch(1);">일정 수정 완료</a>
          		</c:otherwise>
          		</c:choose>
          		<a class="btn-large default" onclick="gotoDelLeagueMatch(2);">일정 일괄 삭제</a>
          </div>
        </div>
        </div>
	     
        
      </div>
	  </div>
</body>

<form name="lfrm" id="lfrm" method="post"  action="league">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cp" type="hidden" value="${cp}">
    <input name="sYear" type="hidden" value="${sYear}">
    <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form>

<form name="lmfrm" id="lmfrm" method="post"  action="leagueMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cp" type="hidden" value="${cp}">
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

<form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="${leagueId}">
</form>  

<form name="delFrm" id="delFrm" method="post"  action="save_leagueMatch" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="2">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="leagueId" type="hidden" value="${leagueId}">
   <input type="hidden" name="months" value="${months}">
</form>  
 
</html>