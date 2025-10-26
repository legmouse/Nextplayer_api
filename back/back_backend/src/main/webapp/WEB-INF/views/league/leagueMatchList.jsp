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

const gotoLeagueMgrInfo = (ageGroup, matchDate, idx) => {
	const month = Number(matchDate.substring(5,7));
	window.open("/leagueMgrInfo?ageGroup="+ageGroup+"&leagueId=" + idx + "&months=" + month, "_blank");
}

const fnMoveDetailTeamMgr = (teamId, ageGroup) => {
	window.open('/teamMgrDet?ageGroup=' + ageGroup + "&teamId=" + teamId, "_blank");
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
				<h2><span></span>리그정보 > 경기목록</h2>
			</div>
			<div class="others">
			</div>
		</div>

		<div class="round body">
			<div class="body-head">
				<div class="search">
					<form name="sfrm" id="sfrm" method="post"  action="leagueMatchList" onsubmit="return false;">
						<input type="date" name="sdate" placeholder="리그명 입력" value="${sdate}" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
						<button class="" onclick="gotoSearch();" style="">검색</button>
					</form>

				</div>
			</div>

			<div class="title">
				<h2>
					U18
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
						<col width="25%">
						<col width="*">
					</colgroup>
					<thead>
						<tr>
							<th>
								리그 이름
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
								어웨이 점수
							</th>
							<th>
								어웨이 팀
							</th>
							<th>
								어웨이 엠블럼
							</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty U18LeagueMatch}">
								<tr>
									<td colspan="9">
										등록된 리그 경기가 없습니다.
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach var="result" items="${U18LeagueMatch}" varStatus="status">
									<tr>
										<td>
											<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U18', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
						<col width="25%">
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							리그 이름
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
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U17LeagueMatch}">
							<tr>
								<td colspan="9">
									등록된 리그 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U17LeagueMatch}" varStatus="status">
								<tr>
									<td>
										<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U17', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
						<col width="25%">
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							리그 이름
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
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U15LeagueMatch}">
							<tr>
								<td colspan="9">
									등록된 리그 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U15LeagueMatch}" varStatus="status">
								<tr>
									<td>
										<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U15', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
						<col width="25%">
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							리그 이름
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
							어웨이 점수
						</th>
						<th>
							어웨이 팀
						</th>
						<th>
							어웨이 엠블럼
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U14LeagueMatch}">
							<tr>
								<td colspan="9">
									등록된 리그 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U14LeagueMatch}" varStatus="status">
								<tr>
									<td>
										<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U14', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
					<col width="25%">
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						리그 이름
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
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U12LeagueMatch}">
						<tr>
							<td colspan="9">
								등록된 리그 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U12LeagueMatch}" varStatus="status">
							<tr>
								<td>
									<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U12', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
					<col width="25%">
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						리그 이름
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
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U11LeagueMatch}">
						<tr>
							<td colspan="9">
								등록된 리그 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U11LeagueMatch}" varStatus="status">
							<tr>
								<td>
									<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U11', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
					<col width="25%">
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						리그 이름
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
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U20LeagueMatch}">
						<tr>
							<td colspan="9">
								등록된 리그 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U20LeagueMatch}" varStatus="status">
							<tr>
								<td>
									<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U20', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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
					<col width="25%">
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						리그 이름
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
						어웨이 점수
					</th>
					<th>
						어웨이 팀
					</th>
					<th>
						어웨이 엠블럼
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U22LeagueMatch}">
						<tr>
							<td colspan="9">
								등록된 리그 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U22LeagueMatch}" varStatus="status">
							<tr>
								<td>
									<a style="text-decoration: underline" onclick="gotoLeagueMgrInfo('U22', '${result.match_date}',${result.league_id})"> ${result.league_name} </a>
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