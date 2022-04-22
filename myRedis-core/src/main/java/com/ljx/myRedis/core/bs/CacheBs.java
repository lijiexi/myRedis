package com.ljx.myRedis.core.bs;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheEvict;
import com.ljx.myRedis.core.Cache;
import com.ljx.myRedis.core.support.evict.CacheEvicts;

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
     * 缓存初始化
     */
    public ICache<K, V> build () {
        Cache<K, V> cache = new Cache<>();
        cache.map(map);
        cache.evict(evict);
        cache.sizeLimit(size);
        return cache;
        //TODO cache.init()
    }
}
