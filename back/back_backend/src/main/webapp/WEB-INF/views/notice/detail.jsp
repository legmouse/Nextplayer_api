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

        $('#eBtnSubmit').click(function(e) {

            var txt = $(this).text().trim();
            $('#frm').ajaxIgp({
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
        });

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
        newForm.attr('action', '/updateNotice');
        newForm.append($('<input/>', {type: 'hidden', name: 'boardId', value: val }));

        $(newForm).appendTo('body').submit();
    }

    const fnDeleteReference = (val) => {

        const confirmMsg = confirm('정말 삭제 하시겠습니까?');
        if (confirmMsg) {
            $.ajax({
                type: 'POST',
                url: '/delete_notice',
                enctype: 'multipart/form-data',
                data: {'referenceId': val},
                success: function(res) {
                    if (res.state == 'success') {
                        alert('삭제 되었습니다.')
                        location.href = "/noticeList";
                    } else {
                        alert('삭제 실패했습니다.');
                        location.reload();
                    }
                }
            })

        }
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
                                ${fn:substring(noticeDetail.reg_date, 0, 10)}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">조회수</th>
                            <td class="tl">
                                ${noticeDetail.view_cnt}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                ${noticeDetail.title}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">내용 </th>
                            <td class="tl" style="line-height: 2.1; font-size: medium;">
                                <c:out value="${fn:replace(noticeDetail.content, enter, '<br/>')}" escapeXml="false" />
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">이미지</th>
                            <td class="tl">
                                <c:if test="${!empty noticeDetail.imgFiles}">
                                    <c:forEach items="${noticeDetail.imgFiles}" var="i" varStatus="status">
                                        <div>
                                            ${i.fileName}<br>
                                            <img src="/NP${i.fileSavePath}" data-isClicked="0" onclick="fnClickImg(this)" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">메인 노출여부</th>
                            <td class="tl">
                            	${noticeDetail.show_flag eq '0' ? '노출' : '미노출'}
                            </td>
                        </tr>
                    </tbody>
                </table><br>

            <br>
            <form id="frm" method="post" action="/delete_notice"  enctype="multipart/form-data">
                <input type="hidden" name="boardId" value="${noticeDetail.board_id}">
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${noticeDetail.board_id}')">수정 하기</a>
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