package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.service.FileService;
import kr.co.nextplayer.base.backend.service.PlayerService;
import kr.co.nextplayer.base.backend.service.UageService;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class PlayerController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private PlayerService playerService;

    @Resource
    private UageService uageService;

    @Resource
    private FileService fileService;

    @RequestMapping("/playerList")
    public String playerList(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        int totalCnt = playerService.selectPlayerListCnt(params);

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
        model.addAttribute("tc", totalCnt); // 총 리스트 갯수\

        List<HashMap<String, Object>> playerList = playerService.selectPlayerList(params);
        model.addAttribute("playerList", playerList);

        model.addAttribute("sKeyword", params.get("sKeyword"));
        model.addAttribute("sPosition", params.get("sPosition"));
        model.addAttribute("sYear", params.get("sYear"));

        return "player/list";
    }

    @RequestMapping(value = "/detailPlayer")
    public String detail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        HashMap<String, Object> playerInfo = playerService.selectPlayerInfo(params);
        List<HashMap<String, Object>> playerHistory = playerService.selectPlayerHistoryDetail(params);

        model.addAttribute("playerInfo", playerInfo);
        model.addAttribute("playerHistory", playerHistory);

        return "player/detail";
    }

    @RequestMapping(value = "/savePlayer")
    public String playerModify(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {

        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        String method = params.get("method");

        if (StrUtil.isEmpty(method)) {
            method = "save";
        }

        if (method.equals("modify")) {
            HashMap<String, Object> playerInfo = playerService.selectPlayerInfo(params);
            List<HashMap<String, Object>> playerHistory = playerService.selectPlayerHistoryDetail(params);

            model.addAttribute("playerInfo", playerInfo);
            model.addAttribute("playerHistory", playerHistory);
        }

        model.addAttribute("method", method);
        
        String playerName = params.get("playerName");
        String position = params.get("position");
        String birthday = params.get("birthday");
        
        model.addAttribute("playerName", playerName);
        model.addAttribute("position", position);
        model.addAttribute("birthday", birthday);

        return "player/modify";
    }

    @RequestMapping(value = "/player/search_team")
    @ResponseBody
    public Map<String, Object> search_team(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        logger.info("search_cup_detail was called. params:" + params);

        try {
            List<HashMap<String, Object>> teamList = playerService.selectSearchPlayerTeam(params);
            dataMap.put("teamList", teamList);

            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        return resultMap;
    }

    @RequestMapping(value = "/{method}_player")
    public String save_player(@RequestParam Map<String, String> params, @PathVariable String method, Model model, HttpServletResponse resp) {
        logger.info("save_player was called. params:" + params);

        if (method.equals("save")) {
            int saveResult = playerService.savePlayer(params);
        }

        if (method.equals("modify")) {
            int modifyResult = playerService.modifyPlayer(params);
        }

        if (method.equals("delete" )) {
            int deleteResult = playerService.deletePlayer(params);
        }

        return "redirect:/playerList";
    }

    @RequestMapping(value = "/playerExcelUpload")
    @ResponseBody
    public Map<String, Object> save_player(MultipartHttpServletRequest request, @RequestParam Map<String, String> params,
                           Model model, HttpServletResponse resp, RedirectAttributes attributes) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("excelUpload was called. params:" + params);

        MultipartFile excelFile = request.getFile("excelFile");

        if (excelFile == null || excelFile.isEmpty()) {

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        Map<String, Object> result = new HashMap<String, Object>();

        try {
            result = playerService.excelPlayerSave(request, params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", result);
        return resultMap;
    }

    @RequestMapping("/{roster}PlayerList")
    public String rosterPlayerList(@RequestParam Map<String, String> params, @PathVariable String roster, Model model, HttpServletResponse resp) {
        logger.info("rosterPlayerList was called. params:" + params);

        if (StrUtil.isEmpty(roster)) {
            roster = "national";
        }
        params.put("type", roster);
        model.addAttribute("type", roster);

        List<HashMap<String, Object>> uageList = uageService.selectUseUageList();
        model.addAttribute("uageList", uageList);

        model.addAttribute("sYear", params.get("sYear"));
        model.addAttribute("ageGroup", params.get("ageGroup"));
        model.addAttribute("sKeyword", params.get("sKeyword"));

        int totalCnt = playerService.selectRosterPlayerListCnt(params);

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
        model.addAttribute("tc", totalCnt); // 총 리스트 갯수\

        List<HashMap<String, Object>> rosterList = playerService.selectRosterPlayerList(params);
        model.addAttribute("rosterList", rosterList);

        return "player/roster/list";
    }

    @RequestMapping(value = "/detailRoster")
    public String rosterDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        logger.info("rosterDetail was called. params:" + params);
        String roster = params.get("roster");

        HashMap<String, Object> rosterInfo = playerService.selectRosterInfo(params);
        List<HashMap<String, Object>> rosterPlayer = playerService.selectRosterPlayerDetail(params);

        model.addAttribute("type", roster);
        model.addAttribute("rosterInfo", rosterInfo);
        model.addAttribute("rosterPlayer", rosterPlayer);

        return "player/roster/detail";
    }

    @RequestMapping(value = "/{roster}Save")
    public String rosterModify(@RequestParam Map<String, String> params, @PathVariable String roster, Model model, HttpServletResponse resp) {
        logger.info("rosterModify was called. params:" + params);

        List<HashMap<String, Object>> uageList = uageService.selectUageList();
        model.addAttribute("uageList", uageList);

        if (StrUtil.isEmpty(roster)) {
            roster = "national";
        }

        String method = params.get("method");

        if (StrUtil.isEmpty(method)) {
            method = "save";
        }

        if (method.equals("modify")) {
            HashMap<String, Object> rosterInfo = playerService.selectRosterInfo(params);
            List<HashMap<String, Object>> rosterPlayer = playerService.selectRosterPlayerDetail(params);

            model.addAttribute("rosterInfo", rosterInfo);
            model.addAttribute("rosterPlayer", rosterPlayer);
        }

        model.addAttribute("type", roster);
        model.addAttribute("method", method);

        return "player/roster/modify";
    }

    @RequestMapping(value = "/search_player")
    @ResponseBody
    public Map<String, Object> search_player(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        logger.info("search_player was called. params:" + params);

        try {
            List<HashMap<String, Object>> playerList = playerService.selectSearchPlayerList(params);
            dataMap.put("playerList", playerList);

            resultMap.put("data", dataMap);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        return resultMap;
    }

    @RequestMapping(value = "/save_roster")
    public String save_roster(MultipartHttpServletRequest multi, @RequestParam Map<String, String> params, Model model, HttpServletResponse resp) throws Exception {
        logger.info("save_roster was called. params:" + params);

        String method = params.get("method");
        String roster = params.get("roster");

        if (method.equals("save")) {
            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "attachFile");
            int saveResult = playerService.saveRoster(params, files);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> files = fileService.upload2(params, multi.getFiles("file"), "attachFile");
            int modifyResult = playerService.modifyRoster(params, files);
        }

        if (method.equals("delete" )) {
            int deleteResult = playerService.updateDeleteRoster(params);
        }

        return "redirect:/" + roster + "PlayerList";
    }

    @RequestMapping(value = "/rosterExcelUpload")
    @ResponseBody
    public Map<String, Object> save_rosterExcel(MultipartHttpServletRequest request, @RequestParam Map<String, String> params,
                              Model model, HttpServletResponse resp, RedirectAttributes attributes) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("save_rosterExcel was called. params:" + params);

        String roster = params.get("roster");

        MultipartFile excelFile = request.getFile("excelFile");

        if (excelFile == null || excelFile.isEmpty()) {

            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }

        Map<String, Object> result = new HashMap<String, Object>();

        try {
            result = playerService.excelRosterSave(request, params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", result);
        return resultMap;
    }

}
