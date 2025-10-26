package kr.co.nextplayer.base.front.response.cup;

import kr.co.nextplayer.base.cup.dto.cup.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupMainMatchResponse {

    String ageGroup;
    CupInfoDto cupInfoMap;
    List<CupTeamDto> cupMainTeamList;
    List<CupResultDto> cupSubRankList;
    List<CupMatchCalendarDto> cupSubMatchCalendar;
    List<CupMainMatchDto> cupSubMatchList;

}
