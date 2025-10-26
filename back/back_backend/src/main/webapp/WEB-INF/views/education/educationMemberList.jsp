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
	
    document.frm.submit();

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

const deleteMember = (authId, memberCd) => {

    const confirmMsg = confirm('해당 회원을 삭제 하시겠습니까?');

    if (confirmMsg) {
        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/save_member');
        newForm.append($('<input/>', {type: 'hidden', name: 'sFlag', value: '2' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: '${params.educationId}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'cp', value: '${cp}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'sKeyword', value: '${params.sKeyword}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'authId', value: authId }));
        newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: memberCd }));
        $(newForm).appendTo('body').submit();
    }

}

function clearFrmData(regxForm){
    //console.log('-- clearFrmData regxForm : '+ regxForm);
    var $form = $("#"+regxForm);
    console.log('$form > ', $form);
    $form[0].reset();
}

const gotoAddPopUp = (el) => {
    clearFrmData("addFrm")
}

const fnSearchMember = () => {
    const mKeyword = $("input[name=mKeyword]").val();
    const educationId = '${params.educationId}';
    let param = {
        educationId : educationId,
        mKeyword  : mKeyword
    }

    $.ajax({
        type : 'POST',
        url : '/search_member',
        data: param,
        success: function(res) {
            console.log(res);
            if (res.state == 'success') {
                let str = "";
                if (res.data.length > 0) {
                    for (let i = 0; i < res.data.length; i++) {
                        str +=  "<tr>";
                        str +=      "<td>";
                        str +=          res.data[i].member_id;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          res.data[i].member_nickname;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          res.data[i].phone_no;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          res.data[i].email;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          res.data[i].member_name;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          res.data[i].birth_day;
                        str +=      "</td>";
                        str +=      "<td>";
                        str +=          "<input class='memberChk' type='checkbox' value='"+res.data[i].member_cd+"' name='ch" +i+"' id='ch"+i+"-"+i+"'>";
                        str +=          "<label for='ch"+i+"-"+i+"'></label>";
                        str +=      "</td>";
                        str +=  "</tr>";
                    }
                }
                $("#searchMemberTbody").empty();
                $("#searchMemberTbody").append(str);
            }
        }
    });

}

const gotoAddMember = () => {
    const chkBanner = $("input:checkbox[class='memberChk']:checked");

    let str = "";
    let param = {
        data: []
    };
    $(chkBanner).each(function(i) {

        param.data.push({memberCd: $(this).val()});
    });
    console.log('param : ', param);
    str = "<input type='text' name='data' value='"+JSON.stringify(param)+"')>";
    $("#appendMember").append(str);
    if (chkBanner.length > 0) {
        const confirmMsg = confirm('회원을 저장 하시겠습니까?');

        if(confirmMsg) {
            document.addFrm.submit();
        }
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
  		  	<h2><span></span>회원 관리</h2>
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
                  <button class="btn-large gray-o" onclick="moveTab('faq')">FAQ 관리</button>
                  <button class="btn-large default" onclick="moveTab('member')">회원 관리</button>
              </div>

          </div>
        </div>

        <div class="title">
          <h3>
              회원 관리
          </h3>
            <div class="search">
                <form name="frm" id="frm" method="post"  action="/educationMemberList" onsubmit="return false;">
                    <input type="hidden" name="educationId" value="${params.educationId}">
                    <input name="cp" type="hidden" value="${cp}">
                    <input type="text" name="sKeyword" placeholder="이름 / 이메일 검색" value="${params.sKeyword}" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
                    <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
                </form>
            </div>
        </div>
        <div class="body-head">
          <div class="others">
              <button class="btn-large gray-o btn-pop" data-id="update-member" onclick="gotoAddPopUp()">추가하기</button>
          </div>
        </div>
        <br />
        <br />
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="5%">
            <col width="40%">
            <col width="10%">
            <col width="20%">
            <col width="4%">
            <col width="4%">
            <col width="2%">
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>휴대폰 번호</th>
              <th>이메일</th>
              <th>이름</th>
              <th>닉네임</th>
              <th>생일</th>
              <th>등록일자</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
          	<c:if test="${empty educationMemberList }">
				<tr>
					<td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${educationMemberList}" varStatus="status">
			<tr>
                <td>
                    <c:choose>
                        <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                        <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                    </c:choose>
                </td>
                <td>${result.email}</td>
                <td>${result.phone_no}</td>
				<td>
					${result.member_name}
				</td>
				<td>${result.member_nickname}</td>

				<td>${result.birth_day}</td>
                <td>${fn:replace(result.reg_date, 'T', ' ')}</td>
				<td style="line-height:170%;">
                    <button class="btn-large gray-o" onclick="deleteMember('${result.education_auth_id}', '${result.member_cd}')">삭제하기</button>
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

    <div class="pop" id="update-member">
        <div style="height:auto;">
            <div style="height:auto;  width: 700px;">
                <div class="head">
                    회원 등록
                </div>
                <div class="head p10">

                    <input style="width: 50%"  type="text" id="mKeyword" name="mKeyword" placeholder="이름 / 이메일 / 핸드폰 번호를 입력 해주세요" onkeypress="if(event.keyCode == 13) {fnSearchMember(); return;}">
                    <a class="btn-large default" onclick="fnSearchMember()" >검색</a>
                </div>
                <form name="addFrm" id="addFrm" method="post" action="save_member">
                    <input type="hidden" name="sFlag" value="0">
                    <input type="hidden" name="educationId" value="${params.educationId}">
                    <input name="cp" type="hidden" value="${cp}">
                    <div id="appendMember">

                    </div>
                    <div class="body" style="padding:15px 20px;">
                        <table>
                            <colgroup>
                                <col width="20%">
                                <col width="*">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>
                                        ID
                                    </th>
                                    <th>
                                        닉네임
                                    </th>
                                    <th>
                                        휴대폰 번호
                                    </th>
                                    <th>
                                        이메일
                                    </th>
                                    <th>
                                        이름
                                    </th>
                                    <th>
                                        생일
                                    </th>
                                    <th>
                                        선택
                                    </th>
                                </tr>
                            </thead>
                            <tbody id="searchMemberTbody">

                            </tbody>
                        </table>
                    </div>
                </form>
                <div class="foot">
                    <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddMember();"><span>등록하기</span></a>
                    <a class="login btn-large btn-close-pop gray w100" onclick="closePop()"><span>취소</span></a>
                </div>
            </div>
        </div>
    </div>

</body>
</html>