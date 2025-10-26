package kr.co.nextplayer.base.cup.dto.cup;

import kr.co.nextplayer.base.team.dto.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CupPlayDataDto {

    private List<HomeAwayMatchPlayDataDto> homeAwayMatchPlayData;
    private List<HomeAwayMatchChangeDataDto> homeAwayMatchChangeData;
    private List<HomeAwayStaffDataDto> homeAwayStaffData;
    private List<HomeAwayStaffPenaltyDataDto> homeAwayStaffPenaltyData;
    private List<HomeAwayOwnGoalDataDto> homeAwayOwnGoalData;

}
