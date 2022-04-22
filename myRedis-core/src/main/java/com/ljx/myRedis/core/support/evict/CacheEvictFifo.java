package com.ljx.myRedis.core.support.evict;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.api.ICacheEvictContext;
import com.ljx.myRedis.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 丢弃策略 队列先进先出
 */
public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V> {
    /**
     * 先进先出队列
     */
    private final Queue<K> queue = new LinkedList<>();

    @Override
    public CacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        CacheEntry<K, V> res = null;
        //得到cache
        final ICache<K, V> cache = context.cache();
        //超过大小限制，则进行移除
        if (cache.size() >= context.size()) {
            K evictKey = queue.remove();
            //从cache中移除最初元素
            V evictValue = cache.remove(evictKey);
            res = new CacheEntry<>(evictKey,evictValue);
        }
        //新加入的元素放入queue尾
        final K key = context.key();
        queue.add(key);
        return res;
    }
}
