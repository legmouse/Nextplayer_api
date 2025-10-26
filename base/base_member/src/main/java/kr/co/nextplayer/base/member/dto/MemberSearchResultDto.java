package kr.co.nextplayer.base.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import kr.co.nextplayer.base.member.model.MemberSns;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MemberSearchResultDto {

    @ApiModelProperty(value = "회원번호", required = true, example = "123456789")
    private String memberCd;

    @ApiModelProperty(value = "등록/가입일자", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;

    @ApiModelProperty(value = "관리자 수정자 ID", hidden = true)
    private String updAdminId;

    @ApiModelProperty(value = "수정일자", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updDate;

    @ApiModelProperty(value = "관리자 수정일자", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updAdminDate;

    @ApiModelProperty(value = "회원 상태(0:탈퇴,1:일반,3:차단,4:휴먼,5:삭제,null:오프라인회원)", example = "1")
    private String memberState;

    @ApiModelProperty(value = "회원 sns 정보")
    private List<MemberSns> memberSns;

}
