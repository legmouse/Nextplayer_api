package kr.co.nextplayer.base.community.model;

import kr.co.nextplayer.base.media.model.MediaFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Request {

    private String requestId;
    private String content;

    private String parentId;
    private String parentType;
    private String foreignId;
    private String foreignType;

    private String childForeignId;
    private String childForeignType;

    private String answer;

    private String requestType;

    private String useFlag;
    private String regDate;
    private String answerDate;
    private String answerFlag;
    private String secretFlag;

    private Member member;

    private List<MediaFile> mediaFile = new ArrayList<MediaFile>();
    private List<PublicFile> answerImg = new ArrayList<PublicFile>();
}
