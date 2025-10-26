package kr.co.nextplayer.base.login.oauth.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nextplayer.base.login.oauth.exception.AuthException;
import kr.co.nextplayer.base.login.oauth.utils.UrlBuilder;
import kr.co.nextplayer.base.login.util.HttpClientUtil;
import kr.co.nextplayer.base.login.oauth.cache.AuthStateCache;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthDefaultSource;
import kr.co.nextplayer.base.login.oauth.conf.AuthSource;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.model.AuthToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AuthKakaoRequest extends AuthDefaultRequest {

	private static boolean CHECK_SSO = true;
	private static boolean UNCHECK_SSO = false;

	public AuthKakaoRequest(AuthConfig config) {
		super(config, AuthDefaultSource.KAKAO);
	}

	public AuthKakaoRequest(AuthConfig config, AuthSource source) {
		super(config, source, UNCHECK_SSO);
	}

	public AuthKakaoRequest(AuthConfig config, AuthStateCache authStateCache) {
		super(config, AuthDefaultSource.KAKAO, authStateCache, CHECK_SSO);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getFirstRequestToken(AuthCallback authCallback) throws AuthException {

		String url = UrlBuilder.fromBaseUrl(source.accessToken()).build();
		Map<String, String> param = new HashMap<>();
		param.put("grant_type", "authorization_code");
		param.put("client_id", config.getClientId());
		param.put("client_secret", config.getClientSecret());
		param.put("redirect_uri", config.getRedirectUri());
		param.put("code", authCallback.getCode());
		param.put("state", authCallback.getState());

		log.warn("url:*******" + url + "*******");
		String response = null;

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> dataMap = null;
		response = HttpClientUtil.doPost(url , param);
		try {
			dataMap = mapper.readValue(response, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.warn("response:*******" + response + "*******");
		log.warn("dataMap: " + dataMap);


		//UserInfo
		Map<String, Object> userMap = null;
		try {
			String error = MapUtils.getString(dataMap, "error");
			String access_token = MapUtils.getString(dataMap, "access_token");
			if(StringUtils.isEmpty(error)  && StringUtils.isNotBlank(access_token)){
				userMap = getUserInfo(AuthCallback.builder().oauth_token(access_token).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//merge data
		dataMap.putAll(userMap);

		return dataMap;
	}

	public Map<String, Object>  getUserInfo(AuthCallback authCallback) throws AuthException {
		String url = getUserInfoUrl();
		log.warn("url:*******" + url + "*******");
		String response = null;

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> userMap = null;

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + authCallback.getOauth_token());
		Map<String, String> param = new HashMap<>();
		param.put("perty_keys","[\"properties.nickname\", \"kakao_account.email\", \"kakao_account.phone_number\"]");
		response = HttpClientUtil.doGet(url, null ,headers);
		try {
			userMap = mapper.readValue(response, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.warn("response:*******" + response + "*******");
		log.warn("userMap: " + userMap);

		return userMap;
	}

	@Override
	public String authorize(String state) {
		return UrlBuilder.fromBaseUrl(source.authorize())
				.queryParam("response_type", "code")
				.queryParam("client_id", config.getClientId())
				.queryParam("client_secret", config.getClientSecret())
				.queryParam("redirect_uri", config.getRedirectUri())
				.queryParam("state", getRealState(state))
				.queryParam("prompt", "login")
				.build();
	}

	protected String getUserInfoUrl() {
		return UrlBuilder.fromBaseUrl(source.profileUrl()).build();
	}


	@Override
	protected AuthToken getAccessToken(AuthCallback authCallback) {
		return null;
	}

//	@Override
//	protected Member getUserInfo(AuthToken authToken) {
//		return null;
//	}
}
