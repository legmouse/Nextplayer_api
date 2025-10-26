<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="../../resources/jquery/jquery-3.3.1.min.js"></script>
<script src="../../resources/jquery/jquery-ui.js"></script>
<script src="../../resources/swiper/js/swiper.min.js"></script>
<script src="../../resources/js/layout.js"></script>
<script src="../../resources/js/page/define.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/swiper/css/swiper.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

$( function(){
	$( 'input' ).on("blur keyup", function() {
		$(this).val( $(this).val().replace( /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' ) ); //한글입력방지
		$(this).val($(this).val().replace(/(\s*)/g, '')); //공백제거
	});
})

function press(f){
	//console.log('--- press f.keyCode : '+ f.keyCode);
	if(f.keyCode == 13){ //javascript에서는 13이 enter키를 의미함
		//console.log('--- press f.keyCode Enter ~~  ');
		if(checkForm()) {
			document.frm.submit();
		}
	} 
}

function getCookie(name) {
    cookieName = name + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cookieName);
    var cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}

$(document).ready(function() {
	$('#login').click(function() { 
		//alert('Handler for .submit() called.'); 
		if(checkForm()) {
			document.frm.submit();
		}
	});
	
	//저장된 쿠기값 가져오기
	//쿠키 아이디
	var cookieUserId = getCookie("cookieUserId");
	var cookieUserPw = getCookie("cookieUserPw");
	/* console.log("-- cookieUserId : "+cookieUserId);
	console.log("-- cookieUserPw : "+cookieUserPw); */
	
    $("input[name='id']").val(cookieUserId); 
    $("input[name='pw']").val(cookieUserPw);
    
	if(!isEmpty(cookieUserId)){ // ID 저장하기를 체크 상태로 두기.
        $("#savei").prop("checked", true); 
    }
    if(!isEmpty(cookieUserPw)){ //PW 저장하기를 체크 상태로 두기.
        $("#savep").prop("checked", true); 
    }
});


//로그인 Form check
function checkForm() {
	var valid = true;
	
	var userId = $("input[name='id']").val();
	var userPw = $("input[name='pw']").val();
	
	if(isEmpty(userId)) {
		$("input[name='id']").focus();
		alert('알림!\n 아이디를 입력해 주세요');
		return false;
	}
	console.log('-- password leng : ' + userPw.trim().length);
	
	if(isEmpty(userPw)) {
		$("input[name='pw']").focus();
		alert('알림!\n 비밀번호를 입력해 주세요.');
		return false;
	}
	
	if(valid == false){
		return false;
	}
	
	if($("#savei").prop("checked")){
		$('input[name=useCookieId]').val(true);
	}
	if($("#savep").prop("checked")){
		$('input[name=useCookiePw]').val(true);
	}
	
	return valid;
}


</script>
</head>
<body>

  <div class="wrapper tc" id="wrapper" style="background-color: rgba(57,65,87,1);">
    <div class="login-area">
      <div>
        <div class="login-head">
          <a href="0-메인.html"><h1><img src="resources/img/logo-small.png"></h1></a>
        </div>
        
		<form id="frm" name="frm" method="post" action="loginProcess">
		<input name="useCookieId" type="hidden" value="false">
		<input name="useCookiePw" type="hidden" value="false">
        
        <div class="login-body">
          <p>Administration ID</p>
          <input type="text" name="id"  style="ime-mode:disabled;" onkeypress="javascript:press(event);">
          <p>Password</p>
          <input type="password" name="pw"  style="ime-mode:disabled;" onkeypress="javascript:press(event);">
          <div class="save">
	          <input type="checkbox" id="savei"><label for="savei">아이디 저장</label> 
	          <input type="checkbox" id="savep"><label for="savep">패스워드 저장</label>
          </div>
        </div>
        <div class="login-foot">
          <input type="button" id="login" value="LOGIN" >
        </div>
		</form>     

      </div>
    </div>
  <div>

</body>
</html>