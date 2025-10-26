package kr.co.nextplayer.base.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.linecorp.armeria.spring.ArmeriaServerConfigurator;

import kr.co.nextplayer.base.config.BaseCommonArmeriaConfig;


@Configuration
@Import({ BaseCommonArmeriaConfig.class })
public class FrontBaseArmeriaConfig {
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        return builder -> {

            /*
            var grpcService = GrpcService.builder()
                    .addService(shopGrpcService)
                    .enableUnframedRequests(true)
                    .build();

            // Add an Armeria service.
            builder.service(grpcService);

             */
        };
    }
}
