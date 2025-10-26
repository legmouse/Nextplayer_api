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
<script type="text/javascript" src="resources/apexcharts/apexcharts.min.js"></script>
<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">
<link rel="stylesheet" href="resources/apexcharts/apexcharts.css">

<style>
  .apexcharts-menu-icon{
    display: none;
  }
</style>

<script type="text/javascript">

var _cate = [<c:forEach items="${teamAvgGoalList}" var="result" varStatus="status">
	"${result.play_year}"
	<c:if test="${status.last eq false}">,</c:if></c:forEach>
];

var _series = [{
		name: "득점", data : [<c:forEach items="${teamAvgGoalList}" var="result" varStatus="status">
		"${result.avg_gf}"
		<c:if test="${status.last eq false}">,</c:if></c:forEach>]
	},{
		name: "실점", data : [<c:forEach items="${teamAvgGoalList}" var="result" varStatus="status">
		"${result.avg_ga}"
		<c:if test="${status.last eq false}">,</c:if></c:forEach>]
	}
];

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
	var sArea = "${sArea}";
	var sTeamType = "${sTeamType}";
	$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
	$("input[name=sNickName]").val("${sNickName}");
	
	
	makeChart();	//call chart 
});

function makeChart(){
	var options = {
            chart: {
                height: 230,
                type: 'area',
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'straight',
                width: '1',
            },
            fill: {
              colors: ['rgba(255,0,0,.1)', 'rgba(0,0,0,.1)'],
              type: 'solid'
            },
            colors: ['rgba(255,0,0)', '#545454'],
            grid: {
              borderColor: '#e7e7e7',
            },
            series: [],
            xaxis: {
                categories: [],
            },
            legend: {
                position: 'top',
                horizontalAlign: 'right',
                offsetY:0
            },
        }
	
	options.series = _series;
	options.xaxis.categories = _cate;
	
    var chart = new ApexCharts(document.querySelector("#chart-1"),options);
    chart.render();
}




function gotoAgeGroup(ageGroup){
	  clearFrmData("frm");
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=cp]").val(1);
	  document.frm.submit();
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




//팀 상세 리스트 이동
function gotoTeamMgr(){
	document.tmfrm.submit();
}

//대회관리 - 예선 이동
function gotoCupMgrSubMatch(cupId){
	$('#cmsmfrm input[name=cupId]').val(cupId);
	document.cmsmfrm.submit();
}

//대회관리 - 토너먼트 이동
function gotoCupMgrTourMatch(cupId){
	$('#cmtmfrm input[name=cupId]').val(cupId);
	document.cmtmfrm.submit();
}

//클럽관리 - 요약정보 - 연도 이동
function gototeamMgrDetYear(year){
	$('#tmdfrm input[name=sYear]').val(year);
	document.tmdfrm.submit();
}

