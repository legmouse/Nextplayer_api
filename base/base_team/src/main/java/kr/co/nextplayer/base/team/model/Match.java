package kr.co.nextplayer.base.team.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Match {

    private String leagueMatchId;
    private String cupSubMatchId;

    private String cupId;
    private String cupName;
    private String cupType;

    private String leagueId;
    private String leagueName;

    private String round;
    private String playDate;

    private String pDate;
    private String pTime;

    private String place;

    private String homeId;
    private String home;
    private String homeScore;
    private String homePk;
    private String homeEmblem;
    private String homeType;

    private String awayId;
    private String away;
    private String awayScore;
    private String awayPk;
    private String awayEmblem;
    private String awayType;

    private String matchType;
    private String reason;

    private String matchDate;
    private String timeDiff;
    private String videoLive;
    private String videoRep;
    private String videoHigh;

    private String totalType;

    private String parseDate;

    private String playSDate;
    private String playEDate;

    private String progress;

    private String rank;
    private String result;
}
