<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<script src="../resources/jquery/jquery-3.3.1.min.js"></script>
<script src="../resources/jquery/jquery-ui.js"></script>
<script src="../resources/js/layout.js"></script>

<link rel="stylesheet" href="../resources/css/layout.css">
<link rel="stylesheet" href="../resources/xeicon/xeicon.min.css">
<link rel="stylesheet" href="../resources/nanum/nanumsquare.css">
<link rel="shortcut icon" href="../resources/ico/favicon.ico">
<link rel="apple-touch-icon" href="../resources/ico/mobicon.png">

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
//페이징 처리
function gotoPaging(cp) {
    $('input[name=cp]').val(cp);
    document.frm.submit();
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
  		  	<h2><span></span>문의/공지 > 문의 및 제안</h2>
        </div>
      </div>
 	  <div class="round body">
        <div class="left w50 mobi">
          <div class="body-head">
            <div class="search">
              <form name="frm" id="frm" method="post" action="/qna">
                <input name="cp" type="hidden" value="${cp}">
                <input type="text" name="sTitle" placeholder="제목 입력">
                <i class="xi-search"></i>
              </form>
            </div>
          </div>
          <table cellspacing="0" class="update">
			<colgroup>
				<col width="5%">
				<col width="*">
				<col width="10%">
				<col width="10%">
				<col width="22%">
				<col width="10%">
			</colgroup>
			<thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>이름</th>
                <th>휴대폰 번호</th>
                <th>이메일</th>
                <th>작성일</th>
              </tr>
            </thead>
            <tbody>
           	  
           	  <c:if test="${empty qnaList}">
				<tr>
					<td id="idEmptyList" colspan="6">등록된 내용이 없습니다.</td>
				</tr>
			  </c:if>
			  
			  <c:forEach var="result" items="${qnaList}" varStatus="status">
			  <tr>
                <td>
	                <c:choose>
	              	<c:when test="${cp eq 1}">${tc - (status.index)}</c:when>
	              	<c:otherwise>${(tc - ((cp-1)*cpp)) - (status.index)}</c:otherwise>
	              	</c:choose>
                </td>
                <td class="tl"><a href="qnaDet?qnaId=${result.qna_id}" target="frameview" class="title">${result.title}</a><a></a></td>
                <td>${result.customer}</td>
                <td>${result.phone}</td>
                <td class="tl">${result.email}</td class="tl">
                <td>${result.reg_date}</td>
              </tr>
			  </c:forEach> 
            
            </tbody>
          </table>
          
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
          </div>
        </div>
        <div class="right fr w48 mobi">
          <iframe src="" id="frameview" name="frameview" width="100%" height="650px" frameborder="0" marginheight="0" marginwidth="0">

          </iframe>
        </div>
        <div style="clear:both"></div>
      </div>
    </div>

  <div>
</body>

 
</html>