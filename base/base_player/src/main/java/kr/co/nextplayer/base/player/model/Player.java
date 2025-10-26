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
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String playerId;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "포지션")
    private String position;

    @ApiModelProperty(value = "생년월일")
    private String birthday;

    @ApiModelProperty(value = "팀 번호")
    private String teamId;

    @ApiModelProperty(value = "팀 이름")
    private String teamName;

    @ApiModelProperty(value = "팀 타입")
    private String teamType;

    @ApiModelProperty(value = "연도")
    private String year;

    private String emblem;
}
