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

    const fnGoModify = (val) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/savePlayer');
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'modify' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));

        $(newForm).appendTo('body').submit();
    }

    const fnDeletePlayer = (val) => {

        const confirmMsg = confirm('정말 삭제 하시겠습니까?');

        if (confirmMsg) {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/delete_player');
            newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'delete' }));
            newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));
            $(newForm).appendTo('body').submit();
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
                    <h2><span></span>선수 관리</h2>

                </div>
            </div>
            <div class="round body">

                <div class="body-head">

                    <h4 class="view-title">대표팀 관리 > 선수관리 > 등록하기 </h4>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">

                        </colgroup>
                        <tbody>
                        <c:if test="${!empty playerInfo}">
                            <tr>
                                <th class="tl">등록일</th>
                                <td class="tl">${fn:substring(playerInfo.reg_date,0 , 10)}</td>
                            </tr>
                            <tr>
                                <th class="tl">활성/비활성</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${playerInfo.use_flag eq '0'}">
                                            활성
                                        </c:when>
                                        <c:when test="${playerInfo.use_flag eq '1'}">
                                            비활성
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">이름<em>*</em></th>
                                <td class="tl">
                                    ${playerInfo.name}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">포지션</th>
                                <td class="tl">
                                    ${playerInfo.position}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">생년월일</th>
                                <td class="tl">
                                    ${playerInfo.birthday}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    초등학교
                                </th>
                                <td class="tl">
                                <c:forEach var="result" items="${playerHistory}" varStatus="status">
                                    <c:if test="${result.team_type eq '0'}">
                                ${result.year}년 ${result.team_name}&ensp;
                                    </c:if>
                                </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    중학교
                                </th>
                                <td class="tl">
                                <c:forEach var="result" items="${playerHistory}" varStatus="status">
                                    <c:if test="${result.team_type eq '1'}">
                                        ${result.year}년 ${result.team_name}&ensp;
                                    </c:if>
                                </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    고등학교
                                </th>
                                <td class="tl">
                                <c:forEach var="result" items="${playerHistory}" varStatus="status">
                                    <c:if test="${result.team_type eq '2'}">
                                        ${result.year}년 ${result.team_name}&ensp;
                                    </c:if>
                                </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    대학교
                                </th>
                                <td class="tl">
                                <c:forEach var="result" items="${playerHistory}" varStatus="status">
                                    <c:if test="${result.team_type eq '3'}">
                                        ${result.year}년 ${result.team_name}&ensp;
                                    </c:if>
                                </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    프로
                                </th>
                                <td class="tl">
                                    <c:forEach var="result" items="${playerHistory}" varStatus="status">
                                        <c:if test="${result.team_type eq '4'}">
                                            ${result.year}년 ${result.team_name}&ensp;
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><br>
                <div class="w100 tr mt_10">
                    <a class="btn-large gray-o" onclick="location.href='/playerList'">목록으로</a>
                    <a class="btn-large gray-o" onclick="fnDeletePlayer('${playerInfo.player_id}')">삭제하기</a>
                    <a class="btn-large default" onclick="fnGoModify('${playerInfo.player_id}')">수정 하기</a>
                </div>

            </div>
        </div>
    </div>

</body>
</html>