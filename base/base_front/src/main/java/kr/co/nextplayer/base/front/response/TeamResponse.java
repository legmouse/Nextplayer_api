package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.model.League;
import kr.co.nextplayer.base.team.model.Match;
import kr.co.nextplayer.base.team.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {

    String sYear;
    String ageGroup;

    List<HashMap<String, Object>> uageList;
    List<HashMap<String, Uage>> areaList;

    Team teamInfoMap;
    Team teamResultMap;

    List<Team> areaTeamList;
    List<Team> selTeamResultByYearGraph;
    List<Team> teamAvgGoalList;
    List<Match> teamTotalMatchList;

    List<Team> teamCupPlayedList;
    List<Team> teamCupAvgGoalList;

    List<Cup> teamCupResultList;
    List<Cup> teamCupMatchList;

    List<League> teamLeagueResultByYearList;
    List<League> teamLeagueEnterList;
    List<League> teamLeagueRankList;
    List<League> leagueMatchCalendar;
    List<League> leagueMatchSchedule;

    Team homeTeamMatchResultMap;
    List<Match> homeTeamTotalMatchList;
    List<Cup> homeTeamCupResultList;
    List<Cup> homeTeamCupMatchList;
    List<League> homeTeamLeagueEntertList;
    List<League> homeTeamLeagueMatchList;

    Team awayTeamMatchResultMap;
    List<Match> awayTeamTotalMatchList;
    List<Cup> awayTeamCupResultList;
    List<Cup> awayTeamCupMatchList;
    List<League> awayTeamLeagueEntertList;
    List<League> awayTeamLeagueMatchList;

    List<Team> opponentMatchList;



}
