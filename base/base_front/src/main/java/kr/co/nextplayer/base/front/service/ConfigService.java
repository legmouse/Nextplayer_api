package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.front.mapper.ConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ConfigService {

    @Resource
    private ConfigMapper configMapper;

    public List<HashMap<String, Object>> selectMainMenuList() {

        List<HashMap<String, Object>> mainMenuList = configMapper.selectMainMenuList();

        return mainMenuList;
    }

}
