package kr.co.nextplayer.base.front.response.player;

import kr.co.nextplayer.base.media.dto.MediaDto;
import kr.co.nextplayer.base.player.dto.RosterDto;
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
public class RosterResponse {

    int totalCount;
    List<RosterDto> rosterList;
    List<HashMap<String, Object>> yearList;

}
