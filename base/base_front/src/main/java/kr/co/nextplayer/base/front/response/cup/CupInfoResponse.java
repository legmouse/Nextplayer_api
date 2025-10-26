package kr.co.nextplayer.base.front.response.cup;

import kr.co.nextplayer.base.cup.dto.cup.CupInfoDto;
import kr.co.nextplayer.base.cup.dto.cup.TotalCupListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupInfoResponse {
    String ageGroup;
    CupInfoDto cupInfoMap;
    HashMap<String, Object> summaryData;
}
