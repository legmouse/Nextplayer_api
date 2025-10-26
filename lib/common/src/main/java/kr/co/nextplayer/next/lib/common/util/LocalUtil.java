package kr.co.nextplayer.next.lib.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.constants.Language;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.co.nextplayer.next.lib.common.locale.WebFluxLocaleContextHolder;

public class LocalUtil {

	/**
	 * 다국어 언어 처리
	 */
	public static String getLang() {
		return getLanguage().getLanguageTag();
	}

	public static Language getLanguage() {
		return Language.getLanguage(WebFluxLocaleContextHolder.getLocale());
	}

	public static void changeLanguage(HttpServletRequest request, HttpServletResponse response, String language) throws Exception {
		if (!Language.isSupportedLanguage(language)) {
			throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg1_common_paramErr");
		}

		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Language lang = Language.getLanguage(language);
		if (localeResolver instanceof LocaleContextResolver) {
			((LocaleContextResolver) localeResolver).setLocaleContext(request, response, new SimpleTimeZoneAwareLocaleContext(lang.getLocale(), lang.getTimezone()));
		} else {
			throw new Exception();
		}
	}
}
