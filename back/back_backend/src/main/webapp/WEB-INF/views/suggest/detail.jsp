<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("enter", "\n"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <script src="../resources/jquery/jquery-3.3.1.min.js"></script>
    <script src="../resources/jquery/jquery-ui.js"></script>
    <script src="/resources/jquery/jquery.form.js"></script>
    <script src="../resources/js/layout.js"></script>

    <script type="text/javascript" src="/resources/js/igp.js"></script>
    <script type="text/javascript" src="/resources/js/init.js"></script>
    <script type="text/javascript" src="/resources/js/igp.file.js"></script>

    <link rel="stylesheet" href="../resources/css/layout.css">
    <link rel="stylesheet" href="../resources/xeicon/xeicon.min.css">
    <link rel="stylesheet" href="../resources/nanum/nanumsquare.css">
    <link rel="shortcut icon" href="../resources/ico/favicon.ico">
    <link rel="apple-touch-icon" href="../resources/ico/mobicon.png">

    <script type="text/javascript">

        $(document).ready(function() {

            igpFile = new igp.answerImgFile({
                fileLayer: '#fileLayer',
                seq: '${method eq 'save' ? '' : suggestDetail.suggest_id}',
                allowExt:['JPG', 'JPEG', 'PNG'],
                maxSize:5,
                foreignType: 'Suggest'
            });
            igpFile.setImgFileList(true);

            $('#eBtnSubmit').click(function(e) {
                var txt = $(this).text().trim();
                $('#frm').ajaxIgp({
                    beforeSubmit:function(){
                        if($("#answer").val() == null || $("#answer").val() == ""){
                            alert('답변을 입력해주세요');
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
                        //location.href = '/noticeList';
                        location.reload();
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
        //페이징 처리

        /*const fnSaveAnswer = (val) => {

            const suggestId = '${suggestDetail.suggest_id}';
            const answer = $("#answer").val()

            const param = {
                suggestId: suggestId,
                answer: answer
            }

            $.ajax({
                type: 'POST',
                url: '/modify_suggest',
                data : param,
                success : function(res) {
                    if (res.state == 'SUCCESS') {
                        alert('처리 완료 되었습니다.');
                        location.href = "/suggestList";
                    } else {
                        alert('처리 실패 했습니다.');
                        location.reload();
                    }
                }
            })
        }*/

        const fnRemove = (val) => {
            const confirmMsg = confirm('정말 삭제 하시겠습니까?');
            if (confirmMsg) {

                let formData = new FormData();
                formData.append("suggestId", val);
                formData.append("method", "delete");


                $.ajax({
                    type: 'POST',
                    url: '/modify_suggest',
                    data: formData,
                    contentType : false,
                    processData : false,
                    success: function(res) {
                        if(res.state == 'SUCCESS') {
                            location.href = "/suggestList";
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
                <h2><span></span>문의게시판</h2>
            </div>

        </div>
        <div class="round body">

            <div class="body-head">

                <h4 class="view-title">문의게시판 > 콘텐츠 추가 요청</h4>
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
                        <col width="100px">
                        <col width="*">

                    </colgroup>
                    <tbody>
                    <tr>
                        <th class="tl">
                            답변 상태
                        </th>
                        <td class="tl">
                            <c:choose>
                                <c:when test="${suggestDetail.answer_flag == '0'}">
                                    미답변
                                </c:when>
                                <c:when test="${suggestDetail.answer_flag == '1'}">
                                    답변완료
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            작성일
                        </th>
                        <td class="tl">
                            ${fn:substring(suggestDetail.reg_date, 0, 10)}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            작성자
                        </th>
                        <td class="tl">
                            ${suggestDetail.member_nickname}
                            <c:if test="${!empty suggestDetail.member_name}">
                                /${suggestDetail.member_name}
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            제목
                        </th>
                        <td class="tl">
                            ${suggestDetail.title}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            내용
                        </th>
                        <td class="tl">
                            <c:out value="${fn:replace(suggestDetail.content, enter, '<br/>')}" escapeXml="false" />
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            사진
                        </th>
                        <td class="tl">
                            <c:choose>
                                <c:when test="${!empty suggestDetail.files}">
                                    <c:forEach var="items" items="${suggestDetail.files}" varStatus="status">
                                        <img src="/NP${items.path}/${items.name}" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                    </tbody>
                </table><br>
                <div class="title">
                    <h3>
                        답변하기
                    </h3>
                </div>
                <form id="frm" method="post" action="/modify_suggest"  enctype="multipart/form-data">
                    <input type="hidden" name="suggestId" value="${suggestDetail.suggest_id}">
                    <input type="hidden" name="method" value="${suggestDetail.answer_flag eq '0' ? 'save' : 'modify'}">
                    <input type="hidden" name="foreignType" value="Suggest">
                    <table cellspacing="0" class="update over mt_10">
                        <colgroup>
                            <col width="100px">
                        </colgroup>
                        <tbody>
                        <tr>
                            <th>
                                내용
                            </th>
                            <td>
                                <textarea id="answer" name="answer">${suggestDetail.answer}</textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>이미지</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div><br>
            <div class="tr w100">
                <a class="btn-large gray-o" onclick="location.href='/oneToOneList'">취소 하기</a>
                <a class="btn-large red" onclick="fnRemove('${suggestDetail.suggest_id}')">삭제하기</a>
                <c:choose>
                    <c:when test="${suggestDetail.answer_flag eq '0'}">
                        <a class="btn-large default" id="eBtnSubmit">저장 하기</a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn-large default" id="eBtnSubmit">수정 하기</a>
                    </c:otherwise>
                </c:choose>

            </div>

        </div>
    </div>
</div>



</body>
</html>