//package kr.co.nextplayer.next.lib.common.operation;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("alpha")
//public class RedisOperationTest {
//
//    @Autowired
//    private RedisOperation redisOperation;
//
//    @Test
//    public void testGet() throws Exception {
//        Object o = redisOperation.get("test2");
//        System.out.println(o);
//    }
//
//    @Test
//    public void testSet() throws Exception {
//        boolean b = redisOperation.set("tt", "dd");
//        System.out.println(b);
//    }
//
//    @Test
//    public void testRemove() throws Exception {
//        redisOperation.del("a", "ppp");
//    }
//
//    @Test
//    public void testSetAndExpire() throws Exception {
//        redisOperation.set("test333", "test333", 10);
//    }
//
//    @Test
//    public void testHSet() throws Exception {
//        redisOperation.setValueOfHashKey("a", "aa", "aaa");
//    }
//}