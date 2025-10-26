package kr.co.nextplayer.base.login.oauth.conf;


import kr.co.nextplayer.base.login.oauth.enums.AuthResponseStatus;
import kr.co.nextplayer.base.login.oauth.exception.AuthException;

public interface AuthSource {

    String authorize();

    String accessToken();

    String userInfo();

    String profileUrl();

    void setAuthorize(String url);

    void setAccessToken(String url);


    default String revoke() {
        throw new AuthException(AuthResponseStatus.UNSUPPORTED);
    }

    default String refresh() {
        throw new AuthException(AuthResponseStatus.UNSUPPORTED);
    }

    default String getName() {
        if (this instanceof Enum) {
            return String.valueOf(this);
        }
        return this.getClass().getSimpleName();
    }
}
