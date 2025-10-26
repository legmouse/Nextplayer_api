package kr.co.nextplayer.base.login.oauth.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken implements Serializable {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expireIn;
    private String scope;
    private int appNo;
    private String memberNo;
    private String nickname;
    private String code ;
}
