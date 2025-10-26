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
public class CommunityUpdateReqDto {

    private String title;
    private String content;
    private String subType;
    private String memberCd;
    private String communityId;

    private String foreignType;

    private List<String> fileId;
    private List<String> deleteFileId;

}
