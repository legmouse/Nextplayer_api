package kr.co.nextplayer.base.backend.controller;



import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Controller
public class PushController {
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private UageService uageService;

    @Resource
    private PushService pushService;

    @Resource
    private CupService cupService;

    @Resource
    private LeagueService leagueService;
    
    @Resource
    private MemberService memberService;

    @RequestMapping("/push")
    public String alrimList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("alrimListr was called. params:" + params);
        
        List<HashMap<String, Object>> pushList = pushService.selectPushInfoList();
        
        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();
        
        model.addAttribute("pushList", pushList);
        model.addAttribute("uageList", uageList);

//        HashMap<String, Object> bannerInfo = boardService.selectBannerInfo(params);
//        model.addAttribute("bannerInfo", bannerInfo);

        return "alrim/list";
    }
    
    
    @RequestMapping("/selectCupMatch")
    public String contestPush(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("selectCupMatch was called. params:" + params);
        
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

                params.put("ageGroup", uageStr);
                params.put("cupInfoTB", cupInfoTB);
                params.put("cupSubMatchTB", cupSubMatchTB);
                params.put("cupMainMatchTB", cupMainMatchTB);
                params.put("cupTourMatchTB", cupTourMatchTB);

                List<HashMap<String, Object>> cupMatch = cupService.selectCupMatchList(params);

                model.addAttribute(uageStr + "CupMatch", cupMatch);
            }

            model.addAttribute("sdate", sdate);
            
            String method = params.get("method");
            String text = params.get("bodyText");
            String title = params.get("titleText");
            String typeId = params.get("typeId");
            model.addAttribute("typeId", typeId);
            model.addAttribute("method", method);
            model.addAttribute("bodyText", text);
            model.addAttribute("titleText", title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "alrim/cupMatchList";
    }

    @PostMapping("/insertContestPush")
    public @ResponseBody Map<String, Object> insertContestPush(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> insertContestPush params : "+ params);

        Map<String, Object> map = new HashMap<>();
        
        String reqDt = params.get("date");
        if (StrUtil.isEmpty(reqDt)) {
        	String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        	
        	params.put("date", today);
        }
        
        /*int result = pushService.insertInterestTeamMemberPush(params);
        if (result > 0) {
        	String matchType = (String)params.get("matchType");
        	if (!StrUtil.isEmpty(matchType)) {
        		String matchTB = "";
            	switch(matchType) {
            		case "SUB" :
            			matchTB = params.get("uage") + "_cup_sub_match";
            			params.put("matchTB", matchTB);
            			pushService.updateSubMatchSendFlag(params);
            			break;
            		case "MAIN" :
            			matchTB = params.get("uage") + "_cup_main_match";
            			params.put("matchTB", matchTB);
            			pushService.updateMainMatchSendFlag(params);
            			break;
            		case "TOUR" :
            			matchTB = params.get("uage") + "_cup_tour_match";
            			params.put("matchTB", matchTB);
            			pushService.updateTourMatchSendFlag(params);
            			break;
            	}
        	}
        }*/
        //map.put("result", result > 0 ? "success" : "fail");
        map.put("result", "success");

        return map;
    }
    
    @PostMapping("/insertCupPush")
    public @ResponseBody Map<String, Object> insertCupPush(@RequestBody Map<String, String> params) throws Exception {
        logger.info("request ----> insertCupPush params : "+ params);

        Map<String, Object> map = new HashMap<>();

        String reqDt = params.get("date");
        if (StrUtil.isEmpty(reqDt)) {
        	String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        	
        	params.put("date", today);
        }
        
        int result = pushService.insertInterestAgeGorupMemberPush(params);
        if (result > 0) {
        	String cupInfoTB = params.get("uage") + "_cup_info";
        	params.put("cupInfoTB", cupInfoTB);
        	pushService.updateCupSendFlag(params);
        }
        map.put("result", result > 0 ? "success" : "fail");

        return map;
    }
    
//    @PostMapping("/insertPush")
//    public @ResponseBody Map<String, Object> insertPush(@RequestBody Map<String, Object> params) throws Exception {
//        logger.info("request ----> insertContestPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        int result = pushService.insertMemberPush(params);
//        map.put("result", result > 0 ? "success" : "fail");
//
//        return map;
//    }
    
