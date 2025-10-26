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
public class BoardReqDto {

    private String memberCd;
    private String boardId;
    private String title;
    private String content;
    private String type;
    private String boardType;

    private String sKeyword;
    private int curPage;

    private int pageSize;
    private int offset;

    private List<String> fileId;

}
