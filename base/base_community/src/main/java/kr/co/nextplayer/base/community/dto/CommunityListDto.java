package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class CommunityListDto {

    private String community_id;
    private String member_cd;
    private String title;
    private String content;
    private String type;
    private String sub_type;
    private String reg_date;
    private String reply_cnt;
    private String likes_cnt;
    private String view_cnt;

}
