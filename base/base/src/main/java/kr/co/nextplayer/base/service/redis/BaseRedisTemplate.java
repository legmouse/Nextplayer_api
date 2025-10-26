package kr.co.nextplayer.base.service.redis;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

public class BaseRedisTemplate extends ReactiveRedisTemplate<String, Object> {
    public BaseRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
            RedisSerializationContext<String, Object> serializationContext, boolean exposeConnection) {
        super(connectionFactory, serializationContext, exposeConnection);
    }

    public BaseRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
            RedisSerializationContext<String, Object> serializationContext) {
        super(connectionFactory, serializationContext);
    }
}
