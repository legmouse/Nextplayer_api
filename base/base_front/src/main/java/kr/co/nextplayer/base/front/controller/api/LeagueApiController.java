package kr.co.nextplayer.base.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.service.LeagueService;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "league api")
@RestController
@RequestMapping("/back_front/base_front/api/v1/league_service")
@RequiredArgsConstructor
@CrossOrigin
public class LeagueApiController {

    @Resource
    private LeagueService leagueService;

    @ApiOperation(value = "리그 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
    })
    @GetMapping("/leagueList")
    public ResponseDto leagueList(@RequestParam String ageGroup, @RequestParam(name = "sYear", required = false) String sYear) {

        String leagueInfoTB = ageGroup + "_League_Info";

        League leagueParam = League.builder()
            .leagueInfoTB(leagueInfoTB)
            .sYear(sYear)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueList(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "리그 정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
    })
    @GetMapping("/leagueInfo")
    public ResponseDto leagueInfo(@RequestParam String ageGroup, @RequestParam String leagueId) {

        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueInfoTB = ageGroup + "_League_Info";

        League leagueParam = League.builder()
            .leagueTeamTB(leagueTeamTB)
            .leagueInfoTB(leagueInfoTB)
            .leagueId(leagueId)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueInfo(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "리그 참가 팀 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
    })
    @GetMapping("/leagueTeamList")
    public ResponseDto leagueTeamList(@RequestParam String ageGroup, @RequestParam String leagueId) {

        String leagueTeamTB = ageGroup + "_League_Team";

        League leagueParam = League.builder()
            .leagueTeamTB(leagueTeamTB)
            .leagueId(leagueId)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueTeam(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "리그 최종 순위 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
    })
    @GetMapping("/leagueRankList")
    public ResponseDto leagueRankList(@RequestParam String ageGroup, @RequestParam String leagueId) {

        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueResultTB = ageGroup + "_League_Result";

        League leagueParam = League.builder()
            .leagueTeamTB(leagueTeamTB)
            .leagueResultTB(leagueResultTB)
            .leagueId(leagueId)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueFinalRank(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "리그 경기 일정")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
    })
    @GetMapping("/leagueMatchCalendar")
    public ResponseDto leagueMatchCalendar(@RequestParam String ageGroup, @RequestParam String leagueId) {

        String leagueMatchTB = ageGroup + "_League_Match";

        League leagueParam = League.builder()
            .leagueMatchTB(leagueMatchTB)
            .leagueId(leagueId)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueMatchCalendar(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "리그 경기 결과")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
    })
    @GetMapping("/leagueMatchSchedule")
    public ResponseDto leagueMatchSchedule(@RequestParam String ageGroup, @RequestParam String leagueId) {

        String leagueMatchTB = ageGroup + "_League_Match";

        League leagueParam = League.builder()
            .leagueMatchTB(leagueMatchTB)
            .leagueId(leagueId)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueMatchSchedule(leagueParam))
            .build();

        return result;
    }

    @ApiOperation(value = "팀별 리그 정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
    })
    @GetMapping("/leagueMatchScheduleByTeam")
    public ResponseDto leagueMatchScheduleByTeam(@RequestParam String ageGroup, @RequestParam String teamId, @RequestParam(name = "sYear", required = false) String sYear) {

        String leagueMatchTB = ageGroup + "_League_Match";

        League leagueParam = League.builder()
            .leagueMatchTB(leagueMatchTB)
            .teamId(teamId)
            .sYear(sYear)
            .build();

        ResponseDto result = ResponseDto.builder()
            .data(leagueService.selectLeagueMatchScheduleByTeamRenewal(leagueParam))
            .build();

        return result;
    }

}
