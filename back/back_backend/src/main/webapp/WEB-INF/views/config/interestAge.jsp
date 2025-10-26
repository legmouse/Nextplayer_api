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
    $('input[name="selectedAge"]').on('click', function() {
        $('input[name="selectedAge"]').prop('checked', false);
        $(this).prop('checked', true);
    });
    $('input[name="hideAge"]').on('change', function() {
        /*var variableName = $(this).data('variable');
        if ($(this).is(':checked')) {
            $('#' + variableName).val("true");
        } else {
            $('#' + variableName).val("false");
        }*/
    });
});

const fnSaveInterestAge = () => {

    const uageId = $("input[name='selectedAge']:checked").val();

    const hideArr = [];

    const ageArr = [];
    <c:if test="${!empty uageList}">
        <c:forEach var="item" items="${uageList}">
            ageArr.push({'uage' : '${item.uage}', 'uageId' : '${item.uage_id}'});
        </c:forEach>
    </c:if>

    let hideChk = $("input[name='hideAge']:checked");

    ageArr.forEach(function(i) {
        let valueArr = [];
        hideChk.each(function() {
            let id = $(this).attr('id');
            let uage = $(this).attr('data-id');
            let value = $(this).val();
            if (i.uageId == uage) {
                valueArr.push(value);
            }
        });
        if (valueArr.length > 0) {
            hideArr.push({'uage' : i.uage, 'uageId' : i.uageId, 'valueArr' : valueArr});
        }
    });

    const confirmMsg = confirm("연령을 수정 하시겠습니까?");

    let param = {
        uageId : uageId,
        hideArr : hideArr
    }
    console.log(param)
    if (confirmMsg) {
        $.ajax({
            url : '/save_interestAge',
            type : 'POST',
            contentType: "application/json",
            data: JSON.stringify(param),
            success : function(res) {
                console.log(res);
                if (res.state == 'SUCCESS') {
                    alert('관심 연령이 수정 되었습니다.');
                    location.reload();
                } else {
                    alert('관심 연령 수정에 실패 했습니다.');
                    location.reload();
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
  		  	<h2><span></span>설정 > 관심 연령 설정</h2>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
        </div>
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="15%">
            <col width="15%">
            <col width="15%">
          </colgroup>
          <thead>
            <tr>
              <th>연령</th>
              <th>관심연령 관리</th>
              <th>노출 여부 관리</th>
            </tr>
          </thead>
          <tbody>
			<c:forEach var="result" items="${uageList}" varStatus="status">
			<tr>
				<td>
					${result.uage}
				</td>
				<td class="admin">
                    <input type="checkbox" name="selectedAge" id="ch_${result.uage_id}" value="${result.uage_id}" <c:if test="${result.interest_flag eq '1'}">checked</c:if>>
                    <label for ='ch_${result.uage_id}'></label>
                </td>
                <c:set var="modelString" value="${result.hide_age}" />
                <c:set var="stringArray" value="${fn:split(fn:substring(modelString, 1, fn:length(modelString)-1), ', ')}" />
                <td class="admin">
                    <c:choose>
                        <c:when test="${!empty result.hide_age}">
                            <c:set var="isCupSelected" value="false" />
                            <c:set var="isLeagueSelected" value="false" />
                            <c:set var="isTeamSelected" value="false" />
                            <c:forEach var="item" items="${result.hide_age}">
                                <c:if test="${item eq 'CUP'}">
                                    <c:set var="isCupSelected" value="true" />
                                </c:if>
                                <c:if test="${item eq 'LEAGUE'}">
                                    <c:set var="isLeagueSelected" value="true" />
                                </c:if>
                                <c:if test="${item eq 'TEAM'}">
                                    <c:set var="isTeamSelected" value="true" />
                                </c:if>
                            </c:forEach>

                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="CUP_${result.uage_id}" value="CUP" <c:if test="${isCupSelected eq true}">checked</c:if>><label for="CUP_${result.uage_id}">CUP</label>
                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="LEAGUE_${result.uage_id}" value="LEAGUE" <c:if test="${isLeagueSelected eq true}">checked</c:if>><label for="LEAGUE_${result.uage_id}">LEAGUE</label>
                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="TEAM_${result.uage_id}" value="TEAM" <c:if test="${isTeamSelected eq true}">checked</c:if>><label for="TEAM_${result.uage_id}">TEAM</label>

                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="CUP_${result.uage_id}" value="CUP" ><label for="CUP_${result.uage_id}">CUP</label>
                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="LEAGUE_${result.uage_id}" value="LEAGUE" ><label for="LEAGUE_${result.uage_id}">LEAGUE</label>
                            <input type="checkbox" name="hideAge" data-id="${result.uage_id}" id="TEAM_${result.uage_id}" value="TEAM" ><label for="TEAM_${result.uage_id}">TEAM</label>
                        </c:otherwise>
                    </c:choose>
                    <%--${fn:length(modelString)}
                    ${fn:length(stringArray)}--%>
                </td>
			</tr>
			</c:forEach>
          </tbody>
        </table>
        <div class="body-foot">
            <div class="others">
                <a class="btn-large default" onclick="fnSaveInterestAge();">관심 연령 수정</a>
            </div>
          </div>
      </div>
    </div>

</body>
</html>