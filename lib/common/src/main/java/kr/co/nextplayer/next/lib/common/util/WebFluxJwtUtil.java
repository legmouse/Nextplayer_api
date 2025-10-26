package kr.co.nextplayer.next.lib.common.util;

import io.jsonwebtoken.*;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * kr.co.nextplayer.next.lib.common.util.JwtUtil 의 WebFlux 구현
 */
@Slf4j
public class WebFluxJwtUtil {
    public static Map<String, Object> parseToken(String token, ServerHttpRequest request, JwtProperty jwtProperty) {
        Claims claims = Jwts.claims();
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(jwtProperty.getSecretKey()))
//                    .requireAudience(getIp(request))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            claims.put("isCertifiedToken", Boolean.TRUE);
            claims.put("isExpired", Boolean.FALSE);
        } catch (ExpiredJwtException ex) {
            log.debug("The token is expired", ex);
            claims.put("isCertifiedToken", Boolean.TRUE);
            claims.put("isExpired", Boolean.TRUE);
        } catch (JwtException ex) {
            log.error("JwtException occurs when parseToken",ex);
            claims.put("isCertifiedToken", Boolean.FALSE);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException occurs when parseToken",ex);
            claims.put("isCertifiedToken", Boolean.FALSE);
        }
        return claims;
    }

    public static String generateToken(Map<String, Object> claims, int expireMinutes, ServerHttpRequest request, JwtProperty jwtProperty) {
        String token = null;
        try {
            JwtBuilder jwtBuilder = Jwts.builder();

            if (claims != null) {
                jwtBuilder.setClaims(claims);
            }

            jwtBuilder.setId(getJwtId());
//            jwtBuilder.setAudience(getIp(request));

            if (expireMinutes > 0) {
                Date expireDate = DateUtils.addMinutes(new Date(), expireMinutes);
                jwtBuilder.setExpiration(expireDate);
            }

            byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getSecretKey());
            jwtBuilder.signWith(new SecretKeySpec(secreKeyBytes, SignatureAlgorithm.HS256.getJcaName()), SignatureAlgorithm.HS256);
            token = jwtBuilder.compact();
        } catch (JwtException ex) {
            log.error("JwtException occurs when generateToken", ex);
        } catch (IllegalArgumentException ex) {
            log.error("IllegalArgumentException occurs when generateToken", ex);
        }
        return token;
    }

    private static String getIp(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        String ip = WebFluxIPUtil.getClientIp(request);

        if (StringUtils.isBlank(ip)) {
            ip = request.getHeaders().getFirst("Host");
        }
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    private static String getJwtId() {
        // create unique jwtId not do
        return UUID.randomUUID().toString();
    }
}
