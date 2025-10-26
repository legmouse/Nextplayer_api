<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
	$("input[name=sKeyword]").val("${sKeyword}");

    $("#excelFile").on("click", function () {
        $("#resultDiv").remove();
    });

});


//페이징 처리
function gotoPaging(cp) {
	$('input[name=cp]').val(cp);
	document.frm.submit();
}

//검색 
function gotoSearch(){
    $("input[name=cp]").val(1);
    document.frm.submit();
}


//팀 상세 요약 이동
function gotoTeamGroupDet(teamId){
	$('#tmdfrm input[name="teamGroupId"]').val(teamId);
	document.tmdfrm.submit();
}

function clearFrmData(regxForm){
    //console.log('-- clearFrmData regxForm : '+ regxForm);
    var $form = $("#"+regxForm);
    $form[0].reset();
}

function showTeamPopup(){
    clearFrmData("addFrm");
}

function gotoAdd(){
    document.addFrm.submit();
}

function gotoModPopup(idx, groupName, useFlag ){
    clearFrmData("modFrm");

    $('input[name=groupName]').val(groupName);
    $('input:radio[name=useFlag]:input[value='+useFlag+']').attr('checked', true);
    $('input[name=teamGroupId]').val(idx);

}

function gotoMod(){
    document.modFrm.submit();
}

function gotoDel(idx){
    const confirmMsg = confirm("해당 데이터를 비활성 처리 하시겠습니까?");

    if (confirmMsg) {

        $.ajax({
            type : 'POST',
            url : '/remove_teamGroup',
            data: {teamGroupId : idx},
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    alert('비활성 처리 되었습니다.');
                    location.reload();
                } else {
                    alert('비활성 처리에 실패 했습니다.');
                    location.reload();
                }
            }
        })
    }
}

const gotoAddExcel = () => {
    var formData = new FormData();

    let excelFile = $("#excelFile")[0];

    formData.append("excelFile", excelFile.files[0]);

    $.ajax({
        type: 'POST',
        url : '/teamGroupExcelUpload',
        processData: false,
        contentType: false,
        dataType: 'json',
        data: formData,
        success: function(res) {
            console.log(res);
            if (res.state == 'SUCCESS') {
                let str = "<div id='resultDiv'>" +
                            " 등록된 팀 수 : " + res.data.success + "<br />";
                            if (res.data.teamFailList.length > 0) {
                    str +=  " 팀 등록 실패 이유 : <br/>";
                                for (let i = 0; i < res.data.teamFailList.length; i++) {
                                console.log('i > ', i);
                                console.log('res.data.teamFailList[i] > ', res.data.teamFailList[i])
                    str +=  res.data.teamFailList[i].reason + ", " + res.data.teamFailList[i].data.uage + " / " + res.data.teamFailList[i].data.nickName + "<br />";
                                }
                            }
                            if (res.data.groupFailList.length > 0) {
                    str +=  " 그룹 등록 실패 이유 : <br/>";
                                for (let i = 0; i < res.data.groupFailList.length; i++) {
                    str +=  res.data.groupFailList[i].reason + ", " + res.data.groupFailList[i].data + "<br/>";
                                }
                            }
                    str += "</div>";
                console.log(str);
                $("#popBody").append(str);
            }
        }
    });

}

