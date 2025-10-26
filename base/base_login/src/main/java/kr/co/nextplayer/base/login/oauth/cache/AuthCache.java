package kr.co.nextplayer.base.login.oauth.cache;

public interface AuthCache {

    void set(String key, String value);

    void set(String key, String value, long timeout);

    String get(String key);

    boolean containsKey(String key);

    default void pruneCache() {
    }

}