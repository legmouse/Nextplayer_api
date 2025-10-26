package kr.co.nextplayer.base.backend.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MediaMapper {

    int selectMediaListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectMediaList(Map<String, String> params);

    HashMap<String, Object> selectMediaInfo(Map<String, String> params);

    List<HashMap<String, Object>> selectMediaChildList(Map<String, String> params);

    int insertMedia(Map<String, String> params);

    int insertMediaCross(List<HashMap<String, Object>> params);

    int updateMedia(Map<String, String> params);

    int updateMediaDelete(Map<String, String> params);

    int deleteMediaCross(Map<String, String> params);

    int insertMainMediaData(Map<String, String> params);

    int deleteMainMediaMainFlag(Map<String, String> param);

    int updateMediaMainFlag(Map<String, String> params);

    int deleteMainMediaData(Map<String, String> params);

    int updateMainMediaOrderPlusAll(Map<String, String> params);

    int deleteMainMediaOne(Map<String, String> params);

    int updateMainMediaOrderMinusAll(Map<String, String> params);

    List<Map<String, Object>> selectMainMediaList(Map<String, String> param);

    List<HashMap<String, Object>> selectCreatorList(Map<String, String> param);
}
