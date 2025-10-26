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
	//메시지 출력
	var code = "${code}";
	var msg = decodeURIComponent("${msg}");
	if(!isEmpty(code) && !isEmpty(code)){
		alert('알림!\n'+ '['+msg+']은 중복된 광역명 입니다.\n 확인 후 다시 등록해 주세요.');
		gotoAgeGroup(ageGroup);
		return;
	}
	
	//연령대 선택
	var ageGroup = "${ageGroup}";
	//console.log('--- ageGroup :'+ ageGroup );
	if(isEmpty(ageGroup)){
		$("#U18").addClass("active");
	}else{
		$("#"+ageGroup).addClass("active");
		$("input[name=ageGroup]").val(ageGroup);
	}
	
	//검색
	var areaSearch = "${areaSearch}";
	if(!isEmpty(areaSearch)){
		var sUage = "${sUage}";
		$("select[name='sUage'] option[value='"+sUage+"']").prop("selected", "selected");
		$("input[name=sAreaName]").val("${sAreaName}");
	}
});

function gotoAdd(){
	if(formCheck("addFrm")){
	 var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val();
	
	// $('input[name=sFlag]').val(0); //등록
	 $('input[name=uage]').val(uage); 
	 
	 /* console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 
	 document.addFrm.submit();
	}
}

function gotoMod(){
	if(formCheck("modFrm")){
	 var uage = $("#selUage option:selected").text();
	 var areaName = $("input[name=areaName]").val();
     //var showFlag = $("input[name='showFlagInput']:checked").val();
     //console.log('showFlag : ', showFlag);
	// $('input[name=sFlag]').val(1); //수정
	 $('input[name=uage]').val(uage); 
	 //$("input[name='showFlag']").val(showFlag);
	/*  console.log('--  '
			+', uage : '+ uage
			+', areaName : '+ areaName
	); */
	 console.log('document.modFrm : ', document.modFrm);
	 document.modFrm.submit();
	}
}

function formCheck(regxForm) {
	var valid = true;
	var $form = $("#"+regxForm);
	
	if(regxForm == 'frm'){
		if($('select[name=sUage]').val() < 0) {
			alert("알림!\n 연령 선택 하세요.");
		
			valid = false;
			return false;
		}
	}
	
	/* 
	$form.find('select').each(
		function(key) {
			var $obj = $(this);
			if($obj.val() < 0) {
				console.log('----------[formCheck]  id : '+ $obj.attr('name')+', val : '+$obj.val());
				switch ($obj.attr('name')){
				case 'selTeamArea' :
					alert("알림!\n 광역선택 하세요.");
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
	*/

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
  
function gotoAgeGroup(ageGroup){
	clearFrmData("frm");
	$('input[name=ageGroup]').val(ageGroup);
	document.frm.submit();
}

function gotoAddPopup(){
	clearFrmData("addFrm");
	var uage = "${ageGroup}";
	//console.log('-- gotoAddPopup ageGroup :'+uage);
	
	$("select[name='selUage'] option[value='"+uage+"']").prop("selected", true);
}

function gotoModPopup(idx, uage, areaName, showFlag){
	clearFrmData("modFrm");
	//console.log('-- gotoModPopup idx : '+ idx);
	
	$("select[name='selUage'] option[value='"+uage+"']").prop("selected", true);
	$('input[name=areaName]').val(areaName);
	$('input[name=areaId]').val(idx);
    if (showFlag == 0) {
        $("#showFlag").prop("checked", true);
    } else {
        $("#hideFlag").prop("checked", true);
    }
}

function clearFrmData(regxForm){
	//console.log('-- clearFrmData regxForm : '+ regxForm);
	var $form = $("#"+regxForm);
	$form[0].reset();
}

function gotoCancle(regxForm){
	clearFrmData(regxForm);
}

function gotoDel(idx){
	if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
		$('input[name=areaId]').val(idx);
		document.delFrm.submit();
	}
}

