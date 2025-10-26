package kr.co.nextplayer.base.cup.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Getter
@Setter
@ApiModel
public class CupTour extends Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "토너먼 번호")
    @Id
    private String cupTourMatchId;

    @ApiModelProperty(value = "강수")
    private String round;

    @ApiModelProperty(value = "토너먼트 번호")
    private String tourNo;

    @ApiModelProperty(value = "다음경기 토너먼트 번호")
    private String nextTourNo;

    @ApiModelProperty(value = "다음경기 자리구분(0: 홈, 1: 어웨이)")
    private String nextTourPort;

    @ApiModelProperty(value = "토너먼트 대진표 확정 유무")
    private String fixFlag;

}
