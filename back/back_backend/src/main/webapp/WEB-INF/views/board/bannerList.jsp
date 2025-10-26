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
            document.frm.submit();

        }

        function gotoPaging(cp) {
            $('input[name=cp]').val(cp);
            document.frm.submit();
        }

        const fnChangeRequestType = (val) => {
            const sDate = $("input[name=sDate]").val();
            const eDate = $("input[name=eDate]").val();

            if (sDate || eDate) {
                if (!sDate || !eDate) {
                    alert('올바른 날짜를 입력해주세요.');
                    return false;
                }
            }

            $("input[name=requestType]").val(val);

            document.frm.submit();
        }

        const goDetail = (val) => {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/detailBanner');
            newForm.append($('<input/>', {type: 'hidden', name: 'bannerId', value: val }));
            $(newForm).appendTo('body').submit();
        }

        const goSave = (val) => {

            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/saveBanner');
            newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'save' }));

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
                    <h2><span></span>문의/공지 > 배너 관리</h2>
                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/bannerList" onsubmit="return false;">
                            <input name="cp" type="hidden" value="1">
                            <span class="title ml_10">검색</span><input type="text" value="${searchKeyword}" name="sKeyword" placeholder="문의 제목을 입력해주세요">
                            <a class="btn-large default" onclick="searchList()">검색하기</a>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large default" onclick="goSave()">작성하기</a>
                    </div>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over fix">
                        <colgroup>
                            <col width="*">
                            <col width="40%">
                            <col width="*">
                            <col width="*%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>번호</th>
                            <th>제목</th>
                            <th>조회수</th>
                            <th>작성일</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty bannerList}">
                                    <tr>
                                        <td colspan="4">등록된 내용이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${bannerList}" varStatus="stauts">
                                        <tr onclick="goDetail('${item.banner_id}')">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                    <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${item.title}
                                            </td>
                                            <td>
                                                ${item.view_cnt}
                                            </td>
                                            <td>
                                                ${fn:substring(item.reg_date, 0, 10)}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
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