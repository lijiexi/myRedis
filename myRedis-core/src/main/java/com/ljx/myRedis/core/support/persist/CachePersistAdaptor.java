package com.ljx.myRedis.core.support.persist;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICachePersist;

/**
 * 缓存持久化 适配器
 */
public class CachePersistAdaptor<K,V> implements ICachePersist<K, V> {
    @Override
    public void persist(ICache<K, V> cache) {

    }
}
