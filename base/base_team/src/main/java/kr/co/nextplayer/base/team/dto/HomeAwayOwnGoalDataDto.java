package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeAwayOwnGoalDataDto {

    private String home_away_gbn;
    private String time;
    private String player_number;
    private String player_name;
    private String player_id;

}
