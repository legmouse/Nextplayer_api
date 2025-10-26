package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class RosterTeam {

    private String teamId;

    private String name;

    private String position;
    private String birthday;

    private int nationalCnt;
    private int localCnt;
    private int fiveAreaCnt;
    private int allAreaCnt;
    private int giftedCnt;
    private int futureCnt;
    private int eliteCnt;
}
