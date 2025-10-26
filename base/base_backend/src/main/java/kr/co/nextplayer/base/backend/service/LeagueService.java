package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.ChangeDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.MatchPlayDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.OwnGoalDataDto;
import kr.co.nextplayer.base.backend.dto.joinkfaCraw.PlayDataDto;
import kr.co.nextplayer.base.backend.mapper.CrawMapper;
import kr.co.nextplayer.base.backend.mapper.LeagueMapper;
import kr.co.nextplayer.base.backend.mapper.PlayerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeagueService {

    @Resource
    private LeagueMapper leagueMapper;

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private CrawMapper crawMapper;


    public List<HashMap<String, Object>> selectTeamEnterLeague(Map<String, String> params) {
        return leagueMapper.selectTeamEnterLeague(params);
    }

    public List<HashMap<String, Object>> selectTeamLeagueMatch(Map<String, String> params) {
        return leagueMapper.selectTeamLeagueMatch(params);
    }

    public List<HashMap<String, Object>> selectTeamEnterLeagueMatch(Map<String, String> params) {
        return leagueMapper.selectTeamEnterLeagueMatch(params);
    }

    public List<HashMap<String, Object>> selectTeamLeagueResultByYear(Map<String, String> params) {
        return leagueMapper.selectTeamLeagueResultByYear(params);
    }

    public List<HashMap<String, Object>> selectTeamLeagueRankByYear(Map<String, String> params) {
        return leagueMapper.selectTeamLeagueRankByYear(params);
    }

    public List<HashMap<String, Object>> selectTeamLeagueAvgGoalByYear(Map<String, String> params) {
        return leagueMapper.selectTeamLeagueAvgGoalByYear(params);
    }

    public List<HashMap<String, Object>> selectLeagueInfoList(Map<String, String> params) {
        return leagueMapper.selectLeagueInfoList(params);
    }

    public HashMap<String, Object> selectLeagueInfoListCount(Map<String, String> params) {
        return leagueMapper.selectLeagueInfoListCount(params);
    }

    public HashMap<String, String> selectGetLeagueInfo(Map<String, String> params) {
        return leagueMapper.selectGetLeagueInfo(params);
    }

    @Transactional
    public int insertLeagueInfo(Map<String, String> params) {
        return leagueMapper.insertLeagueInfo(params);
    }

    @Transactional
    public int insertLeagueInfoList(List<Map<String, String>> params) {
        int count = 0;
        for (Map<String, String> param : params) {
            // 연령별 테이블
            String leagueInfoTB = param.get("ageGroup") + "_League_Info";
            param.put("leagueInfoTB", leagueInfoTB);
            param.put("rankType", "0");
            count += leagueMapper.insertLeagueInfo(param);
        }
        return count;
    }

    @Transactional
    public int updateLeagueInfo(Map<String, String> params) {
        return leagueMapper.updateLeagueInfo(params);
    }

    @Transactional
    public int deleteLeagueInfo(Map<String, String> params) {
        return leagueMapper.deleteLeagueInfo(params);
    }

    public List<HashMap<String, Object>> selectLeagueTeamList(Map<String, String> params) {
        return leagueMapper.selectLeagueTeamList(params);
    }

    @Transactional
    public int insertLeagueTeam(Map<String, Object> params) {
        return leagueMapper.insertLeagueTeam(params);
    }

    @Transactional
    public int deleteLeagueTeam(Map<String, String> params) {
        return leagueMapper.deleteLeagueTeam(params);
    }

    public HashMap<String, Object> selectLeagueMatchListCount(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchListCount(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchList(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchList(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchCalendar(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchCalendar(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchListByHomeTeam(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchListByHomeTeam(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchListByAwayTeam(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchListByAwayTeam(params);
    }

    @Transactional
    public int insertLeagueMatch(Map<String, Object> params) {
        return leagueMapper.insertLeagueMatch(params);
    }

    @Transactional
    public int updateLeagueMatch(Map<String, String> params) {
        return leagueMapper.updateLeagueMatch(params);
    }

    @Transactional
    public int updateAllLeagueMatch(Map<String, Object> params) {
        return leagueMapper.updateAllLeagueMatch(params);
    }

    @Transactional
    public int deleteLeagueMatch(Map<String, String> params) {
        return leagueMapper.deleteLeagueMatch(params);
    }

    @Transactional
    public int deleteLeagueMatchOne(Map<String, Object> params) {
        return leagueMapper.deleteLeagueMatchOne(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchSchedule(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchSchedule(params);
    }

    public List<HashMap<String, Object>> selectLeagueMgrList(Map<String, String> params) {
        return leagueMapper.selectLeagueMgrList(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchRank(Map<String, String> params) {
        return leagueMapper.selectLeagueMatchRank(params);
    }

    public List<HashMap<String, Object>> selectLeagueFinalRank(Map<String, String> params) {
        return leagueMapper.selectLeagueFinalRank(params);
    }

    @Transactional
    public int insertLeagueFinalRank(Map<String, Object> params) {
        return leagueMapper.insertLeagueFinalRank(params);
    }

    @Transactional
    public int updateLeagueFinalRank(Map<String, Object> params) {
        return leagueMapper.updateLeagueFinalRank(params);
    }

    @Transactional
    public int deleteLeagueFinalRank(Map<String, Object> params) {
        return leagueMapper.deleteLeagueFinalRank(params);
    }

    public List<HashMap<String, Object>> selectListOfLeagueMatch(Map<String, String> params) {
        return leagueMapper.selectListOfLeagueMatch(params);
    }

    @Transactional
    public int insertLeagueTeamExcel(Map<String, String> params) {
        return leagueMapper.insertLeagueTeamExcel(params);
    }

    @Transactional
    public int insertLeagueInfoExcel(Map<String, Object> params) {
        return leagueMapper.insertLeagueInfoExcel(params);
    }

    @Transactional
    public int updateLeagueTeamExcelByleagueId(Map<String, String> params) {
        return leagueMapper.updateLeagueTeamExcelByleagueId(params);
    }

    @Transactional
    public int updateLeagueTeamExcel_type1(Map<String, Object> params) {
        return leagueMapper.updateLeagueTeamExcel_type1(params);
    }

    @Transactional
    public int updateLeagueTeamExcel_type2(Map<String, Object> params) {
        return leagueMapper.updateLeagueTeamExcel_type2(params);
    }

    @Transactional
    public int insertLeagueMatchExcel(Map<String, String> params) {
        return leagueMapper.insertLeagueMatchExcel(params);
    }

    @Transactional
    public int updateLeagueMatchExcelByLeagueId(Map<String, String> params) {
        return leagueMapper.updateLeagueMatchExcelByLeagueId(params);
    }

    @Transactional
    public int updateLeagueMatchExcel_home(Map<String, Object> params) {
        return leagueMapper.updateLeagueMatchExcel_home(params);
    }

    @Transactional
    public int updateLeagueMatchExcel_away(Map<String, Object> params) {
        return leagueMapper.updateLeagueMatchExcel_away(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchListForMedia(Map<String, String> params) {

        return leagueMapper.selectLeagueMatchListForMedia(params);
    }

    public List<HashMap<String, Object>> selectSearchLeagueInfoList(Map<String, String> params) {

        return leagueMapper.selectSearchLeagueInfoList(params);
    }

    public HashMap<String, Object> selectLeagueMatchForModifyResultRequest(Map<String, String> params) {

        return leagueMapper.selectLeagueMatchForModifyResultRequest(params);
    }

    /*public List<HashMap<String, Object>> selectGetLeagueInfoForMedia(Map<String, String> params) {

        return leagueMapper.selectGetLeagueInfoForMedia(params);
    }*/
    public HashMap<String, Object> selectGetLeagueInfoForMedia(Map<String, String> params) {

        return leagueMapper.selectGetLeagueInfoForMedia(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchScheduleForExcel(Map<String, String> params) {

        return leagueMapper.selectLeagueMatchScheduleForExcel(params);
    }

    @Transactional
    public void updateLeagueMatchInfo(MatchPlayDataDto playData, String ageGroup, int matchId, String level) {

        Map<String, Object> map = new HashMap<>();
        String convertLevel = getLevel(level);

        String leagueMatchTB = ageGroup + "_League_Match";
        String leagueMatchPlayDataTB = ageGroup + "_League_Match_Play_Data";
        String leagueMatchChangeDataTB = ageGroup + "_League_Match_Change_Data";
        String leagueStaffDataTB = ageGroup + "_League_Staff_Data";
        String leagueStaffPenaltyDataTB = ageGroup + "_League_Staff_Penalty_Data";
        String leagueOwnGoalDataTB = ageGroup + "_League_Own_Goal_Data";

        int ord = playerMapper.selectLatestJoinKfaOrd();

        /*기존 경기결과 삭제*/
        Map<String, String> deletePlayDataMap = new HashMap<>();
        deletePlayDataMap.put("matchPlayDataOrChangeDataTB", leagueMatchPlayDataTB);
        deletePlayDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchPlayDataOrChangeData(deletePlayDataMap);

        /*기존 교체선수 삭제*/
        Map<String, String> deleteChangeDataMap = new HashMap<>();
        deleteChangeDataMap.put("matchPlayDataOrChangeDataTB", leagueMatchChangeDataTB);
        deleteChangeDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchPlayDataOrChangeData(deleteChangeDataMap);

        /*기존 감독,코치 삭제*/
        Map<String, String> deleteStaffDataMap = new HashMap<>();
        deleteStaffDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", leagueStaffDataTB);
        deleteStaffDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffDataMap);

        /*기존 감독,코치 경고 삭제*/
        Map<String, String> deleteStaffPenaltyDataMap = new HashMap<>();
        deleteStaffPenaltyDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", leagueStaffPenaltyDataTB);
        deleteStaffPenaltyDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteStaffPenaltyDataMap);

        /*기존 자책골 삭제*/
        Map<String, String> deleteOwnGoalDataMap = new HashMap<>();
        deleteOwnGoalDataMap.put("staffDataOrStaffPenaltyDataOrOwnGoalDataTB", leagueOwnGoalDataTB);
        deleteOwnGoalDataMap.put("matchId", String.valueOf(matchId));
        crawMapper.deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(deleteOwnGoalDataMap);


        /*선발 홈 플레이데이타 넣기*/
        playData.getHomePlaySelectionData().forEach(e ->{
            /*PlayerId Set*/
            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromPlayData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setSelCanGbn("sel");
            e.setLeagueMatchId(matchId);

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", leagueMatchPlayDataTB);
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

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", leagueMatchPlayDataTB);
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

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", leagueMatchPlayDataTB);
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

            Map<String, Object> playDataInsertMap = new HashMap<>();
            playDataInsertMap.put("matchPlayDataTB", leagueMatchPlayDataTB);
            playDataInsertMap.put("playData", e);

            crawMapper.insertMatchPlayData(playDataInsertMap);
        });

        /*홈팀 교체선수*/
        playData.getHomeChangeData().forEach(e -> {
            /*플레이어 아이디 SET*/
            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromChangeData(playData, convertLevel, e, "home", ord);

            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            /*교체데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("matchChangeDataTB", leagueMatchChangeDataTB);
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
            insertMap.put("matchChangeDataTB", leagueMatchChangeDataTB);
            insertMap.put("changeData", e);
            crawMapper.insertMatchChangeData(insertMap);
        });

        /*홈팀 감독, 코치 데이터*/
        playData.getHomeStaffData().forEach(e -> {
            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            /*감독 코치 데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffDataTB", leagueStaffDataTB);
            insertMap.put("staffData", e);
            crawMapper.insertStaffData(insertMap);
        });

        /*어웨이팀 감독, 코치 데이터*/
        playData.getAwayStaffData().forEach(e -> {
            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            /*감독 코치 데이터 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffDataTB", leagueStaffDataTB);
            insertMap.put("staffData", e);
            crawMapper.insertStaffData(insertMap);
        });

        /*홈팀 감독, 코치 페널티 데이터*/
        playData.getHomeStaffPenaltyData().forEach(e -> {
            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            /*감독 코치 데이터 페널티 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffPenaltyDataTB", leagueStaffPenaltyDataTB);
            insertMap.put("staffPenaltyData", e);
            crawMapper.insertStaffPenaltyData(insertMap);
        });

        /*어웨이팀 감독, 코치 페널티 데이터*/
        playData.getAwayStaffPenaltyData().forEach(e -> {
            e.setHomeAwayGbn("away");
            e.setMatchId(matchId);

            /*감독 코치 데이터 페널티 인서트*/
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupStaffPenaltyDataTB", leagueStaffPenaltyDataTB);
            insertMap.put("staffPenaltyData", e);
            crawMapper.insertStaffPenaltyData(insertMap);
        });

        /*홈팀 자살골 데이터*/
        playData.getHomeOwnGoalData().forEach(e -> {

            e.setTeamName(playData.getHomeTeamName());
            setPlayerIdFromOwnGoalData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupOwnGoalDataTB", leagueOwnGoalDataTB);
            insertMap.put("ownGoalData", e);
            crawMapper.insertOwnGoalData(insertMap);
        });

        playData.getHomeOwnGoalData().forEach(e -> {

            e.setTeamName(playData.getAwayTeamName());
            setPlayerIdFromOwnGoalData(convertLevel, e, ord);

            e.setHomeAwayGbn("home");
            e.setMatchId(matchId);

            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("cupOwnGoalDataTB", leagueOwnGoalDataTB);
            insertMap.put("ownGoalData", e);
            crawMapper.insertOwnGoalData(insertMap);
        });


        /*기본적인 매치정보 업데이트*/
        map.put("leagueMatchTB", leagueMatchTB);
        map.put("leagueMatchId", matchId);
        map.put("playData", playData);
        leagueMapper.updateMatchInfo(map);
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
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
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
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
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
            playCandidateDataDto = playData.getAwayPlaySelectionData()
                .stream()
                .filter(d -> d.getPlayerNumber().equals(e.getOutPlayerNumber()))
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
                .filter(d -> d.getPlayerNumber().equals(e.getInPlayerNumber()))
                .findFirst().get();
        }
        return playSelectionDataDto.getPosition();
    }

    private String getLevel(String strValue) {
        switch (strValue) {
            case "1":
                return "ES";
            case "2":
                return "MS";
            case "3":
                return "HS";
            case "5":
                return "UV";
            default:
                return "";
        }
    }

    public List<HashMap<String, Object>> selectMatchPlayData(Map<String, String> params){
        return leagueMapper.selectMatchPlayData(params);
    }

    public List<HashMap<String, Object>> selectMatchChangeData(Map<String, String> params){
        return leagueMapper.selectMatchChangeData(params);
    }
    
    public HashMap<String, Object> selectTeamNameForLeagueMatchCnt(Map<String, String> params) {
    	return leagueMapper.selectTeamNameForLeagueMatchCnt(params);
    }
    
    public HashMap<String, Object> selectTeamNameForLeagueTeamCnt(Map<String, String> params) {
    	return leagueMapper.selectTeamNameForLeagueTeamCnt(params);
    }
    
    @Transactional
    public int updateTeamNameForLeagueMatchHome(Map<String, String> params) {
    	return leagueMapper.updateTeamNameForLeagueMatchHome(params);
    }
    
    @Transactional
    public int updateTeamNameForLeagueMatchAway(Map<String, String> params) {
    	return leagueMapper.updateTeamNameForLeagueMatchAway(params);
    }
    
    @Transactional
    public int updateTeamNameForLeagueTeam(Map<String, String> params) {
    	return leagueMapper.updateTeamNameForLeagueTeam(params);
    }

    public List<HashMap<String, Object>> selectLeagueMatchRankForWin(Map<String, Object> params) {
    	return leagueMapper.selectLeagueMatchRankForWin(params);
    }
    
    public HashMap<String, Object> selectGetLeagueInfoForWin(Map<String, String> params) {
    	return leagueMapper.selectGetLeagueInfoForWin(params);
    }

    public List<HashMap<String, Object>> selectSearchLeagueForChampion(Map<String, String> param) {
        return leagueMapper.selectSearchLeagueForChampion(param);
    }

    public List<HashMap<String, Object>> selectGetLeagueChampions(Map<String, String> param) {
        return leagueMapper.selectGetLeagueChampions(param);
    }

    @Transactional
    public int updateLeaguePrize(Map<String, String> param) {
        return leagueMapper.updateLeaguePrize(param);
    }

    @Transactional
    public int insertLeagueResultForChampion(Map<String, String> param) {

        int result = 0;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> matchDataMap = objectMapper.readValue((String) param.get("matchData"), new TypeReference<Map<String, Object>>() {});

            List<HashMap<String, Object>> lParmas = new ArrayList<HashMap<String, Object>>();
            List<HashMap<String, Object>> dParmas = new ArrayList<HashMap<String, Object>>();

            List<String> leagueDataList = (List<String>) matchDataMap.get("leagueData");
            List<String> deleteDataList = (List<String>) matchDataMap.get("deleteData");

            if (leagueDataList.size() > 0) {
                for (String leagueDataItem : leagueDataList) {
                    Map<String, String> leagueDataItemMap = objectMapper.readValue(leagueDataItem, new TypeReference<HashMap<String, String>>() {});
                    leagueDataItemMap.put("infoId", param.get("leagueId").toString());
                    result = leagueMapper.insertLeagueResultForChampion(leagueDataItemMap);
                }

            }

            if (deleteDataList.size() > 0) {
                for (String deleteDataItem : deleteDataList) {
                    HashMap<String, Object> deleteDataItemMap = objectMapper.readValue(deleteDataItem, new TypeReference<HashMap<String, Object>>() {});
                    String championId = deleteDataItemMap.get("championId").toString();
                    deleteDataItemMap.put("championId", championId);

                    dParmas.add(deleteDataItemMap);
                }

                leagueMapper.updateLeagueResultForChampion(dParmas);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<HashMap<String, Object>> selectProgressLeagueInfo(Map<String, String> param) {
        return leagueMapper.selectProgressLeagueInfo(param);
    }

    @Transactional
    public int updateShowFlagProgressLeague(Map<String, String> param) {
        return leagueMapper.updateShowFlagProgressLeague(param);
    }
    
    public List<MatchDataDto> selectLeagueMatchListCraw(Map<String, String> param) {
        return leagueMapper.selectLeagueMatchListCraw(param);
    }
    
    @Transactional
    public void updateLeagueMatchInfoCraw(MatchPlayDataDto playData, String ageGroup, int matchId, String level, String foreignId, List<MatchDataDto> matchList) {

        Map<String, Object> map = new HashMap<>();

        String leagueMatchTB = "";
        
        MatchPlayDataDto.MatchStatus status = playData.getStatus();
        
    	try {
    		MatchDataDto match = matchList.stream()
                	.filter(item -> item.getMatchId().equals(playData.getMatchId())).findFirst().get();
                
                if (match != null) {
                	int homeScore = playData.getHomeScore();
                    int awayScore = playData.getAwayScore();
                    int homeBfScore = match.getHomeBfScore();
                    int awayBfScore = match.getAwayBfScore();
                    MatchDataDto.MatchStatus bfstatus = match.getMatchStatus();
                    int updFlag = 0;
                	// 이미 종료 처리한 경기는 업데이트 하지 않음
                    // if (!bfstatus.toString().equals("END")) {
                    	if (homeScore != homeBfScore || awayScore != awayBfScore) {
                        	updFlag = 1;
                        	leagueMatchTB = ageGroup + "_League_Match";
                        	
//                        	if (status.toString().equals(MatchPlayDataDto.MatchStatus.END.toString())) {
//                        		map.put("matchStatus", "END");
//                        	} else if (status.toString().equals(MatchPlayDataDto.MatchStatus.START.toString())) {
//                        		map.put("matchStatus", "START");
//                        	}
                        	
                        	map.put("homeScore", homeScore);
                            map.put("awayScore", awayScore);
                        	map.put("leagueMatchTB", leagueMatchTB);
                            map.put("leagueMatchId", matchId);
                            map.put("updFlag", updFlag);
                            map.put("leagueId", foreignId);
                            leagueMapper.updateLeagueMatchScore(map);
                        }
                    // }
                }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


}
