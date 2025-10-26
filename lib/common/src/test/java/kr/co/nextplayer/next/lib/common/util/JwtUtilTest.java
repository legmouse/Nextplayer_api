//package kr.co.nextplayer.next.lib.common.util;
//
//import kr.co.nextplayer.next.lib.common.property.JwtProperty;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ActiveProfiles("alpha")
//public class JwtUtilTest {
//
//    @Test
//    public void generateAndParseTokenTest() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("key1", "value1");
//        map.put("key2", "value2");
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        JwtProperty jwtProperty = new JwtProperty();
//        jwtProperty.setSecretKey("YY0lsEzI+JiRWERJs9ScgUuZTcaSWldasuYm9sBCfe8=");
//        String token = JwtUtil.generateToken(map, 5, request, jwtProperty);
//
//        Map<String, Object> result = JwtUtil.parseToken(token, request, jwtProperty);
//        if (result != null) {
//            assertEquals("value1", result.get("key1"));
//            assertEquals("value2", result.get("key2"));
//        }
//    }
//
//}
//
