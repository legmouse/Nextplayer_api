package kr.co.nextplayer.next.lib.common.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * kr.co.nextplayer.next.lib.common.util.IPUtil의 WebFlux 구현
 */
public class WebFluxIPUtil {
    public static final String[] HEADERS_TO_TRY = {
        "x-forwarded-for",
        "X-Forwarded-For",
        "X-REAL-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
        "REMOTE-HOST"
    };

    public static String getClientIp(ServerHttpRequest request) {
        String clientIp = _getClientIp(request);

        if (null != clientIp && !clientIp.trim().isEmpty()){
            return clientIp;
        }

        return request.getRemoteAddress().getAddress().getHostAddress();
    }

    private static String _getClientIp(ServerHttpRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeaders().getFirst(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddress().getAddress().getHostAddress();
    }
}
