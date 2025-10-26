package kr.co.nextplayer.next.lib.common.aop;

import kr.co.nextplayer.next.lib.common.constants.RedisKey;
import kr.co.nextplayer.next.lib.common.operation.JedisLock;
import kr.co.nextplayer.next.lib.common.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@Aspect
@Slf4j
@Component
@Scope("prototype")
public class RedisLockAop {

    @Resource(name = "jedisConfig")
    private Jedis jedisConfig;

    @Pointcut("@annotation(redisLock)")
    public void redisLockAspect(RedisLock redisLock) {
    }

    @Around("redisLockAspect(redisLock)")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RedisLock redisLock) {
        Object result = null;
        String keyName = RedisKey.LOCK.getKey(redisLock.key());
        JedisLock lock = new JedisLock(jedisConfig, keyName, redisLock.acquiryTimeoutInMillis(), redisLock.lockExpiryInMillis());
        try {
            lock.acquire();
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.info("RedisLockAop occurs an exception", throwable);
        } finally {
            lock.release();
        }
        return result;
    }
}
