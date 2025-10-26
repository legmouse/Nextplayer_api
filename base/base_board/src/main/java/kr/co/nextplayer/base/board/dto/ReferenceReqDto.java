package kr.co.nextplayer.base.board.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ReferenceReqDto {

    private String referenceId;
    private String title;
    private String content;
    private String regDate;

    private int curPage;
    private int pageSize;
    private int offset;
    private String sKeyword;

}
