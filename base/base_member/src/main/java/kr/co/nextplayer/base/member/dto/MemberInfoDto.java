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
public class MemberInfoDto {

    @ApiModelProperty(value = "회원 고유 번호", required = true, example = "11U168628524602020230609004")
    @NotNull(message = "msg28_common_emptyParamErr")
    private String memberCd;

}
