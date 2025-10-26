package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ModifyResultReqDto {

    private String memberCd;

    private String requestId;

    private String foreignParentId;
    private String foreignParentType;
    private String foreignId;
    private String foreignType;

    private String content;

    private String requestType;

    private List<String> fileId;

}
