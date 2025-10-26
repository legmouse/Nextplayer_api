package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.response.GameResponse;
import kr.co.nextplayer.base.front.response.cup.CupSearchMatchResponse;
import kr.co.nextplayer.base.front.response.league.LeagueSearchMatchResponse;
import kr.co.nextplayer.base.front.service.CupService;
import kr.co.nextplayer.base.front.service.GameService;
import kr.co.nextplayer.base.front.service.LeagueService;
import kr.co.nextplayer.base.front.util.DateUtil;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.front.util.DateUtil.YYYYMMDD;

@Slf4j
@Api(tags = "game")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class GameController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private GameService gameService;

    @ApiOperation(value = "경기 검색")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sDate", value="검색 일자", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "searchType", value="검색 타입(전체: All, 대회 : Contest, 리그 : League)", dataTypeClass = String.class),
    })
    @GetMapping( "/searchGame")
    public ResponseDto searchGame(@RequestParam String ageGroup, @RequestParam(required = false) String sDate, @RequestParam String searchType) {

        ResponseDto result = ResponseDto.builder().build();

        if (StrUtil.isEmpty(sDate)) {
            sDate = DateUtil.getCurrentDate(YYYYMMDD);
        }

        if (searchType.equals("All")) {
            String cupTeamTB = ageGroup + "_Cup_Team";
            String cupInfoTB = ageGroup + "_Cup_Info";
            String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
            String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
            String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
            String cupResultTB = ageGroup + "_Cup_Result";

            Cup cupParam = Cup.builder()
                .sDate(sDate)
                .cupTeamTB(cupTeamTB)
                .cupInfoTB(cupInfoTB)
                .cupSubMatchTB(cupSubMatchTB)
                .cupMainMatchTB(cupMainMatchTB)
                .cupTourMatchTB(cupTourMatchTB)
                .cupResultTB(cupResultTB)
                .ageGroup(ageGroup)
                .build();


            String leagueTeamTB = ageGroup + "_League_Team";
            String leagueInfoTB = ageGroup + "_League_Info";
            String leagueMatchTB = ageGroup + "_League_Match";
            String leagueResultTB = ageGroup + "_League_Result";

            League leagueParam = League.builder()
                .sDate(sDate)
                .ageGroup(ageGroup)
                .leagueTeamTB(leagueTeamTB)
                .leagueInfoTB(leagueInfoTB)
                .leagueMatchTB(leagueMatchTB)
                .leagueResultTB(leagueResultTB)
                .build();

            GameResponse gameResponse = gameService.getAllMatchList(cupParam, leagueParam, ageGroup);

            result.setData(gameResponse);
        }

        if (searchType.equals("Contest")) {

            String cupTeamTB = ageGroup + "_Cup_Team";
            String cupInfoTB = ageGroup + "_Cup_Info";
            String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
            String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
            String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
            String cupResultTB = ageGroup + "_Cup_Result";

            Cup cupParam = Cup.builder()
                .sDate(sDate)
                .cupTeamTB(cupTeamTB)
                .cupInfoTB(cupInfoTB)
                .cupSubMatchTB(cupSubMatchTB)
                .cupMainMatchTB(cupMainMatchTB)
                .cupTourMatchTB(cupTourMatchTB)
                .cupResultTB(cupResultTB)
                .ageGroup(ageGroup)
                .build();

            CupSearchMatchResponse cupSearchData = cupService.getSearchMatch(cupParam);
            result.setData(cupSearchData);
        }

        if (searchType.equals("League")) {

            String leagueTeamTB = ageGroup + "_League_Team";
            String leagueInfoTB = ageGroup + "_League_Info";
            String leagueMatchTB = ageGroup + "_League_Match";
            String leagueResultTB = ageGroup + "_League_Result";

            League leagueParam = League.builder()
                .sDate(sDate)
                .ageGroup(ageGroup)
                .leagueTeamTB(leagueTeamTB)
                .leagueInfoTB(leagueInfoTB)
                .leagueMatchTB(leagueMatchTB)
                .leagueResultTB(leagueResultTB)
                .build();

            LeagueSearchMatchResponse leagueSearchData = leagueService.getLeagueSearchMatch(leagueParam);
            result.setData(leagueSearchData);
        }


        return result;
    }


    @ApiOperation(value = "플레이 데이타")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "matchId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "gameType", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "cupCategory", dataTypeClass = String.class, required = false)
    })
    @GetMapping( "/matchPlayData")
    public ResponseDto cupMatchPlayData(@RequestParam String matchId, @RequestParam String ageGroup, @RequestParam String gameType, @RequestParam(required = false) String cupCategory) {

        return ResponseDto.builder()
            .data(gameService.getMatchPlayData(matchId, ageGroup, gameType, cupCategory))
            .build();
    }

}
