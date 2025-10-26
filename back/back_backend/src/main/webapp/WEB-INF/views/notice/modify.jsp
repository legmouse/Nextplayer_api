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

<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

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
    	if ('${method}' == 'save') {
    		$('#noshow').prop('checked', true);
    	}
    	
    	if ('${method}' == 'update') {
    		var show = '${noticeDetail.show_flag}';
    		console.log(show);
    		$('input:radio[name=showFlag]:input[value='+show+']').attr('checked', true);
    	}
    	$('#snote').summernote({
            height: 440,
            lang : 'ko-KR',
            disableResizeEditor : true,
            toolbar : [
                ['style', ['bold', 'underline', 'clear']],
                ['font', ['fontname']],
                ['color', ['color']],
                ['para', ['paragraph']],
                ['insert', ['link', 'picture', 'video']],
                ['view', ['fullscreen']]
            ],
            callbacks: {
                onKeydown: function (e) {
                    var t = e.currentTarget.innerText;
                    if (t.trim().length >= 2000) {
                        if (e.keyCode != 8 && !(e.keyCode >=37 && e.keyCode <=40) && e.keyCode != 46 && !(e.keyCode == 88 && e.ctrlKey) && !(e.keyCode == 67 && e.ctrlKey) && !(e.keyCode == 65 && e.ctrlKey) && e.keyCode == 13)
                            e.preventDefault();
                    }
                },
                onImageUpload: function(files) {
                    uploadSummernoteImageFile(files[0],this);
                },
                onPaste: function (e) {
                    var t = e.currentTarget.innerText;
                    var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                    e.preventDefault();
                    var maxPaste = bufferText.length;
                    if(t.length + bufferText.length > 2000){
                        maxPaste = 2000 - t.length;
                    }
                    if(maxPaste > 0){
                        document.execCommand('insertText', false, bufferText.substring(0, maxPaste));
                    }
                }
            }
        });
    	$('.note-editable').css('white-space', 'pre');

        igpFile = new igp.file({
                                fileLayer:'#fileLayer',
                                boardSeq: '${method eq 'save' ? '' : noticeDetail.board_id}',
                                allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'],
                                maxSize:5,
                                foreignType: 'Notice'
                                });
        igpFile.setFileList(true);

        igpFile2 = new igp.imgFile({
            fileLayer:'#fileLayer2',
            boardSeq: '${method eq 'save' ? '' : noticeDetail.board_id}',
            allowExt:['JPG', 'JPEG', 'PNG'],
            maxSize:5,
            foreignType: 'Notice'
        });
        igpFile2.setImgFileList(true);

        $('#eBtnSubmit').click(function(e) {

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
                	if (res.state == 'fail') {
                		alert('다른 공지사항이 메인페이지에 노출로 설정되어있습니다.');
                	} else {
                		alert(txt+'되었습니다');
                        $('#page-loading').hide();
                        location.href = '/noticeList';
                	}
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });

    function uploadSummernoteImageFile(file, editor) {
        data = new FormData();
        data.append("file", file);
        data.append("foreignType", "Summer");
        $.ajax({
            data : data,
            type : "POST",
            url : "/uploadSummernoteImageFile",
            contentType : false,
            processData : false,
            success : function(data) {
                console.log(data);
                //항상 업로드된 파일의 url이 있어야 한다.
                $(editor).summernote('insertImage', data.url);
            }
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
                <h2><span></span>공지사항</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">

                <h4 class="view-title">공지사항 >
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
            <form id="frm" method="post" action="${method eq 'save' ? '/save_notice' : '/modify_notice'}"  enctype="multipart/form-data">
                <input type="hidden" name="boardId" value="${noticeDetail.board_id}"/>
                <input type="hidden" name="cupCnt"/>
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
                                    <c:when test="${method eq 'update'}">
                                        <input type="text" id="title" name="title" placeholder="제목을 입력해주세요" value="${noticeDetail.title}">
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th class="tl">내용</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'save'}">
                                        <textarea id="snote" name="content"></textarea>
                                    </c:when>
                                    <c:when test="${method eq 'update'}">
                                        <textarea id="snote" name="content">${noticeDetail.content}</textarea>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                                <%--<input type="file">--%>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">이미지</th>
                            <td class="tl">
                                <div id="fileLayer2"></div>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">메인페이지 노출</th>
                            <td class="tl">
								<input type="radio" name="showFlag" id="noshow" value="1"><label class="w20" for="noshow">메인페이지 노출안함</label>
								<input type="radio" name="showFlag" id="show" value="0"><label class="w20" for="show">메인페이지 노출</label>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div><br>
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" onclick="location.href='/noticeList'">취소 하기</a>
                <a class="btn-large default" id="eBtnSubmit" >
                <c:choose>
                    <c:when test="${method eq 'save'}">
                        저장
                    </c:when>
                    <c:when test="${method eq 'update'}">
                        수정
                    </c:when>
                </c:choose>

                </a>
            </div>
        </div>
    </div>
    </div>

</body>

</html>