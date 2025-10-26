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
public class PlayerHistoryDto {

    private String team_id;
    private String nick_name;
    private String team_type;

    private String year;

    private String emblem;

    public PlayerHistoryDto(String teamId, String teamName, String teamType, String year, String emblem) {

        this.team_id = teamId;
        this.nick_name = teamName;
        this.team_type = teamType;
        this.year = year;
        this.emblem = emblem;

    }

}
