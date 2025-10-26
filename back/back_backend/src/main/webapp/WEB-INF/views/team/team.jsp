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
<script src="resources/jquery/jquery-ui2.js"></script>
<!-- <script src="resources/jquery/jquery-ui.js"></script> -->
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
		$("input[name=ageGroup]").val(ageGroup);
	}
	
	//검색
	var sArea = "${sArea}";
	var sTeamType = "${sTeamType}";
	var useFlag = "${useFlag}";
	$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
	$("select[name='useFlag'] option[value='"+useFlag+"']").prop("selected", "selected");
	$("input[name=sNickName]").val("${sNickName}");

    $(".img-del").on("click", function(){
       let teamId = $("input[name=teamId]").val();

       const confirmMsg = confirm("해당 팀의 엠블럼을 삭제 하시겠습니까?");

       if (confirmMsg) {
            $.ajax({
                type : 'POST',
                url : '/remove_emblem',
                data : {teamId : teamId},
                success: function(res) {
                    console.log('res > ', res);
                    if (res.state == 'SUCCESS' && res.data == 1) {
                        alert('엠블럼 삭제가 완료 되었습니다.');
                        document.frm.submit();
                    } else {
                        alert('엠블럼 삭제에 실패 했습니다.');
                        return false;
                    }
                }
            })
       }

    });

    $(".btn-close-pop").on("click", function() {
       $(".img-del").css("display", "");
    });

});

function gotoAgeGroup(ageGroup){
	  clearFrmData("frm");
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=cp]").val(1);
	  document.frm.submit();
}

