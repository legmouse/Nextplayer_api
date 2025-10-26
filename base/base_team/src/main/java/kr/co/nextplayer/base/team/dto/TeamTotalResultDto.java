package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamTotalResultDto {

    private String team_id;
    private int c_won;
    private int c_draw;
    private int c_lost;
    private int l_won;
    private int l_draw;
    private int l_lost;
    private int t_won;
    private int t_draw;
    private int t_lost;


}
