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
    /*if (!${memberDetail}) {
        alert('잘못된 접근입니다.');
        location.href = '/list';
    }*/
    var path = decodeURIComponent('${pushInfo.path}');
    $('#link').text(path);
});


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

    if (sDate || eDate) {
        if (!sDate || !eDate) {
            alert('올바른 날짜를 입력해주세요.');
            return false;
        }
    }

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

const fnMemberDelete = (value) => {
    if (value) {
        const confirmMsg = confirm('정말 삭제하시겠습니까?');
        if (confirmMsg) {

            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/personalDelete?memberCd=' + value);

            $(newForm).appendTo('body').submit();
        }
    }
}

const goMemberModify = (value) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/memberModify');
    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: value }));
    $(newForm).appendTo('body').submit();
}

const moveTab = (val) => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');

    let urlStr = "";
    if (val == 'info') {
        urlStr = "/memberDetail";
    }else if (val == 'education') {
        urlStr = "/memberEducation";
    }

    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: '${memberDetail.member_cd}' }));
    newForm.attr('action', urlStr);
    $(newForm).appendTo('body').submit();
}

function checkAge(tag, age) {
	$('.age').addClass('gray-o');
	$('.age').removeClass('default');
	$(tag).removeClass('gray-o');
	$(tag).addClass('default');
	$('input[name="age"]').val(age);
}

function checkMemberType(tag, position) {
	
	$('.memberType').addClass('gray-o');
	$('.memberType').removeClass('default');
	$(tag).removeClass('gray-o');
	$(tag).addClass('default');
	$('input[name="memberType"]').val(position);
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
                <h2><span></span>알림관리 > 전송내역 > 상세</h2>
            </div>
        </div>
        <div class="round body">
            <div class="body-head">
                <h4 class="view-title">알림 상세 정보</h4>
            </div>
            <div class="scroll">
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">
                    </colgroup>
                    <tbody>
                        <c:if test="${!empty pushInfo}">
                            <tr>
                                <th class="tl">번호</th>
                                <td class="tl">
                                	${num}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">발송상태</th>
                                <td class="tl">
                                	<c:choose>
										<c:when test="${pushInfo.send_flag eq '0'}">발송전</c:when>
										<c:when test="${pushInfo.send_flag eq '1'}">발송완료</c:when>
										<c:otherwise>발송오류</c:otherwise>
									</c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">구분</th>
                                <td class="tl">
                                	${pushInfo.case_name }
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">제목</th>
                                <td class="tl">
                                	${pushInfo.title}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">내용</th>
                                <td class="tl">
                                	${pushInfo.body}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">발송설정시간</th>
                                <td class="tl">
                                	<fmt:parseDate value="${pushInfo.req_dt}" var="time" pattern="yyyy-MM-dd HH:mm:ss" />
									<fmt:formatDate value="${time}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">링크</th>
                                <td class="tl" id="link">
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">수신자 수</th>
                                <td class="tl">
                                	${pushInfo.total_cnt}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">성공</th>
                                <td class="tl">
                                	${pushInfo.suc_cnt}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">실패</th>
                                <td class="tl">
                                	${pushInfo.fail_cnt}
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <br>
            <div class="body-head">
                <hr class="mt_10 mb_10">
                <br>
                <h4 class="view-title">수신자 목록</h4>
            </div>
            <div class="body-head">
                <div class="search">
                    <form name="frm" id="frm" method="post" action="pushDetail" onsubmit="return false;">
                    	<input name="cp" type="hidden" value="${cp}">
                        <input name="memberState" type="hidden" value="${memberState}">
                        <input name="sex" type="hidden" value="${sex}">
                        <input name="uage" type="hidden" value="${uage}">
                        <input name="memberType" type="hidden" value="${memberType}">
                        <input name="age" type="hidden" value="${age}">
                        <input name="pushContentId" type="hidden" value="${pushContentId}">
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
        </div>
    </div>
  </div>
  
</body>
</html>