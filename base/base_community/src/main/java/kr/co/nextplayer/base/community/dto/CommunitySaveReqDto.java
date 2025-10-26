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
@Schema(description = "커뮤니티 등록/수정 dto")
public class CommunitySaveReqDto {

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "커뮤티니 대분류 0: 블라인드 (추가 예정)")
    private String type;

    @Schema(description = "커뮤티니 소분류 0: 문의 (추가 예정)")
    private String subType;

    @Schema(description = "이미지 업로드 id 목록")
    private List<String> fileId;

}
