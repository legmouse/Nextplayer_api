package kr.co.nextplayer.base.community.response;

import kr.co.nextplayer.base.community.dto.CommunityListDto;
import kr.co.nextplayer.base.community.dto.RequestListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestListResponse {
    private int totalCount;
    private List<RequestListDto> requestList;
}
