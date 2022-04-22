package com.ljx.myRedis.api;

/**
 * 缓存明细信息
 * @param <K> key
 * @param <V> value
 */
public interface ICacheEntry<K, V> {

    K key();

    V value();
}
