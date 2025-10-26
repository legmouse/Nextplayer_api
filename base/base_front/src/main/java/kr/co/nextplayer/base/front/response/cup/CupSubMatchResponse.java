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
public class CupSubMatchResponse {

    String ageGroup;
    CupInfoDto cupInfoMap;
    List<CupTeamDto> cupSubTeamList;
    List<CupResultDto> cupSubRankList;
    List<CupMatchCalendarDto> cupSubMatchCalendar;
    List<CupSubMatchDto> cupSubMatchList;
}
