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

    /**
     * RDB加载
     * @param path rdb文件路径
     * @param <K>   key
     * @param <V>   value
     * @return
     */
    public static <K,V> ICacheLoad<K,V> dbJson (final String path) {
        return new CacheLoadDbJson<>(path);
    }

    /**
     * aof加载
     * @param path 文件路径
     * @param <K> key
     * @param <V> value
     * @return
     */
    public static <K,V> ICacheLoad<K,V> aof (final String path) {
        return new CacheLoadAof<>(path);
    }
}
