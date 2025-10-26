package kr.co.nextplayer.base.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberConfirmDto {

    @ApiModelProperty(value = "번호")
    private Integer confirmNo;

    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    @ApiModelProperty(value = "회원 이름")
    private String memberName;

    @ApiModelProperty(value = "생년월일")
    private String birthDate;

    @ApiModelProperty(value = "성별(여:W,남:M)")
    private String sex;

    @ApiModelProperty(value = "휴대폰번호")
    private String phoneNo;

    @ApiModelProperty(value = "CI(고유식별정보)")
    private String ciNo;

    @ApiModelProperty(value = "국가코드")
    private String nation;

    @ApiModelProperty(value = "이메일주소")
    private String email;

    @ApiModelProperty(value = "인증수단(휴대폰:P,SMS:S,이메일:E)")
    private String certType;

    @ApiModelProperty(value = "인증일시")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime certDate;

    @ApiModelProperty(value = "회원정보")
    private Member member;
}