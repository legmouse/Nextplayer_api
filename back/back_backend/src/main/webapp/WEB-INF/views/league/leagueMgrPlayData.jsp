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
<!-- ExcelJS 라이브러리 로드 -->
<script src="resources/js/exceljs.min.js"></script>
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

});

//대회 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.cmgrfrm.submit();
}

//대회 관리 리스트 이동
function gotoCupMgr() {
	document.cmgrfrm.submit();
}

</script>
</head>
<body>
<div class="wrapper" id="wrapper">
	<jsp:include page="../common/menu.jsp" flush="true">
		<jsp:param name="page" value="main"/>
		<jsp:param name="main" value="0"/>
	</jsp:include>

	<div class="contents active">
		<div class="head">
			<div class="sub-menu">
				<h2><span></span>리그정보 > 관리 > 매치 상세</h2>
				<c:forEach var="result" items="${uageList}" varStatus="status">
					<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
				</c:forEach>
			</div>

		</div>
		<div class="round body" style="min-height: 1500px;">

			<div class="title">
				<h3>
					기본정보
				</h3>
			</div>

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
							<c:if test="${!empty leagueInfoMap.sdate }">
								${leagueInfoMap.sdate} ~ ${leagueInfoMap.edate}
							</c:if>
						</td>
						<th>참가팀</th>
						<td class="tl"><c:if test="${!empty leagueInfoMap.sdate }">${leagueInfoMap.ltCnt}팀</c:if></td>
					</tr>

					</tbody>
				</table>
			</div>

			</br>
			<div style="width: 45%;float: left;">
				<div class="title">
					<h3>
						홈 선발선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
					</colgroup>
					<thead>
					<th>배번</th>
					<th>선수명</th>
					<th>포지션</th>
					<th>득점</th>
					<th>도움</th>
					<th>경고</th>
					<th>퇴장</th>
					<th>pso</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${homeSelectionPlayDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.player_number}
							</td>
							<td class="tl" <c:if test="${e.player_id eq 0}"> style="color: red" </c:if> >
									${e.player_name}
							</td>
							<td class="tl">
									${e.position}
							</td>
							<td class="tl">
									${e.goal}
							</td>
							<td class="tl">
									${e.help}
							</td>
							<td class="tl">
									${e.warning}
							</td>
							<td class="tl">
									${e.gexit}
							</td>
							<td class="tl">
									${e.pso}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>

			<div style="width: 45%;float: left; margin-left: 10%">
				<div class="title">
					<h3>
						어웨이 선발선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
					</colgroup>
					<thead>
					<th>배번</th>
					<th>선수명</th>
					<th>포지션</th>
					<th>득점</th>
					<th>도움</th>
					<th>경고</th>
					<th>퇴장</th>
					<th>pso</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${awaySelectionPlayDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.player_number}
							</td>
							<td class="tl" <c:if test="${e.player_id eq 0}"> style="color: red" </c:if> >
									${e.player_name}
							</td>
							<td class="tl">
									${e.position}
							</td>
							<td class="tl">
									${e.goal}
							</td>
							<td class="tl">
									${e.help}
							</td>
							<td class="tl">
									${e.warning}
							</td>
							<td class="tl">
									${e.gexit}
							</td>
							<td class="tl">
									${e.pso}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>

			</br>
			<div style="width: 45%;float: left;clear: both;">
				<div class="title">
					<h3>
						홈 후발선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
					</colgroup>
					<thead>
					<th>배번</th>
					<th>선수명</th>
					<th>포지션</th>
					<th>득점</th>
					<th>도움</th>
					<th>경고</th>
					<th>퇴장</th>
					<th>pso</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${homeCandidatePlayDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.player_number}
							</td>
							<td class="tl" <c:if test="${e.player_id eq 0}"> style="color: red" </c:if> >
									${e.player_name}
							</td>
							<td class="tl">
									${e.position}
							</td>
							<td class="tl">
									${e.goal}
							</td>
							<td class="tl">
									${e.help}
							</td>
							<td class="tl">
									${e.warning}
							</td>
							<td class="tl">
									${e.gexit}
							</td>
							<td class="tl">
									${e.pso}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>

			<div style="width: 45%;float: left; margin-left: 10%;">
				<div class="title">
					<h3>
						어웨이 후발선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
						<col width="12.5%">
					</colgroup>
					<thead>
					<th>배번</th>
					<th>선수명</th>
					<th>포지션</th>
					<th>득점</th>
					<th>도움</th>
					<th>경고</th>
					<th>퇴장</th>
					<th>pso</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${awayCandidatePlayDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.player_number}
							</td>
							<td class="tl" <c:if test="${e.player_id eq 0}"> style="color: red" </c:if> >
									${e.player_name}
							</td>
							<td class="tl">
									${e.position}
							</td>
							<td class="tl">
									${e.goal}
							</td>
							<td class="tl">
									${e.help}
							</td>
							<td class="tl">
									${e.warning}
							</td>
							<td class="tl">
									${e.gexit}
							</td>
							<td class="tl">
									${e.pso}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>
			<br/>

			<div style="width: 45%;float: left;clear: both;">
				<div class="title">
					<h3>
						홈 교체선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
					</colgroup>
					<thead>
					<th>시간</th>
					<th>배번</th>
					<th>IN</th>
					<th>배번</th>
					<th>OUT</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${homeChangeDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.time}
							</td>
							<td class="tl">
									${e.in_player_number}
							</td>
							<td class="tl" <c:if test="${e.in_player_id eq 0}"> style="color: red" </c:if> >
									${e.in_player_name}
							</td>
							<td class="tl">
									${e.out_player_number}
							</td>
							<td class="tl" <c:if test="${e.out_player_id eq 0}"> style="color: red" </c:if> >
									${e.out_player_name}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>
			<div style="width: 45%;float: left; margin-left: 10%;">
				<div class="title">
					<h3>
						어웨이 교체선수
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
						<col width="20%">
					</colgroup>
					<thead>
					<th>시간</th>
					<th>배번</th>
					<th>IN</th>
					<th>배번</th>
					<th>OUT</th>
					</thead>
					<tbody>
					<c:forEach var="e" items="${awayChangeDataList}" varStatus="status">
						<tr>
							<td class="tl">
									${e.time}
							</td>
							<td class="tl">
									${e.in_player_number}
							</td>
							<td class="tl" <c:if test="${e.in_player_id eq 0}"> style="color: red" </c:if> >
									${e.in_player_name}
							</td>
							<td class="tl">
									${e.out_player_number}
							</td>
							<td class="tl" <c:if test="${e.out_player_id eq 0}"> style="color: red" </c:if> >
									${e.out_player_name}
							</td>
						</tr>
					</c:forEach>

					</tbody>
				</table>
			</div>
			<br>
		</div>
	</div>

</div>
</body>

<form name="cmgrfrm" id="cmgrfrm" method="post"  action="cupMgr">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

</html>