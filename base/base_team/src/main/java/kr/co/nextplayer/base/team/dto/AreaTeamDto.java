package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AreaTeamDto {

    private String team_id;
    private String uage;
    private String team_type;
    private String nick_name;
    private String emblem;

    private String addr;
    private int plays;
    private int win;
    private int draw;
    private int lose;
    private int gf;
    private int ga;
    private int gd;
    private int points;

}
