package kr.co.nextplayer.base.login.oauth.conf;

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
public class AuthConfig {
    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String state;
    
}
