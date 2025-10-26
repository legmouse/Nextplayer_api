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
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">
<link rel="stylesheet" href="resources/css/content.css?ver=2">

<script type="text/javascript">

$(document).ready(function() {
    /*$('input[type="checkbox"]').on('click', function() {
        $('input[type="checkbox"]').prop('checked', false);
        $(this).prop('checked', true);
    });*/


    detail = null;
    order = 1;

    <c:if test="${!empty bannerList}">
        let bannerListStr = "";
        order = Number('${fn:length(bannerList)}') + 1;
        <c:forEach var="item" items="${bannerList}" varStatus="status">
        bannerListStr += "<tr id='tr${item.banner_id}' class='categoryInfo'>";
        bannerListStr +=  "<td>";
        bannerListStr +=    "<select class='uage'>";
        bannerListStr +=        "<option value=''>전체</option>";
        bannerListStr +=        "<option value='U22' <c:if test="${item.uage eq 'U22'}">selected</c:if>>대학</option>";
        bannerListStr +=        "<option value='U18' <c:if test="${item.uage eq 'U18'}">selected</c:if>>고등</option>";
        bannerListStr +=        "<option value='U15' <c:if test="${item.uage eq 'U15'}">selected</c:if>>중등</option>";
        bannerListStr +=        "<option value='U12' <c:if test="${item.uage eq 'U12'}">selected</c:if>>초등</option>";
        bannerListStr +=    "</select>";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "<div class='w100 fl'>";
        bannerListStr +=        "<input type='date' name='sdate' class='sdate' value='${item.show_sdate}' autocomplete='off' >";
        bannerListStr +=      "</div>";
        bannerListStr +=      "<div class='w5 fl tc' style='height:28px; line-height:28px;'>~</div>";
        bannerListStr +=      "<div class='w100 fl'>";
        bannerListStr +=        "<input type='date' name='edate' class='edate' value='${item.show_edate}' autocomplete='off' >";
        bannerListStr +=      "</div>";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "${item.title}";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "${item.url_link}";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        <c:if test="${!empty item.files}">
            <c:forEach var="file" items="${item.files}" varStatus="status2">
        bannerListStr +=      "<img style='width: 300px; height: 100px;' src='/NP${file.file_save_path}'>";
            </c:forEach>
        </c:if>
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "<input type='hidden' data-order='ord${item.banner_id}' class='detail_order' value='${status.index + 1}'>";
        bannerListStr +=      "<input class='bannerId' type='hidden' id='input${item.banner_id}' name='bannerId' value='${item.banner_id}'>";
        bannerListStr +=      "<input type='checkbox' id='currentTr${status.index + 1}' name='currentTr${status.index + 1}' class='currentTr'>";
        bannerListStr +=      "<button class='btn-large gray-o' data-id='${item.banner_id}' onclick='removeTab(this)'>삭제하기</button>";
        bannerListStr +=  "</td>";
        bannerListStr +=  "<td>";
        bannerListStr +=      "<button class='btn-large gray-o' data-id='${item.banner_id}' onclick='gotoBannerModify(this)'>배너 수정하기</button>";
        bannerListStr +=  "</td>";
        bannerListStr += "</tr>";
        </c:forEach>

        $("#bannerTBody").append(bannerListStr);
    </c:if>


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
        if (!(index == 1)) {
            $tr.prev().before($tr);
            $tr.find('.detail_order').val(index-1);
            $tr.next().find('.detail_order').val(index);
        }
    });

    $(document).on('click', ".btn_down", function() {

        //var last_order = detail ? detail[detail.length - 1].detailOrder : order;
        var last_order = order;
        var el = $('input:checkbox[class=currentTr]:checked');
        var $tr = $(el).parent().parent();
        var current_order = $tr.index() + 1;
        if (current_order != last_order-1) {
            $tr.next().after($tr);
            var pre_index = $tr.prev().find('.detail_order').val();
            var index = $tr.index() + 1;
            $tr.find('.detail_order').val(index);
            $tr.prev().find('.detail_order').val(pre_index - 1);
        }

    });

});

