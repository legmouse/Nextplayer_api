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

//대회 예선 결과 수정
function gotoCupSubMatchResult(idx) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";
	var cp = '${cp}';

	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cmgrsrfrm.submit();
}

function gotoAddCupFullMatchResult(idx, teamType) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";
	var cp = '${cp}';
	if (ageGroup != null && ageGroup != '') {
		url = 'cupMgr?ageGroup=' + ageGroup + '&cp=' + cp;
	}
	window.history.pushState(null,null, url);
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
	$('input[name=teamType]').val(teamType);
    document.cmgrsrfrm.submit();
}



//대회 본선 결과 수정
function gotoCupMainMatchResult(idx) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";
	var cp = '${cp}';
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cmgrmrfrm.submit();
}

//대회 토너먼트 결과 수정
function gotoCupTourMatchResult(idx) {
	var url = 'cupMgr';
	var ageGroup = "${ageGroup}";
	var cp = '${cp}';

	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cmgrtrfrm.submit();
}

//대회 기본개요수상 등록
function gotoAddCupPrize(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cpfrm.submit();
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

//참가팀 등록으로 이동
function gotoAddCupTeam(cupId){
	$("input[name=cupId]").val(cupId);
	document.ctfrm.submit();
}

//경기일정 등록으로 이동
function gotoAddCupMatch(cupId){
	$("input[name=cupId]").val(cupId);
	document.cmfrm.submit();
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


function gotoDel(idx, ctCnt){
	if(ctCnt > 0){
		alert('알림!\n등록된 참가팀이 존재하여 삭제 할 수 없습니다. \n참가팀을 먼저 삭제해주세요.');
		return;
	}
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=cupId]').val(idx);
		document.delFrm.submit();
	}
}

