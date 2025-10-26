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

const moveTab = (val) => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');

    let urlStr = "";
    if (val == 'info') {
        urlStr = "/educationDetail";
    }else if (val == 'question') {
        urlStr = "/educationQuestionList";
    } else if (val == 'faq') {
        urlStr = "/educationFaqList";
    } else if (val == 'member') {
        urlStr = "/educationMemberList";
    }

    newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: '${params.educationId}' }));
    newForm.attr('action', urlStr);
    //newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: val }));
    $(newForm).appendTo('body').submit();
}

function gotoPaging(cp) {
    /* var szHtml = "<input name='teamSearch' type='hidden' value='1'> ";
    $("#frm").append(szHtml); */
    $('input[name=cp]').val(cp);

    /* var area = $("select[name=sArea]").val();
    var teamType = $("select[name=sTeamType]").val();
    var leagueName = $("input[name=sLeagueName]").val();
    console.log('--- selArea : '+ selArea+', selTeam : '+ selTeam); */

    document.frm.submit();
}

const fnDownload = () => {

    let jsonData = {
        educationId: '${params.educationId}',
        'excelFlag': 'educationQuestion'
    };

    let excelUrl = "excelDownload";
    let request = new XMLHttpRequest();

    request.open('POST', excelUrl, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.responseType = 'blob';

    request.onload = function(e) {

        $("#page-loading").hide();

        let filename = "";
        let disposition = request.getResponseHeader('Content-Disposition');
        if (disposition && disposition.indexOf('attachment') !== -1) {
            let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            let matches = filenameRegex.exec(disposition);
            if (matches != null && matches[1])
                filename = decodeURI( matches[1].replace(/['"]/g, '') );
        }
        //console.log("FILENAME: " + filename);

        if (this.status === 200) {

            let blob = this.response;
            if(window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, filename);
            }else{
                let downloadLink = window.document.createElement('a');
                let contentTypeHeader = request.getResponseHeader("Content-Type");
                downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                downloadLink.download = filename;
                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);
            }

            //setTimeout(() => $('#pop-proof-1').modal('hide'), 3000);
        }

    };

    request.send(JSON.stringify(jsonData));

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
  		  	<h2><span></span>질문 관리</h2>
        </div>
        <div class="others">
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
              <div class="search">
                  <button class="btn-large gray-o" onclick="moveTab('info')">기본정보 수정</button>
                  <button class="btn-large default" onclick="moveTab('question')">질문 관리</button>
                  <button class="btn-large gray-o" onclick="moveTab('faq')">FAQ 관리</button>
                  <button class="btn-large gray-o" onclick="moveTab('member')">회원 관리</button>
              </div>
          </div>
        </div>

        <div class="title">
          <h3>
              질문 관리
          </h3>
        </div>
        <div class="body-head">
          <div class="others">
              <button class="btn-large gray-o" onclick="fnDownload()">질문 목록 엑셀 다운</button>
          </div>
        </div>
        <br />
        <br />
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="10%">
            <col width="15%">
            <col width="15%">
            <col width="15%">
            <col width="55%">
          </colgroup>
          <thead>
            <tr>
              <th>이름</th>
              <th>닉네임</th>
              <th>이메일</th>
              <th>전화번호</th>
              <th>내용</th>
            </tr>
          </thead>
          <tbody>
          	<c:if test="${empty educationQuestionList }">
				<tr>
					<td id="idEmptyList" colspan="5">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${educationQuestionList}" varStatus="status">
			<tr>
				<td>
					${result.member_name}
				</td>
				<td>${result.member_nickname}</td>
				<td>${result.email}</td>
				<td>${result.phone_no}</td>
				<td style="line-height:170%;">
                    <c:out value="${fn:replace(result.content, enter, '<br/>')}" escapeXml="false" />
                </td>
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
            <!-- <a><i class="xi-angle-left-min"></i></a>
            <a class="active">1</a>
            <a>2</a>
            <a>3</a>
            <a>4</a>
            <a>5</a>
            <a><i class="xi-angle-right-min"></i></a> -->
        </div>
      </div>


    </div>

</body>
</html>