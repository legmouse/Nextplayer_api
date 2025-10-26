package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TeamMapper {

    List<HashMap<String, Object>> selectSearchTeamList(Map<String, Object> params);

    List<HashMap<String, Object>> selectSearchTeamList2(Map<String, String> params);

    List<HashMap<String, Object>> selectSearchTeam(Map<String, String> params);

    HashMap<String, Object> selectTeamListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamList(Map<String, String> params);

    int insertTeam(Map<String, String> params);

    int updateTeam(Map<String, String> params);

    int deleteTeam(Map<String, String> params);

    int insertTeamExcel(Map<String, String> params);

    HashMap<String, Object> selectTeamMgrListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamMgrList(Map<String, String> params);

    HashMap<String, Object> selectTeamInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamAvgGoal(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamListForMedia(Map<String, String> params);

    int selectTeamGroupListCount(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamGroupList(Map<String, String> params);

    int insertTeamGroup(Map<String, String> params);

    int updateTeamGroup(Map<String, String> params);

    int updateDeleteTeamGroup(Map<String, String> params);

    HashMap<String, Object> selectTeamGroupDetail(Map<String, String> params);

    int updateTeamForGroup(List<HashMap<String, Object>> params);

    int updateDeleteTeamForGroup(Map<String, String> params);

    int selectCheckTeamGroupName(Map<String, String> params);

    List<HashMap<String, Object>> selectCheckTeam(Map<String, String> params);

    int updateTeamGroupIdForTeam(Map<String, String> params);
    
    int updateTeamForNickName(Map<String, String> params);
    
    int updateTeamForTeamName(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamListForExcelDown(Map<String, String> param);

    int updateRemoveEmblem(Map<String, String> param);

    List<HashMap<String, Object>> selectTeamMgrListForExcelDown(Map<String, String> param);

    List<HashMap<String, Object>> selectTeamGroupListForExcelDown(Map<String, String> param);

    int deleteTeamGroup(Map<String, String> param);

    int deleteTeamGroupFromTeam(Map<String, String> param);
    
    List<HashMap<String, Object>> selectJoinKfaPlayerList(Map<String, String> params);
    
    List<HashMap<String, Object>> selectJoinKfaTeamList(Map<String, String> params);
    
    int updateJoinKfaTeam(Map<String, String> params);
    
    List<HashMap<String, Object>> selectPlayerList(Map<String, String> params);
    
    int insertJoinKfaPlayer(Map<String, String> params);
    
    int updateOriginalTeamGroupName(Map<String, String> params);
    
    int updateJoinKfaPlayer(Map<String, String> params);
    
    int updateDeleteJoinKfaPlayer(Map<String, String> params);
    
    int updateJoinKfaPlayerMapping(Map<String, String> params);
    
    List<HashMap<String, Object>> selectJoinKfaTeamListSearch(Map<String, String> params);
    
    int insertJoinKfaTeam(Map<String, String> params);
    
    int updateDeleteJoinKfaTeam(Map<String, String> params);
    
    int updateModJoinKfaTeam(Map<String, String> params);
    
    List<HashMap<String, Object>> selectPlayerToName(Map<String, String> params);
}
