package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PlayerReqDto {

    private String rosterId;
    private String playerId;

    private String teamId;
    private String ageGroup;
    private String sYear;
    private String uage;
    private String rosterType;
    private String type;

    private int curPage;
    private int pageSize;
    private int offset;
    private int sRow;
    private int eCount;

    private String sKeyword;
}
