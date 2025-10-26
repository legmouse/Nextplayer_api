package kr.co.nextplayer.base.community.response;

import kr.co.nextplayer.base.community.dto.RequestListDto;
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
public class RequestSearchResponse {
    private String ageGroup;
    private List<HashMap<String, Object>> searchList;
}
