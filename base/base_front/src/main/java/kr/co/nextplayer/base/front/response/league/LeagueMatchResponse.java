package kr.co.nextplayer.base.front.response.league;

import com.linecorp.armeria.internal.shaded.fastutil.Hash;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.*;
import kr.co.nextplayer.base.team.dto.CommonTeamDto;
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
public class LeagueMatchResponse {

    String sYear;
    String ageGroup;

    List<HashMap<String, Uage>> uageList;
    List<HashMap<String, Uage>> areaList;

    LeagueInfoDto leagueInfoMap;
    List<CommonTeamDto> leagueTeamList;
    List<LeagueTeamDto> leagueRankList;
    List<LeagueMatchCalendarDto> leagueMatchCalendar;
    List<LeagueMatchDto> leagueMatchSchedule;

    HashMap<String, Object> summaryData;

}
