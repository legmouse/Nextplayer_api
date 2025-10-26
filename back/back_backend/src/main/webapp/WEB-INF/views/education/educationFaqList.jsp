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

function clearFrmData(regxForm){
    //console.log('-- clearFrmData regxForm : '+ regxForm);
    var $form = $("#"+regxForm);
    console.log('$form > ', $form);
    $form[0].reset();
}

const gotoModPopUp = (el) => {
    clearFrmData("modFrm")
    clearFrmData("addFrm")
    $('input[name=faqId]').val($(el).attr('data-faqId'));
    $('input[name=title]').val($(el).attr('data-title'));
    $('textarea[name=question]').val($(el).attr('data-question').replaceAll('<br/>', '\n'));
    $('textarea[name=answer]').val($(el).attr('data-answer').replaceAll('<br/>', '\n'));
}

const openDetailPop = (el) => {
    $("#detailTitle").empty();
    $("#detailQuestion").empty();
    $("#detailAnswer").empty();

    $("#detailTitle").append($(el).attr("data-title"));
    $("#detailQuestion").append($(el).attr("data-question"));
    $("#detailAnswer").append($(el).attr("data-answer"));
}

const gotoAddFaq = () => {

    const confirmMsg = confirm("FAQ를 등록 하시겠습니까?");

    document.addFrm.submit();

    /*if (confirmMsg) {
        const educationId = $("input[name='educationId']").val();
        const foreignType = $("input[name='foreignType']").val();
        const title = $("input[name='title']").val();
        const question = $("textarea[name='question']").val();
        const answer = $("textarea[name='answer']").val();

        let param = {
            educationId: educationId,
            foreignType: foreignType,
            title : title,
            question: question,
            answer: answer
        }

        $.ajax({
            type : 'POST',
            url : '/save_faq',
            data: param,
            success: function(res) {
                if (res.state == 'success') {
                    alert('저장 되었습니다.');
                    location.reload();
                } else {
                    alert('저장에 실패했습니다.');
                    location.reload();
                }
            }
        })
    }*/


}

const showFaqPopup = () => {
    clearFrmData("addFrm");
}

const gotoModFaq = () => {

    const confirmMsg = confirm("FAQ를 수정 하시겠습니까?");

    document.modFrm.submit();

    /*if (confirmMsg) {
        const educationId = $("input[name='educationId']").val();
        const foreignType = $("input[name='foreignType']").val();
        const title = $("input[name='title']").val();
        const question = $("textarea[name='question']").val();
        const answer = $("textarea[name='answer']").val();

        let param = {
            educationId: educationId,
            foreignType: foreignType,
            title : title,
            question: question,
            answer: answer
        }

        $.ajax({
            type : 'POST',
            url : '/save_faq',
            data: param,
            success: function(res) {
                if (res.state == 'success') {
                    alert('저장 되었습니다.');
                    location.reload();
                } else {
                    alert('저장에 실패했습니다.');
                    location.reload();
                }
            }
        })
    }*/


}

const fnDeleteFaq = (val) => {
    const confirmMsg = confirm("FAQ를 삭제 하시겠습니까?");

    if (confirmMsg) {
        $('input[name=faqId]').val(val);
        document.delFrm.submit();
    }
}

