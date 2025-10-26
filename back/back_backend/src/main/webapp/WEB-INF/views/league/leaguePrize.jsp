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
        //연령대 선택
        var ageGroup = "${ageGroup}";
        console.log('--- ageGroup :'+ ageGroup );
        if(isEmpty(ageGroup)){
            $("#U18").addClass("active");
        }else{
            $("#"+ageGroup).addClass("active");
        }

        //검색
        /* var teamSearch = "${teamSearch}";
	if(!isEmpty(teamSearch)){
		var sArea = "${sArea}";
		var sTeamType = "${sTeamType}";
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
		$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
		$("input[name=sNickName]").val("${sNickName}");
	} */
    });

    //리그 연령대 이동
    function gotoAgeGroup(ageGroup){
        $('input[name=ageGroup]').val(ageGroup);
        document.lfrm.submit();
    }

    //리그 리스트 이동
    function gotoLeague() {
        document.lfrm.submit();
    }

    //대회 기본정보 수정
    function gotoFixLeagueInfo(idx) {
        $('input[name=sFlag]').val('1');
        $('input[name=leagueId]').val(idx);
        document.lifrm.submit();
    }

    //대회 수상 등록
    function gotoLeaguePrize(sFlag){
        if(formCheck('frm')){
            $('input[name=sFlag]').val(sFlag);

            let param = fnMakeParamData();

            $("input[name='matchData']").val(JSON.stringify(param));

            document.frm.submit();
        }
    }

    //대회 개요 등록 후 팀정보 이동
    function gotoCupTeam(sFlag){
        if(formCheck('frm')){
            $('input[name=sFlag]').val(sFlag);
            $('input[name=mvFlag]').val('5');

            let param = fnMakeParamData();

            $("input[name='matchData']").val(JSON.stringify(param));

            document.frm.submit();
        }
    }

    function gotoCupTeamFullLeague(sFlag, teamType){
        if(formCheck('frm')){
            $('input[name=sFlag]').val(sFlag);
            $('input[name=mvFlag]').val('5');
            $('input[name=teamType]').val(teamType);

            let param = fnMakeParamData();

            $("input[name='matchData']").val(JSON.stringify(param));

            document.frm.submit();
        }
    }

    const fnMakeParamData = () => {

        let param = {
            leagueData: [],
            deleteData: []
        };

        const leagueInputs = $("input[name='leagueData']");

        leagueInputs.each(function() {

            const ageGroup = $(this).attr('data-ageGroup');
            const year = $(this).attr('data-year');
            const teamId = $(this).attr('data-teamId');
            const played = $(this).attr('data-played');
            const win = $(this).attr('data-win');
            const draw = $(this).attr('data-draw');
            const lose = $(this).attr('data-lose');
            const gf = $(this).attr('data-gf');
            const ga = $(this).attr('data-ga');
            const foreignId = $(this).val();
            const nickName = $(this).attr('data-nickName');


            const data = param.leagueData.find(item => item.ageGroup === ageGroup && item.foreignId === foreignId);
            if (data) {

            } else {
                const newData = {
                    ageGroup: ageGroup,
                    year: year,
                    nickName: nickName,
                    teamId: teamId,
                    played: played,
                    win: win,
                    draw: draw,
                    lose: lose,
                    gf: gf,
                    ga: ga,
                    foreignId: foreignId,
                };
                param.leagueData.push(JSON.stringify(newData));
            }

        });

        const deleteInputs = $("input[name='deleteData']");

        deleteInputs.each(function() {
            const championId = $(this).val();
            const newData = {
                championId: championId
            }
            param.deleteData.push(JSON.stringify(newData));
        });

        return param;
    }

    function formCheck(regxForm) {
        var valid = true;
        var $form = $("#"+regxForm);

        if(valid == false){
            return false;
        }

        $form.find('input:text').each(
            function(key) {
                var $obj = $(this);
                if(isEmpty($obj.val())) {
                    /* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
                    */
                    switch ($obj.attr('name')){
                        case 'cInfo' :
                            alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
                            $obj.focus();
                            valid = false;
                            return false;
                            break;

                        case 'cPrize' :
                            alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
                            $obj.focus();
                            valid = false;
                            return false;
                            break;
                    }

                }else{
                    if(isRegExp($obj.val())){
                        alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
                        $obj.focus();
                        valid = false;
                        return false;
                    }

                    // remove blank
                    var objVal = $obj.val();
                    objVal = objVal.trim(objVal);
                    $obj.val(objVal);

                }
            }
        );

        if(valid == false){
            return false;
        }

        $form.find('select').each(
            function(key) {
                var $obj = $(this);
                if($obj.val() < 0) {
                    console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
                    switch ($obj.attr('name')){
                        case 'selArea' :
                            alert("알림!\n 광역 선택 하세요.");
                            break;

                    }

                    valid = false;
                    return false;
                }
            }
        );

        if(valid == false){
            return false;
        }

        var sdate = $("[name=sdate]").val();
        var edate = $("[name=edate]").val();

        if(!isEmpty(sdate) && !isEmpty(edate)) {
            if(compareDate1(sdate, edate, 'yyyy-MM-dd') > 0){
                $('input[name=edate]').focus();
                alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
                valid = false;
                return false;
            }

        }

        return valid;
    }


    //검색
    function gotoSearch(){
        //console.log('--- gotoSearch');
        if(searchFormCheck("sfrm")){
            $("input[name=cp]").val(1);
            document.sfrm.submit();
        }
    }

    function searchFormCheck(regxForm) {
        var valid = true;

        var area = $("select[name=sArea]").val();
        var leagueName = $("input[name=sLeagueName]").val();

        if(area < 0 && isEmpty(leagueName)){
            alert('알림!\n 검색어를 입력 하세요.');
            valid = false;
            return false;
        }

        if(valid == false){
            return false;
        }

        if(!isEmpty(leagueName) && isRegExp(leagueName) ){
            alert('알림!\n'+ '['+$("input[name=sLeagueName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
            $("input[name=sLeagueName]").focus();
            valid = false;
            return false;
        }


        if(area < 0 && !isEmpty(leagueName)){
            $("select[name=sArea]").val('');
        }

        return valid;
    }

    /* function gotoDel(idx, emblem){
        if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
            $('input[name=teamId]').val(idx);
            $('input[name=emblem]').val(emblem);
            document.delFrm.submit();
        }
    } */

    const fnPopup = (el) => {
        $("#sCupName").val(null);
        $(".pop").attr("data-divId", $(el).attr("data-divId"));
        $(".pop").show();
    }

    const fnSearchLeague =() => {
        const sLeagueYear = $("#sLeagueYear option:selected").val();
        const ageGroup = $("#ageGroup option:selected").val();
        const sLeagueName = $("#sLeagueName").val();

        const index = $(".pop").attr("data-divId");
        console.log('index : ', index);

        const params = {
            sLeagueYear: sLeagueYear,
            ageGroup: ageGroup,
            sLeagueName: sLeagueName
        }

        $.ajax({
            type: 'POST',
            url: '/search_league_champion',
            data: params,
            success: function(res) {
                let str = "";
                if (res.state == "SUCCESS") {
                    console.log(res)
                    if (res.data.length > 0) {
                        $("#leagueList tr").empty();
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>" +
                                        "<td>" +
                                            "<input type='checkbox' " +
                                            "name='ch" + i + "' id='ch" + i + "' " +
                                            "value='" + res.data[i].league_id + "' " +
                                            "data-leagueName='" + res.data[i].league_name + "' " +
                                            "data-ageGroup='" + res.data[i].uage + "' " +
                                            "data-year='" + res.data[i].year + "' " +
                                            "data-teamId='" + res.data[i].team_id + "' " +
                                            "data-nickName='" + res.data[i].nick_name + "' " +
                                            "data-played='" + res.data[i].played + "' " +
                                            "data-win='" + res.data[i].win + "' " +
                                            "data-draw='" + res.data[i].draw + "' " +
                                            "data-lose='" + res.data[i].lose + "' " +
                                            "data-gf='" + res.data[i].gf + "' " +
                                            "data-ga='" + res.data[i].ga + "'>" +
                                        "<label for='ch" + i + "'></label>" +
                                        "</td>" +
                                        "<td>" +
                                        res.data[i].league_name +
                                        "</td>" +
                                        "<td>" +
                                        res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                        "</td>"+
                                    "</tr>";
                        }
                        $("#leagueList").append(str);
                    }
                } else {
                    str = "<tr><td colspan='3'>검색 결과가 없습니다.</td></tr>";
                    $("#leagueList tr").empty();
                    $("#leagueInfoList").append(str);
                }
            }
        });
    }

    const closeLeaguePop = () => {

        $(".pop").fadeOut();

        $("#leagueList").empty();

        const listStr = '<td colspan="3">리그 검색후 선택해 주세요.</td>';
        $("#leagueList").append(listStr);
        $("#sLeagueName").val(null);

    }

    const addLeagueMatch = () => {
        // $("#championTbody").children().remove();
        const chkMatch = $("input[type=checkbox]:checked");

        let str = "";
        let addStr = "";

        $(chkMatch).each(function(i) {
            str += "<tr id='"+ $(this).attr('data-ageGroup') + $(this).val() +"'>" +
                        "<td>" + $(this).attr('data-year') + "</td>" +
                        "<td>" + $(this).attr('data-leagueName') + "</td>" +
                        "<td>" + $(this).attr('data-nickName') + "</td>" +
                        "<td>" + $(this).attr('data-win') + "</td>" +
                        "<td>" + $(this).attr('data-lose') + "</td>" +
                        "<td>" + $(this).attr('data-gf') + "</td>" +
                        "<td>" + $(this).attr('data-ga') + "</td>" +
                        "<td>" +
                            "<a class='btn-large default' " +
                            "data-ageGroup='" + $(this).attr('data-ageGroup') + "' " +
                            "data-foreignId='" + $(this).val() + "' " +
                            "onclick='fnRemoveNewTap($(this))'>삭제하기" +
                            "</a>" +
                        "</td>"
            addStr += "<input type='hidden' " +
                        "name='leagueData' " +
                        "data-id='" + $(this).attr('data-ageGroup') + $(this).val() + "' " +
                        "data-foreignId='" + $(this).val() + "' " +
                        "data-ageGroup='" + $(this).attr('data-ageGroup') + "' " +
                        "data-year='" + $(this).attr('data-year') + "' " +
                        "data-nickName='" + $(this).attr('data-nickName') + "' " +
                        "data-teamId='" + $(this).attr('data-teamId') + "' " +
                        "data-played='" + $(this).attr('data-played') + "' " +
                        "data-win='" + $(this).attr('data-win') + "' " +
                        "data-draw='" + $(this).attr('data-draw') + "' " +
                        "data-lose='" + $(this).attr('data-lose') + "' " +
                        "data-gf='" + $(this).attr('data-gf') + "' " +
                        "data-ga='" + $(this).attr('data-ga') + "' " +
                        "value='" +$(this).val() + "'>";

        });
        $("#emptyTr").remove();
        $("#selectedLeague").append(addStr);
        $("#championTbody").append(str);
        $("#leagueList").empty();
        $("#sLeagueName").val(null);
        closeLeaguePop();
    }

    const fnRemoveNewTap = (el) => {
        $("#" + $(el).attr('data-ageGroup') + $(el).attr('data-foreignId')).remove();
        $("input[data-id='" + $(el).attr('data-ageGroup') + $(el).attr('data-foreignId') + "']").remove();

        const tbLength = $("#championTbody > tr").length;
        if (tbLength == 0) {
            let str = "<tr id='emptyTr'><td colspan='8'>추가하기 버튼을 눌러주세요</td></tr>";
            $("#championTbody").append(str);
        }
    }

    const fnRemoveOrgTap = (el) => {

        $("#org" + $(el).attr('data-ageGroup') + $(el).attr('data-foreignId')).remove();
        const tbLength = $("#championTbody > tr").length;
        if (tbLength == 0) {
            let str = "<tr id='emptyTr'><td colspan='8'>추가하기 버튼을 눌러주세요</td></tr>";
            $("#championTbody").append(str);
        }

        let addStr = "<input type='hidden' name='deleteData' value='" + $(el).attr('data-id') + "'>";


        $("#deleteLeague").append(addStr);
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
                <h2><span></span>대회정보 > 등록/수정</h2>
                <c:forEach var="result" items="${uageList}" varStatus="status">
                    <a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
                </c:forEach>
            </div>

        </div>
        <div class="round body">
            <div class="body-head">
                <div class="search">
                    <form name="sfrm" id="sfrm" method="post"  action="league" onsubmit="return false;">
                        <input name="sFlag" type="hidden" value="${sFlag}">
                        <input name="ageGroup" type="hidden" value="${ageGroup}">

                        <input type="text" name="sLeagueName" placeholder="대회명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
                        <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
                    </form>
                </div>
                <div class="others">
                    <a class="btn-large gray-o" onclick="gotoLeague();"><i class="xi-long-arrow-left"></i> 리그등록 리스트</a>
                </div>
            </div>

            <div class="title">
                <h3>
                    기본정보
                    <a class="btn-open open-1-o ac" data-id="open-1"><i class="xi-caret-down-circle-o"></i></a>
                    <a class="btn-close open-1-c" data-id="open-1"><i class="xi-caret-up-circle-o"></i></a>
                    <a onclick="gotoFixLeagueInfo('${leagueId}');" class="btn-large gray-o">수정</a>
                </h3>
            </div>
            <div class="open-area" id="open-1">
                <div class="scroll">
                    <table cellspacing="0" class="update">
                        <colgroup>
                            <col width="10.3%">
                            <col width="23%">
                            <col width="10.3%">
                        </colgroup>
                        <tbody>
                        <tr>
                            <th colspan="2">활성여부</th>
                            <td class="tl" colspan="2">
                                <c:if test="${leagueInfoMap.play_flag eq '0'}">활성</c:if>
                                <c:if test="${leagueInfoMap.play_flag eq '1'}">비활성</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th>리그명</th>
                            <td class="tl">${leagueInfoMap.league_name}</td>
                            <th>리그기간</th>
                            <td class="tl">${leagueInfoMap.play_sdate} ~ ${leagueInfoMap.play_edate}</td>
                        </tr>
                        <tr>
                            <th colspan="2">순위선정방식</th>
                            <td colspan="2" class="tl">
                                <c:if test="${leagueInfoMap.rank_type eq '0'}">골득실</c:if>
                                <c:if test="${leagueInfoMap.rank_type eq '1'}">승자승</c:if>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <br>
            </div>

            <div class="title">
                <h3 class="w50">대회개요</h3>
                <h3 class="w50">대회수상</h3>
            </div>

            <form name="frm" id="frm" action="save_leaguePrize" onsubmit="return false;">
                <input name="sFlag" type="hidden" value="">
                <input name="mvFlag" type="hidden" value="">
                <input name="ageGroup" type="hidden" value="${ageGroup}">
                <input name="leagueId" type="hidden" value="${leagueInfoMap.league_id}">
                <input name="teamType" type="hidden" value="">

                <input name="matchData" type="hidden" value="">

                <div class="scroll">
                    <table cellspacing="0" class="update">
                        <colgroup>
                            <col width="50%">
                            <col width="50%">
                        </colgroup>
                        <tbody>
                        <tr>
                            <td class="tl">
                                <textarea name="lInfo" style="min-height:200px;">${leagueInfoMap.league_info}</textarea>
                            </td>
                            <td class="tl">
                                <textarea name="lPrize" style="min-height:200px;">${leagueInfoMap.league_prize}</textarea>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <br />
                    <div class="tr w100">
                        <a class="btn-large default btn-pop" data-id="pop-openPop">추가하기</a>
                    </div>
                    <br />
                    <table cellspacing="0" class="update">
                        <colgroup>
                            <col width="10%">
                            <col width="20%">
                            <col width="10%">
                            <col width="10%">
                            <col width="10%">
                            <col width="10%">
                            <col width="10%">
                            <col width="10%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>연도</th>
                            <th>리그명</th>
                            <th>우승팀</th>
                            <th>승</th>
                            <th>패</th>
                            <th>득점</th>
                            <th>실점</th>
                            <th>삭제하기</th>
                        </tr>
                        </thead>
                        <tbody id="championTbody">
                        <c:set var="now" value="<%=new java.util.Date()%>" />
                        <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
                        <c:choose>
                            <c:when test="${empty championInfo}">
                                <tr id="emptyTr">
                                    <td colspan="8">추가하기 버튼을 눌러주세요</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="result" items="${championInfo}" varStatus="status">
                                    <tr id="org${result.uage}${result.foreign_id}">
                                        <td>
                                                ${result.year}
                                        </td>
                                        <td>
                                                ${result.league_name}
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                <c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
                                            </c:choose>
                                                ${result.nick_name}
                                        </td>
                                        <td>
                                                ${result.win}
                                        </td>
                                        <td>
                                                ${result.lose}
                                        </td>
                                        <td>
                                                ${result.gf}
                                        </td>
                                        <td>
                                                ${result.ga}
                                        </td>
                                        <td>
                                            <button class="btn-large gray-o" data-id="${result.champion_id}" data-ageGroup="${result.uage}" data-foreignId="${result.foreign_id}" onclick="fnRemoveOrgTap($(this))">삭제하기</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <div id="selectedLeague"></div>
                    <div id="deleteLeague"></div>
                </div>
            </form>

            <div class="body-foot">
                <div class="search">
                </div>
                <div class="others">
                    <c:choose>
                        <c:when test="${sFlag eq '0' }">
                            <a class="btn-large default" onclick="gotoLeaguePrize(0);">개요등록</a>
                            <a class="btn-large gray-o" onclick="gotoCupTeam(0);">개요등록 후 팀 등록</a>
                        </c:when>
                        <c:when test="${sFlag eq '1' }">
                            <a class="btn-large gray-o" onclick="gotoLeaguePrize(1);">개요수정</a>
                            <a class="btn-large gray-o" onclick="gotoCupTeam(1);">개요수정 후 팀 등록</a>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <div class="pop" id="pop-openPop">
                <div style="height:auto;">
                    <div style="height:auto; width: 1100px;">
                        <div class="head">
                            리그 선택하기
                            <a class="close btn-close-pop" onclick="closeLeaguePop()"></a>
                        </div>
                        <div class="head p10 grid4">
                            <select id="sLeagueYear" style="width:100px;">
                                <option value="">전체</option>
                                <c:forEach var="items" begin="2014" end="${sysYear}" step="1" varStatus="status">
                                    <option value="${sysYear - status.count + 1}">${sysYear - status.count + 1 }년</option>
                                </c:forEach>
                            </select>
                            <select id="ageGroup" style="width:100px;">
                                <c:forEach var="result" items="${uageList}" varStatus="status">
                                    <option value="${result.uage }">${result.uage}</option>
                                </c:forEach>
                            </select>
                            <input type="text" id="sLeagueName" placeholder="리그명을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchLeague(); return;}">
                            <a class="btn-large default" onclick="fnSearchLeague()" >검색</a>
                        </div>
                        <div class="body grid1" style="padding:15px 10px;">
                            <div>
                                <div class="mt_10" id="groupDiv">
                                </div>
                                <table cellspacing="0" class="update mt_10">
                                    <colgroup>
                                        <col width="5%">
                                        <col width="10%">
                                        <col width="10%">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th rowspan="2">
                                            선택
                                        </th>
                                        <th rowspan="2">
                                            리그명
                                        </th>
                                        <th rowspan="2">
                                            리그일정
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody id="leagueList">
                                    <tr>
                                        <td colspan="10">
                                            리그 검색후 선택해 주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="mt_10 w100 tr">
                                    <a class="btn-large default" onclick="addLeagueMatch()">추가하기</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
</body>

<form name="lfrm" id="lfrm" method="post"  action="league">
    <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
    <input name="sFlag" type="hidden" value="0">
    <input name="ageGroup" type="hidden" value="${ageGroup}">
    <input name="leagueId" type="hidden" value="">
</form>

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
<input name="sFlag" type="hidden" value="0">
<input name="ageGroup" type="hidden" value="">
</form>  -->

</html>