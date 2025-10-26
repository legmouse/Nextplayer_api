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
	$("#chkAppAll").click(function() {
        if ($("#chkAppAll").is(":checked")) {
            $("input[name=popupAppType]").prop("checked", true);
        } else {
            $("input[name=popupAppType]").prop("checked", false);
        }
    });
});

const serialize = (p_form) => {
    const form = document.getElementById(p_form);
    const formData = new FormData(form);
    const json = {};

    formData.forEach((value, key) => {
        if (json[key] || key === 'delFileId') {
            // 해당 키가 이미 존재하면 배열로 추가
            if (Array.isArray(json[key])) {
                json[key].push(value);
            } else {
                json[key] = [json[key], value];
            }
        } else {
            json[key] = value;
        }
    });
    return json;
}

function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	if(regxForm == 'frm'){
		if($('select[name=sUage]').val() < 0) {
			alert("알림!\n 연령 선택 하세요.");
		
			valid = false;
			return false;
		}
	}

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				switch ($obj.attr('name')){
				case 'teamName' :
					break;
				case 'address' :
					break;
				} */

				alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
				$obj.focus();
			
				valid = false;
				return false;
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
    
    return valid;
}

const searchList = () => {

    const sDate = $("input[name=sDate]").val();
    const eDate = $("input[name=eDate]").val();
	$('input[name="cp"]').val(1);
    if (sDate || eDate) {
        if (!sDate || !eDate) {
            alert('올바른 날짜를 입력해주세요.');
            return false;
        }
    }
    
    var agree = $('input[type="radio"]:checked').val();
    document.frm.pushAgree.value = agree; 
    document.frm.submit();

}

const goMemberList = (state) => {

    $("input[name=memberState]").val(state);
    $("input[name=sDate]").val(null);
    $("input[name=eDate]").val(null);
    $('input[name=cp]').val(null);
    $('input[name=searchKeyword]').val(null);

    document.frm.submit();
}

const goRegist = () => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/memberRegist');
    $(newForm).appendTo('body').submit();
}

const goDetail = (value) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/memberDetail');
    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: value }));
    $(newForm).appendTo('body').submit();
}

//페이징 처리
function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
}

