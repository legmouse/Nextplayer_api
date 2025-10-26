package kr.co.nextplayer.base.batch.mapper;

import kr.co.nextplayer.base.batch.dto.CupCrawMatchList;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CrawMapper {
    List<CupCrawMatchList> selectCrawMatchList(String age);
    List<CupCrawMatchList> selectCrawMatchDataList(String age);

    void updateAfterEndCnt(HashMap<String, Object> map);

}
