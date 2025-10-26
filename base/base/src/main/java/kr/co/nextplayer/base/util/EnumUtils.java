package kr.co.nextplayer.base.util;

public class EnumUtils {

    public static boolean equals(Enum<?> v, String value) {
        return v.name().equals(value);
    }
}
