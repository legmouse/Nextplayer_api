package kr.co.nextplayer.next.lib.common.locale;

import java.util.Locale;
import java.util.TimeZone;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * LocaleContextHolder gets locale from "Current Thread", which is not compatible with WebFlux.
 */
public class WebFluxLocaleContextHolder {
    private final static Locale DEFAULT_LOCALE = Locale.KOREAN;
    private final static TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Asia/Seoul");
    private final static LocaleContext DEFAULT_LOCALE_CONTEXT = new SimpleTimeZoneAwareLocaleContext(DEFAULT_LOCALE, DEFAULT_TIMEZONE);

    public static LocaleContext getLocaleContext() {
        // Fallback for blocking getLocaleContext()
        return DEFAULT_LOCALE_CONTEXT;
	}

    public static Locale getLocale() {
        // Fallback for blocking getLocale()
        return DEFAULT_LOCALE;
    }

    public static Mono<LocaleContext> getLocaleContextMono() {
        return getServerWebExchange().map(exchange -> exchange.getLocaleContext());
    }

    public static Mono<Locale> getLocaleMono() {
        return getServerWebExchange().map(exchange -> exchange.getLocaleContext().getLocale());
    }

    /**
     * Maybe we can get locale from ServerWebExchange context?(not tested yet)
     */
    public static Mono<ServerWebExchange> getServerWebExchange() {
        return Mono.deferContextual(Mono::just)
            .map(contextView -> contextView.get(ServerWebExchange.class));
    }
}
