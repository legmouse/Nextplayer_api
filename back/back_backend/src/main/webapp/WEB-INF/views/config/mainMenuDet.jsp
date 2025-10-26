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
<link rel="stylesheet" href="resources/css/content.css?ver=2">

<script type="text/javascript">

$(document).ready(function() {
    /*$('input[type="checkbox"]').on('click', function() {
        $('input[type="checkbox"]').prop('checked', false);
        $(this).prop('checked', true);
    });*/

    const key = '${key}';

    detail = null;
    order = 1;

    <c:if test="${!empty mediaList}">
        let mediaListStr = "";
        order = Number('${fn:length(mediaList)}') + 1;
        <c:if test="${key == 'mediaVideo' || key == 'mediaBlog' }">
            <c:forEach var="item" items="${mediaList}" varStatus="status">
                    mediaListStr += "<tr id='tr${item.media_id}' class='categoryInfo'>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=      "${fn:replace(item.title, '\"', '&quot;')}"
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=      '${fn:substring(item.reg_date,0 ,10)}';
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=      '${fn:substring(item.submit_date, 0, 10)}';
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=     '${item.creator_name}';
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    <c:forEach var="item2" items="${menuList}" varStatus="status2">
                        <c:if test="${item.sub_type eq item2.code_value}">
                    mediaListStr +=  "${item2.category_name}";
                        </c:if>
                    </c:forEach>
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=      '${item.view_cnt}'
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "<td>";
                    mediaListStr +=      "<input type='hidden' data-order='ord${item.media_id}' class='detail_order' value='${status.index + 1}'>";
                    mediaListStr +=      "<input class='mediaId' type='hidden' id='input${item.media_id}' name='mediaId' value='${item.media_id}'>";
                    mediaListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
                    mediaListStr +=      "<button class='btn-large gray-o' data-id='${item.media_id}' onclick='removeTab(this)'>삭제하기</button>";
                    mediaListStr +=  "</td>";
                    mediaListStr +=  "</tr>";

            </c:forEach>
        </c:if>
        <c:if test="${key == 'mediaNews'}">
            <c:forEach var="item" items="${mediaList}" varStatus="status">
                mediaListStr += "<tr id='tr${item.media_id}' class='categoryInfo'>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "${item.source}";
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "${fn:replace(item.title, '\"', '&quot;')}"
                mediaListStr +=  "</td>";
                <c:choose>
                    <c:when test="${!empty item.cupInfoList}">
                mediaListStr +=  "<td>";
                    <c:forEach var="cupInfo" items="${item.cupInfoList}">
                mediaListStr +=      "${cupInfo.cup_name}";
                    </c:forEach>
                mediaListStr +=  "</td>";
                    </c:when>
                    <c:otherwise>
                mediaListStr +=  "<td>";
                mediaListStr +=      "-";
                mediaListStr +=  "</td>";
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${!empty item.leagueInfoList}">
                mediaListStr +=  "<td>";
                    <c:forEach var="leagueInfo" items="${item.leagueInfoList}">
                mediaListStr +=      "${leagueInfo.league_name}";
                    </c:forEach>
                mediaListStr +=  "</td>";
                    </c:when>
                    <c:otherwise>
                mediaListStr +=  "<td>";
                mediaListStr +=      "-";
                mediaListStr +=  "</td>";
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${!empty item.teamList}">
                mediaListStr +=  "<td>";
                    <c:forEach var="teamInfo" items="${item.teamList}" varStatus="status2">
                mediaListStr +=      "${teamInfo.nick_name}"
                <c:if test="${status2.last eq false}">
                mediaListStr +=       ", ";
                </c:if>
                    </c:forEach>
                mediaListStr +=  "</td>";
                    </c:when>
                    <c:otherwise>
                mediaListStr +=  "<td>";
                mediaListStr +=      "-";
                mediaListStr +=  "</td>";
                    </c:otherwise>
                </c:choose>
                mediaListStr +=  "<td>";
                mediaListStr +=      "${item.uage}";
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "${fn:substring(item.reg_date, 0, 10)}";
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "${fn:substring(item.submit_date, 0, 10)}";
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                <c:forEach var="item2" items="${menuList}" varStatus="status2">
                <c:if test="${item.sub_type eq item2.code_value}">
                mediaListStr +=  "${item2.category_name}";
                </c:if>
                </c:forEach>
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "${item.view_cnt}";
                mediaListStr +=  "</td>";
                mediaListStr +=  "<td>";
                mediaListStr +=      "<input type='hidden' data-order='ord${item.media_id}' class='detail_order' value='${status.index + 1}'>";
                mediaListStr +=      "<input class='mediaId' type='hidden' id='input${item.media_id}' name='mediaId' value='${item.media_id}'>";
                mediaListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
                mediaListStr +=      "<button class='btn-large gray-o' data-id='${item.media_id}' onclick='removeTab(this)'>삭제하기</button>";
                mediaListStr +=  "</td>";
                mediaListStr += "</tr>";
            </c:forEach>
        </c:if>
        <c:if test="${key == 'mediaGame'}">
            <c:forEach var="item" items="${mediaList}" varStatus="status">
            mediaListStr += "<tr id='tr${item.media_id}' class='categoryInfo'>";
            mediaListStr +=  "<td>";
            mediaListStr +=      "${item.uage}";
            mediaListStr +=  "</td>";
            mediaListStr +=  "<td>";
            mediaListStr +=      "${item.title}";
            mediaListStr +=  "</td>";
                <c:choose>
                    <c:when test="${!empty item.cupInfoList}">
            mediaListStr +=  "<td>";
                    <c:forEach var="cupInfo" items="${item.cupInfoList}">
            mediaListStr +=      "${cupInfo.cup_name}";
                    </c:forEach>
            mediaListStr +=  "</td>";
                    </c:when>
                <c:otherwise>
            mediaListStr +=  "<td>";
            mediaListStr +=      "-";
            mediaListStr +=  "</td>";
                </c:otherwise>
            </c:choose>
            <c:choose>
            <c:when test="${!empty item.leagueInfoList}">
            mediaListStr +=  "<td>";
            <c:forEach var="leagueInfo" items="${item.leagueInfoList}">
            mediaListStr +=      "${leagueInfo.league_name}";
            </c:forEach>
            mediaListStr +=  "</td>";
            </c:when>
            <c:otherwise>
            mediaListStr +=  "<td>";
            mediaListStr +=      "-";
            mediaListStr +=  "</td>";
            </c:otherwise>
            </c:choose>
                <c:choose>
                    <c:when test="${!empty item.teamList}">
            mediaListStr +=  "<td>";
                    <c:forEach var="teamInfo" items="${item.teamList}" varStatus="status2">
            mediaListStr +=      "${teamInfo.nick_name}"
                    <c:if test="${status2.last eq false}">
            mediaListStr +=       ", ";
                    </c:if>
                    </c:forEach>
            mediaListStr +=  "</td>";
                    </c:when>
                    <c:otherwise>
            mediaListStr +=  "<td>";
            mediaListStr +=      "-";
            mediaListStr +=  "</td>";
                    </c:otherwise>
                </c:choose>
            mediaListStr +=  "<td>";
            mediaListStr +=      "${fn:substring(item.reg_date, 0, 10)}";
            mediaListStr +=  "</td>";
            mediaListStr +=  "<td>";
            mediaListStr +=      "${item.view_cnt}";
            mediaListStr +=  "</td>";
            mediaListStr +=  "<td>";
            mediaListStr +=      "<input type='hidden' data-order='ord${item.media_id}' class='detail_order' value='${status.index + 1}'>";
            mediaListStr +=      "<input class='mediaId' type='hidden' id='input${item.media_id}' name='mediaId' value='${item.media_id}'>";
            mediaListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
            mediaListStr +=      "<button class='btn-large gray-o' data-id='${item.media_id}' onclick='removeTab(this)'>삭제하기</button>";
            mediaListStr +=  "</td>";
            mediaListStr += "</tr>";
            </c:forEach>
        </c:if>
        $("#mediaTBody").append(mediaListStr);
    </c:if>

    <c:if test="${!empty rosterList}">
        let rosterListStr = "";
        order = Number('${fn:length(rosterList)}') + 1;
        <c:forEach var="item" items="${rosterList}" varStatus="status">
        rosterListStr += "<tr id='tr${item.roster_id}' class='categoryInfo'>";
        rosterListStr +=  "<td>";
        <c:if test="${item.roster_type eq '0'}">
        rosterListStr +=      "국가대표";
        </c:if>
        <c:if test="${item.roster_type eq '1'}">
        rosterListStr +=      "골든에이지";
        </c:if>
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "${item.uage}";
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "${item.title}";
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "${item.year}";
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "${item.cnt}";
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "${fn:substring(item.reg_date, 0, 10)}";
        rosterListStr +=  "</td>";
        rosterListStr +=  "<td>";
        rosterListStr +=      "<input type='hidden' data-order='ord${item.roster_id}' class='detail_order' value='${status.index + 1}'>";
        rosterListStr +=      "<input class='rosterId' type='hidden' id='input${item.roster_id}' name='rosterId' value='${item.roster_id}'>";
        rosterListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
        rosterListStr +=      "<button class='btn-large gray-o' data-id='${item.roster_id}' onclick='removeTab(this)'>삭제하기</button>";
        rosterListStr +=  "</td>";
        rosterListStr += "</tr>";
        </c:forEach>
        $("#rosterTBody").append(rosterListStr);
    </c:if>

    <c:if test="${!empty bannerList}">
        let bannerListStr = "";
        order = Number('${fn:length(bannerList)}') + 1;
        <c:forEach var="item" items="${bannerList}" varStatus="status">
        bannerListStr += "<tr id='tr${item.banner_id}' class='categoryInfo'>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "<div class='w30 fl'>";
        bannerListStr +=        "<input type='date' name='sdate' class='sdate' value='${item.show_sdate}' autocomplete='off' >";
        bannerListStr +=      "</div>";
        bannerListStr +=      "<div class='w5 fl tc' style='height:28px; line-height:28px;'>-</div>";
        bannerListStr +=      "<div class='w30 fl'>";
        bannerListStr +=        "<input type='date' name='edate' class='edate' value='${item.show_edate}' autocomplete='off' >";
        bannerListStr +=      "</div>";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "${item.title}";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "${item.url_link}";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        <c:if test="${!empty item.files}">
            <c:forEach var="file" items="${item.files}" varStatus="status2">
        bannerListStr +=      "<img style='width: 300px; height: 100px;' src='/NP${file.file_save_path}'>";
            </c:forEach>
        </c:if>
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "<input type='hidden' data-order='ord${item.roster_id}' class='detail_order' value='${status.index + 1}'>";
        bannerListStr +=      "<input class='bannerId' type='hidden' id='input${item.banner_id}' name='bannerId' value='${item.banner_id}'>";
        bannerListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
        bannerListStr +=      "<button class='btn-large gray-o' data-id='${item.banner_id}' onclick='removeTab(this)'>삭제하기</button>";
        bannerListStr +=  "</td>";
        bannerListStr += "</tr>";
        </c:forEach>

        $("#bannerTBody").append(bannerListStr);
    </c:if>

    <c:if test="${!empty columnList}">
        let columnListStr = "";
        order = Number('${fn:length(columnList)}') + 1;
        <c:forEach var="item" items="${columnList}" varStatus="status">
            columnListStr += "<tr id='tr${item.education_column_id}' class='categoryInfo'>";
            columnListStr +=  "<td>";
            columnListStr +=      "${item.title}";
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            columnListStr +=      "${item.reg_date}";
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            columnListStr +=      "${item.submit_date}";
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            columnListStr +=      "${item.submit_writer}";
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            <c:forEach var="item2" items="${menuList}" varStatus="status2">
                <c:if test="${item.column_type eq item2.code_value}">
            columnListStr +=  "${item2.category_name}";
                </c:if>
            </c:forEach>
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            columnListStr +=      "${item.view_cnt}";
            columnListStr +=  "</td>";
            columnListStr +=  "<td>";
            columnListStr +=      "<input type='hidden' data-order='ord${item.education_column_id}' class='detail_order' value='${status.index + 1}'>";
            columnListStr +=      "<input class='educationColumnId' type='hidden' id='input${item.education_column_id}' name='educationColumnId' value='${item.education_column_id}'>";
            columnListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
            columnListStr +=      "<button class='btn-large gray-o' data-id='${item.column_id}' onclick='removeTab(this)'>삭제하기</button>";
            columnListStr +=  "</td>";
            columnListStr += "</tr>";
        </c:forEach>

        $("#columnTBody").append(columnListStr);
    </c:if>

    $(document).on('click', '.categoryInfo', function() {
        var checkBox = $(this).find('.currentTr');
        $('.currentTr').prop("checked", false);
        $('.categoryInfo').css("background-color", "");
        checkBox.prop("checked", true);
        var el =  $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        $tr.css("background-color", "rgb(128, 183, 255)")

    });

    $(document).on('click', ".btn_up", function() {

        var el = $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        var index = $tr.find('.detail_order').val();
        if (!(index == 1)) {
            $tr.prev().before($tr);
            $tr.find('.detail_order').val(index-1);
            $tr.next().find('.detail_order').val(index);
        }
    });

    $(document).on('click', ".btn_down", function() {

        //var last_order = detail ? detail[detail.length - 1].detailOrder : order;
        var last_order = order;
        var el = $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        var current_order = $tr.index() + 1;
        if (current_order != last_order-1) {
            $tr.next().after($tr);
            var pre_index = $tr.prev().find('.detail_order').val();
            var index = $tr.index() + 1;
            $tr.find('.detail_order').val(index);
            $tr.prev().find('.detail_order').val(pre_index - 1);
        }

    });

});

