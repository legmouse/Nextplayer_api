package kr.co.nextplayer.base.player.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Roster implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String rosterId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String comment;

    @ApiModelProperty(value = "연도")
    private String year;

    @ApiModelProperty(value = "내용")
    private String uage;

    @ApiModelProperty(value = "대표팀 타입")
    private String rosterType;

    @ApiModelProperty(value = "소집 타입")
    private String type;

    @ApiModelProperty(value = "등록 일자")
    private String regDate;

    @ApiModelProperty(value = "소집 횟수")
    private int enterCnt;

    @ApiModelProperty(value = "지역센터 소집 횟수")
    private int localCnt;

    @ApiModelProperty(value = "5대광역 소집 횟수")
    private int fiveAreaCnt;

    @ApiModelProperty(value = "합동광역 소집 횟수")
    private int allAreaCnt;

    @ApiModelProperty(value = "퓨처팀 소집 횟수")
    private int futureCnt;

    private int giftedCnt;

    @ApiModelProperty(value = "영재센터 소집 횟수")
    private int eliteCnt;

    @ApiModelProperty(value = "센터 타입")
    private String centerType;

    private List<PublicFile> rosterFiles = new ArrayList<PublicFile>();
}
