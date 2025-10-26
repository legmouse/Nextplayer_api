<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script src="/resources/jquery/jquery.form.js"></script>
<script src="resources/js/layout.js"></script>

<script type="text/javascript" src="/resources/js/igp.js"></script>
<script type="text/javascript" src="/resources/js/init.js"></script>
<script type="text/javascript" src="/resources/js/igp.file.js"></script>


<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

    $(document).ready(function(){

        igpFile = new igp.file({
            fileLayer:'#fileLayer',
            boardSeq: '${method eq 'save' ? '' : bannerInfo.banner_id}',
            allowExt:['JPG', 'JPEG','PNG'],
            maxSize:5,
            foreignType: 'Banner',
            subType: 'PC',
            method: ''
        });
        igpFile.setFileListParam(false);

        igpFile2 = new igp.imgFile({
            fileLayer:'#fileLayer2',
            boardSeq: '${method eq 'save' ? '' : bannerInfo.banner_id}',
            allowExt:['JPG', 'JPEG', 'PNG'],
            maxSize:5,
            foreignType: 'Banner',
            subType: 'Mobile',
            method: ''
        });
        igpFile2.setFileListParam(false);

    });

    const fnGoModify = (val) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/saveBanner');
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'modify' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'bannerId', value: val }));

        $(newForm).appendTo('body').submit();
    }

    const fnRemove = (val) => {
        const confirmMsg = confirm('정말 삭제 하시겠습니까?');
        const reqType = '${reqType}';
        if (confirmMsg) {

            let formData = new FormData();
            formData.append("bannerId", val);
            formData.append("method", "delete");


            $.ajax({
                type: 'POST',
                url: '/delete_banner',
                data: formData,
                contentType : false,
                processData : false,
                success: function(res) {
                    if(res.state == 'SUCCESS') {
                        location.href = "/bannerList";
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
                <h2><span></span>기본정보</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">
                <h4 class="view-title">기본정보 </h4>
            </div>
            <hr class="mb_10">
            <div class="scroll">
                <div class="title">
                    <h3>
                        내용
                    </h3>
                </div>
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">
                    </colgroup>
                    <tbody>

                        <tr>
                            <th class="tl">작성일</th>
                            <td class="tl">
                                ${fn:substring(bannerInfo.reg_date, 0, 10)}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">조회수</th>
                            <td class="tl">
                                ${bannerInfo.view_cnt}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                ${bannerInfo.title}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">웹 링크</th>
                            <td class="tl">
                                ${bannerInfo.url_link}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">앱 링크</th>
                            <td class="tl">
                                ${bannerInfo.url_app_link}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">웹 새창 여부</th>
                            <td class="tl">
                                <c:set var="modelString" value="${bannerInfo.banner_type}" />
                                <c:set var="stringArray" value="${fn:split(fn:substring(modelString, 1, fn:length(modelString)-1), ', ')}" />
                                <input type="checkbox" name="popupType" id="android" value="1" <c:if test="${stringArray[0] eq '1'}">checked</c:if> onclick="return false;" ><label for="android">Android</label>
                                <input type="checkbox" name="popupType" id="ios" value="1" <c:if test="${stringArray[1] eq '1'}">checked</c:if> onclick="return false;" ><label for="ios">IOS</label>
                                <input type="checkbox" name="popupType" id="ipad" value="1" <c:if test="${stringArray[2] eq '1'}">checked</c:if> onclick="return false;" ><label for="ipad">iPad</label>
                                <input type="checkbox" name="popupType" id="pc" value="1" <c:if test="${stringArray[3] eq '1'}">checked</c:if> onclick="return false;" ><label for="pc">pc</label>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">앱 새창 여부</th>
                            <td class="tl">
                                <c:set var="modelStringApp" value="${bannerInfo.banner_app_type}" />
                                <c:set var="stringArrayApp" value="${fn:split(fn:substring(modelStringApp, 1, fn:length(modelStringApp)-1), ', ')}" />
                                <input type="checkbox" name="popupAppType" id="androidApp" value="1" <c:if test="${stringArrayApp[0] eq '1'}">checked</c:if> onclick="return false;" ><label for="androidApp">Android</label>
                                <input type="checkbox" name="popupAppType" id="iosApp" value="1" <c:if test="${stringArrayApp[1] eq '1'}">checked</c:if> onclick="return false;" ><label for="iosApp">IOS</label>
                                <input type="checkbox" name="popupAppType" id="ipadApp" value="1" <c:if test="${stringArrayApp[2] eq '1'}">checked</c:if> onclick="return false;" ><label for="ipadApp">iPad</label>
                                <input type="checkbox" name="popupAppType" id="pcApp" value="1" <c:if test="${stringArrayApp[3] eq '1'}">checked</c:if> onclick="return false;" ><label for="pcApp">pc</label>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">권한 체크 여부</th>
                            <td class="tl">
                                <input type="radio" name="authFlag" id="ra4-1" value="0" <c:if test="${bannerInfo.auth_flag eq '0'}">checked</c:if> onclick="return false;"><label for="ra4-1">로그인 X</label>
                                <input type="radio" name="authFlag" id="ra4-2" value="1" <c:if test="${bannerInfo.auth_flag eq '1'}">checked</c:if> onclick="return false;"><label for="ra4-2">로그인 O</label>
                                <input type="radio" name="authFlag" id="ra4-3" value="2" <c:if test="${bannerInfo.auth_flag eq '2'}">checked</c:if> onclick="return false;"><label for="ra4-3">토큰(상세 X)</label>
                                <input type="radio" name="authFlag" id="ra4-4" value="3" <c:if test="${bannerInfo.auth_flag eq '3'}">checked</c:if> onclick="return false;"><label for="ra4-4">토큰(상세 O)</label>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer2"></div>
                            </td>
                        </tr>
                    </tbody>
                </table><br>
            <br>
            <form id="frm" method="post" action="/delete_banner"  enctype="multipart/form-data">
                <input type="hidden" name="bannerId" value="${bannerInfo.reference_id}">
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit" onclick="fnRemove('${bannerInfo.banner_id}')">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${bannerInfo.banner_id}')">수정 하기</a>
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