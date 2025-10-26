package kr.co.nextplayer.base.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.member.dto.MemberSnsRegDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "SNS 인증정보")
public class Auth {

    @ApiModelProperty(value = "sns_access_token")
    private String access_token;
    @ApiModelProperty(value = "sns_refresh_token")
    private String refresh_token;
    @ApiModelProperty(value = "sns_token_type")
    private String token_type;
    @ApiModelProperty(value = "sns_expires_in")
    private String expires_in;

    @ApiModelProperty(value = "회원 sns정보")
    private MemberSnsRegDto memberSns;

    @ApiModelProperty(value = "기존 회원 맞음:1, 기존 회원 아님:0")
    private String memberExisted;

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "refreshToken")
    private String refreshToken;

    @ApiModelProperty(value = "interestAge")
    private String interestAge;

    @ApiModelProperty(value = "fcmToken")
    private String fcm_token;

}
