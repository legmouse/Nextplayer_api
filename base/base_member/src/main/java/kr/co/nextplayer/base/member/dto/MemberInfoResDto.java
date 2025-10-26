package kr.co.nextplayer.base.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.member.model.Team;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberInfoResDto {

    @ApiModelProperty(value = "회원 고유 번호", required = true, example = "11U168628524602020230609004")
    @NotNull(message = "msg28_common_emptyParamErr")
    private String member_cd;

    @ApiModelProperty(value = "회원 아이디", hidden = true)
    private String member_id;

    @ApiModelProperty(value = "회원 닉네임", hidden = true)
    private String member_nickname;

    @ApiModelProperty(value = "이메일주소")
    private String email;

    @ApiModelProperty(value = "휴대폰번호")
    private String phone_no;

    @ApiModelProperty(value = "성별(여:W,남:M)")
    private String sex;

    @ApiModelProperty(value = "출생년도")
    private int age;

    @ApiModelProperty(value = "회원 이름")
    private String member_name;

    @ApiModelProperty(value = "연령대")
    private String age_group;

    @ApiModelProperty(value = "생년월일")
    private String birth_date;

    @ApiModelProperty(value = "자녀 나이", hidden = true)
    private String children_age;

    @ApiModelProperty(value = "회원 타입", hidden = true)
    private String member_type;

    @ApiModelProperty(value = "인증수단(휴대폰:P,SMS:S,이메일:E,기업:C) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)")
    private String cert_type;

    private String interest_age;
    private String interest_team;

    private int cMoney;

    private Team teamInfo;
}

