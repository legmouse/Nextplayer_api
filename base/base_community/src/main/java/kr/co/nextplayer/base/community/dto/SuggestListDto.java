package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.community.model.Member;
import kr.co.nextplayer.base.community.model.PublicFile;
import kr.co.nextplayer.base.media.model.MediaFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class SuggestListDto {

    private String suggestId;

    private String title;
    private String content;

    private String answer;
    private String answerFlag;
    private String answerDate;

    private String secretFlag;

    private String regDate;

    private String sKeyword;
    private String sType;
    private int curPage;
    private int sRow;
    private int eCount;

    private String memberCd;
    private String memberId;
    private String memberNickname;

    private String openFlag;

    private List<MediaFile> mediaFile;
    private List<PublicFile> answerImg = new ArrayList<PublicFile>();
}
