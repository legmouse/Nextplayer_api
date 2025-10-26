package kr.co.nextplayer.base.reply.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reply {

    private String replyId;
    private String communityId;
    private String memberCd;
    private String content;
    private String parentId;
    private String bestFlag;
    private String useFlag;
    private String likesCnt;
    private String regDate;

    private List<Reply> replies;
    //private List<Integer> likeCnt;
    private int likeCnt;

}
