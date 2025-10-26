package kr.co.nextplayer.base.login.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * google Auth
 */
@Configuration
@Slf4j
public class GoogleAuthorizationConfig implements WebFluxConfigurer {

    @Value("${auth.google.cas.client_id}")
    private String clientId;
    @Value("${auth.google.cas.client_secret}")
    private String clientSecret;
    @Value("${auth.google.cas.application_name}")
    private String applicationName;
    @Value("${auth.google.cas.redirect_uri}")
    private String redirectUrl;


    @Bean(name = "googleAuthorization")
    public GoogleAuthorization googleFeed() {
        GoogleClientSecrets clientSecrets = null;
        try {
            GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
            details.setClientId(clientId);
            details.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets();
            clientSecrets.setInstalled(details);
        } catch (Exception e) {
            log.error("authorization configuration error:{}", e.getMessage());
        }
        // 构建bean
        return GoogleAuthorization.builder()
                .googleClientSecrets(clientSecrets)
                .applicationName(applicationName)
                .redirectUrl(redirectUrl)
                .build();
    }
}
