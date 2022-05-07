package com.ljx.myRedis.core.support.evict;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.api.ICacheEvict;
import com.ljx.myRedis.api.ICacheEvictContext;
import com.ljx.myRedis.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * 丢弃策略 LRU 最近最少使用
 * 基于LinkedList实现
 * @param <K> key
 * @param <V> value
 */
public class CacheEvictLru<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLru.class);
    /**
     * 基于LinkedList实现的LRU算法
     */
    private final List<K> list = new LinkedList<>();

    @Override
    public ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        final ICache<K,V> cache = context.cache();
        //cache超过context限制，移除LinkedList队尾元素
        if (!list.isEmpty()&&cache.size() >= context.size()) {
            K evictKey = list.get(list.size()-1);
            //从LinkedList中删除元素
            this.list.remove(evictKey);
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey,evictValue);
        }
        return result;
    }

    /**
     * 放入元素
     * 1.删除已经存在的元素
     * 2.新元素放到链表头部
     * @param key key
     */
    @Override
    public void updateKey(K key) {
        this.list.remove(key);
        this.list.add(0,key);
    }

    @Override
    public void removeKey(K key) {
       this.list.remove(key);
    }
}
