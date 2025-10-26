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
<script type="text/javascript" src="resources/apexcharts/apexcharts.min.js"></script>
<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">
<link rel="stylesheet" href="resources/apexcharts/apexcharts.css">
<c:set var="uri" value="${requestScope['javax.servlet.forward.servlet_path']}" />

<style>
  .apexcharts-menu-icon{
    display: none;
  }
</style>

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
	var sArea = "${sArea}";
	var sTeamType = "${sTeamType}";
	$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	$("select[name='sTeamType'] option[value='"+sTeamType+"']").prop("selected", "selected");
	$("input[name=sNickName]").val("${sNickName}");
	
	var teamId = '${teamInfoMap.team_id}';
	
	$(".btn-close-pop").click(function() {
		$('body').css('overflow', 'auto');
	    $(".pop").fadeOut();
	    $('#add, #map').val('');
	    // $('#add-player').find('#childTbody').html('');
	    // $('#mapping-player').find('#childTbody').html('');
	});
});

function gotoAgeGroup(ageGroup){
	  clearFrmData("frm");
	  $('input[name=ageGroup]').val(ageGroup);
	  $("input[name=cp]").val(1);
	  document.frm.submit();
}

//검색 
/* function gotoSearch(){
	//console.log('--- gotoSearch');
	if(searchFormCheck("sfrm")){
		$("input[name=cp]").val(1);
		document.sfrm.submit(); 
	}
} */

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




//팀 상세 리스트 이동
function gotoTeamMgr(){
	document.tmfrm.submit();
}

//대회관리 - 예선 이동
function gotoCupMgrSubMatch(cupId){
	$('#cmsmfrm input[name=cupId]').val(cupId);
	document.cmsmfrm.submit();
}

//대회관리 - 토너먼트 이동
function gotoCupMgrTourMatch(cupId){
	$('#cmtmfrm input[name=cupId]').val(cupId);
	document.cmtmfrm.submit();
}

//클럽관리 - 대회정보 - 연도 이동
function gototeamMgrDetCupYear(year){
	$('#tmdcfrm input[name=sYear]').val(year);
	document.tmdcfrm.submit();
}

//클럽관리 - 요약정보 
function gototeamMgrDet(){
	document.tmdfrm.submit();
}
//클럽관리 - 대회정보 
function gototeamMgrDetCup(){
	document.tmdcfrm.submit();
}
//클럽관리 - 리그정보 
function gototeamMgrDetLeague(){
	document.tmdlfrm.submit();
}
//클럽관리 - 선수정보 
function gototeamMgrDetPlayer(){
	document.tmdpfrm.submit();
}

function matchingTeam() {
	var originalGroupName = document.tmdmfrm.originalGroupName.value;
	var npTeamId = document.tmdmfrm.npTeamId.value;
	var npTeamGroupId = document.tmdmfrm.npTeamGroupId.value;
	var param = {
			"npTeamId": npTeamId,
			"npTeamGroupId": npTeamGroupId,
			"originalGroupName": originalGroupName
	};
	$.ajax({
        type: 'POST',
        url: '/groupNameCheck',
        data: param,
        success: function(res) {
        	if (res.status > 0) {
        		alert('성공');
        		window.location.reload();
        	} else {
        		alert('실패! 팀 그룹에 속하지 않았거나 JOINKFA 팀명이 정확하지 않습니다.');
        		return false;
        	}
        }
    });
}

function addKfaPlayerPop(obj) {
	
	var teamId = '${teamInfoMap.team_id}';
	
	$.ajax({
        type: 'POST',
        url: '/getPlayerList',
        data: {"teamId" : teamId},
        success: function(res) {
        	var htmlStr = '';
        	if (res.list != null && res.list.length > 0) {
        		for (var i = 0; i < res.list.length; i++) {
        			htmlStr += '<tr>';
            		htmlStr += 		'<td>';
            		htmlStr +=			'<input type="checkbox" ' + 
            								'name="ch' + i + '" ' + 
            								'id="player' + res.list[i].player_id + '" ' +
            								'data-playerid="' + res.list[i].player_id + '" ' +
            								'data-playername="' + res.list[i].name + '" ' +
            								'data-position="' + res.list[i].position + '" ' +
            								'data-birthday="' + res.list[i].birthday + '">';
            		htmlStr +=			'<label for="player' + res.list[i].player_id + '"></label>';
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].name;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].position;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].birthday;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].team_name;
            		htmlStr += 		'</td>';
            		htmlStr += '</tr>';
        		}
        	} else {
        		htmlStr += '<tr>';
        		htmlStr += 		'<td colspan="5">';
        		htmlStr +=			'조회 결과가 없습니다.';
        		htmlStr += 		'</td>';
        		htmlStr += '</tr>';
        	}
        	$(".pop").hide();
            var currentpopid = $(obj).attr("data-id");
            $("#"+currentpopid).find('#childTbody').html(htmlStr);
            $("#"+currentpopid).fadeIn();
            $('body').css('overflow', 'hidden');
        }
    });
}

function viewInsertInput() {
	$('#singleAdd').css('display', 'none');
	$('#selectList').css('display', 'none');
	$('#insertInput').css('display', 'block');
}

function viewSelectList() {
	$('#singleAdd').css('display', 'inline-block');
	$('#insertInput').css('display', 'none');
	$('#selectList').css('display', 'block');
}

function regPlayerList() {
	var chkplayer = $("input[type=checkbox]:checked");
	var params = {};
	var param = {
			playerList : []
	};
	
	var joinKfaTeamId = '${joinKfaTeamId}';
	
	if (joinKfaTeamId == null || joinKfaTeamId == '') {
		alert('팀 매칭 이후에 시도해주세요.');
		return false;
	}
	
	if (chkplayer.length == 0) {
		alert('선수를 선택해주세요.');
		return false;
	}
	
	$(chkplayer).each(function() {
		var data = {};
		data.playerId = $(this).data('playerid');
		data.playerName = $(this).data('playername');
		data.playerPosition = $(this).data('position');
		data.playerBirthday = $(this).data('birthday');
		data.teamId = joinKfaTeamId;
		param.playerList.push(JSON.stringify(data));
	});
	params.playerList = (JSON.stringify(param));
	
	$.ajax({
        type: 'POST',
        url: '/regPlayerList',
        data: params,
        success: function(res) {
        	window.location.reload();
        }
	});
	
}

