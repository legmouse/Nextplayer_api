package kr.co.nextplayer.base.reply.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import kr.co.nextplayer.base.reply.model.Reply;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class ReplyDto {

    private String member_cd;
    private String community_id;
    private String reply_id;
    private String content;
    private String parent_id;
    //private String likes_cnt;
    private String reg_date;
    private String use_flag;

    private List<Reply> replies;
    //private List<Integer> like_cnt;
    private int likes_cnt;

}
