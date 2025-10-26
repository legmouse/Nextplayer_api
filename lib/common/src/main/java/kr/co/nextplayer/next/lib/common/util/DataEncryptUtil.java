package kr.co.nextplayer.next.lib.common.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataEncryptUtil {

    public static String hideStartInfo(int num, String value) {
        if (value == null || value.length() <= num) {
            return hideAll(value);
        }
        char[] arr = value.toCharArray();
        Arrays.fill(arr, 0, arr.length - num, '*');
        return new String(arr);
    }

    public static String hideEndInfo(int num, String value) {
        if (value == null || value.length() <= num) {
            return hideAll(value);
        }
        char[] arr = value.toCharArray();
        Arrays.fill(arr, arr.length - num, arr.length, '*');
        return new String(arr);
    }

    public static String hideAfterInfo(int num, String value) {
        if (value == null || value.length() <= num) {
            return hideAll(value);
        }
        char[] arr = value.toCharArray();
        Arrays.fill(arr, num, arr.length, '*');
        return new String(arr);
    }

    public static String hideMiddleInfo(String value) {
        if (value == null || value.length() <= 2) {
            return hideEndInfo(1, value);
        }
        return value.replaceAll("(?<=.{1}).(?=.{1})", "*");
    }

    public static String hidePhoneInfo(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() <= 7) {
            return value.replaceAll("(?<=\\_)[0-9]+(?=\\_)", "*");
        }

        if (value.indexOf("-") < 0) {
            return value.replaceAll("(?<=.{3}).(?=.{3})", "*");
        } else {
            String newStr = "";
            int lastIndex = value.lastIndexOf("-");
            int first = value.indexOf("-");
            if(lastIndex != first){
                char[] arr = value.toCharArray();
                Arrays.fill(arr, (first+1), lastIndex, '*');
                newStr = new String(arr);
            } else {
                newStr = value.replaceAll("(?<=.{3}).(?=.{3})", "*");
            }

            return newStr;
        }
    }

    public static String hideEmailInfo(String value) {
        if (value == null) {
            return null;
        }

        int index = value.indexOf("@");
        if (index < 0) {
            return value.replaceAll("[0-9]{3}(?=\\@)", "***");
        } else {
            char[] arr = value.toCharArray();
            if(index>3){
                Arrays.fill(arr, (index-3), index, '*');
                return new String(arr);
            } else {
                Arrays.fill(arr, (index-1), index, '*');
                return new String(arr);
            }
        }
    }

    public static String hideAddressInfo(String value) {
        if (value == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("[0-9]").matcher(value);
        if (matcher.find()) {
            return value.substring(0, matcher.start());
        }
        return value;
    }

    public static String hideNum(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("[0-9]", "*");
    }

    public static String hideAll(String value) {
        if (value == null) {
            return null;
        }
        char[] arr = value.toCharArray();
        Arrays.fill(arr, '*');
        return new String(arr);
    }

//    public static void main(String[] args) {
//        System.out.println(">>>>: " + DataEncryptUtil.hidePhoneInfo("01030898528"));
//        System.out.println(">>>>: " + DataEncryptUtil.hidePhoneInfo("010-7107-7891"));
//        System.out.println(">>>>: " + DataEncryptUtil.hideEmailInfo("stdq_wang@naver.com3"));
//    }

}
