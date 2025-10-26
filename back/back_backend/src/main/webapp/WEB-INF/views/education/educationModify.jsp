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
            boardSeq: '${method eq 'save' ? '' : educationInfo.education_id}',
            allowExt:['JPG', 'JPEG', 'PNG'],
            maxSize:5,
            foreignType: 'Education'
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
                    location.href = '/educationList';
                },
                timeout: 1000*60*10
            });
            e.preventDefault();
            return false;
        });

    });

const fnSearchGoods = () => {
    $("#goodsList").empty();

    const idx = $("#thirdChildCate option:selected").val() ? $("#thirdChildCate option:selected").val() : $("#childCate option:selected").val();
    const sKeyword = $("input[name='sKeyword']").val();

    const param = {
        'idx': idx,
        'sKeyword' : sKeyword
    }

    $.ajax({
        type : 'POST',
        url : '/search_goods',
        data : param,
        success: function(res) {
            if (res.state == 'SUCCESS') {
                let str = "";

                if (res.data.length > 0) {
                    $("#goodsList tr").empty();
                    for (let i = 0; i <res.data.length; i++) {
                        str += "<tr>";
                        str +=  "<td>";
                        str +=      res.data[i].goods_name;
                        str +=  "</td>";
                        str +=  "<td>";
                        str +=      res.data[i].goods_code;
                        str +=  "</td>";
                        str +=  "<td>";
                        str +=      res.data[i].price;
                        str +=  "</td>";
                        str +=  "<td><img style='width: 100%;' src='https://store.nextplayer.co.kr/data/base/goods/small/";
                        str +=      res.data[i].goods_img;
                        str +=  "'></td>";
                        str +=  "<td>";
                        str +=      "<button class='btn-large defalut' " ;
                        str +=          "data-goodsId='" + res.data[i].goods_id + "' ";
                        str +=          "data-goodsCode='" + res.data[i].goods_code + "' ";
                        str +=          "data-goodsName='" + res.data[i].goods_name + "' ";
                        str +=          "data-goodsImg='https://store.nextplayer.co.kr/data/base/goods/small/" + res.data[i].goods_img + "' ";
                        str +=          "data-price='" + res.data[i].price + "' ";
                        str +=      "onclick='appendGoods(this)'>";
                        str +=      "선택</button>";
                        str +=  "</td>";
                    }

                    $("#goodsList").append(str);
                }

            }
        }
    });

}

const appendGoods = (el) => {
    console.log(el);
    let str = "";
    $("#appendGoodsList tr").empty();
    str += "<tr>";
    str +=  "<td>";
    str +=      "<input type='hidden' name='goodsId' value='"+$(el).attr('data-goodsId')+"'>";
    str +=      $(el).attr('data-goodsName');
    str +=  "</td>";
    str +=  "<td>";
    str +=      "<input type='hidden' name='goodsCode' value='"+$(el).attr('data-goodsCode')+"'>";
    str +=      $(el).attr('data-goodsCode');
    str +=  "</td>";
    str +=  "<td>";
    str +=      $(el).attr('data-price');
    str +=  "</td>";
    str +=  "<td><img style='width: 50%;' src='" + $(el).attr('data-goodsImg') + "' ></td>";
    str +=  "<td>";
    str +=      "<button class='btn-large gray' onclick='removeAppendGoods()'>삭제</button";
    str +=  "</td>";

    $("#appendGoodsList").append(str);
    $("#good-search").hide();
    $("#goodsList tr").empty();
}

const removeAppendGoods = () => {
    $("#appendGoodsList tr").empty();
}

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

const fnChangeCate = () => {
    let idx = $("#parentCate option:selected").val();
    $.ajax({
        data: {idx: idx},
        type: "POST",
        url: "/search_child_cate",
        success: function(res) {
            if (res.data) {
                let str = "";
                $("#childCate option").remove();
                for(let i = 0; i < res.data.length; i++) {
                    str +=  "<option value='" + res.data[i].IDX + "'>" + res.data[i].NAME + "</option>";
                }
                $("#childCate").append(str);

                $("#thirdChildCate option").remove();
                $.ajax({
                    data: {idx: res.data[0].IDX},
                    type: "POST",
                    url: "/search_child_cate",
                    success: function(res) {
                        if (res.data) {
                            let str = "";
                            $("#thirdChildCate option").remove();
                            if (res.data.length > 0) {
                                for(let i = 0; i < res.data.length; i++) {
                                    str +=  "<option value='" + res.data[i].IDX + "'>" + res.data[i].NAME + "</option>";
                                }
                            } else {
                                str += "<option value=''>-</option>"
                            }
                            $("#thirdChildCate").append(str);
                        }
                    }
                });

            }
        }
    });
}

