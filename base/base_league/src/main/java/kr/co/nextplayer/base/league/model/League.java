package kr.co.nextplayer.base.league.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import kr.co.nextplayer.base.team.model.Team;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class League extends Team implements Serializable {

    private static final long serialVersionUID = 1L;

    private String teamId;

    @ApiModelProperty(value = "리그번호")
    private String leagueId;

    @ApiModelProperty(value = "리그 정보 테이블")
    private String leagueInfoTB;

    @ApiModelProperty(value = "리그 팀 테이블")
    private String leagueTeamTB;

    @ApiModelProperty(value = "리그 매치 테이블")
    private String leagueMatchTB;

    @ApiModelProperty(value = "리그 결과 테이블")
    private String leagueResultTB;

    private String leagueMatchPlayDataTB;
    private String leagueMatchChangeDataTB;

    @ApiModelProperty(value = "리그명")
    private String leagueName;

    @ApiModelProperty(value = "광역명")
    private String areaName;

    private String ageGroup;

    @ApiModelProperty(value = "리그매치 번호")
    private String leagueMatchId;

    @ApiModelProperty(value = "리그 결과 번호")
    private String leagueResultId;

    @ApiModelProperty(value = "경기장소")
    private String place;

    @ApiModelProperty(value = "홈팀 id")
    private String homeId;

    @ApiModelProperty(value = "홈팀")
    private String home;

    @ApiModelProperty(value = "홈팀 점수")
    private String homeScore;

    private String homeEmblem;
    private String homeType;


    @ApiModelProperty(value = "어웨이팀 id")
    private String awayId;
    @ApiModelProperty(value = "어웨이팀")
    private String away;

    @ApiModelProperty(value = "어웨이팀 점수")
    private String awayScore;

    private String awayEmblem;
    private String awayType;

    @ApiModelProperty(value = "경기사유")
    private String reason;

    @ApiModelProperty(value = "최종성적")
    private String result;

    @ApiModelProperty(value = "최종순위")
    private String rank;

    @ApiModelProperty(value = "경기 수")
    private String played;

    @ApiModelProperty(value = "승점")
    private String points;

    @ApiModelProperty(value = "추가 승점")
    private String pointsAdd;

    @ApiModelProperty(value = "승")
    private String won;

    @ApiModelProperty(value = "무")
    private String draw;

    @ApiModelProperty(value = "패")
    private String lost;

    @ApiModelProperty(value = "득점")
    private String gf;

    @ApiModelProperty(value = "실점")
    private String ga;

    @ApiModelProperty(value = "골득실")
    private String gd;

    @ApiModelProperty(value = "생중계")
    private String videoLive;

    @ApiModelProperty(value = "다시보기")
    private String videoRep;

    @ApiModelProperty(value = "하이라이트")
    private String videoHigh;

    @ApiModelProperty(value = "대회 타입(0: 정규, 1: 승부차기, 2: 홈팀부전승, 3: 어웨이부전승, 4: 홈팀몰수패, 5: 어웨이몰수패")
    private String matchType;

    @ApiModelProperty(value = "리그 활성 여부(0: 사용, 1: 미사용)")
    private String playFlag;

    @ApiModelProperty(value = "사용 여부(0: 사용, 1: 미사용)")
    private String useFlag;

    private String matchDate;

    private String sYear;

    @ApiModelProperty(value = "리그 기간 시작일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String playSDate;

    @ApiModelProperty(value = "리그 기간 종료일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String playEDate;

    @ApiModelProperty(value = "생성일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String regDate;

    @ApiModelProperty(value = "수정일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String modDate;

    private String lLeagueId;
    private String lTeamId;
    private String infoLeagueId;

    private String years;
    private String months;

    private String playDate;
    private String pDate;
    private String pTime;

    private String timeDiff;

    private String parseDate;

    private String playTotalCnt;
    private String rankPoint;
    private String rankPointAdd;
    private String point;
    private String goalPoint;
    private String losePoint;

    private String sDate;

    private String parentTB;
    private String parentId;

    private String childTB;
    private String childId;
    private String childKey;

    //private Team team;

}
