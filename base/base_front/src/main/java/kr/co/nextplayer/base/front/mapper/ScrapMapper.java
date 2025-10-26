package kr.co.nextplayer.base.front.mapper;

import kr.co.nextplayer.base.front.dto.ScrapReqDto;
import kr.co.nextplayer.base.front.model.Scrap;

import java.util.List;

public interface ScrapMapper {

    int insertScrap(ScrapReqDto scrapReqDto);

    int deleteScrap(ScrapReqDto scrapReqDto);

    int deleteScrapOverlap(ScrapReqDto scrapReqDto);

    List<Scrap> selectScrap(ScrapReqDto scrapReqDto);

}
