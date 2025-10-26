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

var _arCupTeamList = [<c:forEach var="item" items="${cupTeamList}" varStatus="status">
{
	"index" : "${status.index}",
	"key": "${item.cup_team_id}",
	"nickName": "${item.nick_name}",
	"teamId": "${item.team_id}",
	"cupId": "${item.cup_id}"
}<c:if test="${status.last eq false}">,</c:if></c:forEach>
];

var _cupTeamCount = "${fn:length(cupTeamList)}";
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
	
	//경기일정결과 그룹 조회
	var round = "${rounds}";
	var tour_team = "${cupInfoMap.tour_team}";

	$(".tabnum > a[data-id='num3-"+round+"']").removeClass("gray-o");
	$(".tabnum > a[data-id='num3-"+round+"']").addClass("default");
	
});

//리그 연령대 이동
function gotoAgeGroup(ageGroup){
	  $('input[name=ageGroup]').val(ageGroup);
	  document.cfrm.submit();
}

//리그 리스트 이동
function gotoCup() {
	document.cfrm.submit();
}

//대회 기본정보 수정
function gotoCupInfo(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cifrm.submit();
}

//대회 기본개요수상 수정
function gotoCupPrize(idx) {
	$('input[name=sFlag]').val('1');
	$('input[name=cupId]').val(idx);
    document.cpfrm.submit();
}

function gotoCupTeam(idx) {
    $('input[name=sFlag]').val('1');
    $('input[name=cupId]').val(idx);
    document.ctfrm.submit();
}


//리그 기본정보 등록
function gotoLeagueInfo(sFlag){
	if(formCheck('frm')){
		$('input[name=sFlag]').val(sFlag);
		 document.frm.submit();
	}
}





function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	if($('input:radio[name=pFlag]').is(':checked') == false) {
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
				case 'leagueName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'sdate' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'edate' :
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
   				case 'selArea' :
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
	
    var sdate = $("[name=sdate]").val();
	var edate = $("[name=edate]").val();
	
    if(!isEmpty(sdate) && !isEmpty(edate)) {
		if(compareDate1(sdate, edate, 'yyyy-MM-dd') > 0){
			$('input[name=edate]').focus();
			alert("알림!\n 종료일이 시작일 보다 작습니다. \n 확인 후 다시 등록 하세요.");
			valid = false;
			return false;
		}
		
	}
    
    return valid;
}


//참가팀 일괄 삭제 
function gotoDelCupTeam(){
	if( _cupTeamCount == 0 ){
		alert('알림! \n등록된 내용이 없습니다.');
		return;
	}
	
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		document.delFrm.submit();
	}
}


