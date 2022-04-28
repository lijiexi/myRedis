package com.ljx.myRedis.core.bs;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEvict;
import com.ljx.myRedis.api.ICacheLoad;
import com.ljx.myRedis.api.ICachePersist;
import com.ljx.myRedis.core.Cache;
import com.ljx.myRedis.core.support.evict.CacheEvicts;
import com.ljx.myRedis.core.support.load.CacheLoads;
import com.ljx.myRedis.core.support.persist.CachePersists;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存引导类
 */
public class CacheBs<K, V> {
    private CacheBs () {}

    /**
     * 创建实例对象
     */
    public static <K,V> CacheBs<K,V> newInstance () {
        return new CacheBs<>();
    }

    /**
     * map实现
     */
    private Map<K, V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱逐策略
     */
    private ICacheEvict<K, V> evict = CacheEvicts.fifo();

    /**
     * 加载策略 默认none
     */
    private ICacheLoad<K,V> load = CacheLoads.none();

    /**
     * 持久化实现策略
     * 默认为none
     */
    private ICachePersist<K, V> persist = CachePersists.none();
    /**
     * map实现
     */
    public CacheBs<K, V> map (Map<K, V> map) {
        //TODO ArgUtil.notNull(map, "map");
        this.map = map;
        return this;
    }

    /**
     * 设置size大小
     */
    public CacheBs<K, V> size (int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置驱逐策略
     */
    public CacheBs<K, V> evict (ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    /**
     * 设置加载策略
     * @param load 加载方式
     * @return this
     */
    public CacheBs<K, V> load (ICacheLoad<K, V> load) {
        this.load = load;
        return this;
    }
    /**
     * 设置持久化策略
     */
    public CacheBs<K,V> persist (ICachePersist<K,V> persist) {
        this.persist = persist;
        return this;
    }
    /**
     * 缓存初始化
     */
    public ICache<K, V> build () {
        Cache<K, V> cache = new Cache<>();
        cache.map(map);
        cache.evict(evict);
        cache.sizeLimit(size);
        cache.load(load);
        cache.persist(persist);
        //调用cache初始化，删除策略等
        cache.init();
        return cache;

    }
}
