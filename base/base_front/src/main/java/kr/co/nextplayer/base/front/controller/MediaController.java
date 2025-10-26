package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.response.media.*;
import kr.co.nextplayer.base.front.response.media.MediaDetailListResponse;
import kr.co.nextplayer.base.front.service.MediaService;
import kr.co.nextplayer.base.front.util.DateUtil;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.media.dto.GameReqDto;
import kr.co.nextplayer.base.media.dto.MediaReqDto;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.front.util.DateUtil.YYYY;

@Slf4j
@Api(tags = "media")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class MediaController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private MediaService mediaService;

    @ApiOperation(value = "미디어 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "mediaType", value = "미디어 타입(Video, News, Blog, Interview)", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "subType", value = "서브 타입(Video: 0-경기, 1-기술, 2-멘탈, 3-재활, 4-기타, News: 0-경기, 1-대표팀, 2-칼럼, 3-인터뷰, Blog: 0-경험, 1-멘탈, 2-정보, 3-칼럼)", dataTypeClass = String.class, required = false),
    })
    @GetMapping( "/mediaList")
    public ResponseDto mediaList (
        @RequestParam String mediaType
        , @RequestParam(required = false) String sKeyword
        , @RequestParam(required = false, defaultValue = "0") String subType
        , @RequestParam(required = false) String ageGroup
        , @RequestParam(required = false) String creatorId
        , @RequestParam(required = true, defaultValue = "1") int curPage
        , @RequestParam(required = true, defaultValue = "10") int pageSize) {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .mediaType(mediaType)
            .sKeyword(sKeyword)
            .subType(subType)
            .ageGroup(ageGroup)
            .creatorId(creatorId)
            .curPage(curPage)
            .pageSize(pageSize)
            .build();

        MediaResponse mediaResponse = mediaService.getMediaList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(mediaResponse).build();

        return result;
    }

    @ApiOperation(value = "경기영상 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "matchType", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sDate", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "sType", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/gameList")
    public ResponseDto gameList(
        @RequestParam(required = false) String sKeyword
        , @RequestParam(required = true, defaultValue = "Cup") String matchType
        , @RequestParam(required = true, defaultValue = "U18") String ageGroup
        , @RequestParam(required = false) String sDate
        , @RequestParam(required = false) String sType
        , @RequestParam(required = true, defaultValue = "1") int curPage
        , @RequestParam(required = true, defaultValue = "10") int pageSize
    ) {

        if (StrUtil.isEmpty(sDate)) {
            sDate = DateUtil.getCurrentDate(YYYY);
        }

        GameReqDto gameReqDto = GameReqDto.builder()
            .gameType(matchType)
            .sKeyword(sKeyword)
            .ageGroup(ageGroup)
            .sDate(sDate)
            .sType(sType)
            .curPage(curPage)
            .pageSize(pageSize)
            .build();

        GameMediaResponse mediaResponse = mediaService.getGameMediaList(gameReqDto);

        ResponseDto result = ResponseDto.builder().data(mediaResponse).build();

        return result;
    }

    @ApiOperation(value = "비디오 미디어 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/videoMediaList")
    public ResponseDto videoMediaList() {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .build();

        VideoMediaResponse videoMediaResponse = mediaService.getVideoMediaList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(videoMediaResponse).build();

        return result;
    }

    @ApiOperation(value = "인터넷 뉴스` 미디어 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/newsMediaList")
    public ResponseDto newsMediaList() {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .build();

        NewsMediaResponse newsMediaResponse = mediaService.getNewsMediaList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(newsMediaResponse).build();

        return result;
    }

    @ApiOperation(value = "블로그 미디어 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/blogMediaList")
    public ResponseDto blogMediaList() {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .build();

        BlogMediaResponse blogMediaResponse = mediaService.getBlogMediaList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(blogMediaResponse).build();

        return result;
    }

    @ApiOperation(value = "미디어 세부 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "foreignId", value = "대회 id, 리그 id, 팀 id", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sType", value = "검색타입 (Cup, League, Team)", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "mediaType", value = "미디어 타입(Video, News)", dataTypeClass = String.class),
    })
    @GetMapping( "/mediaDetailList")
    public ResponseDto mediaDetailList(@RequestParam String foreignId, @RequestParam String sType,
                                       @RequestParam String mediaType, @RequestParam String ageGroup) {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .foreignId(foreignId)
            .mediaType(mediaType)
            .sType(sType)
            .ageGroup(ageGroup)
            .build();

        MediaDetailListResponse mediaResponse = mediaService.getMediaDetailList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(mediaResponse).build();

        return result;
    }

    @ApiOperation(value = "미디어 팀 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "foreignId", value = "팀 id", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "mediaType", value = "미디어 타입(Video, News)", dataTypeClass = String.class),
    })
    @GetMapping( "/mediaTeamDetailList")
    public ResponseDto mediaTeamDetailList(@RequestParam String foreignId, @RequestParam String mediaType, @RequestParam String ageGroup) {

        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .foreignId(foreignId)
            .mediaType(mediaType)
            .ageGroup(ageGroup)
            .build();

        MediaDetailListResponse mediaResponse = mediaService.getMediaTeamDetailList(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(mediaResponse).build();

        return result;
    }

    @ApiOperation(value = "미디어 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "mediaId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "mediaType", value = "미디어 타입(Video, News)", dataTypeClass = String.class),
    })
    @GetMapping( "/mediaDetail")
    public ResponseDto mediaDetail(@RequestParam String mediaId, @RequestParam String mediaType) {
        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .mediaId(mediaId)
            .mediaType(mediaType)
            .build();

        MediaDetailResponse mediaResponse = mediaService.getMediaDetail(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(mediaResponse).build();

        return result;
    }

    @ApiOperation(value = "미디어 조회수 증가")
    @ApiImplicitParams({

    })
    @PostMapping("/updateViewCnt")
    public ResponseDto updateViewCnt(@RequestParam String mediaId, @RequestParam String mediaType) {
        MediaReqDto mediaReqDto = MediaReqDto.builder()
            .mediaId(mediaId)
            .mediaType(mediaType)
            .build();

        int updateResult = mediaService.updateViewCnt(mediaReqDto);

        ResponseDto result = ResponseDto.builder().data(updateResult).build();

        return result;
    }
}