function gotoDelComm(idx, ctCnt, csmCnt, cmmCnt, ctmCnt){
	if (ctmCnt > 0) {
		alert('알림!\n등록된 토너먼트 정보가 존재하여 삭제 할 수 없습니다. \n토너먼트 정보를 먼저 삭제해주세요.');
		return;
	}
	
	if (cmmCnt > 0) {
		alert('알림!\n등록된 본선 정보가 존재하여 삭제 할 수 없습니다. \n본선 정보를 먼저 삭제해주세요.');
		return;
	}
	
	if (csmCnt > 0) {
		alert('알림!\n등록된 예선 정보가 존재하여 삭제 할 수 없습니다. \n예선 정보를 먼저 삭제해주세요.');
		return;
	}
	
	if(ctCnt > 0){
		alert('알림!\n등록된 참가팀이 존재하여 삭제 할 수 없습니다. \n참가팀을 먼저 삭제해주세요.');
		return;
	}
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=cupId]').val(idx);
		document.delFrm.submit();
	}
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
  		  	<h2><span></span>대회정보 > 관리</h2>
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
          <form name="frm" id="frm" method="post"  action="cupMgr" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup }">

			  <c:set var="now" value="<%=new java.util.Date()%>" />
			  <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
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
              <input type="text" name="sCupName" placeholder="대회명 입력" value="${sCupName}" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update over">
            <thead>
              <tr>
	              <th>번호</th>
	              <th>대회명</th>
	              <th>활성</th>
	              <th>대회일정</th>
	              <th>참가팀</th>
				  <c:if test="${ageGroup eq 'U12' or ageGroup eq 'U11'}">
				  <th>참가팀2</th>
				  </c:if>
	              <th>대회유형</th>
	              <th>토너먼트</th>
	              <th>토너먼트 팀수</th>
	              <th>관리</th>
	              <th>삭제</th>
              </tr>
            </thead>
            <tbody>
            <c:if test="${empty cupMgrList}">
				<tr>
					<td id="idEmptyList" colspan="10">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${cupMgrList}" varStatus="status">
			<tr>
              <td>
              	<c:choose>
              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
              	</c:choose>
              </td>
              <td class="tl">${result.cup_name}</td>
              <td>
              	<%-- <span class="label red">${result.team_type }</span> --%>
              	<c:choose>
              	<c:when test="${result.play_flag eq '0'}">활성</c:when>
              	<c:when test="${result.play_flag eq '1'}">비활성</c:when>
              	</c:choose>
              </td>
              <td>${result.play_sdate} ~ ${result.play_edate}</td>
              <td>${result.cup_team}팀</td>
			  <c:if test="${ageGroup eq 'U12' or ageGroup eq 'U11'}">
			  <td>${result.cup_team2}팀</td>
			  </c:if>
              <td class="tl">
              	<c:if test="${result.cup_type eq 0}">예선+토너먼트</c:if>
              	<c:if test="${result.cup_type eq 1}">예선+본선+토너먼트</c:if>
              	<c:if test="${result.cup_type eq 2}">풀리그</c:if>
              	<c:if test="${result.cup_type eq 3}">토너먼트</c:if>
              	<c:if test="${result.cup_type eq 4}">풀리그 + 풀리그</c:if>
              </td>
              <td>
              	<c:if test="${result.tour_type eq 0}">대진표</c:if>
              	<c:if test="${result.tour_type eq 1}">추첨제</c:if>
              </td>
              <td>${result.tour_team}팀</td>
              <td>
				<c:choose>
					<c:when test="${ageGroup ne 'U12' and ageGroup ne 'U11'}">

						<c:choose>
							<c:when test="${result.cup_type eq '3'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.csmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.csmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>

						<c:choose>
							<c:when test="${result.cup_type ne '1'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.cmmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.cmmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>



						<c:choose>
							<c:when test="${result.cup_type eq '2'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.ctmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.ctmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>

						<c:if test="${result.noti_cup_id ne '0'}">
							<a class="btn-large blue">순위확정</a>
						</c:if>

					</c:when>

					<c:otherwise>

						<c:choose>
							<c:when test="${result.cup_type eq '3' or result.cup_type eq '4'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.csmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.csmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupSubMatchResult('${result.cup_id}');">예선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>

						<c:choose>
							<c:when test="${result.cup_type ne '1'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.cmmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.cmmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupMainMatchResult('${result.cup_id}');">본선결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>



						<c:choose>
							<c:when test="${result.cup_type eq '2' or result.cup_type eq '4'}">
							</c:when>
							<c:when test="${result.progress eq '종료'}">
								<c:choose>
									<c:when test="${result.ctmCnt eq 0 }">
										<a class="btn-large gray" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${result.progress ne '종료'}">
								<c:choose>
									<c:when test="${result.ctmCnt eq 0 }">
										<a class="btn-large default" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과등록</a>
									</c:when>
									<c:otherwise>
										<a class="btn-large gray-o" onclick="gotoCupTourMatchResult('${result.cup_id}');">토너먼트결과수정</a>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>

						<c:choose>
							<c:when test="${result.cup_type eq '4'}">
								<c:choose>
									<c:when test="${result.progress eq '종료'}">
										<c:choose>
											<c:when test="${result.csmCnt1 eq '0'}">
												<a class="btn-large gray" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '1');">1차 풀리그 등록</a>
											</c:when>
											<c:otherwise>
												<a class="btn-large gray" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '1');">1차 풀리그 수정</a>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${result.progress ne '종료'}">
										<c:choose>
											<c:when test="${result.csmCnt1 eq '0'}">
												<a class="btn-large default" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '1');">1차 풀리그 등록</a>
											</c:when>
											<c:otherwise>
												<a class="btn-large gray-o" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '1');">1차 풀리그 수정</a>
											</c:otherwise>
										</c:choose>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${result.progress eq '종료'}">
										<c:choose>
											<c:when test="${result.csmCnt2 eq '0'}">
												<a class="btn-large gray" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '2');">2차 풀리그 등록</a>
											</c:when>
											<c:otherwise>
												<a class="btn-large gray" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '2');">2차 풀리그 수정</a>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${result.progress ne '종료'}">
										<c:choose>
											<c:when test="${result.csmCnt2 eq '0'}">
												<a class="btn-large default" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '2');">2차 풀리그 등록</a>
											</c:when>
											<c:otherwise>
												<a class="btn-large gray-o" onclick="gotoAddCupFullMatchResult('${result.cup_id}', '2');">2차 풀리그 수정</a>
											</c:otherwise>
										</c:choose>
									</c:when>
								</c:choose>
							</c:when>
						</c:choose>

						<c:if test="${result.noti_cup_id ne '0'}">
							<a class="btn-large blue">순위확정</a>
						</c:if>

					</c:otherwise>

				</c:choose>

              </td>
              <td class="admin">
              	<c:choose>
              		<c:when test="${ageGroup eq 'U12' or ageGroup eq 'U11'}">
                		<a onclick="gotoDel('${result.cup_id}','${result.ctCnt}');"><i class="xi-close-circle-o"></i></a>
                	</c:when>
                	<c:otherwise>
                		<a onclick="gotoDelComm('${result.cup_id}','${result.ctCnt}', '${result.csmCnt}', '${result.cmmCnt}', '${result.ctmCnt}');"><i class="xi-close-circle-o"></i></a>
                	</c:otherwise>
                </c:choose>
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
	<input name="excelFlag" type="hidden" value="cupInfo">
	<input name="ageGroup" type="hidden" value="${ageGroup}">
    <div class="pop" id="update-cupInfo-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            대회 기본 정보 일괄 등록
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

    <!--팝업 끝-->
  <div>
</body>

<form name="cmfrm" id="cmfrm" method="post"  action="cupMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="ctfrm" id="ctfrm" method="post"  action="cupTeam">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="cmgrsrfrm" id="cmgrsrfrm" method="post"  action="cupMgrSubMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
  <input name="teamType" type="hidden" value="">
</form>

<form name="cmgrmrfrm" id="cmgrmrfrm" method="post"  action="cupMgrMainMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="cmgrtrfrm" id="cmgrtrfrm" method="post"  action="cupMgrTourMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="cpfrm" id="cpfrm" method="post"  action="cupPrize">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team" onsubmit="return false;">
  <input name="sFlag" type="hidden" value="10">
  <input name="cp" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="delFrm" id="delFrm" method="post" action="save_cupInfo" >
   <input type="hidden" name="sFlag" value="2">
   <input type="hidden" name="cupId">
   <input name="cp" type="hidden" value="${cp}">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="mvFlag" type="hidden" value="cupMgr">
</form>


</html>