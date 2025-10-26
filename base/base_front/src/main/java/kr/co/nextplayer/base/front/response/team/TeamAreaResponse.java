package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.team.dto.AreaTeamDto;

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
public class TeamAreaResponse {

    List<HashMap<String, Uage>> areaList;
    List<HashMap<String, Object>> uageList;

    List<AreaTeamDto> areaTeamList;

}
