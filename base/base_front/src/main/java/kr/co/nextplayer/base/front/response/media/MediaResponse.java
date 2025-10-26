package kr.co.nextplayer.base.front.response.media;

import kr.co.nextplayer.base.media.dto.MediaDto;
import kr.co.nextplayer.base.media.model.Creator;
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
public class MediaResponse {

    int total_count;
    List<MediaDto> mediaList;
    List<Creator> creatorList;
    List<HashMap<String, Object>> menuList;

}
