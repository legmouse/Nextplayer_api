package kr.co.nextplayer.base.community.dto;

import io.swagger.annotations.ApiModel;
import kr.co.nextplayer.base.community.model.PublicFile;
import kr.co.nextplayer.base.media.model.MediaFile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class RequestListDto {

    private String requestId;

    private String parentId;
    private String parentType;
    private String foreignId;
    private String foreignType;

    private String requestType;

    private String content;
    private String answer;

    private String useFlag;
    private String answerFlag;
    private String secretFlag;

    private String regDate;
    private String answerDate;

    private Map foreignInfo;

    private Map matchData;

    private String sKeyword;
    private String sType;
    private int curPage;
    private int sRow;
    private int eCount;

    private String memberCd;
    private String memberId;
    private String memberNickname;

    private List<MediaFile> mediaFile;
    private List<PublicFile> answerImg = new ArrayList<PublicFile>();
}
