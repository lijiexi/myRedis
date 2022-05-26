package com.ljx.myRedis.core.support.interceptor.aof;

import com.alibaba.fastjson.JSON;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.api.ICacheInterceptor;
import com.ljx.myRedis.api.ICacheInterceptorContext;
import com.ljx.myRedis.api.ICachePersist;
import com.ljx.myRedis.core.model.PersistAofEntry;
import com.ljx.myRedis.core.support.persist.CachePersistAof;

/**
 * aof持久化拦截器
 * aof持久化到文件，暂时没有buffer性质
 */
public class CacheInterceptorAof<K,V> implements ICacheInterceptor<K,V> {
    private static final Log log = LogFactory.getLog(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        //持久化类
        ICache<K,V> cache = context.cache();
        ICachePersist<K,V> persist = cache.persist();
        if (persist instanceof CachePersistAof) {
            CachePersistAof<K,V> cachePersistAof = (CachePersistAof<K,V>) persist;
            String methodName = context.method().getName();
            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(context.params());

            String json = JSON.toJSONString(aofEntry);
            //进行持久化
            log.debug("AOF 开始追加文件内容：{}", json);
            cachePersistAof.append(json);
            //线上环境使用
            cachePersistAof.persist(cache);
            log.debug("AOF 完成追加文件内容：{}", json);
        }
    }
}
