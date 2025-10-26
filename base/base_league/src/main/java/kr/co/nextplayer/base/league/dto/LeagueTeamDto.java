package kr.co.nextplayer.base.league.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeagueTeamDto {

    private String team_id;
    private String team_type;
    private String emblem;
    private String team;

    private String league_result_id;
    private String result;
    private int rank;
    private int playTotalCnt;
    private int rankPoint;
    private int rankPointAdd;

    private int win;
    private int draw;
    private int lose;

    private int point;
    private int losePoint;
    private int goalPoint;


}
