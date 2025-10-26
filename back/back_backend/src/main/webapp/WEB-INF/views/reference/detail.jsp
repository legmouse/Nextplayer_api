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

        igpFile = new igp.file({fileLayer:'#fileLayer', boardSeq: '${method eq 'save' ? '' : referenceInfo.reference_id}', allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'], maxSize:5, foreignType: 'Reference'});
        igpFile.setFileList(false);

        $('#eBtnSubmit').click(function(e) {
            const cupCnt = $("#selectedCupMatch").children().length;

            $("input[name='cupCnt']").val(cupCnt);

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
                    location.href = '/referenceList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });

    const fnGoModify = (val) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/modifyReference');
        newForm.append($('<input/>', {type: 'hidden', name: 'referenceId', value: val }));

        $(newForm).appendTo('body').submit();
    }

    const fnDeleteReference = (val) => {

        const confirmMsg = confirm('정말 삭제 하시겠습니까?');
        if (confirmMsg) {
            $.ajax({
                type: 'POST',
                url: '/delete_reference',
                enctype: 'multipart/form-data',
                data: {'referenceId': val},
                success: function(res) {
                    if (res.state == 'success') {
                        alert('삭제 되었습니다.')
                        location.href = "/referenceList";
                    } else {
                        alert('삭제 실패했습니다.');
                        location.reload();
                    }
                }
            })

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
                                ${fn:substring(referenceInfo.reg_date, 0, 10)}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">조회수</th>
                            <td class="tl">
                                ${referenceInfo.view_cnt}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">제목</th>
                            <td class="tl">
                                ${referenceInfo.title}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">내용 </th>
                            <td class="tl">
                                ${referenceInfo.content}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">첨부파일</th>
                            <td class="tl">
                                <div id="fileLayer"></div>
                            </td>
                        </tr>
                    </tbody>
                </table><br>
                <div class="title">
                    <h3>
                        등록 대회
                    </h3>
                </div>
                <table cellspacing="0" class="update over mt_10">
                    <colgroup>
                        <col width="*">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>대회명</th>
                            <th>대회일정 </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty cupList}">
                                <tr>
                                    <td colspan="2">
                                        등록된 대회가 없습니다.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="result" items="${cupList}" varStatus="status">
                                    <tr>
                                        <td>
                                            ${result.cup_name}
                                        </td>
                                        <td>
                                            ${result.sdate} ~ ${result.edate}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>  
                <br>

                <div class="title">
                    <h3>
                        등록 리그
                    </h3>
                    
                </div>
                <table cellspacing="0" class="update over mt_10">
                    <colgroup>
                        <col width="*">

                    </colgroup>
                    <thead>
                        <tr>
                            <th>리그명</th>
                            <th>리그일정 </th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty leagueList}">
                            <tr>
                                <td colspan="2">
                                    등록된 리그가 없습니다.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="result" items="${leagueList}" varStatus="status">
                                <tr>
                                    <td>
                                            ${result.league_name}
                                    </td>
                                    <td>
                                            ${result.sdate} ~ ${result.edate}
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    </tbody>
                </table>                
            </div>
            <br>
            <form id="frm" method="post" action="/delete_reference"  enctype="multipart/form-data">
                <input type="hidden" name="referenceId" value="${referenceInfo.reference_id}">
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${referenceInfo.reference_id}')">수정 하기</a>
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