package kr.co.nextplayer.base.backend.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayDataDto {
    private String teamName;
    private String playerNumber;
    private String position;
    private String playerName;
    private boolean captain;
    private String compete;
    private String goal;
    private String help;
    private String warning;
    private String exit;
    private String pso;
    private long playerId;
    private long leagueMatchId;
    private String selCanGbn;
    private String homeAwayGbn;
    private String captainFlag;
}
