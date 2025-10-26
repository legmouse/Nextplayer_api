package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PopupMapper {

    int selectPopupListCnt(Map<String, String> param);

    List<HashMap<String, Object>> selectPopupList(Map<String, String> param);

    int insertPopup(Map<String, String> param);

    int updatePopup(Map<String, String> param);

    int deletePopup(Map<String, String> param);

    Map<String, Object> selectPopupDetail(Map<String, String> param);

}
