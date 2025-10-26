package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.LeagueResponse;
import kr.co.nextplayer.base.front.response.league.LeagueInfoResponse;
import kr.co.nextplayer.base.front.response.league.LeagueListResponse;
import kr.co.nextplayer.base.front.response.league.LeagueMatchResponse;
import kr.co.nextplayer.base.front.response.league.LeagueSearchMatchResponse;
import kr.co.nextplayer.base.front.util.StrUtil;
import kr.co.nextplayer.base.league.dto.*;
import kr.co.nextplayer.base.league.mapper.LeagueMapper;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.team.dto.CommonTeamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class LeagueService {

    @Resource
    private UageMapper uageMapper;

    @Resource
    private LeagueMapper leagueMapper;

    /**
     * 리그 목록
     * @param league
     * @return
     */
    public List<LeagueListDto> selectLeagueList(League league) {
        return leagueMapper.selectLeagueList(league);
    }

    /**
     * 리그 상세 정보
     * @param league
     * @return
     */
    public LeagueInfoDto selectLeagueInfo(League league) {
        return leagueMapper.selectLeagueInfo(league);
    }

    /**
     * 리그 참가 팀 목록
     * @param league
     * @return
     */
    public List<CommonTeamDto> selectLeagueTeam(League league) {
        return leagueMapper.selectLeagueTeam(league);
    }

    /**
     * 리그 최종 순위
     * @param league
     * @return
     */
    public List<LeagueTeamDto> selectLeagueFinalRank(League league) {
        return leagueMapper.selectLeagueFinalRank(league);
    }

    /**
     * 리그 경기 일정
     * @param league
     * @return
     */
    public List<LeagueMatchCalendarDto> selectLeagueMatchCalendar(League league) {
        return leagueMapper.selectLeagueMatchCalendar(league);
    }

    /**
     * 리그 경기 결과 목록
     * @param league
     * @return
     */
    public List<LeagueMatchDto> selectLeagueMatchSchedule(League league) {
        return leagueMapper.selectLeagueMatchSchedule(league);
    }

    /**
     * 팀별 리그 정보
     * @param league
     * @return
     */
    public List<LeagueMatchDto> selectLeagueMatchScheduleByTeamRenewal(League league) {
        return leagueMapper.selectLeagueMatchScheduleByTeamRenewal(league);
    }

    public LeagueInfoResponse getLeagueInfo(League leagueParam) {

        if (StrUtil.isEmpty(leagueParam.getAgeGroup())) {
            leagueParam.setAgeGroup("U18");
        }

        String leagueInfoTB = leagueParam.getAgeGroup() + "_League_Info";
        String leagueTeamTB = leagueParam.getAgeGroup() + "_League_Team";

        leagueParam.setLeagueInfoTB(leagueInfoTB);
        leagueParam.setLeagueTeamTB(leagueTeamTB);

        LeagueInfoDto leagueInfo = leagueMapper.selectLeagueInfo(leagueParam);


        LeagueInfoResponse leagueResponse = LeagueInfoResponse.builder()
            .leagueInfo(leagueInfo)
            .ageGroup(leagueParam.getAgeGroup())
            .build();

        return leagueResponse;
    }

    public LeagueListResponse getLeagueList(League leagueParam) {


        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();
        List<HashMap<String, Uage>> areaList = uageMapper.selectAreaList(leagueParam.getAgeGroup());

        List<LeagueListDto> leagueList = new ArrayList<LeagueListDto>();
        List<LeagueListDto> totalLeagueList = new ArrayList<LeagueListDto>();

        if (StrUtil.isEmpty(leagueParam.getAgeGroup())) {
            leagueParam.setAgeGroup("U18");
        }

        String leagueInfoTB = leagueParam.getAgeGroup() + "_League_Info";
        String leagueTeamTB = leagueParam.getAgeGroup() + "_League_Team";

        leagueParam.setLeagueInfoTB(leagueInfoTB);
        leagueParam.setLeagueTeamTB(leagueTeamTB);

        leagueList = leagueMapper.selectLeagueList(leagueParam);

        totalLeagueList.addAll(leagueList);

        LeagueListResponse leagueResponse = LeagueListResponse.builder()
            .uageList(uageList)
            .areaList(areaList)
            .totalleagueList(totalLeagueList)
            .build();

        return leagueResponse;
    }

    public LeagueMatchResponse getLeagueMatch(League leagueParam) {

        LeagueInfoDto leagueInfoMap = leagueMapper.selectLeagueInfo(leagueParam);
        List<CommonTeamDto> leagueTeamList = leagueMapper.selectLeagueTeam(leagueParam);
        List<LeagueTeamDto> leagueRankList = leagueMapper.selectLeagueFinalRank(leagueParam);
        List<LeagueMatchCalendarDto> leagueMatchCalendar = leagueMapper.selectLeagueMatchCalendar(leagueParam);
        List<LeagueMatchDto> leagueMatchSchedule = leagueMapper.selectLeagueMatchSchedule(leagueParam);

        HashMap<String, Object> summaryData = leagueMapper.selectLeagueSummaryData(leagueParam);

        LeagueMatchResponse leagueResponse = LeagueMatchResponse.builder()
            .leagueInfoMap(leagueInfoMap)
            .leagueTeamList(leagueTeamList)
            .leagueRankList(leagueRankList)
            .leagueMatchCalendar(leagueMatchCalendar)
            .leagueMatchSchedule(leagueMatchSchedule)
            .summaryData(summaryData)
            .build();


        return leagueResponse;
    }

    public LeagueSearchMatchResponse getLeagueSearchMatch(League leagueParam) {

        List<LeagueSearchMatchDto> leagueMatchSchedule = leagueMapper.selectSearchLeagueMatch(leagueParam);
        List<LeagueInfoDto> leagueInfoList = leagueMapper.selectLeagueInfoList(leagueParam);

        LeagueSearchMatchResponse leagueResponse = LeagueSearchMatchResponse.builder()
            .leagueSearchList(leagueMatchSchedule)
            .ageGroup(leagueParam.getAgeGroup())
            .leagueInfoList(leagueInfoList)
            .build();


        return leagueResponse;
    }

}
