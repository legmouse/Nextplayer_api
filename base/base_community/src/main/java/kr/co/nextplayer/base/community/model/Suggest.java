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
public class Suggest {

    private String suggestId;
    private String title;
    private String content;

    private String answer;
    private String answerFlag;
    private String answerDate;

    private String suggestType;

    private String useFlag;
    private String regDate;

    private String secretFlag;

    private Member member;

    private List<MediaFile> mediaFile = new ArrayList<MediaFile>();
    private List<PublicFile> answerImg = new ArrayList<PublicFile>();
}
