package kr.co.nextplayer.base.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(value = "회원 인증정보")
public class MemberConfirmRegDto {

    @ApiModelProperty(value = "회원 이름")
    private String memberName;

    @ApiModelProperty(value = "회원 고유번호")
    private String memberCd;

    @ApiModelProperty(value = "출생년도", example = "1996")
    private int age;

    @ApiModelProperty(value = "생년월일", example = "2003-03-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;

    @ApiModelProperty(value = "성별(여:W,남:M)")
    private String sex;

    @ApiModelProperty(value = "휴대폰번호")
    private String phoneNo;

    @ApiModelProperty(value = "CI(고유식별정보)")
    private String ciNo;

    @ApiModelProperty(value = "이메일주소")
    private String email;

    @ApiModelProperty(value = "연령대")
    private String ageGroup;

    @ApiModelProperty(value = "인증수단(휴대폰:P,SMS:S,이메일:E,기업:C) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)")
    private String certType;

}