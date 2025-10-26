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
const goDetail = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/detail${mediaType}');
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaId', value: val }));
    newForm.append($('<input/>', {type: 'hidden', name: 'subType', value: '${subType}' }));
    $(newForm).appendTo('body').submit();
}

function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
}

const fnSearch = () => {
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
                    <h2><span></span>미디어 관리</h2>

                </div>
            </div>
            <div class="round body">
                <div class="body-head">
                    <a class="btn-large default <c:if test="${mediaType ne 'Video'}">gray-o</c:if> " href="/mediaVideo">영상정보</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'News'}">gray-o</c:if> " href="/mediaNews">인터넷기사</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'Blog'}">gray-o</c:if> " href="/mediaBlog">블로그</a>
                    <a class="btn-large default <c:if test="${mediaType ne 'Game'}">gray-o</c:if> " href="/mediaGame">경기영상</a>
                    <%--<a class="btn-large default <c:if test="${mediaType ne 'Interview'}">gray-o</c:if> " href="/mediaInterview">인터뷰/칼럼</a>--%>
                    <hr class="mt_10 mb_10">
                </div>
                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/media${mediaType}">
                            <input name="cp" type="hidden" value="1">
                            <input name="subType" type="hidden" value="${subType}">
                            <input name="creatorId" type="hidden" value="${params.creator_id}">

                            <c:if test="${mediaType ne 'Game'}">
                                <span class="title">크리에이터</span>
                                <select id="creatorId">
                                    <option value="">선택하기</option>
                                    <c:if test="${!empty creatorList}">
                                        <c:forEach var="item" items="${creatorList}" varStatus="status">
                                            <option value="${item.creator_id}" <c:if test="${params.creatorId eq item.creator_id}">selected</c:if>>${item.creator_name}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                                <span class="title">구분</span>
                                <select id="subType">
                                    <option value="All" <c:if test="${subType eq 'All'}">selected</c:if>>전체</option>
                                    <c:forEach var="data" items="${menuList}" varStatus="status">
                                        <option value="${data.code_value}" <c:if test="${subType eq data.code_value.toString()}">selected</c:if>>${data.category_name}</option>
                                    </c:forEach>
                                </select>
                            </c:if>

                            <span class="title">등록일</span>
                            <input type="date" name="sDate" value="${params.sDate}">
                            -
                            <input type="date" name="eDate" value="${params.eDate}">
                            <c:if test="${mediaType eq 'Game' && subType eq '0'}">
                                <span class="title ml_10">구분</span>
                                <select id="">
                                    <option value="videoType">전체</option>
                                    <option value="0">하이라이트 </option>
                                    <option value="1">다시보기</option>
                                </select>
                            </c:if>
                            <span class="title ml_10">검색</span><input type="text" name="sTitle" placeholder="미디어의 제목을 입력해주세요" value="${params.sTitle}">

                            <button class="btn-large default" onclick="fnSearch()">검색하기</button>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large default" onclick="location.href='/regist${mediaType}'">등록 하기</a>
                    </div>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over">
                        <colgroup>
                            <col width="55px">
                            <c:choose>
                                <c:when test="${mediaType eq 'Game' && subType eq '0' || mediaType eq 'News'}">
                                    <col width="150px">
                                    <col width="*">
                                    <col width="20%">
                                    <col width="20%">
                                    <col width="200px">
                                    <col width="100px">
                                    <col width="100px">
                                </c:when>
                            </c:choose>
                        </colgroup>
                        <thead>
                        <tr>
                            <th><a>번호</a></th>
                            <c:choose>
                                <c:when test="${mediaType eq 'Video'}">
                                    <th>제목</th>
                                    <th>등록일</th>
                                    <th>영상 등록일</th>
                                    <th>작성자</th>
                                    <th>카테고리</th>
                                </c:when>
                                <c:when test="${mediaType eq 'News'}">
                            <th>출처</th>
                            <th>제목</th>
                            <th>등록 대회</th>
                            <th>등록 리그</th>
                            <th>등록 학원/클럽</th>
                            <th>연령</th>
                            <th>등록일</th>
                            <th>기사 등록일</th>
                            <th>카테고리</th>
                                </c:when>
                                <c:when test="${mediaType eq 'Blog'}">
                                    <th>제목</th>
                                    <th>등록일</th>
                                    <th>블로그 등록일</th>
                                    <th>작성자</th>
                                    <th>카테고리</th>
                                </c:when>
                                <c:when test="${mediaType eq 'Game'}">
                                    <th>구분</th>
                                    <th>제목</th>
                                    <th>등록 대회</th>
                                    <th>등록 리그</th>
                                    <th>등록 학원/클럽</th>
                                    <th>연령</th>
                                    <th>등록일</th>
                                </c:when>
                            </c:choose>
                            <th>조회수</th>
                            <th>활성/비활성</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty mediaList}">
                                    <c:choose>
                                        <c:when test="${mediaType eq 'Video'}">
                                            <td colspan="7">
                                                등록된 미디어가 없습니다.
                                            </td>
                                        </c:when>
                                        <c:when test="${mediaType eq 'News'}">
                                            <td colspan="8">
                                                등록된 미디어가 없습니다.
                                            </td>
                                        </c:when>
                                        <c:when test="${mediaType eq 'Blog'}">
                                            <td colspan="3">
                                                등록된 미디어가 없습니다.
                                            </td>
                                        </c:when>
                                        <c:when test="${mediaType eq 'Interview'}">
                                            <td colspan="3">
                                                등록된 미디어가 없습니다.
                                            </td>
                                        </c:when>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${mediaList}" varStatus="status">
                                        <tr onclick="goDetail('${result.media_id}')">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                    <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <c:choose>
                                                <c:when test="${mediaType eq 'Video'}">
                                                    <td>
                                                        ${result.title}
                                                    </td>
                                                    <td>
                                                        ${fn:substring(result.reg_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                        ${fn:substring(result.submit_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                        ${result.creator_name}
                                                    </td>
                                                    <td>
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <c:if test="${result.sub_type eq data.code_value}">
                                                                ${data.category_name}
                                                            </c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        ${result.view_cnt}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.use_flag eq '0'}">
                                                                활성
                                                            </c:when>
                                                            <c:otherwise>
                                                                비활성
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                                <c:when test="${mediaType eq 'News'}">
                                                    <td>
                                                        ${result.source}
                                                    </td>
                                                    <td>
                                                        ${result.title}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${!empty result.cupInfoList}">
                                                                <c:forEach var="items" items="${result.cupInfoList}" varStatus="status">
                                                                    ${items.cup_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${!empty result.leagueInfoList}">
                                                                <c:forEach var="items" items="${result.leagueInfoList}" varStatus="status">
                                                                    ${items.league_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${!empty result.teamList}">
                                                                <c:forEach var="items" items="${result.teamList}" varStatus="status">
                                                                    ${items.nick_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        ${result.uage}
                                                    </td>
                                                    <td>
                                                        ${fn:substring(result.reg_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                        ${fn:substring(result.submit_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <c:if test="${result.sub_type eq data.code_value}">
                                                                ${data.category_name}
                                                            </c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        ${result.view_cnt}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.use_flag eq '0'}">
                                                                활성
                                                            </c:when>
                                                            <c:otherwise>
                                                                비활성
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                                <c:when test="${mediaType eq 'Blog'}">
                                                    <td>
                                                        ${result.title}
                                                    </td>
                                                    <td>
                                                            ${fn:substring(result.reg_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                            ${fn:substring(result.submit_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                        ${result.creator_name}
                                                    </td>
                                                    <td>
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <c:if test="${result.sub_type eq data.code_value}">
                                                                ${data.category_name}
                                                            </c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                            ${result.view_cnt}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.use_flag eq '0'}">
                                                                활성
                                                            </c:when>
                                                            <c:otherwise>
                                                                비활성
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                                <c:when test="${mediaType eq 'Game'}">
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${result.type eq '0'}">
                                                            하이라이트
                                                        </c:when>
                                                        <c:when test="${result.type eq '1'}">
                                                            다시보기
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td class="t_left">
                                                    ${result.title}
                                                </td>
                                                    <td class="t_left">
                                                        <c:choose>
                                                            <c:when test="${!empty result.cupInfoList}">
                                                                <c:forEach var="items" items="${result.cupInfoList}" varStatus="status">
                                                                    ${items.cup_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="t_left">
                                                        <c:choose>
                                                            <c:when test="${!empty result.leagueInfoList}">
                                                                <c:forEach var="items" items="${result.leagueInfoList}" varStatus="status">
                                                                    ${items.league_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${!empty result.teamList}">
                                                                <c:forEach var="items" items="${result.teamList}" varStatus="status">
                                                                    ${items.nick_name}
                                                                    <c:if test="${status.last eq false}">,<br/></c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                -
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                            ${result.uage}
                                                    </td>
                                                    <td>
                                                            ${fn:substring(result.reg_date, 0, 10)}
                                                    </td>
                                                    <td>
                                                            ${result.view_cnt}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.use_flag eq '0'}">
                                                                활성
                                                            </c:when>
                                                            <c:otherwise>
                                                                비활성
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                            </c:choose>
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