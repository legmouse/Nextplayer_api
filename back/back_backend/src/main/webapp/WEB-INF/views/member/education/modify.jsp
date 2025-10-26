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

<!-- summernote -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

    $(document).ready(function(){



        igpFile = new igp.file({
            fileLayer:'#fileLayer',
            boardSeq: '${params.method eq 'save' ? '' : memberDetail.education_file_id}',
            allowExt:['HWP', 'XLS', 'PPT', 'DOC', 'XLSX', 'PPTX', 'DOCX', 'PDF', 'JPG', 'JPEG', 'GIF', 'BMP', 'PNG', 'ZIP'],
            maxSize:1,
            foreignType: 'EduFile'
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
                    location.href = '/memberDetail?memberCd=${params.memberCd}';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });

    const fnDeleteEducation = (val) => {

        $("input[name=educationFileId]").val(val);

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
                location.href = '/memberDetail?memberCd=${params.memberCd}';
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
  		  	<h2><span></span>회원관리 > 피츠앤솔 >
                <c:choose>
                    <c:when test="${params.method eq 'Regist'}">
                        등록
                    </c:when>
                    <c:when test="${params.method eq 'Modify'}">
                        수정
                    </c:when>
                </c:choose>
            </h2>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
        </div>

        <div class="title">
          <h3>
              기본 정보
          </h3>
        </div>

        <form id="frm" method="post" action="${params.method eq 'Regist' ? '/save_member_education' : '/modify_member_education'}"  enctype="multipart/form-data">
            <input type="hidden" name="educationFileId" value="${memberDetail.education_file_id}"/>
            <input type="hidden" name="memberCd" value="${params.memberCd}"/>
            <table cellspacing="0" class="update view">
              <colgroup>
                    <col width="20%">
                    <col width="*">
              </colgroup>
              <tbody>
                <tr>
                    <th class="tl">제목</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" id="title" name="title">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" id="title" name="title" value="${memberDetail.title}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">파일</th>
                    <td class="tl">
                        <div id="fileLayer"></div>
                    </td>
                </tr>
              </tbody>
            </table>

        </form>


        <div class="body-foot">
            <div class="others">
                <a class="btn-large default" id="eBtnSubmit">
                    <c:choose>
                        <c:when test="${params.method eq 'Regist'}">
                            저장하기
                        </c:when>
                        <c:otherwise>
                            수정하기
                        </c:otherwise>
                    </c:choose>
                </a>
                <c:if test="${params.method eq 'Modify'}">
                    <a class="btn-large red" onclick="fnDeleteEducation('${memberDetail.education_file_id}')">
                        삭제하기
                    </a>
                </c:if>
            </div>
          </div>
      </div>
    </div>

</body>
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="delete_member_education">
    <input type="hidden" name="educationFileId" value="">
</form>
</html>