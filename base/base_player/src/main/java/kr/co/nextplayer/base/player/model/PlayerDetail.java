package kr.co.nextplayer.base.player.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PlayerDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String playerId;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "포지션")
    private String position;

    @ApiModelProperty(value = "생년월일")
    private String birthday;

    private String teamId;
    private String emblem;

}
