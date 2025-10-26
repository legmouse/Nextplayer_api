package kr.co.nextplayer.base.front.config;

import com.linecorp.armeria.client.grpc.GrpcClients;

import kr.co.nextplayer.base.board.config.BoardBaseConfig;
import kr.co.nextplayer.base.community.config.CommunityBaseConfig;
import kr.co.nextplayer.base.config.*;
import kr.co.nextplayer.base.config.properties.MediaProperties;
import kr.co.nextplayer.base.cup.config.CupBaseConfig;
import kr.co.nextplayer.base.file.config.FileBaseConfig;
import kr.co.nextplayer.base.league.config.LeagueBaseConfig;
import kr.co.nextplayer.base.likes.config.LikesBaseConfig;
import kr.co.nextplayer.base.media.config.MediaBaseConfig;
import kr.co.nextplayer.base.member.config.MemberBaseConfig;
import kr.co.nextplayer.base.player.config.PlayerBaseConfig;
import kr.co.nextplayer.base.reply.config.ReplyBaseConfig;
import kr.co.nextplayer.base.team.config.TeamBaseConfig;
import kr.co.nextplayer.lib.util.mybatis.HotReloadMybatisConfig;
import kr.co.nextplayer.base.store.config.StoreDataSourceConfig;
import kr.co.nextplayer.next.lib.common.config.PropertiesConfig;
import kr.co.nextplayer.proto.login.ReactorLoginServiceGrpc.ReactorLoginServiceStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.info.OsInfo;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.nio.file.Paths;


@Configuration
@Import({
    BaseLocalDateTimeSerializerConfig.class,
    PropertiesConfig.class,
    BaseSpringFoxConfig.class,
    BaseRedisConfig.class,
    BaseConfig.class,
    HotReloadMybatisConfig.class,
    BaseCurrentUserArgumentResolverConfig.class,
    MemberBaseConfig.class,
    BaseWebFluxConfig.class,
    CupBaseConfig.class,
    LeagueBaseConfig.class,
    BoardBaseConfig.class,
    TeamBaseConfig.class,
    MediaBaseConfig.class,
    PlayerBaseConfig.class,
    CommunityBaseConfig.class,
    FileBaseConfig.class,
    ReplyBaseConfig.class,
    LikesBaseConfig.class,
    BaseSpringObjectMapperConfig.class,
    StoreDataSourceConfig.class,
})
@ComponentScan({"kr.co.nextplayer.base.front"})
@MapperScan({"kr.co.nextplayer.base.front.mapper"})
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class FrontBaseConfig implements WebFluxConfigurer {
    @Value("${base_front.grpc.login}")
    private String loginGrpcUrl;

    private final MediaProperties mediaProperties;

    @Bean
    public ReactorLoginServiceStub loginServiceStub() {
        return GrpcClients.newClient(loginGrpcUrl, ReactorLoginServiceStub.class);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024);
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
    }


}
