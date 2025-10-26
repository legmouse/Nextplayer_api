<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
	
});

function gotoMod(){
	var idx = document.modFrm.pushId.value;
	var caseText = document.modFrm.body.value;
	$.ajax({
		type: 'POST',
		url: '/update_push',
		data: {pushId : idx, useFlag : 0, caseText: caseText},
		success: function(res) {
			if (res.state == "SUCCESS") {
				alert("수정되었습니다.");
				location.reload();
			} else {
				alert("수정에 실패했습니다.");
				return false;
			}
		}
	});
}

function gotoDel(idx){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$.ajax({
            type: 'POST',
            url: '/update_push',
            data: {pushId : idx, useFlag : 1},
            success: function(res) {
                if (res.state == "SUCCESS") {
                    alert("삭제 완료 되었습니다.");
                    location.reload();
                } else {
                    alert("삭제 실패했습니다.");
                    return false;
                }
            }
        });
	}
}

// 경기 선택 페이지 이동
function gotoMatchList(id, method, title, text){
	$('input[name=typeId]').val(id);
	$('input[name=method]').val(method);
	$('input[name=bodyText]').val(text);
	$('input[name=titleText]').val(title);
	document.sendFrm.submit(); 
}

function gotoModPushPopup(id){
	
	$.ajax({
        type: 'GET',
        url: '/push_info',
        data: {pushId: id},
        success: function (res) {
            if (res.data != null && res.data != '') {
            	$('input[name=pushId]').val(res.data.id);
            	$('#case-name').html(res.data.case_name);
            	$('#case-text').html(res.data.case_text);
            	$('#case-title').val(res.data.case_title);
            }  
        }
    });
	return false;
}

function clearPush() {
	$('input[name=pushId]').val();
	$('#case-name').html('');
	$('#case-text').html('');
	$('#case-title').val('');
}

function clearCup() {
	$('#childTbody').html('');
	$('#selYears').val('');
	$('#cup-send-push').find('input[name="method"]').val('');
	$('#cup-send-push').find('input[name="bodyText"]').val('');
	$('#cup-send-push').find('input[name="titleText"]').val('');
}

function gotoSearch() {
	var param = {};
	var keyword = $('input[name="sCupName"]').val();
	var sYear = $('#selYears option:selected').val();
	var ageGroup = $('#selAgeGroup option:selected').val();
	param.sCupName = keyword;
	param.sYear = sYear;
	param.ageGroup = ageGroup;
	
	$.ajax({
        type: 'POST',
        url: '/cupSelectList',
        data: param,
        success: function(res) {
        	var htmlStr = '';
        	if (res.data != null && res.data != '') {
        		for (var i = 0; i < res.data.length; i++) {
        			htmlStr += '<tr>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.data[i].cup_name;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.data[i].play_sdate + ' ~ ' + res.data[i].play_edate;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			'<input type="checkbox" name="ck"' + 
            								'data-cupid="' + res.data[i].cup_id + '" data-cupname="' + res.data[i].cup_name + 
            								'" id="ck_' + res.data[i].cup_id + '" value="' + res.data[i].cup_id + 
            								'" data-uage="' + ageGroup + '">';
            		htmlStr +=			'<label for="ck_' + res.data[i].cup_id + '"></label>';
            		htmlStr += 		'</td>';
            		htmlStr += '</tr>';
        		}
        	} else {
        		htmlStr += '<tr>';
        		htmlStr += 		'<td colspan="3">';
        		htmlStr +=			'조회 결과가 없습니다.';
        		htmlStr += 		'</td>';
        		htmlStr += '</tr>';
        	}
        	
        	$("#cup-send-push").find('#childTbody').html(htmlStr);
        }
    });
}

function openSendPush(name, type) {
	$('#sendName').html(name)
	var text = $('input[name="' + type + '"]').val();
	$('#sendBody').html(text);
	$('input[name="title"]').val(name);
	$('#send-push').find('input[name="method"]').val(type);
}

function clearSendPush() {
	$('#sendName').html('')
	$('#sendBody').html('');
	$('input[name="title"]').val('');
}

