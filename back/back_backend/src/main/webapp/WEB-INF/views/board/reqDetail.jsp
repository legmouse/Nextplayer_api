<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("enter", "\n"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <script src="../resources/jquery/jquery-3.3.1.min.js"></script>
    <script src="../resources/jquery/jquery-ui.js"></script>
    <script src="/resources/jquery/jquery.form.js"></script>
    <script src="../resources/js/layout.js"></script>

    <script type="text/javascript" src="/resources/js/igp.js"></script>
    <script type="text/javascript" src="/resources/js/init.js"></script>
    <script type="text/javascript" src="/resources/js/igp.file.js"></script>

    <link rel="stylesheet" href="../resources/css/layout.css">
    <link rel="stylesheet" href="../resources/xeicon/xeicon.min.css">
    <link rel="stylesheet" href="../resources/nanum/nanumsquare.css">
    <link rel="shortcut icon" href="../resources/ico/favicon.ico">
    <link rel="apple-touch-icon" href="../resources/ico/mobicon.png">

    <script type="text/javascript">

        $(document).ready(function() {
            const reqType = '${reqType}';
            igpFile = new igp.answerImgFile({
                fileLayer: reqType == 'match' ? '#fileLayer' : (reqType == 'team' ? '#fileLayer2' : '#fileLayer3'),
                seq: '${method eq 'save' ? '' : reqDetail.request_id}',
                allowExt:['JPG', 'JPEG', 'PNG'],
                maxSize:5,
                foreignType: 'Request'
            });
            igpFile.setImgFileList(true);

            $('#eBtnSubmit').click(function(e) {
                var txt = $(this).text().trim();
                $('#frm').ajaxIgp({
                    beforeSubmit:function(){
                        if($("#answer").val() == null || $("#answer").val() == ""){
                            alert('답변을 입력해주세요');
                            $("#title").focus();
                            return false;
                        }
                        if(confirm(txt+'하시겠습니까?')){
                        }else{
                            return false;
                        }
                        $('#page-loading').show();
                        return true;

                    }, success:function(res){
                        alert(txt+'되었습니다');
                        $('#page-loading').hide();
                        location.href = '/${reqType}ReqList';
                        //location.reload();
                    },
                    timeout: 1000*60*10
                });
                e.preventDefault();
                return false;
            });

            $('img').on('click', function(){

                let isClick = $(this).attr('data-isClicked')


                $(this).attr('style', '');

                const bfCss = {
                    'object-fit': 'contain',
                    'border': '1px solid #ddd',
                    'width': '100px',
                    'height': '100px',
                    'margin-top': '10px'
                }

                const afCss = {
                    'max-width': '900px',
                    'margin-top': '10px'
                }

                if (isClick === '0') {
                    $(this).attr('data-isClicked', '1');
                    $(this).css(afCss);
                } else {
                    $(this).attr('data-isClicked', '0');
                    $(this).css(bfCss);
                }

            })

        });


        //페이징 처리

        /*const goModifyMatch = (id, groups, matchType) => {
            const reqType = '${reqType}';
            const childTB = '${childTB}';
            const ageGroup = '${ageGroup}';

            let targetUrl = "";
            if (reqType == 'match') {
                if (matchType == 'Cup') {
                    if (childTB.includes('Sub_Match')) {
                        targetUrl = "/cupMgrSubMatch?ageGroup=" + ageGroup + "&cupId=" + id + "&groups=" + groups;
                    } else if (childTB.includes('Tour_Match')) {
                        targetUrl = "/cupMgrTourMatch?ageGroup=" + ageGroup + "&cupId=" + id + "&round=" + groups;
                    } else if (childTB.includes('Main_Match')) {
                        targetUrl = "/cupMgrMainMatch?ageGroup=" + ageGroup + "&cupId=" + id + "&groups=" + groups;
                    }
                    window.open(targetUrl);
                }
                if (matchType == 'League') {
                    targetUrl = "/leagueMgrInfo?ageGroup=";
                    window.open(targetUrl + ageGroup + "&leagueId=" + id + "&months=" + groups);
                }
            }
        }*/

        /*const goModifyTeam = (id, year) => {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/playerList');
            newForm.attr("target", id);
            newForm.append($('<input/>', {type: 'hidden', name: 'teamId', value: id }));
            newForm.append($('<input/>', {type: 'hidden', name: 'sYear', value: year }));

            $(newForm).appendTo('body').submit();
        }*/

        /*const fnModifyRequest = (val) => {
            <%--let newForm = $('<form></form>');--%>
            <%--newForm.attr('name', 'newForm');--%>
            <%--newForm.attr('method', 'post');--%>
            <%--newForm.attr('action', '/board/modify_request');--%>
            <%--newForm.append($('<input/>', {type: 'hidden', name: 'requestId', value: '${reqDetail.request_id}' }));--%>
            <%--newForm.append($('<input/>', {type: 'hidden', name: 'method', value: val }));--%>
            <%--newForm.append($('<input/>', {type: 'hidden', name: 'reqType', value: '${reqType}'}));--%>

            <%--$(newForm).appendTo('body').submit();--%>
            const requestId = '${reqDetail.request_id}';
            const reqType = '${reqType}';

            const answer = $("#answer").val();

            const param = {
                requestId : requestId,
                method : val,
                reqType : reqType,
                answer: answer
            }

            $.ajax({
                type: 'POST',
                url: '/modify_request',
                data : param,
                success : function(res) {
                    if (res.state == 'SUCCESS') {
                        alert('처리 완료 되었습니다.');
                        location.href = "/" + reqType + "ReqList";
                    } else {
                        alert('처리 실패 했습니다.');
                        location.reload();
                    }
                }
            })
        }*/

        const fnCancelAnswer = () => {
            const requestId = '${reqDetail.request_id}';
            const reqType = '${reqType}';

            const confirmMsg = confirm('답변 취소 하시겠습니까?');

            const param = {
                requestId : requestId
            }

            if (confirmMsg) {
                $.ajax({
                    type: 'POST',
                    url: '/cancel_answer_request',
                    data : param,
                    success : function(res) {
                        if (res.state == 'SUCCESS') {
                            alert('처리 완료 되었습니다.');
                            location.href = "/" + reqType + "ReqList";
                        } else {
                            alert('처리 실패 했습니다.');
                            location.reload();
                        }
                    }
                })
            }
        }

        const fnRemove = (val) => {
            const confirmMsg = confirm('정말 삭제 하시겠습니까?');
            const reqType = '${reqType}';
            if (confirmMsg) {

                let formData = new FormData();
                formData.append("requestId", val);
                formData.append("method", "delete");


                $.ajax({
                    type: 'POST',
                    url: '/modify_request',
                    data: formData,
                    contentType : false,
                    processData : false,
                    success: function(res) {
                        if(res.state == 'SUCCESS') {
                            location.href = "/" + reqType + "ReqList";
                        }
                    }
                });

            }
        }

        const fnMoveDetailMgr = (cupId, uage, groups, matchCategory) => {

            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('action', '/cupMgr' + matchCategory + 'Match');
            newForm.attr('method', 'post');
            newForm.attr('target', '_blank');

            newForm.append($('<input/>', {type: 'hidden', name: 'cupId', value: cupId }));
            newForm.append($('<input/>', {type: 'hidden', name: 'ageGroup', value: uage }));

            if (matchCategory == 'Tour') {
                newForm.append($('<input/>', {type: 'hidden', name: 'round', value: groups }));
            } else {
                newForm.append($('<input/>', {type: 'hidden', name: 'groups', value: groups }));
            }
            $(newForm).appendTo('body').submit();
        }

        const fnMoveDetailLeagueMgr = (leagueId, uage, matchDate) => {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('action', '/leagueMgrInfo');
            newForm.attr('method', 'post');
            newForm.attr('target', '_blank');
            newForm.append($('<input/>', {type: 'hidden', name: 'leagueId', value: leagueId }));
            newForm.append($('<input/>', {type: 'hidden', name: 'ageGroup', value: uage }));
            newForm.append($('<input/>', {type: 'hidden', name: 'months', value: Number(matchDate.substring(5,7)) }));
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
                    <h2><span></span>1:1문의</h2>
                </div>

            </div>
            <div class="round body">

                <div class="body-head">

                    <h4 class="view-title">정보수정요청 > 상세페이지</h4>
                </div>
                <hr class="mb_10">
                <div class="scroll">
                    <div class="title">
                        <h3>
                            내용
                        </h3>
                    </div>
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="180px">
                            <col width="*">

                        </colgroup>
                        <tbody>
                        <tr>
                            <th class="tl">
                                수정 상태
                            </th>
                            <c:choose>
                                <c:when test="${reqDetail.answer_flag eq '0'}">
                                    <td class="tl">
                                        미답변
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="tl">
                                        답변완료
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <th class="tl">
                                작성일
                            </th>
                            <td class="tl">
                                ${fn:substring(reqDetail.reg_date, 0, 10)}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">
                                작성자
                            </th>
                            <td class="tl">
                                ${reqDetail.member_nickname}
                                <c:if test="${!empty reqDetail.member_name}">
                                    /${reqDetail.member_name}
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">
                                회원종류
                            </th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${reqDetail.member_type eq '1'}">학부모</c:when>
                                    <c:when test="${reqDetail.member_type eq '2'}">감독/코치</c:when>
                                    <c:when test="${reqDetail.member_type eq '3'}">레슨 선생님</c:when>
                                    <c:when test="${reqDetail.member_type eq '4'}">학원/클럽 관계자</c:when>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table><br>
                    <div class="title">
                        <h3>

                        </h3>
                    </div>

                    <div class="title">
                        <h3>
                            문의 내용
                        </h3>
                    </div>

                    <c:choose>
                        <c:when test="${reqType eq 'match'}">

                            <table cellspacing="0" class="update over mt_10">
                                <colgroup>
                                    <col width="180px">
                                    <col width="*">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th class="tl">
                                            답변 상태
                                        </th>
                                        <td class="tl">
                                            <c:choose>
                                                <c:when test="${reqDetail.answer_flag eq '0'}">미답변</c:when>
                                                <c:when test="${reqDetail.answer_flag eq '1'}">답변완료</c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">
                                            구분
                                        </th>
                                        <td class="tl">
                                            <c:choose>
                                                <c:when test="${reqDetail.answer_flag eq '0'}">대회 정보 수정 요청</c:when>
                                                <c:when test="${reqDetail.answer_flag eq '1'}">리그 정보 수정 요청</c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <th class="tl">
                                        대회
                                    </th>
                                    <td class="tl">
                                        ${parent.match_name}
                                    </td>
                                    <tr>
                                        <th class="tl">
                                            경기
                                        </th>
                                        <td class="tl">
                                            <c:if test="${!empty child and !empty parent}">
                                                <c:choose>
                                                    <c:when test="${child.cup_league_category eq 'Cup'}">
                                                        <a class="title" onclick="fnMoveDetailMgr('${parent.cup_id}', '${child.uage}', '${child.groups}', '${child.match_category}')">${child.match_date} ${child.home} VS ${child.away}</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="title" onclick="fnMoveDetailLeagueMgr('${child.league_id}', '${child.uage}', '${child.match_date}')">${child.match_date} ${child.home} VS ${child.away}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">
                                            내용
                                        </th>
                                        <td class="tl">
                                            <c:out value="${fn:replace(reqDetail.content, enter, '<br/>')}" escapeXml="false" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">
                                            사진
                                        </th>
                                        <td class="tl">
                                            <c:choose>
                                                <c:when test="${!empty reqDetail.files}">
                                                    <c:forEach var="items" items="${reqDetail.files}" varStatus="status">
                                                        <img src="/NP${items.path}/${items.name}" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                                    </c:forEach>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <br>
                            <div class="title">
                                <h3>
                                    답변하기
                                </h3>
                            </div>
                            <form id="frm" method="post" action="/modify_request"  enctype="multipart/form-data">
                                <input type="hidden" name="requestId" value="${reqDetail.request_id}">
                                <input type="hidden" name="method" value="${reqDetail.answer_flag eq '0' ? 'save' : 'modify'}">
                                <input type="hidden" name="foreignType" value="Request">
                                <table cellspacing="0" class="update over mt_10">
                                    <colgroup>
                                        <col width="100px">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th>
                                                내용
                                            </th>
                                            <td>
                                                <textarea id="answer" name="answer">${reqDetail.answer}</textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>이미지</th>
                                            <td class="tl">
                                                <div id="fileLayer"></div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </c:when>
                        <c:when test="${reqType eq 'team'}">
                            <%--<table cellspacing="0" class="update over mt_10">
                                <colgroup>
                                    <col width="*">
                                    <col width="*">
                                    <col width="*">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연도</th>
                                        <th>팀명</th>
                                        <th>바로가기</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty child}">
                                            <tr>
                                                <td colspan="11">
                                                    등록된 팀이 없습니다.
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>
                                                    ${child.year}
                                                </td>
                                                <td>
                                                    ${child.team_name}
                                                </td>
                                                <td>
                                                    <button onclick="goModifyTeam('${child.team_id}', '${child.year}')">수정하기</button>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>--%>

                            <table cellspacing="0" class="update over mt_10">
                                <colgroup>
                                    <col width="180px">
                                    <col width="*">
                                </colgroup>
                                <tr>
                                    <th class="tl">
                                        답변 상태
                                    </th>
                                    <td class="tl">
                                        <c:choose>
                                            <c:when test="${reqDetail.answer_flag eq '0'}">미답변</c:when>
                                            <c:when test="${reqDetail.answer_flag eq '1'}">답변완료</c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        구분
                                    </th>
                                    <td class="tl">
                                        팀 정보 수정 요청
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        팀명
                                    </th>
                                    <td class="tl">
                                        ${child.team_name}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        연령
                                    </th>
                                    <td class="tl">
                                        ${child.uage}
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        내용
                                    </th>
                                    <td class="tl">
                                        <c:out value="${fn:replace(reqDetail.content, enter, '<br/>')}" escapeXml="false" />
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        사진
                                    </th>
                                    <td class="tl">
                                        <c:choose>
                                            <c:when test="${!empty reqDetail.files}">
                                                <c:forEach var="items" items="${reqDetail.files}" varStatus="status">
                                                    <img src="/NP${items.path}/${items.name}" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>

                            <br>
                            <div class="title">
                                <h3>
                                    답변하기
                                </h3>
                            </div>
                            <form id="frm" method="post" action="/modify_request"  enctype="multipart/form-data">
                                <input type="hidden" name="requestId" value="${reqDetail.request_id}">
                                <input type="hidden" name="method" value="${reqDetail.answer_flag eq '0' ? 'save' : 'modify'}">
                                <input type="hidden" name="foreignType" value="Request">
                                <table cellspacing="0" class="update over mt_10">
                                    <colgroup>
                                        <col width="100px">
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>
                                            내용
                                        </th>
                                        <td>
                                            <textarea id="answer" name="answer">${reqDetail.answer}</textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>이미지</th>
                                        <td class="tl">
                                            <div id="fileLayer2"></div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </c:when>
                        <c:when test="${reqType eq 'etc'}">
                            <table cellspacing="0" class="update over mt_10">
                                <colgroup>
                                    <col width="180px">
                                    <col width="*">
                                </colgroup>
                                <tr>
                                    <th class="tl">
                                        답변 상태
                                    </th>
                                    <td class="tl">
                                        <c:choose>
                                            <c:when test="${reqDetail.answer_flag eq '0'}">미답변</c:when>
                                            <c:when test="${reqDetail.answer_flag eq '1'}">답변완료</c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        구분
                                    </th>
                                    <td class="tl">
                                        기타
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        내용
                                    </th>
                                    <td class="tl">
                                        <c:out value="${fn:replace(reqDetail.content, enter, '<br/>')}" escapeXml="false" />
                                    </td>
                                </tr>
                                <tr>
                                    <th class="tl">
                                        사진
                                    </th>
                                    <td class="tl">
                                        <c:choose>
                                            <c:when test="${!empty reqDetail.files}">
                                                <c:forEach var="items" items="${reqDetail.files}" varStatus="status">
                                                    <img src="/NP${items.path}/${items.name}" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>

                            <br>
                            <div class="title">
                                <h3>
                                    답변하기
                                </h3>
                            </div>
                            <form id="frm" method="post" action="/modify_request"  enctype="multipart/form-data">
                                <input type="hidden" name="requestId" value="${reqDetail.request_id}">
                                <input type="hidden" name="method" value="${reqDetail.answer_flag eq '0' ? 'save' : 'modify'}">
                                <input type="hidden" name="foreignType" value="Request">
                                <table cellspacing="0" class="update over mt_10">
                                    <colgroup>
                                        <col width="100px">
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>
                                            내용
                                        </th>
                                        <td>
                                            <textarea id="answer" name="answer">${reqDetail.answer}</textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>이미지</th>
                                        <td class="tl">
                                            <div id="fileLayer3"></div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </c:when>
                    </c:choose>

                </div><br>
                <div class="tr w100">
                    <a class="btn-large red" onclick="fnRemove('${reqDetail.request_id}')">삭제하기</a>
                    <c:choose>
                        <c:when test="${reqDetail.answer_flag eq '0'}">
                            <%--<a class="btn-large default" onclick="fnModifyRequest('1')">답변 저장</a>--%>
                            <a class="btn-large default" id="eBtnSubmit">답변 저장</a>
                        </c:when>
                        <c:otherwise>
                            <%--<a class="btn-large red default" onclick="fnCancelAnswer()">답변 취소</a>--%>
                            <%--<a class="btn-large default" onclick="fnModifyRequest('0')">답변 수정</a>--%>
                            <a class="btn-large default" id="eBtnSubmit">답변 수정</a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </div>

    </div>

</body>


</html>