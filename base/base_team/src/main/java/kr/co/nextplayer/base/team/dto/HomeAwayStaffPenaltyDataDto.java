package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeAwayStaffPenaltyDataDto {

    private String time;
    private String penalty_rank;
    private String staff_name;
    private String penalty_type;
    private String home_away_gbn;

}
