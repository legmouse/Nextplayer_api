package kr.co.nextplayer.base.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linecorp.armeria.internal.shaded.fastutil.Hash;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import kr.co.nextplayer.base.backend.model.Uage;
import kr.co.nextplayer.base.backend.service.CupService;
import kr.co.nextplayer.base.backend.service.PushService;
import kr.co.nextplayer.base.backend.service.UageService;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class CupController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CupService cupService;

    @Resource
    private UageService uageService;
    
    @Resource
    private PushService pushService;

    @RequestMapping(value = "/cup")
    public String cup(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cup was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        // 검색
        String sCupName = params.get("sCupName");
        if (!StrUtil.isEmpty(sCupName)) {
            model.addAttribute("sCupName", sCupName);
        }

        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        HashMap<String, Object> countMap = cupService.selectCupInfoListCount(params);
        long totalCount = (Long) countMap.get("totalCount");

        int tp = 1;
        if (totalCount > 0) {
            tp = (int) totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " + totalCount + ", tp :" + tp + ", cp:" + cp
            + ", start : " + map.get("start") + ", end : " + map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end", map.get("end"));
        model.addAttribute("prev", map.get("prev"));
        model.addAttribute("next", map.get("next"));

        model.addAttribute("cp", cp); // 현재페이지번호
        model.addAttribute("cpp", cpp); // 현재페이지 갯수
        model.addAttribute("tp", tp); // 총 페이지 번호
        model.addAttribute("tc", totalCount); // 총 리스트 갯수
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */

        // 연령별 대회 리스트 getting cup list
        List<HashMap<String, Object>> cupInfoList = cupService.selectCupInfoList(params);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        model.addAttribute("cupInfoList", cupInfoList);
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sCupName", params.get("sCupName"));
        model.addAttribute("regionType", params.get("regionType"));

        return "cup/cup";
    }

    // 대회 - 등록/수정 > 대회정보 등록
    @RequestMapping(value = "/cupInfo")
    public String cupInfo(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupInfo was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
//		String leagueTeamTB = ageGroup + "_League_Team";
//		params.put("leagueTeamTB", leagueTeamTB);

        if (sFlag.equals(Define.MODE_FIX)) {// 수정
            // 대회 기본 정보
            HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
            model.addAttribute("cupInfoMap", cupInfoMap);
            
            params.put("method", "CUP");
            HashMap<String, Object> pushInfo = pushService.selectPushInfo(params);
            model.addAttribute("pushInfo", pushInfo);
        }

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "cup/cupInfo";
    }

    // 대회 - 등록/수정 > 대회정보 등록
    @RequestMapping(value = "/cupInfoCraw")
    public String cupInfoCraw(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupInfo was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "cup/cupInfoCraw";
    }

    /*
     * 대회정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupInfo", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupInfo(@RequestParam final Map<String, String> params, Model model,
                               RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupInfo params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            cupService.insertCupInfo(params);

        } else if (sFlag.equals(Define.MODE_FIX)) {// 수정
            cupService.updateCupInfo(params);

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            cupService.deleteCupInfo(params); // 타겟 테이블 데이터 삭제
        }
        /*
         * else if(sFlag.equals(Define.MODE_DATA_RESET)) {//연령별 전체삭제
         * sqlSessionInst.delete("delTeam", params); //타겟 테이블 데이터 전체 삭제 //파일 삭제 String
         * filePath = StrUtil.getContentPath(); StrUtil.deleteDirectory(new
         * File(filePath+"/emblem/"+ageGroup+"/")); }
         */

        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag)) {
            return "redirect:/" + mvFlag;
        }

        return "redirect:/cup";
    }

    @PostMapping(value = "/save_cupInfo_list")
    @ResponseBody
    public JSONObject save_cupInfo_list(@RequestBody List<Map<String, String>> params) {
        JSONObject resultObj = new JSONObject();
        int count = cupService.insertCupInfoList(params);
        resultObj.put("count", count);
        return resultObj;
    }


    // 대회 - 등록/수정 > 대회정보 개요등록
    @RequestMapping(value = "/cupPrize")
    public String cupPrize(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupPrize was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
//		String leagueTeamTB = ageGroup + "_League_Team";
//		params.put("leagueTeamTB", leagueTeamTB);

        if (sFlag.equals(Define.MODE_FIX)) {// 수정
            // 대회 기본 정보
            HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
            model.addAttribute("cupInfoMap", cupInfoMap);
            List<HashMap<String, Object>> championInfo = cupService.selectGetCupChampions(params);
            model.addAttribute("championInfo", championInfo);
        }

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "cup/cupPrize";
    }

    /*
     * 대회개요/수상 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupPrize", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupPrize(@RequestParam Map<String, String> params, Model model,
                                RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupPrize params : " + params);
        String sFlag = params.get("sFlag");
        String cupId = params.get("cupId");
//		String cp = params.get("cp");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("sFlag", sFlag);

        if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_FIX)) {// 등록, 수정
            cupService.updateCupPrize(params);
            cupService.insertCupResultForChampion(params);
        }

        String teamType = params.get("teamType");
        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag) && mvFlag.equals(Define.MODE_MOVE)) {
            if ((ageGroup.equals("U12") || ageGroup.equals("U11")) && !StrUtil.isEmpty(teamType)) {
                redirectAttributes.addAttribute("teamType", teamType);
                return "redirect:/cupTeam";
            } else {
                return "redirect:/cupTeam";
            }
        }

        return "redirect:/cupPrize";
    }

    // 대회 - 등록/수정 > 대회참가팀 등록
    @RequestMapping(value = "/cupTeam")
    public String cupTeam(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
    //public String cupTeam(@RequestParam Map<String, String> params, @RequestParam(value = "cupId") String cupId2, Model model, HttpServletResponse resp) {
        logger.info("cupTeam was called. params:" + params);
        //logger.info("cupTeam was called. cupId:" + cupId2);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);


        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);

        model.addAttribute("cupTeamList", cupTeamList);
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("teamType", params.get("teamType"));

        return "cup/cupTeam";
    }

    /*
     * 대회 참가팀 정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupTeam", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupTeam(@RequestParam final Map<String, String> params, Model model,
                               RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupTeam params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String cupId = params.get("cupId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);

        String teamType = params.get("teamType");

        if ((ageGroup.equals("U12") || ageGroup.equals("U11")) && !StrUtil.isEmpty(teamType)) {
            redirectAttributes.addAttribute("teamType", params.get("teamType"));
        }

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            HashMap<String, Object> mparams = new HashMap<String, Object>();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            try {

                if (!StrUtil.isEmpty(params.get("teamData"))) {
                    ObjectMapper mapper = new ObjectMapper();
                    JSONPObject json = new JSONPObject("JSON.parse", params.get("teamData"));

                    String jsonString = json.getValue().toString();
                    list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

                    System.out.println("list.size() = " + list.size());
                    List<Object> paramList = new ArrayList<>();
                    if (list.size() > 0) {

                        for (Map<String, Object> map: list) {
                            if (!StrUtil.isEmpty(map.get("nick_name")) && !StrUtil.isEmpty(map.get("team_id"))) {
                                map.put("cupTeamTB", cupTeamTB);
                                map.put("teamType", teamType);
                                map.put("cupId", cupId);

                                cupService.insertCupTeam(map);
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (sFlag.equals(Define.MODE_FIX)) {// 수정

            HashMap<String, Object> mparams = new HashMap<String, Object>();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            try {

                if(!params.get("cupType").equals("3")) {
                    // 대회 타입이 토너먼트가 아닐 경우
                    ObjectMapper mapper = new ObjectMapper();
                    JSONPObject json = new JSONPObject("JSON.parse", params.get("teamData"));

                    String jsonString = json.getValue().toString();
                    list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

                    System.out.println("list.size() = " + list.size());
                    List<Object> paramList = new ArrayList<>();
                    if (list.size() > 0) {

                        for (Map<String, Object> map: list) {
                            if (!StrUtil.isEmpty(map.get("nick_name")) && !StrUtil.isEmpty(map.get("team_id"))) {
                                map.put("cupTeamTB", cupTeamTB);
                                map.put("teamType", teamType);
                                map.put("cupId", cupId);
                                cupService.insertCupTeam(map);
                            }
                        }

                    }
                } else {
                    List<Object> teamList = makeCupTeamList(params);
                    if (teamList.size() > 0) {
                        for (Object map: teamList) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                            obj.put("cupTeamTB", cupTeamTB);
                            obj.put("cupId", cupId);
                            if (obj.get("newTab") == null) {
                                cupService.updateTourCupTeam(obj);
                            } else {
                                cupService.insertTourCupTeam(obj);
                            }

                        }
                    }
                    System.out.println("teamList = " + teamList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            /*Todo*/
            cupService.deleteCupTeam(params); // 해당 대회 참가팀 데이터 전체 삭제

        } else if (sFlag.equals(Define.MODE_DELETE_ONE)) {
            cupService.deleteCupTeamOne(params); // 해당 대회 참가팀 데이터 전체 삭제
        } else if (sFlag.equals(Define.MODE_FIX_ONE)) {
            cupService.updateCupTeamOne(params);
        }

        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag) && mvFlag.equals(Define.MODE_MOVE)) {
            return "redirect:/cupSubMatch";
        }

        return "redirect:/cupTeam";
    }

    // 대회 - 등록/수정 > 대회 예선 경기일정 등록
    @RequestMapping(value = "/cupSubMatch")
    public String cupSubMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupSubMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
