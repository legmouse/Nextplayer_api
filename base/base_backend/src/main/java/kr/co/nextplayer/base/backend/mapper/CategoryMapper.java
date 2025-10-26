package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CategoryMapper {

    List<HashMap<String, Object>> selectCategoryGroup(Map<String, String> params);

    List<HashMap<String, Object>> selectCategoryDetailList(Map<String, String> params);

    String selectCategoryCdMax(Map<String, String> params);

    String selectCategoryValMax(Map<String, String> params);

    int insertCategory(Map<String, String> params);

    int updateCategory(Map<String, String> params);

    int deleteCategory(Map<String, String> params);

}
