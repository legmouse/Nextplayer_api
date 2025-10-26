package kr.co.nextplayer.next.lib.common.resolver;

import java.util.Locale;
import java.util.TimeZone;

import kr.co.nextplayer.next.lib.common.constants.Language;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class WebFluxCookieLocaleTimeZoneContextResolver implements LocaleContextResolver {
    public static final String LOCALE_COOKIE_NAME = "USER_LOCALE";

	public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = WebFluxCookieLocaleTimeZoneContextResolver.class.getName() + ".LOCALE";
	public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = WebFluxCookieLocaleTimeZoneContextResolver.class.getName() + ".TIME_ZONE";

    private Locale defaultLocale;
    private TimeZone defaultTimeZone;

	private void parseLocale(ServerWebExchange exchange) {
		if (exchange.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) != null) {
			return;
		}

		Locale locale = null;
		TimeZone timeZone = null;

        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(LOCALE_COOKIE_NAME);

		if (cookie != null && StringUtils.isNotEmpty(cookie.getValue())) {
			// parse from cookie
			Language language = Language.getLanguage(cookie.getValue());
			locale = language.getLocale();
			timeZone = language.getTimezone();
		}

		if (locale == null) {
			locale = getDefaultLocale();
		}
		if (timeZone == null) {
			timeZone = getDefaultTimeZone();
		}

        exchange.getAttributes().put(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
        exchange.getAttributes().put(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone);
	}

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        parseLocale(exchange);

		Locale locale = (Locale) exchange.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
		TimeZone timeZone = (TimeZone) exchange.getAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME);

        if (locale == null) {
            locale = getDefaultLocale();
        }

        if (timeZone == null) {
            timeZone = getDefaultTimeZone();
        }

		return new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
		TimeZone timeZone = null;

		if (localeContext == null) {
            exchange.getResponse().getCookies().remove(LOCALE_COOKIE_NAME);
			return;
		}

		Locale locale = localeContext.getLocale();

		if (localeContext instanceof TimeZoneAwareLocaleContext) {
			timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
		}

        exchange.getResponse().addCookie(ResponseCookie.from(LOCALE_COOKIE_NAME, locale.getLanguage()).build());

		exchange.getAttributes().put(LOCALE_REQUEST_ATTRIBUTE_NAME, locale != null ? locale : getDefaultLocale());
		exchange.getAttributes().put(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone != null ? timeZone : getDefaultTimeZone());
    }
}
