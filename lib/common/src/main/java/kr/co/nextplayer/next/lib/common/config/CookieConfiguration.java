package kr.co.nextplayer.next.lib.common.config;

import kr.co.nextplayer.next.lib.common.property.NextplayerProperty;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.util.CookieGenerator;

import javax.annotation.Resource;

@Configuration
@Setter
public class CookieConfiguration implements WebFluxConfigurer {
	@Resource
    NextplayerProperty nextplayerProperty;

	@Bean
	public CookieGenerator localCookie() {
		CookieGenerator cookie = new CookieGenerator();
//		cookie.setCookieDomain("nextplayer.com");
		cookie.setCookieHttpOnly(true);
		cookie.setCookieMaxAge(-1);
		cookie.setCookieName(nextplayerProperty.getLanguageCookieName());
		return cookie;
	}
}
