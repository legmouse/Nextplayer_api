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

    });

    const goMemberList = (state) => {

        let newForm = $('<form></form>');
        newForm.attr('name', 'newForm');
        newForm.attr('method', 'post');
        newForm.attr('action', '/list');
        $(newForm).appendTo('body').submit();

    }

    function setThumbnail(obj){
        // 썸네일 이미지 보여주기
        var pathPoint = obj.value.lastIndexOf('.');
        var filePoint = obj.value.substring(pathPoint+1, obj.length);
        var fileType = filePoint.toLowerCase();
        var fileSize = obj.files[0].size;
        var maxSize = 1024 * 1024 * 5; 	// 5MB

        var fileName = obj.files[0].name;

        var blackList = ['() {', '|', '/', ';', '%00', '0x00'];

        if(fileType != "jpg" && fileType != "jpeg" && fileType != "png"){
            alert("이미지만 업로드 할 수 있습니다.");

            obj.value = "";
            obj.focus();
            return false;
        }

        if(fileSize > maxSize){
            alert("최대 5MB 크기의 이미지만 등록 가능합니다.");
            obj.value = "";
            return false;
        }

        for(var i=0; i < blackList.length; i++){
            if(fileName.indexOf(blackList[i]) > -1){
                alert('잘못된 파일 이름입니다.');
                obj.value = "";
                return false;
            }
        }


        $("#image_container").empty();

        var reader = new FileReader();

        var img = document.createElement('img');

        reader.onload = function(event){
            img.setAttribute("src", event.target.result);
            img.setAttribute("id", "img_file");
            img.setAttribute("onclick", "fnRemoveThumbnail();");
        };
        var str = '이미지를 클릭하면 이미지를 삭제할 수 있습니다. <br>';

        $("#image_container").append(str);
        $("#image_container").append(img);

        reader.readAsDataURL(event.target.files[0]);
    }

    const RegistMember = () => {

        const imageFile = $("#profileImg")[0];

        const memberType = $("#memberType").val();

        // 필수 값 start
        const memberId = $("#memberId").val();
        const memberPwd = $("#memberPwd").val();
        const email = $("#email").val();
        const phone = $("#phone").val();
        const memberNickname = $("#memberNickname").val();
        const memberName = $("#memberName").val();
        const age = $("#age").val();
        // 필수 값 end

        const birthDate = $("#birthDate").val();
        const childrenAge = $("#childrenAge").val();
        const sex = $("#sex").val();
        const memo = $("#memo").val();

        const isIdChecked = $("#memberId").attr("data-isChecked");
        const isEmailChecked = $("#email").attr("data-isChecked");

        if (!email) {
            alert('이메일을 입력해 주세요.');
            return false;
        }
        if (!isEmailChecked) {
            alert('이메일 중복체크를 해주세요.');
            return false;
        }
        if (email.replace(new RegExp(/[A-Za-z0-9]([\w\.-]*)@[A-Za-z0-9]([\w-]*)(\.[A-Za-z0-9][\w-]*)+/,),'',).length > 0) {
            alert('이메일 형식에 맞게 작성해주세요.');
            return false;
        }

        if (!memberId) {
            alert('아이디를 입력해 주세요.');
            return false;
        }
        if (!isIdChecked) {
            alert('아이디 중복체크를 해주세요.');
            return false;
        }
        if ((memberId.replace(new RegExp(/[0-9a-zA-Z]/g), '').length > 0) ||
            (memberId !== undefined && (memberId.length < 5 || memberId.length > 20))) {
            alert('사용할 수 없는 아이디 입니다.\n아이디는 한글을 제외한 최소 6글자 최대 20글자로 입력해주세요.');
            return false;
        }

        if (!memberPwd) {
            alert('비밀번호를 입력해 주세요.');
            return false;
        }
        if (memberPwd.replace(new RegExp(/(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*?_~])[a-zA-Z0-9!@#$%^&*?_~]{8,20}/,),'',).length > 0) {
            alert('사용할 수 없는 비밀번호입니다.\n비밀번호는 알파벳, 숫자, 특수문자를 1개이상 포함하여\n최소 8자, 최대 20자 사이로 입력해 주세요.');
            return false;
        }

        if (!phone) {
            alert('휴대폰 번호를 입력해주세요.');
            return false;
        }
        if (phone.replace(new RegExp(/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/),'',).length > 0) {
            alert('휴대폰 번호를 확인해주세요.');
            return false;
        }

        if (!memberNickname) {
            alert('닉네임을 입력해주세요.');
            return false;
        }
        if ((memberNickname.replace(new RegExp(/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/), '').length > 0) ||
            (memberNickname !== undefined && (memberNickname.length < 2 || memberNickname.length > 12))
        ) {
            alert('사용할 수 없는 닉네임 입니다.\n닉네임은 최소 2글자 최대 12글자로 입력해주세요.');
            return false;
        }

        if(memberName) {
            //if (memberName.match(new RegExp(/^[ㄱ-ㅎ가-힣]+$/))) {
            if (memberName.replace(new RegExp(/^[ㄱ-ㅎ가-힣]+$/), '').length > 0) {

            } else {
                alert('사용할 수 없는 이름 입니다.\n이름은 최소 2글자 최대 5글자로 입력해주세요.')
                return false;
            }
        }

        if (!age) {
            alert('출생연도를 입력해주세요.');
            return false;
        }
        /*if (age.match(new RegExp(/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/))) {
            return true;
        } else {
            alert('출생연도를 확인해 주세요');
            return false;
        }*/


        const paramData = {
            "memberType": memberType,
            "childrenAge": childrenAge,
            "memberConfirm": {
                "age": age,
                "birthDate": birthDate,
                "certType": "A",
                "email": email,
                "memberName": memberName,
                "phoneNo": phone,
                "sex": sex
            },
            "memberId": memberId,
            "memberNickname": memberNickname,
            "memberPwd": memberPwd,
            "memo": memo
        }

        console.log(paramData);

        const confirmMsg = confirm('회원 등록 하시겠습니까?');

        if (confirmMsg) {
            $.ajax({
                type: 'POST',
                //url: 'http://192.168.1.36:8112/back_login/base_login/api/v1/auth/personal_register',
                url: 'https://mobile.nextplayer.co.kr/back_login/base_login/api/v1/auth/personal_register',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(paramData),
                dataType : 'json',
                success: function(data) {
                    console.log(data);
                    if (data.state == 'SUCCESS' && data.stateCode == 200) {
                        alert('회원 등록에 성공했습니다.');
                        location.href = '/list';
                    } else {
                        alert('회원 등록에 실패했습니다.');
                        location.reload();
                    }
                }
            });
        }

        /*var formData = new FormData();
        formData.append("multipartFile", imageFile.files[0]);*/

        /*if (imageFile) {

            $.ajax({
                type: 'POST',
                url: 'http://192.168.1.36:8112/back_login/base_login/api/v1/auth/upload_pofileImg',
                processData: false,
                contentType: false,
                data: formData,
                dataType : 'json',
                success: function(data) {
                    console.log(data);
                }
            });

        }*/

    }

    const modifyMember = () => {

        const memberType = $("#memberType").val();

        // 필수 값 start
        const email = $("#email").val();
        const phone = $("#phone").val();
        const memberNickname = $("#memberNickname").val();
        const memberName = $("#memberName").val();
        const age = $("#age").val();
        // 필수 값 end

        const birthDate = $("#birthDate").val();
        const childrenAge = $("#childrenAge").val();
        const sex = $("#sex").val();
        const memo = $("#memo").val();

        const isEmailChecked = $("#email").attr("data-isChecked");

        if (!email) {
            alert('이메일을 입력해 주세요.');
            return false;
        }
        if (!isEmailChecked) {
            alert('이메일 중복체크를 해주세요.');
            return false;
        }
        if (email.replace(new RegExp(/[A-Za-z0-9]([\w\.-]*)@[A-Za-z0-9]([\w-]*)(\.[A-Za-z0-9][\w-]*)+/,),'',).length > 0) {
            alert('이메일 형식에 맞게 작성해주세요.');
            return false;
        }

        if (!phone) {
            alert('휴대폰 번호를 입력해주세요.');
            return false;
        }
        if (phone.replace(new RegExp(/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/),'',).length > 0) {
            alert('휴대폰 번호를 확인해주세요.');
            return false;
        }

        if (!memberNickname) {
            alert('닉네임을 입력해주세요.');
            return false;
        }
        if ((memberNickname.replace(new RegExp(/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/), '').length > 0) ||
            (memberNickname !== undefined && (memberNickname.length < 2 || memberNickname.length > 12))
        ) {
            alert('사용할 수 없는 닉네임 입니다.\n닉네임은 최소 2글자 최대 12글자로 입력해주세요.');
            return false;
        }

        if(memberName) {
            //if (memberName.match(new RegExp(/^[ㄱ-ㅎ가-힣]+$/))) {
            if (memberName.match(new RegExp(/^[ㄱ-ㅎ가-힣]+$/))) {

            } else {
                alert('사용할 수 없는 이름 입니다.\n이름은 최소 2글자 최대 5글자로 입력해주세요.')
                return false;
            }
        }

        if (!age) {
            alert('출생연도를 입력해주세요.');
            return false;
        }


        const paramData = {
                "age": age,
                "birthDate": birthDate,
                "certType": "A",
                "email": email,
                "memberName": memberName,
                "phoneNo": phone,
                "sex": sex
        }

        console.log(paramData);

        const confirmMsg = confirm('회원 수정 하시겠습니까?');

        if (confirmMsg) {
            let newForm = $('<form></form>');
            newForm.attr('name', 'newForm');
            newForm.attr('method', 'post');
            newForm.attr('action', '/personalModify');

            newForm.append($('<input/>', {type: 'hidden', name: 'memberCd', value: '${memberDetail.member_cd}' }));
            newForm.append($('<input/>', {type: 'hidden', name: 'memberType', value: memberType }));
            newForm.append($('<input/>', {type: 'hidden', name: 'childrenAge', value: childrenAge }));
            newForm.append($('<input/>', {type: 'hidden', name: 'memberNickname', value: memberNickname }));
            newForm.append($('<input/>', {type: 'hidden', name: 'memberConfirm', value: JSON.stringify(paramData) }));

            $(newForm).appendTo('body').submit();
        }

        /*if (confirmMsg) {
            $.ajax({
                type: 'POST',
                //url: 'http://192.168.1.12:8081/back_login/base_login/api/v1/auth/personal_modify',
                url: '/personalModify',
                data: paramData,
                success: function(data) {
                    console.log(data);
                    if (data.state == 'SUCCESS' && data.stateCode == 200) {
                        alert('회원 수정에 성공했습니다.');
                        location.href = '/list';
                    } else {
                        alert('회원 수정에 실패했습니다.');
                        location.reload();
                    }
                }
            });
        }*/

    }

    const fnMemberEmailCheck = () => {

        const email = $("#email").val();

        if (!email) {
            alert('이메일을 입력해 주세요.');
            return false;
        }
        if (email.replace(new RegExp(/[A-Za-z0-9]([\w\.-]*)@[A-Za-z0-9]([\w-]*)(\.[A-Za-z0-9][\w-]*)+/,),'',).length > 0) {
            alert('이메일 형식에 맞게 작성해주세요.');
            return false;
        }

        $.ajax({
            type: 'GET',
            //url: 'http://192.168.1.12:8112/back_login/base_login/api/v1/auth/emailUseCheck?email=' + email,
            url: 'https://mobile.nextplayer.co.kr/back_login/base_login/api/v1/auth/emailUseCheck?email=' + email,
            contentType: 'application/json; charset=utf-8',
            success: function(data) {
                console.log('data > ', data);
                if (data.state == 'SUCCESS' && data.stateCode == 200) {
                    if (data.data == 1) {
                        alert('이미 가입된 이메일입니다.');
                        $("#memberId").val(null);
                        $("#memberId").focus();
                    } else {
                        $("#email").attr("data-isChecked", true);
                        $("#email").attr("readonly", true);
                        $("#emailCheckBtn").attr("disabled", true);
                    }
                } else {
                    alert('이메일 중복체크에 실패했습니다.');
                    return;
                }
            }
        });
    }

    const fnMemberEmailCheckForModify = (value) => {

        const email = $("#email").val();

        if (!email) {
            alert('이메일을 입력해 주세요.');
            return false;
        }
        if (email.replace(new RegExp(/[A-Za-z0-9]([\w\.-]*)@[A-Za-z0-9]([\w-]*)(\.[A-Za-z0-9][\w-]*)+/,),'',).length > 0) {
            alert('이메일 형식에 맞게 작성해주세요.');
            return false;
        }

        const param = {
            memberCd: value,
            email : email
        }

        $.ajax({
            type: 'GET',
            //url: 'http://192.168.1.12:8081/back_login/base_login/api/v1/auth/emailUseCheckForModify?email=' + email + "&memberCd=" + value,
            url: '/emailUseCheck',
            data: param,
            contentType: 'application/json; charset=utf-8',
            success: function(data) {
                console.log(data.data);
                if (data.state == 'SUCCESS') {
                    if (data.data == 1) {
                        alert('이미 가입된 이메일입니다.');
                        $("#memberId").val(null);
                        $("#memberId").focus();
                    } else {
                        $("#email").attr("data-isChecked", true);
                        $("#email").attr("readonly", true);
                        $("#emailCheckBtn").attr("disabled", true);
                    }
                } else {
                    alert('이메일 중복체크에 실패했습니다.');
                    return;
                }
            }
        });
    }

    const fnMemberIdCheck = () => {

        const memberId = $("#memberId").val();

        if (!memberId) {
            alert('아이디를 입력해 주세요.');
            return false;
        }
        if ((memberId.replace(new RegExp(/[0-9a-zA-Z]/g), '').length > 0) ||
            (memberId !== undefined && (memberId.length < 5 || memberId.length > 20))) {
            alert('사용할 수 없는 아이디 입니다.\n아이디는 한글을 제외한 최소 6글자 최대 20글자로 입력해주세요.');
            return false;
        }

        $.ajax({
            type: 'GET',
            //url: 'http://192.168.1.12:8112/back_login/base_login/api/v1/auth/idUseCheck?memberId=' + memberId,
            url: 'https://mobile.nextplayer.co.kr/back_login/base_login/api/v1/auth/idUseCheck?memberId=' + memberId,
            contentType: 'application/json; charset=utf-8',
            success: function(data) {
                console.log(data);
                if (data.state == 'SUCCESS' && data.stateCode == 200) {
                    if (data.data == 1) {
                        alert('중복된 아이디입니다.');
                        $("#memberId").val(null);
                        $("#memberId").focus();
                    } else {
                        $("#memberId").attr("data-isChecked", true);
                        $("#memberId").attr("readonly", true);
                        $("#idCheckBtn").attr("disabled", true);
                    }
                } else {
                    alert('아이디 중복체크에 실패했습니다.');
                    return;
                }
            }
        });

    }

    function fnRemoveThumbnail(){
        $("#image_container").empty();
        $("#input[name=profileImg]").val("");
    }

</script>
<body>
    <div class="wrapper" id="wrapper">
        <jsp:include page="../common/menu.jsp" flush="true">
            <jsp:param name="page" value="main" />
            <jsp:param name="main" value="0" />
        </jsp:include>
        <div class="contents active">
            <div class="head">
                <div class="sub-menu">
                    <h2><span></span>회원관리</h2>
                </div>
            </div>
            <div class="round body">
                <div class="body-head">
                    <h4 class="view-title">기본 정보 <c:if test="${method eq 'Modify'}">수정</c:if></h4>
                </div>
                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">
                        </colgroup>
                        <tbody>
                        <tr>
                            <th class="tl">계정종류</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <select id="memberType" name="memberType">
                                            <option value="1">학부모</option>
                                            <option value="2">감독/코치</option>
                                            <option value="3">레슨 선생님</option>
                                            <option value="4">학원/클럽 관계자</option>
                                        </select>
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <select id="memberType" name="memberType">
                                            <option value="1" <c:if test="${memberDetail.member_type eq '1'}">selected</c:if>>학부모</option>
                                            <option value="2" <c:if test="${memberDetail.member_type eq '2'}">selected</c:if>>감독/코치</option>
                                            <option value="3" <c:if test="${memberDetail.member_type eq '3'}">selected</c:if>>레슨 선생님</option>
                                            <option value="4" <c:if test="${memberDetail.member_type eq '4'}">selected</c:if>>학원/클럽 관계자</option>
                                        </select>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr><tr>
                            <th class="tl">이메일 <em>*</em></th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="email" name="email" placeholder="1111@gmail.com" value="">
                                        <button type="button" id="emailCheckBtn" onclick="fnMemberEmailCheck()">중복확인</button>
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="email" name="email" placeholder="1111@gmail.com" value="${memberDetail.email}">
                                        <button type="button" id="emailCheckBtn" onclick="fnMemberEmailCheckForModify('${memberDetail.member_cd}')">중복확인</button>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <c:if test="${method eq 'Regist'}">
                        <tr>
                            <th class="tl">아이디 <em>*</em></th>
                            <td class="tl">
                                <input type="text" id="memberId" name="memberId" placeholder="" value="">
                                <button type="button" id="idCheckBtn" onclick="fnMemberIdCheck()">중복확인</button>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">비밀번호 <em>*</em></th>
                            <td class="tl"><input type="text" id="memberPwd" name="memberPwd" placeholder="" value=""></td>
                        </tr>
                        </c:if>
                        <tr>
                            <th class="tl">휴대폰 번호<em>*</em></th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="phone" name="phone" placeholder="휴대폰 번호 (“-“ 없이 입력)" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="phone" name="phone" placeholder="휴대폰 번호 (“-“ 없이 입력)" value="${memberDetail.phone_no}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">닉네임 <em>*</em></th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="memberNickname" name="memberNickname" placeholder="" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="memberNickname" name="memberNickname" placeholder="" value="${memberDetail.member_nickname}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">이름 </th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="memberName" name="memberName" placeholder="" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="memberName" name="memberName" placeholder="" value="${memberDetail.member_name}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">출생연도 <em>*</em></th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="age" name="age" placeholder="예) 1993" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="age" name="age" placeholder="예) 1993" value="${memberDetail.age}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">생일</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="birthDate" name="birthDate" placeholder="예) 06-30" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="birthDate" name="birthDate" placeholder="예) 06-30" value="${memberDetail.birth_date}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">자녀연령</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <input type="text" id="childrenAge" name="childrenAge" placeholder="예) 2000" value="">
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <input type="text" id="childrenAge" name="childrenAge" placeholder="예) 2000" value="${memberDetail.childrenAge}">
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">성별 <em>*</em></th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                        <select id="sex" name="sex">
                                            <option value="M">남</option>
                                            <option value="W">여</option>
                                        </select>
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                        <select id="sex" name="sex">
                                            <option value="M" <c:if test="${memberDetail.sex eq 'M'}">selected</c:if>>남</option>
                                            <option value="W" <c:if test="${memberDetail.sex eq 'W'}">selected</c:if>>여</option>
                                        </select>
                                    </c:when>
                                </c:choose>

                            </td>
                        </tr>
                        <%--<tr>
                            <th class="tl">프로필 이미지</th>
                            <td class="tl">
                                <input type="file" id="profileImg" name="profileImg" onChange="setThumbnail(this);" accept="image/jpeg, image/jpg, image/png"/>
                                <div id="image_container"></div>
                            </td>
                        </tr>--%>
                        <tr>
                            <th class="tl">메모</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${method eq 'Regist'}">
                                <textarea id="memo" name="memo"></textarea>
                                    </c:when>
                                    <c:when test="${method eq 'Modify'}">
                                <textarea id="memo" name="memo">${memberDetail.memo}</textarea>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <br>
                <a class="btn-large gray-o" onclick="goMemberList()">취소 하기</a>
                <c:choose>
                    <c:when test="${method eq 'Regist'}">
                <a href="#" class="btn-large default" onclick="RegistMember()">등록</a>
                    </c:when>
                    <c:when test="${method eq 'Modify'}">
                <a href="#" class="btn-large default" onclick="modifyMember()">수정</a>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>
