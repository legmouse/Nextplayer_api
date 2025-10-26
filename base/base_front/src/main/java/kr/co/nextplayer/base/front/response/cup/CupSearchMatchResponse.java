package kr.co.nextplayer.base.front.response.cup;

import kr.co.nextplayer.base.cup.dto.cup.CupInfoDto;
import kr.co.nextplayer.base.cup.dto.cup.CupInfoListDto;
import kr.co.nextplayer.base.cup.dto.cup.CupSearchDto;
import kr.co.nextplayer.base.cup.dto.cup.CupTourMatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupSearchMatchResponse {

    String ageGroup;

    List<CupSearchDto> cupSearchList;
    List<CupInfoListDto> cupInfoList;
}
