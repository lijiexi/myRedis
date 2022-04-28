package com.ljx.myRedis.core.support.persist;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICachePersist;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内部缓存持久化类
 * 每10分钟进行一次rdb
 */
public class InnerCachePersist<K, V> {

    /**
     * 缓存信息
     */
    private final ICache<K,V> cache;

    /**
     * 持久化策略
     */
    private final ICachePersist<K, V> persist;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist (ICache<K, V> cache, ICachePersist<K,V> persist) {
        this.cache = cache;
        this.persist = persist;
        this.init();
    }
    /**
     * 初始化
     */
    private void init () {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                persist.persist(cache);
            }
        },0,1, TimeUnit.MINUTES);
    }
}
