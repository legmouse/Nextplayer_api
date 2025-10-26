package kr.co.nextplayer.base.front.mapper;

import kr.co.nextplayer.base.front.model.Uage;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ConfigMapper {

    @Select("SELECT main_menu_id, menu_name, menu_key, menu_order FROM main_menu WHERE use_flag = 0 ORDER BY menu_order")
    List<HashMap<String, Object>> selectMainMenuList();


}
