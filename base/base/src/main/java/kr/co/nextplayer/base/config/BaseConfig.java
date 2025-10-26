package kr.co.nextplayer.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.authentication.AuthenticationTokenManager;
import kr.co.nextplayer.base.resolver.AuthenticationMemberMethodArgumentResolver;
import kr.co.nextplayer.base.service.BaseMemberService;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Base 설정
 * <p>하위 프로젝트에서 공통으로 사용할 설정을 관리</p>
 */
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@ComponentScan({ "kr.co.nextplayer.next", "kr.co.nextplayer.base.service" })
@MapperScan({ "kr.co.nextplayer.base.mapper" })
@Import({
    BaseRedisConfig.class
})
public class BaseConfig {

    /**
     * 프로필 빈 등록
     * @param environment
     * @return
     */
    @Bean
    BaseProfiles baseProfiles(Environment environment) {
        return new BaseProfiles(environment);
    }

    /**
     * 토큰 매니저 등록
     * @param redisOperation
     * @param jwtProperty
     * @param objectMapper
     * @return
     */
    @Bean
    AuthenticationTokenManager tokenManager(RedisOperation redisOperation, JwtProperty jwtProperty, ObjectMapper objectMapper) {
        return new AuthenticationTokenManager(redisOperation, jwtProperty, objectMapper);
    }

    /**
     * 회원 인증 MethodArgumentResolver 등록
     * @param memberService
     * @param tokenManager
     * @return
     */
    @Bean
    AuthenticationMemberMethodArgumentResolver authenticationMemberMethodArgumentResolver(BaseMemberService memberService, AuthenticationTokenManager tokenManager) {
        return new AuthenticationMemberMethodArgumentResolver(memberService, tokenManager);
    }

    /**
     * 비밀번호 암호화 Encoder 등록
     * @return
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }


}
