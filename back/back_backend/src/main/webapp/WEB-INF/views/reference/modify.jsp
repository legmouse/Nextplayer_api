<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script src="/resources/jquery/jquery.form.js"></script>
<script src="resources/js/layout.js"></script>

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

        igpFile = new igp.file({
                                fileLayer:'#fileLayer',
                                boardSeq: '${method eq 'save' ? '' : referenceInfo.reference_id}',
                                allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'],
                                maxSize:5,
                                foreignType: 'Reference'
                                });
        igpFile.setFileList(true);

        $('#eBtnSubmit').click(function(e) {
            const cupCnt = $("#selectedCupMatch").children().length;
            const leagueCnt = $("#selectedLeagueMatch").children().length;

            $("input[name='cupCnt']").val(cupCnt);
            $("input[name='leagueCnt']").val(leagueCnt);

            var txt = $(this).text().trim();
            $('#frm').ajaxIgp({
                beforeSubmit:function(){
                    if($("#title").val() == null || $("#title").val() == ""){
                        alert('제목 입력해주세요');
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
                    location.href = '/referenceList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

        if ('${method}' == 'modify') {
            let str = "";
            let addStr = "";
            <c:if test="${!empty cupList}">
                <c:forEach var="i" items="${cupList}" varStatus="status">
                    str += "<div id=selectedDiv" + '${status.index}' + ">" +
                                "<input type='hidden'" +
                                    " name='cupId" + '${status.index}' + "'" +
                                    " data-id='"+ '${i.ageGroup}' + '${i.cup_id}' +
                                    "' data-ageGroup='" + '${i.ageGroup}' +
                                    "' data-cupId='" + '${i.cup_id}' +
                                    "' value='" + '${i.cup_id}' + "' >" +
                                "<input type='hidden'" +
                                    " name='uage" + '${status.index}' + "'" +
                                    " data-id='" + '${i.ageGroup}' + '${i.cup_id}' +
                                    "' value='" + '${i.ageGroup}' + "'>" +
                                "</div>";

                    addStr += "<a class='btn-large gray-o x'" +
                                " data-matchId='" + '${i.cup_id}' +
                                "' data-ageGroup='" + '${i.ageGroup}' +
                                "' onclick='cancelCupMatch($(this))'>" +
                                "<span class='cored'>" +
                                "[" + '${i.ageGroup}' + "]" +
                                "</span>" +
                                    '${i.cup_name}'
                                "</a>";
                </c:forEach>

            $("#selectedCupMatch").append(str);
            $("#addedCupMatch").append(addStr);

            </c:if>

            <c:if test="${!empty leagueList}">
            let leagueStr = "";
            let leagueAddStr = "";
            <c:forEach var="i" items="${leagueList}" varStatus="status">
            leagueStr += "<div id=selectedDiv" + '${status.index}' + ">" +
                "<input type='hidden'" +
                " name='leagueId" + '${status.index}' + "'" +
                " data-id='"+ '${i.ageGroup}' + '${i.league_id}' +
                "' data-ageGroup='" + '${i.ageGroup}' +
                "' data-leagueId='" + '${i.league_id}' +
                "' value='" + '${i.league_id}' + "' >" +
                "<input type='hidden'" +
                " name='uage" + '${status.index}' + "'" +
                " data-id='" + '${i.ageGroup}' + '${i.league_id}' +
                "' value='" + '${i.ageGroup}' + "'>" +
                "</div>";

            leagueAddStr += "<a class='btn-large gray-o x'" +
                " data-matchId='" + '${i.league_id}' +
                "' data-ageGroup='" + '${i.ageGroup}' +
                "' onclick='cancelCupMatch($(this))'>" +
                "<span class='cored'>" +
                "[" + '${i.ageGroup}' + "]" +
                "</span>" +
                '${i.league_name}'
            "</a>";
            </c:forEach>

            $("#selectedLeagueMatch").append(leagueStr);
            $("#addedLeagueMatch").append(leagueAddStr);

            </c:if>

        }

    });

    const closeCupPop = () => {

        $(".pop").fadeOut();

        $("#selectMatchType").empty();
        $("#groupDiv").empty();
        $("#cupDetailList").empty();
        $("#cupInfoList").empty();

        const listStr = '<tr><td colspan="3">대회를 검색 해주세요.</td></tr>';

        $("#cupInfoList").append(listStr);

    }

    const closeLeaguePop = () => {

        $(".pop").fadeOut();

        $("#selectMatchType").empty();
        $("#groupDiv").empty();
        $("#leagueDetailList").empty();
        $("#leagueInfoList").empty();

        const listStr = '<tr><td colspan="3">리그를 검색 해주세요.</td></tr>';

        $("#leagueInfoList").append(listStr);

    }

    const fnSearchCup = () => {
        const sYear = $("#sYear option:selected").val();
        const ageGroup = $("#ageGroup option:selected").val();
        const sCupName = $("#sCupName").val();

        const param = {
            'sYear': sYear,
            'ageGroup': ageGroup,
            'sCupName': sCupName,
        }

        $.ajax({
            type: 'POST',
            url: '/search_cup',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    let str = "";
                    if (res.data.length > 0) {
                        $("#cupInfoList tr").empty();
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>" +
                                        "<td>" +
                                            res.data[i].cup_name +
                                        "</td>" +
                                        "<td>" +
                                            res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                        "</td>" +
                                        "<td>" +
                                            "<button class='btn-large' data-id='" + res.data[i].cup_id + "' data-ageGroup='"+ ageGroup +"' data-cupName='" + res.data[i].cup_name + "' onclick='fnSelectCup(this)'>선택하기</button>" +
                                        "</td>" +
                                   "</tr>";
                        }
                        $("#cupInfoList").append(str);

                    } else {
                        str = "<tr><td colspan='3'>검색 결과가 없습니다.</td></tr>";
                        $("#cupInfoList tr").empty();
                        $("#cupInfoList").append(str);
                    }
                } else {
                    alert("검색에 실패했습니다.");
                    return false;
                }
            }
        })
    }

    const fnSearchLeague = () => {
        const sYear = $("#sLYear option:selected").val();
        const ageGroup = $("#lAgeGroup option:selected").val();
        const sLeagueName = $("#sLeagueName").val();

        const param = {
            'sYear': sYear,
            'ageGroup': ageGroup,
            'sLeagueName': sLeagueName,
        }

        $.ajax({
            type: 'POST',
            url: '/search_league',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    let str = "";
                    if (res.data.length > 0) {
                        $("#leagueInfoList tr").empty();
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>" +
                                "<td>" +
                                res.data[i].league_name +
                                "</td>" +
                                "<td>" +
                                res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                "</td>" +
                                "<td>" +
                                "<button class='btn-large' data-id='" + res.data[i].league_id + "' data-ageGroup='"+ ageGroup +"' data-leagueName='" + res.data[i].league_name + "' onclick='fnSelectLeague(this)'>선택하기</button>" +
                                "</td>" +
                                "</tr>";
                        }
                        $("#leagueInfoList").append(str);

                    } else {
                        str = "<tr><td colspan='3'>검색 결과가 없습니다.</td></tr>";
                        $("#leagueInfoList tr").empty();
                        $("#leagueInfoList").append(str);
                    }
                } else {
                    alert("검색에 실패했습니다.");
                    return false;
                }
            }
        })
    }

    const fnSelectCup = (el) => {
        closeCupPop();

        const cupId = $(el).attr('data-id');
        const cupName = $(el).attr('data-cupName');
        const ageGroup = $(el).attr('data-ageGroup');

        let addStr = "";
        let str = "";

        const cnt = $("#selectedCupMatch").children().length;

        str += "<div id=selectedDiv" + cnt + ">" +
                "<input type='hidden'" +
                    " name='cupId" + cnt + "'" +
                    " data-id='"+ ageGroup + cupId +
                    "' data-ageGroup='" + ageGroup +
                    "' data-cupId='" + cupId +
                    "' value='" + cupId + "' >" +
                "<input type='hidden'" +
                    " name='uage" + cnt + "'" +
                    " data-id='" + ageGroup + cupId +
                    "' value='" + ageGroup + "'>" +
                "</div>";

        addStr += "<a class='btn-large gray-o x'" +
                        " data-matchId='" + cupId +
                        "' data-ageGroup='" + ageGroup +
                        "' onclick='cancelCupMatch($(this))'>" +
                        "<span class='cored'>" +
                        "[" + ageGroup + "]" +
                        "</span>" +
                        cupName
                    "</a>";

        $("#selectedCupMatch").append(str);
        $("#addedCupMatch").append(addStr);


        console.log(cnt);
    }

    const fnSelectLeague = (el) => {
        closeCupPop();

        const leagueId = $(el).attr('data-id');
        const leagueName = $(el).attr('data-leagueName');
        const ageGroup = $(el).attr('data-ageGroup');

        let addStr = "";
        let str = "";

        const cnt = $("#selectedLeagueMatch").children().length;

        str += "<div id=selectedDiv" + cnt + ">" +
            "<input type='hidden'" +
            " name='leagueId" + cnt + "'" +
            " data-id='"+ ageGroup + leagueId +
            "' data-ageGroup='" + ageGroup +
            "' data-leagueId='" + leagueId +
            "' value='" + leagueId + "' >" +
            "<input type='hidden'" +
            " name='uage" + cnt + "'" +
            " data-id='" + ageGroup + leagueId +
            "' value='" + ageGroup + "'>" +
            "</div>";

        addStr += "<a class='btn-large gray-o x'" +
            " data-matchId='" + leagueId +
            "' data-ageGroup='" + ageGroup +
            "' onclick='cancelCupMatch($(this))'>" +
            "<span class='cored'>" +
            "[" + ageGroup + "]" +
            "</span>" +
            leagueName
        "</a>";

        $("#selectedLeagueMatch").append(str);
        $("#addedLeagueMatch").append(addStr);


        console.log(cnt);
    }

    const cancelCupMatch = (el) => {
        $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-matchId') +"']").remove();
        $(el).remove();
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
                <h2><span></span>자료실</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">

                <h4 class="view-title">자료실 >
                    <c:choose>
                        <c:when test="${method eq 'regist'}">
                            등록하기
                        </c:when>
                        <c:when test="${method eq 'modify'}">
                            수정하기
                        </c:when>
                    </c:choose>

                </h4>
            </div>
            <%--<form id="frm" method="post" action="/save_reference"  enctype="multipart/form-data">--%>
            <form id="frm" method="post" action="${method eq 'save' ? '/save_reference' : '/modify_reference'}"  enctype="multipart/form-data">
                <input type="hidden" name="referenceId" value="${referenceInfo.reference_id}"/>
                <input type="hidden" name="cupCnt"/>
                <input type="hidden" name="leagueCnt"/>
                <div class="scroll">
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">

                    </colgroup>
                    <tbody>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <input type="text" id="title" name="title" placeholder="제목을 입력해주세요" value="">
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        <input type="text" id="title" name="title" placeholder="제목을 입력해주세요" value="${referenceInfo.title}">
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th class="tl">내용</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <textarea id="content" name="content" placeholder="내용을 입력해주세요"></textarea>
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        <textarea id="content" name="content" placeholder="내용을 입력해주세요">${referenceInfo.content}</textarea>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                                <%--<input type="file">--%>
                            </td>
                        </tr>
                        
                        
                        <tr>
                            <th class="tl">대회</th>
                            <td class="tl">
                                <a class="btn-large default btn-pop" data-id="pop-competition">대회 등록하기</a>
                                <div id="addedCupMatch"></div>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">리그</th>
                            <td class="tl">
                                <a class="btn-large default btn-pop" data-id="pop-league">리그 등록하기</a>
                                <div id="addedLeagueMatch"></div>
                            </td>
                        </tr>

                    </tbody>
                </table>
                <div id="selectedCupMatch">
                </div>
                <div id="selectedLeagueMatch">
                </div>
            </div><br>
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" onclick="location.href=''">취소 하기</a>
                <a class="btn-large default" id="eBtnSubmit" >
                <c:choose>
                    <c:when test="${method eq 'save'}">
                        저장
                    </c:when>
                    <c:when test="${method eq 'modify'}">
                        수정
                    </c:when>
                </c:choose>

                </a>
            </div>
        </div>
    </div>
    </div>
    <!--팝업-->
    <c:set var="now" value="<%=new java.util.Date()%>" />
    <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
    <div class="pop" id="pop-competition">
        <div style="height:auto;">
            <div style="height:auto; width: 600px;">
                <div class="head">
                    대회 선택하기
                    <a class="close btn-close-pop" onclick="closeCupPop()"></a>
                </div>
                <div class="head p10 grid4">
                    <select name="sYear" id="sYear" style="width:100px;">
                        <option value="">연도선택</option>
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                    <select id="ageGroup" style="width:100px;">
                        <c:forEach var="result" items="${uageList}" varStatus="status">
                            <option value="${result.uage }">${result.uage}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="sCupName" placeholder="대회명을 입력해주세요" onkeydown="javascript:if(event.keyCode==13){fnSearchCup();}">
                    <a class="btn-large default" onclick="fnSearchCup()">검색</a>
                </div>
                <div class="body" style="padding:15px 10px;">
                    <div>
                        <table cellspacing="0" class="update view">
                            <colgroup>
                                <col width="*">
                                <col width="*">
                                <col width="*">

                            </colgroup>
                            <thead>
                                <tr>
                                    <th>대회명</th>
                                    <th>대회일정</th>
                                    <th>선택하기</th>
                                </tr>
                            </thead>
                            <tbody id="cupInfoList">
                                <tr>
                                    <td colspan="3">대회를 검색 해주세요.</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    
                </div>

            </div>
        </div>
    </div>
    <div class="pop" id="pop-league">
        <div style="height:auto;">
            <div style="height:auto; width: 900px;">
                <div class="head">
                    리그 선택하기
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10 grid4">
                    <select name="sLYear" id="sLYear" style="width:100px;">
                        <option value="">연도선택</option>
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                    <select id="lAgeGroup" style="width:100px;">
                        <c:forEach var="result" items="${uageList}" varStatus="status">
                            <option value="${result.uage }">${result.uage}</option>
                        </c:forEach>
                    </select>
                    <input type="text" placeholder="리그명을 입력해주세요" onkeydown="javascript:if(event.keyCode==13){fnSearchLeague();}">
                    
                    <a class="btn-large default" onclick="fnSearchLeague()">검색</a>
                </div>
                <div class="body" style="padding:15px 10px;">
                    <div>
                        <table cellspacing="0" class="update view">
                            <colgroup>
                                <col width="*">
                                <col width="*">
                                <col width="*">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>리그명</th>
                                    <th>리그일정</th>
                                    <th>선택하기</th>
                                </tr>
                            </thead>
                            <tbody id="leagueInfoList">
                            <tr>
                                <td colspan="3">리그를 검색 해주세요.</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    
                </div>

            </div>
        </div>
    </div>
    <div class="pop" id="pop-club">
        <div style="height:auto;">
            <div style="height:auto; width: 900px;">
                <div class="head">
                    학원/클럽 선택하기
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10 grid4">
                    <select>
                        <option>전체</option>
                        <option>학원</option>
                        <option>클럽</option>
                        <option>유스</option>

                    </select>
                    <select>
                        <option>U18</option>
                        <option>U17</option>
                        <option>U15</option>
                        <option>U14</option>
                        <option>U12</option>
                        <option>U11</option>
                        <option>U22</option>
                        <option>U20</option>
                    </select>
                    <input type="text" placeholder="팀명을 입력해주세요">
                    <a class="btn-large default" href="">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">

                    <div class="tl">
                        학원/클럽

                        <table cellspacing="0" class="update mt_10">
                            <colgroup>
                                <col width="55px">
                                <col width="55px">
                                <col width="55px">
                                <col width="55px">
                                <col width="10%">
                                <col width="15%">
                                <col width="*">
                                <col width="45px">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>광역</th>
                                    <th>엠블럼</th>
                                    <th>나이</th>
                                    <th>구분</th>
                                    <th>사용명칭</th>
                                    <th>정식명칭</th>
                                    <th>소재지</th>
                                    <th>탐연동</th>
                                    <th>활성여부</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="checkbox" name="ch2" id="ch2-1"><label for="ch2-1"></label></td>
                                    <td>경기</td>
                                    <td><img src="../resources/img/logo/none.png" class="logo"></td>
                                    <td>U17</td>
                                    <td><span class="label red">유스</span></td>

                                    <td>포항제철고</td>
                                    <td>포항제철고등학교</td>
                                    <td class="tl">경상북도 포항시 남구 효곡동</td>
                                    <td>학원</td>
                                    <td>활성</td>

                                    </td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox" name="ch2" id="ch2-1"><label for="ch2-1"></label></td>
                                    <td>경기</td>
                                    <td><img src="../resources/img/logo/none.png" class="logo"></td>
                                    <td>U17</td>
                                    <td><span class="label blue">유스</span></td>

                                    <td>포항제철고</td>
                                    <td>포항제철고등학교</td>
                                    <td class="tl">경상북도 포항시 남구 효곡동</td>
                                    <td>학원</td>
                                    <td>활성</td>

                                    </td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox" name="ch2" id="ch2-1"><label for="ch2-1"></label></td>
                                    <td>경기</td>
                                    <td><img src="../resources/img/logo/none.png" class="logo"></td>
                                    <td>U17</td>
                                    <td><span class="label green">유스</span></td>

                                    <td>포항제철고</td>
                                    <td>포항제철고등학교</td>
                                    <td class="tl">경상북도 포항시 남구 효곡동</td>
                                    <td>학원</td>
                                    <td>활성</td>

                                    </td>
                                </tr>

                            </tbody>
                        </table>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" href="">추가하기</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

</body>

</html>