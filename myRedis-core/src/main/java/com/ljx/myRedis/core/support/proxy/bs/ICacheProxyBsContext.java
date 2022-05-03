package com.ljx.myRedis.core.support.proxy.bs;

import com.ljx.myRedis.annotation.CacheInterceptor;
import com.ljx.myRedis.api.ICache;

import java.lang.reflect.Method;

/**
 * 代理引导类上下文接口
 */
public interface ICacheProxyBsContext {
    /**
     * 拦截器信息
     * @return 拦截器
     */
    CacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     * @return 代理
     */
    ICache target();

    /**
     * 目标对象
     * @param target 对象
     * @return 结果
     */
    ICacheProxyBsContext target(final ICache target);

    /**
     * 参数信息
     * @return 参数信息
     */
    Object[] params();

    /**
     * 方法信息
     * @return 方法信息
     */
    Method method();

    /**
     * 方法执行
     * @return
     * @throws Throwable 异常
     */
    Object process() throws Throwable;
}