const fnSaveBanner = (key) => {
    let row = [];
    let bannerId = $('.bannerId');
    let detailOrder = $('.detail_order');
    let sdate = $('.sdate');
    let edate = $('.edate');
    let uage = $('.uage');
    for (let i = 0; i < bannerId.length; i++) {
        if(!isEmpty(sdate[i].value) && !isEmpty(edate[i].value)) {
            if(sdate[i].value > edate[i].value){
                alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
                return false;
            }
        } else {
            alert('시작 날짜 종료 날짜를 입력해주세요.');
            return false;
        }

        row[i] = {
            bannerId : bannerId[i].value,
            bannerOrder : detailOrder[i].value,
            sdate : sdate[i].value,
            edate : edate[i].value,
            uage : $(uage[i]).val()
        }
    }
    console.log('row : ', row);
    let param = {
        bannerConfigId : '${bannerConfigId}',
        bannerList: JSON.stringify(row),
        bannerType: key == 'banner' ? 'Banner' : ''
    }

    const confirmMsg = confirm('저장 하시겠습니까?');


    if (confirmMsg) {
        $.ajax({
            url : '/save_sub_banner',
            type : 'POST',
            dataType : 'JSON',
            contentType : "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function(data){
                var success = "success";
                var fail = "fail";

                if(success == data.result){
                    alert("저장 되었습니다.");
                    location.reload();
                }
            }
        })
    }
}


function escapeHTML(text) {
    let map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };

    return text.replace(/[&<>"']/g, function(m) {
        return map[m];
    });
}


const fnSearchBanner = () => {

    let sTitle = $('input[name=sBannerTitle]').val();


    let param = {
        sTitle : sTitle,
        key : 'banner'
    }

    $.ajax({
        type: 'POST',
        url: '/search_media_data',
        data: param,
        success: function(res) {

            if (res.state == 'success') {
                let str = "";
                if (res.data.length > 0) {
                        for (let i = 0; i < res.data.length; i++) {
                            str += "<tr>";
                            str +=  "<td>";
                            str +=      "<input class='bannerChk' type='checkbox' value='" + res.data[i].banner_id + "' name='ch"+i+"' id='ch"+i+"-"+i+"' ";
                            str +=      "data-title='" + res.data[i].title + "' ";
                            if (res.data[i].files) {
                                for (let j = 0; j < res.data[i].files.length; j++) {
                            str +=      "data-fileSavePath='/NP" + res.data[i].files[j].file_save_path + "' ";
                                }
                            } else {
                            str +=      "data-fileSavePath='' ";
                            }
                            res.data[i].url_link ? str += "data-urlLink='" + res.data[i].url_link + "' >" : str += "data-urlLink='' >";
                            str +=      "<label for='ch"+i+"-"+i+"'></label>";
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].title;
                            str +=  "</td>";
                            str +=  "<td>";
                            str +=      res.data[i].url_link ? res.data[i].url_link : '';
                            str +=  "</td>";
                            str +=  "<td>";
                            if (res.data[i].files) {
                                for (let j = 0; j < res.data[i].files.length; j++) {
                            str +=      "<img style='width: 300px; height: 100px;' src='/NP" + res.data[i].files[j].file_save_path + "'>";
                                }
                            }
                            str +=  "</td>";
                            str +=  "</tr>";

                        }

                        $("#bannerList").append(str);
                } else {
                    str += "<tr><td colspan='4'>검색 결과가 없습니다.</td></tr>"
                    $("#bannerList tr").empty();
                    $("#bannerList").append(str);
                }
            }
        }
    });
}

