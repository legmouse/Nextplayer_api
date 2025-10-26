package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class IndirectMatches implements Serializable {

    private static final long serialVersionUID = 1L;

    private String team;

    private long hmtTeamId;
    private long hmtAnotherTeamId;
    private String hmtTeam;
    private String hmtAnotherTeam;
    private String hmtEmblem;
    private String hmtAnotherEmblem;
    private int hmtWon;
    private int hmtLose;
    private int hmtEq;

    private long antTeamId;
    private long antAnotherTeamId;
    private String antTeam;
    private String antAnotherTeam;
    private String antEmblem;
    private String antAnotherEmblem;
    private int antWon;
    private int antLose;
    private int antEq;

    List<IndirectMatchesDetail> hmtDetails = new ArrayList<>();
    List<IndirectMatchesDetail> antDetails = new ArrayList<>();

}
