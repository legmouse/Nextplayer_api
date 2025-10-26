package kr.co.nextplayer.base.backend.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BaseRedis {
    private final Logger baseLog = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected BaseRedisTemplate template;

    @SuppressWarnings("unchecked")
    public <T> Mono<T> getOp(String key) {
        return template.opsForValue()
                .get(key)
                .map(value -> (T) value)
                .doOnNext(value -> baseLog.info("Cache hit: {}", key));
    }

    public Mono<Boolean> setOp(String key, Object value) {
        return template.opsForValue().set(key, value).doOnSuccess(result -> {
            baseLog.info("Cache Write: ", key);
        });
    }

    public Mono<Boolean> setOp(String key, Object value, Duration duration) {
        return template.opsForValue().set(key, value, duration).doOnSuccess(result -> {
            baseLog.info("Cache Write: {}, expire={}", key, duration);
        });
    }

    public Mono<Boolean> evictOp(String key) {
        return template.opsForValue().delete(key).doOnSuccess(result -> {
            baseLog.info("Cache Evict: {}", key);
        });
    }

    public <T> Mono<T> getOpWithFallback(String key,  Mono<T> fallback) {
        var fallbackWithLog = fallback.doFirst(() -> {
            baseLog.info("Cache Miss: {}", key);
        });

        return this.<T>getOp(key).switchIfEmpty(fallbackWithLog);
    }
}
