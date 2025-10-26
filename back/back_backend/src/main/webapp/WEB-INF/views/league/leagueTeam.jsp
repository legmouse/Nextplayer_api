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
	
	console.log('-- [init] trCnt : ' + $('#frmTB > tbody tr').length);
	console.log('--- item0 td cnt : '+ $('.item0 td').length);
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

//리그 기본정보 
function gotoLeagueInfo(idx) {
    document.ltfrm.submit();
}


//리그 학원/클럽  
function gotoTeam(trCnt){
	$('#find-club').show();
	clearTeamPopup();
	if(trCnt != null || trCnt != ''){
		$('input[name=index]').val(trCnt);
	}
	
	$('input[name=stNickName]').focus();
}

//학원/클럽 팝업 초기화 
function clearTeamPopup(){
	var szHtml = "";
	$('input[name=index]').val('');
	$('input[name=stNickName]').val('');
	$("#idTd").html('');
	szHtml += "<td id='idEmptyList' colspan='4'>등록된 내용이 없습니다.</td>";
	
	$('#idTd').append(szHtml);
}


function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				*/
				switch ($obj.attr('name')){
				case 'nickName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'teamName' :
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
    
    if($('input:radio[name=teamType]').is(':checked') == false) {
		//console.log('----------[formCheck] radio id : '+ $obj.attr('name')+', val : '+$obj.val());
		alert("알림!\n 구분 선택 하세요.");
	
		valid = false;
		return false;
	}
    
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


function searchTeamCallback(contentJson, errorMsg, jsonParam) {
	console.log("ajax recv json: " + JSON.stringify(contentJson) + ", jsonParam : "+  JSON.stringify(jsonParam));
	
	var code = contentJson.code;
	console.log('--- code : '+ code);
	
	if(code == 0){
		console.log('-------------- ajax 정상 ');
	
		setTeamContentsPopup(contentJson.teamList, jsonParam);
		
	}else if(code == 98){
		alert('알림!\n 잘못된 정보 입니다. \n 확인 후 다시 이용해 주세요.');
	}else{
		alert('알림!\n ['+code+'] 코드 오류 입니다. \n 확인 후 다시 이용해 주세요.');
	}
	
}

function setTeamContentsPopup(contentJson, jsonParam){
	console.log('--- contentJson.length : '+ contentJson.length+', '+contentJson[0].area_name +', index : '+jsonParam.jsonData.index);
	var szHtml = "";
	$("#idTd").html('');
	
	for (var i = 0; i < contentJson.length; i++) {
		    szHtml += "<tr>";
			szHtml += "<td>"+contentJson[i].area_name+"</td>";
			var teamType = contentJson[i].team_type;
			if(teamType == 0){
				szHtml += "<td>학원</td>";
				
			}else if(teamType == 1){
				szHtml += "<td>클럽</td>";
				
			}else if(teamType == 2){
				szHtml += "<td>유스</td>";
				
			}   
			szHtml += "<td><a class='btn-close-pop title' onclick='setLeagueTeamContents(\""+contentJson[i].team_id+"\",\""+contentJson[i].team_name+"\",\""+contentJson[i].nick_name+"\",\""+contentJson[i].team_type+"\",\""+contentJson[i].addr+"\",\""+contentJson[i].emblem+"\",\""+jsonParam.jsonData.index+"\");'>"+contentJson[i].nick_name+"</a></td>";
			szHtml += "<td>"+contentJson[i].addr+"</td>";
			szHtml += "</tr>";
	}
	$('#idTd').append(szHtml);
}

function setLeagueTeamContents(teamId, teamName, nickName, teamType, addr, emblem, index){
	console.log('-- [setLeagueTeamContents] nickName :'+ nickName+', teamId : '+ teamId+', teamType : '+ teamType+', index : '+ index);
	$("#find-club").hide();
	rowAddContentsHtml(teamId, teamName, nickName, teamType, addr, emblem, index);
	
	
}

function rowAddContentsHtml(teamId, teamName, nickName, teamType, addr, emblem, index){
	var trCnt = $("#frmTB > tbody tr").length;
	var item0Td = $('.item0 td').length;
	console.log('-- [rowAddContentsHtml] trCnt : ' + trCnt  );
	console.log('--- item0 td cnt : '+ $('.item0 td').length);
	if(item0Td == 1){
		$("#tblmId").html('');
		trCnt = 0; 
	} 

	if(index == null || index == ''){
		$('input[name=trCnt]').val(trCnt + 1);
	}else{
		$('input[name=trCnt]').val(trCnt);
	}
	
	
	var szHtml = "";
	
	if((index == null ||  index == '') ){
		szHtml += "<tr class='item"+trCnt+"'>";
	}
	szHtml += "<td><img src='/NP"+emblem+"' class='logo'></td>";
	szHtml += " <td>";
		if(teamType == 0){
			szHtml += "<span class='label blue'>학원</span>";
		}else if(teamType == 1){
			szHtml += "<span class='label green'>클럽</span>";
		}else if(teamType == 2){
			szHtml += "<span class='label red'>유스</span>";
		}
	szHtml += " </td>";
	szHtml += "<td><a class='btn-pop title' data-id='modefy-school-single'>"+nickName+"</a></td>";
	szHtml += "<td>"+teamName+"</td>";
	szHtml += " <td>"+addr+"</td>";
	szHtml += "<td class='admin'>";
		//추가 시 해당 리스트 바로 밑으로 생성
		if(index == null || index == ''){
			szHtml += "<a class='btn-pop' data-id='find-club' onclick='gotoTeam("+trCnt+");'><i class='xi-plus-circle-o'></a></i>";
			szHtml += "<a onclick='removeTeam("+trCnt+");'><i class='xi-close-circle-o'></i></a>";
			szHtml += "<input name='teamId"+trCnt+"' type='hidden' value='"+teamId+"'>";
			szHtml += "<input name='nickName"+trCnt+"' type='hidden' value='"+nickName+"'>";
		
		}else{
			szHtml += "<a class='btn-pop' data-id='find-club' onclick='gotoTeam("+index+");'><i class='xi-plus-circle-o'></a></i>";
			szHtml += "<a onclick='removeTeam("+index+");'><i class='xi-close-circle-o'></i></a>";
			szHtml += "<input name='teamId"+index+"' type='hidden' value='"+teamId+"'>";
			szHtml += "<input name='nickName"+index+"' type='hidden' value='"+nickName+"'>";
			
		}
		
	szHtml += "</td>";
	
	if((index == null ||  index == '') ){
		szHtml += "</tr> ";
	}
	
	if((index == null ||  index == '')){
		$('#tblmId').append(szHtml);
			/* // item 의 최대번호 구하기
            var newitem = $("#tblmId tr:eq(1)").html(szHtml);
            newitem.removeClass();
           
            newitem.addClass("item"+(parseInt(lastItemNo)+1)); 

            $("#tblmId").append(newitem);
            */

	}else {
		console.log('--  [rowFixContentsHtml] frm tr lng : ' + $("#frmTB > tr").length +', index : '+ index );
		/* $("#frmTB > tbody tr:eq("+index+")").html("");
		$("#frmTB > tbody tr:eq("+index+")").html(szHtml); */
		$(".item"+index).html('');
		$(".item"+index).html(szHtml);
	}
}

function removeTeam(index){
	$(".item"+index).remove();//tr 테그 삭제
	console.log('--  [removeTeam] frm tr lng : ' + $("#frmTB > tr").length +', index : '+ index );
	
	if($("#frmTB > tbody tr").length == 0){
		var szHtml = "<tr class='item0'><td id='idEmptyList' colspan='6'>등록된 내용이 없습니다.</td></tr>";
		$('#tblmId').append(szHtml);
	}else{
		resetRowDataOrder();
	}
}

//리그 팀정보 리스트 순서 재정의 
function resetRowDataOrder(){
	var trCnt = $('#frmTB > tbody tr').length;
	console.log('-- [resetTrOrder] trCnt : ' + trCnt);
	$("input[name=trCnt]").val(trCnt);
	for (var i = 0; i < trCnt; i++) {
		 $("#frmTB > tbody tr:eq("+i+")").attr('class', 'item'+i);
		 $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(0)").attr('onclick', 'gotoTeam(\''+i+'\')');
		 $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(1)").attr('onclick', 'removeTeam(\''+i+'\')');
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=teamId]").attr('name', 'teamId'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=nickName]").attr('name', 'nickName'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=leagueName]").attr('name', 'leagueName'+i);
		 
	}
}

