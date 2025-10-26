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
public class MemberInactive implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "회원번호")
    private String memberCd;

    @ApiModelProperty(value = "휴면구분(휴면:0,탈퇴:1,삭제:2)")
    private String inactiveType;

    @ApiModelProperty(value = "사유 코드(직접입력:0,기타...)")
    private String reasonCd;

    @ApiModelProperty(value = "사유 내용")
    private String reasonContent;

    @ApiModelProperty(value = "휴면 등록 일시")
    private LocalDateTime inactiveDate;

    @ApiModelProperty(value = "휴면 등록자 구분(1:본인,2:운영자,3:배치작업)")
    private String inactiveUserType;

    @ApiModelProperty(value = "휴면 등록자 번호")
    private String inactiveUserCd;



    @ApiModelProperty(value = "회원번호(SAP코드)")
    private String ZBCNR;

    @ApiModelProperty(value = "아이디", required = true, example = "ID123456789")
    private String memberId;

    @ApiModelProperty(value = "비밀번호", required = true, example = "Pwd123456789")
    private String memberPwd;

    @ApiModelProperty(value = "등록/가입일자", hidden = true)
    private LocalDateTime regDate;

    @ApiModelProperty(value = "관리자 수정자 ID", hidden = true)
    private String updAdminId;

    @ApiModelProperty(value = "수정일자", hidden = true)
    private LocalDateTime updDate;

    @ApiModelProperty(value = "회원구분(1:북클럽(개인),2:어린이,3:해외거주,6:미승인,7:일반기업,8:계열사,9:대량구매 기업)", required = true, example = "1")
    private String memberTypeCd;

    @ApiModelProperty(value = "카드발행여부(미발행:0,발행:1)", required = true, example = "0")
    private String cardFlag;

    @ApiModelProperty(value = "회원등급(A:일반,B:골드,C:플래티넘)", required = true, example = "A")
    private String memberGrade;

    @ApiModelProperty(value = "마일리지 적립여부(미적립:0,적립:1)", required = true, example = "1")
    private String mileageFlag;

    @ApiModelProperty(value = "인터넷인증 여부 (온라인 인증 여부)", required = true, example = "1")
    private String onlineCertFlag;

    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,null:오프라인회원)", example = "1")
    private String memberState;

    @ApiModelProperty(value = "회원 가입구분(on line / off line)", example = "1")
    private String memberRegFlag;

    @ApiModelProperty(value = "법인회원구분(0:개인,1:기업)", required = true, example = "0")
    private String coFlag;

    @ApiModelProperty(value = "dm 발송구분(1:동의,0:미동의)")
    private String dmFlag;

    @ApiModelProperty(value = "개인정보 유효기간(탈퇴시까지:-1, 기간(년):1~99)")
    private String infoExpiryYear;


}