function pushRegCup() {
	var typeId = $('#cup-send-push').find('input[name="typeId"]').val();
	var method = $('#cup-send-push').find('input[name="method"]').val();
	var text = $('#cup-send-push').find('input[name="bodyText"]').val();
	var title = $('#cup-send-push').find('input[name="titleText"]').val();
	
	$("input[name=ck]:checked").each(function () {
    	var ckvalue = $(this).val()
        var cupname = $(this).data('cupname');
        var uage = $(this).data('uage');
        var param = {
            ageGroup : uage,
            cupId: ckvalue,
        };
    	text = text.replace('연령', uage);
    	text = text.replace('대회', cupname + ' 대회');
        var data = {
            title: title,
            body: text,
            uage,
            path: encodeURIComponent('/contest/' + ckvalue + '?ageGroup=' + uage),
            param: JSON.stringify(param),
            description: '대회 상세로 이동',
            method: method,
            autoFlag : 1,
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
            }
        });

    });

    alert('푸시 등록 완료');
    location.reload();
}

/* function pushReg() {
	var method = $('#send-push').find('input[name="method"]').val();
	
	var titleStr = $('input[name="title"]').val();
	var bodyStr = $('#sendBody').val();
	if (bodyStr == null || bodyStr == '') {
		alert('발송 메세지를 입력해주세요.')
		return false;
	}
	
	if (bodyStr.length > 250) {
		alert('메세지는 최대 250자 입니다.');
		return false;
	}
    var param = {
        method : method
    };
    var data = {
        title: titleStr,
        body: bodyStr,
        method: method,
        path: encodeURIComponent('/'),
        param: JSON.stringify(param),
        description: titleStr + ' 상세로 이동',
        method: method
    };

    $.ajax({
        type: 'POST',
        url: '/insertPush',
        dataType: 'json',
        contentType : "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res);
        }
    });

    alert('푸시 등록 완료');
    location.reload();
} */

function openCupSelect(id, method, title, text){
	$('#cup-send-push').find('input[name="typeId"]').val(id);
	$('#cup-send-push').find('input[name="method"]').val(method);
	$('#cup-send-push').find('input[name="bodyText"]').val(text);
	$('#cup-send-push').find('input[name="titleText"]').val(title);
}

function pushAdd() {
	var caseName = $('input[name="caseName"]').val();
	var pushType = $('input[name="pushType"]').val();
	var caseText = $('#addBody').val();
	$.ajax({
		type: 'POST',
		url: '/addPush',
		data: {caseName : caseName, type : pushType, caseText: caseText},
		success: function(res) {
			if (res.state == "SUCCESS") {
				alert("추가되었습니다.");
				location.reload();
			} else {
				alert("추가에 실패했습니다.");
				return false;
			}
		}
	});
}

