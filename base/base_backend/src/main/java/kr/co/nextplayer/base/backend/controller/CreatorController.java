package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.service.*;
import kr.co.nextplayer.base.backend.util.DateUtil;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.backend.util.DateUtil.YYYY;

@Controller
public class CreatorController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CreatorService creatorService;

    @Resource
    private UageService uageService;

    @Resource
    private FileService fileService;

    @RequestMapping(value = "/creator{mediaType}")
    public String creatorList(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) throws Exception {
        logger.info("media was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }
        String subType = params.get("subType");
        if (StrUtil.isEmpty(subType)) {
            subType = "0";
            params.put("subType", subType);
        }
        model.addAttribute("subType", subType);

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);

        String categoryType = "";

        int totalCnt = creatorService.selectCreatorListCnt(params);

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

        List<HashMap<String, Object>> creatorList = creatorService.selectCreatorList(params);

        model.addAttribute("creatorList", creatorList);
        model.addAttribute("params", params);

        return "creator/list";
    }

    @RequestMapping(value = "/creatorDetail{mediaType}")
    public String detail(@RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) throws Exception {
        logger.info("detail was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }

        HashMap<String, Object> creatorInfo = creatorService.selectCreatorInfo(params);

        Map<String, String> imgParam = new HashMap<String, String>();

        imgParam.put("foreignId", creatorInfo.get("creator_id").toString());
        imgParam.put("foreignType", "Creator");

        imgParam.put("subType", "profileImg");
        Map<String, Object> profileImg = creatorService.selectImgFileInfo(imgParam);
        // bannerImg
        imgParam.put("subType", "bannerImg");
        Map<String, Object> bannerImg = creatorService.selectImgFileInfo(imgParam);

        model.addAttribute("creatorInfo", creatorInfo);
        model.addAttribute("profileImg", profileImg);
        model.addAttribute("bannerImg", bannerImg);

        model.addAttribute("mediaType", mediaType);
        params.put("mediaType", mediaType);

        return "creator/detail";
    }

    @RequestMapping(value = "/creator_{method}")
    public String regist(@RequestParam Map<String, String> params, @PathVariable String method, Model model) throws Exception {
        logger.info("media was called. params:" + params);
        logger.info("method:" + method);

        model.addAttribute("mediaType", params.get("mediaType"));

        if (method.equals("modify")) {
            HashMap<String, Object> creatorInfo = creatorService.selectCreatorInfo(params);

            model.addAttribute("creatorInfo", creatorInfo);

            Map<String, String> imgParam = new HashMap<String, String>();

            imgParam.put("foreignId", creatorInfo.get("creator_id").toString());
            imgParam.put("foreignType", "Creator");

            imgParam.put("subType", "profileImg");
            Map<String, Object> profileImg = creatorService.selectImgFileInfo(imgParam);
            // bannerImg
            imgParam.put("subType", "bannerImg");
            Map<String, Object> bannerImg = creatorService.selectImgFileInfo(imgParam);

            model.addAttribute("profileImg", profileImg);
            model.addAttribute("bannerImg", bannerImg);

        }

        return "creator/modify";
    }

    @RequestMapping(value = "/saveCreator{mediaType}")
    public @ResponseBody Map<String, Object> saveMedia(MultipartHttpServletRequest request, @RequestParam Map<String, String> params, @PathVariable String mediaType, Model model, HttpServletResponse resp) throws Exception {
        logger.info("saveMedia was called. params:" + params);
        logger.info("mediaType:" + mediaType);

        Map<String, Object> map = new HashMap<String, Object>();

        if (StrUtil.isEmpty(mediaType)) {
            mediaType = "Video";
        }
        params.put("mediaType", mediaType);

        String method = params.get("method");
        params.put("foreignType", "Creator");

        int result = 0;

        if (method.equals("regist")) {
            result = creatorService.insertCreator(params, request);
        }

        if (method.equals("delete")) {
            result = creatorService.updateCreatorDelete(params);
        }

        if (method.equals("modify")) {
            result = creatorService.updateCreator(params, request);
        }

        map.put("mediaType", mediaType);

        if (result > 0) {
            map.put("state", "success");
        } else {
            map.put("state", "fail");
        }

        return map;
    }

}