const fnDownload = () => {

    let jsonData = {
        'excelFlag': 'memberList'
    };

    let excelUrl = "excelDownload";
    let request = new XMLHttpRequest();

    request.open('POST', excelUrl, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.responseType = 'blob';

    request.onload = function(e) {

        $("#page-loading").hide();

        let filename = "";
        let disposition = request.getResponseHeader('Content-Disposition');
        if (disposition && disposition.indexOf('attachment') !== -1) {
            let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            let matches = filenameRegex.exec(disposition);
            if (matches != null && matches[1])
                filename = decodeURI( matches[1].replace(/['"]/g, '') );
        }
        //console.log("FILENAME: " + filename);

        if (this.status === 200) {

            let blob = this.response;
            if(window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, filename);
            }else{
                let downloadLink = window.document.createElement('a');
                let contentTypeHeader = request.getResponseHeader("Content-Type");
                downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                downloadLink.download = filename;
                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);
            }

            //setTimeout(() => $('#pop-proof-1').modal('hide'), 3000);
        }

    };

    request.send(JSON.stringify(jsonData));

}

function fnFilter(type) {
	$('input[name="type"]').val(type);
	document.frm.submit();
}

function checkSex(tag, sex) {
	$('.sex').addClass('gray-o');
	$('.sex').removeClass('default');
	$(tag).addClass('default');
	$(tag).removeClass('gray-o');
	$('input[name="sex"]').val(sex);
}

function checkUage(tag, uage) {
	$('.uage').addClass('gray-o');
	$('.uage').removeClass('default');
	$(tag).addClass('default');
	$(tag).removeClass('gray-o');
	$('input[name="uage"]').val(uage);
}

function checkMemberType(tag, position) {
	/* if (position != null && position != '') {
		var memberType = $('input[name="memberType"]').val();
		if (memberType != null && memberType != '') {
			if (memberType.indexOf(position) > -1) {
				memberType = memberType.replace(position, '');
				if (memberType == ',') {
					$('input[name="memberType"]').val('');
				} else {
					$('input[name="memberType"]').val(memberType);
				}
				$(tag).addClass('gray-o');
				$(tag).removeClass('default');
			} else {
				memberType = memberType + ',' + position;
				$('input[name="memberType"]').val(memberType);
				$(tag).removeClass('gray-o');
				$(tag).addClass('default');
			}
		} else {
			$('input[name="memberType"]').val(position);
			$(tag).removeClass('gray-o');
			$(tag).addClass('default');
		}
	} */
	
	$('.memberType').addClass('gray-o');
	$('.memberType').removeClass('default');
	$(tag).removeClass('gray-o');
	$(tag).addClass('default');
	$('input[name="memberType"]').val(position);
}

function checkAge(tag, age) {
	$('.age').addClass('gray-o');
	$('.age').removeClass('default');
	$(tag).removeClass('gray-o');
	$(tag).addClass('default');
	$('input[name="age"]').val(age);
}

function allCheck() {
	var checked = $('#checkAll').prop("checked");
	if (checked) {
		$('input[type="checkbox"]').prop("checked", true);
	} else {
		$('input[type="checkbox"]').prop("checked", false);
	}
}

function clearAddPush() {
	$('#pushTitle').val('')
	$('#pushBody').val('');
	$('#selPushType').val(11);
	$('#pushPath').val('');
	
	var today = new Date();
	var year = today.getFullYear();
	var month = today.getMonth() + 1;
	var day = today.getDate();
	var hours = today.getHours();
	var minutes = today.getMinutes();
	
	var monthStr = '';
	var dayStr = '';
	var hoursStr = '';
	var minutesStr = '';
	
	if (month < 10) {
		monthStr = '0' + month;
	}
	if (day < 10) {
		dayStr = '0' + day;
	}
	
	if (hours < 10) {
		hoursStr = '0' + hours;
	}
	
	if (minutes < 10) {
		minutesStr = '0' + minutes;
	}
	
	
	var dateStr = year + '-' + monthStr + '-' + dayStr;
	$('#pushDate').val(dateStr)
	$('#pushTime').val(hoursStr);
	$('#pushMinute').val(minutesStr);
}

function pushAdd() {
    if (allCheckFlag) {
        pushAddAll();
    } else {
        pushAddSelect();
    }
}

function pushAddAll() {
    const data = serialize('frm');
    const pushType = $('#selPushType option:selected').val();
    const title = $('#pushTitle').val();
    const body = $('#pushBody').val();
    const path = $('#pushPath').val();

    const date = $('#pushDate').val();
    const time = $('#pushTime option:selected').val();
    const minutes = $('#pushMinute option:selected').val();

    data.pushType = pushType;
    data.title = title;
    data.body = body;
    data.path = encodeURIComponent(path);
    data.date = date + ' ' + time + ':' + minutes + ':00';
    data.autoFlag = 1;

    console.log(data);

    $.ajax({
        type: 'POST',
        url: '/sendPushAll',
        contentType : "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res);
            if (res.state == 'SUCCESS') {
                alert('알림 등록 완료');
                location.href = '/sendPush';
            } else {
                alert('알림 등록 실패');
                location.href = '/sendPush';
            }
        }
    });
}

function pushAddSelect() {
    var pushType = $('#selPushType option:selected').val();
    var title = $('#pushTitle').val();
    var body = $('#pushBody').val();
    var path = $('#pushPath').val();

    var date = $('#pushDate').val();
    var time = $('#pushTime option:selected').val();
    var minutes = $('#pushMinute option:selected').val();

    var arr = [];

    $("input[name=ck]:checked").each(function () {
        var token = $(this).data('token');
        var memberCd = $(this).data('cd');
        var member = {
            fcmToken: token,
            memberCd: memberCd
        };
        arr.push(member);
    });

    var data = {
        title: title,
        body: body,
        path: encodeURIComponent(path),
        description: '푸시 알림',
        typeId: pushType,
        date: date + ' ' + time + ':' + minutes + ':00',
        autoFlag : 1,
        list: arr,
        cnt: arr.length,
    };

    $.ajax({
        type: 'POST',
        url: '/send_push',
        data: data,
        success: function (res) {
            console.log(res);
            if (res.state == 'SUCCESS') {
                alert('알림 등록 완료');
                location.href = '/sendPush';
            } else {
                alert('알림 등록 실패');
                location.href = '/sendPush';
            }
        }
    });
    /* alert('알림 등록 완료');
    location.href = '/sendPush'; */
}

function checkboxCehck() {
	var check  = $("input[name=ck]:checked");
	if (check.length == 0 && !allCheckFlag) {
		alert('선택된 회원이 없습니다.');
		return false;
	} else {
        if (allCheckFlag) {
            alert('검색하기를 누르지 않아도\n선택된 값의 회원이 등록됩니다.');
        }
		$('#send-push').fadeIn();
	}
}

let allCheckFlag = false;
const sendAll = () => {
    allCheckFlag = !allCheckFlag;
    let msg = "선택";
    if (!allCheckFlag) msg = "선택 해제";

    alert(msg + " 되었습니다.");
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
                <h2><span></span>알림관리 > 알림 보내기</h2>
            </div>
        </div>
        <div class="round body">
            <div class="body-head">
                <hr class="mt_10 mb_10">
            </div>
            <div class="body-head">
                <div class="search">
                    <form name="frm" id="frm" method="post" action="sendPush" onsubmit="return false;">
                    	<input name="cp" type="hidden" value="${cp}">
                        <input name="memberState" type="hidden" value="${memberState}">
                        <input name="sex" type="hidden" value="${sex}">
                        <input name="uage" type="hidden" value="${uage}">
                        <input name="memberType" type="hidden" value="${memberType}">
                        <input name="age" type="hidden" value="${age}">
                    	<div style="margin-bottom: 10px;">
                		<span class="title">수신 동의</span>
                		<div style="margin-top: 10px;">
                			<input type="radio" name="pushAgree" id="ck0" value="" <c:if test="${empty pushAgree}">checked</c:if>>
                			<label for="ck0">전체</label>
                			<input type="radio" name="pushAgree" id="ck1" value="MARKETING" <c:if test="${pushAgree eq 'MARKETING'}">checked</c:if>>
                			<label for="ck1">마케팅</label>
		                	<input type="radio" name="pushAgree" id="ck2" value="EVENT" <c:if test="${pushAgree eq 'EVENT'}">checked</c:if>>
		                	<label for="ck2">이벤트</label>
		                	<input type="radio" name="pushAgree" id="ck3" value="NEW" <c:if test="${pushAgree eq 'NEW'}">checked</c:if>>
		                	<label for="ck3">신상품</label>
		                </div>
                	</div>
                	<div style="margin-bottom: 10px;">
                		<span class="title">연령</span>
                		<div style="margin-top: 10px;">
                			<a class="btn-large ${empty age ? 'default' : 'gray-o'} age" onclick="checkAge(this, '');">전체</a>
                			<a class="btn-large ${age eq '10' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '10');">10대</a>
                			<a class="btn-large ${age eq '20' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '20');">20대</a>
                			<a class="btn-large ${age eq '30' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '30');">30대</a>
                			<a class="btn-large ${age eq '40' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '40');">40대</a>
                			<a class="btn-large ${age eq '50' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '50');">50대</a>
                			<a class="btn-large ${age eq '60' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '60');">60대</a>
                			<a class="btn-large ${age eq '70' ? 'default' : 'gray-o'} age" onclick="checkAge(this, '70');">70대</a>
		                </div>
                	</div>
                	<div style="margin-bottom: 10px;">
                		<span class="title">계정 종류</span>
                		<div style="margin-top: 10px;">
                			<a class="btn-large ${empty memberType ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, '');">전체</a>
                			<a class="btn-large ${memberType eq 'PARENTS' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'PARENTS');">학부모</a>
                			<a class="btn-large ${memberType eq 'DIRECTOR' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'DIRECTOR');">감독/코치</a>
                			<a class="btn-large ${memberType eq 'TEACHER' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'TEACHER');">레슨 선생님</a>
                			<a class="btn-large ${memberType eq 'ACADENY' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'ACADENY');">학원/클럽 관계자</a>
                			<a class="btn-large ${memberType eq 'PLAYER' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'PLAYER');">선수</a>
                			<a class="btn-large ${memberType eq 'ETC' ? 'default' : 'gray-o'} memberType" onclick="checkMemberType(this, 'ETC');">기타</a>
		                </div>
                	</div>
                	<div style="margin-bottom: 10px;">
                		<span class="title">관심 연령</span>
                		<div style="margin-top: 10px;">
                			<a class="btn-large ${empty uage ? 'default' : 'gray-o'} uage" onclick="checkUage(this, '');">전체</a>
                			<c:forEach var="age" items="${uageList}">
                				<a class="btn-large ${uage eq age.uage ? 'default' : 'gray-o'} uage" onclick="checkUage(this, '${age.uage}');">${age.uage}</a>
                			</c:forEach>
		                </div>
                	</div>
                        <div style="margin-bottom: 10px;">
                        	<span class="title">가입일</span>
                        	<div style="margin-top: 10px;">
                        		<input type="date" value="${sDate}" name="sDate">
                        		-
                        		<input type="date" value="${eDate}" name="eDate">
                        	</div>
                        </div>
                        <div style="margin-bottom: 10px;">
                		<span class="title">성별</span>
                		<div style="margin-top: 10px;">
                			<a class="btn-large ${empty sex ? 'default' : 'gray-o'} sex" onclick="checkSex(this, '');">전체</a>
                			<a class="btn-large ${sex eq 'M' ? 'default' : 'gray-o'} sex" onclick="checkSex(this, 'M');">남</a>
                			<a class="btn-large ${sex eq 'W' ? 'default' : 'gray-o'} sex" onclick="checkSex(this, 'W');">여</a>
		                </div>
                	</div>
                        <div style="margin-bottom: 10px;">
                        	<input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="이메일, 이름 및 닉네임을 입력해주세요" onkeydown="javascript:if(event.keyCode==13){searchList();}">
                        	<a class="btn-large default" onclick="searchList()">검색하기</a>
                        </div>
                        <div>
                            <a class="btn-large default" onclick="sendAll()">검색조건 전체선택</a>
                        </div>
                    </form>
                </div>
            </div>

            <div class="scroll">
                <table cellspacing="0" class="update over">
                    <%-- <colgroup>
                    	<col width="55px">
                        <col width="55px">
                    </colgroup> --%>
                    <thead>
                        <tr>
                        	<th>아이디</th>
                            <th>닉네임</th>
                            <th>휴대폰 번호</th>
                            <th>이메일</th>
                            <th>이름</th>
                            <th>생일</th>
                            <th><input type="checkbox" id="checkAll" onclick="allCheck()"><label for="checkAll"></label></th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty memberList}">
                            <tr>
                                <td id="idEmptyList" colspan="8">등록된 내용이 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="result" items="${memberList}" varStatus="status">
                                <tr>
                                    <td>
                                    	${result.member_id}
                                    </td>
                                    <td>
                                        ${result.member_nickname}
                                    </td>
                                    <td>
                                        ${result.phone_no}
                                    </td>
                                    <td>
                                        ${result.email}
                                    </td>
                                    <td>
                                        ${result.member_name}
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${!empty result.birth_date}">
                                                ${result.birth_date}
                                            </c:when>
                                            <c:otherwise>
                                                ${result.age}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                		<input type="checkbox" name="ck" id="check${status.index}" data-cd="${result.member_cd}" data-token="${result.fcm_token}">
                                		<label for="check${status.index}"></label>
                                	</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
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
            <div class="body-foot">
				<div class="others">
					<a class="btn-large default" data-id="send-push" onclick="checkboxCehck();">알림 보내기</a>
				</div>
			</div>
        </div>
    </div>
  </div>
  
  <div class="pop" id="send-push">
      <div style="height:auto;">
        <div style="width:470px; height:auto;">
          <div class="head">
            알림 발송하기
          </div>
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">구분</span>
                <select id="selPushType" name="pushType" style="width: 330px;">
                	<c:forEach var="push" items="${pushList}">
                		<option value="${push.type_id}">${push.case_name}</option>
                	</c:forEach>
                </select>
              </li>
              <li class="title">
                <span class="title">제목</span>
                <input type="text" name="title" id="pushTitle" style="width: 330px;">
              </li>
              <li class="title" style="height: 100%;">
                <span class="title">내용</span>
                <textarea id="pushBody" name="body" placeholder="발송메세지 입력" style="width: 330px; resize: none;"></textarea>
              </li>
              <li class="title">
                <span class="title">발송시간</span>
                <c:set var="now" value="<%=new java.util.Date()%>" />
				<fmt:formatDate value="${now}" var="curDate" pattern="yyyy-MM-dd" />
				<fmt:formatDate value="${now}" var="curHour" pattern="HH" />
				<fmt:formatDate value="${now}" var="curMinute" pattern="mm" />
				<input type="date" id="pushDate" name="sDate" value="${curDate}" style="width: 45%;">
				<select id="pushTime" name="sTime" style="width:15%;">
					<option value="">시간 선택</option>
					<c:forEach begin="0" end="23" var="hour">
						<option value="${hour < 10 ? '0' : ''}${hour}"<c:if test="${hour eq curHour}"> selected</c:if>>${hour < 10 ? '0' : ''}${hour}</option>
					</c:forEach>
				</select>
				<select id="pushMinute" name="sMinute" style="width:15%;">
					<option value="">분 선택</option>
					<c:forEach begin="0" end="50" var="minute">
						<option value="${minute < 10 ? '0' : ''}${minute}"<c:if test="${minute eq curMinute}">selected</c:if>>${minute < 10 ? '0' : ''}${minute}</option>
					</c:forEach>
				</select>
              </li>
              <li class="title">
                <span class="title">링크</span>
                <input type="text" name="path" id="pushPath" style="width: 330px;">
              </li>
            </ul>
          </div>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="pushAdd();">발송하기</a>
            <a class="login btn-large btn-close-pop gray w100" onclick="clearAddPush();">취소</a>
          </div>
        </div>
      </div>
    </div>  
</body>
</html>