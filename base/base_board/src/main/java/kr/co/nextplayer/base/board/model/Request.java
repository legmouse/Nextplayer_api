package kr.co.nextplayer.base.board.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String requestId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "")
    private String foreignParentId;

    @ApiModelProperty(value = "")
    private String foreignParentType;

    @ApiModelProperty(value = "")
    private String foreignId;

    @ApiModelProperty(value = "")
    private String foreignType;

    @ApiModelProperty(value = "요청 타입")
    private String requestType;

    @ApiModelProperty(value = "답변 플래그")
    private int answerFlag;

    @ApiModelProperty(value = "답변")
    private String answer;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime regDate;

    @ApiModelProperty(value = "답변 등록일")
    private LocalDateTime answerDate;
}
