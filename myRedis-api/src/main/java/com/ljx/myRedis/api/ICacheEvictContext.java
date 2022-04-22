package com.ljx.myRedis.api;

/**
 * evict策略
 * 1.新加入的key
 * 2.map实现
 * 3.淘汰监听器
 */
public interface ICacheEvictContext<K, V> {
    /**
     * 新加入的key
     */
    K key();

    /**
     * cache实现
     * @return map
     */
    ICache<K, V> cache();

    /**
     * 获取大小
     */
    int size();
}
