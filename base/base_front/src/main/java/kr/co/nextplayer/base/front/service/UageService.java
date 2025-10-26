package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.front.mapper.UageMapper;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.front.response.UageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UageService {

    @Resource
    private UageMapper uageMapper;

    public List<HashMap<String, Object>> selectUageList() {

        List<HashMap<String, Object>> uageList = uageMapper.selectUageList();

        return uageList;
    }

    public List<HashMap<String, Uage>> selectAreaList(String ageGroup) {

        List<HashMap<String, Uage>> areaList = uageMapper.selectAreaList(ageGroup);

        return areaList;
    }

    public List<HashMap<String, Uage>> selectAreaByUageList(String ageGroup) {

        List<HashMap<String, Uage>> areaList = uageMapper.selectAreaByUageList(ageGroup);

        return areaList;
    }

}
