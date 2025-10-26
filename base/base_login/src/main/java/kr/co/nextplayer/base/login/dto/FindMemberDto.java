package kr.co.nextplayer.base.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "회원정보 찾기")
public class FindMemberDto {

    @ApiModelProperty(value = "타입(SMS:S,이메일:E)", required = true)
    @NotNull(message = "msg28_common_emptyParamErr")
    private String type;

    @ApiModelProperty(value = "회원 아이디")
    private String id;

    @ApiModelProperty(value = "휴대폰번호")
    private String phoneNo;

    @ApiModelProperty(value = "이메일주소")
    private String email;

    @ApiModelProperty(value = "인증번호")
    private String ConfirmCode;

}