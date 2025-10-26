package kr.co.nextplayer.base.login.oauth.cache;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.Getter;
import lombok.Setter;

public class AuthDefaultCache implements AuthCache {

    private static Map<String, CacheState> stateCache = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = cacheLock.writeLock();
    private final Lock readLock = cacheLock.readLock();

    public AuthDefaultCache() {
        if (AuthCacheConfig.schedulePrune) {
            this.schedulePrune(AuthCacheConfig.timeout);
        }
    }

    @Override
    public void set(String key, String value) {
        set(key, value, AuthCacheConfig.timeout);
    }

    @Override
    public void set(String key, String value, long timeout) {
        writeLock.lock();
        try {
            stateCache.put(key, new CacheState(value, timeout));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String get(String key) {
        readLock.lock();
        try {
            CacheState cacheState = stateCache.get(key);
            if (null == cacheState || cacheState.isExpired()) {
                return null;
            }
            return cacheState.getState();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean containsKey(String key) {
        readLock.lock();
        try {
            CacheState cacheState = stateCache.get(key);
            return null != cacheState && !cacheState.isExpired();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void pruneCache() {
        Iterator<CacheState> values = stateCache.values().iterator();
        CacheState cacheState;
        while (values.hasNext()) {
            cacheState = values.next();
            if (cacheState.isExpired()) {
                values.remove();
            }
        }
    }

    public void schedulePrune(long delay) {
        AuthCacheScheduler.INSTANCE.schedule(this::pruneCache, delay);
    }

    @Getter
    @Setter
    private class CacheState implements Serializable {
        private String state;
        private long expire;

        CacheState(String state, long expire) {
            this.state = state;
            // 实际过期时间等于当前时间加上有效期
            this.expire = System.currentTimeMillis() + expire;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > this.expire;
        }
    }
}
