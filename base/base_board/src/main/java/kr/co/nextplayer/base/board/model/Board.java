package kr.co.nextplayer.base.board.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.file.model.MediaFile;
import kr.co.nextplayer.base.file.model.PublicFileModel;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String boardId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "타입")
    private int type;

    @ApiModelProperty(value = "답변 플래그")
    private int answerFlag;

    @ApiModelProperty(value = "답변")
    private String answer;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime regDate;

    @ApiModelProperty(value = "답변 등록일")
    private LocalDateTime answerDate;

    private String useFlag;

    private List<MediaFile> imgFile = new ArrayList<MediaFile>();
    private List<PublicFile> answerImg = new ArrayList<PublicFile>();
}
