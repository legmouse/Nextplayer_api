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
public class ReportReqDto {

    @Schema(description = "참조 타입(ex: 커뮤니티 - Community, 댓글 - Reply")
    private String foreignType;

    @Schema(description = "참조 id")
    private String foreignId;

    @Schema(description = "사유")
    private String reason;

}
