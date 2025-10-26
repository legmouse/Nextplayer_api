package kr.co.nextplayer.base.batch.mapper;

import kr.co.nextplayer.base.batch.dto.joinkfaCraw.MatchDataDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface CupMapper {
    List<MatchDataDto> selectCupTourMatchListCraw(Map<String, String> map);
    List<MatchDataDto> selectCupMainMatchListCraw(Map<String, String> map);
    List<MatchDataDto> selectCupSubMatchListCraw(Map<String, String> map);

    int updateCupSubMatchEndFlag(Map<String, String> params);

    int updateCupMainMatchEndFlag(Map<String, String> params);

    int updateCupTourMatchEndFlag(Map<String, String> params);

    int updateCupMainMatchScore(Map<String, Object> params);
    int updateCupSubMatchScore(Map<String, Object> params);
    int updateCupTourMatchScore(Map<String, Object> params);

    HashMap<String, Object> selectCupNextTourMatch(Map<String, String> params);
    int updateCupTourNextMatch(Map<String, String> params);
    int updateCupTourMatchFixFlag(Map<String, String> params);

    void updateMainMatchInfo(Map<String, Object> params);

    void updateSubMatchInfo(Map<String, Object> params);

    void updateTourMatchInfo(Map<String, Object> params);

    void insertMatchPlayData(Map<String, Object> params);
    void insertMatchChangeData(Map<String, Object> params);
    void deleteMatchPlayDataOrChangeData(Map<String, String> params);
    void deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(Map<String, String> params);

    void insertStaffData(Map<String, Object> params);
    void insertStaffPenaltyData(Map<String, Object> params);
    void insertOwnGoalData(Map<String, Object> params);
}
