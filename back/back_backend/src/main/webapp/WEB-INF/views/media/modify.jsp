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
<script>
    let cupMapList = [];
    let leagueMapList = [];

    $(document).ready(function() {
        const mediaType = "${mediaType}";
        const teamList = '<c:out value="${teamMediaList}"/>';
        if (teamList) {
            let str = "";
            let addStr = "";
            <c:forEach var="i" items="${teamMediaList}" varStatus="status">
            str += "<input type='hidden' " +
                "name='teamData'" +
                " data-id='"+ '${i.uage}' + '${i.team_id}' +
                "' data-ageGroup='"+ '${i.uage}' +
                "' data-cupType='" + '${i.uage}' +
                "' value='" + '${i.team_id}' + "' >";

            addStr += "<a class='btn-large gray-o x'" +
                " data-teamId='" + '${i.team_id}' +
                "' data-ageGroup='" + '${i.uage}' +
                "' data-cupType='" + '${i.uage}' +
                "' onclick='cancelTeam($(this))'>" +
                '${i.nick_name}' +
                "<span class='cored'>" + '${i.uage}' + "</span>" +
                "</a>";
            </c:forEach>

            $("#selectedTeam").append(str);
            $("#addedTeam").append(addStr);
        }

        const leagueList = '<c:out value="${leagueMediaList}"/>';
        if (leagueList) {
            let str = "";
            let addStr = "";
            <c:choose>
                <c:when test="${mediaType eq 'Game'}">
                    <c:forEach var="i" items="${leagueMediaList}" varStatus="status">
                    nameStr = "League_Match";

                    str += "<input type='hidden' " +
                        "name='leagueData'" +
                        " data-id='"+ '${i.uage}' + '${i.league_match_id}' +
                        "' data-ageGroup='"+ '${i.uage}' +
                        "' data-leagueId='"+ '${i.league_id}' +
                        "' data-cupType='" + nameStr +
                        "' value='" + '${i.league_match_id}' + "' >";

                    addStr += "<a class='btn-large gray-o x'" +
                        " data-matchId='" + '${i.league_match_id}' +
                        "' data-ageGroup='" + '${i.uage}' +
                        "' data-cupType='" + nameStr +
                        "' data-homeId='" + '${i.home_id}' +
                        "' data-awayId='" + '${i.away_id}' +
                        "' onclick='cancelLeagueMatch($(this))'>" +
                        '${i.home}' + "vs" +
                        '${i.away}' + "," +
                        '${i.playdate}' + " " +
                        '${i.ptime}' + " " +
                        '${i.place}' +
                        "</a>";
                    </c:forEach>
                </c:when>
                <c:when test="${mediaType eq 'News'}">
                    <c:forEach var="i" items="${leagueMediaList}" varStatus="status">

                    str += "<input type='hidden' " +
                        "name='leagueData'" +
                        " data-id='"+ '${i.uage}' + '${i.league_id}' +
                        "' data-ageGroup='"+ '${i.uage}' +
                        "' data-leagueId='"+ '${i.league_id}' +
                        "' value='" + '${i.league_id}' + "' >";

                    addStr += "<a class='btn-large gray-o x'" +
                        " data-matchId='" + '${i.league_id}' +
                        "' data-ageGroup='" + '${i.uage}' +
                        "' onclick='cancelLeagueMatch($(this))'>" +
                        '${i.league_name}' +
                        "</a>";
                    </c:forEach>
                </c:when>
            </c:choose>
            $("#selectedLeagueMatch").append(str);
            $("#addedLeagueMatch").append(addStr);
        }

        const cupList = '<c:out value="${cupMediaList}"/>';
        if (cupList) {
            let str = "";
            let nameStr = "";

            let addStr = "";
            let ageGroup = "";
            <c:forEach var="i" items="${cupMediaList}" varStatus="status">

                //nameStr = "Cup_Sub_Match";
                <c:choose>
                    <c:when test="${mediaType eq 'Game'}">
                        nameStr = "${i.cupType}".replace('${i.uage}' +'_','');
                        str += "<input type='hidden' " +
                            "name='cupData'" +
                            " data-id='"+ '${i.uage}' + '${i.matchId}' +
                            "' data-ageGroup='"+ '${i.uage}' +
                            "' data-cupId='"+ '${i.cup_id}' +
                            "' data-cupType='" + nameStr +
                            "' value='" + '${i.matchId}' + "' >";

                        addStr += "<a class='btn-large gray-o x'" +
                            " data-matchId='" + '${i.matchId}' +
                            "' data-ageGroup='" + '${i.uage}' +
                            "' data-cupType='" + nameStr +
                            "' data-homeId='" + '${i.home_id}' +
                            "' data-awayId='" + '${i.away_id}' +
                            "' onclick='cancelCupMatch($(this))'>" +
                            '${i.home}' + "vs" +
                            '${i.away}' + "," +
                            '${i.playdate}' + " " +
                            '${i.ptime}' + " " +
                            '${i.place}' +
                            "</a>";
                    </c:when>
                    <c:when test="${mediaType eq 'News'}">
                        str += "<input type='hidden' " +
                            "name='cupData'" +
                            " data-id='"+ '${i.uage}' + '${i.cup_id}' +
                            "' data-ageGroup='"+ '${i.uage}' +
                            "' data-cupId='"+ '${i.cup_id}' +
                            "' value='" + '${i.cup_id}' + "' >";

                        addStr += "<a class='btn-large gray-o x'" +
                            " data-matchId='" + '${i.cup_id}' +
                            "' data-ageGroup='" + '${i.uage}' +
                            "' onclick='cancelCupMatch($(this))'>" +
                            '${i.cup_name}' +
                            "</a>";
                    </c:when>
                </c:choose>


            </c:forEach>

            $("#selectedCupMatch").append(str);
            $("#addedCupMatch").append(addStr);
        }

        const isModify = ${method eq 'modify'};

        if(isModify) {
            if (mediaType == 'Game' || mediaType == 'News') {
                $("#uageTr").show();
                $("#typeTr").show();
                $("#cupTr").show();
                $("#leagueTr").show();
                $("#teamTr").show();
            }
        }

        $("#subType").change(function() {
            let val = $(this).val();
            if(mediaType == 'Game') {
                /*if (val != 0) {
                    $("#uageTr").hide();
                    $("#typeTr").hide();
                    $("#cupTr").hide();
                    $("#leagueTr").hide();
                    $("#teamTr").hide();
                } else {
                    $("#uageTr").show();
                    $("#typeTr").show();
                    $("#cupTr").show();
                    $("#leagueTr").show();
                    $("#teamTr").show();
                }*/
                $("#uageTr").show();
                $("#typeTr").show();
                $("#cupTr").show();
                $("#leagueTr").show();
                $("#teamTr").show();
            } else if(mediaType == 'News') {
                /*if (val == 0 || val == 1 || val == 4) {
                    $("#uageTr").show();
                    $("#typeTr").show();
                    $("#cupTr").show();
                    $("#leagueTr").show();
                    $("#teamTr").show();

                    $("#imgTr").hide();
                    $("#contentTr").hide();
                } else {
                    $("#uageTr").hide();
                    $("#typeTr").hide();
                    $("#cupTr").hide();
                    $("#leagueTr").hide();
                    $("#teamTr").hide();

                    $("#imgTr").show();
                    $("#contentTr").show();
                }*/
                $("#uageTr").show();
                $("#typeTr").show();
                $("#cupTr").show();
                $("#leagueTr").show();
                $("#teamTr").show();

                $("#imgTr").hide();
                $("#contentTr").hide();
            }
        });

    });

    const fnSearchCup = () => {

        $("#selectMatchType").empty();
        $("#groupDiv").empty();
        $("#cupInfoList").empty();
        $("#cupDetailList").empty();

        $("#addedCupMatch").empty();
        $("#selectedCupMatch").empty();

        const listStr = '<td colspan="10">대회 검색후 선택해 주세요.</td>';
        $("#cupDetailList").append(listStr);

        const sYear = $("#selYears option:selected").val();
        const ageGroup = $("#ageGroup option:selected").val();
        const sCupName = $("#sCupName").val();

        const param = {
            'sYear': sYear,
            'ageGroup': ageGroup,
            'sCupName': sCupName,
        }

        const mediaType = '${mediaType}';

        $.ajax({
            type: 'POST',
            url: '/search_cup',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    let str = "";
                    if (mediaType === 'Game') {
                        if (res.data.length > 0) {
                            $("#cupInfoList tr").empty();
                            for (let i = 0; i < res.data.length; i++) {
                                //str +=  "<tr onclick='fnSearchCupDetail("+ res.data[i].cup_id + ", " + res.data[i].cup_type + ", " + ageGroup + ")'>" +
                                str += "<tr onclick='fnSearchCupDetail(" + res.data[i].cup_id + ", " + res.data[i].cup_type + ", \"" + ageGroup + "\")'>" +
                                    "<td>" +
                                    res.data[i].cup_name +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                    "</td>" +
                                    "</tr>";
                            }
                            $("#cupInfoList").append(str);

                        } else {
                            str = "<tr><td colspan='2'>검색 결과가 없습니다.</td></tr>";
                            $("#cupInfoList tr").empty();
                            $("#cupInfoList").append(str);
                        }
                    }
                    if (mediaType === 'News') {
                        if (res.data.length > 0) {
                            $("#cupInfoList tr").empty();
                            for (let i = 0; i < res.data.length; i++) {
                                // str += "<tr onclick='fnSearchCupDetail(" + res.data[i].cup_id + ", " + res.data[i].cup_type + ", \"" + ageGroup + "\")'>" +
                                str += "<tr>" +
                                    "<td>" +
                                    "<input type='checkbox' value='" + res.data[i].cup_id + "'" +
                                        " data-cupId='" + res.data[i].cup_id + "'" +
                                        " data-ageGroup='" + res.ageGroup + "'" +
                                        " data-cupName='" + res.data[i].cup_name + "'" +
                                        " name='ch" + i + "' id='ch" + i + "-" + i + "'>" +
                                    "<label for='ch" + i + "-" + i + "'></label>" +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].cup_name +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                    "</td>" +
                                    "</td>";
                            }
                            $("#cupInfoList").append(str);

                        } else {
                            str = "<tr><td colspan='2'>검색 결과가 없습니다.</td></tr>";
                            $("#cupInfoList tr").empty();
                            $("#cupInfoList").append(str);
                        }
                    }
                } else {
                    alert("검색에 실패했습니다.");
                    return false;
                }
            }
        })

    }

    const fnSearchCupDetail = (cupId, cupType, ageGroup, groups, matchType, isSearch) => {

        if (!isSearch) {
            cupMapList = [];
        }

        if(!cupId) {
            alert('잘못된 접근입니다.');
            return;
        }

        const param = {
            cupId : cupId,
            cupType : cupType,
            groups: groups,
            ageGroup: ageGroup,
            matchType: matchType
        }

        $.ajax({
            type: 'POST',
            url: '/search_cup_detail',
            data: param,
            success: function(res) {

                if (res.state == 'SUCCESS') {

                    let matchTypeStr = "";
                    let groupStr = "";
                    let cupDetailStr = "";

                    $("#selectMatchType").empty();
                    $("#groupDiv").empty();
                    $("#cupDetailList").empty();

                    fnGroupsHtml(res);

                    matchTypeStr += "<select style='width: 100%;' id='matchSelect' onchange='fnSelectCupGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\")'>";

                    if (res.data.cupInfoMap.cup_type == 0) {

                        if (res.matchType == '0') {
                            matchTypeStr += "<option value='0' selected>예선경기</option>" +
                                            "<option value='1'>토너먼트</option>";
                        } else {
                            matchTypeStr += "<option value='0'>예선경기</option>" +
                                            "<option value='1' selected>토너먼트</option>";
                        }

                    } else if (res.data.cupInfoMap.cup_type == 1) {

                        if (res.matchType == '0') {
                            matchTypeStr += "<option value='0' selected>예선경기</option>" +
                                            "<option value='1'>본선경기</option>" +
                                            "<option value='2'>토너먼트</option>";
                        } else if (res.matchType == '1') {
                            matchTypeStr += "<option value='0'>예선경기</option>" +
                                            "<option value='1' selected>본선경기</option>" +
                                            "<option value='2'>토너먼트</option>";
                        } else if (res.matchType == '2') {
                            matchTypeStr += "<option value='0'>예선경기</option>" +
                                            "<option value='1'>본선경기</option>" +
                                            "<option value='2' selected>토너먼트</option>";
                        }

                    } else if (res.data.cupInfoMap.cup_type == 2) {

                        matchTypeStr += "<option value='0'>풀리그</option>";

                    } else if (res.data.cupInfoMap.cup_type == 3) {

                        matchTypeStr += "<option value='0'>토너먼트</option>";

                    } else if (res.data.cupInfoMap.cup_type == 4) {

                        if (res.teamType != '2') {
                            matchTypeStr += "<option value='1' selected>1차 풀리그</option>" +
                                            "<option value='2'>2차 풀리그</option>";
                        } else {
                            matchTypeStr += "<option value='1'>1차 풀리그</option>" +
                                            "<option value='2' selected>2차 풀리그</option>";
                        }

                    }

                    matchTypeStr += "</select>";

                    $("#groupDiv").append(groupStr);
                    $("#selectMatchType").append(matchTypeStr);

                    fnListHtml(res);

                }
            }
        })
    }

    const fnSearchLeague = () => {

        $("#addedLeagueMatch").empty();

        //$("#selectMatchType").empty();
        //$("#groupDiv").empty();
        $("#leagueInfoList").empty();
        $("#leagueDetailList").empty();

        const listStr = '<td colspan="10">리그 검색후 선택해 주세요.</td>';
        $("#leagueDetailList").append(listStr);

        const sYear = $("#leagueSYear option:selected").val();
        const ageGroup = $("#leagueAgeGroup option:selected").val();
        const sCupName = $("#sLeagueName").val();

        const param = {
            'sYear': sYear,
            'ageGroup': ageGroup,
            'sLeagueName': sCupName,
        }

        const mediaType = '${mediaType}';

        $.ajax({
            type: 'POST',
            url: '/search_league',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    let str = "";
                    if (mediaType === 'Game') {
                        if (res.data.length > 0) {
                            $("#leagueInfoList tr").empty();
                            for (let i = 0; i < res.data.length; i++) {
                                str += "<tr onclick='fnSearchLeagueDetail(" + res.data[i].league_id + ", \"" + res.ageGroup + "\")'>" +
                                    "<td>" +
                                    res.data[i].league_name +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                    "</td>" +
                                    "</td>";
                            }
                            $("#leagueInfoList").append(str);

                        } else {
                            str = "<tr><td colspan='2'>검색 결과가 없습니다.</td></tr>";
                            $("#leagueInfoList tr").empty();
                            $("#leagueInfoList").append(str);
                        }
                    } else if (mediaType === 'News') {
                        if (res.data.length > 0) {

                            $("#leagueInfoList tr").empty();
                            for (let i = 0; i < res.data.length; i++) {
                                // str += "<tr onclick='fnSearchLeagueDetail(" + res.data[i].league_id + ", \"" + res.ageGroup + "\")'>" +
                                str += "<tr>" +
                                    "<td>" +
                                        "<input type='checkbox' value='" + res.data[i].league_id + "'" +
                                            " data-leagueId='" + res.data[i].league_id + "'" +
                                            " data-ageGroup='" + res.ageGroup + "'" +
                                            " data-leagueName='" + res.data[i].league_name + "'" +
                                            " name='ch" + i + "' id='ch" + i + "-" + i + "'>" +
                                        "<label for='ch" + i + "-" + i + "'></label>" +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].league_name +
                                    "</td>" +
                                    "<td>" +
                                    res.data[i].play_sdate + "~" + res.data[i].play_edate +
                                    "</td>" +
                                    "</td>";
                            }
                            $("#leagueInfoList").append(str);

                        } else {
                            str = "<tr><td colspan='2'>검색 결과가 없습니다.</td></tr>";
                            $("#leagueInfoList tr").empty();
                            $("#leagueInfoList").append(str);
                        }
                    }

                } else {
                    alert("검색에 실패했습니다.");
                    return false;
                }
            }
        })

    }

    const fnSearchLeagueDetail = (leagueId, ageGroup, months, isSearch) => {

        if (!isSearch) {
            leagueMapList = [];
        }

        if(!leagueId) {
            alert('잘못된 접근입니다.');
            return;
        }

        const param = {
            leagueId : leagueId,
            ageGroup: ageGroup,
            months: months,
        }

        $.ajax({
            type: 'POST',
            url: '/search_league_detail',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {

                    let leagueDetailStr = "";

                    $("#leagueGroupDiv").empty();
                    $("#leagueDetailList").empty();

                    let monthStr = "";
                    for (let i = 1; i <= 12; i++) {
                        monthStr += "<a class='btn-large w6 btn-tab";

                        if (res.months == i) {
                            monthStr += " blue-o";
                        } else {
                            monthStr += " gray-o";
                        }
                        monthStr += " data-id='num" + i + "' onclick='fnSearchLeagueDetail(" + res.data.leagueInfo.league_id + ", \"" + res.ageGroup + "\", " + i +", true)'>" + i + "월</a>";
                    }

                    $("#leagueGroupDiv").append(monthStr);

                    if (res.data.matchList.length > 0) {
                        for (let i = 0; i < res.data.matchList.length; i++) {
                            leagueDetailStr += "<tr>" +
                                                    "<td>" +
                                                        "<input type='checkbox' onclick='fnSelectLeagueMatch(this)' value='" + res.data.matchList[i].league_match_id + "'" +
                                                            " data-leagueId='" + res.data.leagueInfo.league_id + "'" +
                                                            " data-ageGroup='" + res.ageGroup + "'" +
                                                            " data-home='" + res.data.matchList[i].home + "'" +
                                                            " data-homeId='" + res.data.matchList[i].home_id + "'" +
                                                            " data-away='" + res.data.matchList[i].away + "'" +
                                                            " data-awayId='" + res.data.matchList[i].away_id + "'" +
                                                            " data-playdate='" + res.data.matchList[i].playdate + "'" +
                                                            " data-ptime='" + res.data.matchList[i].ptime + "'" +
                                                            " data-place='" + res.data.matchList[i].place + "'";

                                                            let isChecked = leagueMapList.some(item => item.leagueId == res.data.leagueInfo.league_id && item.matchId == res.data.matchList[i].league_match_id);
                            if (isChecked) {
                            leagueDetailStr +=              "checked ";
                            }
                            leagueDetailStr +=              " name='ch" + i + "' id='ch" + i + "-" + i + "'>" +
                                                        "<label for='ch" + i + "-" + i + "'></label>" +
                                                    "</td>" +
                                                    "<td>" + res.data.matchList[i].playdate + "</td>" +
                                                    "<td>" + res.data.matchList[i].ptime + "</td>" +
                                                    "<td>" + res.data.matchList[i].place + "</td>" +
                                                    "<td>" + res.data.matchList[i].home + "</td>" +
                                                    "<td>" + res.data.matchList[i].home_score + "</td>" +
                                                    "<td>" + res.data.matchList[i].away_score + "</td>" +
                                                    "<td>" + res.data.matchList[i].away + "</td>" +
                                                "</tr>";
                        }
                    } else {
                        leagueDetailStr += "<tr><td colspan='10'> 조회된 리그 일정이 없습니다.</td><tr>";
                    }

                    $("#leagueDetailList").append(leagueDetailStr);

                }
            }
        })
    }

    const fnSearchTeam = () => {

        //$("#selectMatchType").empty();
        //$("#groupDiv").empty();
        $("#teamDetailList").empty();

        const listStr = '<td colspan="10">팀을 검색 해주세요.</td>';
        $("#teamDetailList").append(listStr);

        const teamType = $("#selectTeamType option:selected").val();
        const ageGroup = $("#teamAgeGroup option:selected").val();
        const sTeamName = $("#sTeamName").val();

        const param = {
            'ageGroup': ageGroup,
            'sTeamType': teamType,
            'sNickName': sTeamName,
        }

        $.ajax({
            type: 'POST',
            url: '/search_team',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    let str = "";
                    if (res.data.length > 0) {
                        $("#teamDetailList").empty();
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>" +
                                        "<td>" +
                                            "<input type='checkbox' value='" + res.data[i].team_id + "'" +
                                            " data-ageGroup='" + res.ageGroup + "'" +
                                            " data-nickName='" + res.data[i].nick_name + "'" +
                                            " name='ch" + i + "' id='ch" + i + "-" + i + "'>" +
                                            "<label for='ch" + i + "-" + i + "'></label>" +
                                        "</td>" +
                                        "<td>" + res.data[i].area_name + "</td>";
                            if (res.data[i].emblem) {
                                str +=  "<td>" +
                                            "<img src='NP" + res.data[i].emblem + "' class='logo'>"
                                        "</td>";
                            } else {
                                str +=  "<td>" +
                                            "<img src='resources/img/logo/none.png' class='logo'>"
                                        "</td>";
                            }
                            str +=      "<td>" + res.data[i].uage + "</td>";
                            if (res.data[i].team_type == 0) {
                                str +=  "<td><span class='label blue'>학원</span></td>";
                            } else if (res.data[i].team_type == 1) {
                                str +=  "<td><span class='label green'>클럽</span></td>";
                            } else if (res.data[i].team_type == 2) {
                                str +=  "<td><span class='label red'>유스</span></td>";
                            }
                            str +=      "<td>" + res.data[i].nick_name + "</td>" +
                                        "<td>" + res.data[i].team_name + "</td>" +
                                        "<td>" + res.data[i].addr + "</td>";
                            if (res.data[i].use_flag == 0) {
                                str +=  "<td>활성</td>";
                            } else {
                                str +=  "<td>비활성</td>";
                            }

                        }
                        $("#teamDetailList").append(str);

                    } else {
                        str = "<tr><td colspan='9'>검색 결과가 없습니다.</td></tr>";
                        $("#teamDetailList").empty();
                        $("#teamDetailList").append(str);
                    }
                } else {
                    alert("검색에 실패했습니다.");
                    return false;
                }
            }
        })

    }

    const fnListHtml = (res) => {
        let cupDetailStr = "";
        if (res.data.matchList.length > 0) {

            for(let i = 0; i < res.data.matchList.length; i++) {
                cupDetailStr += "<tr>" +
                                    "<td>" +
                                        "<input type='checkbox' onclick='fnSelectCupMatch(this)'";
                if (res.data.cupInfoMap.cup_type == 0 && res.matchType == '0' ||
                    res.data.cupInfoMap.cup_type == 1 && res.matchType == '0' ||
                    res.data.cupInfoMap.cup_type == 2 || res.teamType == '1' ||
                    res.teamType == '2') {

                    cupDetailStr +=     " value='" + res.data.matchList[i].cup_sub_match_id + "' " +
                                        "data-cupId='" + res.data.cupInfoMap.cup_id + "' " +
                                        "data-ageGroup='" + res.ageGroup + "' " +
                                        "data-home='" + res.data.matchList[i].home + "' " +
                                        "data-homeId='" + res.data.matchList[i].home_id + "' " +
                                        "data-away='" + res.data.matchList[i].away + "' " +
                                        "data-awayId='" + res.data.matchList[i].away_id + "' " +
                                        "data-playdate='" + res.data.matchList[i].playdate + "' " +
                                        "data-ptime='" + res.data.matchList[i].ptime + "' " +
                                        "data-place='" + res.data.matchList[i].place + "' " +
                                        "data-cupType='" + res.data.cupInfoMap.cup_type + "' " +
                                        "data-matchType='" + res.matchType + "' ";

                    let isChecked = cupMapList.some(item => item.cupId == res.data.cupInfoMap.cup_id && item.matchId == res.data.matchList[i].cup_sub_match_id);


                    if (isChecked) {
                        cupDetailStr += "checked ";
                    }


                    if (res.teamType) {
                    cupDetailStr +=     "data-teamType='" + res.teamType + "' " +
                                        "name='ch" + i + "' id='ch" + i + "-" + i + "'>";
                    } else {
                    cupDetailStr +=     "name='ch" + i + "' id='ch" + i + "-" + i + "'>";
                    }

                }

                if ((res.data.cupInfoMap.cup_type == 0 && res.matchType == '1') ||
                    (res.data.cupInfoMap.cup_type == 1 && res.matchType == '2') ||
                    res.data.cupInfoMap.cup_type == 3) {

                    let isChecked = cupMapList.some(item => item.cupId == res.data.cupInfoMap.cup_id && item.matchId == res.data.matchList[i].cup_tour_match_id);

                    cupDetailStr +=     " value='" + res.data.matchList[i].cup_tour_match_id + "' " +
                                        "data-cupId='" + res.data.cupInfoMap.cup_id + "' " +
                                        "data-ageGroup='" + res.ageGroup + "' " +
                                        "data-home='" + res.data.matchList[i].home + "' " +
                                        "data-homeId='" + res.data.matchList[i].home_id + "' " +
                                        "data-away='" + res.data.matchList[i].away + "' " +
                                        "data-awayId='" + res.data.matchList[i].away_id + "' " +
                                        "data-playdate='" + res.data.matchList[i].playdate + "' " +
                                        "data-ptime='" + res.data.matchList[i].ptime + "' " +
                                        "data-place='" + res.data.matchList[i].place + "' " +
                                        "data-cupType='" + res.data.cupInfoMap.cup_type + "' " +
                                        "data-matchType='" + res.matchType + "' ";

                    if (isChecked) {
                        cupDetailStr += "checked ";
                    }

                        cupDetailStr += "name='ch" + i + "' id='ch" + i + "-" + i + "'>";

                }

                if (res.data.cupInfoMap.cup_type == 1 && res.matchType == '1') {

                    let isChecked = cupMapList.some(item => item.cupId == res.data.cupInfoMap.cup_id && item.matchId == res.data.matchList[i].cup_main_match_id);

                    cupDetailStr +=     " value='" + res.data.matchList[i].cup_main_match_id + "' " +
                                        "data-cupId='" + res.data.cupInfoMap.cup_id + "' " +
                                        "data-ageGroup='" + res.ageGroup + "' " +
                                        "data-home='" + res.data.matchList[i].home + "' " +
                                        "data-homeId='" + res.data.matchList[i].home_id + "' " +
                                        "data-away='" + res.data.matchList[i].away + "' " +
                                        "data-awayId='" + res.data.matchList[i].away_id + "' " +
                                        "data-playdate='" + res.data.matchList[i].playdate + "' " +
                                        "data-ptime='" + res.data.matchList[i].ptime + "' " +
                                        "data-place='" + res.data.matchList[i].place + "' " +
                                        "data-cupType='" + res.data.cupInfoMap.cup_type + "' " +
                                        "data-matchType='" + res.matchType + "' ";

                    if (isChecked) {
                        cupDetailStr += "checked ";
                    }

                    if (res.teamType) {
                    cupDetailStr +=     "data-teamType='" + res.teamType + "' " +
                                        "name='ch" + i + "' id='ch" + i + "-" + i + "'>";
                    } else {
                    cupDetailStr +=     "name='ch" + i + "' id='ch" + i + "-" + i + "'>";
                    }

                }

                cupDetailStr += "<label for='ch" + i + "-" + i + "'></label>" +
                                "</td>" +
                                "<td>" + res.data.matchList[i].playdate + "</td>" +
                                "<td>" + res.data.matchList[i].ptime + "</td>" +
                                "<td>" + res.data.matchList[i].place + "</td>" +
                                "<td>" + res.data.matchList[i].home + "</td>" +
                                "<td>" + res.data.matchList[i].home_score + "</td>" +
                                "<td>" + res.data.matchList[i].home_pk + "</td>" +
                                "<td>" + res.data.matchList[i].away_pk + "</td>" +
                                "<td>" + res.data.matchList[i].away_score + "</td>" +
                                "<td>" + res.data.matchList[i].away + "</td>" +
                                "</tr>";

            }
        } else {
            cupDetailStr += "<tr><td colspan='10'> 조회된 대회 일정이 없습니다.</td><tr>";
        }

        $("#cupDetailList").append(cupDetailStr);
    }

    const fnGroupsHtml = (res) => {
        $("#groupDiv").empty();
        const arRound = [128, 64, 32, 16, 8, 4, 2];

        let groupStr = "";

        // 예선 + 토너먼트 타입 -> 토너먼트 선택시
        if ((res.data.cupInfoMap.cup_type == 0 && res.matchType == '1') ||
            (res.data.cupInfoMap.cup_type == 1 && res.matchType == '2') ||
            res.data.cupInfoMap.cup_type == 3) {
            for (let i = 0; i < arRound.length; i++) {
                if (arRound[i] > res.data.cupInfoMap.tour_team && (arRound[i] - res.data.cupInfoMap.tour_team < res.data.cupInfoMap.tour_team)) {
                    groupStr += "<a class='btn-large";
                    if (res.data.cupInfoMap.tour_team == res.groups) {
                        groupStr += " blue-o'";
                    } else {
                        groupStr += " gray-o'";
                    }
                    groupStr += " onclick='fnSelectTourGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\"," + res.data.cupInfoMap.tour_team + ")'>와일드카드</a>";
                }
                if (arRound[i] == 2) {
                    groupStr += "<a class='btn-large";
                    if (arRound[i] == res.groups) {
                        groupStr += " blue-o'";
                    } else {
                        groupStr += " gray-o'";
                    }
                    groupStr +=  " onclick='fnSelectTourGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\"," + arRound[i] + ")'>결승</a>";
                }
                if (arRound[i] > 2 && arRound[i] <= res.data.cupInfoMap.tour_team) {
                    groupStr += "<a class='btn-large";
                    if (arRound[i] == res.groups) {
                        groupStr += " blue-o'";
                    } else {
                        groupStr += " gray-o'";
                    }
                    groupStr += " onclick='fnSelectTourGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\"," + arRound[i] + ")'>" + arRound[i] + "강</a>";
                }

            }
        }
        // 예선 + 토너먼트, 예선 + 본선 + 토너먼트, 풀리그 + 풀리그
        // select box 예선경기, 1차 풀리그 선택
        if (res.data.cupInfoMap.cup_type == 0 && res.matchType == '0' ||
            res.data.cupInfoMap.cup_type == 1 && res.matchType == '0' ||
            res.data.cupInfoMap.cup_type == 2 || res.teamType == '1') {
            for (let i = 1; i <= res.data.cupInfoMap.group_count; i++) {
                groupStr += "<a class='btn-large";
                if (res.groups == i) {
                    groupStr += " blue-o'";
                } else {
                    groupStr += " gray-o'";
                }
                groupStr += " onclick='fnSelectCupGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\", " + i + ")'>" +
                    i +"조</a>";
            }
        }

        // 풀리그 + 풀리그
        // select box 2차 풀리그 선택
        if (res.data.cupInfoMap.cup_type == 1 && res.matchType == '1' ||
            res.teamType == '2') {
            for (let i = 1; i <= res.data.cupInfoMap.m_group_count; i++) {
                groupStr += "<a class='btn-large";
                if (res.groups == i) {
                    groupStr += " blue-o'";
                } else {
                    groupStr += " gray-o'";
                }
                groupStr += " onclick='fnSelectCupGroup(" + res.data.cupInfoMap.cup_id + ", " + res.data.cupInfoMap.cup_type + ", \"" + res.ageGroup + "\", " + i + ")'>" +
                    i +"조</a>";
            }
        }

        $("#groupDiv").append(groupStr);
    }

    // 예선, 풀리그 조 선택
    const fnSelectCupGroup = (cupId, cupType, ageGroup, groups) => {
        const matchType = $("#matchSelect option:selected").val();

        if (!groups) {
            groups = 1;
        }
        fnSearchCupDetail(cupId, cupType, ageGroup, groups, matchType, true);
    }

    // 토너먼트 조 선택
    const fnSelectTourGroup = (cupId, cupType, ageGroup, groups) => {
        const matchType = $("#matchSelect option:selected").val();
        fnSearchCupDetail(cupId, cupType, ageGroup, groups, matchType, true);
    }

    const fnSelectCupMatch = (el) => {
        const map = {
            matchId : $(el).val(),
            cupId : $(el).attr('data-cupId'),
            ageGroup : $(el).attr('data-ageGroup'),
            home : {
                homeId : $(el).attr('data-homeId'),
                homeName : $(el).attr('data-home')
            },
            away : {
                awayId : $(el).attr('data-awayId'),
                awayName :  $(el).attr('data-away')
            },
            playDate: $(el).attr('data-playDate'),
            pTime : $(el).attr('data-pTime'),
            place : $(el).attr('data-place'),
            cupType : $(el).attr('data-cupType'),
            matchType : $(el).attr('data-matchType'),
            teamType : $(el).attr('data-teamType'),
            cupName : $(el).attr('data-cupName')
        }

        if (el.checked) {
            cupMapList.push(map);
        } else {
            cupMapList = cupMapList.filter(item => !(item.cupId == $(el).attr('data-cupId') && item.matchId == $(el).val()));
        }
    }

    const fnSelectLeagueMatch = (el) => {
        const map = {
            matchId : $(el).val(),
            leagueId : $(el).attr('data-leagueId'),
            ageGroup : $(el).attr('data-ageGroup'),
            home : {
                homeId : $(el).attr('data-homeId'),
                homeName : $(el).attr('data-home')
            },
            away : {
                awayId : $(el).attr('data-awayId'),
                awayName :  $(el).attr('data-away')
            },
            playDate: $(el).attr('data-playDate'),
            pTime : $(el).attr('data-pTime'),
            place : $(el).attr('data-place')
        }

        if (el.checked) {
            leagueMapList.push(map);
        } else {
            leagueMapList = cupMapList.filter(item => !(item.leagueId == $(el).attr('data-leagueId') && item.matchId == $(el).val()));
        }
    }

    const addCupMatch = () => {

        let str = "";
        let nameStr = "";

        let addStr = "";
        let ageGroup = "";
        const mediaType = "${mediaType}";

        let homeTeamStr = "";
        let awayTeamStr = "";

        if (mediaType === 'Game') {
            if (cupMapList.length > 0) {
                cupMapList.forEach(item => {
                    console.log(item);
                    if (item.cupType == '0' && item.matchType == '0' ||
                        item.cupType == '1' && item.matchType == '0' ||
                        item.cupType == '4' && item.matchType == '1' && item.teamType == '1' ||
                        item.cupType == '4' && item.matchType == '2' && item.teamType == '2' ||
                        item.cupType == '2') {
                        nameStr = "Cup_Sub_Match";
                    }
                    if (item.cupType == '0' && item.matchType == '1' ||
                        item.cupType == '1' && item.matchType == '2' ||
                        item.cupType == '3') {
                        nameStr = "Cup_Tour_Match";
                    }
                    if (item.cupType == '1' && item.matchType == '1' ||
                        item.cupType == '4' && item.matchType == '1' && item.teamType == '2') {
                        nameStr = "Cup_Main_Match";
                    }

                    if (mediaType === 'Game') {
                        str += "<input type='hidden' " +
                            "name='cupData'" +
                            " data-id='"+ item.ageGroup + item.matchId +
                            "' data-ageGroup='"+ item.ageGroup +
                            "' data-cupId='"+ item.cupId +
                            "' data-cupType='" + nameStr +
                            "' value='" + item.matchId + "' >";

                        addStr += "<a class='btn-large gray-o x'" +
                            " data-matchId='" + item.matchId +
                            "' data-ageGroup='" + item.ageGroup +
                            "' data-homeId='" + item.home.homeId +
                            "' data-awayId='" + item.away.awayId +
                            "' data-cupType='" + item.cupType +
                            "' onclick='cancelCupMatch($(this))'>" +
                            item.home.homeName + "vs" +
                            item.away.awayName + "," +
                            item.playDate + " " +
                            item.pTime + " " +
                            item.place +
                            "</a>";

                        homeTeamStr += "<input type='hidden' " +
                            "name='teamData'" +
                            " data-id='"+ item.ageGroup + item.matchId +
                            "' data-ageGroup='"+ item.ageGroup +
                            "' data-cupType='" + item.cupType +
                            "' value='" + item.home.homeId + "' >";

                        awayTeamStr += "<input type='hidden' " +
                            "name='teamData'" +
                            " data-id='"+ item.ageGroup + item.matchId +
                            "' data-ageGroup='"+ item.ageGroup +
                            "' data-cupType='" + item.cupType +
                            "' value='" + item.away.awayId + "' >";
                    }

                });
            } else {
                alert('선택된 경기가 없습니다.');
                return false;
            }
        } else {
            const chkMatch = $("input[type=checkbox]:checked");

            $(chkMatch).each(function(i) {
                str += "<input type='hidden' " +
                    "name='cupData'" +
                    " data-id='"+$(this).attr('data-ageGroup') + $(this).val() +
                    "' data-ageGroup='"+$(this).attr('data-ageGroup') +
                    "' data-cupId='"+$(this).attr('data-cupId') +
                    "' value='" + $(this).val() + "' >";

                addStr += "<a class='btn-large gray-o x'" +
                    " data-matchId='" + $(this).val() +
                    "' data-ageGroup='" + $(this).attr('data-ageGroup') +
                    "' onclick='cancelCupMatch($(this))'>" +
                    $(this).attr('data-cupName')
                "</a>";
            });
        }

        $("#selectedCupMatch").append(str);
        $("#addedCupMatch").append(addStr);

        $("#selectedTeam").append(homeTeamStr);
        $("#selectedTeam").append(awayTeamStr);

        closeCupPop()

    }

    const cancelCupMatch = (el) => {
        console.log(el);
        $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-matchId') +"']").remove();
        $(el).remove();

        const mediaType = '${mediaType}';

        if (mediaType === 'Game') {
            $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-homeId') +"']").remove();
            $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-awayId') +"']").remove();
        }

    }

    const closeCupPop = () => {

        $(".pop").fadeOut();

        $("#selectMatchType").empty();
        $("#groupDiv").empty();
        $("#cupDetailList").empty();
        $("#cupInfoList").empty();

        const listStr = '<td colspan="2">대회를 검색 해주세요.</td>';
        const listStr2 = '<td colspan="10">대회 검색후 선택해 주세요.</td>';
        $("#cupInfoList").append(listStr);
        $("#cupDetailList").append(listStr2);

    }

    const addLeagueMatch = () => {

        const chkMatch = $("input[type=checkbox]:checked");

        let str = "";
        let nameStr = "";

        let addStr = "";

        const mediaType = '${mediaType}';

        let homeTeamStr = "";
        let awayTeamStr = "";

        if (mediaType === 'Game') {
            if (leagueMapList.length > 0) {
                leagueMapList.forEach(item => {
                    nameStr = "League_Match";

                    str += "<input type='hidden' " +
                        "name='leagueData'" +
                        " data-id='"+ item.ageGroup + item.matchId +
                        "' data-ageGroup='"+ item.ageGroup +
                        "' data-leagueId='"+ item.leagueId +
                        "' data-cupType='" + nameStr +
                        "' value='" + item.matchId + "' >";

                    addStr += "<a class='btn-large gray-o x'" +
                        " data-matchId='" + item.matchId +
                        "' data-ageGroup='" + item.ageGroup +
                        "' data-homeId='" + item.home.homeId +
                        "' data-awayId='" + item.away.awayId +
                        /*"' data-cupType='" + $(this).attr('data-cupType') +*/
                        "' onclick='cancelLeagueMatch($(this))'>" +
                        item.home.homeName + "vs" +
                        item.away.awayName + "," +
                        item.playDate + " " +
                        item.pTime + " " +
                        item.place +
                        "</a>";

                    homeTeamStr += "<input type='hidden' " +
                        "name='teamData'" +
                        " data-id='"+ item.ageGroup + item.matchId +
                        "' data-ageGroup='"+ item.ageGroup +
                        "' data-cupType='" + item.ageGroup +
                        "' value='" + item.home.homeId + "' >";

                    awayTeamStr += "<input type='hidden' " +
                        "name='teamData'" +
                        " data-id='"+ item.ageGroup + item.matchId +
                        "' data-ageGroup='"+ item.ageGroup +
                        "' data-cupType='" + item.ageGroup +
                        "' value='" + item.away.awayId + "' >";
                })
            }
        } else {
            if (mediaType === 'News') {
                $(chkMatch).each(function(i) {
                    str += "<input type='hidden' " +
                        "name='leagueData'" +
                        " data-id='"+$(this).attr('data-ageGroup') + $(this).val() +
                        "' data-ageGroup='"+$(this).attr('data-ageGroup') +
                        "' data-leagueId='"+$(this).attr('data-leagueId') +
                        "' value='" + $(this).val() + "' >";

                    addStr += "<a class='btn-large gray-o x'" +
                        " data-matchId='" + $(this).val() +
                        "' data-ageGroup='" + $(this).attr('data-ageGroup') +
                        "' onclick='cancelLeagueMatch($(this))'>" +
                        $(this).attr('data-leagueName') +
                        "</a>";
                });
            }
        }


        console.log('str : ', str);
        console.log('addStr : ', addStr);

        $("#selectedLeagueMatch").append(str);
        $("#addedLeagueMatch").append(addStr);

        $("#selectedTeam").append(homeTeamStr);
        $("#selectedTeam").append(awayTeamStr);

        closeLeaguePop()

    }

    const cancelLeagueMatch = (el) => {
        $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-matchId') +"']").remove();
        $(el).remove();

        const mediaType = '${mediaType}';

        if (mediaType === 'Game') {
            $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-homeId') +"']").remove();
            $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-awayId') +"']").remove();
        }
    }

    const closeLeaguePop = () => {

        $(".pop").fadeOut();

        $("#leagueGroupDiv").empty();
        $("#leagueDetailList").empty();
        $("#leagueInfoList").empty();

        const listStr = '<td colspan="2">리그를 검색 해주세요.</td>';
        const listStr2 = '<td colspan="10">리그 검색후 선택해 주세요.</td>';
        $("#leagueInfoList").append(listStr);
        $("#leagueDetailList").append(listStr2);

    }

    const addTeam = (val) => {

        const chkMatch = $("input[type=checkbox]:checked");

        let str = "";
        let nameStr = "";

        let addStr = "";

        $(chkMatch).each(function(i) {

            nameStr = "League_Match";


            str += "<input type='hidden' " +
                    "name='teamData'" +
                    " data-id='"+$(this).attr('data-ageGroup') + $(this).val() +
                    "' data-ageGroup='"+ $(this).attr('data-ageGroup') +
                    "' data-cupType='" + $(this).attr('data-ageGroup') +
                    "' value='" + $(this).val() + "' >";

            addStr += "<a class='btn-large gray-o x'" +
                        " data-teamId='" + $(this).val() +
                        "' data-ageGroup='" + $(this).attr('data-ageGroup') +
                        "' data-cupType='" + $(this).attr('data-ageGroup') +
                        "' onclick='cancelTeam($(this))'>" +
                        $(this).attr('data-nickName') +
                        "<span class='cored'>" + $(this).attr('data-ageGroup') + "</span>" +
                    "</a>";
        });

        $("#selectedTeam").append(str);
        $("#addedTeam").append(addStr);

        closeTeamPop()

    }

    const cancelTeam = (el) => {
        $("input[data-id="+ "'"+ $(el).attr('data-ageGroup') + $(el).attr('data-teamId') +"']").remove();
        $(el).remove();
    }

    const closeTeamPop = () => {

        $(".pop").fadeOut();

        $("#teamDetailList").empty();

        const listStr = '<td colspan="19">팀을 검색 해주세요.</td>';

        $("#teamDetailList").append(listStr);

    }

    const saveMedia = (val) => {
        const method = "${method}"
        const mediaType = "${mediaType}";

        const useFlag = $("input[name='ra1']:checked").val();
        const title = $("input[name='title']").val();
        const urlLink = $("input[name='urlLink']").val()
        const imgUrl = $("input[name='imgUrl']").val()
        const type = $("input[name='ra2']:checked").val();
        const submitDate = $("input[name='submitDate']").val();

        const source = $("input[name='source']").val();
        const summary = $("textarea[name='summary']").val();
        const content = $("textarea[name='content']").val();
        //const content = $("textarea[name='content']").val().replace(/\n/g, '<br />');

        const uage = $("#uage option:selected").val();

        const subType = $("#subType option:selected").val();

        const refUrl = $("input[name='refUrl']").val()

        const creatorId = $("#creator option:selected").val();

        const showFlag = $("input[name='showFlag']:checked").val();
        const mainFlag = $("input[name='mainFlag']:checked").val();

        if (content) {
        }
        //content.replace(/\n/g, '<br />');
        if(!useFlag) {
            alert('활성 여부를 체크 해주세요.');
            return false;
        }
        if(!title) {
            alert('제목을 입력 해주세요.');
            return false;
        }
        if(!urlLink) {
            alert('링크를 입력 해주세요.');
            return false;
        }

        if(mediaType == 'Game' && subType == 0 && !type) {
            alert('구분을 체크 해주세요.');
            return false;
        }

        let param = {
            cupData: [],
            leagueData: [],
            teamData: []
        };

        const cupInputs = $("input[name='cupData']");
        const leagueInputs = $("input[name='leagueData']");
        const teamInputs = $("input[name='teamData']");

        cupInputs.each(function() {

            const ageGroup = $(this).attr('data-agegroup');
            const cupId = $(this).attr('data-cupid');
            const cupType = $(this).attr('data-cuptype');
            const value = $(this).val();


            if (mediaType === 'Game') {
                const data = param.cupData.find(item => item.ageGroup === ageGroup && item.cupId === cupId);
                if (data) {
                    if (data.childType) {
                        data.childType = cupType;
                    } else {
                        data.childType = cupType;
                    }
                } else {
                    const newData = {
                        ageGroup: ageGroup,
                        parentId: cupId,
                        childId: value
                    };
                    newData.childType = cupType;
                    param.cupData.push(JSON.stringify(newData));
                }
            }

            if (mediaType === 'News') {

                const newData = {
                    ageGroup: ageGroup,
                    parentId: cupId,
                };

                param.cupData.push(JSON.stringify(newData));
            }

        });

        leagueInputs.each(function() {

            const ageGroup = $(this).attr('data-agegroup');
            const cupId = $(this).attr('data-leagueid');
            const cupType = $(this).attr('data-cuptype');
            const value = $(this).val();

            if (mediaType === 'Game') {
                const data = param.cupData.find(item => item.ageGroup === ageGroup && item.cupId === cupId);
                if (data) {
                    if (data.childType) {
                        data.childType = cupType;
                    } else {
                        data.childType = cupType;
                    }
                } else {
                    const newData = {
                        ageGroup: ageGroup,
                        parentId: cupId,
                        childId: value
                    };
                    newData.childType = cupType;
                    param.leagueData.push(JSON.stringify(newData));
                }
            }

            if (mediaType === 'News') {

                const newData = {
                    ageGroup: ageGroup,
                    parentId: cupId,
                };

                param.leagueData.push(JSON.stringify(newData));
            }

        });

        teamInputs.each(function() {
           const ageGroup = $(this).attr('data-agegroup');
           const value = $(this).val();
           const childType = $(this).attr('data-cuptype');
           const newData = {
               ageGroup: ageGroup,
               childId: value,
               childType: childType,
           }
            param.teamData.push(JSON.stringify(newData));
        });

        console.log(param);

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');

        newForm.attr('action', '/saveMedia' + mediaType);
        if (method == "modify") {
            const mediaId = "${mediaInfo.media_id}"
            newForm.append($('<input/>', {type: 'hidden', name: 'mediaId', value: mediaId }));
        }
        newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));
        newForm.append($('<input/>', {type: 'hidden', name: 'useFlag', value: useFlag }));
        newForm.append($('<input/>', {type: 'hidden', name: 'title', value: title }));
        newForm.append($('<input/>', {type: 'hidden', name: 'urlLink', value: urlLink }));
        newForm.append($('<input/>', {type: 'hidden', name: 'imgUrl', value: imgUrl }));
        newForm.append($('<input/>', {type: 'hidden', name: 'type', value: type }));
        newForm.append($('<input/>', {type: 'hidden', name: 'source', value: source }));
        newForm.append($('<input/>', {type: 'hidden', name: 'content', value: content }));
        newForm.append($('<input/>', {type: 'hidden', name: 'submitDate', value: submitDate }));
        newForm.append($('<input/>', {type: 'hidden', name: 'summary', value: summary }));
        if(mediaType == 'Game' || mediaType == 'News') {
            newForm.append($('<input/>', {type: 'hidden', name: 'uage', value: uage }));
        }
        newForm.append($('<input/>', {type: 'hidden', name: 'subType', value: subType }));
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: method }));
        newForm.append($('<input/>', {type: 'hidden', name: 'refUrl', value: refUrl }));
        newForm.append($('<input/>', {type: 'hidden', name: 'creatorId', value: creatorId }));

        newForm.append($('<input/>', {type: 'hidden', name: 'showFlag', value: showFlag }));
        newForm.append($('<input/>', {type: 'hidden', name: 'mainFlag', value: mainFlag }));
        newForm.append($('<input/>', {type: 'hidden', name: 'bfMainFlag', value: '${mediaInfo.main_flag}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'bfMediaOrder', value: '${mediaInfo.media_order}' }));


        newForm.append($('<input/>', {type: 'hidden', name: 'matchData', value: JSON.stringify(param) }));

        $(newForm).appendTo('body').submit();

    }

    const craw_youtube = () => {
        const craw_url = $("#craw_url").val();
        if (!craw_url) {
            alert("주소를 입력하세요");
            return;
        }
        $.ajax({
            type: 'GET',
            url: '/craw_youtube',
            data: {url:craw_url},
            success: function(res) {

                $("input[name=title]").val(res.title);
                $("input[name=urlLink]").val(res.embedded);
                $("input[name=refUrl]").val(res.link);
                $("input[name=imgUrl]").val(res.thumbnail);
                $("textarea[name=content]").val(res.infoText);
                $("input[name=submitDate]").val(replace_date(res.uploadDate));
            },
            error:function(request,status,error){
                alert('크롤링 도중 에러 발생');
            }
        });

    }


    const craw_news = () => {
        const craw_url = $("#craw_url_news").val();
        if (!craw_url) {
            alert("주소를 입력하세요");
            return;
        }
        $.ajax({
            type: 'GET',
            url: '/craw_news',
            data: {url:craw_url},
            success: function(res) {
                console.log(res);
                $("input[name=title]").val(res.title);
                $("textarea[name=content]").val(res.content);
                let img = "";
                if (res.imageList) {
                    img = res.imageList.split(',')[0];
                }
                $("input[name=imgUrl]").val(img);
                $("input[name=submitDate]").val(replace_date_news(res.writeDate));
                $("input[name=source]").val(res.source);
                $("input[name=urlLink]").val(craw_url);
            },
            error:function(request,status,error){
                alert('크롤링 도중 에러 발생');
            }
        });

    }


    const craw_blog = () => {
        const craw_url = $("#craw_url_blog").val();
        if (!craw_url) {
            alert("주소를 입력하세요");
            return;
        }
        $.ajax({
            type: 'GET',
            url: '/craw_blog',
            data: {url:craw_url},
            success: function(res) {
                console.log(res);
                $("input[name=title]").val(res.title);
                $("textarea[name=content]").val(res.content);
                let img = "";
                if (res.contentImage) {
                    img = res.contentImage.split(',')[0];
                }
                $("input[name=imgUrl]").val(img);
                $("input[name=submitDate]").val(replace_date_blog(res.writeDate));
                $("input[name=urlLink]").val(craw_url);
            },
            error:function(request,status,error){
                alert('크롤링 도중 에러 발생');
            }
        });

    }


    const replace_date = (date) => {
        var parts = date.split(": ");
        var dateString = parts[1];

        if (date.indexOf(':') == -1) {
            dateString = date;
        }

        // 날짜 형식 변경 (마지막의 점(.)을 제거)
        var formattedString = dateString.replace(/(\d+)\. (\d+)\. (\d+)\./, function(match, year, month, day) {
            // 날짜 부분을 0으로 채워서 두 자리로 만들기
            month = month.length === 1 ? '0' + month : month;
            day = day.length === 1 ? '0' + day : day;

            // 변경된 날짜 형식으로 반환 (마지막의 점을 제거)
            return year + '-' + month + '-' + day;
        });

        return formattedString;
    };

    const replace_date_news = (date) => {

        if(date.indexOf('기사입력') === -1){
            return date.split(' ')[0];
        }

        // 문자열에서 "기사입력 " 이후의 부분을 추출
        var dateString = date.split('기사입력 ')[1];

        // 날짜 형식 변경
        var formattedString = dateString.replace(/(\d+)\.(\d+)\.(\d+).*/, function(match, year, month, day) {
            // 날짜 부분을 0으로 채워서 두 자리로 만들기
            month = month.length === 1 ? '0' + month : month;
            day = day.length === 1 ? '0' + day : day;

            // 변경된 날짜 형식으로 반환
            return year + '-' + month + '-' + day;
        });

        return formattedString;
    };

    const replace_date_blog = (date) => {
        // 날짜 문자열을 공백과 마침표로 분할
        var dateParts = date.split(/[.\s]+/);

        // 년, 월, 일 부분 추출
        var year = parseInt(dateParts[0], 10);
        var month = parseInt(dateParts[1], 10);
        var day = parseInt(dateParts[2], 10);

        // 날짜를 형식화하기 위해 Date 객체 생성
        var formattedDate = new Date(year, month - 1, day); // 월은 0부터 시작하므로 1을 빼줍니다.

        // 날짜를 "YYYY-MM-DD" 형식으로 변환
        var formattedDateString = formattedDate.toISOString().slice(0, 10);

        return formattedDateString;
    };

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
                    <h2>
                        <span></span>
                        미디어 관리
                    </h2>
                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <h4 class="view-title">
                        <c:choose>
                            <c:when test="${method eq 'regist'}">
                                <c:choose>
                                    <c:when test="${mediaType eq 'Video'}">
                                        미디어 관리 > 동영상 > 등록하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'News'}">
                                        미디어 관리 > 뉴스 > 등록하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'Blog'}">
                                        미디어 관리 > 블로그 > 등록하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'Game'}">
                                        미디어 관리 > 경기영상 > 등록하기
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${mediaType eq 'Video'}">
                                        미디어 관리 > 동영상 > 수정하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'News'}">
                                        미디어 관리 > 뉴스 > 수정하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'Blog'}">
                                        미디어 관리 > 블로그 > 수정하기
                                    </c:when>
                                    <c:when test="${mediaType eq 'Game'}">
                                        미디어 관리 > 경기영상 > 수정하기
                                    </c:when>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </h4>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">
                        </colgroup>
                        <tbody>
                            <c:choose>
                                <c:when test="${mediaType eq 'Video'}">
                                    <tr>
                                        <th class="tl">크롤링</th>
                                        <td class="tl" style="width: 90%;"><input type="text" name="url" placeholder="" id="craw_url" style="width: 90%;" >
                                            <a class="btn-large default btn-pop" onclick="craw_youtube()">클롤링 하기</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">영상 타입</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}" <c:if test="${mediaInfo.sub_type eq data.code_value}">selected</c:if>>${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}">${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">활성/비활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${mediaInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${mediaInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">메인 비활성/활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${mediaInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${mediaInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0"><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">제목</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="title" placeholder="" value="${mediaInfo.title}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="title" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value="${mediaInfo.url_link}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="imgTr">
                                        <th class="tl">이미지 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value="${mediaInfo.img_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="refTr">
                                        <th class="tl">참고 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="refUrl" placeholder="" value="${mediaInfo.ref_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="refUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="contentTr">
                                        <th class="tl">내용</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <textarea type="text" name="content">${mediaInfo.content}</textarea>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <textarea type="text" name="content"></textarea>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">업로드 일자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="date" name="submitDate" placeholder="" value="${fn:substring(mediaInfo.submit_date, 0, 10)}">
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="date" name="submitDate" placeholder="" value="">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">작성자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}" <c:if test="${mediaInfo.creator_id eq data.creator_id}">selected</c:if>>${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}">${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">이미지 링크 노출</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="showFlag" id="show-1" value="0" <c:if test="${mediaInfo.show_flag eq '0'}">checked</c:if>><label for="show-1">노출 안함</label>
                                                    <input type="radio" name="showFlag" id="show-2" value="1" <c:if test="${mediaInfo.show_flag eq '1'}">checked</c:if>><label for="show-2">노출</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="showFlag" id="show-1" value="0" checked><label for="show-1">노출 안함</label>
                                                    <input type="radio" name="showFlag" id="show-2" value="1"><label for="show-2">노출</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                                <c:when test="${mediaType eq 'News'}">
                                    <tr>
                                        <th class="tl">크롤링</th>
                                        <td class="tl" style="width: 90%;"><input type="text" name="url" placeholder="" id="craw_url_news" style="width: 90%;" >
                                            <a class="btn-large default btn-pop" onclick="craw_news()">클롤링 하기</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">뉴스 타입</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}" <c:if test="${mediaInfo.sub_type eq data.code_value}">selected</c:if>>${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}">${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">활성/비활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${mediaInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${mediaInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">메인 비활성/활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${mediaInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${mediaInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0"><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">제목</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="title" placeholder="" value="${fn:replace(mediaInfo.title, '\"', '&quot;')}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="title" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value="${mediaInfo.url_link}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="uageTr">
                                        <th class="tl">연령</th>
                                        <td>
                                            <select id="uage" style="">
                                                <c:choose>
                                                    <c:when test="${method eq 'modify'}">
                                                        <c:forEach var="result" items="${uageList}" varStatus="status">
                                                            <option value="${result.uage}" <c:if test="${mediaInfo.uage eq result.uage}">selected</c:if>>${result.uage}</option>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="result" items="${uageList}" varStatus="status">
                                                            <option value="${result.uage}">${result.uage}</option>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">이미지 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value="${mediaInfo.img_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">출처</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="source" placeholder="" value="${mediaInfo.source}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="source" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">
                                            기사 등록일
                                        </th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="date" name="submitDate" placeholder="" value="${fn:substring(mediaInfo.submit_date,0 ,10)}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="date" name="submitDate" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">요약</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <textarea type="text" name="summary">${mediaInfo.summary}</textarea>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <textarea type="text" name="summary"></textarea>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">내용</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <textarea type="text" name="content">${mediaInfo.content}</textarea>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <textarea type="text" name="content"></textarea>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="cupTr">
                                        <th class="tl">대회</th>
                                        <td class="tl">
                                            <a class="btn-large default btn-pop" data-id="pop-competition">대회 등록하기</a>
                                            <div id="addedCupMatch">

                                            </div>
                                        </td>
                                    </tr>
                                    <tr id="leagueTr">
                                        <th class="tl">리그</th>
                                        <td class="tl">
                                            <a class="btn-large default btn-pop" data-id="pop-league">리그 등록하기</a>
                                            <div id="addedLeagueMatch">

                                            </div>
                                        </td>
                                    </tr>

                                    <tr id="teamTr">
                                        <th class="tl">학원/클럽 등록하기</th>
                                        <td class="tl">
                                            <a class="btn-large default btn-pop" data-id="pop-club">학원/클럽 등록하기</a>
                                            <div id="addedTeam"></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">작성자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}" <c:if test="${mediaInfo.creator_id eq data.creator_id}">selected</c:if>>${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}">${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                                <c:when test="${mediaType eq 'Blog'}">
                                    <tr>
                                        <th class="tl">크롤링</th>
                                        <td class="tl" style="width: 90%;"><input type="text" name="url" placeholder="" id="craw_url_blog" style="width: 90%;" >
                                            <a class="btn-large default btn-pop" onclick="craw_blog()">클롤링 하기</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">블로그 타입</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}" <c:if test="${mediaInfo.sub_type eq data.code_value}">selected</c:if>>${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}">${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">활성/비활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${mediaInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${mediaInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">메인 비활성/활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${mediaInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${mediaInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0"><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">제목</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="title" placeholder="" value="${mediaInfo.title}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="title" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value="${mediaInfo.url_link}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">이미지 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value="${mediaInfo.img_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">출처</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="source" placeholder="" value="${mediaInfo.source}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="source" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">
                                                블로그 등록일
                                        </th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="date" name="submitDate" placeholder="" value="${fn:substring(mediaInfo.submit_date, 0, 10)}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="date" name="submitDate" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">요약</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><textarea type="text" name="summary">${mediaInfo.summary}</textarea></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><textarea type="text" name="summary"></textarea></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">내용</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <textarea type="text" name="content">${mediaInfo.content}</textarea>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <textarea type="text" name="content"></textarea>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">작성자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}" <c:if test="${mediaInfo.creator_id eq data.creator_id}">selected</c:if>>${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}">${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                                <c:when test="${mediaType eq 'Interview'}">
                                    <tr>
                                        <th class="tl">활성/비활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${mediaInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${mediaInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">메인 비활성/활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${mediaInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${mediaInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0"><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">제목</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="title" placeholder="" value="${mediaInfo.title}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="title" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value="${mediaInfo.url_link}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                                <c:when test="${mediaType eq 'Game'}">
                                    <tr>
                                        <th class="tl">크롤링</th>
                                        <td class="tl" style="width: 90%;"><input type="text" name="url" placeholder="" id="craw_url" style="width: 90%;" >
                                            <a class="btn-large default btn-pop" onclick="craw_youtube()">클롤링 하기</a>
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <th class="tl">영상 타입</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}" <c:if test="${mediaInfo.sub_type eq data.code_value}">selected</c:if>>${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="subType">
                                                        <c:forEach var="data" items="${menuList}" varStatus="status">
                                                            <option value="${data.code_value}">${data.category_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>--%>
                                    <tr>
                                        <th class="tl">활성/비활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${mediaInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${mediaInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                                    <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">메인 비활성/활성</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${mediaInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${mediaInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="mainFlag" id="ra2-1" value="0"><label for="ra2-1">비활성</label>
                                                    <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">제목</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="title" placeholder="" value="${mediaInfo.title}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="title" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value="${mediaInfo.url_link}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="urlLink" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="imgTr">
                                        <th class="tl">이미지 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value="${mediaInfo.img_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="imgUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="refTr">
                                        <th class="tl">참고 링크</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl"><input type="text" name="refUrl" placeholder="" value="${mediaInfo.ref_url}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl"><input type="text" name="refUrl" placeholder="" value=""></td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="contentTr">
                                        <th class="tl">내용</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <textarea type="text" name="content">${mediaInfo.content}</textarea>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <textarea type="text" name="content"></textarea>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="uageTr">
                                        <th class="tl">연령</th>
                                        <td>
                                            <select id="uage" style="">
                                                <c:choose>
                                                    <c:when test="${method eq 'modify'}">
                                                        <c:forEach var="result" items="${uageList}" varStatus="status">
                                                            <option value="${result.uage}" <c:if test="${mediaInfo.uage eq result.uage}">selected</c:if>>${result.uage}</option>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="result" items="${uageList}" varStatus="status">
                                                            <option value="${result.uage}">${result.uage}</option>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">업로드 일자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="date" name="submitDate" placeholder="" value="${fn:substring(mediaInfo.submit_date, 0, 10)}">
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="date" name="submitDate" placeholder="" value="">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="typeTr">
                                        <th class="tl">구분</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="ra2" id="ra3-1" value="1" <c:if test="${mediaInfo.type eq '1'}">checked</c:if>><label for="ra3-1">다시보기</label>
                                                    <input type="radio" name="ra2" id="ra3-2" value="0" <c:if test="${mediaInfo.type eq '0'}">checked</c:if>><label for="ra3-2">하이라이트</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="ra2" id="ra3-1" value="1"><label for="ra3-1">다시보기</label>
                                                    <input type="radio" name="ra2" id="ra3-2" value="0"><label for="ra3-2">하이라이트</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr id="cupTr">
                                        <th class="tl">대회</th>
                                        <td class="tl">
                                            <a class="btn-large default btn-pop" data-id="pop-competition">대회 등록하기</a>
                                            <div id="addedCupMatch">

                                            </div>
                                        </td>
                                    </tr>
                                    <tr id="leagueTr">
                                        <th class="tl">리그</th>
                                        <td class="tl">
                                            <a class="btn-large default btn-pop" data-id="pop-league">리그 등록하기</a>
                                            <div id="addedLeagueMatch">

                                            </div>
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <th class="tl">작성자</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}" <c:if test="${mediaInfo.creator_id eq data.creator_id}">selected</c:if>>${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <select id="creator">
                                                        <option value="">선택안함</option>
                                                        <c:forEach var="data" items="${creatorList}" varStatus="status">
                                                            <option value="${data.creator_id}">${data.creator_name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>--%>
                                    <tr>
                                        <th class="tl">이미지 링크 노출</th>
                                        <c:choose>
                                            <c:when test="${method eq 'modify'}">
                                                <td class="tl">
                                                    <input type="radio" name="showFlag" id="show-1" value="0" <c:if test="${mediaInfo.show_flag eq '0'}">checked</c:if>><label for="show-1">노출 안함</label>
                                                    <input type="radio" name="showFlag" id="show-2" value="1" <c:if test="${mediaInfo.show_flag eq '1'}">checked</c:if>><label for="show-2">노출</label>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="radio" name="showFlag" id="show-1" value="0" checked><label for="show-1">노출 안함</label>
                                                    <input type="radio" name="showFlag" id="show-2" value="1"><label for="show-2">노출</label>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                            </c:choose>
                        </tbody>
                    </table>
                </div><br>
                <div class="tr w100">
                    <a class="btn-large gray-o" onclick="location.href=''">취소 하기</a>
                    <c:choose>
                        <c:when test="${method eq 'regist'}">
                            <a class="btn-large default" onclick="saveMedia('save')">저장 하기</a>
                        </c:when>
                        <c:when test="${method eq 'modify'}">
                            <a class="btn-large default" onclick="saveMedia('modify')">수정 하기</a>
                        </c:when>
                    </c:choose>

                </div>
            </div>
        </div>

    </div>

    <!--팝업-->

    <div class="pop" id="pop-competition">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    대회 선택하기
                    <a class="close btn-close-pop" onclick="closeCupPop()"></a>
                </div>
                <div class="head p10 grid4">
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
                    <select id="ageGroup" style="width:100px;">
                        <c:forEach var="result" items="${uageList}" varStatus="status">
                            <option value="${result.uage }">${result.uage}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="sCupName" placeholder="대회명을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchCup(); return;}">
                    <a class="btn-large default" onclick="fnSearchCup()" >검색</a>
                </div>
                <c:choose>
                    <c:when test="${mediaType eq 'Game'}">
                        <div class="body grid2" style="padding:15px 10px;">
                            <div>
                                <p class="fs14 mt_10">대회</p>
                                <table cellspacing="0" class="update mt_10">
                                    <colgroup>
                                        <col width="*">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>대회명</th>
                                        <th>대회일정</th>
                                    </tr>
                                    </thead>
                                    <tbody id="cupInfoList">
                                    <tr>
                                        <td colspan="2">
                                            대회를 검색 해주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div>
                                <div id="selectMatchType">
                                </div>

                                <div class="mt_10" id="groupDiv">
                                </div>
                                <table cellspacing="0" class="update mt_10">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="5%">
                                        <col width="10%">
                                        <col width="15%">
                                        <col width="5%">
                                        <col width="5%">
                                        <col width="5%">
                                        <col width="5%">
                                        <col width="15%">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th rowspan="2">
                                            선택
                                        </th>
                                        <th rowspan="2">
                                            경기일
                                        </th>
                                        <th rowspan="2">
                                            경기 시간
                                        </th>
                                        <th rowspan="2">
                                            경기 장소
                                        </th>
                                        <th colspan="3">
                                            홈팀
                                        </th>
                                        <th colspan="3">
                                            어웨이팀
                                        </th>
                                    </tr>
                                    <tr>
                                        <th>홈팀</th>
                                        <th>점수</th>
                                        <th>PK</th>
                                        <th>PK</th>
                                        <th>점수</th>
                                        <th>어웨이팀</th>
                                    </tr>
                                    </thead>
                                    <tbody id="cupDetailList">
                                    <tr>
                                        <td colspan="10">
                                            대회 검색후 선택해 주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="mt_10 w100 tr">
                                    <a class="btn-large default" onclick="addCupMatch()">추가하기</a>
                                </div>
                            </div>
                            <div id="selectDiv">

                            </div>
                        </div>
                    </c:when>
                    <c:when test="${mediaType eq 'News'}">
                        <div class="body" style="padding:15px 20px;">

                            <div class="tl">
                                대회
                                <table cellspacing="0" class="update view mt_10">
                                    <colgroup>
                                        <col width="5%">
                                        <col width="*">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>선택</th>
                                        <th>대회명</th>
                                        <th>대회일정</th>
                                    </tr>
                                    </thead>
                                    <tbody id="cupInfoList">
                                    <tr>
                                        <td colspan="3">
                                            대회를 검색 해주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="mt_10 w100 tr">
                                    <a class="btn-large default" onclick="addCupMatch()">추가하기</a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                </c:choose>


            </div>
        </div>
    </div>
    <div class="pop" id="pop-league">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    리그 선택하기
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10 grid4">
                    <select name="sYear" id="leagueSYear">
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
                    <select id="leagueAgeGroup" style="width:100px;">
                        <c:forEach var="result" items="${uageList}" varStatus="status">
                            <option value="${result.uage }">${result.uage}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="sLeagueName" placeholder="리그명을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchLeague(); return;}">
                    <a class="btn-large default" onclick="fnSearchLeague()">검색</a>
                </div>
                <c:choose>
                    <c:when test="${mediaType eq 'Game'}">
                        <div class="body grid2" style="padding:15px 20px;">
                            <div>
                                <p class="fs14 mt_10">리그</p>
                                <table cellspacing="0" class="update view mt_10">
                                    <colgroup>
                                        <col width="*">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>리그명</th>
                                        <th>리그일정</th>
                                    </tr>
                                    </thead>
                                    <tbody id="leagueInfoList">
                                    <tr>
                                        <td colspan="2">
                                            리그를 검색 해주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div>
                                <p class="fs14 mt_10">리그 경기</p>
                                <div class="mt_10" id="leagueGroupDiv">
                                </div>
                                <table cellspacing="0" class="update mt_10">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="5%">
                                        <col width="10%">
                                        <col width="15%">
                                        <col width="5%">
                                        <col width="5%">
                                        <col width="15%">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th rowspan="2">
                                            선택
                                        </th>
                                        <th rowspan="2">
                                            경기일
                                        </th>
                                        <th rowspan="2">
                                            경기 시간
                                        </th>
                                        <th rowspan="2">
                                            경기 장소
                                        </th>
                                        <th colspan="2">
                                            홈팀
                                        </th>
                                        <th colspan="2">
                                            어웨이팀
                                        </th>
                                    </tr>
                                    <tr>
                                        <th>홈팀</th>
                                        <th>점수</th>

                                        <th>점수</th>
                                        <th>어웨이팀</th>
                                    </tr>
                                    </thead>
                                    <tbody id="leagueDetailList">
                                    <tr>
                                        <td colspan="10">
                                            대회 검색후 선택해 주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="mt_10 w100 tr">
                                    <a class="btn-large default" onclick="addLeagueMatch()">추가하기</a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${mediaType eq 'News'}">
                        <div class="body" style="padding:15px 20px;">

                            <div class="tl">
                                리그
                                <table cellspacing="0" class="update view mt_10">
                                    <colgroup>
                                        <col width="5%">
                                        <col width="*">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th>선택</th>
                                        <th>리그명</th>
                                        <th>리그일정</th>
                                    </tr>
                                    </thead>
                                    <tbody id="leagueInfoList">
                                    <tr>
                                        <td colspan="3">
                                            리그를 검색 해주세요.
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="mt_10 w100 tr">
                                    <a class="btn-large default" onclick="addLeagueMatch()">추가하기</a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                </c:choose>

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
                    <select id="selectTeamType">
                        <option value="">전체</option>
                        <option value="0">학원</option>
                        <option value="1">클럽</option>
                        <option value="2">유스</option>
                    </select>
                    <select id="teamAgeGroup" style="width:100px;">
                        <c:forEach var="result" items="${uageList}" varStatus="status">
                            <option value="${result.uage }">${result.uage}</option>
                        </c:forEach>
                    </select>
                    <input type="text" id="sTeamName" placeholder="팀명을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchTeam(); return;}">
                    <a class="btn-large default" onclick="fnSearchTeam()">검색</a>
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
                                    <th>활성여부</th>
                                </tr>
                            </thead>
                            <tbody id="teamDetailList">
                                <tr>
                                    <td colspan="9">
                                        팀을 검색 해주세요.
                                    </td>
                                    <%--<td><input type="checkbox" name="ch2" id="ch2-1"><label for="ch2-1"></label></td>
                                    <td>경기</td>
                                    <td><img src="../resources/img/logo/none.png" class="logo"></td>
                                    <td>U17</td>
                                    <td><span class="label red">유스</span></td>

                                    <td>포항제철고</td>
                                    <td>포항제철고등학교</td>
                                    <td class="tl">경상북도 포항시 남구 효곡동</td>
                                    <td>학원</td>
                                    <td>활성</td>

                                    </td>--%>
                                </tr>

                            </tbody>
                        </table>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addTeam()">추가하기</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div id="selectedCupMatch">
    </div>
    <div id="selectedLeagueMatch">

    </div>
    <div id="selectedTeam">

    </div>

</body>


</html>