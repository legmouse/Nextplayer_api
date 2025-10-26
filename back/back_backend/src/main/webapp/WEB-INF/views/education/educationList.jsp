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
	//메시지 출력
	var code = "${code}";
	var msg = decodeURIComponent("${msg}");
	if(!isEmpty(code) && !isEmpty(code)){
		alert('알림!\n'+ '['+msg+']은 중복된 광역명 입니다.\n 확인 후 다시 등록해 주세요.');
		gotoAgeGroup(ageGroup);
		return;
	}
	
	//연령대 선택
	var ageGroup = "${ageGroup}";
	//console.log('--- ageGroup :'+ ageGroup );
	if(isEmpty(ageGroup)){
		$("#U18").addClass("active");
	}else{
		$("#"+ageGroup).addClass("active");
		$("input[name=ageGroup]").val(ageGroup);
	}
	
	//검색
	var areaSearch = "${areaSearch}";
	if(!isEmpty(areaSearch)){
		var sUage = "${sUage}";
		$("select[name='sUage'] option[value='"+sUage+"']").prop("selected", "selected");
		$("input[name=sAreaName]").val("${sAreaName}");
	}
});

const gotoModify = () => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Regist' }));
    newForm.attr('action', '/educationModify');

    $(newForm).appendTo('body').submit();

}

const goDetail = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/educationDetail');
    newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: val }));
    $(newForm).appendTo('body').submit();
}

//검색 
function gotoSearch(){
	console.log('--- gotoSearch');
	
	if(formCheck("frm")){
		var szHtml = "<input name='areaSearch' type='hidden' value='1'> ";
		$("#frm").append(szHtml);
		document.frm.submit(); 
	}
}

function gotoPaging(cp) {
    /* var szHtml = "<input name='teamSearch' type='hidden' value='1'> ";
    $("#frm").append(szHtml); */
    $('input[name=cp]').val(cp);


    document.frm.submit();
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
  		  	<h2><span></span>교육관리 > 부모학교</h2>
        </div>
        <div class="others">
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
            <form name="frm" id="frm" method="post"  action="educationList" onsubmit="return false;">
            	<input name="cp" type="hidden" value="${cp}">
              <%--<input type="text" name="sAreaName" placeholder="광역명 입력">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>--%>
            </form>
          </div>
          <div class="others">
            <a class="btn-large default btn-pop" data-id="update-area-add" onclick="gotoModify()">교육 등록</a>
          </div>
        </div>
        <br />
        <br />
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="10%">
            <col width="55%">
            <col width="15%">
            <col width="15%">
            <col width="10%">
            <col width="5%">
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>제목</th>
              <th>승인 회원 수</th>
              <th>강연시간</th>
              <th>등록일</th>
              <th>조회수</th>
            </tr>
          </thead>
          <tbody>
          	<c:if test="${empty educationList }">
				<tr>
					<td id="idEmptyList" colspan="5">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${educationList}" varStatus="status">
			<tr onclick="goDetail('${result.education_id}')">
				<td>
					<c:choose>
	              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
	              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
	              	</c:choose>
				</td>
				<td>${result.title}</td>
				<td>${result.cnt}</td>
				<td>${result.play_time}</td>
				<td>${fn:substring(result.reg_date, 0, 10)}</td>
                <td>${result.view_cnt}</td>
			</tr>
			</c:forEach>
          </tbody>
        </table>
        <div class="pagination">
        <!-- paging -->
        <c:if test="${prev}">
            <a href='javascript:gotoPaging(${start -1});'><i class="xi-angle-left-min"></i></a>
        </c:if>
        <c:forEach var="i" begin="${start }" end="${end }">
            <c:choose>
                <c:when test="${i eq cp}">
                    <a href='javascript:gotoPaging(${i});' class="active">${i}</a>
                </c:when>
                <c:otherwise>
                    <a href='javascript:gotoPaging(${i});' >${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${next }">
            <a href='javascript:gotoPaging(${end +1});'><i class="xi-angle-right-min"></i></a>
        </c:if>
        </div>
      </div>
    </div>

</body>
</html>