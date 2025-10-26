package kr.co.nextplayer.base.media.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class GameReqDto {

    private String mediaId;

    private String foreignId;

    private String creatorId;

    private String mediaType;
    private String sType;
    private String ageGroup;

    private String subType;
    private String gameType;

    private String sDate;
    private String sKeyword;
    private int curPage;
    private int pageSize;
    private int offset;

    private int sRow;
    private int eCount;

    private String submitDate;

    private Long viewCnt;

    private String infoTB;
    private String teamTB;

    private String cupSubMatchTB;
    private String cupMainMatchTB;
    private String cupTourMatchTB;

    private String leagueMatchTB;

}