const fnDownload = () => {

    let sKeyword = $("input[name=sKeyword]").val();

    let jsonData = {
        'sKeyword': sKeyword,
        'excelFlag': 'teamGroupList'
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

function gotoOrder(orderName){
    $("#tmfrm input[name=cp]").val(1);
    $('#tmfrm input[name="orderName"]').val(orderName);

    if($("#tmfrm input[name='order']").val() == "ASC"){
        $("#tmfrm [name='order']").val("DESC");
    }else{
        $("#tmfrm [name='order']").val("ASC");
    }

    document.tmfrm.submit();
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
  		  	<h2><span></span>학원·클럽 > 관리</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
        <div class="others">
          <!-- 현재년도 -->

        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">

          <form name="frm" id="frm" method="post"  action="teamGroup" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">

              <input type="text" name="sKeyword" placeholder="팀 그룹명 검색" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
            <a class="btn-large default btn-pop" data-id="update-school-single" onclick="showTeamPopup()">그룹 생성</a>
              <a class="btn-large default " data-id="update-area-add" onclick="fnDownload();">목록 엑셀 다운로드</a>
              <a class="btn-large gray-o btn-pop" data-id="pop-excel-upload">엑셀 업로드</a>
            <%-- <a class="btn-large gray-o" onclick="gotoDataReset();">${ageGroup}연령 데이터 초기화</a> --%>
          </div>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update over">
            <colgroup>
				<col width="4%">
				<col width="4%">
				<col width="4%">
				<col width="3%">
				<col width="3%">
            </colgroup>
            <thead>
				<tr>
	              <th><!-- <a onclick="gotoOrder('key');"> -->번호 <!-- <i class="xi-caret-up-min"></i></a> --></th>
                  <th><a onclick="gotoOrder('groupName');">그룹명<i class="xi-caret-up-min"></i></a></th>
                  <th><a onclick="gotoOrder('cnt');">소속팀 수<i class="xi-caret-up-min"></i></a></th>
                  <th><a onclick="gotoOrder('useFlag');">활성여부<i class="xi-caret-up-min"></i></a></th>
				  <th>관리</th>
	            </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty teamGroupList}">
                        <tr>
                            <td colspan="4">등록된 내역이 없습니다.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="result" items="${teamGroupList}" varStatus="status">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
                                        <c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a class="title" onclick="gotoTeamGroupDet('${result.team_group_id}');">${result.group_name}</a>
                                </td>
                                <td>
                                    ${result.cnt}
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${result.use_flag eq '0'}">
                                            활성
                                        </c:when>
                                        <c:when test="${result.use_flag eq '1'}">
                                            비활성
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td class="admin">
                                    <a class="btn-pop" data-id="modefy-school-single" onclick="gotoModPopup('${result.team_group_id}', '${result.group_name}', '${result.use_flag}');"><i class="xi-catched"></i></a>
                                    <a onclick="gotoDel('${result.team_group_id}');"><i class="xi-close-circle-o"></i></a>
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

        <div class="pop" id="update-school-single">
            <div style="height:auto;">
                <div style="height:auto;">
                    <div class="head">
                        팀 그룹 등록
                    </div>

                    <form name="addFrm" id="addFrm" method="post" enctype="multipart/form-data" action="save_teamGroup">
                        <input type="hidden" name="sFlag" value="0">
                        <div class="body" style="padding:15px 20px;">
                            <ul class="signup-list">
                                <li class="title">
                                    <span class="title">그룹명</span><input type="text" name="groupName" placeholder="그룹명 입력">
                                </li>
                                <li class="title">
                                    <span class="title">활성 / 비활성</span>
                                    <input type="radio" name="useFlag" id="b1" value="0" checked><label class="w20" for="b1">활성</label>
                                    <input type="radio" name="useFlag" id="b2" value="1" ><label class="w20" for="b2">비활성</label>
                                </li>
                            </ul>
                        </div>
                    </form>
                    <div class="foot">
                        <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAdd();"><span>등록하기</span></a>
                        <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="pop" id="modefy-school-single">
            <div style="height:auto;">
                <div style="height:auto;">
                    <div class="head">
                        팀 그룹 수정
                    </div>
                    <form name="modFrm" id="modFrm" method="post" enctype="multipart/form-data" action="save_teamGroup">
                        <input type="hidden" name="sFlag" value="1">
                        <input type="hidden" name="teamGroupId">
                        <div class="body" style="padding:15px 20px;">
                            <ul class="signup-list">
                                <li class="title">
                                    <span class="title">그룹명</span><input type="text" name="groupName" placeholder="그룹명" >
                                </li>
                                <li class="title">
                                    <span class="title">활성 / 비활성</span>
                                    <input type="radio" name="useFlag" id="a1" value="0"><label class="w20" for="a1">활성</label>
                                    <input type="radio" name="useFlag" id="a2" value="1"><label class="w20" for="a2">비활성</label>
                                </li>
                            </ul>
                        </div>
                    </form>
                    <div class="foot">
                        <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoMod();"><span>수정하기</span></a>
                        <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                    </div>
                </div>
            </div>
        </div>

        <%--<form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="teamGroupExcelUpload" onsubmit="return false;">
            <input name="excelFlag" type="hidden" value="player">--%>
            <div class="pop" id="pop-excel-upload">
                <div style="height:auto;">
                    <div style="height:auto;">
                        <div class="head">
                            팀 그룹 일괄 등록
                        </div>
                        <div class="body" id="popBody" style="padding:15px 20px;">
                            <ul class="signup-list">
                                <li>
                                    <input type="file" id="excelFile" name="excelFile">
                                </li>
                            </ul>
                        </div>
                        <div class="foot">
                            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddExcel();"><span>등록하기</span></a>
                            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                        </div>
                    </div>
                </div>
            <%--</div>
        </form>--%>

</body>

<form name="tmdfrm" id="tmdfrm" method="post"  action="teamGroupDet">
    <input name="cp" type="hidden" value="${cp}">
    <input name="teamGroupId" type="hidden" value="">
</form>

<form name="delFrm" id="delFrm" method="post"  enctype="multipart/form-data" action="save_teamGroup" >
    <input type="hidden" name="sFlag" value="2">
    <input type="hidden" name="teamGroupId">
</form>

<form name="tmfrm" id="tmfrm" method="post"  action="teamGroup" onsubmit="return false;">
    <input name="cp" type="hidden" value="${cp}">
    <input name="sKeyword" type="hidden" value="${sKeyword}">
    <input name="orderName" type="hidden" value="${orderName}">
    <input name="order" type="hidden" value="${order}">
</form>

</html>