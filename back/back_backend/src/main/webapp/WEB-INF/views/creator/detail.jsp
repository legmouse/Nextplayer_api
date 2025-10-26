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
  const fnModifyCreator = (val) => {

    const mediaType = "${mediaType}";

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/creator_modify');
    newForm.append($('<input/>', {type: 'hidden', name: 'mediaType', value: mediaType }));
    newForm.append($('<input/>', {type: 'hidden', name: 'creatorId', value: val }));

    $(newForm).appendTo('body').submit();

  }

  const fnDeleteCreator = (val) => {
    const mediaType = "${mediaType}";

    let formData = new FormData();

    formData.append('mediaType', mediaType);
    formData.append('method', 'delete');
    formData.append('creatorId', val);

    const confirmMsg = confirm("해당 크리에이터를 삭제 하시겠습니까?");

    if (confirmMsg) {
      $.ajax({
        type : 'POST',
        url : '/saveCreator' + mediaType,
        processData: false,
        contentType: false,
        dataType: 'json',
        data : formData,
        success : function(res) {
          if (res.state == 'success') {
            alert("삭제 되었습니다.");
            location.href = "/creator" + res.mediaType;
          } else {
            alert("실패 했습니다.");
            location.href = "/creator" + res.mediaType;
          }
        }
      });
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
                크리에이터 관리 > 영상정보
              </c:when>
              <c:when test="${mediaType eq 'News'}">
                크리에이터 관리 > 뉴스
              </c:when>
              <c:when test="${mediaType eq 'Blog'}">
                크리에이터 관리 > 블로그
              </c:when>
            </c:choose>
          </h4>
        </div>

        <div class="scroll">
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
              <th class="tl">활성/비활성</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${creatorInfo.use_flag eq '0'}">
                    활성
                  </c:when>
                  <c:when test="${creatorInfo.use_flag eq '1'}">
                    비활성
                  </c:when>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th class="tl">크리에이터 명</th>
              <td class="tl">
                ${creatorInfo.creator_name}
              </td>
            </tr>
            <tr>
              <th class="tl">이미지</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${!empty profileImg}">
                    <a href="/common/download.do?publicFileId=${profileImg.public_file_id}">
                      <img src='/resources/img/icon/${profileImg.file_ext}.gif' alt="" /> ${profileImg.file_name}
                    </a>
                  </c:when>
                  <c:otherwise>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th class="tl">배너</th>
              <td class="tl">
                <c:choose>
                  <c:when test="${!empty bannerImg}">
                    <a href="/common/download.do?publicFileId=${bannerImg.public_file_id}">
                      <img src='/resources/img/icon/${bannerImg.file_ext}.gif' alt="" /> ${bannerImg.file_name}
                    </a>
                  </c:when>
                  <c:otherwise>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th class="tl">URL</th>
              <td class="tl">
                ${creatorInfo.url_link}
              </td>
            </tr>
            <tr>
              <th class="tl">채널 ID</th>
              <td class="tl">
                ${creatorInfo.channelId}
              </td>
            </tr>

            </tbody>
          </table><br>

        </div>
        <br>
        <div class="tr w100">
          <a class="btn-large gray-o" onclick="location.href='/creator${mediaType}'">목록으로 이동</a>
          <a class="btn-large gray-o" onclick="fnDeleteCreator('${creatorInfo.creator_id}')">삭제 하기</a>
          <%--<a class="btn-large gray-o" onclick="fnRemoveMedia()">삭제 하기</a>--%>
          <a class="btn-large default" onclick="fnModifyCreator('${creatorInfo.creator_id}')">수정 하기</a>
        </div>

      </div>
    </div>

  </div>

</body>

</html>