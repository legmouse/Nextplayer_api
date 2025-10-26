package kr.co.nextplayer.base.front.mapper;

import com.linecorp.armeria.internal.shaded.fastutil.Hash;
import kr.co.nextplayer.base.front.model.Uage;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;


public interface UageMapper {

    @Results(id = "uageMap", value = {
        @Result(column = "hide_age", property = "hide_age", typeHandler = kr.co.nextplayer.lib.util.mybatis.JsonTypeHandler.class, javaType = java.util.ArrayList.class)
    })
    @Select("SELECT * FROM Uage WHERE use_flag = 0 ORDER BY uage DESC")
    List<HashMap<String, Object>> selectUageList();

    @Result(column = "area_id", property = "areaId")
    @Result(column = "area_name", property = "areaName")
    @Result(column = "uage", property = "uage")
    @Select("SELECT " +
                "area_id, uage, area_name " +
            "FROM " +
                "Area " +
            "WHERE " +
                "use_flag = 0 " +
                "AND uage = #{ageGroup} " +
                "AND show_flag = 0 " +
            "ORDER BY order_number")
    List<HashMap<String, Uage>> selectAreaList(String ageGroup);

    /*@Result(column = "area_id", property = "areaId")
    @Result(column = "area_name", property = "areaName")
    @Result(column = "uage", property = "uage")*/
    @Select("SELECT area_id, uage, area_name FROM Area WHERE use_flag = 0 AND show_flag = 0  AND uage = #{ageGroup}")
    List<HashMap<String, Uage>> selectAreaByUageList(String ageGroup);

    @Select("SELECT" +
                " @N:= @N +1 AS year" +
                " FROM\n" +
                " Area, (SELECT @N:= YEAR(CURDATE()) - 10 FROM DUAL) NN" +
                " WHERE" +
                " @N < DATE_FORMAT(NOW(), '%Y')")
    List<HashMap<String, Object>> selectYearList();

    @Select("SELECT" +
                " category_name," +
                " code_value " +
            "FROM" +
                " Category " +
            "WHERE" +
                " use_flag = 0" +
                " AND parent_cd = #{categoryType} " +
                " AND show_flag = 0 " +
            "ORDER BY category_order ASC")
    List<HashMap<String, Object>> selectCategoryMenu(String categoryType);

    @Select("SELECT uage  FROM Uage WHERE use_flag = 0 AND interest_flag = 1")
    HashMap<String, Object> selectInterestAge();
}
