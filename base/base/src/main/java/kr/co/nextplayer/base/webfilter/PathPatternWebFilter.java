package kr.co.nextplayer.base.webfilter;

import org.slf4j.Logger;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public abstract class PathPatternWebFilter implements WebFilter {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(PathPatternWebFilter.class);

    private List<PathPattern> includePathPatterns = new ArrayList<>();
    private List<PathPattern> excludePathPatterns = new ArrayList<>();

    public PathPatternWebFilter() {
        super();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        RequestPath path = exchange.getRequest().getPath();
        PathContainer pathContainer = path.pathWithinApplication();

        if (excludePathPatterns.stream().anyMatch(pathPattern -> pathPattern.matches(pathContainer))) {
            logger.info(path.toString() + " path excluded");
            return chain.filter(exchange);
        }

        if (includePathPatterns.stream().anyMatch(pathPattern -> pathPattern.matches(pathContainer))) {
            logger.info(path.toString() + " path matched");
            return filterMatched(exchange, chain);
        }


        return chain.filter(exchange);
    }

    protected void addIncludePathPatterns(String... patterns) {
        for (String pattern : patterns) {
            includePathPatterns.add(new PathPatternParser().parse(pattern));
        }
    }

    protected void addExcludePathPatterns(String... patterns) {
        for (String pattern : patterns) {
            excludePathPatterns.add(new PathPatternParser().parse(pattern));
        }
    }

    public abstract Mono<Void> filterMatched(ServerWebExchange exchange, WebFilterChain chain);
}
