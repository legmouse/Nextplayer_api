package kr.co.nextplayer.base.login.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * google authorization
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GoogleAuthorization {
    // applicationName
    private String applicationName;
    // redirectUrl
    private String redirectUrl;
    // application credentials
    private GoogleClientSecrets googleClientSecrets;
    // authorized domain
    private final static List<String> scopes = Collections.singletonList(
            "https://www.googleapis.com/auth/userinfo.email"
    );

    public List<String> getScopes() {
        return scopes;
    }

}
