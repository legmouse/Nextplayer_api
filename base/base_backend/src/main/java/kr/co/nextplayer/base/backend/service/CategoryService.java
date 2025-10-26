package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.dto.AttchFileInfoDto;
import kr.co.nextplayer.base.backend.mapper.BoardMapper;
import kr.co.nextplayer.base.backend.mapper.CategoryMapper;
import kr.co.nextplayer.base.backend.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public List<HashMap<String, Object>> selectCategoryGroup(Map<String, String> params) {
        return categoryMapper.selectCategoryGroup(params);
    }

    public List<HashMap<String, Object>> selectCategoryDetailList(Map<String, String> params) {
        return categoryMapper.selectCategoryDetailList(params);
    }

    public String selectCategoryCdMax(Map<String, String> params) {
        return categoryMapper.selectCategoryCdMax(params);
    }

    public String selectCategoryValMax(Map<String, String> params) {
        return categoryMapper.selectCategoryValMax(params);
    }

    @Transactional
    public int insertCategory(Map<String, String> params) {
        return categoryMapper.insertCategory(params);
    }

    @Transactional
    public int updateCategory(Map<String, String> params) {
        return categoryMapper.updateCategory(params);
    }
    @Transactional
    public int deleteCategory(Map<String, String> params) {
        return categoryMapper.deleteCategory(params);
    }

    public int saveCategory(List<Map<String, String>> params) {

        int result = 0;

        for(Map<String, String> map : params) {
            if (map.get("status").equals("C")) {
                result = insertCategory(map);
                if (result == 0) {
                    return result;
                }
            } else if (map.get("status").equals("M")) {
                result = updateCategory(map);
                if (result == 0) {
                    return result;
                }
            }
            else if (map.get("status").equals("D")) {
                result = deleteCategory(map);
                if (result == 0) {
                    return result;
                }
            } else if (map.get("status").equals("") || map.get("status") == null) {
                result = updateCategory(map);
                if (result == 0) {
                    return result;
                }
            }
        }

        return result;
    }

}
