package com.ljx.myRedis.core.support.evict;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEvictContext;

/**
 * 驱逐策略
 * 1.新加入的key
 * 2.map实现
 * 3.淘汰监听器
 */
public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {
    //新加入的key
    private K key;

    //cache实现
    private ICache<K, V> cache;

    //最大大小
    private int size;

    @Override
    public K key() {
        return key;
    }

    public CacheEvictContext<K, V> key (K key) {
        this.key = key;
        return this;
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheEvictContext<K, V> cache (ICache<K, V> cache) {
        this.cache = cache;
        return  this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheEvictContext<K, V> size (int size) {
        this.size = size;
        return this;
    }
}
