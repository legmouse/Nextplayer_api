package kr.co.nextplayer.base.front.mapper.test;

import kr.co.nextplayer.base.front.model.test.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    Test getTestData(@Param("memberCd") String memberCd);
    Test getTestDataNoToken();

}
