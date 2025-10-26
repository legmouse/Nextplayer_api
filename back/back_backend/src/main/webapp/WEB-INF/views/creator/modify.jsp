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
<script>

    $(document).ready(function() {
        $(".eBtnFileDel").on("click", function() {
            let str = "";
            if($(this).attr('data-fileType') === 'profileFile') {
                str += '<input type="file" id="profileImg" name="profileImg" style="width:50%;">';
                $(".profileTd").append(str);
                $("#delProfileFileImg").val($(this).attr('data-fileId'));
            } else {
                str += '<input type="file" id="bannerImg" name="bannerImg" style="width:50%;">';
                $(".bannerTd").append(str);
                $("#delBannerFileImg").val($(this).attr('data-fileId'));
            }

            if($(this).data('filesn')){
                frm.append('<input type="text" name="delFileVO['+$('[name$=".fileSeq"]').length+'].fileSeq" value="'+$(this).data('filesn')+'" />');
            }
            $(this).remove();
        })
    });

    const saveCreator = () => {
        const method = "${method}";
        const mediaType = "${mediaType}";

        const useFlag = $("input[name='ra1']:checked").val();
        const creatorName = $("input[name=creatorName]").val();
        const urlLink = $("input[name=urlLink]").val();
        const channelId = $("input[name=channelId]").val();

        const profileImg = $("#profileImg")[0];
        const bannerImg = $("#bannerImg")[0];

        const delProfileFile = $("#delProfileFileImg").val();
        const delBannerFile = $("#delBannerFileImg").val();

        if (!creatorName) {
            alert('크리에이터 명을 입력 해주세요');
            return false;
        }

        let formData = new FormData();
        if (profileImg) {
            formData.append("profileImg", profileImg.files[0]);
        }
        if (bannerImg) {
            formData.append("bannerImg", bannerImg.files[0]);
        }
        formData.append("method", method);
        formData.append("useFlag", useFlag);
        formData.append("creatorName", creatorName);
        formData.append("urlLink", urlLink);
        formData.append("channelId", channelId);

        formData.append("delProfileFile", delProfileFile);
        formData.append("delBannerFile", delBannerFile);

        if (method == "modify") {
            const creatorId = "${creatorInfo.creator_id}";
            formData.append('creatorId', creatorId);
        }

        $.ajax({
            type : 'POST',
            url : '/saveCreator' + mediaType,
            processData: false,
            contentType: false,
            dataType: 'json',
            data : formData,
            success : function(res) {
                if (res.state == 'success') {
                    alert(method == "modify" ? "수정 되었습니다." : "등록 되었습니다.");
                    location.href = "/creator" + res.mediaType;
                } else {
                    alert("실패 했습니다.");
                    location.href = "/creator" + res.mediaType;
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
                    <h2>
                        <span></span>
                        크리에이터 관리
                    </h2>
                </div>
            </div>
            <div class="round body">

                <div class="body-head">
                    <h4 class="view-title">
                        <c:choose>
                            <c:when test="${method eq 'regist'}">
                                크리에이터 관리 > 등록하기
                            </c:when>
                            <c:otherwise>
                                크리에이터 관리 > 수정하기
                            </c:otherwise>
                        </c:choose>
                    </h4>
                </div>

                <div class="scroll">
                    <table cellspacing="0" class="update view">
                        <colgroup>
                            <col width="20%">
                            <col width="*">
                        </colgroup>
                        <tbody>
                            <tr>
                                <th class="tl">활성 / 비활성</th>
                                <c:choose>
                                    <c:when test="${method eq 'modify'}">
                                        <td class="tl">
                                            <input type="radio" name="ra1" id="ra1-1" value="0" <c:if test="${creatorInfo.use_flag eq '0'}">checked</c:if>><label for="ra1-1">활성</label>
                                            <input type="radio" name="ra1" id="ra1-2" value="1" <c:if test="${creatorInfo.use_flag eq '1'}">checked</c:if>><label for="ra1-2">비활성</label>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="tl">
                                            <input type="radio" name="ra1" id="ra1-1" value="0"><label for="ra1-1">활성</label>
                                            <input type="radio" name="ra1" id="ra1-2" value="1"><label for="ra1-2">비활성</label>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <th class="tl">크리에이터 명</th>
                                <c:choose>
                                    <c:when test="${method eq 'modify'}">
                                        <td class="tl">
                                            <input type="text" name="creatorName" value="${creatorInfo.creator_name}">
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="tl">
                                            <input type="text" name="creatorName" value="">
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <c:choose>
                                <c:when test="${method eq 'modify'}">
                                    <tr>
                                        <th class="tl">이미지</th>
                                        <c:choose>
                                            <c:when test="${!empty profileImg}">
                                                <td class="tl profileTd">
                                                    <a class="btn-large gray-o x eBtnFileDel" data-fileType="profileFile" data-fileId="${profileImg.public_file_id}">
                                                        <img src='/resources/img/icon/${profileImg.file_ext}.gif' alt="" /> ${profileImg.file_name}
                                                    </a>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="file" id="profileImg" name="profileImg" style="width:50%;">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr>
                                        <th class="tl">배너</th>
                                        <c:choose>
                                            <c:when test="${!empty bannerImg}">
                                                <td class="tl bannerTd">
                                                    <a class="btn-large gray-o x eBtnFileDel" data-fileType="bannerFile" data-fileId="${bannerImg.public_file_id}">
                                                        <img src='/resources/img/icon/${bannerImg.file_ext}.gif' alt="" /> ${bannerImg.file_name}
                                                    </a>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="tl">
                                                    <input type="file" id="bannerImg" name="bannerImg" style="width:50%;">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <th class="tl">이미지</th>
                                        <td class="tl">
                                            <input type="file" id="profileImg" name="profileImg" style="width:50%;">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="tl">배너</th>
                                        <td class="tl">
                                            <input type="file" id="bannerImg" name="bannerImg" style="width:50%;">
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            <tr>
                                <th class="tl">URL</th>
                                <c:choose>
                                    <c:when test="${method eq 'modify'}">
                                        <td class="tl">
                                            <input type="text" name="urlLink" placeholder="" value="${creatorInfo.url_link}">
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="tl">
                                            <input type="text" name="urlLink" placeholder="" value="">
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <th class="tl">채널 ID</th>
                                <c:choose>
                                    <c:when test="${method eq 'modify'}">
                                        <td class="tl">
                                            <input type="text" name="channelId" placeholder="" value="${creatorInfo.channel_id}">
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="tl">
                                            <input type="text" name="channelId" placeholder="" value="">
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </tbody>
                    </table>
                </div><br>
                <div class="tr w100">
                    <a class="btn-large gray-o" onclick="location.href='/creator${mediaType}'">취소 하기</a>
                    <c:choose>
                        <c:when test="${method eq 'regist'}">
                            <a class="btn-large default" onclick="saveCreator('save')">저장 하기</a>
                        </c:when>
                        <c:when test="${method eq 'modify'}">
                            <a class="btn-large default" onclick="saveCreator('modify')">수정 하기</a>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
        <input type="hidden" id="delProfileFileImg" name="profileImg" style="width:50%;">
        <input type="hidden" id="delBannerFileImg" name="profileImg" style="width:50%;">
    </div>

</body>


</html>