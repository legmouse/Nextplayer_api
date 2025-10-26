package kr.co.nextplayer.base.team.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class TeamDto {

    private String ageGroup;

    private String teamId;
    private String teamId1;
    private String teamId2;

    private String areaId;

    private String homeTeamId;
    private String awayTeamId;

    private String uage;

    private String areaName;

    private String teamType;

    private String teamName;

    private String nickName;

    private String sNickName;

    private String addr;

    private String phone;

    private String fax;

    private String emblem;

    private String pId;

    private String pIdName;

    private String useFlag;

    private String regDate;

    private String sYear;

    private String rosterType;

    private String leagueInfoTB;
    private String leagueTeamTB;
    private String leagueMatchTB;
    private String leagueResultTB;

    private String cupInfoTB;
    private String cupTeamTB;
    private String cupSubMatchTB;
    private String cupMainMatchTB;
    private String cupTourMatchTB;
    private String cupResultTB;

    private String opponentGameVW;

    private String childId;

    private int curPage;
    private int offset;
    private int pageSize;
    private String sKeyword;

    private String teamGroupId;
}
