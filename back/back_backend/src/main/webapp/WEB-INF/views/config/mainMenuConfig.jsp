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
<link rel="stylesheet" href="resources/css/content.css?ver=1">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

$(document).ready(function() {

    /*$('input[type="radio"]').on('click', function() {
        console.log($(this))
        let index = $(this).attr('data-index');

        $('input:radio[name=useChk' + index + ']').prop('checked', false);
        $('input:radio[name=unUseChk' + index + ']').prop('checked', false);

        $(this).prop('checked', true);
    });*/

    $('input[type="radio"]').on('click', function() {
        alert()
        if ($(this).prop('checked')) {
            const radioName = $(this).attr('name');
            $(`input[name="${radioName}"]`).not(this).prop('checked', false);
        }
    });

    $(document).on('click', '.categoryInfo', function() {


        var checkBox = $(this).find('.currentTr');
        $('.currentTr').prop("checked", false);
        $('.categoryInfo').css("background-color", "");
        checkBox.prop("checked", true);
        var el =  $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        $tr.css("background-color", "rgb(128, 183, 255)")

    });

    $(document).on('click', ".btn_up", function() {

        var el = $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        var index = $tr.find('.detail_order').val();
        console.log('index : ', index)
        if (!(index == 1)) {
            $tr.prev().before($tr);
            $tr.find('.detail_order').val(index-1);
            $tr.next().find('.detail_order').val(index);
        }
    });

    $(document).on('click', ".btn_down", function() {
        var last_order = detail[detail.length - 1].detailOrder;
        var el = $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        var current_order = $tr.index();

        if (current_order != last_order) {
            $tr.next().after($tr);
            var pre_index = $tr.prev().find('.detail_order').val();
            console.log('pre_index : ', pre_index)
            var index = $tr.index() + 1;
            $tr.find('.detail_order').val(index);
            $tr.prev().find('.detail_order').val(pre_index-1);
        }
    });

    let detail;

    $.ajax({
        url : '/main_menu_config',
        type : 'GET',
        success : function(data) {
            detail = data.menuList;

            let str = '';

            $.each(detail, function(idx, item) {
                str += '<tr id="' + item.main_menu_id + '" data-index="' + idx + '" class="categoryInfo">';
                str += '<td>' + item.menu_order + '</td>';
                str += '<td>' + item.menu_name + '</td>';
                str += '<td>';
                if (item.use_flag == '0') {
                    str += '<input class="useFlag" type="radio" data-index="' + idx + '" name="ra'+idx+'" id="ra'+idx+'_1" value="0" checked> ';
                    str += '<label for="ra' + idx + '_1">활성</label>';
                    str += '<input class="useFlag" type="radio" data-index="' + idx + '" name="ra'+idx+'" id="ra'+idx+'_2" value="1"> ';
                    str += '<label for="ra' + idx + '_2">비활성</label>';
                } else {
                    str += '<input class="useFlag" type="radio" data-index="' + idx + '" name="ra'+idx+'" id="ra'+idx+'_1" value="0"> ';
                    str += '<label for="ra' + idx + '_1">활성</label>';
                    str += '<input class="useFlag" type="radio" data-index="' + idx + '" name="ra'+idx+'" id="ra'+idx+'_2" value="1" checked> ';
                    str += '<label for="ra' + idx + '_2">비활성</label>';
                }
                str += '<input type="hidden" class="seq" value="'+ item.main_menu_id+'">';
                str += '<input type="hidden" class="detail_order" value="'+ item.menu_order+'">';
                str += '<input type="checkbox" id="currentTr' +idx+ '" name="currentTr' +idx+ '" class="currentTr">';
                str += '</td>';
                if (item.menu_key !== 'leagueRank') {
                str += '<td><button class="btn-large default" onclick="fnGoMenuDet(\'' + item.menu_key + '\')">수정하기</button></td>';
                } else {
                str += '<td>-</td>';
                }
            });

            $('tbody').html(str);
        }

    });

    $(document).on("click", ".btn_save", function() {

        /* var currList = $('.ac'); */
        var detailOrder = $('.detail_order');
        var seq = $('.seq');
        var useFlag = $('input:radio[class="useFlag"]:checked');


        var row = [];

        for(var i = 0; i < seq.length; i++) {

            row[i] = {
                "mainMenuId": seq[i].value,
                "menuOrder": detailOrder[i].value,
                "useFlag": useFlag[i].value
            };

        }

        if(confirm("현재 카테고리 상태를 저장하시겠습니까?")) {
            $.ajax({
                url: '/saveMainMenu.do',
                type: 'POST',
                dataType: 'JSON',
                contentType : "application/json; charset=UTF-8",
                data: JSON.stringify(row),
                success: function(data){
                    var success = "success";
                    var fail = "fail";

                    if(success == data.result){
                        alert("카테고리를 저장했습니다.");
                        location.reload();
                    }
                }
            });
        }

    });

});

