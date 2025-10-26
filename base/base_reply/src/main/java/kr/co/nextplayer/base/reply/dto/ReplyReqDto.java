package kr.co.nextplayer.base.reply.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ReplyReqDto {
    private String memberCd;
    private String communityId;
    private String replyId;
    private String content;
    private String parentId;
}
