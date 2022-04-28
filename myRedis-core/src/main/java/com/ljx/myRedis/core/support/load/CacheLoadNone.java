package com.ljx.myRedis.core.support.load;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheLoad;

/**
 * 默认无加载策略
 */
public class CacheLoadNone<K, V> implements ICacheLoad<K, V> {
    @Override
    public void load(ICache<K, V> cache) {
        //do nothing
    }
}
