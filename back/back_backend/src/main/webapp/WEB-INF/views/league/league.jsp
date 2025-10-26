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
	var sArea = "${sArea}";
	if(!isEmpty(sArea)){
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	}
	var sLeagueName = "${sLeagueName}";
	if(!isEmpty(sLeagueName)){
		$("input[name=sLeagueName]").val(sLeagueName);
	}
	console.log('--- [init search] sArea :'+ sArea+', sLeagueName : '+sLeagueName );
});

//리그 기본정보 생성
function gotoAddLeagueInfo() {
	  $('input[name=sFlag]').val('0');
	  document.lifrm.submit();
}
//리그 기본정보 생성 크롤링
function gotoAddLeagueInfoCraw() {
    $('input[name=sFlag]').val('0');
    document.lifrm.action = "leagueInfoCraw";
    document.lifrm.submit();
}
//리그 기본정보 수정
function gotoFixLeagueInfo(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=leagueId]').val(idx);
    document.lifrm.submit();
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

//연령대 선택
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=sLeagueName]").val('');
	  $("select[name=sArea]").val('');
	  document.frm.submit();
}

//검색 
function gotoSearch(){
	if(searchFormCheck("frm")){
		$("input[name=cp]").val(1);
		document.frm.submit(); 
	}
}

function searchFormCheck(regxForm) {
	var valid = true;
	
	var area = $("select[name=sArea]").val();
	var leagueName = $("input[name=sLeagueName]").val();
	
	/*if(area < 0 && isEmpty(leagueName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}*/
    
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

//참가팀 등록으로 이동
function gotoAddLeagueTeam(leagueId){
	$("input[name=leagueId]").val(leagueId);
	document.ltfrm.submit();
}

//경기일정 등록으로 이동
function gotoAddLeagueMatch(leagueId){
	$("input[name=leagueId]").val(leagueId);
	document.lmfrm.submit();
}



/* //데이터 초기화
function gotoDataReset(){ 
	if (confirm("정말 데이터 초기 하시겠습니까?")) {
  		document.gotoResetFrm.submit();
    }
} */


// 폼 input type 초기화
function clearFrmData(regxForm){
	//console.log('-- clearFrmData regxForm : '+ regxForm);
	var $form = $("#"+regxForm);
	$form[0].reset();
}

//페이징 처리
function gotoPaging(cp) {
	  $('input[name=cp]').val(cp);
	  document.frm.submit();
}


function gotoDel(idx, ltCnt){
	if(ltCnt > 0){
		alert('알림!\n등록된 참가팀이 존재하여 삭제 할수 없습니다. \n참가팀을 먼저 삭제해주세요.');
		return;
	}
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=leagueId]').val(idx);
		document.delFrm.submit();
	}
}

//대회 기본개요수상 등록
function gotoAddLeaguePrize(idx) {
    $('input[name=sFlag]').val('1');
    $('input[name=leagueId]').val(idx);
    document.lpfrm.submit();
}


function gotoYear(year){
    $("input[name=cp]").val(1);
    $('#frm input[name="sYear"]').val(year);
    document.frm.submit();
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
          <form name="frm" id="frm" method="post"  action="league" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup }">

              <select name="sYear" id="selYears" onchange="gotoYear(this.value);">
                  <option value="">연도선택</option>
                  <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                      <c:set var="revI" value="${sysYear - (i - 2014)}" />
                      <c:choose>
                          <c:when test="${revI eq sYear}">
                              <option value="${revI}" selected>${revI}</option>
                          </c:when>
                          <c:otherwise>
                              <option value="${revI}">${revI}</option>
                          </c:otherwise>
                      </c:choose>
                  </c:forEach>
              </select>

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
          	 <a class="btn-large default btn-pop" data-id="update-leagueInfo-add">일괄등록</a>
          	<a class="btn-large default" onclick="gotoAddLeagueInfo();">리그생성</a>
          	<a class="btn-large default" onclick="gotoAddLeagueInfoCraw();">크롤링 리그생성</a>
          </div>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update over">
            <thead>
              <tr>
	              <th>번호</th>
	              <th>광역</th>
	              <th>리그명</th>
	              <th>활성</th>
	              <th>대회일정</th>
	              <th>참가팀</th>
	              <th>진행여부</th>
	              <th>관리</th>
	              <th>삭제</th>
              </tr>
            </thead>
            <tbody>
            <c:if test="${empty leagueInfoList }">
				<tr>
					<td id="idEmptyList" colspan="9">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${leagueInfoList}" varStatus="status"> 
			<tr>
              <td>
              	<c:choose>
              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
              	</c:choose>
              </td>
              <td>${result.area_name }</td>
              <td>${result.league_name }</td>
            
              <td>
              	<%-- <span class="label red">${result.team_type }</span> --%>
              	<c:choose>
              	<c:when test="${result.play_flag eq '0'}">활성</c:when>
              	<c:when test="${result.play_flag eq '1'}">비활성</c:when>
              	</c:choose>
              </td>
              <td>${result.play_sdate } ~ ${result.play_edate }</td>
              <td >${result.ltCnt}</td>
               <td>${result.progress}</td>
               <td>
