package kr.co.nextplayer.base.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.encoding.DecodingClient;
import com.linecorp.armeria.client.logging.ContentPreviewingClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.ContentPreviewingService;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BaseCommonArmeriaConfig {
    /**
     * Armeria 공통 설정(로깅 등...)
     *
     * @return
     */
    @Bean("baseCommonArmeriaServerConfigurator")
    public ArmeriaServerConfigurator baseCommonArmeriaConfigurator() {
        return builder -> {
            log.info("Configurating common Armeria server settings...");

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false);

            // Content Preview
            builder.decorator(ContentPreviewingService.newDecorator(Integer.MAX_VALUE));
        };
    }

    @Bean
    public ClientFactory clientFactory() {
        return ClientFactory.insecure();
    }
}
