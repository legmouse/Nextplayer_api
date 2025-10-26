package kr.co.nextplayer.base.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import kr.co.nextplayer.next.lib.common.util.WebFluxJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * 인증 토큰 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class AuthenticationTokenManager {

    private static final String REDIS_KEY_ORDER_ACESS_TOKEN_FORMAT = "ORDER_ACCESS_TOKEN_%s";

    private final RedisOperation redisOperation;
    private final JwtProperty jwtProperty;
    private final ObjectMapper objectMapper;

    /**
     * 회원 정보를 리턴
     * @param exchange
     * @return
     */
    public Account getAccount(ServerWebExchange exchange) {
        return getAccount(exchange, exchange.getRequest().getHeaders().getFirst("accessToken"));
    }

    /**
     * 주문한 회원의 정보를 리턴
     * @param exchange
     * @return
     */
    public Account getAccountByOrder(ServerWebExchange exchange, String orderId) {
        String key = getOrderAccessTokenKey(orderId);
        Object value = redisOperation.get(key);
        if (value != null) {
            return getAccount(exchange, value.toString());
        }
        return null;
    }

    /**
     * accessToken에 해당하는 회원 정보를 리턴
     * @param exchange
     * @param accessToken
     * @return
     */
    private Account getAccount(ServerWebExchange exchange, String accessToken) {
        ServerHttpRequest request = exchange.getRequest();
        try {
            Object token = redisOperation.get(accessToken);
            log.info("token : {}", token);
            Assert.notNull(token, "토큰이 Redis에 존재하지 않습니다.");
            Map<String, Object> parseToken = WebFluxJwtUtil.parseToken(token.toString(), request, jwtProperty);
            boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
            Assert.isTrue(isCertifiedToken, "토큰에 인증 문제가 발생하였습니다.");
            boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
            Assert.isTrue(!isExpired, "토큰이 만료되었습니다.");
            // token 중에 저장된 유저정보 가져오기
            Object accountByToken = parseToken.get("accountByToken");
            return objectMapper.convertValue(accountByToken, Account.class);
        } catch (Exception e) {
            log.error("getAccount error", e);
        }
        return null;
    }

    /**
     * 주문자에 토큰을 저장
     * @param exchange
     * @return
     */
    public void setOrderAccessToken(ServerWebExchange exchange, String orderId) {
        ServerHttpRequest request = exchange.getRequest();
        String accessToken = request.getHeaders().getFirst("accessToken");
        log.info("accessToken : {}", accessToken);
        redisOperation.set(getOrderAccessTokenKey(orderId), accessToken);
    }


    /**
     * 주문한 회원의 토큰을 삭제
     * @param orderId
     * @return
     */
    public void deleteOrderAccessToken(String orderId) {
        String key = getOrderAccessTokenKey(orderId);
        // 주문에서 사용한 이후 바로 삭제처리
        redisOperation.delete(key);
    }

    /**
     * 주문자 토큰 키를 생성
     * @param orderId
     * @return
     */
    private String getOrderAccessTokenKey(String orderId) {
        return String.format(REDIS_KEY_ORDER_ACESS_TOKEN_FORMAT, orderId);
    }

}
