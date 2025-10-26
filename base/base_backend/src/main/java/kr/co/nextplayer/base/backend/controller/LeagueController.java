package kr.co.nextplayer.base.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.backend.dto.UageDto;
import kr.co.nextplayer.base.backend.model.Uage;
import kr.co.nextplayer.base.backend.service.LeagueService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class LeagueController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private LeagueService leagueService;

    @Resource
    private UageService uageService;

    @RequestMapping(value = "/league")
    public String league(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("league was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        // 검색
        String sArea = params.get("sArea");
        if (!StrUtil.isEmpty(sArea)) {
            model.addAttribute("sArea", sArea);
        }
        String sLeagueName = params.get("sLeagueName");
        if (!StrUtil.isEmpty(sLeagueName)) {
            model.addAttribute("sLeagueName", sLeagueName);
        }

        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        HashMap<String, Object> countMap = leagueService.selectLeagueInfoListCount(params);
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

        // 연령별 리그 리스트 getting league list
        List<HashMap<String, Object>> leagueInfoList = leagueService.selectLeagueInfoList(params);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("leagueInfoList", leagueInfoList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

//			String webPath = StrUtil.getWebPath();
//			model.addAttribute("webPath", webPath);

        return "league/league";
    }

    // 리그 - 등록/수정 > 리그정보 등록
    @RequestMapping(value = "/leagueInfo")
    public String leagueInfo(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueInfo was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        if (sFlag.equals(Define.MODE_FIX)) {// 수정
            // 리그 기본 정보
            HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
            model.addAttribute("leagueInfoMap", leagueInfoMap);
        }

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

        return "league/leagueInfo";
    }

    @RequestMapping(value = "/leaguePrize")
    public String leaguePrize(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leaguePrize was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_league_Info";
        String leagueTeamTB = ageGroup + "_league_team";
        params.put("leagueInfoTB", leagueInfoTB);
        params.put("leagueTeamTB", leagueTeamTB);
//		String leagueTeamTB = ageGroup + "_League_Team";
//		params.put("leagueTeamTB", leagueTeamTB);

        if (sFlag.equals(Define.MODE_FIX)) {// 수정
            // 대회 기본 정보
            HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
            model.addAttribute("leagueInfoMap", leagueInfoMap);

            List<HashMap<String, Object>> championInfo = leagueService.selectGetLeagueChampions(params);
            model.addAttribute("championInfo", championInfo);
        }

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        // 대회 기본정보 아이디
        String leagueId = params.get("leagueId");
        model.addAttribute("leagueId", leagueId);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "league/leaguePrize";
    }

    @RequestMapping(value = "/save_leaguePrize", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_leaguePrize(@RequestParam Map<String, String> params, Model model,
                                RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leaguePrize params : " + params);
        String sFlag = params.get("sFlag");
        String leagueId = params.get("leagueId");
//		String cp = params.get("cp");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_league_info";
        params.put("leagueInfoTB", leagueInfoTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("leagueId", leagueId);
        redirectAttributes.addAttribute("sFlag", sFlag);

        if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_FIX)) {// 등록, 수정
            leagueService.updateLeaguePrize(params);
            leagueService.insertLeagueResultForChampion(params);
        }

        String teamType = params.get("teamType");
        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag) && mvFlag.equals(Define.MODE_MOVE)) {
                return "redirect:/leagueTeam";
        }

        return "redirect:/leaguePrize";
    }

    // 리그 - 등록/수정 > 리그정보 등록
    @RequestMapping(value = "/leagueInfoCraw")
    public String leagueInfoCraw(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueInfo was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        return "league/leagueInfoCraw";
    }


    /*
     * 리그정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_leagueInfo", method = { RequestMethod.GET, RequestMethod.POST })
    // public String save_leagueInfo(MultipartHttpServletRequest request,
    // @RequestParam Map<String, String> params, Model model, HttpServletResponse
    // resp, RedirectAttributes redirectAttributes) {
    public String save_leagueInfo(@RequestParam final Map<String, String> params, Model model,
                                  RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leagueInfo params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);

//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            leagueService.insertLeagueInfo(params);

//			recordLog(Define.STR_SUCCESS,"[학원.클럽.유스] 등록-성공 -params:"+params.toString()); //접속하여 행위한 정보에대한 기록

        } else if (sFlag.equals(Define.MODE_FIX)) {// 수정
            leagueService.updateLeagueInfo(params);

            redirectAttributes.addAttribute("cp", cp);

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            // sqlSessionInst.update("udtTeam", params);
            leagueService.deleteLeagueInfo(params); // 타겟 테이블 데이터 삭제

        }

        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag) && mvFlag.equals(Define.MODE_MOVE)) {
            return "redirect:/leagueTeam";
        }

        return "redirect:/league";
    }

    @PostMapping(value = "/save_leagueInfo_list")
    @ResponseBody
    public JSONObject save_leagueInfo_list(@RequestBody List<Map<String, String>> params) {
        JSONObject resultObj = new JSONObject();
        int count = leagueService.insertLeagueInfoList(params);
        resultObj.put("count", count);
        return resultObj;
    }

    // 리그 - 등록/수정 > 리그참가팀 등록
    @RequestMapping(value = "/leagueTeam")
    public String leagueTeam(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueTeam was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        // 리그 기본정보 아이디
        String leagueId = params.get("leagueId");
        model.addAttribute("leagueId", leagueId);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        // 리그 기본 정보
        HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
        model.addAttribute("leagueInfoMap", leagueInfoMap);


        // 리그 참가팀 정보
        List<HashMap<String, Object>> leagueTeamList = leagueService.selectLeagueTeamList(params);

        model.addAttribute("leagueTeamList", leagueTeamList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("cp", params.get("cp"));

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

        return "league/leagueTeam";
    }

    /*
     * 리그 참가팀 정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_leagueTeam", method = { RequestMethod.GET, RequestMethod.POST })
    // public String save_leagueInfo(MultipartHttpServletRequest request,
    // @RequestParam Map<String, String> params, Model model, HttpServletResponse
    // resp, RedirectAttributes redirectAttributes) {
    public String save_leagueTeam(@RequestParam final Map<String, String> params, Model model,
                                  RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leagueTeam params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String leagueId = params.get("leagueId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("leagueId", leagueId);

        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            HashMap<String, Object> mparams = new HashMap<String, Object>();

            List<Object> leagueTeamList = makeLeagueTeamList(params);
            mparams.put("leagueTeamTB", leagueTeamTB);
            mparams.put("list", leagueTeamList);
            leagueService.insertLeagueTeam(mparams);

        } else if (sFlag.equals(Define.MODE_FIX)) {// 수정
            // 삭제
            leagueService.deleteLeagueTeam(params); // 타겟 테이블 데이터 전체 삭제

            HashMap<String, Object> mparams = new HashMap<String, Object>();

            List<Object> leagueTeamList = makeLeagueTeamList(params);
            mparams.put("leagueTeamTB", leagueTeamTB);
            mparams.put("list", leagueTeamList);
            leagueService.insertLeagueTeam(mparams);

            redirectAttributes.addAttribute("cp", cp);

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            leagueService.deleteLeagueTeam(params); // 해당 리그 참가팀 데이터 전체 삭제

        }

        String mvFlag = params.get("mvFlag");
        if (!StrUtil.isEmpty(mvFlag) && mvFlag.equals(Define.MODE_MOVE)) {
            return "redirect:/leagueMatch";
        }

        return "redirect:/leagueTeam";
    }

    // 리그 - 등록/수정 > 리그 경기일정 등록
    @RequestMapping(value = "/leagueMatch")
    public String leagueMatch(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueMatch was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        // 리그 기본정보 아이디
        String leagueId = params.get("leagueId");
        model.addAttribute("leagueId", leagueId);

        // 연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        // 리그 기본 정보
        HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
        model.addAttribute("leagueInfoMap", leagueInfoMap);

        // 리그 참가팀 정보
        List<HashMap<String, Object>> leagueTeamList = leagueService.selectLeagueTeamList(params);
        model.addAttribute("leagueTeamList", leagueTeamList);

        // 리그 경기일정 정보
        List<HashMap<String, Object>> leagueMatchList = leagueService.selectLeagueMatchList(params);
        model.addAttribute("leagueMatchList", leagueMatchList);

        // 리그 경기일정 캘린더
        List<HashMap<String, Object>> leagueMatchCalendar = leagueService.selectLeagueMatchCalendar(params);
        model.addAttribute("leagueMatchCalendar", leagueMatchCalendar);

        // 리그 경기일정 년 검색
        String years = params.get("years");
        if (!StrUtil.isEmpty(years)) {
            model.addAttribute("years", years);
        }
        // 리그 경기일정 월 검색
        String months = params.get("months");
        if (!StrUtil.isEmpty(months)) {
            model.addAttribute("months", months);
        }

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);
        model.addAttribute("cp", params.get("cp"));

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

        return "league/leagueMatch";
    }

    /*
     * 리그 경기일정 정보 - 등록.수정.삭제
     */
    @RequestMapping(value = "/save_leagueMatch", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_leagueMatch(@RequestParam final Map<String, String> params, Model model,
                                   RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leagueMatch params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String leagueId = params.get("leagueId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

//		model.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("leagueId", leagueId);

        try {
            if (sFlag.equals(Define.MODE_ADD)) { // 등록
                HashMap<String, Object> mparams = new HashMap<String, Object>();

                List<Object> leagueMatchList = makeLeagueMatchList(params);
                mparams.put("leagueMatchTB", leagueMatchTB);
                mparams.put("list", leagueMatchList);
                leagueService.insertLeagueMatch(mparams);

            } else if (sFlag.equals(Define.MODE_FIX)) {// 수정
                // 삭제

                HashMap<String, Object> mparams = new HashMap<String, Object>();

                List<Object> leagueMatchList = makeLeagueMatchList(params);

                List<Object> deleteLeagueMatchList = makeLeagueDeleteMatchList(params);

                if(leagueMatchList.size() > 0) {
                    List<Map<String, Object>> newTabObj = new ArrayList<Map<String, Object>>();
                    for(Object map : leagueMatchList) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                        obj.put("leagueMatchTB", leagueMatchTB);
                        if (obj.get("newTap") == null) {
                            leagueService.updateAllLeagueMatch(obj);
                        } else {
                            // 새로 추가된 경기 데이터
                            newTabObj.add(obj);
                        }
                    }

                    // 새로 추가된 경기 데이터 insert
                    if (newTabObj.size() > 0) {
                        for(Map<String, Object> map : newTabObj) {
                            leagueService.insertLeagueMatch(map);
                        }
                    }
                }

                // 경기 삭제
                if (deleteLeagueMatchList.size() > 0) {
                    for(Object map : deleteLeagueMatchList) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> obj = objectMapper.convertValue(map, Map.class);
                        obj.put("leagueMatchTB", leagueMatchTB);
                        leagueService.deleteLeagueMatchOne(obj);
                    }
                }


                redirectAttributes.addAttribute("cp", cp);

            } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
                leagueService.deleteLeagueMatch(params); // 타겟 테이블 데이터 전체 삭제

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/leagueMatch";
    }

    // 리그 - 관리 > 리그 경기관리 리스트
    @RequestMapping(value = "/leagueMgr")
    public String leagueMgr(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueMgr was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);
        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);

        // 검색
        String sArea = params.get("sArea");
        if (!StrUtil.isEmpty(sArea)) {
            model.addAttribute("sArea", sArea);
        }
        String sLeagueName = params.get("sLeagueName");
        if (!StrUtil.isEmpty(sLeagueName)) {
            model.addAttribute("sLeagueName", sLeagueName);
        }

        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        HashMap<String, Object> countMap = leagueService.selectLeagueInfoListCount(params);
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

        // 연령별 리그 리스트 getting league list
        List<HashMap<String, Object>> leagueMgrList = leagueService.selectLeagueMgrList(params);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();


        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        model.addAttribute("leagueMgrList", leagueMgrList);
        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

        return "league/leagueMgr";
    }

    // 리그 - 관리 > > 리그 경기관리 일정 등록
    @RequestMapping(value = "/leagueMgrInfo")
    public String leagueMgrInfo(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueMgrInfo was called. params:" + params);

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
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        // 리그 기본정보 아이디
        String leagueId = params.get("leagueId");
        model.addAttribute("leagueId", leagueId);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();

        // 지역정보
        List<HashMap<String, Object>> areaList = uageService.selectAreaList(params);

        // 리그 기본 정보
        HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
        model.addAttribute("leagueInfoMap", leagueInfoMap);

        // 리그 참가팀 경기 순위 정보
        List<HashMap<String, Object>> leagueMatchRank = leagueService.selectLeagueMatchRank(params);
        List<HashMap<String, Object>> beforeRank = leagueMatchRank.stream().collect(Collectors.toList());
        // 리그 경기 결과 정보
        List<HashMap<String, Object>> leagueMatchSchedule = leagueService.selectLeagueMatchSchedule(params);
        model.addAttribute("leagueMatchSchedule", leagueMatchSchedule);

        // 리그 경기일정 캘린더
        List<HashMap<String, Object>> leagueMatchCalendar = leagueService.selectLeagueMatchCalendar(params);
        model.addAttribute("leagueMatchCalendar", leagueMatchCalendar);
        
        HashMap<String, Object> leagueInfo = leagueService.selectGetLeagueInfoForWin(params);
        
        
        if (leagueMatchRank.size() > 0) {
        	
        	// 순위선정방식 확인 
        	// 0 : 골득실
        	// 1 : 승자승
        	if (leagueInfoMap.get("rank_type") != null && (int) leagueInfo.get("rank_type") == 1) {
        		
        		// 같은 승점 존재 여부 확인
        		List<String> pointList = new ArrayList<>();
            	for (int i = 0; i < leagueMatchRank.size(); i++) {
            		for (int j = 1; j < leagueMatchRank.size(); j++) {
            			if (i != j) {
            				BigDecimal team1Point = (BigDecimal) leagueMatchRank.get(i).get("rankPoint");
            				BigDecimal team2Point = (BigDecimal) leagueMatchRank.get(j).get("rankPoint");
            				
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
        		mparams.put("leagueId", params.get("leagueId"));
        		mparams.put("leagueMatchTB", leagueMatchTB);
        		mparams.put("leagueTeamTB", leagueTeamTB);
        		
        		// 승점이 같은 팀이 있는 경우
        		if (pointList.size() == 1) {
        			List<String> teamList = new ArrayList<>();
                	int point = Integer.parseInt(pointList.get(0));
                	List<HashMap<String, Object>> filter = leagueMatchRank.stream()
                		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                		    .collect(Collectors.toList());
                	for(HashMap<String, Object> team : filter) {
                		if (team != null) {
                			teamList.add(String.valueOf((int)team.get("team_id")));
                		}
                	}
                	mparams.put("list", teamList);
                	if (teamList.size() > 0) {
                		List<HashMap<String, Object>> matchInfo = leagueService.selectLeagueMatchRankForWin(mparams);
                		int drawCount = 0;
        				int playCount = 0;
        				for (HashMap<String, Object> match : matchInfo) {
        					drawCount += ((BigDecimal)match.get("draw")).intValue();
        					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
        				}
        				if (drawCount != playCount) {
        					List<Integer> rankIndex = new ArrayList<>();
                			for (int i = 0; i < matchInfo.size(); i++) {
                				for (int j = 0; j < leagueMatchRank.size(); j++) {
                					if (matchInfo.get(i).get("team_id").equals(leagueMatchRank.get(j).get("team_id"))) {
                						int num = j;
                						rankIndex.add(num);
                					}
                					
                				}
                			}
                			List<Integer> sortIndex = sortList(rankIndex);
                			for (int i = 0; i < rankIndex.size(); i++) {
                					HashMap<String, Object> save = leagueMatchRank.get(sortIndex.get(i));
            						HashMap<String, Object> insert = beforeRank.get(rankIndex.get(i));
            						if (!insert.equals(save)) {
            							leagueMatchRank.set(sortIndex.get(i), insert);
            							leagueMatchRank.set(rankIndex.get(i), save);
            						}
                			}
        				}
                	}
        		} else if (pointList.size() > 1) {
        			for (int i = 0; i < pointList.size(); i++) {
                    	List<String> teamList = new ArrayList<>();
                    	int point = Integer.parseInt(pointList.get(i));
                    	List<HashMap<String, Object>> filter = leagueMatchRank.stream()
                    		    .filter(t -> ((BigDecimal)t.get("rankPoint")).intValue() == point)
                    		    .collect(Collectors.toList());
                    	for(HashMap<String, Object> team : filter) {
                    		if (team != null) {
                    			teamList.add(String.valueOf((int)team.get("team_id")));
                    		}
                    	}
        				mparams.put("list", teamList);
        				if (teamList.size() > 0) {
            				List<HashMap<String, Object>> matchInfo = leagueService.selectLeagueMatchRankForWin(mparams);
            				int drawCount = 0;
            				int playCount = 0;
            				for (HashMap<String, Object> match : matchInfo) {
            					drawCount += ((BigDecimal)match.get("draw")).intValue();
            					playCount += ((BigDecimal)match.get("playTotalCnt")).intValue();
            				}
            				if (drawCount != playCount) {
            					List<Integer> rankIndex = new ArrayList<>();
                				for (int j = 0; j < matchInfo.size(); j++) {
                    				for (int k = 0; k < leagueMatchRank.size(); k++) {
                    					if (matchInfo.get(j).get("team_id").equals(leagueMatchRank.get(k).get("team_id"))) {
                    						int num = k;
                    						rankIndex.add(num);
                    					}
                    					
                    				}
                    			}
                				List<HashMap<String, Object>> sortTeamRank = new ArrayList<>();
                				for (int l = 0; l < rankIndex.size(); l++) {
                					sortTeamRank.add(l, leagueMatchRank.get(rankIndex.get(l)));
                				}
                					
            					List<Integer> sortIndex = sortList(rankIndex);
                    			for (int j = 0; j < rankIndex.size(); j++) {
            						leagueMatchRank.set(sortIndex.get(j), sortTeamRank.get(j));
                    			}
            				}
        				}
        			}
        		}
        	}
        }
        
        model.addAttribute("leagueMatchRank", leagueMatchRank);
        // 리그 참가팀 경기 최종순위 정보
        List<HashMap<String, Object>> leagueFinalRank = leagueService.selectLeagueFinalRank(params);
        model.addAttribute("leagueFinalRank", leagueFinalRank);
        

        // 리그 경기일정 년 검색
        String years = params.get("years");
        if (!StrUtil.isEmpty(years)) {
            model.addAttribute("years", years);
        }
        // 리그 경기일정 월 검색
        String months = params.get("months");
        if (!StrUtil.isEmpty(months)) {
            model.addAttribute("months", months);
        }

        model.addAttribute("uageList", uageList);
        model.addAttribute("areaList", areaList);

        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("sFlag", sFlag);

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("sLeagueName", params.get("sLeagueName"));

        return "league/leagueMgrInfo";
    }


    // 리그 - 관리 > > 리그 경기관리 일정 등록
    @RequestMapping(value = "/leagueMgrMatchPlayData")
    public String leagueMgrPlayData(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueMatchPlayData was called. params:" + params);

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
            params.put("ageGroup", ageGroup);
        }

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        // 리그 기본정보 아이디
        String leagueId = params.get("leagueId");
        model.addAttribute("leagueId", leagueId);

        //연령 정보
        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        UageDto uageDto = UageDto.builder()
            .ageGroup(params.get("ageGroup"))
            .areaSearch(params.get("areaSearch"))
            .areaId(params.get("areaId"))
            .build();


        // 리그 기본 정보
        HashMap<String, String> leagueInfoMap = leagueService.selectGetLeagueInfo(params);
        model.addAttribute("leagueInfoMap", leagueInfoMap);

        /*플레이데이타 시작*/
        Map<String, String> playDataParamMap = new HashMap<>();
        playDataParamMap.put("matchId", params.get("matchId"));
        String cupMatchPlayDataTB = ageGroup + "_League_Match_Play_Data";

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("homeSelectionPlayDataList", leagueService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "home");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("homeCandidatePlayDataList", leagueService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "sel");
        model.addAttribute("awaySelectionPlayDataList", leagueService.selectMatchPlayData(playDataParamMap));

        playDataParamMap.put("cupMatchPlayDataTB", cupMatchPlayDataTB);
        playDataParamMap.put("homeAwayGbn", "away");
        playDataParamMap.put("selCanGbn", "can");
        model.addAttribute("awayCandidatePlayDataList", leagueService.selectMatchPlayData(playDataParamMap));
        /*플레이데이타 끝*/

        /*교체데이타 시작*/
        Map<String, String> chnageDataParamMap = new HashMap<>();
        String cupMatchChangeDataTB = ageGroup + "_League_Match_Change_Data";
        chnageDataParamMap.put("matchId", params.get("matchId"));
        chnageDataParamMap.put("cupMatchChangeDataTB", cupMatchChangeDataTB);

        chnageDataParamMap.put("homeAwayGbn", "home");
        model.addAttribute("homeChangeDataList", leagueService.selectMatchChangeData(chnageDataParamMap));

        chnageDataParamMap.put("homeAwayGbn", "away");
        model.addAttribute("awayChangeDataList", leagueService.selectMatchChangeData(chnageDataParamMap));
        /*교체데이타 끝*/

        model.addAttribute("uageList", uageList);
        model.addAttribute("ageGroup", ageGroup);

        return "league/leagueMgrPlayData";
    }

    /*
     * 리그 관리 > 경기결과 정보 - 수정.삭제
     */
    @RequestMapping(value = "/save_leagueMgrInfo", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_leagueMgrInfo(@RequestParam final Map<String, String> params, Model model,
                                     RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leagueMgrInfo params : " + params);
        String sFlag = params.get("sFlag");
        String cp = params.get("cp");
        String leagueId = params.get("leagueId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }
        // 연령별 테이블
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("leagueId", leagueId);

        if (sFlag.equals(Define.MODE_FIX)) {// 수정
            leagueService.updateLeagueMatch(params); // 리그 경기결과 수정

            redirectAttributes.addAttribute("cp", cp);

        } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 리그 경기 결과 일괄 수정
            HashMap<String, Object> mparams = new HashMap<String, Object>();

            List<Object> leagueMgrInfoList = makeLeagueMgrInfoList(params);
            System.out.println("-- leagueMgrInfoList : " + leagueMgrInfoList);
//			mparams.put("key", leagueId);
            if (leagueMgrInfoList.size() > 0) {
                for (Object map: leagueMgrInfoList) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> obj = objectMapper.convertValue(map, Map.class);

                    obj.put("leagueMatchTB", leagueMatchTB);
                    obj.put("leagueId", leagueId);
                    

                    leagueService.updateAllLeagueMatch(obj);
                }
            }

            redirectAttributes.addAttribute("cp", cp);
        }

        return "redirect:/leagueMgrInfo";
    }

    /*
     * 리그 관리 > 최종순위 확정
     */
    @RequestMapping(value = "/save_leagueMgrInfoRank", method = { RequestMethod.GET, RequestMethod.POST })
    public String save_leagueMgrInfoRank(@RequestParam final Map<String, String> params, Model model,
                                         RedirectAttributes redirectAttributes) {
        logger.info("request ----> save_leagueMgrInfoRank params : " + params);
        String sFlag = params.get("sFlag");
//		String cp = params.get("cp");
        String leagueId = params.get("leagueId");

        // 연령대
        String ageGroup = params.get("ageGroup");
        if (StrUtil.isEmpty(ageGroup)) {
            ageGroup = "U18";
        }

        // 연령별 테이블
        String leagueInfoTB = ageGroup + "_League_Info";
        params.put("leagueInfoTB", leagueInfoTB);
        String leagueTeamTB = ageGroup + "_League_Team";
        params.put("leagueTeamTB", leagueTeamTB);
        String leagueMatchTB = ageGroup + "_League_Match";
        params.put("leagueMatchTB", leagueMatchTB);

        //대회 결과 연령별 테이블
        String leagueResultTB = ageGroup + "_League_Result";
        params.put("leagueResultTB", leagueResultTB);


        redirectAttributes.addAttribute("ageGroup", ageGroup);
        redirectAttributes.addAttribute("leagueId", leagueId);


        HashMap<String, Object> mparams = new HashMap<String, Object>();
        List<Object> leagueMgrInfoRankList = makeLeagueMgrInfoRankList(params);
        //System.out.println("-- leagueMgrInfoList : " + cupMgrSubRankList);
        mparams.put("leagueResultTB", leagueResultTB);
        mparams.put("leagueId", leagueId);
        mparams.put("list", leagueMgrInfoRankList);
        System.out.println("-- mparams : " + mparams.toString());


        if (sFlag.equals(Define.MODE_ADD)) { // 등록
            leagueService.insertLeagueFinalRank(mparams);

        } else if (sFlag.equals(Define.MODE_ALL_FIX)) {// 수정
            leagueService.updateLeagueFinalRank(mparams);

        } else if (sFlag.equals(Define.MODE_DELETE)) {// 삭제
            leagueService.deleteLeagueFinalRank(mparams);

        }


        return "redirect:/leagueMgrInfo";
    }

    // 리그 - 관리 > > 리그 경기관리 일정 등록
    @RequestMapping(value = "/leagueMatchList")
    public String leagueMatchList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("leagueMgrInfo was called. params:" + params);

        String sdate = params.get("sdate");
        if (StrUtil.isEmpty(sdate)) {
            sdate = DateUtil.getCurrentDate();
        }

        params.put("sdate", sdate);

        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();

        for (HashMap<String, Object> uage: uageList) {
            String uageStr = uage.get("uage").toString();
            String leagueInfoTB = uageStr + "_League_Info";
            String leagueMatchTB = uageStr + "_League_Match";

            params.put("leagueInfoTB", leagueInfoTB);
            params.put("leagueMatchTB", leagueMatchTB);

            List<HashMap<String, Object>> leagueMatch = leagueService.selectListOfLeagueMatch(params);

            model.addAttribute(uageStr + "LeagueMatch", leagueMatch);
        }

        model.addAttribute("sdate", sdate);

        return "league/leagueMatchList";
    }

    @RequestMapping(value = "/search_league_champion")
    @ResponseBody
    public Map<String, Object> searchLeague(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("searchLeague was called. params:" + params);

        String ageGroup = params.get("ageGroup");
        String leagueInfoTB = ageGroup + "_league_info";
        String leagueResultTB = ageGroup + "_league_result";

        params.put("leagueInfoTB", leagueInfoTB);
        params.put("leagueResultTB", leagueResultTB);

        List<HashMap<String, Object>> leagueList = new ArrayList<HashMap<String, Object>>();

        try {
            leagueList = leagueService.selectSearchLeagueForChampion(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", leagueList);
        return resultMap;
    }


    private List<Object> makeLeagueTeamList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String szTotalCnt = params.get("trCnt");

//		if(StrUtil.isEmpty(szTotalCnt)) {
//			szTotalCnt = "15";
//		}

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nickName", params.get("nickName" + i));
            map.put("leagueName", params.get("leagueName"));
            map.put("teamId", params.get("teamId" + i));
            map.put("leagueId", params.get("leagueId"));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeLeagueMatchList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String szTotalCnt = params.get("trCnt");

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matchDate", params.get("pdate" + i));
            map.put("leagueMatchId", params.get("leagueMatchId" + i));
            map.put("place", params.get("place" + i));
            map.put("matchType", params.get("matchType" + i));
            map.put("home", params.get("home" + i));
            map.put("away", params.get("away" + i));
            map.put("homeId", params.get("selHome" + i));
            map.put("awayId", params.get("selAway" + i));
            map.put("leagueName", params.get("leagueName"));
            map.put("leagueId", params.get("leagueId"));
            map.put("newTap", params.get("newTap" + i));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeLeagueDeleteMatchList(Map<String, String> param) {
        List<Object> objList = new ArrayList<Object>();

        int delCnt = Integer.parseInt(param.get("delCnt"));

        for (int i = 0; i < delCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("leagueMatchId", param.get("delTab" + i));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeLeagueMgrInfoList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String szTotalCnt = params.get("trCnt");

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        for (int i = 0; i < totalCnt; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("matchDate", params.get("pdate" + i));
            map.put("place", params.get("place" + i));
            map.put("home", params.get("home" + i));
            map.put("away", params.get("away" + i));
            map.put("homeId", params.get("selHome" + i));
            map.put("awayId", params.get("selAway" + i));
            map.put("homeScore", params.get("homeScore" + i));
            map.put("awayScore", params.get("awayScore" + i));
            map.put("reason", params.get("reason" + i));
            map.put("matchType", params.get("matchType" + i));
            map.put("leagueMatchId", params.get("leagueMatchId" + i));
            map.put("leagueId", params.get("leagueId"));
            objList.add(map);
        }

        return objList;
    }

    private List<Object> makeLeagueMgrInfoRankList(Map<String, String> params) {
        List<Object> objList = new ArrayList<Object>();

        String sFlag = params.get("sFlag");
        String szTotalCnt = params.get("trCnt");
        String leagueId = params.get("leagueId");
        String result = "";
        String pointsAdd = "";

        // System.out.println("--- szTotalCnt : "+ szTotalCnt);
        int totalCnt = Integer.parseInt(String.valueOf(szTotalCnt));

        if (sFlag.equals(Define.MODE_DELETE)) { // 삭제

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("leagueResultId", params.get("leagueResultId" + i));

                objList.add(map);
            }//for


        } else if (sFlag.equals(Define.MODE_ADD) || sFlag.equals(Define.MODE_ALL_FIX)) {// 등록, 수정

            for (int i = 0; i < totalCnt; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("result", params.get("selRank" + i) + "위");
                map.put("rank", params.get("selRank" + i));
                map.put("teamId", params.get("teamId" + i));
                map.put("played", params.get("playTotalCnt" + i));
                map.put("points", params.get("rankPoint" + i));


//				map.put("pointsAdd", params.get("rankPointAdd" + i));
                pointsAdd = params.get("rankPointAdd" + i);
                if(StrUtil.isEmpty(pointsAdd)) {
                    pointsAdd = "0";
                }
                map.put("pointsAdd", pointsAdd);


                map.put("won", params.get("win" + i));
                map.put("draw", params.get("draw" + i));
                map.put("lost", params.get("lose" + i));
                map.put("gf", params.get("point" + i));
                map.put("ga", params.get("losePoint" + i));
                map.put("gd", params.get("goalPoint" + i));


                if(sFlag.equals(Define.MODE_ALL_FIX)) {
                    map.put("leagueResultId", params.get("leagueResultId" + i));
                }

                objList.add(map);
            }//for

        }//elseif


        return objList;
    }

    private List<Integer> sortList(List<Integer> list) {
    	List<Integer> newList = list.stream().collect(Collectors.toList());
    	Collections.sort(newList);
    	return newList;
    }
}
