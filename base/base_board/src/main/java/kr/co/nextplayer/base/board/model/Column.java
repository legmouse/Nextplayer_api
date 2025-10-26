package kr.co.nextplayer.base.board.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Column implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String educationColumnId;

    @ApiModelProperty(value = "제목")
    private String title;

    private String writer;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "요약")
    private String summary;

    private String columnType;

    @ApiModelProperty(value = "노출 여부")
    private String showFlag;

    @ApiModelProperty(value = "등록일")
    private String regDate;

    private String submitDate;

    private String urlLink;

    private String useFlag;

    private String mType;

    private List<PublicFile> imgFile = new ArrayList<PublicFile>();
}
