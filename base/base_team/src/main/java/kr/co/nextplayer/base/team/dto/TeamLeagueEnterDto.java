package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamLeagueEnterDto {

    private String l_league_id;
    private String league_id;
    private String league_name;

    private String team_id;

    private String play_sdate;
    private String rank;

    private int played;
    private int points;
    private int won;
    private int draw;
    private int lost;
    private int gf;
    private int ga;
    private int gd;

    private String uage;
    private String area_name;
    private String team_type;
    private String team_name;
    private String nick_name;
    private String emblem;
    private String use_flag;

}
