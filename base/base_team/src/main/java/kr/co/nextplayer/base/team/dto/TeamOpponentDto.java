package kr.co.nextplayer.base.team.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamOpponentDto {

    private int won;
    private int lost;
    private int draw;
    private String sdate;

}
