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

        const goDetail = (val) => {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/${reqType}ReqDetail');
            newForm.append($('<input/>', {type: 'hidden', name: 'requestId', value: val }));
            $(newForm).appendTo('body').submit();
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
                    <h2><span></span>문의/공지</h2>
                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <a class="btn-large default <c:if test="${reqType ne 'match'}">gray</c:if>" href="/matchReqList">경기 결과 수정요청</a>
                    <a class="btn-large default <c:if test="${reqType ne 'team'}">gray</c:if>" href="/teamReqList">팀 정보 수정요청</a>
                    <a class="btn-large default <c:if test="${reqType ne 'etc'}">gray</c:if>" href="/etcReqList">기타 수정요청</a>
                    <hr class="mt_10 mb_10">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/${reqType}ReqList" onsubmit="return false;">
                            <input name="cp" type="hidden" value="1">
                            <input name="requestType" type="hidden" value="${requestType}">

                            <c:if test="${reqType eq 'match'}">
                                <span class="title ml_10">대회/리그 선택</span>
                                <select name="requestType" onchange="fnChangeRequestType(this.value)">
                                    <option value="0" <c:if test="${requestType eq '0'}">selected</c:if>>대회</option>
                                    <option value="1" <c:if test="${requestType eq '1'}">selected</c:if>>리그</option>
                                </select>
                            </c:if>
                            <span class="title ml_10">문의일</span>
                            <input type="date" name="sdate" value="${sdate}">
                            -
                            <input type="date" name="edate" value="${edate}">
                            <span class="title ml_10">검색</span><input type="text" value="${searchKeyword}" name="searchKeyword" placeholder="문의 제목을 입력해주세요">
                            <a class="btn-large default" onclick="searchList()">검색하기</a>
                        </form>
                    </div>

                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over fix">
                        <colgroup>
                            <c:choose>
                                <c:when test="${reqType eq 'match'}">
                                    <col width="55px">
                                    <col width="380px">
                                    <col width="120px">
                                    <col width="120px">
                                    <col width="*">
                                    <col width="130px">
                                    <col width="80px">
                                    <col width="80px">
                                    <col width="100px">
                                </c:when>
                                <c:when test="${reqType eq 'team'}">
                                    <col width="55px">
                                    <col width="15%">
                                    <col width="calc(60% - 55px)">
                                    <col width="8%">
                                    <col width="8%">
                                    <col width="8%">
                                    <col width="8%">
                                    <col width="10%">
                                </c:when>
                                <c:when test="${reqType eq 'etc'}">
                                    <col width="55px">
                                    <col width="calc(60% - 55px)">
                                    <col width="8%">
                                    <col width="8%">
                                    <col width="10%">
                                </c:when>
                            </c:choose>
                        </colgroup>
                        <thead>
                        <tr>
                            <c:choose>
                                <c:when test="${reqType eq 'match'}">
                                    <th>번호</th>
                                    <th>대회명</th>
                                    <th>홈팀</th>
                                    <th>어웨이팀</th>
                                    <th>내용</th>
                                    <th>작성자</th>
                                    <th>회원종류</th>
                                    <th>답변여부</th>
                                    <th>작성일</th>
                                </c:when>
                                <c:when test="${reqType eq 'team'}">
                                    <th>번호</th>
                                    <th>팀이름</th>
                                    <th>내용</th>
                                    <th>연령</th>
                                    <th>작성자</th>
                                    <th>회원종류</th>
                                    <th>답변여부</th>
                                    <th>작성일</th>
                                </c:when>
                                <c:when test="${reqType eq 'etc'}">
                                    <th>번호</th>
                                    <th>내용</th>
                                    <th>작성자</th>
                                    <th>답변여부</th>
                                    <th>작성일</th>
                                </c:when>
                            </c:choose>

                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty reqList}">
                                    <tr>
                                        <td colspan="5">등록된 내용이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${reqType eq 'match'}">
                                            <c:forEach var="result" items="${reqList}" varStatus="status">
                                                <tr onclick="goDetail('${result.request_id}')">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                            <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                            ${result.child.match_name}
                                                    </td>
                                                    <td>
                                                            ${result.child.home}
                                                    </td>
                                                    <td>
                                                            ${result.child.away}
                                                    </td>
                                                    <td class="t_left dot">
                                                            ${result.content}
                                                    </td>

                                                    <td>
                                                            ${result.member_nickname}
                                                        <c:if test="${!empty result.member_name}">
                                                            /${result.member_name}
                                                        </c:if>
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
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${reqType eq 'team'}">
                                            <c:forEach var="result" items="${reqList}" varStatus="status">
                                                <tr onclick="goDetail('${result.request_id}')">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                            <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                            ${result.child.team_name}
                                                    </td>
                                                    <td class="t_left dot">
                                                            ${result.content}
                                                    </td>
                                                    <td class="">
                                                            ${result.child.uage}
                                                    </td>
                                                    <td>
                                                        ${result.member_nickname}
                                                        <c:if test="${!empty result.member_name}">
                                                            /${result.member_name}
                                                        </c:if>
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
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${reqType eq 'etc'}">
                                            <c:forEach var="result" items="${reqList}" varStatus="status">
                                                <tr onclick="goDetail('${result.request_id}')">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                            <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="t_left dot">
                                                            ${result.content}
                                                    </td>
                                                    <td>
                                                            ${result.member_nickname}
                                                        <c:if test="${!empty result.member_name}">
                                                            /${result.member_name}
                                                        </c:if>
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
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
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