<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <script src="../resources/jquery/jquery-3.3.1.min.js"></script>
    <script src="../resources/jquery/jquery-ui.js"></script>
    <script src="../resources/js/layout.js"></script>

    <link rel="stylesheet" href="../resources/css/layout.css">
    <link rel="stylesheet" href="../resources/xeicon/xeicon.min.css">
    <link rel="stylesheet" href="../resources/nanum/nanumsquare.css">
    <link rel="shortcut icon" href="../resources/ico/favicon.ico">
    <link rel="apple-touch-icon" href="../resources/ico/mobicon.png">

    <script type="text/javascript">

        $(document).ready(function() {

        });
        //페이징 처리

        const searchList = () => {

            const type = $("select[name='type'] option:selected").val();
            const answerFlag = $("select[name='answerFlag'] option:selected").val();
            const sDate = $("input[name=sDate]").val();
            const eDate = $("input[name=eDate]").val();
            const searchKeyword = $("input[name='searchKeyword']").val();

            if (sDate || eDate) {
                if (!sDate || !eDate) {
                    alert('올바른 날짜를 입력해주세요.');
                    return false;
                }
            }

            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.append($('<input/>', {type: 'hidden', name: 'type', value: type }));
            newForm.append($('<input/>', {type: 'hidden', name: 'answerFlag', value: answerFlag }));
            newForm.append($('<input/>', {type: 'hidden', name: 'sDate', value: sDate }));
            newForm.append($('<input/>', {type: 'hidden', name: 'eDate', value: eDate }));
            newForm.append($('<input/>', {type: 'hidden', name: 'searchKeyword', value: searchKeyword }));
            newForm.attr('action', '/oneToOneList');

            $(newForm).appendTo('body').submit();

        }

        const goDetail = (val) => {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/oneToOneDetail');
            newForm.append($('<input/>', {type: 'hidden', name: 'boardId', value: val }));
            $(newForm).appendTo('body').submit();
        }

        function gotoPaging(cp) {
            $('input[name=cp]').val(cp);
            document.frm.submit();
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
                <h2><span></span>1:1문의</h2>

            </div>
            <!--<div class="others">
<select>
<option>2020</option>
<option>2019</option>
<option>2018</option>
<option>2017</option>
<option>2016</option>
<option>2015</option>
<option>2014</option>
</select>
</div>-->
        </div>
        <div class="round body">

            <div class="body-head">
                <a class="btn-large default" href="/oneToOneList">1:1문의</a>
                <hr class="mt_10 mb_10">
                <div class="search">
                    <form name="frm" id="frm" method="post" action="/oneToOneList" onsubmit="return false;">
                        <input name="cp" type="hidden" value="1">
                        <input name="ageGroup" type="hidden" value="">
                        <span class="title ml_10">카테고리</span>
                        <select name="type">
                            <option value="">선택</option>
                            <option value="0" <c:if test="${param.type eq '0'}">selected</c:if>>계정 문의</option>
                            <option value="1" <c:if test="${param.type eq '1'}">selected</c:if>>대회, 리그 문의</option>
                            <option value="2" <c:if test="${param.type eq '2'}">selected</c:if>>학원, 클럽 문의</option>
                            <option value="3" <c:if test="${param.type eq '3'}">selected</c:if>>축구정보 문의</option>
                            <option value="4" <c:if test="${param.type eq '4'}">selected</c:if>>제휴 문의</option>
                            <option value="5" <c:if test="${param.type eq '5'}">selected</c:if>>기타</option>
                        </select>
                        <span class="title ml_10">답변상태</span>
                        <select name="answerFlag">
                            <option value="">전체</option>
                            <option value="0" <c:if test="${param.answerFlag eq '0'}">selected</c:if>>미답변</option>
                            <option value="1" <c:if test="${param.answerFlag eq '1'}">selected</c:if>>답변완료</option>
                        </select>
                        <span class="title ml_10">문의일</span>
                        <input name="sdate" type="date" value="${param.sdate}">
                        -
                        <input name="edate" type="date" value="${param.edate}">
                        <span class="title ml_10">검색</span><input type="text" name="searchKeyword" value="${param.searchKeyword}" placeholder="문의 제목을 입력해주세요">
                        <a class="btn-large default" onclick="searchList()">검색하기</a>
                    </form>
                </div>
                <div class="others">

<%--                    <select>--%>
<%--                        <option>최신순</option>--%>
<%--                        <option>조회수순</option>--%>
<%--                    </select>--%>
                </div>
            </div>

            <div class="scroll">
                <table cellspacing="0" class="update over">
                    <colgroup>
                        <col width="55px">

                    </colgroup>
                    <thead>
                    <tr>
                        <th>번호</th>
                        <th>종류</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>연락처</th>
                        <th>회원종류</th>
                        <th>답변여부</th>
                        <th>작성일</th>
                        <th>답변일</th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty oneToOneList}">
                                <tr>
                                    <td colspan="7">등록된 문의가 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="result" items="${oneToOneList}" varStatus="status">
                                    <tr onclick="goDetail('${result.board_id}')">
                                        <td>
                                            <c:choose>
                                                <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${result.type eq '0'}">계정문의</c:when>
                                                <c:when test="${result.type eq '1'}">대회,리그 문의</c:when>
                                                <c:when test="${result.type eq '2'}">학원,클럽 문의</c:when>
                                                <c:when test="${result.type eq '3'}">축구정보 문의</c:when>
                                                <c:when test="${result.type eq '4'}">제휴문의</c:when>
                                                <c:when test="${result.type eq '5'}">기타</c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            ${result.title}
                                        </td>
                                        <td>
                                            ${result.member_nickname}
                                            <c:if test="${!empty result.member_name}">
                                                /${result.member_name}
                                            </c:if>
                                        </td>
                                        <td>
                                            ${result.phone_no}
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${result.member_type eq '1'}">학부모</c:when>
                                                <c:when test="${result.member_type eq '2'}">감독/코치</c:when>
                                                <c:when test="${result.member_type eq '3'}">레슨 선생님</c:when>
                                                <c:when test="${result.member_type eq '4'}">학원/클럽 관계자</c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${result.answer_flag == '0'}">
                                                    미답변
                                                </c:when>
                                                <c:when test="${result.answer_flag == '1'}">
                                                    답변완료
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            ${fn:substring(result.reg_date, 0, 10)}
                                        </td>
                                        <td>
                                            ${fn:substring(result.answer_date, 0, 10)}
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
<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team"
    onsubmit="return false;">
    <input name="sFlag" type="hidden" value="10">
    <input name="cp" type="hidden" value="1">
    <input name="ageGroup" type="hidden" value="U18">
</form>
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="save_team">
    <input type="hidden" name="sFlag" value="2">
    <input type="hidden" name="teamId">
    <input type="hidden" name="emblem">
    <input name="ageGroup" type="hidden" value="U18">
</form>

</html>