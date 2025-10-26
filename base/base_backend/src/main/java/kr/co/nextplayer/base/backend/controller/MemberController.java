package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.TeamDto;
import kr.co.nextplayer.base.backend.mapper.HomeMapper;
import kr.co.nextplayer.base.backend.service.FileService;
import kr.co.nextplayer.base.backend.service.MemberService;
import kr.co.nextplayer.base.backend.service.UageService;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.backend.util.DateUtil.YYYY;

@Controller
public class MemberController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    @Resource
    MemberService memberService;

    @Resource
    HomeMapper homeMapper;

    @Resource
    FileService fileService;
    
    @Resource
    private UageService uageService;

    @RequestMapping(value = "/list")
    public String memberList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("member list was called. params:" + params);
        List<HashMap<String, Object>> memberList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> countMap = new HashMap<String, Object>();
        HashMap<String, Object> memberTotalMap = new HashMap<String, Object>();

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

        try {

            memberTotalMap = memberService.selectMemberCount();

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

            model.addAttribute("total", homeMapper.selectCountVisitorsTotal());
            model.addAttribute("today", homeMapper.selectCountVisitorsToday());

            model.addAttribute("totalMinute", homeMapper.selectCountVisitorsTotalMinute());
            model.addAttribute("todayMinute", homeMapper.selectCountVisitorsTodayMinute());
            
            List<HashMap<String, Object>> uageList = uageService.selectUageList();

            model.addAttribute("uageList", uageList);

            memberList = memberService.selectMemberList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("memberList", memberList);
        model.addAttribute("memberTotalMap", memberTotalMap);

        return "member/list";
    }

    @RequestMapping(value = "/member{method}")
    public String memberModify(@RequestParam Map<String, String> params, @PathVariable String method, Model model, HttpServletResponse resp) {
        logger.info("member was called. params:" + params);

        HashMap<String, Object> memberDetail = new HashMap<String, Object>();

        if("Modify".equals(method)) {
            try {
                memberDetail = memberService.selectMemberDetail(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("memberDetail", memberDetail);
        model.addAttribute("method", method);
        return "member/memberModify";
    }

    @RequestMapping(value = "/memberDetail")
    public String memberDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        HashMap<String, Object> memberDetail = new HashMap<String, Object>();

        try {
            memberDetail = memberService.selectMemberDetail(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("memberDetail", memberDetail);

        return "member/detail";
    }

    @RequestMapping(value = "/visitAvg")
    public String visitAvg(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        String sYear = params.get("sYear");
        String sMonth = params.get("sMonth");

        HashMap<String, Object> memberTotalMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> dayVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalDayVisitAvg = 0;

        List<HashMap<String, Object>> dayOrgVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalDayOrgVisitAvg = 0;

        List<HashMap<String, Object>> monthOrgVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalMonthOrgVisitAvg = 0;

        try {

            memberTotalMap = memberService.selectMemberCount();

            totalDayVisitAvg = memberService.selectTotalDayVisitAvg(params);
            dayVisitAvg = memberService.selectDayVisitAvg(params);

            totalDayOrgVisitAvg = memberService.selectTotalDayOrgVisitAvg(params);
            dayOrgVisitAvg = memberService.selectDayOrgVisitAvg(params);

            totalMonthOrgVisitAvg = memberService.selectTotalMonthOrgVisitAvg(params);
            monthOrgVisitAvg = memberService.selectMonthOrgVisitAvg(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("memberTotalMap", memberTotalMap);
        model.addAttribute("total", homeMapper.selectCountVisitorsTotal());
        model.addAttribute("today", homeMapper.selectCountVisitorsToday());

        model.addAttribute("totalDayVisitAvg", totalDayVisitAvg);
        model.addAttribute("dayVisitAvg", dayVisitAvg);

        model.addAttribute("totalDayOrgVisitAvg", totalDayOrgVisitAvg);
        model.addAttribute("dayOrgVisitAvg", dayOrgVisitAvg);

        model.addAttribute("totalMonthOrgVisitAvg", totalMonthOrgVisitAvg);
        model.addAttribute("monthOrgVisitAvg", monthOrgVisitAvg);

        return "member/visitAvg";
    }

    @RequestMapping(value = "/dayVisitAvg")
    @ResponseBody
    public Map<String, Object> dayVisitAvg(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> dayVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalDayVisitAvg = 0;

        try {

            totalDayVisitAvg = memberService.selectTotalDayVisitAvg(params);
            dayVisitAvg = memberService.selectDayVisitAvg(params);
            dataMap.put("totalDayVisitAvg", totalDayVisitAvg);
            dataMap.put("dayVisitAvg", dayVisitAvg);
            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return resultMap;
    }

    @RequestMapping(value = "/dayOrgVisitAvg")
    @ResponseBody
    public Map<String, Object> dayOrgVisitAvg(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> dayOrgVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalDayOrgVisitAvg= 0;

        try {

            totalDayOrgVisitAvg = memberService.selectTotalDayOrgVisitAvg(params);
            dayOrgVisitAvg = memberService.selectDayOrgVisitAvg(params);
            dataMap.put("totalDayOrgVisitAvg", totalDayOrgVisitAvg);
            dataMap.put("dayOrgVisitAvg", dayOrgVisitAvg);
            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return resultMap;
    }

    @RequestMapping(value = "/monthOrgVisitAvg")
    @ResponseBody
    public Map<String, Object> monthOrgVisitAvg(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> monthOrgVisitAvg = new ArrayList<HashMap<String, Object>>();
        int totalMonthOrgVisitAvg= 0;

        try {

            totalMonthOrgVisitAvg = memberService.selectTotalMonthOrgVisitAvg(params);
            monthOrgVisitAvg = memberService.selectMonthOrgVisitAvg(params);

            dataMap.put("totalDayOrgVisitAvg", totalMonthOrgVisitAvg);
            dataMap.put("monthOrgVisitAvg", monthOrgVisitAvg);

            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return resultMap;
    }

    @RequestMapping(value = "/memberAvg")
    public String memberAvg(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        String sYear = params.get("sYear");
        String sMonth = params.get("sMonth");

        HashMap<String, Object> memberTotalMap = new HashMap<String, Object>();

        int totalMemberJoinCnt = 0;
        List<HashMap<String, Object>> dayJoinMember = new ArrayList<HashMap<String, Object>>();

        int totalMemberCnt = 0;
        List<HashMap<String, Object>> memberCntByPosition = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByAgeGroup = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByCertType = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByGender = new ArrayList<HashMap<String, Object>>();

        List<HashMap<String, Object>> memberCntByPARENTS = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByDIRECTOR = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByTEACHER = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByACADEMY = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByPLAYER = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByETC = new ArrayList<HashMap<String, Object>>();

        List<HashMap<String, Object>> memberCntByParentsGender = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByDirectorGender = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByTeacherGender = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByAcademyGender = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByPlayerGender = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> memberCntByEtcGender = new ArrayList<HashMap<String, Object>>();

        int parentsMemberCnt = 0;
        int playerMemberCnt = 0;
        int academyMemberCnt = 0;
        int teacherMemberCnt = 0;
        int directorMemberCnt = 0;
        int etcMemberCnt = 0;

        try {

            memberTotalMap = memberService.selectMemberCount();

            totalMemberJoinCnt = memberService.selectTotalJoinMemberCnt(params);
            dayJoinMember = memberService.selectDayJoinMember(params);

            memberCntByPosition = memberService.selectMemberCntByPosition();
            memberCntByAgeGroup = memberService.selectMemberCntByAgeGroup();
            memberCntByCertType = memberService.selectMemberCntByCertType();
            memberCntByGender = memberService.selectMemberCntByGender();

            totalMemberCnt = memberService.selectMemberTotalCnt(params);

            // 계정 유형별 연령대 그래프
            memberCntByPARENTS = memberService.selectMemberCntByPositionAndAgeGroup("PARENTS");
            memberCntByDIRECTOR = memberService.selectMemberCntByPositionAndAgeGroup("DIRECTOR");
            memberCntByTEACHER = memberService.selectMemberCntByPositionAndAgeGroup("TEACHER");
            memberCntByACADEMY = memberService.selectMemberCntByPositionAndAgeGroup("ACADEMY");
            memberCntByPLAYER = memberService.selectMemberCntByPositionAndAgeGroup("PLAYER");
            memberCntByETC = memberService.selectMemberCntByPositionAndAgeGroup("ETC");

            // 계정 유형별 성별 그래프
            memberCntByParentsGender = memberService.selectMemberCntByPositionAndGender("PARENTS");
            memberCntByDirectorGender = memberService.selectMemberCntByPositionAndGender("DIRECTOR");
            memberCntByTeacherGender = memberService.selectMemberCntByPositionAndGender("TEACHER");
            memberCntByAcademyGender = memberService.selectMemberCntByPositionAndGender("ACADEMY");
            memberCntByPlayerGender = memberService.selectMemberCntByPositionAndGender("PLAYER");
            memberCntByEtcGender = memberService.selectMemberCntByPositionAndGender("ETC");

            params.put("position", "PARENTS");
            parentsMemberCnt = memberService.selectMemberTotalCnt(params);

            params.put("position", "PLAYER");
            playerMemberCnt = memberService.selectMemberTotalCnt(params);

            params.put("position", "ACADEMY");
            academyMemberCnt = memberService.selectMemberTotalCnt(params);

            params.put("position", "TEACHER");
            teacherMemberCnt = memberService.selectMemberTotalCnt(params);

            params.put("position", "DIRECTOR");
            directorMemberCnt = memberService.selectMemberTotalCnt(params);

            params.put("position", "ETC");
            etcMemberCnt = memberService.selectMemberTotalCnt(params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("memberTotalMap", memberTotalMap);
        model.addAttribute("total", homeMapper.selectCountVisitorsTotal());
        model.addAttribute("today", homeMapper.selectCountVisitorsToday());

        model.addAttribute("totalMemberJoinCnt", totalMemberJoinCnt);
        model.addAttribute("dayJoinMember", dayJoinMember);

        model.addAttribute("memberCntByPosition", memberCntByPosition);
        model.addAttribute("memberCntByAgeGroup", memberCntByAgeGroup);
        model.addAttribute("memberCntByCertType", memberCntByCertType);
        model.addAttribute("memberCntByGender", memberCntByGender);

        model.addAttribute("totalMemberCnt", totalMemberCnt);

        model.addAttribute("memberCntByPARENTS", memberCntByPARENTS);
        model.addAttribute("memberCntByDIRECTOR", memberCntByDIRECTOR);
        model.addAttribute("memberCntByTEACHER", memberCntByTEACHER);
        model.addAttribute("memberCntByACADEMY", memberCntByACADEMY);
        model.addAttribute("memberCntByPLAYER", memberCntByPLAYER);
        model.addAttribute("memberCntByETC", memberCntByETC);

        model.addAttribute("memberCntByParentsGender", memberCntByParentsGender);
        model.addAttribute("memberCntByDirectorGender", memberCntByDirectorGender);
        model.addAttribute("memberCntByTeacherGender", memberCntByTeacherGender);
        model.addAttribute("memberCntByAcademyGender", memberCntByAcademyGender);
        model.addAttribute("memberCntByPlayerGender", memberCntByPlayerGender);
        model.addAttribute("memberCntByEtcGender", memberCntByEtcGender);

        model.addAttribute("parentsMemberCnt", parentsMemberCnt);
        model.addAttribute("playerMemberCnt", playerMemberCnt);
        model.addAttribute("academyMemberCnt", academyMemberCnt);
        model.addAttribute("teacherMemberCnt", teacherMemberCnt);
        model.addAttribute("directorMemberCnt", directorMemberCnt);
        model.addAttribute("etcMemberCnt", etcMemberCnt);

        return "member/memberAvg";
    }

    @RequestMapping(value = "/dayJoinAvg")
    @ResponseBody
    public Map<String, Object> dayJoinAvg(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> dayJoinMember = new ArrayList<HashMap<String, Object>>();
        int totalMemberJoinCnt = 0;

        try {

            totalMemberJoinCnt = memberService.selectTotalJoinMemberCnt(params);
            dayJoinMember = memberService.selectDayJoinMember(params);

            dataMap.put("totalMemberJoinCnt", totalMemberJoinCnt);
            dataMap.put("dayJoinMember", dayJoinMember);

            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return resultMap;
    }

    @RequestMapping(value = "/personalModify")
    public String personalModify(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

        try {

            int result = memberService.modifyMember(params);

            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return "redirect:/list";
    }

    @RequestMapping(value = "/personalDelete")
    public String personalDelete(String memberCd, HttpServletResponse resp) {

        try {

            int result = memberService.deleteMember(memberCd);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "redirect:/list";
    }

    @RequestMapping(value = "/emailUseCheck")
    @ResponseBody
    public Map<String, Object> emailUseCheck(@RequestParam Map<String, String> params, HttpServletResponse resp) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<HashMap<String, Object>> dayJoinMember = new ArrayList<HashMap<String, Object>>();
        int activeEmailCount = 0;

        try {

            activeEmailCount = memberService.selectCheckActiveEmail(params);

            resultMap.put("data", activeEmailCount);
            resultMap.put("state", "SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }


        return resultMap;
    }

    @RequestMapping(value = "/memberEducation")
    public String memberEducation(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        List<HashMap<String, Object>> memberEducation = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> memberDetail = new HashMap<String, Object>();
        int totalCount = 0;

        try {

            memberDetail = memberService.selectMemberDetail(params);

            int cp = StrUtil.getCurrentPage(params);
            int cpp = Define.COUNT_PER_PAGE; // 현재페이지 갯수

            totalCount = memberService.selectMemberEducationListCnt(params);

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

            memberEducation = memberService.selectMemberEducationList(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("memberEducation", memberEducation);
        model.addAttribute("memberDetail", memberDetail);

        return "member/education/list";
    }

    @RequestMapping(value = "memberEducationModify")
    public String memberEducationModify(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("memberEducationModify was called. params:"+params);

        String method = params.get("method");
        model.addAttribute("params", params);

        if (method.equals("Modify")) {
            HashMap<String, Object> memberDetail = memberService.selectDetailMemberEducation(params);
            model.addAttribute("memberDetail", memberDetail);
        }

        return "member/education/modify";
    }

    @RequestMapping(value = "/{method}_member_education", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyMemberEducation(@PathVariable String method,
                                                             MultipartHttpServletRequest req,
                                                             MultipartHttpServletRequest multi,
                                                             @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyMemberEducation was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "EduFile");

        Map<String, Object> map = new HashMap<String, Object>();

        int result = 0;

        if (method.equals("save")) {
            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "");
            result = memberService.insertMemberEducation(params, files);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "");
            result = memberService.updateMemberEducation(params, files);
        }

        if (method.equals("delete")) {
            result = memberService.deleteMemberEducation(params);
        }

        if (result > 0) {
            map.put("state", "success");
        } else {
            map.put("state", "fail");
        }

        return map;
    }

}
