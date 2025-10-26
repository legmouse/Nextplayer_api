package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeAwayMatchChangeDataDto {

    private String time;
    private String in_player_id;
    private String in_player_number;
    private String in_player_name;
    private String out_player_id;
    private String out_player_number;
    private String out_player_name;
    private String home_away_gbn;

}
