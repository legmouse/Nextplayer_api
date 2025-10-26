package kr.co.nextplayer.base.league.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.team.dto.HomeAwayMatchChangeDataDto;
import kr.co.nextplayer.base.team.dto.HomeAwayMatchPlayDataDto;
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
public class LeagueMatchDto {

    private String league_id;

    private String league_match_id;
    private String league_name;
    private String playdate;
    private String pdate;
    private String ptime;
    private String match_date;
    private String place;
    private String time_diff;

    private String match_type;
    private String reason;

    private String use_flag;
    private String score_show_flag;

    private String parse_date;

    private String years;
    private String months;

    private HomeAwayTeamDto homeAwayTeam;
    private List<HomeAwayMatchPlayDataDto> homeAwayMatchPlayData;
    private List<HomeAwayMatchChangeDataDto> homeAwayMatchChangeData;

    List<LeagueMediaDto> mediaList = new ArrayList<LeagueMediaDto>();
}
