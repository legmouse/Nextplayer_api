package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.front.model.Uage;
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
public class TeamCupInfoResponse {

    List<HashMap<String, Uage>> areaList;
    List<HashMap<String, Uage>> uageList;

    CommonTeamDto teamInfoMap;

    TeamCupLeagueInfoDto teamResultMap;
    List<CommonTeamDto> teamCupPlayedList;
    List<TeamResultDto> teamCupAvgGoalList;
    List<TeamCupResultDto> teamCupResultList;
    List<CupSubMatchDto> teamCupMatchList;
    List<TeamCupResultDto> lastThreeYearList;

}
