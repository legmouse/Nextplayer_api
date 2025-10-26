<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">--%>
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

    $(document).ready(function() {


        if ('${method}' == 'modify') {
            let str = "";
            <c:forEach var="i" items="${rosterPlayer}" varStatus="status">
            str += "<tr id='appendTr" + ${status.index} + "'>" +
                "<td>" + '${i.name}' + "</td>" +
                "<td>" + '${i.position}' + "</td>" +
                "<td>" + '${i.birthday}' + "</td>" +
                "<td>" + '${i.team_name}' + "</td>" +
                "<td>" +
                "<button class='btn-large blue-o' data-index='" + ${status.index} + "' onclick='removePlayer(this)'>삭제</button>" +
                "<input name='playerId" + ${status.index} + "' class='playerId' type='hidden' value='" + '${i.player_id}' + "' />" +
                "<input name='teamId" + ${status.index} + "' class='teamId' type='hidden' value='" + '${i.team_id}' + "' />" +
                "</td>"+
                "</tr>";
            </c:forEach>

            $("#appendTbody").append(str);
        }

        igpFile = new igp.file({
            fileLayer:'#fileLayer',
            boardSeq: '${method eq 'save' ? '' : rosterInfo.roster_id}',
            allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'],
            maxSize:5,
            foreignType: 'Roster'
        });
        igpFile.setFileList(true);

        $('#eBtnSubmit').click(function(e) {

            const trCnt = $("#appendTbody > tr").length;

            if (trCnt > 0) {

                $("input[name='trCnt']").val(trCnt);

                let playerInput = $(".playerId");
                let teamInput = $(".teamId");
                for (let i = 0; i < playerInput.length; i++) {
                    let playerId = $("input[name='playerId" + i +"']").val();
                    let teamId = $("input[name='teamId" + i +"']").val();

                    let playerStr = "<input type='hidden' class='playerId' name='playerId" + i + "' value='" + playerInput[i].value + "'>"
                    let teamStr = "<input type='hidden' class='teamId' name='teamId" + i + "' value='" + teamInput[i].value + "'>"
                    $("#frm").append(playerStr);
                    $("#frm").append(teamStr);

                    /*newForm.append($('<input/>', {type: 'hidden', name: 'playerId' + i , value: playerId }));
                    newForm.append($('<input/>', {type: 'hidden', name: 'teamId' +  i , value: teamId }));*/
                }


                /*for (let i = 0; i < trCnt; i++) {
                    let playerId = $("input[name='playerId" + i +"']").val();
                    let teamId = $("input[name='teamId" + i +"']").val();

                    let playerStr = "<input type='hidden' class='playerId' name='playerId" + i + "' value='" + playerId + "'>"
                    let teamStr = "<input type='hidden' class='teamId' name='teamId" + i + "' value='" + teamId + "'>"
                    console.log(playerStr);
                    $("#frm").append(playerStr);
                    $("#frm").append(teamStr);

                    /!*newForm.append($('<input/>', {type: 'hidden', name: 'playerId' + i , value: playerId }));
                    newForm.append($('<input/>', {type: 'hidden', name: 'teamId' +  i , value: teamId }));*!/
                }*/
            }

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
                    //location.href = '/${roster}PlayerList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
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

    const fnSearchPlayer = () => {
        const sNickName = $("input[name='sNickName']").val();
        const sYear = $("select[name='sYear'] option:selected").val();

        const params = {
            sKeyword: sNickName,
            sYear: sYear
        }

        $.ajax({
            type: 'POST',
            url: '/search_player',
            data: params,
            success: function(res) {
                let str = "";
                if (res.state == 'SUCCESS') {
                    $("#searchTbody").empty();
                    if (res.data.playerList.length > 0) {
                        for (let i = 0; i < res.data.playerList.length; i++) {
                            str += "<tr>" +
                                    "<td>" + res.data.playerList[i].name + "</td>" +
                                    "<td>" + res.data.playerList[i].position + "</td>" +
                                    "<td>" + res.data.playerList[i].birthday + "</td>" +
                                    "<td>" + res.data.playerList[i].team_name + "</td>" +
                                    "<td>" +
                                        "<button data-id='" + res.data.playerList[i].player_id +
                                            "' data-teamId='" + res.data.playerList[i].team_id +
                                            "' data-name='" + res.data.playerList[i].name +
                                            "' data-position='" + res.data.playerList[i].position +
                                            "' data-teamName='" + res.data.playerList[i].team_name +
                                            "' data-birthday='" + res.data.playerList[i].birthday + "' class='btn-large default' onclick='fnAppendPlayer(this)'>추가</button>" +
                                    "</td>" +
                                   "</tr>";
                        }
                    } else {
                        str += "<tr><td colspan='5'>조회된 선수가 없습니다.</td></tr>";
                    }
                }

                $("#searchTbody").append(str);
            }
        });
    }

    const fnAppendPlayer = (el) => {

        const playerId = $(el).attr("data-id");
        const teamId = $(el).attr("data-teamId");
        const name = $(el).attr("data-name");
        const position = $(el).attr("data-position");
        const birthday = $(el).attr("data-birthday");
        const teamName = $(el).attr("data-teamName");

        const trCnt = $("#appendTbody > tr").length;
        console.log(trCnt)
        let str = "";

        str += "<tr id='appendTr" + trCnt + "'>" +
                    "<td>" + name + "</td>" +
                    "<td>" + position + "</td>" +
                    "<td>" + birthday + "</td>" +
                    "<td>" + teamName + "</td>" +
                    "<td>" +
                        "<button class='btn-large blue-o' data-index='" + trCnt + "' onclick='removePlayer(this)'>삭제</button>" +
                        "<input name='playerId" + trCnt + "' class='playerId' type='hidden' value='" + playerId + "' />" +
                        "<input name='teamId" + trCnt + "' class='teamId' type='hidden' value='" + teamId + "' />" +
                    "</td>"+
                "</tr>";

        $("#appendTbody").append(str);


    }

    const removePlayer = (el) => {
        const index = $(el).attr("data-index");
        $("#appendTr" + index).remove();
    }

    const saveRoster = () => {

        const useFlag = $("input[name='useFlag']:checked").val();
        const title = $("input[name='title']").val();
        const year = $("input[name='year']").val();
        const ageGroup = $("select[name='ageGroup'] option:selected").val();
        const comment = $("textarea[name='comment']").val();
        const type = $("select[name='type'] option:selected").val();

        const trCnt = $("#appendTbody > tr").length;

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/save_roster');
        newForm.append($('<input/>', {type: 'hidden', name: 'roster', value: '${type}' }));

        if ('${method}' == 'save') {
            newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'save' }));
        } else {
            newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'modify' }));
            newForm.append($('<input/>', {type: 'hidden', name: 'rosterId', value: '${rosterInfo.roster_id}' }));
        }

        /*newForm.append($('<input/>', {type: 'hidden', name: 'useFlag', value: useFlag }));
        newForm.append($('<input/>', {type: 'hidden', name: 'title', value: title }));
        newForm.append($('<input/>', {type: 'hidden', name: 'year', value: year }));
        newForm.append($('<input/>', {type: 'hidden', name: 'ageGroup', value: ageGroup }));
        newForm.append($('<input/>', {type: 'hidden', name: 'comment', value: comment }));
        newForm.append($('<input/>', {type: 'hidden', name: 'type', value: type }));
        newForm.append($('<input/>', {type: 'hidden', name: 'trCnt', value: trCnt }));

        if (trCnt > 0) {
            for (let i = 0; i < trCnt; i++) {
                let playerId = $("input[name='playerId" + i +"']").val();
                let teamId = $("input[name='teamId" + i +"']").val();
                newForm.append($('<input/>', {type: 'hidden', name: 'playerId' + i , value: playerId }));
                newForm.append($('<input/>', {type: 'hidden', name: 'teamId' +  i , value: teamId }));
                console.log('playerId : ' + playerId);
                console.log('teamId : ' + teamId);
            }
        }

        $(newForm).appendTo('body').submit();*/

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
                    <h2>
                        <span></span>
                        대표팀 관리
                    </h2>
                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <c:choose>
                        <c:when test="${type eq 'national'}">
                            <h4 class="view-title">대표팀 관리 > 국가대표 >
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        등록하기
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        수정하기
                                    </c:when>
                                </c:choose>
                            </h4>
                        </c:when>
                        <c:otherwise>
                            <h4 class="view-title">대표팀 관리 > 골든에이지 >
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        등록하기
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        수정하기
                                    </c:when>
                                </c:choose>
                            </h4>
                        </c:otherwise>
                    </c:choose>

                </div>
                <div class="body-head">
                </div>
                <div class="scroll">
                    <form id="frm" method="post" action="${method eq 'save' ? '/save_roster' : '/save_roster'}"  enctype="multipart/form-data">
                        <input type="hidden" name="foreignType" value="Roster">
                        <input type="hidden" name="method" value="${method}">
                        <input type="hidden" name="roster" value="${type}">
                        <input type="hidden" name="trCnt" value="">
                        <input type="hidden" name="playerId" value="">
                        <input type="hidden" name="teamId" value="">
                        <input type="hidden" name="rosterId" value="${rosterInfo.roster_id}">
                        <table cellspacing="0" class="update view">
                            <colgroup>
                                <col width="20%">
                                <col width="*">

                            </colgroup>
                            <tbody>
                            <tr>
                                <th class="tl">활성/비활성</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${method eq 'save'}">
                                            <input type="radio" name="useFlag" id="ra1-1" value="0" checked><label for="ra1-1">활성</label>
                                            <input type="radio" name="useFlag" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                        </c:when>
                                        <c:when test="${method eq 'modify'}">
                                            <input type="radio" name="useFlag" id="ra1-1" value="0" <c:if test="${rosterInfo.use_flag eq '0'}">checked</c:if> /><label for="ra1-1">활성</label>
                                            <input type="radio" name="useFlag" id="ra1-2" value="1" <c:if test="${rosterInfo.use_flag eq '1'}">checked</c:if> /><label for="ra1-2">비활성</label>
                                        </c:when>
                                    </c:choose>

                                </td>
                            </tr>
                            <tr>
                                <th class="tl">제목</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${method eq 'save'}">
                                            <textarea id="title" name="title" placeholder="제목을 입력해주세요."></textarea>
                                        </c:when>
                                        <c:when test="${method eq 'modify'}">
                                            <textarea id="title" name="title" placeholder="제목을 입력해주세요.">${rosterInfo.title}</textarea>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">연도</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${method eq 'save'}">
                                            <input type="text" name="year" placeholder="연도를 입력해주세요" value="">
                                        </c:when>
                                        <c:when test="${method eq 'modify'}">
                                            <input type="text" name="year" placeholder="" value="${rosterInfo.year}" />
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>

                            <c:if test="${type eq 'national'}">
                                <tr>
                                    <th class="tl">나이</th>
                                    <td class="tl">
                                        <select name="ageGroup">
                                            <c:choose>
                                                <c:when test="${method eq 'save'}">
                                                    <c:forEach var="result" items="${uageList}" varStatus="status">
                                                        <option value="${result.uage }">${result.uage}</option>
                                                    </c:forEach>
                                                </c:when>
                                                <c:when test="${method eq 'modify'}">
                                                    <c:forEach var="result" items="${uageList}" varStatus="status">
                                                        <option value="${result.uage }" <c:if test="${rosterInfo.uage eq result.uage}">selected</c:if> >${result.uage}</option>
                                                    </c:forEach>
                                                </c:when>
                                            </c:choose>

                                        </select>
                                    </td>
                                </tr>
                            </c:if>

                            <c:if test="${type eq 'golden'}">
                                <tr>
                                    <th class="tl">구분</th>
                                    <td class="tl">
                                        <c:choose>
                                            <c:when test="${method eq 'save'}">
                                                <select name="type">
                                                    <option value="0">지역센터</option>
                                                    <option value="1">5대광역</option>
                                                    <option value="2">합동광역</option>
                                                    <option value="3">퓨처팀</option>
                                                    <option value="4">영재센터</option>
                                                </select>
                                            </c:when>
                                            <c:when test="${method eq 'modify'}">
                                                <select name="type">
                                                    <option value="0" <c:if test="${rosterInfo.type eq '0'}">selected</c:if> >지역센터</option>
                                                    <option value="1" <c:if test="${rosterInfo.type eq '1'}">selected</c:if> >5대광역</option>
                                                    <option value="2" <c:if test="${rosterInfo.type eq '2'}">selected</c:if> >합동광역</option>
                                                    <option value="3" <c:if test="${rosterInfo.type eq '3'}">selected</c:if> >퓨처팀</option>
                                                    <option value="4" <c:if test="${rosterInfo.type eq '4'}">selected</c:if> >영재센터</option>
                                                </select>
                                            </c:when>
                                        </c:choose>

                                    </td>
                                </tr>
                            </c:if>

                            <%--<tr>
                                <th class="tl">첨부파일</th>
                                <td class="tl"><input type="file" id="file1"></td>
                            </tr>--%>
                            <tr>
                                <th class="tl">
                                    설명
                                </th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${method eq 'save'}">
                                            <textarea name="comment" placeholder="최대 1,000자까지 입력 가능합니다. "></textarea>
                                        </c:when>
                                        <c:when test="${method eq 'modify'}">
                                            <textarea name="comment" placeholder="최대 1,000자까지 입력 가능합니다. ">${rosterInfo.comment}</textarea>
                                        </c:when>
                                    </c:choose>

                                </td>
                            </tr>
                            <tr>
                                <th class="tl">
                                    첨부파일
                                </th>
                                <td class="tl">
                                    <div id="fileLayer"></div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                    <br>
                    <div>
                        <div class="title">
                            <h3>
                                선수 검색
                            </h3>
                        </div>
                        <div class="search">
                            <span class="title">연도</span>
                            <c:set var="now" value="<%=new java.util.Date()%>" />
                            <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
                            <select name="sYear" id="sYear" class="">
                                <option value="">선택</option>
                                <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                                    <option value="${i}" <c:if test="${sYear eq i}">selected</c:if>>${i}년</option>
                                </c:forEach>
                            </select>
                            <input type="text" name="sNickName" placeholder="이름을 입력해주세요" onkeydown="javascript:if(event.keyCode==13){fnSearchPlayer();}">
                            <a class="btn-large default" onclick="fnSearchPlayer()">검색하기</a>
                        </div>
                    </div>
                    <div class="scroll mt_10">

                        <table cellspacing="0" class="update over">
                            <colgroup>
                                <col width="*">
                                <col width="*">
                                <col width="*">
                                <col width="*">
                                <col width="55px">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>이름</th>
                                <th>포지션</th>
                                <th>생년월일</th>
                                <th>소속팀</th>
                                <th>추가</th>
                            </tr>
                            </thead>
                            <tbody id="searchTbody">
                                <td colspan="5">
                                    선수 검색을 해주세요.
                                </td>
                            </tbody>
                        </table>
                    </div>
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

                                <col width="55px">

                            </colgroup>
                            <thead>
                            <tr>
                                <th>이름</th>
                                <th>포지션</th>
                                <th>생년월일</th>
                                <th>소속팀</th>
                                <th>삭제</th>
                            </tr>
                            </thead>
                            <tbody id="appendTbody">
                                <%--<td colspan="5">
                                    선수 검색 후 추가 해주세요.
                                </td>--%>
                            </tbody>
                        </table>
                    </div>


                </div>

                <br>
                <div class="w100 tr mt_10">
                    <a class="btn-large gray-o" onclick="location.href='/${type}PlayerList'">취소 하기</a>
                    <a class="btn-large default" id="eBtnSubmit" >
                        <c:choose>
                            <c:when test="${method eq 'save'}">
                                등록 하기
                            </c:when>
                            <c:when test="${method eq 'modify'}">
                                수정 하기
                            </c:when>
                        </c:choose>
                    </a>
                    <%--<c:choose>
                        <c:when test="${method eq 'save'}">
                            <a class="btn-large default" onclick="saveRoster()">등록 하기</a>
                        </c:when>
                        <c:when test="${method eq 'modify'}">
                            <a class="btn-large default" onclick="saveRoster()">수정 하기</a>
                        </c:when>
                    </c:choose>--%>
                </div>

            </div>
        </div>
    </div>


</body>


</html>