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
public class CommunityReqDto {

    private String memberCd;
    private String title;
    private String content;
    private String type;
    private String subType;

    private String foreignId;
    private String foreignType;

    private List<String> fileId;

}
