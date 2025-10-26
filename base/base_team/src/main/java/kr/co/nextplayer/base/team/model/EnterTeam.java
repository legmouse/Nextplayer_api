package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class EnterTeam {

    private String teamId;

    private String team;

    private String nickName;

    private int enterCnt;
}
