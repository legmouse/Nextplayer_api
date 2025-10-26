package kr.co.nextplayer.base.front.service.test;

import kr.co.nextplayer.base.front.model.test.Test;
import kr.co.nextplayer.base.front.response.TestResponse;
import kr.co.nextplayer.base.front.mapper.test.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

    private final TestMapper testMapper;

    /**
     * 파라미터 넘어가는것까지 테스트
     * @param memberCd
     * @return
     */
    public TestResponse getTest(String memberCd) {
        Test testData = testMapper.getTestData(memberCd);
        TestResponse testResponse = TestResponse.builder()
            .memberCd(testData.getMemberCd())
            .test(testData.getTest())
            .build();

        return testResponse;
    }

    /**
     * 파라미터 넘어가는것까지 테스트 토큰 없이
     * @return
     */
    public TestResponse getTestNoToken() {
        Test testData = testMapper.getTestDataNoToken();
        TestResponse testResponse = TestResponse.builder()
            .memberCd(testData.getMemberCd())
            .test(testData.getTest())
            .build();

        return testResponse;
    }
}
