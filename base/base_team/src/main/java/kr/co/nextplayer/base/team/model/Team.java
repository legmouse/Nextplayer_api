package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Getter
@Setter
@ApiModel
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    private String teamId;

    private String team;

    private String teams;

    private String uage;

    private String areaName;

    private String teamType;

    private String teamName;

    private String nickName;

    private String addr;

    private String phone;

    private String fax;

    private String emblem;

    private String pId;

    private String pIdName;

    private String useFlag;

    private String regDate;

    private String plays;

    private String win;
    private String lose;
    private String draw;

    private String points;

    private String ga;
    private String gd;
    private String gf;

    private String played;
    private String won;
    private String lost;

    private String avgGf;
    private String avgGa;

    private String cPlayed;
    private String cWon;
    private String cLost;
    private String cDraw;
    private String cGa;
    private String cGf;
    private String cAvgGf;
    private String cAvgGa;

    private String lPlayed;
    private String lWon;
    private String lLost;
    private String lDraw;
    private String lGa;
    private String lGf;
    private String lAvgGf;
    private String lAvgGa;

    private String tWon;
    private String tDraw;
    private String tLost;

    private String playYear;

    private String result;
    private String rank;

    private Match match;

}
