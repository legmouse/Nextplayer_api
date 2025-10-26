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

<script type="text/javascript">

$(document).ready(function() {
	//연령대 선택
	var ageGroup = "${ageGroup}";
	console.log('--- ageGroup :'+ ageGroup );
	if(isEmpty(ageGroup)){
		$("#U18").addClass("active");
	}else{
		$("#"+ageGroup).addClass("active");
	}
	
	//검색
	/* var teamSearch = "${teamSearch}";
	if(!isEmpty(teamSearch)){
		var sArea = "${sArea}";
		var sTeamType = "${sTeamType}";
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
		$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
		$("input[name=sNickName]").val("${sNickName}");
	} */

    $('#insertGbnAll').click(function(){
        var checked = $('#insertGbnAll').is(':checked');

        if (checked) {
            $('input:checkbox').prop('checked', true);
        } else {
            $('input:checkbox').prop('checked', false);
        }
    });

});

let staticData;

//리그 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.lfrm.submit();
}

//리그 리스트 이동
function gotoLeague() {
	document.lfrm.submit();
}

//리그 기본정보 등록
function leagueInsert(){
    const params = [];
    for (let i = 0; i < staticData.length; i++) {

        const insertCheck = $("#insertGbn_" + i).is(':checked');
        if (!insertCheck) continue;

        if (!formCheck(i)) {
            return;
        }

        const leagueNameOrigin = $("#leagueNameOrigin_"+i).val();
        const ageGroup = $("#ageGroup_"+i).val();
        const leagueName = $("[name=leagueName_"+i+"]").val();
        const selArea = $("select[name=selArea_"+i+"]").val();
        const pFlag = $("[name=pFlag_"+i+"]").val();
        const sdate = $("[name=sdate_"+i+"]").val();
        const edate = $("[name=edate_"+i+"]").val();

        const param = {
            leagueNameOrigin,
            leagueName,
            selArea,
            pFlag,
            ageGroup,
            sdate,
            edate,
        }
        params.push(param);
    }

    if (params.length === 0) {
        alert('선택된 리그가 없습니다.');
        return;
    }

    $.ajax({
        url: '/save_leagueInfo_list',
        type: "POST",
        dataType: 'JSON',
        contentType: "application/json",
        data: JSON.stringify(params),
        success: function(){
            alert("성공했습니다.");
            $("#inputDiv").empty();
            $("#leagueInsertBtn").hide();
        },
        error: function (request, status, error) {
            alert('에러가 발생했습니다. 다시 시도해주세요.');
        },
    });
}

function formCheck(count) {
	var valid = true;
	var $form = $("#form_"+count);
	
	if($('input:radio[name=pFlag_'+count+']').is(':checked') == false) {
		//console.log('----------[formCheck] radio id : '+ $obj.attr('name')+', val : '+$obj.val());
		alert("알림!\n 활성여부 선택 하세요.");
	
		valid = false;
		return false;
	}
    
   	if(valid == false){
   		return false;
   	} 

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				*/
                switch ($obj.attr('name')) {
                    case 'leagueName_' + count :
                        alert('알림!\n' + '[' + $obj.prop('placeholder') + ']을 입력 하세요.');
                        $obj.focus();
                        valid = false;
                        return false;
                        break;

                    case 'sdate_' + count :
                        alert('알림!\n' + '[' + $obj.prop('placeholder') + ']을 입력 하세요.');
                        $obj.focus();
                        valid = false;
                        return false;
                        break;

                    case 'edate_' + count :
                        alert('알림!\n' + '[' + $obj.prop('placeholder') + ']을 입력 하세요.');
                        $obj.focus();
                        valid = false;
                        return false;
                        break;
                }

			
			}else{
				if(isRegExp($obj.val())){
					alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
					$obj.focus();
					valid = false;
					return false;
				}
				
				// remove blank
				var objVal = $obj.val();
				objVal = objVal.trim(objVal);
				$obj.val(objVal);
				
			}
		}
	);
    
    if(valid == false){
   		return false;
   	} 
   	
    $form.find('select').each(
   		function(key) {
   			var $obj = $(this);
   			if($obj.val() < 0) {
   				console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
                switch ($obj.attr('name')) {
                    case 'selArea_'+count :
                        alert("알림!\n 광역 선택 하세요.");
                        break;
                }
   			
   				valid = false;
   				return false;
   			}
   		}
   	);
    
    if(valid == false){
   		return false;
   	} 
	
    var sdate = $("[name=sdate_"+count+"]").val();
	var edate = $("[name=edate_"+count+"]").val();
	
    if(!isEmpty(sdate) && !isEmpty(edate)) {
		if(compareDate1(sdate, edate, 'yyyy-MM-dd') > 0){
			$('input[name=edate_'+count+']').focus();
			alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
			valid = false;
			return false;
		}
		
	}
    
    return valid;
}


