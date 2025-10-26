package kr.co.nextplayer.base.config;

import java.util.Locale;
import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.i18n.LocaleContextResolver;

import kr.co.nextplayer.next.lib.common.resolver.WebFluxCookieLocaleTimeZoneContextResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BaseLocaleConfig extends DelegatingWebFluxConfiguration {
    @Override
    public LocaleContextResolver createLocaleContextResolver() {
        var resolver = new WebFluxCookieLocaleTimeZoneContextResolver();
		resolver.setDefaultLocale(Locale.KOREAN);
		resolver.setDefaultTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return resolver;
    }
}
