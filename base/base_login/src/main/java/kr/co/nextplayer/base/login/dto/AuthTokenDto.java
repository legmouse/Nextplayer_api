package kr.co.nextplayer.base.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class AuthTokenDto {

    @ApiModelProperty(value = "accessToken")
    String accessToken;

    @ApiModelProperty(value = "refreshToken")
    String refreshToken;

    @ApiModelProperty(value = "interestAge")
    String interestAge;

    String fcmToken;

}
