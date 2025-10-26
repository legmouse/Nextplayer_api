package kr.co.nextplayer.base.util;

import java.util.regex.Pattern;

/**
 * 검증 유틸
 */
public class ValidateUtils {

    /**
     * 휴대폰 정규식 체크
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        return Pattern.matches("^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", value);
    }

    /**
     * 이메일 정규식 체크
     *
     * @param value
     */
    public static boolean isEmail(String value) {
        return Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", value);
    }
}