<%--                 <a class="btn-pop" data-id="modefy-school-single" onclick="gotoModPopup('${result.team_id}','${result.area_name}','${result.team_type}','${result.nick_name}','${result.team_name}','${result.addr}','${result.emblem}');"><i class="xi-catched"></i></a> --%>
                <a class="btn-large gray-o" onclick="gotoFixLeagueInfo('${result.league_id}');">정보수정</a>
                <c:choose>
                <c:when test="${result.progress eq '종료'}">
                    <%--<c:choose>
                        <c:when test="${empty result.league_info}">
                            <a class="btn-large gray" onclick="gotoAddLeaguePrize('${result.league_id}');">개요등록</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn-large gray" onclick="gotoAddLeaguePrize('${result.league_id}');">개요수정</a>
                        </c:otherwise>
                    </c:choose>--%>
					<c:choose>
	                <c:when test="${result.ltCnt eq 0 }">
		                <a class="btn-large gray" onclick="gotoAddLeagueTeam('${result.league_id}');">참가팀등록</a>
	                </c:when>
	                <c:otherwise>
		                <a class="btn-large gray" onclick="gotoAddLeagueTeam('${result.league_id}');">참가팀수정</a>
	                </c:otherwise>
					</c:choose>
               	</c:when>
                <c:when test="${result.progress ne '종료'}">
                    <%--<c:choose>
                        <c:when test="${empty result.league_info}">
                            <a class="btn-large default" onclick="gotoAddLeaguePrize('${result.league_id}');">개요등록</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn-large gray-o" onclick="gotoAddLeaguePrize('${result.league_id}');">개요수정</a>
                        </c:otherwise>
                    </c:choose>--%>
					<c:choose>
	                <c:when test="${result.ltCnt eq 0 }">
		                <a class="btn-large default" onclick="gotoAddLeagueTeam('${result.league_id}');">참가팀등록</a>
	                </c:when>
	                <c:otherwise>
			                <a class="btn-large gray-o" onclick="gotoAddLeagueTeam('${result.league_id}');">참가팀수정</a>
	                </c:otherwise>
					</c:choose>                
                </c:when>
                </c:choose>
                
              	 <c:choose>
              	 <c:when test="${result.progress eq '종료'}">
              	 	<c:choose>
	                <c:when test="${result.ltCnt eq 0 }">
		                <a class="btn-large gray" onclick="gotoAddLeagueMatch('${result.league_id}');">일정등록</a>
	                </c:when>
	                <c:otherwise>
		                <a class="btn-large gray" onclick="gotoAddLeagueMatch('${result.league_id}');">일정수정</a>
	                </c:otherwise>
	                </c:choose>
              	 </c:when>
              	  <c:when test="${result.ltCnt eq 0 }">
	                <a class="btn-large default" onclick="gotoAddLeagueMatch('${result.league_id}');">일정등록</a>
              	  </c:when>
              	   <c:otherwise>
	                <a class="btn-large gray-o" onclick="gotoAddLeagueMatch('${result.league_id}');">일정수정</a>
              	   </c:otherwise>
              	 </c:choose>
              </td>
              <td class="admin">
                <a onclick="gotoDel('${result.league_id}','${result.ltCnt}');"><i class="xi-close-circle-o"></i></a>
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
	  </div>

   <!-- 엑셀 - 리그 기본정보  일괄등록 --> 
    <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
	<input name="excelFlag" type="hidden" value="leagueInfo">
	<input name="ageGroup" type="hidden" value="${ageGroup}">
    <div class="pop" id="update-leagueInfo-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            리그 기본 정보 일괄 등록
          </div>
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li>
                <input type="file" id="excelFile" name="excelFile">
              </li>
            </ul>
          </div>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddExcel();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    </form>
    
    <div class="pop" id="update-area-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            광역 등록
          </div>
          <form name="addAreaFrm" id="addAreaFrm" method="post"  action="save_area">
           <input type="hidden" name="sFlag" value="0">
           <input type="hidden" name="uage">
           <input type="hidden" name="page" value="team"> 
           
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">연령선택</span>
                <select style="width:50%;" name="selUage" id="selUage">
		          	<c:forEach var="result" items="${uageList}" varStatus="status">
		          	<option value="${result.uage}">${result.uage}</option>
		          	</c:forEach>
		        </select>
              </li>
              <li class="title">
                <span class="title">광역명</span>
                <input type="text" name="areaName" placeholder="광역명 입력 (예: 호남/제주)">
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddArea();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    <!--팝업 끝-->
  <div>
</body>

<form name="lmfrm" id="lmfrm" method="post"  action="leagueMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
    <input name="sYear" type="hidden" value="${sYear}">
    <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form> 

<form name="ltfrm" id="ltfrm" method="post"  action="leagueTeam">
  <input name="sFlag" type="hidden" value="">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
    <input name="sYear" type="hidden" value="${sYear}">
    <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form> 

<form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
    <input name="sYear" type="hidden" value="${sYear}">
    <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form>

<form name="lpfrm" id="lpfrm" method="post"  action="leaguePrize">
    <input name="sFlag" type="hidden" value="">
    <input name="ageGroup" type="hidden" value="${ageGroup}">
    <input name="leagueId" type="hidden" value="">
</form>


<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team" onsubmit="return false;">
  <input name="sFlag" type="hidden" value="10"> 
  <input name="cp" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="delFrm" id="delFrm" method="post" action="save_leagueInfo" >
   <input type="hidden" name="sFlag" value="2">
   <input type="hidden" name="leagueId">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

 
</html>