const fnSaveShowPointFlag = () => {
    let row = [];

    const key = '${key}';

    let matchId = $('input[name=matchId]');
    let matchCategory = $('input[name=matchCategory]');
    let ageGroup = $('input[name=ageGroup]');
    let scoreShowFlag = $('input:radio[class="scoreShowFlag"]:checked');

    for(let i = 0; i < matchId.length; i++) {
        row[i] = {
            "matchId": matchId[i].value,
            "matchCategory": matchCategory[i].value,
            "ageGroup": ageGroup[i].value,
            "scoreShowFlag": scoreShowFlag[i].value,
            "type" : key == 'leagueMatch' ? 'league' : 'cup'
        };
    }

    const confirmMsg = confirm('저장 하시겠습니까?');

    if(confirmMsg) {
        $.ajax({
            url : '/save_show_point_flag',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(row),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}


const fnSaveShowFlagProgressCup = () => {
    let row = [];

    let cupId = $('input[name=cupId]');
    let ageGroup = $('input[name=ageGroup]');
    let showFlag = $('input:radio[class="showFlag"]:checked');

    for(let i = 0; i < cupId.length; i++) {
        row[i] = {
            "cupId": cupId[i].value,
            "ageGroup": ageGroup[i].value,
            "showFlag": showFlag[i].value,
        };
    }

    const confirmMsg = confirm('저장 하시겠습니까?');

    if(confirmMsg) {
        $.ajax({
            url : '/save_show_flag',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(row),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

const fnSaveShowFlagProgressLeague = () => {
    let row = [];

    let leagueId = $('input[name=leagueId]');
    let ageGroup = $('input[name=ageGroup]');
    let showFlag = $('input:radio[class="showFlag"]:checked');

    for(let i = 0; i < leagueId.length; i++) {
        row[i] = {
            "leagueId": leagueId[i].value,
            "ageGroup": ageGroup[i].value,
            "showFlag": showFlag[i].value,
        };
    }

    const confirmMsg = confirm('저장 하시겠습니까?');

    if(confirmMsg) {
        $.ajax({
            url : '/save_league_show_flag',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(row),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

const fnSaveLiveShowFlag = () => {
    let row = [];

    let matchId = $('input[name=matchId]');
    let matchCategory = $('input[name=matchCategory]');
    let ageGroup = $('input[name=ageGroup]');
    let liveShowFlag = $('input:radio[class="liveShowFlag"]:checked');

    for(let i = 0; i < matchId.length; i++) {
        row[i] = {
            "matchId": matchId[i].value,
            "matchCategory": matchCategory[i].value,
            "ageGroup": ageGroup[i].value,
            "liveShowFlag": liveShowFlag[i].value,
            "type" : 'cup'
        };
    }

    const confirmMsg = confirm('저장 하시겠습니까?');

    if(confirmMsg) {
        $.ajax({
            url : '/save_live_show_flag',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(row),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

const fnSaveMainMedia = (key) => {
    let row = [];
    let mediaId = $('.mediaId');
    let detailOrder = $('.detail_order');

    for (let i = 0; i < mediaId.length; i++) {
        row[i] = {
            mediaId : mediaId[i].value,
            mediaOrder : detailOrder[i].value
        }
    }

    const confirmMsg = confirm('저장 하시겠습니까?');

    let param = {
        mediaList: JSON.stringify(row),
        mediaType: key == 'mediaVideo' ? 'Video' : (key == 'mediaBlog' ? 'Blog' : (key == 'mediaNews' ? 'News' : 'Game'))
    }

    if (confirmMsg) {
        $.ajax({
            url : '/save_main_media_order',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }

}

const fnSaveRoster = (key) => {
    let row = [];
    let rosterId = $('.rosterId');
    let detailOrder = $('.detail_order');

    for (let i = 0; i < rosterId.length; i++) {
        row[i] = {
            rosterId : rosterId[i].value,
            rosterOrder : detailOrder[i].value
        }
    }

    const confirmMsg = confirm('저장 하시겠습니까?');


    if (confirmMsg) {
        $.ajax({
            url : '/save_main_roster_order',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(row),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

const fnSaveBanner = (key) => {
    let row = [];
    let bannerId = $('.bannerId');
    let detailOrder = $('.detail_order');
    let sdate = $('.sdate');
    let edate = $('.edate');
    for (let i = 0; i < bannerId.length; i++) {
        if(!isEmpty(sdate[i].value) && !isEmpty(edate[i].value)) {
            console.log(' >> ', sdate[i].value > edate[i].value);
            console.log(' sdate>> ', sdate[i].value);
            console.log(' edate>> ', edate[i].value);
            if(sdate[i].value > edate[i].value){
                alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
                return false;
            }
        } else {
            alert('시작 날짜 종료 날짜를 입력해주세요.');
            return false;
        }

        row[i] = {
            bannerId : bannerId[i].value,
            bannerOrder : detailOrder[i].value,
            sdate : sdate[i].value,
            edate : edate[i].value
        }
    }

    let param = {
        bannerList: JSON.stringify(row),
        bannerType: key == 'banner' ? 'Banner' : ''
    }

    const confirmMsg = confirm('저장 하시겠습니까?');


    if (confirmMsg) {
        $.ajax({
            url : '/save_main_banner_order',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

const fnSaveColumn = (key) => {
    let row = [];
    let educationColumnId = $('.educationColumnId');
    let detailOrder = $('.detail_order');

    for (let i = 0; i < educationColumnId.length; i++) {
        row[i] = {
            educationColumnId : educationColumnId[i].value,
            columnOrder : detailOrder[i].value
        }
    }

    let param = {
        columnList: JSON.stringify(row),
    }

    const confirmMsg = confirm('저장 하시겠습니까?');


    if (confirmMsg) {
        $.ajax({
            url : '/save_main_column_order',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}

function escapeHTML(text) {
    let map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return text.replace(/[&<>"']/g, function(m) {
        return map[m];
    });
}


const fnSearchMedia = (key) => {

    let sTitle = "";

    if (key == 'mediaVideo') {
        $("#mediaVideoList").empty();
        sTitle = $('input[name=sVTitle]').val();
    } else if (key == 'mediaBlog') {
        $("#mediaBlogList").empty();
        sTitle = $('input[name=sBTitle]').val();
    } else if (key == 'mediaNews') {
        $("#mediaNewsList").empty();
        sTitle = $('input[name=sNTitle]').val();
    } else if (key == 'mediaGame') {
        $("#mediaGameList").empty();
        sTitle = $('input[name=sGTitle]').val();
    } else if (key == 'roster') {
        $("#rosterList").empty();
        sTitle = $('input[name=sRTitle]').val();
    } else if (key == 'banner'){
        $("#bannerList").empty();
        sTitle = $('input[name=sBannerTitle]').val();
    } else if (key == 'column') {
        $("#columnList").empty();
        sTitle = $('input[name=sColumnTitle]').val();
    }

    let param = {
        sTitle : sTitle,
        key : key
    }

    $.ajax({
        type: 'POST',
        url: '/search_media_data',
        data: param,
        success: function(res) {

            if (res.state == 'success') {
                let str = "";
                if (res.data.length > 0) {
                    if (key == 'mediaVideo' || key == 'mediaBlog') {
                        for (let i = 0; i < res.data.length; i++) {
                            console.log('res.data[i].reg_date : ', res.data[i].reg_date);
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='mediaChk' type='checkbox' value='" + res.data[i].media_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-title='" + escapeHTML(res.data[i].title) + "' ";
                            //str +=      "data-regDate='" + res.data[i].reg_date.substring(0, 10) + "' ";
                            //res.data[i].reg_date ? str +=      "data-regDate='" + res.data[i].reg_date.substring(0, 10) + "' " : "data-regDate='' ";
                            str +=      "data-regDate='" + res.data[i].reg_date + "' ";
                            //res.data[i].submit_date ? str +=      "data-submitDate='" + res.data[i].submit_date.substring(0, 10) + "' " : "data-submitDate='' ";
                            str +=      "data-submitDate='" + res.data[i].submit_date + "' ";
                            str +=      res.data[i].creator_name ? "data-creatorName='" + res.data[i].creator_name + "' " : "data-creatorName='' ";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].sub_type == res.menuList[j].code_value) {
                            str +=      "data-categoryName='" + res.menuList[j].category_name + "' ";
                                }
                            }
                            str +=      "data-viewCnt='" + res.data[i].view_cnt + "' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            res.data[i].reg_date ? str +=      res.data[i].reg_date.substring(0, 10) : '';
                            str +=  "</td>";
                            str +=  "<td>";
                            res.data[i].submit_date ? str +=      res.data[i].submit_date.substring(0, 10) : '';
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].creator_name ? res.data[i].creator_name : '';
                            str +=  "</td>";
                            str +=  "<td>";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].sub_type == res.menuList[j].code_value) {
                                    str +=      res.menuList[j].category_name;
                                }
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].view_cnt;
                            str +=  "</td>";
                        }

                        if (key == 'mediaVideo') {
                            $("#mediaVideoList").append(str);
                        } else if (key == 'mediaBlog') {
                            $("#mediaBlogList").append(str);
                        }
                    }
                    if (key == 'mediaNews') {
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='mediaChk' type='checkbox' value='" + res.data[i].media_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-source='" + escapeHTML(res.data[i].source) + "' ";
                            str +=      "data-title='" + escapeHTML(res.data[i].title) + "' ";
                            str +=      "data-cupInfo='";
                            if (res.data[i].cupInfoList) {
                                for (let j = 0; j < res.data[i].cupInfoList.length; j++) {
                            str +=      res.data[i].cupInfoList[j].cup_name;
                                    if (j != res.data[i].cupInfoList.length -1) {
                            str +=     ","
                                    }
                                }
                            } else {
                            str +=      "-";
                            }
                            str +=      "' ";
                            str +=      "data-leagueInfo='";
                            if (res.data[i].leagueInfoList) {
                                for (let j = 0; j < res.data[i].leagueInfoList.length; j++) {
                            str +=      res.data[i].leagueInfoList[j].league_name;
                                    if (j != res.data[i].leagueInfoList.length -1) {
                            str +=     ","
                                    }
                                }
                            } else {
                            str +=     "-";
                            }
                            str +=      "' ";
                            str +=      "data-teamInfo='";
                            if (res.data[i].teamList) {
                                for (let j = 0; j < res.data[i].teamList.length; j++) {
                                    str +=      res.data[i].teamList[j].nick_name;
                                    if (j != res.data[i].teamList.length -1) {
                                        str +=     ","
                                    }
                                }
                            } else {
                            str +=      "-";
                            }
                            str +=      "' ";
                            str +=      "data-ageGroup='" + res.data[i].uage + "' ";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].sub_type == res.menuList[j].code_value) {
                            str +=      "data-categoryName='" + res.menuList[j].category_name + "' ";
                                }
                            }
                            str +=      "data-viewCnt='" + res.data[i].view_cnt + "' ";
                            str +=      "data-regDate='" + res.data[i].reg_date.substring(0, 10) + "' ";
                            str +=      "data-submitDate='" + res.data[i].submit_date.substring(0, 10) + "' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].source;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].cupInfoList) {
                                for (let j = 0; j < res.data[i].cupInfoList.length; j++) {
                            str +=      res.data[i].cupInfoList[j].cup_name;
                                    if (j != res.data[i].cupInfoList.length -1) {
                            str +=     ", "
                                    }
                                }
                            } else {
                            str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].leagueInfoList) {
                                for (let j = 0; j < res.data[i].leagueInfoList.length; j++) {
                            str +=      res.data[i].leagueInfoList[j].league_name;
                                    if (j != res.data[i].leagueInfoList.length -1) {
                            str +=     ", "
                                    }
                                }
                            } else {
                                str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].teamList) {
                                for (let j = 0; j < res.data[i].teamList.length; j++) {
                            str +=      res.data[i].teamList[j].nick_name;
                                    if (j != res.data[i].teamList.length -1) {
                            str +=     ", <br />"
                                    }
                                }
                            } else {
                                str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].uage;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].reg_date.substring(0, 10);
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].submit_date.substring(0, 10);
                            str +=  "</td>";
                            str +=  "<td>";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].sub_type == res.menuList[j].code_value) {
                            str +=      res.menuList[j].category_name;
                                }
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].view_cnt;
                            str +=  "</td>";

                        }
                            $("#mediaNewsList").append(str);
                    }
                    if (key == 'mediaGame') {
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='mediaChk' type='checkbox' value='" + res.data[i].media_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-ageGroup='" + res.data[i].uage + "' ";
                            str +=      "data-title='" + escapeHTML(res.data[i].title) + "' ";
                            str +=      "data-cupInfo='";
                            if (res.data[i].cupInfoList) {
                                for (let j = 0; j < res.data[i].cupInfoList.length; j++) {
                                    str +=      res.data[i].cupInfoList[j].cup_name;
                                    if (j != res.data[i].cupInfoList.length -1) {
                                        str +=     ","
                                    }
                                }
                            } else {
                                str +=      "-";
                            }
                            str +=      "' ";
                            str +=      "data-leagueInfo='";
                            if (res.data[i].leagueInfoList) {
                                for (let j = 0; j < res.data[i].leagueInfoList.length; j++) {
                                    str +=      res.data[i].leagueInfoList[j].league_name;
                                    if (j != res.data[i].leagueInfoList.length -1) {
                                        str +=     ","
                                    }
                                }
                            } else {
                                str +=     "-";
                            }
                            str +=      "' ";
                            str +=      "data-teamInfo='";
                            if (res.data[i].teamList) {
                                for (let j = 0; j < res.data[i].teamList.length; j++) {
                                    str +=      res.data[i].teamList[j].nick_name;
                                    if (j != res.data[i].teamList.length -1) {
                                        str +=     ","
                                    }
                                }
                            } else {
                                str +=      "-";
                            }
                            str +=      "' ";
                            str +=      "data-ageGroup='" + res.data[i].uage + "' ";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].sub_type == res.menuList[j].code_value) {
                                    str +=      "data-categoryName='" + res.menuList[j].category_name + "' ";
                                }
                            }
                            str +=      "data-viewCnt='" + res.data[i].view_cnt + "' ";
                            str +=      "data-regDate='" + res.data[i].reg_date.substring(0, 10) + "' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].uage;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].cupInfoList) {
                                for (let j = 0; j < res.data[i].cupInfoList.length; j++) {
                                    str +=      res.data[i].cupInfoList[j].cup_name;
                                    if (j != res.data[i].cupInfoList.length -1) {
                                        str +=     ", "
                                    }
                                }
                            } else {
                                str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].leagueInfoList) {
                                for (let j = 0; j < res.data[i].leagueInfoList.length; j++) {
                                    str +=      res.data[i].leagueInfoList[j].league_name;
                                    if (j != res.data[i].leagueInfoList.length -1) {
                                        str +=     ", "
                                    }
                                }
                            } else {
                                str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].teamList) {
                                for (let j = 0; j < res.data[i].teamList.length; j++) {
                                    str +=      res.data[i].teamList[j].nick_name;
                                    if (j != res.data[i].teamList.length -1) {
                                        str +=     ", <br />"
                                    }
                                }
                            } else {
                                str += "-"
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].reg_date.substring(0, 10);
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].view_cnt;
                            str +=  "</td>";
                        }
                        $("#mediaGameList").append(str);
                    }
                    if (key == 'roster') {
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='rosterChk' type='checkbox' value='" + res.data[i].roster_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-rosterType='" + res.data[i].roster_type + "' ";
                            str +=      "data-regDate='" + res.data[i].reg_date.substring(0, 10) + "' ";
                            str +=      "data-title='" + res.data[i].title + "' ";
                            res.data[i].uage ? str += "data-uage='" + res.data[i].uage + "' " : str += "data-uage='' ";
                            str +=      "data-year='" + res.data[i].year + "' ";
                            str +=      "data-cnt='" + res.data[i].cnt + "' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].roster_type == '0') {
                            str +=      '국가대표';
                            } else {
                            str +=      '골든에이지';
                            }
                            str +=  "</td>";
                            str +=  "<td>";
                            res.data[i].uage ? str += res.data[i].uage : '-';
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].year;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].cnt;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].reg_date.substring(0, 10);
                            str +=  "</td>";

                        }

                        $("#rosterList").append(str);
                    }
                    if (key == 'banner') {
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='bannerChk' type='checkbox' value='" + res.data[i].banner_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-title='" + res.data[i].title + "' ";
                            if (res.data[i].files) {
                                for (let j = 0; j < res.data[i].files.length; j++) {
                            str +=      "data-fileSavePath='/NP" + res.data[i].files[j].file_save_path + "' ";
                                }
                            } else {
                            str +=      "data-fileSavePath='' ";
                            }
                            res.data[i].url_link ? str += "data-urlLink='" + res.data[i].url_link + "' >" : str += "data-urlLink='' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].url_link ? res.data[i].url_link : '';
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].files) {
                                for (let j = 0; j < res.data[i].files.length; j++) {
                            str +=      "<img style='width: 300px; height: 100px;' src='/NP" + res.data[i].files[j].file_save_path + "'>";
                                }
                            }
                            str +=  "</td>";
                            str +=  "</tr>";

                        }

                        $("#bannerList").append(str);
                    }
                    if (key == 'column') {
                        for (let i = 0; i < res.data.length; i++) {
                            str +=  "<tr>";
                            str +=      "<td>";
                            str +=          "<input class='columnChk' type='checkbox' value='" + res.data[i].education_column_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=          "data-title='" + res.data[i].title + "' ";
                            str +=          "data-regDate='" + res.data[i].reg_date + "' ";
                            str +=          "data-submitDate='" + res.data[i].submit_date + "' ";
                            str +=          "data-writer='" + res.data[i].writer + "' ";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].column_type == res.menuList[j].code_value) {
                            str +=          "data-columnType='" + res.menuList[j].category_name + "' ";
                                }
                            }
                            str +=          "data-viewCnt='" + res.data[i].view_cnt + "' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=      "<td>";
                            str +=          res.data[i].title;
                            str +=      "</td>";
                            str +=      "<td>";
                            str +=          res.data[i].reg_date;
                            str +=      "</td>";
                            str +=      "<td>";
                            str +=          res.data[i].submit_date;
                            str +=      "</td>";
                            str +=      "<td>";
                            str +=          res.data[i].writer;
                            str +=      "</td>";
                            str +=      "<td>";
                            for (let j = 0; j < res.menuList.length; j++) {
                                if (res.data[i].column_type == res.menuList[j].code_value) {
                            str +=      res.menuList[j].category_name;
                                }
                            }
                            str +=      "</td>";
                            str +=      "<td>";
                            str +=          res.data[i].view_cnt
                            str +=      "</td>";
                            str +=  "</tr>";
                        }
                        $("#columnList").append(str);
                    }
                } else {
                    str += "<tr><td colspan='7'>검색 결과가 없습니다.</td></tr>"
                    if (key == 'mediaVideo') {
                        $("#mediaVideoList tr").empty();
                        $("#mediaVideoList").append(str);
                    } else if (key == 'mediaBlog') {
                        $("#mediaBlogList tr").empty();
                        $("#mediaBlogList").append(str);
                    } else if (key == 'mediaNews') {
                        $("#mediaNewsList tr").empty();
                        $("#mediaNewsList").append(str);
                    } else if (key == 'mediaGame') {
                        $("#mediaGameList tr").empty();
                        $("#mediaGameList").append(str);
                    } else if (key == 'roster') {
                        $("#rosterList tr").empty();
                        $("#rosterList").append(str);
                    } else if (key == 'banner') {
                        $("#bannerList tr").empty();
                        $("#bannerList").append(str);
                    }
                }
            }
        }
    });
}

