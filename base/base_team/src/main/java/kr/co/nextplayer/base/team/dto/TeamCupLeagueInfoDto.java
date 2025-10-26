package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamCupLeagueInfoDto {

    private String team_id;

    private int c_played;
    private int c_won;
    private int c_draw;
    private int c_lost;
    private int c_gf;
    private int c_ga;
    private float c_avg_gf;
    private float c_avg_ga;

    private int l_played;
    private int l_won;
    private int l_draw;
    private int l_lost;
    private int l_gf;
    private int l_ga;
    private float l_avg_gf;
    private float l_avg_ga;

}
