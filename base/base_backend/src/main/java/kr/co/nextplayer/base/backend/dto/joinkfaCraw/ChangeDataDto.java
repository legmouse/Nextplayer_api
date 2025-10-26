package kr.co.nextplayer.base.backend.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDataDto {

    private String teamName;

    private String time;
    private String inPlayerNumber;
    private String inPlayerName;
    private int inPlayerId;
    private String outPlayerNumber;
    private String outPlayerName;
    private int outPlayerId;
    private String homeAwayGbn;
    private int matchId;
}
