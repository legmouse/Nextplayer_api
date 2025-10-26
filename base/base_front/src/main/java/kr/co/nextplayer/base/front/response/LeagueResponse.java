package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.model.League;
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
public class LeagueResponse {

    String sYear;
    String ageGroup;

    List<HashMap<String, Uage>> uageList;
    List<HashMap<String, Uage>> areaList;

    League leagueInfoMap;
    List<League> leagueTeamList;
    List<League> leagueRankList;
    List<League> leagueMatchCalendar;
    List<League> leagueMatchSchedule;
    List<League> totalleagueList;

}
