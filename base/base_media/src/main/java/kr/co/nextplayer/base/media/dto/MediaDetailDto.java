package kr.co.nextplayer.base.media.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.nextplayer.base.media.model.Creator;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class MediaDetailDto {

    @ApiModelProperty(value = "번호")
    private String media_id;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "출처")
    private String source;

    @ApiModelProperty(value = "요약")
    private String summary;

    @ApiModelProperty(value = "링크")
    private String url_link;

    @ApiModelProperty(value = "미디어 타입")
    private String media_type;

    @ApiModelProperty(value = "종류")
    private String type;

    private String video_type;

    private String img_url;

    private List<HashMap<String, Object>> cup_tag;
    private List<HashMap<String, Object>> league_tag;
    private List<HashMap<String, Object>> team_tag;

    private List<HashMap<String, Object>> cup_match;
    private List<HashMap<String, Object>> league_match;

    private String reg_date;
    private String submit_date;

    private Creator creator;

    private String showFlag;

}
