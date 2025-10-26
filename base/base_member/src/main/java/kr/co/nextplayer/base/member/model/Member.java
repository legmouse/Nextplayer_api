package kr.co.nextplayer.base.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.member.constants.Position;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "등록/가입일자", hidden = true)
    private LocalDateTime regDate;

    @ApiModelProperty(value = "관리자 수정자 ID", hidden = true)
    private String updAdminId;

    @ApiModelProperty(value = "관리자 수정일자", hidden = true)
    private LocalDateTime updAdminDate;

    @ApiModelProperty(value = "수정일자", hidden = true)
    private LocalDateTime updDate;

    @ApiModelProperty(value = "마지막 로그인 아이피", hidden = true)
    private String lastConnectIp;

    @ApiModelProperty(value = "마지막 로그인 날짜", hidden = true)
    private LocalDateTime lastConnectDate;

    @ApiModelProperty(value = "패스워드 업데이트 날짜", hidden = true)
    private LocalDateTime pwdUpdDate;

    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제)", example = "1")
    private String memberState;

    @ApiModelProperty(value = "프로필 이미지 경로", hidden = true)
    private String profileImg;

    @ApiModelProperty(value = "메모", hidden = true)
    private String memo;

    @ApiModelProperty(value = "유형", hidden = true)
    private Position position;

    @ApiModelProperty(value = "회원 타입", hidden = true)
    private String memberType;

    @ApiModelProperty(value = "관심 연령", hidden = true)
    private String interestAge;

    @ApiModelProperty(value = "fcm token", hidden = true)
    private String fcmToken;
}
