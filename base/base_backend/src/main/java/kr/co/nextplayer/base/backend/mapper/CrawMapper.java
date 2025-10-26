package kr.co.nextplayer.base.backend.mapper;

import kr.co.nextplayer.base.backend.dto.joinkfaCraw.GameDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CrawMapper {

    List<GameDto> selectUniversityLeagueTitle();
    List<GameDto> selectHighSchoolLeagueTitle();
    List<GameDto> selectMiddleSchoolLeagueTitle();
    List<GameDto> selectElementarySchoolLeagueTitle();
    List<GameDto> selectUniversityCupTitle();
    List<GameDto> selectHighSchoolCupTitle();
    List<GameDto> selectMiddleSchoolCupTitle();
    List<GameDto> selectElementarySchoolCupTitle();

    void insertMatchPlayData(Map<String, Object> params);
    void insertMatchChangeData(Map<String, Object> params);
    void deleteMatchPlayDataOrChangeData(Map<String, String> params);
    void deleteMatchStaffDataOrStaffPenaltyDataOrOwnGoalData(Map<String, String> params);

    void insertStaffData(Map<String, Object> params);
    void insertStaffPenaltyData(Map<String, Object> params);
    void insertOwnGoalData(Map<String, Object> params);

}
