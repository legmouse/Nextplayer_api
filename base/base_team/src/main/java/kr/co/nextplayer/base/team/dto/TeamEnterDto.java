package kr.co.nextplayer.base.team.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class TeamEnterDto {

    private String team_id;
    private String nick_name;
    private int enter_cnt;

    public TeamEnterDto(String teamId, String nickName, int enterCnt) {
        this.team_id = teamId;
        this.nick_name = nickName;
        this.enter_cnt = enterCnt;
    }
}
