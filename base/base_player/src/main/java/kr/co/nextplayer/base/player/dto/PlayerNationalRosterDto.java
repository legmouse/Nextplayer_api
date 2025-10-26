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
public class PlayerNationalRosterDto {

    private String year;
    private int enter_cnt;

    public PlayerNationalRosterDto(String year, int enterCnt) {

        this.year = year;
        this.enter_cnt = enterCnt;

    }

}
