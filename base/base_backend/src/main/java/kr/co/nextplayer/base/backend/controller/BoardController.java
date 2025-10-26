package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.BoardDto;
import kr.co.nextplayer.base.backend.dto.PartnershipDto;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.vo.ReferenceVO;
import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller()
//@RequestMapping("/board/")
public class BoardController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private BoardService boardService;

    @Resource
    private UageService uageService;

    @Resource
    private FileService fileService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;

    @Resource
    private TeamService teamService;

    // 문의/공지 > 리스트
    @RequestMapping(value = "/qna")
    public String qna(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. params:" + params);

        // 검색
        String sTitle = params.get("sTitle");
        if (!StrUtil.isEmpty(sTitle)) {
            model.addAttribute("sTitle", sTitle);
        }

        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        HashMap<String, Object> countMap = boardService.selectQnaListCount(params);
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


        // QnA List
        List<HashMap<String, Object>> qnaList = boardService.selectQnaList(params);
        model.addAttribute("qnaList", qnaList);

        return "board/qna";
    }

    // 문의/공지 > 상세
    @RequestMapping(value = "/qnaDet")
    public String qnaDet(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. params:" + params);

        // QnA 상세
        HashMap<String, Object> qnaMap = boardService.selectQnaDet(params);
        model.addAttribute("qnaMap", qnaMap);

        return "board/qnaDet";
    }

    // 문의/공지 > 삭제
    @RequestMapping(value = "/qnaDel")
    public String qnaDel(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDel was called. params:" + params);

        boardService.updateQnaUseFlag(params);

        return null;
    }

    @RequestMapping(value = "/referenceList")
    public String referenceList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("referenceList was called. params:" + params);

        model.addAttribute("searchKeyword", params.get("searchKeyword"));
        model.addAttribute("sDate", params.get("sDate"));
        model.addAttribute("eDate", params.get("eDate"));

        int totalCnt = boardService.selectReferenceListCnt(params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int tp = 1;
        if (totalCnt > 0) {
            tp = (int) totalCnt / cpp;
            if ((totalCnt % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCnt, Define.COUNT_PAGE);

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end", map.get("end"));
        model.addAttribute("prev", map.get("prev"));
        model.addAttribute("next", map.get("next"));
        model.addAttribute("cp", cp); // 현재페이지번호
        model.addAttribute("cpp", cpp); // 현재페이지 갯수
        model.addAttribute("tp", tp); // 총 페이지 번호
        model.addAttribute("tc", totalCnt); // 총 리스트 갯수

        List<HashMap<String, Object>> referenceList = boardService.selectReferenceList(params);

        model.addAttribute("referenceList", referenceList);

        return "reference/list";
    }

    @RequestMapping("/{method}Reference")
    public String modify(@RequestParam Map<String, String> params, @PathVariable String method, Model model, HttpServletResponse resp) {
        logger.info("referenceList was called. params:" + params);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        model.addAttribute("uageList", uageList);
        model.addAttribute("method", method);

        if (method.equals("modify")) {

            HashMap<String, Object> referenceInfo = boardService.selectReferenceInfo(params);
            model.addAttribute("referenceInfo", referenceInfo);

            List<HashMap<String, Object>> childList = boardService.selectReferenceChildList(params);

            HashMap<String, String> cupData = new HashMap<String, String>();
            List<HashMap<String, String>> cupResult = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> leagueData = new HashMap<String, String>();
            List<HashMap<String, String>> leagueResult = new ArrayList<HashMap<String, String>>();

            if (childList.size() > 0) {
                for (int i = 0; i < childList.size(); i++) {
                    HashMap<String, String> pParams = new HashMap<String, String>();
                    String foreignId = "";
                    String foreignTB = "";

                    if (childList.get(i).get("foreignTB") != null) {
                        foreignTB = childList.get(i).get("foreignTB").toString();
                        if (foreignTB.toLowerCase().contains("cup")) {
                            pParams.put("cupInfoTB", foreignTB);
                        } else {
                            pParams.put("leagueInfoTB", foreignTB);
                            pParams.put("leagueTeamTB", foreignTB.substring(0, 3) + "_league_team");
                        }
                    }

                    if (childList.get(i).get("foreignId") != null) {
                        foreignId = childList.get(i).get("foreignId").toString();
                        if (foreignTB.toLowerCase().contains("cup")) {
                            pParams.put("cupId", foreignId);
                        } else {
                            pParams.put("leagueId", foreignId);
                        }
                    }

                    if (foreignTB.toLowerCase().contains("cup")) {
                        cupData = cupService.selectGetCupInfo(pParams);
                        cupResult.add(cupData);
                    } else {
                        leagueData = leagueService.selectGetLeagueInfo(pParams);
                        leagueResult.add(leagueData);
                    }

                }
            }
            model.addAttribute("cupList", cupResult);
            model.addAttribute("leagueList", leagueResult);
        }

        return "reference/modify";
    }

    @RequestMapping(value = "/{method}_reference", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyReference(@PathVariable String method,
                                                             MultipartHttpServletRequest req,
                                                             MultipartHttpServletRequest multi,
                                                             @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyReference was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "Reference");

        Map<String, Object> map = new HashMap<String, Object>();

        if (method.equals("save")) {
            List<AttchFileInfoDto> files = fileService.upload(multi);
            boardService.insertReference(params, files);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> files = fileService.upload(multi);
            int modifyResult = boardService.updateReference(params, files);
        }

        if (method.equals("delete")) {
            int result = boardService.updateDeleteReference(params);
            if (result > 0) {
                map.put("state", "success");
            } else {
                map.put("state", "fail");
            }
        }


        return map;
    }

    @RequestMapping("/detailReference")
    public String detail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("detail was called. params:" + params);

        HashMap<String, Object> referenceInfo = boardService.selectReferenceInfo(params);
        model.addAttribute("referenceInfo", referenceInfo);

        List<HashMap<String, Object>> childList = boardService.selectReferenceChildList(params);

        HashMap<String, String> cupData = new HashMap<String, String>();
        List<HashMap<String, String>> cupResult = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> leagueData = new HashMap<String, String>();
        List<HashMap<String, String>> leagueResult = new ArrayList<HashMap<String, String>>();

        if (childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                HashMap<String, String> pParams = new HashMap<String, String>();
                String foreignId = "";
                String foreignTB = "";

                if (childList.get(i).get("foreignTB") != null) {
                    foreignTB = childList.get(i).get("foreignTB").toString();
                    if (foreignTB.toLowerCase().contains("cup")) {
                        pParams.put("cupInfoTB", foreignTB);
                    } else {
                        pParams.put("leagueInfoTB", foreignTB);
                        pParams.put("leagueTeamTB", foreignTB.substring(0, 3) + "_league_team");
                    }
                }

                if (childList.get(i).get("foreignId") != null) {
                    foreignId = childList.get(i).get("foreignId").toString();
                    if (foreignTB.toLowerCase().contains("cup")) {
                        pParams.put("cupId", foreignId);
                    } else {
                        pParams.put("leagueId", foreignId);
                    }
                }

                if (foreignTB.toLowerCase().contains("cup")) {
                    cupData = cupService.selectGetCupInfo(pParams);
                    cupResult.add(cupData);
                } else {
                    leagueData = leagueService.selectGetLeagueInfo(pParams);
                    leagueResult.add(leagueData);
                }
            }
        }
        model.addAttribute("cupList", cupResult);
        model.addAttribute("leagueList", leagueResult);
        return "reference/detail";
    }

    // 문의/공지 > 리스트
    @RequestMapping(value = "/{reqType}ReqList")
    public String ReqList(@PathVariable String reqType, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. reqType:" + reqType);

        // 검색
        if (StrUtil.isEmpty(reqType)) {
            reqType = "match";
        }

        if (reqType.equals("match")) {
            if (StrUtil.isEmpty(params.get("requestType"))) {
                params.put("requestType", "0");
            }

            model.addAttribute("requestType", params.get("requestType"));
        }

        params.put("reqType", reqType);
        model.addAttribute("reqType", reqType);
        model.addAttribute("sUpdFlag", params.get("sUpdFlag"));
        model.addAttribute("sdate", params.get("sdate"));
        model.addAttribute("edate", params.get("edate"));
        model.addAttribute("searchKeyword", params.get("searchKeyword"));
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        long totalCount = Long.valueOf(boardService.selectRequestListCount(params));

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

        List<HashMap<String, Object>> reqList = boardService.selectRequestList(params);

        List<HashMap<String, Object>> reqChildList = new ArrayList<HashMap<String, Object>>();

        if (reqList.size() > 0) {
            if (reqType.equals("match")) {
                for (HashMap<String, Object> req : reqList) {

                    if (!StrUtil.isEmpty(req.get("foreign_parent_id")) && !StrUtil.isEmpty(req.get("foreign_parent_type"))) {

                        String parentId = req.get("foreign_parent_id").toString();
                        String parentTB = req.get("foreign_parent_type").toString();

                        if (StrUtil.isEmpty(req.get("foreign_id")) && StrUtil.isEmpty(req.get("foreign_type"))) {

                            HashMap<String, String> mParams = new HashMap<>();
                            HashMap<String, Object> child = new HashMap<>();

                            if (parentTB.contains("Cup")) {
                                mParams.put("cupId", parentId);
                                mParams.put("cupInfoTB", parentTB);

                                child = cupService.selectGetCupInfoForMedia(mParams);
                            } else if (parentTB.contains("League")) {
                                mParams.put("leagueId", parentId);
                                mParams.put("leagueInfoTB", parentTB);

                                child = leagueService.selectGetLeagueInfoForMedia(mParams);
                            }

                            req.put("child", child);
                        } else {
                            String childId = req.get("foreign_id").toString();
                            String childTB = req.get("foreign_type").toString();

                            HashMap<String, String> mParams = new HashMap<>();
                            HashMap<String, Object> child = new HashMap<>();

                            String childKey = "";

                            if (parentTB.contains("Cup")) {

                                if (childTB.contains("Cup_Sub")) {
                                    childKey = "a.cup_sub_match_id";
                                } else if (childTB.contains("Cup_Main")) {
                                    childKey = "a.cup_main_match_id";
                                } else if (childTB.contains("Cup_Tour")) {
                                    childKey = "a.cup_tour_match_id";
                                }

                                mParams.put("parentId", parentId);
                                mParams.put("parentTB", parentTB);
                                mParams.put("childId", childId);
                                mParams.put("childTB", childTB);
                                mParams.put("childKey", childKey);

                                child = cupService.selectCupMatchForModifyResultRequest(mParams);
                            } else if (parentTB.contains("League")) {

                                childKey = "a.league_match_id";

                                mParams.put("parentId", parentId);
                                mParams.put("parentTB", parentTB);
                                mParams.put("childId", childId);
                                mParams.put("childTB", childTB);
                                mParams.put("childKey", childKey);
                                child = leagueService.selectLeagueMatchForModifyResultRequest(mParams);
                            }

                            req.put("child", child);
                        }
                    }
                }
            } else if (reqType.equals("team")) {

                for (HashMap<String, Object> req : reqList) {

                    HashMap<String, String> mParams = new HashMap<>();
                    HashMap<String, Object> child = new HashMap<>();

                    if (!StrUtil.isEmpty(req.get("foreign_parent_id"))) {
                        String parentId = req.get("foreign_parent_id").toString();
                        mParams.put("teamId", parentId);
                        child = teamService.selectTeamInfo(mParams);
                    }


                    req.put("child", child);
                }
            }
        }

        model.addAttribute("reqList", reqList);

        return "board/reqList";
    }

    @RequestMapping(value = "/{reqType}ReqDetail")
    public String ReqDetail(@PathVariable String reqType, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. reqType:" + reqType);
        model.addAttribute("reqType", reqType);
        // QnA 상세
        HashMap<String, Object> reqDetail = boardService.selectRequestDetail(params);
        model.addAttribute("reqDetail", reqDetail);

        HashMap<String, Object> parent = new HashMap<>();
        HashMap<String, Object> child = new HashMap<>();

        String parentId = "";
        String parentTB = "";
        String childId = "";
        String childTB = "";
        String ageGroup = "";

        if (reqDetail != null) {
            if (reqType.equals("match")) {
                parentId = reqDetail.get("foreign_parent_id").toString();
                parentTB = reqDetail.get("foreign_parent_type").toString();

                if (StrUtil.isEmpty(reqDetail.get("foreign_id")) && StrUtil.isEmpty(reqDetail.get("foreign_type"))) {

                    HashMap<String, String> mParams = new HashMap<>();

                    if (parentTB.contains("Cup")) {
                        mParams.put("cupId", parentId);
                        mParams.put("cupInfoTB", parentTB);

                        parent = cupService.selectGetCupInfoForMedia(mParams);
                    } else if (parentTB.contains("League")) {
                        mParams.put("leagueId", parentId);
                        mParams.put("leagueInfoTB", parentTB);

                        parent = leagueService.selectGetLeagueInfoForMedia(mParams);
                    }

                } else {

                    childId = reqDetail.get("foreign_id").toString();
                    childTB = reqDetail.get("foreign_type").toString();

                    HashMap<String, String> mParams = new HashMap<>();

                    String childKey = "";

                    if (parentTB.contains("Cup")) {

                        if (childTB.contains("Cup_Sub")) {
                            childKey = "a.cup_sub_match_id";
                        } else if (childTB.contains("Cup_Main")) {
                            childKey = "a.cup_main_match_id";
                        } else if (childTB.contains("Cup_Tour")) {
                            childKey = "a.cup_tour_match_id";
                        }

                        mParams.put("parentId", parentId);
                        mParams.put("parentTB", parentTB);
                        mParams.put("childId", childId);
                        mParams.put("childTB", childTB);
                        mParams.put("childKey", childKey);

                        child = cupService.selectCupMatchForModifyResultRequest(mParams);

                        mParams.put("cupId", parentId);
                        mParams.put("cupInfoTB", parentTB);
                        parent = cupService.selectGetCupInfoForMedia(mParams);

                    } else if (parentTB.contains("League")) {

                        childKey = "a.league_match_id";

                        mParams.put("parentId", parentId);
                        mParams.put("parentTB", parentTB);
                        mParams.put("childId", childId);
                        mParams.put("childTB", childTB);
                        mParams.put("childKey", childKey);
                        child = leagueService.selectLeagueMatchForModifyResultRequest(mParams);

                        mParams.put("leagueId", parentId);
                        mParams.put("leagueInfoTB", parentTB);
                        parent = leagueService.selectGetLeagueInfoForMedia(mParams);

                    }
                    ageGroup = parentTB.substring(0, 3);
                }

            } else if (reqType.equals("team")) {

                HashMap<String, String> mParams = new HashMap<>();
                child = new HashMap<>();

                if (!StrUtil.isEmpty(reqDetail.get("foreign_parent_id"))) {
                    parentId = reqDetail.get("foreign_parent_id").toString();
                    mParams.put("teamId", parentId);
                    child = teamService.selectTeamInfo(mParams);
                    if (!StrUtil.isEmpty(reqDetail.get("foreign_id"))) {
                        child.put("year", reqDetail.get("foreign_id").toString());
                    }
                    if (!StrUtil.isEmpty(reqDetail.get("foreign_type"))) {
                        ageGroup = reqDetail.get("foreign_type").toString();
                    }
                }
            }
        }
        model.addAttribute("ageGroup", ageGroup);
        model.addAttribute("childTB", childTB);
        model.addAttribute("parent", parent);
        model.addAttribute("child", child);

        return "board/reqDetail";
    }

    @PostMapping("/modify_request")
    @ResponseBody
    public JSONObject modifyRequest(MultipartHttpServletRequest multi, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. params:" + params);

        JSONObject resultObj = new JSONObject();

        int result = 0;

        try {
            if (!params.get("method").equals("delete")) {
                List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("answerImg"), "answerImg");
                result = boardService.updateModifyRequest(params, imgFiles);
            } else {
                result = boardService.deleteRequest(params);
            }
        } catch (Exception e) {
            resultObj.put("state", "ERROR");
        }


        if (result > 0) {
            resultObj.put("state", "SUCCESS");
            resultObj.put("data", result);
        } else {
            resultObj.put("state", "FALSE");
        }


        return resultObj;
    }

    @PostMapping("/cancel_answer_request")
    @ResponseBody
    public JSONObject cancelAnswerRequest(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. params:" + params);

        JSONObject resultObj = new JSONObject();

        String reqType = params.get("reqType");
        String method = params.get("method");

        int result = 0;

        try {
            result = boardService.updateCancelAnswerRequest(params);
        } catch (Exception e) {
            resultObj.put("state", "ERROR");
        }


        if (result > 0) {
            resultObj.put("state", "SUCCESS");
            resultObj.put("data", result);
        } else {
            resultObj.put("state", "FALSE");
        }


        return resultObj;
    }

    @RequestMapping(value = "/oneToOneList")
    public String oneToOneList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. params:" + params);
        model.addAttribute("param", params);
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int cnt = boardService.selectOneToOneListCount(params);
        long totalCount = Long.valueOf(cnt);

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


        // QnA List
        List<HashMap<String, Object>> list = boardService.selectOneToOneList(params);
        model.addAttribute("oneToOneList", list);

        return "board/oneToOneList";
    }

    @RequestMapping(value = "/oneToOneDetail")
    public String oneToOneDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. reqType:" + params);

        // QnA 상세
        HashMap<String, Object> oneToOneDetail = boardService.selectOneToOneDetail(params);
        model.addAttribute("oneToOneDetail", oneToOneDetail);

        return "board/oneToOneDetail";
    }

    @PostMapping("/modify_oneToOne")
    @ResponseBody
    public JSONObject saveOneToOne(MultipartHttpServletRequest multi, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. params:" + params);

        JSONObject resultObj = new JSONObject();

        int result = 0;

        try {
            String method = params.get("method");
            if (method.equals("save") || method.equals("modify")) {
                List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("answerImg"), "answerImg");
                result = boardService.updateSaveAnswer(params, imgFiles);
            }

            if (method.equals("delete")) {
                result = boardService.deleteOneToOne(params);
            }
        } catch (Exception e) {
            resultObj.put("state", "ERROR");
        }


        if (result > 0) {
            resultObj.put("state", "SUCCESS");
            resultObj.put("data", result);
        } else {
            resultObj.put("state", "FALSE");
        }


        return resultObj;
    }

    @RequestMapping(value = "/noticeList")
    public String noticeList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. params:" + params);
        model.addAttribute("param", params);
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int cnt = boardService.selectNoticeListCount(params);
        long totalCount = Long.valueOf(cnt);

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


        // QnA List
        /*List<HashMap<String, Object>> list = boardService.selectNoticeList(params);*/
        List<BoardDto> list = boardService.selectNoticeList(params);
        model.addAttribute("noticeList", list);

        return "notice/list";
    }

    @RequestMapping(value = "/noticeDetail")
    public String noticeDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. reqType:" + params);

        // QnA 상세
        BoardDto noticeDetail = boardService.selectNoticeDetail(params);
        model.addAttribute("noticeDetail", noticeDetail);

        return "notice/detail";
    }

    @RequestMapping("/{method}Notice")
    public String modifyNotice(@RequestParam Map<String, String> params, @PathVariable String method, Model model, HttpServletResponse resp) {
        logger.info("referenceList was called. params:" + params);

        model.addAttribute("method", method);

        if (method.equals("update")) {

            BoardDto noticeDetail = boardService.selectNoticeDetail(params);
            model.addAttribute("noticeDetail", noticeDetail);

        }

        return "notice/modify";
    }

    @RequestMapping(value = "/{method}_notice", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyNotice(@PathVariable String method,
                                                             MultipartHttpServletRequest req,
                                                             MultipartHttpServletRequest multi,
                                                             @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyReference was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "Notice");

        Map<String, Object> map = new HashMap<String, Object>();
        
        String showFlag = params.get("showFlag");
        String showcheck = boardService.selectNoticeShowCheck(params);
        
        if (method.equals("save")) {
        	if (showFlag.equals("0")) {
        		if (!StrUtil.isEmpty(showcheck)) {
        			map.put("state", "fail");
        			return map;
        		}
        	}
        	
        	List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "attachFile");
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            boardService.insertNotice(params, files, imgFiles);
        }

        if (method.equals("modify")) {
        	if (showFlag.equals("0")) {
        		String boardId = params.get("boardId");
        		if (!StrUtil.isEmpty(showcheck) && !boardId.equals(showcheck)) {
        			map.put("state", "fail");
        			return map;
        		}
        	}
        	
        	List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "attachFile");
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            int modifyResult = boardService.updateNotice(params, files, imgFiles);
        }

        if (method.equals("delete")) {
            int result = boardService.updateDeleteNotice(params);
            if (result > 0) {
                map.put("state", "success");
            } else {
                map.put("state", "fail");
            }
        }


        return map;
    }

    /**
     * 콘텐츠 추가 요청
     * @param params
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping(value = "/suggestList")
    public String suggestList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. params:" + params);
        model.addAttribute("param", params);
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int cnt = boardService.selectSuggestListCount(params);
        long totalCount = Long.valueOf(cnt);

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


        // QnA List
        List<HashMap<String, Object>> suggestList = boardService.selectSuggestList(params);
        model.addAttribute("suggestList", suggestList);

        return "suggest/list";
    }

    /**
     * 컨텐츠 추가 요청 상세 페이지
     * @param params
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping(value = "/suggestDetail")
    public String suggestDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. reqType:" + params);

        // QnA 상세
        HashMap<String, Object> suggestDetail = boardService.selectSuggestDetail(params);
        model.addAttribute("suggestDetail", suggestDetail);

        return "suggest/detail";
    }

    /**
     * 콘텐츠 추가 요청 답변
     * @param params
     * @param model
     * @param resp
     * @return
     */
    @PostMapping("/modify_suggest")
    @ResponseBody
    public JSONObject saveSuggest(MultipartHttpServletRequest multi, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. params:" + params);

        JSONObject resultObj = new JSONObject();

        int result = 0;

        try {
            if (!params.get("method").equals("delete")) {
                List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("answerImg"), "answerImg");
                result = boardService.updateSaveSuggest(params, imgFiles);
            } else {
                result = boardService.deleteSuggest(params);
            }
        } catch (Exception e) {
            resultObj.put("state", "ERROR");
        }


        if (result > 0) {
            resultObj.put("state", "SUCCESS");
            resultObj.put("data", result);
        } else {
            resultObj.put("state", "FALSE");
        }


        return resultObj;
    }

    /**
     * 제휴 문의 목록
     * @param params
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping(value = "/partnershipList")
    public String partnershipList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qna was called. params:" + params);
        model.addAttribute("param", params);
        /*
         * 페이징 처리
         * -----------------------------------------------------------------------------
         * -------------------
         */
        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int cnt = boardService.selectPartnershipListCount(params);
        long totalCount = Long.valueOf(cnt);

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


        // QnA List
        //List<HashMap<String, Object>> partnershipList = boardService.selectPartnershipList(params);
        List<PartnershipDto> partnershipList = boardService.selectPartnershipList(params);
        model.addAttribute("partnershipList", partnershipList);

        return "partnership/list";
    }

    /**
     * 컨텐츠 추가 요청 상세 페이지
     * @param params
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping(value = "/partnershipDetail")
    public String partnershipDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("qnaDet was called. reqType:" + params);

        // QnA 상세
        HashMap<String, Object> partnershipDetail = boardService.selectPartnershipDetail(params);
        model.addAttribute("partnershipDetail", partnershipDetail);

        return "partnership/detail";
    }

    @PostMapping("/modify_partnership")
    @ResponseBody
    public JSONObject modifyPartnership(MultipartHttpServletRequest multi, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("modifyPartnership was called. params:" + params);

        JSONObject resultObj = new JSONObject();

        int result = 0;

        try {
            if (!params.get("method").equals("delete")) {

            } else {
                result = boardService.deletePartnership(params);
            }
        } catch (Exception e) {
            resultObj.put("state", "ERROR");
        }


        if (result > 0) {
            resultObj.put("state", "SUCCESS");
            resultObj.put("data", result);
        } else {
            resultObj.put("state", "FALSE");
        }


        return resultObj;
    }

    @RequestMapping(value = "/bannerList")
    public String bannerList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("bannerList was called. reqType:" + params);

        model.addAttribute("sKeyword", params.get("sKeyword"));

        int totalCnt = boardService.selectBannerListCnt(params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

        int tp = 1;
        if (totalCnt > 0) {
            tp = (int) totalCnt / cpp;
            if ((totalCnt % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp - 1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCnt, Define.COUNT_PAGE);

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end", map.get("end"));
        model.addAttribute("prev", map.get("prev"));
        model.addAttribute("next", map.get("next"));
        model.addAttribute("cp", cp); // 현재페이지번호
        model.addAttribute("cpp", cpp); // 현재페이지 갯수
        model.addAttribute("tp", tp); // 총 페이지 번호
        model.addAttribute("tc", totalCnt); // 총 리스트 갯수

        List<HashMap<String, Object>> bannerList = boardService.selectBannerList(params);

        model.addAttribute("bannerList", bannerList);

        return "board/bannerList";
    }

    @RequestMapping("/saveBanner")
    public String modifyBanner(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("modifyBanner was called. params:" + params);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();

        String method = params.get("method");

        model.addAttribute("uageList", uageList);
        model.addAttribute("method", method);

        if (method.equals("modify")) {

            HashMap<String, Object> bannerInfo = boardService.selectBannerInfo(params);
            model.addAttribute("bannerInfo", bannerInfo);
        }

        return "board/bannerModify";
    }

    @RequestMapping("/detailBanner")
    public String detailBanner(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("detailBanner was called. params:" + params);

        HashMap<String, Object> bannerInfo = boardService.selectBannerInfo(params);
        model.addAttribute("bannerInfo", bannerInfo);

        return "board/bannerDetail";
    }

    @RequestMapping(value = "/{method}_banner", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modify_Banner(@PathVariable String method,
                                                             MultipartHttpServletRequest req,
                                                             MultipartHttpServletRequest multi,
                                                             @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyBanner was called. params: " + params);
        logger.info("modifyBanner was called. multi: " + multi.getFile("file"));

        params.put("method", method);
        params.put("foreignType", "Banner");

        Map<String, Object> map = new HashMap<String, Object>();

        if (method.equals("save")) {

            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "PC");
            List<AttchFileInfoDto> mobileFiles = fileService.upload2(params, multi.getFiles("imgFile"), "Mobile");

            boardService.insertBanner(params, files, mobileFiles);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "PC");
            List<AttchFileInfoDto> mobileFiles = fileService.upload2(params, multi.getFiles("imgFile"), "Mobile");
            int modifyResult = boardService.updateBanner(params, files, mobileFiles);
        }

        if (method.equals("delete")) {
            int result = boardService.updateDeleteBanner(params);
            if (result > 0) {
                map.put("state", "SUCCESS");
                map.put("data", result);
            } else {
                map.put("state", "FALSE");
            }
        }


        return map;
    }

}
