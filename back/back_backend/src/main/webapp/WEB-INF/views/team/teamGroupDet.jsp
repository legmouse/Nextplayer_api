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

    const fnGoModify = (val) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/savePlayer');
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'modify' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));

        $(newForm).appendTo('body').submit();
    }

    const fnDeletePlayer = (val) => {

        const confirmMsg = confirm('정말 삭제 하시겠습니까?');

        if (confirmMsg) {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/delete_player');
            newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'delete' }));
            newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: val }));
            $(newForm).appendTo('body').submit();
        }

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
                                " data-teamName='" + res.data[i].team_name + "'" +
                                " data-teamType='" + res.data[i].team_type + "'" +
                                " data-emblem='" + res.data[i].emblem + "'" +
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

    const addTeam = (val) => {

        const chkMatch = $("input[type=checkbox]:checked");

        let str = "";
        let nameStr = "";

        let addStr = "";

        $(chkMatch).each(function(i) {
            console.log($(this))
            str += "<input type='text' " +
                "name='teamData'" +
                " data-id='"+$(this).attr('data-ageGroup') + $(this).val() +
                "' data-ageGroup='"+ $(this).attr('data-ageGroup') +
                "' data-cupType='" + $(this).attr('data-ageGroup') +
                "' value='" + $(this).val() + "' >";

            addStr += "<tr>" +
                        "<td><img src='" + $(this).attr('data-emblem') + "' class='logo'></td>" +
                        "<td>" + $(this).attr('data-teamName') + "</td>" +
                        "<td>" + $(this).attr('data-nickName') + "</td>" +
                        "<td>" + $(this).attr('data-ageGroup') + "</td>";
            if ($(this).attr('data-teamType') == '0') {
            addStr +=   "<td>학원</td>";
            } else if ($(this).attr('data-teamType') == '1') {
            addStr +=   "<td>클럽</td>";
            } else if ($(this).attr('data-teamType') == '2') {
            addStr +=   "<td>유스</td>";
            }
            addStr += '<td><a class="btn-large gray-o">삭제하기</a></td>';

        });

        $("#selectedTeam").append(str);

        let confirmMsg = confirm("선택한 팀을 추가 하시겠습니까?");

        if (confirmMsg) {

            let addParam = {
                teamGroupId: '${groupDetail.team_group_id}',
                method: 'add',
            }

            let param = {
                teamData: []
            };

            const teamInputs = $("input[name='teamData']");

            teamInputs.each(function() {
                const ageGroup = $(this).attr('data-agegroup');
                const value = $(this).val();
                const newData = {
                    ageGroup: ageGroup,
                    childId: value,
                }
                param.teamData.push(JSON.stringify(newData));
            });
            console.log('param' , param);
            addParam.teamData = (JSON.stringify(param));
            console.log('addParam' , addParam);
            console.log('team_group_id' , '${groupDetail.team_group_id}');
            $.ajax({
                type: 'POST',
                url: '/add_teamGroup',
                data: addParam,
                success: function(res) {
                    console.log(res);
                    if (res.state === 'SUCCESS') {
                        alert('등록이 완료 되었습니다.');
                        location.reload();
                    } else {
                        alert('등록에 실패했습니다.');
                        location.reload();
                    }
                }
            });

        }

        //$("#childTbody:last").append(addStr);

        closeTeamPop();

    }

    const closeTeamPop = () => {

        $(".pop").fadeOut();

        $("#teamDetailList").empty();

        const listStr = '<td colspan="19">팀을 검색 해주세요.</td>';

        $("#teamDetailList").append(listStr);

    }

    const fnDeleteTeamGroup = (val) => {
        let confirmMsg = confirm('해당 팀을 삭제 하시겠습니까?');

        if (confirmMsg) {
            let param = {
                teamId : val,
                method : 'delete'
            }

            $.ajax({
                type: 'POST',
                url: '/add_teamGroup',
                data: param,
                success: function(res) {
                    console.log(res);
                    if (res.state === 'SUCCESS') {
                        alert('삭제 완료 되었습니다.');
                        location.reload();
                    } else {
                        alert('삭제에 실패했습니다.');
                        location.reload();
                    }
                }
            });

        }
    }

    const gotoList = () => {
        document.tmdfrm.submit();
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
                    <h2><span></span>팀 그룹 관리</h2>

                </div>
            </div>
            <div class="round body">

                <div class="body-head">

                    <h4 class="view-title">팀 그룹 관리 > 등록하기 </h4>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">

                        </colgroup>
                        <tbody>
                        <c:if test="${!empty groupDetail}">
                            <tr>
                                <th class="tl">이름<em>*</em></th>
                                <td class="tl">
                                        ${groupDetail.group_name}
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">활성/비활성</th>
                                <td class="tl">
                                    <c:choose>
                                        <c:when test="${groupDetail.use_flag eq '0'}">
                                            활성
                                        </c:when>
                                        <c:when test="${groupDetail.use_flag eq '1'}">
                                            비활성
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                    <br /><br />
                    <div class="w100 tr mt_10">
                        <a class="btn-large default btn-pop" data-id="pop-teamGroup">추가하기</a>
                    </div>
                    <br /><br />
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="20%">
                            <col width ="*">
                            <col width ="*">
                            <col width ="*">
                            <col width ="*">
                            <col width ="*">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>엠블럼</th>
                            <th>팀명</th>
                            <th>명칭</th>
                            <th>연령</th>
                            <th>구분</th>
                            <th>관리</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                            <c:choose>
                                <c:when test="${empty groupDetail.teams}">
                                    <tr>
                                        <td colspan="6">등록된 내역이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${groupDetail.teams}" varStatus="status">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                    <c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${result.team_name}
                                            </td>
                                            <td>
                                                ${result.nick_name}
                                            </td>
                                            <td>
                                                ${result.uage}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.team_type eq '0'}"><span class="">학원</span></c:when>
                                                    <c:when test="${result.team_type eq '1'}"><span class="">클럽</span></c:when>
                                                    <c:when test="${result.team_type eq '2'}"><span class="">유스</span></c:when> <%-- 유스일경우?? --%>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a class="btn-large gray-o" onclick="fnDeleteTeamGroup('${result.team_id}')">삭제하기</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div><br>
                <div class="w100 tr mt_10">
                    <a class="btn-large gray-o" onclick="gotoList()">목록으로</a>
                </div>

            </div>
        </div>
    </div>

    <div class="pop" id="pop-teamGroup">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    관련팀 추가
                    <a class="close btn-close-pop" onclick="closeTeamPop()"></a>
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
                    <a class="btn-large default" onclick="fnSearchTeam()" >검색</a>
                </div>
                <div class="body grid" style="padding:15px 10px;">
                    <div>
                        <div id="selectMatchType">
                        </div>

                        <div class="mt_10" id="groupDiv">
                        </div>
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
                                <td colspan="10">
                                    팀 검색후 선택해 주세요.
                                </td>
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

    <div id="selectedTeam">
    </div>

</body>
<form name="tmdfrm" id="tmdfrm" method="post"  action="teamGroup">
    <input name="cp" type="hidden" value="${cp}">
</form>
</html>