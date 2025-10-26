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

//리그 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.lfrm.submit();
}

//리그 리스트 이동
function gotoLeague() {
	document.lfrm.submit();
}

//리그 기본정보 등록
function gotoLeagueInfo(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		 document.frm.submit();
	}
}

//리그 기본정보 등록 후 팀정보 이동
function gotoLeagueTeam(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		$('input[name=mvFlag]').val('5');
		document.frm.submit();
	}
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
          	  <input name="sFlag" type="hidden" value="${sFlag}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup}">
  			  
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
        
        <form name="frm" id="frm" action="save_leagueInfo" onsubmit="return false;">
         <input name="sFlag" type="hidden" value="">
         <input name="ageGroup" type="hidden" value="${ageGroup}">
         <input name="leagueId" type="hidden" value="${leagueInfoMap.league_id }">
          
        <div class="scroll">
          <table cellspacing="0" class="update">
          	<colgroup>
            <col width="10%">
            <col width="20%">
            <col width="10%">
            <col width="30%">
            <col width="10%">
            <col width="*">
          </colgroup>
            <tbody>
              <tr>
		          <th>활성여부</th>
	              <td class="tl">
	              	<c:choose>
	              	<c:when test="${leagueInfoMap.play_flag eq '1' }">
		                <input type="radio" checked="checked" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:when>
	              	<c:when test="${leagueInfoMap.play_flag eq '0' }">
		                <input type="radio" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" checked="checked" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:when>
	              	<c:otherwise>
	              	 	<input type="radio" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:otherwise>
	              	</c:choose>
	              </td>
	              <th>리그명</th>
	              <td class="tl" colspan="3">
		              <input type="text" name="leagueName" value="${leagueInfoMap.league_name }"  placeholder="리그명 입력 (예: [광역] 년도 리그명)">
	              </td>
              </tr>
			  <tr>
				  <th>리그명 오리지널</th>
				  <td class="tl" colspan="3">
					  <input type="text" name="leagueNameOrigin" value="${leagueInfoMap.league_name_origin }"  placeholder="리그명 입력 (예: [광역] 년도 리그명)">
				  </td>
			  </tr>
              <tr>
	              <th>광역선택</th>
	              <td class="tl">
					<div class="w80 fl">
					<select name="selArea">
						  <option value="-1" selected>광역 선택</option>
				          <c:forEach var="result" items="${areaList}" varStatus="status">
				            <c:choose>
				          	<c:when test="${result.area_name eq leagueInfoMap.area_name}">
					          	<option value="${result.area_name}" selected>${result.area_name}</option>
				          	</c:when> 
				          	<c:otherwise>
					          	<option value="${result.area_name}">${result.area_name}</option>
				          	</c:otherwise>
				          	</c:choose>
				          	
				          </c:forEach>
					</select>
					</div>
					<div>
					<!-- &nbsp;<a class="btn-pop" data-id="update-area-add"><i class="xi-cog" style="line-height: 28px;"></i></a> -->
					</div>
	              </td>
	              <th>대회기간</th>
	              <td class="tl">
	                <div class="w40 fl"><input type="text" name="sdate" value="${leagueInfoMap.play_sdate }" placeholder="시작일 - 예)20190301" autocomplete="off" class="datepicker"></div>
	                <div class="w5 fl tc" style="height:28px; line-height:28px;">-</div>
	                <div class="w40 fl"><input type="text" name="edate" value="${leagueInfoMap.play_edate }" placeholder="종료일 - 예)20190301" autocomplete="off" class="datepicker"></div>
	              </td>
	              <th>순위 선정방식</th>
	              <td class="tl">
	              	<c:choose>
	              	<c:when test="${leagueInfoMap.rank_type eq '0' }">
		                <input type="radio" checked="checked" name="rankType" id="rankType1" value="0"><label for="rankType1">골득실</label>
		                <input type="radio" name=rankType id="rankType2" value="1"><label for="rankType2">승자승</label>
	              	</c:when>
	              	<c:when test="${leagueInfoMap.rank_type eq '1' }">
		                <input type="radio" name="rankType" id="rankType1" value="0"><label for="rankType1">골득실</label>
		                <input type="radio" checked="checked" name="rankType" id="rankType2" value="1"><label for="rankType2">승자승</label>
	              	</c:when>
	              	<c:otherwise>
	              	 	<input type="radio" name="rankType" id="rankType1" value="0"><label for="rankType1">골득실</label>
		                <input type="radio" name="rankType" id="rankType2" value="1"><label for="rankType2">승자승</label>
	              	</c:otherwise>
	              	</c:choose>
	              </td>
            </tr>
            </tbody>
          </table>
        </div>
        </form>
        
        <div class="body-foot">
          <div class="search">
          </div>
          <div class="others">
          	<c:choose>
          	<c:when test="${sFlag eq '0' }">
	            <a class="btn-large default" onclick="gotoLeagueInfo(0);">리그생성</a>
	            <a class="btn-large gray-o" onclick="gotoLeagueTeam(0);">리그생성 후 팀 등록</a> 
          	</c:when>
          	<c:when test="${sFlag eq '1' }">
          		<a class="btn-large gray-o" onclick="gotoLeagueInfo(1);">리그수정</a>
	            <a class="btn-large gray-o" onclick="gotoLeagueTeam(1);">리그생성 후 팀 등록</a> 
          	</c:when>
          	</c:choose>
          </div>
        </div>
  <div>
</body>

<form name="lfrm" id="lfrm" method="post"  action="league">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
	<input name="sYear" type="hidden" value="${sYear}">
	<input name="sLeagueName" type="hidden" value="${sLeagueName}">
</form>  

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

</html>