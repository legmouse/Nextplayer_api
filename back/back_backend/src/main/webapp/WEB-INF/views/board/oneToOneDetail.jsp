<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                seq: '${method eq 'save' ? '' : oneToOneDetail.board_id}',
                allowExt:['JPG', 'JPEG', 'PNG'],
                maxSize:5,
                foreignType: 'Board'
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
                        location.href = '/oneToOneList';
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

            const boardId = '${oneToOneDetail.board_id}';
            const answer = $("#answer").val()

            const param = {
                boardId: boardId,
                answer: answer
            }

            $.ajax({
                type: 'POST',
                url: '/modify_oneToOne',
                data : param,
                success : function(res) {
                    if (res.state == 'SUCCESS') {
                        alert('처리 완료 되었습니다.');
                        location.href = "/oneToOneList";
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
            formData.append("boardId", val);
            formData.append("method", "delete");


            $.ajax({
                type: 'POST',
                url: '/modify_oneToOne',
                data: formData,
                contentType : false,
                processData : false,
                success: function(res) {
                    if(res.state == 'SUCCESS') {
                        location.href = '/oneToOneList';
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
                <h2><span></span>1:1문의 관리</h2>
            </div>

        </div>
        <div class="round body">

            <div class="body-head">

                <h4 class="view-title">1:1문의 > 상세페이지</h4>
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
                                <c:when test="${oneToOneDetail.answer_flag == '0'}">
                                    미답변
                                </c:when>
                                <c:when test="${oneToOneDetail.answer_flag == '1'}">
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
                            ${fn:replace(oneToOneDetail.reg_date, 'T', ' ')}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            작성자
                        </th>
                        <td class="tl">
                            ${oneToOneDetail.member_nickname}
                            <c:if test="${!empty oneToOneDetail.member_name}">
                                /${oneToOneDetail.member_name}
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            연락처
                        </th>
                        <td class="tl">
                            ${oneToOneDetail.phone_no}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            회원종류
                        </th>
                        <td class="tl">
                            <c:choose>
                                <c:when test="${oneToOneDetail.member_type eq '1'}">학부모</c:when>
                                <c:when test="${oneToOneDetail.member_type eq '2'}">감독/코치</c:when>
                                <c:when test="${oneToOneDetail.member_type eq '3'}">레슨 선생님</c:when>
                                <c:when test="${oneToOneDetail.member_type eq '4'}">학원/클럽 관계자</c:when>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            카테고리
                        </th>
                        <td class="tl">
                            <c:choose>
                                <c:when test="${oneToOneDetail.type eq '0'}">계정문의</c:when>
                                <c:when test="${oneToOneDetail.type eq '1'}">대회,리그 문의</c:when>
                                <c:when test="${oneToOneDetail.type eq '2'}">학원,클럽 문의</c:when>
                                <c:when test="${oneToOneDetail.type eq '3'}">축구정보 문의</c:when>
                                <c:when test="${oneToOneDetail.type eq '4'}">제휴문의</c:when>
                                <c:when test="${oneToOneDetail.type eq '5'}">기타</c:when>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            제목
                        </th>
                        <td class="tl">
                            ${oneToOneDetail.title}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            내용
                        </th>
                        <td class="tl">
                            ${oneToOneDetail.content}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">
                            사진
                        </th>
                        <td class="tl">
                            <c:choose>
                                <c:when test="${!empty oneToOneDetail.files}">
                                    <c:forEach var="items" items="${oneToOneDetail.files}" varStatus="status">
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
                <form id="frm" method="post" action="/modify_oneToOne"  enctype="multipart/form-data">
                    <input type="hidden" name="boardId" value="${oneToOneDetail.board_id}">
                    <input type="hidden" name="method" value="${oneToOneDetail.answer_flag eq '0' ? 'save' : 'modify'}">
                    <input type="hidden" name="foreignType" value="Board">
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
                                <textarea style="height: 300px;" id="answer" name="answer">${oneToOneDetail.answer}</textarea>
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
                <a class="btn-large gray-o" onclick="location.href='/oneToOneList'">목록으로 이동</a>
                <a class="btn-large red" onclick="fnRemove('${oneToOneDetail.board_id}')">삭제하기</a>
                <c:choose>
                    <c:when test="${oneToOneDetail.answer_flag eq '0'}">
                        <a class="btn-large default" id="eBtnSubmit">저장 하기</a>
                    </c:when>
                    <c:otherwise>
                        <%--<a class="btn-large default" onclick="fnSaveAnswer()">수정 하기</a>--%>
                        <a class="btn-large default" id="eBtnSubmit">수정 하기</a>
                    </c:otherwise>
                </c:choose>

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