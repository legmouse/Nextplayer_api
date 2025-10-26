package kr.co.nextplayer.base.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "SMS 인증")
public class SmsCertSend {

    @ApiModelProperty(value = "sms_from_phone_no")
    private String fromPhoneNo;
    @ApiModelProperty(value = "sms_cert_gbn")
    private String certGbn;
    @ApiModelProperty(value = "sms_cert_number")
    private String certNumber;

}
