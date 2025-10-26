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

	$("input[name=cType]").click(function(){

		const cTypeValue = $(this).val();

		if (cTypeValue == 4) {
			const str = '<th id="appendTh">참가팀2</th>' +
					    '<td class="tl" id="appendTd">' +
					  	'<div class="w80 fl"><input type="text" name="cupTeam2" placeholder="팀수" value="${cupInfoMap.cup_team2}"></div>' +
					  	'<div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>' +
				  		'</td>';
			$("#appendTr").append(str);

			const str2 = '1차 풀리그 팀수';
			const str3 = '2차 풀리그 팀수';

			$("#teamCntTh1").html(str2);
			$("#teamCntTh2").html(str3);

		} else {
			$("#appendTh").remove();
			$("#appendTd").remove();

			const str2 = '예선 조편성 팀수';
			const str3 = '본선 조편성 팀수';

			$("#teamCntTh1").html(str2);
			$("#teamCntTh2").html(str3);
		}

	});

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

//대회 기본정보 등록
function gotoCupInfo(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		 document.frm.submit();
	}
}

//리그 기본정보 등록 후 팀정보 이동
function gotoCupPrize(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		$('input[name=mvFlag]').val('cupPrize');
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

function sendPush(type, id, name, age, typeId, title, text) {
	
	var auto = '${pushInfo.auto_flag}';
	if (auto == '1' || auto == 1) {
		alert('자동발송 기능이 off로 설정되어 있습니다. 자동발송을 on으로 설정해주세요.')
		return false;
	}
	
    var param = {
        ageGroup : age,
        cupId: id,
    };
    
    text = text.replace('연령', age);
	text = text.replace('대회', name + ' 대회');
    
    var data = {
    	title: title,
    	body: text,
    	uage: age,
    	cupId: id,
        path: encodeURIComponent('/contest/' + id + '?ageGroup=' + age),
        param: JSON.stringify(param),
        description: '대회 상세로 이동',
        method: type,
        autoFlag: 0,
        typeId: typeId,
        androidApp: 0,
        iosApp: 0,
        ipadApp: 0,
        authFlag: 0
    };

    $.ajax({
        type: 'POST',
        url: '/insertCupPush',
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
          	<a class="btn-large gray-o" onclick="gotoCup();"><i class="xi-long-arrow-left"></i> 대회등록 리스트</a>
          </div>
        </div>
        
        <form name="frm" id="frm" action="save_cupInfo" onsubmit="return false;">
         <input name="sFlag" type="hidden" value="">
         <input name="mvFlag" type="hidden" value="">
         <input name="ageGroup" type="hidden" value="${ageGroup}">
         <input name="cupId" type="hidden" value="${cupInfoMap.cup_id }">
          
        <div class="scroll">
          <table cellspacing="0" class="update">
          	<colgroup>
	            <%--<col width="10.3%">
	            <col width="23%">
	            <col width="10.3%">
	            <col width="22%">
	            <col width="10.3%">
	            <col width="22%">--%>
	          </colgroup>
            <tbody>
			<tr>
				<th>대회명 오리지널</th>
				<td class="tl" colspan="8">
					<input type="text" name="cupNameOrigin" value="${cupInfoMap.cup_name_origin }"  placeholder="대회명 입력 (예: [지역] 회차(년도) 대회명)">
				</td>

			</tr>
              <tr>
		          <th>활성여부</th>
	              <td class="tl">
	              	<c:choose>
	              	<c:when test="${cupInfoMap.play_flag eq '1' }">
		                <input type="radio" checked="checked" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:when>
	              	<c:when test="${cupInfoMap.play_flag eq '0' }">
		                <input type="radio" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" checked="checked" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:when>
	              	<c:otherwise>
	              	 	<input type="radio" name="pFlag" id="pf1" value="1"><label for="pf1">비활성</label>
		                <input type="radio" name="pFlag" id="pf2" value="0"><label for="pf2">활성</label>
	              	</c:otherwise>
	              	</c:choose>
	              </td>
	              
	              <th>대회유형</th>
	              <td class="tl" colspan="3">
	              <c:choose>
	              	<c:when test="${cupInfoMap.cup_type eq '0' }">
	              		<input type="radio" name="cType" id="ct1" value="0" checked="checked"><label for="ct1">예선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct2" value="1"><label for="ct2">예선리그+본선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct3" value="2"><label for="ct3">풀리그</label>
		                <input type="radio" name="cType" id="ct4" value="3"><label for="ct4">토너먼트</label>
						<c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
						<input type="radio" name="cType" id="ct5" value="4"><label for="ct5">풀리그 + 풀리그</label>
						</c:if>
	              	</c:when>
	              	<c:when test="${cupInfoMap.cup_type eq '1' }">
	              		<input type="radio" name="cType" id="ct1" value="0"><label for="ct1">예선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct2" value="1" checked="checked"><label for="ct2">예선리그+본선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct3" value="2"><label for="ct3">풀리그</label>
		                <input type="radio" name="cType" id="ct4" value="3"><label for="ct4">토너먼트</label>
						<c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
						<input type="radio" name="cType" id="ct5" value="4"><label for="ct5">풀리그 + 풀리그</label>
						</c:if>
	              	</c:when>
	              	<c:when test="${cupInfoMap.cup_type eq '2' }">
	              		<input type="radio" name="cType" id="ct1" value="0"><label for="ct1">예선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct2" value="1"><label for="ct2">예선리그+본선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct3" value="2" checked="checked"><label for="ct3">풀리그</label>
		                <input type="radio" name="cType" id="ct4" value="3"><label for="ct4">토너먼트</label>
						<c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
						<input type="radio" name="cType" id="ct5" value="4"><label for="ct5">풀리그 + 풀리그</label>
						</c:if>
	              	</c:when>
	              	<c:when test="${cupInfoMap.cup_type eq '3' }">
	              		<input type="radio" name="cType" id="ct1" value="0"><label for="ct1">예선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct2" value="1"><label for="ct2">예선리그+본선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct3" value="2"><label for="ct3">풀리그</label>
		                <input type="radio" name="cType" id="ct4" value="3" checked="checked"><label for="ct4">토너먼트</label>
						<c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
						<input type="radio" name="cType" id="ct5" value="4"><label for="ct5">풀리그 + 풀리그</label>
						</c:if>
	              	</c:when>
				  	<c:when test="${cupInfoMap.cup_type eq '4' }">
					  <input type="radio" name="cType" id="ct1" value="0"><label for="ct1">예선리그+토너먼트</label>
					  <input type="radio" name="cType" id="ct2" value="1"><label for="ct2">예선리그+본선리그+토너먼트</label>
					  <input type="radio" name="cType" id="ct3" value="2"><label for="ct3">풀리그</label>
					  <input type="radio" name="cType" id="ct4" value="3"><label for="ct4">토너먼트</label>
					  <c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
					  <input type="radio" name="cType" id="ct5" value="4"checked="checked"><label for="ct5">풀리그 + 풀리그</label>
					  </c:if>

				 	 </c:when>
	              	<c:otherwise>
	              		<input type="radio" name="cType" id="ct1" value="0"><label for="ct1">예선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct2" value="1"><label for="ct2">예선리그+본선리그+토너먼트</label>
		                <input type="radio" name="cType" id="ct3" value="2"><label for="ct3">풀리그</label>
		                <input type="radio" name="cType" id="ct4" value="3"><label for="ct4">토너먼트</label>
						<c:if test="${ageGroup eq 'U12' || ageGroup eq 'U11'}">
						<input type="radio" name="cType" id="ct5" value="4" onclick="fnFullLeague()"><label for="ct5">풀리그 + 풀리그</label>
						</c:if>
	              	</c:otherwise>
	              </c:choose>
	                
	              </td>
              </tr>
              <tr id="appendTr">
	              <th>대회명</th>
	              <td class="tl">
		              <input type="text" name="cupName" value="${cupInfoMap.cup_name }"  placeholder="대회명 입력 (예: [지역] 회차(년도) 대회명)">
	              </td>
	              <th>대회기간</th>
	              <td class="tl">
	                <div class="w40 fl"><input type="text" name="sdate" value="${cupInfoMap.play_sdate }" placeholder="시작일 - 예)20190301" autocomplete="off" class="datepicker"></div>
	                <div class="w5 fl tc" style="height:28px; line-height:28px;">-</div>
	                <div class="w40 fl"><input type="text" name="edate" value="${cupInfoMap.play_edate }" placeholder="종료일 - 예)20190301" autocomplete="off" class="datepicker"></div>
	              </td>
	              <th>참가팀</th>
	              <td class="tl">
	                <div class="w80 fl"><input type="text" name="cupTeam" placeholder="팀수" value="${cupInfoMap.cup_team}"></div>
	                <div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>
	              </td>
				  <c:if test="${cupInfoMap.cup_type eq '4'}">
					  <th>참가팀2</th>
                        <td class="tl">
                            <div class="w80 fl"><input type="text" name="cupTeam2" placeholder="팀수" value="${cupInfoMap.cup_team2}"></div>
                            <div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>
                        </td>
				  </c:if>
              </tr>
              <tr>
	              <th id="teamCntTh1">예선 조편성 팀수</th>
	              <td class="tl">
		              <div class="w40 fl"><input type="text" name="sTeamCnt" value="${cupInfoMap.sub_team_count}"  placeholder="예선 조/팀(예 10/4)"></div>
	              </td>
	              <th id="teamCntTh2">본선 조편성 팀수</th>
	              <td class="tl">
		              <div class="w40 fl"><input type="text" name="mTeamCnt" value="${cupInfoMap.main_team_count}"  placeholder="본선 조/팀(예 10/4)"></div>
	              </td>
	              <th>순위선정방식</th>
	              <td class="tl">
	                <c:choose>
	              	<c:when test="${cupInfoMap.rank_type eq '1' }">
		                <input type="radio" name="rType" id="rt2" value="0"><label for="rt2">골득실</label>
		                <input type="radio" checked="checked" name="rType" id="rt1" value="1"><label for="rt1">승자승</label>
	              	</c:when>
	              	<c:when test="${cupInfoMap.rank_type eq '0' }">
		                <input type="radio" checked="checked" name="rType" id="rt2" value="0"><label for="rt2">골득실</label>
		                <input type="radio" name="rType" id="rt1" value="1"><label for="rt1">승자승</label>
	              	</c:when>
	              	<c:otherwise>
		                <input type="radio" name="rType" id="rt2" value="0"><label for="rt2">골득실</label>
	              	 	<input type="radio" name="rType" id="rt1" value="1"><label for="rt1">승자승</label>
	              	</c:otherwise>
	              	</c:choose>
	              </td>
              </tr>
              <tr>
	              <th>토너먼트 타입</th>
	              <td class="tl">
	                <c:choose>
	              	<c:when test="${cupInfoMap.tour_type eq '1' }">
		                <input type="radio" name="tType" id="tt2" value="0"><label for="tt2">대진표</label>
		                <input type="radio" checked="checked" name="tType" id="tt1" value="1"><label for="tt1">추첨제</label>
	              	</c:when>
	              	<c:when test="${cupInfoMap.tour_type eq '0' }">
		                <input type="radio" checked="checked" name="tType" id="tt2" value="0"><label for="tt2">대진표</label>
		                <input type="radio" name="tType" id="tt1" value="1"><label for="tt1">추첨제</label>
	              	</c:when>
	              	<c:otherwise>
		                <input type="radio" name="tType" id="tt2" value="0"><label for="tt2">대진표</label>
	              	 	<input type="radio" name="tType" id="tt1" value="1"><label for="tt1">추첨제</label>
	              	</c:otherwise>
	              	</c:choose>
	              </td>
	              <th>토너먼트 팀수</th>
	              <td class="tl">
	                <div class="w30 fl"><input type="text" name="tourTeam" placeholder="팀수" value="${cupInfoMap.tour_team}"></div>
	                <div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>
	              </td>
				  <th>대회 타입</th>
				  <td class="tl">
					  <input type="radio" name="regionType" id="regionType0" value="NATIONAL" <c:if test="${cupInfoMap.region_type eq 'NATIONAL'}">checked</c:if>><label for="regionType0">전국대회</label>
					  <input type="radio" name="regionType" id="regionType1" value="LOCAL" <c:if test="${cupInfoMap.region_type eq 'LOCAL'}">checked</c:if>><label for="regionType1">지방대회</label>
				  </td>
              </tr>
            </tbody>
          </table>
        </div>
        </form>
        
        <div class="body-foot">
          <div class="search">
          	<c:if test="${sFlag eq '1'}">
          		<c:choose>
          			<c:when test="${cupInfoMap.send_flag eq '0'}">
          				<a class="btn-large default" onclick="sendPush('CUP', '${cupInfoMap.cup_id}', '${cupInfoMap.cup_name }', '${ageGroup}', '${pushInfo.type_id}', '${pushInfo.case_title}', '${pushInfo.case_text}');">알림발송</a>
          			</c:when>
          			<c:otherwise>
          				<a class="btn-large gray-o" style="cursor: default;">알림발송</a>
          			</c:otherwise>
          		</c:choose>
          	</c:if>
          </div>
          <div class="others">
          	<c:choose>
          	<c:when test="${sFlag eq '0' }">
	            <a class="btn-large default" onclick="gotoCupInfo(0);">대회생성</a>
	            <a class="btn-large gray-o" onclick="gotoCupPrize(0);">대회생성 후 개요등록</a> 
          	</c:when>
          	<c:when test="${sFlag eq '1' }">
          		<a class="btn-large gray-o" onclick="gotoCupInfo(1);">대회수정</a>
	            <a class="btn-large gray-o" onclick="gotoCupPrize(1);">대회수정 후 개요등록</a> 
          	</c:when>
          	</c:choose>
          </div>
        </div>
  <div>
</body>

<form name="cfrm" id="cfrm" method="post"  action="cup">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

</html>