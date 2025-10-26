package kr.co.nextplayer.base.community.response;

import kr.co.nextplayer.base.community.dto.SuggestListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestListResponse {
    private int totalCount;
    private List<SuggestListDto> suggestList;
}
