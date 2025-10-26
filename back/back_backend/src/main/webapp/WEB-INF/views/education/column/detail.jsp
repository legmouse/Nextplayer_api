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
        newForm.attr('action', '/columnModify');
        newForm.append($('<input/>', {type: 'hidden', name: 'educationColumnId', value: val }));
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

const fnDeleteColumn = (val) => {

    $("input[name=educationColumnId]").val(val);

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
            location.href = '/columnList';
        },
        timeout: 1000*60*10
    });

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
                <h2><span></span>칼럼</h2>
            </div>
        </div>
        <div class="round body">

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
                            ${columnInfo.title}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">요약</th>
                        <td class="tl" style="line-height: 2.1;">
                            ${columnInfo.summary}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">노출 / 비노출</th>

                        <td class="tl">
                        <c:choose>
                            <c:when test="${columnInfo.show_flag eq '0'}">
                                노출
                            </c:when>
                            <c:otherwise>
                                비노출
                            </c:otherwise>
                        </c:choose>
                        </tr>
                    </tr>
                    <tr>
                        <th class="tl">메인 비활성 / 활성</th>
                        <td class="tl">
                        <c:choose>
                            <c:when test="${columnInfo.main_flag eq '0'}">
                                비활성
                            </c:when>
                            <c:otherwise>
                                활성
                            </c:otherwise>
                        </c:choose>
                        </tr>
                    </tr>
                    <tr>
                        <th class="tl">내용</th>
                        <td class="tl" style="line-height: 2.1;">
                            ${columnInfo.content}
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">이미지</th>
                        <td class="tl">
                            <c:if test="${!empty columnInfo.imgFiles}">
                                    <div>
                                        ${columnInfo.imgFiles.fileName}<br>
                                        <img src="/NP${columnInfo.imgFiles.fileSavePath}" data-isClicked="0" onclick="fnClickImg(this)" style="object-fit: contain; border: 1px solid #ddd; width: 100px; height: 100px; margin-top: 10px;">
                                    </div>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">카테고리</th>
                        <td class="tl">
                            <c:forEach var="data" items="${menuList}" varStatus="status2">
                                <c:choose>
                                    <c:when test="${columnInfo.column_type.toString() eq data.code_value.toString()}">
                                        ${data.category_name}
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <th class="tl">이동 링크</th>
                        <td class="tl">
                            ${columnInfo.url_link}
                        </td>
                    </tr>
                    </tbody>
                </table>

                <br />
                <br />
                <br />

            <br>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit" onclick="fnDeleteColumn('${columnInfo.education_column_id}')">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${columnInfo.education_column_id}')">수정 하기</a>
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
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="delete_column">
    <input type="hidden" name="educationColumnId" value="">
</form>

</html>