//		// 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//		String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);


        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupSubMatchList = cupService.selectCupSubMatchList(params);
        model.addAttribute("cupSubMatchList", cupSubMatchList);

        // 대회 예선 경기일정 그룹 선택
        String groups = params.get("groups");
        if (StrUtil.isEmpty(groups)) {
            groups = "1";
        }
        model.addAttribute("groups", groups);
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("teamType", params.get("teamType"));

        model.addAttribute("cp", params.get("cp"));

        return "cup/cupSubMatch";
    }

    /*
     * 대회 예선 경기일정 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupSubMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupSubMatch(@RequestParam final Map<String, String> params, Model model,
                                   RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupSubMatch params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String cupId = params.get("cupId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String groups = params.get("groups");
        if (StrUtil.isEmpty(groups)) {
            groups = "1";
        }

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);

//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);

        try {

            if (sFlag.equals(Define.MODE_ADD)) { // 등록
            	List<Object> cupSubList = makeCupSubList(params);
            	if (cupSubList.size() > 0) {
                    for (int i = 0; i < cupSubList.size(); i++) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(cupSubList.get(i), Map.class);
                        obj.put("cupId", cupId);
                        obj.put("groups", groups);
                        obj.put("match_date", params.get("date" + i));
                        obj.put("time", params.get("time" + i));
                        obj.put("home_id", params.get("selHome" + i));
                        obj.put("away_id", params.get("selAway" + i));
                        obj.put("matchType", 0);
                        cupSubList.set(i, obj);
                    }
                }
            	HashMap<String, Object> mparams = new HashMap<String, Object>();
            	if (cupSubList.size() > 0) {
            		mparams.put("cupSubMatchTB", cupSubMatchTB);
            		mparams.put("excelContent", cupSubList);
            		cupService.insertCupSubMatch(mparams);
//                    for (Object map: cupSubList) {
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
//                        obj.put("cupSubMatchTB", cupSubMatchTB);
//                        obj.put("cupId", cupId);
//
//                        cupService.insertCupSubMatch(obj);
//                    }
                }
            	
            	redirectAttributes.addAttribute("cp", cp);

            } else if (sFlag.equals(Define.MODE_FIX)) {// 수정


                HashMap<String, Object> mparams = new HashMap<String, Object>();

                mparams.put("cupSubMatchTB", cupSubMatchTB);
                mparams.put("groups", params.get("groups"));
                List<Object> cupSubList = makeCupSubList(params);

                if (cupSubList.size() > 0) {
                    for (Object map: cupSubList) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                        obj.put("cupSubMatchTB", cupSubMatchTB);
                        obj.put("cupId", cupId);
                        
                        if (!StrUtil.isEmpty(obj.get("cupSubMatchId"))) {
                        	cupService.updateAllCupSubMatch(obj);
                        }
                    }
                    
                    List<Object> objList = new ArrayList<Object>();
                    

                    for (int i = 0; i < cupSubList.size(); i++) {
                    	ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(cupSubList.get(i), Map.class);
                    	if (StrUtil.isEmpty(obj.get("cupSubMatchId"))) {
                    		obj.put("cupId", cupId);
                            obj.put("groups", groups);
                            obj.put("match_date", params.get("date" + i));
                            obj.put("time", params.get("time" + i));
                            obj.put("home_id", params.get("selHome" + i));
                            obj.put("away_id", params.get("selAway" + i));
                            obj.put("matchType", 0);
                            objList.add(obj);
                    	}
                    }
                    
                    if (objList.size() > 0) {
                    	HashMap<String, Object> mmparams = new HashMap<String, Object>();
                        mmparams.put("cupSubMatchTB", cupSubMatchTB);
                        mmparams.put("excelContent", objList);
                		cupService.insertCupSubMatch(mmparams);
                    }
                }

                /*System.out.println("-- leagueTeamList : " + cupSubList);
                mparams.put("cupId", cupId);
                mparams.put("cupSubMatchTB", cupSubMatchTB);
                mparams.put("list", cupSubList);
                cupService.updateAllCupSubMatch(mparams);*/

                redirectAttributes.addAttribute("cp", cp);
                redirectAttributes.addAttribute("sFlag", sFlag);

            } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
                cupService.deleteCupSubMatch(params); // 해당 대회 예선경기 데이터 전체 삭제

            } else if (sFlag.equals(Define.MODE_DELETE_ONE)) {
                cupService.deleteCupSubMatchOne(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String redirectPath = "";
        if (!StrUtil.isEmpty(params.get("teamType"))) {
            redirectPath = "redirect:/cupSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&teamType=" + params.get("teamType") + "&modify=1";
        } else {
            redirectPath = "redirect:/cupSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&modify=1";
        }


        return redirectPath;
    }

    // 대회 - 등록/수정 > 대회 본선 참가팀 등록
    @RequestMapping(value = "/cupMainTeam")
    public String cupMainTeam(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMainTeam was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupSubMatchList = cupService.selectCupSubMatchAllList(params);
        model.addAttribute("cupSubMatchList", cupSubMatchList);

        // 대회 본선 참가팀 정보
        List<HashMap<String, Object>> cupMainTeamList = cupService.selectCupMainTeamListByGroups(params);
        model.addAttribute("cupMainTeamList", cupMainTeamList);

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "cup/cupMainTeam";
    }

    /*
     * 대회 본선 참가팀 정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupMainTeam", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMainTeam(@RequestParam final Map<String, String> params, Model model,
                                   RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMainTeam params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String cupId = params.get("cupId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);

        try {
            if (sFlag.equals(Define.MODE_ADD)) { // 등록

            } else if (sFlag.equals(Define.MODE_FIX)) {// 수정

                cupService.deleteCupSubMatchGroups(params); // 해당 대회 예선경기 데이터 전체 삭제

                HashMap<String, Object> mparams = new HashMap<String, Object>();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                ObjectMapper mapper = new ObjectMapper();
                JSONPObject json = new JSONPObject("JSON.parse", params.get("subMatchData"));

                String jsonString = json.getValue().toString();
                list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

                System.out.println("list.size() = " + list.size());
                List<Object> paramList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).put("cupId", Integer.parseInt(cupId));
                    list.get(i).put("matchType", Integer.parseInt(list.get(i).get("matchType").toString()));
                    list.get(i).put("home_id", Integer.parseInt(list.get(i).get("home_id").toString()));
                    list.get(i).put("away_id", Integer.parseInt(list.get(i).get("away_id").toString()));
                    list.get(i).put("groups", Integer.parseInt(list.get(i).get("groups").toString()));
                    paramList.add(list.get(i));
                }

                //mparams.put("cupSubMatchTB", cupSubMatchTB);

                mparams.put("excelContent", paramList);

                //mparams.put("cupTeamTB", cupTeamTB);
                cupService.insertCupSubMatch(mparams);

            } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
                cupService.updateCupMainTeamReset(params); // 해당 대회 참가팀 데이터 전체 초기
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/cupMainTeam";
    }


    // 대회 - 등록/수정 > 대회 본선 경기일정 등록
    @RequestMapping(value = "/cupMainMatch")
    public String cupMainMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMainMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
//		// 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//		String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupSubMatchList = cupService.selectCupSubMatchAllList(params);
        model.addAttribute("cupSubMatchList", cupSubMatchList);

        // 대회 본선 참가팀 정보
        List<HashMap<String, Object>> cupMainTeamList = cupService.selectCupMainTeamListByGroups(params);
        model.addAttribute("cupMainTeamList", cupMainTeamList);

        // 대회 본선 경기일정 정보
        List<HashMap<String, Object>> cupMainMatchList = cupService.selectCupMainMatchList(params);
        model.addAttribute("cupMainMatchList", cupMainMatchList);

        // 대회 본선 경기일정 그룹 선택
        String groups = params.get("groups");
        if (!StrUtil.isEmpty(groups)) {
            model.addAttribute("groups", groups);
        }

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "cup/cupMainMatch";
    }

    /*
     * 대회 본선 경기일정 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_cupMainMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMainMatch(@RequestParam final Map<String, String> params, Model model,
                                    RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMainMatch params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String cupId = params.get("cupId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
//
//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);

        if (sFlag.equals(Define.MODE_ADD)) { // 등록

        } else if (sFlag.equals(Define.MODE_FIX)) {// 수정

            HashMap<String, Object> mparams = new HashMap<String, Object>();

            mparams.put("cupMainMatchTB", cupMainMatchTB);
            mparams.put("groups", params.get("groups"));

            List<Object> cupTourList = makeCupMainList(params);

            if (cupTourList.size() > 0) {
                for (Object map: cupTourList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupMainMatchTB", cupMainMatchTB);
                    obj.put("cupId", cupId);

                    cupService.updateAllCupMainMatch(obj);
                }
            }

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            cupService.deleteCupMainMatch(params); // 해당 대회 본선 경기일정 데이터 전체 삭제

        }

        return "redirect:/cupMainMatch";
    }

    // 대회 - 등록/수정 > 대회 토너먼트 등록
    @RequestMapping(value = "/cupTourMatch")
    public String cupTourMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupTourMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupSubMatchList = cupService.selectCupSubMatchAllList(params);
        model.addAttribute("cupSubMatchList", cupSubMatchList);

        // 대회 본선 참가팀 정보
        List<HashMap<String, Object>> cupMainTeamList = cupService.selectCupMainTeamListByGroups(params);
        model.addAttribute("cupMainTeamList", cupMainTeamList);

        // 대회 본선 경기일정 정보
        List<HashMap<String, Object>> cupMainMatchList = cupService.selectCupMainMatchAllList(params);
        model.addAttribute("cupMainMatchList", cupMainMatchList);


        // 토너먼트 강수
        String round = params.get("round");
        if(StrUtil.isEmpty(round)) {
            round = String.valueOf(cupInfoMap.get("tour_team"));
            params.put("round", round);
        }
        model.addAttribute("rounds", round);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupTourMatchList = cupService.selectCupTourMatchList(params);
        model.addAttribute("cupTourMatchList", cupTourMatchList);

        // 대회 경기일정 그룹 선택
        String groups = params.get("groups");
        if (!StrUtil.isEmpty(groups)) {
            model.addAttribute("groups", groups);
        }

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("cp", params.get("cp"));

        return "cup/cupTourMatch";
    }


    @RequestMapping(value = "/save_cupTourMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupTourMatch(@RequestParam final Map<String, String> params, Model model,
                                    RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMainMatch params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String cupId = params.get("cupId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String round = params.get("round");

        // 대회 본선 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);
//
//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);

        try {
            if (sFlag.equals(Define.MODE_ADD)) { // 등록


            } else if (sFlag.equals(Define.MODE_FIX)) {// 수정


                HashMap<String, Object> mparams = new HashMap<String, Object>();

                mparams.put("cupTourMathTB", cupTourMatchTB);
                mparams.put("groups", params.get("groups"));

                List<Object> cupTourList = makeCupTourList(params);

                mparams.put("cupId", cupId);
                mparams.put("list", cupTourList);


                if (cupTourList.size() > 0) {
                    for (Object map: cupTourList) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                        obj.put("cupTourMatchTB", cupTourMatchTB);
                        obj.put("cupId", cupId);

                        cupService.updateAllCupTourMatch(obj);
                    }
                }

                //cupService.updateAllCupTourMatch(mparams);

            } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
                cupService.delteCupTourMatch(params); // 해당 대회 본선 경기일정 데이터 전체 삭제
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return "redirect:/cupTourMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&round=" + groups;
        return "redirect:/cupTourMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&round=" + round;
    }

    // 대회 - 관리 > 대회 경기관리 리스트
    @RequestMapping(value = "/cupMgr")
    public String cupMgr(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMgr was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
        // 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 검색
        String sCupName = params.get("sCupName");
        if (!StrUtil.isEmpty(sCupName)) {
            model.addAttribute("sCupName", sCupName);
        }

        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        HashMap<String, Object> countMap = cupService.selectCupInfoListCount(params);
        long totalCount = (Long) countMap.get("totalCount");

        int tp = 1;
        if (totalCount > 0) {
            tp = (int) totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);
        logger.info(" ------ [team list paging]  totalCount : " + totalCount + ", tp :" + tp + ", cp:" + cp
            + ", start : " + map.get("start") + ", end : " + map.get("end"));

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end", map.get("end"));
        model.addAttribute("prev", map.get("prev"));
        model.addAttribute("next", map.get("next"));

        model.addAttribute("cp", cp); // 현재페이지번호
        model.addAttribute("cpp", cpp); // 현재페이지 갯수
        model.addAttribute("tp", tp); // 총 페이지 번호
        model.addAttribute("tc", totalCount); // 총 리스트 갯수
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */

        // 연령별 대회 리스트 getting league list
        //cupInfoList
        List<HashMap<String, Object>> cupMgrList = cupService.selectCupMgrList(params);


        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        model.addAttribute("cupMgrList", cupMgrList);
        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sCupName", params.get("sCupName"));

        return "cup/cupMgr";
    }

    // 대회 - 관리 > 대회 예선 결과 등록
    @RequestMapping(value = "/cupMgrSubMatch")
    public String cupMgrSubMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMgrSubMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