//검색 
function gotoSearch(){
	//console.log('--- gotoSearch');
	if(searchFormCheck("sfrm")){
		$("input[name=cp]").val(1);
		document.sfrm.submit(); 
	}
}

function searchFormCheck(regxForm) {
	var valid = true;
	
	var area = $("select[name=sArea]").val();
	var leagueName = $("input[name=sLeagueName]").val();
	
	if(area < 0 && isEmpty(leagueName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}
    
    if(valid == false){
   		return false;
   	} 
    
    if(!isEmpty(leagueName) && isRegExp(leagueName) ){
    	alert('알림!\n'+ '['+$("input[name=sLeagueName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
    	$("input[name=sLeagueName]").focus();
		valid = false;
		return false;
    }
    
    
    if(area < 0 && !isEmpty(leagueName)){
    	$("select[name=sArea]").val('');
	}
    
    return valid;
}

const runCraw = () => {
    $("#inputDiv").empty();
    $("#inputDiv").append('<h1>크롤링중</h1>');
	const ageGroup = "${ageGroup}";
	const matchType = "LEAGUE2";
	const level = getLevelFromAgeGroup(ageGroup);

	const jsonParam = {
		matchType,
		level
	}
	$.ajax({
        url: '/craw/getGameCraw',
		type: "GET",
		dataType: 'JSON',
		data: jsonParam,
        tryCount: 1,
        retryLimit: 1,
        timeout: 1 * 60 * 60 * 1000, // 1hour
		success: function(data){
            $("#inputDiv").empty();
            staticData = data;
            for (let i = 0; i < data.length; i++) {
                $("#inputDiv").append(getInputIdv(i, data[i].title, data[i].startDate, data[i].endDate, data[i].ageGroup));
                $("#inputDiv").append('<br/>');
            }
            if (data.length > 0) {
                $("#leagueInsertBtn").show();
            } else {
                alert('신규 경기가 없습니다.')
            }

		},
		error: function (request, status, error) {
            $("#inputDiv").empty();
			alert('에러가 발생했습니다. 다시 시도해주세요.');
		},
	});
};

const getLevelFromAgeGroup = (ageGroup) => {
	let level = "";

	switch (ageGroup) {
		case "U11" :
			level = 1;
			break;
		case "U12" :
			level = 1;
			break;
		case "U14" :
			level = 2;
			break;
		case "U15" :
			level = 2;
			break;
		case "U17" :
			level = 3;
			break;
		case "U18" :
			level = 3;
			break;
		case "U20" :
			level = 5;
			break;
		case "U22" :
			level = 5;
			break;
	}

	return level;
};

const getInputIdv = (count, leagueName, sDate, eDate, ageGroup) => {
    const inputDiv =
        '<form id="form_' + count + '">' +
        '<div class="scroll">' +
        '  <input type="hidden" id="ageGroup_' + count + '" value="' + ageGroup + '">' +
        '  <input type="hidden" id="leagueNameOrigin_' + count + '" value="' + leagueName + '">' +
        '  <table cellspacing="0" class="update">' +
        '  	<colgroup>' +
        '    <col width="10%">' +
        '    <col width="20%">' +
        '    <col width="10%">' +
        '    <col width="30%">' +
        '    <col width="10%">' +
        '    <col width="*">' +
        '  </colgroup>' +
        '    <tbody>' +
        '      <tr>' +
        '          <th>등록여부</th>' +
        '          <td class="tl" colspan="6">' +
        '      	 	<input type="checkbox" id="insertGbn_' + count + '" value=""><label for="insertGbn_' + count + '"></label>' +
        '          </td>' +
        '      </tr>' +
        '      <tr>' +
        '          <th>활성여부</th>' +
        '          <td class="tl">' +
        '      	 	<input type="radio" name="pFlag_' + count + '" id="pf1_' + count + '" value="1"><label for="pf1_' + count + '">비활성</label>' +
        '            <input type="radio" name="pFlag_' + count + '" id="pf2_' + count + '" value="0"><label for="pf2_' + count + '">활성</label>' +
        '          </td>' +
        '          <th>리그명</th>' +
        '          <td class="tl" colspan="3">' +
        '              <input type="text" name="leagueName_' + count + '" value="' + leagueName + '" placeholder="리그명 입력 (예: [광역] 년도 리그명)">' +
        '          </td>' +
        '      </tr>' +
        '      <tr>' +
        '          <th>광역선택</th>' +
        '          <td class="tl">' +
        '			<div class="w80 fl">' +
        getArea(count) +
        '			</div>' +
        '          </td>' +
        '          <th>대회기간</th>' +
        '          <td class="tl">' +
        '            <div class="w40 fl"><input type="text" name="sdate_' + count + '" value="' + sDate + '" placeholder="시작일 - 예)20190301" autocomplete="off" class="datepicker"></div>' +
        '            <div class="w5 fl tc" style="height:28px; line-height:28px;">-</div>' +
        '            <div class="w40 fl"><input type="text" name="edate_' + count + '" value="' + eDate + '" placeholder="종료일 - 예)20190301" autocomplete="off" class="datepicker"></div>' +
        '          </td>' +
        '    </tr>' +
        '    </tbody>' +
        '  </table>' +
        '</div>' +
        '</form>';
    return inputDiv;
}

const getArea = (count) => {
    const name =  $('#sArea').find('option').map(function() {return $(this).text();}).get()
    const value =  $('#sArea').find('option').map(function() {return $(this).val();}).get()

    let select = '<select id="sArea_' + count + '" name="selArea_' + count + '">';
    for (let i = 0; i < name.length; i++) {
        const option = '<option value="'+value[i]+'">'+name[i]+'</option>';
        select += option;
    }
    select += '</select>';
    return select;
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
                    <h2><span></span>리그정보 > 등록/수정</h2>
                    <c:forEach var="result" items="${uageList}" varStatus="status">
                        <a id="${result.uage }"
                           onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
                    </c:forEach>
                </div>
                <div class="others">

                </div>
            </div>
            <div class="round body">
                <div class="body-head">
                    <div class="search">
                        <form name="sfrm" id="sfrm" method="post" action="league" onsubmit="return false;">
                            <input name="sFlag" type="hidden" value="${sFlag}">
                            <input name="ageGroup" type="hidden" value="${ageGroup}">

                            <select class="w10" name="sArea" id="sArea">
                                <option value="-1" selected>광역 선택</option>
                                <c:forEach var="result" items="${areaList}" varStatus="status">
                                    <option value="${result.area_name}">${result.area_name}</option>
                                </c:forEach>
                            </select>

                            <input type="text" name="sLeagueName" placeholder="리그명 입력"
                                   onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
                            <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large gray-o" onclick="runCraw();"> 크롤링</a>
                        <a class="btn-large gray-o" onclick="gotoLeague();"><i class="xi-long-arrow-left"></i> 리그등록 리스트</a>
                    </div>
                </div>
                <div>
                    <input type="checkbox" id="insertGbnAll"><label for="insertGbnAll">모두 선택</label>
                </div>
                <br/>
                <form name="frm" id="frm" action="save_leagueInfo" onsubmit="return false;">
                    <input name="sFlag" type="hidden" value="">
                    <input name="ageGroup" type="hidden" value="${ageGroup}">
                    <input name="leagueId" type="hidden" value="${leagueInfoMap.league_id }">

                    <div id="inputDiv">

                    </div>
                </form>

                <div class="body-foot">
                    <div class="search">
                    </div>
                    <div class="others">
                        <a class="btn-large default" id="leagueInsertBtn" style="display: none" onclick="leagueInsert()">리그생성</a>
                    </div>
                </div>
            </div>
        </div>
  </div>
</body>

<form name="lfrm" id="lfrm" method="post"  action="league">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

</html>