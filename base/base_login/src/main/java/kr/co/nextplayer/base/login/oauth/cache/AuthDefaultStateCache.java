package kr.co.nextplayer.base.login.oauth.cache;

public enum AuthDefaultStateCache implements AuthStateCache {

    INSTANCE;

    private AuthCache authCache;

    AuthDefaultStateCache() {
        authCache = new AuthDefaultCache();
    }

    @Override
    public void cache(String key, String value) {
        authCache.set(key, value);
    }

    @Override
    public void cache(String key, String value, long timeout) {
        authCache.set(key, value, timeout);
    }

    @Override
    public String get(String key) {
        return authCache.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return authCache.containsKey(key);
    }
}
