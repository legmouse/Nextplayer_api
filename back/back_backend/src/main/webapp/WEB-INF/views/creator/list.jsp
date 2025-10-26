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
});
const goDetail = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/creatorDetail${mediaType}');
    newForm.append($('<input/>', {type: 'hidden', name: 'creatorId', value: val }));
    $(newForm).appendTo('body').submit();
}

function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
}

const fnSearch = () => {
    document.frm.submit();
}

const goModify = (val) => {

    const mediaType = '${mediaType}'

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/creator_' + val);
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));

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
                    <h2><span></span>크리에이터 관리</h2>

                </div>
            </div>
            <div class="round body">
                <div class="body-head">
                    <a class="btn-large default <c:if test="${mediaType ne 'Video'}">gray-o</c:if> " href="/creatorVideo">영상정보</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'News'}">gray-o</c:if> " href="/creatorNews">인터넷기사</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'Blog'}">gray-o</c:if> " href="/creatorBlog">블로그</a>
                    <%--<a class="btn-large default <c:if test="${mediaType ne 'Interview'}">gray-o</c:if> " href="/mediaInterview">인터뷰/칼럼</a>--%>
                    <hr class="mt_10 mb_10">
                </div>
                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/creator${mediaType}">
                            <input name="cp" type="hidden" value="1">

                            <span class="title ml_10">검색</span><input type="text" name="sKeyword" placeholder="크리에이터 이름을 입력해주세요" value="${params.sTitle}">

                            <button class="btn-large default" onclick="fnSearch()">검색하기</button>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large default" onclick="goModify('regist')">등록 하기</a>
                    </div>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over">
                        <colgroup>
                            <col width="5%">
                            <col width="70%">
                            <col width="5%">
                            <col width="20%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th><a>번호</a></th>
                            <th>크리에이터명</th>
                            <th>노출 여부</th>
                            <th>등록일</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty creatorList}">
                                    <td colspan="4">
                                        등록된 미디어가 없습니다.
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${creatorList}" varStatus="status">
                                        <tr onclick="goDetail('${result.creator_id}')">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                    <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${result.creator_name}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.show_flag eq '0'}">
                                                        노출
                                                    </c:when>
                                                    <c:when test="${result.show_flag eq '0'}">
                                                        비노출
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${result.reg_date}
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