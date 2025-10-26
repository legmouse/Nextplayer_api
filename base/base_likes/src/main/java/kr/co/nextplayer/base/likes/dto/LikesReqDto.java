package kr.co.nextplayer.base.likes.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class LikesReqDto {

    private String likesId;
    private String foreignId;
    private String foreignType;
    private String memberCd;

}
