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

//검색 
function gotoSearch(){
	//console.log('--- gotoSearch');
	document.sfrm.submit();
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

const crawPlay = (cupId, autoCrawDataFlag, age) => {
	const cupInfoTB = age.toLowerCase() + '_cup_info';
	if (autoCrawDataFlag == 0) {
		autoCrawDataFlag = 1;
	} else {
		autoCrawDataFlag = 0;
	}
	const data = {
		cupId,
		autoCrawDataFlag,
		cupInfoTB
	};

	$.ajax({
		type: 'POST',
		url: '/crawSetting',
		dataType: 'json',
		contentType : "application/json; charset=UTF-8",
		data: JSON.stringify(data),
		success: function (res) {
			console.log(res);
			location.reload();
		}
	});
}

const crawScore = (cupId, autoCrawFlag, age) => {

	const cupInfoTB = age.toLowerCase() + '_cup_info';

	if (autoCrawFlag == 0) {
		autoCrawFlag = 1;
	} else {
		autoCrawFlag = 0;
	}

	const data = {
		cupId,
		autoCrawFlag,
		cupInfoTB
	}

	$.ajax({
		type: 'POST',
		url: '/crawSetting',
		dataType: 'json',
		contentType : "application/json; charset=UTF-8",
		data: JSON.stringify(data),
		success: function (res) {
			console.log(res);
			location.reload();
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
				<h2><span></span>대회정보 > 경기목록</h2>
			</div>
			<div class="others">
			</div>
		</div>

		<div class="round body" style="overflow: hidden;">
			<div class="body-head" style="float: left;">
				<div class="search">
					<h3>경기 검색</h3>
					<form name="sfrm" id="sfrm" method="post"  action="crawSetting" onsubmit="return false;">
						<input type="hidden" name="typeId" value="${typeId}">
						<input type="hidden" name="method" value="${method}">
						<input type="hidden" name="bodyText" value="${bodyText}">
						<input type="hidden" name="titleText" value="${titleText}">
						<input type="date" name="sdate" value="${sdate}">
						<button class="" onclick="gotoSearch();" style="">검색</button>
					</form>
				</div>
			</div>
			<div class="scroll">
				<h2>
					U18
				</h2>
				<table cellspacing="0" class="update">
					<colgroup>
						<col width="25%">
						<col width="*">
					</colgroup>
					<thead>
						<tr>
							<th>
								대회 이름
							</th>
							<th>
								데이터 수집
							</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty U18CupMatch}">
								<tr>
									<td colspan="2">
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
											<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U18')">경기 결과 수집</a>
											<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U18')">라인업 수집</a>
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
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							데이터 수집
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U17CupMatch}">
							<tr>
								<td colspan="2">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U17CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U17')">${result.cup_name}</a>
									</td>
									<td>
										<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U17')">경기 결과 수집</a>
										<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U17')">라인업 수집</a>
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
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							데이터 수집
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U15CupMatch}">
							<tr>
								<td colspan="2">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U15CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U15')">${result.cup_name}</a>
									</td>
									<td>
										<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U15')">경기 결과 수집</a>
										<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U15')">라인업 수집</a>
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
						<col width="*">
					</colgroup>
					<thead>
					<tr>
						<th>
							대회 이름
						</th>
						<th>
							데이터 수집
						</th>
					</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${empty U14CupMatch}">
							<tr>
								<td colspan="2">
									등록된 경기가 없습니다.
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="result" items="${U14CupMatch}" varStatus="status">
								<tr>
									<td>
										<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U14')">${result.cup_name}</a>
									</td>
									<td>
										<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U14')">경기 결과 수집</a>
										<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U14')">라인업 수집</a>
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
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						데이터 수집
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U12CupMatch}">
						<tr>
							<td colspan="2">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U12CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U12')">${result.cup_name}</a>
								</td>
								<td>
									<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U12')">경기 결과 수집</a>
									<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U12')">라인업 수집</a>
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
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						데이터 수집
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U11CupMatch}">
						<tr>
							<td colspan="2">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U11CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U11')">${result.cup_name}</a>
								</td>
									<td>
										<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U11')">경기 결과 수집</a>
										<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U11')">라인업 수집</a>
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
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						데이터 수집
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U20CupMatch}">
						<tr>
							<td colspan="2">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U20CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U20')">${result.cup_name}</a>
								</td>
								<td>
									<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U20')">경기 결과 수집</a>
									<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U20')">라인업 수집</a>
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
					<col width="*">
				</colgroup>
				<thead>
				<tr>
					<th>
						대회 이름
					</th>
					<th>
						데이터 수집
					</th>
				</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${empty U22CupMatch}">
						<tr>
							<td colspan="2">
								등록된 경기가 없습니다.
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="result" items="${U22CupMatch}" varStatus="status">
							<tr>
								<td>
									<a class="title" onclick="fnMoveDetailMgr('${result.cup_id}', '${result.match_category}', '${result.groups}', 'U22')">${result.cup_name}</a>
								</td>
								<td>
									<a class="${result.auto_craw_flag eq '1'? 'cored':'black'}" onclick="crawScore('${result.cup_id}', '${result.auto_craw_flag}', 'U22')">경기 결과 수집</a>
									<a class="${result.auto_craw_data_flag eq '1'? 'cored':'black'}" onclick="crawPlay('${result.cup_id}', '${result.auto_craw_data_flag}', 'U22')">라인업 수집</a>
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