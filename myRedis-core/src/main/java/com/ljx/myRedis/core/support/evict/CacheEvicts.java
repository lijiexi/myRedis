package com.ljx.myRedis.core.support.evict;

import com.ljx.myRedis.api.ICacheEvict;

/**
 * 丢弃策略
 * 创建CacheBs的时候进行选择
 */
public final class CacheEvicts {

    private CacheEvicts () {}

    /**
     * 无策略
     */
//    public static <K, V> ICacheEvict<K, V> none () {
//        return new CacheEvict<>();
//    }
    /**
     * 先进先出策略
     */
    public static <K, V> ICacheEvict<K, V> fifo () {
        return new CacheEvictFifo<>();
    }
    /**
     * LRU驱逐策略
     * 基于LinkedList实现
     */
    public static <K,V> ICacheEvict<K,V> lru () {
        return new CacheEvictLru<>();
    }

    /**
     * 基于双向链表+Map的Lru实现
     * @param <K> key
     * @param <V> value
     * @return
     */
    public static <K,V> ICacheEvict<K,V> lruDoubleListMap () {
        return new CacheEvictLruDoubleListMap<>();
    }
}
