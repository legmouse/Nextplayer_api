package kr.co.nextplayer.base.front.response.league;

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
public class LeagueSearchMatchResponse {

    String ageGroup;

    List<LeagueSearchMatchDto> leagueSearchList;
    List<LeagueInfoDto> leagueInfoList;

}
