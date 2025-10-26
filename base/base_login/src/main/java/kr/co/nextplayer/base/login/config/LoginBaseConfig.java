package kr.co.nextplayer.base.login.config;

import kr.co.nextplayer.base.config.*;
import kr.co.nextplayer.base.login.resolver.WebFluxCurrentUserMethodArgumentResolver;
import kr.co.nextplayer.base.member.config.MemberBaseConfig;
import kr.co.nextplayer.base.store.config.StoreDataSourceConfig;
import kr.co.nextplayer.lib.util.mybatis.HotReloadMybatisConfig;
import kr.co.nextplayer.next.lib.common.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Slf4j
@Configuration
@Import({
    AdminBaseArmeriaConfig.class,
    BaseLocalDateTimeSerializerConfig.class,
    PropertiesConfig.class,
    GoogleAuthorizationConfig.class,
    BaseLocaleConfig.class,
    PropertiesConfig.class,
    BaseRedisConfig.class,
    BaseMybatisConfig.class,
    BaseSpringFoxConfig.class,
    MemberBaseConfig.class,
    BaseWebFluxConfig.class,
    BaseConfig.class,
    HotReloadMybatisConfig.class,
    StoreDataSourceConfig.class,
})
@ComponentScan({ "kr.co.nextplayer.base.login"})
@MapperScan({
    "kr.co.nextplayer.base.login.mapper"
})
public class LoginBaseConfig implements WebFluxConfigurer {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
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
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(contextArgumentResolver());
    }

    @Bean
    public WebFluxCurrentUserMethodArgumentResolver contextArgumentResolver() {
        return new WebFluxCurrentUserMethodArgumentResolver();
    }

}
