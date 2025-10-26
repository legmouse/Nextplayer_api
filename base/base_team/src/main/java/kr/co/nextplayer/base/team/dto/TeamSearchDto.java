package kr.co.nextplayer.base.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamSearchDto {

    private String team_id;
    private String uage;
    private String team_type;
    private String team_name;
    private String nick_name;
    private String emblem;


}
