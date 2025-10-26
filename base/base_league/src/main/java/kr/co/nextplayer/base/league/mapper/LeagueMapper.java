package kr.co.nextplayer.base.league.mapper;

import kr.co.nextplayer.base.league.dto.*;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.team.dto.CommonTeamDto;
import kr.co.nextplayer.base.team.dto.TeamLeagueEnterDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.Banner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface LeagueMapper {

    /**
     * 전연령 리그 리스트
     * @param league
     * @return
     */
    List<LeagueListDto> selectLeagueList(League league);

    List<BannerLeagueListDto> selectBannerLeagueList(League league);

    List<LeagueMatchDto> selectLeagueMatchList(League league);

    /**
     * 리그 기본정보
     * @param league
     * @return
     */
    LeagueInfoDto selectLeagueInfo(League league);

    /**
     * 리그 팀내역 (순위표)
     * @param league
     * @return
     */
    List<CommonTeamDto> selectLeagueTeam(League league);

    /**
     * 리그최종 순위
     * @param league
     * @return
     */
    List<LeagueTeamDto> selectLeagueFinalRank(League league);

    /**
     * 리그 경기일정 캘린더
     * @param league
     * @return
     */
    List<LeagueMatchCalendarDto> selectLeagueMatchCalendar(League league);

    /**
     * 리그결과 경기 리스트
     * @param league
     * @return
     */
    List<LeagueMatchDto> selectLeagueMatchSchedule(League league);

    /**
     * 메인 - 연령별 리그 우승팀 내역
     */
    List<LeagueWinnerListDto> selectLeagueWinnerList(League league);

    List<LeagueMatchDto> selectLeagueMatchScheduleByTeamRenewal(League league);

    List<LeagueResultDto> selectTeamLeagueResultByYear(League league);

    List<LeagueYearRankDto> selectTeamLeagueRankByYear(League league);

    List<TeamLeagueEnterDto> selectTeamEnterLeague(League league);

    List<LeagueMatchDto> selectTeamLeagueMatch(League league);

    List<LeagueSearchMatchDto> selectSearchLeagueMatch(League league);

    List<HashMap<String, Object>> selectLeagueInfoListForMedia(League league);

    List<HashMap<String, Object>> selectLeagueMatchListForMedia(League league);

    List<LeagueInfoDto> selectLeagueInfoList(League league);

    List<LeagueYearRankDto> selectTeamLeagueRankByThisYear(League league);

    HashMap<String, Object> selectLeagueSummaryData(League league);

    int selectIsTodayMatch(Map<String, Object> param);
}
