package com.ljx.myRedis.core.model;

import com.ljx.myRedis.api.ICacheEntry;

/**
 * 返回缓存明细 key value信息
 */
public class CacheEntry<K, V> implements ICacheEntry<K, V> {

    private final K key;

    private final V value;

    public CacheEntry (K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 新建元素
     */
    public static <K, V> CacheEntry<K, V> of (final K key, final V value) {
        return new CacheEntry<>(key, value);
    }
    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }
    public String toString () {
        return "EvictEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
