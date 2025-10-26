package kr.co.nextplayer.base.cup.dto.cup;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupTeamDto {

    private String team_id;
    private String team_type;
    private String emblem;
    private String team;
    private String teams;

    private String main_teams;

    private String confirmRank;

}
