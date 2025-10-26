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
@AllArgsConstructor
@NoArgsConstructor
public class AuthCallback implements Serializable {

	private String code;

    private String auth_code;

    private String state;

    private String authorization_code;

    private String oauth_token;

    private String oauth_verifier;

    private String ticket;

    private String teamId;

    private String keyId;

    private String secretCode;

    private String clientId;

}
