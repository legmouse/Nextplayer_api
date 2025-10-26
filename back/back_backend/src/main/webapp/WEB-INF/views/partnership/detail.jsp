<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("enter", "\n"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="resources/jquery/jquery-3.3.1.min.js"></script>
<script src="resources/jquery/jquery-ui.js"></script>
<script src="/resources/jquery/jquery.form.js"></script>
<script src="resources/js/layout.js"></script>

<script type="text/javascript" src="/resources/js/igp.js"></script>
<script type="text/javascript" src="/resources/js/init.js"></script>
<script type="text/javascript" src="/resources/js/igp.file.js"></script>


<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

    $(document).ready(function(){

    });

    const fnDownloadFile = (val) => {

    }

    const fnRemove = (val) => {
        const confirmMsg = confirm('정말 삭제 하시겠습니까?');
        const reqType = '${reqType}';
        if (confirmMsg) {

            let formData = new FormData();
            formData.append("suggestId", val);
            formData.append("method", "delete");


            $.ajax({
                type: 'POST',
                url: '/modify_partnership',
                data: formData,
                contentType : false,
                processData : false,
                success: function(res) {
                    if(res.state == 'SUCCESS') {
                        location.href = "/partnershipList";
                    }
                }
            });

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
                <h2><span></span>제휴 문의</h2>
            </div>
        </div>
        <div class="round body">

            <div class="body-head">
                <h4 class="view-title">제휴 문의 </h4>
            </div>
            <hr class="mb_10">
            <div class="scroll">
                <div class="title">
                    <h3>
                        내용
                    </h3>
                </div>
                <table cellspacing="0" class="update view">
                    <colgroup>
                        <col width="20%">
                        <col width="*">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th class="tl">작성일</th>
                            <td class="tl">
                                ${fn:substring(partnershipDetail.reg_date, 0, 10)}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">아이디</th>
                            <td class="tl">
                                ${partnershipDetail.member_id}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">이름</th>
                            <td class="tl">
                                ${partnershipDetail.customer}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">이메일</th>
                            <td class="tl">
                                ${partnershipDetail.email}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">휴대폰 번호</th>
                            <td class="tl">
                                ${partnershipDetail.phone}
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">내용</th>
                            <td class="tl">
                                <c:out value="${fn:replace(partnershipDetail.content, enter, '<br/>')}" escapeXml="false" />
                            </td>
                        </tr>
                        <tr>
                            <th class="tl">파일</th>
                            <td class="tl">
                                <c:choose>
                                    <c:when test="${!empty partnershipDetail.files}">
                                        <c:forEach var="items" items="${partnershipDetail.files}" varStatus="status">
                                            <%--<a class="btn-large gray-o" onclick="fnDownloadFile(${items.file_id})">--%>
                                            <a class="btn-large gray-o" href="/common/download2.do?fileId=${items.file_id}">
                                                <img src="/resources/img/icon/${items.file_ext}.gif" />
                                                ${items.original_name}
                                            </a>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </tbody>
                </table><br>

            <br>
            <form id="frm" method="post" action="/delete_reference"  enctype="multipart/form-data">
                <input type="hidden" name="referenceId" value="${partnershipDetail.board_id}">
            </form>
            <div class="tr w100">
                <a class="btn-large gray-o" id="eBtnSubmit" onclick="fnRemove('${partnershipDetail.suggest_id}')">삭제</a>
                <a class="btn-large default" onclick="fnGoModify('${partnershipDetail.board_id}')">수정 하기</a>
            </div>
            
        </div>
    </div>
    </div>


</body>
<form name="gotoResetFrm" id="gotoResetFrm" method="post" enctype="multipart/form-data" action="save_team"
    onsubmit="return false;">
    <input name="sFlag" type="hidden" value="10">
    <input name="cp" type="hidden" value="1">
    <input name="ageGroup" type="hidden" value="U18">
</form>
<form name="delFrm" id="delFrm" method="post" enctype="multipart/form-data" action="save_team">
    <input type="hidden" name="sFlag" value="2">
    <input type="hidden" name="teamId">
    <input type="hidden" name="emblem">
    <input name="ageGroup" type="hidden" value="U18">
</form>

</html>