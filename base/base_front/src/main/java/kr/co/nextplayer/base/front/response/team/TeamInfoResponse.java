package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.team.dto.AreaTeamDto;
import kr.co.nextplayer.base.team.dto.CommonTeamDto;
import kr.co.nextplayer.base.team.dto.TeamMatchDto;
import kr.co.nextplayer.base.team.dto.TeamResultDto;
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
public class TeamInfoResponse {

    List<HashMap<String, Uage>> areaList;
    List<HashMap<String, Uage>> uageList;

    CommonTeamDto teamInfoMap;

    TeamResultDto teamResultMap;
    List<TeamResultDto> teamResultMapGraph;
    List<TeamResultDto> teamAvgGoalList;
    List<TeamMatchDto> teamTotalMatchList;

    List<HashMap<String, Object>> teamGroup;

}
