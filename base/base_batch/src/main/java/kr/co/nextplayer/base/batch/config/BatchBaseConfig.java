package kr.co.nextplayer.base.batch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.batch.firebase.FcmSender;
import kr.co.nextplayer.base.batch.firebase.FcmWebClient;
import kr.co.nextplayer.base.batch.mapper.PushMapper;
import kr.co.nextplayer.base.config.BaseConfig;
import kr.co.nextplayer.base.config.BaseRedisConfig;
import kr.co.nextplayer.base.member.config.MemberBaseConfig;
import kr.co.nextplayer.base.store.config.StoreDataSourceConfig;
import kr.co.nextplayer.lib.util.mybatis.HotReloadMybatisConfig;
import kr.co.nextplayer.next.lib.common.config.PropertiesConfig;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@Import({
    BaseRedisConfig.class,
    PropertiesConfig.class,
    MemberBaseConfig.class,
    BaseConfig.class,
    HotReloadMybatisConfig.class,
    StoreDataSourceConfig.class,
})
@ComponentScan({ "kr.co.nextplayer.base.batch"})
@MapperScan({ "kr.co.nextplayer.base.batch.mapper"})
public class BatchBaseConfig implements WebFluxConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    /**
     * FCM Web Client 등록
     */
    @Bean
    FcmWebClient FcmWebClient(ObjectMapper objectMapper, PushMapper pushMapper) {
        return new FcmWebClient(objectMapper, pushMapper);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 코어 스레드 풀 크기 설정
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기 설정
        executor.setQueueCapacity(500); // 작업 큐 용량 설정
        executor.setThreadNamePrefix("AsyncThread-"); // 스레드 이름 접두사 설정

        // 작업이 완료된 후 스레드 풀이 종료될 때까지 대기할 시간 설정 (단위: 초)
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

    @Bean
    public CorsConfigurationSource corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOriginPattern("*");
        config.addExposedHeader(HttpHeaders.SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

