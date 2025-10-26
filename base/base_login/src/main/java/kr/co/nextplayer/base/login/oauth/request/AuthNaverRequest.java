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
public class AuthNaverRequest extends AuthDefaultRequest {

	private static boolean CHECK_SSO = true;
	private static boolean UNCHECK_SSO = false;

	public AuthNaverRequest(AuthConfig config) {
		super(config, AuthDefaultSource.NAVER);
	}

	public AuthNaverRequest(AuthConfig config, AuthSource source) {
		super(config, source, UNCHECK_SSO);
	}

	public AuthNaverRequest(AuthConfig config, AuthStateCache authStateCache) {
		super(config, AuthDefaultSource.NAVER, authStateCache, CHECK_SSO);
	}

	private String getRequestTokenUri(AuthCallback authCallback) throws AuthException {

		log.info("authCallback.getState(): *******" + authCallback.getState() + "*******");
		log.info("source: *******" + source + "*******");
		log.info("authStateCache: *******" + authStateCache + "*******");

		return UrlBuilder.fromBaseUrl(source.accessToken()).queryParam("grant_type", "authorization_code")
				.queryParam("format", "json").queryParam("service", config.getRedirectUri()).build();
	}

	@SuppressWarnings("unchecked")
	public String getRequestToken(AuthCallback authCallback) throws AuthException {
		String url = getRequestTokenUri(authCallback);
		log.warn("url:*******" + url + "*******");
		String response = null;

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> dataMap = null;
		response = HttpClientUtil.doGet(url);
		try {
			dataMap = mapper.readValue(response, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("response:*******" + response + "*******");
		log.info("dataMap: " + dataMap);
		this.checkResponse(dataMap);

		//String empNo = MapUtils.getString(MapUtils.getMap(MapUtils.getMap(dataMap, "serviceResponse"), "authenticationSuccess"), "user");
		String empNo = "";
		log.info("empNo: " + empNo);
		return empNo;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getFirstRequestToken(AuthCallback authCallback) throws Exception {

		String url = accessTokenUrl(authCallback.getCode(), authCallback.getState());
		log.info("url:*******" + url + "*******");

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> dataMap = null;
        String response = HttpClientUtil.doGet(url);
        dataMap = mapper.readValue(response, Map.class);
		log.info("response:*******" + response + "*******");
		log.info("dataMap: " + dataMap);


		//UserInfo
        String error = MapUtils.getString(dataMap, "error");
        String access_token = MapUtils.getString(dataMap, "access_token");
        if(StringUtils.isEmpty(error)  && StringUtils.isNotBlank(access_token)){
            Map<String, Object> userMap = getUserInfo(AuthCallback.builder().oauth_token(access_token).build());
            //merge data
            dataMap.putAll(userMap);
        }

		return dataMap;
	}

	public Map<String, Object>  getUserInfo(AuthCallback authCallback) throws AuthException {
		String url = getUserInfoUrl();
		log.info("url:*******" + url + "*******");
		String response = null;

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> userMap = null;

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + authCallback.getOauth_token());
		response = HttpClientUtil.doGet(url, null ,headers);
		try {
			userMap = mapper.readValue(response, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("response:*******" + response + "*******");
		log.info("userMap: " + userMap);

		return userMap;
	}



	@Override
	protected AuthToken getAccessToken(AuthCallback authCallback) {
		return null;
	}

	protected String accessTokenUrl(String code, String state) {
		return UrlBuilder.fromBaseUrl(source.accessToken())
				.queryParam("grant_type", "authorization_code")
				.queryParam("client_id", config.getClientId())
				.queryParam("client_secret", config.getClientSecret())
				.queryParam("code", code)
				.queryParam("state", state)
				.build();
	}

	protected String getUserInfoUrl() {
		return UrlBuilder.fromBaseUrl(source.profileUrl()).build();
	}


	private void checkResponse(Map<String, Object> dataMap) throws AuthException {
		Map result = MapUtils.getMap(dataMap, "serviceResponse");

		if (result.containsKey("authenticationFailure")) {
			String error = MapUtils.getString(MapUtils.getMap(result, "authenticationFailure"), "description");
			log.error("oauth bad request:" + error);
			throw new AuthException(error);
		}
	}
}
