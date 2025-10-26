package kr.co.nextplayer.base.backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nextplayer.base.backend.mapper.CupMapper;
import kr.co.nextplayer.base.backend.mapper.PushMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushService {

    @Resource
    private PushMapper pushMapper;
    
    @Resource
    private CupMapper cupMapper;
    
    @Transactional
    public int insertInterestTeamMemberPush(Map<String, String> param) {
    	
    	List<Map<String, Object>> memberList = pushMapper.selectMemberByInterestTeam(param);
    	
    	if (memberList.size() > 0) {
    		String cnt = String.valueOf(memberList.size());
            param.put("cnt", cnt);
            
            pushMapper.insertMemberPushContent(param);
            
            HashMap<String, Object> mparam = new HashMap<String, Object>();
            List<Object> ObjList = new ArrayList<Object>();
            
            memberList.forEach(m -> {
            	HashMap<String, Object> map = new HashMap<String, Object>();
            	map.put("memberCd", String.valueOf(m.get("member_cd")));
            	map.put("fcmToken", String.valueOf(m.get("fcm_token")));
            	map.put("pushContentId", param.get("pushContentId"));
            	ObjList.add(map);
            });
            
            if (ObjList.size() > 0) {
            	mparam.put("list", ObjList);
                pushMapper.insertMemberPush(mparam);
            }
    	}
        
        return memberList.size();
    }
    
    @Transactional
    public int insertInterestAgeGorupMemberPush(Map<String, String> param) {
        List<Map<String, Object>> memberList = pushMapper.selectMemberByInterestAge(param);
        if (!memberList.isEmpty()) {
        	String cnt = String.valueOf(memberList.size());
            param.put("cnt", cnt);
            pushMapper.insertMemberPushContent(param);
            
            HashMap<String, Object> mparam = new HashMap<String, Object>();
            List<Object> ObjList = new ArrayList<Object>();
            memberList.forEach(m -> {
            	HashMap<String, Object> map = new HashMap<String, Object>();
            	map.put("memberCd", String.valueOf(m.get("member_cd")));
            	map.put("fcmToken", String.valueOf(m.get("fcm_token")));
            	map.put("pushContentId", param.get("pushContentId"));
            	ObjList.add(map);
            });
            
            if (ObjList.size() > 0) {
            	mparam.put("list", ObjList);
                pushMapper.insertMemberPush(mparam);
            }
        }
        
        return memberList.size();
    }
    
//    @Transactional
//    public int insertMemberPush(Map<String, Object> param) {
//        List<Map<String, Object>> memberList = pushMapper.selectMemberByPushAgree(param);
//        int cnt = memberList.size();
//        param.put("cnt", cnt);
//        pushMapper.insertMemberPushContent(param);
//        memberList.forEach(m -> {
//            param.put("memberCd", String.valueOf(m.get("member_cd")));
//            param.put("fcmToken", String.valueOf(m.get("fcm_token")));
//            pushMapper.insertMemberPush(param);
//        });
//        
//        return memberList.size();
//    }

//    @Transactional
//    public int insertInterestMemberPush(Map<String, String> param) {
//        List<Map<String, Object>> memberList = configMapper.selectMemberByInterestAge(param.get("uage"));
//        memberList.forEach(m -> {
//            param.put("memberCd", String.valueOf(m.get("member_cd")));
//            param.put("fcmToken", String.valueOf(m.get("fcm_token")));
//            configMapper.insertMemberPush(param);
//        });
//        return memberList.size();
//    }
    
    @Transactional
    public int insertPush(Map<String, String> param) {
        return pushMapper.insertPush(param);
    }

    @Transactional
    public int updatePush(Map<String, String> param) {
        return pushMapper.updatePush(param);
    }

    public List<HashMap<String, Object>> selectPushInfoList() {
        return pushMapper.selectPushInfoList();
    }
    
    public HashMap<String, Object> selectPushInfo(Map<String, String> param) {
        return pushMapper.selectPushInfo(param);
    }
    
    @Transactional
    public int updateCupSendFlag(Map<String, String> param) {
    	return pushMapper.updateCupSendFlag(param);
    }
    
    @Transactional
    public int updateSubMatchSendFlag(Map<String, String> param) {
    	return pushMapper.updateSubMatchSendFlag(param);
    }
    
    @Transactional
    public int updateMainMatchSendFlag(Map<String, String> param) {
    	return pushMapper.updateMainMatchSendFlag(param);
    }
    
    @Transactional
    public int updateTourMatchSendFlag(Map<String, String> param) {
    	return pushMapper.updateTourMatchSendFlag(param);
    }
    
    public List<HashMap<String, Object>> selectSendPushList(Map<String, String> params) {
        return pushMapper.selectSendPushList(params);
    }
    
    public HashMap<String, Object> selectSendPushListCount(Map<String, String> params) {
        return pushMapper.selectSendPushListCount(params);
    }
    
    public List<HashMap<String, Object>> selectPushListToSend() {
        return pushMapper.selectPushListToSend();
    }
    
    @Transactional
    public int insertSendPush(Map<String, String> params) {
    	int result = 0;
    	result = pushMapper.insertMemberPushContent(params);
    	
    	List<Object> objList = new ArrayList<Object>();

        String szTotalCnt = (String)params.get("cnt");

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("pushContentId", params.get("pushContentId"));
            map.put("fcmToken", params.get("list[" + i + "][fcmToken]"));
            map.put("memberCd", params.get("list[" + i + "][memberCd]"));
            objList.add(map);
        }
        HashMap<String, Object> mparam = new HashMap<String, Object>();
        mparam.put("list", objList);
    	result = pushMapper.insertMemberPush(mparam);
    	return result;
    }

    @Transactional
    public int insertSendPushAll(Map<String, String> params, List<HashMap<String, Object>> memberList) {
        int result = 0;
        pushMapper.insertMemberPushContent(params);

        List<Object> objList = new ArrayList<Object>();

        memberList.forEach(member -> {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("pushContentId", params.get("pushContentId"));
            map.put("fcmToken", member.get("fcm_token"));
            map.put("memberCd", member.get("member_cd"));
            objList.add(map);
        });

        HashMap<String, Object> mparam = new HashMap<String, Object>();
        mparam.put("list", objList);
        result = pushMapper.insertMemberPush(mparam);
        return result;
    }
    
    public HashMap<String, Object> selectMemberPushListCount(Map<String, String> params) {
        return pushMapper.selectMemberPushListCount(params);
    }
    
    public List<HashMap<String, Object>> selectMemberPushList(Map<String, String> params) {
        return pushMapper.selectMemberPushList(params);
    }
    
    public HashMap<String, Object> selectPushDetail(Map<String, String> params) {
        return pushMapper.selectPushDetail(params);
    }
    
    public void insertPushLineup(String ageGroup, int matchId, String cupType, String cupId) {
    	
    	HashMap<String, String> param = new HashMap<>();
    	
    	param.put("cupId", cupId);
    	param.put("matchId", String.valueOf(matchId));
    	param.put("method", "LINEUP");
    	param.put("autoFlag", "0");
    	
    	HashMap<String, Object> matchInfo = null;
    	
    	String cupMatchTB = "";
    	
    	switch (cupType) {
        	case "MAIN":
        		cupMatchTB = ageGroup + "_Cup_Main_Match";
        		param.put("cupMainMatchId", cupMatchTB);
        		param.put("matchTB", cupMatchTB);
        		param.put("cupMainMatchId", String.valueOf(matchId));
        		matchInfo = cupMapper.selectCupMainMatchInfo(param);
        		break;
        	case "SUB":
        		cupMatchTB = ageGroup + "_Cup_Sub_Match";
        		param.put("cupSubMatchTB", cupMatchTB);
        		param.put("matchTB", cupMatchTB);
        		param.put("cupSubMatchId", String.valueOf(matchId));
        		matchInfo = cupMapper.selectCupSubMatchInfo(param);
        		break;
        	case "TOUR":
        		cupMatchTB = ageGroup + "_Cup_Tour_Match";
        		param.put("cupTourMatchTB", cupMatchTB);
        		param.put("matchTB", cupMatchTB);
        		param.put("cupTourMatchId", String.valueOf(matchId));
        		matchInfo = cupMapper.selectCupTourMatchInfo(param);
        		break;
    	}
    	
    	if (matchInfo != null) {
    		HashMap<String, Object> pushInfo = pushMapper.selectPushInfo(param);
    		
    		String typeId = pushInfo.get("type_id").toString();
    		String title = pushInfo.get("case_title").toString();
    		String body = pushInfo.get("case_text").toString();
    		
    		String homeId = matchInfo.get("home_id").toString();
    		String awayId = matchInfo.get("away_id").toString();
    		param.put("home", homeId);
    		param.put("away", awayId);
    		
    		String homeName = matchInfo.get("home").toString();
    		String awayName = matchInfo.get("away").toString();
    		body = body.replaceFirst("팀명", homeName);
    		body = body.replaceFirst("팀명", awayName);
    		
    		param.put("typeId", typeId);
    		param.put("title", title);
    		param.put("body", body);
    		
    		/*List<Map<String, Object>> memberList = pushMapper.selectMemberByInterestTeam(param);
    		
    		if (memberList.size() > 0) {
    			String cnt = String.valueOf(memberList.size());
                param.put("cnt", cnt);
                
                String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                param.put("date", today);
                
                pushMapper.insertMemberPushContent(param);
                
                HashMap<String, Object> mparam = new HashMap<String, Object>();
                List<Object> ObjList = new ArrayList<Object>();
                
                memberList.forEach(m -> {
                	HashMap<String, Object> map = new HashMap<String, Object>();
                	map.put("memberCd", String.valueOf(m.get("member_cd")));
                	map.put("fcmToken", String.valueOf(m.get("fcm_token")));
                	map.put("pushContentId", param.get("pushContentId"));
                	ObjList.add(map);
                });
                
                if (ObjList.size() > 0) {
                	mparam.put("list", ObjList);
                    pushMapper.insertMemberPush(mparam);
                    
                    switch (cupType) {
                		case "MAIN":
                			pushMapper.updateMainMatchSendFlag(param);
                			break;
                		case "SUB":
                			pushMapper.updateSubMatchSendFlag(param);
                			break;
                		case "TOUR":
                			pushMapper.updateTourMatchSendFlag(param);
                			break;
                    }
                }
    		}*/
    		
    	}
    }
}
