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

<script src="resources/apexcharts/apexcharts.js"></script>
<script src="resources/apexcharts/apexcharts.css"></script>

<script type="text/javascript">
let chart;

let daysVisitDay = [];
let daysVisitCnt = [];

let daysOrgVisitDay = [];
let daysOrgVisitCnt = [];

let monthOrgVisitDay = [];
let monthOrgVisitCnt = [];

<c:forEach var="item" items="${dayVisitAvg}" varStatus="status">
daysVisitDay.push("${item.days}일");
daysVisitCnt.push("${item.cnt eq '-' ? 0 : item.cnt}");
</c:forEach>

<c:forEach var="item" items="${dayOrgVisitAvg}" varStatus="status">
daysOrgVisitDay.push("${item.days}일");
daysOrgVisitCnt.push("${item.cnt eq '-' ? 0 : item.cnt}");
</c:forEach>

<c:forEach var="item" items="${monthOrgVisitAvg}" varStatus="status">
monthOrgVisitDay.push("${item.days}월");
monthOrgVisitCnt.push("${item.cnt eq '-' ? 0 : item.cnt}");
</c:forEach>

$(document).ready(function() {

    let option1 = fnMakeLineChart(daysVisitDay, daysVisitCnt, 'days');
    chart = new ApexCharts(document.querySelector("#chart-1"), option1);
    chart.render();

    let option2 = fnMakeLineChart(daysOrgVisitDay, daysOrgVisitCnt, 'daysOrg');
    let chart2 = new ApexCharts(document.querySelector("#chart-2"), option2);
    chart2.render();

    let option3 = fnMakeLineChart(monthOrgVisitDay, monthOrgVisitCnt, 'monthOrg');
    let chart3 = new ApexCharts(document.querySelector("#chart-3"), option3);
    chart3.render();

});

const fnMakeLineChart = (days, cnt, type) => {
    let nameStr = type == 'days' ? '일단위 방문자수' : type == 'daysOrg' ? '일단위 순수 방문자수' : '월단위 순수 방문자 수'

    var options = {
        series: [{
            name: nameStr,
            data: cnt
        }],
        /*chart: {
            type: 'bar',
            stacked: true,
            height: 250,
            width: '100%',
            toolbar: {
                show: false
            }
        },
        plotOptions: {
            bar: {
                columnWidth: '50%',
                endingShape: 'rounded',
                dataLabels: {
                    position: 'center', // top, center, bottom
                },
            },
        },
        dataLabels: {
            enabled: true,
            position: 'top',
            formatter: function (val) {
                return val;
            },
            offsetY: 0,
            style: {
                fontSize: '12px',
                colors: ["#fff"]
            }
        },

        stroke: {
            show: true,
            width: 2,
            colors: ['transparent']
        },*/

        chart: {
            type: "line",
            stacked: true,
            height: 250,
            toolbar: {
                show: false,
            },
        },

        dataLabels: {
            enabled: true,
            formatter: function (val) {
                return val;
            },
            offsetY: 0,
            style: {
                fontSize: "12px",
            },
        },

        stroke: {
            show: true,
            width: 1,
        },

        xaxis: {
            categories: days
        },
        yaxis: {

        },
        fill: {
            opacity: 1
        },
        tooltip: {
            y: {
                formatter: function (val) {
                    return + val + "명"
                }
            }
        }
    };

    return options;
}

const fnChangeDay = () => {
    const year = $("#daySYear option:selected").val();
    const month = $("#daySMonth option:selected").val();

    $("#dayMonth").text(month + "월");

    const param = {
        sYear: year,
        sMonth: month
    };

    $.ajax({
        type: 'POST',
        url: '/dayVisitAvg',
        data: param,
        success: function(res) {

            if (res.state == 'SUCCESS') {

                $("#chart-1").remove();
                $("#chart1Div").append('<div id="chart-1"></div>');

                arr = []
                $("#dayTotalCnt").text(res.data.totalDayVisitAvg.toLocaleString());
                $("#dayTBody").empty();

                let str = "";
                let days = [];
                let cnt = [];
                for (let i = 0; i < res.data.dayVisitAvg.length; i++) {
                    days.push(res.data.dayVisitAvg[i].days + "일");
                    cnt.push(res.data.dayVisitAvg[i].cnt === '-' ? 0 : res.data.dayVisitAvg[i].cnt);

                    if (i % 5 == 0) {
                        str += "<tr>";
                    }
                    str += "<td>" +
                                res.data.dayVisitAvg[i].days + "일" +
                            "</td>" +
                            "<td>";
                                if (res.data.dayVisitAvg[i].cnt !== '-') {
                                    str += Number(res.data.dayVisitAvg[i].cnt).toLocaleString();
                                } else {
                                    str += res.data.dayVisitAvg[i].cnt;
                                }
                            str += "</td>";
                    if (i % 5 == 4) {
                        str += "</tr>";
                    }
                }

                $("#dayTBody").append(str);

                let options = fnMakeLineChart(days, cnt, 'days');
                console.log('options : ', options)
                chart = new ApexCharts(document.querySelector("#chart-1"), options);
                chart.render();
            }
        }
    });

}

