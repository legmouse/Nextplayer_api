package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamCupResultDto {

    private String cup_id;
    private String cup_name;
    private String result;

    private String cup_type;
    private String play_sdate;
    private String play_edate;

    private String year;


}
