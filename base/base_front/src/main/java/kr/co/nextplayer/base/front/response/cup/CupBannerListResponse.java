package kr.co.nextplayer.base.front.response.cup;

import kr.co.nextplayer.base.cup.dto.cup.BannerCupListDto;
import kr.co.nextplayer.base.cup.dto.cup.TotalCupListDto;
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
public class CupBannerListResponse {

    List<HashMap<String, Uage>> uageList;
    List<BannerCupListDto> totalCupList;
}
