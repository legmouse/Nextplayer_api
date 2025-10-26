package kr.co.nextplayer.base.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MemberRegisterDto {

	@ApiModelProperty(value = "회원번호", required = true, example = "123456789")
	private String memberCd;

	@ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,null:오프라인회원)", example = "1")
	private String memberState;

}
