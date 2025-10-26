package kr.co.nextplayer.base.util;

import kr.co.nextplayer.base.exception.BaseValidateException;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Base Assert
 */
public final class BaseAssert extends Assert {

    private final static int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BaseValidateException(message);
        }
    }

    public static void isUse(Object object, String message) {
        if (object != null) {
            throw new BaseValidateException(message);
        }
    }

    public static void isTrue(boolean value, String message) {
        if (!value) {
            throw new BaseValidateException(message);
        }
    }

    public static void hasLength(String value, String message) {
        if (value == null || value.length() == 0) {
            throw new BaseValidateException(message);
        }
    }


    public static void isEmpty(List<?> values, String message) {
        if (values == null || values.size() == 0) {
            throw new BaseValidateException(message);
        }
    }

    /**
     * 사업자등록번호 검증
     *
     * @param value
     * @param message
     */
    public static void isTrueBusinessNumber(String value, String message) {
        isTrue(isValidBusinessNumber(value), message);
    }


    private static boolean isValidBusinessNumber(String regNum) {

        if (!isNumeric(regNum) || regNum.length() != 10)
            return false;

        int sum = 0;
        int j = -1;
        for (int i = 0; i < 9; i++) {
            j = Character.getNumericValue(regNum.charAt(i));
            sum += j * LOGIC_NUM[i];
        }

        sum += (int) (Character.getNumericValue(regNum.charAt(8)) * 5 / 10);

        int checkNum = (10 - sum % 10) % 10;
        return (checkNum == Character.getNumericValue(regNum.charAt(9)));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 이메일 정규식 체크
     *
     * @param value
     * @param message
     */
    public static void isTrueEmail(String value, String message) {
        isTrue(ValidateUtils.isEmail(value), message);
    }

    /**
     * 휴대폰번호 정규식 체크
     *
     * @param value
     * @param message
     */
    public static void isTruePhoneNumber(String value, String message) {
        isTrue(ValidateUtils.isPhone(value), message);
    }

}