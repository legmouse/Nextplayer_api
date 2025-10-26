package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.excel.ExcelRead2;
import kr.co.nextplayer.base.backend.excel.ExcelReadOption;
import kr.co.nextplayer.base.backend.mapper.PlayerMapper;
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
public class PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private FileService fileService;

    public int selectPlayerListCnt(Map<String, String> params) {
        return playerMapper.selectPlayerListCnt(params);
    }

    public List<HashMap<String, Object>> selectPlayerList(Map<String, String> params) {
        return playerMapper.selectPlayerList(params);
    }

    public HashMap<String, Object> selectPlayerInfo(Map<String, String> params) {
        return playerMapper.selectPlayerInfo(params);
    }

    public List<HashMap<String, Object>> selectPlayerHistoryDetail(Map<String, String> params) {
        return playerMapper.selectPlayerHistoryDetail(params);
    }

    public List<HashMap<String, Object>> selectSearchPlayerTeam(Map<String, String> params) {
        return playerMapper.selectSearchPlayerTeam(params);
    }

    @Transactional
    public int savePlayer(Map<String, String> params) {

        int playerInsertResult = playerMapper.insertPlayer(params);

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("playerId", params.get("playerId"));

        int eleCnt = Integer.parseInt(params.get("eleCnt"));
        int midCnt = Integer.parseInt(params.get("midCnt"));
        int highCnt = Integer.parseInt(params.get("highCnt"));
        int collegeCnt = Integer.parseInt(params.get("collegeCnt"));
        int proCnt = Integer.parseInt(params.get("proCnt"));

        List<Object> teamDataList = makePlayerList(params);
        mparams.put("list", teamDataList);

        if (teamDataList.size() > 0) {
            int historyResult = playerMapper.insertPlayerHistory(mparams);
        }

        return playerInsertResult;
    }

    @Transactional
    public int modifyPlayer(Map<String, String> params) {

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("playerId", params.get("playerId"));

        int updatePlayerResult = playerMapper.updatePlayer(params);
        int deletePlayerHistory = 0;
        int historyUpdate = 0;
        if (updatePlayerResult > 0) {
            deletePlayerHistory = playerMapper.deletePlayerHistory(params);

            List<Object> teamDataList = makePlayerList(params);
            mparams.put("list", teamDataList);
            if (teamDataList.size() > 0) {
                historyUpdate = playerMapper.insertPlayerHistory(mparams);
            }
        }

        return historyUpdate;
    }

    @Transactional
    public int deletePlayer(Map<String, String> params) {
        int deletePlayer = playerMapper.updateDeletePlayer(params);
        if (deletePlayer > 0) {
            playerMapper.updateDeletePlayerHistory(params);
        }
        return deletePlayer;
    }

    @Transactional
    public Map<String, Object> excelPlayerSave(MultipartHttpServletRequest request, Map<String, String> params) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        MultipartFile excelFile = request.getFile("excelFile");

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath + excelFile.getOriginalFilename());

        String excelFlag = params.get("excelFlag");

        String[] colums = StrUtil.makeExcelColumns(12);
        ExcelReadOption excelReadOption = StrUtil.excelUpload(request, colums);

        List<Map<String, Object>> excelContent = ExcelRead2.readPlayerExcel(request);

        //List<Map<String, Object>> finalParma = makeTeamList(excelContent);

        List<Map<String, Object>> playerFailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> teamFailList = new ArrayList<Map<String, Object>>();

        int index = 0;

        int insertPlayerResult = 0;

        if (excelContent.size() > 0) {
            // 엑셀 데이터 있을 경우
            for (Map<String, Object> map : excelContent) {
                Map<String, String> searchPlayerParam = new HashMap<String, String>();
                searchPlayerParam.put("name", map.get("name").toString());
                searchPlayerParam.put("birthday", map.get("birthday").toString());
                // 선수 중복 체크
                int checkPlayer = playerMapper.selectCheckPlayer(searchPlayerParam);

                if (checkPlayer > 0) {
                    Map<String, Object> failMap = new HashMap<String, Object>();
                    failMap.put("reason", "이미 등록된 선수");
                    failMap.put("name", map.get("name"));
                    failMap.put("birthday", map.get("birthday"));
                    playerFailList.add(failMap);
                    continue;
                }

                List<Map<String, String>> teamInfo = (List) map.get("teamInfo");    // 엑셀에서 만들어진 팀 정보
                List<Map<String ,String>> effectiveTeam = new ArrayList<Map<String, String>>(); // 유효한 팀 목록
                List<Map<String, String>> invalidTeam = new ArrayList<Map<String, String>>(); // 유효하지 않은 팀 목록

                if (teamInfo.size() > 0) {
                    // 엑셀에 등록된 팀 유효한지 체크
                    int u12RegOrder = 0;
                    int u15RegOrder = 0;
                    int u18RegOrder = 0;
                    int u22RegOrder = 0;
                    int proRegOrder = 0;

                    for (Map<String, String> teamMap : teamInfo) {
                        // obj.get("teamType") 으로 ageGroup 설정
                        String ageGroup = "";
                        if (teamMap.get("teamType").equals("0")) {
                            ageGroup = "U12";
                            teamMap.put("regOrder", Integer.toString(u12RegOrder));
                            u12RegOrder++;
                        } else if (teamMap.get("teamType").equals("1")) {
                            ageGroup = "U15";
                            teamMap.put("regOrder", Integer.toString(u15RegOrder));
                            u15RegOrder++;
                        } else if (teamMap.get("teamType").equals("2")) {
                            ageGroup = "U18";
                            teamMap.put("regOrder", Integer.toString(u18RegOrder));
                            u18RegOrder++;
                        } else if (teamMap.get("teamType").equals("3")) {
                            ageGroup = "U22";
                            teamMap.put("regOrder", Integer.toString(u22RegOrder));
                            u22RegOrder++;
                        } else if (teamMap.get("teamType").equals("4")) {
                            ageGroup = "Pro";
                            teamMap.put("regOrder", Integer.toString(proRegOrder));
                            proRegOrder++;
                        }

                        String teamName = teamMap.get("teamName").toString();

                        Map<String, String> teamSearchParam = new HashMap<String, String>();
                        teamSearchParam.put("ageGroup", ageGroup);
                        teamSearchParam.put("teamName", teamName);

                        List<HashMap<String, Object>> searchedTeamInfo = teamMapper.selectSearchTeam(teamSearchParam);

                        if(searchedTeamInfo.size() == 0) {
                            invalidTeam.add(teamSearchParam);
                            Map<String, Object> failMap = new HashMap<String, Object>();
                            failMap.put("reason", "등록되지 않는 팀");
                            failMap.put("data", teamSearchParam);
                            teamFailList.add(failMap);
                            continue;
                        } else if(searchedTeamInfo.size() > 1) {
                            invalidTeam.add(teamSearchParam);
                            Map<String, Object> failMap = new HashMap<String, Object>();
                            failMap.put("reason", "조회된 팀이 2개 이상");
                            failMap.put("data", teamSearchParam);
                            teamFailList.add(failMap);
                            continue;
                        } else {
                            teamMap.put("teamId", searchedTeamInfo.get(0).get("team_id").toString());
                            effectiveTeam.add(teamMap);
                        }
                    }
                }

                if(effectiveTeam.size() > 0 && teamFailList.size() == 0) {
                    // 유효한 팀이 있고, 팀도 이상 없을 때
                    Map<String, String> insertPlayerParam = new HashMap<String, String>();
                    insertPlayerParam.put("name", map.get("name").toString());
                    insertPlayerParam.put("position", map.get("position").toString());
                    insertPlayerParam.put("birthday", map.get("birthday").toString());
                    insertPlayerParam.put("useFlag", map.get("useFlag").toString());

                    insertPlayerResult += playerMapper.insertPlayer(insertPlayerParam);

                    if(insertPlayerResult > 0) {
                        for (Map<String, String> effectiveTeamMap: effectiveTeam) {
                            effectiveTeamMap.put("playerId", insertPlayerParam.get("playerId"));
                            playerMapper.insertPlayerHistoryForExcel(effectiveTeamMap);
                        }
                    }

                }

            }
        }

        resultMap.put("teamFailList", teamFailList);
        resultMap.put("playerFailList", playerFailList);
        resultMap.put("success", insertPlayerResult);
        return resultMap;
    }

    private List<Object> makePlayerList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        int trCnt = 0;

        for (int j = 0; j < 5; j ++) {

            if (j == 0) {
                trCnt = Integer.parseInt(params.get("eleCnt"));
            } else if (j == 1) {
                trCnt = Integer.parseInt(params.get("midCnt"));
            } else if (j == 2) {
                trCnt = Integer.parseInt(params.get("highCnt"));
            } else if (j == 3) {
                trCnt = Integer.parseInt(params.get("collegeCnt"));
            } else if (j == 4) {
                trCnt = Integer.parseInt(params.get("proCnt"));
            }

            for (int i = 0; i < trCnt; i ++) {
                HashMap<String, Object> map =new HashMap<String, Object>();
                if (!StrUtil.isEmpty(params.get("teamId" + j + "_" + i))) {
                    map.put("year", params.get("sYear" + j + "_" + i));
                    map.put("teamId", params.get("teamId" + j + "_" + i));
                    map.put("useFlag", params.get("useFlag" + j + "_" + i));
                    map.put("regOrder", i);
                    map.put("teamType", Integer.toString(j));

                    objList.add(map);
                }
            }
        }


        return objList;
    }

    public int selectRosterPlayerListCnt(Map<String, String> params) {
        return playerMapper.selectRosterPlayerListCnt(params);
    }

    public List<HashMap<String, Object>> selectRosterPlayerList(Map<String, String> params) {
        return playerMapper.selectRosterPlayerList(params);
    }

    public List<HashMap<String, Object>> selectSearchPlayerList(Map<String, String> params) {
        return playerMapper.selectSearchPlayerList(params);
    }

    public HashMap<String, Object> selectRosterInfo(Map<String, String> params) {
        return playerMapper.selectRosterInfo(params);
    }

    public List<HashMap<String, Object>> selectRosterPlayerDetail(Map<String, String> params) {
        return playerMapper.selectRosterPlayerDetail(params);
    }

    @Transactional
    public int saveRoster(Map<String, String> params, List<AttchFileInfoDto> files) {

        String roster = params.get("roster");

        if (roster.equals("national")) {
            params.put("rosterType", "0");
        } else if (roster.equals("golden")) {
            params.put("rosterType", "1");
        }

        int rosterInsertResult = playerMapper.insertRoster(params);

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("rosterId", params.get("rosterId"));

        List<Object> rosterPlayerList = makeRosterPlayerList(params);
        mparams.put("list", rosterPlayerList);

        if (rosterPlayerList.size() > 0) {
            int insertRosterPlayerResult = playerMapper.insertRosterPlayer(mparams);
        }

        if (files != null) {
            fileService.insertPlayerAttchFileInfo(files, params);
        }

        return rosterInsertResult;
    }

    private List<Object> makeRosterPlayerList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        int trCnt = StrUtil.isEmpty(params.get("trCnt")) ? 0 : Integer.parseInt(params.get("trCnt"));

        if (trCnt > 0) {
            for (int i = 0; i < trCnt; i ++) {
                HashMap<String, Object> map =new HashMap<String, Object>();
                if (!StrUtil.isEmpty(params.get("playerId" + i))) {
                    map.put("playerId", params.get("playerId" + i));
                    map.put("teamId", params.get("teamId" + i));
                    objList.add(map);
                }
            }
        }

        return objList;
    }

    @Transactional
    public int modifyRoster(Map<String, String> params, List<AttchFileInfoDto> files) {

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        mparams.put("rosterId", params.get("rosterId"));

        fileService.deleteAttchFileInfo(files, params);

        if (files != null) {
            params.put("foreignId", params.get("rosterId"));
            fileService.insertPlayerAttchFileInfo(files, params);
        }

        int updateRosterResult = playerMapper.updateRoster(params);
        int deleteRosterPlayer = 0;
        int rosterPlayerUpdate = 0;

        if (updateRosterResult > 0) {
            deleteRosterPlayer = playerMapper.deleteRosterPlayer(params);

            List<Object> rosterPlayerList = makeRosterPlayerList(params);
            mparams.put("list", rosterPlayerList);

            if (rosterPlayerList.size() > 0) {
                rosterPlayerUpdate = playerMapper.insertRosterPlayer(mparams);
            }

        }

        return rosterPlayerUpdate;

    }

    @Transactional
    public int updateDeleteRoster(Map<String, String> params) {

        int deleteRoster = playerMapper.updateDeleteRoster(params);

        if (deleteRoster > 0) {
            playerMapper.updateDeleteRosterPlayer(params);
        }

        return deleteRoster;
    }

    @Transactional
    public Map<String, Object> excelRosterSave(MultipartHttpServletRequest request, Map<String, String> params) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String roster = params.get("roster");

        MultipartFile excelFile = request.getFile("excelFile");

        String contentPath = StrUtil.getContentPath();
        File destFile = new File(contentPath + excelFile.getOriginalFilename());

        String excelFlag = params.get("excelFlag");

        String[] colums = StrUtil.makeExcelColumns(12);
        ExcelReadOption excelReadOption = StrUtil.excelUpload(request, colums);

        Map excelContent = ExcelRead2.readRosterExcel(request, roster);

        Map<String, String> rosterParam = (Map<String, String>) excelContent.get("rosterParam");
        List<Map<String, String>> playerData = (List<Map<String, String>>) excelContent.get("rosterPlayerParam");

        HashMap<String, Object> insertPlayerParam = new HashMap<String, Object>();
        HashMap<String, Object> insertFailParam = new HashMap<String, Object>();
        List successDataList = new ArrayList();
        List failDataList = new ArrayList();

        String rosterType = "0";
        if (roster.equals("national")) {
            rosterParam.put("rosterType", rosterType);
        } else if (roster.equals("golden")) {
            rosterType = "1";
            rosterParam.put("rosterType", rosterType);
        }

        List<Map<String, Object>> playerFailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> teamFailList = new ArrayList<Map<String, Object>>();

        int insertRosterResult = 0;

        if(excelContent != null) {
            if(playerData.size() > 0) {
                // 엑셀 선수 정보 체크
                List<Map<String ,String>> effectiveTeam = new ArrayList<Map<String, String>>(); // 유효한 팀 목록
                List<Map<String, String>> invalidTeam = new ArrayList<Map<String, String>>(); // 유효하지 않은 팀 목록
                for (Map<String, String> playerMap : playerData) {
                    int checkPlayer = playerMapper.selectCheckPlayer(playerMap);

                    if (checkPlayer == 0) {
                        Map<String, Object> failMap = new HashMap<String, Object>();
                        failMap.put("reason", "등록되지 않은 선수");
                        failMap.put("name", playerMap.get("name"));
                        failMap.put("birthday", playerMap.get("birthday"));
                        playerFailList.add(failMap);
                        playerMap.clear();
                        continue;
                    } else if (checkPlayer > 1) {
                        Map<String, Object> failMap = new HashMap<String, Object>();
                        failMap.put("reason", "중복되어 등록된 선수");
                        failMap.put("name", playerMap.get("name"));
                        failMap.put("birthday", playerMap.get("birthday"));
                        playerFailList.add(failMap);
                        playerMap.clear();
                        continue;
                    }

                    List<HashMap<String, Object>> searchedTeamInfo = teamMapper.selectSearchTeam(playerMap);

                    if(searchedTeamInfo.size() == 0) {
                        invalidTeam.add(playerMap);
                        Map<String, Object> failMap = new HashMap<String, Object>();
                        failMap.put("reason", "등록되지 않는 팀");
                        failMap.put("data", playerMap);
                        teamFailList.add(failMap);
                        continue;
                    } else if(searchedTeamInfo.size() > 1) {
                        invalidTeam.add(playerMap);
                        Map<String, Object> failMap = new HashMap<String, Object>();
                        failMap.put("reason", "조회된 팀이 2개 이상");
                        failMap.put("data", playerMap);
                        teamFailList.add(failMap);
                        continue;
                    } else {
                        playerMap.put("teamId", searchedTeamInfo.get(0).get("team_id").toString());
                        effectiveTeam.add(playerMap);
                    }
                }

                if(effectiveTeam.size() > 0 && teamFailList.size() == 0) {
                    insertRosterResult += playerMapper.insertRoster(rosterParam);
                    if (insertRosterResult > 0) {
                        for (Map<String, String> data : playerData) {
                            if (!data.isEmpty()) {
                                Map<String, Object> playerInfo = playerMapper.selectSearchRosterPlayer(data);
                                data.put("rosterId", rosterParam.get("rosterId"));
                                data.put("playerId", playerInfo.get("player_id").toString());
                                if (!StrUtil.isEmpty(data.get("rosterId")) && !StrUtil.isEmpty(data.get("playerId")) && !StrUtil.isEmpty(data.get("teamId"))) {
                                    playerMapper.insertRosterPlayerForExcel(data);
                                }
                            }
                        }
                    }
                }

            }
        }

        resultMap.put("teamFailList", teamFailList);
        resultMap.put("playerFailList", playerFailList);
        resultMap.put("success", insertRosterResult);
        return resultMap;
    }

    public List<Map<String, Object>> selectMainRosterList() {
        return playerMapper.selectMainRosterList();
    }

}
