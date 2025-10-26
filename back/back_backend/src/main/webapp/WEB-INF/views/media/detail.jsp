<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("enter", "\n"); %>
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

  const fnGoMediaList = () => {

    const mediaType = "${mediaType}";
    const subType = "${subType}"
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/media' + mediaType);
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));
    newForm.append($('<input/>', {type: 'hidden', name: 'subType', value: subType }));

    $(newForm).appendTo('body').submit();

  }

  const fnModifyMedia = (val) => {

    const mediaType = "${mediaType}";

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/modify' + mediaType);
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaId', value: val }));

    $(newForm).appendTo('body').submit();

  }

  const fnDeleteMedia = (val) => {
    const mediaType = "${mediaType}";

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/saveMedia' + mediaType);
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'delete' }));
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaId', value: val }));

    const confirmMsg = confirm("해당 미디어를 삭제 하시겠습니까?");

    if (confirmMsg) {
      $(newForm).appendTo('body').submit();
    }

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
          <h2><span></span>미디어 관리</h2>

        </div>
      </div>
      <div class="round body">

        <div class="body-head">

          <h4 class="view-title">
            <c:choose>
              <c:when test="${mediaType eq 'Video'}">
                미디어 관리 > 동영상 > 상세페이지
              </c:when>
              <c:when test="${mediaType eq 'News'}">
                미디어 관리 > 뉴스 > 상세페이지
              </c:when>
              <c:when test="${mediaType eq 'Blog'}">
                미디어 관리 > 블로그 > 상세페이지
              </c:when>
              <c:when test="${mediaType eq 'Game'}">
                미디어 관리 > 경기영상 > 상세페이지
              </c:when>
            </c:choose>
          </h4>
        </div>

        <div class="scroll">
          <div class="title">
            <h3>
              <c:choose>
                <c:when test="${mediaType eq 'Video' || mediaType eq 'Game'}">
                  영상
                </c:when>
              </c:choose>
            </h3>

          </div>
          <c:choose>
            <c:when test="${mediaType eq 'Video' or mediaType eq 'Game'}">
              <table cellspacing="0" class="update view">
                <colgroup>
                  <col width="20%">
                  <col width="*">

                </colgroup>
                <thead>
                <tr>
                  <th class="tl">
                    <c:choose>
                      <c:when test="${mediaType eq 'Video' || mediaType eq 'Game'}">
                        영상
                      </c:when>
                    </c:choose>
                  </th>
                </tr>
                </thead>
                <tbody>
                <tr>

                  <td class="tl">
                    <iframe width="560" height="315"
                            src="${mediaInfo.url_link}"
                            title="YouTube video player" frameborder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                            allowfullscreen></iframe>
                  </td>
                </tr>
                </tbody>
              </table><br>
            </c:when>
          </c:choose>

          <div class="title">
            <h3>
              기본 정보
            </h3>
          </div>
          <table cellspacing="0" class="update view">
            <colgroup>
              <col width="*">

            </colgroup>
            <tbody>
            <tr>
              <th class="tl">
                <c:choose>
                  <c:when test="${mediaType eq 'Video'}">영상 타입</c:when>
                  <c:when test="${mediaType eq 'News'}">뉴스 타입</c:when>
                  <c:when test="${mediaType eq 'Blog'}">블로그 타입</c:when>
                </c:choose>
              </th>
              <td class="tl">
                <c:if test="${mediaType ne 'Game'}">
                  <c:forEach var="data" items="${menuList}" varStatus="status">
                    <c:if test="${mediaInfo.sub_type eq data.code_value}">
                      ${data.category_name}
                    </c:if>
                  </c:forEach>
                </c:if>
              </td>
            </tr>
            <tr>
              <th class="tl">활성/비활성</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${mediaInfo.use_flag eq '0'}">
                    활성
                  </c:when>
                  <c:when test="${mediaInfo.use_flag eq '1'}">
                    비활성
                  </c:when>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th class="tl">메인 비활성/활성</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${mediaInfo.main_flag eq '0'}">
                    비활성
                  </c:when>
                  <c:when test="${mediaInfo.main_flag eq '1'}">
                    활성
                  </c:when>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th class="tl">제목</th>
              <td class="tl">
                ${mediaInfo.title}
              </td>
            </tr>
            <tr>
              <th class="tl">링크</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${mediaType eq 'News'}">
                    <a href="${mediaInfo.url_link}" target="_blank">${mediaInfo.url_link}</a>
                  </c:when>
                  <c:otherwise>
                    ${mediaInfo.url_link}
                  </c:otherwise>
                </c:choose>

              </td>
            </tr>
            <tr>
              <th class="tl">이미지 링크</th>
              <td class="tl">
                  <a href="${mediaInfo.img_url}" target="_blank">${mediaInfo.img_url}</a>
              </td>
            </tr>

            <c:choose>
              <c:when test="${mediaType eq 'Game'}">
                  <tr>
                    <th class="tl">구분</th>
                    <td class="tl">
                      <c:choose>
                          <c:when test="${mediaInfo.type eq '0'}">
                            하이라이트
                          </c:when>
                          <c:when test="${mediaInfo.type eq '1'}">
                            다시보기
                          </c:when>
                      </c:choose>
                    </td>
                  </tr>
              </c:when>
            </c:choose>
            <tr>
              <th class="tl">참고 링크</th>
              <td class="tl">
                ${mediaInfo.ref_url}
              </td>
            </tr>

            <c:if test="${mediaType eq 'Video' || mediaType eq 'Game'}">
              <tr>
                <th class="tl">내용</th>
                <td class="tl">
                  <c:out value="${fn:replace(mediaInfo.content, enter, '<br/>')}" escapeXml="false" />
                </td>
              </tr>
              <tr>
                <th class="tl">
                  업로드 일자
                </th>
                <td class="tl">${mediaInfo.submit_date}</td>
              </tr>
            </c:if>
            <c:if test="${mediaType eq 'News' || mediaType eq 'Blog'}">
              <tr>
                <th class="tl">출처</th>
                <td class="tl">${mediaInfo.source}</td>
              </tr>
              <tr>
                <th class="tl">
                  <c:choose>
                    <c:when test="${mediaType eq 'News'}">기사 </c:when>
                    <c:when test="${mediaType eq 'Blog'}">블로그 </c:when>
                  </c:choose>
                  등록일
                </th>
                <td class="tl">${mediaInfo.submit_date}</td>
              </tr>
              <tr>
                <th class="tl">요약</th>
                <td class="tl" style="line-height: 2.1;">
                    <c:out value="${fn:replace(mediaInfo.summary, enter, '<br/>')}" escapeXml="false" />
                </td>
              </tr>
              <tr>
                <th class="tl">내용</th>
                <td class="tl" style="line-height: 2.1;">
                    <c:out value="${fn:replace(mediaInfo.content, enter, '<br/>')}" escapeXml="false" />
                </td>
              </tr>
            </c:if>
            <tr>
              <th class="tl">작성자</th>
              <td class="tl">
                ${mediaInfo.creator_name}
              </td>
            </tr>
            </tbody>
            <c:if test="${mediaType eq 'Game' || mediaType eq 'Video'}">
              <tr>
                <th class="tl">이미지 링크 노출</th>
                <td class="tl">
                  <c:choose>
                    <c:when test="${mediaInfo.show_flag eq '0'}">
                      노출 안함
                    </c:when>
                    <c:when test="${mediaInfo.show_flag eq '1'}">
                      노출
                    </c:when>
                  </c:choose>
                </td>
              </tr>
            </c:if>
          </table><br>

          <c:choose>
            <c:when test="${mediaType eq 'Game'}">
              <div class="title">
                <h3>
                  등록 대회
                </h3>
              </div>
              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="*">
                  <col width="*">
                  <col width="*">
                  <col width="*">
                  <col width="*">

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
                    대회명
                  </th>
                  <th rowspan="2">
                    대회일정
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
                <tbody>
                <c:choose>
                  <c:when test="${empty cupMediaList}">
                    <tr>
                      <td colspan="11">
                        등록된 대회가 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="cupList" items="${cupMediaList}" varStatus="index">
                      <tr>
                        <td>${cupList.cup_name}</td>
                        <td>${fn:substring(cupList.play_sdate, 0, 10)} ~ ${fn:substring(cupList.play_edate, 0, 10)}</td>
                        <td>${cupList.playdate}</td>
                        <td>${cupList.ptime}</td>
                        <td>${cupList.place}</td>
                        <td>
                            ${cupList.home}
                          <c:choose>
                            <c:when test="${empty cupList.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${cupList.home_emblem}" class="logo"></c:otherwise>
                          </c:choose>
                        </td>
                        <td>${cupList.home_score}</td>
                        <td>${cupList.home_pk}</td>
                        <td>${cupList.away_pk}</td>
                        <td>${cupList.away_score}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty cupList.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${cupList.away_emblem}" class="logo"></c:otherwise>
                          </c:choose>
                            ${cupList.away}
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table><br>
            </c:when>
            <c:when test="${mediaType eq 'News'}">
              <div class="title">
                <h3>
                  등록 대회
                </h3>
              </div>
              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="*">
                  <col width="*">
                </colgroup>
                <thead>
                <tr>
                  <th rowspan="2">
                    대회명
                  </th>
                  <th rowspan="2">
                    대회일정
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty cupMediaList}">
                    <tr>
                      <td colspan="2">
                        등록된 대회가 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="cupList" items="${cupMediaList}" varStatus="index">
                      <tr>
                        <td>${cupList.cup_name}</td>
                        <td>${fn:substring(cupList.play_sdate, 0, 10)} ~ ${fn:substring(cupList.play_edate, 0, 10)}</td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table><br>
            </c:when>
          </c:choose>

          <%--<p class="summary">등록된 리그가 없습니다.</p>--%>
          <c:choose>
            <c:when test="${mediaType eq 'Game'}">
              <div class="title">
                <h3>
                  등록 리그
                </h3>
              </div>
              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="*">
                  <col width="*">
                  <col width="*">
                  <col width="*">
                  <col width="*">

                  <col width="15%">
                  <col width="5%">
                    <%--<col width="5%">--%>
                    <%--<col width="5%">--%>
                  <col width="5%">
                  <col width="15%">
                </colgroup>
                <thead>
                <tr>
                  <th rowspan="2">
                    리그명
                  </th>
                  <th rowspan="2">
                    대회일정
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
                    <%--<th>PK</th>--%>
                    <%--<th>PK</th>--%>
                  <th>점수</th>
                  <th>어웨이팀</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty leagueMediaList}">
                    <tr>
                      <td colspan="11">
                        등록된 리그가 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="leagueList" items="${leagueMediaList}" varStatus="index">
                      <tr>
                        <td>${leagueList.league_name}</td>
                        <td>${fn:substring(leagueList.play_sdate, 0, 10)} ~ ${fn:substring(leagueList.play_edate, 0, 10)}</td>
                        <td>${leagueList.playdate}</td>
                        <td>${leagueList.ptime}</td>
                        <td>${leagueList.place}</td>
                        <td>
                            ${leagueList.home}
                          <c:choose>
                            <c:when test="${empty leagueList.home_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${leagueList.home_emblem}" class="logo"></c:otherwise>
                          </c:choose>
                        </td>
                        <td>${leagueList.home_score}</td>
                          <%--<td>${leagueList.home_pk}</td>
                          <td>${leagueList.away_pk}</td>--%>
                        <td>${leagueList.away_score}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty leagueList.away_emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${leagueList.away_emblem}" class="logo"></c:otherwise>
                          </c:choose>
                            ${leagueList.away}
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table>
              <br>
            </c:when>
            <c:when test="${mediaType eq 'News'}">
              <div class="title">
                <h3>
                  등록 리그
                </h3>
              </div>
              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="*">
                  <col width="*">
                </colgroup>
                <thead>
                <tr>
                  <th rowspan="2">
                    리그명
                  </th>
                  <th rowspan="2">
                    대회일정
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty leagueMediaList}">
                    <tr>
                      <td colspan="2">
                        등록된 리그가 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="leagueList" items="${leagueMediaList}" varStatus="index">
                      <tr>
                        <td>${leagueList.league_name}</td>
                        <td>${fn:substring(leagueList.play_sdate, 0, 10)} ~ ${fn:substring(leagueList.play_edate, 0, 10)}</td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table>
              <br>
            </c:when>
          </c:choose>

          <c:choose>
            <c:when test="${mediaType eq 'Game'}">
              <div class="title">
                <h3>
                  등록 학원/클럽
                </h3>
              </div>

              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="55px">

                </colgroup>
                <thead>
                <tr>
                  <th>광역</th>
                  <th>엠블럼</th>
                  <th>구분</th>
                  <th>사용명칭</th>
                  <th>정식명칭</th>
                  <th>소재지</th>
                  <th>활성여부</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty teamMediaList}">
                    <tr>
                      <td colspan="7">
                        등록된 팀이 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="teamList" items="${teamMediaList}" varStatus="index">
                      <tr>
                        <td>${teamList.area_name}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty teamList.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${teamList.emblem}" class="logo"></c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${teamList.team_type eq '0'}">
                              <span class="label blue">학원</span>
                            </c:when>
                            <c:when test="${teamList.team_type eq '1'}">
                              <span class="label green">클럽</span>
                            </c:when>
                            <c:when test="${teamList.team_type eq '2'}">
                              <span class="label red">유스</span>
                            </c:when>
                          </c:choose>
                        </td>
                        <td>${teamList.nick_name}</td>
                        <td>${teamList.team_name}</td>
                        <td class="tl">${teamList.addr}</td>
                        <td>
                          <c:choose>
                            <c:when test="${teamList.use_flag eq '0'}">
                              활성
                            </c:when>
                            <c:when test="${teamList.use_flag eq '1'}">
                              비활성
                            </c:when>
                          </c:choose>
                        </td>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table>
            </c:when>
            <c:when test="${mediaType eq 'News'}">
              <div class="title">
                <h3>
                  등록 학원/클럽
                </h3>
              </div>

              <table cellspacing="0" class="update mt_10">
                <colgroup>
                  <col width="55px">

                </colgroup>
                <thead>
                <tr>
                  <th>광역</th>
                  <th>엠블럼</th>
                  <th>구분</th>
                  <th>사용명칭</th>
                  <th>정식명칭</th>
                  <th>소재지</th>
                  <th>활성여부</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty teamMediaList}">
                    <tr>
                      <td colspan="7">
                        등록된 팀이 없습니다.
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="teamList" items="${teamMediaList}" varStatus="index">
                      <tr>
                        <td>${teamList.area_name}</td>
                        <td>
                          <c:choose>
                            <c:when test="${empty teamList.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
                            <c:otherwise><img src="/NP${teamList.emblem}" class="logo"></c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${teamList.team_type eq '0'}">
                              <span class="label blue">학원</span>
                            </c:when>
                            <c:when test="${teamList.team_type eq '1'}">
                              <span class="label green">클럽</span>
                            </c:when>
                            <c:when test="${teamList.team_type eq '2'}">
                              <span class="label red">유스</span>
                            </c:when>
                          </c:choose>
                        </td>
                        <td>${teamList.nick_name}</td>
                        <td>${teamList.team_name}</td>
                        <td class="tl">${teamList.addr}</td>
                        <td>
                          <c:choose>
                            <c:when test="${teamList.use_flag eq '0'}">
                              활성
                            </c:when>
                            <c:when test="${teamList.use_flag eq '1'}">
                              비활성
                            </c:when>
                          </c:choose>
                        </td>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
                </tbody>
              </table>
            </c:when>
          </c:choose>
        </div>
        <br>
        <div class="tr w100">
          <a class="btn-large gray-o" onclick="fnGoMediaList()">목록으로 이동</a>
          <a class="btn-large gray-o" onclick="fnDeleteMedia('${mediaInfo.media_id}')">삭제 하기</a>
          <%--<a class="btn-large gray-o" onclick="fnRemoveMedia()">삭제 하기</a>--%>
          <a class="btn-large default" onclick="fnModifyMedia('${mediaInfo.media_id}')">수정 하기</a>
        </div>

      </div>
    </div>

  </div>

</body>

</html>