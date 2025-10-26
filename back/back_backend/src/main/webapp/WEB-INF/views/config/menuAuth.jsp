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

    $('.btn-pop').on('click', function() {
       $("input[name=userId]").val($(this).attr('data-value'));
    });

    $(".btn-close-pop").on("click", function () {
        $("input[name=userId]").val('');
    })

    $('input[type="checkbox"]').on('click', function() {
        $('input[type="checkbox"]').prop('checked', false);
        $(this).prop('checked', true);
    });
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
    }

}

const fnModifyManager = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/menuAuthModify');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Modify' }));
    newForm.append($('<input/>', {type: 'hidden', name: 'userId', value: val }));
    $(newForm).appendTo('body').submit();
}

const fnRemoveManager = (val) => {
    const confirmMsg = confirm("해당 관리자를 삭제 하시겠습니까?");

    if (confirmMsg) {
        $.ajax({
            type: 'POST',
            url: '/update_manager',
            data: {userId : val, method: 'Delete'},
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
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="15%">
            <col width="15%">
            <col width="15%">
            <col width="15%">
            <col width="15%">
          </colgroup>
          <thead>
            <tr>
              <th>ID</th>
              <th>PW</th>
              <th>담당자</th>
              <th>구분</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
			<c:forEach var="result" items="${managerList}" varStatus="status">
			<tr>
				<td>
					${result.id}
				</td>
                <td>
                    <a class="btn-large red btn-pop" data-id="pop-openPop" data-value="${result.user_id}">변경하기</a>
				</td>
				<td>
                    ${result.user_name}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${result.grade eq '63'}">
                            일반 관리자
                        </c:when>
                    </c:choose>
                </td>
                <td>
                    <button class="btn-large default gray-o" onclick="fnModifyManager('${result.user_id}')">수정하기</button>
                    |
                    <button class="btn-large default" onclick="fnRemoveManager('${result.user_id}')">삭제하기</button>
                </td>
			</tr>
			</c:forEach>
          </tbody>
        </table>
        <div class="body-foot">
            <div class="others">
                <a class="btn-large default" onclick="fnMoveRegist();">추가하기</a>
            </div>
          </div>
      </div>
    </div>

    <div class="pop" id="pop-openPop">
        <div style="height:auto;">
            <div style="height:auto;">
                <div class="head">
                    비밀번호 변경
                </div>
                <input type="hidden" name="userId" value="">
                <div class="body" style="padding:15px 20px;">
                    <ul class="signup-list">
                        <li class="title">
                            <span class="title">변경할 비밀번호</span>
                            <input type="text" name="pw">
                        </li>
                    </ul>
                </div>
                <div class="foot">
                    <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="fnChangePw();"><span>변경하기</span></a>
                    <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                </div>
            </div>
        </div>
    </div>

</body>
</html>