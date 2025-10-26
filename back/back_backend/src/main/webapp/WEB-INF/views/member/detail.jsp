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
                <h2><span></span>회원관리</h2>
            </div>
        </div>
        <div class="round body">
            <div class="body-head">
                <div class="search">
                    <button class="btn-large default" onclick="moveTab('info')">기본정보</button>
                    <button class="btn-large gray-o" onclick="moveTab('education')">피츠앤솔 정밀보행분석</button>
                </div>
                <br />
                <br />
                <br />
                <h4 class="view-title">기본 정보</h4>
            </div>
            <div class="scroll">
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">
                    </colgroup>
                    <tbody>
                        <c:if test="${!empty memberDetail}">
                            <tr>
                                <th class="tl">구분</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${memberDetail.member_state eq '0'}">탈퇴회원</c:when>
                                        <c:when test="${memberDetail.member_state eq '1'}">일반회원</c:when>
                                        <c:when test="${memberDetail.member_state eq '3'}">차단회원</c:when>
                                        <c:when test="${memberDetail.member_state eq '4'}">휴면회원</c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">계정종류</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${memberDetail.member_type eq '1'}">학부모</c:when>
                                        <c:when test="${memberDetail.member_type eq '2'}">감독/코치</c:when>
                                        <c:when test="${memberDetail.member_type eq '3'}">레슨 선생님</c:when>
                                        <c:when test="${memberDetail.member_type eq '4'}">학원/클럽 관계자</c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">아이디</th>
                                <td class="tl">
                                        ${memberDetail.member_id}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">이메일</th>
                                <td class="tl">
                                    ${memberDetail.email}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">휴대폰번호</th>
                                <td class="tl">
                                        ${memberDetail.phone_no}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">성별</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${memberDetail.sex eq 'M'}">남</c:when>
                                        <c:when test="${memberDetail.sex eq 'W'}">여</c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">출생연도</th>
                                <td class="tl">
                                    ${memberDetail.age}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">닉네임</th>
                                <td class="tl">
                                        ${memberDetail.member_nickname}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">가입일</th>
                                <td class="tl">
                                        ${fn:substring(memberDetail.reg_date, 0, 10)}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">가입방식</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${memberDetail.cert_type eq 'KAKAO'}">카카오</c:when>
                                        <c:when test="${memberDetail.cert_type eq 'NAVER'}">네이버</c:when>
                                        <c:when test="${memberDetail.cert_type eq 'GOOGLE'}">구글</c:when>
                                        <c:when test="${memberDetail.cert_type eq 'APPLE'}">애플</c:when>
                                        <c:otherwise>일반가입</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">이름</th>
                                <td class="tl">
                                        ${memberDetail.member_name}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">연령대</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${memberDetail.age_group eq '10~19'}">10대</c:when>
                                        <c:when test="${memberDetail.age_group eq '20~29'}">20대</c:when>
                                        <c:when test="${memberDetail.age_group eq '30~39'}">30대</c:when>
                                        <c:when test="${memberDetail.age_group eq '40~49'}">40대</c:when>
                                        <c:when test="${memberDetail.age_group eq '50~59'}">50대</c:when>
                                        <c:when test="${memberDetail.age_group eq '60~69'}">60대</c:when>
                                        <c:when test="${memberDetail.age_group eq '70~79'}">70대</c:when>
                                        <c:when test="${memberDetail.age_group eq '80~89'}">80대</c:when>
                                        <c:when test="${memberDetail.age_group eq '90~99'}">90대</c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">생일</th>
                                <td class="tl">
                                    ${memberDetail.birth_date}
                                </td>
                            </tr>

                            <tr>
                                <th class="tl">자녀연령</th>
                                <td class="tl">
                                    ${memberDetail.children_age}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">메모</th>
                                <td class="tl">
                                    ${memberDetail.memo}
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <br>
            <a class="btn-large gray-o" onclick="fnMemberDelete('${memberDetail.member_cd}')">삭제 하기</a>
            <a class="btn-large default" onclick="goMemberModify('${memberDetail.member_cd}')">수정 하기</a>
        </div>
    </div>
  </div>
  
</body>
</html>