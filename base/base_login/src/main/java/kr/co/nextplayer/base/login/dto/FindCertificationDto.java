package kr.co.nextplayer.base.login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindCertificationDto {

    @ApiModelProperty(value = "인증 성공여부(성공:1,실패:-1)")
    @Builder.Default
    private int checkState = -1;

    @ApiModelProperty(value = "기존 가입자 수량")
    @Builder.Default
    private int userCount = 0;

    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,null:오프라인회원)")
    private String memberState;

    @ApiModelProperty(value = "회원 가입 방식: 인증수단(휴대폰:P,SMS:S,이메일:E) + sns구분(네이버:NAVER,카카오:KAKAO,구글:GOOGLE,애플:APPLE)")
    private String regType;

    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    @ApiModelProperty(value = "아이디")
    private String memberId;

    @ApiModelProperty(value = "아이디_noMasking")
    private String memberId_noMasking;

    @ApiModelProperty(value = "사용자 명")
    private String memberName;

    @ApiModelProperty(value = "인증일시")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime certDate;

}
