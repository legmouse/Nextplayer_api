package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.ChangeDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchPlayDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.OwnGoalDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.PlayDataDto;
import kr.co.nextplayer.base.backend.mapper.CrawMapper;
import kr.co.nextplayer.base.backend.mapper.CupMapper;
import kr.co.nextplayer.base.backend.mapper.PlayerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CupService {

    @Resource
    private CupMapper cupMapper;

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private CrawMapper crawMapper;

    public List<HashMap<String, Object>> selectTeamCupResult(Map<String, String> params) {
        return cupMapper.selectTeamCupResult(params);
    }

    public List<HashMap<String, Object>> selectTeamCupMatch(Map<String, String> params) {
        return cupMapper.selectTeamCupMatch(params);
    }

    public List<HashMap<String, Object>> selectTeamCupPlayedByYear(Map<String, String> params) {
        return cupMapper.selectTeamCupPlayedByYear(params);
    }

    public List<HashMap<String, Object>> selectTeamCupMatchByDetCup(Map<String, String> params) {
        return cupMapper.selectTeamCupMatchByDetCup(params);
    }

    public List<HashMap<String, Object>> selectTeamCupAvgGoalByYear(Map<String, String> params) {
        return cupMapper.selectTeamCupAvgGoalByYear(params);
    }

    public HashMap<String, Object> selectCupInfoListCount(Map<String, String> params) {
        return cupMapper.selectCupInfoListCount(params);
    }

    public List<HashMap<String, Object>> selectCupInfoList(Map<String, String> params) {
        return cupMapper.selectCupInfoList(params);
    }
    public HashMap<String, String> selectGetCupInfo(Map<String, String> params) {
        return cupMapper.selectGetCupInfo(params);
    }

    @Transactional
    public int insertCupInfo(Map<String, String> params) {
        return cupMapper.insertCupInfo(params);
    }

    @Transactional
    public int insertCupInfoList(List<Map<String, String>> params) {
        int count = 0;
        for (Map<String, String> param : params) {
            // 연령별 테이블
            String leagueInfoTB = param.get("ageGroup") + "_Cup_Info";
            param.put("cupInfoTB", leagueInfoTB);
            count += cupMapper.insertCupInfo(param);
        }
        return count;
    }

    @Transactional
    public int updateCupInfo(Map<String, String> params) {
        return cupMapper.updateCupInfo(params);
    }

    @Transactional
    public int deleteCupInfo(Map<String, String> params) {
        return cupMapper.deleteCupInfo(params);
    }

    @Transactional
    public int updateCupPrize(Map<String, String> params) {
        return cupMapper.updateCupPrize(params);
    }
    
    public List<HashMap<String, Object>> selectCupTeamListByGroups(Map<String, String> params) {
        return cupMapper.selectCupTeamListByGroups(params);
    }

    @Transactional
    public int insertCupTeam(Map<String, Object> params) {
        return cupMapper.insertCupTeam(params);
    }

    @Transactional
    public int insertTourCupTeam(Map<String, Object> params) {
        return cupMapper.insertTourCupTeam(params);
    }

    @Transactional
    public int updateTourCupTeam(Map<String, Object> params) {
        return cupMapper.updateTourCupTeam(params);
    }

    @Transactional
    public int deleteCupTeam(Map<String, String> params) {
        return cupMapper.deleteCupTeam(params);
    }

    @Transactional
    public int deleteCupTeamOne(Map<String, String> params) {
        return cupMapper.deleteCupTeamOne(params);
    }

    @Transactional
    public int updateCupTeamOne(Map<String, String> params) {
        return cupMapper.updateCupTeamOne(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchList(Map<String, String> params) {
        return cupMapper.selectCupSubMatchList(params);
    }

    @Transactional
    public int updateAllCupSubMatch(Map<String, Object> params) {
        return cupMapper.updateAllCupSubMatch(params);
    }
    
    @Transactional
    public int deleteCupSubMatch(Map<String, String> params) {
        return cupMapper.deleteCupSubMatch(params);
    }

    @Transactional
    public int deleteCupSubMatchGroups(Map<String, String> params) {
        return cupMapper.deleteCupSubMatchGroups(params);
    }

    @Transactional
    public int deleteCupSubMatchOne(Map<String, String> params) {
        return cupMapper.deleteCupSubMatchOne(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchAllList(Map<String, String> params) {
        return cupMapper.selectCupSubMatchAllList(params);
    }

    public List<HashMap<String, Object>> selectCupMainTeamListByGroups(Map<String, String> params) {
        return cupMapper.selectCupMainTeamListByGroups(params);
    }
    
    @Transactional
    public int insertCupSubMatch(Map<String, Object> params) {
        return cupMapper.insertCupSubMatch(params);
    }

    @Transactional
    public int updateCupMainTeamReset(Map<String, String> params) {
        return cupMapper.updateCupMainTeamReset(params);
    }
    
    public List<HashMap<String, Object>> selectCupMainMatchList(Map<String, String> params) {
        return cupMapper.selectCupMainMatchList(params);
    }
    
    @Transactional
    public int deleteCupMainMatch(Map<String, String> params) {
        return cupMapper.deleteCupMainMatch(params);
    }

    @Transactional
    public int updateAllCupMainMatch(Map<String, Object> params) {
        return cupMapper.updateAllCupMainMatch(params);
    }
    public List<HashMap<String, Object>> selectCupMainMatchAllList(Map<String, String> params) {
        return cupMapper.selectCupMainMatchAllList(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchList(Map<String, String> params) {
        return cupMapper.selectCupTourMatchList(params);
    }
    
    @Transactional
    public int updateAllCupTourMatch(Map<String, Object> params) {
        return cupMapper.updateAllCupTourMatch(params);
    }

    @Transactional
    public int delteCupTourMatch(Map<String, String> params) {
        return cupMapper.delteCupTourMatch(params);
    }

    public List<HashMap<String, Object>> selectCupMgrList(Map<String, String> params) {
        return cupMapper.selectCupMgrList(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchRank(Map<String, String> params) {
        return cupMapper.selectCupSubMatchRank(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchRankByFinal(Map<String, String> params) {
        return cupMapper.selectCupSubMatchRankByFinal(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchResultList(Map<String, String> params) {
        return cupMapper.selectCupSubMatchResultList(params);
    }

    @Transactional
    public int updateCupSubVideo(Map<String, String> params) {
        return cupMapper.updateCupSubVideo(params);
    }

    @Transactional
    public int updateCupSubMatchScore(Map<String, Object> params) {
        return cupMapper.updateCupSubMatchScore(params);
    }

    @Transactional
    public int insertCupSubRank(Map<String, Object> params) {
        return cupMapper.insertCupSubRank(params);
    }

    @Transactional
    public int updateCupSubRank(Map<String, Object> params) {
        return cupMapper.updateCupSubRank(params);
    }

    @Transactional
    public int deleteCupSubRank(Map<String, Object> params) {
        return cupMapper.deleteCupSubRank(params);
    }

    public List<HashMap<String, Object>> selectCupMainMatchRank(Map<String, String> params) {
        return cupMapper.selectCupMainMatchRank(params);
    }

    public List<HashMap<String, Object>> selectCupMainMatchRankByFinal(Map<String, String> params) {
        return cupMapper.selectCupMainMatchRankByFinal(params);
    }

    public String selectCupRankCount(Map<String, String> params) {
        return cupMapper.selectCupRankCount(params);
    }

    public List<HashMap<String, Object>> selectCupMainMatchResultList(Map<String, String> params) {
        return cupMapper.selectCupMainMatchResultList(params);
    }

    @Transactional
    public int updateCupMainMatchScore(Map<String, Object> params) {
        return cupMapper.updateCupMainMatchScore(params);
    }

    @Transactional
    public int updateCupMainVideo(Map<String, String> params) {
        return cupMapper.updateCupMainVideo(params);
    }

    @Transactional
    public int updateCupMainRank(Map<String, Object> params) {
        return cupMapper.updateCupMainRank(params);
    }

    @Transactional
    public int updateCupMainRankReset(Map<String, Object> params) {
        return cupMapper.updateCupMainRankReset(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchRank(Map<String, String> params) {
        return cupMapper.selectCupTourMatchRank(params);
    }

    public int selectCupTourRankCount(Map<String, String> params) {
        return cupMapper.selectCupTourRankCount(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchResultList(Map<String, String> params) {
        return cupMapper.selectCupTourMatchResultList(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchRankRenewal(Map<String, String> params) {
        return cupMapper.selectCupTourMatchRankRenewal(params);
    }

    @Transactional
    public int updateCupTourMatchScore(Map<String, Object> params) {
        return cupMapper.updateCupTourMatchScore(params);
    }

    @Transactional
    public int updateCupTourMatchByMatchId(Map<String, String> params) {
        return cupMapper.updateCupTourMatchByMatchId(params);
    }

    @Transactional
    public int updateCupTourVideo(Map<String, String> params) {
        return cupMapper.updateCupTourVideo(params);
    }

    @Transactional
    public int insertCupTourRank(Map<String, Object> params) {
        return cupMapper.insertCupTourRank(params);
    }

    @Transactional
    public int updateCupTourRankRenewal(Map<String, Object> params) {
        return cupMapper.updateCupTourRankRenewal(params);
    }

    @Transactional
    public int updateCupTourRank(Map<String, Object> params) {
        return cupMapper.updateCupTourRank(params);
    }

    @Transactional
    public int deleteCupTourRank(Map<String, Object> params) {
        return cupMapper.deleteCupTourRank(params);
    }

    public List<HashMap<String, Object>> selectCupMatchList(Map<String, String> params) {
        return cupMapper.selectCupMatchList(params);
    }

    public HashMap<String, Object> selectCupTourMatchInfo(Map<String, String> params) {
        return cupMapper.selectCupTourMatchInfo(params);
    }

    public HashMap<String, Object> selectCupNextTourMatch(Map<String, String> params) {
        return cupMapper.selectCupNextTourMatch(params);
    }

    @Transactional
    public int updateCupTourMatchFixFlag(Map<String, String> params) {
        return cupMapper.updateCupTourMatchFixFlag(params);
    }

    @Transactional
    public int updateCupTourNextMatch(Map<String, String> params) {
        return cupMapper.updateCupTourNextMatch(params);
    }

    @Transactional
    public int insertCupInfoExcel(Map<String, Object> params) {
        return cupMapper.insertCupInfoExcel(params);
    }

    @Transactional
    public int insertCupTeamExcelByOnlyTour(Map<String, String> params) {
        return cupMapper.insertCupTeamExcelByOnlyTour(params);
    }

    @Transactional
    public int insertCupTeamExcel(Map<String, String> params) {
        return cupMapper.insertCupTeamExcel(params);
    }

    @Transactional
    public int updateCupTeamExcelBycupId(Map<String, String> params) {
        return cupMapper.updateCupTeamExcelBycupId(params);
    }
    
    public List<HashMap<String, Object>> selectCupTeamList(Map<String, String> params) {
        return cupMapper.selectCupTeamList(params);
    }

    @Transactional
    public int updateCupTeamExcel_type1(Map<String, Object> params) {
        return cupMapper.updateCupTeamExcel_type1(params);
    }

    @Transactional
    public int updateCupTeamExcel_type2(Map<String, Object> params) {
        return cupMapper.updateCupTeamExcel_type2(params);
    }

    public HashMap<String, Object> selectCupSubMatchListCount(Map<String, String> params) {
        return cupMapper.selectCupSubMatchListCount(params);
    }

    @Transactional
    public int insertCupSubMatchExcel(Map<String, Object> params) {
        return cupMapper.insertCupSubMatchExcel(params);
    }

    @Transactional
    public int insertCupSubMatchForExcel(Map<String, String> params) {
        return cupMapper.insertCupSubMatchForExcel(params);
    }

    @Transactional
    public int updateCupSubMatchExcelByCupId(Map<String, String> params) {
        return cupMapper.updateCupSubMatchExcelByCupId(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchListByHomeTeam(Map<String, String> params) {
        return cupMapper.selectCupSubMatchListByHomeTeam(params);
    }

    public List<HashMap<String, Object>> selectCupSubMatchListByAwayTeam(Map<String, String> params) {
        return cupMapper.selectCupSubMatchListByAwayTeam(params);
    }

    public int updateCupSubMatchExcel_home(Map<String, Object> params) {
        return cupMapper.updateCupSubMatchExcel_home(params);
    }

    @Transactional
    public int updateCupSubMatchExcelHome(Map<String, Object> params) {
        return cupMapper.updateCupSubMatchExcelHome(params);
    }

    @Transactional
    public int updateCupSubMatchExcel_away(Map<String, Object> params) {
        return cupMapper.updateCupSubMatchExcel_away(params);
    }

    @Transactional
    public int updateCupSubMatchExcelAway(Map<String, Object> params) {
        return cupMapper.updateCupSubMatchExcelAway(params);
    }

    @Transactional
    public int updateCupMainTeam(Map<String, String> params) {
        return cupMapper.updateCupMainTeam(params);
    }

    public HashMap<String, Object> selectCupMainMatchListCount(Map<String, String> params) {
        return cupMapper.selectCupMainMatchListCount(params);
    }

    public List<HashMap<String, Object>> selectCupMainMatchListByHomeTeam(Map<String, String> params) {
        return cupMapper.selectCupMainMatchListByHomeTeam(params);
    }

    public List<HashMap<String, Object>> selectCupMainMatchListByAwayTeam(Map<String, String> params) {
        return cupMapper.selectCupMainMatchListByAwayTeam(params);
    }

    @Transactional
    public int insertCupMainMatchExcel(Map<String, Object> params) {
        return cupMapper.insertCupMainMatchExcel(params);
    }

    @Transactional
    public int insertCupMainMatch(Map<String, String> params) {
        return cupMapper.insertCupMainMatch(params);
    }

    @Transactional
    public int updateCupMainMatchExcelByCupId(Map<String, String> params) {
        return cupMapper.updateCupMainMatchExcelByCupId(params);
    }

    @Transactional
    public int updateCupMainMatchExcel_home(Map<String, Object> params) {
        return cupMapper.updateCupMainMatchExcel_home(params);
    }

    @Transactional
    public int updateCupMainMatchExcelHome(Map<String, Object> params) {
        return cupMapper.updateCupMainMatchExcelHome(params);
    }

    @Transactional
    public int updateCupMainMatchExcel_away(Map<String, Object> params) {
        return cupMapper.updateCupMainMatchExcel_away(params);
    }

    @Transactional
    public int updateCupMainMatchExcelAway(Map<String, Object> params) {
        return cupMapper.updateCupMainMatchExcelAway(params);
    }

    public HashMap<String, Object> selectCupTourMatchListCount(Map<String, String> params) {
        return cupMapper.selectCupTourMatchListCount(params);
    }

    @Transactional
    public int deleteCupTourMatch(Map<String, String> params) {
        return cupMapper.deleteCupTourMatch(params);
    }

    @Transactional
    public int insertCupTourMatchExcel(Map<String, Object> params) {
        return cupMapper.insertCupTourMatchExcel(params);
    }

    @Transactional
    public int insertCupTourMatch(Map<String, String> params) {
        return cupMapper.insertCupTourMatch(params);
    }

    @Transactional
    public int updateCupTourMatchExcelByCupId(Map<String, String> params) {
        return cupMapper.updateCupTourMatchExcelByCupId(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchListByHomeTeam(Map<String, String> params) {
        return cupMapper.selectCupTourMatchListByHomeTeam(params);
    }

    public List<HashMap<String, Object>> selectCupTourMatchListByAwayTeam(Map<String, String> params) {
        return cupMapper.selectCupTourMatchListByAwayTeam(params);
    }

    @Transactional
    public int updateCupTourMatchExcel_home(Map<String, Object> params) {
        return cupMapper.updateCupTourMatchExcel_home(params);
    }

    @Transactional
    public int updateCupTourMatchExcelHome(Map<String, Object> params) {
        return cupMapper.updateCupTourMatchExcelHome(params);
    }

    @Transactional
    public int updateCupTourMatchExcel_away(Map<String, Object> params) {
        return cupMapper.updateCupTourMatchExcel_away(params);
    }

    @Transactional
    public int updateCupTourMatchExcelAway(Map<String, Object> params) {
        return cupMapper.updateCupTourMatchExcelAway(params);
    }

    public List<HashMap<String, Object>> selectCupMatchListForMedia(Map<String, String> params) {

        return cupMapper.selectCupMatchListForMedia(params);
    }

    public List<HashMap<String, Object>> selectSearchCupMatchList(Map<String, String> params) {

        return cupMapper.selectSearchCupMatchList(params);
    }

    public HashMap<String, Object> selectCupMatchForModifyResultRequest(Map<String, String> params) {

        return cupMapper.selectCupMatchForModifyResultRequest(params);
    }

    public HashMap<String, Object> selectGetCupInfoForMedia(Map<String, String> params) {
        return cupMapper.selectGetCupInfoForMedia(params);
    }

    @Transactional
    public int updateCupSubMatchEndFlag(Map<String, String> params) {
        return cupMapper.updateCupSubMatchEndFlag(params);
    }

    @Transactional
    public int updateCupMainMatchEndFlag(Map<String, String> params) {
        return cupMapper.updateCupMainMatchEndFlag(params);
    }

    public List<HashMap<String, Object>> selectGetCupChampions(Map<String, String> params) {
        return cupMapper.selectGetCupChampions(params);
    }

    public List<HashMap<String, Object>> selectSearchCupForChampion(Map<String, String> params) {
        return cupMapper.selectSearchCupForChampion(params);
    }

    public HashMap<String, Object> selectSearchCupResultForChampion(Map<String, String> params) {
        return cupMapper.selectSearchCupResultForChampion(params);
    }

    @Transactional
    public int insertCupResultForChampion(Map<String, String> params) {

        int result = 0;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> matchDataMap = objectMapper.readValue((String) params.get("matchData"), new TypeReference<Map<String, Object>>() {});

            List<HashMap<String, Object>> cParmas = new ArrayList<HashMap<String, Object>>();
            List<HashMap<String, Object>> dParmas = new ArrayList<HashMap<String, Object>>();

            List<String> cupDataList = (List<String>) matchDataMap.get("cupData");
            List<String> deleteDataList = (List<String>) matchDataMap.get("deleteData");

            if (cupDataList.size() > 0) {
                for (String cupDataItem : cupDataList) {
                    HashMap<String, Object> cupDataItemMap = objectMapper.readValue(cupDataItem, new TypeReference<HashMap<String, Object>>() {});
                    cupDataItemMap.put("infoId", params.get("cupId").toString());
                    cParmas.add(cupDataItemMap);
                }

                result = cupMapper.insertCupResultForChampion(cParmas);
            }

            if (deleteDataList.size() > 0) {
                for (String deleteDataItem : deleteDataList) {
                    HashMap<String, Object> deleteDataItemMap = objectMapper.readValue(deleteDataItem, new TypeReference<HashMap<String, Object>>() {});
                    String championId = deleteDataItemMap.get("championId").toString();
                    deleteDataItemMap.put("championId", championId);

                    dParmas.add(deleteDataItemMap);
                }

                cupMapper.updateCupResultForChampion(dParmas);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public void updateCupMatchInfo(MatchPlayDataDto playData, String ageGroup, int matchId, String level, String cupType) {

        Map<String, Object> map = new HashMap<>();
        String convertLevel = getLevel(level);

        String cupMatchTB = "";
        String cupMatchPlayDataTB = "";
        String cupMatchChangeDataTB = "";
        String cupStaffDataTB = "";
        String cupStaffPenaltyDataTB = "";
        String cupOwnGoalDataTB = "";

        int ord = playerMapper.selectLatestJoinKfaOrd();
        
        switch (cupType) {
            case "MAIN":
                cupMatchTB = ageGroup + "_Cup_Main_Match";
                cupMatchPlayDataTB = ageGroup + "_Cup_Main_Match_Play_Data";
                cupMatchChangeDataTB = ageGroup + "_Cup_Main_Match_Change_Data";
                cupStaffDataTB = ageGroup + "_Cup_Main_Staff_Data";
                cupStaffPenaltyDataTB = ageGroup + "_Cup_Main_Staff_Penalty_Data";
                cupOwnGoalDataTB = ageGroup + "_Cup_Main_Own_Goal_Data";

                /*기본적인 매치정보 업데이트*/
                map.put("cupMatchTB", cupMatchTB);
                map.put("matchId", matchId);
                map.put("playData", playData);
                cupMapper.updateMainMatchInfo(map);
                break;
            case "SUB":
                cupMatchTB = ageGroup + "_Cup_Sub_Match";
                cupMatchPlayDataTB = ageGroup + "_Cup_Sub_Match_Play_Data";
                cupMatchChangeDataTB = ageGroup + "_Cup_Sub_Match_Change_Data";
                cupStaffDataTB = ageGroup + "_Cup_Sub_Staff_Data";
                cupStaffPenaltyDataTB = ageGroup + "_Cup_Sub_Staff_Penalty_Data";
                cupOwnGoalDataTB = ageGroup + "_Cup_Sub_Own_Goal_Data";

                /*기본적인 매치정보 업데이트*/
                map.put("cupMatchTB", cupMatchTB);
                map.put("matchId", matchId);
                map.put("playData", playData);
                cupMapper.updateSubMatchInfo(map);
                break;
            case "TOUR":
                cupMatchTB = ageGroup + "_Cup_Tour_Match";
                cupMatchPlayDataTB = ageGroup + "_Cup_Tour_Match_Play_Data";
                cupMatchChangeDataTB = ageGroup + "_Cup_Tour_Match_Change_Data";
                cupStaffDataTB = ageGroup + "_Cup_Tour_Staff_Data";
                cupStaffPenaltyDataTB = ageGroup + "_Cup_Tour_Staff_Penalty_Data";
                cupOwnGoalDataTB = ageGroup + "_Cup_Tour_Own_Goal_Data";

                /*기본적인 매치정보 업데이트*/
                map.put("cupMatchTB", cupMatchTB);
                map.put("matchId", matchId);
                map.put("playData", playData);
                cupMapper.updateTourMatchInfo(map);
                break;
        }

        /*기존 경기결과 삭제*/
        Map<String, String> deletePlayDataMap = new HashMap<>();
        deletePlayDataMap.put("matchPlayDataOrChangeDataTB", cupMatchPlayDataTB);
        deletePlayDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchPlayDataOrChangeData(deletePlayDataMap);

        /*기존 교체선수 삭제*/
        Map<String, String> deleteChangeDataMap = new HashMap<>();
        deleteChangeDataMap.put("matchPlayDataOrChangeDataTB", cupMatchChangeDataTB);
        deleteChangeDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchPlayDataOrChangeData(deleteChangeDataMap);

        /*기존 감독,코치 삭제*/
        Map<String, String> deleteStaffDataMap = new HashMap<>();
        deleteStaffDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupStaffDataTB);
        deleteStaffDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffDataMap);

        /*기존 감독,코치 경고 삭제*/
        Map<String, String> deleteStaffPenaltyDataMap = new HashMap<>();
        deleteStaffPenaltyDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupStaffPenaltyDataTB);
        deleteStaffPenaltyDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffPenaltyDataMap);

        /*기존 자책골 삭제*/
        Map<String, String> deleteOwnGoalDataMap = new HashMap<>();
        deleteOwnGoalDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", cupOwnGoalDataTB);
        deleteOwnGoalDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteOwnGoalDataMap);

        /*선발 홈 플레이데이타 넣기*/
        final String finalCupMatchPlayDataTB = cupMatchPlayDataTB;
        playData.getHomePlaySelectionData().forEach(e ->{
            /*PlayerId Set*/
            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromPlayData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setSelCanGbn("sel");
            e.setLeagueMatchId(matchId);

            /* 주장일 경우 처리 */
            if (e.isCaptain()) {
                e.setCaptainFlag("1");
            } else {
                e.setCaptainFlag("0");
            }

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
            playDataInsertMap.put("playData", e);

            crawMapper.insertMatchPlayData(playDataInsertMap);
        });

        /*후발 홈 플레이데이타 넣기*/
        playData.getHomePlayCandidateData().forEach(e ->{
            /*PlayerId Set*/
            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromPlayData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setSelCanGbn("can");
            e.setLeagueMatchId(matchId);

            /* 주장일 경우 처리 */
            if (e.isCaptain()) {
                e.setCaptainFlag("1");
            } else {
                e.setCaptainFlag("0");
            }

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
            playDataInsertMap.put("playData", e);

            crawMapper.insertMatchPlayData(playDataInsertMap);
        });

        /*선발 어웨이 플레이데이타 넣기*/
        playData.getAwayPlaySelectionData().forEach(e ->{
            /*PlayerId Set*/
            e.setTeamName(playData.getAwayTeamName());
            setPlayerIdFromPlayData(convertLevel, e, ord);

            e.setHomeAwayGbn("away");
            e.setSelCanGbn("sel");
            e.setLeagueMatchId(matchId);

            /* 주장일 경우 처리 */
            if (e.isCaptain()) {
                e.setCaptainFlag("1");
            } else {
                e.setCaptainFlag("0");
            }

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
            playDataInsertMap.put("playData", e);

            crawMapper.insertMatchPlayData(playDataInsertMap);
        });

        /*후발 어웨이 플레이데이타 넣기*/
        playData.getAwayPlayCandidateData().forEach(e ->{
            /*PlayerId Set*/
            e.setTeamName(playData.getAwayTeamName());
            setPlayerIdFromPlayData(convertLevel, e, ord);

            e.setHomeAwayGbn("away");
            e.setSelCanGbn("can");
            e.setLeagueMatchId(matchId);

            /* 주장일 경우 처리 */
            if (e.isCaptain()) {
                e.setCaptainFlag("1");
            } else {
                e.setCaptainFlag("0");
            }

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", finalCupMatchPlayDataTB);
            playDataInsertMap.put("playData", e);

            crawMapper.insertMatchPlayData(playDataInsertMap);
        });

        /*홈팀 교체선수*/
        final String finalCupMatchChangeDataTB = cupMatchChangeDataTB;
        playData.getHomeChangeData().forEach(e -> {
            /*플레이어 아이디 SET*/
            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromChangeData(playData, convertLevel, e, "home", ord);

            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            /*교체데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("matchChangeDataTB", finalCupMatchChangeDataTB);
            insertMap.put("changeData", e);
            crawMapper.insertMatchChangeData(insertMap);
        });

        /*어웨이팀 교체선수*/
        playData.getAwayChangeData().forEach(e -> {
            /*플레이어 아이디 SET*/
            e.setTeamName(playData.getAwayTeamName());
            setPlayerIdFromChangeData(playData, convertLevel, e, "away", ord);

            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            /*교체데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("matchChangeDataTB", finalCupMatchChangeDataTB);
            insertMap.put("changeData", e);
            crawMapper.insertMatchChangeData(insertMap);
        });

        /*홈팀 감독, 코치 데이터*/
        final String finalCupStaffDataTB = cupStaffDataTB;
        playData.getHomeStaffData().forEach(e -> {
            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);



            /*감독 코치 데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffDataTB", finalCupStaffDataTB);
            insertMap.put("staffData", e);
            crawMapper.insertStaffData(insertMap);
        });

        /*어웨이팀 감독, 코치 데이터*/
        playData.getAwayStaffData().forEach(e -> {
            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            /*감독 코치 데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffDataTB", finalCupStaffDataTB);
            insertMap.put("staffData", e);
            crawMapper.insertStaffData(insertMap);
        });

        /*홈팀 감독, 코치 페널티 데이터*/
        final String finalCupStaffPenaltyDataTB = cupStaffPenaltyDataTB;
        playData.getHomeStaffPenaltyData().forEach(e -> {
            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            /*감독 코치 데이터 페널티 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffPenaltyDataTB", finalCupStaffPenaltyDataTB);
            insertMap.put("staffPenaltyData", e);
            crawMapper.insertStaffPenaltyData(insertMap);
        });

        /*어웨이팀 감독, 코치 페널티 데이터*/
        playData.getAwayStaffPenaltyData().forEach(e -> {
            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            /*감독 코치 데이터 페널티 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffPenaltyDataTB", finalCupStaffPenaltyDataTB);
            insertMap.put("staffPenaltyData", e);
            crawMapper.insertStaffPenaltyData(insertMap);
        });

        /*홈팀 자살골 데이터*/
        final String finalCupOwnGoalDataTB = cupOwnGoalDataTB;
        playData.getHomeOwnGoalData().forEach(e -> {

            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromOwnGoalData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupOwnGoalDataTB", finalCupOwnGoalDataTB);
            insertMap.put("ownGoalData", e);
            crawMapper.insertOwnGoalData(insertMap);
        });

        playData.getAwayOwnGoalData().forEach(e -> {

            e.setTeamName(playData.getAwayTeamName());
            setPlayerIdFromOwnGoalData(convertLevel, e, ord);

            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupOwnGoalDataTB", finalCupOwnGoalDataTB);
            insertMap.put("ownGoalData", e);
            crawMapper.insertOwnGoalData(insertMap);
        });

    }

    private void setPlayerIdFromPlayData(String convertLevel, PlayDataDto e, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", e.getPosition());
        playerSearchMap.put("playerName", e.getPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> playerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (playerIds.size() == 1) e.setPlayerId(playerIds.get(0));
    }

    private void setPlayerIdFromOwnGoalData(String convertLevel, OwnGoalDataDto e, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("playerName", e.getPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> playerIds = playerMapper.selectJoinkfaPlayerIdOwnGoal(playerSearchMap);
        if (playerIds.size() == 1) e.setPlayerId(playerIds.get(0));
    }

    private void setPlayerIdFromChangeData(MatchPlayDataDto playData, String convertLevel, ChangeDataDto e, String homeAwayGbn, int ord) {
        Map<String, String> playerSearchMap = new HashMap<>();
        String inPlayerPosition = "";
        String outPlayerPosition = "";
        if (homeAwayGbn.equals("home")) {
            inPlayerPosition = getHomeInPlayerPosition(playData, e);
            if (!convertLevel.equals("ES")) {
                /*초등경기에는 in 밖에 없음*/
                outPlayerPosition = getHomeOutPlayerPosition(playData, e);
            }
        } else if (homeAwayGbn.equals("away")) {
            inPlayerPosition = getAwayInPlayerPosition(playData, e);
            if (!convertLevel.equals("ES")) {
                outPlayerPosition = getAwayOutPlayerPosition(playData, e);
            }
        }


        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", inPlayerPosition);
        playerSearchMap.put("playerName", e.getInPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> inPlayerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (inPlayerIds.size() == 1) e.setInPlayerId(inPlayerIds.get(0));

        playerSearchMap.put("level", convertLevel);
        playerSearchMap.put("position", outPlayerPosition);
        playerSearchMap.put("playerName", e.getOutPlayerName());
        playerSearchMap.put("ord", String.valueOf(ord));
        playerSearchMap.put("teamName", e.getTeamName());
        List<Integer> outPlayerIds = playerMapper.selectJoinkfaPlayerId(playerSearchMap);
        /*선수명단에 있으면 선수명단 아이디 넣기*/
        if (outPlayerIds.size() == 1) e.setOutPlayerId(outPlayerIds.get(0));
    }

    private String getAwayInPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playCandidateDataDto = playData.getAwayPlayCandidateData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playCandidateDataDto) {
            playCandidateDataDto = playData.getAwayPlaySelectionData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
                .findFirst().get();
        }
        return playCandidateDataDto.getPosition();
    }

    private String getAwayOutPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playSelectionDataDto = playData.getAwayPlaySelectionData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playSelectionDataDto) {
            playSelectionDataDto = playData.getAwayPlayCandidateData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
                .findFirst().get();
        }
        return playSelectionDataDto.getPosition();
    }

    private String getHomeInPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playCandidateDataDto = playData.getHomePlayCandidateData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playCandidateDataDto) {
            playCandidateDataDto = playData.getHomePlaySelectionData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
                .findFirst().get();
        }
        return playCandidateDataDto.getPosition();
    }

    private String getHomeOutPlayerPosition(MatchPlayDataDto playData, ChangeDataDto e) {
        PlayDataDto playSelectionDataDto = playData.getHomePlaySelectionData()
            .stream()
            .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
            .findFirst().orElse(null);
        if (null == playSelectionDataDto) {
            playSelectionDataDto = playData.getHomePlayCandidateData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
                .findFirst().get();
        }
        return playSelectionDataDto.getPosition();
    }

    private String getLevel(String strValue) {
        switch (strValue) {
            case "51":
                return "ES";
            case "52":
                return "MS";
            case "53":
                return "HS";
            case "54":
                return "UV";
            default:
                return "";
        }
    }

    public List<HashMap<String, Object>> selectMatchPlayData(Map<String, String> params){
        return cupMapper.selectMatchPlayData(params);
    }

    public List<HashMap<String, Object>> selectMatchChangeData(Map<String, String> params){
        return cupMapper.selectMatchChangeData(params);
    }
    
    public HashMap<String, Object> selectTeamNameForCupMainMatchCnt(Map<String, String> params) {
    	return cupMapper.selectTeamNameForCupMainMatchCnt(params);
    }
    
    public HashMap<String, Object> selectTeamNameForCupSubMatchCnt(Map<String, String> params) {
    	return cupMapper.selectTeamNameForCupSubMatchCnt(params);
    }
    
    public HashMap<String, Object> selectTeamNameForCupTourMatchCnt(Map<String, String> params) {
    	return cupMapper.selectTeamNameForCupTourMatchCnt(params);
    }
    
    public HashMap<String, Object> selectTeamNameForCupTeamInSubMatchCnt(Map<String, String> params) {
    	return cupMapper.selectTeamNameForCupTeamInSubMatchCnt(params);
    }
    
    public HashMap<String, Object> selectTeamNameForCupTeamInMainMatchCnt(Map<String, String> params) {
    	return cupMapper.selectTeamNameForCupTeamInMainMatchCnt(params);
    }

    @Transactional
    public int updateTeamNameForCupMainMatchHome(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupMainMatchHome(params);
    }
    
    @Transactional
    public int updateTeamNameForCupMainMatchAway(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupMainMatchAway(params);
    }
    
    @Transactional
    public int updateTeamNameForCupSubMatchHome(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupSubMatchHome(params);
    }
    
    @Transactional
    public int updateTeamNameForCupSubMatchAway(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupSubMatchAway(params);
    }
    
    @Transactional
    public int updateTeamNameForCupTourMatchHome(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupTourMatchHome(params);
    }
    
    @Transactional
    public int updateTeamNameForCupTourMatchAway(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupTourMatchAway(params);
    }
    
    @Transactional
    public int updateTeamNameForCupTeamInMainMatch(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupTeamInMainMatch(params);
    }
    
    @Transactional
    public int updateTeamNameForCupTeamInSubMatch(Map<String, String> params) {
    	return cupMapper.updateTeamNameForCupTeamInSubMatch(params);
    }

    @Transactional
    public int updateTeamNameForChampion(Map<String, String> params) {
    	return cupMapper.updateTeamNameForChampion(params);
    }
    
    @Transactional
    public int updateCupSubMatchOne(Map<String, String> params) {
    	return cupMapper.updateCupSubMatchOne(params);
    }
    
    public List<HashMap<String, Object>> selectCupSubMatchForWin(Map<String, String> params) {
    	return cupMapper.selectCupSubMatchForWin(params);
    }
    
    public HashMap<String, Object> selectGetCupInfoForWin(Map<String, String> params) {
    	return cupMapper.selectGetCupInfoForWin(params);
    }
    
    @Transactional
    public int updateCupMainMatchOne(Map<String, String> params) {
    	return cupMapper.updateCupMainMatchOne(params);
    }
    
    public HashMap<String, Object> selectCupMainMatchForWin(Map<String, String> params) {
    	return cupMapper.selectCupMainMatchForWin(params);
    }
    
    public List<HashMap<String, Object>> selectCupSubMatchRankForWin(Map<String, Object> params) {
    	return cupMapper.selectCupSubMatchRankForWin(params);
    }
    
    public List<HashMap<String, Object>> selectCupMainMatchRankForWin(Map<String, Object> params) {
    	return cupMapper.selectCupSubMatchRankForWin(params);
    }

    public List<HashMap<String, Object>> selectProgressCupInfo(Map<String, String> params) {
    	return cupMapper.selectProgressCupInfo(params);
    }

    public List<HashMap<String, Object>> selectStaffData(Map<String, String> param) {
        return cupMapper.selectStaffData(param);
    }

    public List<HashMap<String, Object>> selectOwnGoalData(Map<String, String> param) {
        return cupMapper.selectOwnGoalData(param);
    }

    public List<HashMap<String, Object>> selectStaffPenaltyData(Map<String, String> param) {
        return cupMapper.selectStaffPenaltyData(param);
    }
    
    public List<MatchDataDto> selectCupTourMatchListCraw(Map<String, String> param) {
        return cupMapper.selectCupTourMatchListCraw(param);
    }
    
    public List<MatchDataDto> selectCupMainMatchListCraw(Map<String, String> param) {
        return cupMapper.selectCupMainMatchListCraw(param);
    }
    
    public List<MatchDataDto> selectCupSubMatchListCraw(Map<String, String> param) {
        return cupMapper.selectCupSubMatchListCraw(param);
    }
    
    @Transactional
    public void updateCupMatchInfoCraw(MatchPlayDataDto playData, String ageGroup, int matchId, String level, String cupType, String foreignId, List<MatchDataDto> matchList) {

        Map<String, Object> map = new HashMap<>();

        String cupMatchTB = "";
        
        MatchPlayDataDto.MatchStatus status = playData.getStatus();
        
    	try {
    		MatchDataDto match = matchList.stream()
                	.filter(item -> item.getMatchId().equals(playData.getMatchId())).findFirst().get();
                
                if (match != null) {
                	int homeScore = playData.getHomeScore();
                    int awayScore = playData.getAwayScore();
                    int homePk = playData.getHomePenaltyScore();
                    int awayPk = playData.getAwayPenaltyScore();
                    int homeBfScore = match.getHomeBfScore();
                    int awayBfScore = match.getAwayBfScore();
                    int homeBfPk = match.getHomePk();
                    int awayBfPk = match.getAwayPk();
                    int endFlag = match.getEndFlag();
                    int updFlag = 0;
                	// 이미 종료 처리한 경기는 업데이트 하지 않음
                    if (endFlag == 0) {
                    	switch (cupType) {
                        case "MAIN":
                        	// 수정 전 점수와 크롤링한 점수를 비교하여 다르면 수정
                        	// 같으면 수정하지 않음
                            if (homeScore != homeBfScore || awayScore != awayBfScore) {
                            	updFlag = 1;
                            	cupMatchTB = ageGroup + "_Cup_Main_Match";
                            	
                                if (homePk > 0 && awayPk > 0) {
                        			if (homeBfPk != homePk || awayBfPk != awayPk) {
                        				map.put("homePk", homePk);
                                        map.put("awayPk", awayPk);
                        			} else {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                        			}
                        			map.put("selMatchType", 1);
                        			
                        			if (updFlag == 1) {
                        				updFlag = 2;
                        			} else if (updFlag == 0) {
                        				updFlag = 3;
                        			}
                        		} else {
                        			if (homeBfPk > 0 && awayBfPk > 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 1);
                        			} else if (homeBfPk == 0 && awayBfPk == 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 0);
                        			}
                        		}
                            	
                            	if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                            		map.put("endFlag", 1);
                            	}
                            	
                            	map.put("homeScore", homeScore);
                                map.put("awayScore", awayScore);
                            	map.put("cupMainMatchTB", cupMatchTB);
                                map.put("cupMainMatchId", matchId);
                                map.put("updFlag", updFlag);
                                map.put("reason", "");
                                cupMapper.updateCupMainMatchScore(map);
                            }
                            break;
                        case "SUB":
                        	cupMatchTB = ageGroup + "_Cup_Sub_Match";
                    		if (homeScore != homeBfScore || awayScore != awayBfScore) {
                    			updFlag = 1;
                    			if (homePk > 0 && awayPk > 0) {
                        			if (homeBfPk != homePk || awayBfPk != awayPk) {
                        				map.put("homePk", homePk);
                                        map.put("awayPk", awayPk);
                        			} else {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                        			}
                        			map.put("selMatchType", 1);
                        			
                        			if (updFlag == 1) {
                        				updFlag = 2;
                        			} else if (updFlag == 0) {
                        				updFlag = 3;
                        			}
                        		} else {
                        			if (homeBfPk > 0 && awayBfPk > 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 1);
                        			} else if (homeBfPk == 0 && awayBfPk == 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 0);
                        			}
                        		}
                            	
                            	if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                            		map.put("endFlag", 1);
                            	}
                            	
                            	map.put("homeScore", homeScore);
                                map.put("awayScore", awayScore);
                            	map.put("cupSubMatchTB", cupMatchTB);
                            	map.put("cupSubMatchId", matchId);
                            	map.put("updFlag", updFlag);
                                map.put("reason", "");
                                cupMapper.updateCupSubMatchScore(map);
                    		} else {
                    			if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                    				Map<String, String> param = new HashMap<>();
                        			param.put("cupId", foreignId);
                        			param.put("cupSubMatchTB", cupMatchTB);
                        			param.put("cupSubMatchId", match.getMatchId());
                    				cupMapper.updateCupSubMatchEndFlag(param);
                            	}
                    		}
                            break;
                        case "TOUR":
                        	if (homeScore != homeBfScore || awayScore != awayBfScore) {
                        		
                        		updFlag = 1;
                        		
                        		cupMatchTB = ageGroup + "_Cup_Tour_Match";
                        		
                        		if (homePk > 0 && awayPk > 0) {
                        			if (homeBfPk != homePk || awayBfPk != awayPk) {
                        				map.put("homePk", homePk);
                                        map.put("awayPk", awayPk);
                        			} else {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                        			}
                        			map.put("selMatchType", 1);
                        			
                        			if (updFlag == 1) {
                        				updFlag = 2;
                        			} else if (updFlag == 0) {
                        				updFlag = 3;
                        			}
                        		} else {
                        			if (homeBfPk > 0 && awayBfPk > 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 1);
                        			} else if (homeBfPk == 0 && awayBfPk == 0) {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                            			map.put("selMatchType", 0);
                        			}
                        		}
                            	
                            	map.put("homeScore", homeScore);
                                map.put("awayScore", awayScore);
                            	map.put("cupTourMatchTB", cupMatchTB);
                            	map.put("cupTourMatchId", matchId);
                                map.put("updFlag", updFlag);
                                map.put("reason", "");
                                cupMapper.updateCupTourMatchScore(map);
                                
                                Map<String, String> matchParam = new HashMap<String, String>();
                                
                                if (match.getRound() != 2)  {
                                    //if (tourMatchInfo.get("home_score").toString().equals(tourMatchInfo.get("away_score").toString())) {
                                    if (homeScore == awayScore) {
                                        // 동점인 경우
                                        if (homePk > awayPk) {
                                            // home 팀이 pk 승
                                            if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                                matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getHome());
                                                matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                            } else {
                                                // 다음 매치 away일 때
                                                matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getHome());
                                                matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                            }
                                        } else {
                                            // away 팀이 pk 승
                                        	if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                        		matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getAway());
                                                matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                            } else {
                                                // away일 때
                                            	matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getAway());
                                                matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                            }
                                        }
                                    } else {
                                        if (homeScore > awayScore){
                                        	if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                        		matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getHome());
                                                matchParam.put("homeId", String.valueOf(match.getHomeId()));

                                            } else {
                                                // 다음 매치 away일 때
                                            	matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getHome());
                                                matchParam.put("awayId",  String.valueOf(match.getHomeId()));
                                            }
                                        } else {
                                        	if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                        		matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getAway());
                                                matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                            } else {
                                                // 다음 매치 away일 때
                                            	matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getAway());
                                                matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                            }
                                        }
                                    }
                                    matchParam.put("cupId", foreignId);
                                    matchParam.put("cupTourMatchTB", cupMatchTB);
                                    HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                    nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                    matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());

                                    if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                        map.put("endFlag", 1);
                                        map.put("fixFlag", 1);
                                        cupMapper.updateCupTourNextMatch(matchParam);
                                    }
                                }
                            } else {
                            	if (homePk > 0 && awayPk > 0) {
                            		cupMatchTB = ageGroup + "_Cup_Tour_Match";
                            		
                            		if (homeBfPk != homePk || awayBfPk != awayPk) {
                        				map.put("homePk", homePk);
                                        map.put("awayPk", awayPk);
                        			} else {
                        				map.put("homePk", homeBfPk);
                                        map.put("awayPk", awayBfPk);
                        			}
                        			map.put("selMatchType", 1);
                        			
                        			if (updFlag == 1) {
                        				updFlag = 2;
                        			} else if (updFlag == 0) {
                        				updFlag = 3;
                        			}
                                	
                                	map.put("homeScore", homeBfScore);
                                    map.put("awayScore", awayBfScore);
                                	map.put("cupTourMatchTB", cupMatchTB);
                                	map.put("cupTourMatchId", matchId);
                                	map.put("updFlag", updFlag);
                                    map.put("reason", "");
                                    cupMapper.updateCupTourMatchScore(map);
                                    
                                    Map<String, String> matchParam = new HashMap<String, String>();
                            		if (match.getRound() != 2)  {
                            			if (homePk > awayPk) {
                                            // home 팀이 pk 승
                                            if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                                matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getHome());
                                                matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                            } else {
                                                // 다음 매치 away일 때
                                                matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getHome());
                                                matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                            }
                                        } else {
                                            // away 팀이 pk 승
                                        	if (match.getNextTourPort() == 0) {
                                                // 다음 매치 home일 때
                                        		matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("home", match.getAway());
                                                matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                            } else {
                                                // away일 때
                                            	matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                matchParam.put("away", match.getAway());
                                                matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                            }
                                        }
                                		matchParam.put("cupId", foreignId);
                                        matchParam.put("cupTourMatchTB", cupMatchTB);
                                        HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                        nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                        matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());

                                        if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                                            map.put("endFlag", 1);
                                            map.put("fixFlag", 1);
                                            cupMapper.updateCupTourNextMatch(matchParam);
                                        }
                            		}
                            	} else {
                            		if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
                            			cupMatchTB = ageGroup + "_Cup_Tour_Match";
                            			Map<String, String> param = new HashMap<>();
                            			param.put("cupId", foreignId);
                            			param.put("cupTourMatchTB", cupMatchTB);
                            			param.put("cupTourMatchId", match.getMatchId());
                                		cupMapper.updateCupTourMatchFixFlag(param);
                                		 Map<String, String> matchParam = new HashMap<String, String>();
                                 		if (match.getRound() != 2)  {
                                 			if ((homeBfPk > awayBfPk) || (homeScore > awayScore)) {
                                                 // home 팀이 pk 승
                                                 if (match.getNextTourPort() == 0) {
                                                     // 다음 매치 home일 때
                                                     matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                     matchParam.put("home", match.getHome());
                                                     matchParam.put("homeId", String.valueOf(match.getHomeId()));
                                                 } else {
                                                     // 다음 매치 away일 때
                                                     matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                     matchParam.put("away", match.getHome());
                                                     matchParam.put("awayId", String.valueOf(match.getHomeId()));
                                                 }
                                             } else {
                                                 // away 팀이 pk 승
                                             	if (match.getNextTourPort() == 0) {
                                                     // 다음 매치 home일 때
                                             		matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                     matchParam.put("home", match.getAway());
                                                     matchParam.put("homeId", String.valueOf(match.getAwayId()));

                                                 } else {
                                                     // away일 때
                                                 	matchParam.put("nextTourNo", String.valueOf(match.getNextTourNo()));
                                                     matchParam.put("away", match.getAway());
                                                     matchParam.put("awayId", String.valueOf(match.getAwayId()));
                                                 }
                                             }
                                             matchParam.put("cupId", foreignId);
                                             matchParam.put("cupTourMatchTB", cupMatchTB);
                                             HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();
                                             nextMacthInfo = cupMapper.selectCupNextTourMatch(matchParam);
                                             matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());
                                             cupMapper.updateCupTourNextMatch(matchParam);
                                 		}
                                	}
                            	}
                            }
                            break;
                    	}
                    }
                }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public List<HashMap<String, Object>> selectCrawSettingCupList(Map<String, String> params) {
        return cupMapper.selectCrawSettingCupList(params);
    }
    public void updateCrawSetting(Map<String, String> params) {
        cupMapper.updateCrawSetting(params);
    }
}
