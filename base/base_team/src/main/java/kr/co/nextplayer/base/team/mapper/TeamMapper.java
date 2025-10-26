package kr.co.nextplayer.base.team.mapper;

import kr.co.nextplayer.base.team.dto.*;
import kr.co.nextplayer.base.team.model.*;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TeamMapper {

    /**
     * 광역별 클럽 리스트
     * @param teamDto
     * @return
     */
    List<AreaTeamDto> selectAreaTeamList(TeamDto teamDto);

    /**
     * 팀정보 상세
     * @param teamId
     * @return
     */
    /*@Result(column = "team_id", property = "teamId")
    @Result(column = "uage", property = "uage")
    @Result(column = "team_name", property = "teamName")
    @Result(column = "team_type", property = "teamType")
    @Result(column = "nick_name", property = "nickName")
    @Result(column = "area_name", property = "areaName")
    @Result(column = "emblem", property = "emblem")
    @Result(column = "addr", property = "addr")
    @Result(column = "pId", property = "pId")
    @Result(column = "pId_name", property = "pIdName")*/
    @Select("SELECT " +
        "team_id, uage, area_name, team_type, team_name, nick_name, emblem, addr, pId, pId_name, team_group_id " +
        "FROM Team " +
        "WHERE team_id = #{teamId}")
    CommonTeamDto selectTeamInfo(String teamId);

    /**
     * 학원클럽 - 요약정보 - 해당연도 승무패, 득/실, 평균 득/실점
     * @param teamDto
     * @return
     */
    TeamResultDto selectTeamResultByYear(TeamDto teamDto);

    /**
     *
     * @param teamDto
     * @return
     */
    List<TeamResultDto> selectTeamResultByYearGraph(TeamDto teamDto);

    /**
     * 학원클럽 - 요약정보 - 연도별 평균 득/실점
     * @param teamDto
     * @return
     */
    List<TeamResultDto> selectTeamAvgGoal(TeamDto teamDto);

    /**
     * 학원클럽 - 요약정보 -  리그+대회 경기결과 리스트
     * @param teamDto
     * @return
     */
    List<TeamMatchDto> selectTeamTotalMatch(TeamDto teamDto);

    /**
     * 학원클럽 - 요약정보 - 해당연도 승무패, 득/실, 평균 득/실점
     * @param teamDto
     * @return
     */
    TeamCupLeagueInfoDto selectTeamResultByYear2(TeamDto teamDto);

    /**
     * 학원클럽 - 대회정보 - 대회 경기 수 추이
     * @param teamDto
     * @return
     */
    List<CommonTeamDto> selectTeamCupPlayedByYear(TeamDto teamDto);

    /**
     * 학원클럽 - 대회정보 - 대회 평균 득/실점 추이
     * @param teamDto
     * @return
     */
    List<TeamResultDto> selectTeamCupAvgGoalByYear(TeamDto teamDto);

    /**
     * 학원클럽 - 리그정보 - 연도별 리그 승무패
     * @param teamDto
     * @return
     */
    List<Team> selectTeamLeagueResultByYear(TeamDto teamDto);

    /**
     * 학원클럽 - 요약정보 - 전체, 대회, 리그 승무패
     * @param teamDto
     * @return
     */
    TeamTotalResultDto selectTeamMatchResult(TeamDto teamDto);

    /**
     * 학원클럽 - 요약정보 - 해당연도 승무패, 득/실, 평균 득/실점
     * @param teamDto
     * @return
     */
    List<TeamResultDto> selectTeamResultByYearRenewal(TeamDto teamDto);

    List<TeamOpponentDto> selectVwOpponentResultRenewal(TeamDto teamDto);

    List<EnterTeam> selectEnterTeamInfo(TeamDto teamDto);

    List<RosterTeam> selectRosterTeamInfo(TeamDto teamDto);

    List<HashMap<String, Object>> selectTeamListForMedia(TeamDto teamDto);

    int selectSearchTeamCnt(TeamDto teamDto);

    List<TeamSearchDto> selectSearchTeam(TeamDto teamDto);

    List<IndirectMatchesSqlResultSet> selectIndirectMatches(Map map);

    List<HashMap<String, Object>> selectTeamGroup(TeamDto teamDto);

    HashMap<String, Object> selectInterestTeamData(TeamDto teamDto);

}
