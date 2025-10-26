package kr.co.nextplayer.base.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberInfo {

    @ApiModelProperty(value = "회원번호", required = true, example = "123456789")
    @Id
    private String memberCd;

    @ApiModelProperty(value = "회원 이름", hidden = true)
    private String memberName;

    @ApiModelProperty(value = "회원 닉네임", hidden = true)
    private String memberNickname;

    @ApiModelProperty(value = "자녀 나이", hidden = true)
    private String childrenAge;

    @ApiModelProperty(value = "아이디", required = true, example = "ID123456789")
    private String memberId;

    @ApiModelProperty(value = "비밀번호", required = true, example = "Pwd123456789")
    private String memberPwd;


    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제)", example = "1")
    private String memberState;

    @ApiModelProperty(value = "메모", hidden = true)
    private String memo;

    @ApiModelProperty(value = "회원 타입", hidden = true)
    private String memberType;

    @ApiModelProperty(value = "번호")
    private Integer confirmNo;

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

    private String interestAge;
    private String interestTeam;

    private Team teamInfo;
}
