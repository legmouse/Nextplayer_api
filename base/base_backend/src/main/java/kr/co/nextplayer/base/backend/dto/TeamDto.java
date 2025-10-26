package kr.co.nextplayer.base.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class TeamDto {

    private String team_id;
    private int teamId;
    private String areaId;

    private String selArea;

    private String teamType;
    private String teamName;
    private String nickName;
    private String addr;
    private String emblem;

    private String emblemFileParam;
    private String emblemFlag;

    private String uage;
    private String ageGroup;
    private String sArea;
    private String sTeamType;
    private String sNickName;

    private String totalCount;

    private String areaSearch;

    private String pId;
    private String pIdName;

    private String useFlag;

    private String sFlag;
    private String cp;

    private String sYear;

    private String orderName;
    private String order;

    private int sRow;
    private int eCount;

}
