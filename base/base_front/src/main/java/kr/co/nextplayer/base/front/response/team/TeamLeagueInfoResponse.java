package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.LeagueMatchCalendarDto;
import kr.co.nextplayer.base.league.dto.LeagueMatchDto;
import kr.co.nextplayer.base.league.dto.LeagueResultDto;
import kr.co.nextplayer.base.league.dto.LeagueYearRankDto;
import kr.co.nextplayer.base.team.dto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamLeagueInfoResponse {

    String sYear;
    String ageGroup;

    CommonTeamDto teamInfoMap;

    TeamCupLeagueInfoDto teamResultMap;

    List<LeagueResultDto> teamLeagueResultByYearList;
    List<TeamLeagueEnterDto> teamLeagueEnterList;
    List<LeagueYearRankDto> teamLeagueRankList;
    List<LeagueMatchCalendarDto> leagueMatchCalendar;
    List<LeagueMatchDto> leagueMatchSchedule;

}
