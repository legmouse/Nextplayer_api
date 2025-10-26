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

        igpFile = new igp.file({fileLayer:'#fileLayer', boardSeq: '${method eq 'save' ? '' : noticeDetail.board_id}', allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'], maxSize:5, foreignType: 'Notice'});
        igpFile.setFileList(false);

        /*$('#eBtnSubmit').click(function(e) {

            var txt = $(this).text().trim();
            $('#delFrm').ajaxIgp({
                beforeSubmit:function(){
                    if(confirm(txt+'하시겠습니까?')){
                    }else{
                        return false;
                    }
                    $('#page-loading').show();
                    return true;

                }, success:function(res){
                    alert(txt+'되었습니다');
                    $('#page-loading').hide();
                    location.href = '/noticeList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });*/

        $('img').on('click', function(){

            let isClick = $(this).attr('data-isClicked')


            $(this).attr('style', '');

            const bfCss = {
                'object-fit': 'contain',
                'border': '1px solid #ddd',
                'width': '100px',
                'height': '100px',
                'margin-top': '10px'
            }

            const afCss = {
                'max-width': '900px',
                'margin-top': '10px'
            }

            if (isClick === '0') {
                $(this).attr('data-isClicked', '1');
                $(this).css(afCss);
            } else {
                $(this).attr('data-isClicked', '0');
                $(this).css(bfCss);
            }

        })

    });

    const fnGoModify = (val) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/popupModify');
        newForm.append($('<input/>', {type: 'hidden', name: 'popupId', value: val }));
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Modify' }));

        $(newForm).appendTo('body').submit();
    }

    const fnClickImg = (el) => {
        /*let isClick = $(el).attr('data-isClicked')

        const bfCss = {
            'object-fit': 'contain',
            'border': '1px solid #ddd',
            'width': '100px',
            'height': '100px',
            'margin-top': '10px'
        }

        const afCss = {
            'max-width': '900px'
        }

        if (isClick === '0') {
            $(el).attr('data-isClicked', '1');
            /!*$(el).css(afCss);*!/
            $(el).css("maxWidth", "900px");
        } else {
            $(el).attr('data-isClicked', '0');
            $(el).css(bfCss);
        }*/
    }

const moveTab = (val) => {

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');

    let urlStr = "";
    if (val == 'info') {
        urlStr = "/educationDetail";
        newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Modify' }));
    }else if (val == 'question') {
        urlStr = "/educationQuestionList";
    } else if (val == 'faq') {
        urlStr = "/educationFaqList";
    } else if (val == 'member') {
        urlStr = "/educationMemberList";
    }

    newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: '${educationInfo.education_id}' }));
    newForm.attr('action', urlStr);
    //newForm.append($('<input/>', {type: 'hidden', name: 'educationId', value: val }));
    $(newForm).appendTo('body').submit();
}

const fnDeletePopup = (val) => {

    $("input[name=educationId]").val(val);

    $('#delFrm').ajaxIgp({
        beforeSubmit:function(){
            if(confirm('정말 삭제 하시겠습니까?')){
            }else{
                return false;
            }
            $('#page-loading').show();
            return true;

        }, success:function(res){
            alert('삭제 되었습니다');
            $('#page-loading').hide();
            location.href = '/popupList';
        },
        timeout: 1000*60*10
    });

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
                <h2><span></span>팝업관리 > 상세</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">
                <div class="search">
                </div>
            </div>

            <div class="title">
                <h3>
                    상세 정보
                </h3>
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
                        <th class="tl">제목</th>
                        <td class="tl">
                            ${popupInfo.title}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">팝업 기간</th>
                        <td class="tl">
                            ${popupInfo.show_sdate} ~ ${popupInfo.show_edate}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">노출 / 비노출</th>

                        <td class="tl">
                        <c:choose>
                            <c:when test="${popupInfo.show_flag eq '0'}">
                                노출
                            </c:when>
                            <c:otherwise>
                                비노출
                            </c:otherwise>
                        </c:choose>
                        </tr>
                    </tr>
                    <tr>
                        <th class="tl">이미지</th>
                        <td class="tl">
                            <c:if test="${!empty popupInfo.imgFiles}">
                                    <div>
                                        ${popupInfo.imgFiles.fileName}<br>
                                        <img src="/NP${popupInfo.imgFiles.fileSavePath}" data-isClicked="0" onclick="fnClickImg(this)" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                    </div>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">웹 이동 링크</th>
                        <td class="tl">
                            ${popupInfo.url_link}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">앱 이동 링크</th>
                        <td class="tl">
                            ${popupInfo.url_app_link}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">웹 새창 여부</th>
                        <td class="tl">
                            <c:set var="modelString" value="${popupInfo.popup_type}" />
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
                            <c:set var="modelStringApp" value="${popupInfo.popup_app_type}" />
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
                            <input type="radio" name="authFlag" id="ra4-1" value="0" <c:if test="${popupInfo.auth_flag eq '0'}">checked</c:if> onclick="return false;"><label for="ra4-1">로그인 X</label>
                            <input type="radio" name="authFlag" id="ra4-2" value="1" <c:if test="${popupInfo.auth_flag eq '1'}">checked</c:if> onclick="return false;"><label for="ra4-2">로그인 O</label>
                            <input type="radio" name="authFlag" id="ra4-3" value="2" <c:if test="${popupInfo.auth_flag eq '2'}">checked</c:if> onclick="return false;"><label for="ra4-3">토큰</label>
                        </td>
                    </tr>
                    </tbody>
                </table>

                <br />
                <br />
                <br />

            <form id="frm" method="post" action="/delete_popup"  enctype="multipart/form-data">
                <input type="hidden" name="popupId" value="${popupInfo.popup_id}">
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit" onclick="fnDeletePopup('${popupInfo.popup_id}')">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${popupInfo.popup_id}')">수정 하기</a>
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
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="delete_popup">
    <input type="hidden" name="popupId" value="${popupInfo.popup_id}">
</form>

</html>