package kr.co.nextplayer.base.front.response.league;

import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.LeagueListDto;
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
public class LeagueListResponse {

    List<HashMap<String, Object>> uageList;
    List<HashMap<String, Uage>> areaList;

    List<LeagueListDto> totalleagueList;

}
