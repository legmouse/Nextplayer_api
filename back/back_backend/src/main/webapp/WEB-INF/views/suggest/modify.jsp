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
                                boardSeq: '${method eq 'save' ? '' : noticeDetail.board_id}',
                                allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'],
                                maxSize:5,
                                foreignType: 'Notice'
                                });
        igpFile.setFileList(true);

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
                    alert(txt+'되었습니다');
                    $('#page-loading').hide();
                    location.href = '/noticeList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });


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
                                        <textarea id="content" name="content" placeholder="내용을 입력해주세요"></textarea>
                                    </c:when>
                                    <c:when test="${method eq 'update'}">
                                        <textarea id="content" name="content" placeholder="내용을 입력해주세요">${noticeDetail.content}</textarea>
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