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

var _cupTeamCount = "${fn:length(cupTeamList)}";
var _cupType = "${cupInfoMap.cup_type}";

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
});

//대회 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.cfrm.submit();
}

//대회 리스트 이동
function gotoCup() {
	document.cfrm.submit();
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





//대회 참가팀 일괄 삭제 
function gotoDelCupTeam(){
	if( _cupTeamCount == 0 ){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		document.delFrm.submit();
	}
}

//대회 팀등록 
function gotoAddCupTeam(sFlag){
	/* 
	console.log('-- [gotoAddLeagueTeam] trCnt : ' + $('#frmTB > tbody tr').length);
	console.log('-- [gotoAddLeagueTeam] item0 td cnt : '+ $('.item0 td').length);
	if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
		alert('알림! \n리그 팀정보를 등록해 주세요.');
		return;
	}
	 */
	$('input[name=sFlag]').val(sFlag);
	document.frm.submit();
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

//리그 팀정보 등록 후 경기일정 이동
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


function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	if($('input:radio[name=pFlag]').is(':checked') == false) {
		//console.log('----------[formCheck] radio id : '+ $obj.attr('name')+', val : '+$obj.val());
		alert("알림!\n 활성여부 선택 하세요.");
	
		valid = false;
		return false;
	}
    
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
				case 'cupName' :
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
	var cupName = $("input[name=sCupName]").val();
	
	if(area < 0 && isEmpty(cupName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}
    
    if(valid == false){
   		return false;
   	} 
    
    if(!isEmpty(cupName) && isRegExp(cupName) ){
    	alert('알림!\n'+ '['+$("input[name=sCupName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
    	$("input[name=sCupName]").focus();
		valid = false;
		return false;
    }
    
    
    if(area < 0 && !isEmpty(cupName)){
    	$("select[name=sArea]").val('');
	}
    
    return valid;
}



//학원/클럽  
function gotoTeam(tdCnt){
	$('#find-club').show();
	clearTeamPopup();
	if(tdCnt != null || tdCnt != ''){
		$('input[name=index]').val(tdCnt);
	}
	
	$('input[name=stNickName]').focus();
}

//학원/클럽 팝업 초기화 
function clearTeamPopup(){
	var szHtml = "";
	$('input[name=index]').val('');
	$('input[name=trId]').val('');
	$('input[name=tdId]').val('');
	$('input[name=stNickName]').val('');
	$("#idTd").html('');
	szHtml += "<td id='idEmptyList' colspan='4'>등록된 내용이 없습니다.</td>";
	
	$('#idTd').append(szHtml);
}



//토너먼트가 아닐 때 팀등록 위치 지정 
function gotoTeamByTarget(trId, tdId){
	$('#find-club').show();
	clearTeamPopup();
	
	console.log("trId:"+trId+", tdId:"+tdId);
	$('#addFrm input[name=trId]').val(trId);
	$('#addFrm input[name=tdId]').val(tdId);
/* 	
	if(tdCnt != null || tdCnt != ''){
		$('input[name=index]').val(tdCnt);
	}
	
	$('input[name=stNickName]').focus();
 */
 }




//학원/클럽 찾기 
function gotoTeamSearch(){
	if(checkTeamSearchForm()){
		var index = $('#addFrm input[name=index]').val();
		var nickName = $('#addFrm input[name=stNickName]').val();
		var trId = $('#addFrm input[name=trId]').val();
		var tdId = $('#addFrm input[name=tdId]').val();
		var sFlag = "${sFlag}";
		var ageGroup = "${ageGroup}";
		var jsonData = {
				"sFlag":sFlag,
				"index":index,
				"trId":trId,
				"tdId":tdId,
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
			
			if(_cupType == 3){ 	//대회유형이 토너먼트일 경우 리그와 동일한방식으로 참가팀등록
				szHtml += "<td><a class='btn-close-pop title' onclick='setCupTeamContents(\""+contentJson[i].team_id+"\",\""+contentJson[i].team_name+"\",\""+contentJson[i].nick_name+"\",\""+contentJson[i].team_type+"\",\""+contentJson[i].addr+"\",\""+contentJson[i].emblem+"\",\""+jsonParam.jsonData.index+"\");'>"+contentJson[i].nick_name+"</a></td>";
			}else{
				szHtml += "<td><a class='btn-close-pop title' onclick='setCupTeamContents2(\""+contentJson[i].team_id+"\",\""+contentJson[i].nick_name+"\",\""+contentJson[i].team_type+"\",\""+jsonParam.jsonData.trId+"\",\""+jsonParam.jsonData.tdId+"\",\""+jsonParam.jsonData.index+"\");'>"+contentJson[i].nick_name+"</a></td>";
			}
			
			szHtml += "<td>"+contentJson[i].addr+"</td>";
			szHtml += "</tr>";
	}
	$('#idTd').append(szHtml);
}

function setCupTeamContents2(teamId, nickName, teamType, trId, tdId, index){
	console.log('-- [setCupTeamContents2] nickName :'+ nickName+', teamId : '+ teamId+', teamType : '+ teamType+', trId : '+ trId+', tdId : '+ tdId);
	$("#find-club").hide();
	
	addContentsHtml(teamId, nickName, teamType, trId, tdId, index);
	
}

function addContentsHtml(teamId, nickName, teamType, trId, tdId, index){
	console.log('-- [addContentsHtml] trId :' + trId + ', tdId :' + tdId );
	
	resetTd(trId, tdId);
	
	var szHtml = "";
	
	szHtml += "<input name='nickName' type='hidden' value='"+nickName+"'>";
	szHtml += "<input name='teamId' type='hidden' value='"+teamId+"'>";
	szHtml += "<input name='groups' type='hidden' value='"+tdId+"'>";
	szHtml += "<input name='teams' type='hidden' value='"+(parseInt(trId, 10) + 1) +"'>";
	szHtml += "<a class='btn-pop title' data-id='modefy-school-single'>";
	
	if(teamType == 0){
		szHtml += "[학원]";
	}else if(teamType == 1){
		szHtml += "[클럽]";
	}else if(teamType == 2){
		szHtml += "[유스]";
	}
	
	szHtml += nickName;
	szHtml += "</a> <a class='co999' onclick='renewTd("+trId+", "+tdId+");'><i class='xi-close-circle-o'></i></a>";
	
	
	$("#tr"+trId+" #td"+tdId+"").append(szHtml);
	
}


function resetTd(trId, tdId){
	//console.log('-- [resetTd] trId :' + trId + ', tdId :' + tdId );
	
	$("#tr"+trId+" #td"+tdId+"").empty();
}


function renewTd(trId, tdId){
	console.log('-- [renewTd] trId :' + trId + ', tdId :' + tdId );
	
	resetTd(trId, tdId);
	
	var szHtml = "";
 	szHtml += "<a class='btn-pop' data-id='find-club' onclick='gotoTeamByTarget("+trId+", "+tdId+");'><i class='xi-plus-circle-o'></i></a>";  
	
	$("#tr"+trId+" #td"+tdId+"").append(szHtml);
	
}







function setCupTeamContents(teamId, teamName, nickName, teamType, addr, emblem, index){
	console.log('-- [setCupTeamContents] nickName :'+ nickName+', teamId : '+ teamId+', teamType : '+ teamType+', index : '+ index);
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





/* 임시이동  */

//예선 경기일정 등록으로 이동
function gotoAddCupSubMatch(cupId){
	$("input[name=cupId]").val(cupId);
	document.csmfrm.submit();
}


//토너먼트 등록으로 이동
function gotoAddCupTourMatch(cupId){
	$("input[name=cupId]").val(cupId);
	document.ctmfrm.submit();
}


/* function gotoDel(idx, emblem){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=teamId]').val(idx);
		$('input[name=emblem]').val(emblem);
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
  		  	<h2><span></span>대회정보 > 등록/수정</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
        <div class="others">
          <select>
            <option>2020</option>
            <option>2019</option>
            <option>2018</option>
            <option>2017</option>
            <option>2016</option>
            <option>2015</option>
            <option>2014</option>
          </select>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="sfrm" id="sfrm" method="post"  action="league" onsubmit="return false;">
          	  <input name="sFlag" type="hidden" value="${sFlag}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup}">
  			  
              <input type="text" name="sCupName" placeholder="대회명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoCup();"><i class="xi-long-arrow-left"></i> 대회등록 리스트</a>
          </div>
        </div>
        
        <div class="title">
          <h3>
            기본정보
            <a class="btn-open open-1-o ac" data-id="open-1"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-1-c" data-id="open-1"><i class="xi-caret-up-circle-o"></i></a>
            <a onclick="gotoCupInfo('${cupId}');" class="btn-large gray-o">수정</a>
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
                  <th>예선 조편성</th>
                  <td class="tl">${fn:split(cupInfoMap.sub_team_count, '/')[0]}조 ${fn:split(cupInfoMap.sub_team_count, '/')[1]}개팀</td>
                  <th>본선 조편성</th>
                  <td class="tl">${fn:split(cupInfoMap.main_team_count, '/')[0]}조 ${fn:split(cupInfoMap.main_team_count, '/')[1]}개팀</td>
                  <th>순위선정방식</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.rank_type eq '0'}">골득실</c:if>
                  	<c:if test="${cupInfoMap.rank_type eq '1'}">승자승</c:if>
                  </td>
                </tr>
                <tr>
                  <th>토너먼트 타입</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.tour_type eq '0'}">대진표</c:if>
                  	<c:if test="${cupInfoMap.tour_type eq '1'}">추첨제</c:if>
                  </td>
                  <th>토너먼트 팀수</th>
                  <td class="tl" colspan="3">${cupInfoMap.tour_team}팀</td>
                </tr>
              </tbody>
            </table>
          </div>
          <br>
        </div>
        
        <div class="title">
          <h3 class="w50">대회개요
          	<a class="btn-open open-2-o ac" data-id="open-2"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-2-c" data-id="open-2"><i class="xi-caret-up-circle-o"></i></a>
            <a onclick="gotoCupPrize('${cupId}');" class="btn-large gray-o">수정</a>
          </h3>
          <h3 class="w50 open-area" id="open-2-other">대회수상</h3>
        </div>
        <div class="open-area" id="open-2">
          <div class="scroll">
          <table cellspacing="0" class="update">
            <colgroup>
              <col width="50%">
              <col width="50%">
            </colgroup>
            <tbody>
       		 <tr>
       		 	<% pageContext.setAttribute("newLineChar", "\n"); %>
                <td class="tl lh-160">
	                <c:if test="${empty cupInfoMap.cup_info}">등록된 내역이 없습니다.</c:if>
	                ${fn:replace(cupInfoMap.cup_info, newLineChar, "<br/>")}
                </td>
                <td class="tl lh-160">
	                <c:if test="${empty cupInfoMap.cup_prize}">등록된 내역이 없습니다.</c:if>
	                ${fn:replace(cupInfoMap.cup_prize, newLineChar, "<br/>")}
                </td>
              </tr>
            </tbody>
          </table>
          </div><br>
        </div>
        
        
        <div class="title">
          <h3 class="w100">
          	<c:choose>
        	<c:when test="${cupInfoMap.cup_type eq 3 }">
            참가팀 일괄 등록
        	</c:when>
        	<c:otherwise>
            참가팀 및 조편성 일괄 등록
        	</c:otherwise>
        	</c:choose>
          </h3>
        </div>
        
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="cupTeam">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input name="cupId" type="hidden" value="${cupId}">
 		<input name="teamColumn" type="hidden" value="${cupInfoMap.group_count}"> 
 		<input type="hidden" name="cupType" value="${cupInfoMap.cup_type}"> <!-- 0:예선+토너먼트,  1:예선+본선+토너먼트, 2: 풀리그, 3: 토너먼트 -->
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
        
        <div class="title">
          <h3 class="w100">
         	<c:choose>
        	<c:when test="${cupInfoMap.cup_type eq 3 }">
            참가팀 등록
        	</c:when>
        	<c:otherwise>
            참가팀 및 조편성 등록
        	</c:otherwise>
        	</c:choose>
          </h3>
        </div>
        
<!--         <div class="show-area"  id="num1"> -->
        <div id="num1">
        <div class="scroll">
        
        <form name="frm" id="frm" method="post"  action="save_cupTeam" onsubmit="return false;">
           <input type="hidden" name="sFlag">
           <input type="hidden" name="mvFlag">
           <input type="hidden" name="ageGroup"  value="${ageGroup}">
           <input type="hidden" name="trCnt" value="${fn:length(cupTeamList)}">
           <input type="hidden" name="cupId" value="${cupId}">
           <input type="hidden" name="cupName" value="${cupInfoMap.cup_name}">
           <input type="hidden" name="cupType" value="${cupInfoMap.cup_type}"> <!-- 0:예선+토너먼트,  1:예선+본선+토너먼트, 2: 풀리그, 3: 토너먼트 -->
        
        
        <c:choose>
        <c:when test="${cupInfoMap.cup_type eq 3 }">  <!-- 토너먼트 -->
        	 <table id="frmTB"cellspacing="0" class="update">
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
			  <tbody>
			  	<tbody id="tblmId">
	          	<c:if test="${empty cupTeamList }">
					<tr class="item0">
						<td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
						<!-- <td> <a class="btn-pop" data-id="find-club" onclick="gotoTeam();"><i class="xi-plus-circle-o"></i></td> -->
					</tr>
				</c:if>
				<c:forEach var="result" items="${cupTeamList}" varStatus="status"> 
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
		              	<c:when test="${result.team_type2 eq '0'}"><span class="label blue">학원</span></c:when>
		              	<c:when test="${result.team_type2 eq '1'}"><span class="label green">클럽</span></c:when>
		              	<c:when test="${result.team_type2 eq '2'}"><span class="label red">유스</span></c:when> <%-- 유스일경우?? --%>
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
			  	
			  </tbody>
	        </table>
        
        
        </c:when>
        <c:otherwise>
        	  <table id="frmTB"cellspacing="0" class="update">
	          <colgroup>
	            <col width="55px">
	
	            <c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
	            <col width="*">
	            </c:forEach>
	            
	          </colgroup>
	          <thead>
	            <tr>
	              <th></th>
	              
	              <c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
	              <th>${i}조</th>
	              </c:forEach>
	              
	            </tr>
			  </thead>
			  <tbody>
			  
				<c:set var="teamRowData" value="가|나|다|라|마|바|사|아|자|차|카|타|파|하" /> <!-- 참가팀순번 정렬 -->
				<c:set var="teamRow" value="${fn:split(teamRowData, '|')}"/>
				
				<c:set var="cnt" value="0" /> <!-- count -->
				<c:set var="tdNum" value="0" /> <!-- count -->
	          
	
				<c:forEach var="team_count" begin="0" step="1" end="${cupInfoMap.team_count-1}" varStatus="status"> <!-- ROW 조별 팀수 ex)4팀-->
				  	<tr id="tr${status.index}">
						
						<c:forEach var="res1" items="${teamRow}" varStatus="index1">
							<c:if test="${index1.index == team_count }">
								<td>${res1} 팀</td> 
							</c:if>
						</c:forEach>
	
				  		
						<c:forEach var="group_count" begin="0" step="1" end="${cupInfoMap.group_count-1}"> <!-- CELL 조 갯수 -->
				  			<c:choose>
					  		<c:when test="${empty cupTeamList}">
				  				<td id="td${group_count + 1}" class="input2">
					  				<a class="btn-pop" data-id="find-club" onclick="gotoTeamByTarget('${status.index}', '${group_count + 1}');"><i class="xi-plus-circle-o"></i></a>
					  			</td>
					  		
					  		</c:when>
					  		
					  		<c:when test="${fn:length(cupTeamList) <= cnt }"> <!-- 리스트는 존재하지만 해당 조/자리에 팀등록이 비어있음 -->
			  					<c:set var="cnt" value="${cnt + 1 }" />
			  					<c:set var="tdNum" value="${tdNum + 1 }" />
					  			
					  			<td id="td${tdNum }" class="input2">
					  				<a class="btn-pop" data-id="find-club" onclick="gotoTeamByTarget('${status.index}', '${group_count + 1}');"><i class="xi-plus-circle-o"></i></a>
					  			</td>
					  		</c:when>
					  		
					  		<c:otherwise>
					  			<c:forEach var="result" items="${cupTeamList}" varStatus="status" begin="${cnt }" end="${cnt }">
					  				<c:set var="cnt" value="${cnt + 1 }" />
					  				<c:set var="tdNum" value="${tdNum + 1 }" />
	
					  				<c:if test="${group_count+1 eq result.groups }">
							  			<td id="td${tdNum }" class="input2"> 
						  					<a class="btn-pop title" data-id="modefy-school-single">
						  						
						  						<c:if test="${result.team_id eq '-1'}">[매칭오류]</c:if>
						  						<c:if test="${result.team_id ne '-1'}">[${result.team_type}]</c:if>
						  					
						  						${result.nick_name }</a>
						  					<a class="co999"><i class="xi-close-circle-o"></i></a>
						  				</td>
					  				</c:if> 
	
					  				<c:if test="${group_count+1 ne result.groups }">
					  				<c:set var="cnt" value="${cnt - 1 }" />
						  				<td id="td${tdNum }" class="input2">
							  				<a class="btn-pop" data-id="find-club" onclick="gotoTeam(${tdNum});"><i class="xi-plus-circle-o"></i></a>
							  			</td>
					  				</c:if>
				  			 
					  			</c:forEach>
					  		</c:otherwise>
					  		</c:choose>
						</c:forEach>
	
	
				  	</tr>
				  	
				</c:forEach>
	 
	 
			  </tbody>
	        </table>
        </c:otherwise>
        </c:choose>
        
        <%-- 
        <c:if test="${cupInfoMap.cup_type ne 3 }">
        
        
        <table id="frmTB"cellspacing="0" class="update">
          <colgroup>
            <col width="55px">

            <c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
            <col width="*">
            </c:forEach>
            
          </colgroup>
          <thead>
            <tr>
              <th></th>
              
              <c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
              <th>${i}조</th>
              </c:forEach>
              
            </tr>
		  </thead>
		  <tbody>
		  
			<c:set var="teamRowData" value="가|나|다|라|마|바|사|아|자|차|카|타|파|하" /> <!-- 참가팀순번 정렬 -->
			<c:set var="teamRow" value="${fn:split(teamRowData, '|')}"/>
			
			<c:set var="cnt" value="0" /> <!-- count -->
			<c:set var="tdNum" value="0" /> <!-- count -->
          

			<c:forEach var="team_count" begin="0" step="1" end="${cupInfoMap.team_count-1}" varStatus="status"> <!-- ROW 조별 팀수 ex)4팀-->
			  	<tr id="tr${status.index}">
			  		
			  		<c:forEach var="res1" items="${teamRow}" varStatus="index1">
			           	<c:if test="${index1.index == team_count }">
							<td>${res1} 팀</td> 
						</c:if>
					</c:forEach>

			  		
					<c:forEach var="group_count" begin="0" step="1" end="${cupInfoMap.group_count-1}"> <!-- CELL 조 갯수 -->
			  			
			  			<c:choose>
				  		<c:when test="${empty cupTeamList}">
			  				<td id="td${tdNum }" class="input2">
				  				<a class="btn-pop" data-id="find-club" onclick="gotoTeam(${tdNum});"><i class="xi-plus-circle-o"></i></a>
				  			</td>
				  		
				  		</c:when>
				  		
				  		<c:when test="${fn:length(cupTeamList) <= cnt }">
		  					<c:set var="cnt" value="${cnt + 1 }" />
		  					<c:set var="tdNum" value="${tdNum + 1 }" />
				  			
				  			<td id="td${tdNum }" class="input2">
				  				<a class="btn-pop" data-id="find-club"><i class="xi-plus-circle-o"></i></a>
				  			</td>
				  		</c:when>
				  		
				  		<c:otherwise>
				  			<c:forEach var="result" items="${cupTeamList}" varStatus="status" begin="${cnt }" end="${cnt }">
				  				<c:set var="cnt" value="${cnt + 1 }" />
				  				<c:set var="tdNum" value="${tdNum + 1 }" />

				  				<c:if test="${group_count+1 eq result.groups }">
						  			<td id="td${tdNum }" class="input2"> ${cnt } | ${result.groups } | ${group_count }
					  					<a class="btn-pop title" data-id="modefy-school-single">[${result.team_type}] ${result.nick_name }</a>
					  					<a class="co999"><i class="xi-close-circle-o"></i></a>
					  				</td>
				  				</c:if> 

				  				<c:if test="${group_count+1 ne result.groups }">
				  				<c:set var="cnt" value="${cnt - 1 }" />
					  				<td id="td${tdNum }" class="input2">
						  				<a class="btn-pop" data-id="find-club" onclick="gotoTeam(${tdNum});"><i class="xi-plus-circle-o"></i></a>
						  			</td>
				  				</c:if>
			  			 
				  			</c:forEach>
				  		</c:otherwise>
				  		</c:choose>
					</c:forEach>


			  	</tr>
			  	
			</c:forEach>
 
 
		  </tbody>
        </table>
        </c:if>
         --%>
        </form>
        
        </div>
        <div class="body-foot">
          <div class="others">
          	<a class="btn-large default" onclick="gotoDelCupTeam(2);">참가팀 일괄 삭제</a>
          	
          	<c:choose>
          	<c:when test="${fn:length(cupTeamList) eq 0 }">
	            <a class="btn-large default" onclick="gotoAddCupTeam(0);">참가팀 등록</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">참가팀 등록 후 일정등록</a>
          	</c:when>
          	<c:otherwise>
	            <a class="btn-large default" onclick="gotoAddCupTeam(1);">참가팀 수정</a>
<!-- 	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(1);">참가팀 수정 후 일정등록</a> -->

<!-- 임시이동 -->
				<c:if test="${cupInfoMap.cup_type ne 3 }" >
	            <a class="btn-large gray-o" onclick="gotoAddCupSubMatch('${cupInfoMap.cup_id}');">예선 일정등록 이동</a>
				</c:if>
				<c:if test="${cupInfoMap.cup_type eq 3 }" >
	            <a class="btn-large gray-o" onclick="gotoAddCupTourMatch('${cupInfoMap.cup_id}');">토너먼트 일정등록 이동</a>
				</c:if>
          	
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
			<form id="addFrm" onsubmit="return false;">
			  <input type="hidden" name="index">
			  <input type="hidden" name="trId">
			  <input type="hidden" name="tdId">
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
  	
  	
	<div class="pop" id="modefy-school-single">
		<div style="height:auto;">
		  <div style="height:auto;">
			<div class="head">
			  학원/클럽 개별 수정
			</div>
			<div class="body" style="padding:15px 20px;">
			  <ul class="signup-list">
				<li class="title">
				  <span class="title">사용명칭</span><input type="text" placeholder="사용명칭 입력 (예: 전주영생고)" value="전주영생고">
				</li>
				<li class="title">
				  <span class="title">정식명칭</span><input type="text" placeholder="정식명칭 입력 (예: 전주영생고등학교)" value="전주영생고등학교">
				</li>
				<li class="title">
				  <span class="title">구분</span>
				  <input type="radio" name="name" id="school" checked><label class="w20" for="school">학원</label>
				  <input type="radio" name="name" id="club"><label class="w20" for="club">클럽</label>
				  <input type="radio" name="name" id="youth"><label class="w20" for="youth">유스</label>
				</li>
				<li class="title">
				  <span class="title">지역선택</span>
				  <select style="width:50%;">
					 <option>광역 선택</option>
					<option>서울/인천</option>
					<option>중부/강원</option>
					<option>경기</option>
					<option>호남/제주</option>
					<option>영남</option>
					<option>전국</option>
				  </select>
				  <a class="btn-pop" data-id="update-area-add"><i class="xi-cog" style="line-height: 28px;"></i></a>
				</li>
				<li class="title">
				  <span class="title">소재지</span>
				  <input type="text" placeholder="소재지 입력 (예: 서울특별시 마포구 성산동)" value="전라북도 전주시 완산구 효자동3가">
				</li>
				<li class="title">
				  <span class="title">앰블럼</span>
				  <input type="file" style="width:50%;">
				  <a class="img-del"><span>기존파일</span><i class="xi-close"></i></a>
				</li>
			  </ul>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;"><span>등록하기</span></a>
			  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
			</div>
		  </div>
		</div>
	  </div>
    
    <!--팝업 끝-->          
        
        
  <div>
</body>

<form name="cfrm" id="cfrm" method="post"  action="cup">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
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



<!-- 임시이동 -->
<form name="csmfrm" id="csmfrm" method="post"  action="cupSubMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>   

<form name="ctmfrm" id="ctmfrm" method="post"  action="cupTourMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form> 

</html>