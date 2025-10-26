package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.EducationDto;
import kr.co.nextplayer.base.backend.service.FileService;
import kr.co.nextplayer.base.backend.service.PopupService;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class PopupController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private PopupService popupService;

    @Resource
    private FileService fileService;

    @RequestMapping(value = "/popupList")
    public String popupList(@RequestParam Map<String, String> params, Model model) {
        logger.info("popupList was called. params:" + params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = popupService.selectPopupListCnt(params);
        int tp = 1;
        if (totalCount > 0) {
            tp = (int)totalCount / cpp;
            if ((totalCount % cpp) > 0) {
                tp += 1;
            }
        }

        int sRow = (cp -1) * cpp;

        params.put("sRow", "" + sRow);
        params.put("eCount", "" + cpp);

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, Define.COUNT_PAGE);

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수

        List<HashMap<String, Object>> popupList = popupService.selectPopupList(params);
        model.addAttribute("popupList", popupList);

        return "popup/list";
    }

    @RequestMapping(value = "popupModify")
    public String popupModify(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("popupModify was called. params:"+params);

        String method = params.get("method");

        params.put("today", DateUtil.getCurrentDate(DateUtil.YYYYMMDD));
        params.put("threeDay", DateUtil.add(Calendar.DATE, 3));
        params.put("sevenDay", DateUtil.add(Calendar.DATE, 7));
        params.put("tenDay", DateUtil.add(Calendar.DATE, 10));
        params.put("twentyDay", DateUtil.add(Calendar.DATE, 20));
        params.put("thirtyDay", DateUtil.add(Calendar.DATE, 30));

        if (method.equals("Modify")) {
            Map<String, Object> popupInfo = popupService.selectPopupDetail(params);
            model.addAttribute("popupInfo", popupInfo);
        }

        model.addAttribute("params", params);
        model.addAttribute("method", method);
        return "popup/modify";
    }

    @RequestMapping(value = "/{method}_popup", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyPopup(@PathVariable String method,
                                                             MultipartHttpServletRequest req,
                                                             MultipartHttpServletRequest multi,
                                                             @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyPopup was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "Popup");

        Map<String, Object> map = new HashMap<String, Object>();

        int result = 0;

        if (method.equals("save")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("file"), "");
            result = popupService.insertPopup(params, imgFiles);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("file"), "");
            result = popupService.updatePopup(params, imgFiles);
        }

        if (method.equals("delete")) {
            result = popupService.deleteEducation(params);
        }

        if (result > 0) {
            map.put("state", "success");
        } else {
            map.put("state", "fail");
        }
        return map;
    }

    @RequestMapping("popupDetail")
    public String popupDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("popupDetail was called. params:" + params);

        Map<String, Object> popupInfo = popupService.selectPopupDetail(params);
        model.addAttribute("popupInfo", popupInfo);


        return "popup/detail";
    }

}
