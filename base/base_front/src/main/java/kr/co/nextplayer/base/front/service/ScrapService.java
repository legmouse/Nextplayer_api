package kr.co.nextplayer.base.front.service;

import kr.co.nextplayer.base.front.dto.ScrapReqDto;
import kr.co.nextplayer.base.front.mapper.ScrapMapper;
import kr.co.nextplayer.base.front.model.Scrap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ScrapService {

    @Resource
    private ScrapMapper scrapMapper;

    public int insertScrap(ScrapReqDto scrapReqDto) {
        scrapMapper.deleteScrapOverlap(scrapReqDto);
        return scrapMapper.insertScrap(scrapReqDto);
    }

    public int deleteScrap(ScrapReqDto scrapReqDto) {
        return scrapMapper.deleteScrap(scrapReqDto);
    }

    public List<Scrap> selectScrap(ScrapReqDto scrapReqDto){
        return scrapMapper.selectScrap(scrapReqDto);
    }

}
