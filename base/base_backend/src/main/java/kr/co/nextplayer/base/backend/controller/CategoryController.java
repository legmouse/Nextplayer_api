package kr.co.nextplayer.base.backend.controller;

import kr.co.nextplayer.base.backend.service.CategoryService;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class CategoryController {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Resource
    private CategoryService categoryService;

    @RequestMapping(value = "/category_list")
    public String list(@RequestParam Map<String, String> params, Model model) {


        List<HashMap<String, Object>> groups = categoryService.selectCategoryGroup(params);

        model.addAttribute("groups", groups);

        return "category/list";
    }

    @RequestMapping(value = "/detail_{category}_list")
    public @ResponseBody Map<String, Object> goCategoryDetailList(@RequestParam Map<String, String> params, @PathVariable String category) {
        Map<String, Object> result = new HashMap<>();

        if(StrUtil.isEmpty(category)) {
            category = "media";
        }
        category.toUpperCase();
        params.put("category", category);

        List<HashMap<String, Object>> details = categoryService.selectCategoryDetailList(params);

        String cdMax = categoryService.selectCategoryCdMax(params);

        String valMax = categoryService.selectCategoryValMax(params);

        result.put("detail", details);
        result.put("codeMax", cdMax);
        result.put("valMax", valMax);

        return result;
    }

    @RequestMapping(value = "/saveCategory.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveCategory(@RequestBody List<Map<String, String>> params) throws Exception {
        logger.info("request ----> saveCategory params : "+ params);
        Map<String, Object> map = new HashMap<>();

        int result = categoryService.saveCategory(params);

        map.put("result", result == 1 ? "success" : "fail");

        return map;
    }

}
