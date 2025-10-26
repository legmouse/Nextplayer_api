package kr.co.nextplayer.base.league.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.team.dto.HomeAwayTeamDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Getter
@Setter
@ApiModel
public class LeagueSearchMatchDto {

    private String league_id;

    private String league_match_id;
    private String league_name;
    private String playDate;
    private String pdate;
    private String ptime;
    private String match_date;
    private String place;
    private String time_diff;

    private String match_type;
    private String matchType;

    private String video_live;
    private String url_link;
    private String media_type;

    private String use_flag;

    private HomeAwayTeamDto homeAwayTeam;

    private List<LeagueMediaDto> mediaList = new ArrayList<LeagueMediaDto>();

}
