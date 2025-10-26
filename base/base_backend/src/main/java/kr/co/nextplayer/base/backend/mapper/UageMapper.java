package kr.co.nextplayer.base.backend.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface UageMapper {

    @Select("SELECT * FROM Uage ORDER BY uage DESC")
    List<HashMap<String, Object>> selectUageList();

    List<HashMap<String, Object>> selectAreaList(Map<String, String> params);

    HashMap<String, Object> selectAreaListCount(Map<String, String> params);

    HashMap<String, Object> selectAreaCount(Map<String, String> params);

    //HashMap<String, Object> checkUserWithSessionKey(Map<String, String> params);

    int insertArea(Map<String, String> params);

    int updateArea(Map<String, String> params);

    @Results(id = "uageMap", value = {
        @Result(column = "hide_age", property = "hide_age", typeHandler = kr.co.nextplayer.lib.util.mybatis.JsonTypeHandler.class, javaType = java.util.ArrayList.class)
    })
    @Select("SELECT * FROM Uage WHERE use_flag = 0 ORDER BY uage DESC")
    List<HashMap<String, Object>> selectUseUageList();

    List<HashMap<String, Object>> selectCategoryMenu(String params);

    List<HashMap<String, Object>> selectCreatorList(String params);

    int updateInterestAge(String param);

    void updateHideAgeReset();
    void updateHideAge(Map<String, Object> hideMap);
}
