package kr.co.nextplayer.next.lib.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }


    /**
     * 다운로드시 한글 깨짐 방지 처리
     */

    public static final String FIREFOX = "Firefox";
    public static final String SAFARI = "Safari";
    public static final String CHROME = "Chrome";
    public static final String OPERA = "Opera";
    public static final String MSIE = "MSIE";
    public static final String EDGE = "Edge";
    public static final String OTHER = "Other";

    public static final String TYPEKEY = "type";
    public static final String VERSIONKEY = "version";

    public static String getDisposition(String filename, String userAgent, String charSet) throws Exception {

        String encodedFilename = null;
        HashMap<String, String> result = WebUtils.getBrowser(userAgent);
        float version = Float.parseFloat(result.get(WebUtils.VERSIONKEY));
        //System.out.println("=====>>>>> browser type = "+result.get(TYPEKEY));
        //System.out.println("=====>>>>> browser version = "+result.get(VERSIONKEY));
        //System.out.println("=====>>>>> filename = "+filename);

        if (WebUtils.MSIE.equals(result.get(WebUtils.TYPEKEY)) && version <= 8.0f) {
            encodedFilename = "Content-Disposition: attachment; filename=" + URLEncoder.encode(filename, charSet).replaceAll("\\+", "%20");
        } else if (WebUtils.OTHER.equals(result.get(WebUtils.TYPEKEY))) {
            throw new RuntimeException("Not supported browser");
        } else {
            encodedFilename = "attachment; filename*=" + charSet + "''" + URLEncoder.encode(filename, charSet);
        }

        return encodedFilename;
    }

    public static HashMap<String, String> getBrowser(String userAgent) {

        HashMap<String, String> result = new HashMap<String, String>();
        Pattern pattern = null;
        Matcher matcher = null;
        //System.out.println("=====>>>>> userAgent = "+userAgent);

        pattern = Pattern.compile("MSIE ([0-9]{1,2}.[0-9])");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, MSIE);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        if (userAgent.indexOf("Trident/7.0") > -1) {
            result.put(TYPEKEY, MSIE);
            result.put(VERSIONKEY, "11.0");
            return result;
        }

        pattern = Pattern.compile("Edge/([0-9]{1,3}.[0-9]{1,5})");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, EDGE);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        pattern = Pattern.compile("Firefox/([0-9]{1,3}.[0-9]{1,3})");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, FIREFOX);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        pattern = Pattern.compile("OPR/([0-9]{1,3}.[0-9]{1,3})");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, OPERA);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        pattern = Pattern.compile("Chrome/([0-9]{1,3}.[0-9]{1,3})");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, CHROME);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        pattern = Pattern.compile("Version/([0-9]{1,2}.[0-9]{1,3})");
        matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            result.put(TYPEKEY, SAFARI);
            result.put(VERSIONKEY, matcher.group(1));
            return result;
        }

        result.put(TYPEKEY, OTHER);
        result.put(VERSIONKEY, "0.0");
        return result;
    }

    public static String encodeStrToUTF8(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] btArray = str.getBytes(Charset.forName("UTF-8"));
        for(int i = 0 ; i < btArray.length ; i++){
            short c = (short) ((short)btArray[i] & 0x00ff);
            if(c == '%' || c == ' ' || c == ' '){
                sb.append(String.format("%%%02x", c));
            } else if(c < 128){
                sb.append(String.format("%c", c));
            }else {
                if(i+2 < btArray.length){
                    sb.append(String.format("%%%02X%%%02X%%%02X", btArray[i], btArray[i+1], btArray[i+2]));
                    i+=2;
                }
            }
        }
        return sb.toString();
    }
}
