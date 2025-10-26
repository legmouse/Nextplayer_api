package kr.co.nextplayer.base.login.webfilter;

import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.util.filter.NextplayerAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * kr.co.nextplayer.next.app.backstage.interceptor.AccessTokenInterceptor의 WebFlux 구현
 */
@Slf4j
@Component
public class AccessTokenWebFilter extends PathPatternWebFilter {
    private final JwtProperty jwtProperty;
    private final RedisOperation redisOperation;

    public AccessTokenWebFilter(JwtProperty jwtProperty, RedisOperation redisOperation) {
        this.jwtProperty = jwtProperty;
        this.redisOperation = redisOperation;

        this.addIncludePathPatterns(
            "/*/base_login/api/v1/auth/logout/**",
            "/*/base_login/api/v1/auth/delayPassWord/**"
        );
        this.addIncludePathPatterns("/*/base_login/api/v1/auth/member_info");
        this.addIncludePathPatterns("/*/base_login/api/v1/auth/checkFCM");

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
            NextplayerAccessTokenUtil.checkToken(exchange, jwtProperty, redisOperation);
        } catch (Exception e) {
            return Mono.error(e);
        }

        return chain.filter(exchange);
    }


}
