package kr.co.nextplayer.base.league.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BannerLeagueListDto {

    private String league_id;
    private String league_name;
    private String league_name2;
    private String place;
    private String play_flag;
    private String play_sdate;
    private String play_edate;
    private String parsSDate;
    private String parsEDate;
    private String progress;
    private String uage;

}
