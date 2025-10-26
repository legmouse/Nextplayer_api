$(document).ready(function(){

  $(".btn-pop").click(function() {
    $(".pop").hide();
    var currentpopid = $(this).attr("data-id");
    $("#"+currentpopid).fadeIn();
  });
  $(".btn-close-pop").click(function() {
    $(".pop").fadeOut();
  });

  $( function() {
    $( ".datepicker" ).datepicker({
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
  } );


  $(".btn-show").click(function() {
    var btn_show = $(this).attr("data-id");
    $(".show-area").hide();
    $("#"+btn_show).show();
  });
  $(".layer-pop").click(function() {
    var layer_show = $(this).attr("data-id");
    $(".table-layer").hide();
    $("#"+layer_show).show();
  });
  $(".layer-close").click(function() {
    $(".table-layer").hide();
  });
  $(".btn-tab").click(function() {
    var btn_tab = $(this).attr("data-id");
    $(".btn-tab").removeClass("default").addClass("gray-o");
    $(this).addClass("default").removeClass("gray-o");

    $(".show-tab").removeClass("ac");
    $("#"+btn_tab).addClass("ac");
  });
  
  $(".btn-open").click(function() {
    var btn_open = $(this).attr("data-id");
    $(this).removeClass("ac");
    $("#"+btn_open).addClass("ac");
    $("."+btn_open+"-c").addClass("ac");
    $("#"+btn_open+"-other").addClass("ac");
  });
  $(".btn-close").click(function() {
    var btn_close = $(this).attr("data-id");
    $(this).removeClass("ac");
    $("#"+btn_close).removeClass("ac");
    $("."+btn_close+"-o").addClass("ac");
    $("#"+btn_close+"-other").removeClass("ac");
  });
  
  $(".btn-tab2").click(function() {
    var btn_tab2 = $(this).attr("data-id");
    $(".btn-tab2").removeClass("default").addClass("gray-o");
    $(this).addClass("default").removeClass("gray-o");

    $(".show-tab2").removeClass("ac");
    $("#"+btn_tab2).addClass("ac");
  });
  $(".btn-tab3").click(function() {
    var btn_tab3 = $(this).attr("data-id");
    $(".btn-tab3").removeClass("default").addClass("gray-o");
    $(this).addClass("default").removeClass("gray-o");

    $(".show-tab3").removeClass("ac");
    $("#"+btn_tab3).addClass("ac");
  });

  $('#wrapper').scroll(function(){
      var scrollT = $(this).scrollTop(); //스크롤바의 상단위치
      if(scrollT >= "1") { // 스크롤바가 맨 아래에 위치할 때
        $(".header").addClass("active");
      }
      else{
        $(".header").removeClass("active");
      }
      //var scrollH = $(this).height(); //스크롤바를 갖는 div의 높이
      //var contentH = $('#divContent').height(); //문서 전체 내용을 갖는 div의 높이
      //if(scrollT + scrollH >= contentH) { // 스크롤바가 맨 아래에 위치할 때
      //    $('#divContent').append(imgs);
      //}
  });
  /*관리자용*/

   // 쿠키 생성
   function setCookie(cName, cValue, cDay){
        var expire = new Date();
        expire.setDate(expire.getDate() + cDay);
        cookies = cName + '=' + escape(cValue) + '; path=/ ';
        if(typeof cDay != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
        document.cookie = cookies;
   }

   // 쿠키 가져오기
   function getCookie(cName) {
        cName = cName + '=';
        var cookieData = document.cookie;
        var start = cookieData.indexOf(cName);
        var cValue = '';
        if(start != -1){
             start += cName.length;
             var end = cookieData.indexOf(';', start);
             if(end == -1)end = cookieData.length;
             cValue = cookieData.substring(start, end);
        }
        return unescape(cValue);
   }


  if(getCookie('left-area') == "active") {
    $(".left-area").addClass("active");
    $(".contents").addClass("active");
    $(".btn-menu-on-off").addClass("active");
    $(".btn-menu-on-off").attr("data-id","open");
  };
  $(".btn-menu-on-off").click(function() {
    var btn_menu_on_off = $(this).attr("data-id");
    if(btn_menu_on_off == "close") { // 스크롤바가 맨 아래에 위치할 때
      $(".left-area").addClass("active");
      $(".contents").addClass("active");
      $(this).addClass("active");
      $(this).attr("data-id","open");
      setCookie('left-area', 'active', 1)
    }
    else{
      $(".left-area").removeClass("active");
      $(".contents").removeClass("active");
      $(this).removeClass("active");
      $(this).attr("data-id","close");
      setCookie('left-area', '', -1)
    }
  });
  $("table thead th a").click(function() {
    var th_a = $(this).attr("class");
    if(th_a == "active") { // 스크롤바가 맨 아래에 위치할 때
      $(this).removeClass("active");
    }
    else{
      $(this).addClass("active");
    }
  });
  $(function() {
    $("#sortable").sortable();
    $("#sortable").disableSelection();
  });
});