const closePop = () => {
    clearFrmData("modFrm")
    clearFrmData("addFrm")
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
  		  	<h2><span></span>FAQ 관리</h2>
        </div>
        <div class="others">
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
              <div class="search">
                  <button class="btn-large gray-o" onclick="moveTab('info')">기본정보 수정</button>
                  <button class="btn-large gray-o" onclick="moveTab('question')">질문 관리</button>
                  <button class="btn-large default" onclick="moveTab('faq')">FAQ 관리</button>
                  <button class="btn-large gray-o" onclick="moveTab('member')">회원 관리</button>
              </div>
          </div>
        </div>

        <div class="title">
          <h3>
              FAQ
          </h3>
        </div>
        <div class="body-head">
            <div class="others">
                <button class="btn-large gray-o btn-pop" data-id="update-faq" onclick="showFaqPopup()">FAQ 등록</button>
            </div>
        </div>
        <br />
        <br />
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="5%">
            <col width="500px">
            <col width="500px">
            <col width="5%">
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>질문</th>
              <th>답변</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
          	<c:if test="${empty educationFaqList }">
				<tr>
					<td id="idEmptyList" colspan="3">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${educationFaqList}" varStatus="status">
			<tr>
				<td>
                    <c:choose>
                        <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                        <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                    </c:choose>
				</td>
				<td class="btn-pop" data-id="detail-faq" data-title="${result.title}" data-question="<c:out value="${fn:replace(result.question, enter, '<br/>')}" escapeXml="false" />" data-answer="<c:out value="${fn:replace(result.answer, enter, '<br/>')}" escapeXml="false" />" ><c:out value="${fn:replace(result.question, enter, '<br/>')}" escapeXml="false" /></td>
				<td><c:out value="${fn:replace(result.answer, enter, '<br/>')}" escapeXml="false" /></td>
				<td>
                    <%--<button class="btn-large gray-o btn-pop" data-id="modify-update-faq" onclick="gotoModPopUp('${result.faq_id}', '${result.title}', '<c:out value="${fn:replace(result.question, enter, '<br/>')}" escapeXml="false" />', '<c:out value="${fn:replace(result.answer, enter, '<br/>')}" escapeXml="false" />')">수정하기</button>--%>
                    <button class="btn-large gray-o btn-pop" data-id="modify-update-faq" data-faqId="${result.faq_id}" data-title="${result.title}" data-question="<c:out value="${fn:replace(result.question, enter, '<br/>')}" escapeXml="false" />" data-answer="<c:out value="${fn:replace(result.answer, enter, '<br/>')}" escapeXml="false" />" onclick="gotoModPopUp(this)">수정하기</button>
                    |
                    <button class="btn-large red" onclick="fnDeleteFaq('${result.faq_id}')">삭제하기</button>
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
        </div>
      </div>


    </div>



    <div class="pop" id="update-faq">
        <div style="height:auto;">
            <div style="height:auto;  width: 700px;">
                <div class="head">
                    FAQ 등록
                </div>
                <form name="addFrm" id="addFrm" method="post" action="save_faq">
                <input type="hidden" name="sFlag" value="0">
                <input type="hidden" name="educationId" value="${params.educationId}">
                <input type="hidden" name="foreignType" value="Education">
                <input type="hidden" name="faqId" value="">
                <input name="cp" type="hidden" value="${cp}">
                <div class="body" style="padding:15px 20px;">
                    <table>
                        <colgroup>
                            <col width="20%">
                            <col width="*">
                        </colgroup>
                        <tbody>
                            <tr>
                                <th class="tl">제목</th>
                                <td>
                                    <input type="text" name="title">
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">질문</th>
                                <td>
                                    <textarea type="text" name="question" placeholder=""></textarea>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">답변</th>
                                <td>
                                    <textarea type="text" name="answer" placeholder=""></textarea>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                </form>
                <div class="foot">
                    <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddFaq();"><span>등록하기</span></a>
                    <a class="login btn-large btn-close-pop gray w100" onclick="closePop()"><span>취소</span></a>
                </div>
            </div>
        </div>
    </div>

    <div class="pop" id="modify-update-faq">
        <div style="height:auto;">
            <div style="height:auto;  width: 700px;">
                <div class="head">
                    FAQ 수정
                </div>
                <form name="modFrm" id="modFrm" method="post" action="save_faq">
                    <input type="hidden" name="sFlag" value="1">
                    <input type="hidden" name="educationId" value="${params.educationId}">
                    <input type="hidden" name="foreignType" value="Education">
                    <input type="hidden" name="faqId" value="">
                    <input name="cp" type="hidden" value="${cp}">
                    <div class="body" style="padding:15px 20px;">
                        <table>
                            <colgroup>
                                <col width="20%">
                                <col width="*">
                            </colgroup>
                            <tbody>
                            <tr>
                                <th class="tl">제목</th>
                                <td>
                                    <input type="text" name="title">
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">질문</th>
                                <td>
                                    <textarea type="text" name="question" placeholder=""></textarea>
                                </td>
                            </tr>
                            <tr>
                                <th class="tl">답변</th>
                                <td>
                                    <textarea type="text" name="answer" placeholder=""></textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
                <div class="foot">
                    <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoModFaq();"><span>수정하기</span></a>
                    <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                </div>
            </div>
        </div>
    </div>

    <form name="delFrm" id="delFrm" method="post" action="save_faq">
        <input type="hidden" name="sFlag" value="2">
        <input type="hidden" name="faqId" value="">
        <input type="hidden" name="educationId" value="${params.educationId}">
    </form>

</body>
</html>