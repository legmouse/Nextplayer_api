package kr.co.nextplayer.base.member.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Team {

    private String teamId;
    private String uage;
    private String nickName;
    private String addr;
    private String emblem;

}