const addMediaList = (key) => {
    //const chkMedia = $("input[type=checkbox]:checked");
    const chkMedia = $("input:checkbox[class='mediaChk']:checked");
    let str = "";
    let inputStr = "";

    $("#mediaDiv").find('input');
    if (key == 'mediaVideo' || key == 'mediaBlog') {

        $(chkMedia).each(function(i) {
            let $findInput = $("#input"+$(this).val()).val();
            //++order;
            if (!$findInput) {
                str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
                str +=  "<td>";
                str +=      $(this).attr('data-title');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-regDate');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-submitDate');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-creatorName') ? $(this).attr('data-creatorName') : '';
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-categoryName');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-viewCnt');
                str +=  "</td>";
                str +=  "<td>";
                str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
                str +=      "<input class='mediaId' type='hidden' id='input"+$(this).val()+"' name='mediaId' value='" + $(this).val() + "'>";
                str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
                /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
                str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
                str +=  "</td>";
                str +=  "</tr>";

            }
            order++;
        });
    }
    if (key == 'mediaNews') {
        $(chkMedia).each(function(i) {
            let $findInput = $("#input"+$(this).val()).val();
            if (!$findInput) {
                str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
                str +=  "<td>";
                str +=      $(this).attr('data-source');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-title');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-cupInfo');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-leagueInfo');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-teamInfo');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-ageGroup');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-regDate');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-submitDate');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-categoryName');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-viewCnt');
                str +=  "</td>";
                str +=  "<td>";
                str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
                str +=      "<input class='mediaId' type='hidden' id='input"+$(this).val()+"' name='mediaId' value='" + $(this).val() + "'>";
                str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
                /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
                str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
                str +=  "</td>";
                str +=  "</tr>";
            }
            order++;
        });
    }

    if (key == 'mediaGame') {
        $(chkMedia).each(function(i) {
            let $findInput = $("#input"+$(this).val()).val();
            if (!$findInput) {
                str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
                str +=  "<td>";
                str +=      $(this).attr('data-ageGroup');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-title');
                str +=  "</td>";
                str +=  "<td>";
                $(this).attr('data-cupInfo') ? str += $(this).attr('data-cupInfo') : str += '-';
                str +=  "</td>";
                str +=  "<td>";
                $(this).attr('data-leagueInfo') ? str += $(this).attr('data-leagueInfo') : str += '-';
                str +=  "</td>";
                str +=  "<td>";
                $(this).attr('data-teamInfo') ? str += $(this).attr('data-teamInfo') : str += '-';
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-regDate');
                str +=  "</td>";
                str +=  "<td>";
                str +=      $(this).attr('data-viewCnt');
                str +=  "</td>";
                str +=  "<td>";
                str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
                str +=      "<input class='mediaId' type='hidden' id='input"+$(this).val()+"' name='mediaId' value='" + $(this).val() + "'>";
                str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
                /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
                str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
                str +=  "</td>";
                str +=  "</tr>";
            }
            order++;
        });
    }

    $("#mediaTBody").append(str);
    $("#mediaDiv").append(inputStr);

    $(".pop").fadeOut();


    if (key == 'mediaVideo') {
        $("input[name=sVTitle]").val(null);
        $("#mediaVideoList").empty();
        const listStr = "<td colspan='7'>영상을 검색 해주세요.</td>";
        $("#mediaVideoList").append(listStr);
    }
    if (key == 'mediaBlog') {
        $("input[name=sBTitle]").val(null);
        $("#mediaBlogList").empty();
        const listStr = "<td colspan='7'>영상을 검색 해주세요.</td>";
        $("#mediaBlogList").append(listStr);
    }
    if (key == 'mediaNews') {
        $("input[name=sNTitle]").val(null);
        $("#mediaNewsList").empty();
        const listStr = "<td colspan='11'>뉴스를 검색 해주세요.</td>";
        $("#mediaNewsList").append(listStr);
    }
}

