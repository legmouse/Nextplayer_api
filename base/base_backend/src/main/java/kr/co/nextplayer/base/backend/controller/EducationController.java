package kr.co.nextplayer.base.backend.controller;

import com.google.gson.JsonObject;
import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.dto.EducationDto;
import kr.co.nextplayer.base.backend.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;
import kr.co.nextplayer.base.backend.util.Define;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static kr.co.nextplayer.base.backend.util.Define.LIVE_WEB_PATH;
import static kr.co.nextplayer.base.backend.util.Define.LOCAL_WEB_PATH;

@Controller
public class EducationController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private EducationService educationService;

    @Resource
    private StoreService storeService;

    @Resource
    private UageService uageService;

    @Resource
    private FileService fileService;

    @RequestMapping(value = "educationList")
    public String educationList(@RequestParam Map<String, String> params, Model model) {
        logger.info("educationList was called. params:"+params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = educationService.selectEducationListCnt(params);
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

        List<HashMap<String, Object>> educationList = educationService.selectEducationList(params);

        model.addAttribute("educationList", educationList);

        return "education/educationList";
    }

    @RequestMapping(value = "educationModify")
    public String educationModify(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("educationModify was called. params:"+params);

        String method = params.get("method");

        List<HashMap<String, Object>> storeCategory = storeService.selectStoreCategory();
        model.addAttribute("storeCategory", storeCategory);

        if (storeCategory.size() > 0) {
            params.put("idx", storeCategory.get(0).get("IDX").toString());
            List<HashMap<String, Object>> childCate = storeService.selectStoreChildCategory(params);
            model.addAttribute("childCategory", childCate);

            params.put("idx", childCate.get(0).get("IDX").toString());
            List<HashMap<String, Object>> thirdChild = storeService.selectStoreChildCategory(params);
            model.addAttribute("thirdChild", thirdChild);
        }

        if (method.equals("Modify")) {
            EducationDto educationInfo = educationService.selectEducationDetail(params);
            model.addAttribute("educationInfo", educationInfo);

            params.put("goodsId", educationInfo.getGoods_id());
            Map<String, Object> goodsInfo = storeService.selectStoreGoodsDetail(params);
            model.addAttribute("goodsInfo", goodsInfo);
        }

        model.addAttribute("method", method);
        return "education/educationModify";
    }

    @RequestMapping(value = "/search_child_cate", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> searchChildCate(@RequestParam Map<String, String> params) {
        logger.info("searchChildCate was called. params: " + params);
        Map<String, Object> map = new HashMap<String, Object>();

        List<HashMap<String, Object>> childCate = storeService.selectStoreChildCategory(params);
        map.put("data", childCate);

        return map;
    }

    @RequestMapping(value = "/{method}_education", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyEducation(@PathVariable String method,
                                                          MultipartHttpServletRequest req,
                                                          MultipartHttpServletRequest multi,
                                                          @RequestParam Map<String, String> params, HttpSession session) throws Exception {
        logger.info("modifyEducation was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "Education");

        Map<String, Object> map = new HashMap<String, Object>();

        int result = 0;

        if (method.equals("save")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            result = educationService.insertEducation(params, imgFiles);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            result = educationService.updateEducation(params, imgFiles);
        }

        if (method.equals("delete")) {
            result = educationService.deleteEducation(params);
        }

        if (result > 0) {
            map.put("state", "success");
        } else {
            map.put("state", "fail");
        }

        return map;
    }

    @RequestMapping("educationDetail")
    public String detail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("detail was called. params:" + params);

        EducationDto educationInfo = educationService.selectEducationDetail(params);
        model.addAttribute("educationInfo", educationInfo);

        params.put("goodsId", educationInfo.getGoods_id());
        Map<String, Object> goodsInfo = storeService.selectStoreGoodsDetail(params);
        model.addAttribute("goodsInfo", goodsInfo);

        return "education/educationDetail";
    }

    @RequestMapping(value = "/search_goods")
    @ResponseBody
    public Map<String, Object> searchGoods(@RequestParam Map<String, String> params, HttpServletResponse resp) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("searchGoods was called. params:" + params);

        List<HashMap<String, Object>> goodsList = new ArrayList<HashMap<String, Object>>();

        try {
            goodsList = storeService.selectStoreGoodsList(params);
            resultMap.put("state", "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "ERROR");
        }

        resultMap.put("data", goodsList);
        return resultMap;
    }

    @PostMapping(value="/uploadSummernoteImageFile", produces = "application/json")
    @ResponseBody
    public JsonObject uploadSummernoteImageFile(@RequestParam Map<String, String> params, @RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        int uploadResult = 0;

        try {
            params.put("foreignType", "Summer");
            AttchFileInfoDto files = fileService.upload3(params, multipartFile);
            uploadResult = fileService.insertSummernoteFileInfo(files, params);

            String path = "";
            if (Define.IS_DEV) {
                path = LOCAL_WEB_PATH;
            } else {
                path = LIVE_WEB_PATH;
            }

            jsonObject.addProperty("url", path + files.getFileSavePath().replace("/content_nextplayer", ""));
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            //FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

    @RequestMapping(value = "educationFaqList")
    public String educationFaqList(@RequestParam Map<String, String> params, Model model) {
        logger.info("educationFaqList was called. params:"+params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = educationService.selectEducationFaqListCount(params);
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

        List<HashMap<String, Object>> educationFaqList = educationService.selectEducationFaqList(params);

        model.addAttribute("educationFaqList", educationFaqList);
        model.addAttribute("params", params);

        return "education/educationFaqList";
    }

    @RequestMapping(value = "/save_faq")
    public String saveFaq(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        logger.info("request ----> saveFaq params : "+ params);


        redirectAttributes.addAttribute("educationId", params.get("educationId"));
        redirectAttributes.addAttribute("cp", params.get("cp"));
        int result = 0;

        String sFlag = params.get("sFlag");

        try {
            if (sFlag.equals(Define.MODE_ADD)) {
                result = educationService.insertFaq(params);
            } else if (sFlag.equals(Define.MODE_FIX)) {
                result = educationService.updateFaq(params);
            } else if (sFlag.equals(Define.MODE_DELETE)) {
                result = educationService.deleteFaq(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/educationFaqList";
    }

    @RequestMapping(value = "educationQuestionList")
    public String educationQuestionList(@RequestParam Map<String, String> params, Model model) {
        logger.info("educationQuestionList was called. params:"+params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = educationService.selectEducationQuestionListCount(params);
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

        List<HashMap<String, Object>> educationQuestionList = educationService.selectEducationQuestionList(params);

        model.addAttribute("educationQuestionList", educationQuestionList);
        model.addAttribute("params", params);

        return "education/educationQuestionList";
    }

    @RequestMapping(value = "educationMemberList")
    public String educationMemberList(@RequestParam Map<String, String> params, Model model) {
        logger.info("educationMemberList was called. params:"+params);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = educationService.selectEducationMemberListCount(params);
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

        HashMap<String, Object> map = StrUtil.calcPage(cp, totalCount, 20);

        model.addAttribute("start", map.get("start"));
        model.addAttribute("end",  map.get("end"));
        model.addAttribute("prev",  map.get("prev"));
        model.addAttribute("next",  map.get("next"));

        model.addAttribute("cp", cp); //현재페이지번호
        model.addAttribute("cpp", cpp); //현재페이지 갯수
        model.addAttribute("tp", tp); //총 페이지 번호
        model.addAttribute("tc", totalCount); //총 리스트 갯수

        List<HashMap<String, Object>> educationMemberList = educationService.selectEducationMemberList(params);

        model.addAttribute("educationMemberList", educationMemberList);
        model.addAttribute("params", params);

        return "education/educationMemberList";
    }

    @RequestMapping("search_member")
    public @ResponseBody Map<String, Object> searchMember(@RequestParam Map<String, String> param) throws Exception {
        logger.info("request ----> searchMember params : "+ param);

        Map<String, Object> map = new HashMap<>();

        List<HashMap<String, Object>> memberList = new ArrayList<HashMap<String, Object>>();

        try {
            memberList = educationService.selectSearchEducationMember(param);
            map.put("state", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "fail");
        }
        map.put("data", memberList);
        return map;
    }
    @RequestMapping(value = "/save_member")
    public String saveMember(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        logger.info("request ----> saveFaq params : "+ params);


        redirectAttributes.addAttribute("educationId", params.get("educationId"));
        redirectAttributes.addAttribute("cp", params.get("cp"));
        int result = 0;

        String sFlag = params.get("sFlag");

        try {
            if (sFlag.equals(Define.MODE_ADD)) {
                result = educationService.insertMember(params);
            } else if (sFlag.equals(Define.MODE_DELETE)) {
                result = educationService.deleteMember(params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/educationMemberList";
    }

    @RequestMapping(value = "columnList")
    public String columnList(@RequestParam Map<String, String> params, Model model) {
        logger.info("columnList was called. params:"+params);

        String columnType = params.get("columnType");
        if (StrUtil.isEmpty(columnType)) {
            columnType = "All";
            params.put("columnType", columnType);
        }
        model.addAttribute("columnType", columnType);

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu("C0001");
        model.addAttribute("menuList", menuList);

        int cp = StrUtil.getCurrentPage(params);
        int cpp = Define.COUNT_PER_PAGE; //현재페이지 갯수

        int totalCount = educationService.selectColumnListCnt(params);
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

        List<HashMap<String, Object>> columnList = educationService.selectColumnList(params);

        model.addAttribute("columnList", columnList);

        return "education/column/list";
    }

    @RequestMapping(value = "columnModify")
    public String educationModifycolumnModify(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        logger.info("columnModify was called. params:"+params);

        String method = params.get("method");

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu("C0001");
        model.addAttribute("menuList", menuList);

        if (method.equals("Modify")) {
            Map<String, Object> columnInfo = educationService.selectColumnDetail(params);
            model.addAttribute("columnInfo", columnInfo);

        }

        model.addAttribute("method", method);
        return "education/column/modify";
    }

    @RequestMapping(value = "/{method}_column", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> modifyCloumn(@PathVariable String method, @RequestParam Map<String, String> params,
                                                          MultipartHttpServletRequest req, MultipartHttpServletRequest multi, HttpSession session) throws Exception {
        logger.info("modifyEducation was called. params: " + params);

        params.put("method", method);
        params.put("foreignType", "Column");

        Map<String, Object> map = new HashMap<String, Object>();

        int result = 0;

        if (method.equals("save")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            result = educationService.insertColumn(params, imgFiles);
        }

        if (method.equals("modify")) {
            List<AttchFileInfoDto> imgFiles = fileService.upload2(params, multi.getFiles("imgFile"), "imgFile");
            result = educationService.updateColumn(params, imgFiles);
        }

        if (method.equals("delete")) {
            result = educationService.deleteColumn(params);
        }

        if (result > 0) {
            map.put("state", "success");
        } else {
            map.put("state", "fail");
        }

        return map;
    }

    @RequestMapping("columnDetail")
    public String columnDetail(@RequestParam Map<String, String> params, Model model, HttpServletResponse resp){
        logger.info("columnDetail was called. params:" + params);

        Map<String, Object> columnInfo = educationService.selectColumnDetail(params);
        model.addAttribute("columnInfo", columnInfo);

        List<HashMap<String, Object>> menuList = uageService.selectCategoryMenu("C0001");
        model.addAttribute("menuList", menuList);

        return "education/column/detail";
    }

}
