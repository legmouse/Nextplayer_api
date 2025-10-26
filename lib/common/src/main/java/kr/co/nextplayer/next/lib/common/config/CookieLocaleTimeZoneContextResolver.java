package kr.co.nextplayer.next.lib.common.config;

import kr.co.nextplayer.next.lib.common.constants.Language;
import kr.co.nextplayer.next.lib.common.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.util.CookieGenerator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.TimeZone;

public class CookieLocaleTimeZoneContextResolver extends AbstractLocaleContextResolver {

	public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = CookieLocaleTimeZoneContextResolver.class.getName() + ".LOCALE";

	public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".TIME_ZONE";

	@Resource
	private CookieGenerator localCookie;

	@Override
	public LocaleContext resolveLocaleContext(HttpServletRequest request) {
		parseLocale(request);

		Locale locale = (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
		TimeZone timeZone = (TimeZone) request.getAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME);
		return new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
	}

	@Override
	public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
		TimeZone timeZone = null;

		if (localeContext == null) {
			localCookie.removeCookie(response);
			return;
		}
		Locale locale = localeContext.getLocale();
		if (localeContext instanceof TimeZoneAwareLocaleContext) {
			timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
		}

		localCookie.addCookie(response, (locale != null ? locale.getLanguage() : ""));
		request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale != null ? locale : getDefaultLocale());
		request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone != null ? timeZone : getDefaultTimeZone());
	}

	private void parseLocale(HttpServletRequest request) {
		if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) != null) {
			return;
		}

		Locale locale = null;
		TimeZone timeZone = null;
		String cookieValue = WebUtils.getCookieValue(request, localCookie.getCookieName());
		if (StringUtils.isNotEmpty(cookieValue)) {
			// parse from cookie
			Language language = Language.getLanguage(cookieValue);
			locale = language.getLocale();
			timeZone = language.getTimezone();
		}

		if (locale == null) {
			locale = getDefaultLocale();
		}
		if (timeZone == null) {
			timeZone = getDefaultTimeZone();
		}
		request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
		request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone);
	}
}
