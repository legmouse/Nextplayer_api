package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.response.player.*;
import kr.co.nextplayer.base.front.service.PlayerService;
import kr.co.nextplayer.base.player.dto.PlayerReqDto;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "player")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class PlayerController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private PlayerService playerService;

    @ApiOperation(value = "대표팀 목록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "rosterType", value = "0: 국가대표, 1: 골든에이지", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", value = "국가대표일 때만", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "type", value = "골든이에지 0: 지역센터, 1: 5대광역, 2: 합동광역, 3: 영재센터", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "curPage", dataTypeClass = Integer.class, defaultValue = "1"),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", dataTypeClass = String.class),
    })
    @GetMapping( "/rosterList")
    public ResponseDto rosterList(@RequestParam(required = false) String sYear, @RequestParam String rosterType,
                                  @RequestParam(required = false) String ageGroup, @RequestParam(required = false) String type,
                                  @RequestParam(required = false) String sKeyword, @RequestParam int curPage) {



        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .sYear(sYear)
            .rosterType(rosterType)
            .ageGroup(ageGroup)
            .type(type)
            .curPage(curPage)
            .sKeyword(sKeyword)
            .build();

        RosterResponse rosterResponse = playerService.getRosterList(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(rosterResponse).build();

        return result;
    }

    @ApiOperation(value = "국가대표팀 목록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/nationalRosterList")
    public ResponseDto nationalRosterList() {
        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .build();

        NationalRosterResponse rosterResponse = playerService.getNationalRosterList(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(rosterResponse).build();

        return result;
    }

    @ApiOperation(value = "골든에이지 목록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/goldenAgeRosterList")
    public ResponseDto goldenAgeRosterList() {
        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .build();

        GoldenAgeRosterResponse rosterResponse = playerService.getGoldenAgeRosterList(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(rosterResponse).build();

        return result;
    }

    @ApiOperation(value = "대표팀 선수 목록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "rosterType", value = "0: 국가대표, 1: 골든에이지", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", value = "국가대표일 때만", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "type", value = "골든에이지 0: 지역센터, 1: 5대광역, 2: 합동광역, 3: 영재센터", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "sKeyword", value = "이름 입력", dataTypeClass = String.class, required = false),
    })
    @GetMapping( "/rosterPlayerList")
    public ResponseDto rosterPlayerList(@RequestParam(required = false) String sYear, @RequestParam String rosterType,
                                        @RequestParam(required = false) String ageGroup, @RequestParam(required = false) String type,
                                        @RequestParam(required = false) String sKeyword, @RequestParam(required = true, defaultValue = "1") int curPage, @RequestParam(required = true, defaultValue = "10") int pageSize) {

        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .sYear(sYear)
            .rosterType(rosterType)
            .ageGroup(ageGroup)
            .type(type)
            .sKeyword(sKeyword)
            .curPage(curPage)
            .pageSize(pageSize)
            .build();

        RosterPlayerResponse rosterResponse = playerService.getRosterPlayerList(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(rosterResponse).build();

        return result;
    }

    @ApiOperation(value = "대표팀 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "rosterId", dataTypeClass = String.class),
    })
    @GetMapping( "/rosterDetail")
    public ResponseDto rosterDetail(@RequestParam String rosterId) {

        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .rosterId(rosterId)
            .build();

        RosterInfoResponse rosterResponse = playerService.getRosterInfo(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(rosterResponse).build();

        return result;
    }

    @ApiOperation(value = "선수 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "playerId", dataTypeClass = String.class),
    })
    @GetMapping( "/playerDetail")
    public ResponseDto playerDetail(@RequestParam String playerId) {

        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .playerId(playerId)
            .build();

        PlayerInfoResponse playerResponse = playerService.getPlayerInfo(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(playerResponse).build();

        return result;
    }

    @ApiOperation(value = "선수 대표팀 소집 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "playerId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "rosterType", value ="0: 국가대표, 1: 골든에이지", dataTypeClass = String.class),
    })
    @GetMapping( "/playerJoinRoster")
    public ResponseDto playerJoinRoster(@RequestParam String playerId, @RequestParam String rosterType) {

        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .playerId(playerId)
            .rosterType(rosterType)
            .build();

        PlayerJoinRosterResponse playerResponse = playerService.getPlayerJoinRosterInfo(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(playerResponse).build();

        return result;
    }

    @ApiOperation(value = "축구 정보 페이지 대표팀 정보 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping( "/rosterInfoPageList")
    public ResponseDto infoPageRosterList() {

        PlayerReqDto playerReqDto = PlayerReqDto.builder()
            .build();

        RosterInfoPageResponse playerResponse = playerService.getRosterInfoPageList(playerReqDto);

        ResponseDto result = ResponseDto.builder().data(playerResponse).build();

        return result;
    }

}
