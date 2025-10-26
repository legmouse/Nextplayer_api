<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
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

<script type="text/javascript">


  $(document).ready(function() {

    if ('${method}' == 'modify') {

      let eleData = [];
      let midData = [];
      let highData = [];
      let collegeData = [];
      let proData = [];
      <c:forEach var="i" items="${playerHistory}" varStatus="status">
      <c:if test="${i.team_type eq '0'}">
      eleData.push({
        year: '${i.year}',
        teamId: '${i.team_id}',
        teamType: '${i.team_type}',
        teamName: '${i.team_name}',
        nickName: '${i.nick_name}',
        uage: '${i.uage}',
        useFlag: '${i.use_flag}'
      })
      </c:if>
      <c:if test="${i.team_type eq '1'}">
      midData.push({
        year: '${i.year}',
        teamId: '${i.team_id}',
        teamType: '${i.team_type}',
        teamName: '${i.team_name}',
        nickName: '${i.nick_name}',
        uage: '${i.uage}',
        useFlag: '${i.use_flag}'
      })
      </c:if>
      <c:if test="${i.team_type eq '2'}">
      highData.push({
        year: '${i.year}',
        teamId: '${i.team_id}',
        teamType: '${i.team_type}',
        teamName: '${i.team_name}',
        nickName: '${i.nick_name}',
        uage: '${i.uage}',
        useFlag: '${i.use_flag}'
      })
      </c:if>

      <c:if test="${i.team_type eq '3'}">
      collegeData.push({
        year: '${i.year}',
        teamId: '${i.team_id}',
        teamType: '${i.team_type}',
        teamName: '${i.team_name}',
        nickName: '${i.nick_name}',
        uage: '${i.uage}',
        useFlag: '${i.use_flag}'
      })
      </c:if>

      <c:if test="${i.team_type eq '4'}">
      proData.push({
        year: '${i.year}',
        teamId: '${i.team_id}',
        teamType: '${i.team_type}',
        teamName: '${i.team_name}',
        nickName: '${i.nick_name}',
        uage: '${i.uage}',
        useFlag: '${i.use_flag}'
      })
      </c:if>

      </c:forEach>
      console.log(eleData)
      fnModifyHtml(eleData, 0);
      fnModifyHtml(midData, 1);
      fnModifyHtml(highData, 2);
      fnModifyHtml(collegeData, 3);
      fnModifyHtml(proData, 4);

    }
    
    if ('${method}' == 'save') {
    	if ('${playerName}' != null && '${playerName}' != '' && '${playerName}' != 'undefined') {
    		$('input[name=name]').val('${playerName}');
    	}
    	
		if ('${position}' != null && '${position}' != '' && '${position}' != 'undefined') {
			$("select[name='position'] option[value='"+'${position}'+"']").prop("selected", true);
    	}
		
		if ('${birthday}' != null && '${birthday}' != '' && '${birthday}' != 'undefined') {
			$('input[name=birthday]').val('${birthday}');
		}
    }

  });

  const fnModifyHtml = (data, type) => {
    let typeStr = "";

    if (type == 0) {
      typeStr = "초등학교";
    } else if (type == 1) {
      typeStr = "중학교";
    } else if (type == 2) {
      typeStr = "고등학교";
    } else if (type == 3) {
      typeStr = "대학교";
    } else if (type == 4) {
      typeStr = "프로";
    }

    if (data.length > 0) {
      let num = 0;
      for (let i = 0 ; i < data.length; i++) {
        num++;
        console.log('i : ' + i)
        let str = "";
        str +=  "<tr>" +
                "<th class='tl'>";
        if (i == 0) {
          str +=  "<button class='btn-add' onclick='fnAppendTap("+type+")'></button>" + typeStr;
        } else {
          str +=  "<button class='btn-minus' onclick='fnRemoveTap("+type+")'></button>" + typeStr;
        }

        str +=  "<div class='box-group'>";

        //"<input type='radio' value='0' name='ex" + val + "_" + tbLength + "' id='ex" + val + "-" + ((tbLength * 2) + 1) + "' checked /><label for='ex" + val + "-" + ((tbLength * 2) + 1) + "'>노출</label>" +
        //"<input type='radio' value='1' name='ex" + val + "_" + tbLength + "' id='ex" + val + "-" + ((tbLength * 2) + 2) + "' /><label for='ex" + val + "-" + ((tbLength * 2) + 2) + "'>비노출</label>" +

        if (data[i].useFlag == '0') {
          if (i == 0) {
            str += "<input type='radio' value='0' name='ex" + type + "_" + i + "' id='ex" + type + "-1' checked /><label for='ex" + type + "-1'>노출</label>" +
                    "<input type='radio' value='1' name='ex" + type + "_" + i + "' id='ex" + type + "-2' /><label for='ex" + type + "-2'>비노출</label>";
          } else {
            str += "<input type='radio' value='0' name='ex" + type + "_" + i + "' id='ex" + type + "-" + ((i * 2) + 1) + "' checked /><label for='ex" + type + "-" + ((i * 2) + 1) + "'>노출</label>" +
                    "<input type='radio' value='1' name='ex" + type + "_" + i + "' id='ex" + type + "-" + ((i * 2) + 2)+ "' /><label for='ex" + type + "-" + ((i * 2) + 2) + "'>비노출</label>";
          }

        } else {
          if (i == 0) {
            str += "<input type='radio' value='0' name='ex" + type + "_" + i + "' id='ex" + type + "-1' /><label for='ex" + type + "-1'>노출</label>" +
                    "<input type='radio' value='1' name='ex" + type + "_" + i + "' id='ex" + type + "-2' checked /><label for='ex" + type + "-2'>비노출</label>";
          } else {
            str += "<input type='radio' value='0' name='ex" + type + "_" + i + "' id='ex" + type + "-" + ((i * 2) + 1) + "' /><label for='ex" + type + "-" + ((i * 2) + 1) + "'>노출</label>" +
                    "<input type='radio' value='1' name='ex" + type + "_" + i + "' id='ex" + type + "-" + ((i * 2) + 2) + "' checked /><label for='ex" + type + "-" + ((i * 2) + 2) + "'>비노출</label>";
          }
        }

          str += "</div>" +
                 "</th>" +
                 "<td class='tl' id='td" + type + "_" + i + "'>" +
                 "<div class='type4 mb_10'>" +
                 "<select id='sYear" + type + "_" + i + "'>";
        for (let y=2005; y <= ${sysYear}; y++) {
          if (Number(data[i].year) == y) {
            str +=  "<option value='"+y+"' selected >"+y+"</option>";
          } else {
            str +=  "<option value='"+y+"' >"+y+"</option>";
          }
        }

        str +=  "</select>" +
                "<button class='btn-large default btn-pop' data-divId='td" + type + "_" + i + "' onclick='fnPopup(this)'>학원/클럽 찾기</button>" +
                "<a class='btn-large gray-o x' data-id='td" + type + "_" + i + "' onclick='cancelSelectTeam(this)'>" + data[i].nickName +
                "<span class='cored'>" + data[i].uage + "</span></a>" +
                "<input type='hidden' name='td" + type + "_" + i + "' value='" + data[i].teamId + "'/>" +
                "</div>" +
        "</td>" +
        "</tr>";

        $("#teamType" + type).append(str);
      }
    } else {
      let str = "";
      str +=  "<tr>" +
              "<th class='tl'>" +
              "<button class='btn-add' onclick='fnAppendTap("+type+")'>-</button>" + typeStr +
              "<div class='box-group'>" +
              "<input type='radio' value='0' name='ex" + type + "_0' id='ex" + type + "-0' checked /><label for='ex" + type + "-0'>노출</label>" +
              "<input type='radio' value='1' name='ex" + type + "_0' id='ex" + type + "-0' /><label for='ex" + type + "-0'>비노출</label>" +
              "</div>" +
              "</th>" +
              "<td class='tl' id='td" + type + "_0'>" +
              "<div class='type4 mb_10'>" +
              "<select id='sYear" + type + "_0'>";
              for (let y=2005; y <= ${sysYear}; y++) {
                  str +=  "<option value='"+y+"' >"+y+"</option>";
              }
              /*"<option value='2023'>2023</option>" +
              "<option value='2022'>2022</option>" +
              "<option value='2021'>2021</option>" +
              "<option value='2020'>2020</option>" +
              "<option value='2019'>2019</option>" +
              "<option value='2018'>2018</option>" +
              "<option value='2017'>2017</option>" +
              "<option value='2016'>2016</option>" +
              "<option value='2015'>2015</option>" +
              "<option value='2014'>2014</option>" +
              "<option value='2013'>2013</option>" +
              "<option value='2012'>2012</option>" +
              "<option value='2011'>2011</option>" +
              "<option value='2010'>2010</option>" +
              "<option value='2009'>2009</option>" +
              "<option value='2008'>2008</option>" +
              "<option value='2007'>2007</option>" +
              "<option value='2006'>2006</option>" +
              "<option value='2005'>2005</option>" +*/
              str +=  "</select>" +
              "<button class='btn-large default btn-pop' data-divId='td" + type + "_0' onclick='fnPopup(this)'>학원/클럽 찾기</button>" +
              "</div>" +
              "</td>" +
              "</tr>";

      $("#teamType" + type).append(str);
    }
  }

  const fnAppendTap = (val) => {

    const tbLength = $("#teamType" + val + " > tr").length;
    console.log(tbLength);
    let teamStr = '';

    if (val == 0) {
      teamStr = '초등학교';
    } else if (val == 1) {
      teamStr = '중학교';
    } else if (val == 2) {
      teamStr = '고등학교';
    } else if (val == 3) {
      teamStr = '대학교';
    } else if (val == 4) {
      teamStr = '프로';
    }

    let str = "";
    str +=  "<tr>" +
            "<th class='tl'>" +
            "<button class='btn-minus' onclick='fnRemoveTap(" + val + ")'></button>" +
            teamStr +
            "<div class='box-group'>" +
            "<input type='radio' value='0' name='ex" + val + "_" + tbLength + "' id='ex" + val + "-" + ((tbLength * 2) + 1) + "' checked /><label for='ex" + val + "-" + ((tbLength * 2) + 1) + "'>노출</label>" +
            "<input type='radio' value='1' name='ex" + val + "_" + tbLength + "' id='ex" + val + "-" + ((tbLength * 2) + 2) + "' /><label for='ex" + val + "-" + ((tbLength * 2) + 2) + "'>비노출</label>" +
            "</div>" +
            "</th>" +
            "<td class='tl' id='td" + val + "_" + tbLength + "'>" +
            "<div class='type4 mb_10'>" +
            "<select id='sYear" + val + "_" + tbLength + "'>";
            for (let y=2014; y <= ${sysYear}; y++) {
              str +=  "<option value='"+y+"' >"+y+"</option>";
            }
            str += "</select>" +
            "<button class='btn-large default btn-pop' data-divId='td" + val + "_" + tbLength + "' onclick='fnPopup(this)'>학원/클럽 찾기</button>" +
            "</div>" +
            "</td>" +
            "</tr>";

    $("#teamType" + val).append(str);

  }

  const fnPopup = (el) => {

    $("input[name='sKeyword']").val(null);
    $("#teamList").empty();

    console.log($(el).attr("data-divId"));
    $(".pop").attr("data-divId", $(el).attr("data-divId"));
    $(".pop").show();
  }

  const fnRemoveTap = (val) => {
    const trCnt = $("#teamType" + val + " > tr").length;
    if (trCnt > 1) {
      $("#teamType" + val + " > tr:last").remove();
    }

  }

  const fnSearchTeam = () => {
    const ageGroup = $("#ageGroup option:selected").val();
    const sKeyword = $("input[name='sKeyword']").val();

    const params = {
      ageGroup: ageGroup,
      sKeyword: sKeyword
    };

    $.ajax({
      type: 'POST',
      url: '/player/search_team',
      data: params,
      success: function(res) {
        $("#teamList").empty();
        let str = "";
        if (res.state == 'SUCCESS') {
          if (res.data.teamList.length > 0) {
            for (let i = 0; i < res.data.teamList.length; i++) {
              str += "<div class='team_name'>";
              if (res.data.teamList[i].emblem) {
                str +=  "<img src='NP" + res.data.teamList[i].emblem + "' class='amblr'/>";
              } else {
                str +=  "<img src='resources/img/logo/none.png' class='amblr' />";
              }
              str += res.data.teamList[i].nick_name +
                        "<div class='tr'>" +
                        "<button class='btn-small blue-o' onclick='fnSelectTeam(" + res.data.teamList[i].team_id + ", \"" + res.data.teamList[i].uage + "\" ,\"" + res.data.teamList[i].nick_name + "\")'>선택</button>" +
                        "</div>" +
                      "</div>";
            }
          } else {
            str += "<div class='team_name'>검색 결과가 없습니다.</div>";
          }

          $("#teamList").append(str);

        }
      }
    })

  }

  const fnSelectTeam = (teamId, uage, nickName) => {
    const divId = $(".pop").attr("data-divId");

    $("#" + divId).find('a').remove();
    $("#" + divId).find('input').remove();

    let str = "<a class='btn-large gray-o x' data-id='"+divId+"' onclick='cancelSelectTeam(this)'>" +
                nickName +
              "<span class='cored'>" + uage + "</span></a>" +
              "<input type='hidden' name='" + divId + "' value='" + teamId + "'/>";

    $("#" + divId).append(str);
    $(".pop").fadeOut();
  }

  const cancelSelectTeam = (el) => {
    const dataId = $(el).attr("data-id");
    $("input[name='"+dataId+ "'").remove();
    $(el).remove();

  }

  const fnSavePlayer = () => {
    const useFlag = $("input[name='useFlag']:checked").val();
    const name = $("input[name='name']").val();
    const position = $("#position option:selected").val();
    const birthday = $("input[name='birthday']").val();


    const tb0Length = $("#teamType0 > tr").length;
    const tb1Length = $("#teamType1 > tr").length;
    const tb2Length = $("#teamType2 > tr").length;
    const tb3Length = $("#teamType3 > tr").length;
    const tb4Length = $("#teamType4 > tr").length;

    let newForm = $('<form></form>');
    newForm.attr('name', 'newForm');
    newForm.attr('method', 'post');
    if ('${method}' == 'save') {
      newForm.attr('action', '/save_player');
    } else {
      newForm.attr('action', '/modify_player');
      newForm.append($('<input/>', {type: 'hidden', name: 'playerId', value: '${playerInfo.player_id}' }));
    }


    newForm.append($('<input/>', {type: 'hidden', name: 'name', value: name }));
    newForm.append($('<input/>', {type: 'hidden', name: 'position', value: position }));
    newForm.append($('<input/>', {type: 'hidden', name: 'birthday', value: birthday }));
    newForm.append($('<input/>', {type: 'hidden', name: 'useFlag', value: useFlag }));

    newForm.append($('<input/>', {type: 'hidden', name: 'midCnt', value: tb1Length }));
    newForm.append($('<input/>', {type: 'hidden', name: 'eleCnt', value: tb0Length }));
    newForm.append($('<input/>', {type: 'hidden', name: 'highCnt', value: tb2Length }));
    newForm.append($('<input/>', {type: 'hidden', name: 'collegeCnt', value: tb3Length }));
    newForm.append($('<input/>', {type: 'hidden', name: 'proCnt', value: tb4Length }));

    for (let j = 0; j < 5; j ++) {
      for (let i = 0; i < $("#teamType" + j + " > tr").length; i++) {
        let teamIdVal = $("input[name='td" + j + "_" + i +"']").val();
        let useFlagVal = $("input[name='ex" + j + "_" + i +"']:checked").val();
        let sYearVal = $("#sYear" + j + "_" + i +" option:selected").val();
        console.log('j : ' + j + "[" + teamIdVal + ", " + useFlag + ", " + sYearVal + "]");
        newForm.append($('<input/>', {type: 'hidden', name: 'teamId' +j +'_' + i , value: teamIdVal }));
        newForm.append($('<input/>', {type: 'hidden', name: 'useFlag' + j + '_' + i , value: useFlagVal }));
        newForm.append($('<input/>', {type: 'hidden', name: 'sYear' + j + '_' + i , value: sYearVal }));
      }
    }

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
        <h2><span></span>선수 관리</h2>
      </div>
    </div>
    <div class="round body">
      <div class="body-head">
        <h4 class="view-title">대표팀 관리 > 선수관리 > 등록하기</h4>
      </div>

      <div class="scroll">
        <table cellspacing="0" class="update view">
          <colgroup>
            <col width="20%" />
            <col width="*" />
          </colgroup>
          <tbody>
          <tr>
            <th class="tl">활성/비활성</th>
            <td class="tl">
              <c:choose>
                <c:when test="${method eq 'save'}">
                  <input type="radio" value="0" name="useFlag" id="ra1-1" checked /><label for="ra1-1">활성</label>
                  <input type="radio" value="1" name="useFlag" id="ra1-2" /><label for="ra1-2">비활성</label>
                </c:when>
                <c:when test="${method eq 'modify'}">
                  <input type="radio" value="0" name="useFlag" id="ra1-1" <c:if test="${playerInfo.use_flag eq '0'}">checked</c:if> /><label for="ra1-1">활성</label>
                  <input type="radio" value="1" name="useFlag" id="ra1-2" <c:if test="${playerInfo.use_flag eq '1'}">checked</c:if> /><label for="ra1-2">비활성</label>
                </c:when>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th class="tl">이름<em>*</em></th>
            <td class="tl">
              <c:choose>
                <c:when test="${method eq 'save'}">
                  <input type="text" name="name" placeholder="" value="" />
                </c:when>
                <c:when test="${method eq 'modify'}">
                  <input type="text" name="name" placeholder="" value="${playerInfo.name}" />
                </c:when>
              </c:choose>

            </td>
          </tr>
          <tr>
            <th class="tl">포지션</th>
            <td class="tl">
              <c:choose>
                <c:when test="${method eq 'save'}">
                  <%--<input type="text" name="position" placeholder="" value="" />--%>
                  <select id="position" name="position">
                    <option value="">선택하기</option>
                    <option value="FW">FW</option>
                    <option value="MF">MF</option>
                    <option value="DF">DF</option>
                    <option value="GK">GK</option>
                  </select>
                </c:when>
                <c:when test="${method eq 'modify'}">
                  <select id="position" name="position">
                    <option value="">선택하기</option>
                    <option value="FW" <c:if test="${playerInfo.position eq 'FW'}">selected</c:if>>FW</option>
                    <option value="MF" <c:if test="${playerInfo.position eq 'MF'}">selected</c:if>>MF</option>
                    <option value="DF" <c:if test="${playerInfo.position eq 'DF'}">selected</c:if>>DF</option>
                    <option value="GK" <c:if test="${playerInfo.position eq 'GK'}">selected</c:if>>GK</option>
                  </select>
                </c:when>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th class="tl">생년월일</th>
            <td class="tl">
              <c:choose>
                <c:when test="${method eq 'save'}">
                  <input type="text" name="birthday" class="datepicker"  placeholder="" value="" />
                </c:when>
                <c:when test="${method eq 'modify'}">
                  <input type="text" name="birthday" class="datepicker" placeholder="" value="${playerInfo.birthday}" />
                </c:when>
              </c:choose>
            </td>
          </tr>
          </tbody>

          <c:choose>
            <c:when test="${method eq 'save'}">
              <tbody id="teamType0">
                <tr>
                  <th class="tl">
                    <button class="btn-add" onclick="fnAppendTap(0)"></button>
                    초등학교
                    <div class="box-group">
                      <input type="radio" value="0" name="ex0_0" id="ex0-1" checked /><label for="ex0-1">노출</label>
                      <input type="radio" value="1" name="ex0_0" id="ex0-2" /><label for="ex0-2">비노출</label>
                    </div>
                  </th>
                  <td class="tl" id="td0_0">
                    <div class="type4 mb_10">
                      <select id="sYear0_0">
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                          <c:choose>
                            <c:when test="${i eq sYear}">
                              <option value="${i}" selected>${i}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${i}">${i}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>

                      <button class="btn-large default btn-pop" data-id="update" data-divId="td0_0" onclick="fnPopup(this)">
                          <%--<button class="btn-large default btn-pop" data-id="update">--%>
                        학원/클럽 찾기
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
              <tbody id="teamType1">
                <tr>
                  <th class="tl">
                    <button class="btn-add" onclick="fnAppendTap(1)"></button>중학교

                    <div class="box-group">
                      <input type="radio" value="0" name="ex1_0" id="ex1-1" checked /><label for="ex1-1" >노출</label>
                      <input type="radio" value="1" name="ex1_0" id="ex1-2" /><label for="ex1-2">비노출</label>
                    </div>
                  </th>
                  <td class="tl" id="td1_0">
                    <div class="type4 mb_10">
                      <select id="sYear1_0">
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                          <c:choose>
                            <c:when test="${i eq sYear}">
                              <option value="${i}" selected>${i}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${i}">${i}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>

                      <button class="btn-large default btn-pop" data-id="update" data-divId="td1_0" onclick="fnPopup(this)">
                        학원/클럽 찾기
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
              <tbody id="teamType2">
                <tr>
                  <th class="tl">
                    <button class="btn-add" onclick="fnAppendTap(2)"></button>고등학교

                    <div class="box-group">
                      <input type="radio" value="0" name="ex2_0" id="ex2-1" checked /><label for="ex2-1" >노출</label>
                      <input type="radio" value="1" name="ex2_0" id="ex2-2" /><label for="ex2-2">비노출</label>
                    </div>
                  </th>
                  <td class="tl" id="td2_0">
                    <div class="type4 mb_10">
                      <select id="sYear2_0">
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                          <c:choose>
                            <c:when test="${i eq sYear}">
                              <option value="${i}" selected>${i}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${i}">${i}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                      <button class="btn-large default btn-pop" data-id="update" data-divId="td2_0" onclick="fnPopup(this)">
                        학원/클럽 찾기
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
              <tbody id="teamType3">
                <tr>
                  <th class="tl">
                    <button class="btn-add" onclick="fnAppendTap(3)"></button>대학교

                    <div class="box-group">
                      <input type="radio" value="0" name="ex3_0" id="ex3-1" checked /><label for="ex3-1">노출</label>
                      <input type="radio" value="1" name="ex3_0" id="ex3-2" /><label for="ex3-2">비노출</label>
                    </div>
                  </th>
                  <td class="tl" id="td3_0">
                    <div class="type4 mb_10">
                      <select id="sYear3_0">
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                          <c:choose>
                            <c:when test="${i eq sYear}">
                              <option value="${i}" selected>${i}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${i}">${i}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                      <button class="btn-large default btn-pop" data-id="update" data-divId="td3_0" onclick="fnPopup(this)">
                        학원/클럽 찾기
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
              <tbody id="teamType4">
                <tr>
                  <th class="tl">
                    <button class="btn-add" onclick="fnAppendTap(4)"></button>프로

                    <div class="box-group">
                      <input type="radio" value="0" name="ex4_0" id="ex4-1" checked /><label for="ex4-1">노출</label>
                      <input type="radio" value="1" name="ex4_0" id="ex4-2" /><label for="ex4-2">비노출</label>
                    </div>
                  </th>
                  <td class="tl" id="td4_0">
                    <div class="type4 mb_10">
                      <select id="sYear4_0">
                        <c:forEach var="i" begin="2014" end="${sysYear }" step="1">
                          <c:choose>
                            <c:when test="${i eq sYear}">
                              <option value="${i}" selected>${i}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${i}">${i}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                      <button class="btn-large default btn-pop" data-id="update" data-divId="td4_0" onclick="fnPopup(this)">
                        학원/클럽 찾기
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </c:when>

            <c:when test="${method eq 'modify'}">
              <tbody id="teamType0">
              </tbody>
              <tbody id="teamType1">
              </tbody>
              <tbody id="teamType2">
              </tbody>
              <tbody id="teamType3">
              </tbody>
              <tbody id="teamType4">
              </tbody>
            </c:when>
          </c:choose>

        </table>
      </div>
      <br />
      <div class="w100 tr mt_10">
        <a class="btn-large gray-o" onclick="location.href=''">취소 하기</a>
        <c:choose>
          <c:when test="${method eq 'save'}">
            <a class="btn-large default" onclick="fnSavePlayer()">등록 하기</a>
          </c:when>
          <c:when test="${method eq 'modify'}">
            <a class="btn-large default" onclick="fnSavePlayer()">수정 하기</a>
          </c:when>
        </c:choose>
      </div>
    </div>
  </div>
</div>



<div class="pop" id="update">
  <div style="height: auto">
    <div style="height: auto; width: 400px">
      <div class="head">학원/클럽 찾기</div>
      <input type="hidden" name="sFlag" value="0" />
      <input type="hidden" name="ageGroup" value="U18" />
      <input type="hidden" id="pId" name="pId" />
      <div class="body" style="padding: 15px 20px">
        <div class="type3">
          <select id="ageGroup" style="width:100px;">
            <c:forEach var="result" items="${uageList}" varStatus="status">
              <option value="${result.uage }">${result.uage}</option>
            </c:forEach>
          </select>
          <input type="text" name="sKeyword" placeholder="" value="" />
          <a class="btn-large default" onclick="fnSearchTeam()">검색</a>
        </div>
        <hr class="mt_10" />
        <div id="teamList">

        </div>
      </div>
      <div class="foot">
        <%--<a class="login btn-large default w100" style="margin-bottom: 5px" onclick="gotoAdd();" >
          <span>선택하기</span>
        </a>--%>
        <a class="login btn-large btn-close-pop gray w100">
          <span>취소</span>
        </a>
      </div>
    </div>
  </div>
</div>

</body>
</html>
