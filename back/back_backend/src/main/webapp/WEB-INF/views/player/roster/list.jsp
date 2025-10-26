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

        $("#excelFile").on("click", function () {
            $("#resultDiv").remove();
        });

    });

    const goDetail = (val) => {
        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/detailRoster');
        newForm.append($('<input/>', {type: 'hidden', name: 'rosterId', value: val }));
        newForm.append($('<input/>', {type: 'hidden', name: 'roster', value: '${type}' }));
        $(newForm).appendTo('body').submit();
    }

    const gotoAddExcel = () => {
        var formData = new FormData();

        let excelFile = $("#excelFile")[0];

        formData.append("excelFile", excelFile.files[0]);
        formData.append('roster', '${type}')

        $.ajax({
            type: 'POST',
            url : '/rosterExcelUpload',
            processData: false,
            contentType: false,
            dataType: 'json',
            data: formData,
            success: function(res) {
                console.log(res);
                if (res.state == 'SUCCESS') {
                    let str = "<div id='resultDiv'>" +
                        " 등록된 선수 수 : " + res.data.success + "<br />";
                    if (res.data.teamFailList.length > 0) {
                        str +=  " 팀 등록 실패 이유 : <br/>";
                        for (let i = 0; i < res.data.teamFailList.length; i++) {
                            console.log('i > ', i);
                            console.log('res.data.teamFailList[i] > ', res.data.teamFailList[i])
                            str +=  res.data.teamFailList[i].reason + ", " + res.data.teamFailList[i].data.ageGroup + " / " + res.data.teamFailList[i].data.teamName + "<br />";
                        }
                    }
                    if (res.data.playerFailList.length > 0) {
                        str +=  " 선수 등록 실패 이유 : <br/>";
                        for (let i = 0; i < res.data.playerFailList.length; i++) {
                            str +=  res.data.playerFailList[i].reason + "이름: " + res.data.playerFailList[i].name + " / 생년월일 : " + res.data.playerFailList[i].birthday +"<br/>";
                        }
                    }
                    str += "</div>";
                    console.log(str);
                    $("#popBody").append(str);
                }
            }
        });

    }

    function gotoPaging(cp) {
        $('input[name=cp]').val(cp);
        document.frm.submit();
    }

    const fnSearch = () => {

        <%--let newForm = $('<form></form>');

        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/${rosterType}PlayerList');
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'save' }));
        $(newForm).appendTo('body').submit();--%>
        document.frm.submit();
    }

    /*function gotoAddExcel() {
        var file = $("#excelFile").val();

        if(isEmpty(file)) {
            alert("파일을 선택해주세요.");
            //showAlert("알림!","파일을 선택해주세요.");
            return false;

        }else if (!checkFileType(file)) {
            alert("엑셀 파일만 업로드 가능합니다.");
            return false;
        }

        if (confirm("업로드 하시겠습니까?")) {
            document.excelUploadFrm.submit();
        }
    }*/

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
                    <h2><span></span>대표팀 관리</h2>
                </div>

            </div>
            <div class="round body">
                <div class="body-head">
                    <a class="btn-large default <c:if test="${type eq 'golden'}">gray-o</c:if>" href="/nationalPlayerList">국가대표</a>
                    <a class="btn-large default <c:if test="${type eq 'national'}">gray-o</c:if>" href="/goldenPlayerList">골든에이지</a>
                    <hr class="mt_10">
                </div>
                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/${type}PlayerList" onsubmit="return false;">
                            <input name="cp" type="hidden" value="1">
                            <span class="title">연도</span>
                            <c:set var="now" value="<%=new java.util.Date()%>" />
                            <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
                            <select name="sYear" id="selYears">
                                <option value="">연도선택</option>
                                <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                                    <c:set var="revI" value="${sysYear - (i - 2014)}" />
                                    <c:choose>
                                        <c:when test="${revI eq sYear}">
                                            <option value="${revI}" selected>${revI}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${revI}">${revI}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>

                            <span class="title ml_10">나이</span>
                            <select name="ageGroup">
                                <option value="">선택</option>
                                <c:forEach var="item" items="${uageList}" varStatus="status">
                                    <option value="${item.uage}" <c:if test="${ageGroup eq item.uage}">selected</c:if>>${item.uage}</option>
                                </c:forEach>

                            </select>
                            <span class="title ml_10">검색</span><input type="text" name="sKeyword" value="${sKeyword}" placeholder="제목, 선수명을 입력해주세요" onkeydown="javascript:if(event.keyCode==13){fnSearch();}">

                            <a class="btn-large default" onclick="fnSearch()">검색하기</a>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large gray-o btn-pop" data-id="pop-excel-upload">엑셀 등록 하기</a>
                        <a class="btn-large default" onclick="location.href='/${type}Save'">등록 하기</a>
                    </div>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update over">
                        <colgroup>
                            <col width="55px">

                        </colgroup>
                        <thead>
                        <tr>
                            <th><a>번호 <i class="xi-caret-up-min"></i></a></th>
                            <th>제목</th>
                            <th>연도</th>
                            <c:choose>
                                <c:when test="${type eq 'national'}">
                                    <th>나이</th>
                                </c:when>
                                <c:when test="${type eq 'golden'}">
                                    <th>구분</th>
                                </c:when>
                            </c:choose>

                            <th>등록선수</th>
                            <th>등록일</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty rosterList}">
                                    <td colspan="6">
                                        <c:choose>
                                            <c:when test="${type eq 'national'}">등록된 국가대표팀이 없습니다.</c:when>
                                            <c:when test="${type eq 'golden'}">등록된 골든에이지 대표팀이 없습니다.</c:when>
                                        </c:choose>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${rosterList}" varStatus="status">
                                        <tr onclick="goDetail('${result.roster_id}')">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                    <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${result.title}
                                            </td>
                                            <td>
                                                ${result.year}
                                            </td>
                                            <c:choose>
                                                <c:when test="${type eq 'national'}">
                                                    <td>
                                                            ${result.uage}
                                                    </td>
                                                </c:when>
                                                <c:when test="${type eq 'golden'}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${result.type eq '0'}">지역센터</c:when>
                                                            <c:when test="${result.type eq '1'}">5대광역</c:when>
                                                            <c:when test="${result.type eq '2'}">합동광역</c:when>
                                                            <c:when test="${result.type eq '3'}">퓨처팀</c:when>
                                                            <c:when test="${result.type eq '4'}">영재센터</c:when>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                            </c:choose>
                                            <td>
                                                ${result.cnt}
                                            </td>
                                            <td>
                                                ${fn:substring(result.reg_date, 0, 10)}
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

    <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="rosterExcelUpload" onsubmit="return false;">
        <input name="excelFlag" type="hidden" value="roster">
        <input name="roster" type="hidden" value="${type}">
        <div class="pop" id="pop-excel-upload">
            <div style="height:auto;">
                <div style="height:auto;">
                    <div class="head">
                        <c:choose>
                            <c:when test="${type eq 'national'}">
                                국가대표 일괄 등록
                            </c:when>
                            <c:when test="${type eq 'golden'}">
                                골든에이지 일괄 등록
                            </c:when>
                        </c:choose>

                    </div>
                    <div class="body" id="popBody" style="padding:15px 20px;">
                        <ul class="signup-list">
                            <li>
                                <input type="file" id="excelFile" name="excelFile">
                            </li>
                        </ul>
                    </div>
                    <div class="foot">
                        <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddExcel();"><span>등록하기</span></a>
                        <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                    </div>
                </div>
            </div>
        </div>
    </form>

</body>


</html>