//데이터 초기화
function gotoDataReset(){ 
	if (confirm("정말 데이터 초기 하시겠습니까?")) {
  		document.gotoResetFrm.submit();
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

//학원/클럽 등록 팝업 - 팝업 오픈
function showTeamPopup(){
	clearFrmData("addFrm");
	var ageGroup = "${ageGroup}";
	//console.log('-- showTeamPopupageGroup  : '+ ageGroup);
	 $('input[name=ageGroup]').val(ageGroup);
}

//학원/클럽 수정
function gotoModPopup(idx, areaName, teamType, nickName, teamName, addr, emblem, pId, pIdName, useFlag ){
	clearFrmData("modFrm");
	//console.log('-- gotoModPopup idx : '+ idx);
	var ageGroup = "${ageGroup}";
	$('input[name=ageGroup]').val(ageGroup);
	$('input[name=nickName]').val(nickName);
	$('input[name=teamName]').val(teamName);
	$('input:radio[name=teamType]:input[value='+teamType+']').attr('checked', true);
	$("select[name='selArea'] option[value='"+areaName+"']").prop("selected", true);
	$('input[name=addr]').val(addr);
	$('input[name=imgFilePath]').val(emblem);
	$('input[name=teamId]').val(idx);
	
	$('input[name=pId]').val(pId);
	$('input[name=pIdName]').val(pIdName);

    $('input:radio[name=useFlag]:input[value='+useFlag+']').attr('checked', true);
    if (!emblem) {
        $(".img-del").css("display", "none");
    }

}

// 폼 input type 초기화
function clearFrmData(regxForm){
	//console.log('-- clearFrmData regxForm : '+ regxForm);
	var $form = $("#"+regxForm);
	$form[0].reset();
}

// 학원/클럽 개별 등록
function gotoAdd(){
	if(formCheck("addFrm")){
	/*  var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val(); */
	
	// $('input[name=sFlag]').val(0); //등록
	// $('input[name=uage]').val(uage); 
	 
	 /* console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.addFrm.submit();
	}
}

// 학원/클럽 개별 수정
function gotoMod(){
	if(formCheck("modFrm")){
	/*  var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val(); */
	 
	// $('input[name=sFlag]').val(1); //수정
	// $('input[name=uage]').val(uage); 
	 
	/*  console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.modFrm.submit();
	}
}

function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				*/
				switch ($obj.attr('name')){
				case 'nickName' :
					alert('알림!\n'+ '['+$obj.prop('placeholder')+']을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'teamName' :
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
    
    if($('input:radio[name=teamType]').is(':checked') == false) {
		//console.log('----------[formCheck] radio id : '+ $obj.attr('name')+', val : '+$obj.val());
		alert("알림!\n 구분 선택 하세요.");
	
		valid = false;
		return false;
	}
    
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
    
	
    
    return valid;
}

//페이징 처리
function gotoPaging(cp) {
	/* var szHtml = "<input name='teamSearch' type='hidden' value='1'> ";
	$("#frm").append(szHtml); */
	$('input[name=cp]').val(cp);
	  
	  /* var area = $("select[name=sArea]").val();
	  var teamType = $("select[name=sTeamType]").val();
	  var leagueName = $("input[name=sLeagueName]").val();
	  console.log('--- selArea : '+ selArea+', selTeam : '+ selTeam); */
	 
	 document.frm.submit();
}

//광역등록 팝업 - 팝업 오픈
function showAreaPopup(){
	clearFrmData("addAreaFrm");
	var uage = "${ageGroup}";
	
	$("select[name='selUage'] option[value='"+uage+"']").prop("selected", true);
}

//광역등록 팝업 - 등록 
function gotoAddArea(){
	if(areaFormCheck("addAreaFrm")){
	 var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val();
	
	 $('input[name=uage]').val(uage); 
	 
	 /* console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.addAreaFrm.submit();
	}
}

function areaFormCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				/* console.log('----------[formCheck]  id : '+ $obj.attr('id')+', name : '+ $obj.attr('name'));
				switch ($obj.attr('name')){
				case 'teamName' :
					break;
				case 'address' :
					break;
				} */

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
		}
	);
    
    return valid;
}

//검색 
function gotoSearch(){
	//console.log('--- gotoSearch');
	if(searchFormCheck("frm")){
		$("input[name=cp]").val(1);
		document.frm.submit(); 
	}
}

function searchFormCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	var area = $("select[name=sArea]").val();
	var teamType = $("select[name=sTeamType]").val();
	var nickName = $("input[name=sNickName]").val();
	console.log('-- nickName:'+nickName);
	/*if(area < 0 && teamType < 0 && isEmpty(nickName)){
		alert('알림!\n 검색어를 입력 하세요.');
		valid = false;
		return false;
	}*/
    
    if(valid == false){
   		return false;
   	} 
    
    if(!isEmpty(nickName) && isRegExp(nickName) ){
    	alert('알림!\n'+ '['+$("input[name=sNickName]").prop('placeholder')+'] 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
    	$("input[name=sNickName]").focus();
		valid = false;
		return false;
    }
	
   /*  $form.find('select').each(
   		function(key) {
   			var $obj = $(this);
   			if($obj.val() < 0) {
   				console.log('----------[formCheck] select id : '+ $obj.attr('name')+', val : '+$obj.val());
   				switch ($obj.attr('name')){
   				case 'sArea' :
   					alert("알림!\n 광역 선택 하세요.");
   					break;
   				case 'sTeamType' :
   					alert("알림!\n 학원/클럽 선택 하세요.");
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
    
    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
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
		}
	);
    */
    
    return valid;
}

//삭제 
function gotoDel(idx, emblem){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=teamId]').val(idx);
		$('input[name=emblem]').val(emblem);
		document.delFrm.submit();
	}
}



function autoComplete(appendTo){ 
	$(appendTo + "  #pIdName").autocomplete({
		source : function( request, response ) {
            $.ajax({
                   type: 'post',
                   url: "ajax",
                   dataType: "json",
//                   data: request,
                   data: {
                	   cmd: "doSearchTeam2",
                	   ageGroup: "${ageGroup}",
                   	   nickName: request.term   
                   },
                   success: function(data) {
                       //서버에서 json 데이터 response 후 목록에 추가
                       response(
                    		   $.map(data.sTeamList, function(item) {
                    			   return {
		                        		label: item.nick_name,		   
		                        		value: item.nick_name,
		                        		pId: item.team_id
                    			   }
                    		   })
                       );
                   }
              });
           },    // source 는 자동 완성 대상
	       select : function(event, ui) {    //아이템 선택시
	
				$(appendTo+" [name='pId']").val(ui.item.pId);

	       },
	       focus : function(event, ui) {    //포커스 가면
	    	   return false;//한글 에러 잡기용도로 사용됨
	       },
	       minLength: 2,// 최소 글자수
	       autoFocus: true, //첫번째 항목 자동 포커스 기본값 false
	       classes: {    //잘 모르겠음
	    	   "ui-autocomplete": "highlight"
	       },
	       delay: 500,    //검색창에 글자 써지고 나서 autocomplete 창 뜰 때 까지 딜레이 시간(ms)
	       appendTo: appendTo,	//div 팝업으로 자동완성 영역을 한정시킴
/* 	       open: function(event, ui) {
	            $(this).autocomplete("widget").css({
	                "width": 250
	            });
	       }, */
	       close : function(event){    //자동완성창 닫아질때 호출
	    	   //console.log("close event:"+event);
	    	   return false;
	       }	
	});
}

const fnDownload = () => {

    const ageGroup = $("input[name=ageGroup]").val();
    let sArea = $("select[name=sArea]").val();
    let sTeamType = $("select[name=sTeamType]").val();
    let sNickName = $("input[name=sNickName]").val();

    let jsonData = {
        'ageGroup': ageGroup,
        'sArea': sArea,
        'sTeamType': sTeamType,
        'sNickName': sNickName,
        'excelFlag': 'teamList'
    };

    let excelUrl = "excelDownload";
    let request = new XMLHttpRequest();

    request.open('POST', excelUrl, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.responseType = 'blob';

    request.onload = function(e) {

        $("#page-loading").hide();

        let filename = "";
        let disposition = request.getResponseHeader('Content-Disposition');
        if (disposition && disposition.indexOf('attachment') !== -1) {
            let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            let matches = filenameRegex.exec(disposition);
            if (matches != null && matches[1])
                filename = decodeURI( matches[1].replace(/['"]/g, '') );
        }
        //console.log("FILENAME: " + filename);

        if (this.status === 200) {

            let blob = this.response;
            if(window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, filename);
            }else{
                let downloadLink = window.document.createElement('a');
                let contentTypeHeader = request.getResponseHeader("Content-Type");
                downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                downloadLink.download = filename;
                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);
            }

            //setTimeout(() => $('#pop-proof-1').modal('hide'), 3000);
        }

    };

    request.send(JSON.stringify(jsonData));

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
  		  	<h2><span></span>학원·클럽 > 등록/수정</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
         <!--  <a class="active">U18<span></span></a>
          <a>U15<span></span></a>
          <a>U12<span></span></a> -->
        </div>
        <!--<div class="others">
          <select>
            <option>2020</option>
            <option>2019</option>
            <option>2018</option>
            <option>2017</option>
            <option>2016</option>
            <option>2015</option>
            <option>2014</option>
          </select>
        </div>-->
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
          <form name="frm" id="frm" method="post"  action="team" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="">

	          <select class=" w150" name="sArea">
	          <option value="-1" selected>광역 선택</option>
	          <c:forEach var="result" items="${areaList}" varStatus="status">
	          	<option value="${result.area_name}">${result.area_name}</option>
	          </c:forEach>
	          </select>
	          <select class=" w150" name="sTeamType">
	            <option value="-1">학원/클럽 선택</option>
	            <option value="0">학원</option>
	            <option value="1">클럽</option>
	            <option value="2">유스</option>
	          </select>
              <select class="w150" name="useFlag">
                  <option value="-1">활성/비활성 선택</option>
                  <option value="0">활성</option>
                  <option value="1">비활성</option>
              </select>
              <input type="text" name="sNickName" placeholder="사용명칭 학원/클럽명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
            <a class="btn-large default btn-pop" data-id="update-area-add" onclick="showAreaPopup();">광역설정</a>
            <a class="btn-large default " data-id="update-area-add" onclick="fnDownload();">목록 엑셀 다운로드</a>
            <a class="btn-large default btn-pop" data-id="update-school-add">일괄등록</a>
            <a class="btn-large default btn-pop" data-id="update-school-single" onclick="showTeamPopup();">개별등록</a>
            <a class="btn-large gray-o">등록 폼 다운로드</a>
            <%-- <a class="btn-large gray-o" onclick="gotoDataReset();">${ageGroup}연령 데이터 초기화</a> --%>
          </div>
        </div>
        <div class="scroll">
          <table cellspacing="0" class="update over">
            <colgroup>
              <col width="55px">
              <col width="55px">
              <col width="55px">
              <col width="55px">
              <col width="10%">
              <col width="15%">
              <col width="*">
              <col width="10%">
              <col width="45px">
              <col width="10%">
            </colgroup>
            <thead>
              <tr>
                <th><a>번호 <i class="xi-caret-up-min"></i></a></th>
                <th>광역</th>
                <th>엠블럼</th>
                <th>구분</th>
                <th>사용명칭</th>
                <th>정식명칭</th>
                <th><a>소재지 <i class="xi-caret-up-min"></i></a></th>
                <th>팀연동</th>
                <th>활성여부</th>
                <th>관리</th>
              </tr>
            </thead>
            <tbody>
            <c:if test="${empty teamList }">
				<tr>
					<td id="idEmptyList" colspan="9">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${teamList}" varStatus="status"> 
			<tr>
              <td>
              	<c:choose>
              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
              	</c:choose>
              </td>
              <td>${result.area_name }</td>
              <td>
              	<!-- <img src="resources/img/logo/none.png" class="logo"> -->
              	<c:choose>
              	<c:when test="${empty result.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
              	<c:otherwise><img src="/NP${result.emblem}" class="logo"></c:otherwise>
              	</c:choose>
              </td>
              <td>
              	<%-- <span class="label red">${result.team_type }</span> --%>
              	<c:choose>
              	<c:when test="${result.team_type eq '0'}"><span class="">학원</span></c:when>
              	<c:when test="${result.team_type eq '1'}"><span class="">클럽</span></c:when>
              	<c:when test="${result.team_type eq '2'}"><span class="">유스</span></c:when> <%-- 유스일경우?? --%>
              	</c:choose>
              </td>
              <td>${result.nick_name }</td>
              <td>${result.team_name }</td>
              <td class="tl">${result.addr }</td>
              <td>
              	<c:choose>
              	<c:when test="${result.pId eq 0}">-</c:when>
              	<c:otherwise>
              		<c:forEach var="res" items="${teamList}" varStatus="status1">
              		<c:if test="${result.pId eq res.team_id}">${result.pId_name}</c:if>
              		</c:forEach>
              	</c:otherwise>
              	</c:choose>
              </td>
             <td>
              	<c:choose>
              	<c:when test="${result.use_flag eq '0'}"><span class="coblue fwb">활성</span></c:when>
              	<c:when test="${result.use_flag eq '1'}"><span class="cored fwb" style="min-width: 40px!important;">비활성</span></c:when>
              	</c:choose>
              </td>
              <td class="admin">
                <a class="btn-pop" data-id="modefy-school-single" onclick="gotoModPopup('${result.team_id}','${result.area_name}','${result.team_type}','${result.nick_name}','${result.team_name}','${result.addr}','${result.emblem}', '${result.pId}', '${result.pId_name}', '${result.use_flag}');"><i class="xi-catched"></i></a>
                <a onclick="gotoDel('${result.team_id}','${result.emblem}');"><i class="xi-close-circle-o"></i></a>
              </td>
            </tr>
			</c:forEach>
          
            </tbody>
          </table>
        </div>
        <div class="pagination">
          <!-- paging -->
			<c:if test="${prev}">
				<a href='javascript:gotoPaging(${start -1});'><i class="xi-angle-left-min"></i></a>
			</c:if>
			<c:forEach var="i" begin="${start }" end="${end }">
				<c:choose>
					<c:when test="${i eq cp}">
						<a href='javascript:gotoPaging(${i});' class="active">${i}</a>
					</c:when>
					<c:otherwise>
						<a href='javascript:gotoPaging(${i});' >${i}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:if test="${next }">
				<a href='javascript:gotoPaging(${end +1});'><i class="xi-angle-right-min"></i></a>
			</c:if>
          <!-- <a><i class="xi-angle-left-min"></i></a>
          <a class="active">1</a>
          <a>2</a>
          <a>3</a>
          <a>4</a>
          <a>5</a>
          <a><i class="xi-angle-right-min"></i></a> -->
        </div>
      </div>
	  </div>

    <!--팝업-->
    <div class="pop" id="update-school-single">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            학원/클럽 개별 등록
          </div>
          
          <form name="addFrm" id="addFrm" method="post" enctype="multipart/form-data" action="save_team">
           <input type="hidden" name="sFlag" value="0">
           <input type="hidden" name="ageGroup" value="${ageGroup}">
           <input type="hidden" id="pId" name="pId" >
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">사용명칭</span><input type="text" name="nickName" placeholder="사용명칭 입력 (예: 전주영생고)">
              </li>
              <li class="title">
                <span class="title">정식명칭</span><input type="text" name="teamName" placeholder="정식명칭 입력 (예: 전주영생고등학교)">
              </li>
              <li class="title">
                <span class="title">구분</span>
                <input type="radio" name="teamType" id="b1" value="0" ><label class="w20" for="b1">학원</label>
                <input type="radio" name="teamType" id="b2" value="1" ><label class="w20" for="b2">클럽</label>
                <input type="radio" name="teamType" id="b3" value="2" ><label class="w20" for="b3">유스</label>
              </li>
              <li class="title">
                <span class="title">지역선택</span>
                <select style="width:50%;" name="selArea" id="selArea">
                  <option value="-1" selected>광역 선택</option>
		          <c:forEach var="result" items="${areaList}" varStatus="status">
		          	<option value="${result.area_name}">${result.area_name}</option>
		          </c:forEach>
                  <!-- <option>광역 선택</option>
                  <option>서울/인천</option>
                  <option>중부/강원</option>
                  <option>경기</option>
                  <option>호남/제주</option>
                  <option>영남</option>
                  <option>전국</option> -->
                </select>
                <a class="btn-pop" data-id="update-area-add" onclick="showAreaPopup();"><i class="xi-cog" style="line-height: 28px;"></i></a>
              </li>
              <li class="title">
                <span class="title">소재지</span>
                <input type="text" name="addr" value="" placeholder="소재지 입력 (예: 서울특별시 마포구 성산동)">
              </li>
              <li class="title">
                <span class="title">앰블럼</span>
                <input type="file" id="emblemFile" name="emblemFile" value="">
              </li>
              <li class="title">
                <span class="title">팀연동</span>
                <input type="text" id="pIdName" name="pIdName" oninput="autoComplete('#update-school-single');">
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAdd();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    <div class="pop" id="modefy-school-single">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            학원/클럽 개별 수정
          </div>
          <form name="modFrm" id="modFrm" method="post" enctype="multipart/form-data" action="save_team">
           <input type="hidden" name="sFlag" value="1">
           <input type="hidden" name="ageGroup" value="${ageGroup}">
           <input type="hidden" name="teamId">
           <input type="hidden" name="imgFilePath">
           <input type="hidden" name="cp" value="${cp}">
           <input type="hidden" id="pId" name="pId" >
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">사용명칭</span><input type="text" name="nickName" placeholder="사용명칭 입력 (예: 전주영생고)" >
              </li>
              <li class="title">
                <span class="title">정식명칭</span><input type="text" name="teamName" placeholder="정식명칭 입력 (예: 전주영생고등학교)" >
              </li>
              <li class="title">
                <span class="title">구분</span>
                <input type="radio" name="teamType" id="a1" value="0"><label class="w20" for="a1">학원</label>
                <input type="radio" name="teamType" id="a2" value="1"><label class="w20" for="a2">클럽</label>
                <input type="radio" name="teamType" id="a3" value="2"><label class="w20" for="a3">유스</label>
              </li>
              <li class="title">
                <span class="title">지역선택</span>
                <select style="width:50%;" name="selArea" id="selArea">
                	<option value="-1" selected>광역 선택</option>
			          <c:forEach var="result" items="${areaList}" varStatus="status">
			          	<option value="${result.area_name}">${result.area_name}</option>
			          </c:forEach>
                 <!--  <option>광역 선택</option>
                  <option>서울/인천</option>
                  <option>중부/강원</option>
                  <option>경기</option>
                  <option>호남/제주</option>
                  <option>영남</option>
                  <option>전국</option> -->
                </select>
                <a class="btn-pop" data-id="update-area-add"><i class="xi-cog" style="line-height: 28px;"></i></a>
              </li>
              <li class="title">
                <span class="title">소재지</span>
                <input type="text" name="addr" placeholder="소재지 입력 (예: 서울특별시 마포구 성산동)" >
              </li>
              <li class="title">
                <span class="title">앰블럼</span>
                <input type="file" id="emblemFile" name="emblemFile" style="width:50%;">
                    <a class="img-del"><span>기존파일</span><i class="xi-close-circle-o"></i></a>
              </li>
              <li class="title">
                <span class="title">팀연동</span>
                <input type="text" id="pIdName" name="pIdName" oninput="autoComplete('#modefy-school-single');">
              </li>
              <li class="title">
                <span class="title">활성 / 비활성</span>
                  <input type="radio" name="useFlag" id="u1" value="0"><label class="w20" for="u1">활성</label>
                  <input type="radio" name="useFlag" id="u2" value="1"><label class="w20" for="u2">비활성</label>
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoMod();"><span>수정하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    
    <form name="excelUploadFrm" id="excelUploadFrm" method="post" enctype="multipart/form-data" action="excelUpload" onsubmit="return false;">
	<input name="excelFlag" type="hidden" value="team">
    <div class="pop" id="update-school-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            학원/클럽 일괄 등록
          </div>
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li>
                <input type="file" id="excelFile" name="excelFile">
              </li>
            </ul>
          </div>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddExcel();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    </form>
    
    <div class="pop" id="update-area-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            광역 등록
          </div>
          <form name="addAreaFrm" id="addAreaFrm" method="post"  action="save_area">
           <input type="hidden" name="sFlag" value="0">
           <input type="hidden" name="uage">
           <input type="hidden" name="page" value="team"> 
           
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">연령선택</span>
                <select style="width:50%;" name="selUage" id="selUage">
		          	<c:forEach var="result" items="${uageList}" varStatus="status">
		          	<option value="${result.uage}">${result.uage}</option>
		          	</c:forEach>
		        </select>
		        
                <!-- <select style="width:50%;">
                   <option>U18</option>
                  <option>U15</option>
                  <option>U12</option>
                </select> -->
              </li>
              <li class="title">
                <span class="title">광역명</span>
                <input type="text" name="areaName" placeholder="광역명 입력 (예: 호남/제주)">
              </li>
            </ul>
          </div>
          </form>
          <div class="foot">
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAddArea();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    <!--팝업 끝-->
  <div>
</body>

<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team" onsubmit="return false;">
  <input name="sFlag" type="hidden" value="10"> 
  <input name="cp" type="hidden" value="1">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="delFrm" id="delFrm" method="post"  enctype="multipart/form-data" action="save_team" >
   <input type="hidden" name="sFlag" value="2">
   <input type="hidden" name="teamId">
   <input type="hidden" name="emblem">
   <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>  

<%-- <form name="frm" id="frm" method="post"  action="team">
  <input name="cp" type="hidden" value="${cp}">
  <input name="ageGroup" type="hidden" value="">
</form>  --%>

<%-- <form name="gotoResetFrm" id="gotoResetFrm" method="post"  action="save_team" onsubmit="return false;">
  <input name="sFlag" type="hidden" value="10"> <!-- 데이터 초기화 : HomeController-->
  <input name="cp" type="hidden" value="1">
  <input name="tbName" type="hidden" value="Team">
  <input name="page" type="hidden" value="team">
  <input type="hidden" name="arKeys">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> --%>
 
</html>