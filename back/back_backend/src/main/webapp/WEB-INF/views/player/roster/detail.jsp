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
<script src="/resources/jquery/jquery.form.js"></script>

<script type="text/javascript" src="/resources/js/igp.js"></script>
<script type="text/javascript" src="/resources/js/init.js"></script>
<script type="text/javascript" src="/resources/js/igp.file.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

    $(document).ready(function(){

        $(document).ready(function() {

            igpFile = new igp.file({fileLayer:'#fileLayer', boardSeq: '${method eq 'save' ? '' : rosterInfo.roster_id}', allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'], maxSize:5, foreignType: 'Roster'});
            igpFile.setFileList(false);

        });

    });

    const goDetail = (val) => {
        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/detailPlayer');
        newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));
        $(newForm).appendTo('body').submit();
    }

    const fnModifyRoster = (val) => {
        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/${type}Save');
        newForm.append($('<input/>', {type: 'hidden', name: 'rosterId', value: val }));
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'modify' }));
        $(newForm).appendTo('body').submit();
    }

    const fnDeleteRoster = (val) => {

        const confirmMsg = confirm('정말 삭제 하시겠습니까?');

        if (confirmMsg) {
            $("#frm").submit();
        }

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
                    <h2><span></span>선수 관리</h2>

                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <c:choose>
                        <c:when test="${type eq 'national'}">
                            <h4 class="view-title">대표팀 관리 > 국가대표 > 상세페이지 </h4>
                        </c:when>
                        <c:otherwise>
                            <h4 class="view-title">대표팀 관리 > 골든에이지 > 상세페이지 </h4>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">

                        </colgroup>
                        <tbody>
                        <tr>
                            <th class="tl">등록일</th>
                            <td class="tl">${fn:substring(rosterInfo.reg_date, 0, 10)}</td>
                        </tr>
                        <tr>
                            <th class="tl">활성/비활성</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${rosterInfo.use_flag eq '0'}">
                                        활성
                                    </c:when>
                                    <c:when test="${rosterInfo.use_flag eq '1'}">
                                        비활성
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                ${rosterInfo.title}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">연도</th>
                            <td class="tl">
                                ${rosterInfo.year}
                            </td>
                        </tr>

                        <c:if test="${type eq 'national'}">
                        <tr>
                            <th class="tl">나이</th>
                            <td class="tl">
                                    ${rosterInfo.uage}
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${type eq 'golden'}">
                            <tr>
                                <th class="tl">구분</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${rosterInfo.type eq '0'}">
                                            지역센터
                                        </c:when>
                                        <c:when test="${rosterInfo.type eq '1'}">
                                            5대광역
                                        </c:when>
                                        <c:when test="${rosterInfo.type eq '2'}">
                                            합동광역
                                        </c:when>
                                        <c:when test="${rosterInfo.type eq '3'}">
                                            퓨처팀
                                        </c:when>
                                        <c:when test="${rosterInfo.type eq '4'}">
                                            영재센터
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>

                        <%--<tr>
                            <th class="tl">첨부파일 </th>
                            <td class="tl">
                                <a class="btn-large gray-o">첨부된 파일명.pdf</a>
                            </td>
                        </tr>--%>
                        <tr>
                            <th class="tl">
                                설명
                            </th>
                            <td class="tl">
                                ${rosterInfo.comment}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <br>
                    <div class="scroll">
                        <div class="title">
                            <h3>
                                등록 선수
                            </h3>
                        </div>
                        <table cellspacing="0" class="update over">
                            <colgroup>
                                <col width="*">
                                <col width="*">
                                <col width="*">
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>NO</th>
                                <th>이름</th>
                                <th>포지션</th>
                                <th>생년월일</th>
                                <th>소속팀</th>
                            </tr>
                            </thead>
                            <tbody id="appendTbody">
                                <c:choose>
                                    <c:when test="${empty rosterPlayer}">
                                        <td colspan="4">
                                            등록된 선수가 없습니다.
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="result" items="${rosterPlayer}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${result.name}</td>
                                                <td>${result.position}</td>
                                                <td>${result.birthday}</td>
                                                <td>${result.team_name}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div><br>
                <div class="w100 tr mt_10">
                    <a class="btn-large gray-o" onclick="location.href='/${type}PlayerList'">목록으로</a>
                    <a class="btn-large gray-o" onclick="fnDeleteRoster('${rosterInfo.roster_id}')">삭제 하기</a>
                    <a class="btn-large default" onclick="fnModifyRoster('${rosterInfo.roster_id}')">수정 하기</a>
                </div>

            </div>
        </div>

    </div>
    <form id="frm" method="post" action="${method eq 'save' ? '/save_roster' : '/save_roster'}"  enctype="multipart/form-data">
        <input type="hidden" name="rosterId" value="${rosterInfo.roster_id}">
        <input type="hidden" name="method" value="delete">
        <input type="hidden" name="roster" value="${type}">
    </form>

</body>


</html>