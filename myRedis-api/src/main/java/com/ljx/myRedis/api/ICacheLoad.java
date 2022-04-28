package com.ljx.myRedis.api;

/**
 * 在缓存启动时，指定初始化加载的信息
 */
public interface ICacheLoad<K, V> {
    /**
     * 加载缓存信息
     */
    void load (final ICache<K, V> cache);
}
