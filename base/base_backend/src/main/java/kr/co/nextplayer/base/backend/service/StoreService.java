package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.storeMapper.StoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreService {

    @Resource
    private StoreMapper storeMapper;

    public List<HashMap<String, Object>> selectStoreGoodsList(Map<String, String> param) {
        return storeMapper.selectStoreGoodsList(param);
    }

    public Map<String, Object> selectStoreGoodsDetail(Map<String, String> param) {
        return storeMapper.selectStoreGoodsDetail(param);
    }

    public List<HashMap<String, Object>> selectStoreCategory() {
        return storeMapper.selectStoreCategory();
    }

    public List<HashMap<String, Object>> selectStoreChildCategory(Map<String, String> param) {
        return storeMapper.selectStoreChildCategory(param);
    }

}