//		// 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//		String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);


        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 예선 참가팀 경기 순위 정보
        List<HashMap<String, Object>> cupSubMatchRank = cupService.selectCupSubMatchRank(params);
        List<HashMap<String, Object>> beforeRank = cupSubMatchRank.stream().collect(Collectors.toList());
        
        HashMap<String, Object> cupInfo = cupService.selectGetCupInfoForWin(params);
        
        if (cupSubMatchRank.size() > 0) {
        	if (cupInfo.get("rank_type") != null && (int) cupInfo.get("rank_type") == 1) {
        		List<String> pointList = new ArrayList<>();
            	for (int i = 0; i < cupSubMatchRank.size(); i++) {
            		for (int j = 1; j < cupSubMatchRank.size(); j++) {
            			if (i != j) {
            				BigDecimal team1Point = (BigDecimal) cupSubMatchRank.get(i).get("rankPoint");
            				BigDecimal team2Point = (BigDecimal) cupSubMatchRank.get(j).get("rankPoint");
            				
                			if (team1Point.compareTo(team2Point) == 0) {
                				if (!pointList.contains(String.valueOf(team1Point))) {
                					pointList.add(String.valueOf(team1Point));
                				}
                			}
            			}
            		}
            	}
            	HashMap<String, Object> mparams = new HashMap<String, Object>(); 
        		mparams.put("ageGroup", params.get("ageGroup"));
        		mparams.put("groups", params.get("groups"));
        		mparams.put("cupId", params.get("cupId"));
        		mparams.put("cupSubMatchTB", cupSubMatchTB);
        		mparams.put("cupTeamTB", cupTeamTB);
        		
        		if (pointList.size() == 1) {
        			List<String> teamList = new ArrayList<>();
                	int point = Integer.parseInt(pointList.get(0));
                	List<HashMap<String, Object>> filter = cupSubMatchRank.stream()
                		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                		    .collect(Collectors.toList());
                	for(HashMap<String, Object> team : filter) {
                		if (team != null) {
                			teamList.add(String.valueOf((int)team.get("team_id")));
                		}
                	}
                	mparams.put("list", teamList);
                	if (teamList.size() > 0) {
                		List<HashMap<String, Object>> matchInfo = cupService.selectCupSubMatchRankForWin(mparams);
                		int drawCount = 0;
        				int playCount = 0;
        				for (HashMap<String, Object> match : matchInfo) {
        					drawCount += ((BigDecimal)match.get("draw")).intValue();
        					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
        				}
        				if (drawCount != playCount) {
        					List<Integer> rankIndex = new ArrayList<>();
                			for (int i = 0; i < matchInfo.size(); i++) {
                				for (int j = 0; j < cupSubMatchRank.size(); j++) {
                					if (matchInfo.get(i).get("team_id").equals(cupSubMatchRank.get(j).get("team_id"))) {
                						int num = j;
                						rankIndex.add(num);
                					}
                					
                				}
                			}
                			List<Integer> sortIndex = sortList(rankIndex);
                			for (int i = 0; i < rankIndex.size(); i++) {
                					HashMap<String, Object> save = cupSubMatchRank.get(sortIndex.get(i));
        							HashMap<String, Object> insert = beforeRank.get(rankIndex.get(i));
        							if (!insert.equals(save)) {
        								cupSubMatchRank.set(sortIndex.get(i), insert);
            							cupSubMatchRank.set(rankIndex.get(i), save);
        							}
                			}
        				}
                	}
        		} else if (pointList.size() > 1) {
        			for (int i = 0; i < pointList.size(); i++) {
        				List<String> teamList = new ArrayList<>();
                    	int point = Integer.parseInt(pointList.get(i));
                    	List<HashMap<String, Object>> filter = cupSubMatchRank.stream()
                    		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                    		    .collect(Collectors.toList());
                    	for(HashMap<String, Object> team : filter) {
                    		if (team != null) {
                    			teamList.add(String.valueOf((int)team.get("team_id")));
                    		}
                    	}
        				mparams.put("list", teamList);
        				if (teamList.size() > 0) {
            				List<HashMap<String, Object>> matchInfo = cupService.selectCupSubMatchRankForWin(mparams);
            				int drawCount = 0;
            				int playCount = 0;
            				for (HashMap<String, Object> match : matchInfo) {
            					drawCount += ((BigDecimal)match.get("draw")).intValue();
            					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
            				}
            				if (drawCount != playCount) {
            					List<Integer> rankIndex = new ArrayList<>();
                				for (int j = 0; j < matchInfo.size(); j++) {
                    				for (int k = 0; k < cupSubMatchRank.size(); k++) {
                    					if (matchInfo.get(j).get("team_id").equals(cupSubMatchRank.get(k))) {
                    						int num = k;
                    						rankIndex.add(num);
                    					}
                    					
                    				}
                    				// rankIndex.add(i, )
                    			}
                				List<HashMap<String, Object>> sortTeamRank = new ArrayList<>();
                				for (int l = 0; l < rankIndex.size(); l++) {
                					sortTeamRank.add(l, cupSubMatchRank.get(rankIndex.get(l)));
                				}
                					
            					List<Integer> sortIndex = sortList(rankIndex);
                    			for (int j = 0; j < rankIndex.size(); j++) {
                    				cupSubMatchRank.set(sortIndex.get(j), sortTeamRank.get(j));
                    			}
            				}
            			}
        			}
        		}
        	}
        }
        model.addAttribute("cupSubMatchRank", cupSubMatchRank);
        // 대회 예선 참가팀 경기 최종순위 정보
        List<HashMap<String, Object>> cupSubMatchRankByFinal = cupService.selectCupSubMatchRankByFinal(params);
        model.addAttribute("cupSubMatchRankByFinal", cupSubMatchRankByFinal);

        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupSubMatchResultList = cupService.selectCupSubMatchResultList(params);
        model.addAttribute("cupSubMatchResultList", cupSubMatchResultList);
        
        params.put("method", "LINEUP");
        HashMap<String, Object> pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushLineup", pushInfo);
        
        params.put("method", "END");
        pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushEnd", pushInfo);

        // 대회 경기일정 그룹 선택
        String groups = params.get("groups");
        if (!StrUtil.isEmpty(groups)) {
            model.addAttribute("groups", groups);
        }else {
            model.addAttribute("groups", "1");
        }

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("teamType", params.get("teamType"));
        
        model.addAttribute("modify", params.get("modify"));
        model.addAttribute("cp", params.get("cp"));

        return "cup/cupMgrSubMatch";
    }

    /*
     * 대회 관리 > 영상등록
     */
    @RequestMapping(value = "/save_cupMgrSubMatchVideo", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrSubMatchVideo(@RequestParam final Map<String, String> params, Model model,
                                           RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrSubMatchVideo params : " + params);
        String sFlag = params.get("sFlag");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }


        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);
        String teamType = params.get("teamType");
        if (!StrUtil.isEmpty(teamType)) {
            redirectAttributes.addAttribute("teamType", teamType);
        }


        if (sFlag.equals(Define.MODE_FIX)) { // 수정
            cupService.updateCupSubVideo(params);
        }

        return "redirect:/cupMgrSubMatch";
    }

    /*
     * 대회 관리 > 예선 추가, 수정, 삭제
     */
    @RequestMapping(value = "/save_cupMgrSubMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrSubMatch(@RequestParam final Map<String, String> params, Model model,
                                      RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrSubMatch params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        List<Object> cupMgrSubMatchList = makeCupMgrSubMatchList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("cupSubMatchTB", cupSubMatchTB);
        //mparams.put("cupId", cupId);
        //mparams.put("groups", groups);
        mparams.put("list", cupMgrSubMatchList);
        System.out.println("-- mparams : " + mparams.toString());

        try {
            if (sFlag.equals(Define.MODE_ADD)) { // 등록
//			sqlSessionInst.insert("insCupSubRank", mparams);

            } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 수정

                if(cupMgrSubMatchList.size() > 0) {
                    for (Object map: cupMgrSubMatchList) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                        obj.put("cupSubMatchTB", cupSubMatchTB);
                        obj.put("cupId", cupId);

                        cupService.updateCupSubMatchScore(obj);
                    }
                }


            } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
