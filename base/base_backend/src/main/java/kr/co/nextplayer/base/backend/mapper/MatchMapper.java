package kr.co.nextplayer.base.backend.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MatchMapper {

    HashMap<String, Object> selectTeamMatchResult(Map<String, String> params);

    HashMap<String, Object> selectTeamCupAvgGoal(Map<String, String> params);

    List<HashMap<String, Object>> selectTeamTotalMatch(Map<String, String> params);

}