//    @PostMapping("/insertInterestTeamPush")
//    public @ResponseBody Map<String, Object> insertInterestTeamPush(@RequestBody Map<String, String> params) throws Exception {
//        logger.info("request ----> insertInterestTeamPush params : "+ params);
//
//        Map<String, Object> map = new HashMap<>();
//
//
//        int result = configService.insertInterestTeamMemberPush(params);
//        map.put("result", result == 1 ? "success" : "fail");
//
//        return map;
//    }
    
    @RequestMapping(value = "/push_info")
    @ResponseBody
    public Map<String, Object> push_info(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("request ----> push_info params : "+ params);
        
        HashMap<String, Object> pushInfo = pushService.selectPushInfo(params);
        
        resultMap.put("data", pushInfo);
        return resultMap;
    }
    
    @RequestMapping(value = "/update_push")
    @ResponseBody
    public Map<String, Object> update_push(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("request ----> update_push params : "+ params);
        
        int result = 0;

        try {
        	result = pushService.updatePush(params);

            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/cupSelectList")
    @ResponseBody
    public Map<String, Object> cupSelectList(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // logger.info("request ----> cupSelectList params : "+ params);
        
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
        
        List<HashMap<String, Object>> cupInfoList = cupService.selectCupInfoList(params);
        logger.info("params : "+ params);
        logger.info("cupInfoList : "+ cupInfoList);
        resultMap.put("data", cupInfoList);
        
        return resultMap;
    }
    
    @RequestMapping(value = "/addPush")
    @ResponseBody
    public Map<String, Object> addPush(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // logger.info("request ----> cupSelectList params : "+ params);
        
        int result = 0;

        try {
        	result = pushService.insertPush(params);

            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        
        return resultMap;
    }

    @RequestMapping(value = "/pushList")
    public String pushList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("member list was called. params:" + params);
        List<HashMap<String, Object>> pushList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> countMap = new HashMap<String, Object>();

        try {

            int cp = StrUtil.getCurrentPage(params);
            int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

            countMap = pushService.selectSendPushListCount(params);
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

            model.addAttribute("start", map.get("start"));
            model.addAttribute("end", map.get("end"));
            model.addAttribute("prev", map.get("prev"));
            model.addAttribute("next", map.get("next"));

            model.addAttribute("cp", cp); // 현재페이지번호
            model.addAttribute("cpp", cpp); // 현재페이지 갯수
            model.addAttribute("tp", tp); // 총 페이지 번호
            model.addAttribute("tc", totalCount); // 총 리스트 갯수
            model.addAttribute("type", params.get("type"));
            pushList = pushService.selectSendPushList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        model.addAttribute("pushList", pushList);

        return "alrim/pushList";
    }
    
    @RequestMapping(value = "/sendPush")
    public String sendPush(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("member list was called. params:" + params);
        List<HashMap<String, Object>> memberList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> countMap = new HashMap<String, Object>();

        String memberState = params.get("memberState");

        if (StrUtil.isEmpty(memberState)) {
            memberState = "1";
            params.put("memberState", memberState);
        }

        model.addAttribute("memberState", memberState);
        model.addAttribute("sex", params.get("sex"));
        model.addAttribute("memberType", params.get("memberType"));
        model.addAttribute("uage", params.get("uage"));

        model.addAttribute("searchKeyword", params.get("searchKeyword"));
        model.addAttribute("sDate", params.get("sDate"));
        model.addAttribute("eDate", params.get("eDate"));
        
        String pushAgree = params.get("pushAgree");
        model.addAttribute("pushAgree", pushAgree);
        
        String ageStr = params.get("age");
        if (!StrUtil.isEmpty(params.get("age"))) {
        	int age = Integer.parseInt(ageStr);
            
            LocalDate today = LocalDate.now();
            int todayYear = today.getYear();
            
            int endAge = todayYear - age;
            int startAge = todayYear - (age + 9);
            params.put("endAge", String.valueOf(endAge));
            params.put("startAge", String.valueOf(startAge));
        }
        model.addAttribute("age", ageStr);
        params.put("push", "0");

        try {

            int cp = StrUtil.getCurrentPage(params);
            int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

            countMap = memberService.selectMemberListCount(params);
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

            model.addAttribute("start", map.get("start"));
            model.addAttribute("end", map.get("end"));
            model.addAttribute("prev", map.get("prev"));
            model.addAttribute("next", map.get("next"));

            model.addAttribute("cp", cp); // 현재페이지번호
            model.addAttribute("cpp", cpp); // 현재페이지 갯수
            model.addAttribute("tp", tp); // 총 페이지 번호
            model.addAttribute("tc", totalCount); // 총 리스트 갯수

            List<HashMap<String, Object>> uageList = uageService.selectUageList();

            model.addAttribute("uageList", uageList);
            memberList = memberService.selectMemberList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        List<HashMap<String, Object>> pushList = pushService.selectPushListToSend();
        model.addAttribute("pushList", pushList);
        model.addAttribute("memberList", memberList);

        return "alrim/sendPush";
    }
    
    @RequestMapping(value = "/send_push")
    @ResponseBody
    public Map<String, Object> send_push(@RequestParam Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
         logger.info("request ----> send_push params : "+ params);
        
        int result = 0;
        
        String reqDt = params.get("date");
        if (StrUtil.isEmpty(reqDt)) {
        	String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        	
        	params.put("date", today);
        }

        try {
        	result = pushService.insertSendPush(params);

            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);
        
        return resultMap;
    }

    @PostMapping(value = "/sendPushAll")
    @ResponseBody
    public Map<String, Object> sendPushAll(@RequestBody Map<String, String> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("request ----> send_push params : "+ params);

        int result = 0;

        String reqDt = params.get("date");
        if (StrUtil.isEmpty(reqDt)) {
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            params.put("date", today);
        }

        String memberState = params.get("memberState");

        if (StrUtil.isEmpty(memberState)) {
            memberState = "1";
            params.put("memberState", memberState);
        }

        String ageStr = params.get("age");
        if (!StrUtil.isEmpty(params.get("age"))) {
            int age = Integer.parseInt(ageStr);

            LocalDate today = LocalDate.now();
            int todayYear = today.getYear();

            int endAge = todayYear - age;
            int startAge = todayYear - (age + 9);
            params.put("endAge", String.valueOf(endAge));
            params.put("startAge", String.valueOf(startAge));
        }
        params.put("push", "0");

        try {

            List<HashMap<String, Object>> memberList = memberService.selectMemberList(params);
            result = pushService.insertSendPushAll(params, memberList);

            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }
        resultMap.put("data", result);

        return resultMap;
    }

    
    @RequestMapping(value = "/pushDetail")
    public String pushDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("push detail was called. params:" + params);
        
        List<HashMap<String, Object>> memberList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> countMap = new HashMap<String, Object>();

        String memberState = params.get("memberState");

        if (StrUtil.isEmpty(memberState)) {
            memberState = "1";
            params.put("memberState", memberState);
        }
        
        String pushContentId = params.get("pushContentId");
        model.addAttribute("pushContentId", pushContentId);
        
        String num = params.get("num");
        model.addAttribute("num", num);
        
        model.addAttribute("memberState", memberState);
        model.addAttribute("sex", params.get("sex"));
        model.addAttribute("memberType", params.get("memberType"));
        model.addAttribute("uage", params.get("uage"));

        model.addAttribute("searchKeyword", params.get("searchKeyword"));
        
        String pushAgree = params.get("pushAgree");
        model.addAttribute("pushAgree", pushAgree);
        
        String ageStr = params.get("age");
        if (!StrUtil.isEmpty(params.get("age"))) {
        	int age = Integer.parseInt(ageStr);
            
            LocalDate today = LocalDate.now();
            int todayYear = today.getYear();
            
            int endAge = todayYear - age;
            int startAge = todayYear - (age + 9);
            params.put("endAge", String.valueOf(endAge));
            params.put("startAge", String.valueOf(startAge));
        }
        model.addAttribute("age", ageStr);

        try {

            int cp = StrUtil.getCurrentPage(params);
            int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

            countMap = pushService.selectMemberPushListCount(params);
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

            model.addAttribute("start", map.get("start"));
            model.addAttribute("end", map.get("end"));
            model.addAttribute("prev", map.get("prev"));
            model.addAttribute("next", map.get("next"));

            model.addAttribute("cp", cp); // 현재페이지번호
            model.addAttribute("cpp", cpp); // 현재페이지 갯수
            model.addAttribute("tp", tp); // 총 페이지 번호
            model.addAttribute("tc", totalCount); // 총 리스트 갯수

            List<HashMap<String, Object>> uageList = uageService.selectUageList();

            model.addAttribute("uageList", uageList);

            memberList = pushService.selectMemberPushList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        HashMap<String, Object> pushInfo = pushService.selectPushDetail(params);
        
        model.addAttribute("pushInfo", pushInfo);
        model.addAttribute("memberList", memberList);

        return "alrim/detail";
    }
}
