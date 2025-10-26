package kr.co.nextplayer.base.backend.dto.joinkfaCraw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffDataDto {
    private String rank;
    private String staffName;

    private String homeAwayGbn;
    private int matchId;
}
