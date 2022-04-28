package com.ljx.myRedis.core.load;

import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheLoad;

/**
 * 初始化的时候，放入 2 个固定的信息。
 */
public class MyCacheLoad implements ICacheLoad<String,String> {

    @Override
    public void load(ICache<String, String> cache) {
        cache.put("1","1");
        cache.put("2","2");
    }
}