//검색 
function gotoSearch(){
	console.log('--- gotoSearch');
	
	if(formCheck("frm")){
		var szHtml = "<input name='areaSearch' type='hidden' value='1'> ";
		$("#frm").append(szHtml);
		document.frm.submit(); 
	}
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
  		  	<h2><span></span>설정 > 광역설정</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
          <!-- <a class="active" onclick="gotoAgeGroup('U18');">U18<span></span></a>
          <a onclick="gotoAgeGroup('U15');">U15<span></span></a>
          <a onclick="gotoAgeGroup('U12');">U12<span></span></a> -->
        </div>
        <div class="others">
          <!-- <select>
            <option>2020</option>
            <option>2019</option>
            <option>2018</option>
            <option>2017</option>
            <option>2016</option>
            <option>2015</option>
            <option>2014</option>
          </select> -->
        </div>
      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">
            <form name="frm" id="frm" method="post"  action="area" onsubmit="return false;">
            	<input name="cp" type="hidden" value="${cp}">
  				<input name="ageGroup" type="hidden" value="">
            	 <select name="sUage">
            	  	<option value="-1" selected>연령 선택</option>
		          	<c:forEach var="result" items="${uageList}" varStatus="status">
		         	<option value="${result.uage}">${result.uage}</option>
		          	</c:forEach>
		        </select>
              <!-- <select>
                <option>연령 선택</option>
                <option>U18</option>
                <option>U15</option>
                <option>U13</option>
              </select> -->
              <input type="text" name="sAreaName" placeholder="광역명 입력">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
            </form>
          </div>
          <div class="others">
            <a class="btn-large default btn-pop" data-id="update-area-add" onclick="gotoAddPopup();">광역등록</a>
          </div>
        </div>
        <table cellspacing="0" class="update">
          <colgroup>
            <col width="15%">
            <col width="15%">
            <col width="*">
            <col width="15%">
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>연령</th>
              <th>광역명</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
          	<c:if test="${empty areaList }">
				<tr>
					<td id="idEmptyList" colspan="4">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${areaList}" varStatus="status"> 
			<tr>
				<td>
					<c:choose>
	              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
	              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
	              	</c:choose>
				</td>
				<td>${result.uage}</td>
				<td>${result.area_name}</td>
				<td class="admin">
	                <a class="btn-pop" data-id="modify-area-add" onclick="gotoModPopup('${result.area_id}','${result.uage}','${result.area_name}', '${result.show_flag}');"><i class="xi-catched"></i></a>
	                <%-- <a class="btn-pop"  onclick="gotoModPopup('${result.area_id}');"><i class="xi-catched"></i></a> --%>
	                <a onclick="gotoDel('${result.area_id}');"><i class="xi-close-circle-o"></i></a>
                </td class="admin">
			</tr>
			</c:forEach>
            <!-- <tr>
              <td>1</td>
              <td>U18</td>
              <td>서울/인천</td>
              <td class="admin">
                <a class="btn-pop" data-id="modify-area-add"><i class="xi-catched"></i></a>
                <a><i class="xi-close-circle-o"></i></a>
              </td class="admin">
            </tr> -->
          </tbody>
        </table>
      </div>
    </div>
    <!--팝업-->
    <div class="pop" id="update-area-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            광역 등록
          </div>
          <form name="addFrm" id="addFrm" method="post"  action="save_area">
           <input type="hidden" name="sFlag" value="0">
           <input type="hidden" name="uage">
           
          <div class="body" style="padding:15px 20px;">
            <ul class="signup-list">
              <li class="title">
                <span class="title">연령선택</span>
                <select style="width:50%;" name="selUage" id="selUage">
		          	<c:forEach var="result" items="${uageList}" varStatus="status">
		          	<option value="${result.uage}">${result.uage}</option>
		          	</c:forEach>
		        </select>
          
               <!--  <select style="width:50%;">
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
            <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoAdd();"><span>등록하기</span></a>
            <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
          </div>
        </div>
      </div>
    </div>
    <div class="pop" id="modify-area-add">
      <div style="height:auto;">
        <div style="height:auto;">
          <div class="head">
            광역 수정
          </div>
          <form name="modFrm" id="modFrm" method="post"  action="save_area">
           <input type="hidden" name="sFlag" value="1">
           <input type="hidden" name="areaId">
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
                  <option selected>U15</option>
                  <option>U12</option>
                </select> -->
              </li>
              <li class="title">
                <span class="title">광역명</span>
                <input type="text" name="areaName" placeholder="광역명 입력 (예: 호남/제주)" >
              </li>
              <li class="title">
                <span class="title">노출 여부</span>
                <input type="radio" name="showFlag" id="showFlag" value="0" ><label for="showFlag">노출</label>
                <input type="radio" name="showFlag" id="hideFlag" value="1" ><label for="hideFlag">비노출 </label>
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