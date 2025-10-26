package kr.co.nextplayer.base.config;

import kr.co.nextplayer.next.lib.common.resolver.ShopWebFluxCurrentUserMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BaseCurrentUserArgumentResolverConfig {
    @Bean
    public ShopWebFluxCurrentUserMethodArgumentResolver contextArgumentResolver() {
        return new ShopWebFluxCurrentUserMethodArgumentResolver();
    }
}