//클럽관리 - 요약정보 
function gototeamMgrDet(){
	document.tmdfrm.submit();
}
//클럽관리 - 대회정보 
function gototeamMgrDetCup(){
	document.tmdcfrm.submit();
}
//클럽관리 - 리그정보 
function gototeamMgrDetLeague(){
	document.tmdlfrm.submit();
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
  		  	<h2><span></span>학원·클럽 > 관리</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
        <div class="others">
          <!-- 현재년도 --> 
		  <c:set var="now" value="<%=new java.util.Date()%>" />
		  <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set> 	
          <select name="selYears" id="selYears" onchange="gotoYear(this.value);">
          	<c:forEach var="i" begin="2014" end="${sysYear }" step="1">
          		<c:choose>
	          	<c:when test="${i eq sYear}">
	          		<option value="${i}" selected>${i}</option>
	          	</c:when> 
	          	<c:when test="${empty sYear and i eq sysYear}">
	          		<option value="${i}" selected>${i}</option>
	          	</c:when> 
	          	<c:otherwise>
	          		<option value="${i}">${i}</option>
	          	</c:otherwise>
	          	</c:choose> 
          	</c:forEach>
          </select>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="frm" id="frm" method="post"  action="teamMgr" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="">
  			  <input name="sYear" type="hidden" value="${sYear}">
  			  
	          <select class="w10" name="sArea">
	          <option value="-1" selected>광역 선택</option>
	          <c:forEach var="result" items="${areaList}" varStatus="status">
	          	<option value="${result.area_name}">${result.area_name}</option>
	          </c:forEach>
	          </select>
	          <select class="w10" name="sTeamType">
	            <option value="-1">학원/클럽 선택</option>
	            <option value="0">학원</option>
	            <option value="1">클럽</option>
	            <option value="2">유스</option>
	          </select>
              <input type="text" name="sNickName" placeholder="사용명칭 학원/클럽명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoTeamMgr();"><i class="xi-long-arrow-left"></i> 학원/클럽관리 리스트</a>
          </div>
        </div>
        <br>
        <div class="body-head" style="border-bottom: 1px solid #eee;margin-bottom: 15px">
	        <c:choose>
	       	<c:when test="${empty teamInfoMap.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	       	<c:otherwise><img src="/NP${teamInfoMap.emblem}" class="logo"></c:otherwise>
	       	</c:choose>
          <h3>
          
          	<c:choose>
          	<c:when test="${teamInfoMap.team_type eq '0'}"><span class="label blue">학원</span></c:when>
          	<c:when test="${teamInfoMap.team_type eq '1'}"><span class="label green">클럽</span></c:when>
          	<c:when test="${teamInfoMap.team_type eq '2'}"><span class="label red">유스</span></c:when>
          	</c:choose>
		              	
            ${teamInfoMap.nick_name}
            <a class="btn-large gray-o btn-pop" data-id="modefy-school-single">정보수정</a>
            <br>
            <span class="summary">${teamInfoMap.addr}</span>
          </h3>
          <div class="others">
            <div class="tab">
              <a class="active" onclick="gototeamMgrDet();">요약정보</a>
              <a onclick="gototeamMgrDetCup();">대회정보</a>
              <a onclick="gototeamMgrDetLeague();">리그정보</a>
            </div>
          </div>
       </div>
       
       <div class="body-body">
          
          <div>
			<!-- 현재년도 --> 
			  <c:set var="now" value="<%=new java.util.Date()%>" />
			  <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set> 	
	          	<c:forEach var="i" begin="2014" end="${sysYear}" step="1" varStatus="status">
	          		<c:choose>
		          	<c:when test="${(sysYear - status.count + 1) eq sYear}">
		          		<a class="btn-large default" onclick="gototeamMgrDetYear('${sysYear - status.count + 1 }');">${sysYear - status.count + 1 }</a>
		          	</c:when> 
		          	<c:when test="${empty sYear and (sysYear - status.count + 1) eq sysYear}">
		          		<a class="btn-large default" onclick="gototeamMgrDetYear('${sysYear - status.count + 1 }');">${sysYear - stuatus.count + 1}</a>
		          	</c:when> 
		          	<c:otherwise>
		          		<a class="btn-large gray-o" onclick="gototeamMgrDetYear('${sysYear - status.count + 1 }');">${sysYear - status.count + 1}</a>
		          	</c:otherwise>
		          	</c:choose>
	          	</c:forEach>
          	            
          </div>
          <div class="left w65 mobi"><br>
            <h4>연도별 평균 득/실점 추이</h4>
            <div id="chart-1"></div>
            
            
            <br>
            <h4 style="display: inline-block">
            	<c:choose>
            	<c:when test="${empty sYear}">${sysYear}년 경기 결과</c:when>
            	<c:otherwise>${sYear}년 경기 결과</c:otherwise>
            	</c:choose>
            	(
            	전체:${teamMatchResultMap.t_won}<c:if test="${empty teamMatchResultMap.t_won}">0</c:if>승 ${teamMatchResultMap.t_draw}<c:if test="${empty teamMatchResultMap.t_draw}">0</c:if>무 ${teamMatchResultMap.t_lost}<c:if test="${empty teamMatchResultMap.t_lost}">0</c:if>패 |
            	대회:${teamMatchResultMap.c_won}<c:if test="${empty teamMatchResultMap.c_won}">0</c:if>승 ${teamMatchResultMap.c_draw}<c:if test="${empty teamMatchResultMap.c_draw}">0</c:if>무 ${teamMatchResultMap.c_lost}<c:if test="${empty teamMatchResultMap.c_lost}">0</c:if>패 |
            	리그:${teamMatchResultMap.l_won}<c:if test="${empty teamMatchResultMap.l_won}">0</c:if>승 ${teamMatchResultMap.l_draw}<c:if test="${empty teamMatchResultMap.l_draw}">0</c:if>무 ${teamMatchResultMap.l_lost}<c:if test="${empty teamMatchResultMap.l_lost}">0</c:if>패
            	)
            </h4>
            <div class="others" style="float: right;">
              <a class="btn-large default btn-tab" data-id="num1">전체</a>
              <a class="btn-large gray btn-tab" data-id="num2">대회</a>
              <a class="btn-large gray btn-tab" data-id="num3">리그</a>
            </div>

            <br><br>
            
            <div class="show-tab ac" id="num1">
            <c:if test="${empty teamTotalMatchList}">
            등록된 내역이 없습니다.
            </c:if>

            <c:forEach var="result" items="${teamTotalMatchList}" varStatus="status">
              <div class="team-history">
                <c:choose>
               	<c:when test="${teamInfoMap.team_id eq result.home_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.away_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<c:choose>
		          		<c:when test="${result.home_pk > result.away_pk}">
		          			<div class="result win" style="background-image: url(/NP${result.away_emblem})">
			                  <span>승</span>
			                </div>
		          		</c:when>
		          		<c:when test="${result.home_pk < result.away_pk}">
		          			<div class="result lose" style="background-image: url(/NP${result.away_emblem})">
			                  <span>패</span>
			                </div>
		          		</c:when>
		          		<c:otherwise>
			          		<div class="result" style="background-image: url(/NP${result.away_emblem})">
			                  <span>무</span>
			                </div>
		          		</c:otherwise> 
		          		</c:choose>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.away_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	</c:choose> 
               	</c:when>
               	<c:when test="${teamInfoMap.team_id eq result.away_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.home_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<c:choose>
		          		<c:when test="${result.home_pk > result.away_pk}">
		          			<div class="result lose" style="background-image: url(/NP${result.home_emblem})">
			                  <span>패</span>
			                </div>
		          		</c:when>
		          		<c:when test="${result.home_pk < result.away_pk}">
		          			<div class="result win" style="background-image: url(/NP${result.home_emblem})">
			                  <span>승</span>
			                </div>
		          		</c:when>
		          		<c:otherwise>
			          		<div class="result" style="background-image: url(/NP${result.home_emblem})">
			                  <span>무</span>
			                </div>
		          		</c:otherwise> 
		          		</c:choose>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.home_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	</c:choose>
               	</c:when>
               	</c:choose>
               	
               	<c:choose>
               	<c:when test="${result.total_type eq 1}"><i class="l">리</i></c:when>
               	<c:when test="${result.total_type eq 2}"><i class="c">대</i></c:when>
               	</c:choose>
                
                <p class="score">
                	<strong <c:if test="${result.home_score > result.away_score}"> class="red"</c:if> >${result.home_score}</strong>vs<strong <c:if test="${result.home_score < result.away_score}"> class="red"</c:if>>${result.away_score}</strong>
                	<c:if test="${result.match_type eq 1}">
                	<strong <c:if test="${result.home_pk > result.away_pk}"> class="red"</c:if> >${result.home_pk}</strong>pso<strong <c:if test="${result.home_pk < result.away_pk}"> class="red"</c:if>>${result.away_pk}</strong>
                	</c:if>
                </p>
                <p class="name">
                	<c:choose>
                	<c:when test="${teamInfoMap.team_id eq result.home_id}">
                		<c:choose>
			          	<c:when test="${result.away_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.away_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.away_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.away}
                	</c:when>
                	<c:when test="${teamInfoMap.team_id eq result.away_id}">
                		<c:choose>
			          	<c:when test="${result.home_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.home_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.home_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.home}
                	</c:when>
                	</c:choose>
                </p>
                <p class="summary">${result.match_date}</p>
                <p class="summary">${result.place}</p>
              </div>
            </c:forEach>
            </div>
            
            
            
            <div class="show-tab" id="num2">
            <c:if test="${empty teamCupMatchList}">
            등록된 내역이 없습니다.
            </c:if>
            
            <c:forEach var="result" items="${teamCupMatchList}" varStatus="status">
              <div class="team-history">
                <c:choose>
               	<c:when test="${teamInfoMap.team_id eq result.home_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.away_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<c:choose>
		          		<c:when test="${result.home_pk > result.away_pk}">
		          			<div class="result win" style="background-image: url(/NP${result.away_emblem})">
			                  <span>승</span>
			                </div>
		          		</c:when>
		          		<c:when test="${result.home_pk < result.away_pk}">
		          			<div class="result lose" style="background-image: url(/NP${result.away_emblem})">
			                  <span>패</span>
			                </div>
		          		</c:when>
		          		<c:otherwise>
			          		<div class="result" style="background-image: url(/NP${result.away_emblem})">
			                  <span>무</span>
			                </div>
		          		</c:otherwise> 
		          		</c:choose>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.away_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	</c:choose> 
               	</c:when>
               	<c:when test="${teamInfoMap.team_id eq result.away_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.home_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<c:choose>
		          		<c:when test="${result.home_pk > result.away_pk}">
		          			<div class="result lose" style="background-image: url(/NP${result.home_emblem})">
			                  <span>패</span>
			                </div>
		          		</c:when>
		          		<c:when test="${result.home_pk < result.away_pk}">
		          			<div class="result win" style="background-image: url(/NP${result.home_emblem})">
			                  <span>승</span>
			                </div>
		          		</c:when>
		          		<c:otherwise>
			          		<div class="result" style="background-image: url(/NP${result.home_emblem})">
			                  <span>무</span>
			                </div>
		          		</c:otherwise> 
		          		</c:choose>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.home_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	</c:choose>
               	</c:when>
               	</c:choose>
                <i class="c">대</i>
                <p class="score">
                	<strong <c:if test="${result.home_score > result.away_score}"> class="red"</c:if> >${result.home_score}</strong>vs<strong <c:if test="${result.home_score < result.away_score}"> class="red"</c:if>>${result.away_score}</strong>
                	<c:if test="${result.match_type eq 1}">
                	<strong <c:if test="${result.home_pk > result.away_pk}"> class="red"</c:if> >${result.home_pk}</strong>pso<strong <c:if test="${result.home_pk < result.away_pk}"> class="red"</c:if>>${result.away_pk}</strong>
                	</c:if>
                </p>
                <p class="name">
                	<c:choose>
                	<c:when test="${teamInfoMap.team_id eq result.home_id}">
                		<c:choose>
			          	<c:when test="${result.away_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.away_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.away_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.away}
                	</c:when>
                	<c:when test="${teamInfoMap.team_id eq result.away_id}">
                		<c:choose>
			          	<c:when test="${result.home_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.home_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.home_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.home}
                	</c:when>
                	</c:choose>
                </p>
                <p class="summary">${result.match_date}</p>
                <p class="summary">${result.place}</p>
              </div>
            </c:forEach>
            </div>
            
            
            
            <div class="show-tab" id="num3">
            <c:if test="${empty teamLeagueMatchList}">
            등록된 내역이 없습니다.
            </c:if>
            
            <c:forEach var="result" items="${teamLeagueMatchList}" varStatus="status">
              <div class="team-history">
              	<c:choose>
               	<c:when test="${teamInfoMap.team_id eq result.home_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.away_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<div class="result" style="background-image: url(/NP${result.away_emblem})">
		                  <span>무</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.away_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	</c:choose> 
               	</c:when>
               	<c:when test="${teamInfoMap.team_id eq result.away_id}">
               		<c:choose>
		          	<c:when test="${result.home_score > result.away_score}">
		          		<div class="result lose" style="background-image: url(/NP${result.home_emblem})">
		                  <span>패</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score eq result.away_score}">
		          		<div class="result" style="background-image: url(/NP${result.home_emblem})">
		                  <span>무</span>
		                </div>
		          	</c:when>
		          	<c:when test="${result.home_score < result.away_score}">
		          		<div class="result win" style="background-image: url(/NP${result.home_emblem})">
		                  <span>승</span>
		                </div>
		          	</c:when>
		          	</c:choose>
               	</c:when>
               	</c:choose>
                
                <i class="l">리</i>
                <p class="score">
                	<strong <c:if test="${result.home_score > result.away_score}"> class="red"</c:if> >${result.home_score}</strong>vs<strong <c:if test="${result.home_score < result.away_score}"> class="red"</c:if>>${result.away_score}</strong>
                </p>
                <p class="name">
                	<c:choose>
                	<c:when test="${teamInfoMap.team_id eq result.home_id}">
                		<c:choose>
			          	<c:when test="${result.away_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.away_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.away_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.away}
                	</c:when>
                	<c:when test="${teamInfoMap.team_id eq result.away_id}">
                		<c:choose>
			          	<c:when test="${result.home_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${result.home_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${result.home_type eq '2'}"><span class="label red">유스</span></c:when>
			          	</c:choose>
			          	${result.home}
                	</c:when>
                	</c:choose>
                </p>
                <p class="summary">${result.match_date}</p>
                <p class="summary">${result.place}</p>
              </div>
            </c:forEach>
            </div>
            
            
          </div>
          <div class="right w35 mobi"><br>
            <h4>
            	<c:choose>
            	<c:when test="${empty sYear}">${sysYear}년 대회 성적</c:when>
            	<c:otherwise>${sYear}년 대회 성적</c:otherwise>
            	</c:choose>
            </h4><br>
            <div class="scroll">
            <table cellspacing="0" class="update">
              <thead>
                <tr>
                  <th>진행</th>
                  <th>대회명</th>
                  <th>성적</th>
                  <th>결과</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${empty teamCupResultList}">
            	 	<tr>
            	 		<td colspan="4">등록된 내역이 없습니다.</td>
            	 	</tr>
            	</c:if>
            	 
           	 	<c:forEach var="result" items="${teamCupResultList}" varStatus="status">
           	 	<tr>
                  <td>
                    <c:choose>
                    <c:when test="${result.progress eq '종료'}">
                    	<span class="label gray">종료</span>
                    </c:when>
                    <c:when test="${result.progress eq '진행중'}">
						<span class="label purple">진행</span>
                    </c:when>
                    <c:when test="${result.progress eq '예정'}">
                    	<span class="label blue-d">예정</span>
                    </c:when>
                    </c:choose>
                  </td>
                  <td class="tl">${result.cup_name}</td>
                  <td>
                  	<c:choose>
                    <c:when test="${result.progress eq '종료'}">${result.result}</c:when>
                    <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td>
 		            <c:choose>
                    <c:when test="${result.progress eq '종료'}">
                    	<c:choose>
		                <c:when test="${result.cup_type eq 3 }"> <!-- 토너먼트 -->
			                <a class="btn-large gray-o" onclick="gotoCupMgrTourMatch('${result.cup_id}');">결과</a>
		                </c:when>
		                <c:otherwise>
			                <a class="btn-large gray-o" onclick="gotoCupMgrSubMatch('${result.cup_id}');">결과</a>
		                </c:otherwise>
						</c:choose>
                    </c:when>
                    <c:when test="${result.progress ne '종료'}">
                    	<c:choose>
		                <c:when test="${result.cup_type eq 3 }"> <!-- 토너먼트 -->
			                <a class="btn-large gray-o" onclick="gotoCupMgrTourMatch('${result.cup_id}');">일정</a>
		                </c:when>
		                <c:otherwise>
			                <a class="btn-large gray-o" onclick="gotoCupMgrSubMatch('${result.cup_id}');">일정</a>
		                </c:otherwise>
						</c:choose>
                    </c:when>
                    </c:choose>     
                  </td>
                </tr>
           	 	</c:forEach> 
              
              </tbody>
            </table>
            </div><br><br>
            

            
            <c:if test="${empty teamLeagueEntertList}">
           	 	<h4>
            	<c:choose>
            	<c:when test="${empty sYear}">${sysYear}년 리그 성적</c:when>
            	<c:otherwise>${sYear}년 리그 성적</c:otherwise>
            	</c:choose>
            	</h4><br>
            	<div class="scroll">
	            <table cellspacing="0" class="update">
	              <colgroup>
	                <col width="5%">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	              </colgroup>
	              <thead>
	                <tr>
	                  <th>순위</th>
	                  <th>팀명</th>
	                  <th>경기</th>
	                  <th>승점</th>
	                  <th>승</th>
	                  <th>무</th>
	                  <th>패</th>
	                  <th>득</th>
	                  <th>실</th>
	                </tr>
	              </thead>
	              <tbody>
	                <tr>
	                  <td colspan="9">등록된 내역이 없습니다.</td>
	                </tr>
	              </tbody>
	            </table>
	          </div> 
           	</c:if>
           	
           	 
          	<c:forEach var="result" items="${teamLeagueEntertList}" varStatus="status">
          	<c:if test="${leagueId ne result.league_id}">
          	
          		<h4>${result.league_name} 성적</h4><br>
	            <div class="scroll">
	            <table cellspacing="0" class="update">
	              <colgroup>
	                <col width="5%">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	                <col width="*">
	              </colgroup>
	              <thead>
	                <tr>
	                  <th>순위</th>
	                  <th>팀명</th>
	                  <th>경기</th>
	                  <th>승점</th>
	                  <th>승</th>
	                  <th>무</th>
	                  <th>패</th>
	                  <th>득</th>
	                  <th>실</th>
	                  <th>+/-</th>
	                </tr>
	              </thead>
	              <tbody>
	              
	              <c:forEach var="res" items="${teamLeagueEntertList}" varStatus="status">
	              <c:if test="${result.league_id eq res.league_id}">
	              	<tr <c:if test="${teamInfoMap.team_id eq res.team_id}">class="currTeam"</c:if> >
	                  <td>${res.rank}</td>
	                  <td class="input2">
	                    <c:choose>
		              	<c:when test="${empty res.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
		              	<c:otherwise><img src="/NP${res.emblem}" class="logo"></c:otherwise>
		              	</c:choose>
		              	<c:choose>
		              	<c:when test="${res.team_type eq '0'}"><span class="label blue">학원</span></c:when>
			          	<c:when test="${res.team_type eq '1'}"><span class="label green">클럽</span></c:when>
			          	<c:when test="${res.team_type eq '2'}"><span class="label red">유스</span></c:when>
		              	</c:choose>
			          	${res.nick_name}
	                  </td>
	                  <td class="input2">${res.played}</td>
	                  <td class="input2">${res.points}</td>
	                  <td class="input2">${res.won}</td>
	                  <td class="input2">${res.draw}</td>
	                  <td class="input2">${res.lost}</td>
	                  <td class="input2">${res.gf}</td>
	                  <td class="input2">${res.ga}</td>
	                  <td class="input2">${res.gd}</td>
	                </tr>
	              </c:if>  
	              </c:forEach>
	              
	              </tbody>
	            </table>
	          	</div>
	          
          	<br><br>
	        </c:if>  
			<c:set var="leagueId" value="${result.league_id}" />
          	</c:forEach>
          	
          </div>
        </div>

        <div style="clear:both;"></div>
      </div>
    </div>

    <!--팝업-->
    <div class="pop" id="modefy-school-single">
		<div style="height:auto;">
		  <div style="height:auto;">
			<div class="head">
			  학원/클럽 개별 수정
			</div>
			<div class="body" style="padding:15px 20px;">
			  <ul class="signup-list">
				<li class="title">
				  <span class="title">사용명칭</span><input type="text" placeholder="사용명칭 입력 (예: 전주영생고)" value="전주영생고">
				</li>
				<li class="title">
				  <span class="title">정식명칭</span><input type="text" placeholder="정식명칭 입력 (예: 전주영생고등학교)" value="전주영생고등학교">
				</li>
				<li class="title">
				  <span class="title">구분</span>
				  <input type="radio" name="name" id="school" checked><label class="w20" for="school">학원</label>
				  <input type="radio" name="name" id="club"><label class="w20" for="club">클럽</label>
				  <input type="radio" name="name" id="youth"><label class="w20" for="youth">유스</label>
				</li>
				<li class="title">
				  <span class="title">지역선택</span>
				  <select style="width:50%;">
					 <option>광역 선택</option>
					<option>서울/인천</option>
					<option>중부/강원</option>
					<option>경기</option>
					<option>호남/제주</option>
					<option>영남</option>
					<option>전국</option>
				  </select>
				  <a class="btn-pop" data-id="update-area-add"><i class="xi-cog" style="line-height: 28px;"></i></a>
				</li>
				<li class="title">
				  <span class="title">소재지</span>
				  <input type="text" placeholder="소재지 입력 (예: 서울특별시 마포구 성산동)" value="전라북도 전주시 완산구 효자동3가">
				</li>
				<li class="title">
				  <span class="title">앰블럼</span>
				  <input type="file" style="width:50%;">
				  <a class="img-del"><span>기존파일</span><i class="xi-close"></i></a>
				</li>
			  </ul>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;"><span>등록하기</span></a>
			  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
			</div>
		  </div>
		</div>
	  </div>
    <!--팝업 끝--> 
        
        
  <div>
</body>


<form name="tmfrm" id="tmfrm" method="post"  action="teamMgr">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

<form name="tmdfrm" id="tmdfrm" method="post"  action="teamMgrDet">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
</form> 
<form name="tmdcfrm" id="tmdcfrm" method="post"  action="teamMgrDetCup">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
</form> 
<form name="tmdlfrm" id="tmdlfrm" method="post"  action="teamMgrDetLeague">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
</form> 

<form name="cmsmfrm" id="cmsmfrm" method="post"  action="cupMgrSubMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cupId" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

<form name="cmtmfrm" id="cmtmfrm" method="post"  action="cupMgrTourMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cupId" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

</html>