package kr.co.nextplayer.base.front.response.player;

import kr.co.nextplayer.base.player.dto.PlayerGoldenAgeRosterDto;
import kr.co.nextplayer.base.player.dto.PlayerNationalRosterDto;
import kr.co.nextplayer.base.player.dto.RosterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJoinRosterResponse {

    List<RosterDto> rosterList;
    List<Object> joinRosterByYearList;

}
