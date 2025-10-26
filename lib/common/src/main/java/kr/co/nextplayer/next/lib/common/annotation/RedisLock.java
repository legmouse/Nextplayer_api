package kr.co.nextplayer.next.lib.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLock {

    String key();

    int acquiryTimeoutInMillis() default 10000;

    int lockExpiryInMillis() default 60000;
}
