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
				<h2><span></span>대회정보 > 등록/수정 > 매치 상세</h2>
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
				<table cellspacing="0" class="update fixed">
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
							<c:if test="${cupInfoMap.cup_type eq '4'}">풀리그 + 풀리그</c:if>
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
						<th>예선본선 조편성</th>
						<td class="tl">${cupInfoMap.sub_team_count} , ${cupInfoMap.main_team_count}</td>
						<th>토너먼트 타입</th>
						<td class="tl">
							<c:if test="${cupInfoMap.tour_type eq '0'}">대진표</c:if>
							<c:if test="${cupInfoMap.tour_type eq '1'}">추첨제</c:if>
						</td>
						<th>토너먼트 팀수</th>
						<td class="tl">${cupInfoMap.tour_team}팀</td>
					</tr>
					</tbody>
				</table>
			</div>
			</br>
			<div style="width: 45%;float: left;">
				<div class="title">
					<h3>
						홈 감독/코치
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="30%">
						<col width="70%">
					</colgroup>
					<thead>
					<th>직급</th>
					<th>이름</th>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty homeStaffPlayDataList}">
							<tr>
								<td colspan="2">등록된 데이터가 없습니다</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="item" items="${homeStaffPlayDataList}">
								<tr>
									<td>
											${item.rank}
									</td>
									<td>
											${item.staff_name}
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>
			<div style="width: 45%;float: left; margin-left: 10%">
				<div class="title">
					<h3>
						어웨이 감독/코치
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="30%">
						<col width="70%">
					</colgroup>
					<thead>
					<th>직급</th>
					<th>이름</th>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty awayStaffPlayDataList}">
							<tr>
								<td colspan="2">등록된 데이터가 없습니다</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="item" items="${awayStaffPlayDataList}">
								<tr>
									<td>
											${item.rank}
									</td>
									<td>
											${item.staff_name}
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>
			</br>
			<div style="width: 45%;float: left;clear: both;">
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
			<br>

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
			<div style="width: 45%;float: left;clear: both;">
				<div class="title">
					<h3>
						홈 코치/감독 경고/퇴장
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="25%">
						<col width="25%">
						<col width="25%">
					</colgroup>
					<thead>
					<th>시간</th>
					<th>직급</th>
					<th>이름</th>
					<th>종류</th>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty homeStaffPenaltyDataList}">
							<tr>
								<td colspan="4">
									등록된 데이터가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="e" items="${homeStaffPenaltyDataList}" varStatus="status">
								<tr>
									<td class="tl">
											${e.time}
									</td>
									<td class="tl">
											${e.rank}
									</td>
									<td class="tl">
											${e.staff_name}
									</td>
									<td class="tl">
											${e.penalty_type}
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>

			<div style="width: 45%;float: left; margin-left: 10%;">
				<div class="title">
					<h3>
						어웨이 코치/감독 경고/퇴장
					</h3>
				</div>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="25%">
						<col width="25%">
						<col width="25%">
					</colgroup>
					<thead>
					<th>시간</th>
					<th>직급</th>
					<th>이름</th>
					<th>종류</th>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty awayStaffPenaltyDataList}">
							<tr>
								<td colspan="4">
									등록된 데이터가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="e" items="${awayStaffPenaltyDataList}" varStatus="status">
								<tr>
									<td class="tl">
											${e.time}
									</td>
									<td class="tl">
											${e.rank}
									</td>
									<td class="tl">
											${e.staff_name}
									</td>
									<td class="tl">
											${e.penalty_type}
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
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