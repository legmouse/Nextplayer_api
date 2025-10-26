package kr.co.nextplayer.base.backend.storeMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StoreMapper {

    List<HashMap<String, Object>> selectStoreGoodsList(Map<String, String> param);

    Map<String, Object> selectStoreGoodsDetail(Map<String, String> param);

    List<HashMap<String, Object>> selectStoreCategory();

    List<HashMap<String, Object>> selectStoreChildCategory(Map<String, String> param);

}