const addRosterList = (key) => {
    //const chkMedia = $("input[type=checkbox]:checked");
    const chkRoster = $("input:checkbox[class='rosterChk']:checked");
    let str = "";
    let inputStr = "";


    $(chkRoster).each(function(i) {
        let $findInput = $("#input"+$(this).val()).val();
        //++order;
        if (!$findInput) {
            str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
            str +=  "<td>";
            $(this).attr('data-rosterType') == '0' ? str += '국가대표' : str += '골든에이지';
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-uage');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-title');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-year');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-cnt');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-regDate');
            str +=  "</td>";
            str +=  "<td>";
            str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
            str +=      "<input class='rosterId' type='hidden' id='input"+$(this).val()+"' name='rosterId' value='" + $(this).val() + "'>";
            str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
            /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
            str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
            str +=  "</td>";
            str +=  "</tr>";

        }
        order++;
    });

    $("#rosterTBody").append(str);
    $("#rosterDiv").append(inputStr);

    $(".pop").fadeOut();


    $("input[name=sRTitle]").val(null);
    $("#rosterList").empty();
        const listStr = "<td colspan='7'>검색 해주세요.</td>";
    $("#rosterList").append(listStr);

}

const addBannerList = (key) => {
    //const chkMedia = $("input[type=checkbox]:checked");
    const chkBanner = $("input:checkbox[class='bannerChk']:checked");
    let str = "";
    let inputStr = "";


    $(chkBanner).each(function(i) {
        let $findInput = $("#input"+$(this).val()).val();
        //++order;
        if (!$findInput) {
            str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
            str +=  "<td align='center'>";
            str +=      "<div class='w30 fl'>";
            str +=          "<input type='date' name='sdate' class='sdate' autocomplete='off' >";
            str +=      "</div>";
            str +=      "<div class='w5 fl tc' style='height:28px; line-height:28px;'>-</div>";
            str +=      "<div class='w30 fl'>";
            str +=          "<input type='date' name='edate' class='edate' autocomplete='off' >";
            str +=      "</div>";
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-title');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-urlLink');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-fileSavePath') ? "<img style='width: 300px; height: 100px;' src='" + $(this).attr('data-fileSavePath') + "' >" : '';
            str +=  "</td>";
            str +=  "<td>";
            str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
            str +=      "<input class='bannerId' type='hidden' id='input"+$(this).val()+"' name='bannerId' value='" + $(this).val() + "'>";
            str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
            /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
            str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
            str +=  "</td>";
            str +=  "</tr>";

        }
        order++;
    });

    $("#bannerTBody").append(str);
    $("#bannerDiv").append(inputStr);

    $(".pop").fadeOut();


    $("input[name=sBannerTitle]").val(null);
    $("#bannerList").empty();
    const listStr = "<td colspan='7'>검색 해주세요.</td>";
    $("#bannerList").append(listStr);

}