function regPlayer() {
	
	var joinKfaTeamId = '${joinKfaTeamId}';
	
	if (joinKfaTeamId == null || joinKfaTeamId == '') {
		alert('팀 매칭 이후에 시도해주세요.');
		return false;
	}
	
	var playerName = document.kfafrm.playerName.value;
	if (playerName == null || playerName == '') {
		alert('이름을 입력해주세요.');
		return false;
	}
	var playerPosition = document.kfafrm.playerPosition.value;
	var playerImg = document.kfafrm.playerImg.value;
	var playerBirthday = document.kfafrm.playerBirthday.value;
	var playerWeight = document.kfafrm.playerWeight.value;
	var playerHeight = document.kfafrm.playerHeight.value;
	var param = {
			playerName: playerName,
			playerPosition: playerPosition,
			playerImg: playerImg,
			playerBirthday: playerBirthday,
			playerWeight: playerWeight,
			playerHeight: playerHeight,
			teamId: joinKfaTeamId
	};
	$.ajax({
        type: 'POST',
        url: '/regPlayer',
        data: param,
        success: function(res) {
        	window.location.reload();
        }
	});
}

//학원/클럽 수정
function gotoModPopup(idx, areaName, teamType, nickName, teamName, addr, emblem, useFlag, originalGroupName, teamGroupId){
	clearFrmData("modFrm");
	var ageGroup = "${ageGroup}";
	$('input[name=ageGroup]').val(ageGroup);
	$('input[name=nickName]').val(nickName);
	$('input[name=teamName]').val(teamName);
	$('input:radio[name=teamType]:input[value='+teamType+']').attr('checked', true);
	$("select[name='selArea'] option[value='"+areaName+"']").prop("selected", true);
	$('input[name=addr]').val(addr);
	$('input[name=imgFilePath]').val(emblem);
	$('input[name=teamId]').val(idx);
	
	$('input[name=originalGroupName]').val(originalGroupName);
	$('input[name=teamGroupId]').val(teamGroupId);

    $('input[name=useFlag]').val(useFlag);
    if (!emblem) {
        $(".img-del").css("display", "none");
    }
    
    var uri = "${uri}";
    $('input[name=uri]').val(uri.replace("/", ""));
	$('body').css('overflow', 'hidden');
}

function gotoMod(){
	if(formCheck("modFrm")){
		document.modFrm.submit();
	}
}

//폼 input type 초기화
function clearFrmData(regxForm){
	var $form = $("#"+regxForm);
	$form[0].reset();
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

function modPlayer(id, name, position, img, birthday, weight, height) {
	clearFrmData("modpFrm");
	var ageGroup = "${ageGroup}";
	$('input[name=ageGroup]').val(ageGroup);
	$('input[name=playerId]').val(id);
	$('input[name=playerName]').val(name);
	$('input[name=playerPosition]').val(position);
	$('input[name=playerImg]').val(img);
	$('input[name=playerBirthday]').val(birthday);
	$('input[name=playerWeight]').val(weight);
	$('input[name=playerHeight]').val(height);
	
	$(".pop").hide();
    $("#modefy-player-single").fadeIn();
}

function gotoModPlayer() {
	var valid = true;
	var $form = $("#modpFrm");

    $form.find('input:text').each(
		function(key) {
			var $obj = $(this);
			if(isEmpty($obj.val())) {
				switch ($obj.attr('name')){
				case 'playerName' :
					alert('알림!\n이름을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
					
				case 'playerPosition' :
					alert('알림!\n포지션을 입력 하세요.');
					$obj.focus();
					valid = false;
					return false;
					break;
				} 
			
			}else{
				if(isRegExp($obj.val())){
					alert('알림!\n 등록 오류 입니다.\n<>&" 특수문자가 존재 합니다.\n특수문자 제거 후 다시 등록해 주세요.');
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
   	} else {
   		document.modpFrm.submit();
   	}
}

function gotoDel(id) {
	var confirmMsg = confirm("해당 선수를 삭제 하시겠습니까?");

    if (confirmMsg) {

        $.ajax({
            type : 'POST',
            url : '/remove_joinKfaPlayer',
            data: {playerId : id},
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    alert('삭제되었습니다.');
                    location.reload();
                } else {
                    alert('삭제에 실패 했습니다.');
                    location.reload();
                }
            }
        })
    }
}

function searchPlayerPop(id, name) {
	
	var teamId = '${teamInfoMap.team_id}';
	
	$.ajax({
        type: 'POST',
        url: '/getPlayerList',
        data: {playerName: name},
        success: function(res) {
        	var htmlStr = '';
        	if (res.list != null && res.list.length > 0) {
        		for (var i = 0; i < res.list.length; i++) {
        			htmlStr += '<tr>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].name;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].position;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		if (res.list[i].birthday != null && res.list[i].birthday != '' && res.list[i].birthday != 'undefined') {
            			htmlStr +=			res.list[i].birthday;
            		} else {
            			htmlStr +=			'-';
            		}
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			res.list[i].team_name;
            		htmlStr += 		'</td>';
            		htmlStr += 		'<td>';
            		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModMapping(' + res.list[i].player_id + ', \'add\');">선택하기</a>';
            		htmlStr += 		'</td>';
            		htmlStr += '</tr>';
        		}
        	} else {
        		htmlStr += '<tr>';
        		htmlStr += 		'<td colspan="5">';
        		htmlStr +=			'조회 결과가 없습니다.';
        		htmlStr += 		'</td>';
        		htmlStr += '</tr>';
        	}
        	$(".pop").hide();
        	$('#nameSearch').html("'"+ name + "'으로 검색한 결과입니다.");
        	$('#map').val(name);
            $("#mapping-player").find('#childTbody').html(htmlStr);
            $("#mapping-player").find('input[name=playerId]').val(id);
            $("#mapping-player").fadeIn();
            $('body').css('overflow', 'hidden');
        }
    });
}

function gotoSearch(type) {
	var param = {};
	var teamId = '${teamInfoMap.team_id}';
	var keyword = $('#' + type).val();
	if (keyword == null || keyword == '') {
		alert('검색어를 입력해주세요.');
		return false;
	}
	var sType = $('select[name=sType] option:selected').val();
	var mType = $('select[name=mType] option:selected').val();
	param.teamId = teamId;
	param.keyword = keyword;
	if (type == 'add') {
		param.sType = sType;
	} else if (type == 'map') {
		param.mType = mType;
	}
	$.ajax({
        type: 'POST',
        url: '/getPlayerList',
        data: param,
        success: function(res) {
        	
        	var htmlStr = '';
        	
        	if (type == 'add') {
        		if (res.list != null && res.list.length > 0) {
            		for (var i = 0; i < res.list.length; i++) {
            			htmlStr += '<tr>';
                		htmlStr += 		'<td>';
                		htmlStr +=			'<input type="checkbox" ' + 
                								'name="ch' + i + '" ' + 
                								'id="player' + res.list[i].player_id + '" ' +
                								'data-playerid="' + res.list[i].player_id + '" ' +
                								'data-playername="' + res.list[i].name + '" ' +
                								'data-position="' + res.list[i].position + '" ' +
                								'data-birthday="' + res.list[i].birthday + '">';
                		htmlStr +=			'<label for="player' + res.list[i].player_id + '"></label>';
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].name;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].position;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].birthday;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].team_name;
                		htmlStr += 		'</td>';
                		htmlStr += '</tr>';
            		}
            	} else {
            		htmlStr += '<tr>';
            		htmlStr += 		'<td colspan="5">';
            		htmlStr +=			'조회 결과가 없습니다.';
            		htmlStr += 		'</td>';
            		htmlStr += '</tr>';
            	}
        		$("#selectList").find('#childTbody').html(htmlStr);
        	} else if (type == 'map') {
        		if (res.list != null && res.list.length > 0) {
            		for (var i = 0; i < res.list.length; i++) {
            			htmlStr += '<tr>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].name;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].position;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].birthday;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			res.list[i].team_name;
                		htmlStr += 		'</td>';
                		htmlStr += 		'<td>';
                		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModMapping(' + res.list[i].player_id + ', \"add\");">선택하기</a>';
                		htmlStr += 		'</td>';
                		htmlStr += '</tr>';
            		}
            	} else {
            		htmlStr += '<tr>';
            		htmlStr += 		'<td colspan="5">';
            		htmlStr +=			'조회 결과가 없습니다.';
            		htmlStr += 		'</td>';
            		htmlStr += '</tr>';
            	}
        		$('#nameSearch').html("'" + keyword + "'으로 검색한 결과입니다.");
        		$("#mapping-player").find('#childTbody').html(htmlStr);
        	}
            // $("#mapping-player").fadeIn();
        }
    });
}

