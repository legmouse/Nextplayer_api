package kr.co.nextplayer.base.batch.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffPenaltyDataDto {
    private String time;
    private String rank;
    private String staffName;
    private String penaltyType;

    private String homeAwayGbn;
    private int matchId;

}
