package kr.co.nextplayer.base.backend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class League {

    private String lTeamId;

    private String leagueId;
    private String leagueName;
    private String lRank;
    private String lPoints;
    private String lPlayed;
    private String lWon;
    private String lDraw;
    private String lLost;
    private String lGf;
    private String lGa;
    private String lGd;

    private String lRegDate;
    private String lModDate;

}