const fnChangeChildCate = () => {
    let idx = $("#childCate option:selected").val();
    $.ajax({
        data: {idx: idx},
        type: "POST",
        url: "/search_child_cate",
        success: function(res) {
            console.log(res.data.length)
            if (res.data) {
                let str = "";
                $("#thirdChildCate option").remove();
                if (res.data.length > 0) {
                    for(let i = 0; i < res.data.length; i++) {
                        str +=  "<option value='" + res.data[i].IDX + "'>" + res.data[i].NAME + "</option>";
                    }
                } else {
                    str += "<option value=''>-</option>"
                }
                $("#thirdChildCate").append(str);
            }
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
  		  	<h2><span></span>교육관리 >
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

        <form id="frm" method="post" action="${method eq 'Regist' ? '/save_education' : '/modify_education'}"  enctype="multipart/form-data">
            <input type="hidden" name="educationId" value="${educationInfo.education_id}"/>
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
                                <input type="text" id="title" name="title" value="${educationInfo.title}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">요약</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl"><input type="text" name="summary" placeholder="" value=""></td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl"><input type="text" name="summary" placeholder="" value="${educationInfo.summary}"></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">시간</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl"><input type="text" name="playTime" placeholder="" value=""></td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl"><input type="text" name="playTime" placeholder="" value="${educationInfo.play_time}"></td>
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
                                <input type="radio" name="showFlag" id="ra1-1" value="0" <c:if test="${educationInfo.show_flag eq '0'}">checked</c:if>><label for="ra1-1">노출</label>
                                <input type="radio" name="showFlag" id="ra1-2" value="1" <c:if test="${educationInfo.show_flag eq '1'}">checked</c:if>><label for="ra1-2">비노출</label>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">썸네일 이미지</th>
                    <td class="tl">
                        <div id="fileLayer"></div>
                    </td>
                </tr>
                <tr>
                    <th class="tl">내용</th>
                    <td class="tl">
                        <textarea id="snote" name="content"><c:if test="${method eq 'Modify'}">${educationInfo.content}</c:if></textarea>
                    </td>
                </tr>
                <tr>
                    <th class="tl">동영상 링크</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" name="urlLink" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" name="urlLink" value="${educationInfo.url_link}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">미리보기 링크</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" name="previewLink" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" name="previewLink" value="${educationInfo.preview_link}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th class="tl">이동 링크</th>
                    <c:choose>
                        <c:when test="${method eq 'Regist'}">
                            <td class="tl">
                                <input type="text" name="moveUrl" value="">
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="tl">
                                <input type="text" name="moveUrl" value="${educationInfo.move_url}">
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
              </tbody>
            </table>

            <br />
            <br />

            <div class="body-head">
                <h3>
                    상품 선택
                </h3>
                <br/>
                <a class="btn-large gray-o btn-pop" data-id="good-search">상품 검색하기</a>
            </div>

            <table>
                <colgroup>
                    <col width="10%">
                    <col width="10%">
                    <col width="5%">
                    <col width="10%">
                    <col width="5%">
                </colgroup>
                <thead>
                <tr>
                    <th>
                        상품명
                    </th>
                    <th>
                        상품 코드
                    </th>
                    <th>
                        가격
                    </th>
                    <th>
                        이미지
                    </th>
                    <th>
                        삭제
                    </th>
                </tr>
                </thead>
                <tbody id="appendGoodsList">
                    <c:if test="${!empty goodsInfo}">
                        <tr>
                            <td>
                                ${goodsInfo.goods_name}
                                <input type="hidden" name="goodsId" value="${goodsInfo.goods_id}">
                            </td>
                            <td>
                                ${goodsInfo.goods_code}
                                <input type="hidden" name="goodsCode" value="${goodsInfo.goods_code}">
                            </td>
                            <td>
                                ${goodsInfo.price}
                            </td>
                            <td>
                                <img style='width: 50%;' src="https://store.nextplayer.co.kr/data/base/goods/small/${goodsInfo.goods_img}">
                            </td>
                            <td>
                                <button class='btn-large gray' onclick='removeAppendGoods()'>삭제</button>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

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

    <div class="pop" id="good-search">
        <div style="height:auto;">
            <div style="height:auto; width: 800px;">
                <div class="head">
                    상품 검색
                </div>
                <%--<div class="body" style="padding:15px 20px;">
                    <ul class="signup-list">
                        <li class="title">
                            <span class="title">상품 명</span>
                            <input type="text" name="sKeyword">
                            <a class="btn-large default" onclick="fnSearchGoods()">검색</a>
                        </li>
                    </ul>
                </div>--%>
                <div class="head p10 grid5">
                    <select id="parentCate" style="width:100px;" onchange="fnChangeCate()">
                        <c:forEach var="result" items="${storeCategory}" varStatus="status">
                            <option value="${result.IDX}">${result.NAME}</option>
                        </c:forEach>
                    </select>
                    <select id="childCate" onchange="fnChangeChildCate()">
                        <c:forEach var="result" items="${childCategory}" varStatus="status">
                            <option value="${result.IDX}">${result.NAME}</option>
                        </c:forEach>
                    </select>
                    <select id="thirdChildCate">
                        <c:forEach var="result" items="${thirdChild}" varStatus="status">
                            <option value="${result.IDX}">${result.NAME}</option>
                        </c:forEach>
                    </select>
                    <input type="text" name="sKeyword">

                    <a class="btn-large default" onclick="fnSearchGoods()">검색</a>
                </div>
                <div id="searchResultDiv">
                    <table>
                        <colgroup>
                            <col width="10%">
                            <col width="10%">
                            <col width="5%">
                            <col width="10%">
                            <col width="15%">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>
                                    상품명
                                </th>
                                <th>
                                    상품 코드
                                </th>
                                <th>
                                    가격
                                </th>
                                <th>
                                    이미지
                                </th>
                                <th>
                                    선택
                                </th>
                            </tr>
                        </thead>
                        <tbody id="goodsList">
                        </tbody>
                    </table>
                </div>
                <div class="foot">
                    <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
                </div>
            </div>
        </div>
    </div>

</body>
</html>