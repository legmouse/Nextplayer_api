package kr.co.nextplayer.next.lib.common.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.resolver.Account;
import kr.co.nextplayer.next.lib.common.util.JsonUtil;
import kr.co.nextplayer.next.lib.common.util.WebFluxJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NextplayerAccessTokenUtil {

    private static boolean local = System.getProperty("spring.profiles.active", "local").equals("local");

    public static Boolean checkToken(ServerWebExchange exchange, JwtProperty jwtProperty, RedisOperation redisOperation) throws Exception {
        ServerHttpRequest request = exchange.getRequest();

        // get accessToken
        String accessToken = request.getHeaders().getFirst("accessToken");

        log.info("accessToken : {}", accessToken);

        // accessToken 유무 판단
        if (StringUtils.isEmpty(accessToken)) {
            return accessError(accessToken);
        }

        //redis accessToken 유무 판단
        if (redisOperation.hasKey(accessToken)) {
            //유효시간내의 토큰 해석
            checkAccessToken(accessToken, redisOperation, jwtProperty, exchange);
        } else {
            // RefreshToken 요청
            return needCheckRefreshTokenError(accessToken);
        }

        return true;
    }

    //토큰 체크
    public static Account getAccount(String accessToken, RedisOperation redisOperation, JwtProperty jwtProperty, ServerWebExchange exchange) throws Exception {

        ServerHttpRequest request = exchange.getRequest();
        String accessToken_redis = redisOperation.get(accessToken).toString();

        Map<String, Object> parseToken = WebFluxJwtUtil.parseToken(accessToken_redis, request, jwtProperty);
        boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
        if (!isCertifiedToken) {
            //accessToken is wrong
            accessError(accessToken);
        }
        boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
        if (isExpired) {
            // RefreshToken 요청
            needCheckRefreshTokenError(accessToken);
        }

        Object accountByToken = parseToken.get("accountByToken");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(accountByToken, Account.class);
    }

    //토큰 체크
    public static Boolean checkAccessToken(String accessToken, RedisOperation redisOperation, JwtProperty jwtProperty, ServerWebExchange exchange) throws Exception {

        ServerHttpRequest request = exchange.getRequest();
        String accessToken_redis = redisOperation.get(accessToken).toString();

        Map<String, Object> parseToken = WebFluxJwtUtil.parseToken(accessToken_redis, request, jwtProperty);
        boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
        if (!isCertifiedToken) {
            //accessToken is wrong
            return accessError(accessToken);
        }
        boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
        if (isExpired) {
            // RefreshToken 요청
            return needCheckRefreshTokenError(accessToken);
        }

        // token 중에 저장된 유저정보 가져오기
        Object accountByToken = parseToken.get("accountByToken");
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.convertValue(accountByToken, Account.class);

        if (account == null) {
            //redis 의 토튼이 잘못되었거나 기간이 지났음.
            return accessError(accessToken);
        } else {
            //중복로그인 방지 제거 20240411 이명우
            exchange.getAttributes().put("accountByToken", account);
            //토큰의 회원정보로 해당 회원의 로그인정보중의 토큰과 비교 -> 해당 회원이 session회원 인지 확인
//            String memberCd = account.getMemberCd();
//            if (redisOperation.hasKey(memberCd)) {
//                //로그인중인 사용자 정보
//                Map<Object, Object> redisAccount = redisOperation.getEntireHashFromKey(memberCd);
//                Account sessionData = JsonUtil.mapToObject(redisAccount, Account.class);
//
//                // 요청하는 token 과 로그인중인 token 비교
//                if (accessToken.equals(sessionData.getAccessToken())) {
//                    exchange.getAttributes().put("accountByToken", sessionData);
//                    log.info("accountByToken : {},  {}", account.getMemberCd(), account.getIp());
//
//                    //접속시 마다 새로고침
//                    refreshSessionToken(sessionData, redisOperation, jwtProperty, request);
//                } else {
//                    // 타인 동시 등록
//                    return accessOverlapError(accessToken);
//                }
//            }
        }
        return true;
    }

    //접속시 마다 새로고침
    public static Boolean refreshSessionToken(Account account, RedisOperation redisOperation, JwtProperty jwtProperty, ServerHttpRequest request) {

        if (redisOperation.hasKey(account.getMemberCd())) {
            //기존의 키로 redis 로그인정보 새로고침, redis 기간 연장
            Map<String, Object> map = JsonUtil.ObjectMutualCovertMap(account, Map.class);
            Map<String, Object> mapAccount = JsonUtil.checkParams(map);
            redisOperation.setHashFieldsToValues(account.getMemberCd(), mapAccount, (Long.parseLong(account.getExpireMinutes()) + 120) * 60);
        }

        //token md5 code
        String accessToken_md5 = account.getAccessToken();
        String refreshToken_md5 = account.getRefreshToken();

        Object ojb_accessToken = redisOperation.get(accessToken_md5);
        if (ojb_accessToken != null) {
            //기존의 키로 redis accessToken 새로고침, redis 기간 연장, token 자체 시간 연장불가
            redisOperation.set(accessToken_md5, String.valueOf(ojb_accessToken), Integer.parseInt(account.getExpireMinutes()) * 60);
        }


        Object ojb_refreshToken = redisOperation.get(refreshToken_md5);
        if (ojb_refreshToken != null) {
            //기존의 키로 redis refreshToken 새로고침, redis 기간 연장 + 신규 token
            Map<String, Object> claims = new HashMap<>();
            claims.put("accountByToken", account);
            //Jwt 시간 2주
            int tokenTime = 2 * 7 * 24 * 60;
            String refreshToken = WebFluxJwtUtil.generateToken(claims, tokenTime, request, jwtProperty);
            redisOperation.set(refreshToken_md5, refreshToken, Integer.parseInt(account.getExpireMinutes()) * 60);
        }


        return true;

    }

    //Refresh 토큰 체크
    public static Account checkRefreshToken(String refreshToken, String refreshTokenOrigin, RedisOperation redisOperation, JwtProperty jwtProperty, ServerWebExchange exchange) throws Exception {
        boolean redisHasKey = redisOperation.hasKey(refreshToken);
        if (redisHasKey || !StringUtils.isEmpty(refreshTokenOrigin)) {

            ServerHttpRequest request = exchange.getRequest();
            String refreshToken_redis = "";
            if(redisHasKey){
                refreshToken_redis = redisOperation.get(refreshToken).toString();
            }else{
                refreshToken_redis = refreshTokenOrigin;
            }
            Map<String, Object> parseToken = WebFluxJwtUtil.parseToken(refreshToken_redis, request, jwtProperty);
            boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
            if (!isCertifiedToken) {
                log.warn("refreshToken is wrong or expired : {}", refreshToken);
                throw new CommonLogicException(ApiState.ACCESS_REFRESH_ERROR.getCode(), ApiState.ACCESS_REFRESH_ERROR.getMessage());
            }
            boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
            if (isExpired) {
                log.warn("refreshToken is wrong or expired : {}", refreshToken);
                throw new CommonLogicException(ApiState.ACCESS_REFRESH_ERROR.getCode(), ApiState.ACCESS_REFRESH_ERROR.getMessage());
            }
            // token 중에 저장된 유저정보 가져오기
            Object accountByToken = parseToken.get("accountByToken");
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.convertValue(accountByToken, Account.class);

            //Token 다시 생성
            makeToken(account, jwtProperty, request);
            //redis에 로그인 정보 저장
            Account accountMocked = accountMocked(account, redisOperation);

            return accountMocked;

        } else {
            log.warn("refreshToken is wrong or expired : {}", refreshToken);
            throw new CommonLogicException(ApiState.ACCESS_REFRESH_ERROR.getCode(), ApiState.ACCESS_REFRESH_ERROR.getMessage());
        }
    }

    //token에 로그인 계정 정보 저장
    public static Boolean makeToken(Account account, JwtProperty jwtProperty, ServerHttpRequest request) {

        //token에 로그인 계정 정보 저장
        Map<String, Object> claims = new HashMap<>();
        account.setAccessToken(null);
        account.setRefreshToken(null);
        claims.put("accountByToken", account);

        String token = WebFluxJwtUtil.generateToken(claims, Integer.parseInt(account.getExpireMinutes()), request, jwtProperty);
        account.setAccessToken(token);
        String refreshToken = WebFluxJwtUtil.generateToken(claims, Integer.parseInt(account.getExpireMinutes()) *  4, request, jwtProperty);
        account.setRefreshToken(refreshToken);

        return true;
    }

    //redis 에 로그인 정보 저장
    public static Account accountMocked(Account account, RedisOperation redisOperation) {

        //token => md5 code
        String accessToken_md5 = DigestUtils.md5DigestAsHex(account.getAccessToken().getBytes());
        String refreshToken_md5 = DigestUtils.md5DigestAsHex(account.getRefreshToken().getBytes());

        redisOperation.set(accessToken_md5, account.getAccessToken(), Integer.parseInt(account.getExpireMinutes()) * 60);
        redisOperation.set(refreshToken_md5, account.getRefreshToken(), Integer.parseInt(account.getExpireMinutes()) * 3 * 60);

        String accessTokenOrigin = account.getAccessToken();
        String refreshTokenOrigin = account.getRefreshToken();
        account.setAccessToken(accessToken_md5);
        account.setRefreshToken(refreshToken_md5);

        Map<String, Object> map = JsonUtil.ObjectMutualCovertMap(account, Map.class);
        Map<String, Object> mapAccount = JsonUtil.checkParams(map);
        redisOperation.setHashFieldsToValues(account.getMemberCd(), mapAccount, (Long.parseLong(account.getExpireMinutes())) * 3 * 60);

        account.setAccessTokenOrigin(accessTokenOrigin);
        account.setRefreshTokenOrigin(refreshTokenOrigin);

        return account;
    }

    //토큰 체크
    public static String checkAccessTokenAndGetMemberCd(String accessToken, ServerWebExchange exchange, JwtProperty jwtProperty, RedisOperation redisOperation) throws Exception {

        //redis accessToken 유무 판단
        if (!redisOperation.hasKey(accessToken)) {
            //유효시간내의 토큰 해석
            needCheckRefreshTokenError(accessToken);
        }

        ServerHttpRequest request = exchange.getRequest();
        String accessToken_redis = redisOperation.get(accessToken).toString();

        Map<String, Object> parseToken = WebFluxJwtUtil.parseToken(accessToken_redis, request, jwtProperty);
        boolean isCertifiedToken = Boolean.parseBoolean(parseToken.get("isCertifiedToken").toString());
        if (!isCertifiedToken) {
            //accessToken is wrong
            accessError(accessToken);
        }
        boolean isExpired = Boolean.parseBoolean(parseToken.get("isExpired").toString());
        if (isExpired) {
            // RefreshToken 요청
            needCheckRefreshTokenError(accessToken);
        }

        // token 중에 저장된 유저정보 가져오기
        Object accountByToken = parseToken.get("accountByToken");
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.convertValue(accountByToken, Account.class);

        if (account == null) {
            //redis 의 토튼이 잘못되었거나 기간이 지났음.
            accessError(accessToken);
        } else {
            // 중복로그인 체크 제거 20240411 이명우
            exchange.getAttributes().put("accountByToken", account);
            return account.getMemberCd();
            //토큰의 회원정보로 해당 회원의 로그인정보중의 토큰과 비교 -> 해당 회원이 session회원 인지 확인
//            String memberCd = account.getMemberCd();
//            if (redisOperation.hasKey(memberCd)) {
//                //로그인중인 사용자 정보
//                Map<Object, Object> redisAccount = redisOperation.getEntireHashFromKey(memberCd);
//                Account sessionData = JsonUtil.mapToObject(redisAccount, Account.class);
//
//                // 요청하는 token 과 로그인중인 token 비교
//                if (accessToken.equals(sessionData.getAccessToken())) {
//                    exchange.getAttributes().put("accountByToken", sessionData);
//                    log.info("accountByToken : {},  {}", account.getMemberCd(), account.getIp());
//
//                    //접속시 마다 새로고침
//                    refreshSessionToken(sessionData, redisOperation, jwtProperty, request);
//                    return account.getMemberCd();
//                }
//            }
        }
        return null;
    }

    public static Boolean accessError(String accessToken) throws Exception {
        log.warn("accessToken is wrong : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS.getCode(), ApiState.ACCESS.getMessage());
    }

    public static Boolean accessExpiredError(String accessToken) throws Exception {
        log.warn("accessToken is expired : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS_EXPIRED.getCode(), ApiState.ACCESS_EXPIRED.getMessage());
    }

    public static Boolean accessOverlapError(String accessToken) throws Exception {
        log.warn("accessToken is overlapped : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS_OVERLAP.getCode(), ApiState.ACCESS_OVERLAP.getMessage());
    }

    public static Boolean needCheckRefreshTokenError(String accessToken) throws Exception {
        log.warn("accessToken is expired in redis, need check refreshToken : {}", accessToken);
        throw new CommonLogicException(ApiState.ACCESS_REFRESH.getCode(), ApiState.ACCESS_REFRESH.getMessage());
    }

}
