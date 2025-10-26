package kr.co.nextplayer.base.backend.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    2depth
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {

    private String matchMonth;
    private String matchDate;
    private int matchOrder;
    private String matchTime;
    private String matchDateFormat;
    private String matchPlace;
    private String homeTeam;
    private String homeTeamEmblem;
    private String awayTeam;
    private String awayTeamEmblem;
    private String score;
    private String scoreType;
    private int homeScore;
    private int awayScore;
    private int homePenaltyScore;
    private int awayPenaltyScore;

}
