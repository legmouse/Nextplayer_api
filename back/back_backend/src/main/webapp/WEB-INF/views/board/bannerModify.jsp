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

        if ("${bannerInfo.banner_type}") {
            var stringArray = JSON.parse("${bannerInfo.banner_type}");
            if (stringArray.indexOf(0) !== -1) {
                $("#chkAll").prop("checked", false);
            }
        }

        if ("${bannerInfo.banner_app_type}") {
            var stringArray = JSON.parse("${bannerInfo.banner_app_type}");
            if (stringArray.indexOf(0) !== -1) {
                $("#chkAppAll").prop("checked", false);
            }
        }

        $("#chkAll").click(function() {
            if ($("#chkAll").is(":checked")) {
                $("input[name=popupType]").prop("checked", true);
            } else {
                $("input[name=popupType]").prop("checked", false);
            }
        });

        $("input[name=popupType]").click(function() {
            let total = $("input[name=popupType]").length;
            let checked = $("input[name=popupType]:checked").length;

            if(total != checked) {
                $("#chkAll").prop("checked", false);
            } else {
                $("#chkAll").prop("checked", true);
            }
        });

        $("#chkAppAll").click(function() {
            if ($("#chkAppAll").is(":checked")) {
                $("input[name=popupAppType]").prop("checked", true);
            } else {
                $("input[name=popupAppType]").prop("checked", false);
            }
        });

        $("input[name=popupAppType]").click(function() {
            let total = $("input[name=popupAppType]").length;
            let checked = $("input[name=popupAppType]:checked").length;

            if(total != checked) {
                $("#chkAppAll").prop("checked", false);
            } else {
                $("#chkAppAll").prop("checked", true);
            }
        });

        igpFile = new igp.file({
                                fileLayer:'#fileLayer',
                                boardSeq: '${method eq 'save' ? '' : bannerInfo.banner_id}',
                                allowExt:['JPG', 'JPEG', 'PNG'],
                                maxSize:1,
                                foreignType: 'Banner',
                                subType: 'PC',
                                method: 'modify'
                                });
        igpFile.setFileListParam(true);

        igpFile2 = new igp.imgFile({
                                fileLayer:'#fileLayer2',
                                boardSeq: '${method eq 'save' ? '' : bannerInfo.banner_id}',
                                allowExt:['JPG', 'JPEG', 'PNG'],
                                maxSize:1,
                                foreignType: 'Banner',
                                subType: 'Mobile',
                                method: 'modify'
        });
        igpFile2.setFileListParam(true);

        $('#eBtnSubmit').click(function(e) {
            let android = $("#android").is(":checked") ? $("#android").val() : '0';
            let ios = $("#ios").is(":checked") ? $("#ios").val() : '0';
            let ipad = $("#ipad").is(":checked") ? $("#ipad").val() : '0';
            let pc = $("#pc").is(":checked") ? $("#pc").val() : '0';

            let androidApp = $("#androidApp").is(":checked") ? $("#androidApp").val() : '0';
            let iosApp = $("#iosApp").is(":checked") ? $("#iosApp").val() : '0';
            let ipadApp = $("#ipadApp").is(":checked") ? $("#ipadApp").val() : '0';
            let pcApp = $("#pcApp").is(":checked") ? $("#pcApp").val() : '0';

            let str = "<input type='hidden' name='android' value='" + android + "'>";
                str += "<input type='hidden' name='ios' value='" + ios + "'>";
                str += "<input type='hidden' name='ipad' value='" + ipad + "'>";
                str += "<input type='hidden' name='pc' value='" + pc + "'>";

                str += "<input type='hidden' name='androidApp' value='" + androidApp + "'>";
                str += "<input type='hidden' name='iosApp' value='" + iosApp + "'>";
                str += "<input type='hidden' name='ipadApp' value='" + ipadApp + "'>";
                str += "<input type='hidden' name='pcApp' value='" + pcApp + "'>";
            $("#frm").append(str);
            var txt = $(this).text().trim();
            $('#frm').ajaxIgp({
                beforeSubmit:function(){
                    if($("#title").val() == null || $("#title").val() == ""){
                        alert('제목 입력해주세요');
                        $("#title").focus();
                        return false;
                    }
                    if(confirm(txt+'하시겠습니까?')){
                    }else{
                        return false;
                    }
                    $('#page-loading').show();
                    return true;

                }, success:function(res){
                    alert(txt+'되었습니다');
                    $('#page-loading').hide();
                    location.href = '/bannerList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });

const goDetail = (val) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/detailBanner');
    newForm.append($('<input/>', {type: 'hidden', name: 'bannerId', value: val }));
    $(newForm).appendTo('body').submit();
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
                <h2><span></span>문의/공지</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">

                <h4 class="view-title">배너 >
                    <c:choose>
                        <c:when test="${method eq 'save'}">
                            등록하기
                        </c:when>
                        <c:when test="${method eq 'modify'}">
                            수정하기
                        </c:when>
                    </c:choose>

                </h4>
            </div>
            <%--<form id="frm" method="post" action="/save_reference"  enctype="multipart/form-data">--%>
            <form id="frm" method="post" action="${method eq 'save' ? '/save_banner' : '/modify_banner'}"  enctype="multipart/form-data">
                <input type="hidden" name="bannerId" value="${bannerInfo.banner_id}"/>
                <div class="scroll">
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">

                    </colgroup>
                    <tbody>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <input type="text" id="title" name="title" placeholder="제목을 입력해주세요" value="">
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        <input type="text" id="title" name="title" placeholder="제목을 입력해주세요" value="${bannerInfo.title}">
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th class="tl">웹 링크</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <input type="text" id="urlLink" name="urlLink" value="">
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        <input type="text" id="urlLink" name="urlLink" value="${bannerInfo.url_link}">
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th class="tl">앱 링크</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <input type="text" id="urlAppLink" name="urlAppLink" value="">
                                    </c:when>
                                    <c:when test="${method eq 'modify'}">
                                        <input type="text" id="urlAppLink" name="urlAppLink" value="${bannerInfo.url_app_link}">
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th class="tl">웹 새창 여부</th>
                            <c:choose>
                                <c:when test="${method eq 'save'}">
                                    <td class="tl">
                                        <input id="chkAll" type="checkbox" value="" checked><label for="chkAll">전체</label>
                                        <input type="checkbox" name="popupType" id="android" value="1" checked><label for="android">Android</label>
                                        <input type="checkbox" name="popupType" id="ios" value="1" checked><label for="ios">IOS</label>
                                        <input type="checkbox" name="popupType" id="ipad" value="1" checked><label for="ipad">iPad</label>
                                        <input type="checkbox" name="popupType" id="pc" value="1" checked><label for="pc">PC</label>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="modelString" value="${bannerInfo.banner_type}" />
                                    <c:set var="stringArray" value="${fn:split(fn:substring(modelString, 1, fn:length(modelString)-1), ', ')}" />
                                    <td class="tl">
                                        <input id="chkAll" type="checkbox" value="" checked><label for="chkAll">전체</label>
                                        <input type="checkbox" name="popupType" id="android" value="1" <c:if test="${stringArray[0] eq '1'}">checked</c:if>><label for="android">Android</label>
                                        <input type="checkbox" name="popupType" id="ios" value="1" <c:if test="${stringArray[1] eq '1'}">checked</c:if>><label for="ios">IOS</label>
                                        <input type="checkbox" name="popupType" id="ipad" value="1" <c:if test="${stringArray[2] eq '1'}">checked</c:if>><label for="ipad">iPad</label>
                                        <input type="checkbox" name="popupType" id="pc" value="1" <c:if test="${stringArray[3] eq '1'}">checked</c:if>><label for="pc">pc</label>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <th class="tl">앱 새창 여부</th>
                            <c:choose>
                                <c:when test="${method eq 'save'}">
                                    <td class="tl">
                                        <input id="chkAppAll" type="checkbox"  value="" checked><label for="chkAppAll">전체</label>
                                        <input type="checkbox" name="popupAppType" id="androidApp" value="1" checked><label for="androidApp">Android</label>
                                        <input type="checkbox" name="popupAppType" id="iosApp" value="1" checked><label for="iosApp">IOS</label>
                                        <input type="checkbox" name="popupAppType" id="ipadApp" value="1" checked><label for="ipadApp">iPad</label>
                                        <input type="checkbox" name="popupAppType" id="pcApp" value="1" checked><label for="pcApp">PC</label>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="modelStringApp" value="${bannerInfo.banner_app_type}" />
                                    <c:set var="stringArrayApp" value="${fn:split(fn:substring(modelStringApp, 1, fn:length(modelStringApp)-1), ', ')}" />
                                    <td class="tl">
                                        <input id="chkAppAll" type="checkbox"  value="" checked><label for="chkAppAll">전체</label>
                                        <input type="checkbox" name="popupAppType" id="androidApp" value="1" <c:if test="${stringArrayApp[0] eq '1'}">checked</c:if>><label for="androidApp">Android</label>
                                        <input type="checkbox" name="popupAppType" id="iosApp" value="1" <c:if test="${stringArrayApp[1] eq '1'}">checked</c:if>><label for="iosApp">IOS</label>
                                        <input type="checkbox" name="popupAppType" id="ipadApp" value="1" <c:if test="${stringArrayApp[2] eq '1'}">checked</c:if>><label for="ipadApp">iPad</label>
                                        <input type="checkbox" name="popupAppType" id="pcApp" value="1" <c:if test="${stringArrayApp[3] eq '1'}">checked</c:if>><label for="pcApp">pc</label>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <th class="tl">권한 체크 여부</th>
                            <c:choose>
                                <c:when test="${method eq 'save'}">
                                    <td class="tl">
                                        <input type="radio" name="authFlag" id="ra4-1" value="0" checked><label for="ra4-1">로그인 X</label>
                                        <input type="radio" name="authFlag" id="ra4-2" value="1" ><label for="ra4-2">로그인 O</label>
                                        <input type="radio" name="authFlag" id="ra4-3" value="2" ><label for="ra4-3">토큰</label>
                                        <input type="radio" name="authFlag" id="ra4-4" value="3" ><label for="ra4-4">토큰</label>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="tl">
                                        <input type="radio" name="authFlag" id="ra4-1" value="0" <c:if test="${bannerInfo.auth_flag eq '0'}">checked</c:if>><label for="ra4-1">로그인 X</label>
                                        <input type="radio" name="authFlag" id="ra4-2" value="1" <c:if test="${bannerInfo.auth_flag eq '1'}">checked</c:if>><label for="ra4-2">로그인 O</label>
                                        <input type="radio" name="authFlag" id="ra4-3" value="2" <c:if test="${bannerInfo.auth_flag eq '2'}">checked</c:if>><label for="ra4-3">토큰(상세 X)</label>
                                        <input type="radio" name="authFlag" id="ra4-4" value="3" <c:if test="${bannerInfo.auth_flag eq '3'}">checked</c:if>><label for="ra4-4">토큰(상세 O)</label>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <th class="tl">PC 이미지 첨부</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                                <%--<input type="file">--%>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">Mobile 이미지 첨부</th>
                            <td class="tl">
                                <div id="fileLayer2"></div>
                                <%--<input type="file">--%>
                            </td>
                        </tr>

                    </tbody>
                </table>

            </div><br>
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" onclick="history.back()">취소 하기</a>
                <a class="btn-large default" id="eBtnSubmit" >
                <c:choose>
                    <c:when test="${method eq 'save'}">
                        저장
                    </c:when>
                    <c:when test="${method eq 'modify'}">
                        수정
                    </c:when>
                </c:choose>

                </a>
            </div>
        </div>
    </div>
    </div>
    <!--팝업-->

</body>

</html>