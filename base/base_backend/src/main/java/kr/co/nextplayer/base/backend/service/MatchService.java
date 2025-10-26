package kr.co.nextplayer.base.backend.service;

import kr.co.nextplayer.base.backend.mapper.MatchMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchService {

    @Resource
    MatchMapper matchMapper;

    public HashMap<String, Object> selectTeamMatchResult(Map<String, String> params) {
        return matchMapper.selectTeamMatchResult(params);
    }

    public HashMap<String, Object> selectTeamCupAvgGoal(Map<String, String> params) {
        return matchMapper.selectTeamCupAvgGoal(params);
    }

    public List<HashMap<String, Object>> selectTeamTotalMatch(Map<String, String> params) {
        return matchMapper.selectTeamTotalMatch(params);
    }

}
