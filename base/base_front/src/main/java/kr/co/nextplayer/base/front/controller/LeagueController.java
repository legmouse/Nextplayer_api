package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.response.LeagueResponse;
import kr.co.nextplayer.base.front.response.league.LeagueInfoResponse;
import kr.co.nextplayer.base.front.response.league.LeagueListResponse;
import kr.co.nextplayer.base.front.response.league.LeagueMatchResponse;
import kr.co.nextplayer.base.front.service.LeagueService;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "league")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class LeagueController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private LeagueService leagueService;

    @ApiOperation(value = "리그 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/league")
    public ResponseDto league(@RequestParam String ageGroup, @RequestParam(required = false) String sYear) {

        League leagueParam = League.builder()
            .ageGroup(ageGroup)
            .sYear(sYear)
            .build();

        LeagueListResponse leagueListData = leagueService.getLeagueList(leagueParam);

        ResponseDto result = ResponseDto.builder()
            .data(leagueListData)
            .build();

        return result;
    }

    @ApiOperation(value = "리그 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "leagueId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
    })
    @GetMapping( "/leagueInfo")
    public ResponseDto leagueInfo(@RequestParam String leagueId, @RequestParam String ageGroup) {


        League league = League.builder()
            .ageGroup(ageGroup)
            .leagueId(leagueId)
            .build();

        LeagueInfoResponse leagueInfoData = leagueService.getLeagueInfo(league);

        ResponseDto result = ResponseDto.builder()
            .data(leagueInfoData)
            .build();

        return result;
    }

    @ApiOperation(value = "리그 상세")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        //@ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/leagueMatch")
    public ResponseDto leagueMatch(@RequestParam String leagueId, @RequestParam String ageGroup) {

        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";
        String leagueMatchPlayDataTB = ageGroup + "_League_Match_Play_Data";
        String leagueMatchChangeDataTB = ageGroup + "_League_Match_Change_Data";

        League leagueParam = League.builder()
            .ageGroup(ageGroup)
            .leagueId(leagueId)
            .leagueTeamTB(leagueTeamTB)
            .leagueInfoTB(leagueInfoTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .leagueMatchPlayDataTB(leagueMatchPlayDataTB)
            .leagueMatchChangeDataTB(leagueMatchChangeDataTB)
            .build();

        LeagueMatchResponse leagueListData = leagueService.getLeagueMatch(leagueParam);

        leagueListData.setAgeGroup(ageGroup);
        //leagueListData.setSYear(sYear);

        ResponseDto result = ResponseDto.builder()
            .data(leagueListData)
            .build();

        return result;
    }

}