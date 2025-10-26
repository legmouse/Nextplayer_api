package kr.co.nextplayer.base.webfilter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Component
@Order(0)
public class PriorityCorsWebFilter extends CorsWebFilter {
    public PriorityCorsWebFilter(CorsConfigurationSource corsConfigurationSource) {
        super(corsConfigurationSource);
    }
}
