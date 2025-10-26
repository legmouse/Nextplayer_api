package kr.co.nextplayer.base.team.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class TeamRosterDto {

    private String team_id;
    private String name;

    private String position;
    private String birthday;

    private int national_cnt;
    private int local_cnt;
    private int fiveArea_cnt;
    private int allArea_cnt;
    private int gifted_cnt;
    private int future_cnt;
    private int elite_cnt;

}
