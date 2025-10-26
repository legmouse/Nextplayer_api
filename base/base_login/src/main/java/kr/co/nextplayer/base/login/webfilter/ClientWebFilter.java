package kr.co.nextplayer.base.login.webfilter;

import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.property.NextplayerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * kr.co.nextplayer.next.lib.common.interceptor.ClientInterceptor 의 WebFlux 구현
 */
@Slf4j
@Component
public class ClientWebFilter extends PathPatternWebFilter {
    private final String clientId;

    public ClientWebFilter(NextplayerProperty nextplayerProperty) {
        this.clientId = nextplayerProperty.getClientId();

        this.addIncludePathPatterns("/*/base_login/api/v1/**");
        this.addExcludePathPatterns(
                "/",
                "/css/**",
                "/fonts/**",
                "/images/**",
                "/js/**");
    }

    @Override
    public Mono<Void> filterMatched(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            checkClient(exchange);
        } catch (Exception e) {
            return Mono.error(e);
        }

        return chain.filter(exchange);
    }

    public boolean checkClient(ServerWebExchange exchange) throws Exception {

        ServerHttpRequest request = exchange.getRequest();

        log.info("preHandle path : {}", exchange.getRequest().getPath());

        // check client id
        String requestClientId = request.getHeaders().getFirst("clientId");

        // =>>>>>>개발단계 프리패스, 알파/리얼 배포시 삭제
        requestClientId = clientId;
        // 개발단계 프리패스, 알파/리얼 배포시 삭제<<<<<<=

        if (requestClientId != null && requestClientId.equals(clientId)) {
            return true;
        }

        // client id is wrong
        log.warn("client id is wrong : {}", requestClientId);

        throw new CommonLogicException(ApiState.CLIENT.getCode(), ApiState.CLIENT.getMessage());
    }
}