//리그 팀정보 등록
function gotoAddLeagueTeam(sFlag){
	console.log('-- [gotoAddLeagueTeam] trCnt : ' + $('#frmTB > tbody tr').length);
	console.log('-- [gotoAddLeagueTeam] item0 td cnt : '+ $('.item0 td').length);
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n리그 팀정보를 등록해 주세요.');
		return;
	}
	
	$('input[name=sFlag]').val(sFlag);
	document.frm.submit();
}

//리그 팀정보 등록  후 경기일정 이동
function gotoAddLeagueMatch(sFlag){
	/* var trCnt = $('#frmTB > tbody tr').length;
	var tdCnt = $('#frmTB > tbody tr td').length;
	console.log('-- [resetTrOrder] trCnt : ' + trCnt +', tdCnt : '+ $('#frmTB > tbody tr td').length); */
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n리그 팀정보를 등록해 주세요.');
		return;
	}
	
	$('input[name=sFlag]').val(sFlag);
	$('input[name=mvFlag]').val('5');
	document.frm.submit();
}

//참가팀 일괄 삭제 
function gotoDelLeagueTeam(){
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
        </div>
        </div>
        
        <div class="body-foot">
          <div class="search">
          </div>
          <div class="others">
            <!-- <a class="btn-large default" onclick="gotoLeague();">리그생성</a>
            <a class="btn-large gray-o" href="31-03.리그정보-등록수정-참가팀등록.html">리그생성 후 팀 등록</a> -->
          </div>
        </div>
        
         <div class="title">
          <h3 class="w50">
            리그 참가팀 일괄 등록
          </h3>
        </div>
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="leagueTeam">
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
				<!-- <a class="btn-large default btn-show" data-id="num">참가팀 생성</a> -->
				<a class="btn-large default" onclick="gotoAddExcel();" >참가팀 일괄 등록</a>
				<!-- <a class="btn-large default btn-pop" data-id="update-leagueTeam-add"  >참가팀 일괄 등록</a> -->
				<a class="btn-large gray-o">엑셀 폼 다운로드</a>
              </td>
            </tr>
          </tbody>
        </table>
        </form>
        <br>
        
        <!-- 참가팀 리스트 -->
         <div class="title">
          <h3 class="w50">
            리그 참가팀 등록
          </h3>
        </div>
        <div  id="num">
        <div class="scroll">
        <form name="frm" id="frm" method="post"  action="save_leagueTeam" onsubmit="return false;">
           <input type="hidden" name="sFlag">
           <input type="hidden" name="mvFlag">
           <input type="hidden" name="ageGroup"  value="${ageGroup}">
           <input type="hidden" name="trCnt" value="${fn:length(leagueTeamList)}">
           <input type="hidden" name="leagueId" value="${leagueId}">
           <input name="leagueName" type="hidden" value="${leagueInfoMap.league_name}">
        <table id="frmTB" cellspacing="0" class="update over">
          <colgroup>
            <col width="10%">
            <col width="10%">
            <col width="15%">
            <col width="15%">
            <col width="*">
            <col width="10%">
          </colgroup>
          <thead>
            <tr>
              <th>앰블럼</th>
              <th>구분</th>
              <th>사용명칭</th>
              <th>정식명칭</th>
              <th class="tl">소재지</th>
              <th>관리 <a class="btn-pop" data-id="find-club" onclick="gotoTeam();"><i class="xi-plus-circle-o"></i></a></th>
            </tr>
          </thead>
          <tbody id="tblmId">
          	<c:if test="${empty leagueTeamList }">
				<tr class="item0">
					<td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
					<!-- <td> <a class="btn-pop" data-id="find-club" onclick="gotoTeam();"><i class="xi-plus-circle-o"></i></td> -->
				</tr>
			</c:if>
			<c:forEach var="result" items="${leagueTeamList}" varStatus="status"> 
			<tr class="item${status.index}">
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
				<td><a class="btn-pop title" data-id="modefy-school-single">${result.nick_name}</a></td>
				<td>${result.team_name}</td>
				<td>${result.addr}</td>
				<td class="admin">
				  <a class="btn-pop" data-id="find-club" onclick="gotoTeam(${status.index});"><i class="xi-plus-circle-o"></i>
				  <a onclick="removeTeam(${status.index});"><i class="xi-close-circle-o"></i></a>
				  <input name="teamId${status.index}" type="hidden" value="${result.team_id}">
				  <input name="nickName${status.index}" type="hidden" value="${result.nick_name}">
				  <input name="leagueName${status.index}" type="hidden" value="${result.league_name}">
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
          	<a class="btn-large default" onclick="gotoDelLeagueTeam(2);">참가팀 일괄 삭제</a>
          	
          	<c:choose>
          	<c:when test="${fn:length(leagueTeamList) eq 0 }">
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(0);">참가팀 등록</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">참가팀 등록 후 일정등록</a>
          	</c:when>
          	<c:otherwise>
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(1);">참가팀 수정</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(1);">참가팀 수정 후 일정등록</a>
          	
          	</c:otherwise>
          	</c:choose>
          </div>
        </div>
        </div>
	     
        
      </div>
	  </div>
	
	<!-- 학원/클럽 찾기 팝업 -->
	 <div class="pop" id="find-club">
	 <div style="height:auto;">
	  <div style="height:auto;">
		<div class="head">
		  학원/클럽 찾기
		</div>
		<!-- <form name="addFrm" id="addFrm" method="post" enctype="multipart/form-data" action="save_team"> -->
		<div class="body" style="padding:15px 20px;">
		  <div class="search">
			<form onsubmit="return false;">
			  <input type="hidden" name="index">
			  <input type="text" name="stNickName" placeholder="사용명칭 입력 (예: 전주영생고)" onkeydown="javascript:if(event.keyCode==13){gotoTeamSearch();}">
			  <i class="xi-search" onclick="gotoTeamSearch();" style="cursor:pointer"></i>
			</form>
		  </div>
		  <table cellspacing="0" class="update">
			<thead>
			  <tr>
				<th>광역</th>
				<th>구분</th>
				<th>팀명</th>
				<th>소재지</th>
			  </tr>
			</thead>
			<tbody id="idTd">
			  <tr>
				<td id="idEmptyList" colspan="4">등록된 내용이 없습니다.</td>
			  </tr>
			</tbody>
		  </table>
		</div>
		<!-- </form> -->
		<div class="foot">
		  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
		</div>
	  </div>
	</div>
  	</div>
    <!--팝업 끝-->  

</body>

<form name="lfrm" id="lfrm" method="post"  action="league">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cp" type="hidden" value="${cp}">
    <input name="sYear" type="hidden" value="${sYear}">
    <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form>

<form name="ltfrm" id="ltfrm" method="post"  action="leagueTeam">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cp" type="hidden" value="${cp}">
  <input name="leagueId" type="hidden" value="${leagueId}">
</form>  

<form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="${leagueId}">
</form>  

<form name="delFrm" id="delFrm" method="post"  action="save_leagueTeam" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="2">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="leagueId" type="hidden" value="${leagueId}">
   <input type="hidden" name="months" value="${months}">
</form>  

 
</html>