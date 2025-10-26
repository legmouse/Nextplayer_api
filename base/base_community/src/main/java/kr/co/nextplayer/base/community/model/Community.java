package kr.co.nextplayer.base.community.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Community {

    private String communityId;
    private String memberCd;
    private String title;
    private String content;
    private String type;
    private String subType;
    private String regDate;
    private String replyCnt;
    private String likesCnt;
    private String viewCnt;

    private List<String> fileId;
}
