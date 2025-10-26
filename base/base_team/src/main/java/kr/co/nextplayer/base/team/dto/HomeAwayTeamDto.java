package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeAwayTeamDto {

    private String home_id;
    private String home;
    private int home_score;
    private int home_pk;
    private String home_emblem;
    private String home_type;

    private String away_id;
    private String away;
    private int away_score;
    private int away_pk;
    private String away_emblem;
    private String away_type;

    private String home_sub_rank;
    private String home_main_rank;
    private String away_sub_rank;
    private String away_main_rank;

    private int home_sub_gf;
    private int home_main_gf;
    private int home_sub_ga;
    private int home_main_ga;

    private int away_sub_gf;
    private int away_main_gf;
    private int away_sub_ga;
    private int away_main_ga;
}
