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
<script src="resources/swiper/js/swiper.min.js"></script>
<script src="resources/js/layout.js"></script>

<link rel="stylesheet" href="resources/css/layout.css">
<link rel="stylesheet" href="resources/swiper/css/swiper.css">
<link rel="stylesheet" href="resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="resources/ico/mobicon.png">

<script type="text/javascript">

$(document).ready(function() {
/* 	//검색
	var sArea = "${sArea}";
	if(!isEmpty(sArea)){
		$("select[name='sArea'] option[value='"+sArea+"']").prop("selected", "selected");
	}
	var sLeagueName = "${sLeagueName}";
	if(!isEmpty(sLeagueName)){
		$("input[name=sLeagueName]").val(sLeagueName);
	}
	console.log('--- [init search] sArea :'+ sArea+', sLeagueName : '+sLeagueName ); */
});


function gotoQnaDel(qnaId){
	if(confirm("삭제하시겠습니까?")){
		$("#qdfrm [name='qnaId']").val(qnaId);
		$("#qdfrm").submit();
		
		parent.location.reload();
	}
}


</script>
</head>
<body>

<% pageContext.setAttribute("newLineChar", "\n"); %>

  <h4 class="view-title">문의하기 상세</h4>
  <table cellspacing="0" class="update view">
    <colgroup>
      <col width="15%">
      <col width="*">
      <col width="15%">
      <col width="*">
    </colgroup>
          <tbody>
            <tr>
              <th class="tl">이름</th>
              <td class="tl">${qnaMap.customer}</td>
              <th class="tl">휴대폰 번호</th>
              <td class="tl">${qnaMap.phone}</td>
            </tr>
            <tr>
              <th class="tl">이메일</th>
              <td class="tl">${qnaMap.email}</td>
              <th class="tl">작성일</th>
              <td class="tl">${qnaMap.reg_date}</td>
			</tr>
            <tr>
              <th class="tl">제목</th>
              <!-- <td class="tl" colspan="3">귀사와 제휴에 대해 미팅하고 싶습니다. (첨부: <a class="title">회사소개서.pdf )</a></td> -->
              <td class="tl" colspan="3">${qnaMap.title}</td>

            </tr>
            <tr>
              <td class="tl_lh tl" colspan="4">${fn:replace(qnaMap.content, newLineChar, "<br/>")}</td>
            </tr>
          </tbody>
        </table><br>
        <div class="title">
          <a class="btn-large default" onclick="gotoQnaDel('${qnaMap.qna_id}');">삭제하기</a>
        </div>

</body>

<form name="qdfrm" id="qdfrm" method="post"  action="qnaDel">
  <input name="qnaId" type="hidden" value="">
</form> 

 
</html>