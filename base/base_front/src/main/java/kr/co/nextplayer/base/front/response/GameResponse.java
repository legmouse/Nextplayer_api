package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.cup.dto.cup.BannerCupListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupInfoListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSearchDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.LeagueInfoDto;
import kr.co.nextplayer.base.league.dto.LeagueSearchMatchDto;
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
public class GameResponse {

    String ageGroup;

    List<CupSearchDto> cupSearchList;
    List<LeagueSearchMatchDto> leagueSearchList;

    List<CupInfoListDto> cupInfoList;
    List<LeagueInfoDto> leagueInfoList;
}
