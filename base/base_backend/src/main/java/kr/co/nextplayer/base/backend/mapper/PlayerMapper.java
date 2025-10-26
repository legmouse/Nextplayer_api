package kr.co.nextplayer.base.backend.mapper;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PlayerMapper {

    int selectPlayerListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectPlayerList(Map<String, String> params);

    HashMap<String, Object> selectPlayerInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectPlayerHistoryDetail(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchPlayerTeam(Map<String, String> params);

    int insertPlayer(Map<String, String> params);

    int insertPlayerHistory(Map<String, Object> params);

    int insertPlayerHistoryForExcel(Map<String, String> params);

    int insertMatchFailPlayerHistory(Map<String, Object> params);

    int updatePlayer(Map<String, String> params);

    int updateDeletePlayer(Map<String, String> params);

    int updateDeletePlayerHistory(Map<String, String> params);

    int deletePlayerHistory(Map<String, String> params);

    int selectRosterPlayerListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectRosterPlayerList(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchPlayerList(Map<String, String> params);

    HashMap<String, Object> selectRosterInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectRosterPlayerDetail(Map<String, String> params);

    int insertRoster(Map<String, String> params);

    int insertRosterPlayer(HashMap<String, Object> mparams);

    int insertRosterPlayerForExcel(Map<String, String> mparams);

    int updateRoster(Map<String, String> params);

    int deleteRosterPlayer(Map<String, String> params);

    int updateDeleteRoster(Map<String, String> params);

    int updateDeleteRosterPlayer(Map<String, String> params);

    Map<String, Object> selectSearchRosterPlayer(Map<String, String> params);

    int insertMatchFailRosterPlayer(HashMap<String, Object> params);

    List<Integer> selectJoinkfaPlayerId(Map<String, String> params);

    List<Integer> selectJoinkfaPlayerIdOwnGoal(Map<String, String> params);

    int selectLatestJoinKfaOrd();

    int selectCheckPlayer(Map<String, String> params);

    int insertMainRosterData(Map<String, String> params);

    int deleteMainRosterData();

    List<Map<String, Object>> selectMainRosterList();
}
