package kr.co.nextplayer.base.league.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeagueYearRankDto {

    private String league_id;
    private String team_id;
    private String play_year;
    private String rank;
    private String league_name;

}