function gotoModMapping(id, type) {
	var param = {};
	
	if (type == 'add') {
		var npPlayerId = $('#mapping-player').find('input[name=playerId]').val();
		param.npPlayerId = id;
		param.playerId = npPlayerId;
	} else if (type == 'del') {
		param.playerId = id;
	}
	
	$.ajax({
        type: 'POST',
        url: '/modify_mapping',
        data: param,
        success: function(res) {
        	if (res.state == 'SUCCESS') {
                alert('성공');
                location.reload();
            } else {
                alert('실패');
                location.reload();
            }
        }
	});
}

function gotoSearchTeamPop() {
	$('#search-joinkfa-team').fadeIn();
	// $('body').css('overflow', 'hidden');
}

function closeSearchTeam() {
	$('#addTeam').val();
	$("#selectTeamList").find('#childTbody').html('<tr><td colspan="7">팀을 검색해주세요</td></tr>');
	$('#search-joinkfa-team').fadeOut();
}

function viewInsertTeamInput() {
	$('#teamTitle').html('JOINKFA 팀 등록');
	$('#regBtn').text('등록하기');
	$('#teamBody').css('width', '400px');
	$('#singleTeamAdd').css('display', 'none');
	$('#selectTeamList').css('display', 'none');
	$('#insertTeamInput').css('display', 'block');
	$('input[name=mode]').val(0);
}

function viewSelectTeamList() {
	$('#teamTitle').html('JOINKFA 팀 찾기');
	$('#teamBody').css('width', '1000px');
	$('#singleTeamAdd').css('display', 'inline-block');
	$('#insertTeamInput').css('display', 'none');
	$('#selectTeamList').css('display', 'block');
	clearFrmData("kfatfrm");
}