const addColumnList = (key) => {
    //const chkMedia = $("input[type=checkbox]:checked");
    const chkColumn = $("input:checkbox[class='columnChk']:checked");
    let str = "";
    let inputStr = "";


    $(chkColumn).each(function(i) {
        let $findInput = $("#input"+$(this).val()).val();
        //++order;
        if (!$findInput) {
            str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
            str +=  "<td>";
            str +=      $(this).attr('data-title');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-regDate');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-submitDate');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-writer');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-columnType');
            str +=  "</td>";
            str +=  "<td>";
            str +=      $(this).attr('data-viewCnt');
            str +=  "</td>";
            str +=  "<td>";
            str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
            str +=      "<input class='educationColumnId' type='hidden' id='input"+$(this).val()+"' name='educationColumnId' value='" + $(this).val() + "'>";
            str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
            /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
            str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
            str +=  "</td>";
            str +=  "</tr>";

        }
        order++;
    });

    $("#columnTBody").append(str);
    $("#columnDiv").append(inputStr);

    $(".pop").fadeOut();


    $("input[name=scolumnTitle]").val(null);
    $("#columnList").empty();
    const listStr = "<td colspan='7'>검색 해주세요.</td>";
    $("#columnList").append(listStr);

}

const removeTab = (el) => {

    let id = $(el).attr('data-id')
    let ordValue = $('input[data-order="ord'+id+'"]').val();

    let detailOrder = $(".detail_order");
    let lastValue = $(".detail_order")[detailOrder.length -1].value;

    /*if (ordValue == 1 ) {
        $.each(detailOrder, function(idx, item) {
            $(this).val($(this).val() -1);
        });
    } else {
        if (ordValue != lastValue) {
            if (ordValue >= 2) {
                $.each(detailOrder, function(idx, item) {
                    if (idx > (ordValue -1)) { // 현재 value 이후의 order 1씩 감소
                        let currentValue = parseFloat($(item).val());
                        $(item).val(currentValue - 1);
                    }
                });
            }
        }
    }*/

    if (ordValue != lastValue) {
        $.each(detailOrder, function(idx, item) {
            if (idx > (ordValue -1)) { // 현재 value 이후의 order 1씩 감소
                let currentValue = parseFloat($(item).val());
                $(item).val(currentValue - 1);
            }
        });
    }

    --order;
    $("#input" + id).remove();
    $("#tr" + id).remove();
}

