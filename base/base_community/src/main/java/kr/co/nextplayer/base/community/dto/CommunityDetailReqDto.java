package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class CommunityDetailReqDto {

    private String communityId;
    private String memberCd;

    private String sKeyword;
    private String orderType;
    private String type;
    private String subType;

    private int curPage;
    private int sRow;
    private int eCount;

}
