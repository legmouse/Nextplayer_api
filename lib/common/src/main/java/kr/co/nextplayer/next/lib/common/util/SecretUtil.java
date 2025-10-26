package kr.co.nextplayer.next.lib.common.util;

import org.springframework.util.DigestUtils;

public class SecretUtil {


    public static String generateUserPassWord(String passWord) {
        //암호화 하기
        String md5Password = DigestUtils.md5DigestAsHex(passWord.getBytes());
        return md5Password;
    }

    public static String parseUserPassWord(String passWord) {

        //TODO: 암호화 풀기

        return passWord;
    }

    public static String parseSecretValue(String value) {

        //TODO: 암호화 풀기

        return value;
    }

    public static String generateSecretValue(String value) {

        //TODO: 암호화 하기

        return value;
    }

//    public static void main(String[] args) {
//        System.out.println(" clientID : " + generateUserPassWord("nextplayer20230101clinetid"));
//    }


}
