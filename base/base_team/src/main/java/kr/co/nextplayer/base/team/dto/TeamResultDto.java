package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamResultDto {

    private String team_id;
    private int played;
    private int won;
    private int draw;
    private int lost;
    private int gf;
    private int ga;
    private float avg_gf;
    private float avg_ga;

    private float avg_won;
    private float avg_lost;

    private String year;
    private String play_year;

}
