package com.ljx.myRedis.api;

/**
 * 删除监听器上下文
 * 1.耗时统计
 * 2.监听器
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListenerContext<K,V> {
    /**
     * 被删除的key
     */
    K key();

    /**
     * 值
     */
    V value();

    /**
     * 删除的类型
     */
    String type();
}
