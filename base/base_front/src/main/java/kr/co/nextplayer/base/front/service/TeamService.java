package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.cup.mapper.CupMapper;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.TeamResponse;
import kr.co.nextplayer.base.front.response.team.*;
import kr.co.nextplayer.base.league.dto.LeagueMatchCalendarDto;
import kr.co.nextplayer.base.league.dto.LeagueMatchDto;
import kr.co.nextplayer.base.league.dto.LeagueResultDto;
import kr.co.nextplayer.base.league.dto.LeagueYearRankDto;
import kr.co.nextplayer.base.league.mapper.LeagueMapper;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.member.util.StringUtils;
import kr.co.nextplayer.base.team.dto.*;
import kr.co.nextplayer.base.team.mapper.TeamMapper;
import kr.co.nextplayer.base.team.model.*;
import kr.co.nextplayer.next.lib.common.mybatis.hander.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TeamService {

    @Resource
    private UageMapper uageMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private CupMapper cupMapper;

    @Resource
    private LeagueMapper leagueMapper;

    public CommonTeamDto selectTeamInfo(String teamId) {
        return teamMapper.selectTeamInfo(teamId);
    }

    public TeamCupLeagueInfoDto selectTeamResultByYear2(TeamDto teamDto) {
        return teamMapper.selectTeamResultByYear2(teamDto);
    }

    public List<CommonTeamDto> selectTeamCupPlayedByYear(TeamDto teamDto) {
        return teamMapper.selectTeamCupPlayedByYear(teamDto);
    }

    public List<TeamResultDto> selectTeamCupAvgGoalByYear(TeamDto teamDto) {
        return teamMapper.selectTeamCupAvgGoalByYear(teamDto);
    }

    public TeamResponse getTeamList(String ageGroup) {

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();
        List<HashMap<String, Uage>> areaList = uageMapper.selectAreaList(ageGroup);

        // 리스폰스 변경 하기
        TeamResponse teamResponse = TeamResponse.builder()
            .uageList(uageList)
            .areaList(areaList)
            .build();

        return teamResponse;
    }

    public TeamAreaResponse getAreaTeamList(TeamDto teamDto) {

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        List<AreaTeamDto> areaTeamList = teamMapper.selectAreaTeamList(teamDto);

        TeamAreaResponse teamResponse = TeamAreaResponse.builder()
            .uageList(uageList)
            .areaTeamList(areaTeamList)
            .build();

        return teamResponse;
    }

    public TeamInfoResponse getTeamInfo(TeamDto teamDto) {

        TeamResultDto teamResultMap = teamMapper.selectTeamResultByYear(teamDto);
        List<TeamResultDto> teamResultMapGraph = teamMapper.selectTeamResultByYearGraph(teamDto);
        List<TeamResultDto> teamAvgGoalList = teamMapper.selectTeamAvgGoal(teamDto);
        List<TeamMatchDto> teamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);

        List<HashMap<String, Object>> teamGroup = teamMapper.selectTeamGroup(teamDto);

        /*if (teamTotalMatchList.size() == 0) {
            String pId = teamDto.getPId();

            if (!pId.equals("0")) {
                teamDto.setTeamId(pId);
            }

            teamResultMap = teamMapper.selectTeamResultByYear(teamDto);
            teamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);

        }*/

        if (!teamDto.getPId().equals("0")) {
            String pId = teamDto.getPId();

            if (!pId.equals("0")) {
                teamDto.setTeamId(pId);
            }

            if (StringUtils.isEmpty(teamResultMap.getTeam_id())) {
                teamResultMap = teamMapper.selectTeamResultByYear(teamDto);
            }

            // 연도별 평균 득/실점 추이
            teamResultMapGraph.addAll(teamMapper.selectTeamResultByYearGraph(teamDto));

            if (teamTotalMatchList.size() == 0) {
                teamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);
            }

        }

        TeamInfoResponse teamResponse = TeamInfoResponse.builder()
            .teamResultMap(teamResultMap)
            .teamResultMapGraph(teamResultMapGraph)
            .teamAvgGoalList(teamAvgGoalList)
            .teamTotalMatchList(teamTotalMatchList)
            .teamGroup(teamGroup)
            .build();

        return teamResponse;
    }

    public TeamCupInfoResponse getTeamInfoCup(TeamDto teamDto) {

        Cup cupDto = Cup.builder()
            .teamId(teamDto.getTeamId())
            .sYear(teamDto.getSYear())
            .cupInfoTB(teamDto.getCupInfoTB())
            .cupSubMatchTB(teamDto.getCupSubMatchTB())
            .cupMainMatchTB(teamDto.getCupMainMatchTB())
            .cupTourMatchTB(teamDto.getCupTourMatchTB())
            .cupResultTB(teamDto.getCupResultTB())
            .build();

        TeamCupLeagueInfoDto teamResultMap = teamMapper.selectTeamResultByYear2(teamDto);
        List<CommonTeamDto> teamCupPlayedList = teamMapper.selectTeamCupPlayedByYear(teamDto);
        List<TeamResultDto> teamCupAvgGoalList = teamMapper.selectTeamCupAvgGoalByYear(teamDto);

        List<TeamCupResultDto> teamCupResultList = cupMapper.selectTeamCupResult(cupDto);
        List<CupSubMatchDto> teamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);

        List<TeamCupResultDto> lastThreeYearList = cupMapper.selectTeamJoinCupLastThreeYear(cupDto);

        /*if(teamCupResultList.size() == 0 && teamCupMatchList.size() == 0) {
            String pId = teamDto.getPId();

            if (!pId.equals("0")) {
                teamDto.setTeamId(pId);
                cupDto.setTeamId(pId);
            }

            teamResultMap = teamMapper.selectTeamResultByYear2(teamDto);
            teamCupResultList = cupMapper.selectTeamCupResult(cupDto);
            teamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);

        }*/

        if (!teamDto.getPId().equals("0")) {
            String pId = teamDto.getPId();

            if (!pId.equals("0")) {
                teamDto.setTeamId(pId);
                cupDto.setTeamId(pId);
            }

            if (teamResultMap.getC_played() == 0) {
                teamResultMap = teamMapper.selectTeamResultByYear2(teamDto);
            }

            if (teamCupResultList.size() == 0) {
                teamCupResultList = cupMapper.selectTeamCupResult(cupDto);
            }

            if (teamCupMatchList.size() == 0) {
                teamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);
            }

            teamCupAvgGoalList.addAll(teamMapper.selectTeamCupAvgGoalByYear(teamDto));
            lastThreeYearList.addAll(cupMapper.selectTeamJoinCupLastThreeYear(cupDto));

        }

        TeamCupInfoResponse teamResponse = TeamCupInfoResponse.builder()
            .teamResultMap(teamResultMap)
            .teamCupPlayedList(teamCupPlayedList)
            .teamCupAvgGoalList(teamCupAvgGoalList)
            .teamCupResultList(teamCupResultList)
            .teamCupMatchList(teamCupMatchList)
            .lastThreeYearList(lastThreeYearList)
            .build();

        return teamResponse;
    }

    public TeamLeagueInfoResponse getTeamInfoLeague(TeamDto teamDto) {

        TeamCupLeagueInfoDto teamResultMap = teamMapper.selectTeamResultByYear2(teamDto);


        League leagueParam = League.builder()
            .ageGroup(teamDto.getAgeGroup())
            .teamId(teamDto.getTeamId())
            .sYear(teamDto.getSYear())
            .leagueInfoTB(teamDto.getLeagueInfoTB())
            .leagueTeamTB(teamDto.getLeagueTeamTB())
            .leagueMatchTB(teamDto.getLeagueMatchTB())
            .leagueResultTB(teamDto.getLeagueResultTB())
            .build();

        List<LeagueResultDto> teamLeagueResultByYearList = leagueMapper.selectTeamLeagueResultByYear(leagueParam);
        List<TeamLeagueEnterDto> teamLeagueEnterList = leagueMapper.selectTeamEnterLeague(leagueParam);
        List<LeagueYearRankDto> teamLeagueRankList = leagueMapper.selectTeamLeagueRankByYear(leagueParam);

        /*if(teamLeagueEnterList.size() != 0) {
            leagueParam.setLeagueId(teamLeagueEnterList.get(0).getLeague_id());
        }*/

        List<LeagueMatchCalendarDto> leagueMatchCalendar = leagueMapper.selectLeagueMatchCalendar(leagueParam);
        List<LeagueMatchDto> leagueMatchSchedule = leagueMapper.selectLeagueMatchScheduleByTeamRenewal(leagueParam);

        if (!teamDto.getPId().equals("0")) {
            String pId = teamDto.getPId();

            if (!pId.equals("0")) {
                teamDto.setTeamId(pId);
                leagueParam.setTeamId(pId);
            }

            if (teamResultMap.getL_played() == 0) {
                teamResultMap = teamMapper.selectTeamResultByYear2(teamDto);
            }

            teamLeagueResultByYearList.addAll(leagueMapper.selectTeamLeagueResultByYear(leagueParam));

            if (teamLeagueEnterList.size() == 0) {
                teamLeagueEnterList = leagueMapper.selectTeamEnterLeague(leagueParam);
            }

            teamLeagueRankList.addAll(leagueMapper.selectTeamLeagueRankByYear(leagueParam));

            if (leagueMatchSchedule.size() == 0) {
                leagueMatchSchedule = leagueMapper.selectLeagueMatchScheduleByTeamRenewal(leagueParam);
            }

        }

        TeamLeagueInfoResponse teamResponse = TeamLeagueInfoResponse.builder()
            .teamResultMap(teamResultMap)
            .teamLeagueResultByYearList(teamLeagueResultByYearList)
            .teamLeagueEnterList(teamLeagueEnterList)
            .teamLeagueRankList(teamLeagueRankList)
            .leagueMatchCalendar(leagueMatchCalendar)
            .leagueMatchSchedule(leagueMatchSchedule)
            .build();

        return teamResponse;
    }

    public TeamCompareResponse getTeamCompareInfo(TeamDto teamDto) {

        List<HashMap<String, Uage>> areaList = uageMapper.selectAreaList(teamDto.getAgeGroup());

        // Home Team
        teamDto.setTeamId(teamDto.getHomeTeamId());

        Cup cupDto = Cup.builder()
            .teamId(teamDto.getTeamId())
            .cupInfoTB(teamDto.getCupInfoTB())
            .cupSubMatchTB(teamDto.getCupSubMatchTB())
            .cupMainMatchTB(teamDto.getCupMainMatchTB())
            .cupTourMatchTB(teamDto.getCupTourMatchTB())
            .cupResultTB(teamDto.getCupResultTB())
            .build();

        League leagueParam = League.builder()
            .teamId(teamDto.getTeamId())
            .sYear(teamDto.getSYear())
            .leagueTeamTB(teamDto.getLeagueTeamTB())
            .leagueInfoTB(teamDto.getLeagueInfoTB())
            .leagueMatchTB(teamDto.getLeagueMatchTB())
            .leagueResultTB(teamDto.getLeagueResultTB())
            .build();


        CommonTeamDto homeTeamInfoMap = teamMapper.selectTeamInfo(teamDto.getTeamId());
        TeamTotalResultDto homeTeamMatchResultMap = teamMapper.selectTeamMatchResult(teamDto);

        List<TeamMatchDto> homeTeamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);
        List<TeamResultDto> homeTeamAvgGoalList = teamMapper.selectTeamAvgGoal(teamDto);
        List<TeamResultDto> homeWinningRate = teamMapper.selectTeamResultByYearRenewal(teamDto);

        List<TeamCupResultDto> homeJoinCupList = cupMapper.selectTeamCupResultRenewal(cupDto);
        //List<CupSubMatchDto> homeTeamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);
        List<TeamCupResultDto> homeTeamCupResultList = cupMapper.selectTeamCupResult(cupDto);

        List<TeamLeagueEnterDto> homeTeamLeagueEntertList = leagueMapper.selectTeamEnterLeague(leagueParam);
        List<LeagueMatchDto> homeTeamLeagueMatchList = leagueMapper.selectTeamLeagueMatch(leagueParam);
        List<LeagueYearRankDto> homeTeamTeamRankByYear = leagueMapper.selectTeamLeagueRankByYear(leagueParam);

        //if(homeTeamLeagueMatchList.size() == 0 && homeTeamCupMatchList.size() == 0 && homeTeamTotalMatchList.size() == 0) {
        if(homeTeamLeagueMatchList.size() == 0 && homeTeamTotalMatchList.size() == 0) {

            String pId = homeTeamInfoMap.getPId();

            if(!pId.equals("0")) {
                teamDto.setTeamId(pId);
                leagueParam.setTeamId(pId);
                cupDto.setTeamId(pId);
            }

            homeTeamCupResultList = cupMapper.selectTeamCupResult(cupDto);
            homeTeamLeagueEntertList = leagueMapper.selectTeamEnterLeague(leagueParam);
            homeTeamMatchResultMap = teamMapper.selectTeamMatchResult(teamDto);
            homeTeamLeagueMatchList = leagueMapper.selectTeamLeagueMatch(leagueParam);
            //homeTeamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);
            homeTeamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);

        }

        // Away Team
        teamDto.setTeamId(teamDto.getAwayTeamId());

        cupDto.setTeamId(teamDto.getTeamId());
        leagueParam.setTeamId(teamDto.getTeamId());

        CommonTeamDto awayTeamInfoMap = teamMapper.selectTeamInfo(teamDto.getTeamId());
        TeamTotalResultDto awayTeamMatchResultMap = teamMapper.selectTeamMatchResult(teamDto);

        List<TeamMatchDto> awayTeamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);
        List<TeamResultDto> awayTeamAvgGoalList = teamMapper.selectTeamAvgGoal(teamDto);
        List<TeamResultDto> awayWinningRate = teamMapper.selectTeamResultByYearRenewal(teamDto);

        List<TeamCupResultDto> awayJoinCupList = cupMapper.selectTeamCupResultRenewal(cupDto);
        //List<CupSubMatchDto> awayTeamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);
        List<TeamCupResultDto> awayTeamCupResultList = cupMapper.selectTeamCupResult(cupDto);

        List<TeamLeagueEnterDto> awayTeamLeagueEntertList = leagueMapper.selectTeamEnterLeague(leagueParam);
        List<LeagueMatchDto> awayTeamLeagueMatchList = leagueMapper.selectTeamLeagueMatch(leagueParam);
        List<LeagueYearRankDto> awayTeamTeamRankByYear = leagueMapper.selectTeamLeagueRankByYear(leagueParam);

        //if(awayTeamLeagueMatchList.size() == 0 && awayTeamCupMatchList.size() == 0 && awayTeamTotalMatchList.size() == 0) {
        if(awayTeamLeagueMatchList.size() == 0 && awayTeamTotalMatchList.size() == 0) {

            String pId =awayTeamInfoMap.getPId();

            if(!pId.equals("0")) {
                teamDto.setTeamId(pId);
                leagueParam.setTeamId(pId);
                cupDto.setTeamId(pId);
            }

            awayTeamCupResultList = cupMapper.selectTeamCupResult(cupDto);
            awayTeamLeagueEntertList = leagueMapper.selectTeamEnterLeague(leagueParam);
            awayTeamMatchResultMap = teamMapper.selectTeamMatchResult(teamDto);
            awayTeamLeagueMatchList = leagueMapper.selectTeamLeagueMatch(leagueParam);
            //awayTeamCupMatchList = cupMapper.selectTeamCupMatch(cupDto);
            awayTeamTotalMatchList = teamMapper.selectTeamTotalMatch(teamDto);

        }

        String opponentGameVW = "vw_" + teamDto.getAgeGroup() + "_Game_Record";

        teamDto.setOpponentGameVW(opponentGameVW);
        teamDto.setTeamId1(teamDto.getHomeTeamId());
        teamDto.setTeamId2(teamDto.getAwayTeamId());

        List<TeamOpponentDto> opponentMatchList = teamMapper.selectVwOpponentResultRenewal(teamDto);

        TeamCompareResponse teamResponse = TeamCompareResponse.builder()
            .areaList(areaList)
            .homeTeamInfoMap(homeTeamInfoMap)
            .homeTeamCupResultList(homeTeamCupResultList)
            .homeTeamLeagueEntertList(homeTeamLeagueEntertList)
            .homeTeamMatchResultMap(homeTeamMatchResultMap)
            .homeTeamLeagueMatchList(homeTeamLeagueMatchList)
            //.homeTeamCupMatchList(homeTeamCupMatchList)
            .homeTeamTotalMatchList(homeTeamTotalMatchList)
            .homeWinningRate(homeWinningRate)
            .homeTeamAvgGoalList(homeTeamAvgGoalList)
            .homeJoinCupList(homeJoinCupList)
            .homeTeamTeamRankByYear(homeTeamTeamRankByYear)
            .awayTeamInfoMap(awayTeamInfoMap)
            .awayTeamCupResultList(awayTeamCupResultList)
            .awayTeamLeagueEntertList(awayTeamLeagueEntertList)
            .awayTeamMatchResultMap(awayTeamMatchResultMap)
            .awayTeamLeagueMatchList(awayTeamLeagueMatchList)
            //.awayTeamCupMatchList(awayTeamCupMatchList)
            .awayTeamTotalMatchList(awayTeamTotalMatchList)
            .awayWinningRate(awayWinningRate)
            .awayTeamAvgGoalList(awayTeamAvgGoalList)
            .awayJoinCupList(awayJoinCupList)
            .awayTeamTeamRankByYear(awayTeamTeamRankByYear)
            .opponentMatchList(opponentMatchList)
            .build();

        return teamResponse;
    }

    public TeamEnterResponse getTeamEnterInfo(TeamDto teamDto) {

        List<EnterTeam> enterTeamList = teamMapper.selectEnterTeamInfo(teamDto);
        List<TeamEnterDto> resultList = new ArrayList<TeamEnterDto>();

        if (enterTeamList.size() > 0) {
            for (EnterTeam team: enterTeamList) {
                resultList.add(new TeamEnterDto(team.getTeamId(), team.getNickName(), team.getEnterCnt()));
            }
        }

        TeamEnterResponse enterResponse = TeamEnterResponse.builder()
            .teamEnterList(resultList)
            .build();

        return enterResponse;
    }

    public TeamRosterResponse getTeamRosterInfo(TeamDto teamDto) {

        List<RosterTeam> enterRosterList = teamMapper.selectRosterTeamInfo(teamDto);
        List<TeamRosterDto> resultList = new ArrayList<TeamRosterDto>();

        if (enterRosterList.size() > 0) {
            for (RosterTeam roster: enterRosterList) {
                TeamRosterDto teamRosterDto = TeamRosterDto.builder()
                    .team_id(roster.getTeamId())
                    .name(roster.getName())
                    .position(roster.getPosition())
                    .birthday(roster.getBirthday())
                    .national_cnt(roster.getNationalCnt())
                    .local_cnt(roster.getLocalCnt())
                    .fiveArea_cnt(roster.getFiveAreaCnt())
                    .allArea_cnt(roster.getAllAreaCnt())
                    .future_cnt(roster.getFutureCnt())
                    .elite_cnt(roster.getEliteCnt())
                    .build();

                resultList.add(teamRosterDto);
            }
        }

        TeamRosterResponse enterResponse = TeamRosterResponse.builder()
            .teamRosterList(resultList)
            .build();

        return enterResponse;
    }

    public TeamSearchResponse getTeamSearchList(TeamDto teamDto) {

        int totalCount = teamMapper.selectSearchTeamCnt(teamDto);

        PageInfo pageInfo = new PageInfo(teamDto.getCurPage(), teamDto.getPageSize());

        teamDto.setOffset(pageInfo.getOffset());
        teamDto.setPageSize(pageInfo.getPageSize());

        List<TeamSearchDto> searchList = teamMapper.selectSearchTeam(teamDto);

        // 리스폰스 변경 하기
        TeamSearchResponse teamResponse = TeamSearchResponse.builder()
            .totalCount(totalCount)
            .searchList(searchList)
            .build();

        return teamResponse;
    }

    public List<IndirectMatches> getIndirectMatches(String inTeamId, String inAnTeamId, String inSYear,String uage){
        Map<String,String> map = new HashMap<>();
        map.put("inTeamId",inTeamId);
        map.put("inAnTeamId",inAnTeamId);
        map.put("inSYear",inSYear);
        map.put("uage",uage);
        List<IndirectMatchesSqlResultSet> indirectMatches = teamMapper.selectIndirectMatches(map);

        List<IndirectMatches> list = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger(0);
        indirectMatches.forEach(indirectMatche -> {
            //이터레이터 처음 돌때 넣고 시작
            if(list.isEmpty()) {
                IndirectMatches indirectMatchesAdd = settingIndirectMatches(indirectMatche);
                indirectMatchesAdd.setHmtLose(indirectMatchesAdd.getHmtDetails().size() - (indirectMatchesAdd.getHmtWon() + indirectMatchesAdd.getHmtEq()));
                indirectMatchesAdd.setAntLose(indirectMatchesAdd.getAntDetails().size() - (indirectMatchesAdd.getAntWon() + indirectMatche.getAntEq()));
                list.add(indirectMatchesAdd);
                return;
            }
            int i = counter.get();
            //전 경기랑 중복이면 경기 디테일 넣기
            if(indirectMatche.getHmtAnotherTeamId() == list.get(i).getHmtAnotherTeamId() && indirectMatche.getAntAnotherTeamId() == list.get(i).getAntAnotherTeamId()){
                //이미 들어간 gameId가 있으면 넣지 않음
                if(checkDuplicationGameId(list.get(i).getHmtDetails(), indirectMatche.getHmtGameId())){
                    IndirectMatchesDetail detail = settingHmtDetail(indirectMatche);
                    list.get(i).getHmtDetails().add((detail));

                    //승률 쌓기
                    int won = list.get(i).getHmtWon() + indirectMatche.getHmtWon();
                    int eq = list.get(i).getHmtEq() + indirectMatche.getHmtEq();
                    int lose = list.get(i).getHmtDetails().size() - (won + eq);
                    list.get(i).setHmtWon(won);
                    list.get(i).setHmtEq(eq);
                    list.get(i).setHmtLose(lose);
                }
                //이미 들어간 gameId가 있으면 넣지 않음
                if(checkDuplicationGameId(list.get(i).getAntDetails(), indirectMatche.getAntGameId())){
                    IndirectMatchesDetail detail = settingAntDetail(indirectMatche);
                    list.get(i).getAntDetails().add((detail));

                    //승률 쌓기
                    int won = list.get(i).getAntWon() + indirectMatche.getAntWon();
                    int eq = list.get(i).getAntEq() + indirectMatche.getAntEq();
                    int lose = list.get(i).getAntDetails().size() - (won + eq);
                    list.get(i).setAntWon(won);
                    list.get(i).setAntEq(eq);
                    list.get(i).setAntLose(lose);
                }
                //경기 디테일 쌓고 이터레이터 다음으로 넘기기
                return;
            }

            //승률 최종 계산
            IndirectMatches indirectMatchesAdd = settingIndirectMatches(indirectMatche);
            indirectMatchesAdd.setHmtLose(indirectMatchesAdd.getHmtDetails().size() - (indirectMatchesAdd.getHmtWon() + indirectMatchesAdd.getHmtEq()));
            indirectMatchesAdd.setAntLose(indirectMatchesAdd.getAntDetails().size() - (indirectMatchesAdd.getAntWon() + indirectMatchesAdd.getAntEq()));
            list.add(indirectMatchesAdd);
            counter.set(++i);
        });
        return list;
    }

    public TeamInterestInfoResponse getInterestTeamData(String teamId) {

        CommonTeamDto teamInfoMap = teamMapper.selectTeamInfo(teamId);

        String ageGroup = teamInfoMap.getUage();
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupResultTB = ageGroup + "_Cup_Result";
        String cupTeamTB = ageGroup + "_Cup_Team";
        String leagueInfoTB = ageGroup + "_League_Info";
        String leagueResultTB = ageGroup + "_League_Result";
        String leagueTeamTB = ageGroup + "_League_Team";


        TeamDto teamDto = TeamDto.builder()
            .teamId(teamId)
            .cupInfoTB(cupInfoTB)
            .cupResultTB(cupResultTB)
            .cupTeamTB(cupTeamTB)
            .leagueInfoTB(leagueInfoTB)
            .leagueResultTB(leagueResultTB)
            .leagueTeamTB(leagueTeamTB)
            .build();

        HashMap<String, Object> interestTeamInfo = teamMapper.selectInterestTeamData(teamDto);

        TeamResultDto teamResultData = teamMapper.selectTeamResultByYear(teamDto);

        TeamInterestInfoResponse teamInterestInfoResponse = TeamInterestInfoResponse.builder()
            .teamResultData(teamResultData)
            .interestTeamInfo(interestTeamInfo)
            .build();

        return teamInterestInfoResponse;
    }

    public IndirectMatches settingIndirectMatches (IndirectMatchesSqlResultSet indirectMatche) {
        IndirectMatches result = IndirectMatches.builder()
            .team(indirectMatche.getHmtAnotherTeam())
            .hmtTeamId(indirectMatche.getHmtTeamId())
            .hmtAnotherTeamId(indirectMatche.getHmtAnotherTeamId())
            .hmtTeam(indirectMatche.getHmtTeam())
            .hmtAnotherTeam(indirectMatche.getHmtAnotherTeam())
            .hmtEmblem(indirectMatche.getHmtEmblem())
            .hmtAnotherEmblem(indirectMatche.getHmtAnotherEmblem())
            .hmtWon(indirectMatche.getHmtWon())
            .hmtEq(indirectMatche.getHmtEq())
            .hmtLose(0)
            .hmtDetails(new ArrayList<>())

            .antTeamId(indirectMatche.getAntTeamId())
            .antAnotherTeamId(indirectMatche.getAntAnotherTeamId())
            .antTeam(indirectMatche.getAntTeam())
            .antAnotherTeam(indirectMatche.getAntAnotherTeam())
            .antEmblem(indirectMatche.getAntEmblem())
            .antAnotherEmblem(indirectMatche.getAntAnotherEmblem())
            .antWon(indirectMatche.getAntWon())
            .antEq(indirectMatche.getAntEq())
            .antLose(0)
            .antDetails(new ArrayList<>())
            .build();

        result.getHmtDetails().add(settingHmtDetail(indirectMatche));
        result.getAntDetails().add(settingAntDetail(indirectMatche));

        return result;
    }

    public IndirectMatchesDetail settingHmtDetail(IndirectMatchesSqlResultSet indirectMatche) {
        IndirectMatchesDetail detail = IndirectMatchesDetail.builder()
            .gameId(indirectMatche.getHmtGameId())
            .gameType(indirectMatche.getHmtGameType())
            .place(indirectMatche.getHmtPlace())
            .matchCalcDate(indirectMatche.getHmtMatchCalcDate())
            .matchDate(indirectMatche.getHmtMatchDate())
            .teamId(indirectMatche.getHmtTeamId())
            .anotherTeamId(indirectMatche.getHmtAnotherTeamId())
            .team(indirectMatche.getHmtTeam())
            .anotherTeam(indirectMatche.getHmtAnotherTeam())
            .emblem(indirectMatche.getHmtEmblem())
            .anotherEmblem(indirectMatche.getHmtAnotherEmblem())
            .score(indirectMatche.getHmtScore())
            .anotherScore(indirectMatche.getHmtAnotherScore())
            .pk(indirectMatche.getHmtPk())
            .anotherPk(indirectMatche.getHmtAnotherPk())
            .matchName(indirectMatche.getHmtMatchName())
            .build();

        return detail;
    }

    public IndirectMatchesDetail settingAntDetail(IndirectMatchesSqlResultSet indirectMatche) {
        IndirectMatchesDetail detail = IndirectMatchesDetail.builder()
            .gameId(indirectMatche.getAntGameId())
            .gameType(indirectMatche.getAntGameType())
            .place(indirectMatche.getAntPlace())
            .matchCalcDate(indirectMatche.getAntMatchCalcDate())
            .matchDate(indirectMatche.getAntMatchDate())
            .teamId((indirectMatche.getAntTeamId()))
            .anotherTeamId(indirectMatche.getAntAnotherTeamId())
            .team(indirectMatche.getAntTeam())
            .anotherTeam(indirectMatche.getAntAnotherTeam())
            .emblem(indirectMatche.getAntEmblem())
            .anotherEmblem(indirectMatche.getAntAnotherEmblem())
            .score(indirectMatche.getAntScore())
            .anotherScore(indirectMatche.getAntAnotherScore())
            .pk(indirectMatche.getAntPk())
            .anotherPk(indirectMatche.getAntAnotherPk())
            .matchName(indirectMatche.getAntMatchName())
            .build();

        return detail;
    }

    public boolean checkDuplicationGameId(List<IndirectMatchesDetail> details, long gameId) {
        for (IndirectMatchesDetail detail : details) {
            if (detail.getGameId() == gameId) {
                return false;
            }
        }
        return true;
    }
}
