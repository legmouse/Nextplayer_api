package kr.co.nextplayer.base.login.oauth.utils;

import kr.co.nextplayer.base.login.oauth.exception.AuthException;
import kr.co.nextplayer.base.login.oauth.conf.AuthConfig;
import kr.co.nextplayer.base.login.oauth.conf.AuthSource;
import kr.co.nextplayer.base.login.oauth.cache.AuthStateCache;
import kr.co.nextplayer.base.login.oauth.enums.AuthResponseStatus;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;

public class AuthChecker {

    /**
     * a third-way election in favor of yes or no
     *
     * @param config config
     * @param source source
     * @return true or false
     * @since 1.6.1-beta
     */
    public static boolean isSupportedAuth(AuthConfig config, AuthSource source) {
        boolean isSupported = StringUtils.isNotEmpty(config.getClientId()) && StringUtils.isNotEmpty(config.getClientSecret()) && StringUtils.isNotEmpty(config.getRedirectUri());
        return isSupported;
    }

    /**
     * Check the legitimacy of the settings。Targeted for some platforms.
     *
     * @param config config
     * @param source source
     * @since 1.6.1-beta
     */
    public static void checkConfig(AuthConfig config, AuthSource source) {
        String redirectUri = config.getRedirectUri();
    }

    /**
     * code to check return call
     * <p>
     * {@code v1.10.0} be handed in {@code source} and {@code callback}
     *
     * @param source   Current License Platform
     * @param callback Set of parameters to be imported when redirected from a third-party license
     * @since 1.8.0
     */
    public static void checkCode(AuthSource source, AuthCallback callback) {
        String code = callback.getCode();
        if (StringUtils.isEmpty(code)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_CODE, source);
        }
    }

    /**
     * check-back transmitted {@code state}，Empty or non-existent
     * <p>
     * {@code state} only two things that don't exist.
     * 1. {@code state} Used, removed normally
     * 2. {@code state} Forged for the front end, it doesn't exist
     *
     * @param state          {@code state} must not empty
     * @param source         {@code source} Current License Platform
     * @param authStateCache {@code authStateCache} state cache
     */
    public static void checkState(String state, AuthSource source, AuthStateCache authStateCache) {
        if (StringUtils.isEmpty(state) || !authStateCache.containsKey(state)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_STATUS, source);
        }
    }
}
