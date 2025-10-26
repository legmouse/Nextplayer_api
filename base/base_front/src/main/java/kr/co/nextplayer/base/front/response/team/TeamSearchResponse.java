package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.team.dto.AreaTeamDto;
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
public class TeamSearchResponse {

    int totalCount;
    List<TeamSearchDto> searchList;

}