//엑셀 등록
function gotoAddExcel(){
	var file = $("#excelFile").val();

  if(isEmpty(file)) {
	    alert("파일을 선택해주세요.");
	    //showAlert("알림!","파일을 선택해주세요.");
	    return false;
	    
  }else if (!checkFileType(file)) {
	    alert("엑셀 파일만 업로드 가능합니다.");
  	return false;
  }

  if (confirm("업로드 하시겠습니까?")) {
  	document.excelUploadFrm.submit();
  }
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



//학원/클럽 찾기 
function gotoTeamSearch(){
	if(checkTeamSearchForm()){
		var index = $('input[name=index]').val();
		var nickName = $('input[name=stNickName]').val();
		var sFlag = "${sFlag}";
		var ageGroup = "${ageGroup}";
		var jsonData = {
				"sFlag":sFlag,
				"index":index,
				"ageGroup":ageGroup,
				"nickName":nickName
		};
		doAjax("doSearchTeam", jsonData, searchTeamCallback, {"index":0, "jsonData":jsonData});
	}
}

function checkTeamSearchForm() {
	var valid = true;
	
	var $obj = $("input[name=stNickName]");
	if(isEmpty($obj.val())) {
		alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
		$obj.focus();
	
		valid = false;
		return false;
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
	
  return valid;
}



//리그 경기일정 정보 이동 
function gotoCupTourMatch(round) {
	$('input[name=round]').val(round);
  document.ctmfrm.submit();
}

//리그 팀 경기일정  
function gotoTeamMatch(trCnt){
	if(trCnt != null || trCnt != ''){
		$('input[name=index]').val(trCnt);
	}
	
	rowAddContentsHtml(trCnt);
}

function rowAddContentsHtml(index){
	var trCnt = $("#frmTB > tbody tr").length;
	var item0Td = $('.item0 td').length;
	console.log('-- [rowAddContentsHtml] trCnt : ' + trCnt  );
	console.log('--- item0 td cnt : '+ $('.item0 td').length);
	if(item0Td == 1){
		$("#tblmId").html('');
		trCnt = 0; 
	} 

	$('input[name=trCnt]').val(trCnt + 1);
	/* if(index == null || index == ''){
		$('input[name=trCnt]').val(trCnt + 1);
	}else{
		$('input[name=trCnt]').val(trCnt);
	} */
	
	
	var szHtml = "";
	
	szHtml += "<tr class='item"+trCnt+"'>";
	szHtml += "<td>";
	szHtml += "<input type='text' name='date"+trCnt+"'  autocomplete='off' class='datepicker' placeholder='경기일 - 예) 20190301' maxlength='8'>";
	szHtml += "<input name='pdate"+trCnt+"' type='hidden' value=''>";
	szHtml += "</td>";
	szHtml += "<td><input type='text' name='time"+trCnt+"' placeholder='경기시간 - 예)14:00'> </td>";
	szHtml += "<td><input type='text' name='place"+trCnt+"' class='w100' placeholder='경기장소'> </td>";
	
	szHtml += "<td>";
	szHtml += "<select name='selHome"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>홈팀 선택</option>";
				for (var i = 0; i < _arCupTeamList.length; i++) {
					szHtml += "<option value="+_arCupTeamList[i].teamId+">"+_arCupTeamList[i].nickName+"</option>";
				}
	szHtml += "</select>";
	szHtml += "<input name='home"+trCnt+"' type='hidden' value=''>";
	szHtml += "</td>";
	szHtml += "<td>";
	szHtml += "<select name='selAway"+trCnt+"' class='w100'>";
	szHtml += "<option value='-1' selected>어웨이팀 선택</option>";
				for (var i = 0; i < _arCupTeamList.length; i++) {
					szHtml += "<option value="+_arCupTeamList[i].teamId+">"+_arCupTeamList[i].nickName+"</option>";
				}
	szHtml += "</select>";
	szHtml += "<input name='away"+trCnt+"' type='hidden' value=''>"
	szHtml += " </td>";
	
	szHtml += "<td class='admin'>";
		//추가 시 해당 리스트 바로 밑으로 생성
		//szHtml += "<a class='btn-pop' data-id='find-club' onclick='gotoTeam("+trCnt+");'><i class='xi-plus-circle-o'></a></i>";
		szHtml += "<a onclick='removeTeamMatch("+trCnt+");'><i class='xi-close-circle-o'></i></a>";
		
		
	szHtml += "</td>";
	szHtml += "</tr> ";
	
	$('#tblmId').append(szHtml);
	
	$(document).find("input[name^=date]").removeClass('hasDatepicker').datepicker({
		changeMonth: true, 
	       changeYear: true,
	       nextText: '다음 달',
	       prevText: '이전 달', 
	       dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
	       dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
	       monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	       monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	       dateFormat: "yymmdd",
	       //maxDate: 0,                       // 선택할수있는 최소날짜, ( 0 : 오늘 이후 날짜 선택 불가)
	       onClose: function( selectedDate ) {    
	            //시작일(startDate) datepicker가 닫힐때
	            //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
	           $("#endDate").datepicker( "option", "minDate", selectedDate );
	       }  
	});     
}

//리그 경기일정 rows 삭제 
function removeTeamMatch(index){
	$(".item"+index).remove();//tr 테그 삭제
	console.log('--  [removeTeamMatch] frm tr lng : ' + $("#frmTB > tr").length +', index : '+ index );
	
	if($("#frmTB > tbody tr").length == 0){
		var szHtml = "<tr class='item0'><td id='idEmptyList' colspan='6'>등록된 내용이 없습니다.</td></tr>";
		$('#tblmId').append(szHtml);
	}else{
		resetRowDataOrder();
	}
}


//리그 경기일정 정보 리스트 순서 재정의 
function resetRowDataOrder(){
	var trCnt = $('#frmTB > tbody tr').length;
	console.log('-- [resetRowDataOrder] trCnt : ' + trCnt);
	$("input[name=trCnt]").val(trCnt);
	for (var i = 0; i < trCnt; i++) {
		 $("#frmTB > tbody tr:eq("+i+")").attr('class', 'item'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=date]").attr('name', 'date'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=pdate]").attr('name', 'pdate'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=time]").attr('name', 'time'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=place]").attr('name', 'place'+i);
		 $("#frmTB > tbody tr:eq("+i+") td select[name^=selHome]").attr('name', 'selHome'+i);
		 $("#frmTB > tbody tr:eq("+i+") td select[name^=selAway]").attr('name', 'selAway'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=home]").attr('name', 'home'+i);
		 $("#frmTB > tbody tr:eq("+i+") td input[name^=away]").attr('name', 'away'+i);
		// $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(0)").attr('onclick', 'gotoTeam(\''+i+'\')');
		 $("#frmTB > tbody tr:eq("+i+") td:eq(5) > a:eq(0)").attr('onclick', 'removeTeamMatch(\''+i+'\')');
		 
	}
}

