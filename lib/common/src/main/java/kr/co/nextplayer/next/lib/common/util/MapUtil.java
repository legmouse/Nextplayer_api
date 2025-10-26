package kr.co.nextplayer.next.lib.common.util;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

public class MapUtil {

    /**
     * map to url param
     *
     * @param map param
     * @return url query param string
     */
    public static String transMap2UrlParamForms(@NotNull final Map<String, Object> map) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry entry : map.entrySet()) {
            // 值为空不拼接

            if (ObjectUtils.isEmpty(entry.getValue()))
                continue;

            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String ans = stringBuilder.toString();

        return ans.length() > 0 ? ans.substring(0, ans.length() - 1) : ans;
    }

    /**
     * request params cast to Map
     */
    public static Map<String, Object> requestConvertMap(ServerHttpRequest request) {
        MultiValueMap<String, String> properties = request.getQueryParams();
        //返回值Map
        Map<String, Object> resultMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : properties.entrySet()) {

            String value = null;
            String name = entry.getKey();
            List<String> valueList = entry.getValue();
            int vallen = valueList.size();

            if (null == valueList) {
                value = "";
            } else if (vallen == 1) {
                value = valueList.get(0);
            }else if (vallen > 1) {

                for (String s : valueList) {
                    value = s + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueList.toString();
            }

            //System.out.println("kkk---" + name + "===" + value);

            resultMap.put(name, value);
        }

        return resultMap;
    }


}
