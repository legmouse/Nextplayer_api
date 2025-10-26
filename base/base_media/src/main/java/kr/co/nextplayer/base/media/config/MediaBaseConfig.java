package kr.co.nextplayer.base.media.config;

import kr.co.nextplayer.base.config.BaseConfig;
import kr.co.nextplayer.base.config.BaseSpringObjectMapperConfig;
import kr.co.nextplayer.base.config.properties.MediaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.info.OsInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import kr.co.nextplayer.lib.util.mybatis.HotReloadMybatisConfig;

import java.nio.file.Paths;

@Configuration
@Import({
    HotReloadMybatisConfig.class,
    BaseSpringObjectMapperConfig.class,
    BaseConfig.class,
    HotReloadMybatisConfig.class,
})
@ComponentScan({ "kr.co.nextplayer.base.media" })
@MapperScan({"kr.co.nextplayer.base.media.mapper"})
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class MediaBaseConfig implements WebFluxConfigurer {

    private final MediaProperties mediaProperties;

    /*@Bean
    public CorsConfigurationSource corsWebFilter() {
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOriginPattern("*");
        config.addExposedHeader(HttpHeaders.SET_COOKIE);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("mediaProperties : {}", mediaProperties);
        log.info("getStoragePath : {}", mediaProperties.getStoragePath());
        var rootPath = Paths.get(mediaProperties.getStoragePath()).toAbsolutePath() + "/";
        var prefix = new OsInfo().getName().toLowerCase().contains("windows") ? "file:///" : "file://";
        var resourceLocations = prefix + rootPath;
        log.info("rootPath : {}", rootPath);
        log.info("prefix : {}", prefix);
        log.info("resourceLocations : {}", resourceLocations);
        // 업로드 한 파일 경로 접근시 시스템에 저장된 파일 연결되게 리소스 핸들러 추가
        registry.addResourceHandler(mediaProperties.getResourcePath() + "/**")
            .addResourceLocations(resourceLocations);
    }*/
}
