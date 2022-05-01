package com.ljx.myRedis.core.support.listener.remove;

import com.ljx.myRedis.api.ICacheRemoveListenerContext;

/**
 * 删除监听器的上下文
 * @param <K> key
 * @param <V> value
 */
public class CacheRemoveListenerContext<K,V> implements ICacheRemoveListenerContext<K,V> {
    /**
     * key
     */
    private K key;
    /**
     * value
     */
    private V value;
    /**
     * 删除类型
     */
    private String type;
    @Override
    public K key() {
        return key;
    }

    /**
     * 新建上下文实例
     * @param <K> key
     * @param <V> value
     * @return 上下文
     */
    public static <K,V> CacheRemoveListenerContext<K,V> newInstance () {
        return new CacheRemoveListenerContext<>();
    }
    public CacheRemoveListenerContext<K,V> key (K key) {
        this.key = key;
        return this;
    }

    @Override
    public V value() {
        return value;
    }
    public CacheRemoveListenerContext<K,V> value (V value) {
        this.value = value;
        return this;
    }

    @Override
    public String type() {
        return type;
    }
    public CacheRemoveListenerContext<K,V> type (String type) {
        this.type = type;
        return this;
    }
}