const fnChangeDayOrg = () => {
    const year = $("#dayOrgSYear option:selected").val();
    const month = $("#dayOrgSMonth option:selected").val();

    $("#dayMonth").text(month + "월");

    const param = {
        sYear: year,
        sMonth: month
    };

    $.ajax({
        type: 'POST',
        url: '/dayOrgVisitAvg',
        data: param,
        success: function(res) {
            if (res.state == 'SUCCESS') {

                $("#chart-2").remove();
                $("#chart2Div").append('<div id="chart-2"></div>');

                arr = []
                $("#dayOrgTotalCnt").text(res.data.totalDayOrgVisitAvg.toLocaleString());
                $("#dayOrgTBody").empty()

                let str = "";
                let days = [];
                let cnt = [];
                for (let i = 0; i < res.data.dayOrgVisitAvg.length; i++) {
                    days.push(res.data.dayOrgVisitAvg[i].days + "일");
                    cnt.push(res.data.dayOrgVisitAvg[i].cnt === '-' ? 0 : res.data.dayOrgVisitAvg[i].cnt);

                    if (i % 5 == 0) {
                        str += "<tr>";
                    }
                    str += "<td>" +
                        res.data.dayOrgVisitAvg[i].days + "일" +
                        "</td>" +
                        "<td>";
                            if (res.data.dayOrgVisitAvg[i].cnt !== '-') {
                                str += Number(res.data.dayOrgVisitAvg[i].cnt).toLocaleString();
                            } else {
                                str += res.data.dayOrgVisitAvg[i].cnt;
                            }
                        "</td>";
                    if (i % 5 == 4) {
                        str += "</tr>";
                    }
                }
                let options = fnMakeLineChart(days, cnt, 'daysOrg');

                $("#dayOrgTBody").append(str);

                var chart = new ApexCharts(document.querySelector("#chart-2"), options);
                chart.render();
            }
        }
    });

}

