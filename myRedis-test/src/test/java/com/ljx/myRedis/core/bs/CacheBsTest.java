package com.ljx.myRedis.core.bs;

import com.ljx.myRedis.api.ICache;
import org.junit.Assert;
import org.junit.Test;

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
}
