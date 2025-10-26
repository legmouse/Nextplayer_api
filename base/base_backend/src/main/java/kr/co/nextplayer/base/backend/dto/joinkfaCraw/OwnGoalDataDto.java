package kr.co.nextplayer.base.backend.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnGoalDataDto {

    private String teamName;

    private String time;
    private String playerNumber;
    private String playerName;

    private int playerId;

    private String homeAwayGbn;
    private int matchId;

}