const fnChangeMonthOrg = () => {
    const year = $("#monthOrgSYear option:selected").val();

    $("#monthOrgYear").text(year + "년");

    const param = {
        sYear: year,
    };

    $.ajax({
        type: 'POST',
        url: '/monthOrgVisitAvg',
        data: param,
        success: function(res) {
            if (res.state == 'SUCCESS') {

                $("#chart-3").remove();
                $("#chart3Div").append('<div id="chart-3"></div>');

                arr = []
                $("#monthOrgTotalCnt").text(res.data.totalDayOrgVisitAvg.toLocaleString());
                $("#monthOrgTBody").empty()

                let str = "";
                let days = [];
                let cnt = [];
                for (let i = 0; i < res.data.monthOrgVisitAvg.length; i++) {
                    days.push(res.data.monthOrgVisitAvg[i].days + "월");
                    cnt.push(res.data.monthOrgVisitAvg[i].cnt === '-' ? 0 : res.data.monthOrgVisitAvg[i].cnt);

                    if (i % 3 == 0) {
                        str += "<tr>";
                    }
                    str += "<td>" +
                        res.data.monthOrgVisitAvg[i].days + "월" +
                        "</td>" +
                        "<td>";
                            if (res.data.monthOrgVisitAvg[i].cnt !== '-') {
                                str += Number(res.data.monthOrgVisitAvg[i].cnt).toLocaleString();
                            } else {
                                str += res.data.monthOrgVisitAvg[i].cnt;
                            }
                        "</td>";
                    if (i % 3 == 2) {
                        str += "</tr>";
                    }
                }
                $("#monthOrgTBody").append(str);
                let options = fnMakeLineChart(days, cnt, 'monthOrg');

                var chart = new ApexCharts(document.querySelector("#chart-3"), options);
                chart.render();
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
        <c:set var="now" value="<%=new java.util.Date()%>" />
        <c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
        <c:set var="sysMonth"><fmt:formatDate value="${now}" pattern="MM" /></c:set>
        <div class="head">
            <div class="sub-menu">
                <h2><span></span>회원관리</h2>
            </div>
        </div>
        <div class="round body">
            <div class="body-head">

                <div class="user_counter">
                    <div class="us_1">
                        <p>총 회원수
                            <br>
                            <span>
                                <c:if test="${!empty memberTotalMap.totalMemberCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.totalMemberCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_2">
                        <p>신규 회원수<br>
                            <span>
                                <c:if test="${!empty memberTotalMap.newCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.newCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>휴면 회원수<br>
                            <span>
                                <c:if test="${!empty memberTotalMap.sleepCnt}">
                                    <fmt:formatNumber value="${memberTotalMap.sleepCnt}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>총 방문자수<br>
                            <span>
                                <c:if test="${!empty total}">
                                    <fmt:formatNumber value="${total}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>오늘 방문자수<br>
                            <span>
                                <c:if test="${!empty today}">
                                    <fmt:formatNumber value="${today}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>

                    <%--<div class="us_3">
                        <p>총 방문자수 10분<br>
                            <span>
                                <c:if test="${!empty totalMinute}">
                                    <fmt:formatNumber value="${totalMinute}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>
                    <div class="us_3">
                        <p>오늘 방문자수 10분<br>
                            <span>
                                <c:if test="${!empty todayMinute}">
                                    <fmt:formatNumber value="${todayMinute}" pattern="#,###"/>
                                </c:if>
                            </span>
                        </p>
                    </div>--%>

                </div>

                <hr class="mt_10 mb_10">
                <a class="btn-large default" href="/visitAvg">방문통계</a>
                <a class="btn-large gray-o" href="/memberAvg">회원통계</a>
                <hr class="mt_10 mb_10">
            </div>
            <div class="chart-section">
                <div>
                    <div class="chart-section-head">
                        <div class="title">일단위 방문자수
                            <span class="current">
                                <span id="dayMonth">
                                ${sysMonth}월
                                </span>
                                방문자 수 총합 -
                                <span id="dayTotalCnt">
                                <fmt:formatNumber value="${totalDayVisitAvg }" pattern="#,###" />
                                </span>
                            </span>
                        </div>
                        <div class="select-group">
                            <select id="daySYear" onchange="fnChangeDay()">
                                <c:forEach var="items" begin="2023" end="${sysYear}" step="1" varStatus="status">
                                    <option value="${sysYear - status.count + 1 }"  <c:if test="${(sysYear - status.count + 1 ) eq sysYear}">selected</c:if> >${sysYear - status.count + 1 }년</option>
                                </c:forEach>
                            </select>
                            <select id="daySMonth" name="sMonth" class="sel" onchange="fnChangeDay()">
                                <c:forEach var="items" begin="0" end="11" step="1" varStatus="status">
                                    <fmt:formatNumber var="noMonth" minIntegerDigits="2" value="${status.count}" type="number"/>
                                    <option value="${noMonth}" <c:if test="${noMonth eq sysMonth}">selected</c:if> >${status.count}월</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="chart-section-body" id="dayChartBody">
                        <div id="chart1Div">
                            <div id="chart-1"></div>
                        </div>
                        <div class="chart-section-table type1-1">
                            <div>
                                <table cellspacing="0" class="update over">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                        </tr>
                                    </thead>
                                    <tbody id="dayTBody">
                                        <c:choose>
                                            <c:when test="${empty dayVisitAvg}">
                                                <td colspan="10">방문자가 없습니다</td>
                                            </c:when>
                                            <c:otherwise>

                                                <c:forEach var="result" items="${dayVisitAvg}" varStatus="status">
                                                    <c:if test="${status.index % 5 == 0}">
                                                    <tr>
                                                    </c:if>
                                                        <td>
                                                            ${result.days}일
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${result.cnt ne '-'}">
                                                                    <fmt:formatNumber value="${result.cnt}" pattern="#,###" />
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${result.cnt}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    <c:if test="${status.index % 5 == 4 || status.last}">
                                                    </tr>
                                                    </c:if>
                                                </c:forEach>

                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mt_20">
                    <div class="chart-section-head">
                        <div class="title">일단위 순수 방문자수
                            <span class="current">
                                <span id="dayOrgMonth">
                                ${sysMonth}월
                                </span>
                                순수 방문자 수 총합 -
                                <span id="dayOrgTotalCnt">
                                    <fmt:formatNumber value="${totalDayOrgVisitAvg}" pattern="#,###" />
                                </span>
                            </span>
                        </div>
                        <div class="select-group">
                            <select id="dayOrgSYear" onchange="fnChangeDayOrg()">
                                <c:forEach var="items" begin="2023" end="${sysYear}" step="1" varStatus="status">
                                    <option value="${sysYear - status.count + 1 }"  <c:if test="${(sysYear - status.count + 1 ) eq sysYear}">selected</c:if> >${sysYear - status.count + 1 }년</option>
                                </c:forEach>
                            </select>
                            <select id="dayOrgSMonth" name="sMonth" class="sel" onchange="fnChangeDayOrg()">
                                <c:forEach var="items" begin="0" end="11" step="1" varStatus="status">
                                    <fmt:formatNumber var="noMonth" minIntegerDigits="2" value="${status.count}" type="number"/>
                                    <option value="${noMonth}" <c:if test="${noMonth eq sysMonth}">selected</c:if> >${status.count}월</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="chart-section-body">
                        <div id="chart2Div">
                            <div id="chart-2"></div>
                        </div>
                        <div class="chart-section-table type1-1">
                            <div>
                                <table cellspacing="0" class="update over">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                        <col width="10%">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                            <th>날짜</th>
                                            <th>방문자수</th>
                                        </tr>
                                    </thead>
                                    <tbody id="dayOrgTBody">
                                    <c:choose>
                                        <c:when test="${empty dayOrgVisitAvg}">
                                            <td colspan="10">방문자가 없습니다</td>
                                        </c:when>
                                        <c:otherwise>

                                            <c:forEach var="result" items="${dayOrgVisitAvg}" varStatus="status">
                                                <c:if test="${status.index % 5 == 0}">
                                                    <tr>
                                                </c:if>
                                                <td>
                                                        ${result.days}일
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${result.cnt ne '-'}">
                                                            <fmt:formatNumber value="${result.cnt}" pattern="#,###" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${result.cnt}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <c:if test="${status.index % 5 == 4 || status.last}">
                                                    </tr>
                                                </c:if>
                                            </c:forEach>

                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
    
                <div class="mt_20">
                    <div class="chart-section-head">
                        <div class="title">월단위 순수 방문자 수 (MAU)
                            <span class="current">
                                <span id="monthOrgYear">
                                ${sysYear}년
                                </span>
                                순수 방문자 수 총합 -
                                <span id="monthOrgTotalCnt">
                                    <fmt:formatNumber value="${totalMonthOrgVisitAvg}" pattern="#,###" />
                                </span>
                            </span>
                        </div>
                        <div class="select-group">
                            <select id="monthOrgSYear" onchange="fnChangeMonthOrg()">
                                <c:forEach var="items" begin="2023" end="${sysYear}" step="1" varStatus="status">
                                    <option value="${sysYear - status.count + 1 }"  <c:if test="${(sysYear - status.count + 1 ) eq sysYear}">selected</c:if> >${sysYear - status.count + 1 }년</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="chart-section-body">
                        <div id="chart3Div">
                            <div id="chart-3"></div>
                        </div>
                        <div class="chart-section-table type1-1">
                            <div>
                                <table cellspacing="0" class="update over">
                                    <colgroup>
                                        <col width="6.25%">
                                        <col width="6.25%">
                                        <col width="6.25%">
                                        <col width="6.25%">
                                        <col width="6.25%">
                                        <col width="6.25%">

                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>월</th>
                                            <th>방문자수</th>
                                 
                                            <th>월</th>
                                            <th>방문자수</th>
                                 
                                            <th>월</th>
                                            <th>방문자수</th>
                                        </tr>
                                    </thead>
                                    <tbody id="monthOrgTBody">
                                    <c:choose>
                                        <c:when test="${empty monthOrgVisitAvg}">
                                            <td colspan="10">방문자가 없습니다</td>
                                        </c:when>
                                        <c:otherwise>

                                            <c:forEach var="result" items="${monthOrgVisitAvg}" varStatus="status">
                                                <c:if test="${status.index % 3 == 0}">
                                                    <tr>
                                                </c:if>
                                                <td>
                                                        ${result.days}월
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${result.cnt ne '-'}">
                                                            <fmt:formatNumber value="${result.cnt}" pattern="#,###" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${result.cnt}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <c:if test="${status.index % 3 == 2 || status.last}">
                                                    </tr>
                                                </c:if>
                                            </c:forEach>

                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                            
                        </div>
                    </div>
                    
                </div>
                
            </div>


            <br><br>

        </div>
    </div>
</div>
</body>


</html>