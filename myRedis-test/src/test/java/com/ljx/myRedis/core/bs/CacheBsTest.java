package com.ljx.myRedis.core.bs;

import com.ljx.myRedis.api.ICache;
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
}
