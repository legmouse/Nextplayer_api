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
public class RequestReqDto {

    @Schema(description = "연령(ex: U18, U15, ...")
    private String ageGroup;

    private String parentType;

    private String parentId;


    private String foreignType;

    private String foreignId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "구분(ex: 경기 - 0, 팀 - 1, 기타 - 2")
    private String requestType;

    private List<String> fileId;

}
