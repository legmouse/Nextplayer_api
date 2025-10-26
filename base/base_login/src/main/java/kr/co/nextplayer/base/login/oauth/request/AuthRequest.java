package kr.co.nextplayer.base.login.oauth.request;


import kr.co.nextplayer.base.login.oauth.enums.AuthResponseStatus;
import kr.co.nextplayer.base.login.oauth.exception.AuthException;
import kr.co.nextplayer.base.login.oauth.model.AuthCallback;
import kr.co.nextplayer.base.login.oauth.model.AuthResponse;
import kr.co.nextplayer.base.login.oauth.model.AuthToken;

public interface AuthRequest {


    /**
     * Return url with parameter {@code state}，When authorized to redirect take this{@code state}
     *
     * @param state state Validate parameters，can prevent csrf
     * @return Return Authorization Address
     */
    default String authorize(String state) {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }

    /**
     * Third Party Login
     *
     * @param authCallback
     * @return Return user information after successful login
     */
    default AuthResponse login(AuthCallback authCallback) {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }

    /**
     * revoke authorization
     *
     * @param authToken return Token after successful login
     * @return AuthResponse
     */
    default AuthResponse revoke(AuthToken authToken) {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }

    /**
     * refresh access token
     *
     * @param authToken returned Token after successful login
     * @return AuthResponse
     */
    default AuthResponse refresh(AuthToken authToken) {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }
}