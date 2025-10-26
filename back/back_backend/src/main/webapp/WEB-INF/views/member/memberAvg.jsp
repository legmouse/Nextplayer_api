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

    $(document).ready(function() {
        let daysJoinDay = [];
        let daysJoinCnt = [];

        <c:forEach var="item" items="${dayJoinMember}" varStatus="status">
        daysJoinDay.push("${item.days}일");
        daysJoinCnt.push("${item.cnt eq '-' ? 0 : item.cnt}");
        </c:forEach>

        let option1 = fnMakeLineChart(daysJoinDay, daysJoinCnt);

        let chart = new ApexCharts(document.querySelector("#chart-1"), option1);
        chart.render();

        let memberPosition = [];
        let positionCnt = [];

        <c:forEach var="item" items="${memberCntByPosition}" varStatus="status">
            memberPosition.push("${item.position}");
            positionCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option2 = fnMakeCircleChart(memberPosition, positionCnt, 'position');
        let chart2 = new ApexCharts(document.querySelector("#dount-2"), option2);
        chart2.render();

        let memberAgeGroup = [];
        let ageGroupCnt = [];

        <c:forEach var="item" items="${memberCntByAgeGroup}" varStatus="status">
        memberAgeGroup.push('${item.age_group}');
        ageGroupCnt.push(${item.cnt});
        </c:forEach>

        let option3 = fnMakeCircleChart(memberAgeGroup, ageGroupCnt, 'ageGroup');
        let chart3 = new ApexCharts(document.querySelector("#dount-3"), option3);
        chart3.render();

        let memberCertType = [];
        let certTypeCnt = [];

        <c:forEach var="item" items="${memberCntByCertType}" varStatus="status">
            memberCertType.push("${item.cert_type}");
            certTypeCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option4 = fnMakeCircleChart(memberCertType, certTypeCnt, 'certType');
        let chart4 = new ApexCharts(document.querySelector("#dount-1"), option4);
        chart4.render();

        let memberGender = [];
        let genderCnt = [];

        <c:forEach var="item" items="${memberCntByGender}" varStatus="status">
        memberGender.push("${item.gender}");
        genderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option5 = fnMakeCircleChart(memberGender, genderCnt, 'gender');
        let chart5 = new ApexCharts(document.querySelector("#dount-4"), option5);
        chart5.render();

        let memberPositionGender = [];
        let positionGenderCnt = [];
        let genderArr = [];
        let genderCnt2 = [];
        // 학부모 그래프
        <c:forEach var="item" items="${memberCntByPARENTS}" varStatus="status">
            memberPositionGender.push("${item.age_group}");
            positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByParentsGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option6 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'parents');
        let chart6 = new ApexCharts(document.querySelector("#pie-1"), option6);
        chart6.render();

        let option6_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart6_1 = new ApexCharts(document.querySelector("#pie-1-1"), option6_1);
        chart6_1.render();

        memberPositionGender = [];
        positionGenderCnt = [];
        genderArr = [];
        genderCnt2 = [];

        // 선수 그래프
        <c:forEach var="item" items="${memberCntByPLAYER}" varStatus="status">
        memberPositionGender.push("${item.age_group}");
        positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByPlayerGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option7 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'player');
        let chart7 = new ApexCharts(document.querySelector("#pie-2"), option7);
        chart7.render();

        let option7_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart7_1 = new ApexCharts(document.querySelector("#pie-2-1"), option7_1);
        chart7_1.render();

        memberPositionGender = [];
        positionGenderCnt = [];
        genderArr = [];
        genderCnt2 = [];

        // 학원/클럽 관계자 그래프
        <c:forEach var="item" items="${memberCntByACADEMY}" varStatus="status">
        memberPositionGender.push("${item.age_group}");
        positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByAcademyGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option8 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'academy');
        let chart8 = new ApexCharts(document.querySelector("#pie-3"), option8);
        chart8.render();

        let option8_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart8_1 = new ApexCharts(document.querySelector("#pie-3-1"), option8_1);
        chart8_1.render();

        memberPositionGender = [];
        positionGenderCnt = [];
        genderArr = [];
        genderCnt2 = [];

        // 레슨 선생님그래프
        <c:forEach var="item" items="${memberCntByTEACHER}" varStatus="status">
        memberPositionGender.push("${item.age_group}");
        positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByTeacherGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option9 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'teacher');
        let chart9 = new ApexCharts(document.querySelector("#pie-4"), option9);
        chart9.render();

        let option9_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart9_1 = new ApexCharts(document.querySelector("#pie-4-1"), option9_1);
        chart9_1.render();

        memberPositionGender = [];
        positionGenderCnt = [];
        genderArr = [];
        genderCnt2 = [];

        // 감독/코치 그래프
        <c:forEach var="item" items="${memberCntByDIRECTOR}" varStatus="status">
        memberPositionGender.push("${item.age_group}");
        positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByDirectorGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option10 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'director');
        let chart10 = new ApexCharts(document.querySelector("#pie-5"), option10);
        chart10.render();

        let option10_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart10_1 = new ApexCharts(document.querySelector("#pie-5-1"), option10_1);
        chart10_1.render();

        memberPositionGender = [];
        positionGenderCnt = [];
        genderArr = [];
        genderCnt2 = [];

        // 기타 그래프
        <c:forEach var="item" items="${memberCntByETC}" varStatus="status">
        memberPositionGender.push("${item.age_group}");
        positionGenderCnt.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        <c:forEach var="item" items="${memberCntByEtcGender}" varStatus="status">
        genderArr.push("${item.gender}");
        genderCnt2.push(${item.cnt eq '-' ? 0 : item.cnt});
        </c:forEach>

        let option11 = fnMakeSmallCircleChart(memberPositionGender, positionGenderCnt, 'etc');
        let chart11 = new ApexCharts(document.querySelector("#pie-6"), option11);
        chart11.render();

        let option11_1 = fnMakeSmallCircleGenderChart(genderArr, genderCnt2);
        let chart11_1 = new ApexCharts(document.querySelector("#pie-6-1"), option11_1);
        chart11_1.render();

    });

    const fnMakeLineChart = (days, cnt) => {

        let options = {
            series: [{
                name: '일단위 가입자수',
                data: cnt
            }],
            /*chart: {
                type: 'bar',
                stacked: true,
                height: 250,
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
                formatter: function (val) {
                    return val;
                },
                offsetY: -20,
                style: {
                    fontSize: '12px',
                    colors: ["#fff"]
                }
            },

            stroke: {
                show: true,
                width: 2,
                colors: ['transparent']
            },
            */

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

    const fnMakeCircleChart = (position, cnt, type) => {

        let colorsArr = [];

        if (type == "position") {
            colorsArr = ["#065eb6", "#13562b", "#ffa920", "#bc201b", "#2d2d2d", "#6d3f99"];
        } else if (type == 'ageGroup') {
            colorsArr = ["#371957", "#582c85", "#6d3f99", "#8453a2", "#b890c2", "#dbbedc", "#f3ebf5"];
        } else if (type == "certType") {
            colorsArr = ["#4ec538", "#fbdf03", "#000000", "#4070df"];
        } else if (type == "gender") {
            colorsArr = ["#6acdf4", "#f05a59"];
        }

        let options = {
            series: cnt,
            chart: {
                type: 'donut',
                height: 300,

            },

            dataLabels: {
            },
            labels : position,
            colors: colorsArr ,
            legend: {
                position: 'bottom'
            },
            responsive: [{
                breakpoint: 300,

                options: {
                    chart: {
                        width: '10%'
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }]
        };

        return options
    }

    const fnMakeSmallCircleChart = (position, cnt, type) => {

        let colorsArr = [];

        if (type == 'parents') {
            colorsArr = ["#065eb6", "#2e7bc9", "#569cd8", "#7ab4e6", "#94c4f3", "#a9d4fd", "#dae9f7"];
        } else if (type == 'player') {
            colorsArr = ["#13562b", "#287742", "#3d9855", "#4fbb68", "#63db7c", "#76fc95", "#d2f7db"];
        } else if (type == 'academy') {
            colorsArr = ["#ffa920", "#f3af31", "#f3b93d", "#fac866", "#fdd37f", "#fedc9c", "#fceac7"];
        } else if (type == 'teacher') {
            colorsArr = ["#bc201b", "#cf332c", "#d35348", "#e6756c", "#f39c92", "#fbc2b8", "#ffe1db"];
        } else if (type == 'director') {
            colorsArr = ["#371957", "#582c85", "#6d3f99", "#8453a2", "#b890c2", "#dbbedc", "#f3ebf5"];
        } else if (type == 'etc') {
            colorsArr = ["#2d2d2d", "#4f4f4f", "#6a6a6a", "#858585", "#9e9e9e", "#b9b9b9", "#e3e3e3"];
        }

        let options = {
            series: cnt,
            chart: {
                type: 'pie',
                height: 200,
            },

            dataLabels: {
            },
            labels : position,
            colors: colorsArr ,
            legend: {
                position: 'right'
            },
            responsive: [{
                breakpoint: 300,

                options: {
                    chart: {
                        width: '10%'
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }]
        };

        return options
    }

    const fnMakeSmallCircleGenderChart = (position, cnt) => {

        let options = {
            series: cnt,
            chart: {
                type: 'pie',
                height: 200,

            },

            dataLabels: {
            },
            labels: position,
            colors: ["#6acdf4", "#f05a59"],
            legend: {
                position: 'right'
            },
            responsive: [{
                breakpoint: 300,

                options: {
                    chart: {
                        width: '10%'
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }]
        };

        return options
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
            url: '/dayJoinAvg',
            data: param,
            success: function(res) {
                if (res.state == 'SUCCESS') {

                    $("#chart-1").remove();
                    $("#chart1Div").append('<div id="chart-1"></div>');

                    arr = []
                    $("#dayTotalCnt").text(res.data.totalMemberJoinCnt.toLocaleString());
                    $("#dayTBody").empty()

                    let str = "";
                    let days = [];
                    let cnt = [];
                    console.log('res : ',res)
                    for (let i = 0; i < res.data.dayJoinMember.length; i++) {
                        days.push(res.data.dayJoinMember[i].days + "일");
                        cnt.push(res.data.dayJoinMember[i].cnt === '-' ? 0 : res.data.dayJoinMember[i].cnt);

                        if (i % 5 == 0) {
                            str += "<tr>";
                        }
                        str += "<td>" +
                            res.data.dayJoinMember[i].days + "일" +
                            "</td>" +
                            "<td>";
                                if (res.data.dayJoinMember[i].cnt !== '-') {
                                    str += Number(res.data.dayJoinMember[i].cnt).toLocaleString();
                                } else {
                                    str += res.data.dayJoinMember[i].cnt;
                                }
                            "</td>";
                        if (i % 5 == 4) {
                            str += "</tr>";
                        }
                    }
                    let options = fnMakeLineChart(days, cnt);

                    $("#dayTBody").append(str);
                    $("#chart-1").empty();

                    var chart = new ApexCharts(document.querySelector("#chart-1"), options);
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
                <a class="btn-large gray-o" href="/visitAvg">방문통계</a>
                <a class="btn-large default" href="/memberAvg">회원통계</a>
                <hr class="mt_10 mb_10">
            </div>
            <div class="chart-section">
                <div>
                    <div class="chart-section-head">
                        <div class="title">일단위 가입자수
                            <span class="current">
                                <span id="dayMonth">
                                ${sysMonth}월
                                </span>
                                가입자 수 -
                                <span id="dayTotalCnt">
                                    <fmt:formatNumber value="${totalMemberJoinCnt}" pattern="#,###" />
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
                    <div class="chart-section-body">
                        <div id="chart1Div">
                            <div id="chart-1"></div>
                        </div>

                        <div class="chart-section-table type1-1">

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
                                    <c:when test="${empty dayJoinMember}">
                                        <td colspan="10">방문자가 없습니다</td>
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${dayJoinMember}" varStatus="status">
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
            <hr class="mt_20">
            <div class="chart-section mt_20 grid-type-1">
                <div>
                    <div class="title">계정 유형별 회원 통계</div>
                    <div id="dount-2" class="dount"></div>

                    <div class="chart-section-table type1-1">
                        <table cellspacing="0" class="update over mt_20">
                            <colgroup>
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>계정 유형</th>
                                    <th>회원수</th>
                                    <th>계정 유형</th>
                                    <th>회원수</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty memberCntByPosition}">
                                </c:when>
                                <c:otherwise>

                                    <c:forEach var="result" items="${memberCntByPosition}" varStatus="status">
                                        <c:if test="${status.index % 2 == 0}">
                                            <tr>
                                        </c:if>
                                        <td>
                                            ${result.position}
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
                                        <c:if test="${status.index % 2 == 1 || status.last}">
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="total">
                                    <td colspan="2">종합</td>
                                    <td colspan="2"><fmt:formatNumber value="${totalMemberCnt}" pattern="#,###" /></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div>
                    <div class="title">연령대별 회원 통계</div>
                    <div id="dount-3" class="dount"></div>

                    <div class="chart-section-table type1-1">
                        <table cellspacing="0" class="update over mt_20">
                            <colgroup>
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>연령대</th>
                                    <th>회원수</th>
                                    <th>연령대</th>
                                    <th>회원수</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty memberCntByAgeGroup}">
                                </c:when>
                                <c:otherwise>

                                    <c:forEach var="result" items="${memberCntByAgeGroup}" varStatus="status">
                                        <c:if test="${status.index % 2 == 0}">
                                            <tr>
                                        </c:if>
                                        <td>
                                            ${result.age_group}
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${result.cnt}" pattern="#,###" />
                                        </td>
                                        <c:if test="${status.index % 2 == 1 || status.last}">
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="total">
                                    <td colspan="2">종합</td>
                                    <td colspan="2"><fmt:formatNumber value="${totalMemberCnt}" pattern="#,###" /></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div>
                    <div class="title">가입 방식별 회원 통계 </div>
                    <div id="dount-1" class="dount"></div>

                    <div class="chart-section-table type1-1">
                        <table cellspacing="0" class="update over mt_20">
                            <colgroup>
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>가입경로</th>
                                    <th>회원수</th>
                                    <th>가입경로</th>
                                    <th>회원수</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty memberCntByCertType}">
                                </c:when>
                                <c:otherwise>

                                    <c:forEach var="result" items="${memberCntByCertType}" varStatus="status">
                                        <c:if test="${status.index % 2 == 0}">
                                            <tr>
                                        </c:if>
                                        <td>
                                                ${result.cert_type}
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
                                        <c:if test="${status.index % 2 == 1 || status.last}">
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="total">
                                    <td colspan="2">종합</td>
                                    <td colspan="2"><fmt:formatNumber value="${totalMemberCnt}" pattern="#,###" /></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                </div>
                <div>
                    <div class="title">성별 회원 통계</div>
                    <div id="dount-4" class="dount"></div>

                    <div class="chart-section-table type1-1">
                        <table cellspacing="0" class="update over mt_20">
                            <colgroup>
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                                <col width="25%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>성별</th>
                                    <th>회원수</th>
                                    <th>성별</th>
                                    <th>회원수</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty memberCntByCertType}">
                                </c:when>
                                <c:otherwise>

                                    <c:forEach var="result" items="${memberCntByGender}" varStatus="status">
                                        <c:if test="${status.index % 2 == 0}">
                                            <tr>
                                        </c:if>
                                        <td>
                                                ${result.gender}
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
                                        <c:if test="${status.index % 2 == 1 || status.last}">
                                            </tr>
                                        </c:if>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
                            </tbody>
                            <tfoot>
                                <tr class="total">
                                    <td colspan="2">종합</td>
                                    <td colspan="2"><fmt:formatNumber value="${totalMemberCnt}" pattern="#,###" /></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <hr class="mt_20">
            <p class="title mt_20 fw_9">계정 유형별 연령대</p>
            <div class="chart-section grid-type-1 mt_10">
                <div class="chart-pie-grid">
                    <div>
                        <p class="title">학부모</p>
                    </div>
                    <div></div>
                    <div>
                        <div id="pie-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>학부모 연령별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연령대</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByPARENTS}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByPARENTS}" varStatus="status">
                                        <tr>
                                            <td>
                                                    ${result.age_group}
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
                                        </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${parentsMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                                
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-1-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>학부모 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>성별</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByParentsGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByParentsGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                    ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${parentsMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="chart-pie-grid">
                    <div>
                        <p class="title">선수</p>
                    </div>
                    <div></div>
                    <div>
                        <div id="pie-2" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>선수 연령별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연령대</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByPLAYER}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByPLAYER}" varStatus="status">
                                            <tr>
                                                <td>
                                                    ${result.age_group}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${playerMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-2-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>선수 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>성별</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByPlayerGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByPlayerGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${playerMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="chart-pie-grid">
                    <div>
                        <p class="title">학원/클럽 관계자</p>
                    </div>
                    <div></div>
                    <div>

                        <div id="pie-3" class="dount"></div>

                        <div class="chart-section-table type1-1">
                            <p>학원/클럽 관계자 연령별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연령대</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByACADEMY}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByACADEMY}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.age_group}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${academyMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-3-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>학원/클럽 관계자 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>성별</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByAcademyGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByAcademyGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${academyMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="chart-pie-grid">
                    <div>
                        <p class="title">레슨 선생님</p>
                    </div>
                    <div></div>
                    <div>
                        <div id="pie-4" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>레슨 선생님 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연령대</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByTEACHER}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByTEACHER}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.age_group}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${teacherMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-4-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>레슨 선생님 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>성별</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByTeacherGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByTeacherGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${teacherMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="chart-pie-grid">
                    <div>
                        <p class="title">감독/코치</p>
                    </div>
                    <div></div>
                    <div>
                        <div id="pie-5" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>감독/코치 연령별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>연령대</th>
                                    <th>회원수</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByDIRECTOR}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByDIRECTOR}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.age_group}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                <tr class="total">
                                    <td>종합</td>
                                    <td><fmt:formatNumber value="${directorMemberCnt}" pattern="#,###" /></td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-5-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>감독/코치 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>성별</th>
                                    <th>회원수</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByDirectorGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByDirectorGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                <tr class="total">
                                    <td>종합</td>
                                    <td><fmt:formatNumber value="${directorMemberCnt}" pattern="#,###" /></td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="chart-pie-grid">
                    <div>
                        <p class="title">기타</p>
                    </div>
                    <div></div>
                    <div>
                        <div id="pie-6" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>기타 연령별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>연령대</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByETC}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByETC}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.age_group}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${etcMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div>
                        <div id="pie-6-1" class="dount"></div>
                        <div class="chart-section-table type1-1">
                            <p>기타 성별 통계</p>
                            <table cellspacing="0" class="update over">
                                <colgroup>
                                    <col width="50%">
                                    <col width="50%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>성별</th>
                                        <th>회원수</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty memberCntByEtcGender}">
                                    </c:when>
                                    <c:otherwise>

                                        <c:forEach var="result" items="${memberCntByEtcGender}" varStatus="status">
                                            <tr>
                                                <td>
                                                        ${result.gender}
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
                                            </tr>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                                <tfoot>
                                    <tr class="total">
                                        <td>종합</td>
                                        <td><fmt:formatNumber value="${etcMemberCnt}" pattern="#,###" /></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</body>
<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team"
    onsubmit="return false;">
    <input name="sFlag" type="hidden" value="10">
    <input name="cp" type="hidden" value="1">
    <input name="ageGroup" type="hidden" value="U18">
</form>
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="save_team">
    <input type="hidden" name="sFlag" value="2">
    <input type="hidden" name="teamId">
    <input type="hidden" name="emblem">
    <input name="ageGroup" type="hidden" value="U18">
</form>

</html>