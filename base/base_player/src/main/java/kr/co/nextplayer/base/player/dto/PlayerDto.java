package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PlayerDto {

    private String player_id;
    private String name;
    private String position;
    private String birthday;
    private String team_id;
    private String nick_name;
    private String team_type;

    private String emblem;

    public PlayerDto(String playerId, String name, String position, String birthday, String teamId, String teamName, String teamType, String emblem) {
        this.player_id = playerId;
        this.name = name;
        this.position = position;
        this.birthday = birthday;
        this.team_id = teamId;
        this.nick_name = teamName;
        this.team_type = teamType;
        this.emblem = emblem;
    }

}
