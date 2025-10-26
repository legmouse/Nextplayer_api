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
        newForm.attr('action', '/detailPlayer');
        newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));
        $(newForm).appendTo('body').submit();
    }

    /*function gotoAddExcel(){
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

    function gotoPaging(cp) {
        $('input[name=cp]').val(cp);
        document.frm.submit();
    }

    const fnSearch = () => {
        document.frm.submit();
    }

    const gotoAddExcel = () => {
        var formData = new FormData();

        let excelFile = $("#excelFile")[0];

        formData.append("excelFile", excelFile.files[0]);

        $.ajax({
            type: 'POST',
            url : '/playerExcelUpload',
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
                    <h2><span></span>선수 관리

                    </h2>

                </div>

            </div>
            <div class="round body">

                <div class="body-head">
                    <div class="search">
                        <form name="frm" id="frm" method="post" action="/playerList">
                            <input name="cp" type="hidden" value="1">
                            <span class="title">검색</span><input type="text" name="sKeyword" value="${sKeyword}" placeholder="선수 이름 또는 소속팀을 입력해주세요">
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
                            <span class="title">포지션</span>
                            <select id="sPosition" name="sPosition">
                                <option value="">선택하기</option>
                                <option value="FW" <c:if test="${sPosition eq 'FW'}">selected</c:if>>FW</option>
                                <option value="MF" <c:if test="${sPosition eq 'MF'}">selected</c:if>>MF</option>
                                <option value="DF" <c:if test="${sPosition eq 'DF'}">selected</c:if>>DF</option>
                                <option value="GK" <c:if test="${sPosition eq 'GK'}">selected</c:if>>GK</option>
                            </select>
                            <button class="btn-large default" onclick="fnSearch()">검색하기</button>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large default" onclick="location.href='/savePlayer'">등록 하기</a>
                        <a class="btn-large gray-o btn-pop" data-id="pop-excel-upload" >엑셀 업로드</a>
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
                            <th>이름</th>
                            <th>포지션</th>
                            <th>생년월일</th>
                            <th>소속팀</th>
                            <th>등록일</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty playerList}">
                                    <tr>
                                        <td colspan="6">
                                            등록된 선수가 없습니다
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${playerList}" varStatus="status">
                                        <tr onclick="goDetail('${result.player_id}')">
                                            <td>
                                                <c:choose>
                                                    <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                                    <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${result.name}
                                            </td>
                                            <td>
                                                ${result.position}
                                            </td>
                                            <td>
                                                ${result.birthday}
                                            </td>
                                            <td>
                                                ${result.team_name}
                                            </td>
                                            <td>
                                                ${fn:substring(result.reg_date,0 ,10)}
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

    <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="playerExcelUpload" onsubmit="return false;">
        <input name="excelFlag" type="hidden" value="player">
        <div class="pop" id="pop-excel-upload">
            <div style="height:auto;">
                <div style="height:auto;">
                    <div class="head">
                        선수 일괄 등록
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