function gotoSearchTeam() {
	var searchTxt = $('#addTeam').val();
	if (searchTxt == null || searchTxt == '') {
		alert('팀명을 입력해주세요.');
		return false;
	}
	$.ajax({
        type: 'POST',
        url: '/getTeamList',
        data: {searchTxt: searchTxt},
        success: function(res) {
			var htmlStr = '';
        	
        	if (res.list != null && res.list.length > 0) {
            	for (var i = 0; i < res.list.length; i++) {
            		htmlStr += '<tr>';
               		htmlStr += 		'<td>';
               		htmlStr +=			res.list[i].team_name;
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		if (res.list[i].team_create_date != null && res.list[i].team_create_date != '') {
               			htmlStr +=			res.list[i].team_create_date;
               		} else {
               			htmlStr += '-';
               		}
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		if (res.list[i].team_address != null && res.list[i].team_address != '') {
               			htmlStr +=			res.list[i].team_address;
               		} else {
               			htmlStr += '-';
               		}
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		var levelStr = res.list[i].level;
               		switch(levelStr) {
       					case 'ES' :
       						levelStr = '초등';
       						break;
       					case 'MS' :
       						levelStr = '중등';
       						break;
       					case 'HS' :
       						levelStr = '고등';
       						break;
       					case 'UV' :
       						levelStr = '대학';
       						break;
       					case 'U2' :
       						levelStr = 'U12';
       						break;
       					case 'U5' :
       						levelStr = 'U15';
    	   					break;
	       				case 'U8' :
       						levelStr = 'U18';
       						break;
       				}
               		htmlStr +=			levelStr;
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		if (res.list[i].director != null && res.list[i].director != '') {
               			htmlStr +=			res.list[i].director;
               		} else {
               			htmlStr += '-';
               		}
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		if (res.list[i].coach != null && res.list[i].coach != '') {
               			htmlStr +=			res.list[i].coach;
               		} else {
               			htmlStr += '-';
               		}
               		htmlStr += 		'</td>';
               		htmlStr += 		'<td>';
               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoTeamNameInsert(\'' + res.list[i].team_name + '\');">선택하기</a> ';
               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModJoinKfaTeam(\'' + res.list[i].team_id + '\', \'' + res.list[i].team_name + '\', \'' + res.list[i].team_create_date + '\',\'' + res.list[i].team_address + '\',\'' + res.list[i].team_contact + '\',\'' + res.list[i].level + '\',\'' + res.list[i].team_img + '\');">수정하기</a> ';
               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoDelJoinKfaTeam(\'' + res.list[i].team_id + '\');">삭제</a> ';
               		htmlStr += 		'</td>';
               		htmlStr += '</tr>';
            	}
            } else {
            	htmlStr += '<tr>';
            	htmlStr += 		'<td colspan="7">';
            	htmlStr +=			'조회 결과가 없습니다.';
            	htmlStr += 		'</td>';
            	htmlStr += '</tr>';
            }
        	$("#selectTeamList").find('#childTbody').html(htmlStr);
        }
	});
}

function gotoTeamNameInsert(teamName) {
	$('#modefy-school-single').find('input[name=originalGroupName]').val(teamName);
	$("#selectTeamList").find('#childTbody').html('<tr><td colspan="7">팀을 검색해주세요</td></tr>');
	$('#addTeam').val('');
	$("#search-joinkfa-team").fadeOut();
	$('input[name=kfaTeamId]').val('');
}

function gotoModJoinKfaTeam(id, teamName, dateStr, address, tel, level, img) {
	$('input[name=kfaTeamId]').val(id);
	$('input[name=newTeamName]').val(teamName);
	$('input[name=teamCreateDate]').val(dateStr);
	$('input[name=teamAddress]').val(address);
	$('input[name=teamContact]').val(tel);
	$('input[name=teamImg]').val(img);
	$("select[name='level'] option[value='"+level+"']").prop("selected", true);
	$('#teamTitle').html('JOINKFA 팀 수정');
	$('#regBtn').text('수정하기');
	$('#teamBody').css('width', '400px');
	$('#singleTeamAdd').css('display', 'none');
	$('#selectTeamList').css('display', 'none');
	$('#insertTeamInput').css('display', 'block');
	$('input[name=mode]').val(1);
}

function regTeam() {
	var newTeamName = document.kfatfrm.newTeamName.value;
	if (newTeamName == null || newTeamName == '') {
		alert('팀명을 입력해주세요.');
		return false;
	}
	var teamImg = document.kfatfrm.teamImg.value;
	var teamCreateDate = document.kfatfrm.teamCreateDate.value;
	var teamAddress = document.kfatfrm.teamAddress.value;
	var teamContact = document.kfatfrm.teamContact.value;
	var level = document.kfatfrm.level.value;
	var kfaTeamId = document.kfatfrm.kfaTeamId.value;
	var searchTxt = $('#addTeam').val();
	
	var param = {
			kfaTeamId: kfaTeamId,
			newTeamName: newTeamName,
			teamImg: teamImg,
			teamCreateDate: teamCreateDate,
			teamAddress: teamAddress,
			teamContact: teamContact,
			level: level,
			searchTxt: searchTxt
	};
	var mode = $('input[name=mode]').val();
	if (mode == 0) {
		$.ajax({
	        type: 'POST',
	        url: '/regJoinKfaTeam',
	        data: param,
	        success: function(res) {
	        	if (res.state == 'SUCCESS') {
	        		alert('팀이 등록되었습니다.');
	        		clearFrmData("kfatfrm");
	        		viewSelectTeamList();
	        		var htmlStr = '';
		        	if (res.list != null && res.list.length > 0) {
		            	for (var i = 0; i < res.list.length; i++) {
		            		htmlStr += '<tr>';
		               		htmlStr += 		'<td>';
		               		htmlStr +=			res.list[i].team_name;
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].team_create_date != null && res.list[i].team_create_date != '') {
		               			htmlStr +=			res.list[i].team_create_date;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].team_address != null && res.list[i].team_address != '') {
		               			htmlStr +=			res.list[i].team_address;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		var levelStr = res.list[i].level;
		               		switch(levelStr) {
		               			case 'ES' :
		               				levelStr = '초등';
		               				break;
		               			case 'MS' :
		               				levelStr = '중등';
		               				break;
		               			case 'HS' :
		               				levelStr = '고등';
		               				break;
		               			case 'UV' :
		               				levelStr = '대학';
		               				break;
		               			case 'U2' :
		               				levelStr = 'U12';
		               				break;
		               			case 'U5' :
		               				levelStr = 'U15';
		               				break;
		               			case 'U8' :
		               				levelStr = 'U18';
		               				break;
		               		}
		               		htmlStr +=			levelStr;
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].director != null && res.list[i].director != '') {
		               			htmlStr +=			res.list[i].director;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].coach != null && res.list[i].coach != '') {
		               			htmlStr +=			res.list[i].coach;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoTeamNameInsert(\'' + res.list[i].team_name + '\');">선택하기</a> ';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModJoinKfaTeam(\'' + res.list[i].team_id + '\', \'' + res.list[i].team_name + '\', \'' + res.list[i].team_create_date + '\',\'' + res.list[i].team_address + '\',\'' + res.list[i].team_contact + '\',\'' + res.list[i].level + '\',\'' + res.list[i].team_img + '\');">수정하기</a> ';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoDelJoinKfaTeam(\'' + res.list[i].team_id + '\');">삭제</a> ';
		               		htmlStr += 		'</td>';
		               		htmlStr += '</tr>';
		            	}
		            } else {
		            	htmlStr += '<tr>';
		            	htmlStr += 		'<td colspan="7">';
		            	htmlStr +=			'조회 결과가 없습니다.';
		            	htmlStr += 		'</td>';
		            	htmlStr += '</tr>';
		            }
		        	$("#selectTeamList").find('#childTbody').html(htmlStr);
	        	} else {
	        		alert('팀 등록에 실패하였습니다.');
	        	}
	        }
		});
	} else if (mode == 1) {
		$.ajax({
	        type: 'POST',
	        url: '/modJoinKfaTeam',
	        data: param,
	        success: function(res) {
	        	if (res.state == 'SUCCESS') {
	        		alert('수정되었습니다.');
	        		viewSelectTeamList();
	        		var htmlStr = '';
		        	if (res.list != null && res.list.length > 0) {
		            	for (var i = 0; i < res.list.length; i++) {
		            		htmlStr += '<tr>';
		               		htmlStr += 		'<td>';
		               		htmlStr +=			res.list[i].team_name;
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].team_create_date != null && res.list[i].team_create_date != '') {
		               			htmlStr +=			res.list[i].team_create_date;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].team_address != null && res.list[i].team_address != '') {
		               			htmlStr +=			res.list[i].team_address;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		var levelStr = res.list[i].level;
		               		switch(levelStr) {
	               				case 'ES' :
	               					levelStr = '초등';
	               					break;
	               				case 'MS' :
	               					levelStr = '중등';
	               					break;
	               				case 'HS' :
	               					levelStr = '고등';
	               					break;
	               				case 'UV' :
	               					levelStr = '대학';
	               					break;
	               				case 'U2' :
	               					levelStr = 'U12';
	               					break;
	               				case 'U5' :
	               					levelStr = 'U15';
	               					break;
	               				case 'U8' :
	               					levelStr = 'U18';
	               					break;
	               			}
		               		htmlStr +=			levelStr;
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].director != null && res.list[i].director != '') {
		               			htmlStr +=			res.list[i].director;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		if (res.list[i].coach != null && res.list[i].coach != '') {
		               			htmlStr +=			res.list[i].coach;
		               		} else {
		               			htmlStr += '-';
		               		}
		               		htmlStr += 		'</td>';
		               		htmlStr += 		'<td>';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoTeamNameInsert(\'' + res.list[i].team_name + '\');">선택하기</a> ';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModJoinKfaTeam(\'' + res.list[i].team_id + '\', \'' + res.list[i].team_name + '\', \'' + res.list[i].team_create_date + '\',\'' + res.list[i].team_address + '\',\'' + res.list[i].team_contact + '\',\'' + res.list[i].level + '\',\'' + res.list[i].team_img + '\');">수정하기</a> ';
		               		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoDelJoinKfaTeam(\'' + res.list[i].team_id + '\');">삭제</a> ';
		               		htmlStr += 		'</td>';
		               		htmlStr += '</tr>';
		            	}
		            } else {
		            	htmlStr += '<tr>';
		            	htmlStr += 		'<td colspan="7">';
		            	htmlStr +=			'조회 결과가 없습니다.';
		            	htmlStr += 		'</td>';
		            	htmlStr += '</tr>';
		            }
		        	$("#selectTeamList").find('#childTbody').html(htmlStr);
	        	} else {
	        		alert('팀 수정에 실패 했습니다.');
	        	}
	        }
		});
	}
	clearFrmData("kfatfrm");
}

function gotoDelJoinKfaTeam(id) {
	var confirmMsg = confirm("해당 팀을 삭제 하시겠습니까?");

    if (confirmMsg) {
    	var searchTxt = $('#addTeam').val();
        $.ajax({
            type : 'POST',
            url : '/remove_joinKfaTeam',
            data: {kfaTeamId : id, searchTxt: searchTxt},
            success: function(res) {
                if (res.state == 'SUCCESS') {
                    alert('삭제되었습니다.');
                    var htmlStr = '';
                    if (res.list != null && res.list.length > 0) {
                    	for (var i = 0; i < res.list.length; i++) {
                    		htmlStr += '<tr>';
                       		htmlStr += 		'<td>';
                       		htmlStr +=			res.list[i].team_name;
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		if (res.list[i].team_create_date != null && res.list[i].team_create_date != '') {
                       			htmlStr +=			res.list[i].team_create_date;
                       		} else {
                       			htmlStr += '-';
                       		}
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		if (res.list[i].team_address != null && res.list[i].team_address != '') {
                       			htmlStr +=			res.list[i].team_address;
                       		} else {
                       			htmlStr += '-';
                       		}
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		var levelStr = res.list[i].level;
                       		switch(levelStr) {
               					case 'ES' :
               						levelStr = '초등';
               						break;
               					case 'MS' :
               						levelStr = '중등';
               						break;
               					case 'HS' :
               						levelStr = '고등';
               						break;
               					case 'UV' :
               						levelStr = '대학';
               						break;
               					case 'U2' :
               						levelStr = 'U12';
               						break;
               					case 'U5' :
               						levelStr = 'U15';
               						break;
               					case 'U8' :
               						levelStr = 'U18';
               						break;
               				}
                       		htmlStr +=			levelStr;
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		if (res.list[i].director != null && res.list[i].director != '') {
                       			htmlStr +=			res.list[i].director;
                       		} else {
                       			htmlStr += '-';
                       		}
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		if (res.list[i].coach != null && res.list[i].coach != '') {
                       			htmlStr +=			res.list[i].coach;
                       		} else {
                       			htmlStr += '-';
                       		}
                       		htmlStr += 		'</td>';
                       		htmlStr += 		'<td>';
                       		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoTeamNameInsert(\'' + res.list[i].team_name + '\');">선택하기</a> ';
                       		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoModJoinKfaTeam(\'' + res.list[i].team_name + '\', \'' + res.list[i].team_create_date + '\',\'' + res.list[i].team_address + '\',\'' + res.list[i].team_contact + '\',\'' + res.list[i].level + '\',\'' + res.list[i].team_img + '\');">수정하기</a> ';
                       		htmlStr +=			'<a class="btn-large gray-o" onclick="gotoDelJoinKfaTeam(\'' + res.list[i].team_id + '\');">삭제</a> ';
                       		htmlStr += 		'</td>';
                       		htmlStr += '</tr>';
                    	}
                    } else {
                    	htmlStr += '<tr>';
                    	htmlStr += 		'<td colspan="7">';
                    	htmlStr +=			'조회 결과가 없습니다.';
                    	htmlStr += 		'</td>';
                    	htmlStr += '</tr>';
                    }
                	$("#selectTeamList").find('#childTbody').html(htmlStr);
                } else {
                    alert('삭제에 실패 했습니다.');
                }
            }
        })
    }
}

function addPlayer(teamId, playerName, position, birthday) {
	$.ajax({
        type : 'POST',
        url : '/playerCheck',
        data: {teamId: teamId, playerName: playerName},
        success: function(res) {
        	if (res.state == 'SUCCESS') {
        		var url = '../savePlayer';
        		if (res.method == 'modify') {
        			if (res.list.length > 0) {
        				url = '../playerList?sKeyword=' + playerName;
        				window.open(url, 'blank');
        				/* var listFilter = res.list.filter((item) => item.team_id == teamId);
        				if (listFilter.length > 1) {
        					url = '../playerList?sKeyword=' + listFilter[0].name;
        					window.open(url, 'blank');
        				} else if (listFilter.length == 1) {
        					url = '../playerList?sKeyword=' + listFilter[0].name;
        					// url += '?playerId=' + listFilter[0].player_id + '&method=' + res.method;
        					window.open(url, 'blank');
        				} else {
        					url += '?playerName=' + playerName + '&position=' + position + '&birthday=' + birthday;
        					window.open(url, 'blank');
        				} */
        			} 
        			/* else if (res.list.length == 1) {
        				url = '../playerList?sKeyword=' + res.list[0].name;
        				// url += '?playerId=' + res.list[0].player_id + '&method=' + res.method; 
        				window.open(url, 'blank')
        			}*/ 
        			else {
        				url += '?playerName=' + playerName + '&position=' + position + '&birthday=' + birthday;
        				window.open(url, 'blank');
        			}
        		} else if (res.method == 'save') {
        			url += '?playerName=' + playerName + '&position=' + position + '&birthday=' + birthday;
        			window.open(url, 'blank');
        		}
        	} else {
        		alert('에러가 발생하였습니다.');
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
  		  	<h2><span></span>학원·클럽 > 관리</h2>
  		  	<c:forEach var="result" items="${uageList}" varStatus="status">
  		  		<a id="${result.uage }" onclick="gotoAgeGroup('${result.uage}');">${result.uage }<span></span></a>
  		  	</c:forEach>
        </div>

      </div>
      <div class="round body">
        <div class="body-head">
          <div class="search">

          <form name="frm" id="frm" method="post"  action="teamMgr" onsubmit="return false;">
          	  <input name="cp" type="hidden" value="${cp}">
  			  <input name="ageGroup" type="hidden" value="">
  			  <input name="sYear" type="hidden" value="${sYear}">

  			  <input name="orderName" type="hidden" value="${orderName}">
			  	<input name="order" type="hidden" value="${order}">
						<c:set var="now" value="<%=new java.util.Date()%>" />
						<c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy" /></c:set>
							<select name="selYears" id="selYears" onchange="gotoYear(this.value);" class="ac">
								<c:forEach var="i" begin="2014" end="${sysYear }" step="1">
									<c:choose>
									<c:when test="${i eq sYear}">
										<option value="${i}" selected>${i}년</option>
									</c:when>
									<c:when test="${empty sYear and i eq sysYear}">
										<option value="${i}" selected>${i}년</option>
									</c:when>
									<c:otherwise>
										<option value="${i}">${i}년</option>
									</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<span class="mlr5 co999">|</span>
	          <select class="w150" name="sArea">
	          <option value="-1" selected>광역 선택</option>
	          <c:forEach var="result" items="${areaList}" varStatus="status">
	          	<option value="${result.area_name}">${result.area_name}</option>
	          </c:forEach>
	          </select>
	          <select class="w150" name="sTeamType">
	            <option value="-1">학원/클럽 선택</option>
	            <option value="0">학원</option>
	            <option value="1">클럽</option>
	            <option value="2">유스</option>
	          </select>
              <input type="text" name="sNickName" placeholder="사용명칭 학원/클럽명 입력" onkeydown="javascript:if(event.keyCode==13){gotoSearch();}">
              <i class="xi-search" onclick="gotoSearch();" style="cursor:pointer"></i>
          </form>
          </div>
          <div class="others">
          	<a class="btn-large gray-o" onclick="gotoTeamMgr();"><i class="xi-long-arrow-left"></i> 학원/클럽관리 리스트</a>
          </div>
        </div>
        <br>
        <div class="body-head" style="border-bottom: 1px solid #eee;margin-bottom: 15px">
	        <c:choose>
	       	<c:when test="${empty teamInfoMap.emblem}"><img src="resources/img/logo/none.png" class="logo"></c:when>
	       	<c:otherwise><img src="/NP${teamInfoMap.emblem}" class="logo"></c:otherwise>
	       	</c:choose>
          <h3>



            ${teamInfoMap.nick_name}
            <c:choose>
          	<c:when test="${teamInfoMap.team_type eq '0'}"><span class="">[학원]</span></c:when>
          	<c:when test="${teamInfoMap.team_type eq '1'}"><span class="">[클럽]</span></c:when>
          	<c:when test="${teamInfoMap.team_type eq '2'}"><span class="">[유스]</span></c:when>
          	</c:choose>
            <a class="btn-large gray-o btn-pop" data-id="modefy-school-single" onclick="gotoModPopup('${teamInfoMap.team_id}','${teamInfoMap.area_name}','${teamInfoMap.team_type}','${teamInfoMap.nick_name}','${teamInfoMap.team_name}','${teamInfoMap.addr}','${teamInfoMap.emblem}', '${teamInfoMap.use_flag}', '${teamInfoMap.original_group_name}', '${teamInfoMap.team_group_id}');">정보수정</a>
            <c:if test="${teamInfoMap.original_group_name ne null and teamInfoMap.original_group_name ne ''}">
            <br>
            <span class="summary">${teamInfoMap.original_group_name}</span>
            </c:if>
            <br>
            <span class="summary">${teamInfoMap.addr}</span>
          </h3>
          <div class="others">
            <div class="tab" 
            <c:if test="${teamInfoMap.original_group_name ne null and teamInfoMap.original_group_name ne ''}">
            	style="margin-top: 60px;"
            </c:if>
            >
              <a onclick="gototeamMgrDet();">요약정보</a>
              <a onclick="gototeamMgrDetCup();">대회정보</a>
              <a onclick="gototeamMgrDetLeague();">리그정보</a>
              <a class="active" onclick="gototeamMgrDetPlayer();">선수정보</a>
            </div>
          </div>
       </div>
       
       <div class="body-body">
		  <div class="w100 mt_10">
		  	<c:choose>
		  		<c:when test="${includeTeam eq 0}">
		  			<a class="btn-large gray" style="cursor: default;">팀 매칭</a>
		  		</c:when>
		  		<c:otherwise>
		  			<a class="btn-large gray-o" onclick="matchingTeam();">팀 매칭</a>
		  			<!-- <a class="btn-large gray" style="cursor: default;">선수정보 불러오기</a> -->
		  		</c:otherwise>
		  	</c:choose>
          </div>
          <br>
          <br>
          <br>
          <div class="scroll">
          <div class="tl mb_10" style="width: 300px; float: left;">
                    	<h4>감독정보</h4>
                    </div>
                    <div class="tr mb_10" style="width: 300px; float: right;">
                    	<a class="btn-large gray-o">추가하기</a>
                    </div>
          <br>
          <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="20%">
                            <col width ="*">
                            <col width ="10%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>구분</th>
                            <th>이름</th>
                            <th>삭제하기</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                            <c:choose>
                                <c:when test="${empty groupDetail.teams}">
                                    <tr>
                                        <td colspan="3">등록된 내역이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <br>
                    <br>
          			<br>
                    <div class="tl mb_10" style="width: 300px; float: left;">
                    	<h4>선수정보</h4>
                    </div>
                    <div class="tr mb_10" style="width: 300px; float: right;">
                    	<a class="btn-large gray-o" data-id="add-player" onclick="addKfaPlayerPop(this);">KFA 선수 추가하기</a>
                    </div>
          			<table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="20%">
                            <col width ="20%">
                            <col width ="*">
                            <col width ="20%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>포지션</th>
                            <th>이미지</th>
                            <th>이름</th>
                            <th>비고</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                            <c:choose>
                                <c:when test="${empty playerList}">
                                    <tr>
                                        <td colspan="4">등록된 내역이 없습니다.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                	<c:forEach var="player" items="${playerList}" varStatus="index">
                                		<tr>
                                			<td>${player.player_position}</td>
                                			<td>
                                				<c:choose>
	       											<c:when test="${empty player.player_img_binary}"><img src="resources/img/logo/none.png" style="width: 60px;"></c:when>
	       											<c:otherwise><img src="${player.player_img_binary}" style="width: 60px;"></c:otherwise>
	       										</c:choose>
                                			</td>
                                			<td>${player.player_name}</td>
                                			<td>
                                				<a class="btn-large gray-o" onclick="modPlayer('${player.player_id}', '${player.player_name}', '${player.player_position}', '${player.player_img}', '${player.player_birthday}', '${player.player_weight}', '${player.player_height}');">수정하기</a>
                                				<a class="btn-large gray-o" onclick="gotoDel('${player.player_id}')">삭제하기</a>
                                				<c:choose>
                                					<c:when test="${not empty player.np_player_id}">
                                						<span class="btn-large gray" style="color: black; cursor: default;">
                                							${player.name}, ${player.position}, ${player.team_name} 
                                							<i class="xi-close-circle-o" style="cursor: pointer;" onclick="gotoModMapping('${player.player_id}', 'del');"></i>
                                						</span>
                                					</c:when>
                                					<c:otherwise>
                                						<a class="btn-large gray-o" onclick="searchPlayerPop('${player.player_id}', '${player.player_name}');">검색하기</a>
                                						<a class="btn-large gray-o" target="blank" onclick="addPlayer('${teamInfoMap.team_id}', '${player.player_name}', '${player.player_position}', '${player.player_birthday}')">추가하기</a>
                                					</c:otherwise>
                                				</c:choose>
                                			</td>
                                		</tr>
                                	</c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <!-- <div class="w100 tr mt_10" style="width: 300px;">
                    	<a class="btn-large gray-o">수정하기</a>
                    </div> -->
                    </div>
        </div>
      </div>
    </div>

    <!--팝업-->
	  <div class="pop" id="modefy-school-single">
		<div style="height:auto;">
		  <div style="height:auto;">
			<div class="head">
			  학원/클럽 개별 수정
			</div>
			<form name="modFrm" id="modFrm" method="post" enctype="multipart/form-data" action="save_team_mgr">
           		<input type="hidden" name="ageGroup" value="${ageGroup}">
           		<input type="hidden" name="teamId">
           		<input type="hidden" name="imgFilePath">
           		<input type="hidden" name="useFlag">
           		<input type="hidden" name="teamGroupId">
           		<input type="hidden" name="uri">
           		<input type="hidden" name="cp" value="${cp}">
				<div class="body" style="padding:15px 20px;">
				  <ul class="signup-list">
					<li class="title">
					  <span class="title">사용명칭</span>
					  <input type="text" name="nickName" placeholder="사용명칭 입력 (예: 전주영생고)">
					</li>
					<li class="title">
					  <span class="title">정식명칭</span>
					  <input type="text" name="teamName" placeholder="정식명칭 입력 (예: 전주영생고등학교)">
					</li>
					<li class="title">
					  <span class="title">JOINKFA명칭</span>
					  <input type="text" name="originalGroupName" placeholder="JOINKFA에 등록된 이름 입력">
					  <i class="xi-search" style="cursor:pointer; position: absolute; line-height: 27px;" onclick="gotoSearchTeamPop();"></i>
					</li>
					<li class="title">
					  <span class="title">구분</span>
					  <input type="radio" name="teamType" id="school" value="0"><label class="w20" for="school">학원</label>
					  <input type="radio" name="teamType" id="club" value="1"><label class="w20" for="club">클럽</label>
					  <input type="radio" name="teamType" id="youth" value="2"><label class="w20" for="youth">유스</label>
					</li>
					<li class="title">
					  <span class="title">지역선택</span>
					  <select style="width:50%;" name="selArea" id="selArea">
					  	<option value="-1" selected>광역 선택</option>
						<c:forEach var="result" items="${areaList}" varStatus="status">
							<option value="${result.area_name}">${result.area_name}</option>
			          	</c:forEach>
					  </select>
					  <a class="btn-pop" data-id="update-area-add"><i class="xi-cog" style="line-height: 28px;"></i></a>
					</li>
					<li class="title">
					  <span class="title">소재지</span>
					  <input type="text" name="addr" placeholder="소재지 입력 (예: 서울특별시 마포구 성산동)" value="전라북도 전주시 완산구 효자동3가">
					</li>
					<li class="title">
					  <span class="title">앰블럼</span>
					  <input type="file" id="emblemFile" name="emblemFile" style="width:50%;">
					  <a class="img-del"><span>기존파일</span><i class="xi-close-circle-o"></i></a>
					</li>
				  </ul>
				</div>
			</form>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoMod();"><span>등록하기</span></a>
			  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
			</div>
		  </div>
		</div>
	  </div>
	  
	  <div class="pop" id="add-player">
		<div style="height:auto;">
		  <div style="height:auto; width: 450px;">
			<div class="head">
			  선수 추가하기
			  <div class="tr mt_20" style="width: 300px; float: right;">
                    	<a class="btn-large gray-o" id="singleAdd" onclick="viewInsertInput();">직접입력</a>
                    </div>
			</div>
			<div id="selectList">
			<div class="body" style="padding:15px 20px;">
				<div>
          			<div class="search">
          				<select name="sType" id="sType" style="right: 310px;">
          					<option value="playerName">선수 이름</option>
          					<option value="teamName">팀명</option>
          				</select>
						<input type="text" id="add" style="width: 220px; position: relative; left: 10px;" onkeydown="javascript:if(event.keyCode==13){gotoSearch('add');}">
						<i class="xi-search" style="cursor:pointer; right: 90px;" onclick="gotoSearch('add');"></i>
					</div>
					<br>
					<table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="10%">
                            <col width ="20%">
                            <col width ="20%">
                            <col width ="20%">
                            <col width ="*">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>선택</th>
                            <th>이름</th>
                            <th>포지션</th>
                            <th>생년월일</th>
                            <th>소속팀</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                        </tbody>
                    </table>
				</div>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="regPlayerList();"><span>추가하기</span></a>
			  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
			</div>
			</div>
			<div id="insertInput" style="display: none;">
			<div class="body" style="padding:15px 20px;">
			  <form name="kfafrm" id="kfafrm">
			  <ul class="signup-list">
				<li class="title">
				  <span class="title">이름</span>
				  <input type="text" name="playerName">
				</li>
				<li class="title">
				  <span class="title">포지션</span>
				  <input type="text" name="playerPosition">
				</li>
				<li class="title">
				  <span class="title">사진 URL 주소</span>
				  <input type="text" name="playerImg">
				</li>
				<li class="title">
				  <span class="title">생년월일</span>
				  <input type="text" name="playerBirthday" placeholder="예) 20020101">
				</li>
				<li class="title">
				  <span class="title">몸무게</span>
				  <input type="text" name="playerWeight">
				</li>
				<li class="title">
				  <span class="title">키</span>
				  <input type="text" name="playerHeight">
				</li>
			  </ul>
			  </form>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="regPlayer();"><span>등록하기</span></a>
			  <a class="login btn-large gray w100" onclick="viewSelectList();"><span>취소</span></a>
			</div>
			</div>
		  </div>
		</div>
	  </div>
	  
	  <div class="pop" id="modefy-player-single">
		<div style="height:auto;">
		  <div style="height:auto;">
			<div class="head">
			 KFA 선수 수정
			</div>
			<form name="modpFrm" id="modpFrm" method="post" action="save_player_mgr">
           		<input type="hidden" name="ageGroup" value="${ageGroup}">
           		<input name="npTeamId" type="hidden" value="${teamInfoMap.team_id}">
           		<input type="hidden" name="playerId">
           		<input type="hidden" name="cp" value="${cp}">
				<div class="body" style="padding:15px 20px;">
				  <ul class="signup-list">
					<li class="title">
					  <span class="title">이름</span>
					  <input type="text" name="playerName">
					</li>
					<li class="title">
					  <span class="title">포지션</span>
					  <input type="text" name="playerPosition">
					</li>
					<li class="title">
					  <span class="title">사진 URL 주소</span>
					  <input type="text" name="playerImg">
					</li>
					<li class="title">
					  <span class="title">생년월일</span>
					  <input type="text" name="playerBirthday" placeholder="예) 20020101">
					</li>
					<li class="title">
					  <span class="title">몸무게</span>
					  <input type="text" name="playerWeight">
					</li>
					<li class="title">
					  <span class="title">키</span>
					  <input type="text" name="playerHeight">
					</li>
				  </ul>
				</div>
			</form>
			<div class="foot">
			  <a class="login btn-large default w100" style="margin-bottom:5px;" onclick="gotoModPlayer();"><span>수정하기</span></a>
			  <a class="login btn-large btn-close-pop gray w100"><span>취소</span></a>
			</div>
		  </div>
		</div>
	  </div>
	  
	  <div class="pop" id="mapping-player">
		<div style="height:auto;">
		  <div style="height:auto; width: 500px;">
		  	<input type="hidden" name="playerId">
			<div class="head">
			  선수 매핑하기
			  <div class="tr mt_20" style="width: 300px; float: right;">
			  	<i class="xi-close-circle-o btn-close-pop" style="cursor:pointer"></i>
			  </div>
			</div>
			<div class="body" style="padding:15px 20px;">
				<div class="search">
				<div>
					<select name="mType" id="mType" style="right: 360px;">
						<option value="all">전체 검색</option>
						<option value="team">팀내 검색</option>
					</select>
					<input type="text" id="map" style="width: 240px;" onkeydown="javascript:if(event.keyCode==13){gotoSearch('map');}">
					<i class="xi-search" style="cursor:pointer; right: 115px;" onclick="gotoSearch('map');"></i>
				</div>
				<br>
				<div>
					<span id="nameSearch"></span>
				</div>
				<br>
				<table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="20%">
                            <col width ="20%">
                            <col width ="20%">
                            <col width ="*">
                            <col width ="15%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>이름</th>
                            <th>포지션</th>
                            <th>생년월일</th>
                            <th>소속팀</th>
                            <th>선택하기</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                        </tbody>
                    </table>
				</div>
			</div>
		  </div>
		</div>
	  </div>
	  
	  <div class="pop" id="search-joinkfa-team">
		<div style="height:auto;">
		  <div id="teamBody" style="height:auto; width: 1000px;">
			<div class="head">
			  <p id="teamTitle" style="position: absolute;">
			   	JOINKFA 팀 찾기
			  </p>
			  <div class="tr mt_20" style="width: 200px; float: right;">
                    	<a class="btn-large gray-o" id="singleTeamAdd" onclick="viewInsertTeamInput();">팀 등록</a>
                    </div>
			</div>
			<div id="selectTeamList">
			<div class="body" style="padding:15px 20px;">
				<div>
          			<div class="search">
          				<input type="hidden" name="mode">
						<input type="text" id="addTeam" onkeydown="javascript:if(event.keyCode==13){gotoSearchTeam();}">
						<i class="xi-search" style="cursor:pointer;" onclick="gotoSearchTeam();"></i>
					</div>
					<br>
					<table cellspacing="0" class="update view">
                        <colgroup>
                            <col width ="20%">
                            <col width ="10%">
                            <col width ="30%">
                            <col width ="8%">
                            <col width ="8%">
                            <col width ="8%">
                            <col width ="30%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>팀명</th>
                            <th>팀 창단일</th>
                            <th>팀 소재지</th>
                            <th>구분</th>
                            <th>감독</th>
                            <th>코치</th>
                            <th>비고</th>
                        </tr>
                        </thead>
                        <tbody id="childTbody">
                        	<tr>
                        		<td colspan="7">팀을 검색해주세요</td>
                        	</tr>
                        </tbody>
                    </table>
				</div>
			</div>
			<div class="foot">
			  <a class="login btn-large gray w100" onclick="closeSearchTeam();"><span>취소</span></a>
			</div>
			</div>
			<div id="insertTeamInput" style="display: none;">
			<div class="body" style="padding:15px 20px;">
			  <form name="kfatfrm" id="kfatfrm">
			  <input type="hidden" name="kfaTeamId" value="">
			  <ul class="signup-list">
				<li class="title">
				  <span class="title">팀명</span>
				  <input type="text" name="newTeamName">
				</li>
				<li class="title">
				  <span class="title">엠블렘 URL</span>
				  <input type="text" name="teamImg">
				</li>
				<li class="title">
				  <span class="title">팀 창단일</span>
				  <input type="text" name="teamCreateDate" placeholder="예) 20020101">
				</li>
				<li class="title">
				  <span class="title">팀 소재지</span>
				  <input type="text" name="teamAddress">
				</li>
				<li class="title">
				  <span class="title">팀 연락처</span>
				  <input type="text" name="teamContact" placeholder="예) 010-1234-1234">
				</li>
				<li class="title">
				  <span class="title">구분</span>
				  <select name="level">
				  	<option value="ES">초등</option>
				  	<option value="MS">중등</option>
				  	<option value="HS">고등</option>
				  	<option value="UV">대학</option>
				  	<option value="U2">U12</option>
				  	<option value="U5">U15</option>
				  	<option value="U8">U18</option>
				  </select>
				</li>
			  </ul>
			  </form>
			</div>
			<div class="foot">
			  <a class="login btn-large default w100" id="regBtn" style="margin-bottom:5px;" onclick="regTeam();"><span>등록하기</span></a>
			  <a class="login btn-large gray w100" onclick="viewSelectTeamList();"><span>취소</span></a>
			</div>
			</div>
		  </div>
		</div>
	  </div>
    <!--팝업 끝--> 
  <div>
</body>

<form name="tmfrm" id="tmfrm" method="post"  action="teamMgr">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
</form> 

<form name="tmdfrm" id="tmdfrm" method="post"  action="teamMgrDet">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 
<form name="tmdcfrm" id="tmdcfrm" method="post"  action="teamMgrDetCup">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 
<form name="tmdlfrm" id="tmdlfrm" method="post"  action="teamMgrDetLeague">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

<form name="tmdpfrm" id="tmdpfrm" method="post"  action="teamMgrDetPlayer">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="teamId" type="hidden" value="${teamInfoMap.team_id}">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form>

<form name="tmdmfrm" id="tmdmfrm">
  <input name="cp" type="hidden" value="${cp}">
  <input name="sYear" type="hidden" value="${sYear}">
  <input name="npTeamId" type="hidden" value="${teamInfoMap.team_id}">
  <input name="npTeamGroupId" type="hidden" value="${teamInfoMap.team_group_id}">
  <input name="originalGroupName" type="hidden" value="${teamInfoMap.original_group_name}">
  <input name="joinKfaTeamId" type="hidden" value="${joinKfaTeamId}">
</form> 

<form name="cmsmfrm" id="cmsmfrm" method="post"  action="cupMgrSubMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cupId" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

<form name="cmtmfrm" id="cmtmfrm" method="post"  action="cupMgrTourMatch">
  <input name="sFlag" type="hidden" value="1">
  <input name="cupId" type="hidden" value="">
  <input name="ageGroup" type="hidden" value="${ageGroup}">
</form> 

</html>