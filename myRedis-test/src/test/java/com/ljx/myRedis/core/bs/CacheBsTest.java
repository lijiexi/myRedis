package com.ljx.myRedis.core.bs;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.core.Cache;
import com.ljx.myRedis.core.listener.MyRemoveListener;
import com.ljx.myRedis.core.listener.MySlowListener;
import com.ljx.myRedis.core.load.MyCacheLoad;
import com.ljx.myRedis.core.support.evict.CacheEvicts;
import com.ljx.myRedis.core.support.listener.remove.CacheRemoveListener;
import com.ljx.myRedis.core.support.listener.remove.CacheRemoveListeners;
import com.ljx.myRedis.core.support.listener.slow.CacheSlowListeners;
import com.ljx.myRedis.core.support.load.CacheLoads;
import com.ljx.myRedis.core.support.persist.CachePersists;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 测试引导类CacheBs
 */
public class CacheBsTest {
    /**
     * 测试新建缓存，并指定缓存大小
     */
    @Test
    public void helloTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance().size(3).build();
        cache.put("1","1");
        cache.put("2","2");
        cache.put("3","3");
        cache.put("4","4");
        cache.put("5","5");
        cache.put("6","6");
        System.out.println(cache.keySet());
    }
    /**
     * 过期测试
     */
    @Test
    public void expireTest () throws InterruptedException {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).build();
        cache.put("2","2");
        cache.put("3","3");
        cache.put("4","4");
        TimeUnit.MILLISECONDS.sleep(50);
        System.out.println(cache.keySet());
        cache.expire("2",40);
        Thread.sleep(50);
        System.out.println(cache.keySet());
        Assert.assertEquals(2, cache.size());
    }
    /**
     * load加载接口测试
     */
    @Test
    public void loadTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(2).load(new MyCacheLoad()).build();
        Assert.assertEquals(2, cache.size());
        System.out.println(cache.entrySet());
    }
    /**
     * rdb持久化测试
     */
    @Test
    public void persistRdb () throws InterruptedException {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .load(new MyCacheLoad())
                .persist(CachePersists.dbJson("test.rdb")).build();
        Assert.assertEquals(2, cache.size());
        TimeUnit.MINUTES.sleep(3);
    }
    /**
     * rdb加载测试
     */
    @Test
    public void loadRdb () throws InterruptedException {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).load(CacheLoads.dbJson("rdb.txt"))
                .persist(CachePersists.dbJson("rdb.txt"))
                .build();
        cache.put("5","5");
        cache.expire("5",1);
        cache.put("6","6");
        System.out.println(cache.entrySet());
        TimeUnit.MINUTES.sleep(3);
        System.out.println(cache.entrySet());
    }
    /**
     * 删除监听器测试
     */
    @Test
    public void removeListenersTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(2).load(CacheLoads.dbJson("rdb.txt"))
                .addRemoveListener(new MyRemoveListener<String,String>())
                .build();
        cache.put("1","1");
        cache.put("2","1");
        cache.put("3","1");
        cache.remove("1");
        System.out.println(cache.entrySet());
    }
    /**
     * 慢日志监听器测试
     */
    @Test
    public void slowListenerTest () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).addSlowListener(new MySlowListener())
                .addSlowListener(CacheSlowListeners.defaults()).build();
        cache.put("1","1");
        cache.get("1");
        cache.get("2");
        System.out.println(cache.entrySet());
    }
    /**
     * aof持久化测试
     */
    @Test
    public void aofTest () throws InterruptedException {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).persist(CachePersists.aof("aof.txt")).build();
        cache.put("1","1");
        cache.put("2","2");
        cache.put("3","3");
        cache.expire("3",300);
        TimeUnit.SECONDS.sleep(61);
    }
    /**
     * aof加载测试
     */
    @Test
    public void aofLoad () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).load(CacheLoads.aof("aof.txt")).build();
        System.out.println(cache.entrySet());
    }
    /**
     * LRU淘汰策略测试
     */
    @Test
    public void lruTest() {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(3).evict(CacheEvicts.lru()).build();
        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");
        cache.get("A");
        cache.put("e", "LRU");
        cache.put("f", "LRU");

        System.out.println(cache.entrySet());
    }

    /**
     * 测试基于双向链表+Map的Lru实现
     */
    @Test
    public void doubleListLru () {
        ICache<String,String> cache = CacheBs.<String,String>newInstance()
                .size(2).evict(CacheEvicts.lruDoubleListMap())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        // 访问一次A
        cache.get("A");
        cache.put("C", "FIFO");
        cache.put("D", "FIFO");
        cache.remove("A");
        cache.remove("C");
        System.out.println(cache.entrySet());
    }

    /**
     * 测试基于lruMap的lru-2算法实现
     */
    @Test
    public void lru2 () {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.lru2())
                .build();
        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");
        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");
        cache.put("E", "LRU");
        cache.put("F", "LRU");
        cache.put("G", "LRU");
        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }
}