//			sqlSessionInst.delete("delCupSubRank", mparams);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
// teamType=" + params.get("teamType");
        String redirectPath = "";
        if (!StrUtil.isEmpty(params.get("teamType"))) {
            redirectPath = "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&teamType=" + params.get("teamType") + "&modify=1";
        } else {
            redirectPath = "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&modify=1";
        }

        //return "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "groups=" + groups;
        return redirectPath;

    }

    /*
     * 대회 관리 > 예선 최종순위 확정
     */
    @RequestMapping(value = "/save_cupMgrSubMatchRank", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrSubMatchRank(@RequestParam final Map<String, String> params, Model model,
                                          RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrSubMatchRank params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        List<Object> cupMgrSubRankList = makeCupMgrSubRankList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("cupResultTB", cupResultTB);
        mparams.put("cupId", cupId);
        mparams.put("groups", groups);
        mparams.put("list", cupMgrSubRankList);
        mparams.put("teamType", params.get("teamType"));
        System.out.println("-- mparams : " + mparams.toString());

        if (sFlag.equals(Define.MODE_ADD)) { // 등록

            if (cupMgrSubRankList.size() > 0) {
                for (Object map : cupMgrSubRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);
                    obj.put("groups", groups);
                    obj.put("teamType", params.get("teamType"));

                    cupService.insertCupSubRank(obj);
                }
            }

        } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 수정

            if (cupMgrSubRankList.size() > 0) {
                for (Object map : cupMgrSubRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);
                    obj.put("groups", groups);
                    obj.put("teamType", params.get("teamType"));

                    cupService.updateCupSubRank(obj);
                }
            }

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            cupService.deleteCupSubRank(mparams);

        }

        String redirectPath = "";

        if (!StrUtil.isEmpty(params.get("teamType"))) {
            redirectPath = "redirect:/cupMgrSubMatch?teamType=" + params.get("teamType");
        } else {
            redirectPath = "redirect:/cupMgrSubMatch";
        }

        return redirectPath;
    }

    // 대회 - 관리 > 대회 본선 결과 등록
    @RequestMapping(value = "/cupMgrMainMatch")
    public String cupMgrMainMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMgrMainMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
//		String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
//		params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);
//		// 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
//		String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);


        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 대회 예선 참가팀 경기 순위 정보
        List<HashMap<String, Object>> cupMainMatchRank = cupService.selectCupMainMatchRank(params);
        List<HashMap<String, Object>> beforeRank = cupMainMatchRank.stream().collect(Collectors.toList());
        
        // 대회 예선 참가팀 경기 최종순위 정보
        List<HashMap<String, Object>> cupMainMatchRankByFinal = cupService.selectCupMainMatchRankByFinal(params);
        model.addAttribute("cupMainMatchRankByFinal", cupMainMatchRankByFinal);
        
        HashMap<String, Object> cupInfo = cupService.selectGetCupInfoForWin(params);
        
        if (cupMainMatchRank.size() > 0) {
        	if (cupInfo.get("rank_type") != null && (int) cupInfo.get("rank_type") == 1) {
        		List<String> pointList = new ArrayList<>();
            	for (int i = 0; i < cupMainMatchRank.size(); i++) {
            		for (int j = 1; j < cupMainMatchRank.size(); j++) {
            			if (i != j) {
            				BigDecimal team1Point = (BigDecimal) cupMainMatchRank.get(i).get("rankPoint");
            				BigDecimal team2Point = (BigDecimal) cupMainMatchRank.get(j).get("rankPoint");
            				
                			if (team1Point.compareTo(team2Point) == 0) {
                				if (!pointList.contains(String.valueOf(team1Point))) {
                					pointList.add(String.valueOf(team1Point));
                				}
                			}
            			}
            		}
            	}
            	HashMap<String, Object> mparams = new HashMap<String, Object>(); 
        		mparams.put("ageGroup", params.get("ageGroup"));
        		mparams.put("groups", params.get("groups"));
        		mparams.put("cupId", params.get("cupId"));
        		mparams.put("cupMainMatchRank", cupMainMatchRank);
        		mparams.put("cupTeamTB", cupTeamTB);
        		
        		if (pointList.size() == 1) {
        			List<String> teamList = new ArrayList<>();
                	int point = Integer.parseInt(pointList.get(0));
                	List<HashMap<String, Object>> filter = cupMainMatchRank.stream()
                		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                		    .collect(Collectors.toList());
                	for(HashMap<String, Object> team : filter) {
                		if (team != null) {
                			teamList.add(String.valueOf((int)team.get("team_id")));
                		}
                	}
                	mparams.put("list", teamList);
                	if (teamList.size() > 0) {
                		List<HashMap<String, Object>> matchInfo = cupService.selectCupMainMatchRankForWin(mparams);
                		int drawCount = 0;
        				int playCount = 0;
        				for (HashMap<String, Object> match : matchInfo) {
        					drawCount += ((BigDecimal)match.get("draw")).intValue();
        					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
        				}
        				if (drawCount != playCount) {
        					List<Integer> rankIndex = new ArrayList<>();
                			for (int i = 0; i < matchInfo.size(); i++) {
                				for (int j = 0; j < cupMainMatchRank.size(); j++) {
                					if (matchInfo.get(i).get("team_id").equals(cupMainMatchRank.get(j).get("team_id"))) {
                						int num = j;
                						rankIndex.add(num);
                					}
                					
                				}
                			}
                			List<Integer> sortIndex = sortList(rankIndex);
                			for (int i = 0; i < rankIndex.size(); i++) {
                					HashMap<String, Object> save = cupMainMatchRank.get(sortIndex.get(i));
        							HashMap<String, Object> insert = beforeRank.get(rankIndex.get(i));
        							if (!insert.equals(save)) {
        								cupMainMatchRank.set(sortIndex.get(i), insert);
        								cupMainMatchRank.set(rankIndex.get(i), save);
        							}
                			}
        				}
                	}
        		} else if (pointList.size() > 1) {
        			for (int i = 0; i < pointList.size(); i++) {
        				List<String> teamList = new ArrayList<>();
                    	int point = Integer.parseInt(pointList.get(i));
                    	List<HashMap<String, Object>> filter = cupMainMatchRank.stream()
                    		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                    		    .collect(Collectors.toList());
                    	for(HashMap<String, Object> team : filter) {
                    		if (team != null) {
                    			teamList.add(String.valueOf((int)team.get("team_id")));
                    		}
                    	}
        				mparams.put("list", teamList);
        				if (teamList.size() > 0) {
            				List<HashMap<String, Object>> matchInfo = cupService.selectCupMainMatchRankForWin(mparams);
            				int drawCount = 0;
            				int playCount = 0;
            				for (HashMap<String, Object> match : matchInfo) {
            					drawCount += ((BigDecimal)match.get("draw")).intValue();
            					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
            				}
            				if (drawCount != playCount) {
            					List<Integer> rankIndex = new ArrayList<>();
                				for (int j = 0; j < matchInfo.size(); j++) {
                    				for (int k = 0; k < cupMainMatchRank.size(); k++) {
                    					if (matchInfo.get(j).get("team_id").equals(cupMainMatchRank.get(k).get("team_id"))) {
                    						int num = k;
                    						rankIndex.add(num);
                    					}
                    					
                    				}
                    			}
                				List<Integer> sortIndex = sortList(rankIndex);
                    			for (int j = 0; j < rankIndex.size(); j++) {
                    					HashMap<String, Object> save = cupMainMatchRank.get(sortIndex.get(i));
            							HashMap<String, Object> insert = beforeRank.get(rankIndex.get(i));
            							if (!insert.equals(save)) {
            								cupMainMatchRank.set(sortIndex.get(i), insert);
            								cupMainMatchRank.set(rankIndex.get(i), save);
            							}
                    			}
            				}
        				}
        			}
        		}
        	}
        }
        
        model.addAttribute("cupMainMatchRank", cupMainMatchRank);

        // 대회 최종순위 등록개수
        // 토너먼트타입을 제외한 최종순위 등록 시 예선 등록여부 확인용
        // 예선등록된 팀 수 = 예선 최종순위 등록된 팀 수
        String rankCount = cupService.selectCupRankCount(params);
        model.addAttribute("rankCount", rankCount);

        System.out.println("rankcount:"+rankCount);


        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupMainTeamList = cupService.selectCupMainTeamListByGroups(params);
        model.addAttribute("cupMainTeamList", cupMainTeamList);

        // 대회 예선 경기일정 정보
        List<HashMap<String, Object>> cupMainMatchResultList = cupService.selectCupMainMatchResultList(params);
        model.addAttribute("cupMainMatchResultList", cupMainMatchResultList);
        
        params.put("method", "LINEUP");
        HashMap<String, Object> pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushLineup", pushInfo);
        
        params.put("method", "END");
        pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushEnd", pushInfo);

        // 대회 경기일정 그룹 선택
        String groups = params.get("groups");
        if (!StrUtil.isEmpty(groups)) {
            model.addAttribute("groups", groups);
        }else {
            model.addAttribute("groups", "1");
        }

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("cp", params.get("cp"));

        return "cup/cupMgrMainMatch";
    }

    /*
     * 대회 관리 > 본선 추가, 수정, 삭제
     */
    @RequestMapping(value = "/save_cupMgrMainMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrMainMatch(@RequestParam final Map<String, String> params, Model model,
                                       RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrMainMatch params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        List<Object> cupMgrMainMatchList = makeCupMgrMainMatchList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("cupMainMatchTB", cupMainMatchTB);
        //mparams.put("cupId", cupId);
        //mparams.put("groups", groups);
        mparams.put("list", cupMgrMainMatchList);
        System.out.println("-- mparams : " + mparams.toString());

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
//			sqlSessionInst.insert("insCupSubRank", mparams);

        } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 수정
            if(cupMgrMainMatchList.size() > 0) {
                for (Object map : cupMgrMainMatchList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupMainMatchTB", cupMainMatchTB);

                    cupService.updateCupMainMatchScore(obj);
                }
            }

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
//			sqlSessionInst.delete("delCupSubRank", mparams);

        }

        return "redirect:/cupMgrMainMatch";
    }

    /*
     * 대회 관리 > 영상등록
     */
    @RequestMapping(value = "/save_cupMgrMainMatchVideo", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrMainMatchVideo(@RequestParam final Map<String, String> params, Model model,
                                            RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrMainMatchVideo params : " + params);
        String sFlag = params.get("sFlag");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);

        if (sFlag.equals(Define.MODE_FIX)) { // 수정
            cupService.updateCupMainVideo(params);
        }


        return "redirect:/cupMgrMainMatch";
    }

    /*
     * 대회 관리 > 본선 최종순위 확정
     */
    @RequestMapping(value = "/save_cupMgrMainMatchRank", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrMainMatchRank(@RequestParam final Map<String, String> params, Model model,
                                           RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrMainMatchRank params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        List<Object> cupMgrMainRankList = makeCupMgrMainRankList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("cupResultTB", cupResultTB);
        mparams.put("cupId", cupId);
        mparams.put("groups", groups);
        mparams.put("list", cupMgrMainRankList);
        System.out.println("-- mparams : " + mparams.toString());

        if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) { // 등록, 수정
            if(cupMgrMainRankList.size() > 0) {
                for (Object map : cupMgrMainRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);
                    obj.put("groups", groups);
                    cupService.updateCupMainRank(obj);
                }
            }
        } else if (sFlag.equals(Define.MODE_DELETE)) {// 초기화
            if(cupMgrMainRankList.size() > 0) {
                for (Object map : cupMgrMainRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);
                    obj.put("groups", groups);
                    cupService.updateCupMainRankReset(obj);
                }
            }
        }


        return "redirect:/cupMgrMainMatch";
    }

    // 대회 - 관리 > 대회 토너먼트 결과 등록
    @RequestMapping(value = "/cupMgrTourMatch")
    public String cupMgrTourMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("cupMgrTourMatch was called. params:" + params);

        // 사용여부
        String sFlag = params.get("sFlag");
        if (StrUtil.isEmpty(sFlag)) {
            sFlag = "0";
            params.put("sFlag", sFlag);
        }

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        // 토너먼트 강수
        String round = params.get("round");
        if(StrUtil.isEmpty(round)) {
            round = String.valueOf(cupInfoMap.get("tour_team"));
            params.put("round", round);
            params.put("groups", round);
        }
        model.addAttribute("round", round);
        model.addAttribute("groups", round);


        // 대회 토너먼트 참가팀 경기 순위 정보
        //List<HashMap<String, Object>> cupTourMatchRank = cupService.selectCupTourMatchRank(params);
        //model.addAttribute("cupTourMatchRank", cupTourMatchRank);

        List<HashMap<String, Object>> cupTourMatchRank = cupService.selectCupTourMatchRankRenewal(params);
        model.addAttribute("cupTourMatchRank", cupTourMatchRank);

        // 대회 토너먼트 최종순위 정보
