package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamMatchDto {

    private String league_id;
    private String team_id;

    private String pdate;
    private String place;
    private String match_date;
    private String match_type;

    private String time_diff;
    private String video_live;
    private String video_rep;
    private String video_high;

    private String year;
    private String reason;

    private HomeAwayTeamDto homeAwayTeamDto;
}
