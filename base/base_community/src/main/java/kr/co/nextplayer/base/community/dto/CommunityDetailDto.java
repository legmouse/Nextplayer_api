package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class CommunityDetailDto {

    private String community_id;
    private String member_cd;
    private String title;
    private String content;
    private String type;
    private String sub_type;
    private String likes_cnt;
    private String reg_date;

    private boolean isLiked;

    private List<String> files;

}
