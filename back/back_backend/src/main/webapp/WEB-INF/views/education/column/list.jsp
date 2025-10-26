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
    $("#subType").change(function(){
        $('input[name=subType]').val($(this).val());
        document.frm.submit();
    });

    $("#creatorId").change(function(){
        $('input[name=creatorId]').val($(this).val());
        document.frm.submit();
    });
});

function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
}

const fnSearch = () => {
    let selType = $("#columnType option:selected").val();
    $("input[name=columnType]").val(selType);
    document.frm.submit();
}

const goModify = () => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/columnModify');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Regist' }));

    $(newForm).appendTo('body').submit();
}

const goDetail = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/columnDetail');
    newForm.append($('<input/>', {type: 'hidden', name: 'educationColumnId', value: val }));
    $(newForm).appendTo('body').submit();
}

</script>
</head>

<body>

    <div class="wrapper" id="wrapper">
        <jsp:include page="../../common/menu.jsp" flush="true">
            <jsp:param name="page" value="main" />
            <jsp:param name="main" value="0" />
        </jsp:include>
        <div class="contents active">
            <div class="head">
                <div class="sub-menu">
                    <h2><span></span>칼럼</h2>

                </div>
            </div>
            <div class="round body">
                <div class="body-head">
                    <%--<c:forEach var="data" items="${menuList}" varStatus="status">
                        <a class="btn-large default" data-id="${data.category_cd}" >${data.category_name}</a>
                    </c:forEach>--%>
                    <%--<a class="btn-large default <c:if test="${mediaType ne 'Video'}">gray-o</c:if> " href="/mediaVideo">영상정보</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'News'}">gray-o</c:if> " href="/mediaNews">인터넷기사</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'Blog'}">gray-o</c:if> " href="/mediaBlog">블로그</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'Game'}">gray-o</c:if> " href="/mediaGame">경기영상</a>--%>
                    <%--<a class="btn-large default <c:if test="${mediaType ne 'Interview'}">gray-o</c:if> " href="/mediaInterview">인터뷰/칼럼</a>--%>
                </div>
                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/columnList">
                            <input name="cp" type="hidden" value="1">
                            <input name="columnType" type="hidden" value="${columnType}">

                            <span class="title">구분</span>
                            <select id="columnType">
                                <option value="All" <c:if test="${columnType eq 'All'}">selected</c:if>>전체</option>
                                <c:forEach var="data" items="${menuList}" varStatus="status">
                                    <option value="${data.code_value}" <c:if test="${columnType eq data.code_value.toString()}">selected</c:if>>${data.category_name}</option>
                                </c:forEach>
                            </select>

                            <span class="title">등록일</span>
                            <input type="date" name="sDate" value="${params.sDate}">
                            -
                            <input type="date" name="eDate" value="${params.eDate}">

                            <span class="title ml_10">검색</span><input type="text" name="sTitle" placeholder="칼럼의 제목을 입력해주세요" value="${params.sTitle}">

                            <button class="btn-large default" onclick="fnSearch()">검색하기</button>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large default" onclick="goModify()">등록 하기</a>
                    </div>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over">
                        <colgroup>
                            <col width="5%">
                            <col width="60%">
                            <col width="10%">
                            <col width="10%">
                            <col width="5%">
                            <col width="5%">
                            <col width="5%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th><a>번호</a></th>
                            <th><a>제목</a></th>
                            <th>작성자</th>
                            <th>카테고리</th>
                            <th>조회수</th>
                            <th>등록일</th>
                            <th>노출/비노출</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty columnList}">
                                    <tr>
                                        <td colspan="7">
                                            등록된 데이터가 없습니다.
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${columnList}" varStatus="status">
                                        <tr onclick="goDetail('${item.education_column_id}')">
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
                                                ${item.writer}
                                            </td>
                                            <td>
                                                <c:forEach var="data" items="${menuList}" varStatus="status2">
                                                    <c:choose>
                                                        <c:when test="${item.column_type.toString() eq data.code_value.toString()}">
                                                            ${data.category_name}
                                                        </c:when>
                                                    </c:choose>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                ${item.view_cnt}
                                            </td>
                                            <td>
                                                ${fn:substring(item.reg_date, 0 ,10)}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.show_flag eq '0'}">
                                                        노출
                                                    </c:when>
                                                    <c:otherwise>
                                                        비노출
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