package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ConfigMapper {

    List<HashMap<String, Object>> selectManagerList();

    int insertManager(Map<String, String> params);

    int updateUserInfo(Map<String, String> params);

    int updateManagerPw(Map<String, String> params);

    int deleteManager(Map<String, String> params);

    List<HashMap<String, Object>> selectMenuInfoList();

    int insertRoleInfo(Map<String, String> params);

    int insertTrgetRoleRelate(Map<String, String> params);

    int insertAuthorRoleRelate(Map<String, String> params);

    int insertMenuAuthorRelate(List<HashMap<String, String>> params);

    int deleteMenuAuthorRelateData(Map<String, String> params);

    List<HashMap<String, Object>> selectUserMenuList(Map<String, String> param);

    List<HashMap<String, Object>> selectUserUpperMenuList(Map<String, String> param);

    HashMap<String, Object> selectUserInfo(Map<String, String> param);

    List<HashMap<String, Object>> selectUpperMenuList();

    List<HashMap<String, Object>> selectMainMenuList();

    int updateMainMenu(Map<String, String> param);
}