const addBannerList = (key) => {
    //const chkMedia = $("input[type=checkbox]:checked");
    const chkBanner = $("input:checkbox[class='bannerChk']:checked");
    let str = "";
    let inputStr = "";

    $(chkBanner).each(function(i) {
        //++order;
        str += "<tr id='tr"+$(this).val()+"' class='categoryInfo'>";
        str +=  "<td>";
        str +=    "<select class='uage'>";
        str +=        "<option value=''>전체</option>";
        str +=        "<option value='U22'>대학</option>";
        str +=        "<option value='U18'>고등</option>";
        str +=        "<option value='U15'>중등</option>";
        str +=        "<option value='U12'>초등</option>";
        str +=    "</select>";
        str +=  "</td>";
        str +=  "<td align='center'>";
        str +=      "<div class='w100 fl'>";
        str +=          "<input type='date' name='sdate' class='sdate' autocomplete='off' >";
        str +=      "</div>";
        str +=      "<div class='w5 fl tc' style='height:28px; line-height:28px;'>~</div>";
        str +=      "<div class='w100 fl'>";
        str +=          "<input type='date' name='edate' class='edate' autocomplete='off' >";
        str +=      "</div>";
        str +=  "</td>";
        str +=  "<td>";
        str +=      $(this).attr('data-title');
        str +=  "</td>";
        str +=  "<td>";
        str +=      $(this).attr('data-urlLink');
        str +=  "</td>";
        str +=  "<td>";
        str +=      $(this).attr('data-fileSavePath') ? "<img style='width: 300px; height: 100px;' src='" + $(this).attr('data-fileSavePath') + "' >" : '';
        str +=  "</td>";
        str +=  "<td>";
        str +=      "<input type='hidden' data-order='ord"+$(this).val()+"' class='detail_order' value='"+ order +"'>";
        str +=      "<input class='bannerId' type='hidden' id='input"+$(this).val()+"' name='bannerId' value='" + $(this).val() + "'>";
        str +=      "<input type='checkbox' id='currentTr" +i+ "' name='currentTr" +i+ "' class='currentTr'>";
        /*str +=      "<button class='btn-large gray-o' onclick='removeTab("+order+", \"" +$(this).val()+ "\")'>삭제하기</button>";*/
        str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='removeTab(this)'>삭제하기</button>";
        str +=  "</td>";
        str +=  "<td>";
        str +=      "<button class='btn-large gray-o' data-id='"+$(this).val()+"' onclick='gotoBannerModify(this)'>배너 수정하기</button>";
        str +=  "</td>";
        str +=  "</tr>";

        order++;
    });

    $("#bannerTBody").append(str);
    $("#bannerDiv").append(inputStr);

    $(".pop").fadeOut();


    $("input[name=sBannerTitle]").val(null);
    $("#bannerList").empty();
    const listStr = "<td colspan='7'>검색 해주세요.</td>";
    $("#bannerList").append(listStr);

}

const removeTab = (el) => {

    let id = $(el).attr('data-id')
    let ordValue = $('input[data-order="ord'+id+'"]').val();

    let detailOrder = $(".detail_order");
    let lastValue = $(".detail_order")[detailOrder.length -1].value;

    if (ordValue != lastValue) {
        $.each(detailOrder, function(idx, item) {
            if (idx > (ordValue -1)) { // 현재 value 이후의 order 1씩 감소
                let currentValue = parseFloat($(item).val());
                $(item).val(currentValue - 1);
            }
        });
    }

    --order;
    $("#input" + id).remove();
    $("#tr" + id).remove();
}

const gotoBannerModify = (el) => {
    window.open('/saveBanner?method=modify&bannerId=' + $(el).attr('data-id'), "_blank");
}

