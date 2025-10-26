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

});


function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	if(regxForm == 'frm'){
		if($('select[name=sUage]').val() < 0) {
			alert("알림!\n 연령 선택 하세요.");
		
			valid = false;
			return false;
		}
	}

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				switch ($obj.attr('name')){
				case 'teamName' :
					break;
				case 'address' :
					break;
				} */

				alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
				$obj.focus();
			
				valid = false;
				return false;
			}else{
				if(isRegExp($obj.val())){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				// remove blank
				var objVal = $obj.val();
				objVal = objVal.trim(objVal);
				$obj.val(objVal);
				
			}
		}
	);
    
    return valid;
}

const searchList = () => {

    const sDate = $("input[name=sDate]").val();
    const eDate = $("input[name=eDate]").val();

    if (sDate || eDate) {
        if (!sDate || !eDate) {
            alert('올바른 날짜를 입력해주세요.');
            return false;
        }
    }

    document.frm.submit();

}

const goMemberList = (state) => {

    $("input[name=memberState]").val(state);
    $("input[name=sDate]").val(null);
    $("input[name=eDate]").val(null);
    $('input[name=cp]').val(null);
    $('input[name=searchKeyword]').val(null);

    document.frm.submit();
}

const goRegist = () => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/memberRegist');
    $(newForm).appendTo('body').submit();
}

const goDetail = (value) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/memberDetail');
    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: value }));
    $(newForm).appendTo('body').submit();
}

//페이징 처리
function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
}

const fnDownload = () => {

    let jsonData = {
        'excelFlag': 'memberList'
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

const moveTab = (val) => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');

    let urlStr = "";
    if (val == 'info') {
        urlStr = "/memberDetail";
    }else if (val == 'education') {
        urlStr = "/memberEducation";
    }

    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: '${memberDetail.member_cd}' }));
    newForm.attr('action', urlStr);
    $(newForm).appendTo('body').submit();
}

const gotoModify = () => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Regist' }));
    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: '${memberDetail.member_cd}' }));
    newForm.attr('action', '/memberEducationModify');

    $(newForm).appendTo('body').submit();

}

const gotoMod = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Modify' }));
    newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: '${memberDetail.member_cd}' }));
    newForm.append($('<input/>', {type: 'hidden', name: 'educationFileId', value: val }));
    newForm.attr('action', '/memberEducationModify');

    $(newForm).appendTo('body').submit();
}

</script>
</head>
<body>
  <div class="wrapper" id="wrapper">
	<jsp:include page="../../common/menu.jsp" flush="true">
	<jsp:param name="page" value="main" />
	<jsp:param name="main" value="0" />
	</jsp:include>
    <div class="contents active">
        <div class="head">
            <div class="sub-menu">
                <h2><span></span>회원관리</h2>
            </div>
        </div>
        <div class="round body">
            <div class="body-head">
                <div class="search">
                    <button class="btn-large gray-o" onclick="moveTab('info')">기본정보</button>
                    <button class="btn-large default" onclick="moveTab('education')">피츠앤솔 정밀보행분석</button>
                </div>
                <br />
                <br />
                <br />
                <h4 class="view-title">피츠앤솔 정밀보행분석</h4>
            </div>
            <div class="body-head">
                <div class="others">
                    <a class="btn-large default btn-pop" data-id="update-area-add" onclick="gotoModify()">추가하기</a>
                </div>
            </div>
            <br />
            <br />
            <div class="scroll">
                <table cellspacing="0" class="update over">
                    <colgroup>
                        <col width="5%">
                        <col width="50%">
                        <col width="30%">
                        <col width="10%">
                        <col width="5%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>제목</th>
                            <th>파일</th>
                            <th>등록일</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty memberEducation}">
                            <tr>
                                <td id="idEmptyList" colspan="5">등록된 내용이 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="result" items="${memberEducation}" varStatus="status">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                            <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        ${result.title}
                                    </td>
                                    <td>
                                        ${result.eduFile.fileName}
                                    </td>
                                    <td>
                                        ${fn:substring(result.reg_date, 0, 10)}
                                    </td>
                                    <td>
                                        <button class="btn-large" onclick="gotoMod('${result.education_file_id}')">수정하기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
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
  </div>
  
</body>
</html>