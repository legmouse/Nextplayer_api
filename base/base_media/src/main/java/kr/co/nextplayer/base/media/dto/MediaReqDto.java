package kr.co.nextplayer.base.media.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MediaReqDto {

    private String mediaId;

    private String foreignId;

    private String creatorId;

    private String mediaType;
    private String sType;
    private String ageGroup;

    private String subType;

    private String sKeyword;
    private int curPage;
    private int pageSize;
    private int offset;

    private String submitDate;

    private Long viewCnt;

}
