package kr.co.nextplayer.next.lib.common.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.TimeZone;

@Getter
public enum Language {
	ENGLISH(Locale.ENGLISH, TimeZone.getTimeZone("America/New_York")),
	JAPANESE(Locale.JAPANESE, TimeZone.getTimeZone("Asia/Tokyo")),
	SIMPLIFIED_CHINESE(Locale.SIMPLIFIED_CHINESE, TimeZone.getTimeZone("Asia/Shanghai")),
	KOREAN(Locale.KOREAN, TimeZone.getTimeZone("Asia/Seoul"));


	private String languageTag;

	private Locale locale;

	private TimeZone timezone;

	private Language(Locale defaultLocale, TimeZone timezone) {
		this.languageTag = defaultLocale.toLanguageTag();
		this.locale = defaultLocale;
		this.timezone = timezone;
	}

	public static Locale getLocale(String language) {
		if (StringUtils.isBlank(language)) {
			return KOREAN.locale;
		}

		for (Language item : Language.values()) {
			if (StringUtils.equals(item.getLanguageTag(), language)) {
				return item.getLocale();
			}
		}

		// default
		return KOREAN.locale;
	}

	public static Language getLanguage(String language) {
		if (StringUtils.isBlank(language)) {
			return KOREAN;
		}

		for (Language item : Language.values()) {
			if (StringUtils.equals(item.getLanguageTag(), language)) {
				return item;
			}
		}

		// default
		return KOREAN;
	}

	public static Language getLanguage(Locale locale) {
		if (locale == null) {
			return KOREAN;
		}

		for (Language item : Language.values()) {
			if (item.getLocale().equals(locale)) {
				return item;
			}
		}

		// default
		return KOREAN;
	}

	public static boolean isSupportedLanguage(String language) {
		if (StringUtils.isBlank(language)) {
			return false;
		}

		for (Language item : Language.values()) {
			if (StringUtils.equals(item.getLanguageTag(), language)) {
				return true;
			}
		}

		return false;
	}
}
