package com.ljx.myRedis.api;

import java.lang.reflect.Method;

/**
 * 拦截器上下文接口
 * 1.get
 * 2.put
 * 3.remove
 * 4.expire
 * 5.evict
 */
public interface ICacheInterceptorContext<K,V> {
    /**
     * 缓存信息
     */
    ICache<K,V> cache();

    /**
     * 执行等方法信息
     */
    Method method();

    /**
     * 执行参数
     */
    Object[] params();

    /**
     * 执行后等结果
     */
    Object result();
    /**
     * 开始时间
     * @return 时间
     */
    long startMills();

    /**
     * 结束时间
     * @return 时间
     */
    long endMills();
}