const fnMoveList = () => {
    location.href = "/mainMenuConfig";
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
  		  	<h2><span></span>설정 > 메인 메뉴 설정</h2>
        </div>
      </div>
      <c:choose>
          <c:when test="${key eq 'leagueMatch' || key eq 'cupMatch'}">
              <div class="round body">
                  <div class="body-head">
                      당일 경기
                  </div>
                  <table cellspacing="0" class="">
                      <colgroup>
                          <col width="5%">
                          <col width="10%">
                          <col width="5%">
                          <col width="10%">
                          <col width="15%">
                          <col width="5%">
                          <col width="5%">
                          <col width="5%">
                          <col width="5%">
                          <col width="15%">
                          <col width="12.5%">
                      </colgroup>
                      <thead>
                      <tr>
                          <th rowspan="2">연령</th>
                          <th rowspan="2">
                              <c:choose>
                                  <c:when test="${key eq 'cupMatch'}">대회 명</c:when>
                                  <c:when test="${key eq 'leagueMatch'}">리그 명</c:when>
                              </c:choose>
                          </th>
                          <th rowspan="2">경기일시</th>
                          <th rowspan="2">경기장소</th>
                          <th colspan="3">홈팀</th>
                          <th colspan="3">어웨이팀</th>
                          <th rowspan="2">점수 노출 설정</th>
                      </tr>
                      <tr>
                          <th>팀명</th>
                          <th>점수</th>
                          <th>PK</th>
                          <th>PK</th>
                          <th>점수</th>
                          <th>팀명</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:choose>
                          <c:when test="${!empty matchList}">
                              <c:forEach var="result" items="${matchList}" varStatus="status">
                                  <tr>
                                      <td>
                                          ${result.ageGroup}
                                      </td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${key eq 'cupMatch'}">${result.cup_name}</c:when>
                                              <c:when test="${key eq 'leagueMatch'}">${result.league_name}</c:when>
                                          </c:choose>
                                      </td>
                                      <td>
                                          ${result.match_date}
                                      </td>
                                      <td>
                                          ${result.place}
                                      </td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${result.home_id ne '-1'}">
                                                  <a>
                                                  ${result.home}
                                                  </a>
                                                  <c:choose>
                                                      <c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                      <c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
                                                  </c:choose>
                                              </c:when>
                                              <c:otherwise>
                                                  미정
                                              </c:otherwise>
                                          </c:choose>
                                      </td>
                                      <td>
                                          ${result.home_score}
                                      </td>
                                      <td>
                                          ${result.home_pk}
                                      </td>
                                      <td>
                                          ${result.away_pk}
                                      </td>
                                      <td>
                                          ${result.away_score}
                                      </td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${result.away_id ne '-1'}">
                                                  <a>
                                                      ${result.away}
                                                  </a>
                                                  <c:choose>
                                                      <c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                      <c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
                                                  </c:choose>
                                              </c:when>
                                              <c:otherwise>
                                                  미정
                                              </c:otherwise>
                                          </c:choose>
                                      </td>
                                      <td>
                                          <input type="hidden" name="ageGroup" value="${result.ageGroup}" >
                                          <input type="hidden" name="matchId" value="${result.match_id}" >
                                          <input type="hidden" name="matchCategory" value="${result.match_category}" >
                                          <input type="radio" class="scoreShowFlag" name="ra${status.index}" id="ra${status.index}_1" <c:if test="${result.score_show_flag eq '0'}">checked</c:if> value="0"><label for="ra${status.index}_1">노출</label>
                                          <input type="radio" class="scoreShowFlag" name="ra${status.index}" id="ra${status.index}_2" <c:if test="${result.score_show_flag eq '1'}">checked</c:if> value="1"><label for="ra${status.index}_2">비노출</label>
                                      </td>
                                  </tr>
                              </c:forEach>
                          </c:when>
                      </c:choose>
                      </tbody>
                  </table>
                  <div class="body-foot">
                      <div class="others">
                          <a class="btn-large gray-o" onclick="fnMoveList();">취소</a>
                          <a class="btn-large default" onclick="fnSaveShowPointFlag();">저장하기</a>
                      </div>
                  </div>
              </div>
          </c:when>
          <c:when test="${key eq 'progressCup'}">
            <div class="round body">
                <div class="scroll">
                    <table cellspacing="0" class="">
                        <thead>
                        <tr>
                            <th>연령</th>
                            <th>대회명</th>
                            <th>활성</th>
                            <th>대회일정</th>
                            <th>참가팀</th>
                            <th>참가팀2</th>
                            <th>대회유형</th>
                            <th>토너먼트</th>
                            <th>토너먼트 팀수</th>
                            <th>관리</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty cupList}">
                                    <tr>
                                        <td id="idEmptyList" colspan="8">등록된 내용이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="result" items="${cupList}" varStatus="status">
                                        <tr>
                                            <td class="tl">
                                                ${result.ageGroup}
                                            </td>
                                            <td class="tl">
                                                ${result.cup_name}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.play_flag eq '0'}">활성</c:when>
                                                    <c:when test="${result.play_flag eq '1'}">비활성</c:when>
                                                </c:choose>
                                            </td>
                                            <td>${result.play_sdate} ~ ${result.play_edate}</td>
                                            <td>${result.cup_team}팀</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.ageGroup eq 'U12' || result.ageGroup eq 'U11'}">
                                                        ${result.cup_team2}팀
                                                    </c:when>
                                                    <c:otherwise>
                                                        -
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="tl">
                                                <c:if test="${result.cup_type eq 0}">예선+토너먼트</c:if>
                                                <c:if test="${result.cup_type eq 1}">예선+본선+토너먼트</c:if>
                                                <c:if test="${result.cup_type eq 2}">풀리그</c:if>
                                                <c:if test="${result.cup_type eq 3}">토너먼트</c:if>
                                                <c:if test="${result.cup_type eq 4}">풀리그 + 풀리그</c:if>
                                            </td>
                                            <td>
                                                <c:if test="${result.tour_type eq 0}">대진표</c:if>
                                                <c:if test="${result.tour_type eq 1}">추첨제</c:if>
                                            </td>
                                            <td>${result.tour_team}팀 </td>
                                            <td>
                                                <input type="hidden" name="ageGroup" value="${result.ageGroup}" >
                                                <input type="hidden" name="cupId" value="${result.cup_id}" >
                                                <input type="radio" class="showFlag" name="ra${status.index}" id="ra${status.index}_1" <c:if test="${result.show_flag eq '0'}">checked</c:if> value="0"><label for="ra${status.index}_1">노출</label>
                                                <input type="radio" class="showFlag" name="ra${status.index}" id="ra${status.index}_2" <c:if test="${result.show_flag eq '1'}">checked</c:if> value="1"><label for="ra${status.index}_2">비노출</label>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                <div class="body-foot">
                    <div class="others">
                        <a class="btn-large gray-o" onclick="fnMoveList();">취소</a>
                        <a class="btn-large default" onclick="fnSaveShowFlagProgressCup();">저장하기</a>
                    </div>
                </div>
            </div>
          </c:when>

          <c:when test="${key eq 'progressLeague'}">
              <div class="round body">
                  <div class="scroll">
                      <table cellspacing="0" class="">
                          <thead>
                          <tr>
                              <th>연령</th>
                              <th>리그명</th>
                              <th>활성</th>
                              <th>리그일정</th>
                              <th>참가팀</th>
                              <th>관리</th>
                          </tr>
                          </thead>
                          <tbody>
                          <c:choose>
                              <c:when test="${empty leagueList}">
                                  <tr>
                                      <td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
                                  </tr>
                              </c:when>
                              <c:otherwise>
                                  <c:forEach var="result" items="${leagueList}" varStatus="status">
                                      <tr>
                                          <td class="tl">
                                                  ${result.ageGroup}
                                          </td>
                                          <td class="tl">
                                                  ${result.league_name}
                                          </td>
                                          <td>
                                              <c:choose>
                                                  <c:when test="${result.play_flag eq '0'}">활성</c:when>
                                                  <c:when test="${result.play_flag eq '1'}">비활성</c:when>
                                              </c:choose>
                                          </td>
                                          <td>${result.play_sdate} ~ ${result.play_edate}</td>
                                          <td>${result.cnt}팀</td>
                                          <td>
                                              <input type="hidden" name="ageGroup" value="${result.ageGroup}" >
                                              <input type="hidden" name="leagueId" value="${result.league_id}" >
                                              <input type="radio" class="showFlag" name="ra${status.index}" id="ra${status.index}_1" <c:if test="${result.show_flag eq '0'}">checked</c:if> value="0"><label for="ra${status.index}_1">노출</label>
                                              <input type="radio" class="showFlag" name="ra${status.index}" id="ra${status.index}_2" <c:if test="${result.show_flag eq '1'}">checked</c:if> value="1"><label for="ra${status.index}_2">비노출</label>
                                          </td>
                                      </tr>
                                  </c:forEach>
                              </c:otherwise>
                          </c:choose>
                          </tbody>
                      </table>
                  </div>
                  <div class="body-foot">
                      <div class="others">
                          <a class="btn-large gray-o" onclick="fnMoveList();">취소</a>
                          <a class="btn-large default" onclick="fnSaveShowFlagProgressLeague();">저장하기</a>
                      </div>
                  </div>
              </div>
          </c:when>

          <c:when test="${key.contains('media')}">
            <div class="round body">
                <div class="body-head">
                    <h4 class="view-title">
                        <c:choose>
                            <c:when test="${key eq 'mediaVideo'}">
                                영상
                            </c:when>
                            <c:when test="${key eq 'mediaNews'}">
                                뉴스
                            </c:when>
                            <c:when test="${key eq 'mediaBlog'}">
                                블로그
                            </c:when>
                            <c:when test="${key eq 'mediaGame'}">
                                경기 영상
                            </c:when>
                        </c:choose>
                    </h4>
                    <div class="others">
                        <a class="btn-large gray-o btn-pop" data-id="pop-${key}">추가하기</a>
                    </div>
                </div>
                <c:if test="${key eq 'mediaGame'}">
                <div>
                    <div class="scroll">
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="5%">
                                <col width="10%">
                                <col width="5%">
                                <col width="10%">
                                <col width="15%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="5%">
                                <col width="15%">
                                <col width="12.5%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th rowspan="2">연령</th>
                                <th rowspan="2">대회 명</th>
                                <th rowspan="2">경기일시</th>
                                <th rowspan="2">경기장소</th>
                                <th colspan="3">홈팀</th>
                                <th colspan="3">어웨이팀</th>
                                <th rowspan="2">라이브 노출 설정</th>
                            </tr>
                            <tr>
                                <th>팀명</th>
                                <th>점수</th>
                                <th>PK</th>
                                <th>PK</th>
                                <th>점수</th>
                                <th>팀명</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${!empty liveList}">
                                    <c:forEach var="result" items="${liveList}" varStatus="status">
                                        <tr>
                                            <td>
                                                ${result.ageGroup}
                                            </td>
                                            <td>
                                                ${result.cup_name}
                                            </td>
                                            <td>
                                                    ${result.match_date}
                                            </td>
                                            <td>
                                                    ${result.place}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.home_id ne '-1'}">
                                                        <a>
                                                                ${result.home}
                                                        </a>
                                                        <c:choose>
                                                            <c:when test="${empty result.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                            <c:otherwise><img src="/NP${result.home_emblem}" class="logo"></c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        미정
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                    ${result.home_score}
                                            </td>
                                            <td>
                                                    ${result.home_pk}
                                            </td>
                                            <td>
                                                    ${result.away_pk}
                                            </td>
                                            <td>
                                                    ${result.away_score}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${result.away_id ne '-1'}">
                                                        <a>
                                                                ${result.away}
                                                        </a>
                                                        <c:choose>
                                                            <c:when test="${empty result.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                                                            <c:otherwise><img src="/NP${result.away_emblem}" class="logo"></c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        미정
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <input type="hidden" name="ageGroup" value="${result.ageGroup}" >
                                                <input type="hidden" name="matchId" value="${result.match_id}" >
                                                <input type="hidden" name="matchCategory" value="${result.match_category}" >
                                                <input type="radio" class="liveShowFlag" name="ra${status.index}" id="ra${status.index}_1" <c:if test="${result.live_show_flag eq '0'}">checked</c:if> value="0"><label for="ra${status.index}_1">노출</label>
                                                <input type="radio" class="liveShowFlag" name="ra${status.index}" id="ra${status.index}_2" <c:if test="${result.live_show_flag eq '1'}">checked</c:if> value="1"><label for="ra${status.index}_2">비노출</label>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <div class="body-foot">
                        <div class="others">
                            <a class="btn-large gray-o" onclick="fnMoveList();">취소</a>
                            <a class="btn-large default" onclick="fnSaveLiveShowFlag();">저장하기</a>
                        </div>
                    </div>
                </div>
                <br />
                <br />
                <br />
                <br />
                </c:if>
                <div class="content_mainMenu mb_10">
                    <div>
                        <div class="category_wrap">

                            <div class="ca_body view">
                                <div class="" id="tab_view_1">
                                    <table class="">
                                        <c:choose>
                                            <c:when test="${key eq 'mediaVideo' || key eq 'mediaBlog'}">
                                                <thead>
                                                <tr>
                                                    <th>제목</th>
                                                    <th>등록일</th>
                                                    <th>
                                                        <c:choose>
                                                            <c:when test="${key eq 'mediaVideo'}">영상</c:when>
                                                            <c:when test="${key eq 'mediaBlog'}">블로그</c:when>
                                                        </c:choose>
                                                        등록일
                                                    </th>
                                                    <th>작성자</th>
                                                    <th>카테고리</th>
                                                    <th>조회수</th>
                                                    <th>삭제하기</th>
                                                </tr>
                                                </thead>
                                                <tbody id="mediaTBody">

                                                </tbody>
                                            </c:when>
                                            <c:when test="${key eq 'mediaNews'}">
                                                <thead>
                                                <tr>
                                                    <th>출처</th>
                                                    <th>제목</th>
                                                    <th>등록대회</th>
                                                    <th>등록리그</th>
                                                    <th>학원/클럽</th>
                                                    <th>연령</th>
                                                    <th>등록일</th>
                                                    <th>기사 등록일</th>
                                                    <th>카테고리</th>
                                                    <th>조회수</th>
                                                    <th>삭제하기</th>
                                                </tr>
                                                </thead>
                                                <tbody id="mediaTBody">

                                                </tbody>
                                            </c:when>
                                            <c:when test="${key eq 'mediaGame'}">

                                                <thead>
                                                <tr>
                                                    <th>연령</th>
                                                    <th>제목</th>
                                                    <th>등록대회</th>
                                                    <th>등록리그</th>
                                                    <th>학원/클럽</th>
                                                    <th>등록일</th>
                                                    <th>조회수</th>
                                                    <th>삭제하기</th>
                                                </tr>
                                                </thead>
                                                <tbody id="mediaTBody">

                                                </tbody>
                                            </c:when>
                                        </c:choose>
                                    </table>
                                </div>
                            </div>

                            <br />

                            <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                                <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                            </div>
                            <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                <button type="button" class="btn_sku gray" id="cancle" onclick="fnMoveList()">취소</button>
                                <button type="button" class="btn_sku btn_save blue_olbg" id="save" onclick="fnSaveMainMedia('${key}')">저장</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="mediaDiv">
                </div>

            </div>
          </c:when>
          <c:when test="${key eq 'roster'}">
              <div class="round body">
                  <div class="body-head">
                      <h4 class="view-title">
                          국가대표 / 골든에이지
                      </h4>
                      <div class="others">
                          <a class="btn-large gray-o btn-pop" data-id="pop-${key}">추가하기</a>
                      </div>
                  </div>
                  <div class="content_mainMenu mb_10">
                      <div>
                          <div class="category_wrap">
                              <div class="ca_body view">
                                  <div class="" id="tab_view_1">
                                      <table class="">
                                          <thead>
                                          <tr>
                                              <th>종류</th>
                                              <th>연령</th>
                                              <th>제목</th>
                                              <th>연도</th>
                                              <th>등록선수 수</th>
                                              <th>등록일자</th>
                                              <th>관리</th>
                                          </tr>
                                          </thead>
                                          <tbody id="rosterTBody">

                                          </tbody>
                                      </table>
                                  </div>
                              </div>

                              <br />

                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                                  <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                              </div>
                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button type="button" class="btn_sku gray" id="cancle" onclick="fnMoveList()">취소</button>
                                  <button type="button" class="btn_sku btn_save blue_olbg" id="save" onclick="fnSaveRoster('${key}')">저장</button>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          </c:when>
          <c:when test="${key eq 'banner'}">
              <div class="round body">
                  <div class="body-head">
                      <h4 class="view-title">
                          배너
                      </h4>
                      <div class="others">
                          <a class="btn-large gray-o btn-pop" data-id="pop-${key}">추가하기</a>
                      </div>
                  </div>
                  <div class="content_mainMenu mb_10">
                      <div>
                          <div class="category_wrap">
                              <div class="ca_body view">
                                  <div class="" id="tab_view_1">
                                      <table class="">
                                          <thead>
                                          <tr>
                                              <th>노출기간</th>
                                              <th>배너명</th>
                                              <th>URL</th>
                                              <th>이미지</th>
                                              <th>삭제하기</th>
                                          </tr>
                                          </thead>
                                          <tbody id="bannerTBody">

                                          </tbody>
                                      </table>
                                  </div>
                              </div>

                              <br />

                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                                  <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                              </div>
                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button type="button" class="btn_sku gray" id="cancle" onclick="fnMoveList()">취소</button>
                                  <button type="button" class="btn_sku btn_save blue_olbg" id="save" onclick="fnSaveBanner('${key}')">저장</button>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          </c:when>
          <c:when test="${key eq 'column'}">
              <div class="round body">
                  <div class="body-head">
                      <h4 class="view-title">
                          칼럼
                      </h4>
                      <div class="others">
                          <a class="btn-large gray-o btn-pop" data-id="pop-${key}">추가하기</a>
                      </div>
                  </div>
                  <div class="content_mainMenu mb_10">
                      <div>
                          <div class="category_wrap">
                              <div class="ca_body view">
                                  <div class="" id="tab_view_1">
                                      <table class="">
                                          <thead>
                                          <tr>
                                              <th>제목</th>
                                              <th>등록일</th>
                                              <th>칼럼 등록일</th>
                                              <th>작성자</th>
                                              <th>카테고리</th>
                                              <th>조회수</th>
                                              <th>삭제하기</th>
                                          </tr>
                                          </thead>
                                          <tbody id="columnTBody">

                                          </tbody>
                                      </table>
                                  </div>
                              </div>

                              <br />

                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                                  <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                              </div>
                              <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                                border-top: 1px solid #ccc;
                                grid-template-columns: 1fr 1fr;
                                display: grid;
                                gap: 5px;
                                padding: 15px;">
                                  <button type="button" class="btn_sku gray" id="cancle" onclick="fnMoveList()">취소</button>
                                  <button type="button" class="btn_sku btn_save blue_olbg" id="save" onclick="fnSaveColumn('${key}')">저장</button>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          </c:when>
      </c:choose>
    </div>


    <div class="pop" id="pop-mediaVideo">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    영상 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10 grid1">
                    <input type="text" name="sVTitle" id="sVTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="*">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>제목</th>
                                    <th>등록일</th>
                                    <th>영상 등록일</th>
                                    <th>작성자</th>
                                    <th>카테고리</th>
                                    <th>조회수</th>
                                </tr>
                            </thead>
                            <tbody id="mediaVideoList">
                                <tr>
                                    <td colspan="7">
                                        영상을 검색 해주세요.
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addMediaList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-mediaBlog">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    블로그 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10 grid1">
                    <input type="text" name="sBTitle" id="sBTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>제목</th>
                                <th>등록일</th>
                                <th>블로그 등록일</th>
                                <th>작성자</th>
                                <th>카테고리</th>
                                <th>조회수</th>
                            </tr>
                            </thead>
                            <tbody id="mediaBlogList">
                            <tr>
                                <td colspan="7">
                                    블로그를 검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addMediaList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-mediaNews">
        <div style="height:auto;">
            <div style="height:auto; width: 2000px;">
                <div class="head">
                    뉴스 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sNTitle" id="sNTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>출처</th>
                                <th>제목</th>
                                <th>등록대회</th>
                                <th>등록리그</th>
                                <th>학원/클럽</th>
                                <th>연령</th>
                                <th>등록일</th>
                                <th>기사 등록일</th>
                                <th>카테고리</th>
                                <th>조회수</th>
                            </tr>
                            </thead>
                            <tbody id="mediaNewsList">
                            <tr>
                                <td colspan="11">
                                    뉴스를 검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addMediaList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-mediaGame">
        <div style="height:auto;">
            <div style="height:auto; width: 2000px;">
                <div class="head">
                    경기영상 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sGTitle" id="sGTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>연령</th>
                                <th>제목</th>
                                <th>등록대회</th>
                                <th>등록리그</th>
                                <th>학원/클럽</th>
                                <th>등록일</th>
                                <th>조회수</th>
                            </tr>
                            </thead>
                            <tbody id="mediaGameList">
                            <tr>
                                <td colspan="8">
                                    경기를 검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addMediaList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-roster">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    경기영상 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sRTitle" id="sRTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>종류</th>
                                <th>연령</th>
                                <th>제목</th>
                                <th>연도</th>
                                <th>등록선수 수</th>
                                <th>등록일자</th>
                                <th>관리</th>
                            </tr>
                            </thead>
                            <tbody id="rosterList">
                            <tr>
                                <td colspan="8">
                                    검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addRosterList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-banner">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    배너 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sBannerTitle" id="sBannerTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="30px">
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>배너명</th>
                                <th>URL</th>
                                <th>이미지</th>
                            </tr>
                            </thead>
                            <tbody id="bannerList">
                            <tr>
                                <td colspan="4">
                                    검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addBannerList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="pop-column">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    칼럼 메인페이지 추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sColumnTitle" id="sColumnTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMedia('${key}'); return;}">
                    <a class="btn-large default" onclick="fnSearchMedia('${key}')">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="30px">
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>제목</th>
                                <th>등록일</th>
                                <th>칼럼 등록일</th>
                                <th>작성자</th>
                                <th>카테고리</th>
                                <th>조회수</th>
                            </tr>
                            </thead>
                            <tbody id="columnList">
                            <tr>
                                <td colspan="7">
                                    검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addColumnList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>