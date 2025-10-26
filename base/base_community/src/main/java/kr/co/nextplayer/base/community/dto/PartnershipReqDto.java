package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PartnershipReqDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "이름")
    private String customer;

    @Schema(description = "휴대폰 번호")
    private String phone;

    @Schema(description = "내용")
    private String content;

    private List<String> fileId;

}