function savePush() {
	$("input[type=radio]:checked").each(function () {
		var ckVal = $(this).val();
		var idx = $(this).data('idx');      
		$.ajax({
            type: 'POST',
            url: '/update_push',
            data: {pushId : idx, useFlag : 0, autoFlag : ckVal},
            success: function (res) {
                console.log(res);
            }
        });

    });

    alert('알림 설정 완료');
    location.reload();
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
  		  	<h2><span></span>알림관리 > 알림설정</h2>
        </div>
      </div>
      <div class="round body">
      	<div class="body-head">
			<a class="btn-large default btn-pop" data-id="add-push">푸시 추가</a>
			<hr class="mt_10 mb_10">
		</div>
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="15%">
            <col width="20%">
            <col width="*">
            <col width="10%">
            <col width="10%">
            <col width="10%">
          </colgroup>
          <thead>
            <tr>
              <th>구분</th>
              <th>제목</th>
              <th>발송 메세지</th>
              <th>수정하기</th>
              <th>자동발송 설정</th>
              <th>수동 발송하기</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach var="push" items="${pushList}" varStatus="status">
          		<tr>
          			<td>
          				${push.case_name}
          			</td>
          			<td>
          				${push.case_title}
          			</td>
          			<td>
          				<input type="hidden" name="${push.type}" value="${push.case_text}">
          				${push.case_text}
          			</td>
          			<td>
          				<a class="btn-large gray btn-pop" data-id="modify-push" onclick="gotoModPushPopup(${push.type_id});">수정하기</a>
          			</td>
          			<td>
          				<input type="radio" name="autoFlag${status.index}" data-idx="${push.type_id}" id="ct1_${status.index}" value="1" <c:if test="${push.auto_flag eq 1}">checked</c:if>><label for="ct1_${status.index}">활성</label>
		                <input type="radio" name="autoFlag${status.index}" data-idx="${push.type_id}" id="ct2_${status.index}" value="0" <c:if test="${push.auto_flag eq 0}">checked</c:if>><label for="ct2_${status.index}">비활성</label>
						<%-- <a class="btn-pop" data-id="modify-push" onclick="gotoModPushPopup(${push.id});"><i class="xi-catched"></i></a>
	                	<a onclick="gotoDel(${push.id});"><i class="xi-close-circle-o"></i></a> --%>
					</td>
					<td>
						<c:choose>
          					<c:when test="${push.type eq 'START' or push.type eq 'END' or push.type eq 'LINEUP'}">
          						<c:choose>
          							<c:when test="${push.auto_flag eq 1}">
          								<a class="btn-large gray-o" style="cursor: default;">수동 발송하기</a>
          							</c:when>
          							<c:otherwise>
          								<a class="btn-large default" onclick="gotoMatchList('${push.type_id}', '${push.type}', ${push.case_title}, '${push.case_text}');">수동 발송하기</a>
          							</c:otherwise>
          						</c:choose>
          					</c:when>
          					<c:when test="${push.type eq 'CUP'}">
          						<c:choose>
          							<c:when test="${push.auto_flag eq 1}">
          								<a class="btn-large gray-o" style="cursor: default;">수동 발송하기</a>
          							</c:when>
          							<c:otherwise>
          								<a class="btn-large default btn-pop" data-id="cup-send-push" onclick="openCupSelect('${push.type_id}', '${push.type}', ${push.case_title}, '${push.case_text}');">수동 발송하기</a>
          							</c:otherwise>
          						</c:choose>
          					</c:when>
          					<c:otherwise>
          						<c:choose>
          							<c:when test="${push.auto_flag eq 1}">
          								<a class="btn-large gray-o" style="cursor: default;">수동 발송하기</a>
          							</c:when>
          							<c:otherwise>
          								<a class="btn-large default btn-pop" data-id="send-push" onclick="openSendPush('${push.case_name}', '${push.type}')">수동 발송하기</a>
          							</c:otherwise>
          						</c:choose>
          					</c:otherwise>
          				</c:choose>
          			</td>
          		</tr>
          	</c:forEach>
          	<!-- <tr>
				<td id="interest-team-match-start">
					관심팀 경기 시작
				</td>
				<td>
					[] 금일 팀명 vs 팀명 경기가 있습니다.
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o" onclick="gotoMatchList('start')">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td id="">
					관심 팀 경기 종료
				</td>
				<td>
					[] 금일 팀명 vs 팀명 경기가 종료되었습니다.
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o" onclick="gotoMatchList('end')">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td td="interest-lineup">
					관심 팀 라인업
				</td>
				<td>
					[] 팀명 vs 팀명 경기의 라인업이 등록되었습니다.
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o" onclick="gotoMatchList('lineup')">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td id="interest-age">
					관심 연령 대회등록
				</td>
				<td>
					[] 대회가 등록되었습니다.
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o" onclick="gotoMatchList('cup')">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td id="advert">
					마케팅
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td id="new-goods">
					신상품
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup();"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			<tr>
				<td id="event">
					이벤트
				</td>
				<td>
				</td>
				<td>
				</td>
				<td>
					<a class="btn-large gray-o">발송하기</a>
					<a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup('이벤트');"><i class="xi-catched"></i></a>
	                <a onclick="gotoDel();"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr> -->
          </tbody>
        </table>
        <div class="body-foot">
				<div class="others">
					<a class="btn-large default" onclick="savePush();">저장</a>
				</div>
			</div>
      </div>
    </div>
    </div>
    <!--팝업-->
    <div class="pop" id="modify-push">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            수정하기
          </div>
          <form name="modFrm" id="modFrm" method="post"  action="save_area">
           <input type="hidden" name="pushId">
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">Case</span>
                <span id="case-name" class="title" style="width: 160px;"></span>
              </li>
              <li class="title">
                <span class="title">제목</span>
                <input type="text" id="case-title" name="title" placeholder="발송제목을 수정할 수 있습니다" >
              </li>
              <li class="title" style="height: 100%;">
                <span class="title">메세지</span>
                <textarea id="case-text" name="body" placeholder="발송메세지를 수정할 수 있습니다" style="width: 275px; resize: none;"></textarea>
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoMod();"><span>수정하기</span></a>
            <a class="login btn-large btn-close-pop gray w100" onclick="clearPush();"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    
    <div class="pop" id="cup-send-push">
    	<input type="hidden" name="typeId">
    	<input type="hidden" name="method">
    	<input type="hidden" name="titleText">
    	<input type="hidden" name="bodyText">
		<div style="height:auto;">
		  <div style="height:auto; width: 600px;">
			<div class="head">
			  대회 선택하기
			</div>
			<div id="selectList">
			<div class="body" style="padding:15px 20px;">
				<div>
          			<div class="search">
          				<c:set var="now" value="<%=new java.util.Date()%>" />
			  			<c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
			  			<select name="sYear" id="selYears" style="left: 0;">
				  			<option value="">연도선택</option>
				  			<c:forEach var="i" begin="2014" end="${sysYear }" step="1">
					  			<c:choose>
						  			<c:when test="${i eq sYear}">
							  			<option value="${i}" selected>${i}</option>
						  			</c:when>
						  			<c:otherwise>
							  			<option value="${i}">${i}</option>
						  			</c:otherwise>
					  			</c:choose>
				  			</c:forEach>
			  			</select>
			  			<select name="ageGroup" id="selAgeGroup" style="left: 110px;">
				  			<c:forEach var="age" items="${uageList}">
				  				<option value="${age.uage}">${age.uage}</option>
				  			</c:forEach>
			  			</select>
						<input type="text" name="sCupName" style="width: 220px; position: relative; left: 50px;" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
						<i class="xi-search" style="cursor:pointer; right: 125px;" onclick="gotoSearch();"></i>
					</div>
					<br>
					<table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="*">
                            <col width ="*">
                            <col width ="10%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>대회명</th>
                            <th>대회일정</th>
                            <th>선택</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                        </tbody>
                    </table>
				</div>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="pushRegCup();"><span>푸시 발송</span></a>
			  <a class="login btn-large btn-close-pop gray w100" onclick="clearCup();"><span>취소</span></a>
			</div>
			</div>
		  </div>
		</div>
	  </div>
    
<!--     <div class="pop" id="send-push">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            발송하기
          </div>
          <input type="hidden" name="method">
          <input type="hidden" name="title">
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">Case</span>
                <span id="sendName" class="title" style="width: 160px;"></span>
              </li>
              <li class="title" style="height: 100%;">
                <span class="title">발송 메세지</span>
                <textarea id="sendBody" name="body" placeholder="발송메세지를 수정할 수 있습니다" style="width: 275px; resize: none;"></textarea>
              </li>
            </ul>
          </div>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="pushReg();"><span>발송하기</span></a>
            <a class="login btn-large btn-close-pop gray w100" onclick="clearSendPush();"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div> -->
    
    <div class="pop" id="add-push">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            푸시 추가
          </div>
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">Case</span>
                <input type="text" name="caseName" placeholder="푸시 케이스 입력">
              </li>
              <li class="title">
                <span class="title">구분코드</span>
                <input type="text" name="pushType" placeholder="구분코드 입력 (예: MARKETING)">
              </li>
              <li class="title" style="height: 100%;">
                <span class="title">발송 메세지</span>
                <textarea id="addBody" name="newCaseText" placeholder="발송메세지 입력" style="width: 275px; resize: none;"></textarea>
              </li>
            </ul>
          </div>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="pushAdd();"><span>추가</span></a>
            <a class="login btn-large btn-close-pop gray w100" onclick="clearAddPush();"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    <!--팝업 끝-->
  
<%-- <form name="frm" id="frm" method="post"  action="area">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="">
</form>   --%>

<form name="sendFrm" id="sendFrm" method="post"  action="selectCupMatch" >
	<input type="hidden" name="typeId" value="">
	<input type="hidden" name="method" value="">
	<input type="hidden" name="titleText" value="">
	<input type="hidden" name="bodyText" value="">
</form>
  
</body>
</html>