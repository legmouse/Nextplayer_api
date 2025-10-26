package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PlayerDetailDto {

    private String player_id;
    private String name;
    private String position;
    private String birthday;

    private String team_id;
    private String emblem;

    /*public PlayerDetailDto(String playerId, String name, String position, String birthday) {
        this.player_id = playerId;
        this.name = name;
        this.position = position;
        this.birthday = birthday;
    }*/

}
