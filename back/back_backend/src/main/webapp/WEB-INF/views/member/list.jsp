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
                <div class="user_counter">
                    <div class="us_1">
                        <p>총 회원수
                            <br>
                            <span>
                                <c:if test="${!empty memberTotalMap.totalMemberCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.totalMemberCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_2">
                        <p>신규 회원수<br>
                            <span>
                                <c:if test="${!empty memberTotalMap.newCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.newCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>휴면 회원수<br>
                            <span>
                                <c:if test="${!empty memberTotalMap.sleepCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.sleepCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>총 방문자수<br>
                            <span>
                                <c:if test="${!empty total}">
                                    <fmt:formatNumber value="${total}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>오늘 방문자수<br>
                            <span>
                                <c:if test="${!empty today}">
                                    <fmt:formatNumber value="${today}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    
                    <%--<div class="us_3">
                        <p>총 방문자수 10분<br>
                            <span>
                                <c:if test="${!empty totalMinute}">
                                    <fmt:formatNumber value="${totalMinute}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>오늘 방문자수 10분<br>
                            <span>
                                <c:if test="${!empty todayMinute}">
                                    <fmt:formatNumber value="${todayMinute}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>--%>

                </div>


                <hr class="mt_10 mb_10">
                <a class="btn-large default <c:if test="${memberState ne '1'}">gray-o</c:if>" href="#" onclick="goMemberList('1')">일반회원</a>
                <a class="btn-large default <c:if test="${memberState ne '4'}">gray-o</c:if>" href="#" onclick="goMemberList('4')">휴면회원</a>
                <c:if test="${login.grade == 127}">
                    <div class="others">
                        <button class="btn-large default" onclick="fnDownload()">목록 엑셀 다운로드</button>
                    </div>
                </c:if>
                <hr class="mt_10 mb_10">
            </div>
            <div class="body-head">
                <div class="search">
                    <form name="frm" id="frm" method="post" action="list" onsubmit="return false;">
                        <span class="title">검색</span>
                        <input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="이메일, 이름 및 닉네임을 입력해주세요">
                        <input name="cp" type="hidden" value="${cp}">
                        <input name="memberState" type="hidden" value="${memberState}">
                        <span class="title ml_10">가입일</span>
                        <input type="date" value="${sDate}" name="sDate">
                        -
                        <input type="date" value="${eDate}" name="eDate">
                        <select name="memberType" id="selMemberType">
                        	<option value="" <c:if test="${memberType eq ''}">selected</c:if>>회원종류선택</option>
                        	<option value="PARENTS" <c:if test="${memberType eq 'PARENTS'}">selected</c:if>>학부모</option>
                        	<option value="DIRECTOR" <c:if test="${memberType eq 'DIRECTOR'}">selected</c:if>>감독/코치</option>
                        	<option value="TEACHER" <c:if test="${memberType eq 'TEACHER'}">selected</c:if>>레슨 선생님</option>
                        	<option value="ACADENY" <c:if test="${memberType eq 'ACADENY'}">selected</c:if>>학원/클럽 관계자</option>
                        	<option value="PLAYER" <c:if test="${memberType eq 'PLAYER'}">selected</c:if>>선수</option>
                        	<option value="ETC" <c:if test="${memberType eq 'ETC'}">selected</c:if>>기타</option>
                        </select>
                        <select name="sex" id="selSex">
                        	<option value="" <c:if test="${sex eq ''}">selected</c:if>>성별선택</option>
                        	<option value="M" <c:if test="${sex eq 'M'}">selected</c:if>>남</option>
                        	<option value="W" <c:if test="${sex eq 'W'}">selected</c:if>>여</option>
                        </select>
                        <select name="uage" id="selUage">
                        	<option value="" <c:if test="${uage eq ''}">selected</c:if>>관심연령선택</option>
                        	<c:forEach var="result" items="${uageList}" varStatus="status">
								<option value="${result.uage}" <c:if test="${uage eq result.uage}">selected</c:if>>${result.uage}</option>
							</c:forEach>
						</select>
                        <a class="btn-large default" onclick="searchList()">검색하기</a>
                    </form>
                </div>
                <div class="others">
                    <a class="btn-large default"  onclick="goRegist()">등록 하기</a>
                </div>
            </div>

            <div class="scroll">
                <table cellspacing="0" class="update over">
                    <colgroup>
                    	<col width="55px">
                        <col width="55px">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>이메일</th>
                            <th>이름</th>
                            <th>생일</th>
                            <th>성별</th>
                            <th>회원종류</th>
                            <th>가입방식</th>
                            <th>가입일</th>
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
                                <tr onclick="goDetail('${result.member_cd}')">
                                    <td>
                                        <c:choose>
                                            <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                            <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                        </c:choose>
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
                                                ${result.birth_day}
                                            </c:when>
                                            <c:otherwise>
                                                ${result.age}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.sex eq 'M'}">남</c:when>
                                            <c:when test="${result.sex eq 'W'}">여</c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.position eq 'PARENTS'}">학부모</c:when>
                                            <c:when test="${result.position eq 'DIRECTOR'}">감독/코치</c:when>
                                            <c:when test="${result.position eq 'TEACHER'}">레슨 선생님</c:when>
                                            <c:when test="${result.position eq 'ACADENY'}">학원/클럽 관계자</c:when>
                                            <c:when test="${result.position eq 'PLAYER'}">선수</c:when>
                                            <c:when test="${result.position eq 'ETC'}">기타</c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.cert_type eq 'NAVER'}">네이버</c:when>
                                            <c:when test="${result.cert_type eq 'KAKAO'}">카카오</c:when>
                                            <c:otherwise>일반회원</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        ${fn:substring(result.reg_date, 0, 10)}
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