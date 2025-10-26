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

//연령대 선택
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=sLeagueName]").val('');
	  $("select[name=sArea]").val('');
	  document.sfrm.submit();
}


//리그 관리 이동
function gotoLeagueMgrInfo(sFlag, idx) {
	  $('input[name=sFlag]').val(sFlag);
	  $('input[name=leagueId]').val(idx);
	  document.lmgrfrm.submit();
} 


//검색 
function gotoSearch(){
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

//페이징 처리
function gotoPaging(cp) {
	  $('input[name=cp]').val(cp);
	  document.sfrm.submit();
}


function gotoYear(year){
	$("input[name=cp]").val(1);
	$('#frm input[name="sYear"]').val(year);
	document.sfrm.submit();
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
          <form name="sfrm" id="sfrm" method="post"  action="leagueMgr" onsubmit="return false;">
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
          <!-- <div class="others">
          	 <a class="btn-large default btn-pop" data-id="update-leagueInfo-add">일괄등록</a>
          	<a class="btn-large default" onclick="gotoAddLeagueInfo();">리그생성</a>
          </div> -->
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
	              <th>총경기</th>
	              <th>완료경기</th>
	              <th>잔여경기</th>
	              <th>관리</th>
              </tr>
            </thead>
            <tbody>
			
            <c:if test="${empty leagueMgrList }">
				<tr>
					<td id="idEmptyList" colspan="11">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${leagueMgrList}" varStatus="status"> 
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
              	<c:choose>
              	<c:when test="${result.play_flag eq '0'}">활성</c:when>
              	<c:when test="${result.play_flag eq '1'}">비활성</c:when>
              	</c:choose>
              </td>
              <td>${result.play_sdate } ~ ${result.play_edate }</td>
              <td >${result.ltCnt}</td>
              <td>${result.progress}</td>
              <td>${result.ptCnt}</td>
              <td>${result.endPCnt}</td>
              <td>${result.remainCnt}</td>
              <td>
              	 <c:choose>
              	 <c:when test="${result.progress eq '종료'}">
	                <a class="btn-large gray" onclick="gotoLeagueMgrInfo('1','${result.league_id}');">일정수정</a>
              	 </c:when>
              	  <c:when test="${result.ltCnt eq 0 }">
	                <a class="btn-large default" onclick="gotoLeagueMgrInfo('0','${result.league_id}');">일정등록</a>
              	  </c:when>
              	   <c:otherwise>
	                <a class="btn-large gray-o" onclick="gotoLeagueMgrInfo('1','${result.league_id}');">일정수정</a>
              	   </c:otherwise>
              	 </c:choose>
              	 
              	 <c:if test="${result.noti_league_id ne '0'}">
                	<a class="btn-large blue">순위확정</a>	
                 </c:if>
              
              
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

  <div>
</body>

<form name="lmgrfrm" id="lmgrfrm" method="post"  action="leagueMgrInfo">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form>

<form name="lmfrm" id="lmfrm" method="post"  action="leagueMatch">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
</form> 

<form name="ltfrm" id="ltfrm" method="post"  action="leagueTeam">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="leagueId" type="hidden" value="">
</form> 


<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team" onsubmit="return false;">
  <input name="sFlag" type="hidden" value="10"> 
  <input name="cp" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="delFrm" id="delFrm" method="post"  enctype="multipart/form-data" action="save_team" >
   <input type="hidden" name="sFlag" value="2">
   <input type="hidden" name="teamId">
   <input type="hidden" name="emblem">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

</html>