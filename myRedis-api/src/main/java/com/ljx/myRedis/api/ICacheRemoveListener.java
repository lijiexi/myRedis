package com.ljx.myRedis.api;

/**
 * 删除监听器
 * 监听size满了或者expire过期导致的key删除
 */
public interface ICacheRemoveListener<K,V> {
    /**
     * 监听
     * @param context 删除监听器上下文
     */
    void listen (final ICacheRemoveListenerContext<K,V> context);
}
