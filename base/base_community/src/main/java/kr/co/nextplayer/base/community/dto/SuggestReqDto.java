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
public class SuggestReqDto {

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "공개 / 비공개 여부 0 - 공개, 1 - 비공개")
    private String secretFlag;

    private List<String> fileId;
}
