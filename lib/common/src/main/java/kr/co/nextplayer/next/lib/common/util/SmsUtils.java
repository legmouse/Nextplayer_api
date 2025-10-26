package kr.co.nextplayer.next.lib.common.util;

public class SmsUtils {
    public static String makeLmsParameters(String to, String from, String text) throws Exception {

        // type - 메시지 타입 리턴 가능한 값 SMS - 영자 90 자(한글 45 자) 이하의 단문메시지를 발송합니다.
        String txt = text.replaceAll("\n",System.lineSeparator());

        int textSize = txt.getBytes("EUC-KR").length;

        boolean lmsSend = false;

        if(textSize >= 90) lmsSend = true;

        text=text.replaceAll("\n","\\\\r\\\\n");

        String parameters = "";
        parameters += "{\"message\":{\"to\":\"";
        parameters += to;
        parameters += "\",\"from\":\"";
        parameters += from;
        parameters += "\",\"text\":\"";
        parameters += text;
        if(lmsSend) {
            parameters += "\",\"type\":\"LMS\"";
        } else {
            parameters += "\",\"type\":\"SMS\"";
        }

        parameters += "}}";

        return parameters;
    }
}
