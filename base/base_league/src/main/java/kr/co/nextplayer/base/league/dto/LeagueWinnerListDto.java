package kr.co.nextplayer.base.league.dto;

import kr.co.nextplayer.base.team.dto.CommonTeamDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeagueWinnerListDto {

    private String uage;

    private String league_id;
    private String league_name;
    private String play_sdate;

    private CommonTeamDto team;
    private LeagueResultDto leagueResult;
}
