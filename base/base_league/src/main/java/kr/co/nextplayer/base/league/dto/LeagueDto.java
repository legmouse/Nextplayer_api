package kr.co.nextplayer.base.league.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import kr.co.nextplayer.base.team.model.Team;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class LeagueDto {

    @ApiModelProperty(value = "리그번호")
    private String leagueId;

    @ApiModelProperty(value = "리그명")
    private String leagueName;

    @ApiModelProperty(value = "광역명")
    private String areaName;

    @ApiModelProperty(value = "리그매치 번호")
    private String leagueMatchId;

    @ApiModelProperty(value = "리그 결과 번호")
    private String leagueResultId;

    private String uage;

    private String ageGroup;

    @ApiModelProperty(value = "홈팀 id")
    private String homeId;

    @ApiModelProperty(value = "어웨이팀 id")
    private String awayId;

    @ApiModelProperty(value = "경기장소")
    private String place;

    @ApiModelProperty(value = "홈팀")
    private String home;

    @ApiModelProperty(value = "어웨이팀")
    private String away;

    @ApiModelProperty(value = "홈팀 점수")
    private String homeScore;

    @ApiModelProperty(value = "어웨이팀 점수")
    private String awayScore;

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

    private Team team;

    private String teamId;

    private String teamType;

    private String teamName;

    private String nickName;

    private String addr;

    private String phone;

    private String fax;

    private String emblem;

}
