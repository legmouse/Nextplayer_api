package kr.co.nextplayer.base.util;

import java.util.UUID;

public class RandomUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
