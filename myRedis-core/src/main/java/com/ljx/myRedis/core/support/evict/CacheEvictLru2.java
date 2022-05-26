package com.ljx.myRedis.core.support.evict;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEntry;
import com.ljx.myRedis.api.ICacheEvictContext;
import com.ljx.myRedis.core.model.CacheEntry;
import com.ljx.myRedis.core.support.struct.lru.ILruMap;
import com.ljx.myRedis.core.support.struct.lru.impl.LruMapDoubleList;

/**
 * 淘汰策略 lru-2实现
 * @param <K>
 * @param <V>
 */
public class CacheEvictLru2<K,V> extends AbstractCacheEvict<K,V> {

    private static final Log log = LogFactory.getLog(CacheEvictLru2.class);

    /**
     * 存储第一次访问的数据
     */
    private final ILruMap<K,V> firstLruMap;
    /**
     * 存储访问2次及以上数据
     */
    private final ILruMap<K,V> moreLruMap;

    public CacheEvictLru2 () {
        this.firstLruMap = new LruMapDoubleList<>();
        this.moreLruMap = new LruMapDoubleList<>();
    }

    @Override
    public ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        final ICache<K,V> cache = context.cache();
        //超过限制，移除队尾元素
        if (cache.size() >= context.size()) {
            ICacheEntry<K,V> evictEntry = null;

            //1.firstmap不为空，则优先移除其中元素
            if (!firstLruMap.isEmpty()) {
                evictEntry = firstLruMap.removeEldest();
                log.debug("从 firstLruMap 中淘汰数据：{}", evictEntry);
            } else {
                //2.从moreMap中移除该元素
                evictEntry = moreLruMap.removeEldest();
                log.debug("从 moreLruMap 中淘汰数据：{}", evictEntry);
            }
            //从缓存中移除元素
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey,evictValue);
        }
        return result;
    }

    /**
     * 1. 如果 moreLruMap 已经存在，则处理 more 队列，先删除，再插入。
     * 2. 如果 firstLruMap 中已经存在，则处理 first 队列，先删除 firstLruMap，然后插入 Lru。
     * 3. 如果不在1、2中，说明是新元素，直接插入到 firstLruMap 的开始即可。
     * @param key
     */
    @Override
    public void updateKey(K key) {
        //1.元素已经在多次访问或者第一次访问lru中
        if (moreLruMap.contains(key) || firstLruMap.contains(key)) {
            //1.删除信息
            this.removeKey(key);
            //2.加入到多次lru中
            moreLruMap.updateKey(key);
            log.debug("key: {} 多次访问，加入到 moreLruMap 中", key);
        } else {
            //第一次访问，加入到firstmap中
            firstLruMap.updateKey(key);
            log.debug("key: {} 为第一次访问，加入到 firstLruMap 中", key);
        }
    }

    /**
     * 移除元素
     * 1.多次lru中存在，删除
     * 2.初次lru中存在，删除
     * @param key
     */
    @Override
    public void removeKey(K key) {
        //1. 多次LRU 删除逻辑
        if (moreLruMap.contains(key)) {
            moreLruMap.removeKey(key);
            log.debug("key: {} 从 moreLruMap 中移除", key);
        } else {
            firstLruMap.removeKey(key);
            log.debug("key: {} 从 firstLruMap 中移除", key);
        }
    }
}
