package kr.co.nextplayer.base.front.response.board;

import kr.co.nextplayer.base.board.model.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerListResponse {

    private List<Banner> bannerList;

}
