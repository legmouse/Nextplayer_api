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
	var U18CupMatch = "${U18CupMatch}";
	console.log('--- ageGroup :'+ ageGroup );
	console.log('--- U18CupMatch :'+ U18CupMatch );
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

// 연령대 이동
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

const fnMoveDetailMgr = (cupId, matchType, groups, ageGroup) => {

	let url = "";

	if (matchType == 'sub') {
		url = "/cupMgrSubMatch?ageGroup=" + ageGroup + "&cupId=" + cupId + "&groups=" + groups;
	} else if (matchType == 'tour') {
		url = "/cupMgrTourMatch";
		url = "/cupMgrTourMatch?ageGroup=" + ageGroup + "&cupId=" + cupId + "&round=" + groups;
	} else if (matchType == 'main') {
		url = "/cupMgrMainMatch?ageGroup=" + ageGroup + "&cupId=" + cupId + "&groups=" + groups;
	}

	window.open(url, "_blank");
}

const fnMoveDetailTeamMgr = (teamId, ageGroup) => {
	window.open('/teamMgrDet?ageGroup=' + ageGroup + "&teamId=" + teamId, "_blank");
}

function pushReg(method, title, text) {
	var date = $('#pushDate').val();
	var time = $('#pushTime option:selected').val();
	var minutes = $('#pushMinute option:selected').val();
	var typeId = $('input[name="typeId"]').val();
	
    $("input[name=ck]:checked").each(function () {
    	var ckvalue = $(this).val()
        var cupname = $(this).data('cupname');
        var uage = $(this).data('uage');
        var home = $(this).data('home');
        var away = $(this).data('away');
        var homeId = $(this).data('homeid');
        var awayId = $(this).data('awayid');
        var matchDate = $(this).data('matchdate');
        var param = {
            ageGroup : uage,
            cupId: ckvalue,
        };
        var bodyStr = '';
    	switch(method) {
			case 'START':
				bodyStr = text.replace('팀명', home);
				bodyStr = bodyStr.replace('팀명', away);
				
				var day = new Date(matchDate);
				var hour = day.getHours();
				var minute = day.getMinutes();
				var hourStr = '';
				var minuteStr = '';
				if (hour < 10) {
					hourStr = '0' + hour;
				} else {
					hourStr = hour;
				}
				if (minute < 10) {
					minuteStr = '0' + minute;
				} else {
					minuteStr = minute;
				}
				bodyStr = bodyStr.replace('hh', hourStr);
				bodyStr = bodyStr.replace('mm', minuteStr);
				break;
			case 'END':
				bodyStr = text.replace('팀명', home);
				bodyStr = bodyStr.replace('팀명', away);
				break;
			case 'LINEUP':
				bodyStr = text.replace('팀명', home);
				bodyStr = bodyStr.replace('팀명', away);
				break;
		}
        var data = {
            title: title,
            body: bodyStr,
            uage,
            home: homeId,
            away: awayId,
            path: encodeURIComponent('/contest/' + ckvalue + '?ageGroup=' + uage),
            param: JSON.stringify(param),
            description: '대회 상세로 이동',
            method: method,
            date: date + ' ' + time + ':' + minutes + ':00',
            autoFlag : 1,
            typeId: typeId,
            androidApp: 0,
            iosApp: 0,
            ipadApp: 0,
            authFlag: 0
        };

        $.ajax({
            type: 'POST',
            url: '/insertContestPush',
            dataType: 'json',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(data),
            success: function (res) {
                console.log(res);
            }
        });

    });

    alert('푸시 등록 완료');
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
				<h2><span></span>대회정보 > 경기목록</h2>
			</div>
			<div class="others">
			</div>
		</div>

		<div class="round body" style="overflow: hidden;">
			<div class="body-head" style="float: left;">
				<div class="search">
					<h3>경기 검색</h3>
					<form name="sfrm" id="sfrm" method="post"  action="selectCupMatch" onsubmit="return false;">
						<input type="hidden" name="typeId" value="${typeId}">
						<input type="hidden" name="method" value="${method}">
						<input type="hidden" name="bodyText" value="${bodyText}">
						<input type="hidden" name="titleText" value="${titleText}">
						<input type="date" name="sdate" value="${sdate}">
						<button class="" onclick="gotoSearch();" style="">검색</button>
					</form>
				</div>
			</div>
			<div class="body-head" style="float: right;">
				<div class="search">
					<h3>발송시간 선택</h3>
					<div>
					<c:set var="now" value="<%=new java.util.Date()%>" />
					<fmt:formatDate value="${now}" var="curDate" pattern="yyyy-MM-dd" />
					<fmt:formatDate value="${now}" var="curHour" pattern="HH" />
					<fmt:formatDate value="${now}" var="curMinute" pattern="mm" />
					<span class="title ml_10">일자</span>
							<input type="date" id="pushDate" name="sDate" value="${curDate}">
							<span class="title ml_10">시간</span>
							<select id="pushTime" name="sTime">
								<option value="">시간 선택</option>
								<c:forEach begin="0" end="23" var="hour">
									<option value="${hour < 10 ? '0' : ''}${hour}"<c:if test="${hour eq curHour}"> selected</c:if>>${hour < 10 ? '0' : ''}${hour}</option>
								</c:forEach>
							</select>
							<select id="pushMinute" name="sMinute">
								<option value="">분 선택</option>
								<c:forEach begin="0" end="50" var="minute">
									<option value="${minute < 10 ? '0' : ''}${minute}"<c:if test="${minute eq curMinute}">selected</c:if>>${minute < 10 ? '0' : ''}${minute}</option>
								</c:forEach>
							</select>
                	<button class="btn-large default" onclick="pushReg('${method}', ${titleText}, '${bodyText}')">푸시 발송</button>
                	</div>
            	</div>
			</div>
			<div class="scroll">
				<h2>
					U18
				</h2>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="10%">
						<col width="15">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="8%">
					</colgroup>
					<thead>
						<tr>
							<th>
								대회 이름
							</th>
							<th>
								경기 일시
							</th>
							<th>
								경기 장소
							</th>
							<th>
								홈 엠블럼
							</th>
							<th>
								홈 팀
							</th>
							<th>
								홈 점수
							</th>
							<th>
								홈 PK
							</th>
							<th>
								어웨이 PK
							</th>
							<th>
								어웨이 점수
							</th>
							<th>
								어웨이 팀
							</th>
							<th>
								어웨이 엠블럼
							</th>
							<th>
								선택
							</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty U18CupMatch}">
								<tr>
									<td colspan="12">
										등록된 경기가 없습니다.
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach var="result" items="${U18CupMatch}" varStatus="status">
									<tr>
										<td>
											<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U18')">${result.cup_name}</a>
										</td>
										<td>
											${result.place}
										</td>
										<td>
											${result.match_date}
										</td>
										<td>
											<c:choose>
												<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
												<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
											</c:choose>
										</td>
										<td>
											<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U18')">${result.home}</a>
										</td>
										<td>
											${result.home_score}
										</td>
										<td>
											${result.home_pk}
										</td>
										<td>
											${result.away_pk}
										</td>
										<td>
											${result.away_score}
										</td>
										<td>
											<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U18')">${result.away}</a>
										</td>
										<td>
											<c:choose>
												<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
												<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
											</c:choose>
										</td>
										<td>
                                			<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.match_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}" data-matchdate="${result.match_date }">
                                			<label for="ck_${result.match_id}">
                                			</label>
                            			</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>

		</div>
		<br/>
		<div class="round body">


			<div class="title">
				<h2>
					U17
				</h2>
			</div>
			<div class="scroll">
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="10%">
						<col width="15">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="8%">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							경기 일시
						</th>
						<th>
							경기 장소
						</th>
						<th>
							홈 엠블럼
						</th>
						<th>
							홈 팀
						</th>
						<th>
							홈 점수
						</th>
						<th>
							홈 PK
						</th>
						<th>
							어웨이 PK
						</th>
						<th>
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
						<th>
							선택
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U17CupMatch}">
							<tr>
								<td colspan="12">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U17CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U17')">${result.cup_name}</a>
									</td>
									<td>
											${result.place}
									</td>
									<td>
											${result.match_date}
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U17')">${result.home}</a>
									</td>
									<td>
											${result.home_score}
									</td>
									<td>
											${result.home_pk}
									</td>
									<td>
											${result.away_pk}
									</td>
									<td>
											${result.away_score}
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U17')">${result.away}</a>
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
                                		<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                		<label for="ck_${result.cup_id}">
                                		</label>
                            		</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>

		</div>

		<br/>

		<div class="round body">

			<div class="title">
				<h2>
					U15
				</h2>
			</div>
			<div class="scroll">
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="10%">
						<col width="15">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="8%">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							경기 일시
						</th>
						<th>
							경기 장소
						</th>
						<th>
							홈 엠블럼
						</th>
						<th>
							홈 팀
						</th>
						<th>
							홈 점수
						</th>
						<th>
							홈 PK
						</th>
						<th>
							어웨이 PK
						</th>
						<th>
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
						<th>
							선택
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U15CupMatch}">
							<tr>
								<td colspan="12">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U15CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U15')">${result.cup_name}</a>
									</td>
									<td>
											${result.place}
									</td>
									<td>
											${result.match_date}
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U15')">${result.home}</a>
									</td>
									<td>
											${result.home_score}
									</td>
									<td>
											${result.home_pk}
									</td>
									<td>
											${result.away_pk}
									</td>
									<td>
											${result.away_score}
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U15')">${result.away}</a>
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
                                		<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                		<label for="ck_${result.cup_id}">
                                		</label>
                            		</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>
		</div>

		<br />

		<div class="round body">

			<div class="title">
				<h2>
					U14
				</h2>
			</div>
			<div class="scroll">
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="10%">
						<col width="15">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="*">
						<col width="25%">
						<col width="*">
						<col width="8%">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							경기 일시
						</th>
						<th>
							경기 장소
						</th>
						<th>
							홈 엠블럼
						</th>
						<th>
							홈 팀
						</th>
						<th>
							홈 점수
						</th>
						<th>
							홈 PK
						</th>
						<th>
							어웨이 PK
						</th>
						<th>
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
						<th>
							선택
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U14CupMatch}">
							<tr>
								<td colspan="12">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U14CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U14')">${result.cup_name}</a>
									</td>
									<td>
											${result.place}
									</td>
									<td>
											${result.match_date}
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U14')">${result.home}</a>
									</td>
									<td>
											${result.home_score}
									</td>
									<td>
											${result.home_pk}
									</td>
									<td>
											${result.away_pk}
									</td>
									<td>
											${result.away_score}
									</td>
									<td>
										<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U14')">${result.away}</a>
									</td>
									<td>
										<c:choose>
											<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
											<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
										</c:choose>
									</td>
									<td>
                                		<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                		<label for="ck_${result.cup_id}">
                                		</label>
                            		</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>
		</div>

	<br/>

	<div class="round body">

		<div class="title">
			<h2>
				U12
			</h2>
		</div>
		<div class="scroll">
			<table cellspacing="0" class="update">
				<colgroup>
					<col width="25%">
					<col width="10%">
					<col width="15">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="8%">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						경기 일시
					</th>
					<th>
						경기 장소
					</th>
					<th>
						홈 엠블럼
					</th>
					<th>
						홈 팀
					</th>
					<th>
						홈 점수
					</th>
					<th>
						홈 PK
					</th>
					<th>
						어웨이 PK
					</th>
					<th>
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
					<th>
						선택
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U12CupMatch}">
						<tr>
							<td colspan="12">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U12CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U12')">${result.cup_name}</a>
								</td>
								<td>
										${result.place}
								</td>
								<td>
										${result.match_date}
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U12')">${result.home}</a>
								</td>
								<td>
										${result.home_score}
								</td>
								<td>
										${result.home_pk}
								</td>
								<td>
										${result.away_pk}
								</td>
								<td>
										${result.away_score}
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U12')">${result.away}</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
                                	<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                	<label for="ck_${result.cup_id}">
                                	</label>
                            	</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>

	</div>

	<br />

	<div class="round body">

		<div class="title">
			<h2>
				U11
			</h2>
		</div>
		<div class="scroll">
			<table cellspacing="0" class="update">
				<colgroup>
					<col width="25%">
					<col width="10%">
					<col width="15">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="8%">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						경기 일시
					</th>
					<th>
						경기 장소
					</th>
					<th>
						홈 엠블럼
					</th>
					<th>
						홈 팀
					</th>
					<th>
						홈 점수
					</th>
					<th>
						홈 PK
					</th>
					<th>
						어웨이 PK
					</th>
					<th>
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
					<th>
						선택
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U11CupMatch}">
						<tr>
							<td colspan="12">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U11CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U11')">${result.cup_name}</a>
								</td>
								<td>
										${result.place}
								</td>
								<td>
										${result.match_date}
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U11')">${result.home}</a>
								</td>
								<td>
										${result.home_score}
								</td>
								<td>
										${result.home_pk}
								</td>
								<td>
										${result.away_pk}
								</td>
								<td>
										${result.away_score}
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U11')">${result.away}</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
                                	<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                	<label for="ck_${result.cup_id}">
                                	</label>
                            	</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>

	</div>

	<br />

	<div class="round body">

		<div class="title">
			<h2>
				U20
			</h2>
		</div>
		<div class="scroll">
			<table cellspacing="0" class="update">
				<colgroup>
					<col width="25%">
					<col width="10%">
					<col width="15">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="8%">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						경기 일시
					</th>
					<th>
						경기 장소
					</th>
					<th>
						홈 엠블럼
					</th>
					<th>
						홈 팀
					</th>
					<th>
						홈 점수
					</th>
					<th>
						홈 PK
					</th>
					<th>
						어웨이 PK
					</th>
					<th>
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
					<th>
						선택
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty u20CupMatch}">
						<tr>
							<td colspan="12">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U20CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U20')">${result.cup_name}</a>
								</td>
								<td>
										${result.place}
								</td>
								<td>
										${result.match_date}
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U20')">${result.home}</a>
								</td>
								<td>
										${result.home_score}
								</td>
								<td>
										${result.home_pk}
								</td>
								<td>
										${result.away_pk}
								</td>
								<td>
										${result.away_score}
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U20')">${result.away}</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
                                	<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                	<label for="ck_${result.cup_id}">
                                	</label>
                            	</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>

	</div>

	<br />

	<div class="round body">

		<div class="title">
			<h2>
				U22
			</h2>
		</div>
		<div class="scroll">
			<table cellspacing="0" class="update">
				<colgroup>
					<col width="25%">
					<col width="10%">
					<col width="15">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="*">
					<col width="25%">
					<col width="*">
					<col width="8%">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						경기 일시
					</th>
					<th>
						경기 장소
					</th>
					<th>
						홈 엠블럼
					</th>
					<th>
						홈 팀
					</th>
					<th>
						홈 점수
					</th>
					<th>
						홈 PK
					</th>
					<th>
						어웨이 PK
					</th>
					<th>
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
					<th>
						선택
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U22CupMatch}">
						<tr>
							<td colspan="12">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U22CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_type}', '${result.groups}', 'U22')">${result.cup_name}</a>
								</td>
								<td>
										${result.place}
								</td>
								<td>
										${result.match_date}
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.home_id}', 'U22')">${result.home}</a>
								</td>
								<td>
										${result.home_score}
								</td>
								<td>
										${result.home_pk}
								</td>
								<td>
										${result.away_pk}
								</td>
								<td>
										${result.away_score}
								</td>
								<td>
									<a class="title" onclick="fnMoveDetailTeamMgr('${result.away_id}', 'U22')">${result.away}</a>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
										<c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
									</c:choose>
								</td>
								<td>
                                	<input type="checkbox" name="ck" data-cupid="${result.cup_id}" data-cupname="${result.cup_name}"id="ck_${result.cup_id}" value="${result.cup_id}" data-uage="U18" data-home="${result.home}" data-away="${result.away}" data-homeId="${result.home_id}" data-awayId="${result.away_id}">
                                	<label for="ck_${result.cup_id}">
                                	</label>
                            	</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
		</div>

	</div>

  </div>
</body>


</html>