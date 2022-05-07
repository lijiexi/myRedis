package com.ljx.myRedis.core.support.interceptor.evict;

import com.ljx.myRedis.api.ICacheEvict;
import com.ljx.myRedis.api.ICacheInterceptor;
import com.ljx.myRedis.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

/**
 * 驱逐策略拦截器
 */
public class CacheInterceptorEvict<K,V> implements ICacheInterceptor<K,V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        //得到驱逐策略
        ICacheEvict<K,V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if ("remove".equals(method.getName())) {
            //remove方法，在LinkedList里删除该元素
            evict.removeKey(key);
        } else {
            //其他方法，containsKey,get,put则在LinkedList里更新k
            evict.updateKey(key);
        }


    }
}
