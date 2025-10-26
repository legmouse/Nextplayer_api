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
public class IndirectMatchesDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private long gameId;
    private String gameType;
    private String place;
    private LocalDateTime matchCalcDate;
    private String matchDate;
    private long teamId;
    private long anotherTeamId;
    private String team;
    private String anotherTeam;
    private String emblem;
    private String anotherEmblem;
    private int score;
    private int anotherScore;
    private int pk;
    private int anotherPk;
    private String matchName;

}
