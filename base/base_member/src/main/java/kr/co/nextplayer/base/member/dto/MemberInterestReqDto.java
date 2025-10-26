package kr.co.nextplayer.base.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberInterestReqDto {

    @ApiModelProperty(value = "회원 고유변호", required = true)
    private String memberCd;

    @ApiModelProperty(value = "관심 종류", required = true, example = "Age, Team")
    @NotNull(message = "msg28_common_emptyParamErr")
    private String interestType;

    @ApiModelProperty(value = "연령, 팀 id")
    private String interestValue;

}
