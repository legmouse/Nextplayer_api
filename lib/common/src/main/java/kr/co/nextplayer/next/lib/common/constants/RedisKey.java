package kr.co.nextplayer.next.lib.common.constants;

import lombok.Getter;

@Getter
public enum RedisKey {
    LOCK("lock:%s");

    private String key;

    RedisKey(String key) {
        this.key = key;
    }

    public String getKey(String ... args) {
        return String.format(key, (Object[]) args);
    }

}
