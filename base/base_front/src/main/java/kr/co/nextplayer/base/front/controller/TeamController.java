package kr.co.nextplayer.base.front.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.TeamResponse;
import kr.co.nextplayer.base.front.response.team.*;
import kr.co.nextplayer.base.front.service.TeamService;
import kr.co.nextplayer.base.front.service.UageService;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.team.dto.CommonTeamDto;
import kr.co.nextplayer.base.team.dto.TeamDto;
import kr.co.nextplayer.base.team.model.IndirectMatches;
import kr.co.nextplayer.base.team.model.IndirectMatchesSqlResultSet;
import kr.co.nextplayer.base.team.model.Match;
import kr.co.nextplayer.base.team.model.Team;
import kr.co.nextplayer.next.lib.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Api(tags = "team")
@RestController
@RequestMapping("/back_front/base_front/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class TeamController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private UageService uageService;

    @Resource
    private TeamService teamService;

    @ApiOperation(value = "팀 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/team")
    public ResponseDto team(@RequestParam String ageGroup) {

        ResponseDto result = ResponseDto.builder()
            .data(teamService.getTeamList(ageGroup))
            .build();

        return result;
    }

    @ApiOperation(value = "팀 광역 리스트")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "areaId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "teamType", dataTypeClass = String.class, required = false),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/teamArea")
    public ResponseDto teamArea(@RequestParam String ageGroup, @RequestParam(required = false) String areaId,
                                @RequestParam(required = false) String sNickName, @RequestParam(required = false) String teamType, @RequestParam(required = false) String sYear) {

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupResultTB = ageGroup + "_Cup_Result";

        List<HashMap<String, Uage>> areaList = uageService.selectAreaByUageList(ageGroup);

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            .areaId(areaId)
            .sNickName(sNickName)
            .teamType(teamType)
            .sYear(sYear)
            .cupInfoTB(cupInfoTB)
            .cupTeamTB(cupTeamTB)
            .cupResultTB(cupResultTB)
            .leagueInfoTB(leagueInfoTB)
            .leagueTeamTB(leagueTeamTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .build();

        TeamAreaResponse teamResponse = teamService.getAreaTeamList(teamDto);
        teamResponse.setAreaList(areaList);

        ResponseDto result = ResponseDto.builder()
            .data(teamResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "팀 요약정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
    })
    @GetMapping("/teamInfo")
    public ResponseDto teamInfo(@RequestParam String ageGroup, @RequestParam String teamId, @RequestParam(required = false) String sYear) {

        if (StrUtil.isEmpty(teamId)) {
            throw new NullPointerException("team id is null");
        }

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        CommonTeamDto teamInfo = teamService.selectTeamInfo(teamId);

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            .teamId(teamId)
            .sYear(sYear)
            .pId(teamInfo.getPId())
            .leagueInfoTB(leagueInfoTB)
            .leagueTeamTB(leagueTeamTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .cupInfoTB(cupInfoTB)
            .cupTeamTB(cupTeamTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .teamGroupId(teamInfo.getTeam_group_id())
            .build();

        TeamInfoResponse teamResponse = teamService.getTeamInfo(teamDto);
        teamResponse.setTeamInfoMap(teamInfo);

        ResponseDto result = ResponseDto.builder()
            .data(teamResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "팀 대회 정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/teamInfoCup")
    public ResponseDto teamInfoCup(@RequestParam String ageGroup, @RequestParam String teamId, @RequestParam(required = false) String sYear) {

        if (StrUtil.isEmpty(teamId)) {
            throw new NullPointerException("team id is null");
        }

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        CommonTeamDto teamInfoMap = teamService.selectTeamInfo(teamId);

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            .teamId(teamId)
            .sYear(sYear)
            .pId(teamInfoMap.getPId())
            .leagueInfoTB(leagueInfoTB)
            .leagueTeamTB(leagueTeamTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .cupInfoTB(cupInfoTB)
            .cupTeamTB(cupTeamTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .build();

        TeamCupInfoResponse teamResponse = teamService.getTeamInfoCup(teamDto);
        teamResponse.setTeamInfoMap(teamInfoMap);

        ResponseDto result = ResponseDto.builder()
            .data(teamResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "팀 리그 정보")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/teamInfoLeague")
    public ResponseDto teamInfoLeague(@RequestParam String ageGroup, @RequestParam String teamId, @RequestParam(required = false) String sYear) {

        if (StrUtil.isEmpty(teamId)) {
            throw new NullPointerException("team id is null");
        }

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        CommonTeamDto teamInfoMap = teamService.selectTeamInfo(teamId);

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            .teamId(teamId)
            .sYear(sYear)
            .pId(teamInfoMap.getPId())
            .leagueInfoTB(leagueInfoTB)
            .leagueTeamTB(leagueTeamTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .cupInfoTB(cupInfoTB)
            .cupTeamTB(cupTeamTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .build();

        TeamLeagueInfoResponse teamResponse = teamService.getTeamInfoLeague(teamDto);
        teamResponse.setSYear(sYear);
        teamResponse.setAgeGroup(ageGroup);
        teamResponse.setTeamInfoMap(teamInfoMap);

        ResponseDto result = ResponseDto.builder()
            .data(teamResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "경기 상세 페이지")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        //@ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "homeTeamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "awayTeamId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class, required = false),
    })
    @GetMapping("/teamCompareInfo")
    public ResponseDto teamCompareInfo(@RequestParam String ageGroup, //@RequestParam String teamId,
                                       @RequestParam String homeTeamId, @RequestParam String awayTeamId, @RequestParam(required = false) String sYear) {

        if(StrUtil.isEmpty(homeTeamId) || StrUtil.isEmpty(awayTeamId)) {
            throw new NullPointerException("home team id is null OR away team id is null");
        }

        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        //CommonTeamDto teamInfoMap = teamService.selectTeamInfo(teamId);

        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        String cupResultTB = ageGroup + "_Cup_Result";

        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueTeamTB = ageGroup + "_League_Team";
        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueResultTB = ageGroup + "_League_Result";

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            //.teamId(teamId)
            .homeTeamId(homeTeamId)
            .awayTeamId(awayTeamId)
            .sYear(sYear)
            .leagueInfoTB(leagueInfoTB)
            .leagueTeamTB(leagueTeamTB)
            .leagueMatchTB(leagueMatchTB)
            .leagueResultTB(leagueResultTB)
            .cupInfoTB(cupInfoTB)
            .cupTeamTB(cupTeamTB)
            .cupSubMatchTB(cupSubMatchTB)
            .cupMainMatchTB(cupMainMatchTB)
            .cupTourMatchTB(cupTourMatchTB)
            .cupResultTB(cupResultTB)
            .build();

        TeamCompareResponse teamResponse = teamService.getTeamCompareInfo(teamDto);
        //teamResponse.setTeamInfoMap(teamInfoMap);

        ResponseDto result = ResponseDto.builder()
            .data(teamResponse)
            .build();

        return result;
    }

    @ApiOperation(value = "진학 정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
    })
    @GetMapping("/teamEnter")
    public ResponseDto teamEnterInfo(@RequestParam String teamId, @RequestParam String ageGroup, @RequestParam String sYear) {

        TeamDto teamDto = TeamDto.builder()
            .teamId(teamId)
            .ageGroup(ageGroup)
            .sYear(sYear)
            .build();

        TeamEnterResponse enterResponse = teamService.getTeamEnterInfo(teamDto);

        ResponseDto result = ResponseDto.builder().data(enterResponse).build();

        return result;
    }

    @ApiOperation(value = "출신 선수 정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "teamId", dataTypeClass = String.class),
        @ApiImplicitParam(paramType = "query", name = "sYear", dataTypeClass = String.class),
        //@ApiImplicitParam(paramType = "query", name = "rosterType", value="0 : 국가대표, 1: 골든에이지", dataTypeClass = String.class),
    })
    @GetMapping("/teamRoster")
    public ResponseDto teamRosterInfo(@RequestParam String teamId, @RequestParam String sYear) {

        TeamDto teamDto = TeamDto.builder()
            .teamId(teamId)
            .sYear(sYear)
            //.rosterType(rosterType)
            .build();

        TeamRosterResponse enterResponse = teamService.getTeamRosterInfo(teamDto);

        ResponseDto result = ResponseDto.builder().data(enterResponse).build();

        return result;
    }

    @ApiOperation(value = "팀 검색 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
        @ApiImplicitParam(paramType = "query", name = "ageGroup", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/teamSearch")
    public ResponseDto searchTeam(@RequestParam String ageGroup,
                                  @RequestParam(required = true, defaultValue = "1") int curPage,
                                  @RequestParam(required = true, defaultValue = "20") int pageSize,
                                  @RequestParam(required = false) String sKeyword) {

        TeamDto teamDto = TeamDto.builder()
            .ageGroup(ageGroup)
            .curPage(curPage)
            .pageSize(pageSize)
            .sKeyword(sKeyword)
            .build();

        TeamSearchResponse data = teamService.getTeamSearchList(teamDto);

        ResponseDto result = ResponseDto.builder()
            .data(data)
            .build();

        return result;
    }

    @ApiOperation(value = "간접전적")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/getIndirectMatches")
    public ResponseDto getIndirectMatches(@RequestParam String inTeamId,
                                  @RequestParam String inAnTeamId,
                                  @RequestParam String inSYear,
                                  @RequestParam String uage) {

        List<IndirectMatches> indirectMatches = teamService.getIndirectMatches(inTeamId, inAnTeamId, inSYear, uage);

        ResponseDto result = ResponseDto.builder()
            .data(indirectMatches)
            .build();

        return result;
    }

    @ApiOperation(value = "관심등록 된 팀 정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "clientId", dataTypeClass = String.class, required = true),
    })
    @GetMapping("/getInterestTeamInfo")
    public ResponseDto getIterestTeamInfo(@RequestParam String inTeamId) {

        TeamInterestInfoResponse data = teamService.getInterestTeamData(inTeamId);

        ResponseDto result = ResponseDto.builder()
            .data(data)
            .build();

        return result;
    }

}
