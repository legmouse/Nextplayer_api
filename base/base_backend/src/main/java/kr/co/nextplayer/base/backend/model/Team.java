package kr.co.nextplayer.base.backend.model;

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

    private String ageGroup;
    private String sArea;
    private String sTeamType;
    private String sNickName;

    private String team_id;
    private String uage;

    private String areaName;

    private String teamType;
    private String teamName;
    private String nickName;
    private String addr;
    private String emblem;
    private String useFlag;
    private String pid;
    private String pidName;

    private String totalCount;

    private Cup cup;
    private League league;
}
