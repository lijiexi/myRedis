package com.ljx.myRedis.core.support.load;

import com.ljx.myRedis.api.ICacheLoad;

/**
 * 加载策略工具类
 */
public class CacheLoads {

    private  CacheLoads () {}

    /**
     * 无加载策略
     * @param <K> key
     * @param <V> value
     */
    public static  <K, V> ICacheLoad<K, V> none () {
        return new CacheLoadNone<>();
    }

    public static <K,V> ICacheLoad<K,V> dbJson (final String path) {
        return new CacheLoadDbJson<>(path);
    }
}
