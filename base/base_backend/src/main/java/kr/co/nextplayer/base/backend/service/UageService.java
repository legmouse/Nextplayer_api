package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.mapper.UageMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UageService {

    @Resource
    private UageMapper uageMapper;

    public List<HashMap<String, Object>> selectUageList() {

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        return uageList;
    }

    public List<HashMap<String, Object>> selectAreaList(Map<String, String> params) {

        List<HashMap<String, Object>> areaList = uageMapper.selectAreaList(params);

        return areaList;
    }

    public HashMap<String, Object> selectAreaListCount(Map<String, String> params) {
        return uageMapper.selectAreaListCount(params);
    }

    public HashMap<String, Object> selectAreaCount(Map<String, String> params) {
        return uageMapper.selectAreaCount(params);
    }

    @Transactional
    public int insertArea(Map<String, String> params) {
        return uageMapper.insertArea(params);
    }

    @Transactional
    public int updateArea(Map<String, String> params) {
        return uageMapper.updateArea(params);
    }

//    public List<HashMap<String, Object>> selectAreaByUageList(UageDto uageDto) {
//
//        List<HashMap<String, Object>> areaList = uageMapper.selectAreaByUageList(uageDto);
//
//        return areaList;
//    }

    public List<HashMap<String, Object>> selectUseUageList() {

        List<HashMap<String, Object>> uageList = uageMapper.selectUseUageList();

        return uageList;
    }

    public List<HashMap<String, Object>> selectCategoryMenu(String params) {

        List<HashMap<String, Object>> menuList = uageMapper.selectCategoryMenu(params);

        return menuList;
    }

    public List<HashMap<String, Object >> selectCreatorList(String mediaType) {
        return uageMapper.selectCreatorList(mediaType);
    }

    @Transactional
    public int updateInterestAge(Map<String, Object> param) {
        uageMapper.updateInterestAge(String.valueOf(param.get("uageId")));
        List<Map<String, Object>> hideArr = (List<Map<String, Object>>) param.get("hideArr");

        if (hideArr != null && hideArr.size() > 0) {
            uageMapper.updateHideAgeReset();
            for (Map<String, Object> hideMap : hideArr) {
                uageMapper.updateHideAge(hideMap);
            }
        }

        return 0;
    }

}
