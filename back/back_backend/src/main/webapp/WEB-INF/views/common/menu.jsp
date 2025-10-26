<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Cache-Control" content="no-store" />

<script type="text/javascript" src="resources/js/page/define.js"></script>
<c:set var="uri" value="${requestScope['javax.servlet.forward.servlet_path']}" />
<script type="text/javascript">
	<%--
	SESSION_USER = "<%=_USER%>"; 
	EMP_NO = "<%=szEmpNo%>";
	--%>
	
	$(document).ready(function() {
		var reqURI = " ${ pageContext.request.requestURI }";
		var arReqUri = reqURI.split('/');
		var arPageName = arReqUri[arReqUri.length-1].split('.');

        var uri = "${uri}";

		//console.log('-- arPageName : '+ arPageName[0]);
		//setMenuSelected(arPageName[0]);
      console.log('uri : ', uri)
		setMenuSelected(uri.replace("/", ""));
	});
	
	function setMenuSelected(pageName){
		console.log('---- pageName : '+ pageName);
		if(pageName == 'leagueInfo' || pageName == 'leagueTeam' || pageName == 'leagueMatch' || pageName == 'leagueMgrInfo'){
			$('#menu-9').attr('data-stat','ac');
			$('#menu-9').addClass("active");
			$("#nav").find("a").each(function(e){
				var dataId = $(this).data('id');
				if (dataId === 'menu-9') {
					//console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
					$(this).addClass("active");
				}
			});
			return;
		} else if(pageName == 'cupInfo' || pageName == 'cupPrize' || pageName == 'cupTeam' 
				|| pageName == 'cupSubMatch' || pageName == 'cupMainMatch' || pageName == 'cupTourMatch' 
				|| pageName == 'cupMgrSubMatch' || pageName == 'cupMgrMainMatch' || pageName == 'cupMgrTourMatch'){
			
			$('#menu-5').attr('data-stat','ac');
			$('#menu-5').addClass("active");
			$("#nav").find("a").each(function(e){
				var dataId = $(this).data('id');
				if (dataId === 'menu-5') {
					//console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
					$(this).addClass("active");
				}
			});
			return;
			
		} else if(pageName == 'team' || pageName == 'teamMgrDet' || pageName == 'teamMgrDetCup' || pageName == 'teamMgrDetLeague'){
			$('#menu-1').attr('data-stat','ac');
			$('#menu-1').addClass("active");
			$("#nav").find("a").each(function(e){
				var dataId = $(this).data('id');
				if (dataId === 'menu-1') {
					//console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
					$(this).addClass("active");
				}
			});
			return;
		} else if(pageName == 'noticeDetail' || pageName == 'noticeDetail' ||  pageName == 'matchReqDetail' ||
                pageName == 'teamReqDetail' || pageName == 'etcReqDetail' || pageName == 'oneToOneDetail') {
            $('#menu-13').attr('data-stat', 'ac');
            $('#menu-13').addClass("active");
            $("#nav").find("a").each(function(e){
                var dataId = $(this).data('id');
                if (dataId === 'menu-13') {
                  //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                  $(this).addClass("active");
                }
            });
        } else if(pageName == 'memberDetail') {
            $('#menu-19').attr('data-stat', 'ac');
            $('#menu-19').addClass("active");
            $("#nav").find("a").each(function(e){
              var dataId = $(this).data('id');
              if (dataId === 'menu-19') {
                //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                $(this).addClass("active");
              }
            });
        } else if(pageName == 'detailPlayer' || pageName == 'detailRoster' || pageName == 'goldenPlayerList') {
            $('#menu-23').attr('data-stat', 'ac');
            $('#menu-23').addClass("active");
            $("#nav").find("a").each(function(e){
              var dataId = $(this).data('id');
              if (dataId === 'menu-23') {
                //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                $(this).addClass("active");
              }
            });
        } else if(pageName == 'detailVideo' || pageName == 'detailNews' || pageName == 'detailBlog' || pageName == 'detailGame' ||
                  pageName == 'mediaNews' || pageName == 'mediaBlog' || pageName == 'mediaGame' ||
                  pageName == 'creatorVideo' || pageName == 'creatorNews' || pageName == 'creatorBlog' ||
                  pageName == 'creatorDetailVideo' || pageName == 'creatorDetailNews' || pageName == 'creatorDetailBlog') {
            $('#menu-26').attr('data-stat', 'ac');
            $('#menu-26').addClass("active");
            $("#nav").find("a").each(function(e){
              var dataId = $(this).data('id');
              if (dataId === 'menu-26') {
                //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                $(this).addClass("active");
              }
            });
        } else if(pageName == 'detailReference') {
            $('#menu-29').attr('data-stat', 'ac');
            $('#menu-29').addClass("active");
            $("#nav").find("a").each(function(e){
              var dataId = $(this).data('id');
              if (dataId === 'menu-29') {
                //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                $(this).addClass("active");
              }
            });
        } else if(pageName == 'area' || pageName == 'menuAuthModify'){
            $('#menu-30').attr('data-stat', 'ac');
            $('#menu-30').addClass("active");
            $("#nav").find("a").each(function(e){
              var dataId = $(this).data('id');
              if (dataId === 'menu-30') {
                //console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
                $(this).addClass("active");
              }
            });
        }

		$("#nav").find("a").each(function(e){
			var arNames = $(this).prop("href").split("/");
			if(pageName == arNames[arNames.length -1]){
				var lastParentId = $(this).parent().prop('id'); //해상 요소 바로 위 부모 아이디 조회
				console.log('-- parent id : '+ lastParentId);
				
				$('#'+lastParentId).attr('data-stat','ac');
				$('#'+lastParentId).addClass("active");
				
				$("#nav").find("a").each(function(e){
					var dataId = $(this).data('id');
					if (dataId === lastParentId) {
						//console.log('-- data id : '+ dataId + ', lastParentId : '+ lastParentId);
						$(this).addClass("active");
					}
				});
			}
			
		});
	
	}
	
	
	
	
	 
