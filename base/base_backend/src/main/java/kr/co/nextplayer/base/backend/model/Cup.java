package kr.co.nextplayer.base.backend.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cup {

    private String cupName;
    private String playSDate;
    private String playEDate;
    private String cupType;

    private String cTeamId;
    private String cupId;
    private String cResult;
    private String subRank;
    private String mainRank;

    private String cPlayed;
    private String cWon;
    private String cDraw;
    private String cLost;
    private String cGf;
    private String cGa;
    private String cGd;

    private String cRegDate;
    private String cModDate1;
    private String cModeDate2;
    private String cModeDate3;

    private List<CupData> cupData;

}
