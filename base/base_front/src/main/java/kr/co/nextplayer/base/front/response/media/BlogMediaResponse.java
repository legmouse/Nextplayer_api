package kr.co.nextplayer.base.front.response.media;

import kr.co.nextplayer.base.media.dto.MediaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogMediaResponse {

    List<MediaDto> blogMediaList;

}
