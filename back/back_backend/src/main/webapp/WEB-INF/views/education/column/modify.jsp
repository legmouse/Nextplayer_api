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

        igpFile2 = new igp.imgFile({
            fileLayer:'#fileLayer',
            boardSeq: '${method eq 'save' ? '' : columnInfo.education_column_id}',
            allowExt:['JPG', 'JPEG', 'PNG'],
            maxSize:5,
            foreignType: 'Column'
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
                    alert(txt+'되었습니다');
                    $('#page-loading').hide();
                    location.href = '/columnList';
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
    data.append("foreignType", "Column");
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

const fnModify = () => {
    document.frm.submit;
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
  		  	<h2><span></span>칼럼 >
                <c:choose>
                    <c:when test="${method eq 'Regist'}">
                        등록
                    </c:when>
                    <c:when test="${method eq 'Modify'}">
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

        <form id="frm" name="frm" method="post" action="${method eq 'Regist' ? '/save_column' : '/modify_column'}" enctype="multipart/form-data">
            <input type="hidden" name="educationColumnId" value="${columnInfo.education_column_id}"/>
            <input type="hidden" name="bfColumnOrder" value="${columnInfo.column_order}" />
            <input type="hidden" name="bfMainFlag" value="${columnInfo.main_flag}" />
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
                                <input type="text" id="title" name="title" value="${columnInfo.title}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">요약</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <textarea type="text" name="summary"></textarea>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <textarea type="text" name="summary">${columnInfo.summary}</textarea>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">노출 / 비노출</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="radio" name="showFlag" id="ra1-1" value="0" checked><label for="ra1-1">활성</label>
                                <input type="radio" name="showFlag" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="radio" name="showFlag" id="ra1-1" value="0" <c:if test="${columnInfo.show_flag eq '0'}">checked</c:if>><label for="ra1-1">노출</label>
                                <input type="radio" name="showFlag" id="ra1-2" value="1" <c:if test="${columnInfo.show_flag eq '1'}">checked</c:if>><label for="ra1-2">비노출</label>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">메인 비활성 / 활성</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="radio" name="mainFlag" id="ra2-1" value="0" checked><label for="ra2-1">비활성</label>
                                <input type="radio" name="mainFlag" id="ra2-2" value="1"><label for="ra2-2">활성</label>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="radio" name="mainFlag" id="ra2-1" value="0" <c:if test="${columnInfo.main_flag eq '0'}">checked</c:if>><label for="ra2-1">비활성</label>
                                <input type="radio" name="mainFlag" id="ra2-2" value="1" <c:if test="${columnInfo.main_flag eq '1'}">checked</c:if>><label for="ra2-2">활성</label>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">내용</th>
                    <td class="tl">
                        <textarea id="snote" name="content"><c:if test="${method eq 'Modify'}">${columnInfo.content}</c:if></textarea>
                    </td>
                </tr>

                <tr>
                    <th class="tl">썸네일 이미지</th>
                    <td class="tl">
                        <div id="fileLayer"></div>
                    </td>
                </tr>

                <tr>
                    <th class="tl">카테고리</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <select name="columnType">
                                    <c:forEach var="data" items="${menuList}" varStatus="status">
                                        <option value="${data.code_value}">${data.category_name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <select name="columnType">
                                    <c:forEach var="data" items="${menuList}" varStatus="status">
                                        <option value="${data.code_value}" <c:if test="${columnInfo.column_type eq data.code_value.toString()}">selected</c:if>>${data.category_name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>

                <tr>
                    <th class="tl">업로드 일자</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="date" name="submitDate" placeholder="" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="date" name="submitDate" placeholder="" value="${fn:substring(columnInfo.submit_date, 0, 10)}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>

                <tr>
                    <th class="tl">작성자</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" name="writer" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" name="writer" value="${columnInfo.writer}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>

                <tr>
                    <th class="tl">이동 링크</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" name="urlLink" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" name="urlLink" value="${columnInfo.url_link}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
              </tbody>
            </table>

            <br />
            <br />

        </form>

        <div class="body-foot">
          <div class="others">
              <a class="btn-large default" id="eBtnSubmit">
                  <c:choose>
                      <c:when test="${method eq 'Regist'}">
                          저장하기
                      </c:when>
                      <c:otherwise>
                          수정하기
                      </c:otherwise>
                  </c:choose>
              </a>
          </div>
        </div>

      </div>
    </div>

</body>
</html>