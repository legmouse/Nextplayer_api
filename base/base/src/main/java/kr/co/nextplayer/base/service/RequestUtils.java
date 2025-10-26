package kr.co.nextplayer.base.service;

import kr.co.nextplayer.base.config.BaseProfiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestUtils {

    private final BaseProfiles baseProfiles;

    /**
     * 현재 요청의 서비스 URL을 가공해서 리턴
     * @param request
     * @return
     */
    public String getServiceDomain(ServerHttpRequest request) {
        InetSocketAddress localAddress = request.getLocalAddress();
        var port = localAddress.getPort();
        return getServiceDomain(request, port);
    }

    /**
     * 현재 요청의 서비스 URL을 가공해서 리턴
     * @param request
     * @param port
     * @return
     */
    public String getServiceDomain(ServerHttpRequest request, int port) {
        InetSocketAddress localAddress = request.getLocalAddress();
        var hostAddress = localAddress.getAddress().getHostAddress();
        // 운영환경에서는 도메인으로
        if (baseProfiles.isProd()) {
            return request.getURI().getScheme() + "://" + baseProfiles.getBaseDomain() + ":" + port;
        }
        // 로컬 환경에서 아이피로 리다이렉트 되게
        return request.getURI().getScheme() + "://" + hostAddress + ":" + port;
    }

}