/* function gotoDel(idx, emblem){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=teamId]').val(idx);
		$('input[name=emblem]').val(emblem);
		document.delFrm.submit();
	}
} */
function formALLCheck(regxForm) {
    var valid = true;
    var $form = $("#"+regxForm);

    $form.find("input[name^=date]").each(
        function(key) {
            var $obj = $(this);
            if(isEmpty($obj.val())) {
                console.log('----------[formCheck]   name : '+ $obj.attr('name'));
                alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
                $obj.focus();
                valid = false;
                return false;
            }
            /* else{
                console.log('----------[input date] name : '+ $obj.attr('name')+', val : '+$obj.val()+', index:'+key);
                var pdate = getDateFormat($obj.val(), 'yyyy-MM-dd');
                 var yoil = getDateLabel($obj.val());
                var ptime = $('input[name=time'+key+']').val();
                 console.log('--- date : '+ pdate+', yoil : '+ yoil +', time : '+ ptime);
                 $('input[name=pdate'+key+']').val(pdate+" ("+yoil+") "+ptime);

            } */
        }
    );

    if(valid == false){
        return false;
    }

    $form.find("input[name^=homeScore]").each(
        function(key) {
            var $obj = $(this);
            if(isEmpty($obj.val())) {
                console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
                alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
                $obj.focus();
                valid = false;
                return false;


            }else{
                if(isRegExp($obj.val())){
                    alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
                    $obj.focus();
                    valid = false;
                    return false;
                }

                if(isNumber($obj.val()) == false){
                    alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 숫자로 입력 하세요.');
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

    $form.find("input[name^=awayScore]").each(
        function(key) {
            var $obj = $(this);
            if(isEmpty($obj.val())) {
                console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
                alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
                $obj.focus();
                valid = false;
                return false;


            }else{
                if(isRegExp($obj.val())){
                    alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
                    $obj.focus();
                    valid = false;
                    return false;
                }

                if(isNumber($obj.val()) == false){
                    alert('알림!\n'+ '['+$obj.prop('placeholder')+'] 숫자로 입력 하세요.');
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

    /*$form.find('select').each(
        function(key) {
            var $obj = $(this);
            if($obj.val() < 0) {
                console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
                alert("알림!\n [홈/어웨이]팀을 선택 하세요.");


                valid = false;
                return false;
            }
        }
    );*/


    return valid;
}

function setAllContentsData(regxForm){
    var $form = $("#"+regxForm);

    const tblmId = $("#tblmId");

    $("input[name=trCnt]").val(tblmId[0].children.length);

    $form.find("input[name^=date]").each(
        function(key) {
            var $obj = $(this);
            console.log('----------[input date] name : '+ $obj.attr('name')+', val : '+$obj.val()+', index:'+key);
            var pdate = getDateFormat($obj.val(), 'yyyy-MM-dd');
            var yoil = getDateLabel($obj.val());
            var ptime = $('input[name=time'+key+']').val();
            console.log('--- date : '+ pdate+', yoil : '+ yoil +', time : '+ ptime);
            $('input[name=pdate'+key+']').val(pdate+" ("+yoil+") "+ptime);
        }
    );

    $form.find("select[name^=place]").each(
        function(key) {
            var $obj = $(this);
            //$("#selTeam1 option:selected").text(); //$('select[name=selTeam1]').val();
            console.log('----------[select place] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
            $('input[name=place'+key+']').val($obj.val());
        }
    );

    $form.find("select[name^=cupSubMatchId]").each(
        function(key) {
            var $obj = $(this);
            //$("#selTeam1 option:selected").text(); //$('select[name=selTeam1]').val();
            console.log('----------[select cupSubMatchId] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
            $('input[name=cupTourMatchId'+key+']').val($obj.val());
        }
    );

    $form.find("select[name^=selHome]").each(
        function(key) {
            var $obj = $(this);
            //$("#selTeam1 option:selected").text(); //$('select[name=selTeam1]').val();
            var text = $('select[name='+$obj.attr('name')+'] option:selected').text().trim();
            console.log('[select selHome] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
            $('input[name=home'+key+']').val(text);
        }
    );

    $form.find("select[name^=selAway]").each(
        function(key) {
            var $obj = $(this);
            var text = $('select[name='+$obj.attr('name')+'] option:selected').text().trim();
            console.log('[select selAway] name : '+ $obj.attr('name')+', val : '+$obj.val()+', text :'+text+', index:'+key);
            $('input[name=away'+key+']').val(text);
        }
    );


}

function gotoAddLeagueTeam(sFlag) {

    if($('#frmTB > tbody tr').length == 1 && $('.item0 td').length ==  1){
        alert('알림! \n등록된 내용이 없습니다.');
        return;
    }

    $("input[name=sFlag]").val(sFlag);

    if(formALLCheck('frm')){
        setAllContentsData('frm');
        document.frm.submit();
    }

    const tBody = $("#tblmId");

    const data = [];

    const pDate = $(".pDate");
    const time = $(".time");
    const place = $(".place");
    const home = $(".selHome");
    const away = $(".selAway");

    let round = "${round}" != "" ? "${round}" : "20";

    <c:if test="${!empty cupInfoMap}">
    const matchType = "${cupInfoMap.cup_type}";
    </c:if>

    // for (let i = 0; i < tBody[0].children.length; i++) {
    //     const param = {
    //         match_date: pDate[i].value,
    //         time: time[i].value,
    //         place: place[i].value,
    //         home: home.eq(i).find("option:selected").text().trim(),
    //         away: away.eq(i).find("option:selected").text().trim(),
    //         home_id: home.eq(i).val(),
    //         away_id: away.eq(i).val(),
    //         matchType: matchType,
    //         round: round,
    //     }
    //     data.push(param);
    // }

    console.log(data);

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
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>
        <div class="others">
          
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="sfrm" id="sfrm" method="post"  action="league" onsubmit="return false;">
          	  <input name="sFlag" type="hidden" value="${sFlag}">
  			  <input name="ageGroup" type="hidden" value="${ageGroup}">
  			  
              <input type="text" name="sLeagueName" placeholder="대회명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoCup();"><i class="xi-long-arrow-left"></i> 대회등록 리스트</a>
          </div>
        </div>
        
        <div class="title">
          <h3>
            기본정보
            <a class="btn-open open-1-o ac" data-id="open-1"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-1-c" data-id="open-1"><i class="xi-caret-up-circle-o"></i></a>
            <a onclick="gotoCupInfo('${cupId}');" class="btn-large gray-o">수정</a>
          </h3>
        </div>
        <div class="open-area" id="open-1">
          <div class="scroll">
            <table cellspacing="0" class="update">
              <colgroup>
                <col width="10.3%">
                <col width="23%">
                <col width="10.3%">
                <col width="22%">
                <col width="10.3%">
                <col width="22%">
              </colgroup>
              <tbody>
                <tr>
                  <th>활성여부</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.play_flag eq '0'}">활성</c:if>
                  	<c:if test="${cupInfoMap.play_flag eq '1'}">비활성</c:if>
                  </td>
                  <th>대회유형</th>
                  <td class="tl" colspan="3">
                  	<c:if test="${cupInfoMap.cup_type eq '0'}">예선리그+토너먼트</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '1'}">예선리그+본선리그+토너먼트</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '2'}">풀리그</c:if>
                  	<c:if test="${cupInfoMap.cup_type eq '3'}">토너먼트</c:if>
                  </td>
                </tr>
                <tr>
                  <th>대회명</th>
                  <td class="tl">${cupInfoMap.cup_name}</td>
                  <th>대회기간</th>
                  <td class="tl">${cupInfoMap.play_sdate} ~ ${cupInfoMap.play_edate}</td>
                  <th>참가팀</th>
                  <td class="tl">${cupInfoMap.cup_team}팀</td>
                </tr>
                <tr>
                  <th>예선 조편성</th>
                  <td class="tl">${fn:split(cupInfoMap.sub_team_count, '/')[0]}조 ${fn:split(cupInfoMap.sub_team_count, '/')[1]}개팀</td>
                  <th>본선 조편성</th>
                  <td class="tl">${fn:split(cupInfoMap.main_team_count, '/')[0]}조 ${fn:split(cupInfoMap.main_team_count, '/')[1]}개팀</td>
                  <th>순위선정방식</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.rank_type eq '0'}">골득실</c:if>
                  	<c:if test="${cupInfoMap.rank_type eq '1'}">승자승</c:if>
                  </td>
                </tr>
                <tr>
                  <th>토너먼트 타입</th>
                  <td class="tl">
                  	<c:if test="${cupInfoMap.tour_type eq '0'}">대진표</c:if>
                  	<c:if test="${cupInfoMap.tour_type eq '1'}">추첨제</c:if>
                  </td>
                  <th>토너먼트 팀수</th>
                  <td class="tl" colspan="3">${cupInfoMap.tour_team}팀</td>
                </tr>
              </tbody>
            </table>
          </div>
          <br>
        </div>
        
        <div class="title">
          <h3 class="w50">대회개요
          	<a class="btn-open open-2-o ac" data-id="open-2"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-2-c" data-id="open-2"><i class="xi-caret-up-circle-o"></i></a>
            <a onclick="gotoCupPrize('${cupId}');" class="btn-large gray-o">수정</a>
          </h3>
          <h3 class="w50 open-area" id="open-2-other">대회수상</h3>
        </div>
        <div class="open-area" id="open-2">
          <div class="scroll">
          <table cellspacing="0" class="update">
            <colgroup>
              <col width="50%">
              <col width="50%">
            </colgroup>
            <tbody>
       		 <tr>
       		 	<% pageContext.setAttribute("newLineChar", "\n"); %>
                <td class="tl lh-160">
	                <c:if test="${empty cupInfoMap.cup_info}">등록된 내역이 없습니다.</c:if>
	                ${fn:replace(cupInfoMap.cup_info, newLineChar, "<br/>")}
                </td>
                <td class="tl lh-160">
	                <c:if test="${empty cupInfoMap.cup_prize}">등록된 내역이 없습니다.</c:if>
	                ${fn:replace(cupInfoMap.cup_prize, newLineChar, "<br/>")}
                </td>
              </tr>
            </tbody>
          </table>
          </div><br>
        </div>
        
        <c:if test="${cupInfoMap.cup_type ne 3 }">
        
        
        <div class="title">
          <h3 class="w100">
      			참가팀 및 조편성
            <a class="btn-open open-3-o ac" data-id="open-3"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-3-c" data-id="open-3"><i class="xi-caret-up-circle-o"></i></a>
            <a class="btn-large gray-o" onclick="gotoCupTeam('${cupInfoMap.cup_id}')">수정</a>
          </h3>
        </div>
        <div class="open-area" id="open-3">
          <div class="scroll">
            <table cellspacing="0" class="update">
              <colgroup>
                <col width="55px">
                
              	<c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
            	<col width="*">
	            </c:forEach>
		            
              </colgroup>
              <thead>
                <tr>
	            	<th></th>
	    
	                <c:forEach var="i" begin="1" step="1" end="${cupInfoMap.group_count}">
	            	<th>${i}조</th>
		            </c:forEach>
	                
                </tr>
              </thead>

				<c:set var="teamRowData" value="가|나|다|라|마|바|사|아|자|차|카|타|파|하" /> <!-- 참가팀순번 정렬 -->
				<c:set var="teamRow" value="${fn:split(teamRowData, '|')}"/>
				
				<c:set var="cnt" value="0" /> <!-- count -->
				<c:set var="tdNum" value="0" /> <!-- count -->

				<c:forEach var="team_count" begin="0" step="1" end="${cupInfoMap.team_count-1}" varStatus="status"> <!-- ROW 조별 팀수 ex)4팀-->
			  	<tr id="tr${status.index}">
			  		
			  		<c:forEach var="res1" items="${teamRow}" varStatus="index1">
			           	<c:if test="${index1.index == team_count }">
							<td>${res1} 팀</td> 
						</c:if>
					</c:forEach>

					<c:forEach var="group_count" begin="0" step="1" end="${cupInfoMap.group_count-1}"> <!-- CELL 조 갯수 -->
			  			
			  			<c:forEach var="result" items="${cupTeamList}" varStatus="status" begin="${cnt }" end="${cnt }">
			  				<c:set var="cnt" value="${cnt + 1 }" />
			  				<c:set var="tdNum" value="${tdNum + 1 }" />

			  				<c:if test="${group_count+1 eq result.groups }">
					  			<td id="td${tdNum }" class="input2">
					  			
					  			<c:if test="${result.team_id eq '-1'}">[매칭오류]</c:if>
		  						<c:if test="${result.team_id ne '-1'}">[${result.team_type}]</c:if>
					  			
					  			${result.nick_name }</td>
			  				</c:if> 

			  				<c:if test="${group_count+1 ne result.groups }">
			  				<c:set var="cnt" value="${cnt - 1 }" />
				  				<td id="td${tdNum }" class="input2"></td>
			  				</c:if>
		  			 
			  			</c:forEach>
					</c:forEach>

			  	</tr>
				</c:forEach>

              </table>
          </div><br>
        </div>
        
        
        
        <div class="title">
          <h3 class="w100">
			예선일정
	        <a class="btn-open open-4-o ac" data-id="open-4"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-4-c" data-id="open-4"><i class="xi-caret-up-circle-o"></i></a>
            <a class="btn-large gray-o" href="21-05.대회정보-등록수정-예선일정등록.html">수정</a>
          </h3>
        </div>
        <div class="open-area" id="open-4">
        <div style="margin-bottom:20px;">
          <c:forEach var="i" begin="1" end="${cupInfoMap.group_count }" step="1">
        	<a class="btn-large w6 btn-tab  <c:if test="${i eq 1}"> default </c:if> <c:if test="${i ne 1}"> gray-o </c:if> " data-id="num1-${i}">${i}조</a>
          </c:forEach>
        </div>

        <div class="scroll">
        <c:forEach var="i" begin="1" end="${cupInfoMap.group_count }" step="1">
        
        <table cellspacing="0" class="update show-tab <c:if test="${i eq 1}"> ac</c:if>" id="num1-${i}">
          <colgroup>
            <col width="100px">
            <col width="100px">
            <col width="300px">
            <col width="200px">
            <col width="200px">
          </colgroup>
          <thead>
            <tr>
              <th>경기일</th>
              <th>경기시간</th>
              <th>경기장소</th>
              <th>홈팀</th>
              <th>어웨이팀</th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach var="result" items="${cupSubMatchList}" varStatus="status">
          	<c:if test="${result.groups eq i}">
			<tr>
				<td>${result.pdate}</td>
				<td>${result.ptime}</td>
				<td>${result.place}</td>
				<td>${result.home}</td>
				<td>${result.away}</td>
			</tr>
			</c:if>
          	</c:forEach>
          </tbody>
        </table>
        
        </c:forEach>
        </div><br>
        </div>
        
        <c:if test="${cupInfoMap.cup_type eq 1}">
        
        <div class="title">
          <h3 class="w50">
            본선팀 및 조편성
            <a class="btn-open open-5-o ac" data-id="open-5"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-5-c" data-id="open-5"><i class="xi-caret-up-circle-o"></i></a>
            <a class="btn-large gray-o" href="21-06.대회정보-등록수정-본선팀등록.html">수정</a>
          </h3>
          
        </div>
        <div class="open-area" id="open-5">
          <div class="scroll">
          
          
          <c:set var="groupData" value="가|나|다|라|마|바|사|아|자|차|카|타|파|하" /> <!-- 참가팀 본선 조 정렬 -->
			<c:set var="groupColumn" value="${fn:split(groupData, '|')}"/>
			<c:set var="teamRowData" value="A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|U|X|Y|Z" /> <!-- 참가팀순번 정렬 -->
			<c:set var="teamRow" value="${fn:split(teamRowData, '|')}"/>
	        
            
            <table cellspacing="0" class="update">
              <colgroup>
                <col width="55px">
                
              	<c:forEach var="i" begin="1" step="1" end="${cupInfoMap.m_group_count}">
            	<col width="*">
	            </c:forEach>
		            
              </colgroup>
              <thead>
                <tr>
	            	<th></th>
	    
	                <c:forEach var="i" items="${groupColumn}"  begin="0" step="1" end="${cupInfoMap.m_group_count-1}">
	            	<th>${i}조</th>
		            </c:forEach>
	                
                </tr>
              </thead>
				
				<c:set var="cnt" value="0" /> <!-- count -->
				<c:set var="tdNum" value="0" /> <!-- count -->

				<c:forEach var="team_count" begin="0" step="1" end="${cupInfoMap.m_team_count-1}" varStatus="status"> <!-- ROW 조별 팀수 ex)4팀-->
			  	<tr id="tr${status.index}">
			  		
			  		<c:forEach var="res1" items="${teamRow}" varStatus="index1">
			           	<c:if test="${index1.index == team_count }">
							<td>${res1} 팀</td> 
						</c:if>
					</c:forEach>

					<c:forEach var="group_count" begin="0" step="1" end="${cupInfoMap.m_group_count-1}"> <!-- CELL 조 갯수 -->
			  			
			  			<c:forEach var="result" items="${cupMainTeamList}" varStatus="status" begin="${cnt }" end="${cnt }">
			  				<c:set var="cnt" value="${cnt + 1 }" />
			  				<c:set var="tdNum" value="${tdNum + 1 }" />

			  				<c:if test="${group_count+1 eq result.main_groups }">
					  			<td id="td${tdNum }" class="input2">
					  			
					  			<c:if test="${result.team_id eq '-1'}">[매칭오류]</c:if>
		  						<c:if test="${result.team_id ne '-1'}">[${result.team_type}]</c:if>
					  			
					  			${result.nick_name }</td>
			  				</c:if> 

			  				<c:if test="${group_count+1 ne result.main_groups }">
			  				<c:set var="cnt" value="${cnt - 1 }" />
				  				<td id="td${tdNum }" class="input2"></td>
			  				</c:if>
		  			 
			  			</c:forEach>
					</c:forEach>

			  	</tr>
				</c:forEach>

              </table>
          </div><br>
        </div>
        <div class="title">
          <h3 class="w100">
            본선 일정
            <a class="btn-open open-6-o ac" data-id="open-6"><i class="xi-caret-down-circle-o"></i></a>
            <a class="btn-close open-6-c" data-id="open-6"><i class="xi-caret-up-circle-o"></i></a>
            <a class="btn-large gray-o" href="21-06.대회정보-등록수정-본선팀등록.html">수정</a>
          </h3>
          
        </div>
        <div class="open-area" id="open-6">
          <div class="scroll">
            <div style="margin-bottom:10px;">
            	<c:forEach var="i" items="${groupColumn}" begin="0" step="1" end="${cupInfoMap.m_group_count-1}" varStatus="status">
		        	<a class="btn-large w6 btn-tab3  <c:if test="${status.count eq 1}"> default </c:if> <c:if test="${status.count ne 1}"> gray-o </c:if> " data-id="num2-${status.count}">${i}조</a>
		        </c:forEach>
            
            </div>
            
            <c:forEach var="i" items="${groupColumn}" begin="0" step="1" end="${cupInfoMap.m_group_count-1}" varStatus="status">
            <table cellspacing="0" class="update show-tab3 <c:if test="${status.count eq 1}"> ac</c:if>" id="num2-${status.count}">
              <colgroup>
                <col width="100px">
                <col width="100px">
                <col width="300px">
                <col width="200px">
                <col width="200px">
              </colgroup>
              <thead>
                <tr>
                  <th>경기일</th>
                  <th>경기시간</th>
                  <th>경기장소</th>
                  <th>홈팀</th>
                  <th>어웨이팀</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="result" items="${cupMainMatchList}" varStatus="status1">
	          	<c:if test="${result.groups eq status.count}">
				<tr>
					<td>${result.pdate}</td>
					<td>${result.ptime}</td>
					<td>${result.place}</td>
					<td>${result.home}</td>
					<td>${result.away}</td>
				</tr>
				</c:if>
	          	</c:forEach>
              </tbody>
            </table>
            
            </c:forEach>
          </div><br>
        </div>
        </c:if>  <!-- cupInfoMap.cup_type eq 1 -->
        
        
        </c:if>  <!-- cupInfoMap.cup_type ne 3 -->
        
        
        
        
        <div class="title">
          <h3 class="w100">
            토너먼트팀 및 일정 생성 일괄 등록
          </h3>
        </div>
        
        <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
		<input name="excelFlag" type="hidden" value="cupTourMatch">
		<input name="ageGroup" type="hidden" value="${ageGroup}">
		<input name="cupId" type="hidden" value="${cupId}">
        <table cellspacing="0" class="update">
          <tbody>
            <tr>
              <th>엑셀 파일 등록</th>
              <td class="tl">
                <input type="file" id="excelFile" name="excelFile">
              </td>
              <!-- <td class="tc">
                <input type="radio" name="name1" id="y" checked><label for="y">대진표제</label>
                <input type="radio" name="name1" id="n"><label for="n">추첨제</label>
              </td> -->
              <td class="tr">
				<!-- <a class="btn-large default btn-show" data-id="num">참가팀 생성</a> -->
				<a class="btn-large default" onclick="gotoAddExcel();" >토너먼트 팀 및 일정 생성 일괄 등록</a>
				<!-- <a class="btn-large default btn-pop" data-id="update-leagueTeam-add"  >참가팀 일괄 등록</a> -->
				<a class="btn-large gray-o">엑셀 폼 다운로드</a>
              </td>
            </tr>
          </tbody>
        </table>
        </form>
        <br>
        
		<!-- 대회 경기일정 리스트 -->
        <div id="num1">
        <div class="tabnum" style="margin-bottom:20px;">
        
		<c:set var="roundData" value="128|64|32|16|8|4|2" />
		<c:set var="round" value="${fn:split(roundData, '|')}" />
        
        <c:forEach var="res1" items="${round}" varStatus="index1">
	        <c:choose>
	        <c:when test="${res1 == 2 }">
				<a class="btn-large w6 btn-tab gray-o" data-id="num3-${res1}" onclick="gotoCupTourMatch('${res1}');">결승</a>
			</c:when>
	        <c:when test="${res1 > 2 and res1 <= cupInfoMap.tour_team }">
				<a class="btn-large w6 btn-tab gray-o" data-id="num3-${res1}" onclick="gotoCupTourMatch('${res1}');">${res1}강</a>
			</c:when>
	        <c:when test="${res1 > cupInfoMap.tour_team and (res1 - cupInfoMap.tour_team) < cupInfoMap.tour_team}">
				<a class="btn-large w6 btn-tab gray-o" data-id="num3-${cupInfoMap.tour_team}" onclick="gotoCupTourMatch('${cupInfoMap.tour_team}');">와일드카드</a>
			</c:when>
	        </c:choose>
		</c:forEach>
        
        </div>
        
        
        <div class="scroll">
        <form name="frm" id="frm" method="post"  action="save_cupTourMatch" onsubmit="return false;">
           <input type="hidden" name="sFlag">
           <input type="hidden" name="mvFlag">
           <input type="hidden" name="ageGroup"  value="${ageGroup}">
           <input type="hidden" name="round"  value="${rounds}">
           <input type="hidden" name="trCnt" value="${fn:length(cupTeamList)}">
           <input type="hidden" name="cupId" value="${cupId}">
           <input type="hidden" name="cupName" value="${cupInfoMap.cup_name}">
        
        
        <table id="frmTB"cellspacing="0" class="update">
          <colgroup>
            <col width="55px">
            <col width="200px">
            <col width="100px">
            <col width="200px">
            <col width="200px">
            <col width="200px">
            <col width="55px">
          </colgroup>
          <thead>
            <tr>
              <th>토너먼트 번호</th>
              <th>경기일</th>
              <th>경기시간</th>
              <th>경기장소</th>
              <th>홈팀</th>
              <th>어웨이팀</th>
              <th>관리</th>
              <!-- <th>관리 <a class="btn-pop" data-id="find-club" onclick="gotoTeamMatch();"><i class="xi-plus-circle-o"></i></a></th> -->
            </tr>
		  </thead>
		  <tbody id="tblmId">
          	<c:if test="${empty cupTourMatchList}">
				<tr class="item0">
					<td id="idEmptyList" colspan="7">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${cupTourMatchList}" varStatus="status">
			<tr class="item${status.index}">
				<td>${result.tour_no}</td>
				<td> 
					<input type="text" name="date${status.index}" value="${result.pdate}" class="datepicker pDate" maxlength="8" autocomplete="off">
					<input name="pdate${status.index}" type="hidden" value="">
                    <input type="hidden" name="cupTourMatchId${status.index}" value="${result.cup_tour_match_id}">
                    <input type="hidden" name="round${status.index}" value="${result.round}">
				</td>
				<td><input type="text" name="time${status.index}" value="${result.ptime}" class="time"></td>
				<td><input type="text" name="place${status.index}" value="${result.place}" class="w100 place"></td>
				<td>
	                <select class="w100 selHome" id="selHome${status.index}" name="selHome${status.index}">
                        <option value="-1">미정</option>
	                  <c:forEach var="res1" items="${cupTeamList}" varStatus="status1">
<%--		          	  <c:choose>--%>
<%--		          	  <c:when test="${result.home eq res1.nick_name}">--%>
<%--			          	<option value="${res1.team_id}" selected>${res1.nick_name}</option>--%>
<%--		          	  </c:when> --%>
<%--		          	  <c:otherwise>--%>
<%--			          	<option value="${res1.team_id}">${res1.nick_name}</option>--%>
<%--		          	  </c:otherwise>--%>
<%--		          	  </c:choose>--%>
                          <option value="${res1.team_id}" <c:if test="${result.home eq res1.nick_name}">selected</c:if>>
                                  ${res1.nick_name}
                          </option>
		          	  </c:forEach>
	                </select>
	                <input name="homeId${status.index}" type="hidden" value="">
                    <input name="home${status.index}" type="hidden" value="">
	              </td>
	              <td>
	                <select class="w100 selAway" id="selAway${status.index}" name="selAway${status.index}">
                        <option value="-1">미정</option>
	                  <c:forEach var="res1" items="${cupTeamList}" varStatus="status2">
<%--		          	  <c:choose>--%>
<%--		          	  <c:when test="${result.away eq res1.nick_name}">--%>
<%--			          	<option value="${res1.team_id}" selected>${res1.nick_name}</option>--%>
<%--		          	  </c:when> --%>
<%--		          	  <c:otherwise>--%>
<%--			          	<option value="${res1.team_id}">${res1.nick_name}</option>--%>
<%--		          	  </c:otherwise>--%>
<%--		          	  </c:choose>--%>
                          <option value="${res1.team_id}" <c:if test="${result.away eq res1.nick_name}">selected</c:if>>
                                  ${res1.nick_name}
                          </option>
		          	  </c:forEach>
	                </select>
	                <input name="awayId${status.index}" type="hidden" value="">
                      <input name="away${status.index}" type="hidden" value="">
	              </td>
				<td class="admin">
				  <%-- <a class="btn-pop" data-id="find-club" onclick="gotoTeamMatch(${status.index});"><i class="xi-plus-circle-o"></i> --%>
				  <a onclick="removeTeamMatch(${status.index});"><i class="xi-close-circle-o"></i></a>
				</td>
			</tr>
			</c:forEach>
          </tbody>
        </table>
        </form>
        
        </div>
        <div class="body-foot">
          <a class="btn-large gray-o" onclick="gotoCup();"><i class="xi-long-arrow-left"></i> 목록으로 이동 </a>
          <div class="others">
          	<a class="btn-large default" onclick="gotoDelCupTeam(2);">일정 일괄 삭제</a>
          	
          	<c:choose>
          	<c:when test="${fn:length(cupTeamList) eq 0 }">
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(0);">예선일정 등록</a>
	            <a class="btn-large gray-o" onclick="gotoAddLeagueMatch(0);">예선일정 등록 후 팀 생성</a>
          	</c:when>
          	<c:otherwise>
	            <a class="btn-large default" onclick="gotoAddLeagueTeam(1);">토너먼트 일정 수정</a>
          	</c:otherwise>
          	</c:choose>


          </div>
        </div>
        </div>
      </div>
    </div>
        
        
        
</body>

<form name="cfrm" id="cfrm" method="post"  action="cup">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>
  
<form name="ctmfrm" id="ctmfrm" method="post"  action="cupTourMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cp" type="hidden" value="${cp}">
  <input name="cupId" type="hidden" value="${cupId}">
  <input name="round" type="hidden" value="">
</form>  

<form name="cifrm" id="cifrm" method="post"  action="cupInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>  

<form name="cpfrm" id="cpfrm" method="post"  action="cupPrize">
  <input name="sFlag" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
  <input name="cupId" type="hidden" value="">
</form>

<form name="ctfrm" id="ctfrm" method="post"  action="cupTeam">
    <input name="sFlag" type="hidden" value="">
    <input name="ageGroup" type="hidden" value="${ageGroup}">
    <input name="cupId" type="hidden" value="">
</form>

<!-- <form name="lifrm" id="lifrm" method="post"  action="leagueInfo">
  <input name="sFlag" type="hidden" value="0">
  <input name="ageGroup" type="hidden" value="">
</form>  --> 

<form name="delFrm" id="delFrm" method="post"  action="save_cupTourMatch" onsubmit="return false;">
   <input type="hidden" name="sFlag" value="2">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
   <input name="cupId" type="hidden" value="${cupId}">
   <input type="hidden" name="months" value="${months}">
</form>  

</html>