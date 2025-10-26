package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class RosterPlayerDto {

    private String roster_id;
    private String player_id;

    private String name;
    private String position;
    private String birthday;
    private String nick_name;

    private String emblem;

    public RosterPlayerDto(String rosterId, String playerId, String name, String position, String birthday, String nickName, String emblem) {
        this.roster_id = rosterId;
        this.player_id = playerId;
        this.name = name;
        this.position = position;
        this.birthday = birthday;
        this.nick_name = nickName;
        this.emblem = emblem;
    }
}