</script>

    <div class="left-area active">
      <div id="nav">
        <div class="head">
          <h1><a href="team">넥스트플레이어</a></h1>
        </div>
		    <div class="foot">
          <div class="user" style="background-image:url(https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Gong_Yoo_%28Sep_2016%29.png/250px-Gong_Yoo_%28Sep_2016%29.png)">
            <span><a href="logout"><i class="xi-lock"></i></a></span>
          </div>
        </div>
        <%--${adminMenuList}--%>
        <div class="body">

          <c:forEach var="item" items="${adminUpperMenuList}" varStatus="status">
            <c:choose>
              <c:when test="${item.upper_menu_id eq 1}">
                <c:set var="menuName" value="학원클럽" />
                <c:set var="menuImg" value="school" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 5}">
                <c:set var="menuName" value="대회" />
                <c:set var="menuImg" value="trophy" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 9}">
                <c:set var="menuName" value="리그" />
                <c:set var="menuImg" value="chart-line" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 13}">
                <c:set var="menuName" value="문의/공지" />
                <c:set var="menuImg" value="forum" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 19}">
                <c:set var="menuName" value="회원" />
                <c:set var="menuImg" value="users" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 23}">
                <c:set var="menuName" value="선수관리" />
                <c:set var="menuImg" value="group" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 26}">
                <c:set var="menuName" value="미디어 관리" />
                <c:set var="menuImg" value="play-circle" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 29}">
                <c:set var="menuName" value="자료실" />
                <c:set var="menuImg" value="server" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 30}">
                <c:set var="menuName" value="설정" />
                <c:set var="menuImg" value="cog" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
                <c:set var="isSystemManager" value="true" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 36}">
                <c:set var="menuName" value="교육관리" />
                <c:set var="menuImg" value="cog" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
                <c:set var="isSystemManager" value="true" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 39}">
                <c:set var="menuName" value="팝업관리" />
                <c:set var="menuImg" value="cog" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
                <c:set var="isSystemManager" value="true" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 41}">
                <c:set var="menuName" value="알림관리" />
                <c:set var="menuImg" value="cog" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
                <c:set var="isSystemManager" value="true" />
              </c:when>
              <c:when test="${item.upper_menu_id eq 45}">
                <c:set var="menuName" value="데이터수집" />
                <c:set var="menuImg" value="cog" />
                <c:set var="upperId" value="${item.upper_menu_id}" />
                <c:set var="isSystemManager" value="true" />
              </c:when>
            </c:choose>
            <c:if test="${item.upper_menu_id > 0}">
              <a data-id="menu-${item.upper_menu_id}">
                <i class="xi-${menuImg}"></i>
                <span>
                    <i class="xi-caret-up-min"></i>
                    ${menuName}
                  </span>
              </a>
              <div class="sub" id="menu-${upperId}">
                <c:forEach var="item2" items="${adminMenuList}" varStatus="status">
                  <c:if test="${upperId eq item2.upper_menu_id}">
                    <a href="${item2.url}">${item2.menu_name}</a>
                  </c:if>
                </c:forEach>
                <c:if test="${upperId eq 30}">
                  <c:if test="${login.grade == 127}">
                    <a href="menuAuth">권한별 메뉴 설정</a>
                    <a href="mainMenuConfig">메인 설정</a>
                    <!-- <a href="alrim">푸시 설정</a> -->
                  </c:if>
                </c:if>
              </div>
            </c:if>
          </c:forEach>

          <a class="google" href="https://analytics.google.com/" target="_blank">
            구글 마케팅
          </a>
        </div>

        <script>
          $("div.left-area div.body a").click(function() {
            var left_id1 = $(this).attr("data-id");
            var left_id2 = $("#"+left_id1).attr("data-statu");
            if(left_id2 >= "ac") { 
              $("#"+left_id1).slideUp().attr("data-statu","");
            }
            else{
              $("#"+left_id1).slideDown().attr("data-statu","ac");
            }
          });
        </script>
      </div>
    </div>


