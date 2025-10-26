package kr.co.nextplayer.base.league.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeagueResultDto {

    private String team_id;
    private String league_id;

    private int points;
    private int won;
    private int draw;
    private int lost;
    private int gf;
    private int ga;
    private int gd;
}
