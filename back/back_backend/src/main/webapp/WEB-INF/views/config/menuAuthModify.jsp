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

const fnChangePw = () => {

    const confirmMsg = confirm("비밀번호를 변경 하시겠습니까?");

    if (confirmMsg) {

        let userId = $("input[name=userId]").val();
        let pw = $("input[name=pw]").val();

        if(!pw) {
            alert("변경할 비밀번호를 입력해 주세요.");
            return false;
        }

        let param = {
            userId : userId,
            pw : pw
        };

        $.ajax({
            type: 'POST',
            url: '/update_pw',
            data: param,
            success: function(res) {
                if (res.state == "SUCCESS") {
                    alert('비밀번호 변경이 완료 되었습니다.');
                    location.reload();
                } else {
                    alert('비밀번호 변경에 실패했습니다.');
                    return false;
                }
            }
        });

        document.updateFrm.submit();
    }

}

const fnRemoveManager = (val) => {
    const confirmMsg = confirm("해당 관리자를 삭제 하시겠습니까?");

    if (confirmMsg) {
        $.ajax({
            type: 'POST',
            url: '/update_manager',
            data: {userId : val},
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

const fnMoveRegist = () => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/menuAuthModify');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Regist' }));
    $(newForm).appendTo('body').submit();
}

const fnModifyMenuAuth = () => {
    const method = '${method}';

    const confirmMsg = method == 'Regist' ? confirm('등록 하시겠습니까?') : confirm('수정 하시겠습니까?');

    if (confirmMsg) {
        let userId = $("input[name=userId]").val();
        let id = $("input[name=id]").val();
        let pw = $("input[name=pw]").val();
        let userName = $("input[name=userName]").val();
        let grade = $("select[name=grade]").val();
        const chkMatch = $("input[type=checkbox]:checked");
        let menuArr = [];

        chkMatch.each(function(i) {
            menuArr.push($(this).val());
        });

        if (!id) {
            alert('아이디를 입력 해주세요.');
            return false;
        }
        if (!pw && method == 'Regist') {
            alert('비밀번호를 입력 해주세요.');
            return false;
        }
        if (!userName) {
            alert('담당자를 입력 해주세요.');
            return false;
        }

        let param = {
            id: id,
            pw: pw,
            userName: userName,
            grade: grade,
            method: method,
            menuArr: JSON.stringify(menuArr),
            userId: userId
        }

        $.ajax({
            type: 'POST',
            url: '/update_manager',
            data: param,
            success: function(res) {
                console.log('res : ', res);
                if (res.state == 'SUCCESS') {
                    alert(method == 'Regist' ? '등록을 완료 했습니다.' : '수정을 완료 했습니다.');
                    location.href = "/menuAuth";
                } else {
                    alert(method == 'Regist' ? '등록에 실패 했습니다' : '수정에 실패 했습니다.');
                    return false;
                }
            }
        });

    }

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
  		  	<h2><span></span>설정 > 권한별 메뉴 설정</h2>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
        </div>

        <div class="title">
          <h3>
              계정 정보
          </h3>
        </div>

        <table cellspacing="0" class="update">
          <colgroup>
            <c:choose>
                <c:when test="${method eq 'Regist'}">
                    <col width="15%">
                    <col width="15%">
                    <col width="15%">
                    <col width="15%">
                </c:when>
                <c:otherwise>
                    <col width="15%">
                    <col width="15%">
                    <col width="15%">
                </c:otherwise>
            </c:choose>
          </colgroup>
          <thead>
          <c:choose>
              <c:when test="${method eq 'Regist'}">
                  <tr>
                      <th>ID</th>
                      <th>PW</th>
                      <th>담당자</th>
                      <th>구분</th>
                  </tr>
              </c:when>
              <c:otherwise>
                  <tr>
                      <th>ID</th>
                      <th>담당자</th>
                      <th>구분</th>
                  </tr>
              </c:otherwise>
          </c:choose>

          </thead>
          <tbody>
			<tr>
				<td>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <input type="text" name="id">
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="userId" value="${userInfo.user_id}">
                            <input type="hidden" name="pw" value="${userInfo.pw}">
                            <input type="text" name="id" value="${userInfo.id}">
                        </c:otherwise>
                    </c:choose>
				</td>
                <c:if test="${method eq 'Regist'}">
                    <td>
                        <input type="password" name="pw">
                    </td>
                </c:if>
				<td>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <input type="text" name="userName">
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="userName" value="${userInfo.user_name}">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <select name="grade">
                                <option value="127">최고관리자</option>
                                <option value="63">일반관리자</option>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <select name="grade">
                                <option value="127" <c:if test="${userInfo.grade eq 127}">selected</c:if>>최고관리자</option>
                                <option value="63" <c:if test="${userInfo.grade eq 63}">selected</c:if>>일반관리자</option>
                            </select>
                        </c:otherwise>
                    </c:choose>
                </td>
			</tr>
          </tbody>
        </table>

        <br />

        <div class="title">
          <h3 class="w100">
              권한 관리
          </h3>
        </div>

        <table cellspacing="0" class="update">
            <colgroup>
                <col width="15%">
                <col width="70%">
            </colgroup>
            <tbody>
                <c:forEach var="item" items="${upperMenuList}" varStatus="status">
                    <tr>
                        <th>
                            ${item.menu_name}
                        </th>
                        <td class="tl" style="width: 30%;">
                            <c:choose>
                                <c:when test="${method eq 'Regist'}">
                                    <c:if test="${!empty menuList}">
                                        <c:forEach var="result" items="${menuList}" varStatus="status">
                                            <c:if test="${result.upper_menu_id eq item.menu_id}">
                                                <input type="checkbox" name="teamChk${status.index}" id="teamChk${status.index}" value="${result.menu_id}"> <label for="teamChk${status.index}">${result.menu_name}</label>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${!empty menuList}">
                                        <c:forEach var="result" items="${menuList}" varStatus="status">
                                            <c:if test="${result.upper_menu_id eq item.menu_id}">
                                                <c:set var="isChecked" value="false" />
                                                <c:if test="${!empty userMenuList}">
                                                    <c:forEach var="result2" items="${userMenuList}">
                                                        <c:if test="${result2.menu_id eq result.menu_id}">
                                                            <c:set var="isChecked" value="true" />
                                                        </c:if>
                                                    </c:forEach>
                                                </c:if>
                                                <input type="checkbox" name="teamChk${status.index}" id="teamChk${status.index}" value="${result.menu_id}" <c:if test="${isChecked eq true}">checked</c:if>>
                                                <label for="teamChk${status.index}">${result.menu_name}</label>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="body-foot">
            <div class="others">
                <a class="btn-large default" onclick="fnModifyMenuAuth()">
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            저장하기
                        </c:when>
                        <c:otherwise>
                            수정하기
                        </c:otherwise>
                    </c:choose>
                </a>
            </div>
          </div>
      </div>
    </div>


</body>
</html>