const fnMoveList = () => {
    location.href = "/subBannerList";
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
  		  	<h2><span></span>설정 > 서브 배너 설정</h2>
        </div>
      </div>
          <div class="round body">
              <div class="body-head">
                  <h4 class="view-title">
                      <c:choose>
                          <c:when test="${configKey eq 'main'}">
                              메인 배너
                          </c:when>
                          <c:when test="${configKey eq 'mainSub'}">
                              메인 서브 배너
                          </c:when>
                          <c:when test="${configKey eq 'cupSub'}">
                              대회 예선 배너
                          </c:when>
                          <c:when test="${configKey eq 'cupMain'}">
                              대회 본선 배너
                          </c:when>
                          <c:when test="${configKey eq 'cupTour'}">
                              대회 토너먼트 배너
                          </c:when>
                          <c:when test="${configKey eq 'league'}">
                              리그 배너
                          </c:when>
                          <c:when test="${configKey eq 'team'}">
                              팀 상세 배너
                          </c:when>

                      </c:choose>
                  </h4>
                  <div class="others">
                      <a class="btn-large gray-o btn-pop" data-id="pop-banner">추가하기</a>
                  </div>
              </div>
              <div class="content_mainMenu mb_10">
                  <div>
                      <div class="category_wrap">
                          <div class="ca_body view">
                              <div class="" id="tab_view_1">
                                  <table class="">
                                      <thead>
                                      <tr>
                                          <th>연령</th>
                                          <th>노출기간</th>
                                          <th>배너명</th>
                                          <th>URL</th>
                                          <th>이미지</th>
                                          <th>삭제하기</th>
                                          <th>수정하기</th>
                                      </tr>
                                      </thead>
                                      <tbody id="bannerTBody">

                                      </tbody>
                                  </table>
                              </div>
                          </div>

                          <br />

                          <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                            border-top: 1px solid #ccc;
                            grid-template-columns: 1fr 1fr;
                            display: grid;
                            gap: 5px;
                            padding: 15px;">
                              <button class="btn_sku btn_up blue_olbg">위로 이동</button>
                              <button class="btn_sku btn_down blue_olbg">아래로 이동</button>
                          </div>
                          <div class="ca_foot" style="background-color: rgba(1,1,1,.02);
                            border-top: 1px solid #ccc;
                            grid-template-columns: 1fr 1fr;
                            display: grid;
                            gap: 5px;
                            padding: 15px;">
                              <button type="button" class="btn_sku gray" id="cancle" onclick="fnMoveList()">취소</button>
                              <button type="button" class="btn_sku btn_save blue_olbg" id="save" onclick="fnSaveBanner('${key}')">저장</button>
                          </div>
                      </div>
                  </div>
              </div>
          </div>
    </div>

    <div class="pop" id="pop-banner">
        <div style="height:auto;">
            <div style="height:auto; width: 1100px;">
                <div class="head">
                    <c:choose>
                        <c:when test="${configKey eq 'main'}">
                            메인 배너
                        </c:when>
                        <c:when test="${configKey eq 'mainSub'}">
                            메인 서브 배너
                        </c:when>
                        <c:when test="${configKey eq 'cupSub'}">
                            대회 예선 배너
                        </c:when>
                        <c:when test="${configKey eq 'cupMain'}">
                            대회 본선 배너
                        </c:when>
                        <c:when test="${configKey eq 'cupTour'}">
                            대회 토너먼트 배너
                        </c:when>
                        <c:when test="${configKey eq 'league'}">
                            리그 배너
                        </c:when>
                        <c:when test="${configKey eq 'team'}">
                            팀 상세 배너
                        </c:when>
                    </c:choose>
                    추가
                    <a class="close btn-close-pop"></a>
                </div>
                <div class="head p10">
                    <input style="width: 50%" type="text" name="sBannerTitle" id="sBannerTitle" placeholder="제목을 입력해주세요" onkeypress="if(event.keyCode == 13) {fnSearchBanner(); return;}">
                    <a class="btn-large default" onclick="fnSearchBanner()">검색</a>
                </div>
                <div class="body" style="padding:15px 20px;">
                    <div>
                        <table cellspacing="0" class="">
                            <colgroup>
                                <col width="30px">
                                <col width="*">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>선택</th>
                                <th>배너명</th>
                                <th>URL</th>
                                <th>이미지</th>
                            </tr>
                            </thead>
                            <tbody id="bannerList">
                            <tr>
                                <td colspan="4">
                                    검색 해주세요.
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <div class="mt_10 w100 tr">
                            <a class="btn-large default" onclick="addBannerList('${key}')">추가하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>