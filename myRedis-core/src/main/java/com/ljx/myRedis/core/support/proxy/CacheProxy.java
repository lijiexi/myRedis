package com.ljx.myRedis.core.support.proxy;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.ljx.myRedis.api.ICache;
import com.ljx.myRedis.core.support.proxy.cglib.CglibProxy;
import com.ljx.myRedis.core.support.proxy.dynamic.DynamicProxy;
import com.ljx.myRedis.core.support.proxy.none.NoneProxy;

import java.lang.reflect.Proxy;

/**
 * cache缓存的代理
 */
public class CacheProxy {

    private CacheProxy() {}

    /**
     * 获取cache的代理对象
     * @param cache 对象代理
     * @param <K> key
     * @param <V> value
     * @return 代理信息
     */
    public static <K,V> ICache<K,V> getProxy (final ICache<K,V> cache) {
        //无代理
        if (ObjectUtil.isNull(cache)) {
            return (ICache<K, V>) new NoneProxy(cache).proxy();
        }
        final Class clazz = cache.getClass();
        // 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理。
        if(clazz.isInterface() || Proxy.isProxyClass(clazz)) {

            //return (ICache<K, V>) new DynamicProxy(cache).proxy();
            return null;
        }
        //TODO 否则使用cglib动态代理。
        return (ICache<K, V>) new CglibProxy(cache).proxy();
    }
}
