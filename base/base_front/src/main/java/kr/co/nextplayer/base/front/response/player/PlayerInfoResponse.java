package kr.co.nextplayer.base.front.response.player;

import kr.co.nextplayer.base.player.dto.PlayerDetailDto;
import kr.co.nextplayer.base.player.dto.PlayerDto;
import kr.co.nextplayer.base.player.dto.PlayerHistoryDto;
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
public class PlayerInfoResponse {

    PlayerDetailDto playerInfo;
    List<PlayerHistoryDto> playerHistoryList;
}
