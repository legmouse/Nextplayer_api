package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CreatorMapper {

    int selectCreatorListCnt(Map<String, String> params);

    List<HashMap<String, Object>> selectCreatorList(Map<String, String> params);

    HashMap<String, Object> selectCreatorInfo(Map<String, String> params);

    int insertCreator(Map<String, String> params);

    int updateCreator(Map<String, String> params);

    int updateCreatorDelete(Map<String, String> params);

    HashMap<String, Object> selectImgFileInfo(Map<String, String> params);

}
