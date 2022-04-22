package com.ljx.myRedis.api;

import java.util.Map;

/**
 * 缓存接口，兼容Map
 * 实现一个固定大小的缓存
 */
public interface ICache<K, V> extends Map<K, V> {
    /**
     * 设置过期时间：
     * 如果key不存在则不操作
     */

    /**
     * 淘汰策略
     * @return 淘汰
     * @since 0.0.11
     */
    ICacheEvict<K,V> evict();

}
