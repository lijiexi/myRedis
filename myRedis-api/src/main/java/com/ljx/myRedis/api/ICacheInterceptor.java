package com.ljx.myRedis.api;

/**
 * 拦截器接口
 * cost拦截器
 * 慢操作拦截器等
 */
public interface ICacheInterceptor<K,V> {
    /**
     * 方法执行之前进行的操作
     * @param context 上下文
     */
    void before(ICacheInterceptorContext<K,V> context);

    /**
     * 方法执行后的操作
     * @param context 上下文
     */
    void after(ICacheInterceptorContext<K,V> context);

}
