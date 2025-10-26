package kr.co.nextplayer.base.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.member.constants.Position;
import kr.co.nextplayer.base.member.dto.MemberConfirmRegDto;
import kr.co.nextplayer.base.member.dto.MemberSnsRegDto;
import lombok.*;

import javax.validation.constraints.NotNull;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberRegisterDto {

    @ApiModelProperty(value = "회원 닉네임", required = true, example = "맹구리다")
    @NotNull(message = "msg28_common_emptyParamErr")
    private String memberNickname;

    @ApiModelProperty(value = "자녀 연령", required = true, example = "1996")
    private String childrenAge;

    @ApiModelProperty(value = "아이디", required = true, example = "ID123456789")
    private String memberId;

    @ApiModelProperty(value = "비밀번호", required = true, example = "Pwd123456789")
    private String memberPwd;

    @ApiModelProperty(value = "메모")
    private String memo;

    @ApiModelProperty(value = "유형")
    private Position position;

    @ApiModelProperty(value = "회원 타입")
    private String memberType;

    @ApiModelProperty(value = "회원 인증정보", required = true)
    @NotNull(message = "msg28_common_emptyParamErr")
    private MemberConfirmRegDto memberConfirm;

    @ApiModelProperty(value = "회원 sns정보")
    private MemberSnsRegDto memberSns;

}
