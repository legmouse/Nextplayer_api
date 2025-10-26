package kr.co.nextplayer.base.media.model;

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
public class Creator implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "번호")
    private String creatorId;

    @ApiModelProperty(value = "크리에이터 이름")
    private String creatorName;

    @ApiModelProperty(value = "링크")
    private String creatorLink;

    @ApiModelProperty(value = "채널 ID")
    private String channelId;

    private String profileImg;
    private String bannerImg;

    private List<CreatorFileModel> files = new ArrayList<CreatorFileModel>();

}
