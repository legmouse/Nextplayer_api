package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.response.HomeResponse;
import kr.co.nextplayer.base.front.response.HomeLeagueResponse;
import kr.co.nextplayer.base.front.response.HomeTodayResponse;
import kr.co.nextplayer.base.front.service.ConfigService;
import kr.co.nextplayer.base.front.service.HomeService;
import kr.co.nextplayer.base.front.service.UageService;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import kr.co.nextplayer.next.lib.common.util.WebFluxIPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "home")
@RestController
@RequestMapping("/back_front/base_front/api/v1/main/")
@RequiredArgsConstructor
@CrossOrigin
public class HomeController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private UageService uageService;

    @Resource
    private HomeService homeService;

    @Resource
    private ConfigService configService;


    @ApiOperation(value = "메인 페이지 메뉴 순서 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainMenuList")
    public ResponseDto mainMenuList() {

        List<HashMap<String, Object>> mainMenuList = configService.selectMainMenuList();

        ResponseDto result = ResponseDto.builder()
            .data(mainMenuList)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 진행중인 대회 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainProgressCupMatch")
    public ResponseDto mainCupData(@RequestParam String ageGroup) {

        HomeResponse bannerData = homeService.getBannerProgressCupList(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(bannerData)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 진행중인 리그 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainProgressLeagueMatch")
    public ResponseDto mainLeagueData(@RequestParam String ageGroup) {

        HomeResponse bannerData = homeService.getBannerProgressLeagueList(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(bannerData)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 당일 대회 경기 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainTodayCupMatch")
    public ResponseDto mainTodayCupData(@RequestParam String ageGroup) {

        HomeTodayResponse bannerData = homeService.getBannerTodayCupList(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(bannerData)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 당일 리그 경기 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainTodayLeagueMatch")
    public ResponseDto mainTodayLeagueData(@RequestParam String ageGroup) {

        HomeTodayResponse bannerData = homeService.getBannerTodayLeagueList(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(bannerData)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 리그 순위 데이터 호출")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainLeagueRank")
    public ResponseDto mainLeagueRankData(@RequestParam String ageGroup) {

        HomeLeagueResponse bannerData = homeService.getBannerLeagueList(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(bannerData)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 페이지 방문자수")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/getHistoryCount")
    public ResponseDto getHistoryCount(ServerHttpRequest request) {

        String clientIp = WebFluxIPUtil.getClientIp(request);
        String userAgent = request.getHeaders().getFirst("User-Agent");

        Map historyCount = homeService.getVisitorsHistoryCount(clientIp, userAgent);

        ResponseDto result = ResponseDto.builder()
            .data(historyCount)
            .build();
        return result;
    }

    @ApiOperation(value = "메인 페이지 방문자수")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/getHistoryCountMinute")
    public ResponseDto getHistoryCountMinute(ServerHttpRequest request) {

        String clientIp = WebFluxIPUtil.getClientIp(request);
        Map historyCount = homeService.getVisitorsHistoryCountMinute(clientIp);

        ResponseDto result = ResponseDto.builder()
            .data(historyCount)
            .build();
        return result;
    }

    @ApiOperation(value = "메인 미디어 데이터")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainMediaData")
    public ResponseDto getVideoMedia() {

        Map mediaData = homeService.getHomeMediaData();

        ResponseDto result = ResponseDto.builder()
            .data(mediaData)
            .build();
        return result;
    }

    @ApiOperation(value = "메인 페이지 관심 연령")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainInterestAge")
    public ResponseDto mainInterestAge() {

        Map resultMap = homeService.getHomeInterestAge();

        ResponseDto result = ResponseDto.builder()
            .data(resultMap)
            .build();

        return result;
    }

    @ApiOperation(value = "메인 경기 영상 데이터")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainGameMediaData")
    public ResponseDto getGameVideoMedia(@RequestParam String ageGroup) {

        Map mediaData = homeService.getHomeGameMediaData(ageGroup);

        ResponseDto result = ResponseDto.builder()
            .data(mediaData)
            .build();
        return result;
    }
    // increase
    @ApiOperation(value = "조회수 증가")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "type", value = "조회수 증가 시킬 요소 (ex: 배너 - Banner ... )", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "primaryKey", value = "조회수 증가 시킬 요소의 id 값 (ex: 배너 - banner_id)", dataTypeClass = String.class, required = true),
    })
    @PutMapping( "/increaseViewCnt")
    public ResponseDto updateViewCnt(@RequestParam String type, @RequestParam String primaryKey) {

        int updateResult = homeService.updateViewCnt(type, primaryKey);

        ResponseDto result = ResponseDto.builder()
            .data(updateResult)
            .build();
        return result;
    }

    @ApiOperation(value = "연령 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainUageList")
    public ResponseDto getUageList() {

        Map<String, Object> mediaData = homeService.getUageList();

        ResponseDto result = ResponseDto.builder()
            .data(mediaData)
            .build();
        return result;
    }

    @ApiOperation(value = "메인 칼럼 데이터")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/mainColumnData")
    public ResponseDto getMainColumn() {

        Map columnData = homeService.getHomeColumnData();

        ResponseDto result = ResponseDto.builder()
            .data(columnData)
            .build();
        return result;
    }

    @PostMapping( "pushClick")
    public ResponseDto registerPushClick(@RequestParam String path) {
        homeService.insertPushClick(path);
        return ResponseDto.builder()
            .build();
    }

}