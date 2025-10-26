package kr.co.nextplayer.base.community.response;

import kr.co.nextplayer.base.community.dto.CommunityListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityListResponse {
    private int totalCount;
    private List<CommunityListDto> communityList;
}
