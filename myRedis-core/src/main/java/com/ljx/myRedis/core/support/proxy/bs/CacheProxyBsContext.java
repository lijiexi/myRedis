package com.ljx.myRedis.core.support.proxy.bs;

import com.ljx.myRedis.annotation.CacheInterceptor;
import com.ljx.myRedis.api.ICache;

import java.lang.reflect.Method;

/**
 * 代理引导类上下文
 */
public class CacheProxyBsContext implements ICacheProxyBsContext {
    /**
     * 目标对象
     */
    private ICache target;
    /**
     * 入参
     */
    private Object[] params;
    /**
     * 方法
     */
    private Method method;
    /**
     * 拦截器
     */
    private CacheInterceptor interceptor;

    /**
     * 新建对象
     * @return 对象
     */
    public static CacheProxyBsContext newInstance() {
        return new CacheProxyBsContext();
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public ICacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }
    public CacheProxyBsContext method(Method method) {
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target,params);
    }
}