//		List<HashMap<String, Object>> cupTourRank = sqlSession.selectList("selCupTourRank", params);
//		model.addAttribute("cupTourRank", cupTourRank);

        // 대회 토너먼트 최종순위 등록 수
        int tourRankCount = cupService.selectCupTourRankCount(params);
        model.addAttribute("tourRankCount", tourRankCount);

        // 대회 최종순위 등록개수
        // 토너먼트타입을 제외한 최종순위 등록 시 예선 등록여부 확인용
        // 예선등록된 팀 수 = 예선 최종순위 등록된 팀 수
        String rankCount = cupService.selectCupRankCount(params);
        model.addAttribute("rankCount", rankCount);



        // 대회 참가팀 정보
        List<HashMap<String, Object>> cupTeamList = cupService.selectCupTeamListByGroups(params);
        model.addAttribute("cupTeamList", cupTeamList);


        // 대회 토너먼트 경기일정 정보
        List<HashMap<String, Object>> cupTourMatchResultList = cupService.selectCupTourMatchResultList(params);
        model.addAttribute("cupTourMatchResultList", cupTourMatchResultList);

//		// 대회 경기일정 그룹 선택
//		String groups = params.get("groups");
//		if (!StrUtil.isEmpty(groups)) {
//			model.addAttribute("groups", groups);
//		}
        
        params.put("method", "LINEUP");
        HashMap<String, Object> pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushLineup", pushInfo);
        
        params.put("method", "END");
        pushInfo = pushService.selectPushInfo(params);
        model.addAttribute("pushEnd", pushInfo);

        model.addAttribute("uageList", uageList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("cp", params.get("cp"));

        return "cup/cupMgrTourMatch";
    }

    /*
     * 대회 관리 > 본선 추가, 수정, 삭제
     */
    @RequestMapping(value = "/save_cupMgrTourMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrTourMatch(@RequestParam final Map<String, String> params, Model model,
                                       RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrTourMatch params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String round = params.get("round");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("round", round);


        if (sFlag.equals(Define.MODE_ADD)) { // 등록
//			sqlSessionInst.insert("insCupSubRank", mparams);

        } else if (sFlag.equals(Define.MODE_FIX)) {// 팀수정
            cupService.updateCupTourMatchByMatchId(params);

        } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 일괄수정
            HashMap<String, Object> mparams = new HashMap<String, Object>();
            List<Object> cupMgrTourMatchList = makeCupMgrTourMatchList(params);
            //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
            mparams.put("cupTourMatchTB", cupTourMatchTB);
            //mparams.put("cupId", cupId);
            //mparams.put("groups", groups);
            mparams.put("list", cupMgrTourMatchList);

            if(cupMgrTourMatchList.size() > 0) {
                for (Object map: cupMgrTourMatchList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupTourMatchTB", cupTourMatchTB);
                    cupService.updateCupTourMatchScore(obj);
                }
            }

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
//			sqlSessionInst.delete("delCupSubRank", mparams);

        }


        return "redirect:/cupMgrTourMatch";
    }

    /*
     * 대회 관리 > 영상등록
     */
    @RequestMapping(value = "/save_cupMgrTourMatchVideo", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrTourMatchVideo(@RequestParam final Map<String, String> params, Model model,
                                            RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrTourMatchVideo params : " + params);
        String sFlag = params.get("sFlag");
        String cupId = params.get("cupId");
        String groups = params.get("groups");
        String round = params.get("round");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);
        redirectAttributes.addAttribute("round", round);

        if (sFlag.equals(Define.MODE_FIX)) { // 수정
            cupService.updateCupTourVideo(params);
        }


        return "redirect:/cupMgrTourMatch";
    }

    /*
     * 대회 관리 > 토너먼트 최종순위 확정
     */
    @RequestMapping(value = "/save_cupMgrTourMatchRank", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrTourMatchRank(@RequestParam final Map<String, String> params, Model model,
                                           RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrTourMatchRank params : " + params);
        String sFlag = params.get("sFlag");
        String cupId = params.get("cupId");
        String round = params.get("thisRound");
        String cupType = params.get("cupType");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("round", round);

        HashMap<String, Object> mparams = new HashMap<String, Object>();
        Map<String, List<Object>> cupMgrTourRankList = makeCupMgrTourRankList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("cupResultTB", cupResultTB);
        mparams.put("cupId", cupId);
        mparams.put("list", cupMgrTourRankList);
        System.out.println("-- mparams : " + mparams.toString());

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            //sqlSessionInst.update("udtCupTourMatchRank", mparams);

            if (cupMgrTourRankList.get("insertObj").size() > 0) {
                for (Object map : cupMgrTourRankList.get("insertObj")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);

                    cupService.insertCupTourRank(obj);
                }
            }

            if (cupMgrTourRankList.get("updateObj").size() > 0) {
                for (Object map : cupMgrTourRankList.get("updateObj")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);

                    cupService.updateCupTourRankRenewal(obj);
                }
            }

            /*if(cupMgrTourRankList.size() > 0) {
                for (Object map : cupMgrTourRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);
                    obj.put("cupId", cupId);

                    if(cupType.equals("3")) {
                        //only Tournament
                        cupService.insertCupTourRank(obj);

                    } else {

                        //cupService.updateCupTourRank(obj);
                        cupService.updateCupTourRankRenewal(obj);
                    }

                }
            }*/

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제

            if (cupMgrTourRankList.get("updateObj").size() > 0) {
                for (Object map : cupMgrTourRankList.get("updateObj")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);

                    if(cupType.equals("3")) {
                        //delete
                        cupService.deleteCupTourRank(obj);
                    }else {
                        //update
                        cupService.updateCupTourRank(obj);
                    }

                }
            }

            /*if(cupMgrTourRankList.size() > 0) {
                for (Object map : cupMgrTourRankList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                    obj.put("cupResultTB", cupResultTB);

                    if(cupType.equals("3")) {
                        //delete
                        cupService.deleteCupTourRank(obj);
                    }else {
                        //update
                        cupService.updateCupTourRank(obj);
                    }

                }
            }*/

        }

        return "redirect:/cupMgrTourMatch";
    }

    @RequestMapping(value = "/cupMatchList")
    public String cupMatchList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        String sdate = params.get("sdate");
        if (StrUtil.isEmpty(sdate)) {
            sdate = DateUtil.getCurrentDate();
        }

        params.put("sdate", sdate);

        try {

            List<HashMap<String, Object>> uageList = uageService.selectUseUageList();

            for (HashMap<String, Object> uage: uageList) {
                String uageStr = uage.get("uage").toString();
                String cupInfoTB = uageStr + "_Cup_Info";
                String cupSubMatchTB = uageStr + "_Cup_Sub_Match";
                String cupMainMatchTB = uageStr + "_Cup_Main_Match";
                String cupTourMatchTB = uageStr + "_Cup_Tour_Match";

                params.put("cupInfoTB", cupInfoTB);
                params.put("cupSubMatchTB", cupSubMatchTB);
                params.put("cupMainMatchTB", cupMainMatchTB);
                params.put("cupTourMatchTB", cupTourMatchTB);

                List<HashMap<String, Object>> cupMatch = cupService.selectCupMatchList(params);

                model.addAttribute(uageStr + "CupMatch", cupMatch);
            }

            model.addAttribute("sdate", sdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "cup/cupMatchList";
    }

    @RequestMapping("/cupTourFixRanking")
    @ResponseBody
    public Map<String, Object> cupTourFixRanking(@RequestBody Map<String, String> params) {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String cupId = params.get("cupId");
        String cupTourMatchId = params.get("cupTourMatchId");
        String ageGroup = params.get("ageGroup");

        HashMap<String, Object> tourMatchInfo = new HashMap<String, Object>();
        HashMap<String, Object> nextMacthInfo = new HashMap<String, Object>();

        if (StrUtil.isEmpty(cupId) || StrUtil.isEmpty(ageGroup)) {
            dataMap.put("stateCode", 404);
            dataMap.put("msg", "wrong approach");
            resultMap.put("data", dataMap);
            return resultMap;
        }

        try {

            String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
            params.put("cupTourMatchTB", cupTourMatchTB);

            params.put("cupId", cupId);
            params.put("cupTourMatchId", cupTourMatchId);

            Map<String, String> matchParam = new HashMap<String, String>();

            matchParam.put("cupId", cupId);
            matchParam.put("cupTourMatchTB", cupTourMatchTB);



            tourMatchInfo = cupService.selectCupTourMatchInfo(params);

            if (!tourMatchInfo.get("round").equals(2))  {
                //if (tourMatchInfo.get("home_score").toString().equals(tourMatchInfo.get("away_score").toString())) {
                if (tourMatchInfo.get("home_score") == tourMatchInfo.get("away_score")) {
                    // 동점인 경우
                    if (Integer.parseInt(tourMatchInfo.get("home_pk").toString()) > Integer.parseInt(tourMatchInfo.get("away_pk").toString())) {
                        // home 팀이 pk 승
                        if (tourMatchInfo.get("next_tour_port").toString().equals("0")) {
                            // 다음 매치 home일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("home", tourMatchInfo.get("home").toString());
                            matchParam.put("homeId", tourMatchInfo.get("home_id").toString());
                        } else {
                            // 다음 매치 away일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("away", tourMatchInfo.get("home").toString());
                            matchParam.put("awayId", tourMatchInfo.get("home_id").toString());
                        }
                    } else {
                        // away 팀이 pk 승
                        if (tourMatchInfo.get("next_tour_port").toString().equals("0")) {
                            // 다음 매치 home일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("home", tourMatchInfo.get("away").toString());
                            matchParam.put("homeId", tourMatchInfo.get("away_id").toString());

                        } else {
                            // away일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("away", tourMatchInfo.get("away").toString());
                            matchParam.put("awayId", tourMatchInfo.get("away_id").toString());
                        }
                    }
                } else {
                    if (Integer.parseInt(tourMatchInfo.get("home_score").toString()) > Integer.parseInt(tourMatchInfo.get("away_score").toString())){
                        if (tourMatchInfo.get("next_tour_port").toString().equals("0")) {
                            // 다음 매치 home일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("home", tourMatchInfo.get("home").toString());
                            matchParam.put("homeId", tourMatchInfo.get("home_id").toString());

                        } else {
                            // 다음 매치 away일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("away", tourMatchInfo.get("home").toString());
                            matchParam.put("awayId", tourMatchInfo.get("home_id").toString());
                        }
                    } else {
                        if (tourMatchInfo.get("next_tour_port").toString().equals("0")) {
                            // 다음 매치 home일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("home", tourMatchInfo.get("away").toString());
                            matchParam.put("homeId", tourMatchInfo.get("away_id").toString());

                        } else {
                            // 다음 매치 away일 때
                            matchParam.put("nextTourNo", tourMatchInfo.get("next_tour_no").toString());
                            matchParam.put("away", tourMatchInfo.get("away").toString());
                            matchParam.put("awayId", tourMatchInfo.get("away_id").toString());
                        }
                    }
                }

                nextMacthInfo = cupService.selectCupNextTourMatch(matchParam);
                matchParam.put("cupTourMatchId", nextMacthInfo.get("cup_tour_match_id").toString());
                cupService.updateCupTourNextMatch(matchParam);
            }

            cupService.updateCupTourMatchFixFlag(params);

            dataMap.put("status", "success");
            dataMap.put("stateCode", 200);
            dataMap.put("msg", null);

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("state", "error");
            dataMap.put("stateCode", 500);
            resultMap.put("data", dataMap);
            resultMap.put("msg", "server error");
        }

        resultMap.put("data", dataMap);
        return resultMap;
    }


    @RequestMapping("/cupEndSubMatch")
    @ResponseBody
    public Map<String, Object> cupEndSubMatch(@RequestBody Map<String, String> params) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String cupId = params.get("cupId");
        String cupSubMatchId = params.get("cupSubMatchId");
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(cupId) || StrUtil.isEmpty(ageGroup)) {
            dataMap.put("stateCode", 404);
            dataMap.put("msg", "wrong approach");
            resultMap.put("data", dataMap);
            return resultMap;
        }

        try {

            String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
            params.put("cupSubMatchTB", cupSubMatchTB);

            params.put("cupId", cupId);
            params.put("cupSubMatchId", cupSubMatchId);

            cupService.updateCupSubMatchEndFlag(params);

            dataMap.put("status", "success");
            dataMap.put("stateCode", 200);
            dataMap.put("msg", null);

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("state", "error");
            dataMap.put("stateCode", 500);
            resultMap.put("data", dataMap);
            resultMap.put("msg", "server error");
        }

        resultMap.put("data", dataMap);
        return resultMap;
    }

    @RequestMapping("/cupEndMainMatch")
    @ResponseBody
    public Map<String, Object> cupEndMainMatch(@RequestBody Map<String, String> params) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String cupId = params.get("cupId");
        String cupMainMatchId = params.get("cupMainMatchId");
        String ageGroup = params.get("ageGroup");

        if (StrUtil.isEmpty(cupId) || StrUtil.isEmpty(ageGroup)) {
            dataMap.put("stateCode", 404);
            dataMap.put("msg", "wrong approach");
            resultMap.put("data", dataMap);
            return resultMap;
        }

        try {

            String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
            params.put("cupMainMatchTB", cupMainMatchTB);

            params.put("cupId", cupId);
            params.put("cupMainMatchId", cupMainMatchId);

            cupService.updateCupMainMatchEndFlag(params);

            dataMap.put("status", "success");
            dataMap.put("stateCode", 200);
            dataMap.put("msg", null);

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("state", "error");
            dataMap.put("stateCode", 500);
            resultMap.put("data", dataMap);
            resultMap.put("msg", "server error");
        }

        resultMap.put("data", dataMap);
        return resultMap;
    }

    @RequestMapping(value = "/search_cup_champion")
    @ResponseBody
    public Map<String, Object> searchCup(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);

        String ageGroup = params.get("ageGroup");
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupResultTB = ageGroup + "_Cup_Result";

        params.put("cupInfoTB", cupInfoTB);
        params.put("cupResultTB", cupResultTB);

        List<HashMap<String, Object>> cupList = new ArrayList<HashMap<String, Object>>();

        try {
            cupList = cupService.selectSearchCupForChampion(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", cupList);
        return resultMap;
    }

    @RequestMapping(value = "/search_cup_result")
    @ResponseBody
    public Map<String, Object> searchCupResult(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("search_team was called. params:" + params);

        String ageGroup = params.get("ageGroup");
        String cupInfoTB = ageGroup + "_Cup_Info";
        String cupResultTB = ageGroup + "_Cup_Result";

        params.put("cupInfoTB", cupInfoTB);
        params.put("cupResultTB", cupResultTB);

        HashMap<String, Object> cupResult = new HashMap<String, Object>();

        try {
            cupResult = cupService.selectSearchCupResultForChampion(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", cupResult);
        return resultMap;
    }

    // 대회 - 관리 > 매치상세
    @RequestMapping(value = "/cupMgrTourMatchPlayData")
    public String cupMgrTourMatchPlayData(@RequestParam Map<String, String> params, Model model) {
        logger.info("cupMgrTourMatch was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        /*감독, 코치 데이터 시작*/
        Map<String, String> staffDataParamMap = new HashMap<>();
        staffDataParamMap.put("matchId", params.get("matchId"));
        String staffDataTB = ageGroup + "_Cup_Tour_Staff_Data";

        staffDataParamMap.put("staffDataTB", staffDataTB);
        staffDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));

        staffDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));
        /*감독, 코치 데이터 끝*/

        /*플레이데이타 시작*/
        Map<String, String> playDataParamMap = new HashMap<>();
        playDataParamMap.put("matchId", params.get("matchId"));
        String cupMatchPlayDataTB = ageGroup + "_Cup_Tour_Match_Play_Data";

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("homeSelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("homeCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("awaySelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("awayCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));
        /*플레이데이타 끝*/

        /*교체데이타 시작*/
        Map<String, String> chnageDataParamMap = new HashMap<>();
        String cupMatchChangeDataTB = ageGroup + "_Cup_Tour_Match_Change_Data";
        chnageDataParamMap.put("matchId", params.get("matchId"));
        chnageDataParamMap.put("cupMatchChangeDataTB", cupMatchChangeDataTB);

        chnageDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));

        chnageDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));
        /*교체데이타 끝*/

        /*자책골 데이터 시작*/
        Map<String, String> ownGoalDataMap = new HashMap<>();
        String ownGoalDataTB = ageGroup + "_Cup_Tour_Own_Goal_Data";
        ownGoalDataMap.put("matchId", params.get("matchId"));
        ownGoalDataMap.put("ownGoalDataTB", ownGoalDataTB);

        ownGoalDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        ownGoalDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        /*자책골 데이터 끝*/

        /*코치, 감독 페널티 데이터 시작*/
        Map<String, String> staffPenaltyDataMap = new HashMap<>();
        String staffPenaltyDataTB = ageGroup + "_Cup_Tour_Staff_Penalty_Data";
        staffPenaltyDataMap.put("matchId", params.get("matchId"));
        staffPenaltyDataMap.put("staffPenaltyDataTB", staffPenaltyDataTB);

        staffPenaltyDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));

        staffPenaltyDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));
        /*코치, 감독 페널티 데이터 끝*/

        model.addAttribute("uageList", uageList);
        model.addAttribute("ageGroup", ageGroup);

        return "cup/cupMgrTourMatchPlayData";
    }


    // 대회 - 관리 > 매치상세
    @RequestMapping(value = "/cupMgrMainMatchPlayData")
    public String cupMgrMainMatchPlayData(@RequestParam Map<String, String> params, Model model) {
        logger.info("cupMgrMainMatch was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        /*감독, 코치 데이터 시작*/
        Map<String, String> staffDataParamMap = new HashMap<>();
        staffDataParamMap.put("matchId", params.get("matchId"));
        String staffDataTB = ageGroup + "_Cup_Main_Staff_Data";

        staffDataParamMap.put("staffDataTB", staffDataTB);
        staffDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));

        staffDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));
        /*감독, 코치 데이터 끝*/

        /*플레이데이타 시작*/
        Map<String, String> playDataParamMap = new HashMap<>();
        playDataParamMap.put("matchId", params.get("matchId"));
        String cupMatchPlayDataTB = ageGroup + "_Cup_Main_Match_Play_Data";

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("homeSelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("homeCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("awaySelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("awayCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));
        /*플레이데이타 끝*/

        /*교체데이타 시작*/
        Map<String, String> chnageDataParamMap = new HashMap<>();
        String cupMatchChangeDataTB = ageGroup + "_Cup_Main_Match_Change_Data";
        chnageDataParamMap.put("matchId", params.get("matchId"));
        chnageDataParamMap.put("cupMatchChangeDataTB", cupMatchChangeDataTB);

        chnageDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));

        chnageDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));
        /*교체데이타 끝*/

        /*자책골 데이터 시작*/
        Map<String, String> ownGoalDataMap = new HashMap<>();
        String ownGoalDataTB = ageGroup + "_Cup_Main_Own_Goal_Data";
        ownGoalDataMap.put("matchId", params.get("matchId"));
        ownGoalDataMap.put("ownGoalDataTB", ownGoalDataTB);

        ownGoalDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        ownGoalDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        /*자책골 데이터 끝*/

        /*코치, 감독 페널티 데이터 시작*/
        Map<String, String> staffPenaltyDataMap = new HashMap<>();
        String staffPenaltyDataTB = ageGroup + "_Cup_Main_Staff_Penalty_Data";
        staffPenaltyDataMap.put("matchId", params.get("matchId"));
        staffPenaltyDataMap.put("staffPenaltyDataTB", staffPenaltyDataTB);

        staffPenaltyDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));

        staffPenaltyDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));
        /*코치, 감독 페널티 데이터 끝*/

        model.addAttribute("uageList", uageList);
        model.addAttribute("ageGroup", ageGroup);

        return "cup/cupMgrMainMatchPlayData";
    }


    // 대회 - 관리 > 매치상세
    @RequestMapping(value = "/cupMgrSubMatchPlayData")
    public String cupMgrSubMatchPlayData(@RequestParam Map<String, String> params, Model model) {
        logger.info("cupMgrSubMatch was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String cupInfoTB = ageGroup + "_Cup_Info";
        params.put("cupInfoTB", cupInfoTB);
        String cupTeamTB = ageGroup + "_Cup_Team";
        params.put("cupTeamTB", cupTeamTB);

        // 대회 예선 경기일정 정보 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);
//		// 대회 본선 경기일정 정보 연령별 테이블
//		String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        // 대회 토너먼트(knockout stage) 경기일정 정보 연령별 테이블
        String cupTourMatchTB = ageGroup + "_Cup_Tour_Match";
        params.put("cupTourMatchTB", cupTourMatchTB);

        //대회 결과 연령별 테이블
        String cupResultTB = ageGroup + "_Cup_Result";
        params.put("cupResultTB", cupResultTB);

        // 대회 기본정보 아이디
        String cupId = params.get("cupId");
        model.addAttribute("cupId", cupId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 대회 기본 정보
        HashMap<String, String> cupInfoMap = cupService.selectGetCupInfo(params);
        model.addAttribute("cupInfoMap", cupInfoMap);

        /*감독, 코치 데이터 시작*/
        Map<String, String> staffDataParamMap = new HashMap<>();
        staffDataParamMap.put("matchId", params.get("matchId"));
        String staffDataTB = ageGroup + "_Cup_Sub_Staff_Data";

        staffDataParamMap.put("staffDataTB", staffDataTB);
        staffDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));

        staffDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPlayDataList", cupService.selectStaffData(staffDataParamMap));
        /*감독, 코치 데이터 끝*/

        /*플레이데이타 시작*/
        Map<String, String> playDataParamMap = new HashMap<>();
        playDataParamMap.put("matchId", params.get("matchId"));
        String cupMatchPlayDataTB = ageGroup + "_Cup_Sub_Match_Play_Data";

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB.toLowerCase());
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("homeSelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB.toLowerCase());
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("homeCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB.toLowerCase());
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("awaySelectionPlayDataList", cupService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB.toLowerCase());
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("awayCandidatePlayDataList", cupService.selectMatchPlayData(playDataParamMap));
        /*플레이데이타 끝*/

        /*교체데이타 시작*/
        Map<String, String> chnageDataParamMap = new HashMap<>();
        String cupMatchChangeDataTB = ageGroup + "_Cup_Sub_Match_Change_Data";
        chnageDataParamMap.put("matchId", params.get("matchId"));
        chnageDataParamMap.put("cupMatchChangeDataTB", cupMatchChangeDataTB.toLowerCase());

        chnageDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));

        chnageDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayChangeDataList", cupService.selectMatchChangeData(chnageDataParamMap));
        /*교체데이타 끝*/

        /*자책골 데이터 시작*/
        Map<String, String> ownGoalDataMap = new HashMap<>();
        String ownGoalDataTB = ageGroup + "_Cup_Sub_Own_Goal_Data";
        ownGoalDataMap.put("matchId", params.get("matchId"));
        ownGoalDataMap.put("ownGoalDataTB", ownGoalDataTB);

        ownGoalDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        ownGoalDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayOwnGoalDataList", cupService.selectOwnGoalData(ownGoalDataMap));

        /*자책골 데이터 끝*/

        /*코치, 감독 페널티 데이터 시작*/
        Map<String, String> staffPenaltyDataMap = new HashMap<>();
        String staffPenaltyDataTB = ageGroup + "_Cup_Sub_Staff_Penalty_Data";
        staffPenaltyDataMap.put("matchId", params.get("matchId"));
        staffPenaltyDataMap.put("staffPenaltyDataTB", staffPenaltyDataTB);

        staffPenaltyDataMap.put("homeAwayGbn", "home");
        model.addAttribute("homeStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));

        staffPenaltyDataMap.put("homeAwayGbn", "away");
        model.addAttribute("awayStaffPenaltyDataList", cupService.selectStaffPenaltyData(staffPenaltyDataMap));
        /*코치, 감독 페널티 데이터 끝*/

        model.addAttribute("uageList", uageList);
        model.addAttribute("ageGroup", ageGroup);

        return "cup/cupMgrSubMatchPlayData";
    }
    
    /*
     * 대회 관리 > 예선 개별 수정
     */
    @RequestMapping(value = "/save_cupMgrSubMatchOne", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrSubMatchOne(@RequestParam final Map<String, String> params, Model model,
                                      RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrSubMatchOne params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        //대회 결과 연령별 테이블
        String cupSubMatchTB = ageGroup + "_Cup_Sub_Match";
        params.put("cupSubMatchTB", cupSubMatchTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);
        
        String date = params.get("date");
        String dateStr = DateUtil.format(date, DateUtil.YYYYMMDD);
        String dayOfWeek = DateUtil.getDayOfWeek(dateStr);
        String matchDate = dateStr + " (" + dayOfWeek + ") " + params.get("time");
        params.put("matchDate", matchDate);
        String selHome = params.get("selHome");
        String selAway = params.get("selAway");
        String home = params.get("homeNickName");
        String away = params.get("awayNickName");
        
        params.put("homeId", selHome);
        params.put("awayId", selAway);
        
        params.put("home", home);
        params.put("away", away);

        try {
            if (sFlag.equals(Define.MODE_FIX_ONE)) {
            	cupService.updateCupSubMatchOne(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
// teamType=" + params.get("teamType");
        String redirectPath = "";
        if (!StrUtil.isEmpty(params.get("teamType"))) {
            redirectPath = "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&teamType=" + params.get("teamType");
        } else {
            redirectPath = "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups;
        }

        //return "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "groups=" + groups;
        return redirectPath;

    }
    
    /*
     * 대회 관리 > 본선 개별 수정
     */
    @RequestMapping(value = "/save_cupMgrMainMatchOne", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_cupMgrMainMatchOne(@RequestParam final Map<String, String> params, Model model,
                                      RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_cupMgrMainMatchOne params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String cupId = params.get("cupId");
        String groups = params.get("groups");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        //대회 결과 연령별 테이블
        String cupMainMatchTB = ageGroup + "_Cup_Main_Match";
        params.put("cupMainMatchTB", cupMainMatchTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("cupId", cupId);
        redirectAttributes.addAttribute("groups", groups);
        
        String date = params.get("date");
        String dateStr = DateUtil.format(date, DateUtil.YYYYMMDD);
        String dayOfWeek = DateUtil.getDayOfWeek(dateStr);
        String matchDate = dateStr + " (" + dayOfWeek + ") " + params.get("time");
        params.put("matchDate", matchDate);
        String selHome = params.get("selHome");
        String selAway = params.get("selAway");
        String home = params.get("homeNickName");
        String away = params.get("awayNickName");
        
        params.put("homeId", selHome);
        params.put("awayId", selAway);
        
        params.put("home", home);
        params.put("away", away);

        try {
            if (sFlag.equals(Define.MODE_FIX_ONE)) {
            	cupService.updateCupMainMatchOne(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
// teamType=" + params.get("teamType");
        String redirectPath = "";
        if (!StrUtil.isEmpty(params.get("teamType"))) {
            redirectPath = "redirect:/cupMgrMainMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups + "&teamType=" + params.get("teamType");
        } else {
            redirectPath = "redirect:/cupMgrMainMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "&groups=" + groups;
        }

        //return "redirect:/cupMgrSubMatch?cupId=" + cupId + "&ageGroup=" + ageGroup + "groups=" + groups;
        return redirectPath;

    }


    private List<Object> makeCupSubList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        String szTotalCnt = params.get("trCnt");

        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cupSubMatchId", params.get("cupSubMatchId" + i));
            map.put("groups", params.get("groups" + i));
            map.put("homeId", params.get("selHome" + i));
            map.put("awayId", params.get("selAway" + i));
            map.put("home", params.get("home" + i));
            map.put("away", params.get("away" + i));
            map.put("place", params.get("place" + i));
            map.put("pDate", params.get("pdate" + i));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeCupTeamList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int szTotalCnt = Integer.parseInt(params.get("trCnt"));

        for (int i = 0; i < szTotalCnt; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cupTeamId", params.get("cupTeamId" + i));
            map.put("teamId", params.get("teamId" + i));
            map.put("nickName", params.get("nickName" + i));
            map.put("newTab", params.get("newTab" + i));
            objList.add(map);
        }
        return objList;
    }

    private List<Object> makeCupMainList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        String szTotalCnt = params.get("trCnt");

        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cupMainMatchId", params.get("cupMainMatchId" + i));
            map.put("groups", params.get("groups" + i));
            map.put("homeId", params.get("selHome" + i));
            map.put("awayId", params.get("selAway" + i));
            map.put("home", params.get("home" + i));
            map.put("away", params.get("away" + i));
            map.put("place", params.get("place" + i));
            map.put("pDate", params.get("pdate" + i));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeCupTourList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        String szTotalCnt = params.get("trCnt");

        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cupTourMatchId", params.get("cupTourMatchId" + i));
            map.put("round", params.get("round" + i));
            map.put("homeId", params.get("selHome" + i));
            map.put("awayId", params.get("selAway" + i));
            map.put("home", params.get("home" + i));
            map.put("away", params.get("away" + i));
            map.put("place", params.get("place" + i));
            map.put("pDate", params.get("pdate" + i));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeCupMgrSubMatchList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        //String groups = params.get("groups");
        //String thisRound = params.get("thisRound");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupSubMatchId", params.get("cupSubMatchId" + i));

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("homeScore", params.get("homeScore" + i));
                map.put("awayScore", params.get("awayScore" + i));
                map.put("homePk", params.get("homePk" + i));
                map.put("awayPk", params.get("awayPk" + i));
                map.put("selMatchType", params.get("selMatchType" + i));
                map.put("reason", params.get("reason" + i));

                if(sFlag.equals(Define.MODE_ALL_FIX)) {
                    map.put("cupSubMatchId", params.get("cupSubMatchId" + i));
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private List<Object> makeCupMgrSubRankList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String groups = params.get("groups");
        String thisRound = params.get("thisRound");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupResultId", params.get("cupResultId" + i));

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("subRank", groups + "/" + params.get("selRank" + i));
                map.put("teamId", params.get("teamId" + i));
                map.put("played", params.get("playTotalCnt" + i));
                map.put("points", params.get("rankPoint" + i));
                map.put("won", params.get("win" + i));
                map.put("draw", params.get("draw" + i));
                map.put("lost", params.get("lose" + i));
                map.put("gf", params.get("point" + i));
                map.put("ga", params.get("losePoint" + i));
                map.put("gd", params.get("goalPoint" + i));

                result = params.get("resultType" + i);
                if(!StrUtil.isEmpty(result) && result.equals("4")) {
                    //대회 - 예선+토너먼트
                    //탈락일 경우 해당 round를 남김 (예선)
                    map.put("result", thisRound);
                } else if(StrUtil.isEmpty(result) && (cupType.equals("2") || cupType.equals("4"))) {
                    //대회 - 풀리그
                    //예선을 남기지 않고 조/순위 를 남김
                    map.put("result", groups + "조 " + params.get("selRank" + i) + "위");

                } else if(StrUtil.isEmpty(result)) {
                    //대회 - 예선+토너먼트
                    //토너먼트 진출 시 공란으로 처리
                    map.put("result", "");
                }

                if(sFlag.equals(Define.MODE_ALL_FIX)) {
                    map.put("cupResultId", params.get("cupResultId" + i));
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private List<Object> makeCupMgrMainMatchList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupMainMatchId", params.get("cupMainMatchId" + i));

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("homeScore", params.get("homeScore" + i));
                map.put("awayScore", params.get("awayScore" + i));
                map.put("homePk", params.get("homePk" + i));
                map.put("awayPk", params.get("awayPk" + i));
                map.put("selMatchType", params.get("selMatchType" + i));
                map.put("reason", params.get("reason" + i));

                if(sFlag.equals(Define.MODE_ALL_FIX)) {
                    map.put("cupMainMatchId", params.get("cupMainMatchId" + i));
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private List<Object> makeCupMgrMainRankList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String groups = params.get("groups");
        String thisRound = params.get("thisRound");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));


        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupResultId", params.get("cupResultId" + i));
                map.put("mainRank", "");
                map.put("played", "0");
                map.put("points", "0");
                map.put("won", "0");
                map.put("draw", "0");
                map.put("lost", "0");
                map.put("gf", "0");
                map.put("ga", "0");
                map.put("gd", "0");
                map.put("groups", "-1");
                map.put("result", "");

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mainRank", groups + "/" + params.get("selRank" + i));
                map.put("cupResultId", params.get("cupResultId" + i));
                map.put("teamId", params.get("teamId" + i));
                map.put("played", params.get("playTotalCnt" + i));
                map.put("points", params.get("rankPoint" + i));
                map.put("won", params.get("win" + i));
                map.put("draw", params.get("draw" + i));
                map.put("lost", params.get("lose" + i));
                map.put("gf", params.get("point" + i));
                map.put("ga", params.get("losePoint" + i));
                map.put("gd", params.get("goalPoint" + i));

                result = params.get("resultType" + i);
                if(!StrUtil.isEmpty(result) && result.equals("4")) {
                    //대회 - 예선+본선+토너먼트
                    //탈락일 경우 해당 round를 남김 (본선)
                    map.put("result", thisRound);
//				} else if(StrUtil.isEmpty(result) && cupType.equals("2")) {
//					//대회 - 풀리그
//					//예선을 남기지 않고 조/순위 를 남김
//					map.put("result", groups + "조 " + params.get("selRank" + i) + "위");

                } else if(StrUtil.isEmpty(result)) {
                    //대회 - 예선+본선+토너먼트
                    //토너먼트 진출 시 공란으로 처리
                    map.put("result", "");
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private List<Object> makeCupMgrTourMatchList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupTourMatchId", params.get("cupTourMatchId" + i));

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("homeScore", params.get("homeScore" + i));
                map.put("awayScore", params.get("awayScore" + i));
                map.put("homePk", params.get("homePk" + i));
                map.put("awayPk", params.get("awayPk" + i));
                map.put("selMatchType", params.get("selMatchType" + i));
                map.put("reason", params.get("reason" + i));

                if(sFlag.equals(Define.MODE_ALL_FIX)) {
                    map.put("cupTourMatchId", params.get("cupTourMatchId" + i));
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private Map<String, List<Object>> makeCupMgrTourRankList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String cupType = params.get("cupType");
        String result = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        List<Object> updateObj = new ArrayList<>();
        List<Object> insertObj = new ArrayList<>();

        Map<String, List<Object>> resultMap = new HashMap<String, List<Object>>();

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupResultId", params.get("cupResultId" + i));
                map.put("played", "0");
                map.put("points", "0");
                map.put("won", "0");
                map.put("draw", "0");
                map.put("lost", "0");
                map.put("gf", "0");
                map.put("ga", "0");
                map.put("gd", "0");
                map.put("result", "");

                updateObj.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) ) {// 등록

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cupResultId", params.get("cupResultId" + i));
                map.put("teamId", params.get("teamId" + i));
                map.put("played", params.get("playTotalCnt" + i));
                map.put("points", params.get("rankPoint" + i));
                map.put("won", params.get("win" + i));
                map.put("draw", params.get("draw" + i));
                map.put("lost", params.get("lose" + i));
                map.put("gf", params.get("point" + i));
                map.put("ga", params.get("losePoint" + i));
                map.put("gd", params.get("goalPoint" + i));

                if (params.get("thisRound").equals("2")) {
                    if (params.get("resultType" + i) == null) {
                        map.put("resultType", "우승");
                    } else {
                        map.put("resultType", params.get("resultType" + i));
                    }
                } else {
                    map.put("resultType", params.get("resultType" + i));
                }


                result = params.get("resultType" + i);
                if(!StrUtil.isEmpty(result)) {
                    map.put("result", result);
                }

                if (StrUtil.isEmpty(params.get("cupResultId" + i))) {
                    insertObj.add(map);
                } else {
                    updateObj.add(map);
                }
                // objList.add(map);
            }//for

        }//elseif

        resultMap.put("insertObj", insertObj);
        resultMap.put("updateObj", updateObj);

        return resultMap;
    }

    private List<Object> makeCupResultListForChampion(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String trLength = params.get("trLength");
        String result = "";

        int totalCnt = Integer.parseInt(String.valueOf(trLength));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (!StrUtil.isEmpty(params.get("foreignId_" + i))) {
                map.put("infoId", params.get("cupId"));
                map.put("foreignId", params.get("foreignId_" + i));
                map.put("year", params.get("year_" + i));
                map.put("ageGroup", params.get("ageGroup_" + i));
                map.put("teamId", params.get("teamId_" + i));
                map.put("played", params.get("played_" + i));
                map.put("win", params.get("win_" + i));
                map.put("lose", params.get("lose_" + i));
                map.put("gf", params.get("gf_" + i));
                map.put("ga", params.get("ga_" + i));

                objList.add(map);
            }
        }

        return objList;
    }

    private List<Object> makeDeleteCupResultListForChampion(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String trLength = params.get("delTrLength");
        String result = "";

        int totalCnt = Integer.parseInt(String.valueOf(trLength));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (!StrUtil.isEmpty(params.get("delete_" + i))) {
                map.put("championId", params.get("delete_" + i));

                objList.add(map);
            }
        }

        return objList;
    }
    
    private List<Integer> sortList(List<Integer> list) {
    	List<Integer> newList = list.stream().collect(Collectors.toList());
    	Collections.sort(newList);
    	return newList;
    }
}
