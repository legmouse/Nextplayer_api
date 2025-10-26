package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.cup.dto.cup.TotalCupListDto;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.model.Uage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupResponse {

    String ageGroup;

    List<HashMap<String, Uage>> uageList;
    List<TotalCupListDto> totalCupList;

    List<Cup> cupSubTeamList;
    List<Cup> cupSubRankList;
    List<Cup> cupSubMatchCalendar;
    List<Cup> cupSubMatchList;

    List<Cup> cupMainTeamList;
    List<Cup> cupMainRankList;
    List<Cup> cupMainMatchCalendar;
    List<Cup> cupMainMatchList;

    List<Cup> cupTourEmptyMatchList;
    List<Cup> cupTourMatchList;

    boolean tourType;
    String round;

    Cup cupInfoMap;

}
