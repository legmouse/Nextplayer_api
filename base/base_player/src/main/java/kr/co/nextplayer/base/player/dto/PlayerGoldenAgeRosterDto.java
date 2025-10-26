package kr.co.nextplayer.base.player.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Builder
@Getter
@Setter
@ApiModel
public class PlayerGoldenAgeRosterDto {

    private String year;
    private int local_cnt;
    private int fiveArea_cnt;
    private int allArea_cnt;
    private int futureArea_cnt;

    private int giftedArea_cnt;
    private int eliteArea_cnt;

    public PlayerGoldenAgeRosterDto(String year, int local_cnt, int fiveAreaCnt, int allAreaCnt, int giftedAreaCnt, int futureAreaCnt, int eliteAreaCnt) {

        this.year = year;
        this.local_cnt = local_cnt;
        this.fiveArea_cnt = fiveAreaCnt;
        this.allArea_cnt = allAreaCnt;
        this.futureArea_cnt = futureAreaCnt;
        this.eliteArea_cnt = eliteAreaCnt;
        this.giftedArea_cnt = giftedAreaCnt;
    }

}
