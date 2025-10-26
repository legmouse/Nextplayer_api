package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDataDto;

public interface LeagueMapper {

    List<HashMap<String, Object>> selectTeamEnterLeague(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamLeagueMatch(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamEnterLeagueMatch(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamLeagueResultByYear(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamLeagueRankByYear(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamLeagueAvgGoalByYear(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueInfoList(Map<String, String> params);

    HashMap<String, Object> selectLeagueInfoListCount(Map<String, String> params);

    HashMap<String, String> selectGetLeagueInfo(Map<String, String> params);

    int insertLeagueInfo(Map<String, String> params);

    int updateLeagueInfo(Map<String, String> params);

    int deleteLeagueInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueTeamList(Map<String, String> params);

    int insertLeagueTeam(Map<String, Object> params);

    int deleteLeagueTeam(Map<String, String> params);

    HashMap<String, Object> selectLeagueMatchListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchList(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchCalendar(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchListByHomeTeam(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchListByAwayTeam(Map<String, String> params);

    int insertLeagueMatch(Map<String, Object> params);

    int updateLeagueMatch(Map<String, String> params);

    int updateAllLeagueMatch(Map<String, Object> params);

    int deleteLeagueMatch(Map<String, String> params);

    int deleteLeagueMatchOne(Map<String, Object> params);

    List<HashMap<String, Object>> selectLeagueMatchSchedule(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMgrList(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchRank(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueFinalRank(Map<String, String> params);

    int insertLeagueFinalRank(Map<String, Object> params);

    int updateLeagueFinalRank(Map<String, Object> params);

    int deleteLeagueFinalRank(Map<String, Object> params);

    List<HashMap<String, Object>> selectListOfLeagueMatch(Map<String, String> params);

    int insertLeagueTeamExcel(Map<String, String> params);

    int insertLeagueInfoExcel(Map<String, Object> params);

    int updateLeagueTeamExcelByleagueId(Map<String, String> params);

    int updateLeagueTeamExcel_type1(Map<String, Object> params);

    int updateLeagueTeamExcel_type2(Map<String, Object> params);

    int insertLeagueMatchExcel(Map<String, String> params);

    int updateLeagueMatchExcelByLeagueId(Map<String, String> params);

    int updateLeagueMatchExcel_home(Map<String, Object> params);

    int updateLeagueMatchExcel_away(Map<String, Object> params);

    List<HashMap<String, Object>> selectLeagueMatchListForMedia(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchLeagueInfoList(Map<String, String> params);

    HashMap<String, Object> selectLeagueMatchForModifyResultRequest(Map<String, String> params);

    //List<HashMap<String, Object>> selectGetLeagueInfoForMedia(Map<String, String> params);
    HashMap<String, Object> selectGetLeagueInfoForMedia(Map<String, String> params);

    List<HashMap<String, Object>> selectLeagueMatchScheduleForExcel(Map<String, String> params);

    void updateMatchInfo(Map<String, Object> params);

    List<HashMap<String, Object>> selectMatchPlayData(Map<String, String> params);

    List<HashMap<String, Object>> selectMatchChangeData(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForLeagueMatchCnt(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForLeagueTeamCnt(Map<String, String> params);
    
    int updateTeamNameForLeagueMatchHome(Map<String, String> params);
    
    int updateTeamNameForLeagueMatchAway(Map<String, String> params);
    
    int updateTeamNameForLeagueTeam(Map<String, String> params);
    
    List<HashMap<String, Object>> selectLeagueMatchRankForWin(Map<String, Object> params);
    
    HashMap<String, Object> selectGetLeagueInfoForWin(Map<String, String> params);

    int updateMatchScoreShowFlag(Map<String, String> parma);

    List<HashMap<String, Object>> selectSearchLeagueForChampion(Map<String, String> param);

    List<HashMap<String, Object>> selectGetLeagueChampions(Map<String, String> param);

    int updateLeaguePrize(Map<String, String> param);

    int updateLeagueResultForChampion(List<HashMap<String, Object>> params);

    int insertLeagueResultForChampion(Map<String, String> param);

    List<HashMap<String, Object>> selectProgressLeagueInfo(Map<String, String> param);

    int updateShowFlagProgressLeague(Map<String, String> param);
    
    List<HashMap<String, Object>> selectSearchMatchOrderForPlayDataCraw(Map<String, String> param);
    
    List<MatchDataDto> selectLeagueMatchListCraw(Map<String, String> param);
    
    int updateLeagueMatchScore(Map<String, Object> param);

}
