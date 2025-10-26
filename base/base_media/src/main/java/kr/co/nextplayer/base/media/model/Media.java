package kr.co.nextplayer.base.media.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String mediaId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "출처")
    private String source;

    @ApiModelProperty(value = "요약")
    private String summary;

    @ApiModelProperty(value = "링크")
    private String urlLink;

    @ApiModelProperty(value = "미디어 타입")
    private String mediaType;

    @ApiModelProperty(value = "종류")
    private String type;

    private String videoType;

    private String submitDate;
    private String regDate;

    private String parentTable;
    private String parentId;
    private String childTable;
    private String childId;

    private String imgUrl;
    private String refUrl;

    private Creator creator;

    private Long viewCnt;

    private String subType;

    private String showFlag;

    private String mType;

    private String mediaOrder;

}
