package kr.co.nextplayer.base.cup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.config.BaseLocalDateTimeSerializerConfig;
import kr.co.nextplayer.base.team.model.Team;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Cup extends Team implements Serializable  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "대회번호")
    @Id
    private String cupId;

    @ApiModelProperty(value = "대회번호 매치 아이디")
    @Id
    private String matchId;

    @ApiModelProperty(value = "팀번호")
    @Id
    private String teamId;

    @ApiModelProperty(value = "U-age")
    private String uage;

    @ApiModelProperty(value = "대회 팀 테이블")
    private String cupTeamTB;

    @ApiModelProperty(value = "대회 정보 테이블")
    private String cupInfoTB;

    @ApiModelProperty(value = "대회 예선 정보 테이블")
    private String cupSubMatchTB;

    @ApiModelProperty(value = "대회 본선 정보 테이블")
    private String cupMainMatchTB;

    @ApiModelProperty(value = "대회 토너먼트 정보 테이블")
    private String cupTourMatchTB;

    @ApiModelProperty(value = "대회 결과 정보 테이블")
    private String cupResultTB;

    private String cupSubMatchPlayDataTB;
    private String cupSubMatchChangeDataTB;

    private String cupMainMatchPlayDataTB;
    private String cupMainMatchChangeDataTB;

    private String cupTourMatchPlayDataTB;
    private String cupTourMatchChangeDataTB;

    private String cupSubStaffDataTB;
    private String cupSubStaffPenaltyDataTB;
    private String cupSubOwnGoalDataTB;

    private String cupMainStaffDataTB;
    private String cupMainStaffPenaltyDataTB;
    private String cupMainOwnGoalDataTB;

    private String cupTourStaffDataTB;
    private String cupTourStaffPenaltyDataTB;
    private String cupTourOwnGoalDataTB;

    @ApiModelProperty(value = "대회명")
    private String cup_name;

    @ApiModelProperty(value = "대회명2")
    private String cupName2;

    @ApiModelProperty(value = "대회 장소")
    private String place;

    @ApiModelProperty(value = "참가 팀")
    private String cupTeam;

    @ApiModelProperty(value = "토너먼트 팀수")
    private String tourTeam;

    @ApiModelProperty(value = "그룹")
    private String groups;

    @ApiModelProperty(value = "그룹")
    private String round;

    @ApiModelProperty(value = "대회개요")
    private String cupInfo;

    @ApiModelProperty(value = "대회수상")
    private String cupPrize;

    @ApiModelProperty(value = "U-age")
    private String ageGroup;

    @ApiModelProperty(value = "대회상태")
    private String progress;

    @ApiModelProperty(value = "예선 조편성 팀수 예: 14/4 (조/팀수)")
    private String subTeamCount;

    @ApiModelProperty(value = "본선 조편성 팀수 예: 14/4 (조/팀수)")
    private String mainTeamCount;

    @ApiModelProperty(value = "대회 활성여부(0: 사용, 1: 미사용")
    private String playFlag;

    @ApiModelProperty(value = "사용여부(0: 사용, 1: 미사용")
    private String useFlag;

    @ApiModelProperty(value = "대회 유형(0:예선 + 토너먼트, 1: 예선 + 본선 + 토너먼트, 2: 풀리그, 3: 토너먼트")
    private String cupType;

    @ApiModelProperty(value = "순위선정방식(0: 골득실, 1: 승자승")
    private String rankType;

    @ApiModelProperty(value = "토너먼트 타입(0: 대진표, 1: 추첨제)")
    private String tourType;

    private String matchDate;

    private String sYear;

    private String groupCount;
    private String teamCount;
    private String mGroupCount;
    private String mTeamCount;

    private String sDate;
    private String eDate;
    private String sDate1;
    private String eDate1;
    private String sDate2;
    private String eDate2;

    private String lYears;

    private String playDate;

    @ApiModelProperty(value = "대회 기간 시작일")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String playSDate;

    @ApiModelProperty(value = "대회 기간 종료일")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String playEDate;

    private String parsSDate;

    private String parsEDate;

    @ApiModelProperty(value = "생성일")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonDeserialize(using = BaseLocalDateTimeSerializerConfig.LocalDateTimeDeserializer.class)
    private String regDate;

    private String teamType;

    private String parentTB;
    private String parentId;

    private String childTB;
    private String childId;
    private String childKey;

    private String regionType;

//    @ApiModelProperty(value = "팀정보")
//    private Team team;
}
