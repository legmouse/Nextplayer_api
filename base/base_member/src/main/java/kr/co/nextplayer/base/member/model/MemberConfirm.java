package kr.co.nextplayer.base.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberConfirm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 번호
     */
    @ApiModelProperty(value = "번호")
    private Integer confirmNo;

    /**
     * 회원번호
     */
    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    /**
     * 회원 이름
     */
    @ApiModelProperty(value = "회원 이름")
    private String memberName;

    /**
     * 출생년도
     */
    @ApiModelProperty(value = "출생년도")
    private int age;

    /**
     * 생년월일
     */
    @ApiModelProperty(value = "생년월일")
    private String birthDate;

    /**
     * 성별(여:W,남:M)
     */
    @ApiModelProperty(value = "성별(여:W,남:M)")
    private String sex;

    /**
     * 휴대폰번호
     */
    @ApiModelProperty(value = "휴대폰번호")
    private String phoneNo;

    /**
     * CI(고유식별정보)
     */
    @ApiModelProperty(value = "CI(고유식별정보)")
    private String ciNo;


    /**
     * 이메일주소
     */
    @ApiModelProperty(value = "이메일주소")
    private String email;

    /**
     * 연령대
     */
    @ApiModelProperty(value = "연령대")
    private String ageGroup;

    /**
     * 인증수단(휴대폰:P,SMS:S,이메일:E,기업:C) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)
     */
    @ApiModelProperty(value = "인증수단(휴대폰:P,SMS:S,이메일:E,기업:C) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)")
    private String certType;

    /**
     * 인증일시
     */
    @ApiModelProperty(value = "인증일시")
    private LocalDateTime certDate;

}