package kr.co.nextplayer.base.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.excel.ExcelRead2;
import kr.co.nextplayer.base.backend.excel.ExcelReadOption;
import kr.co.nextplayer.base.backend.mapper.TeamMapper;

import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamService {

    @Resource
    private TeamMapper teamMapper;

    public List<HashMap<String, Object>> selectSearchTeamList(Map<String, Object> params) {
        return teamMapper.selectSearchTeamList(params);
    }

    public List<HashMap<String, Object>> selectSearchTeamList2(Map<String, String> params) {
        return teamMapper.selectSearchTeamList2(params);
    }

    public HashMap<String, Object> selectTeamListCount(Map<String, String> params) {
        return teamMapper.selectTeamListCount(params);
    }

    public List<HashMap<String, Object>> selectTeamList(Map<String, String> params) {
        return teamMapper.selectTeamList(params);
    }

    @Transactional
    public int insertTeam(Map<String, String> params) {
        return teamMapper.insertTeam(params);
    }

    @Transactional
    public int updateTeam(Map<String, String> params) {
        return teamMapper.updateTeam(params);
    }

    @Transactional
    public int deleteTeam(Map<String, String> params) {
        return teamMapper.deleteTeam(params);
    }

    public int insertTeamExcel(Map<String, String> params) {
        return teamMapper.insertTeamExcel(params);
    }

    public HashMap<String, Object> selectTeamMgrListCount(Map<String, String> params) {
        return teamMapper.selectTeamMgrListCount(params);
    }

    public List<HashMap<String, Object>> selectTeamMgrList(Map<String, String> params) {
        return teamMapper.selectTeamMgrList(params);
    }

    public HashMap<String, Object> selectTeamInfo(Map<String, String> params) {
        return teamMapper.selectTeamInfo(params);
    }

    public List<HashMap<String, Object>> selectTeamAvgGoal(Map<String, String> params) {
        return teamMapper.selectTeamAvgGoal(params);
    }

    public List<HashMap<String, Object>> selectTeamListForMedia(Map<String, String> params) {
        return teamMapper.selectTeamListForMedia(params);
    }

    public int selectTeamGroupListCount(Map<String, String> params) {
        return teamMapper.selectTeamGroupListCount(params);
    }

    public List<HashMap<String, Object>> selectTeamGroupList(Map<String, String> params) {
        return teamMapper.selectTeamGroupList(params);
    }

    @Transactional
    public int insertTeamGroup(Map<String, String> params) {
        return teamMapper.insertTeamGroup(params);
    }

    @Transactional
    public int updateTeamGroup(Map<String, String> params) {
        return teamMapper.updateTeamGroup(params);
    }

    @Transactional
    public int deleteTeamGroup(Map<String, String> params) {
        teamMapper.deleteTeamGroupFromTeam(params);
        return teamMapper.updateDeleteTeamGroup(params);
    }

    public HashMap<String, Object> selectTeamGroupDetail(Map<String, String> params) {
        return teamMapper.selectTeamGroupDetail(params);
    }

    @Transactional
    public int insertTeamGroupDetail(Map<String, String> params) {

        int result = 0;

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> teamDataMap = objectMapper.readValue(params.get("teamData"), new TypeReference<Map<String, Object>>() {});

            List<String> teamDataList = (List<String>) teamDataMap.get("teamData");

            List<HashMap<String, Object>> tParmas = new ArrayList<HashMap<String, Object>>();

            if (teamDataList.size() > 0) {
                for (String teamDataItem : teamDataList) {
                    HashMap<String, Object> teamDataItemMap = objectMapper.readValue(teamDataItem, new TypeReference<HashMap<String, Object>>() {});
                    teamDataItemMap.put("teamGroupId", params.get("teamGroupId"));
                    tParmas.add(teamDataItemMap);
                }

            }
            System.out.println("tParmas = " + tParmas);
            result += teamMapper.updateTeamForGroup(tParmas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    public int updateDeleteTeamForGroup(Map<String, String> params) {
        return teamMapper.updateDeleteTeamForGroup(params);
    }

    @Transactional
    public Map<String, Object> insertExcelUploadTeamGroup(MultipartHttpServletRequest request, Map<String, String> params) {

        Map<String, Object> resultMap = new HashMap<String, Object>();


        MultipartFile excelFile = request.getFile("excelFile");

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath + excelFile.getOriginalFilename());

        String[] colums = StrUtil.makeExcelColumns(12);
        ExcelReadOption excelReadOption = StrUtil.excelUpload(request, colums);

        List<Map<String, Object>> excelContent = ExcelRead2.readTeamGroupExcel(request);

        List<Map<String, Object>> groupFailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> teamFailList = new ArrayList<Map<String, Object>>();

        int result = 0;

        for (Map<String, Object> map : excelContent) {
            int insertTeamGroupResult = 0;
            List<Map<String, String>> teamInfo = (List) map.get("teamInfo");

            Map<String, String> groupParam = new HashMap<String, String>();
            groupParam.put("groupName", map.get("groupName").toString());

            // 그룹명 중복 체크
            int checkGroupName = teamMapper.selectCheckTeamGroupName(groupParam);

            if (checkGroupName == 0) {

                List<Map<String ,String>> effectiveTeam = new ArrayList<Map<String, String>>(); // 유효한 팀 목록
                List<Map<String, String>> invalidTeam = new ArrayList<Map<String, String>>(); // 유효하지 않은 팀 목록

                if (teamInfo.size() > 0) {
                    for (Map<String, String> obj : teamInfo) {
                        // 연령과 팀명으로 Team 테이블에 데이터 있는지 확인 및 팀 갯수 체크
                        Map<String, String> teamParam = new HashMap<String, String>();
                        teamParam.put("uage", obj.get("uage").toString());
                        teamParam.put("nickName", obj.get("teamName").toString());

                        List<HashMap<String, Object>> checkTeam = teamMapper.selectCheckTeam(teamParam);
                        if (checkTeam.size() == 0) {
                            invalidTeam.add(teamParam);
                            Map<String, Object> failMap = new HashMap<String, Object>();
                            failMap.put("reason", "등록되지 않는 팀");
                            failMap.put("data", teamParam);
                            teamFailList.add(failMap);
                        } else if (checkTeam.size() > 1) {
                            invalidTeam.add(teamParam);
                            Map<String, Object> failMap = new HashMap<String, Object>();
                            failMap.put("reason", "조회된 팀이 2개 이상");
                            failMap.put("data", teamParam);
                            teamFailList.add(failMap);
                        } else if (!StrUtil.isEmpty(checkTeam.get(0).get("team_group_id"))) {
                            invalidTeam.add(teamParam);
                            Map<String, Object> failMap = new HashMap<String, Object>();
                            failMap.put("reason", "이미 그룹에 포함된 팀");
                            failMap.put("data", teamParam);
                            teamFailList.add(failMap);
                        } else {
                            teamParam.put("teamId", checkTeam.get(0).get("team_id").toString());
                            effectiveTeam.add(teamParam);
                        }
                    }
                }

                if (invalidTeam.size() == 0) {
                    groupParam.put("useFlag" , "0");
                    insertTeamGroupResult = teamMapper.insertTeamGroup(groupParam);

                    if (insertTeamGroupResult > 0) {
                        List<HashMap<String, Object>> teamParam = new ArrayList<HashMap<String, Object>>();
                        for (Map<String , String> obj : effectiveTeam) {
                            HashMap<String, Object> paramMap = new HashMap<String, Object>();
                            paramMap.put("teamGroupId", groupParam.get("teamGroupId"));
                            paramMap.put("childId", obj.get("teamId"));
                            teamParam.add(paramMap);
                        }
                        result += teamMapper.updateTeamForGroup(teamParam);
                    }
                }


            } else {
                Map<String, Object> failMap = new HashMap<String, Object>();
                failMap.put("reason", "그룹명 중복");
                failMap.put("data", groupParam.get("groupName"));
                groupFailList.add(failMap);
            }

        }

        resultMap.put("groupFailList", groupFailList);
        resultMap.put("teamFailList", teamFailList);
        resultMap.put("success", result);
        return resultMap;
    }

    @Transactional
    public int updateTeamForNickName(Map<String, String> params) {
    	return teamMapper.updateTeamForNickName(params);
    }

    @Transactional
    public int updateTeamForTeamName(Map<String, String> params) {
    	return teamMapper.updateTeamForTeamName(params);
    }

    public List<HashMap<String, Object>> selectTeamListForExcelDown (Map<String, String> param) {
        return teamMapper.selectTeamListForExcelDown(param);
    }

    @Transactional
    public int updateRemoveEmblem(Map<String, String> param) {
        return teamMapper.updateRemoveEmblem(param);
    }

    public List<HashMap<String, Object>> selectTeamMgrListForExcelDown (Map<String, String> param) {
        return teamMapper.selectTeamMgrListForExcelDown(param);
    }
    public List<HashMap<String, Object>> selectTeamGroupListForExcelDown (Map<String, String> param) {
        return teamMapper.selectTeamGroupListForExcelDown(param);
    }

    @Transactional
    public int removeTeamGroup(Map<String, String> param) {
        int result = 0;

        result = teamMapper.deleteTeamGroup(param);
        teamMapper.deleteTeamGroupFromTeam(param);

        return result;
    }
    
    public List<HashMap<String, Object>> selectJoinKfaPlayerList(Map<String, String> params) {
        return teamMapper.selectJoinKfaPlayerList(params);
    }
    
    public List<HashMap<String, Object>> selectJoinKfaTeamList(Map<String, String> params) {
        return teamMapper.selectJoinKfaTeamList(params);
    }

    @Transactional
    public int updateJoinKfaTeam(Map<String, String> param) {
        return teamMapper.updateJoinKfaTeam(param);
    }
    
    public List<HashMap<String, Object>> selectPlayerList(Map<String, String> params) {
        return teamMapper.selectPlayerList(params);
    }

    @Transactional
    public int insertJoinKfaPlayer(Map<String, String> param) {
        return teamMapper.insertJoinKfaPlayer(param);
    }

    @Transactional
    public int updateOriginalTeamGroupName(Map<String, String> params) {
    	return teamMapper.updateOriginalTeamGroupName(params);
    }

    @Transactional
    public int updateJoinKfaPlayer(Map<String, String> params) {
    	return teamMapper.updateJoinKfaPlayer(params);
    }

    @Transactional
    public int updateDeleteJoinKfaPlayer(Map<String, String> params) {
    	return teamMapper.updateDeleteJoinKfaPlayer(params);
    }

    @Transactional
    public int updateJoinKfaPlayerMapping(Map<String, String> params) {
    	return teamMapper.updateJoinKfaPlayerMapping(params);
    }
    
    public List<HashMap<String, Object>> selectJoinKfaTeamListSearch(Map<String, String> params) {
        return teamMapper.selectJoinKfaTeamListSearch(params);
    }

    @Transactional
    public int insertJoinKfaTeam(Map<String, String> params) {
    	return teamMapper.insertJoinKfaTeam(params);
    }

    @Transactional
    public int updateDeleteJoinKfaTeam(Map<String, String> params) {
    	return teamMapper.updateDeleteJoinKfaTeam(params);
    }

    @Transactional
    public int updateModJoinKfaTeam(Map<String, String> params) {
    	return teamMapper.updateModJoinKfaTeam(params);
    }
    
    public List<HashMap<String, Object>> selectPlayerToName(Map<String, String> params) {
    	return teamMapper.selectPlayerToName(params);
    }
}
