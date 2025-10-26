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
<script src="resources/jquery/jquery-ui2.js"></script>
<!-- <script src="resources/jquery/jquery-ui.js"></script> -->
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
	var sArea = "${sArea}";
	var sTeamType = "${sTeamType}";
	$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
	$("input[name=sNickName]").val("${sNickName}");
});

function gotoAgeGroup(ageGroup){
	  clearFrmData("frm");
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=cp]").val(1);
	  document.frm.submit();
}

//데이터 초기화
function gotoDataReset(){ 
	if (confirm("정말 데이터 초기 하시겠습니까?")) {
  		document.gotoResetFrm.submit();
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

//학원/클럽 등록 팝업 - 팝업 오픈
function showTeamPopup(){
	clearFrmData("addFrm");
	var ageGroup = "${ageGroup}";
	//console.log('-- showTeamPopupageGroup  : '+ ageGroup);
	 $('input[name=ageGroup]').val(ageGroup);
}

//학원/클럽 수정
function gotoModPopup(idx, areaName, teamType, nickName, teamName, addr, emblem, pId, pIdName ){
	clearFrmData("modFrm");
	//console.log('-- gotoModPopup idx : '+ idx);
	var ageGroup = "${ageGroup}";
	$('input[name=ageGroup]').val(ageGroup);
	$('input[name=nickName]').val(nickName);
	$('input[name=teamName]').val(teamName);
	$('input[name=viewNickName]').val(nickName);
	$('input[name=viewTeamName]').val(teamName);
	$('input[name=teamId]').val(idx);
	$('input[name=pId]').val(pId);
}

// 폼 input type 초기화
function clearFrmData(regxForm){
	//console.log('-- clearFrmData regxForm : '+ regxForm);
	var $form = $("#"+regxForm);
	$form[0].reset();
}

// 학원/클럽 개별 수정
function gotoMod(){
	if(formCheck("modFrm")){
	/*  var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val(); */
	 
	// $('input[name=sFlag]').val(1); //수정
	// $('input[name=uage]').val(uage); 
	 
	/*  console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.modFrm.submit();
	}
}

function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	/* if (isEmpty($form.find('input[name="ㅛㄷㅁ"]').val())) {
		alert('알림!\n[데이터 변경날짜]를 입력 하세요.');
		$form.find('input[name="startDate"]').focus();
		valid = false;
		return false;
	} */
    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
 			/* if(isEmpty($obj.val())) {
				console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				
				switch ($obj.attr('name')){
				case 'newNickName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'newTeamName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
				}

			
			}else{ */
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
				
			/* } */
		}
	);
    
    if(valid == false){
   		return false;
   	} 
    
   	if(valid == false){
   		return false;
   	} 
    
    return valid;
}

//페이징 처리
function gotoPaging(cp) {
	/* var szHtml = "<input name='teamSearch' type='hidden' value='1'> ";
	$("#frm").append(szHtml); */
	$('input[name=cp]').val(cp);
	  
	  /* var area = $("select[name=sArea]").val();
	  var teamType = $("select[name=sTeamType]").val();
	  var leagueName = $("input[name=sLeagueName]").val();
	  console.log('--- selArea : '+ selArea+', selTeam : '+ selTeam); */
	 
	 document.frm.submit();
}

//광역등록 팝업 - 팝업 오픈
function showAreaPopup(){
	clearFrmData("addAreaFrm");
	var uage = "${ageGroup}";
	
	$("select[name='selUage'] option[value='"+uage+"']").prop("selected", true);
}

//광역등록 팝업 - 등록 
function gotoAddArea(){
	if(areaFormCheck("addAreaFrm")){
	 var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val();
	
	 $('input[name=uage]').val(uage); 
	 
	 /* console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.addAreaFrm.submit();
	}
}

function areaFormCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				switch ($obj.attr('name')){
				case 'teamName' :
					break;
				case 'address' :
					break;
				} */

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
    
    return valid;
}

//검색 
function gotoSearch(){
	//console.log('--- gotoSearch');
	if(searchFormCheck("frm")){
		$("input[name=cp]").val(1);
		document.frm.submit(); 
	}
}

function searchFormCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	var area = $("select[name=sArea]").val();
	var teamType = $("select[name=sTeamType]").val();
	var nickName = $("input[name=sNickName]").val();
	console.log('-- nickName:'+nickName);
	if(area < 0 && teamType < 0 && isEmpty(nickName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}
    
    if(valid == false){
   		return false;
   	} 
    
    if(!isEmpty(nickName) && isRegExp(nickName) ){
    	alert('알림!\n'+ '['+$("input[name=sNickName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
    	$("input[name=sNickName]").focus();
		valid = false;
		return false;
    }
    
    return valid;
}


function autoComplete(appendTo){ 
	$(appendTo + "  #pIdName").autocomplete({
		source : function( request, response ) {
            $.ajax({
                   type: 'post',
                   url: "ajax",
                   dataType: "json",
//                   data: request,
                   data: {
                	   cmd: "doSearchTeam2",
                	   ageGroup: "${ageGroup}",
                   	   nickName: request.term   
                   },
                   success: function(data) {
                       //서버에서 json 데이터 response 후 목록에 추가
                       response(
                    		   $.map(data.sTeamList, function(item) {
                    			   return {
		                        		label: item.nick_name,		   
		                        		value: item.nick_name,
		                        		pId: item.team_id
                    			   }
                    		   })
                       );
                   }
              });
           },    // source 는 자동 완성 대상
	       select : function(event, ui) {    //아이템 선택시
	
				$(appendTo+" [name='pId']").val(ui.item.pId);

	       },
	       focus : function(event, ui) {    //포커스 가면
	    	   return false;//한글 에러 잡기용도로 사용됨
	       },
	       minLength: 2,// 최소 글자수
	       autoFocus: true, //첫번째 항목 자동 포커스 기본값 false
	       classes: {    //잘 모르겠음
	    	   "ui-autocomplete": "highlight"
	       },
	       delay: 500,    //검색창에 글자 써지고 나서 autocomplete 창 뜰 때 까지 딜레이 시간(ms)
	       appendTo: appendTo,	//div 팝업으로 자동완성 영역을 한정시킴
/* 	       open: function(event, ui) {
	            $(this).autocomplete("widget").css({
	                "width": 250
	            });
	       }, */
	       close : function(event){    //자동완성창 닫아질때 호출
	    	   //console.log("close event:"+event);
	    	   return false;
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
  		  	<h2><span></span>학원·클럽 > 등록/수정</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="frm" id="frm" method="post"  action="teamNameUpd" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="">

	          <select class=" w150" name="sArea">
	          <option value="-1" selected>광역 선택</option>
	          <c:forEach var="result" items="${areaList}" varStatus="status">
	          	<option value="${result.area_name}">${result.area_name}</option>
	          </c:forEach>
	          </select>
	          <select class=" w150" name="sTeamType">
	            <option value="-1">학원/클럽 선택</option>
	            <option value="0">학원</option>
	            <option value="1">클럽</option>
	            <option value="2">유스</option>
	          </select>
              <input type="text" name="sNickName" placeholder="사용명칭 학원/클럽명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update over">
            <colgroup>
              <col width="55px">
              <col width="55px">
              <col width="55px">
              <col width="55px">
              <col width="10%">
              <col width="15%">
              <col width="*">
              <col width="10%">
              <col width="45px">
              <col width="10%">
            </colgroup>
            <thead>
              <tr>
                <th><a>번호 <i class="xi-caret-up-min"></i></a></th>
                <th>광역</th>
                <th>엠블럼</th>
                <th>구분</th>
                <th>사용명칭</th>
                <th>정식명칭</th>
                <th><a>소재지 <i class="xi-caret-up-min"></i></a></th>
                <th>팀연동</th>
                <th>활성여부</th>
                <th>관리</th>
              </tr>
            </thead>
            <tbody>
            <c:if test="${empty teamList }">
				<tr>
					<td id="idEmptyList" colspan="9">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${teamList}" varStatus="status"> 
			<tr>
              <td>
              	<c:choose>
              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
              	</c:choose>
              </td>
              <td>${result.area_name }</td>
              <td>
              	<c:choose>
              	<c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
              	<c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
              	</c:choose>
              </td>
              <td>
              	<c:choose>
              	<c:when test="${result.team_type eq '0'}"><span class="">학원</span></c:when>
              	<c:when test="${result.team_type eq '1'}"><span class="">클럽</span></c:when>
              	<c:when test="${result.team_type eq '2'}"><span class="">유스</span></c:when> <%-- 유스일경우?? --%>
              	</c:choose>
              </td>
              <td>${result.nick_name }</td>
              <td>${result.team_name }</td>
              <td class="tl">${result.addr }</td>
              <td>
              	<c:choose>
              	<c:when test="${result.pId eq 0}">-</c:when>
              	<c:otherwise>
              		<c:forEach var="res" items="${teamList}" varStatus="status1">
              		<c:if test="${result.pId eq res.team_id}">${result.pId_name}</c:if>
              		</c:forEach>
              	</c:otherwise>
              	</c:choose>
              </td>
             <td>
              	<c:choose>
              	<c:when test="${result.use_flag eq '0'}"><span class="coblue fwb">활성</span></c:when>
              	<c:when test="${result.use_flag eq '1'}"><span class="cored fwb" style="min-width: 40px!important;">비활성</span></c:when>
              	</c:choose>
              </td>
              <td class="admin">
                <a class="btn-pop" data-id="modefy-team-single" onclick="gotoModPopup('${result.team_id}','${result.area_name}','${result.team_type}','${result.nick_name}','${result.team_name}','${result.addr}','${result.emblem}', '${result.pId}', '${result.pId_name}')"><i class="xi-pen"></i></a>
              </td>
            </tr>
			</c:forEach>
          
            </tbody>
          </table>
        </div>
        <div class="pagination">
          <!-- paging -->
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
      </div>
	  </div>
    <div class="pop" id="modefy-team-single">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            팀명 수정하기
          </div>
          <form name="modFrm" id="modFrm" method="post"action="update_teamName">
           <input type="hidden" name="ageGroup" value="${ageGroup}">
           <input type="hidden" name="teamId">
           <input type="hidden" id="pId" name="pId" >
           <input type="hidden" name="nickName">
           <input type="hidden" name="teamName">
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">기존 사용명칭</span><input type="text" name="viewNickName" disabled>
              </li>
              <li class="title">
                <span class="title">수정 사용명칭</span><input type="text" name="newNickName" placeholder="사용명칭 입력 (예: 전주영생고)" >
              </li>
              <li class="title">
                <span class="title">기존 정식명칭</span><input type="text" name="viewTeamName" disabled>
              </li>
              <li class="title">
                <span class="title">수정 정식명칭</span><input type="text" name="newTeamName" placeholder="정식명칭 입력 (예: 전주영생고등학교)" >
              </li>
              <li class="title">
              	<fmt:formatDate var="year" value="${today}" pattern="yyyy"/>
                <span class="title" style="font-size: 12px;">데이터 변경연도</span>
                <select class=" w150" name="year">
                	<c:forEach var="num" begin="0" end="9">
                		<c:if test="${year - num >= 2014}">
                			<option value="${year - num}">${year - num}년</option>
                		</c:if>
					</c:forEach>
                </select>
                <!-- <input type="date" name="startDate" style="display: inline-block; width: 270px;"> -->
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoMod();"><span>수정하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
  <div>
</body>
</html>