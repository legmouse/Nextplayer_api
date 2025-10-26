package kr.co.nextplayer.base.batch.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDataDto {

    private String matchId;
    private String matchOrder;
    private int round;
    private int tourNo;
    private int nextTourNo;
    private int nextTourPort;
    private int homePk;
    private int awayPk;
    private int homeBfScore;
    private int awayBfScore;
    private int endFlag;
    private String home;
    private String away;
    private int homeId;
    private int awayId;
    private int upd_flag;
    private MatchStatus matchStatus;
    
    public enum MatchStatus {
        READY
        , LINEUP
        , START
        , END
    }
}
