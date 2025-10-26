package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDataDto;

public interface CupMapper {

    List<HashMap<String, Object>> selectTeamCupResult(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamCupMatch(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamCupPlayedByYear(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamCupMatchByDetCup(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamCupAvgGoalByYear(Map<String, String> params);

    HashMap<String, Object> selectCupInfoListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectCupInfoList(Map<String, String> params);
    HashMap<String, String> selectGetCupInfo(Map<String, String> params);

    int insertCupInfo(Map<String, String> params);

    int updateCupInfo(Map<String, String> params);

    int deleteCupInfo(Map<String, String> params);

    int updateCupPrize(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTeamListByGroups(Map<String, String> params);
    int insertCupTeam(Map<String, Object> params);

    int insertTourCupTeam(Map<String, Object> params);

    int updateTourCupTeam(Map<String, Object> params);

    int deleteCupTeam(Map<String, String> params);

    int deleteCupTeamOne(Map<String, String> params);

    int updateCupTeamOne(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchList(Map<String, String> params);

    int updateAllCupSubMatch(Map<String, Object> params);
    int deleteCupSubMatch(Map<String, String> params);

    int deleteCupSubMatchGroups(Map<String, String> params);

    int deleteCupSubMatchOne(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchAllList(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainTeamListByGroups(Map<String, String> params);

    int insertCupSubMatch(Map<String, Object> params);

    int updateCupMainTeamReset(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainMatchList(Map<String, String> params);

    int deleteCupMainMatch(Map<String, String> params);

    int updateAllCupMainMatch(Map<String, Object> params);
    List<HashMap<String, Object>> selectCupMainMatchAllList(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTourMatchList(Map<String, String> params);
    int updateAllCupTourMatch(Map<String, Object> params);

    int delteCupTourMatch(Map<String, String> params);
    List<HashMap<String, Object>> selectCupMgrList(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchRank(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchRankByFinal(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchResultList(Map<String, String> params);
    int updateCupSubVideo(Map<String, String> params);

    int updateCupSubMatchScore(Map<String, Object> params);

    int insertCupSubRank(Map<String, Object> params);

    int updateCupSubRank(Map<String, Object> params);

    int deleteCupSubRank(Map<String, Object> params);
    List<HashMap<String, Object>> selectCupMainMatchRank(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainMatchRankByFinal(Map<String, String> params);

    String selectCupRankCount(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainMatchResultList(Map<String, String> params);

    int updateCupMainMatchScore(Map<String, Object> params);

    int updateCupMainVideo(Map<String, String> params);

    int updateCupMainRank(Map<String, Object> params);

    int updateCupMainRankReset(Map<String, Object> params);

    List<HashMap<String, Object>> selectCupTourMatchRank(Map<String, String> params);

    int selectCupTourRankCount(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTourMatchResultList(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTourMatchRankRenewal(Map<String, String> params);

    int updateCupTourMatchScore(Map<String, Object> params);

    int updateCupTourMatchByMatchId(Map<String, String> params);

    int updateCupTourVideo(Map<String, String> params);

    int insertCupTourRank(Map<String, Object> params);

    int updateCupTourRankRenewal(Map<String, Object> params);

    int updateCupTourRank(Map<String, Object> params);

    int deleteCupTourRank(Map<String, Object> params);

    List<HashMap<String, Object>> selectCupMatchList(Map<String, String> params);

    HashMap<String, Object> selectCupTourMatchInfo(Map<String, String> params);

    HashMap<String, Object> selectCupNextTourMatch(Map<String, String> params);

    int updateCupTourMatchFixFlag(Map<String, String> params);

    int updateCupTourNextMatch(Map<String, String> params);

    int insertCupInfoExcel(Map<String, Object> params);

    int insertCupTeamExcelByOnlyTour(Map<String, String> params);

    int insertCupTeamExcel(Map<String, String> params);

    int updateCupTeamExcelBycupId(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTeamList(Map<String, String> params);

    int updateCupTeamExcel_type1(Map<String, Object> params);

    int updateCupTeamExcel_type2(Map<String, Object> params);

    HashMap<String, Object> selectCupSubMatchListCount(Map<String, String> params);

    int insertCupSubMatchExcel(Map<String, Object> params);

    int insertCupSubMatchForExcel(Map<String, String> params);

    int updateCupSubMatchExcelByCupId(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchListByHomeTeam(Map<String, String> params);

    List<HashMap<String, Object>> selectCupSubMatchListByAwayTeam(Map<String, String> params);

    int updateCupSubMatchExcel_home(Map<String, Object> params);

    int updateCupSubMatchExcelHome(Map<String, Object> params);

    int updateCupSubMatchExcel_away(Map<String, Object> params);

    int updateCupSubMatchExcelAway(Map<String, Object> params);

    int updateCupMainTeam(Map<String, String> params);

    HashMap<String, Object> selectCupMainMatchListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainMatchListByHomeTeam(Map<String, String> params);

    List<HashMap<String, Object>> selectCupMainMatchListByAwayTeam(Map<String, String> params);

    int insertCupMainMatchExcel(Map<String, Object> params);

    int insertCupMainMatch(Map<String, String> params);

    int updateCupMainMatchExcelByCupId(Map<String, String> params);

    int updateCupMainMatchExcel_home(Map<String, Object> params);

    int updateCupMainMatchExcelHome(Map<String, Object> params);

    int updateCupMainMatchExcel_away(Map<String, Object> params);

    int updateCupMainMatchExcelAway(Map<String, Object> params);

    HashMap<String, Object> selectCupTourMatchListCount(Map<String, String> params);

    int deleteCupTourMatch(Map<String, String> params);

    int insertCupTourMatchExcel(Map<String, Object> params);

    int insertCupTourMatch(Map<String, String> params);

    int updateCupTourMatchExcelByCupId(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTourMatchListByHomeTeam(Map<String, String> params);

    List<HashMap<String, Object>> selectCupTourMatchListByAwayTeam(Map<String, String> params);

    int updateCupTourMatchExcel_home(Map<String, Object> params);

    int updateCupTourMatchExcelHome(Map<String, Object> params);

    int updateCupTourMatchExcel_away(Map<String, Object> params);


    int updateCupTourMatchExcelAway(Map<String, Object> params);

    List<HashMap<String, Object>> selectCupMatchListForMedia(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchCupMatchList(Map<String, String> params);

    HashMap<String, Object> selectCupMatchForModifyResultRequest(Map<String, String> params);

    HashMap<String, Object> selectGetCupInfoForMedia(Map<String, String> params);

    int updateCupSubMatchEndFlag(Map<String, String> params);

    int updateCupMainMatchEndFlag(Map<String, String> params);

    List<HashMap<String, Object>> selectGetCupChampions(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchCupForChampion(Map<String, String> params);

    HashMap<String, Object> selectSearchCupResultForChampion(Map<String, String> params);

    int insertCupResultForChampion(List<HashMap<String, Object>> params);

    int updateCupResultForChampion(List<HashMap<String, Object>> params);

    void updateMainMatchInfo(Map<String, Object> params);

    void updateSubMatchInfo(Map<String, Object> params);

    void updateTourMatchInfo(Map<String, Object> params);

    List<HashMap<String, Object>> selectMatchPlayData(Map<String, String> params);

    List<HashMap<String, Object>> selectMatchChangeData(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForCupMainMatchCnt(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForCupSubMatchCnt(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForCupTourMatchCnt(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForCupTeamInSubMatchCnt(Map<String, String> params);
    
    HashMap<String, Object> selectTeamNameForCupTeamInMainMatchCnt(Map<String, String> params);
    
    int updateTeamNameForCupMainMatchHome(Map<String, String> params);
    
    int updateTeamNameForCupMainMatchAway(Map<String, String> params);
    
    int updateTeamNameForCupSubMatchHome(Map<String, String> params);
    
    int updateTeamNameForCupSubMatchAway(Map<String, String> params);
    
    int updateTeamNameForCupTourMatchHome(Map<String, String> params);
    
    int updateTeamNameForCupTourMatchAway(Map<String, String> params);
    
    int updateTeamNameForCupTeamInMainMatch(Map<String, String> params);
    
    int updateTeamNameForCupTeamInSubMatch(Map<String, String> params);
    
    int updateTeamNameForChampion(Map<String, String> params);
    
    int updateCupSubMatchOne(Map<String, String> params);
    
    List<HashMap<String, Object>> selectCupSubMatchForWin(Map<String, String> params);
    
    HashMap<String, Object> selectGetCupInfoForWin(Map<String, String> params);
    
    int updateCupMainMatchOne(Map<String, String> params);
    
    HashMap<String, Object> selectCupMainMatchForWin(Map<String, String> params);
    
    List<HashMap<String, Object>> selectCupSubMatchRankForWin(Map<String, Object> params);
    
    List<HashMap<String, Object>> selectCupMainMatchRankForWin(Map<String, Object> params);

    int updateMatchScoreShowFlag(Map<String, String> param);

    int updateLiveShowFlag(Map<String, String> param);

    List<HashMap<String, Object>> selectProgressCupInfo(Map<String, String> param);

    int updateShowFlagProgressCup(Map<String, String> param);

    List<HashMap<String, Object>> selectStaffData(Map<String, String> param);

    List<HashMap<String, Object>> selectOwnGoalData(Map<String, String> param);

    List<HashMap<String, Object>> selectStaffPenaltyData(Map<String, String> param);

    /**
     * 라인업 크롤링 해올 경기 순서 조회
     * @param param
     * @return
     */
    List<HashMap<String, Object>> selectSearchMatchOrderForPlayDataCraw(Map<String, String> param);

    List<MatchDataDto> selectCupTourMatchListCraw(Map<String, String> param);
    List<MatchDataDto> selectCupMainMatchListCraw(Map<String, String> param);
    List<MatchDataDto> selectCupSubMatchListCraw(Map<String, String> param);
    
    HashMap<String, Object> selectCupMainMatchInfo(Map<String, String> params);
    
    HashMap<String, Object> selectCupSubMatchInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectCrawSettingCupList(Map<String, String> params);

    void updateCrawSetting(Map<String, String> params);

}
