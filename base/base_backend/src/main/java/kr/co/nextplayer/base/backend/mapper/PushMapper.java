package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PushMapper {

	List<Map<String, Object>> selectMemberByInterestAge(Map<String, String> param);

    int insertMemberPush(Map<String, Object> param);
    
    int insertMemberPushContent(Map<String, String> param);
    
    List<Map<String, Object>> selectMemberByInterestTeam(Map<String, String> param);
    
    List<HashMap<String, Object>> selectPushInfoList();
    
    int insertPush(Map<String, String> param);
    
    int updatePush(Map<String, String> param);
    
    HashMap<String, Object> selectPushInfo(Map<String, String> param);
    
    List<Map<String, Object>> selectMemberByPushAgree(Map<String, String> param);
    
    int updateCupSendFlag(Map<String, String> param);
    
    int updateSubMatchSendFlag(Map<String, String> param);
    
    int updateMainMatchSendFlag(Map<String, String> param);
    
    int updateTourMatchSendFlag(Map<String, String> param);
    
    List<HashMap<String, Object>> selectSendPushList(Map<String, String> params);
    
    HashMap<String, Object> selectSendPushListCount(Map<String, String> params);
    
    List<HashMap<String, Object>> selectPushListToSend();
    
    List<HashMap<String, Object>> selectMemberPushList(Map<String, String> params);
    
    HashMap<String, Object> selectMemberPushListCount(Map<String, String> params);
    
    HashMap<String, Object> selectPushDetail(Map<String, String> params);
}
