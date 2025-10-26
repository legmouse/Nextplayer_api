package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.league.dto.LeagueYearRankDto;
import kr.co.nextplayer.base.team.dto.TeamCupResultDto;
import kr.co.nextplayer.base.team.dto.TeamResultDto;
import kr.co.nextplayer.base.team.dto.TeamSearchDto;
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
public class TeamInterestInfoResponse {

    TeamResultDto teamResultData;
    HashMap<String, Object> interestTeamInfo;

}
