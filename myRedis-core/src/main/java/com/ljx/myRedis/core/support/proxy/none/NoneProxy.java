package com.ljx.myRedis.core.support.proxy.none;

import com.ljx.myRedis.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 无代理
 * 直接返回原对象
 */
public class NoneProxy implements InvocationHandler, ICacheProxy {
    /**
     * 被代理的对象
     */
    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy,args);
    }

    /**
     * 返回原来对象
     * @return original Object
     */
    @Override
    public Object proxy() {
        return this.target;
    }

}
