package kr.co.nextplayer.base.front.response;

import kr.co.nextplayer.base.cup.dto.cup.BannerCupListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupInfoListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSubMatchDto;
import kr.co.nextplayer.base.cup.model.Cup;
import kr.co.nextplayer.base.front.model.Uage;
import kr.co.nextplayer.base.league.dto.BannerLeagueListDto;
import kr.co.nextplayer.base.league.dto.LeagueWinnerListDto;
import kr.co.nextplayer.base.league.model.League;
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
public class HomeResponse {

    List<HashMap<String, Object>> uageList;
    List<BannerCupListDto> totalCupList;
    List<BannerLeagueListDto> totalLeagueList;

}
