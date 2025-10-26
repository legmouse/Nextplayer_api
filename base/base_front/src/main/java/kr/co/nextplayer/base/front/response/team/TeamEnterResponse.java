package kr.co.nextplayer.base.front.response.team;

import kr.co.nextplayer.base.team.dto.TeamEnterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamEnterResponse {

    List<TeamEnterDto> teamEnterList;

}
