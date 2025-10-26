package kr.co.nextplayer.base.board.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.file.model.MediaFile;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String educationId;

    @ApiModelProperty(value = "상품 번호")
    private String goodsId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "요약")
    private String summary;

    @ApiModelProperty(value = "시간")
    private String playTime;

    @ApiModelProperty(value = "노출 여부")
    private String showFlag;

    @ApiModelProperty(value = "권한 여부")
    private String isChecked;

    @ApiModelProperty(value = "등록일")
    private String regDate;

    private String urlLink;
    private String previewLink;
    private String moveUrl;

    private String useFlag;

    private List<PublicFile> imgFile = new ArrayList<PublicFile>();
    private List<Faq> faqList = new ArrayList<Faq>();

}
