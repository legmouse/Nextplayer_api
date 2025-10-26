package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class IndirectMatchesSqlResultSet implements Serializable {

    private static final long serialVersionUID = 1L;

    private long hmtGameId;
    private String hmtGameType;
    private String hmtPlace;
    private LocalDateTime hmtMatchCalcDate;
    private String hmtMatchDate;
    private long hmtTeamId;
    private long hmtAnotherTeamId;
    private String hmtTeam;
    private String hmtAnotherTeam;
    private String hmtEmblem;
    private String hmtAnotherEmblem;
    private int hmtScore;
    private int hmtAnotherScore;
    private int hmtPk;
    private int hmtAnotherPk;
    private String hmtMatchName;
    private int hmtWon;
    private int hmtEq;

    private long antGameId;
    private String antGameType;
    private String antPlace;
    private LocalDateTime antMatchCalcDate;
    private String antMatchDate;
    private long antTeamId;
    private long antAnotherTeamId;
    private String antTeam;
    private String antAnotherTeam;
    private String antEmblem;
    private String antAnotherEmblem;
    private int antScore;
    private int antAnotherScore;
    private int antPk;
    private int antAnotherPk;
    private String antMatchName;
    private int antWon;
    private int antEq;

}