const fnChangePw = () => {

    const confirmMsg = confirm("비밀번호를 변경 하시겠습니까?");

    if (confirmMsg) {

        let userId = $("input[name=userId]").val();
        let pw = $("input[name=pw]").val();

        if(!pw) {
            alert("변경할 비밀번호를 입력해 주세요.");
            return false;
        }

        let param = {
            userId : userId,
            pw : pw
        };

        $.ajax({
            type: 'POST',
            url: '/update_pw',
            data: param,
            success: function(res) {
                if (res.state == "SUCCESS") {
                    alert('비밀번호 변경이 완료 되었습니다.');
                    location.reload();
                } else {
                    alert('비밀번호 변경에 실패했습니다.');
                    return false;
                }
            }
        });

        document.updateFrm.submit();
    }

}

const fnRemoveManager = (val) => {
    const confirmMsg = confirm("해당 관리자를 삭제 하시겠습니까?");

    if (confirmMsg) {
        $.ajax({
            type: 'POST',
            url: '/update_manager',
            data: {userId : val},
            success: function(res) {
                if (res.state == "SUCCESS") {
                    alert("삭제 완료 되었습니다.");
                    location.reload();
                } else {
                    alert("삭제 실패했습니다.");
                    return false;
                }
            }
        });
    }
}

const fnMoveRegist = () => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/menuAuthModify');
    newForm.append($('<input/>', {type: 'hidden', name: 'method', value: 'Regist' }));
    $(newForm).appendTo('body').submit();
}

const fnModifyMenuAuth = () => {
    const method = '${method}';

    const confirmMsg = method == 'Regist' ? confirm('등록 하시겠습니까?') : confirm('수정 하시겠습니까?');

    if (confirmMsg) {
        let userId = $("input[name=userId]").val();
        let id = $("input[name=id]").val();
        let pw = $("input[name=pw]").val();
        let userName = $("input[name=userName]").val();
        let grade = $("select[name=grade]").val();
        const chkMatch = $("input[type=checkbox]:checked");
        let menuArr = [];

        chkMatch.each(function(i) {
            menuArr.push($(this).val());
        });

        if (!id) {
            alert('아이디를 입력 해주세요.');
            return false;
        }
        if (!pw && method == 'Regist') {
            alert('비밀번호를 입력 해주세요.');
            return false;
        }
        if (!userName) {
            alert('담당자를 입력 해주세요.');
            return false;
        }

        let param = {
            id: id,
            pw: pw,
            userName: userName,
            grade: grade,
            method: method,
            menuArr: JSON.stringify(menuArr),
            userId: userId
        }

        $.ajax({
            type: 'POST',
            url: '/update_manager',
            data: param,
            success: function(res) {
                console.log('res : ', res);
                if (res.state == 'SUCCESS') {
                    alert(method == 'Regist' ? '등록을 완료 했습니다.' : '수정을 완료 했습니다.');
                    location.href = "/menuAuth";
                } else {
                    alert(method == 'Regist' ? '등록에 실패 했습니다' : '수정에 실패 했습니다.');
                    return false;
                }
            }
        });

    }

}

const fnGoMenuDet = (key) => {
    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    newForm.attr('action', '/mainMenuDet');
    newForm.append($('<input/>', {type: 'hidden', name: 'key', value: key }));
    $(newForm).appendTo('body').submit();
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
  		  	<h2><span></span>설정 > 메인 메뉴 설정</h2>
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
        </div>

        <br />

          <div class="content_mainMenu mb_10">
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
                                      <col width="50px">
                                      <col width="50px">
                                      <col width="70px">
                                  </colgroup>
                                  <thead>
                                  <tr>
                                      <th>순서</th>
                                      <th>분류</th>
                                      <th>활성/비활성</th>
                                      <th>수정</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  </tbody>
                              </table>
                          </div>
                      </div>
                      <div class="ca_foot">
                          <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                          <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                      </div>
                      <div class="ca_foot">
                          <button type="button" class="btn_sku gray" id="cancle">취소</button>
                          <button type="button" class="btn_sku btn_save blue_olbg" id="save">저장</button>
                      </div>
                  </div>
              </div>
          </div>

        <%--<div class="body-foot">
            <div class="others">
                <a class="btn-large default" onclick="fnModifyMenuAuth()">
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
          </div>--%>
      </div>
    </div>


</body>
</html>