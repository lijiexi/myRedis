package com.ljx.myRedis.core.support.listener.remove;

import com.ljx.myRedis.api.ICacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除监听器类
 * 可以添加多个监听器
 */
public class CacheRemoveListeners {

    private CacheRemoveListeners() {}

    /**
     * 默认监听器类
     * 使用默认监听器
     * @param <K> key
     * @param <V> value
     * @return
     */
    public static <K,V> List<ICacheRemoveListener<K,V>> defaults() {
        List<ICacheRemoveListener<K,V>> listeners = new ArrayList<>();
        listeners.add(new CacheRemoveListener<>());
        return listeners;
    }
}
