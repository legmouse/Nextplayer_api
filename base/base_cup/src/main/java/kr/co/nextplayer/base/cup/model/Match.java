package kr.co.nextplayer.base.cup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Match {

    @ApiModelProperty(value = "대회 정보 테이블")
    private String cupInfoTB;

    @ApiModelProperty(value = "대회 예선 정보 테이블")
    private String cupSubMatchTB;

    @ApiModelProperty(value = "대회 본선 정보 테이블")
    private String cupMainMatchTB;

    @ApiModelProperty(value = "대회 토너먼트 정보 테이블")
    private String cupTourMatchTB;

    @ApiModelProperty(value = "대회 번호")
    private String cupId;

    @ApiModelProperty(value = "홈팀 id")
    private String homeId;

    @ApiModelProperty(value = "어웨이팀 id")
    private String awayId;

    @ApiModelProperty(value = "조")
    private String groups;

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

    @ApiModelProperty(value = "홈팀 PK점수")
    private String homePk;

    @ApiModelProperty(value = "어웨이팀 PK점수")
    private String awayPk;

    @ApiModelProperty(value = "경기사유")
    private String reason;

    @ApiModelProperty(value = "생중계")
    private String videoLive;

    @ApiModelProperty(value = "다시보기")
    private String videoRep;

    @ApiModelProperty(value = "하이라이트")
    private String videoHigh;

    @ApiModelProperty(value = "대회 타입(0: 정규, 1: 승부차기, 2: 홈팀부전승, 3: 어웨이부전승, 4: 홈팀몰수패, 5: 어웨이몰수패")
    private String matchType;

    @ApiModelProperty(value = "사용여부(0: 사용, 1: 미사용)")
    private String useFlag;

    @ApiModelProperty(value = "경기일시")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private LocalDateTime matchDate;

    @ApiModelProperty(value = "생성일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

}
