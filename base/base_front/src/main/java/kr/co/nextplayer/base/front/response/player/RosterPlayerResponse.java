package kr.co.nextplayer.base.front.response.player;

import kr.co.nextplayer.base.player.dto.RosterDto;
import kr.co.nextplayer.base.player.dto.RosterPlayerDto;
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
public class RosterPlayerResponse {

    int total_count;
    List<RosterPlayerDto> rosterPlayerList;
    List<HashMap<String, Object>> yearList;

}
