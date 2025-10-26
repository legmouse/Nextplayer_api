package kr.co.nextplayer.base.login.config;

import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminBaseArmeriaConfig {
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        return builder -> {
            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.Ø
            builder.accessLogWriter(AccessLogWriter.combined(), false);
        };
    }
}
