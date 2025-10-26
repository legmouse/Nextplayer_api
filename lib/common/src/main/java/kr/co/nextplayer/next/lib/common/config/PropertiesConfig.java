package kr.co.nextplayer.next.lib.common.config;

import kr.co.nextplayer.next.lib.common.property.JwtProperty;
import kr.co.nextplayer.next.lib.common.property.NextplayerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class PropertiesConfig implements WebFluxConfigurer {

    @Bean(name = "jwtPropertyBean")
    @ConfigurationProperties(prefix = "nextplayer.jwt")
    public JwtProperty getJwtProperty() {
        return new JwtProperty();
    }


    @Bean(name = "nextplayerProperty")
    @ConfigurationProperties(prefix = "nextplayer")
    public NextplayerProperty getNextplayerProperty() {
        return new NextplayerProperty();
    }


}
