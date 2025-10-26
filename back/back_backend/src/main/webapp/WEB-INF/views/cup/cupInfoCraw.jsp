<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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

//대회 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.cfrm.submit();
}

//대회 리스트 이동
function gotoCup() {
	document.cfrm.submit();
}

//리그 기본정보 등록 후 팀정보 이동
function gotoCupPrize(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		$('input[name=mvFlag]').val('cupPrize');
		document.frm.submit();
	}
}


//리그 기본정보 등록
function cupInsert(){
    const params = [];
    for (let i = 0; i < staticData.length; i++) {

        const insertCheck = $("#insertGbn_" + i).is(':checked');
        if (!insertCheck) continue;

        if (!formCheck(i)) {
            return;
        }

        const cupNameOrigin = $("#cupNameOrigin_"+i).val();
        const ageGroup = $("#ageGroup_"+i).val();
        const cupName = $("[name=cupName_"+i+"]").val();
        const selArea = $("select[name=selArea_"+i+"]").val();
        const pFlag = $("[name=pFlag_"+i+"]").val();
        const sdate = $("[name=sdate_"+i+"]").val();
        const edate = $("[name=edate_"+i+"]").val();
        const cupTeam = $("[name=cupTeam_"+i+"]").val();
        const cupTeam2 = $("[name=cupTeam2_"+i+"]").val();
        const cType = $("[name=cType_"+i+"]").val();
        const rType = $("[name=rType_"+i+"]").val();
        const sTeamCnt = $("[name=sTeamCnt_"+i+"]").val();
        const mTeamCnt = $("[name=mTeamCnt_"+i+"]").val();
        const tType = $("[name=tType_"+i+"]").val();
        const tourTeam = $("[name=tourTeam_"+i+"]").val();

        const param = {
            cupNameOrigin,
            cupName,
            selArea,
            pFlag,
            ageGroup,
            sdate,
            edate,
            cupTeam,
            cupTeam2,
            cType,
            rType,
            sTeamCnt,
            mTeamCnt,
            tType,
            tourTeam
        }
        params.push(param);
    }

    if (params.length === 0) {
        alert('선택된 대회가 없습니다.');
        return;
    }

    $.ajax({
        url: '/save_cupInfo_list',
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
				switch ($obj.attr('name')){
				case 'cupName_' + count :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;

				case 'sdate_' + count :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'edate_' + count :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
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
   				switch ($obj.attr('name')){
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
	const matchType = "MATCH";
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
				$("#cupInsertBtn").show();
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
			level = 51;
			break;
		case "U12" :
			level = 51;
			break;
		case "U14" :
			level = 52;
			break;
		case "U15" :
			level = 52;
			break;
		case "U17" :
			level = 53;
			break;
		case "U18" :
			level = 53;
			break;
		case "U20" :
			level = 54;
			break;
		case "U22" :
			level = 54;
			break;
	}

	return level;
};

const getInputIdv = (count, cupName, sDate, eDate, ageGroup) => {
	let inputDiv ='';
    inputDiv += '<form id="form_' + count + '">';
    inputDiv +='<div class="scroll">';
    inputDiv += '<input type="hidden" id="ageGroup_' + count + '" value="' + ageGroup + '">';
    inputDiv += '<input type="hidden" id="cupNameOrigin_' + count + '" value="' + cupName + '">';
    inputDiv +='  <table cellspacing="0" class="update">';
    inputDiv +='    <tbody>';
    inputDiv +='      <tr>';
    inputDiv +='          <th>등록여부</th>';
    inputDiv +='          <td class="tl" colspan="6">';
    inputDiv +='          	 	<input type="checkbox" id="insertGbn_' + count + '" value=""><label for="insertGbn_' + count + '"></label>';
    inputDiv +='          </td>';
    inputDiv +='      </tr>';
    inputDiv +='      <tr>';
    inputDiv +='          <th>활성여부</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='          	 	<input type="radio" name="pFlag_'+count+'" id="pf1_'+count+'" value="1"><label for="pf1_'+count+'">비활성</label>';
    inputDiv +='                <input type="radio" name="pFlag_'+count+'" id="pf2_'+count+'" value="0"><label for="pf2_'+count+'">활성</label>';
    inputDiv +='          </td>';
    inputDiv +='          ';
    inputDiv +='          <th>대회유형</th>';
    inputDiv +='          <td class="tl" colspan="3">';
    inputDiv +='          		<input type="radio" name="cType_'+count+'" id="ct1_'+count+'" value="0" onClick="clickCtype(this, '+count+')"><label for="ct1_'+count+'">예선리그+토너먼트</label>';
    inputDiv +='                <input type="radio" name="cType_'+count+'" id="ct2_'+count+'" value="1" onClick="clickCtype(this, '+count+')"><label for="ct2_'+count+'">예선리그+본선리그+토너먼트</label>';
    inputDiv +='                <input type="radio" name="cType_'+count+'" id="ct3_'+count+'" value="2" onClick="clickCtype(this, '+count+')"><label for="ct3_'+count+'">풀리그</label>';
    inputDiv +='                <input type="radio" name="cType_'+count+'" id="ct4_'+count+'" value="3" onClick="clickCtype(this, '+count+')"><label for="ct4_'+count+'">토너먼트</label>';
    if (ageGroup === 'U12' || ageGroup === 'U11') {
        inputDiv +='				<input type="radio" name="cType_'+count+'" id="ct5_'+count+'" value="4" onClick="clickCtype(this, '+count+')"><label for="ct5_'+count+'">풀리그 + 풀리그</label>';
    }
    inputDiv +='          </td>';
    inputDiv +='      </tr>';
    inputDiv +='      <tr id="appendTr_'+count+'">';
    inputDiv +='          <th>대회명</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='              <input type="text" name="cupName_'+count+'" value="'+cupName+'"  placeholder="대회명 입력 (예: [지역] 회차(년도) 대회명)">';
    inputDiv +='          </td>';
    inputDiv +='          <th>대회기간</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='            <div class="w40 fl"><input type="text" name="sdate_'+count+'" value="'+sDate+'" placeholder="시작일 - 예)20190301" autocomplete="off" class="datepicker"></div>';
    inputDiv +='            <div class="w5 fl tc" style="height:28px; line-height:28px;">-</div>';
    inputDiv +='            <div class="w40 fl"><input type="text" name="edate_'+count+'" value="'+eDate+'" placeholder="종료일 - 예)20190301" autocomplete="off" class="datepicker"></div>';
    inputDiv +='          </td>';
    inputDiv +='          <th>참가팀</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='            <div class="w80 fl"><input type="text" name="cupTeam_'+count+'" placeholder="팀수" value=""></div>';
    inputDiv +='            <div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>';
    inputDiv +='          </td>';
    inputDiv +='      </tr>';
    inputDiv +='      <tr>';
    inputDiv +='          <th id="teamCntTh1_'+count+'">예선 조편성 팀수</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='              <div class="w40 fl"><input type="text" name="sTeamCnt_'+count+'" value=""  placeholder="예선 조/팀(예 10/4)"></div>';
    inputDiv +='          </td>';
    inputDiv +='          <th id="teamCntTh2_'+count+'">본선 조편성 팀수</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='              <div class="w40 fl"><input type="text" name="mTeamCnt_'+count+'" value=""  placeholder="본선 조/팀(예 10/4)"></div>';
    inputDiv +='          </td>';
    inputDiv +='          <th>순위선정방식</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='                <input type="radio" name="rType_'+count+'" id="rt2_'+count+'" value="0"><label for="rt2_'+count+'">골득실</label>';
    inputDiv +='          	 	<input type="radio" name="rType_'+count+'" id="rt1_'+count+'" value="1"><label for="rt1_'+count+'">승자승</label>';
    inputDiv +='          </td>';
    inputDiv +='      </tr>';
    inputDiv +='      <tr>';
    inputDiv +='          <th>토너먼트 타입</th>';
    inputDiv +='          <td class="tl">';
    inputDiv +='                <input type="radio" name="tType_'+count+'" id="tt2_'+count+'" value="0"><label for="tt2_'+count+'">대진표</label>';
    inputDiv +='          	 	<input type="radio" name="tType_'+count+'" id="tt1_'+count+'" value="1"><label for="tt1_'+count+'">추첨제</label>';
    inputDiv +='          </td>';
    inputDiv +='          <th>토너먼트 팀수</th>';
    inputDiv +='          <td class="tl" colspan="3"> ';
    inputDiv +='            <div class="w30 fl"><input type="text" name="tourTeam_'+count+'" placeholder="팀수" value=""></div>';
    inputDiv +='            <div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>';
    inputDiv +='          </td>';
    inputDiv +='      </tr>';
    inputDiv +='    </tbody>';
    inputDiv +='  </table>';
    inputDiv +='</div>';
    inputDiv +='</form>';

    return inputDiv;
}

function clickCtype(e, count) {

    const cTypeValue = e.value;

    if (cTypeValue == 4) {
        const str = '<th id="appendTh_'+count+'">참가팀2</th>' +
            '<td class="tl" id="appendTd_'+count+'">' +
            '<div class="w80 fl"><input type="text" name="cupTeam2_'+count+'" placeholder="팀수" value=""></div>' +
            '<div class="w10 fl tc" style="height:28px; line-height:28px;">팀</div>' +
            '</td>';
        $("#appendTr_"+count).append(str);

        const str2 = '1차 풀리그 팀수';
        const str3 = '2차 풀리그 팀수';

        $("#teamCntTh1_"+count).html(str2);
        $("#teamCntTh2_"+count).html(str3);

    } else {
        $("#appendTh_"+count).remove();
        $("#appendTd_"+count).remove();

        const str2 = '예선 조편성 팀수';
        const str3 = '본선 조편성 팀수';

        $("#teamCntTh1_"+count).html(str2);
        $("#teamCntTh2_"+count).html(str3);
    }

    console.log(e.value);
    console.log(count);
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
                    <h2><span></span>대회정보 > 등록/수정</h2>
                    <c:forEach var="result" items="${uageList}" varStatus="status">
                        <a id="${result.uage }"
                           onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
                    </c:forEach>
                </div>

            </div>
            <div class="round body">
                <div class="body-head">
                    <div class="search">
                        <form name="sfrm" id="sfrm" method="post" action="league" onsubmit="return false;">
                            <input name="sFlag" type="hidden" value="${sFlag}">
                            <input name="ageGroup" type="hidden" value="${ageGroup}">

                            <input type="text" name="sLeagueName" placeholder="대회명 입력"
                                   onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
                            <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
                        </form>
                    </div>
                    <div class="others">
                        <a class="btn-large gray-o" onclick="runCraw();"> 크롤링</a>
                        <a class="btn-large gray-o" onclick="gotoCup();"><i class="xi-long-arrow-left"></i> 대회등록 리스트</a>
                    </div>
                </div>
                <div>
                    <input type="checkbox" id="insertGbnAll"><label for="insertGbnAll">모두 선택</label>
                </div>
                <br/>
                <div id="inputDiv">
                </div>

                <div class="body-foot">
                    <div class="search">
                    </div>
                    <div class="others">
                        <a class="btn-large default" id="cupInsertBtn" style="display: none" onclick="cupInsert()">대회
                            등록</a>
                    </div>
                </div>
            </div>
        </div>
  </div>
</body>

<form name="cfrm" id="cfrm" method="post"  action="cup">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

</html>