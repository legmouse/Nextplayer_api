<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script src="resources/js/layout.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/css/content.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

  $(document).ready(function(){

    let categoryType = "";

    var categoryCd= null;
    var codeMax = null;
    var codeVal = null;

    $("#cancle").on('click', function() {
      if(confirm("변경 내용을 저장하지 않고 페이지를 나가시겠습니까?")){
        location.reload();
      }
    });

    $(document).on("click", '.ca_body.list a', function () {
      order = 0;
      clickCount = 0;
      $('.ca_body.list a, .tab_view').removeClass("ac");
      $(this).addClass("ac");
      $(".tab_view").addClass("ac");
      categoryCd = $(this).attr("id");

      sessionStorage.setItem("categoryCd", categoryCd);
      var jsonParam = {categoryCd : categoryCd};

      //const categoryType = '${category}'.toLowerCase();
      categoryType = $(this).attr('data-category');

      $('#selectDiv').hide();

      $.ajax({
        url: '/detail_'+categoryType+'_list',
        type: "GET",
        dataType: 'JSON',
        data: jsonParam,
        async: true,
        success: function(data){
          orgData = data;
          detail = data.detail;

          codeMax = data.codeMax;
          codeVal = data.valMax ? data.valMax : 0;

          var str = '';
          $.each(detail, function(idx, item){
            str += '<tr id="'+ item.category_cd + '" class="categoryInfo">';
            str += '<td><button class="btn_sku bottom_btn btn_delete gray_ol" data-id="'+ item.category_cd +'">-</button></td>';
            str += '<td><div class="inp_wrap">';
            str += '<input type="text" class="inp_sku detail_name org" value="' + item.category_name + '" data-org="" readonly>';
            str += '<input type="hidden" class="detail_cd" value="'+ item.category_cd+'">';
            str += '<input type="hidden" class="detail_order" value="'+ item.category_order+'">';
            str += '<input type="hidden" class="detail_code_value" value="'+ item.code_value+'">';
            str += '<input type="hidden" class="status" value="">';
            str += '<input type="hidden" class="sample_cd" value="'+ item.code_value+'">';
            str += '<input type="hidden" class="seq" value="'+ item.category_id+'">';
            str += '<input type="checkbox" class="currentTr"></div></td>';
            str += '<td><button class="btn_sku bottom_btn btn_modify blue_ol" data-id="'+ item.category_cd +'">수정</button></td>';
            str += '</tr>';
          });
          $('tbody').html(str);
          $('.btn_add').attr("disabled", false);
        }
      });

    });

    $(document).on('click', ".btn_add", function(){
      var code;

      var sam;
      var det;
      var parentCd;
      clickCount++;

      var code_str = codeMax;
      var code_ex = code_str.substr(1, 4);
      var code_num = Number(code_ex);

      //var value_str = codeVal[codeVal.length - 1].codeValue;
      //var value_num = Number(value_str);
      // console.info("value_str : " + value_str);

      const upperCode = categoryType.substring(0, 1).toUpperCase();

      if (code_num < 10 && code_num + 1 < 10) {
        code_num += clickCount;
        code = upperCode + '000' + String(code_num);
      } else if (code_num == 9) {
        code_num += clickCount;
        code = upperCode + '00' + String(code_num);
      } else if (code_num >= 10 && code_num + 1 < 100) {
        code_num += clickCount;
        code = upperCode + '00' + String(code_num);
      } else if (code_num == 99) {
        code_num += clickCount;
        code = upperCode + '0' + String(code_num);
      } else if (code_num >= 100 && code_num + 1 < 1000) {
        code_num += clickCount;
        code = upperCode + '0' + String(code_num);
      } else if (code_num == 999) {
        code_num += clickCount;
        code = upperCode + String(code_num);
      } else if (code_num >= 1000 && code_num + 1 < 10000) {
        code_num += clickCount;
        code = upperCode + String(code_num);
      }

      if (detail.length != 0) {
        var last_order = Number(detail[detail.length - 1].category_order);
        order = last_order + clickCount;
      }

      if (clickCount === 1) {
        codeVal = ++codeVal;
      }

      var add = '';

      add += '<tr class="categoryInfo" id="' + code + '">';
      add += '<td><button class="btn_sku btn_cancle gray_ol" data-id="' + code + '">-</button></td>';
      add += '<td><div class="inp_wrap_sku">';
      add += '<input type="text" class="inp_sku detail_name new" value="">';
      add += '<input type="hidden" class="detail_cd" value="'+ code +'">';
      add += '<input type="hidden" class="detail_order" value="'+ order + '">';
      add += '<input type="hidden" class="detail_code_value" value="'+ codeVal + '">';
      add += '<input type="hidden" class="status" value="C">';
      add += '<input type="hidden" class="seq" value="">';
      add += '<input type="checkbox" class="currentTr">';
      add += '</div></td>';
      add += '<td><button type="button" class="btn_sku btn_cancle blue_ol" data-id="' + code + '">취소</button></td>';
      add += '</tr>';

      $("tbody").append(add);
      order++;
      codeVal++;
    });

    $(document).on("click", ".btn_save", function() {

      /* var currList = $('.ac'); */
      var detailCd = $('.detail_cd');
      var detailName = $('.detail_name');
      var detailOrder = $('.detail_order');
      var detailCodeVal = $('.detail_code_value');

      var seq = $('.seq');

      var useType = null;

      useType = categoryCd;

      var status = $('.status');


      var row = [];

      for(var i = 0; i < seq.length; i++) {
        if (detailName[i].value == "" || detailName[i].value == null) {
          alert("상태값을 입력하세요.");
          return false;
        }

        row[i] = {
          "categoryId": seq[i].value,
          "categoryCd": detailCd[i].value,
          "categoryName": detailName[i].value,
          "parentCd": useType,
          "categoryOrder": detailOrder[i].value,
          "codeValue": detailCodeVal[i].value,
          "status": status[i].value,
          "categoryType": categoryType.toUpperCase()
        };

      }
      console.log('row : ',row)
      if(confirm("현재 카테고리 상태를 저장하시겠습니까?")) {
        $.ajax({
          url: '/saveCategory.do',
          type: 'POST',
          dataType: 'JSON',
          contentType : "application/json; charset=UTF-8",
          data: JSON.stringify(row),
          success: function(data){
            var success = "success";
            var fail = "fail";

            if(success == data.result){
              alert("카테고리를 저장했습니다.");
              var sessionGroupCd =sessionStorage.getItem("categoryCd");
              $('#'+ sessionGroupCd).addClass("ac");

              var sessionUseType = sessionStorage.getItem("useType");

              var jsonParam = {
                categoryCd: sessionGroupCd
              };

              if (categoryCd == 'C0003') {

                jsonParam = {
                  categoryCd : sessionUseType,
                };

              } else if (categoryCd == 'C0004') {

                jsonParam = {
                  categoryCd : sessionUseType
                };

              }  else if (categoryCd == 'C0002') {

                jsonParam = {
                  categoryCd : sessionUseType,
                };

              }

              $.ajax({
                url: '/detail_'+categoryType+'_list',
                type: "GET",
                dataType: 'JSON',
                data: jsonParam,
                async: true,
                success: function(data){
                  orgData = data;
                  detail = data.detail;
                  var str = '';
                  console.log(data);
                  $.each(detail, function(idx, item){
                    str += '<tr id="'+ item.category_cd + '" class="categoryInfo">';
                    str += '<td><button class="btn_sku btn_delete gray_ol" data-id="'+ item.category_cd +'">-</button></td>';
                    str += '<td><div class="inp_wrap">';
                    str += '<input type="text" class="inp detail_name org" value="' + item.category_name +
                            '" data-org="" readonly>';
                    str += '<input type="hidden" class="detail_order" value="'+ item.category_order+'">';
                    str += '<input type="hidden" class="detail_code_value" value="'+ item.code_value+'">';
                    str += '<input type="hidden" class="detail_cd" value="'+ item.category_cd+'">';
                    str += '<input type="hidden" class="status" value="">';
                    str += '<input type="hidden" class="sample_cd" value="'+ item.code_value+'">';
                    str += '<input type="hidden" class="seq" value="'+ item.category_id+'">';
                    str += '<input type="checkbox" class="currentTr"></div></td>';
                    str += '<td><button class="btn_sku btn_modify blue_ol" data-id="'+ item.category_cd +'">수정</button></td>';
                    str += '</tr>';
                  });
                  $('tbody').html(str);
                  $('.btn_add').attr("disabled", false);
                }
              });
              clickCount = 0;
            }
          }
        });
      }

    });

    $(document).on('click', '.btn_cancle', function() {
      $(this).parent().parent().remove();
    });

    $(document).on('click', '.categoryInfo', function() {
      var checkBox = $(this).find(':checkbox');
      $('.currentTr').prop("checked", false);
      $('.categoryInfo').css("background-color", "");
      checkBox.prop("checked", true);
      var el = $('input[type="checkbox"]:checked');
      var $tr = $(el).parent().parent().parent();
      var index = $tr.find('.detail_order').val();
      $tr.css("background-color", "rgb(128, 183, 255)")
    });

    $(document).on('click', ".btn_up", function() {

      var el = $('input[type="checkbox"]:checked');
      var $tr = $(el).parent().parent().parent();
      var index = $tr.find('.detail_order').val();
      console.log(index)
      if (!(index == 0)) {
        $tr.prev().before($tr);
        $tr.find('.detail_order').val(index-1);
        $tr.next().find('.detail_order').val(index);
      }
    });

    // 순서 바꿈(아래로)
    $(document).on('click', ".btn_down", function() {
      var last_order = detail[detail.length - 1].detailOrder;
      var el = $('input[type="checkbox"]:checked');
      var $tr = $(el).parent().parent().parent();
      var current_order = $tr.index();

      if (current_order != last_order) {
        $tr.next().after($tr);
        var pre_index = $tr.prev().find('.detail_order').val();
        var index = $tr.index();
        $tr.find('.detail_order').val(index);
        $tr.prev().find('.detail_order').val(pre_index-1);
      }
    });

    // 카테고리 삭제(행삭제)
    $(document).on('click', '.btn_delete', function() {
      var daID2 = $(this).attr("data-id");
      var tr = $(this).parent().parent();
      tr.hide();
      $("#"+ daID2 +" .status").val("D");
    });

    $(document).on('click', '.btn_modify', function () {

      var daID1 = $(this).attr("data-id");
      var daStatu2 = $(this).attr("data-statu");

      if(daStatu2 == 'write'){
        $("#"+ daID1 +" .org").attr("readonly",true);
        var afttext = $("#"+ daID1 +" .org").attr("data-org");
        $("#"+ daID1 +" .org").val(afttext);
        $("#"+ daID1 +" .status").val("");
        $(this).attr("data-statu","");
        $(this).html("수정");
      }else{
        $("#"+ daID1+ " .org").attr("readonly",false);
        var orgtext =  $("#"+ daID1 +" .org").val();
        $("#"+ daID1 +" .org").attr("data-org", orgtext);
        $("#"+ daID1 +" .status").val("M");
        $(this).attr("data-statu","write");
        $(this).html("취소");
      }

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
  		  	<h2><span></span>설정 > 메뉴설정</h2>

        </div>
        <div class="others">

        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
            <form name="frm" id="frm" method="post"  action="category_list" onsubmit="return false;">
            </form>
          </div>

        </div>

        <br/>

        <div class="content_category mb_10">
          <div>
            <div class="catergory_wrap">
              <div class="ca_head">
                그룹
              </div>
              <div class="ca_body list">
                <c:forEach var="data" items="${groups}" varStatus="status">
                  <a class="group" id="${data.category_cd}" data-category="${data.category_type}">
                      ${data.category_name}
                    <svg xmlns="http://www.w3.org/2000/svg" width="4" height="7" viewBox="0 0 4 7">
                      <path id="arr" data-name="arr" d="M3.5,0,7,4H0Z" transform="translate(4) rotate(90)" fill="#333"/>
                    </svg>
                  </a>
                </c:forEach>
              </div>
            </div>
          </div>
          <div>
          </div>
          <div>
            <div class="catergory_wrap">
              <div class="ca_head">
                상세
              </div>
              <div class="ca_body view">
                <div class="" id="tab_view_1">
                  <table class="">
                    <colgroup>
                      <col width="50px">
                      <col width="*">
                      <col width="70px">
                    </colgroup>
                    <thead>
                    <tr>
                      <th>삭제</th>
                      <th>상태값</th>
                      <th>수정</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                  </table>
                </div>
                <button class="btn_sku btn_add gray_ol mt_10"  disabled="disabled">+ 추가</button>
              </div>
              <div class="ca_foot">
                <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                <button class="btn_sku btn_down blue_olbg">이래로 이동</button>
              </div>
              <div class="ca_foot">
                <button type="button" class="btn_sku gray" id="cancle">취소</button>
                <button type="button" class="btn_sku btn_save blue_olbg" id="save">저장</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--팝업-->
    <!--팝업 끝-->
  <div>
  
<%-- <form name="frm" id="frm" method="post"  action="area">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="">
</form>   --%>

<form name="delFrm" id="delFrm" method="post"  action="save_area" >
   <input type="hidden" name="sFlag" value="1">
   <input type="hidden" name="areaId">
   <input type="hidden" name="useFlag" value="1">
</form>  
  
</body>
</html>