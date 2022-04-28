package com.ljx.myRedis.api;

import java.util.Collection;

/**
 * 缓存过期接口
 */
public interface ICacheExpire<K, V> {

    /**
     * 指定缓存过期时间
     * @param key key
     * @param expireAt 具体过期时间戳
     */
    void expire (final K key, final long expireAt);

    /**
     * 惰性删除中需要处理的key
     * @param keyList
     */
    void refreshExpire (final Collection<K> keyList);

    /**
     * 从过期map中得到key的过期时间
     * 不存在该key返回null
     * @param key key
     * @return 过期时间或者null
     */
    Long expireTime (final K key);
}
