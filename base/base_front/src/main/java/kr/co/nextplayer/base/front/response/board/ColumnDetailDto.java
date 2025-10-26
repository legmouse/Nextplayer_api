package kr.co.nextplayer.base.front.response.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.board.model.Column;
import kr.co.nextplayer.base.board.model.Faq;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ColumnDetailDto {

    private String educationColumnId;

    private String title;

    private String writer;

    private String content;

    private String summary;

    private String columnType;

    private String showFlag;

    private String regDate;

    private String submitDate;

    private String urlLink;

    private String useFlag;

    private Column prevColumn;
    private Column nextColumn;
}
