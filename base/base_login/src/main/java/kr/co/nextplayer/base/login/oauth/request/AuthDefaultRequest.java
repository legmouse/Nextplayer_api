package kr.co.nextplayer.base.login.oauth.request;


import kr.co.nextplayer.base.login.oauth.enums.AuthResponseStatus;
import kr.co.nextplayer.base.login.oauth.exception.AuthException;
import kr.co.nextplayer.base.login.oauth.utils.AuthChecker;
import kr.co.nextplayer.base.login.oauth.utils.StringUtils;
import kr.co.nextplayer.base.login.oauth.utils.UrlBuilder;
import kr.co.nextplayer.base.login.oauth.utils.UuidUtils;
import kr.co.nextplayer.base.login.util.HttpClientUtil;
import kr.co.nextplayer.base.login.oauth.cache.AuthDefaultStateCache;
import kr.co.nextplayer.base.login.oauth.cache.AuthStateCache;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthSource;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.model.AuthResponse;
import kr.co.nextplayer.base.login.oauth.model.AuthToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AuthDefaultRequest implements AuthRequest {
    protected AuthConfig config;
    protected AuthSource source;
    protected AuthStateCache authStateCache;

    public AuthDefaultRequest(AuthConfig config, AuthSource source) {
        this(config, source, AuthDefaultStateCache.INSTANCE, true);
    }
    
    public AuthDefaultRequest(AuthConfig config, AuthSource source, boolean isCheck) {
        this(config, source, AuthDefaultStateCache.INSTANCE, isCheck);
    }

    public AuthDefaultRequest(AuthConfig config, AuthSource source, AuthStateCache authStateCache, boolean isCheck) {
        this.config = config;
        this.source = source;
        this.authStateCache = authStateCache;
        if (!AuthChecker.isSupportedAuth(config, source) && isCheck) {
            throw new AuthException(AuthResponseStatus.PARAMETER_INCOMPLETE, source);
        }
        AuthChecker.checkConfig(config, source);
    }

    protected abstract AuthToken getAccessToken(AuthCallback authCallback);

//    protected abstract Member getUserInfo(AuthToken authToken);

    @Override
    public AuthResponse login(AuthCallback authCallback) {
        try {
            AuthChecker.checkCode(source, authCallback);
      //      AuthChecker.checkState(authCallback.getState(), source, authStateCache);

            AuthToken authToken = this.getAccessToken(authCallback);
//            Member user = this.getUserInfo(authToken);
            return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(null).build();
        } catch (Exception e) {
            log.error("Failed to login with oauth authorization.", e);
            return this.responseError(e);
        }
    }

    private AuthResponse responseError(Exception e) {
        int errorCode = AuthResponseStatus.FAILURE.getCode();
        String errorMsg = e.getMessage();
        if (e instanceof AuthException) {
            AuthException authException = ((AuthException) e);
            errorCode = authException.getErrorCode();
            if (StringUtils.isNotEmpty(authException.getErrorMsg())) {
                errorMsg = authException.getErrorMsg();
            }
        }
        return AuthResponse.builder().code(errorCode).msg(errorMsg).build();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("state", getRealState(state))
            .build();
    }

    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
            .queryParam("code", code)
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("grant_type", "authorization_code")
            .queryParam("redirect_uri", config.getRedirectUri())
            .build();
    }

    protected String refreshTokenUrl(String refreshToken) {
        return UrlBuilder.fromBaseUrl(source.refresh())
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("refresh_token", refreshToken)
            .queryParam("grant_type", "refresh_token")
            .queryParam("redirect_uri", config.getRedirectUri())
            .build();
    }

    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    protected String revokeUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.revoke()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    protected String getRealState(String state) {
        if (StringUtils.isEmpty(state)) {
            state = UuidUtils.getUUID();
        }
        // 缓存state
        authStateCache.cache(state, state);
        return state;
    }

    protected String doPostAuthorizationCode(String code) {
        return HttpClientUtil.doPost(accessTokenUrl(code), null);
    }

    protected String doGetAuthorizationCode(String code) {
        return HttpClientUtil.doGet(accessTokenUrl(code));
    }

    @Deprecated
    protected String doPostUserInfo(AuthToken authToken) {
        return HttpClientUtil.doPost(userInfoUrl(authToken), null);
    }

    protected String doGetUserInfo(AuthToken authToken) {
        return HttpClientUtil.doGet(userInfoUrl(authToken));
    }

    @Deprecated
    protected String doPostRevoke(AuthToken authToken) {
        return HttpClientUtil.doPost(revokeUrl(authToken), null);
    }

    protected String doGetRevoke(AuthToken authToken) {
        return HttpClientUtil.doGet(revokeUrl(authToken));
    }

}
