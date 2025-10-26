package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeAwayMatchPlayDataDto {

    private String player_number;
    private String position;
    private String player_name;
    private String compete;
    private String goal;
    private String help;
    private String warning;
    private String gexit;
    private String pso;
    private String home_away_gbn;
    private String sel_can_gbn;
    private String player_id;
    private String captain_flag;
}
