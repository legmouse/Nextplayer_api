package kr.co.nextplayer.base.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPushSettingDto {

    @ApiModelProperty(value = "경기 시작전 알림", example = "1")
    private String gameTimePushAgree;
    @ApiModelProperty(value = "게임종료 알림", example = "1")
    private String gameEndPushAgree;
    @ApiModelProperty(value = "라인업 알림", example = "1")
    private String lineupPushAgree;
    @ApiModelProperty(value = "대회등록 알림", example = "1")
    private String contestRegPushAgree;
    @ApiModelProperty(value = "마케팅 알림", example = "1")
    private String marketingPushAgree;
    @ApiModelProperty(value = "이벤트 알림", example = "1")
    private String eventPushAgree;
    @ApiModelProperty(value = "신상품 알림", example = "1")
    private String newGoodsPushAgree;

    @ApiModelProperty(value = "회원코드", example = "1", hidden = true)
    private String memberCd;
}
